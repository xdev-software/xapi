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
package xdev.db;


import java.io.Serializable;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

import xdev.db.DBConnection.Query;
import xdev.db.Index.IndexType;
import xdev.lang.Copyable;
import xdev.util.DataSourceMetaData;
import xdev.util.MathUtils;
import xdev.util.ObjectUtils;
import xdev.util.ProgressMonitor;
import xdev.vt.EntityRelationshipModel;


/**
 * This class provides methods to read or change the meta data from the
 * database.
 * 
 * 
 * @author XDEV Software
 * 
 */
public interface DBMetaData extends DataSourceMetaData, Serializable
{
	public final static int	INDICES		= 1;
	public final static int	ROW_COUNT	= 2;
	
	public final static int	ALL			= INDICES | ROW_COUNT;
	
	
	/**
	 * @return <code>true</code> if identifiers like table or columns names are
	 *         case sensitive, <code>false</code> otherwise
	 * 
	 * @throws DBException
	 */
	public boolean isCaseSensitive() throws DBException;
	
	
	/**
	 * Retrieves the maximum number of characters this database allows for a
	 * column name.
	 *
	 * @return the maximum number of characters allowed for a column name; a
	 *         result of zero means that there is no limit or the limit is not
	 *         known
	 * @throws DBException
	 * @since 4.1
	 */
	public int getMaxColumnNameLength() throws DBException;
	
	
	
	/**
	 * The table type of a database table.
	 * 
	 * @author XDEV Software
	 * 
	 */
	public static enum TableType
	{
		TABLE,
		
		VIEW,
		
		/**
		 * @since 4.0
		 */
		SYNONYM,
		
		OTHER;
		
		// SYSTEM_TABLE,
		//
		// GLOBAL_TEMPORARY,
		//
		// LOCAL_TEMPORARY,
		//
		// ALIAS,
		
		public final static EnumSet<TableType>	TABLES_AND_VIEWS			= EnumSet
																					.of(TABLE,VIEW);
		/**
		 * @since 4.0
		 */
		public final static EnumSet<TableType>	TABLES_VIEWS_AND_SYNONYMS	= EnumSet.of(TABLE,
																					VIEW,SYNONYM);
		
	}
	
	public final static int	UNKNOWN_ROW_COUNT	= -1;
	
	
	
	/**
	 * This class includes limited information about the database table.
	 * <p>
	 * The class has properties like type, name.
	 * </p>
	 * 
	 * @author XDEV Software
	 * 
	 */
	public static class TableInfo implements Comparable<TableInfo>, Copyable<TableInfo>,
			Serializable
	{
		private static final long	serialVersionUID	= -5338200279546453498L;
		
		private final TableType		type;
		private final String		schema;
		private final String		name;
		private Map<Object, Object>	clientProperties;
		private final int			hash;
		
		
		/**
		 * Constructor for creating a new instance of a {@link TableInfo}.
		 * 
		 * @param type
		 *            the {@link TableType} of this {@link TableInfo}
		 * 
		 * @param name
		 *            the table name
		 */
		public TableInfo(TableType type, String schema, String name)
		{
			this.type = type;
			this.schema = "".equals(schema) ? null : schema;
			this.name = name;
			
			this.hash = MathUtils.computeHash(type,schema,name);
		}
		
		
		/**
		 * Returns the table type of this {@link TableInfo}.
		 * 
		 * @return the type of the table.
		 */
		public TableType getType()
		{
			return type;
		}
		
		
		/**
		 * Returns the schema of this {@link TableInfo}, may be
		 * <code>null</code>.
		 * 
		 * @return the schema of the table.
		 */
		public String getSchema()
		{
			return schema;
		}
		
		
		/**
		 * Returns the table name of this {@link TableInfo}.
		 * 
		 * @return the name of the table.
		 */
		public String getName()
		{
			return name;
		}
		
		
		public void putClientProperty(Object key, Object value)
		{
			if(value == null && clientProperties == null)
			{
				return;
			}
			
			if(clientProperties == null)
			{
				clientProperties = new HashMap();
			}
			
			if(value == null)
			{
				clientProperties.remove(key);
			}
			else
			{
				clientProperties.put(key,value);
			}
		}
		
		
		public Object getClientProperty(Object key)
		{
			if(clientProperties == null)
			{
				return null;
			}
			else
			{
				return clientProperties.get(key);
			}
		}
		
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		public String toString()
		{
			if(schema != null)
			{
				return schema + "." + name;
			}
			
			return name;
		}
		
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		public int compareTo(TableInfo other)
		{
			int diff = type.compareTo(other.type);
			if(diff != 0)
			{
				return diff;
			}
			
			return name.compareTo(other.name);
		}
		
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		public int hashCode()
		{
			return hash;
		}
		
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		public boolean equals(Object obj)
		{
			if(obj == this)
			{
				return true;
			}
			
			if(obj instanceof TableInfo)
			{
				TableInfo other = (TableInfo)obj;
				return other.hash == hash;
			}
			
			return false;
		}
		
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		public TableInfo clone()
		{
			TableInfo clone = new TableInfo(type,schema,name);
			if(clientProperties != null)
			{
				clone.clientProperties = new HashMap(clientProperties);
			}
			return clone;
		}
	}
	
	
	/**
	 * Create the table infos for this {@link DBMetaData}. Read only the table
	 * name and data types.
	 * 
	 * @return the table infos for this {@link DBMetaData}
	 * 
	 * @throws DBException
	 *             if a database access error occurs
	 */
	public TableInfo[] getTableInfos(ProgressMonitor monitor, EnumSet<TableType> types)
			throws DBException;
	
	
	
