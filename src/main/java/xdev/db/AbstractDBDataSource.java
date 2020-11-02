package xdev.db;

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


import java.util.List;
import java.util.Random;
import java.util.Vector;

import xdev.db.event.DBDataSourceEvent;
import xdev.db.event.DBDataSourceListener;
import xdev.util.AbstractDataSource;
import xdev.util.auth.Password;


/**
 * Abstract base class for all {@link DBDataSource}s including the handling of
 * common {@link xdev.util.DataSource.Parameter}s and
 * {@link DBDataSourceListener}s.
 * 
 * @see DBDataSource
 * @see AbstractDataSource
 * 
 * @author XDEV Software
 * 
 */
public abstract class AbstractDBDataSource<T extends DBDataSource<T>> extends AbstractDataSource<T>
		implements DBDataSource<T>
{
	/**
	 * @since 3.1
	 */
	public final static Parameter<Boolean>	EMBEDDED;
	public final static Parameter<String>	HOST;
	public final static Parameter<Integer>	PORT;
	public final static Parameter<String>	USERNAME;
	public final static Parameter<Password>	PASSWORD;
	public final static Parameter<String>	CATALOG;
	public final static Parameter<String>	SCHEMA;
	/**
	 * @since 4.0
	 */
	public final static Parameter<String>	URL_EXTENSION;
	public final static Parameter<Integer>	CONNECTION_POOL_SIZE;
	public final static Parameter<Boolean>	KEEP_CONNECTIONS_ALIVE;
	public final static Parameter<Boolean>	IS_SERVER_DATASOURCE;
	public final static Parameter<String>	SERVER_URL;
	public final static Parameter<String>	AUTH_KEY;
	public final static Parameter<Boolean>	VERBOSE;
	
	static
	{
		EMBEDDED = new Parameter("embedded",false);
		HOST = new Parameter("host","localhost");
		PORT = new Parameter("port",0);
		USERNAME = new Parameter("username","");
		PASSWORD = new Parameter("password",new Password(""));
		CATALOG = new Parameter("catalog","");
		SCHEMA = new Parameter("schema","");
		URL_EXTENSION = new Parameter("urlExtension","");
		CONNECTION_POOL_SIZE = new Parameter("connectionPoolSize",-1);
		KEEP_CONNECTIONS_ALIVE = new Parameter("keepConnectionsAlive",true);
		IS_SERVER_DATASOURCE = new Parameter("isServerDataSource",false);
		SERVER_URL = new Parameter("serverURL","http://localhost:8080/servlet/");
		AUTH_KEY = new Parameter("authKey",Long.toHexString(System.nanoTime()).toUpperCase()
				.concat(Long.toHexString(new Random().nextLong()).toUpperCase()));
		VERBOSE = new Parameter("verbose",false);
		
		rankParams(EMBEDDED,HOST,PORT,USERNAME,PASSWORD,CATALOG,SCHEMA,URL_EXTENSION,
				CONNECTION_POOL_SIZE,KEEP_CONNECTIONS_ALIVE,IS_SERVER_DATASOURCE,SERVER_URL,
				AUTH_KEY,VERBOSE);
	}
	
	protected List<DBDataSourceListener>	listeners	= new Vector();
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void addDBDataSourceListener(DBDataSourceListener l)
	{
		listeners.add(l);
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void removeDBDataSourceListener(DBDataSourceListener l)
	{
		listeners.remove(l);
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public DBDataSourceListener[] getDBDataSourceListeners()
	{
		return listeners.toArray(new DBDataSourceListener[listeners.size()]);
	}
	
	
	/**
	 * Returns the value of the {@link #EMBEDDED} parameter.
	 * 
	 * @return <code>true</code> if embedded is set, <code>false</code>
	 *         otherwise
	 * @since 3.1
	 */
	public boolean isEmbedded()
	{
		return getParameterValue(EMBEDDED);
	}
	
	
	/**
	 * Returns the host of this {@link AbstractDBDataSource}.
	 * 
	 * @return the host of this {@link AbstractDBDataSource}
	 */
	public String getHost()
	{
		return getParameterValue(HOST);
	}
	
	
	/**
	 * Returns the port of this {@link AbstractDBDataSource}.
	 * 
	 * @return the port of this {@link AbstractDBDataSource}
	 */
	public int getPort()
	{
		return getParameterValue(PORT);
	}
	
	
	/**
	 * Returns the user name of this {@link AbstractDBDataSource}.
	 * 
	 * @return the user name of this {@link AbstractDBDataSource}
	 */
	public String getUserName()
	{
		return getParameterValue(USERNAME);
	}
	
	
	/**
	 * Returns the password of this {@link AbstractDBDataSource}.
	 * 
	 * @return the password of this {@link AbstractDBDataSource}
	 */
	public Password getPassword()
	{
		return getParameterValue(PASSWORD);
	}
	
	
	/**
	 * Returns the catalog of this {@link AbstractDBDataSource}.
	 * 
	 * @return the catalog of this {@link AbstractDBDataSource}
	 */
	public String getCatalog()
	{
		return getParameterValue(CATALOG);
	}
	
	
	/**
	 * Returns the schema of this {@link AbstractDBDataSource}.
	 * 
	 * @return the schema of this {@link AbstractDBDataSource}
	 */
	public String getSchema()
	{
		return getParameterValue(SCHEMA);
	}
	
	
	public String getUrlExtension()
	{
		return getParameterValue(URL_EXTENSION);
	}
	
	
	public void setUrlExtension(String urlExtension)
	{
		getParameter(URL_EXTENSION).setValue(urlExtension);
	}
	
	
	/**
	 * Returns the connection pool size of this {@link AbstractDBDataSource}.
	 * 
	 * @return the connection pool size of this {@link AbstractDBDataSource}
	 */
	public int getConnectionPoolSize()
	{
		return getParameterValue(CONNECTION_POOL_SIZE);
	}
	
	
	/**
	 * Returns <code>true</code> if the connection of this
	 * {@link AbstractDataSource} is alive.
	 * 
	 * @return <code>true</code> if the connection of this
	 *         {@link AbstractDataSource} is alive, otherwise <code>false</code>
	 *         .
	 */
	public boolean keepConnectionsAlive()
	{
		return getParameterValue(KEEP_CONNECTIONS_ALIVE);
	}
	
	
	/**
	 * Determines if this data source is a distibuted client-server data source.
	 * 
	 * @return <code>true</code> if this data source is a distibuted
	 *         client-server data source, <code>false</code> otherwise
	 */
	public boolean isServerDataSource()
	{
		return getParameterValue(IS_SERVER_DATASOURCE);
	}
	
	
	/**
	 * Returns the password of this {@link AbstractDBDataSource}.
	 * 
	 * @return the password of this {@link AbstractDBDataSource}
	 */
	public String getServerURL()
	{
		return getParameterValue(SERVER_URL);
	}
	
	
	/**
	 * Returns the authentification key of this {@link AbstractDBDataSource}.
	 * 
	 * @return the authentification key of this {@link AbstractDBDataSource}
	 */
	public String getAuthKey()
	{
		return getParameterValue(AUTH_KEY);
	}
	
	
	/**
	 * Sets the key which is used to authentificate this client to communicate
	 * with the servlet-part of this data source.
	 * 
	 * @param authKey
	 *            the new authorization key
	 */
	public void setAuthKey(String authKey)
	{
		getParameter(AUTH_KEY).setValue(authKey);
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isParameterRequired(Parameter p)
	{
		if(SERVER_URL.equals(p) || AUTH_KEY.equals(p))
		{
			return isServerDataSource() && !isEmbedded();
		}
		else if(HOST.equals(p) || PORT.equals(p) || CONNECTION_POOL_SIZE.equals(p)
				|| KEEP_CONNECTIONS_ALIVE.equals(p) || IS_SERVER_DATASOURCE.equals(p))
		{
			return !isEmbedded();
		}
		
		return super.isParameterRequired(p);
	}
	
	
	@Override
	public final DBConnection openConnection() throws DBException
	{
		DBConnection con = openConnectionImpl();
		
		if(listeners.size() > 0)
		{
			DBDataSourceEvent event = new DBDataSourceEvent(this,con);
			for(DBDataSourceListener listener : getDBDataSourceListeners())
			{
				listener.connectionOpened(event);
			}
		}
		
		return con;
	}
	
	
	protected abstract DBConnection openConnectionImpl() throws DBException;
}
