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

package com.xdev.jadoth.sqlengine.internal;

import static com.xdev.jadoth.sqlengine.SQL.Punctuation.NEW_LINE;
import static com.xdev.jadoth.util.logging.jul.LoggingAspect.log;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import com.xdev.jadoth.sqlengine.SQL;
import com.xdev.jadoth.sqlengine.dbms.DbmsAdaptor;
import com.xdev.jadoth.sqlengine.exceptions.SQLEngineCouldNotConnectToDBException;
import com.xdev.jadoth.sqlengine.exceptions.SQLEngineException;
import com.xdev.jadoth.sqlengine.interfaces.ConnectionProvider;
import com.xdev.jadoth.sqlengine.interfaces.QueryEventSQLException;
import com.xdev.jadoth.sqlengine.interfaces.QueryListenerPostExecution;
import com.xdev.jadoth.sqlengine.interfaces.QueryListenerPreAssembly;
import com.xdev.jadoth.sqlengine.interfaces.QueryListenerPreExecution;
import com.xdev.jadoth.sqlengine.interfaces.QueryListenerSQLException;
import com.xdev.jadoth.sqlengine.interfaces.SqlExecutor;
import com.xdev.jadoth.sqlengine.internal.tables.SqlIndex;
import com.xdev.jadoth.sqlengine.types.Query;
import com.xdev.jadoth.util.logging.jul.LoggingContext;




/**
 * This class handles access to one concrete database instance.<br>
 * It holds the DdmsAdaptor and ConnectionProvider instances for use with query objects that shall be executed,
 * as well as some common execution logic itself.
 * 
 * @param <A> the generic type
 * @author Thomas Muenz
 */
public class DatabaseGateway<A extends DbmsAdaptor<A>>
{

	/** The connection provider. */
	protected ConnectionProvider<A> connectionProvider;
	
	/** The dbms adaptor. */
	protected A dbmsAdaptor;



	/** The validate table columns. */
	public final boolean validateTableColumns = false;
	
	/** The index validation enabled. */
	public final boolean indexValidationEnabled = false;
	
	/** The index validation_create missing indices. */
	public final boolean indexValidation_createMissingIndices = false;
	
	/** The index validation_drop undefined indices. */
	public final boolean indexValidation_dropUndefinedIndices = false;


	//DB Connection
	/** The connection enabled. */
	protected boolean connectionEnabled = false;



	/**
	 * Instantiates a new database gateway.
	 * 
	 * @param connectionProvider the connection provider
	 */
	public DatabaseGateway(final ConnectionProvider<A> connectionProvider) {
		super();
		this.connectionProvider = connectionProvider;
		this.dbmsAdaptor = connectionProvider.getDbmsAdaptor();
		this.dbmsAdaptor.setDatabaseGateway(this);
		this.connectionEnabled = true;
	}



	/**
	 * Close.
	 * 
	 * @throws SQLEngineException the sQL engine exception
	 */
	public void close() throws SQLEngineException{
		this.connectionProvider.close();
	}


	/**
	 * Gets the dbms adaptor.
	 * 
	 * @return the dbms adaptor
	 */
	public A getDbmsAdaptor() {
		return this.dbmsAdaptor;
	}


	/**
	 * Connect.
	 * 
	 * @return the database gateway
	 * @throws SQLEngineCouldNotConnectToDBException the sQL engine could not connect to db exception
	 */
	public DatabaseGateway<A> connect() throws SQLEngineCouldNotConnectToDBException
	{
		if(this.connectionEnabled) {
			this.dbmsAdaptor.initialize(this);
		}
		return this;
	}

	/**
	 * Gets the connection.
	 * 
	 * @return the connection
	 */
	public Connection getConnection() {
		return this.connectionProvider.getConnection();
	}


	public static final <A extends DbmsAdaptor<A>, R> R execute(
		final DatabaseGateway<A> databaseGateway,
		final Query query,
		final LoggingContext loggingContext,
		final SqlExecutor<R> executor,
		final Object... parameters
	)
	{				
		final QueryListeners localListeners = query.getListeners();
		final QueryListeners globalListeners = SQL.getGlobalQueryListeners();		
		final QueryEvent<R> event = new QueryEvent<R>(databaseGateway, query, parameters);

		
		String assembledQuery = null;		
		Statement statement = null;
		try {
			final Connection connection = databaseGateway.getConnection();
			statement = connection.createStatement();
			event.statement = statement;
		}
		catch(SQLException e){
			if(localListeners != null){
				final QueryListenerSQLException exEx = localListeners.getExecutionExceptionListeners();
				if(exEx != null){
					exEx.invoke(event);
				}
			}
			final QueryListenerSQLException exEx = globalListeners.getExecutionExceptionListeners();
			if(exEx != null){
				exEx.invoke(event);
			}
		}
		
		if(localListeners != null){
			final QueryListenerPreAssembly preAs = localListeners.getPreAssemblyListeners();
			if(preAs != null){
				preAs.invoke(event);
			}
		}
		final QueryListenerPreAssembly preAs = globalListeners.getPreAssemblyListeners();
		if(preAs != null){
			preAs.invoke(event);
		}
		
		//assembly
		assembledQuery = query.assemble(parameters);
		event.assembledQuery = assembledQuery;
					
		if(localListeners != null){
			final QueryListenerPreExecution preEx = localListeners.getPreExecutionListeners();
			if(preEx != null){
				preEx.invoke(event);
			}
		}
		final QueryListenerPreExecution preEx = globalListeners.getPreExecutionListeners();
		if(preEx != null){
			preEx.invoke(event);
		}						
		
		//logging
		log(loggingContext, assembledQuery);
		
		//execution
//		return execute(databaseGateway, statement, query, assembledQuery, executor, parameters);
		return execute(executor, event, localListeners);
	}
	
