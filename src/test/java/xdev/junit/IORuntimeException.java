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
package xdev.junit;

import org.junit.Ignore;

/**
 * 
 * 
 * @author XDEV Software (FHAE)
 */
@Ignore
public class IORuntimeException extends RuntimeException
{
	/**
	 * 
	 */
	private static final long	serialVersionUID	= 2356753648573380378L;


	/**
	 * Constructs a new io runtime exception.
	 */
	public IORuntimeException()
	{
		super();
	}


	/**
	 * Constructs a new io runtime exception with a specific detail
	 * message.
	 * 
	 * @param message
	 *            the detail message. The detail message is saved for later
	 *            retrieval by the {@link #getMessage()} method.
	 */
	public IORuntimeException(String message)
	{
		super(message);
	}


	/**
	 * Constructs a new io runtime exception with the specified cause.
	 * 
	 * @param cause
	 *            the cause (which is saved for later retrieval by the
	 *            {@link #getCause()} method). (A <tt>null</tt> value is
	 *            permitted, and indicates that the cause is nonexistent or
	 *            unknown.)
	 */
	public IORuntimeException(Throwable cause)
	{
		super(cause);
	}


	/**
	 * Constructs a new io runtime exception with the specified detail
	 * message and cause.
	 * 
	 * @param message
	 *            the detail message (which is saved for later retrieval by the
	 *            {@link #getMessage()} method).
	 * @param cause
	 *            the cause (which is saved for later retrieval by the
	 *            {@link #getCause()} method). (A <tt>null</tt> value is
	 *            permitted, and indicates that the cause is nonexistent or
	 *            unknown.)
	 */
	public IORuntimeException(String message, Throwable cause)
	{
		super(message,cause);
	}
}
