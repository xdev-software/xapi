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
 * This class represents a result that is generated when a SQL statement is
 * executed.
 * <p>
 * It has properties like <code>affectedRows</code> and
 * <code>generatedKeys</code>.
 * </p>
 * 
 * 
 * @author XDEV Software
 * 
 */
public class WriteResult
{
	private final int		affectedRows;
	private final Result	generatedKeys;
	

	/**
	 * Construct a new {@link WriteResult} with the given parameters.
	 * 
	 * @param affectedRows
	 *            the count of the affected rows
	 * 
	 * @param generatedKeys
	 *            the {@link Result} with the generated keys or
	 *            <code>null</code>
	 */
	public WriteResult(int affectedRows, Result generatedKeys) throws DBException
	{
		this.affectedRows = affectedRows;
		
		if(generatedKeys != null && !(generatedKeys instanceof PrefetchedResult))
		{
			generatedKeys = new PrefetchedResult(generatedKeys);
		}
		this.generatedKeys = generatedKeys;
	}
	

	/**
	 * Returns the count of affected rows of this {@link WriteResult}.
	 * 
	 * @return the count of affected rows
	 */
	public int getAffectedRows()
	{
		return affectedRows;
	}
	

	/**
	 * Return all generated keys of this {@link WriteResult}.
	 * 
	 * @return a Result including all generated keys, if no keys were generated
	 *         <code>null</code> is returned.
	 */
	public Result getGeneratedKeys()
	{
		return generatedKeys;
	}
	

	/**
	 * Verify if this {@link WriteResult} has generated keys.
	 * 
	 * @return <code>true</code> if keys were generated, otherwise
	 *         <code>false</code>
	 */
	public boolean hasGeneratedKeys()
	{
		return generatedKeys != null;
	}
	

	/**
	 * Closes this {@link WriteResult} and releases all used resources.
	 * 
	 * @throws DBException
	 *             if a database access error occurs.
	 */
	public void close() throws DBException
	{
		if(generatedKeys != null)
		{
			generatedKeys.close();
		}
	}
}
