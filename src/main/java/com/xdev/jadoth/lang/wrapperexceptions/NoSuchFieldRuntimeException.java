
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



/**
 * The Class NoSuchFieldRuntimeException.
 * 
 * @author Thomas Muenz
 */
public class NoSuchFieldRuntimeException extends WrapperRuntimeException 
{

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -4659008015774319430L;

	/**
	 * Instantiates a new no such field runtime exception.
	 */
	public NoSuchFieldRuntimeException() {
		super();
	}

	/**
	 * Instantiates a new no such field runtime exception.
	 * 
	 * @param message the message
	 * @param cause the cause
	 */
	public NoSuchFieldRuntimeException(String message, NoSuchFieldException cause) {
		super(message, cause);
	}

	/**
	 * Instantiates a new no such field runtime exception.
	 * 
	 * @param message the message
	 */
	public NoSuchFieldRuntimeException(String message) {
		super(message);
	}

	/**
	 * Instantiates a new no such field runtime exception.
	 * 
	 * @param cause the cause
	 */
	public NoSuchFieldRuntimeException(NoSuchFieldException cause) {
		super(cause);
	}
	
	/**
	 * @return
	 */
	@Override
	public NoSuchFieldException getCause()
	{
		return (NoSuchFieldException)super.getCause();
	}

}
