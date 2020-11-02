

package com.xdev.jadoth.sqlengine.interfaces;

/*-
 * #%L
 * XDEV Application Framework
 * %%
 * Copyright (C) 2003 - 2020 XDEV Software
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Lesser Public License for more details.
 * 
 * You should have received a copy of the GNU General Lesser Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/lgpl-3.0.html>.
 * #L%
 */


import java.sql.Connection;
import java.sql.SQLException;

import com.xdev.jadoth.sqlengine.dbms.DbmsAdaptor;
import com.xdev.jadoth.sqlengine.dbms.DbmsConnectionInformation;
import com.xdev.jadoth.sqlengine.exceptions.SQLEngineException;



/**
 * A connection provider manages JDBC connections and returns an appropriate
 * connection instance.<br>
 * In the trivial case, this can be always the same connection instance.<br>
 * Other implementations might apply more sophisticated connection management
 * like connection pooling, return connections on a per-thread basis and/or take
 * the application context into account for making the decision which connection
 * to provide.
 * <p>
 * For every connection, there is also provided an apropriate DbmsAdaptor
 * instance for that.<br>
 * This may be always the same DbmsAdaptor instance if all managed connections
 * connect to the same DBMS type or, depending on implementation, may as well be
 * a different DbmsAdaptor instance depending on the used connection.
 * 
 * @param <A>
 *            the generic type
 * @author Thomas Muenz
 */
public interface ConnectionProvider<A extends DbmsAdaptor<A>>
{
	
	/**
	 * Releases all resources held by this connection provider instance.
	 */
	public void close();
	
	
	/**
	 * Closes a specific connection used by this connection provider instance.
	 * 
	 * @param connection
	 *            the connection instance to be closed
	 */
	public void closeConnection(Connection connection);
	
	
	/**
	 * Returns a connection instance as this connection provider sees fit.
	 * 
	 * @return the connection
	 */
	public Connection getConnection();
	
	
	/**
	 * Gets the dbms adaptor.
	 * 
	 * @return the apropriate DbmsAdaptor instance for all provided connections
	 */
	public A getDbmsAdaptor();
	
	
	
	/**
	 * Default implementation of <code>ConnectionProvider</code> that
	 * encapsulates a single JDBC connection instance. Thus, the connection
	 * instance provided by this connection provider is always the same.
	 * 
	 * @param <A>
	 *            the generic type
	 * @author Thomas Muenz
	 */
	public class Body<A extends DbmsAdaptor<A>> implements ConnectionProvider<A>
	{
		// /////////////////////////////////////////////////////////////////////////
		// instance fields //
		// //////////////////
		
		/** The dbms connection information. */
		private DbmsConnectionInformation<A>	dbmsConnectionInformation;
		
		/** The connection. */
		private Connection						connection;
		
		/** The dbms adaptor. */
		private A								dbmsAdaptor;
		
		
		// /////////////////////////////////////////////////////////////////////////
		// constructors //
		// ///////////////
		
		/**
		 * Instantiates a new body.
		 * 
		 * @param conInfo
		 *            the con info
		 * @param jdbcUrlExtension
		 *            the jdbc url extension
		 */
		public Body(final DbmsConnectionInformation<A> conInfo)
		{
			super();
			this.dbmsConnectionInformation = conInfo;
			this.dbmsAdaptor = conInfo.getDbmsAdaptor();
			this.connection = conInfo.createConnection();
		}
		
		
		// /////////////////////////////////////////////////////////////////////////
		// getters //
		// ///////////////////
		
		/**
		 * Returns the encapsulated JDBC connection instance.
		 * 
		 * @return the connection
		 */
		@Override
		public Connection getConnection()
		{
			return this.connection;
		}
		
		
		/**
		 * Gets the dbms adaptor.
		 * 
		 * @return the dbmsAdaptor
		 */
		@Override
		public A getDbmsAdaptor()
		{
			return this.dbmsAdaptor;
		}
		
		
		/**
		 * Gets the dbms connection information.
		 * 
		 * @return the dbmsConnectionInformation that provided the JDBC
		 *         Connection and DbmsAdaptor instances
		 */
		public DbmsConnectionInformation<A> getDbmsConnectionInformation()
		{
			return dbmsConnectionInformation;
		}
		
		
		// /////////////////////////////////////////////////////////////////////////
		// override methods //
		// ///////////////////
		
		/**
		 * Closes the encapsulated connection.
		 */
		@Override
		public void close()
		{
			if(this.connection == null)
				return;
			try
			{
				this.connection.close();
			}
			catch(SQLException e)
			{
				throw new SQLEngineException(e);
			}
		}
		
		
		/**
		 * Calls close() if <code>connection</code> is the encapsulated
		 * connection. Does nothing otherwise.
		 * 
		 * @param connection
		 *            the connection
		 */
		@Override
		public void closeConnection(final Connection connection)
		{
			if(this.connection != connection)
				return;
			this.close();
		}
		
	}
	
}
