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



/**
 * The Class InstantiationRuntimeException.
 * 
 * @author Thomas Muenz
 */
public class InstantiationRuntimeException extends WrapperRuntimeException 
{
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -4659008015774319430L;

	
	
	/**
	 * Instantiates a new instantiation runtime exception.
	 */
	public InstantiationRuntimeException() 
	{
		super();
	}

	/**
	 * Instantiates a new instantiation runtime exception.
	 * 
	 * @param message the message
	 * @param cause the cause
	 */
	public InstantiationRuntimeException(String message, InstantiationException cause) 
	{
		super(message, cause);
	}

	/**
	 * Instantiates a new instantiation runtime exception.
	 * 
	 * @param message the message
	 */
	public InstantiationRuntimeException(String message) 
	{
		super(message);
	}

	/**
	 * Instantiates a new instantiation runtime exception.
	 * 
	 * @param cause the cause
	 */
	public InstantiationRuntimeException(InstantiationException cause) 
	{
		super(cause);
	}
	
	/**
	 * @return
	 */
	@Override
	public InstantiationException getCause()
	{
		return (InstantiationException)super.getCause();
	}

}
