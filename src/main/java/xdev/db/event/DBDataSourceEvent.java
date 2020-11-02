package xdev.db.event;

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


import java.util.EventObject;

import xdev.db.DBConnection;
import xdev.db.DBDataSource;


/**
 * An <code>Event</code> object that provides information about the source of a
 * {@link DBDataSource}-related event.
 * 
 * @author XDEV Software
 * @since 3.0
 */
public class DBDataSourceEvent extends EventObject
{
	private DBConnection	connection;


	/**
	 * Creates a new event object.
	 * 
	 * @param source
	 *            the source of the event
	 */
	public DBDataSourceEvent(DBDataSource source)
	{
		this(source,null);
	}


	/**
	 * Creates a new event object.
	 * 
	 * @param source
	 *            the source of the event
	 * @param connection
	 *            the affected connection
	 */
	public DBDataSourceEvent(DBDataSource source, DBConnection connection)
	{
		super(source);

		this.connection = connection;
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public DBDataSource getSource()
	{
		return (DBDataSource)super.getSource();
	}


	/**
	 * Returns the affected connection of this event.
	 * 
	 * @return the affected connection
	 */
	public DBConnection getConnection()
	{
		return connection;
	}
}