	/**
	 * This class includes information about the database table, the columns of
	 * the database table and the indices.
	 * <p>
	 * The class has properties like tableInfo, columns, indices and rowCount .
	 * </p>
	 * 
	 * @author XDEV Software
	 * 
	 */
	public static class TableMetaData implements Copyable<TableMetaData>, Serializable
	{
		private static final long		serialVersionUID	= -4891170947258386154L;
		
		private final TableInfo			tableInfo;
		private final ColumnMetaData[]	columns;
		private final Index[]			indices;
		private final int				rowCount;
		
		
		/**
		 * Constructor for creating a new instance of a {@link TableMetaData}.
		 * 
		 * @param tableInfo
		 *            the information of the table
		 * 
		 * @param columns
		 *            the column information of the table
		 * 
		 * @param indices
		 *            the indices
		 */
		public TableMetaData(TableInfo tableInfo, ColumnMetaData[] columns, Index[] indices)
		{
			this(tableInfo,columns,indices,UNKNOWN_ROW_COUNT);
		}
		
		
		/**
		 * Constructor for creating a new instance of a {@link TableMetaData}.
		 * 
		 * @param tableInfo
		 *            the information of the table
		 * 
		 * @param columns
		 *            the column information of the table
		 * 
		 * @param indices
		 *            the indices
		 * 
		 * @param rowCount
		 *            the row count
		 */
		public TableMetaData(TableInfo tableInfo, ColumnMetaData[] columns, Index[] indices,
				int rowCount)
		{
			this.tableInfo = tableInfo;
			this.columns = columns;
			this.indices = indices;
			this.rowCount = rowCount;
		}
		
		
		/**
		 * Returns the {@link TableInfo} of this {@link TableMetaData}.
		 * 
		 * @return the {@link TableInfo} of this {@link TableMetaData}
		 */
		public TableInfo getTableInfo()
		{
			return tableInfo;
		}
		
		
		/**
		 * Returns the {@link ColumnMetaData} of this {@link TableMetaData}.
		 * 
		 * @return an array of {@link ColumnMetaData}
		 */
		public ColumnMetaData[] getColumns()
		{
			return columns;
		}
		
		
		/**
		 * Returns the {@link Index} of this {@link TableMetaData}.
		 * 
		 * @return an array of {@link Index}
		 */
		public Index[] getIndices()
		{
			return indices;
		}
		
		
		/**
		 * Returns the row count.
		 * 
		 * @return the row count
		 */
		public int getRowCount()
		{
			return rowCount;
		}
		
		
		/**
		 * Returns <tt>true</tt> if the <code>column</code> is a primary key.
		 * 
		 * @param column
		 *            the column to verify
		 * 
		 * @return <tt>true</tt> if the <code>column</code> is a primary key,
		 *         otherwise <tt>false</tt>
		 */
		public boolean isPrimaryKey(ColumnMetaData column)
		{
			String name = column.getName();
			for(Index index : indices)
			{
				if(index.getType() == IndexType.PRIMARY_KEY && index.containsColumn(name))
				{
					return true;
				}
			}
			
			return false;
		}
		
		
		/**
		 * Returns <tt>true</tt> if the <code>column</code> has a unique index.
		 * 
		 * @param column
		 *            the column to verify
		 * 
		 * @return <tt>true</tt> if the <code>column</code> has a unique,
		 *         otherwise <tt>false</tt>
		 */
		public boolean isUnique(ColumnMetaData column)
		{
			String name = column.getName();
			for(Index index : indices)
			{
				if(index.isUnique() && index.containsColumn(name))
				{
					return true;
				}
			}
			
			return false;
		}
		
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		public String toString()
		{
			return tableInfo.toString();
		}
		
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		public TableMetaData clone()
		{
			return new TableMetaData(tableInfo.clone(),ObjectUtils.clone(columns),
					ObjectUtils.clone(indices),rowCount);
		}
		
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		public boolean equals(Object obj)
		{
			if(obj == this)
			{
				return true;
			}
			
			if(obj instanceof TableMetaData)
			{
				return ((TableMetaData)obj).tableInfo.equals(tableInfo);
			}
			
			return false;
		}
		
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		public int hashCode()
		{
			return tableInfo.hashCode();
		}
	}
	
	
	/**
	 * Create the table meta data for the given {@link TableInfo}s.
	 * 
	 * @param flags
	 *            requested data, e.g {@link #INDICES} | {@link #ROW_COUNT} or
	 *            {@link #ALL}
	 * @param tables
	 *            a number of {@link TableInfo} instances
	 * 
	 * @return a array of {@link TableMetaData}
	 * 
	 * @throws DBException
	 *             if a database access error occurs
	 */
	public TableMetaData[] getTableMetaData(ProgressMonitor monitor, int flags, TableInfo... tables)
			throws DBException;
	
	
	/**
	 * Create the {@link StoredProcedure} Objects of this {@link DBMetaData}.
	 * 
	 * 
	 * @return a array of {@link StoredProcedure}
	 * 
	 * @throws DBException
	 *             if a database access error occurs
	 */
	public StoredProcedure[] getStoredProcedures(ProgressMonitor monitor) throws DBException;
	
	
	/**
	 * Create the {@link EntityRelationshipModel} of all tables of this
	 * {@link DBMetaData}.
	 * 
	 * 
	 * @return a {@link EntityRelationshipModel} of this {@link DBMetaData}
	 * 
	 * @throws DBException
	 *             if a database access error occurs
	 */
	public EntityRelationshipModel getEntityRelationshipModel(ProgressMonitor monitor)
			throws DBException;
	
	
	/**
	 * Create the {@link EntityRelationshipModel} of this {@link DBMetaData}.
	 * 
	 * @return a {@link EntityRelationshipModel} of this {@link DBMetaData}
	 * 
	 * @throws DBException
	 *             if a database access error occurs
	 */
	public EntityRelationshipModel getEntityRelationshipModel(ProgressMonitor monitor,
			TableInfo... tables) throws DBException;
	
	
	/**
	 * Compares the two {@link ColumnMetaData}.
	 * 
	 * @param clientColumn
	 *            the ColumnMetaData to compare
	 * 
	 * @param dbColumn
	 *            the ColumnMetaData to compare <code>clientColumn</code>
	 *            against
	 * 
	 * @return <code>true</code> if the given {@link ColumnMetaData}s are the
	 *         same, otherwise <code>flase</code>
	 * 
	 */
	public boolean equalsType(ColumnMetaData clientColumn, ColumnMetaData dbColumn);
	
	
	
