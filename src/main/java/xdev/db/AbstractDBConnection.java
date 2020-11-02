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


import java.lang.reflect.Field;
import java.text.DateFormat;
import java.util.Date;
import java.util.List;
import java.util.Vector;

import xdev.db.event.DBConnectionEvent;
import xdev.db.event.DBConnectionListener;
import xdev.util.StringUtils;
import xdev.util.logging.LoggerFactory;
import xdev.util.logging.XdevLogger;

import com.xdev.jadoth.sqlengine.internal.DatabaseGateway;


/**
 * <P>
 * A abstract class from the <code>DBConnection</code>. A connection (session)
 * with a specific database. SQL statements are executed and results are
 * returned within the context of a connection.
 * </P>
 * 
 * @param <DS>
 *            the generic type
 * 
 * @see DBDataSource
 * @see DBConnection
 * 
 * @author XDEV Software
 * 
 */
public abstract class AbstractDBConnection<DS extends DBDataSource<DS>> implements DBConnection<DS>
{
	/**
	 * Logger instance for this class.
	 */
	private static final XdevLogger			log				= LoggerFactory
																	.getLogger(AbstractDBConnection.class);
	
	protected final DS						dataSource;
	
	protected List<DBConnectionListener>	listeners		= new Vector();
	
	private boolean							storeQueries	= false;
	private final List<Query>				queries			= new Vector();
	
	
	/**
	 * Constructs a {@link AbstractDBConnection} that is initialized with the
	 * originating data source.
	 * 
	 * @param dataSource
	 *            The originating data source
	 */
	public AbstractDBConnection(DS dataSource)
	{
		this.dataSource = dataSource;
	}
	
	
	/**
	 * Returns the data source of this {@link AbstractDBConnection}.
	 * 
	 * @return this connection's originating data source
	 */
	public DS getDataSource()
	{
		return dataSource;
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void addDBConnectionListener(DBConnectionListener l)
	{
		listeners.add(l);
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void removeDBConnectionListener(DBConnectionListener l)
	{
		listeners.remove(l);
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public DBConnectionListener[] getDBConnectionListeners()
	{
		return listeners.toArray(new DBConnectionListener[listeners.size()]);
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setStoreQueries(boolean store)
	{
		this.storeQueries = store;
		if(!storeQueries)
		{
			queries.clear();
		}
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Query[] getStoredQueries()
	{
		return queries.toArray(new Query[queries.size()]);
	}
	
	
	protected void queryPerformed(String statement, Object[] params, DBException exception)
	{
		Query query = new Query(statement,params);
		if(storeQueries)
		{
			queries.add(query);
		}
		
		log(statement,params);
		
		if(listeners.size() > 0)
		{
			DBConnectionEvent event = new DBConnectionEvent(this);
			event.setQuery(query);
			event.setException(exception);
			for(DBConnectionListener listener : listeners)
			{
				listener.queryPerformed(event);
			}
		}
	}
	
	
	protected void writePerformed(String statement, Object[] params, DBException exception)
	{
		Query query = new Query(statement,params);
		if(storeQueries)
		{
			queries.add(query);
		}
		
		log(statement,params);
		
		if(listeners.size() > 0)
		{
			DBConnectionEvent event = new DBConnectionEvent(this);
			event.setQuery(query);
			event.setException(exception);
			for(DBConnectionListener listener : listeners)
			{
				listener.writePerformed(event);
			}
		}
	}
	
	
	private void log(String statement, Object[] params)
	{
		if(!dataSource.getParameterValue(AbstractDBDataSource.VERBOSE))
		{
			return;
		}
		
		System.out.println(StringUtils.create("=",80));
		System.out.print(getClass().getCanonicalName());
		System.out.print(" @ ");
		System.out.println(DateFormat.getDateTimeInstance().format(new Date()));
		System.out.println(StringUtils.create("-",80));
		System.out.println(statement);
		if(params != null && params.length > 0)
		{
			System.out.println(StringUtils.create("-",80));
			for(int i = 0; i < params.length; i++)
			{
				System.out.print("?(");
				System.out.print(i);
				System.out.print(")");
				if(params[i] == null)
				{
					System.out.println(" = null");
				}
				else
				{
					System.out.print(" = ");
					try
					{
						System.out.print(params[i].toString());
					}
					catch(Exception e)
					{
						System.out.println("Exception @ toString():");
						e.printStackTrace();
					}
					System.out.print(" : ");
					System.out.println(params[i].getClass().getSimpleName());
				}
			}
		}
	}
	
	
	/**
	 * Quick and dirty way to inject the gateway into a xdev.db.sql object
	 */
	protected void decorateDelegate(Object query, DatabaseGateway gateway)
	{
		try
		{
			Field field = query.getClass().getDeclaredField("delegate");
			boolean accessible = field.isAccessible();
			if(!accessible)
			{
				field.setAccessible(true);
			}
			com.xdev.jadoth.sqlengine.types.Query _query = (com.xdev.jadoth.sqlengine.types.Query)field
					.get(query);
			_query.setDatabaseGateway(gateway);
			if(!accessible)
			{
				field.setAccessible(false);
			}
		}
		catch(Exception e)
		{
			log.error(e);
		}
	}
}
