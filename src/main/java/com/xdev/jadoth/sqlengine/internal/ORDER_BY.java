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
 * The Class ORDER_BY.
 * 
 * @author Thomas Muenz
 */
/* (12.11.2009 TM)XXX:
 * NULLS FIRST/LAST ?
 * http://troels.arvin.dk/db/rdbms/#select
 *
 */
public class ORDER_BY extends ListClause<Object> 
{
	///////////////////////////////////////////////////////////////////////////
	// constants        //
	/////////////////////
	/**
	 * 
	 */
	private static final long serialVersionUID = 5758576901393486773L;
	
	
	
	///////////////////////////////////////////////////////////////////////////
	// constructors //
	/////////////////
	/**
	 * Instantiates a new ORDER_BY.
	 */
	protected ORDER_BY()
	{
		super((Object[])null);
	}
	/**
	 * Instantiates a new ORDER_BY.
	 * 
	 * @param elements the elements
	 */
	public ORDER_BY(final Object... elements)
	{
		super(elements);
	}
	protected ORDER_BY(final ORDER_BY copySource)
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
		return SQL.LANG.ORDER_BY;
	}

	
	
	///////////////////////////////////////////////////////////////////////////
	// override methods //
	/////////////////////
	/**
	 * @return
	 */
	@Override
	public ORDER_BY copy()
	{
		return new ORDER_BY(this);
	}
	/**
	 * @param alreadyCopied
	 * @return
	 */
	@Override
	protected ORDER_BY copy(final HashMap<SqlClause<?>, SqlClause<?>> alreadyCopied)
	{
		// no references to other clauses present in this class that would have to be registered
		return new ORDER_BY(this);
	}

}
