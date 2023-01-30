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
package xdev.db.servlet;


import java.io.IOException;


/**
 * Exception thrown by the server when a session has expired. A new session id
 * must be aquired to continue.
 * 
 * @author XDEV Software
 * @since 3.2
 */
public class SessionExpiredException extends IOException
{
	public SessionExpiredException()
	{
		super();
	}
	
	
	public SessionExpiredException(String message, Throwable cause)
	{
		super(message,cause);
	}
	
	
	public SessionExpiredException(String message)
	{
		super(message);
	}
	
	
	public SessionExpiredException(Throwable cause)
	{
		super(cause);
	}
}
