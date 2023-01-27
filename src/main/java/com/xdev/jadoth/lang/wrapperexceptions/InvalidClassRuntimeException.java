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

package com.xdev.jadoth.lang.wrapperexceptions;

import java.io.InvalidClassException;



/**
 * The Class InvalidClassRuntimeException.
 * 
 * @author Thomas Muenzu
 */
public class InvalidClassRuntimeException extends WrapperRuntimeException
{
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -2186078948058908911L;



	/**
	 * Instantiates a new invalid class runtime exception.
	 */
	public InvalidClassRuntimeException() {
		super();
	}

	/**
	 * Instantiates a new invalid class runtime exception.
	 * 
	 * @param message the message
	 * @param cause the cause
	 */
	public InvalidClassRuntimeException(final String message, final InvalidClassException cause) {
		super(message, cause);
	}

	/**
	 * Instantiates a new invalid class runtime exception.
	 * 
	 * @param message the message
	 */
	public InvalidClassRuntimeException(final String message) {
		super(message);
	}

	/**
	 * Instantiates a new invalid class runtime exception.
	 * 
	 * @param cause the cause
	 */
	public InvalidClassRuntimeException(final InvalidClassException cause) {
		super(cause);
	}
	
	/**
	 * @return
	 */
	@Override
	public InvalidClassException getCause()
	{
		return (InvalidClassException)super.getCause();
	}

}