	private static final <R> R execute(
//		final DatabaseGateway<A> databaseGateway,
//		Statement statement,
//		final Query query,
//		final String assembledQuery,
		final SqlExecutor<R> executor,
//		final Object... parameters
		final QueryEvent<R> event,
		final QueryListeners localListeners
	)
	{		
//		final QueryListeners localListeners = query.getListeners();
		final QueryListeners globalListeners = SQL.getGlobalQueryListeners();
		Statement statement = event.statement;
		
		R result = null; 
		try {	
			if(statement == null){
				final Connection connection = event.dbc.getConnection();
				statement = connection.createStatement();
				event.statement = statement;
			}
						
			if(localListeners != null){
				final QueryListenerPreExecution preEx = localListeners.getPreExecutionListeners();
				if(preEx != null){
					preEx.invoke(event);
				}
			}
			final QueryListenerPreExecution preEx = globalListeners.getPreExecutionListeners();
			if(preEx != null){
				preEx.invoke(event);
			}						
				
			//execution
			result = executor.execute(event.dbc, statement, event.assembledQuery);
			event.resultValue = result;
			
			if(localListeners != null){
				final QueryListenerPostExecution postEx = localListeners.getPostExecutionListeners();
				if(postEx != null){
					postEx.invoke(event);
				}
			}
			final QueryListenerPostExecution postEx = globalListeners.getPostExecutionListeners();
			if(postEx != null){
				postEx.invoke(event);
			}			
			return result;
		}
		catch (final SQLException e) {
			if(localListeners != null){
				final QueryListenerSQLException exEx = localListeners.getExecutionExceptionListeners();
				if(exEx != null){
					exEx.invoke(event);
				}
			}
			final QueryListenerSQLException exEx = globalListeners.getExecutionExceptionListeners();
			if(exEx != null){
				exEx.invoke(event);
			}
			
			if(statement != null){
				try {
					statement.close();
				}
				catch(final SQLException e1) {
					throw new SQLEngineException(e1);
				}
			}
			throw event.dbc.getDbmsAdaptor().parseSqlException(e);
		}
	}
	
	
	public <R> R execute(final SqlExecutor<R> executor, final String sql) throws SQLEngineException
	{
		return execute(executor, new QueryEvent<R>(this, sql), null);
	}



	/**
	 * Drop index.
	 * 
	 * @param index the index
	 * @throws SQLEngineException the sQL engine exception
	 */
	public void dropIndex(final SqlIndex index) throws SQLEngineException
	{
		this.execute(SqlExecutor.update, index.DROP_INDEX());
	}

	/**
	 * Checks if is connection enabled.
	 * 
	 * @return the connectionEnabled
	 */
	public boolean isConnectionEnabled() 
	{
		return this.connectionEnabled;
	}
	
	/**
	 * Sets the connection enabled.
	 * 
	 * @param connectionEnabled the connectionEnabled to set
	 */
	public void setConnectionEnabled(final boolean connectionEnabled) {
		this.connectionEnabled = connectionEnabled;
	}



	/**
	 * @return
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() 
	{
		return this.dbmsAdaptor.toString()+ NEW_LINE+this.connectionProvider.toString();
	}
	
	
	private static final class QueryEvent<R> implements QueryEventSQLException //and all others by hierarchy
	{
		///////////////////////////////////////////////////////////////////////////
		// instance fields //
		////////////////////
		
		private DatabaseGateway<?> dbc = null;
		private Statement statement = null;
		private Query query = null;
		private String assembledQuery = null;
		private R resultValue = null;
		private SQLException cause = null;
		private Object[] assembleParameters = null;
		
		
		
		
		///////////////////////////////////////////////////////////////////////////
		// constructors //
		/////////////////
		
		/**
		 * @param dbc
		 * @param statement
		 * @param query
		 * @param assembleParameters
		 */
		private QueryEvent(
			final DatabaseGateway<?> dbc, 
			final Query query, 
			final Object[] assembleParameters
		)
		{
			super();
			this.dbc = dbc;
			this.query = query;			
			this.assembleParameters = assembleParameters;
		}
		
		private QueryEvent(
			final DatabaseGateway<?> dbc, 
			final String assembledQuery
		)
		{
			super();
			this.dbc = dbc;
			this.assembledQuery = assembledQuery;
		}
		
		
		
		///////////////////////////////////////////////////////////////////////////
		// getters          //
		/////////////////////		
		
		/**
		 * @return
		 */
		@Override
		public SQLException getSQLException()
		{
			return this.cause;
		}
		/**
		 * @return
		 */
		@Override
		public Object getResultValue()
		{
			return this.resultValue;
		}
		/**
		 * @return
		 */
		@Override
		public String getAssembledQuery()
		{
			return this.assembledQuery;
		}
		/**
		 * @return
		 */
		@Override
		public Object[] getAssemblyParameters()
		{
			return this.assembleParameters;
		}
		/**
		 * @return
		 */
		@Override
		public DatabaseGateway<?> getDatabaseGateway()
		{
			return this.dbc;
		}
		/**
		 * @return
		 */
		@Override
		public Query getQuery()
		{
			return this.query;
		}
		/**
		 * @return
		 */
		@Override
		public Statement getStatement()
		{
			return this.statement;
		}
		
		
	}

}
