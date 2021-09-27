
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
import java.util.List;

import com.xdev.jadoth.collections.GrowList;
import com.xdev.jadoth.sqlengine.SQL;
import com.xdev.jadoth.sqlengine.internal.interfaces.JoinContainer;
import com.xdev.jadoth.sqlengine.internal.interfaces.Joining;
import com.xdev.jadoth.sqlengine.internal.interfaces.TableClause;
import com.xdev.jadoth.sqlengine.internal.interfaces.TableExpression;
import com.xdev.jadoth.sqlengine.internal.tables.SqlTableIdentity;


/**
 * The Class FROM.
 * 
 * @author Thomas Muenz
 */
public class FROM extends SqlClause<JoinClause> implements TableClause, Joining<JoinClause>, JoinContainer<FROM>
{
	///////////////////////////////////////////////////////////////////////////
	// constants        //
	/////////////////////

	private static final long serialVersionUID = 3857247683297979348L;

	
	
	///////////////////////////////////////////////////////////////////////////
	// instance fields //
	////////////////////
	
	/** The table. */
	private TableExpression table;

	/** The joins. */
	private GrowList<JoinClause> joins = null;

	

	///////////////////////////////////////////////////////////////////////////
	// constructors //
	/////////////////
	
	/**
	 * Instantiates a new fROM.
	 * 
	 * @param table the table
	 */
	public FROM(final TableExpression table) 
	{
		super();
		this.table = table;
	}
	
	protected FROM(final FROM copySource, final HashMap<SqlClause<?>, SqlClause<?>> alreadyCopied)
	{
		super(copySource);
		this.table = copySource.table; //intentionally NOT copy the table (or SELECT) object!
		
		if(copySource.joins != null){
			for (final JoinClause j : copySource.joins) {
				if(j == null) continue;
				this.registerJoinClause(copySqlClause(j.copy(), alreadyCopied));
			}			
		}		
	}


	//////////////////////////////////////////////s/////////////////////////////
	// getters          //
	/////////////////////
	
	/**
	 * Getter of the property <tt>table</tt>.
	 * 
	 * @return Returns the table.
	 */
	public TableExpression getTable() 
	{
		return this.table;
	}
	
	/**
	 * Getter of the property <tt>alias</tt>.
	 * 
	 * @return Returns the alias.
	 */
	public String getAlias() 
	{
		return TableExpression.Utils.getAlias(this.table);
	}


	///////////////////////////////////////////////////////////////////////////
	// setters          //
	/////////////////////
	/**
	 * Setter of the property <tt>table</tt>.
	 * 
	 * @param table The table to set.
	 */
	public void setTable(final TableExpression table) 
	{
		this.table = table;
	}
	


	/**
	 * @return
	 * @see com.xdev.jadoth.sqlengine.internal.SqlClause#keyword()
	 */
	@Override
	public String keyword() 
	{
		return SQL.LANG.FROM;
	}


	/**
	 * Gets the clause tables.
	 * 
	 * @return the clause tables
	 */
	public List<SqlTableIdentity> getClauseTables()
	{
		final GrowList<SqlTableIdentity> allTables = new GrowList<SqlTableIdentity>();
		if(this.table instanceof SqlTableIdentity) {
			allTables.add((SqlTableIdentity)this.table);
		}
		if(this.joins != null){
			for (final JoinClause join : this.joins) {
				final TableExpression table = join.getTable();
				if(table instanceof SqlTableIdentity) {
					allTables.add((SqlTableIdentity)table);
				}
			}
		}		
		return allTables;
	}

	/**
	 * @param <C>
	 * @param newJoin
	 * @return
	 * @see com.xdev.jadoth.sqlengine.internal.interfaces.JoinContainer#registerJoinClause(com.xdev.jadoth.sqlengine.internal.JoinClause)
	 */
	public final <C extends JoinClause> C registerJoinClause(final C newJoin)
	{
		if(this.joins == null){
			this.joins = new GrowList<JoinClause>(8);
		}
		newJoin.setParentFromClause(this);
		this.joins.add(newJoin);
		return newJoin;
	}

