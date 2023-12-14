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
 * The Class SQLEngineCouldNotConnectToDBException.
 * 
 * @author Thomas Muenz
 */
public class SQLEngineCouldNotConnectToDBException extends SQLEngineException
{

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 3565368519675372788L;

	/**
	 * Instantiates a new sQL engine could not connect to db exception.
	 */
	public SQLEngineCouldNotConnectToDBException() {
		super();
	}

	/**
	 * Instantiates a new sQL engine could not connect to db exception.
	 * 
	 * @param reason the reason
	 * @param cause the cause
	 */
	public SQLEngineCouldNotConnectToDBException(final String reason, final Throwable cause) {
		super(reason, cause);
	}

	/**
	 * Instantiates a new sQL engine could not connect to db exception.
	 * 
	 * @param reason the reason
	 */
	public SQLEngineCouldNotConnectToDBException(final String reason) {
		super(reason);
	}

	/**
	 * Instantiates a new sQL engine could not connect to db exception.
	 * 
	 * @param cause the cause
	 */
	public SQLEngineCouldNotConnectToDBException(final Throwable cause) {
		super(cause);
	}



}
