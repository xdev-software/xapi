
package com.xdev.jadoth.sqlengine.exceptions;

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
 * The Class SQLEngineException.
 * 
 * @author Thomas Muenz
 */
public class SQLEngineException extends RuntimeException {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 8480341861825126426L;


	/**
	 * Instantiates a new sQL engine exception.
	 */
	public SQLEngineException() {
		super();
	}

	/**
	 * Instantiates a new sQL engine exception.
	 * 
	 * @param reason the reason
	 * @param cause the cause
	 */
	public SQLEngineException(final String reason, final Throwable cause) {
		super(reason, cause);
	}


	/**
	 * Instantiates a new sQL engine exception.
	 * 
	 * @param reason the reason
	 */
	public SQLEngineException(final String reason) {
		super(reason);
	}


	/**
	 * Instantiates a new sQL engine exception.
	 * 
	 * @param cause the cause
	 */
	public SQLEngineException(final Throwable cause) {
		super(cause);
	}






}
