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

import java.text.ParseException;






/**
 * The Class SQLUnparseableDateStringException.
 * 
 * @author Thomas Muenz
 */
public class SQLUnparseableDateStringException extends SQLEngineRuntimeException {


	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 2691446713229622806L;

	/** The parse exception. */
	private ParseException parseException;

	/**
	 * Instantiates a new sQL unparseable date string exception.
	 * 
	 * @param parseException the parse exception
	 */
	public SQLUnparseableDateStringException(final ParseException parseException) {
		this.parseException = parseException;
	}

	/**
	 * Gets the parses the exception.
	 * 
	 * @return the parses the exception
	 */
	public ParseException getParseException() {
		return parseException;
	}

	/**
	 * Sets the parses the exception.
	 * 
	 * @param parseException the new parses the exception
	 */
	public void setParseException(final ParseException parseException) {
		this.parseException = parseException;
	}
}
