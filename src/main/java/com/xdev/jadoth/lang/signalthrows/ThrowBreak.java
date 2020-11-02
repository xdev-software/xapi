/**
 * 
 */
package com.xdev.jadoth.lang.signalthrows;

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
 * Thrown to signals the outer context to break the current loop, 
 * normally proceeding with the actions following the loop. 
 * 
 * @author Thomas Muenz
 *
 */
public class ThrowBreak extends BranchingThrow
{
	///////////////////////////////////////////////////////////////////////////
	// constants        //
	/////////////////////

	private static final long serialVersionUID = -6268201471764087383L;


	
	///////////////////////////////////////////////////////////////////////////
	// constructors     //
	/////////////////////

	public ThrowBreak()
	{
		super();
	}

	public ThrowBreak(final Throwable cause)
	{
		super(cause);
	}
	
	public ThrowBreak(final Object hint)
	{
		super(hint);
	}

	public ThrowBreak(final Object hint, final Throwable cause)
	{
		super(hint, cause);
	}
	
}
