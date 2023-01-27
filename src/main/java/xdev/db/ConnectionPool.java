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


import java.sql.Array;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.NClob;
import java.sql.PreparedStatement;
import java.sql.SQLClientInfoException;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Savepoint;
import java.sql.Statement;
import java.sql.Struct;
import java.util.Enumeration;
import java.util.Map;
import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;
import java.util.concurrent.Executor;

import xdev.io.IOUtils;
import xdev.util.logging.LoggerFactory;
import xdev.util.logging.XdevLogger;
import xdev.util.systemproperty.XdevSystemPropertyKeys;
import xdev.util.systemproperty.XdevSystemPropertyUtils;

import com.xdev.jadoth.sqlengine.dbms.DbmsAdaptor;
import com.xdev.jadoth.sqlengine.interfaces.ConnectionProvider;


/**
 * A pool including one or more <code>PooledConnection</code> objects. The
 * <code>ConnectionPool</code> provides methods to handle the
 * <code>PooledConnection</code>.
 * <p>
 * </p>
 * 
 * Use {@link XdevSystemPropertyKeys} to enable "tracing who is calling" with
 * XDEV_DB_TRACEWHOISCALLING, connection timeout with XDEV_DB_CONNECTIONTIMEOUT
 * and "stacktrace timeout delay" with XDEV_DB_STACKTRACETIMEOUTDELAY
 * 
 * @author XDEV Software (MP)
 */
public class ConnectionPool implements ConnectionProvider
{
	// default values
	private static final long			DEFAULT_CONNECTION_TIMEOUT			= 5 * 60 * 1000;							// 5min
	private static final long			DEFAULT_STACKTRACE_TIMEOUT_DELAY	= 10 * 1000;								// 10s
	private static final boolean		DEFAULT_CONNECTION_DEBUG_MODE		= false;
	
	/**
	 * Logger instance for this class.
	 */
	private static final XdevLogger		LOGGER								= LoggerFactory
																					.getLogger(ConnectionPool.class);
	
	private final int						size;
	private final boolean					keepConnectionsAlive;
	private final DbmsAdaptor			    adaptor;
	private final ConnectionInformation		info;
	private final Vector<PooledConnection>	pool;
	
	private final Timer						stacktraceTimer						= new Timer();
	
	private int							usedConnections						= 0;
	
	// over system properties configurable variables
	/**
	 * Stacktrace timeout in ms. After this time the connection will be closed
	 * and <code>traceWhoIsCalling == true</code> the stacktrace is set to
	 * leaseStack
	 */
	private boolean						isConnectionDebugMode;
	private long						stackTraceTimeOutDelay;
	private long						connectionTimeout;
	
