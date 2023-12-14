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

import static com.xdev.jadoth.sqlengine.SQL.Punctuation.comma_;



/**
 * The Class ListClause.
 * 
 * @author Thomas Muenz
 */
public abstract class ListClause<E> extends SqlClause<E>
{
	///////////////////////////////////////////////////////////////////////////
	// constants        //
	/////////////////////	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7901230492547630047L;


	
	///////////////////////////////////////////////////////////////////////////
	// constructors //
	/////////////////
	/**
	 * Instantiates a new list clause.
	 * 
	 * @param values the values
	 */
	public ListClause(final E... values)
	{
		super();
		this.bodyElementSeperator = comma_;
		this.indentationAllowed = false;
		if(values != null) {
			for (final E o : values) {
				this.body.add(o);
			}
		}
	}
	protected ListClause(final ListClause<E> copySource)
	{
		super(copySource);
	}

	
	
	///////////////////////////////////////////////////////////////////////////
	// getters          //
	/////////////////////	
	/**
	 * @param singleLineMode
	 * @return
	 * @see com.xdev.jadoth.sqlengine.internal.SqlClause#getBodyElementSeperator(boolean)
	 */
	@Override
	public String getBodyElementSeperator(final boolean singleLineMode) 
	{
		//always return bodyElementSeperator because in this class it contains no linebreak (just ", ")
		return this.bodyElementSeperator;
	}

	
	
	///////////////////////////////////////////////////////////////////////////
	// declared methods //
	/////////////////////
	
	/**
	 * Adds the.
	 * 
	 * @param value the value
	 */
	public void add(final E value)
	{
		if(value == null) return;
		this.body.add(value);
	}
	
}
