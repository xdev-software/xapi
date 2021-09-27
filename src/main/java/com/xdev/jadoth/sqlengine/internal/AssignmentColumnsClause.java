
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
import java.util.Iterator;



/**
 * The Class AssignmentValuesClause.
 * 
 * @author Thomas Muenz
 */
public class AssignmentColumnsClause extends ListClauseEnclosed<SqlColumn>
{
	///////////////////////////////////////////////////////////////////////////
	// constants        //
	/////////////////////
	/**
	 * 
	 */
	private static final long serialVersionUID = 3036187859271043120L;

	
	
	///////////////////////////////////////////////////////////////////////////
	// instance fields //
	////////////////////
	private final Iterable<SqlColumn> columns;
	
	
	
	///////////////////////////////////////////////////////////////////////////
	// constructors //
	/////////////////
	/**
	 * Instantiates a new assignment values clause.
	 */
	public AssignmentColumnsClause(final Iterable<SqlColumn> columns)
	{
		super((SqlColumn[])null);
		this.columns = columns;
	}
	protected AssignmentColumnsClause(final AssignmentColumnsClause copySource)
	{
		super(copySource);
		this.columns = copySource.columns;
	}

	

	///////////////////////////////////////////////////////////////////////////
	// getters          //
	/////////////////////
	/**
	 * @return
	 * @see com.xdev.jadoth.sqlengine.internal.SqlClause#keyword()
	 */
	@Override
	public String keyword() 
	{
		return null;
	}
	
	
	
	///////////////////////////////////////////////////////////////////////////
	// override methods //
	/////////////////////
	/**
	 * 
	 */
	@Override
	public Iterator<SqlColumn> iterator()
	{
		return this.columns.iterator();
	}	
	/**
	 * @return
	 */
	@Override
	public AssignmentColumnsClause copy()
	{
		return new AssignmentColumnsClause(this);
	}
	/**
	 * @param alreadyCopied
	 * @return
	 */
	@Override
	protected AssignmentColumnsClause copy(final HashMap<SqlClause<?>, SqlClause<?>> alreadyCopied)
	{
		// no references to other clauses present in this class that would have to be registered
		return new AssignmentColumnsClause(this);
	}

}
