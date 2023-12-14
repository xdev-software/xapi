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
package xdev.db.servlet;


import java.text.ParseException;
import java.util.Date;
import java.util.EnumSet;
import java.util.Map;

import xdev.db.AbstractDBConnection;
import xdev.db.DBDataSource;
import xdev.db.DBException;
import xdev.db.DBMetaData;
import xdev.db.DBMetaData.TableChange;
import xdev.db.DBMetaData.TableInfo;
import xdev.db.DBMetaData.TableMetaData;
import xdev.db.DBMetaData.TableType;
import xdev.db.DBPager;
import xdev.db.QueryInfo;
import xdev.db.Result;
import xdev.db.Savepoint;
import xdev.db.StoredProcedure;
import xdev.db.StoredProcedure.Param;
import xdev.db.WriteResult;
import xdev.db.sql.SELECT;
import xdev.db.sql.WritingQuery;
import xdev.io.XdevObjectInputStream;
import xdev.io.XdevObjectOutputStream;
import xdev.util.MathUtils;
import xdev.util.Settings;
import xdev.vt.EntityRelationshipModel;

import com.xdev.jadoth.sqlengine.exceptions.SQLEngineException;
import com.xdev.jadoth.sqlengine.internal.DatabaseGateway;


public class ServletDBConnection extends AbstractDBConnection<ServletDBDataSource>
{
	private final ServletDBDataSource	dataSource;
	private SessionInfo					sessionInfo;
	private final DatabaseGateway		gateway;
	private boolean						inTransaction	= false;
	
	
	public ServletDBConnection(ServletDBDataSource dataSource, SessionInfo sessionInfo)
	{
		super(dataSource);
		
		this.dataSource = dataSource;
		this.sessionInfo = sessionInfo;
		this.gateway = dataSource.original.getGateway();
	}
	
	
	private <T> T exec(Exchange<T> exchange) throws Exception
	{
		try
		{
			return exchange.exec();
		}
		catch(SessionExpiredException see)
		{
			dataSource.resetSessionInfo();
			
			if(Settings.autoRenewServletSessions())
			{
				exchange.setSessionInfo(sessionInfo = dataSource.refreshSessionInfo());
				return exchange.exec();
			}
			else
			{
				throw see;
			}
		}
	}
	
	
	DBMetaData getMetaData() throws DBException
	{
		try
		{
			return exec(new Exchange<DBMetaData>(sessionInfo)
			{
				@Override
				void sendRequest(XdevObjectOutputStream out) throws Exception
				{
					out.writeByte(REQUEST_METADATA);
				}
				
				
				@Override
				DBMetaData readResponse(XdevObjectInputStream in) throws Exception
				{
					return (DBMetaData)in.readObject();
				}
			});
			
		}
		catch(Exception e)
		{
			throw new DBException(dataSource,e);
		}
	}
	
	
	TableInfo[] getTableInfos(final EnumSet<TableType> types) throws DBException
	{
		try
		{
			return exec(new Exchange<TableInfo[]>(sessionInfo)
			{
				@Override
				void sendRequest(XdevObjectOutputStream out) throws Exception
				{
					out.writeByte(REQUEST_METADATA_TABLE_INFOS);
					out.writeObject(types);
				}
				
				
				@Override
				TableInfo[] readResponse(XdevObjectInputStream in) throws Exception
				{
					return (TableInfo[])in.readObject();
				}
			});
			
		}
		catch(Exception e)
		{
			throw new DBException(dataSource,e);
		}
	}
	
	
	TableMetaData[] getTableMetaData(final int flags, final TableInfo[] tables) throws DBException
	{
		try
		{
			return exec(new Exchange<TableMetaData[]>(sessionInfo)
			{
				@Override
				void sendRequest(XdevObjectOutputStream out) throws Exception
				{
					out.writeByte(REQUEST_METADATA_TABLE_METADATA);
					out.writeInt(flags);
					out.writeObject(tables);
				}
				
				
				@Override
				TableMetaData[] readResponse(XdevObjectInputStream in) throws Exception
				{
					return (TableMetaData[])in.readObject();
				}
			});
			
		}
		catch(Exception e)
		{
			throw new DBException(dataSource,e);
		}
	}
	
	
	StoredProcedure[] getStoredProcedures() throws DBException
	{
		try
		{
			return exec(new Exchange<StoredProcedure[]>(sessionInfo)
			{
				@Override
				void sendRequest(XdevObjectOutputStream out) throws Exception
				{
					out.writeByte(REQUEST_METADATA_STORED_PROCEDURES);
				}
				
				
				@Override
				StoredProcedure[] readResponse(XdevObjectInputStream in) throws Exception
				{
					return (StoredProcedure[])in.readObject();
				}
			});
			
		}
		catch(Exception e)
		{
			throw new DBException(dataSource,e);
		}
	}
	
	
	EntityRelationshipModel getEntityRelationshipModel(final TableInfo[] tables) throws DBException
	{
		try
		{
			return exec(new Exchange<EntityRelationshipModel>(sessionInfo)
			{
				@Override
				void sendRequest(XdevObjectOutputStream out) throws Exception
				{
					out.writeByte(REQUEST_METADATA_ERS);
					out.writeObject(tables);
				}
				
				
				@Override
				EntityRelationshipModel readResponse(XdevObjectInputStream in) throws Exception
				{
					return (EntityRelationshipModel)in.readObject();
				}
			});
			
		}
		catch(Exception e)
		{
			throw new DBException(dataSource,e);
		}
	}
	
	
	Query[] synchronizeTables(final TableChange[] changes) throws DBException
	{
		try
		{
			return exec(new Exchange<Query[]>(sessionInfo)
			{
				@Override
				void sendRequest(XdevObjectOutputStream out) throws Exception
				{
					out.writeByte(REQUEST_METADATA_SYNC);
					out.writeObject(changes);
				}
				
				
				@Override
				Query[] readResponse(XdevObjectInputStream in) throws Exception
				{
					return (Query[])in.readObject();
				}
			});
			
		}
		catch(Exception e)
		{
			throw new DBException(dataSource,e);
		}
	}
	
	
	@Override
	public void testConnection() throws DBException
	{
		try
		{
			exec(new VoidExchange(sessionInfo)
			{
				@Override
				void sendRequest(XdevObjectOutputStream out) throws Exception
				{
					out.writeByte(REQUEST_TEST_CONNECTION);
				}
			});
			
		}
		catch(Exception e)
		{
			throw new DBException(dataSource,e);
		}
	}
	
	
	@Override
	public ServletResult query(SELECT select, Object... params) throws DBException
	{
		decorateDelegate(select,gateway);
		String sql = select.toString();
		ServletResult result = query(sql,select.getOffsetSkipCount(),
				select.getFetchFirstRowCount(),params);
		result.setQueryInfo(new QueryInfo(select,params));
		return result;
	}
	
	
	@Override
	public ServletResult query(String sql, Object... params) throws DBException
	{
		return query(sql,null,null,params);
	}
	
	
	private ServletResult query(String sql, Integer offset, Integer maxRowCount, Object... params)
			throws DBException
	{
		DBException exception = null;
		
		try
		{
			return queryImpl(sql,offset,maxRowCount,params);
		}
		catch(DBException e)
		{
			exception = e;
			throw e;
		}
		finally
		{
			queryPerformed(sql,params,exception);
		}
	}
	
	
	protected ServletResult queryImpl(final String sql, final Integer offset,
			final Integer maxRowCount, final Object... params) throws DBException
	{
		try
		{
			ServletResult result = exec(new ReadExchange(sessionInfo)
			{
				@Override
				void sendRequest(XdevObjectOutputStream out) throws Exception
				{
					out.writeByte(REQUEST_QUERY_SQL);
					out.writeUTF(sql);
					out.writeObject(offset);
					out.writeObject(maxRowCount);
					out.writeObject(params);
				}
			});
			
			return result;
		}
		catch(Exception e)
		{
			throw new DBException(dataSource,e);
		}
	}
	
	
	@Override
	public DBPager getPager(int rowsPerPage, SELECT select, Object... params) throws DBException
	{
		try
		{
			return new ServletDBPager(rowsPerPage,new QueryInfo(select,params));
		}
		catch(Exception e)
		{
			throw new DBException(dataSource,e);
		}
	}
	
	
	@Override
	public int getQueryRowCount(final String select) throws DBException
	{
		try
		{
			return exec(new Exchange<Integer>(sessionInfo)
			{
				@Override
				void sendRequest(XdevObjectOutputStream out) throws Exception
				{
					out.writeByte(REQUEST_GET_QUERY_ROW_COUNT);
					out.writeUTF(select);
				}
				
				
				@Override
				Integer readResponse(XdevObjectInputStream in) throws Exception
				{
					return in.readInt();
				}
			});
			
		}
		catch(Exception e)
		{
			throw new DBException(dataSource,e);
		}
	}
	
	
	@Override
	public WriteResult write(WritingQuery query, boolean returnGeneratedKeys, Object... params)
			throws DBException
	{
		try
		{
			decorateDelegate(query,gateway);
			String sql = query.toString();
			return write(sql,returnGeneratedKeys,params);
		}
		catch(SQLEngineException e)
		{
			throw new DBException(dataSource,e);
		}
	}
	
	
	@Override
	public WriteResult write(String sql, boolean returnGeneratedKeys, Object... params)
			throws DBException
	{
		DBException exception = null;
		
		try
		{
			return writeImpl(sql,returnGeneratedKeys,params);
		}
		catch(DBException e)
		{
			exception = e;
			throw e;
		}
		finally
		{
			queryPerformed(sql,params,exception);
		}
	}
	
	
	protected WriteResult writeImpl(final String sql, final boolean returnGeneratedKeys,
			final Object... params) throws DBException
	{
		try
		{
			WriteResult result = exec(new Exchange<WriteResult>(sessionInfo)
			{
				@Override
				void sendRequest(XdevObjectOutputStream out) throws Exception
				{
					out.writeByte(REQUEST_WRITE_SQL_RGK);
					out.writeUTF(sql);
					out.writeBoolean(returnGeneratedKeys);
					out.writeObject(params);
				}
				
				
				@Override
				WriteResult readResponse(XdevObjectInputStream in) throws Exception
				{
					int affectedRows = in.readInt();
					Result generatedKeys = null;
					if(in.readBoolean())
					{
						generatedKeys = new ServletResult(in);
					}
					
					return new WriteResult(affectedRows,generatedKeys);
				}
			});
			
			return result;
		}
		catch(Exception e)
		{
			throw new DBException(dataSource,e);
		}
	}
	
	
	@Override
	public WriteResult write(WritingQuery query, String[] columnNames, Object... params)
			throws DBException
	{
		try
		{
			decorateDelegate(query,gateway);
			String sql = query.toString();
			return write(sql,columnNames,params);
		}
		catch(SQLEngineException e)
		{
			throw new DBException(dataSource,e);
		}
	}
	
	
	@Override
	public WriteResult write(String sql, String[] columnNames, Object... params) throws DBException
	{
		DBException exception = null;
		
		try
		{
			return writeImpl(sql,columnNames,params);
		}
		catch(DBException e)
		{
			exception = e;
			throw e;
		}
		finally
		{
			queryPerformed(sql,params,exception);
		}
	}
	
	
	protected WriteResult writeImpl(final String sql, final String[] columnNames,
			final Object... params) throws DBException
	{
		try
		{
			WriteResult result = exec(new Exchange<WriteResult>(sessionInfo)
			{
				@Override
				void sendRequest(XdevObjectOutputStream out) throws Exception
				{
					out.writeByte(REQUEST_WRITE_SQL_COLS);
					out.writeUTF(sql);
					out.writeObject(columnNames);
					out.writeObject(params);
				}
				
				
				@Override
				WriteResult readResponse(XdevObjectInputStream in) throws Exception
				{
					int affectedRows = in.readInt();
					Result generatedKeys = null;
					if(in.readBoolean())
					{
						generatedKeys = new ServletResult(in);
					}
					
					return new WriteResult(affectedRows,generatedKeys);
				}
			});
			
			return result;
		}
		catch(Exception e)
		{
			throw new DBException(dataSource,e);
		}
	}
	
	
	@Override
	public void write(String sql) throws DBException
	{
		DBException exception = null;
		
		try
		{
			writeImpl(sql);
		}
		catch(DBException e)
		{
			exception = e;
			throw e;
		}
		finally
		{
			queryPerformed(sql,new Object[0],exception);
		}
	}
	
	
	protected void writeImpl(final String sql) throws DBException
	{
		try
		{
			exec(new VoidExchange(sessionInfo)
			{
				@Override
				void sendRequest(XdevObjectOutputStream out) throws Exception
				{
					out.writeByte(REQUEST_WRITE_SQL);
					out.writeUTF(sql);
				}
			});
		}
		catch(Exception e)
		{
			throw new DBException(dataSource,e);
		}
	}
	
	
	@Override
	public void call(final StoredProcedure procedure, final Object... params) throws DBException
	{
		try
		{
			exec(new VoidExchange(sessionInfo)
			{
				@Override
				void sendRequest(XdevObjectOutputStream out) throws Exception
				{
					out.writeByte(REQUEST_STORED_PROCEDURE);
					out.writeObject(procedure);
					out.writeObject(params);
				}
				
				
				@Override
				Object readResponse(XdevObjectInputStream in) throws Exception
				{
					int c = procedure.getParamCount();
					for(int i = 0; i < c; i++)
					{
						Param param = procedure.getParam(i);
						switch(param.getParamType())
						{
							case OUT:
							case IN_OUT:
							{
								param.setValue(in.readObject());
							}
							break;
						}
					}
					
					byte flag = in.readByte();
					switch(flag)
					{
						case RESPONSE_STORED_PROCEDURE_RESULT:
						{
							procedure.setReturnValue(new ServletResult(in));
						}
						break;
						
						case RESPONSE_STORED_PROCEDURE_OBJECT:
						{
							procedure.setReturnValue(in.readObject());
						}
						break;
					}
					
					return null;
				}
			});
			
		}
		catch(Exception e)
		{
			throw new DBException(dataSource,e);
		}
	}
	
	
	@Override
	public void beginTransaction() throws DBException
	{
		try
		{
			exec(new VoidExchange(sessionInfo)
			{
				@Override
				void sendRequest(XdevObjectOutputStream out) throws Exception
				{
					out.writeByte(REQUEST_BEGIN_TRANSACTION);
				}
				
				
				@Override
				Object readResponse(XdevObjectInputStream in) throws Exception
				{
					inTransaction = true;
					return null;
				}
			});
		}
		catch(Exception e)
		{
			throw new DBException(dataSource,e);
		}
	}
	
	
	@Override
	public boolean isInTransaction() throws DBException
	{
		return inTransaction;
	}
	
	
	@Override
	public Savepoint setSavepoint() throws DBException
	{
		try
		{
			return exec(new Exchange<Savepoint>(sessionInfo)
			{
				@Override
				void sendRequest(XdevObjectOutputStream out) throws Exception
				{
					out.writeByte(REQUEST_SET_SAVEPOINT);
				}
				
				
				@Override
				Savepoint readResponse(XdevObjectInputStream in) throws Exception
				{
					return (Savepoint)in.readObject();
				}
			});
		}
		catch(Exception e)
		{
			throw new DBException(dataSource,e);
		}
	}
	
	
	@Override
	public Savepoint setSavepoint(final String name) throws DBException
	{
		try
		{
			return exec(new Exchange<Savepoint>(sessionInfo)
			{
				@Override
				void sendRequest(XdevObjectOutputStream out) throws Exception
				{
					out.writeByte(REQUEST_SET_SAVEPOINT_NAME);
					out.writeUTF(name);
				}
				
				
				@Override
				Savepoint readResponse(XdevObjectInputStream in) throws Exception
				{
					return (Savepoint)in.readObject();
				}
			});
		}
		catch(Exception e)
		{
			throw new DBException(dataSource,e);
		}
	}
	
	
	@Override
	public void releaseSavepoint(final Savepoint savepoint) throws DBException
	{
		try
		{
			exec(new VoidExchange(sessionInfo)
			{
				@Override
				void sendRequest(XdevObjectOutputStream out) throws Exception
				{
					out.writeByte(REQUEST_RELEASE_SAVEPOINT);
					out.writeObject(savepoint);
				}
			});
		}
		catch(Exception e)
		{
			throw new DBException(dataSource,e);
		}
	}
	
	
	@Override
	public void commit() throws DBException
	{
		try
		{
			exec(new TransactionEndingExchange(sessionInfo)
			{
				@Override
				void sendRequest(XdevObjectOutputStream out) throws Exception
				{
					out.writeByte(REQUEST_COMMIT);
				}
			});
		}
		catch(Exception e)
		{
			throw new DBException(dataSource,e);
		}
	}
	
	
	@Override
	public void rollback() throws DBException
	{
		try
		{
			exec(new TransactionEndingExchange(sessionInfo)
			{
				@Override
				void sendRequest(XdevObjectOutputStream out) throws Exception
				{
					out.writeByte(REQUEST_ROLLBACK);
				}
			});
		}
		catch(Exception e)
		{
			throw new DBException(dataSource,e);
		}
	}
	
	
	@Override
	public void rollback(final Savepoint savepoint) throws DBException
	{
		try
		{
			exec(new TransactionEndingExchange(sessionInfo)
			{
				@Override
				void sendRequest(XdevObjectOutputStream out) throws Exception
				{
					out.writeByte(REQUEST_ROLLBACK_SAVEPOINT);
					out.writeObject(savepoint);
				}
			});
		}
		catch(Exception e)
		{
			throw new DBException(dataSource,e);
		}
	}
	
	
	@Override
	public void close() throws DBException
	{
	}
	
	
	
