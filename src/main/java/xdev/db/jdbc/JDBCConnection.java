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


import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import xdev.db.AbstractDBConnection;
import xdev.db.DBException;
import xdev.db.DBPager;
import xdev.db.DBUtils;
import xdev.db.PrefetchedResult;
import xdev.db.QueryInfo;
import xdev.db.Result;
import xdev.db.Savepoint;
import xdev.db.StoredProcedure;
import xdev.db.StoredProcedure.Param;
import xdev.db.StoredProcedure.ParamType;
import xdev.db.StoredProcedure.ReturnTypeFlavor;
import xdev.db.WriteResult;
import xdev.db.sql.SELECT;
import xdev.db.sql.WritingQuery;
import xdev.io.ByteHolder;
import xdev.io.CharHolder;
import xdev.util.logging.LoggerFactory;
import xdev.util.logging.XdevLogger;
import xdev.vt.XdevBlob;
import xdev.vt.XdevClob;

import com.xdev.jadoth.sqlengine.dbms.DbmsAdaptor;
import com.xdev.jadoth.sqlengine.internal.DatabaseGateway;


public abstract class JDBCConnection<DS extends JDBCDataSource<DS, A>, A extends DbmsAdaptor<A>>
		extends AbstractDBConnection<DS>
{
	/**
	 * Logger instance for this class.
	 */
	protected static final XdevLogger	log	= LoggerFactory.getLogger(JDBCConnection.class);
											
	protected final DatabaseGateway<A>	gateway;
	private Connection					connection;
										
										
	public JDBCConnection(DS dataSource)
	{
		super(dataSource);
		gateway = dataSource.getGateway();
		
		if(log.isDebugEnabled())
		{
			log.debug("JDBCConnection created: " + this.toString());
		}
	}
	
	
	public final Connection getConnection() throws DBException
	{
		if(connection == null)
		{
			connection = establishConnection();
		}
		
		return connection;
	}
	
	
	protected Connection establishConnection() throws DBException
	{
		
		Connection con = getDataSource().connectImpl();
		
		if(log.isDebugEnabled())
		{
			log.debug(
					"JDBCConnection " + this.toString() + " got the Connection " + con.toString());
		}
		
		return con;
	}
	
	
	@Override
	public void testConnection() throws DBException
	{
		try
		{
			getConnection();
		}
		catch(Exception e)
		{
			throw new DBException(dataSource,e);
		}
	}
	
	
	@Override
	public JDBCResult query(SELECT select, Object... params) throws DBException
	{
		decorateDelegate(select,gateway);
		
		Integer offset = select.getOffsetSkipCount();
		Integer limit = select.getFetchFirstRowCount();
		
		if(!gateway.getDbmsAdaptor().supportsOFFSET_ROWS() && offset != null && offset > 0
				&& limit != null && limit > 0)
		{
			limit += offset;
			select.FETCH_FIRST(limit);
		}
		
		String sql = select.toString();
		JDBCResult result = query(sql,offset,limit,params);
		result.setQueryInfo(new QueryInfo(select,params));
		return result;
	}
	
	
	@Override
	public JDBCResult query(String sql, Object... params) throws DBException
	{
		return query(sql,null,null,params);
	}
	
	
	public JDBCResult query(String sql, Integer offset, Integer maxRowCount, Object... params)
			throws DBException
	{
		try
		{
			ResultSet rs = queryJDBC(sql,params);
			
			JDBCResult result;
			if((offset != null || maxRowCount != null)
					&& !gateway.getDbmsAdaptor().supportsOFFSET_ROWS())
			{
				result = new JDBCResult(rs,offset != null ? offset : 0,0,maxRowCount);
			}
			else
			{
				result = new JDBCResult(rs);
			}
			result.setDataSource(dataSource);
			return result;
		}
		catch(DBException e)
		{
			throw e;
		}
		catch(Exception e)
		{
			throw new DBException(dataSource,e);
		}
	}
	
	
	@Override
	public int getQueryRowCount(String select) throws DBException
	{
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT COUNT(*) FROM (");
		sb.append(select);
		sb.append(") AS _RESULT_COUNT_");
		
		try
		{
			ResultSet result = queryJDBC(sb.toString());
			try
			{
				result.next();
				int rowCount = result.getInt(1);
				return rowCount;
			}
			finally
			{
				JDBCUtils.closeSilent(result);
			}
		}
		catch(DBException e)
		{
			throw e;
		}
		catch(Exception e)
		{
			throw new DBException(dataSource,e);
		}
	}
	
	
	protected ResultSet queryJDBC(String sql, Object... params) throws DBException
	{
		return queryJDBC(sql,params,ResultSet.TYPE_FORWARD_ONLY);
	}
	
	
	protected ResultSet queryJDBC(String sql, Object[] params, int resultSetType) throws DBException
	{
		DBException exception = null;
		
		try
		{
			return queryJDBCImpl(sql,params,resultSetType);
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
	
	
	public ResultSet queryJDBCImpl(String sql, Object[] params, int resultSetType)
			throws DBException
	{
		try
		{
			Connection connection = getConnection();
			
			ResultSet rs;
			if(params != null && params.length > 0)
			{
				prepareParams(connection,params);
				PreparedStatement ps = connection.prepareStatement(sql,resultSetType,
						ResultSet.CONCUR_READ_ONLY);
				for(int i = 0; i < params.length; i++)
				{
					setPreparedStatementParameter(ps,params[i],i + 1);
				}
				rs = ps.executeQuery();
			}
			else
			{
				rs = connection.createStatement(resultSetType,ResultSet.CONCUR_READ_ONLY)
						.executeQuery(sql);
			}
			
			return rs;
		}
		catch(DBException e)
		{
			throw e;
		}
		catch(Exception e)
		{
			throw new DBException(dataSource,e);
		}
	}
	
	
	@Override
	public DBPager getPager(int rowsPerPage, SELECT select, Object... params) throws DBException
	{
		decorateDelegate(select,gateway);
		
		Integer offset = select.getOffsetSkipCount();
		Integer limit = select.getFetchFirstRowCount();
		
		if(!gateway.getDbmsAdaptor().supportsOFFSET_ROWS() && offset != null && offset > 0
				&& limit != null && limit > 0)
		{
			limit += offset;
			select.FETCH_FIRST(limit);
		}
		
		QueryInfo queryInfo = new QueryInfo(select,params);
		String sql = select.toString();
		ResultSet resultSet;
		try
		{
			resultSet = queryJDBCImpl(sql,params,ResultSet.TYPE_SCROLL_SENSITIVE);
		}
		catch(DBException e)
		{
			if(e.getCause() instanceof SQLFeatureNotSupportedException)
			{
				resultSet = queryJDBCImpl(sql,params,ResultSet.TYPE_SCROLL_INSENSITIVE);
			}
			else
			{
				throw e;
			}
		}
		return new JDBCPager(this,queryInfo,resultSet,rowsPerPage);
	}
	
	
	@Override
	public final WriteResult write(WritingQuery query, boolean returnGeneratedKeys,
			Object... params) throws DBException
	{
		try
		{
			decorateDelegate(query,gateway);
			String sql = query.toString();
			return write(sql,returnGeneratedKeys,params);
		}
		catch(DBException e)
		{
			throw e;
		}
		catch(Exception e)
		{
			throw new DBException(dataSource,e);
		}
	}
	
	
	@Override
	public final WriteResult write(String sql, boolean returnGeneratedKeys, Object... params)
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
	
	
	protected WriteResult writeImpl(String sql, boolean returnGeneratedKeys, Object... params)
			throws DBException
	{
		// System.out.println(sql + ", params = " +
		// java.util.Arrays.toString(params));
		
		try
		{
			Connection connection = getConnection();
			
			PreparedStatement ps;
			try
			{
				ps = connection.prepareStatement(sql,returnGeneratedKeys
						? Statement.RETURN_GENERATED_KEYS : Statement.NO_GENERATED_KEYS);
			}
			catch(Exception e)
			{
				// return generated keys is not supported by driver
				
				returnGeneratedKeys = false;
				ps = connection.prepareStatement(sql);
			}
			
			try
			{
				if(params != null && params.length > 0)
				{
					prepareParams(connection,params);
					for(int i = 0; i < params.length; i++)
					{
						setPreparedStatementParameter(ps,params[i],i + 1);
					}
				}
				
				int affectedRows = ps.executeUpdate();
				Result generatedKeys = null;
				if(returnGeneratedKeys)
				{
					ResultSet rs = ps.getGeneratedKeys();
					if(rs != null)
					{
						generatedKeys = new JDBCResult(rs);
						generatedKeys.setDataSource(dataSource);
						
						// prefetch data
						generatedKeys = new PrefetchedResult(generatedKeys);
					}
				}
				
				return new WriteResult(affectedRows,generatedKeys);
			}
			finally
			{
				ps.close();
			}
		}
		catch(DBException e)
		{
			throw e;
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
		catch(DBException e)
		{
			throw e;
		}
		catch(Exception e)
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
	
	
	protected WriteResult writeImpl(String sql, String[] columnNames, Object... params)
			throws DBException
	{
		// System.out.println(sql + ", params = " +
		// java.util.Arrays.toString(params));
		
		try
		{
			Connection connection = getConnection();
			boolean returnGeneratedKeys = true;
			PreparedStatement ps;
			try
			{
				// Fix for issue XDEV-2635
				if(columnNames.length > 0)
				{
					ps = connection.prepareStatement(sql,columnNames);
				}
				else
				{
					// no generated key columns are defined
					returnGeneratedKeys = false;
					ps = connection.prepareStatement(sql);
				}
				
			}
			catch(Exception e)
			{
				// return generated keys is not supported by driver
				
				returnGeneratedKeys = false;
				ps = connection.prepareStatement(sql);
			}
			
			try
			{
				if(params != null && params.length > 0)
				{
					prepareParams(connection,params);
					for(int i = 0; i < params.length; i++)
					{
						setPreparedStatementParameter(ps,params[i],i + 1);
					}
				}
				
				int affectedRows = ps.executeUpdate();
				Result generatedKeys = null;
				if(returnGeneratedKeys)
				{
					ResultSet rs = ps.getGeneratedKeys();
					if(rs != null)
					{
						generatedKeys = new JDBCResult(rs);
						generatedKeys.setDataSource(dataSource);
						
						// prefetch data
						generatedKeys = new PrefetchedResult(generatedKeys);
					}
				}
				
				return new WriteResult(affectedRows,generatedKeys);
			}
			finally
			{
				ps.close();
			}
		}
		catch(DBException e)
		{
			throw e;
		}
		catch(Exception e)
		{
			throw new DBException(dataSource,e);
		}
	}
	
	
	@Override
	public final void write(String sql) throws DBException
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
	
	
	protected void writeImpl(String sql) throws DBException
	{
		// System.out.println(sql);
		
		try
		{
			Statement statement = getConnection().createStatement();
			try
			{
				statement.execute(sql);
			}
			finally
			{
				statement.close();
			}
		}
		catch(Exception e)
		{
			throw new DBException(dataSource,e);
		}
	}
	
	
	@Override
	public void call(StoredProcedure procedure, Object... params) throws DBException
	{
		try
		{
			Connection connection = getConnection();
			prepareParams(connection,params);
			
			procedure.prepareCall(params);
			int pc = procedure.getParamCount();
			
			ReturnTypeFlavor returnTypeFlavor = procedure.getReturnTypeFlavor();
			boolean hasReturnType = returnTypeFlavor == ReturnTypeFlavor.TYPE;
			
			StringBuffer query = new StringBuffer();
			query.append("{");
			if(hasReturnType)
			{
				query.append("? = ");
			}
			query.append("call ");
			query.append(procedure.getName());
			query.append("(");
			for(int i = 0; i < pc; i++)
			{
				if(i > 0)
				{
					query.append(",");
				}
				query.append("?");
			}
			query.append(")}");
			
			try(CallableStatement statement = connection.prepareCall(query.toString()))
			{
				int pi = 1;
				if(hasReturnType)
				{
					statement.registerOutParameter(pi++,-1);
				}
				
				for(int i = 0; i < pc; i++, pi++)
				{
					Param param = procedure.getParam(i);
					ParamType type = param.getParamType();
					
					if(type == ParamType.IN || type == ParamType.IN_OUT)
					{
						statement.setObject(pi,param.getValue());
					}
					
					if(type == ParamType.OUT || type == ParamType.IN_OUT)
					{
						statement.registerOutParameter(pi,-1);
					}
				}
				
				pi = 1;
				Object returnValue = null;
				if(returnTypeFlavor == ReturnTypeFlavor.RESULT_SET)
				{
					ResultSet resultSet = statement.executeQuery();
					/*
					* handed over to StoredProcedure, which is an AutoCloseable
					* itself
					*/
					@SuppressWarnings("resource")
					JDBCResult result = new JDBCResult(resultSet);
					result.setDataSource(dataSource);
					returnValue = result;
				}
				else
				{
					statement.execute();
					
					if(returnTypeFlavor == ReturnTypeFlavor.TYPE)
					{
						returnValue = statement.getObject(pi++);
					}
				}
				
				for(int i = 0; i < pc; i++, pi++)
				{
					Param param = procedure.getParam(i);
					ParamType type = param.getParamType();
					
					if(type == ParamType.OUT || type == ParamType.IN_OUT)
					{
						param.setValue(statement.getObject(pi));
					}
				}
				procedure.setReturnValue(returnValue);
			}		
		}
		catch(DBException e)
		{
			throw e;
		}
		catch(Exception e)
		{
			throw new DBException(dataSource,e);
		}
	}
	
	
	/**
	 * Checks <code>params</code> and converts them to JDBC compatible objects
	 * if necessary.
	 * <p>
	 * Conversions:
	 * <ul>
	 * <li>{@link XdevBlob} to {@link Blob}</li>
	 * <li>{@link ByteHolder} to byte[]</li>
	 * <li>{@link XdevClob} to {@link Clob}</li>
	 * <li>{@link CharHolder} to char[]</li>
	 * <li>{@link Calendar} to {@link Date}</li>
	 * </ul>
	 * </p>
	 * 
	 * @param params
	 *            the objects to check
	 * @throws DBException
	 *             if a sql error occurs
	 */
	protected void prepareParams(Connection connection, Object[] params) throws DBException
	{
		if(params == null)
		{
			return;
		}
		
		for(int i = 0; i < params.length; i++)
		{
			Object param = params[i];
			if(param == null)
			{
				continue;
			}
			
			if(param instanceof XdevBlob)
			{
				params[i] = ((XdevBlob)param).toJDBCBlob();
			}
			else if(param instanceof ByteHolder)
			{
				params[i] = ((ByteHolder)param).toByteArray();
			}
			else if(param instanceof XdevClob)
			{
				params[i] = ((XdevClob)param).toJDBCClob();
			}
			else if(param instanceof CharHolder)
			{
				params[i] = ((CharHolder)param).toCharArray();
			}
			else if(param instanceof Calendar)
			{
				params[i] = new Timestamp(((Calendar)param).getTimeInMillis());
			}
			else if(param instanceof java.util.Date)
			{
				params[i] = new Timestamp(((java.util.Date)param).getTime());
			}
		}
	}
	
	
	protected void setPreparedStatementParameter(PreparedStatement statement, Object parameter,
			int jdbcIndex) throws SQLException, DBException
	{
		statement.setObject(jdbcIndex,parameter);
	}
	
	
	@Override
	public void beginTransaction() throws DBException
	{
		try
		{
			getConnection().setAutoCommit(false);
		}
		catch(Exception e)
		{
			throw new DBException(dataSource,e);
		}
	}
	
	
	@Override
	public boolean isInTransaction() throws DBException
	{
		try
		{
			return connection != null && connection.getAutoCommit() == false;
		}
		catch(Exception e)
		{
			throw new DBException(dataSource,e);
		}
	}
	
	
	@Override
	public Savepoint setSavepoint() throws DBException
	{
		try
		{
			return new XdevDbSavepointAdapter(getConnection().setSavepoint(),dataSource);
		}
		catch(Exception e)
		{
			throw new DBException(dataSource,e);
		}
	}
	
	
	@Override
	public Savepoint setSavepoint(String name) throws DBException
	{
		try
		{
			return new XdevDbSavepointAdapter(getConnection().setSavepoint(name),dataSource);
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
			getConnection().releaseSavepoint(new JavaSqlSavepointAdapter(savepoint));
		}
		catch(Exception e)
		{
			throw new DBException(dataSource,e);
		}
	}
	
	
	@Override
	public void commit() throws DBException
	{
		Connection connection = getConnection();
		try
		{
			connection.commit();
		}
		catch(Exception e)
		{
			throw new DBException(dataSource,e);
		}
		finally
		{
			try
			{
				connection.setAutoCommit(true);
			}
			catch(Exception e)
			{
				throw new DBException(dataSource,e);
			}
		}
	}
	
	
	@Override
	public void rollback() throws DBException
	{
		Connection connection = getConnection();
		try
		{
			connection.rollback();
		}
		catch(Exception e)
		{
			throw new DBException(dataSource,e);
		}
		finally
		{
			try
			{
				connection.setAutoCommit(true);
			}
			catch(Exception e)
			{
				throw new DBException(dataSource,e);
			}
		}
	}
	
	
	@Override
	public void rollback(Savepoint savepoint) throws DBException
	{
		if(savepoint == null)
		{
			rollback();
		}
		else
		{
			Connection connection = getConnection();
			try
			{
				connection.rollback(new JavaSqlSavepointAdapter(savepoint));
			}
			catch(Exception e)
			{
				throw new DBException(dataSource,e);
			}
			finally
			{
				try
				{
					connection.setAutoCommit(true);
				}
				catch(Exception e)
				{
					throw new DBException(dataSource,e);
				}
			}
		}
	}
	
	
	@Override
	public void close() throws DBException
	{
		try
		{
			if(connection != null)
			{
				connection.close();
				if(log.isDebugEnabled())
				{
					log.debug("JDBCConnection " + this.toString() + " closed the Connection "
							+ connection.toString());
				}
				connection = null;
			}
		}
		catch(Exception e)
		{
			throw new DBException(dataSource,e);
		}
	}
	
	
	/**
	 * TODO add foreignKeys to create table statement
	 */
	@Override
	public void createTable(String tableName, String primaryKey, Map<String, String> columnMap,
			boolean isAutoIncrement, Map<String, String> foreignKeys) throws Exception
	{
		if(!columnMap.containsKey(primaryKey))
		{
			columnMap.put(primaryKey,"INTEGER");
		}
		StringBuffer createStatement = null;
		
		if(isAutoIncrement)
		{
			createStatement = new StringBuffer("CREATE TABLE IF NOT EXISTS " + tableName + "("
					+ primaryKey + " " + columnMap.get(primaryKey) + " AUTO_INCREMENT,");
		}
		else
		{
			createStatement = new StringBuffer("CREATE TABLE IF NOT EXISTS " + tableName + "("
					+ primaryKey + " " + columnMap.get(primaryKey) + ",");
		}
		
		for(String keySet : columnMap.keySet())
		{
			if(!keySet.equals(primaryKey))
			{
				createStatement.append(keySet + " " + columnMap.get(keySet) + ",");
			}
		}
		
		createStatement.append(" PRIMARY KEY (" + primaryKey + "))");
		
		if(log.isDebugEnabled())
		{
			log.debug("SQL Statement to create a table: " + createStatement.toString()); //$NON-NLS-1$
		}
		
		Connection connection = getConnection();
		Statement statement = connection.createStatement();
		try
		{
			statement.execute(createStatement.toString());
		}
		catch(Exception e)
		{
			throw e;
		}
		finally
		{
			statement.close();
			connection.close();
		}
	}
	
	
	@Override
	public Date getServerTime() throws DBException, ParseException
	{
		String selectTime = "SELECT CURRENT_TIMESTAMP";
		return getServerTime(selectTime);
	}
	
	
	protected Date getServerTime(String sqlStatement) throws DBException, ParseException
	{
		DBUtils.setDataSource(dataSource);
		Object querySingleValue = DBUtils.querySingleValue(sqlStatement,(Object[])null);
		
		if(querySingleValue instanceof String)
		{
			
			String stringDate = (String)querySingleValue;
			SimpleDateFormat df = null;
			if(stringDate
					.matches("[0-9]{4}[-./][0-9]{2}[-./][0-9]{2} [0-9]{2}:[0-9]{2}:[0-9]{2}.*"))
			{
				df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
				
			}
			else if(stringDate
					.matches("[0-9]{2}[-./][0-9]{2}[-./][0-9]{4} [0-9]{2}:[0-9]{2}:[0-9]{2}.*"))
			{
				df = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss.SSS");
				
			}
			
			if(stringDate.matches(".*[0-9]{3}[0]{3}"))
			{
				// delete last 3 numbers if ms has 6 numbers
				stringDate = stringDate.substring(0,stringDate.length() - 3);
			}
			else if(stringDate.length() <= 19)
			{
				// add ms if there are missing
				stringDate = stringDate + ".000";
			}
			return df.parse(stringDate);
			
		}
		
		return (Date)querySingleValue;
	}
	
}
