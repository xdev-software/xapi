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

import com.xdev.jadoth.sqlengine.SQL;
import com.xdev.jadoth.sqlengine.internal.interfaces.Conditional;


/**
 * The Class ConditionClause.
 * 
 * @param <C> the generic type
 * @author Thomas Muenz
 */
public abstract class ConditionClause<C extends ConditionClause<C>> extends SqlClause<SqlCondition>
	implements Conditional<C>
{
	///////////////////////////////////////////////////////////////////////////
	// constants        //
	/////////////////////
	/**
	 * 
	 */
	private static final long serialVersionUID = -3642728899754205222L;
	
	
	private static final SqlCondition unwrapBooleanArithmeticCondition(final SqlCondition sqlCondition)
	{
		if(sqlCondition instanceof SqlBooleanArithmeticCondition){
			return ((SqlBooleanArithmeticCondition)sqlCondition).getCondition();
		}
		return sqlCondition;
	}
	
	///////////////////////////////////////////////////////////////////////////
	// constructors //
	/////////////////
	/**
	 * Instantiates a new condition clause.
	 */
	public ConditionClause() {
		super();
	}

	/**
	 * Instantiates a new condition clause.
	 * 
	 * @param firstCondition the first condition
	 */
	public ConditionClause(final Object firstCondition) {
		super(unwrapBooleanArithmeticCondition(SQL.util.parseCondition(firstCondition)));
	}
	
	protected ConditionClause(final ConditionClause<C> copySource)
	{
		super(copySource);
	}


	/**
	 * @param singleLineMode
	 * @return
	 * @see com.xdev.jadoth.sqlengine.internal.SqlClause#getBodyElementSeperator(boolean)
	 */
	@Override
	public String getBodyElementSeperator(final boolean singleLineMode) {
		//AND/OR handle their trailing space by themselves
		return singleLineMode?"":this.bodyElementSeperator;
	}


	/**
	 * Adds the condition.
	 * 
	 * @param conditionClause the condition clause
	 * @return the c
	 */
	@SuppressWarnings("unchecked")
	protected C addCondition(final SqlBooleanArithmeticCondition conditionClause)
	{
		this.body.add(conditionClause);
		return (C)this;
	}



	/**
	 * @param condition
	 * @return
	 * @see com.xdev.jadoth.sqlengine.internal.interfaces.Conditional#AND(java.lang.Object)
	 */
	@Override
	@SuppressWarnings("unchecked")
	public C AND(final Object condition)
	{
		if(this.body.isEmpty()){
			this.body.add(SQL.util.parseCondition(condition));
			return (C)this;
		}
		return this.addCondition((AND)(condition instanceof AND?condition:new AND(condition)));
	}

	/**
	 * @param condition
	 * @return
	 * @see com.xdev.jadoth.sqlengine.internal.interfaces.Conditional#OR(java.lang.Object)
	 */
	@Override
	@SuppressWarnings("unchecked")
	public C OR(final Object condition)
	{
		if(this.body.isEmpty()){
			this.body.add(SQL.util.parseCondition(condition));
			return (C)this;
		}
		return this.addCondition((OR)(condition instanceof OR?condition:new OR(condition)));
	}

}
