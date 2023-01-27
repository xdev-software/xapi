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
import java.util.Iterator;

import com.xdev.jadoth.sqlengine.SQL;



/**
 * The Class AssignmentValuesClause.
 * 
 * @author Thomas Muenz
 */
public class AssignmentValuesClause extends ListClauseEnclosed<Object>
{	
	///////////////////////////////////////////////////////////////////////////
	// constants        //
	/////////////////////
	/**
	 * 
	 */
	private static final long serialVersionUID = 722361055359758590L;

	
	
	///////////////////////////////////////////////////////////////////////////
	// instance fields //
	////////////////////
	private final Iterable<Object> values;
	
	
	
	///////////////////////////////////////////////////////////////////////////
	// constructors //
	/////////////////
	/**
	 * Instantiates a new assignment values clause.
	 */
	public AssignmentValuesClause(final Iterable<Object> values){
		super((Object[])null);
		this.values = values;
	}
	
	protected AssignmentValuesClause(final AssignmentValuesClause copySource)
	{
		super(copySource);
		this.values = copySource.values;
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
		return SQL.LANG.VALUES;
	}
	

	
	///////////////////////////////////////////////////////////////////////////
	// override methods //
	/////////////////////
	
	@Override
	public Iterator<Object> iterator()
	{
		return this.values.iterator();
	}

	/**
	 * @return
	 */
	@Override
	public AssignmentValuesClause copy()
	{
		return new AssignmentValuesClause(this);
	}
	/**
	 * @param alreadyCopied
	 * @return
	 */
	@Override
	protected AssignmentValuesClause copy(final HashMap<SqlClause<?>, SqlClause<?>> alreadyCopied)
	{
		// no references to other clauses present in this class that would have to be registered
		return new AssignmentValuesClause(this);
	}

}
