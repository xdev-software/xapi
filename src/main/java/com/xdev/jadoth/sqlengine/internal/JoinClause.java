
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

import com.xdev.jadoth.sqlengine.SQL;
import com.xdev.jadoth.sqlengine.dbms.DbmsDMLAssembler;
import com.xdev.jadoth.sqlengine.internal.interfaces.Joining;
import com.xdev.jadoth.sqlengine.internal.interfaces.TableClause;
import com.xdev.jadoth.sqlengine.internal.interfaces.TableExpression;
import com.xdev.jadoth.sqlengine.internal.interfaces.TableExpression.Utils;


/**
 * The Class JoinClause.
 * 
 * @author Thomas Muenz
 */
public abstract class JoinClause
extends ConditionClause<JoinClause>
implements TableClause, Joining<JoinClause>
{
	///////////////////////////////////////////////////////////////////////////
	// constants        //
	/////////////////////
	/**
	 * 
	 */
	private static final long serialVersionUID = -215557527553568499L;

	
	
	///////////////////////////////////////////////////////////////////////////
	// instance fields //
	////////////////////
	/** The join condition. */
	protected SqlCondition joinCondition;
	
	/** The table. */
	protected TableExpression table;
	
	/** The alias. */
	protected String alias;
	
	/** The parent from clause. */
	protected FROM parentFromClause;



	///////////////////////////////////////////////////////////////////////////
	// constructors //
	/////////////////
	
	protected JoinClause(final JoinClause copySource, final FROM parentFromClause)
	{
		super(copySource);
		// joinCondition is independent from body list, so copy joinCondition (and its nextCondition) happily on its own
		this.joinCondition = copySource.joinCondition == null ?null :copySource.joinCondition.copy(Boolean.FALSE);
		this.table = copySource.table; //intentionally NOT copy the table (or SELECT) object
		this.alias = copySource.alias; //Strings are immutable
		this.parentFromClause = parentFromClause; //copiing from copySource would cause inconsistency
	}
	/**
	 * Instantiates a new join clause.
	 * 
	 * @param table the table
	 * @param joinCondition the join condition
	 */
	public JoinClause(final TableExpression table, final Object joinCondition) {
		this(null, table, joinCondition);
	}

	/**
	 * Instantiates a new join clause.
	 * 
	 * @param parentFromClause the parent from clause
	 * @param table the table
	 * @param joinCondition the join condition
	 */
	public JoinClause(final FROM parentFromClause, final TableExpression table, final Object joinCondition) {
		super();
		this.table = table;
		this.alias = Utils.getAlias(table);
		this.joinCondition = SQL.util.parseCondition(joinCondition);
		this.parentFromClause = parentFromClause;

		this.bodyElementSeperator = "\n";
	}



	///////////////////////////////////////////////////////////////////////////
	// getters          //
	/////////////////////

	/**
	 * Gets the parent from clause.
	 * 
	 * @return the parentFromClause
	 */
	public FROM getParentFromClause() {
		return this.parentFromClause;
	}
	
	/**
	 * Getter of the property <tt>table</tt>.
	 * 
	 * @return Returns the table.
	 */
	public TableExpression getTable() {
		return this.table;
	}
	
	/**
	 * Getter of the property <tt>alias</tt>.
	 * 
	 * @return Returns the alias.
	 */
	public String getAlias() {
		return this.alias;
	}
	
	/**
	 * Gets the join condition.
	 * 
	 * @return the joinCondition
	 */
	public SqlCondition getJoinCondition() {
		return this.joinCondition;
	}



	///////////////////////////////////////////////////////////////////////////
	// setters          //
	/////////////////////

	/**
	 * Setter of the property <tt>table</tt>.
	 * 
	 * @param table The table to set.
	 */
	public void setTable(final TableExpression table) {
		this.table = table;
	}
	
	/**
	 * Sets the parent from clause.
	 * 
	 * @param parentFromClause the parentFromClause to set
	 */
	public void setParentFromClause(final FROM parentFromClause) {
		this.parentFromClause = parentFromClause;
	}
	
	/**
	 * Setter of the property <tt>alias</tt>.
	 * 
	 * @param alias The alias to set.
	 */
	public void setAlias(final String alias) {
		this.alias = alias;
	}
	
	/**
	 * Sets the join condition.
	 * 
	 * @param joinCondition the joinCondition to set
	 */
	public void setJoinCondition(final SqlCondition joinCondition) {
		this.joinCondition = joinCondition;
	}


	/**
	 * @param table
	 * @param joinCondition
	 * @return
	 * @see com.xdev.jadoth.sqlengine.internal.interfaces.Joining#INNER_JOIN(com.xdev.jadoth.sqlengine.internal.interfaces.TableExpression, java.lang.Object)
	 */
	@Override
	public JoinClause INNER_JOIN(final TableExpression table, final Object joinCondition) {
		return this.parentFromClause.INNER_JOIN(table, joinCondition);
	}
	
	/**
	 * @param table
	 * @param joinCondition
	 * @return
	 * @see com.xdev.jadoth.sqlengine.internal.interfaces.Joining#LEFT_JOIN(com.xdev.jadoth.sqlengine.internal.interfaces.TableExpression, java.lang.Object)
	 */
	@Override
	public JoinClause LEFT_JOIN(final TableExpression table, final Object joinCondition) {
		return this.parentFromClause.LEFT_JOIN(table, joinCondition);
	}
	
	/**
	 * @param table
	 * @param joinCondition
	 * @return
	 * @see com.xdev.jadoth.sqlengine.internal.interfaces.Joining#OUTER_JOIN(com.xdev.jadoth.sqlengine.internal.interfaces.TableExpression, java.lang.Object)
	 */
	@Override
	public JoinClause OUTER_JOIN(final TableExpression table, final Object joinCondition) {
		return this.parentFromClause.OUTER_JOIN(table, joinCondition);
	}
	
	/**
	 * @param table
	 * @param joinCondition
	 * @return
	 * @see com.xdev.jadoth.sqlengine.internal.interfaces.Joining#RIGHT_JOIN(com.xdev.jadoth.sqlengine.internal.interfaces.TableExpression, java.lang.Object)
	 */
	@Override
	public JoinClause RIGHT_JOIN(final TableExpression table, final Object joinCondition) {
		return this.parentFromClause.RIGHT_JOIN(table, joinCondition);
	}

	/**
	 * @param dmlAssembler
	 * @param sb
	 * @param indentLevel
	 * @param flags
	 * @return
	 * @see com.xdev.jadoth.sqlengine.internal.SqlClause#assemble(com.xdev.jadoth.sqlengine.dbms.DbmsDMLAssembler, java.lang.StringBuilder, int, int)
	 */
	@Override
	protected StringBuilder assemble(final DbmsDMLAssembler<?> dmlAssembler, final StringBuilder sb, final int indentLevel, final int flags)
	{
		return dmlAssembler.assembleSqlClause(this, sb, indentLevel, flags);
	}



	/**
	 * @param condition
	 * @return
	 * @see com.xdev.jadoth.sqlengine.internal.ConditionClause#AND(java.lang.Object)
	 */
	@Override
	public JoinClause AND(final Object condition) 
	{
		return this.addCondition((AND)(condition instanceof AND?condition:new AND(condition)));
	}

	/**
	 * @param condition
	 * @return
	 * @see com.xdev.jadoth.sqlengine.internal.ConditionClause#OR(java.lang.Object)
	 */
	@Override
	public JoinClause OR(final Object condition) 
	{
		return this.addCondition((OR)(condition instanceof OR?condition:new OR(condition)));
	}
	
	
	/**
	 * @return
	 */
	/*
	 * Note that parentFromClause consistency is ensured by calling registerJoinClause(joinClause.copy())
	 * when copiing the owning clause
	 */
	@Override
	public abstract JoinClause copy();

}
