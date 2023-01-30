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
package xdev.db.locking;


// message handling
/**
 * Locking exception wrapper for general lock issues.
 * 
 * @author XDEV Software jwill
 * @since 4.0
 */
public class LockingException extends RuntimeException
{
	/**
	 * Initializes this exception with the given message.
	 * 
	 * @param message
	 *            the exception message
	 */
	public LockingException(String message)
	{
		super(message);
	}
	
	
	/**
	 * Initializes this exception with the given cause.
	 * 
	 * @param cause
	 *            the exception cause.
	 */
	public LockingException(Throwable cause)
	{
		super(cause);
	}
	
	
	/**
	 * Initializes this exception with the given cause and an additional
	 * message.
	 * 
	 * @param message
	 *            the additional exception message.
	 * @param cause
	 *            the exception cause.
	 */
	public LockingException(String message, Throwable cause)
	{
		super(message,cause);
	}
	
}
