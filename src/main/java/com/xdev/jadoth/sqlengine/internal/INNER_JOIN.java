
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
import com.xdev.jadoth.sqlengine.internal.interfaces.TableExpression;



/**
 * The Class INNER_JOIN.
 * 
 * @author Thomas Muenz
 */
public class INNER_JOIN extends JoinClause
{	
	///////////////////////////////////////////////////////////////////////////
	// constants        //
	/////////////////////
	/**
	 * 
	 */
	private static final long serialVersionUID = -616628123818560823L;



	///////////////////////////////////////////////////////////////////////////
	// constructors //
	/////////////////	
	/**
	 * Instantiates a new iNNE r_ join.
	 */
	protected INNER_JOIN() 
	{
		super(null, null, null);
	}
	
	/**
	 * Instantiates a new iNNE r_ join.
	 * 
	 * @param table the table
	 * @param joinCondition the join condition
	 */
	public INNER_JOIN(final TableExpression table, final Object joinCondition) 
	{
		super(table, joinCondition);
	}
	
	/**
	 * Instantiates a new iNNE r_ join.
	 * 
	 * @param parentFromClause the parent from clause
	 * @param table the table
	 * @param joinCondition the join condition
	 */
	public INNER_JOIN(final FROM parentFromClause, final TableExpression table, final Object joinCondition) 
	{
		super(parentFromClause, table, joinCondition);
	}
	
	protected INNER_JOIN(final INNER_JOIN copySource, final FROM parentFromClause)
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
		return SQL.LANG.INNER_JOIN;
	}

	/**
	 * @return
	 */
	@Override
	public INNER_JOIN copy()
	{
		return new INNER_JOIN(this, null);
	}
	/**
	 * @param alreadyCopied
	 * @return
	 */
	@Override
	protected INNER_JOIN copy(final HashMap<SqlClause<?>, SqlClause<?>> alreadyCopied)
	{
		return new INNER_JOIN(this, copySqlClause(this.parentFromClause, alreadyCopied));
	}
	
}
