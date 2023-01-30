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
package com.xdev.jadoth.sqlengine.internal;

import java.util.HashMap;

import com.xdev.jadoth.sqlengine.SQL;



/**
 * The Class HAVING.
 * 
 * @author Thomas Muenz
 */
public class HAVING extends ConditionClause<HAVING>
{
	///////////////////////////////////////////////////////////////////////////
	// constants        //
	/////////////////////
	/**
	 * 
	 */
	private static final long serialVersionUID = 7508066168288370358L;

	

	///////////////////////////////////////////////////////////////////////////
	// constructors //
	/////////////////
	/**
	 * Instantiates a new hAVING.
	 */
	public HAVING () {
		super();
	}
	/**
	 * Instantiates a new hAVING.
	 * 
	 * @param firstCondition the first condition
	 */
	public HAVING(final Object firstCondition) {
		super(firstCondition);
	}
	
	protected HAVING(final HAVING copySource)
	{
		super(copySource);
	}


	
	///////////////////////////////////////////////////////////////////////////
	// getters          //
	/////////////////////
	/**
	 * @return
	 * @see com.xdev.jadoth.sqlengine.internal.SqlClause#keyword()
	 */
	@Override
	public String keyword() {
		return SQL.LANG.HAVING;
	}

	
	
	///////////////////////////////////////////////////////////////////////////
	// override methods //
	/////////////////////
	/**
	 * @return
	 */
	@Override
	public HAVING copy()
	{
		return new HAVING(this);
	}
	/**
	 * @param alreadyCopied
	 * @return
	 */
	@Override
	protected HAVING copy(final HashMap<SqlClause<?>, SqlClause<?>> alreadyCopied)
	{
		// no references to other clauses present in this class that would have to be registered
		return new HAVING(this);
	}

}
