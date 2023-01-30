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
package xdev.util.logging;


/**
 * This exception is thrown when there is a configuration problem. This can
 * arise when installation of a provider was not done correctly, or if there are
 * configuration problems with the server, or if configuration information
 * required to access the provider or service is malformed or missing.
 * 
 * 
 * @since 3.2
 */
public class LoggingConfigurationException extends RuntimeException
{
	
	/**
	 * Constructs a new {@link LoggingConfigurationException} with the specified
	 * detail message and cause.
	 * <p>
	 * Note that the detail message associated with <code>cause</code> is
	 * <i>not</i> automatically incorporated in this
	 * LoggingConfigurationException's detail message.
	 * 
	 * @param message
	 *            the detail message (which is saved for later retrieval by the
	 *            {@link #getMessage()} method).
	 * @param cause
	 *            the cause (which is saved for later retrieval by the
	 *            {@link #getCause()} method). (A <tt>null</tt> value is
	 *            permitted, and indicates that the cause is nonexistent or
	 *            unknown.)
	 * 
	 */
	public LoggingConfigurationException(String message, Throwable cause)
	{
		super(message,cause);
	}
	
}
