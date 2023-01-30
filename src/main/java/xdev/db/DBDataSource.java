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


import xdev.db.event.DBDataSourceListener;
import xdev.util.*;


/**
 * 
 * Base type for all database drivers.
 * 
 * @author XDEV Software Corp.
 * 
 */
public interface DBDataSource<T extends DBDataSource> extends DataSource<T>
{
	/**
	 * Registers a {@link DBDataSourceListener} at this data source.
	 * 
	 * @param l
	 *            the listener to register
	 */
	public void addDBDataSourceListener(DBDataSourceListener l);
	

	/**
	 * Unregisters a {@link DBDataSourceListener}.
	 * 
	 * @param l
	 *            the listener to unregister
	 */
	public void removeDBDataSourceListener(DBDataSourceListener l);
	

	/**
	 * Retrieves all registered {@link DBDataSourceListener}s.
	 * 
	 * @return a array of {@link DBDataSourceListener}s, or a null-length array
	 *         if no listener is registered
	 */
	public DBDataSourceListener[] getDBDataSourceListeners();
	

	/**
	 * {@inheritDoc}
	 */
	@Override
	public DBMetaData getMetaData() throws DBException;
	

	/**
	 * Open the connection for this {@link DBDataSource}.
	 * 
	 * @return the open {@link DBConnection}
	 * 
	 * @throws DBException
	 *             if a database access error occurs
	 */
	public DBConnection openConnection() throws DBException;
	

	/**
	 * Close all open connections.
	 * 
	 * @throws DBException
	 *             if a database access error occurs
	 */
	public void closeAllOpenConnections() throws DBException;
	

	/**
	 * Determines if this data source is able to create or alter tables in the
	 * database.
	 * 
	 */
	public boolean canExport();
}