	/**
	 * @param newJoin
	 * @return
	 * @see com.xdev.jadoth.sqlengine.internal.interfaces.JoinContainer#addJoinClause(com.xdev.jadoth.sqlengine.internal.JoinClause)
	 */
	@Override
	public final FROM addJoinClause(final JoinClause newJoin)
	{
		this.registerJoinClause(newJoin);
		return this;
	}
	
	/**
	 * @param table
	 * @param joinCondition
	 * @return
	 * @see com.xdev.jadoth.sqlengine.internal.interfaces.Joining#INNER_JOIN(com.xdev.jadoth.sqlengine.internal.interfaces.TableExpression, java.lang.Object)
	 */
	@Override
	public JoinClause INNER_JOIN(final TableExpression table, final Object joinCondition) 
	{
		return this.registerJoinClause(new INNER_JOIN(this, table, joinCondition));
	}
	
	/**
	 * @param table
	 * @param joinCondition
	 * @return
	 * @see com.xdev.jadoth.sqlengine.internal.interfaces.Joining#LEFT_JOIN(com.xdev.jadoth.sqlengine.internal.interfaces.TableExpression, java.lang.Object)
	 */
	@Override
	public JoinClause LEFT_JOIN(final TableExpression table, final Object joinCondition) 
	{
		return this.registerJoinClause(new LEFT_JOIN(this, table, joinCondition));
	}
	
	/**
	 * @param table
	 * @param joinCondition
	 * @return
	 * @see com.xdev.jadoth.sqlengine.internal.interfaces.Joining#RIGHT_JOIN(com.xdev.jadoth.sqlengine.internal.interfaces.TableExpression, java.lang.Object)
	 */
	@Override
	public JoinClause RIGHT_JOIN(final TableExpression table, final Object joinCondition) 
	{
		return this.registerJoinClause(new RIGHT_JOIN(this, table, joinCondition));
	}
	
	/**
	 * @param table
	 * @param joinCondition
	 * @return
	 * @see com.xdev.jadoth.sqlengine.internal.interfaces.Joining#OUTER_JOIN(com.xdev.jadoth.sqlengine.internal.interfaces.TableExpression, java.lang.Object)
	 */
	@Override
	public JoinClause OUTER_JOIN(final TableExpression table, final Object joinCondition) 
	{
		return this.registerJoinClause(new OUTER_JOIN(this, table, joinCondition));
	}

	/**
	 * @return
	 * @see com.xdev.jadoth.sqlengine.internal.interfaces.JoinContainer#getJoinCount()
	 */
	@Override
	public int getJoinCount() 
	{
		return this.joins==null?0:this.joins.size();
	}

	/**
	 * @return
	 * @see com.xdev.jadoth.sqlengine.internal.interfaces.JoinContainer#getJoins()
	 */
	@Override
	public List<JoinClause> getJoins() 
	{
		return this.joins;
	}

	/**
	 * @param joins
	 * @return
	 * @see com.xdev.jadoth.sqlengine.internal.interfaces.JoinContainer#setJoins(java.util.List)
	 */
	@Override
	public FROM setJoins(final List<JoinClause> joins) 
	{
		if(joins == null){
			throw new NullPointerException("joins may not be null");
		}
		if(this.joins == null){
			this.joins = new GrowList<JoinClause>(8);
		}
		else {
			this.joins.clear();
		}		
		this.joins.addAll(joins);
		return this;
	}

	/**
	 * Gets the last join.
	 * 
	 * @return the last join
	 */
	public JoinClause getLastJoin()
	{
		if(this.joins == null) return null;

		int i = this.joins.size()-1;
		JoinClause loopJoin = null;
		while(i --> 0){
			loopJoin = this.joins.get(i);
			if(loopJoin != null) break;
		}
		return loopJoin;
	}

	/**
	 * @return
	 */
	@Override
	public FROM copy()
	{
		return new FROM(this, new HashMap<SqlClause<?>, SqlClause<?>>());
	}

	/**
	 * @param alreadyCopied
	 * @return
	 */
	@Override
	protected FROM copy(final HashMap<SqlClause<?>, SqlClause<?>> alreadyCopied)
	{
		return new FROM(this, alreadyCopied);
	}

}
