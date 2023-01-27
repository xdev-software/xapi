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

package com.xdev.jadoth.sqlengine.exceptions;



/**
 * The Class SQLEngineRuntimeException.
 * 
 * @author Thomas Muenz
 */
public class SQLEngineRuntimeException extends RuntimeException {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 423719784246442887L;

	/**
	 * Instantiates a new sQL engine runtime exception.
	 */
	public SQLEngineRuntimeException() {
		super();
	}

	/**
	 * Instantiates a new sQL engine runtime exception.
	 * 
	 * @param message the message
	 */
	public SQLEngineRuntimeException(final String message) {
		super(message);
	}


	/**
	 * Instantiates a new sQL engine runtime exception.
	 * 
	 * @param message the message
	 * @param cause the cause
	 */
	public SQLEngineRuntimeException(final String message, final Throwable cause) {
		super(message, cause);
	}

	/**
	 * Instantiates a new sQL engine runtime exception.
	 * 
	 * @param cause the cause
	 */
	public SQLEngineRuntimeException(final Throwable cause) {
		super(cause);
	}




}
