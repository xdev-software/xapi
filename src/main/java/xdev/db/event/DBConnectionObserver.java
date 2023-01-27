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


import xdev.db.DBDataSource;


/**
 * Combines {@link DBDataSourceListener} and {@link DBConnectionListener} for a
 * shorter way to observe connections of a {@link DBDataSource}.
 * 
 * <pre>
 * <code>
 * DBConnection con = ...;
 * con.addDBConnectionListener(new DBConnectionObserver()
 * {
 * 		public void queryPerformed(DBConnectionEvent event)
 * 		{
 * 			System.out.println(event.getQuery().getStatement());
 * 		}
 * 
 * 		public void writePerformed(DBConnectionEvent event)
 * 		{
 * 			System.out.println(event.getQuery().getStatement());
 * 		}
 * });
 * </code>
 * </pre>
 * 
 * @author XDEV Software
 * @since 3.0
 */
public abstract class DBConnectionObserver extends DBConnectionAdapter implements
		DBDataSourceListener
{
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void connectionOpened(DBDataSourceEvent event)
	{
		event.getConnection().addDBConnectionListener(this);
	}
}
