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
 * The Class SQLEngineMissingFromClauseException.
 */
public class SQLEngineMissingFromClauseException extends SQLEngineRuntimeException
{
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -9219670724948353948L;

	/**
	 * Instantiates a new sQL engine missing from clause exception.
	 */
	public SQLEngineMissingFromClauseException() {
	}

	/**
	 * Instantiates a new sQL engine missing from clause exception.
	 * 
	 * @param message the message
	 */
	public SQLEngineMissingFromClauseException(final String message) {
		super(message);
	}

	/**
	 * Instantiates a new sQL engine missing from clause exception.
	 * 
	 * @param message the message
	 * @param cause the cause
	 */
	public SQLEngineMissingFromClauseException(final String message, final Throwable cause) {
		super(message, cause);
	}

	/**
	 * Instantiates a new sQL engine missing from clause exception.
	 * 
	 * @param cause the cause
	 */
	public SQLEngineMissingFromClauseException(final Throwable cause) {
		super(cause);
	}

}
