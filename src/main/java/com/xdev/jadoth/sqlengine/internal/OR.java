
package com.xdev.jadoth.sqlengine.internal;

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

import static com.xdev.jadoth.sqlengine.SQL.LANG.OR;


/**
 * The Class OR.
 * 
 * @author Thomas Muenz
 */
public class OR extends SqlBooleanArithmeticCondition 
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -870648054880095595L;

	/**
	 * Instantiates a new oR.
	 * 
	 * @param con the con
	 */
	public OR(final Object con) {
		super(con);
	}
	/**
	 * @param copySource
	 * @param nextCondition
	 */
	protected OR(final OR copySource, final SqlBooleanArithmeticCondition nextCondition)
	{
		super(copySource, nextCondition);
	}


	/**
	 * @return
	 * @see com.xdev.jadoth.sqlengine.internal.SqlBooleanArithmeticCondition#getNewLineSuffixSpace()
	 */
	@Override
	protected String getNewLineSuffixSpace() {
		return "  ";
	}

	/**
	 * @return
	 * @see com.xdev.jadoth.sqlengine.internal.SqlBooleanArithmeticCondition#keyword()
	 */
	@Override
	public String keyword() {
		return OR;
	}
	
	
	
	///////////////////////////////////////////////////////////////////////////
	// override methods //
	/////////////////////
	/**
	 * @return
	 */
	@Override
	public OR copy()
	{
		return new OR(this, null);
	}
	/**
	 * @param copyDeepNextCondition
	 * @return
	 */
	@Override
	public OR copy(final Boolean copyDeepNextCondition)
	{
		//AND has no data of its own, so spare redundant code and use superclass' static copy
		return (OR)copy(this, copyDeepNextCondition);
	}

}
