
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

import static com.xdev.jadoth.sqlengine.SQL.LANG.AND;




/**
 * The Class AND.
 *
 * @author Thomas Muenz
 */
public class AND extends SqlBooleanArithmeticCondition
{
	///////////////////////////////////////////////////////////////////////////
	// constants        //
	/////////////////////

	private static final long serialVersionUID = 8784680894762171315L;



	///////////////////////////////////////////////////////////////////////////
	// constructors //
	/////////////////
	/**
	 * Instantiates a new aND.
	 *
	 * @param con the con
	 */
	public AND(final Object con) {
		super(con);
	}
	/**
	 * @param copySource
	 * @param nextCondition
	 */
	protected AND(final AND copySource, final SqlBooleanArithmeticCondition nextCondition)
	{
		super(copySource, nextCondition);
	}


	
	///////////////////////////////////////////////////////////////////////////
	// getters          //
	/////////////////////
	/**
	 * @return
	 * @see com.xdev.jadoth.sqlengine.internal.SqlBooleanArithmeticCondition#keyword()
	 */
	@Override
	public String keyword() {
		return AND;
	}

	
	
	///////////////////////////////////////////////////////////////////////////
	// override methods //
	/////////////////////
	/**
	 * @return
	 */
	@Override
	public AND copy()
	{
		return new AND(this, null);
	}
	/**
	 * @param copyDeepNextCondition
	 * @return
	 */
	@Override
	public AND copy(final Boolean copyDeepNextCondition)
	{
		//AND has no data of its own, so spare redundant code and use superclass' static copy
		return (AND)copy(this, copyDeepNextCondition);
	}

}