	private static abstract class VoidExchange extends Exchange<Object>
	{
		VoidExchange(SessionInfo info) throws Exception
		{
			super(info);
		}
		
		
		@Override
		Object readResponse(XdevObjectInputStream in) throws Exception
		{
			return null;
		}
	}
	
	
	
	private static abstract class ReadExchange extends Exchange<ServletResult>
	{
		ReadExchange(SessionInfo info) throws Exception
		{
			super(info);
		}
		
		
		@Override
		ServletResult readResponse(XdevObjectInputStream in) throws Exception
		{
			return new ServletResult(in);
		}
	}
	
	
	
	private abstract class TransactionEndingExchange extends Exchange<Object>
	{
		TransactionEndingExchange(SessionInfo info) throws Exception
		{
			super(info);
		}
		
		
		@Override
		Object readResponse(XdevObjectInputStream in) throws Exception
		{
			inTransaction = false;
			return null;
		}
	}
	
	
	
	private class ServletDBPager implements DBPager
	{
		QueryInfo	queryInfo;
		int			id;
		int			rowsPerPage;
		int			totalRows;
		int			maxPageIndex;
		int			currentPageIndex	= -1;
		int			currentRowIndex		= -1;
		
		
		ServletDBPager(int rowsPerPage, final QueryInfo queryInfo) throws Exception
		{
			this.queryInfo = queryInfo;
			
			int[] answer = exec(new Exchange<int[]>(sessionInfo)
			{
				@Override
				void sendRequest(XdevObjectOutputStream out) throws Exception
				{
					SELECT select = queryInfo.getSelect();
					Object[] params = queryInfo.getParameters();
					
					decorateDelegate(select,gateway);
					
					Integer offset = select.getOffsetSkipCount();
					Integer limit = select.getFetchFirstRowCount();
					
					if(!gateway.getDbmsAdaptor().supportsOFFSET_ROWS() && offset != null
							&& offset > 0 && limit != null && limit > 0)
					{
						limit += offset;
						select.FETCH_FIRST(limit);
					}
					
					String sql = select.toString();
					
					out.writeByte(REQUEST_PAGING_START);
					out.writeUTF(sql);
					out.writeObject(params);
				}
				
				
				@Override
				int[] readResponse(XdevObjectInputStream in) throws Exception
				{
					return new int[]{in.readInt(),in.readInt()};
				}
			});
			id = answer[0];
			totalRows = answer[1];
			setRowsPerPageValues(rowsPerPage);
		}
		
		
		@Override
		public DBDataSource<?> getDataSource()
		{
			return dataSource;
		}
		
		
		@Override
		public Result setRowsPerPage(int rowsPerPage) throws DBException
		{
			setRowsPerPageValues(rowsPerPage);
			
			if(currentRowIndex == -1)
			{
				return nextPage();
			}
			
			return gotoPage(rowsPerPage == 0 ? 0 : currentRowIndex / rowsPerPage);
		}
		
		
		void setRowsPerPageValues(int rowsPerPage)
		{
			this.rowsPerPage = rowsPerPage;
			
			if(rowsPerPage > 0)
			{
				maxPageIndex = totalRows / rowsPerPage;
				if(totalRows % rowsPerPage == 0)
				{
					maxPageIndex--;
				}
			}
			else
			{
				maxPageIndex = 0;
			}
		}
		
		
		@Override
		public int getRowsPerPage()
		{
			return rowsPerPage;
		}
		
		
		@Override
		public int getTotalRows()
		{
			return totalRows;
		}
		
		
		@Override
		public int getCurrentPageIndex()
		{
			return currentPageIndex;
		}
		
		
		@Override
		public int getMaxPageIndex()
		{
			return maxPageIndex;
		}
		
		
		@Override
		public boolean hasNextPage()
		{
			return Math.max(currentRowIndex,0) + rowsPerPage < totalRows;
		}
		
		
		@Override
		public boolean hasPreviousPage()
		{
			return this.currentRowIndex > 0;
		}
		
		
		@Override
		public Result nextPage() throws DBException
		{
			if(!hasNextPage())
			{
				return null;
			}
			
			if(currentRowIndex == -1 || currentPageIndex == -1)
			{
				currentRowIndex = currentPageIndex = 0;
			}
			else
			{
				currentRowIndex += rowsPerPage;
				currentPageIndex++;
			}
			
			return getResult();
		}
		
		
		@Override
		public Result previousPage() throws DBException
		{
			if(!hasPreviousPage())
			{
				return null;
			}
			
			currentRowIndex -= rowsPerPage;
			if(currentRowIndex < 0)
			{
				currentRowIndex = 0;
			}
			currentPageIndex--;
			
			return getResult();
		}
		
		
		@Override
		public Result firstPage() throws DBException
		{
			if(!hasPreviousPage())
			{
				return null;
			}
			
			currentRowIndex = currentPageIndex = 0;
			
			return getResult();
		}
		
		
		@Override
		public Result lastPage() throws DBException
		{
			if(!hasNextPage())
			{
				return null;
			}
			
			currentRowIndex = maxPageIndex * rowsPerPage;
			currentPageIndex = maxPageIndex;
			
			return getResult();
		}
		
		
		@Override
		public Result gotoPage(int page) throws DBException
		{
			MathUtils.checkRange(page,0,maxPageIndex);
			
			currentRowIndex = page * rowsPerPage;
			currentPageIndex = page;
			
			return getResult();
		}
		
		
		@Override
		public Result gotoRow(int row) throws DBException
		{
			MathUtils.checkRange(row,0,totalRows - 1);
			
			currentRowIndex = row;
			currentPageIndex = row / rowsPerPage;
			
			return getResult();
		}
		
		
		Result getResult() throws DBException
		{
			try
			{
				ServletResult result = exec(new ReadExchange(sessionInfo)
				{
					@Override
					void sendRequest(XdevObjectOutputStream out) throws Exception
					{
						out.writeByte(REQUEST_PAGING_RESULT);
						out.writeInt(id);
						out.writeInt(currentRowIndex);
						out.writeInt(rowsPerPage);
					}
				});
				result.setQueryInfo(queryInfo);
				return result;
			}
			catch(Exception e)
			{
				throw new DBException(dataSource,e);
			}
		}
		
		
		@Override
		public void close() throws DBException
		{
			try
			{
				exec(new VoidExchange(sessionInfo)
				{
					@Override
					void sendRequest(XdevObjectOutputStream out) throws Exception
					{
						out.writeByte(REQUEST_PAGING_CLOSE);
						out.writeInt(id);
					}
				});
			}
			catch(Exception e)
			{
				throw new DBException(dataSource,e);
			}
		}
		
		
		@Override
		public boolean isClosed() throws DBException
		{
			return false;
		}
	}
	
	
	@Override
	public void createTable(String tableName, String primaryKey, Map<String, String> columnMap,
			boolean isAutoIncrement, Map<String, String> foreignKeys) throws Exception
	{
		// TODO implement for integrate locking if it is necessary
	}
	
	
	@Override
	public Date getServerTime() throws DBException, ParseException
	{
		return null;
	}
}
