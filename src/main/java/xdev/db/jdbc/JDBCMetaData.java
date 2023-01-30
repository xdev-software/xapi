/*
 * XDEV Application Framework - XDEV Application Framework
 * Copyright © 2003 XDEV Software (https://xdev.software)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package xdev.db.jdbc;


import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import com.xdev.jadoth.sqlengine.interfaces.ConnectionProvider;

import xdev.db.AbstractDBMetaData;
import xdev.db.ColumnMetaData;
import xdev.db.DBConnection.Query;
import xdev.db.DBException;
import xdev.db.DataType;
import xdev.db.Index;
import xdev.db.Index.IndexType;
import xdev.db.Result;
import xdev.db.StoredProcedure;
import xdev.db.StoredProcedure.Param;
import xdev.db.StoredProcedure.ParamType;
import xdev.db.StoredProcedure.ReturnTypeFlavor;
import xdev.db.sql.Functions;
import xdev.db.sql.SELECT;
import xdev.db.sql.Table;
import xdev.util.ProgressMonitor;
import xdev.util.logging.LoggerFactory;
import xdev.util.logging.XdevLogger;
import xdev.vt.Cardinality;
import xdev.vt.EntityRelationship;
import xdev.vt.EntityRelationship.Entity;
import xdev.vt.EntityRelationshipModel;


public abstract class JDBCMetaData extends AbstractDBMetaData
{
	private static final XdevLogger LOG = LoggerFactory.getLogger(JDBCMetaData.class);
	
	protected final transient JDBCDataSource	dataSource;
	protected final String						name;
	protected final String						version;
	protected final boolean						isCaseSensitive;
	protected final int							maxColumnNameLength;
	
	
	public JDBCMetaData(JDBCDataSource dataSource) throws DBException
	{
		this.dataSource = dataSource;
		
		try
		{
			Connection connection = dataSource.connectImpl();
			
			try
			{
				DatabaseMetaData meta = connection.getMetaData();
				this.name = meta.getDatabaseProductName();
				this.version = meta.getDatabaseProductVersion();
				this.isCaseSensitive = meta.supportsMixedCaseIdentifiers()
						&& meta.supportsMixedCaseQuotedIdentifiers();
				this.maxColumnNameLength = meta.getMaxColumnNameLength();
				
				// List<String> functions = new ArrayList();
				// functions.addAll(StringUtils.explode(meta.getNumericFunctions(),","));
				// functions.addAll(StringUtils.explode(meta.getStringFunctions(),","));
				// functions.addAll(StringUtils.explode(meta.getSystemFunctions(),","));
				// functions.addAll(StringUtils.explode(meta.getTimeDateFunctions(),","));
				// Collections.sort(functions);
				// System.out.println(functions);
			}
			finally
			{
				connection.close();
			}
		}
		catch(SQLException e)
		{
			throw new DBException(dataSource,e);
		}
	}
	
	
	@Override
	public String getName()
	{
		return name;
	}
	
	
	@Override
	public String getVersion()
	{
		return version;
	}
	
	
	@Override
	public boolean isCaseSensitive() throws DBException
	{
		return isCaseSensitive;
	}
	
	
	@Override
	public int getMaxColumnNameLength() throws DBException
	{
		return maxColumnNameLength;
	}
	
	
	/**
	 * The default implementation doesn't escape the name.
	 * 
	 * @param name
	 * @param sb
	 */
	
	protected void appendEscapedName(String name, StringBuilder sb)
	{
		sb.append(name);
	}
	
	
	protected String getCatalog(JDBCDataSource dataSource)
	{
		String catalog = dataSource.getCatalog();
		if(catalog != null && catalog.length() == 0)
		{
			catalog = null;
		}
		return catalog;
	}
	
	
	protected String getSchema(JDBCDataSource dataSource)
	{
		String schema = dataSource.getSchema();
		if("".equals(schema))
		{
			schema = null;
		}
		return schema;
	}
	
	
	@Override
	public TableInfo[] getTableInfos(ProgressMonitor monitor, EnumSet<TableType> types)
			throws DBException
	{
		monitor.beginTask("",ProgressMonitor.UNKNOWN);
		
		List<TableInfo> list = new ArrayList();
		
		try
		{
			JDBCConnection jdbcConnection = (JDBCConnection)dataSource.openConnection();
			Connection connection = jdbcConnection.getConnection();
			
			try
			{
				DatabaseMetaData meta = connection.getMetaData();
				String catalog = getCatalog(dataSource);
				String schema = getSchema(dataSource);
				
				String[] castTypes = castEnumSetToStringArray(types);
				ResultSet rs = meta.getTables(catalog,schema,null,castTypes);
				while(rs.next() && !monitor.isCanceled())
				{
					String tableTypeName = rs.getString("TABLE_TYPE");
					TableType type;
					if(tableTypeName != null
							&& tableTypeName.equalsIgnoreCase(TableType.TABLE.name()))
					{
						type = TableType.TABLE;
					}
					else if(tableTypeName != null
							&& tableTypeName.equalsIgnoreCase(TableType.VIEW.name()))
					{
						type = TableType.VIEW;
					}
					else
					{
						type = TableType.OTHER;
					}
					
					if(types.contains(type))
					{
						list.add(new TableInfo(type,rs.getString("TABLE_SCHEM"),rs
								.getString("TABLE_NAME")));
					}
				}
				rs.close();
			}
			finally
			{
				jdbcConnection.close();
			}
		}
		catch(SQLException e)
		{
			throw new DBException(dataSource,e);
		}
		
		monitor.done();
		
		TableInfo[] tables = list.toArray(new TableInfo[list.size()]);
		Arrays.sort(tables);
		return tables;
	}
	
	
	protected String[] castEnumSetToStringArray(EnumSet<TableType> types)
	{
		
		String result[] = new String[types.size()];
		int i = 0;
		
		for(TableType tableType : types)
		{
			result[i] = tableType.toString();
			i++;
		}
		return result;
	}
	
	
	
	protected class IndexInfo
	{
		public final String		name;
		public final IndexType	type;
		
		
		public IndexInfo(String name, IndexType type)
		{
			this.name = name;
			this.type = type;
		}
		
		
		@Override
		public int hashCode()
		{
			return name.hashCode();
		}
		
		
		@Override
		public boolean equals(Object obj)
		{
			if(obj == this)
			{
				return true;
			}
			
			if(obj instanceof IndexInfo)
			{
				IndexInfo ii = (IndexInfo)obj;
				return ii.name.equals(name) && ii.type == type;
			}
			
			return false;
		}
	}
	
	
	@Override
	public TableMetaData[] getTableMetaData(ProgressMonitor monitor, int flags, TableInfo... tables)
			throws DBException
	{
		monitor.beginTask("",tables.length);
		
		if(tables == null || tables.length == 0)
		{
			return new TableMetaData[0];
		}
		
		List<TableMetaData> list = new ArrayList(tables.length);
		
		try
		{
			JDBCConnection jdbcConnection = (JDBCConnection)dataSource.openConnection();
			
			try
			{
				DatabaseMetaData meta = jdbcConnection.getConnection().getMetaData();
				int done = 0;
				
				for(TableInfo table : tables)
				{
					if(monitor.isCanceled())
					{
						break;
					}
					
					monitor.setTaskName(table.getName());
					try
					{
						list.add(getTableMetaData(jdbcConnection,meta,flags,table));
					}
					catch(Exception e)
					{
						LOG.error(e);
					}
					monitor.worked(++done);
				}
			}
			finally
			{
				jdbcConnection.close();
			}
		}
		catch(SQLException e)
		{
			throw new DBException(dataSource,e);
		}
		
		monitor.done();
		
		return list.toArray(new TableMetaData[list.size()]);
	}
	
	
	protected TableMetaData getTableMetaData(JDBCConnection jdbcConnection, DatabaseMetaData meta,
			int flags, TableInfo table) throws DBException, SQLException
	{
		String catalog = getCatalog(dataSource);
		String schema = getSchema(dataSource);
		
		String tableName = table.getName();
		Table tableIdentity = new Table(tableName,"META_DUMMY");
		
		Map<String, Object> defaultValues = new HashMap();
		ResultSet rs = meta.getColumns(catalog,schema,tableName,null);
		while(rs.next())
		{
			String columnName = rs.getString("COLUMN_NAME");
			Object defaultValue = rs.getObject("COLUMN_DEF");
			defaultValues.put(columnName,defaultValue);
		}
		rs.close();
		
		SELECT select = new SELECT().FROM(tableIdentity).WHERE("1 = 0");
		Result result = jdbcConnection.query(select);
		int cc = result.getColumnCount();
		ColumnMetaData[] columns = new ColumnMetaData[cc];
		for(int i = 0; i < cc; i++)
		{
			ColumnMetaData column = result.getMetadata(i);
			
			Object defaultValue = column.getDefaultValue();
			if(defaultValue == null && defaultValues.containsKey(column.getName()))
			{
				defaultValue = defaultValues.get(column.getName());
			}
			defaultValue = checkDefaultValue(defaultValue,column);
			
			columns[i] = new ColumnMetaData(tableName,column.getName(),column.getCaption(),
					column.getType(),column.getLength(),column.getScale(),defaultValue,
					column.isNullable(),column.isAutoIncrement());
		}
		result.close();
		
		Map<IndexInfo, Set<String>> indexMap = new LinkedHashMap();
		int count = UNKNOWN_ROW_COUNT;
		
		if(table.getType() == TableType.TABLE)
		{
			Set<String> primaryKeyColumns = new HashSet();
			rs = meta.getPrimaryKeys(catalog,schema,tableName);
			while(rs.next())
			{
				primaryKeyColumns.add(rs.getString("COLUMN_NAME"));
			}
			rs.close();
			
			if((flags & INDICES) != 0)
			{
				if(primaryKeyColumns.size() > 0)
				{
					indexMap.put(new IndexInfo("PRIMARY_KEY",IndexType.PRIMARY_KEY),
							primaryKeyColumns);
				}
				
				rs = meta.getIndexInfo(catalog,schema,tableName,false,true);
				while(rs.next())
				{
					String indexName = rs.getString("INDEX_NAME");
					String columnName = rs.getString("COLUMN_NAME");
					if(indexName != null && columnName != null
							&& !primaryKeyColumns.contains(columnName))
					{
						boolean unique = !rs.getBoolean("NON_UNIQUE");
						IndexInfo info = new IndexInfo(indexName,unique ? IndexType.UNIQUE
								: IndexType.NORMAL);
						Set<String> columnNames = indexMap.get(info);
						if(columnNames == null)
						{
							columnNames = new HashSet();
							indexMap.put(info,columnNames);
						}
						columnNames.add(columnName);
					}
				}
				rs.close();
			}
			
			if((flags & ROW_COUNT) != 0)
			{
				try
				{
					result = jdbcConnection.query(new SELECT().columns(Functions.COUNT()).FROM(
							tableIdentity));
					if(result.next())
					{
						count = result.getInt(0);
					}
					result.close();
				}
				catch(Exception e)
				{
					// Happens
				}
			}
		}
		
		Index[] indices = new Index[indexMap.size()];
		int i = 0;
		for(IndexInfo indexInfo : indexMap.keySet())
		{
			Set<String> columnList = indexMap.get(indexInfo);
			String[] indexColumns = columnList.toArray(new String[columnList.size()]);
			indices[i++] = new Index(indexInfo.name,indexInfo.type,indexColumns);
		}
		
		return new TableMetaData(table,columns,indices,count);
	}
	
	
	protected Object checkDefaultValue(Object defaultValue, ColumnMetaData column)
	{
		if(defaultValue != null)
		{
			DataType type = column.getType();
			if(type.isInt())
			{
				if(!(defaultValue instanceof Long))
				{
					if(defaultValue instanceof Number)
					{
						defaultValue = ((Number)defaultValue).longValue();
					}
					else
					{
						try
						{
							defaultValue = Long.parseLong(defaultValue.toString());
						}
						catch(NumberFormatException e)
						{
						}
					}
				}
			}
			else if(type.isDecimal())
			{
				if(!(defaultValue instanceof Double))
				{
					if(defaultValue instanceof Number)
					{
						defaultValue = ((Number)defaultValue).doubleValue();
					}
					else
					{
						try
						{
							defaultValue = Double.parseDouble(defaultValue.toString());
						}
						catch(NumberFormatException e)
						{
						}
					}
				}
			}
			else if(type.isDate())
			{
				if(!(defaultValue instanceof Date))
				{
					defaultValue = convertToDate(defaultValue.toString());
				}
			}
		}
		
		return defaultValue;
	}
	
	private static SimpleDateFormat	DATETIME_FORMAT	= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
															Locale.ENGLISH);
	private static SimpleDateFormat	DATE_FORMAT		= new SimpleDateFormat("yyyy-MM-dd",
															Locale.ENGLISH);
	private static SimpleDateFormat	TIME_FORMAT		= new SimpleDateFormat("HH:mm:ss",
															Locale.ENGLISH);
	
	
	protected Date convertToDate(String str)
	{
		try
		{
			return java.sql.Timestamp.valueOf(str);
		}
		catch(IllegalArgumentException noSqlTimestamp)
		{
			try
			{
				return java.sql.Date.valueOf(str);
			}
			catch(IllegalArgumentException noSqlDate)
			{
				try
				{
					return java.sql.Time.valueOf(str);
				}
				catch(IllegalArgumentException noSqlTime)
				{
					try
					{
						return DATETIME_FORMAT.parse(str);
					}
					catch(ParseException noDateTime)
					{
						try
						{
							return DATE_FORMAT.parse(str);
						}
						catch(ParseException noDate)
						{
							try
							{
								return TIME_FORMAT.parse(str);
							}
							catch(ParseException noTime)
							{
								Set<Locale> locales = new HashSet();
								locales.add(Locale.getDefault());
								locales.addAll(Arrays.asList(Locale.getAvailableLocales()));
								int[] types = {DateFormat.SHORT,DateFormat.MEDIUM,DateFormat.LONG,
										DateFormat.FULL};
								for(Locale locale : locales)
								{
									for(int dateStyle : types)
									{
										for(int timeStyle : types)
										{
											try
											{
												return DateFormat.getDateTimeInstance(dateStyle,
														timeStyle,locale).parse(str);
											}
											catch(ParseException e)
											{
											}
										}
									}
									for(int dateStyle : types)
									{
										try
										{
											return DateFormat.getDateInstance(dateStyle,locale)
													.parse(str);
										}
										catch(ParseException e)
										{
										}
									}
									for(int timeStyle : types)
									{
										try
										{
											return DateFormat.getTimeInstance(timeStyle,locale)
													.parse(str);
										}
										catch(ParseException e)
										{
										}
									}
								}
							}
						}
					}
				}
			}
		}
		
		return null;
	}
	
	
	@Override
	public StoredProcedure[] getStoredProcedures(ProgressMonitor monitor) throws DBException
	{
		monitor.beginTask("",ProgressMonitor.UNKNOWN);
		
		List<StoredProcedure> list = new ArrayList();
		
		try
		{
			ConnectionProvider connectionProvider = dataSource.getConnectionProvider();
			Connection connection = connectionProvider.getConnection();
			
			try
			{
				DatabaseMetaData meta = connection.getMetaData();
				String catalog = getCatalog(dataSource);
				String schema = getSchema(dataSource);
				
				ResultSet rs = meta.getProcedures(catalog,schema,null);
				while(rs.next() && !monitor.isCanceled())
				{
					String name = rs.getString("PROCEDURE_NAME");
					String description = rs.getString("REMARKS");
					
					ReturnTypeFlavor returnTypeFlavor;
					DataType returnType = null;
					int procedureType = rs.getInt("PROCEDURE_TYPE");
					switch(procedureType)
					{
						case DatabaseMetaData.procedureNoResult:
							returnTypeFlavor = ReturnTypeFlavor.VOID;
						break;
						
						default:
							returnTypeFlavor = ReturnTypeFlavor.UNKNOWN;
					}
					
					List<Param> params = new ArrayList();
					ResultSet rsp = meta.getProcedureColumns(catalog,schema,name,null);
					while(rsp.next())
					{
						DataType dataType = DataType.get(rsp.getInt("DATA_TYPE"));
						String columnName = rsp.getString("COLUMN_NAME");
						switch(rsp.getInt("COLUMN_TYPE"))
						{
							case DatabaseMetaData.procedureColumnReturn:
								returnTypeFlavor = ReturnTypeFlavor.TYPE;
								returnType = dataType;
							break;
							
							case DatabaseMetaData.procedureColumnResult:
								returnTypeFlavor = ReturnTypeFlavor.RESULT_SET;
							break;
							
							case DatabaseMetaData.procedureColumnIn:
								params.add(new Param(ParamType.IN,columnName,dataType));
							break;
							
							case DatabaseMetaData.procedureColumnOut:
								params.add(new Param(ParamType.OUT,columnName,dataType));
							break;
							
							case DatabaseMetaData.procedureColumnInOut:
								params.add(new Param(ParamType.IN_OUT,columnName,dataType));
							break;
						}
					}
					rsp.close();
					
					list.add(new StoredProcedure(returnTypeFlavor,returnType,name,description,
							params.toArray(new Param[params.size()])));
				}
				rs.close();
			}
			finally
			{
				connection.close();
			}
		}
		catch(SQLException e)
		{
			throw new DBException(dataSource,e);
		}
		
		monitor.done();
		
		return list.toArray(new StoredProcedure[list.size()]);
	}
	
	
	@Override
	public EntityRelationshipModel getEntityRelationshipModel(ProgressMonitor monitor,
			TableInfo... tableInfos) throws DBException
	{
		monitor.beginTask("",tableInfos.length);
		
		EntityRelationshipModel model = new EntityRelationshipModel();
		
		try
		{
			List<String> tables = new ArrayList();
			for(TableInfo table : tableInfos)
			{
				if(table.getType() == TableType.TABLE)
				{
					tables.add(table.getName());
				}
			}
			Collections.sort(tables);
			
			ConnectionProvider connectionProvider = dataSource.getConnectionProvider();
			Connection connection = connectionProvider.getConnection();
			
			try
			{
				DatabaseMetaData meta = connection.getMetaData();
				String catalog = getCatalog(dataSource);
				String schema = getSchema(dataSource);
				int done = 0;
				
				for(String table : tables)
				{
					if(monitor.isCanceled())
					{
						break;
					}
					
					monitor.setTaskName(table);
					
					ResultSet rs = meta.getExportedKeys(catalog,schema,table);
					try
					{
						String pkTable = null;
						String fkTable = null;
						List<String> pkColumns = new ArrayList();
						List<String> fkColumns = new ArrayList();
						
						while(rs.next())
						{
							short keySeq = rs.getShort("KEY_SEQ");
							
							if(keySeq == 1 && pkColumns.size() > 0)
							{
								if(tables.contains(pkTable) && tables.contains(fkTable))
								{
									model.add(new EntityRelationship(
											new Entity(pkTable,pkColumns
													.toArray(new String[pkColumns.size()]),
													Cardinality.ONE),new Entity(fkTable,fkColumns
													.toArray(new String[fkColumns.size()]),
													Cardinality.MANY)));
									pkColumns.clear();
									fkColumns.clear();
								}
							}
							
							pkTable = rs.getString("PKTABLE_NAME");
							fkTable = rs.getString("FKTABLE_NAME");
							
							pkColumns.add(rs.getString("PKCOLUMN_NAME"));
							fkColumns.add(rs.getString("FKCOLUMN_NAME"));
						}
						
						if(pkColumns.size() > 0)
						{
							if(tables.contains(pkTable) && tables.contains(fkTable))
							{
								model.add(new EntityRelationship(new Entity(pkTable,pkColumns
										.toArray(new String[pkColumns.size()]),Cardinality.ONE),
										new Entity(fkTable,fkColumns.toArray(new String[fkColumns
												.size()]),Cardinality.MANY)));
								pkColumns.clear();
								fkColumns.clear();
							}
						}
					}
					finally
					{
						rs.close();
					}
					
					monitor.worked(++done);
				}
			}
			finally
			{
				connection.close();
			}
		}
		catch(SQLException e)
		{
			throw new DBException(dataSource,e);
		}
		
		monitor.done();
		
		return model;
	}
	
	
	@Override
	public Query[] synchronizeTables(ProgressMonitor monitor, TableChange... changes)
			throws DBException
	{
		if(!dataSource.canExport())
		{
			throw new UnsupportedOperationException();
		}
		
		monitor.beginTask("",changes.length);
		
		JDBCConnection jdbcConnection = (JDBCConnection)dataSource.openConnection();
		jdbcConnection.setStoreQueries(true);
		
		try
		{
			int done = 0;
			
			for(TableChange change : changes)
			{
				if(monitor.isCanceled())
				{
					break;
				}
				
				switch(change.getType())
				{
					case CREATE:
					{
						monitor.setTaskName(change.getNewTable().getTableInfo().getName());
						createTable(jdbcConnection,change.getNewTable());
					}
					break;
					
					case ALTER:
					{
						monitor.setTaskName(change.getOldTable().getTableInfo().getName());
						alterTable(jdbcConnection,change.getNewTable(),change.getOldTable());
					}
					break;
				}
				
				monitor.worked(++done);
			}
		}
		catch(SQLException e)
		{
			throw new DBException(dataSource,e);
		}
		finally
		{
			jdbcConnection.close();
		}
		
		monitor.done();
		
		return jdbcConnection.getStoredQueries();
	}
	
	
	/**
	 * Creates a new table in the database.<br>
	 * Implementors also have to care about the indices.
	 * 
	 * @param jdbcConnection
	 * @param table
	 * @throws DBException
	 * @throws SQLException
	 */
	
	protected abstract void createTable(JDBCConnection jdbcConnection, TableMetaData table)
			throws DBException, SQLException;
	
	
	protected void alterTable(JDBCConnection jdbcConnection, TableMetaData table,
			TableMetaData existing) throws DBException, SQLException
	{
		// Columns
		
		Map<String, ColumnMetaData> existingColumns = new Hashtable();
		for(ColumnMetaData column : existing.getColumns())
		{
			existingColumns.put(getColumnNameAsKey(column),column);
		}
		
		ColumnMetaData[] columns = table.getColumns();
		for(int i = 0; i < columns.length; i++)
		{
			ColumnMetaData column = columns[i];
			ColumnMetaData existingColumn = existingColumns.remove(getColumnNameAsKey(column));
			
			if(existingColumn == null)
			{
				addColumn(jdbcConnection,table,column,i == 0 ? null : columns[i - 1],
						i < columns.length - 1 ? columns[i + 1] : null);
			}
			else if(!isColumnEqual(table,column,existing,existingColumn))
			{
				alterColumn(jdbcConnection,table,column,existingColumn);
			}
		}
		
		for(ColumnMetaData column : existingColumns.values())
		{
			dropColumn(jdbcConnection,table,column);
		}
		
		// Indices
		
		Index existingPrimaryKey = null;
		Map<String, Index> existingIndices = new Hashtable();
		for(Index index : existing.getIndices())
		{
			if(index.getType() == IndexType.PRIMARY_KEY)
			{
				existingPrimaryKey = index;
			}
			else
			{
				existingIndices.put(index.getName(),index);
			}
		}
		
		for(Index index : table.getIndices())
		{
			Index existingIndex;
			if(index.getType() == IndexType.PRIMARY_KEY)
			{
				existingIndex = existingPrimaryKey;
			}
			else
			{
				existingIndex = existingIndices.remove(index.getName());
			}
			
			if(existingIndex == null)
			{
				createIndex(jdbcConnection,table,index);
			}
			else if(!index.equals(existingIndex))
			{
				dropIndex(jdbcConnection,table,existingIndex);
				createIndex(jdbcConnection,table,index);
			}
		}
		
		for(Index index : existingIndices.values())
		{
			dropIndex(jdbcConnection,table,index);
		}
	}
	
	
	private String getColumnNameAsKey(ColumnMetaData col) throws DBException
	{
		String name = col.getName();
		if(!isCaseSensitive())
		{
			name = name.toUpperCase();
		}
		return name;
	}
	
	
	protected abstract void addColumn(JDBCConnection jdbcConnection, TableMetaData table,
			ColumnMetaData column, ColumnMetaData columnBefore, ColumnMetaData columnAfter)
			throws DBException, SQLException;
	
	
	protected abstract void alterColumn(JDBCConnection jdbcConnection, TableMetaData table,
			ColumnMetaData column, ColumnMetaData existing) throws DBException, SQLException;
	
	
	protected abstract void dropColumn(JDBCConnection jdbcConnection, TableMetaData table,
			ColumnMetaData column) throws DBException, SQLException;
	
	
	protected abstract void createIndex(JDBCConnection jdbcConnection, TableMetaData table,
			Index index) throws DBException, SQLException;
	
	
	protected abstract void dropIndex(JDBCConnection jdbcConnection, TableMetaData table,
			Index index) throws DBException, SQLException;
}
