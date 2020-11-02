package xdev.vt;

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


/**
 * Thrown if a duplicate value is added to a column with a unique index.
 * 
 * @author XDEV Software Corp.
 * 
 */
public class UniqueIndexDuplicateValuesException extends VirtualTableException
{
	/**
	 * Initializes a new {@link UniqueIndexDuplicateValuesException}.
	 */
	public UniqueIndexDuplicateValuesException()
	{
		super();
	}
	

	/**
	 * Initializes a new {@link UniqueIndexDuplicateValuesException}.
	 * 
	 * @param virtualTable
	 *            the exception's source/cause
	 * @since 3.1
	 */
	public UniqueIndexDuplicateValuesException(VirtualTable virtualTable)
	{
		super(virtualTable);
	}
	

	/**
	 * Initializes a new {@link UniqueIndexDuplicateValuesException}.
	 * 
	 * @param message
	 *            the message of the exception.
	 * @param cause
	 *            the {@link Throwable} that caused this exception.
	 */
	public UniqueIndexDuplicateValuesException(String message, Throwable cause)
	{
		super(message,cause);
	}
	

	/**
	 * Initializes a new {@link UniqueIndexDuplicateValuesException}.
	 * 
	 * @param virtualTable
	 *            the exception's source/cause
	 * @param message
	 *            the message of the exception.
	 * @param cause
	 *            the {@link Throwable} that caused this exception.
	 * @since 3.1
	 */
	public UniqueIndexDuplicateValuesException(VirtualTable virtualTable, String message,
			Throwable cause)
	{
		super(virtualTable,message,cause);
	}
	

	/**
	 * Initializes a new {@link UniqueIndexDuplicateValuesException}.
	 * 
	 * @param message
	 *            the message of the exception.
	 */
	public UniqueIndexDuplicateValuesException(String message)
	{
		super(message);
	}
	

	/**
	 * Initializes a new {@link UniqueIndexDuplicateValuesException}.
	 * 
	 * @param virtualTable
	 *            the exception's source/cause
	 * @param message
	 *            the message of the exception.
	 * @since 3.1
	 */
	public UniqueIndexDuplicateValuesException(VirtualTable virtualTable, String message)
	{
		super(virtualTable,message);
	}
	

	/**
	 * Initializes a new {@link UniqueIndexDuplicateValuesException}.
	 * 
	 * @param cause
	 *            the {@link Throwable} that caused this exception.
	 */
	public UniqueIndexDuplicateValuesException(Throwable cause)
	{
		super(cause);
	}
	

	/**
	 * Initializes a new {@link UniqueIndexDuplicateValuesException}.
	 * 
	 * @param virtualTable
	 *            the exception's source/cause
	 * @param cause
	 *            the {@link Throwable} that caused this exception.
	 * @since 3.1
	 */
	public UniqueIndexDuplicateValuesException(VirtualTable virtualTable, Throwable cause)
	{
		super(virtualTable,cause);
	}
}
