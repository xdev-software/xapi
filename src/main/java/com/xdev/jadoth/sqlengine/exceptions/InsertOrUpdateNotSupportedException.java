
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
 * The Class InsertOrUpdateNotSupportedException.
 * 
 * @author Thomas Muenz
 */
public class InsertOrUpdateNotSupportedException extends SQLEngineRuntimeException
{
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1775471886123659698L;



	/**
	 * Instantiates a new insert or update not supported exception.
	 * 
	 * @param message the message
	 */
	public InsertOrUpdateNotSupportedException(final String message) {
		super(message);
	}

	/**
	 * Instantiates a new insert or update not supported exception.
	 */
	public InsertOrUpdateNotSupportedException() {
		super();
	}

}
