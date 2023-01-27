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
/**
 * 
 */
package com.xdev.jadoth.lang.signalthrows;

/**
 * Thrown to signals the outer context to return as soon as possible, 
 * returning a value corresponding to the current state of the outer context's execution
 * (i.e. the current value of a count variable, etc.). 
 * 
 * @author Thomas Muenz
 *
 */
public class ThrowReturn extends BranchingThrow
{
	///////////////////////////////////////////////////////////////////////////
	// constants        //
	/////////////////////
	
	private static final long serialVersionUID = 701797439650463255L;


	
	
	///////////////////////////////////////////////////////////////////////////
	// constructors     //
	/////////////////////

	public ThrowReturn()
	{
		super();
	}

	public ThrowReturn(final Throwable cause)
	{
		super(cause);
	}
	
	public ThrowReturn(final Object hint)
	{
		super(hint);
	}

	public ThrowReturn(final Object hint, final Throwable cause)
	{
		super(hint, cause);
	}
	
}
