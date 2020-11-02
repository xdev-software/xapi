
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

import java.util.HashMap;

import com.xdev.jadoth.sqlengine.SQL;





/**
 * The Class WHERE.
 * 
 * @author Thomas Muenz
 */
public class WHERE extends ConditionClause<WHERE>
{
	///////////////////////////////////////////////////////////////////////////
	// constants        //
	/////////////////////
	/**
	 * 
	 */
	private static final long serialVersionUID = 6995243261988098661L;

	
	
	///////////////////////////////////////////////////////////////////////////
	// constructors //
	/////////////////
	/**
	 * Instantiates a new wHERE.
	 */
	public WHERE()
	{
		super();
	}	
	/**
	 * Instantiates a new wHERE.
	 * 
	 * @param body the body
	 */
	public WHERE(final Object body) 
	{
		super(body);
	}
	/**
	 * 
	 * @param copySource
	 */
	protected WHERE(final WHERE copySource)
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
		return SQL.LANG.WHERE;
	}
	
	
	
	///////////////////////////////////////////////////////////////////////////
	// override methods //
	/////////////////////
	/**
	 * @return
	 */
	@Override
	public WHERE copy()
	{
		return new WHERE(this);
	}
	/**
	 * @param alreadyCopied
	 * @return
	 */
	@Override
	protected WHERE copy(final HashMap<SqlClause<?>, SqlClause<?>> alreadyCopied)
	{
		// no references to other clauses present in this class that would have to be registered
		return new WHERE(this);
	}

}