	/**
	 * Holds table change information.
	 */
	public static class TableChange implements Serializable
	{
		private static final long	serialVersionUID	= -6351235741485438398L;
		
		
		
		public static enum Type
		{
			CREATE, ALTER
			// DELETE
		}
		
		private final Type			type;
		private final TableMetaData	oldTable;
		private final TableMetaData	newTable;
		
		
		public TableChange(TableMetaData oldTable, TableMetaData newTable)
		{
			if(oldTable == null && newTable == null)
			{
				throw new IllegalArgumentException("one of the tables must be set");
			}
			
			if(oldTable == null)
			{
				this.type = Type.CREATE;
			}
			else if(newTable == null)
			{
				// this.type = Type.DELETE;
				
				throw new IllegalArgumentException("delete is not supported");
			}
			else
			{
				this.type = Type.ALTER;
			}
			
			this.oldTable = oldTable;
			this.newTable = newTable;
		}
		
		
		public Type getType()
		{
			return type;
		}
		
		
		public TableMetaData getOldTable()
		{
			return oldTable;
		}
		
		
		public TableMetaData getNewTable()
		{
			return newTable;
		}
	}
	
	
	/**
	 * Synchronized tables in the database according to <code>changes</code>.
	 * 
	 * @param monitor
	 * 
	 * @param changes
	 *            all changes to be synchronized
	 * 
	 * @return all stored queries
	 * 
	 * @throws DBException
	 *             if a database access error occurs
	 */
	public Query[] synchronizeTables(ProgressMonitor monitor, TableChange... changes)
			throws DBException;
	
	
	/**
	 * Releases all resources used by this metadata object.
	 * 
	 * @since 4.0
	 */
	public void dispose();
}
