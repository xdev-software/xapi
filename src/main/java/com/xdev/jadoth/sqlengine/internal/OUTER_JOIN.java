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
import com.xdev.jadoth.sqlengine.internal.interfaces.TableExpression;



/**
 * The Class OUTER_JOIN.
 * 
 * @author Thomas Muenz
 */
public class OUTER_JOIN extends JoinClause
{
	///////////////////////////////////////////////////////////////////////////
	// constants        //
	/////////////////////
	/**
	 * 
	 */
	private static final long serialVersionUID = 6129347170948479943L;

	
	
	///////////////////////////////////////////////////////////////////////////
	// constructors //
	/////////////////
	/**
	 * Instantiates a new oUTE r_ join.
	 */
	public OUTER_JOIN() {
		super(null, null, null);
	}	
	/**
	 * Instantiates a new oUTE r_ join.
	 * 
	 * @param table the table
	 * @param joinCondition the join condition
	 */
	public OUTER_JOIN(final TableExpression table, final Object joinCondition) {
		super(table, joinCondition);
	}	
	/**
	 * Instantiates a new oUTE r_ join.
	 * 
	 * @param parentFromClause the parent from clause
	 * @param table the table
	 * @param joinCondition the join condition
	 */
	public OUTER_JOIN(final FROM parentFromClause, final TableExpression table, final Object joinCondition) {
		super(parentFromClause, table, joinCondition);
	}
	/**
	 * 
	 * @param copySource
	 * @param parentFromClause
	 */
	protected OUTER_JOIN(final OUTER_JOIN copySource, final FROM parentFromClause)
	{
		super(copySource, parentFromClause);
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
		return SQL.LANG.OUTER_JOIN;
	}

	
	
	///////////////////////////////////////////////////////////////////////////
	// override methods //
	/////////////////////
	/**
	 * @return
	 */
	@Override
	public OUTER_JOIN copy()
	{
		return new OUTER_JOIN(this, null);
	}
	/**
	 * @param alreadyCopied
	 * @return
	 */
	@Override
	protected OUTER_JOIN copy(final HashMap<SqlClause<?>, SqlClause<?>> alreadyCopied)
	{
		return new OUTER_JOIN(this, copySqlClause(this.parentFromClause, alreadyCopied));
	}

}
