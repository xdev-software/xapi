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


import java.text.ParseException;
import java.util.Date;
import java.util.EnumSet;
import java.util.Map;

import xdev.db.DBConnection.Query;
import xdev.db.sql.SELECT;
import xdev.db.sql.WritingQuery;
import xdev.util.ProgressMonitor;
import xdev.vt.EntityRelationshipModel;


/**
 * An empty dummy data source which returns empty results.
 * 
 * @author XDEV Software
 * @since 3.2
 */
public class NullDBDataSource extends AbstractDBDataSource<NullDBDataSource>
{
	public final static NullDBDataSource	instance	= new NullDBDataSource();
	
	
	@Override
	public Parameter[] getDefaultParameters()
	{
		return new Parameter[0];
	}
	
	
	@Override
	public DBMetaData getMetaData() throws DBException
	{
		return new NullDBMetaData();
	}
	
	
	@Override
	protected DBConnection openConnectionImpl() throws DBException
	{
		return new NullDBConnection(this);
	}
	
	
	@Override
	public void closeAllOpenConnections() throws DBException
	{
	}
	
	
	@Override
	public boolean canExport()
	{
		return false;
	}
	
	
	@Override
	public void clearCaches()
	{
	}
	
	
	
	private static class NullDBMetaData extends AbstractDBMetaData
	{
		@Override
		public String getName()
		{
			return "Null Data Source";
		}
		
		
		@Override
		public String getVersion()
		{
			return "1.0";
		}
		
		
		@Override
		public boolean isCaseSensitive() throws DBException
		{
			return false;
		}
		
		
		@Override
		public int getMaxColumnNameLength() throws DBException
		{
			return 0;
		}
		
		
		@Override
		public TableInfo[] getTableInfos(ProgressMonitor monitor, EnumSet<TableType> types)
				throws DBException
		{
			return new TableInfo[0];
		}
		
		
		@Override
		public TableMetaData[] getTableMetaData(ProgressMonitor monitor, int flags,
				TableInfo... tables) throws DBException
		{
			return new TableMetaData[0];
		}
		
		
		@Override
		public StoredProcedure[] getStoredProcedures(ProgressMonitor monitor) throws DBException
		{
			return new StoredProcedure[0];
		}
		
		
		@Override
		public EntityRelationshipModel getEntityRelationshipModel(ProgressMonitor monitor,
				TableInfo... tables) throws DBException
		{
			return new EntityRelationshipModel();
		}
		
		
		@Override
		public boolean equalsType(ColumnMetaData clientColumn, ColumnMetaData dbColumn)
		{
			return false;
		}
		
		
		@Override
		public Query[] synchronizeTables(ProgressMonitor monitor, TableChange... changes)
				throws DBException
		{
			return null;
		}
	}
	
	
	
