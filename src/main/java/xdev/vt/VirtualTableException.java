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
package xdev.vt;


/**
 * Thrown if something wents wrong, while processing methods on a
 * {@link VirtualTable}.
 * <p>
 * This is the base class for exception thrown in the context of
 * {@link VirtualTable}s.
 * </p>
 * 
 * @see #getVirtualTable()
 * 
 * @author XDEV Software Corp.
 */
public class VirtualTableException extends RuntimeException
{
	/**
	 * Helper method for throwing a {@link VirtualTableException}, when a
	 * {@link VirtualTable} wasn't found.
	 * 
	 * @param name
	 *            the name of the {@link VirtualTable} not found.
	 * @throws VirtualTableException
	 *             the exception to throw
	 */
	public static void throwVirtualTableNotFound(String name) throws VirtualTableException
	{
		throw new VirtualTableException("VirtualTable '" + name + "' not found");
	}
	

	/**
	 * Helper method for throwing a {@link VirtualTableException}, when a column
	 * name for a {@link VirtualTable} wasn't found.
	 * 
	 * @param vt
	 *            the {@link VirtualTable}.
	 * 
	 * @param columnName
	 *            the name of the column to search.
	 * 
	 * @throws VirtualTableException
	 *             the exception to throw
	 */
	public static void throwColumnNotFound(VirtualTable vt, String columnName)
			throws VirtualTableException
	{
		throw new VirtualTableException(vt,"Column '" + columnName
				+ "' not found in VirtualTable '" + vt.getName() + "'");
	}
	
	/**
	 * @since 3.1
	 */
	private VirtualTable	virtualTable;
	

	/**
	 * Initializes a new {@link VirtualTableException}.
	 */
	public VirtualTableException()
	{
		super();
	}
	

	/**
	 * Initializes a new {@link VirtualTableException}.
	 * 
	 * @param virtualTable
	 *            the exception's source/cause
	 * @since 3.1
	 */
	public VirtualTableException(VirtualTable virtualTable)
	{
		super();
		
		this.virtualTable = virtualTable;
	}
	

	/**
	 * Initializes a new {@link VirtualTableException}.
	 * 
	 * @param message
	 *            the message of the exception.
	 */
	public VirtualTableException(String message)
	{
		super(message);
	}
	

	/**
	 * Initializes a new {@link VirtualTableException}.
	 * 
	 * @param virtualTable
	 *            the exception's source/cause
	 * @param message
	 *            the message of the exception.
	 * @since 3.1
	 */
	public VirtualTableException(VirtualTable virtualTable, String message)
	{
		super(message);
		
		this.virtualTable = virtualTable;
	}
	

	/**
	 * Initializes a new {@link VirtualTableException}.
	 * 
	 * @param cause
	 *            the {@link Throwable} that caused this exception.
	 */
	public VirtualTableException(Throwable cause)
	{
		super(cause);
	}
	

	/**
	 * Initializes a new {@link VirtualTableException}.
	 * 
	 * @param virtualTable
	 *            the exception's source/cause
	 * @param cause
	 *            the {@link Throwable} that caused this exception.
	 * @since 3.1
	 */
	public VirtualTableException(VirtualTable virtualTable, Throwable cause)
	{
		super(cause);
		
		this.virtualTable = virtualTable;
	}
	

	/**
	 * Initializes a new {@link VirtualTableException}.
	 * 
	 * @param message
	 *            the message of the exception.
	 * @param cause
	 *            the {@link Throwable} that caused this exception.
	 */
	public VirtualTableException(String message, Throwable cause)
	{
		super(message,cause);
	}
	

	/**
	 * Initializes a new {@link VirtualTableException}.
	 * 
	 * @param virtualTable
	 *            the exception's source/cause
	 * @param message
	 *            the message of the exception.
	 * @param cause
	 *            the {@link Throwable} that caused this exception.
	 * @since 3.1
	 */
	public VirtualTableException(VirtualTable virtualTable, String message, Throwable cause)
	{
		super(message,cause);
		
		this.virtualTable = virtualTable;
	}
	

	/**
	 * Returns the source resp the cause of this exception.
	 * 
	 * @return the {@link VirtualTable} associated with this exception, may be
	 *         <code>null</code>
	 * @since 3.1
	 */
	public VirtualTable getVirtualTable()
	{
		return virtualTable;
	}
}
