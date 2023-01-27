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


/**
 * An abstract adapter class for receiving connection events.
 * 
 * @author XDEV Software
 * @since 3.0
 */
public abstract class DBConnectionAdapter implements DBConnectionListener
{
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void queryPerformed(DBConnectionEvent event)
	{
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public void writePerformed(DBConnectionEvent event)
	{
	}
}
