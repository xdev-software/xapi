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
package xdev.db.event;


import java.util.EventObject;

import xdev.db.DBConnection;
import xdev.db.DBException;
import xdev.db.DBConnection.Query;


/**
 * An <code>Event</code> object that provides information about the source of a
 * {@link DBConnection}-related event.
 * 
 * @author XDEV Software
 * @since 3.0
 */
public class DBConnectionEvent extends EventObject
{
	private Query		query;
	private DBException	exception;
	

	/**
	 * Create a new connection event.
	 * 
	 * @param source
	 *            the origin of the event
	 */
	public DBConnectionEvent(DBConnection source)
	{
		super(source);
	}
	

	/**
	 * {@inheritDoc}
	 */
	@Override
	public DBConnection getSource()
	{
		return (DBConnection)super.getSource();
	}
	

	/**
	 * Registers a query object at this event.
	 * 
	 * @param query
	 *            the involved query
	 */
	public void setQuery(Query query)
	{
		this.query = query;
	}
	

	/**
	 * Returns the query of this connection event, or <code>null</code> if no
	 * query was involved.
	 * 
	 * @return the event's query or <code>null</code>
	 */
	public Query getQuery()
	{
		return query;
	}
	

	/**
	 * Registers an exception at this event.
	 * 
	 * @param exception
	 *            the occured error
	 */
	public void setException(DBException exception)
	{
		this.exception = exception;
	}
	

	/**
	 * Returns the exception of this connection event, or <code>null</code> if
	 * no error occured.
	 * 
	 * @return the event's exception or <code>null</code>
	 */
	public DBException getException()
	{
		return exception;
	}
	

	/**
	 * Returns <code>true</code> if an error occured, <code>false</code>
	 * otherwise.
	 */
	public boolean hasException()
	{
		return exception != null;
	}
}
