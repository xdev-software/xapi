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


/**
 * The representation of a savepoint, which is a point within the current
 * transaction that can be referenced from the {@link DBConnection#rollback()}
 * method. When a transaction is rolled back to a savepoint all changes made
 * after that savepoint are undone.
 * <p>
 * Savepoints can be either named or unnamed. Unnamed savepoints are identified
 * by an ID generated by the underlying data source.
 * 
 * @author XDEV Software
 */
public interface Savepoint
{
	/**
	 * Retrieves the generated ID for the {@link Savepoint} that this
	 * {@link Savepoint} object represents.
	 * 
	 * @return the numeric ID of this {@link Savepoint}
	 * 
	 * @throws DBException
	 *             if this is a named {@link Savepoint}
	 */
	public int getSavepointId() throws DBException;


	/**
	 * Retrieves the name of the {@link Savepoint} that this {@link Savepoint}
	 * object represents.
	 * 
	 * @return the name of this {@link Savepoint}
	 * 
	 * @throws DBException
	 *             if this is an un-named {@link Savepoint}
	 */
	public String getSavepointName() throws DBException;
}
