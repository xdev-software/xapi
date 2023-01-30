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


import static com.xdev.jadoth.sqlengine.SQL.Punctuation.NEW_LINE;
import static com.xdev.jadoth.sqlengine.SQL.Punctuation.comma_;

import java.util.HashMap;
import java.util.Iterator;

import com.xdev.jadoth.sqlengine.SQL;



/**
 * The Class SET.
 * 
 * @author Thomas Muenz
 */
public class SET extends SqlClause<ColumnValueAssignment>
{
	///////////////////////////////////////////////////////////////////////////
	// constants        //
	/////////////////////	
	/**
	 * 
	 */
	private static final long serialVersionUID = -181186259022877739L;
	
	
	
	///////////////////////////////////////////////////////////////////////////
	// instance fields  //
	/////////////////////	
	/** The values. */
	private final Iterable<ColumnValueAssignment> assignments;

	
	
	///////////////////////////////////////////////////////////////////////////
	// constructors     //
	/////////////////////	
	/**
	 * Instantiates a new sets the.
	 */
	public SET(final Iterable<ColumnValueAssignment> assignments) {
		super();
		this.bodyElementSeperator = comma_+NEW_LINE;
		this.keyWordSeperator = NEW_LINE;
		this.assignments = assignments;
		this.indentFirstBodyElement = true;
	}
	
	protected SET(final SET copySource)
	{
		super(copySource);
		this.assignments = copySource.assignments;		
	}

	
	
	///////////////////////////////////////////////////////////////////////////
	// getters          //
	/////////////////////
	/**
	 * 
	 */
	public Iterable<ColumnValueAssignment> getAssignments()
	{
		return this.assignments;
	}
	
	/**
	 * @return
	 * @see com.xdev.jadoth.sqlengine.internal.SqlClause#keyword()
	 */
	@Override
	public String keyword() {
		return SQL.LANG.SET;
	}
	/**
	 * @param singleLineMode
	 * @return
	 * @see com.xdev.jadoth.sqlengine.internal.SqlClause#getBodyElementSeperator(boolean)
	 */
	@Override
	public String getBodyElementSeperator(final boolean singleLineMode) {
		return singleLineMode?comma_:this.bodyElementSeperator;
	}



	///////////////////////////////////////////////////////////////////////////
	// override methods //
	/////////////////////
	/**
	 * @return
	 */
	@Override
	public Iterator<ColumnValueAssignment> iterator()
	{
		return this.assignments.iterator();
	}



	/**
	 * @return
	 */
	@Override
	public SET copy()
	{
		return new SET(this);
	}
	/**
	 * @param alreadyCopied
	 * @return
	 */
	@Override
	protected SET copy(final HashMap<SqlClause<?>, SqlClause<?>> alreadyCopied)
	{
		// no references to other clauses present in this class that would have to be registered
		return new SET(this);
	}

}