	private final int                   maxStatementsPerConnection;
	
	
	/**
	 * Construct a new {@link ConnectionPool} that is initialize with the given
	 * parameter.
	 * 
	 * @param size
	 *            the limit of concurrent connections, a <code>size</code> <= 0
	 *            means no limit
	 * 
	 * @param keepConnectionsAlive
	 *            <code>true</code> to close the connection definitely or
	 *            <code>false</code> if the connection is no longer used
	 * 
	 * @param adaptor
	 *            the {@link DbmsAdaptor} for this {@link ConnectionPool}
	 * 
	 * @param info
	 *            the connection information
	 * 
	 */
	public ConnectionPool(int size, boolean keepConnectionsAlive, DbmsAdaptor adaptor,
			ConnectionInformation info, int maxStmtPerCon)
	{
		this.size = size;
		this.keepConnectionsAlive = keepConnectionsAlive;
		this.adaptor = adaptor;
		this.info = info;
		this.pool = new Vector<PooledConnection>();
		this.maxStatementsPerConnection = maxStmtPerCon;
		
		this.initConfigurableVariables();
		
	}
	
	
	private void initConfigurableVariables()
	{
		
		this.isConnectionDebugMode = XdevSystemPropertyUtils.getBooleanValue(
				XdevSystemPropertyKeys.CONNECTION_DEBUG_MODE,DEFAULT_CONNECTION_DEBUG_MODE);
		
		this.stackTraceTimeOutDelay = XdevSystemPropertyUtils.getLongValue(
				XdevSystemPropertyKeys.STACKTRACE_TIMEOUT_DELAY,DEFAULT_STACKTRACE_TIMEOUT_DELAY);
		this.connectionTimeout = XdevSystemPropertyUtils.getLongValue(
				XdevSystemPropertyKeys.CONNECTION_TIMEOUT,DEFAULT_CONNECTION_TIMEOUT);
	}
	
	
	private synchronized void reapConnections()
	{
		final long stale = System.currentTimeMillis() - this.connectionTimeout;
		final Enumeration<PooledConnection> connlist = this.pool.elements();
		while(connlist.hasMoreElements())
		{
			final PooledConnection conn = connlist.nextElement();
			
			if(conn.inuse && stale > conn.initTimestamp && !conn.validate())
			{
				this.usedConnections--;
				this.pool.removeElement(conn);
				conn.closeDefinite();
			}
		}
	}
	
	
	// /**
	// * Returns the first {@link Connection} of this {@link ConnectionPool}
	// that
	// * is not in use. If no {@link Connection} is available, a new
	// * {@link PooledConnection} is created and the method (
	// * {@link #getConnection()}) is called again.
	// *
	// * @return the {@link Connection}
	// */
	/**
	 * {@inheritDoc}
	 */
	@Override
	public synchronized Connection getConnection()
	{
		for(final PooledConnection connection : this.pool)
		{
			if(connection.lease() && connection.validate())
			{
				this.usedConnections++;
				
				if(LOGGER.isDebugEnabled())
				{
					LOGGER.debug("Took connection from pool: " + connection.toString() + " "
							+ this.getPoolConnectionStats());
				}
				
				return connection;
			}
		}
		
		this.reapConnections();
		
		if(this.size <= 0 || this.pool.size() < this.size)
		{
			final PooledConnection connection = new PooledConnection(this.info.createConnection(), this.maxStatementsPerConnection);
			connection.lease();
			this.pool.add(connection);
			this.usedConnections++;
			
			if(LOGGER.isDebugEnabled())
			{				
				LOGGER.debug("Took connection from pool: " + connection.toString() + " "
						+ this.getPoolConnectionStats());
			}
			return connection;
		}
		
		try
		{
			Thread.sleep(1000);
		}
		catch(final InterruptedException e)
		{
		}
		
		return this.getConnection();
	}
	
	
	private String getPoolConnectionStats()
	{
		return "Poolsize / used: " + this.pool.size() + " / " + this.usedConnections;
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public DbmsAdaptor getDbmsAdaptor()
	{
		return this.adaptor;
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public synchronized void closeConnection(Connection connection)
	{
		try
		{
			connection.close();
		}
		catch(final Exception e)
		{
		}
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public synchronized void close()
	{
		for(final PooledConnection connection : this.pool)
		{
			connection.closeDefinite();
		}
		this.pool.clear();
		this.usedConnections = 0;
	}
	
	
	/**
	 * @since 3.1
	 */
	public void dumpActiveConnectionStacks()
	{
		for(final PooledConnection connection : this.pool)
		{
			if(connection.inuse)
			{
				LOGGER.info(connection.toString());
				LOGGER.info(connection.leaseStack);
			}
		}
	}
	
	
	
	private class PooledConnection implements ConnectionWrapper
	{
		private static final int UNLIMITED_STATEMENTS_PER_CONNECTION = 0;
		Connection			connection;
		boolean				inuse;
		long				initTimestamp;
		String				leaseStack;
		StacktraceTimeOut	stacktraceTimeOut;
		int                 maxStatementsPerConnection = UNLIMITED_STATEMENTS_PER_CONNECTION;
		
		int                 statementCount = 0;
		
		PooledConnection(Connection connection, int maxStatementsPerCon) 
		{
			this.init(connection);			
			this.maxStatementsPerConnection = maxStatementsPerCon;
		}
		
		private void init(final Connection connection)
		{
			this.connection = connection;
			this.inuse = false;
			this.initTimestamp = 0;
			this.leaseStack = "set system property " + XdevSystemPropertyKeys.CONNECTION_DEBUG_MODE
					+ " to get lease stack";
		}
		
		@Override
		public String toString()
		{
			return super.toString() + " [in use = " + this.inuse + "]";
		}
		
		
		@Override
		public Connection getActualConnection()
		{
			return this.connection;
		}
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		public Statement createStatement() throws SQLException
		{
			this.statementCount++;
			return this.connection.createStatement();
		}
		
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		public Statement createStatement(int resultSetType, int resultSetConcurrency)
				throws SQLException
		{
			this.statementCount++;
			return this.connection.createStatement(resultSetType,resultSetConcurrency);
		}
		
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		public Statement createStatement(int resultSetType, int resultSetConcurrency,
				int resultSetHoldability) throws SQLException
		{
			this.statementCount++;
			return this.connection.createStatement(resultSetType,resultSetConcurrency,
					resultSetHoldability);
		}	
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		public CallableStatement prepareCall(String sql) throws SQLException
		{
			this.statementCount++;
			return this.connection.prepareCall(sql);
		}
		
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency)
				throws SQLException
		{
			this.statementCount++;
			return this.connection.prepareCall(sql,resultSetType,resultSetConcurrency);
		}
		
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		public CallableStatement prepareCall(String sql, int resultSetType,
				int resultSetConcurrency, int resultSetHoldability) throws SQLException
		{
			this.statementCount++;
			return this.connection.prepareCall(sql,resultSetType,resultSetConcurrency,
					resultSetHoldability);
		}
		
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		public PreparedStatement prepareStatement(String sql) throws SQLException
		{
			this.statementCount++;
			return this.connection.prepareStatement(sql);
		}
		
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		public PreparedStatement prepareStatement(String sql, int autoGeneratedKeys)
				throws SQLException
		{
			this.statementCount++;
			return this.connection.prepareStatement(sql,autoGeneratedKeys);
		}
		
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		public PreparedStatement prepareStatement(String sql, int[] columnIndexes)
				throws SQLException
		{
			this.statementCount++;
			return this.connection.prepareStatement(sql,columnIndexes);
		}
		
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		public PreparedStatement prepareStatement(String sql, String[] columnNames)
				throws SQLException
		{
			this.statementCount++;
			return this.connection.prepareStatement(sql,columnNames);
		}
		
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		public PreparedStatement prepareStatement(String sql, int resultSetType,
				int resultSetConcurrency) throws SQLException
		{
			this.statementCount++;
			return this.connection.prepareStatement(sql,resultSetType,resultSetConcurrency);
		}
		
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		public PreparedStatement prepareStatement(String sql, int resultSetType,
				int resultSetConcurrency, int resultSetHoldability) throws SQLException
		{
			this.statementCount++;
			return this.connection.prepareStatement(sql,resultSetType,resultSetConcurrency,
					resultSetHoldability);
		}
		
		synchronized boolean lease()
		{		
			if(this.inuse)
			{
				return false;
			}
			else
			{
				this.inuse = true;
				this.initTimestamp = System.currentTimeMillis();
				
				if(ConnectionPool.this.isConnectionDebugMode)
				{
					final StringBuilder sb = new StringBuilder();
					
					for(final StackTraceElement elem : new Throwable().getStackTrace())
					{
						if(sb.length() > 0)
						{
							sb.append(IOUtils.LINE_SEPARATOR);
						}
						sb.append(elem.toString());
					}
					this.leaseStack = sb.toString();
					
					this.stacktraceTimeOut = new StacktraceTimeOut(LOGGER,this);
					ConnectionPool.this.stacktraceTimer.schedule(this.stacktraceTimeOut,ConnectionPool.this.stackTraceTimeOutDelay);
				}				
				
				return true;
			}
		}
		
		
		boolean validate()
		{
			if(this.maxStatementsPerConnection > UNLIMITED_STATEMENTS_PER_CONNECTION && this.statementCount > this.maxStatementsPerConnection) {				
				return false;
			} else {
				return ConnectionPool.this.info.isConnectionValid(this.connection);
			}
		}
		
		
		@Override
		public void close()
		{
			this.inuse = false;
			
			if(this.stacktraceTimeOut != null)
			{
				this.stacktraceTimeOut.cancel();
			}
			
			ConnectionPool.this.usedConnections--;
			
			if(!ConnectionPool.this.keepConnectionsAlive)
			{
				this.closeDefinite();
			}
		}
		
		
		void closeDefinite()
		{
			this.inuse = false;
			
			try
			{
				this.connection.close();
			}
			catch(final Exception e)
			{
			}
		}
		
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		public void clearWarnings() throws SQLException
		{
			this.connection.clearWarnings();
		}
		
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		public void commit() throws SQLException
		{
			this.connection.commit();
		}
		
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		public Array createArrayOf(String typeName, Object[] elements) throws SQLException
		{
			return this.connection.createArrayOf(typeName,elements);
		}
		
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		public Blob createBlob() throws SQLException
		{
			return this.connection.createBlob();
		}
		
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		public Clob createClob() throws SQLException
		{
			return this.connection.createClob();
		}
		
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		public NClob createNClob() throws SQLException
		{
			return this.connection.createNClob();
		}
		
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		public SQLXML createSQLXML() throws SQLException
		{
			return this.connection.createSQLXML();
		}
		
		
		
		
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		public Struct createStruct(String typeName, Object[] attributes) throws SQLException
		{
			return this.connection.createStruct(typeName,attributes);
		}
		
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		public boolean getAutoCommit() throws SQLException
		{
			return this.connection.getAutoCommit();
		}
		
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		public String getCatalog() throws SQLException
		{
			return this.connection.getCatalog();
		}
		
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		public Properties getClientInfo() throws SQLException
		{
			return this.connection.getClientInfo();
		}
		
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		public String getClientInfo(String name) throws SQLException
		{
			return this.connection.getClientInfo(name);
		}
		
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		public int getHoldability() throws SQLException
		{
			return this.connection.getHoldability();
		}
		
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		public DatabaseMetaData getMetaData() throws SQLException
		{
			return this.connection.getMetaData();
		}
		
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		public int getTransactionIsolation() throws SQLException
		{
			return this.connection.getTransactionIsolation();
		}
		
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		public Map<String, Class<?>> getTypeMap() throws SQLException
		{
			return this.connection.getTypeMap();
		}
		
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		public SQLWarning getWarnings() throws SQLException
		{
			return this.connection.getWarnings();
		}
		
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		public boolean isClosed() throws SQLException
		{
			return this.connection.isClosed();
		}
		
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		public boolean isReadOnly() throws SQLException
		{
			return this.connection.isReadOnly();
		}
		
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		public boolean isValid(int timeout) throws SQLException
		{
			return this.connection.isValid(timeout);
		}
		
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		public String nativeSQL(String sql) throws SQLException
		{
			return this.connection.nativeSQL(sql);
		}
		
		
		
		
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		public void releaseSavepoint(Savepoint savepoint) throws SQLException
		{
			this.connection.releaseSavepoint(savepoint);
		}
		
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		public void rollback() throws SQLException
		{
			this.connection.rollback();
		}
		
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		public void rollback(Savepoint savepoint) throws SQLException
		{
			this.connection.rollback(savepoint);
		}
		
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		public void setAutoCommit(boolean autoCommit) throws SQLException
		{
			this.connection.setAutoCommit(autoCommit);
		}
		
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		public void setCatalog(String catalog) throws SQLException
		{
			this.connection.setCatalog(catalog);
		}
		
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		public void setClientInfo(Properties properties) throws SQLClientInfoException
		{
			this.connection.setClientInfo(properties);
		}
		
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		public void setClientInfo(String name, String value) throws SQLClientInfoException
		{
			this.connection.setClientInfo(name,value);
		}
		
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		public void setHoldability(int holdability) throws SQLException
		{
			this.connection.setHoldability(holdability);
		}
		
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		public void setReadOnly(boolean readOnly) throws SQLException
		{
			this.connection.setReadOnly(readOnly);
		}
		
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		public Savepoint setSavepoint() throws SQLException
		{
			return this.connection.setSavepoint();
		}
		
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		public Savepoint setSavepoint(String name) throws SQLException
		{
			return this.connection.setSavepoint(name);
		}
		
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		public void setTransactionIsolation(int level) throws SQLException
		{
			this.connection.setTransactionIsolation(level);
		}
		
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		public void setTypeMap(Map<String, Class<?>> map) throws SQLException
		{
			this.connection.setTypeMap(map);
		}
		
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		public boolean isWrapperFor(Class<?> iface) throws SQLException
		{
			return this.connection.isWrapperFor(iface);
		}
		
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		public <T> T unwrap(Class<T> iface) throws SQLException
		{
			return this.connection.unwrap(iface);
		}
		
		
		// Java 7 changes
		public void setSchema(String schema) throws SQLException
		{
			try
			{
				this.connection.getClass().getMethod("setSchema",String.class).invoke(this.connection,schema);
			}
			catch(final Exception e)
			{
				throw new SQLException(e);
			}
			
			// connection.setSchema(schema);
		}
		
		
		public String getSchema() throws SQLException
		{
			try
			{
				return (String)this.connection.getClass().getMethod("getSchema").invoke(this.connection);
			}
			catch(final Exception e)
			{
				throw new SQLException(e);
			}
			
			// return connection.getSchema();
		}
		
		
		public void abort(Executor executor) throws SQLException
		{
			try
			{
				this.connection.getClass().getMethod("abort",Executor.class).invoke(this.connection,executor);
			}
			catch(final Exception e)
			{
				throw new SQLException(e);
			}
			
			// connection.abort(executor);
		}
		
		
		public void setNetworkTimeout(Executor executor, int milliseconds) throws SQLException
		{
			try
			{
				this.connection.getClass().getMethod("setNetworkTimeout",Executor.class,int.class)
						.invoke(this.connection,executor,milliseconds);
			}
			catch(final Exception e)
			{
				throw new SQLException(e);
			}
			
			// connection.setNetworkTimeout(executor,milliseconds);
		}
		
		
		public int getNetworkTimeout() throws SQLException
		{
			try
			{
				return (Integer)this.connection.getClass().getMethod("getNetworkTimeout")
						.invoke(this.connection);
			}
			catch(final Exception e)
			{
				throw new SQLException(e);
			}
			
			// return connection.getNetworkTimeout();
		}
	}
	
	
	
	private class StacktraceTimeOut extends TimerTask
	{
		
		final XdevLogger		logger;
		final PooledConnection	connection;
		
		
		public StacktraceTimeOut(final XdevLogger logger, final PooledConnection connection)
		{
			this.logger = logger;
			this.connection = connection;
		}
		
		
		@Override
		public void run()
		{
			this.logger.debug(this.connection.leaseStack);
		}
		
	}
	
}