	private static class NullDBConnection extends AbstractDBConnection<NullDBDataSource>
	{
		NullDBConnection(NullDBDataSource dataSource)
		{
			super(dataSource);
		}
		
		
		@Override
		public void testConnection() throws DBException
		{
		}
		
		
		@Override
		public Result query(SELECT select, Object... params) throws DBException
		{
			return new EmptyResult();
		}
		
		
		@Override
		public Result query(String sql, Object... params) throws DBException
		{
			return new EmptyResult();
		}
		
		
		@Override
		public DBPager getPager(int rowsPerPage, SELECT select, Object... params)
				throws DBException
		{
			return new EmptyPager(getDataSource());
		}
		
		
		@Override
		public int getQueryRowCount(String select) throws DBException
		{
			return 0;
		}
		
		
		@Override
		public WriteResult write(WritingQuery query, boolean returnGeneratedKeys, Object... params)
				throws DBException
		{
			return new WriteResult(0,new EmptyResult());
		}
		
		
		@Override
		public WriteResult write(String sql, boolean returnGeneratedKeys, Object... params)
				throws DBException
		{
			return new WriteResult(0,new EmptyResult());
		}
		
		
		@Override
		public WriteResult write(WritingQuery query, String[] columnNames, Object... params)
				throws DBException
		{
			return new WriteResult(0,new EmptyResult());
		}
		
		
		@Override
		public WriteResult write(String sql, String[] columnNames, Object... params)
				throws DBException
		{
			return new WriteResult(0,new EmptyResult());
		}
		
		
		@Override
		public void write(String sql) throws DBException
		{
		}
		
		
		@Override
		public void call(StoredProcedure procedure, Object... params) throws DBException
		{
		}
		
		
		@Override
		public void beginTransaction() throws DBException
		{
		}
		
		
		@Override
		public boolean isInTransaction() throws DBException
		{
			return false;
		}
		
		
		@Override
		public Savepoint setSavepoint() throws DBException
		{
			return new Savepoint()
			{
				@Override
				public String getSavepointName() throws DBException
				{
					return "" + hashCode();
				}
				
				
				@Override
				public int getSavepointId() throws DBException
				{
					return hashCode();
				}
			};
		}
		
		
		@Override
		public Savepoint setSavepoint(final String name) throws DBException
		{
			return new Savepoint()
			{
				@Override
				public String getSavepointName() throws DBException
				{
					return name;
				}
				
				
				@Override
				public int getSavepointId() throws DBException
				{
					return hashCode();
				}
			};
		}
		
		
		@Override
		public void releaseSavepoint(Savepoint savepoint) throws DBException
		{
		}
		
		
		@Override
		public void commit() throws DBException
		{
		}
		
		
		@Override
		public void rollback() throws DBException
		{
		}
		
		
		@Override
		public void rollback(Savepoint savepoint) throws DBException
		{
		}
		
		
		@Override
		public void close() throws DBException
		{
		}
		
		
		@Override
		public void createTable(String tableName, String primaryKey, Map<String, String> columnMap,
				boolean isAutoIncrement, Map<String, String> foreignKeys) throws Exception
		{
		}
		
		
		@Override
		public Date getServerTime() throws DBException, ParseException
		{
			return new Date(System.currentTimeMillis());
		}
	}
	
	
	
	private static class EmptyResult extends Result
	{
		@Override
		public int getColumnCount()
		{
			return 0;
		}
		
		
		@Override
		public ColumnMetaData getMetadata(int col)
		{
			return null;
		}
		
		
		@Override
		public boolean next() throws DBException
		{
			return false;
		}
		
		
		@Override
		public int skip(int count) throws DBException
		{
			return 0;
		}
		
		
		@Override
		public Object getObject(int col) throws DBException
		{
			return null;
		}
		
		
		@Override
		public void close() throws DBException
		{
		}
	}
	
	
	
	private static class EmptyPager implements DBPager
	{
		private DBDataSource	dataSource;
		
		
		EmptyPager(DBDataSource dataSource)
		{
			this.dataSource = dataSource;
		}
		
		
		@Override
		public DBDataSource<?> getDataSource()
		{
			return dataSource;
		}
		
		
		@Override
		public boolean hasNextPage()
		{
			return false;
		}
		
		
		@Override
		public boolean hasPreviousPage()
		{
			return false;
		}
		
		
		@Override
		public Result nextPage() throws DBException
		{
			return new EmptyResult();
		}
		
		
		@Override
		public Result previousPage() throws DBException
		{
			return new EmptyResult();
		}
		
		
		@Override
		public Result firstPage() throws DBException
		{
			return new EmptyResult();
		}
		
		
		@Override
		public Result lastPage() throws DBException
		{
			return new EmptyResult();
		}
		
		
		@Override
		public Result gotoPage(int page) throws DBException
		{
			return new EmptyResult();
		}
		
		
		@Override
		public Result gotoRow(int row) throws DBException
		{
			return new EmptyResult();
		}
		
		
		@Override
		public int getCurrentPageIndex()
		{
			return 0;
		}
		
		
		@Override
		public int getMaxPageIndex()
		{
			return 0;
		}
		
		
		@Override
		public Result setRowsPerPage(int rowsPerPage)
		{
			return new EmptyResult();
		}
		
		
		@Override
		public int getRowsPerPage()
		{
			return 0;
		}
		
		
		@Override
		public int getTotalRows()
		{
			return 0;
		}
		
		
		@Override
		public void close() throws DBException
		{
		}
		
		
		@Override
		public boolean isClosed() throws DBException
		{
			return false;
		}
	}
}
