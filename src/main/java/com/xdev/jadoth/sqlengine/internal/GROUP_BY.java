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

import static com.xdev.jadoth.sqlengine.SQL.Punctuation.SPACE;

import java.util.HashMap;

import com.xdev.jadoth.sqlengine.SQL;



/**
 * The Class GROUP_BY.
 * 
 * @author Thomas Muenz
 */
public class GROUP_BY extends ListClause<Object>
{	
	///////////////////////////////////////////////////////////////////////////
	// constants        //
	/////////////////////
	/**
	 * 
	 */
	private static final long serialVersionUID = -8920737164500607341L;

	

	///////////////////////////////////////////////////////////////////////////
	// constructors //
	/////////////////
	/**
	 * Instantiates a new GROUP BY clause.
	 * 
	 * @param body the body
	 */
	public GROUP_BY(final Object... body) {
		super(body);
	}
	/**
	 * Instantiates a new GROUP BY clause.
	 */
	protected GROUP_BY() {
		super((Object[])null);
	}
	
	protected GROUP_BY(final GROUP_BY copySource) {
		super(copySource);
	}

	
	
	///////////////////////////////////////////////////////////////////////////
	// getters          //
	/////////////////////
	/**
	 * Key word seperator.
	 * 
	 * @return the string
	 */
	protected String keyWordSeperator() {
		return SPACE;
	}
	
	/**
	 * @return
	 * @see com.xdev.jadoth.sqlengine.internal.SqlClause#keyword()
	 */
	@Override
	public String keyword() {
		return SQL.LANG.GROUP_BY;
	}
	
	
	
	///////////////////////////////////////////////////////////////////////////
	// override methods //
	/////////////////////
	/**
	 * @return
	 */
	@Override
	public GROUP_BY copy()
	{
		return new GROUP_BY(this);
	}
	/**
	 * @param alreadyCopied
	 * @return
	 */
	@Override
	protected GROUP_BY copy(final HashMap<SqlClause<?>, SqlClause<?>> alreadyCopied)
	{
		// no references to other clauses present in this class that would have to be registered
		return new GROUP_BY(this);
	}
	
	
	
	///////////////////////////////////////////////////////////////////////////
	// declared methods //
	/////////////////////
	/**
	 * List start.
	 * 
	 * @return the string
	 */
	protected String listStart() {
		return "";
	}
	
	/**
	 * List end.
	 * 
	 * @return the string
	 */
	protected String listEnd() {
		return "";
	}

}
