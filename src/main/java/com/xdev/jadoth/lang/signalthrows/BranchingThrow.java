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
 * @author Thomas Muenz
 *
 */
public abstract class BranchingThrow extends Throwable
{
	///////////////////////////////////////////////////////////////////////////
	// constants        //
	/////////////////////

	private static final long serialVersionUID = -2235287974282924970L;

	public static final ThrowBreak    BREAK    = new ThrowBreak();
	public static final ThrowContinue CONTINUE = new ThrowContinue();
	public static final ThrowReturn   RETURN   = new ThrowReturn();



	///////////////////////////////////////////////////////////////////////////
	// instance fields  //
	/////////////////////


	private final Object hint;



	///////////////////////////////////////////////////////////////////////////
	// constructors     //
	/////////////////////

	protected BranchingThrow()
	{
		super();
		this.hint = null;
	}

	protected BranchingThrow(final Throwable cause)
	{
		super(cause);
		this.hint = null;
	}

	protected BranchingThrow(final Object hint)
	{
		super();
		this.hint = hint;
	}

	protected BranchingThrow(final Object hint, final Throwable cause)
	{
		super(cause);
		this.hint = hint;
	}



	///////////////////////////////////////////////////////////////////////////
	// getters          //
	/////////////////////

	public Object getHint()
	{
		return this.hint;
	}



	///////////////////////////////////////////////////////////////////////////
	// override methods //
	/////////////////////

	/**
	 * @return
	 * @see java.lang.Throwable#fillInStackTrace()
	 */
	@Override
	public synchronized BranchingThrow fillInStackTrace()
	{
		return this;
	}


}
