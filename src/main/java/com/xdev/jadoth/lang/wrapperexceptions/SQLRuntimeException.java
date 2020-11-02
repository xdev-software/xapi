
package com.xdev.jadoth.lang.wrapperexceptions;

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

import java.io.PrintStream;
import java.sql.SQLException;


/**
 * Thin wrapper exception to enapsulate a SQLException in a RuntimeException.<br>
 * SQLRuntimeException has no stacktrace of its own and prints the stacktrace of the wrapped SQLExceptions
 *
 * @author Thomas Muenz
 *
 */
public class SQLRuntimeException extends RuntimeException
{
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -3720057129602739512L;

	/**
	 * Instantiates a new sQL runtime exception.
	 * 
	 * @param cause the cause
	 */
	public SQLRuntimeException(final SQLException cause) {
		super(cause);
	}

	/**
	 * Gets the cause.
	 * 
	 * @return the encapsulated SQLException
	 */
	@Override
	public SQLException getCause() {
		//the cause can only be an SQLException, so this cast is safe.
		return (SQLException)super.getCause();
	}

	/**
	 * Fill in stack trace.
	 * 
	 * @return the throwable
	 * @return
	 */
	@Override
	public synchronized Throwable fillInStackTrace() {
		// do nothing
		// maybe this method should return the actual SQLException?
		return this;
	}


	/**
	 * Prints the stack trace.
	 * 
	 * @param s the s
	 * @see java.lang.Throwable#printStackTrace(java.io.PrintStream)
	 */
	@Override
	public void printStackTrace(final PrintStream s){
		this.getCause().printStackTrace(s);
	}



}
