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

package com.xdev.jadoth.sqlengine.dbms;


import java.sql.Connection;
import java.sql.Driver;
import java.util.Properties;

import com.xdev.jadoth.sqlengine.exceptions.SQLEngineCouldNotConnectToDBException;



/**
 * The Interface DbmsConnectionInformation.
 * 
 * @param <A>
 *            the generic type
 */
public interface DbmsConnectionInformation<A extends DbmsAdaptor<A>> extends DbmsAdaptor.Member<A>
{
	
	/**
	 * Gets the host.
	 * 
	 * @return the host
	 */
	public String getHost();
	
	
	/**
	 * Gets the port.
	 * 
	 * @return the port
	 */
	public int getPort();
	
	
	/**
	 * Gets the user.
	 * 
	 * @return the user
	 */
	public String getUser();
	
	
	/**
	 * Gets the password.
	 * 
	 * @return the password
	 */
	public String getPassword();
	
	
	/**
	 * Gets the catalog.
	 * 
	 * @return the catalog
	 */
	public String getCatalog();
	
	
	/**
	 * Creates the jdbc connection url.
	 * 
	 * @return the string
	 */
	public String createJdbcConnectionUrl();
	
	
	/**
	 * Gets the jdbc driver class name.
	 * 
	 * @return the jdbc driver class name
	 */
	public String getJdbcDriverClassName();
	
	
	/**
	 * Creates the connection.
	 * 
	 * @return the connection
	 * @throws SQLEngineCouldNotConnectToDBException
	 *             the sQL engine could not connect to db exception
	 */
	public Connection createConnection() throws SQLEngineCouldNotConnectToDBException;
	
	
	
	/**
	 * The Class AbstractBody.
	 * 
	 * @param <A>
	 *            the generic type
	 */
	public abstract class Implementation<A extends DbmsAdaptor<A>> extends
			DbmsAdaptor.Member.Implementation<A> implements DbmsConnectionInformation<A>
	{
		// /////////////////////////////////////////////////////////////////////////
		// static methods //
		// /////////////////
		
		private static Properties createAuthProperties(final String user, final String password)
		{
			final Properties properties = new Properties();
			if(user != null)
			{
				properties.put("user",user);
			}
			if(password != null)
			{
				properties.put("password",password);
			}
			return properties;
		}
		
		// /////////////////////////////////////////////////////////////////////////
		// instance fields //
		// //////////////////
		
		/** The host. */
		private String	host;
		
		/** The port. */
		private int		port;
		
		/** The user. */
		private String	user;
		
		/** The password. */
		private String	password;
		
		/** The catalog. */
		private String	catalog;
		
		/** The url extension. */
		private String	urlExtension;
		
		
		// /////////////////////////////////////////////////////////////////////////
		// constructors //
		// ///////////////
		/**
		 * Instantiates a new abstract body.
		 * 
		 * @param host
		 *            the host
		 * @param port
		 *            the port
		 * @param user
		 *            the user
		 * @param password
		 *            the password
		 * @param catalog
		 *            the catalog
		 * @param urlExtension
		 *            extended url properties
		 * @param dbmsAdaptor
		 *            the dbms adaptor
		 */
		protected Implementation(final String host, final int port, final String user,
				final String password, final String catalog, String urlExtension,
				final A dbmsAdaptor)
		{
			super(dbmsAdaptor);
			this.host = host;
			this.port = port;
			this.user = user;
			this.password = password;
			this.catalog = catalog;
			this.urlExtension = urlExtension;
		}
		
		
		// /////////////////////////////////////////////////////////////////////////
		// getters //
		// ///////////////////
		
		/**
		 * Gets the host.
		 * 
		 * @return the host
		 */
		public String getHost()
		{
			return host;
		}
		
		
		/**
		 * Gets the port.
		 * 
		 * @return the port
		 */
		public int getPort()
		{
			return port;
		}
		
		
		/**
		 * Gets the user.
		 * 
		 * @return the user
		 */
		public String getUser()
		{
			return user;
		}
		
		
		/**
		 * Gets the password.
		 * 
		 * @return the password
		 */
		public String getPassword()
		{
			return password;
		}
		
		
		/**
		 * Gets the catalog.
		 * 
		 * @return the catalog
		 */
		public String getCatalog()
		{
			return catalog;
		}
		
		
		public String getUrlExtension()
		{
			return urlExtension;
		}
		
		
		// /////////////////////////////////////////////////////////////////////////
		// setters //
		// ///////////////////
		
		/**
		 * Sets the host.
		 * 
		 * @param host
		 *            the host to set
		 */
		public void setHost(final String host)
		{
			this.host = host;
		}
		
		
		/**
		 * Sets the port.
		 * 
		 * @param port
		 *            the port to set
		 */
		public void setPort(final int port)
		{
			this.port = port;
		}
		
		
		/**
		 * Sets the user.
		 * 
		 * @param user
		 *            the user to set
		 */
		public void setUser(final String user)
		{
			this.user = user;
		}
		
		
		/**
		 * Sets the password.
		 * 
		 * @param password
		 *            the password to set
		 */
		public void setPassword(final String password)
		{
			this.password = password;
		}
		
		
		/**
		 * Sets the catalog.
		 * 
		 * @param catalog
		 *            the catalog to set
		 */
		public void setCatalog(final String catalog)
		{
			this.catalog = catalog;
		}
		
		
		public void setUrlExtension(String urlExtension)
		{
			this.urlExtension = urlExtension;
		}
		
		
		// /////////////////////////////////////////////////////////////////////////
		// override methods //
		// ///////////////////
		
		/**
		 * @return
		 * @throws SQLEngineCouldNotConnectToDBException
		 * @see com.xdev.jadoth.sqlengine.dbms.DbmsConnectionInformation#createConnection(java.lang.String)
		 */
		@Override
		public Connection createConnection() throws SQLEngineCouldNotConnectToDBException
		{
			try
			{
				final ClassLoader classLoader = this.getClass().getClassLoader();
				final Driver driver = (Driver)classLoader.loadClass(getJdbcDriverClassName())
						.newInstance();
				
				return driver.connect(createJdbcConnectionUrl(),createConnectionProperties());
			}
			catch(final Exception e)
			{
				throw new SQLEngineCouldNotConnectToDBException(e);
			}
		}
		
		
		protected Properties createConnectionProperties()
		{
			return createAuthProperties(this.getUser(),this.getPassword());
		}
		
		
		/**
		 * @return [host]:[port]>[user]//[password] ::[catalog]
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString()
		{
			return this.host + ":" + this.port + ">" + this.user + "//" + this.password + " ::"
					+ this.catalog;
		}
		
	}
	
}
