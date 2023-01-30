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

import com.xdev.jadoth.lang.Copyable;
import com.xdev.jadoth.sqlengine.dbms.DbmsDMLAssembler;
import com.xdev.jadoth.sqlengine.dbms.standard.StandardDMLAssembler;


/**
 * The Class SqlCondition.
 * 
 * @author Thomas Muenz
 */
public class SqlCondition extends QueryPart implements Copyable
{
	///////////////////////////////////////////////////////////////////////////
	// constants        //
	/////////////////////
	/**
	 * 
	 */
	private static final long serialVersionUID = 4216718405076659019L;

	
	
	///////////////////////////////////////////////////////////////////////////
	// instance fields  //
	/////////////////////
	/** The content. */
	protected Object content;
	
	/** The next condition. */
	protected SqlBooleanArithmeticCondition nextCondition = null;



	///////////////////////////////////////////////////////////////////////////
	// constructors     //
	/////////////////////

	/**
	 * Instantiates a new sql condition.
	 * 
	 * @param content the content
	 */
	public SqlCondition(final Object content) 
	{
		super();
		this.content = content;
	}
	
	protected SqlCondition(final SqlCondition copySource, final SqlBooleanArithmeticCondition nextCondition)
	{
		super();
		this.content = copySource; //copy only reference because SqlField etc shall NOT be copied!
		this.nextCondition = nextCondition; //copiing this would cause inconsistency
	}



	///////////////////////////////////////////////////////////////////////////
	// getters          //
	/////////////////////

	/**
	 * Gets the next condition.
	 * 
	 * @return the nextCondition
	 */
	public SqlBooleanArithmeticCondition getNextCondition() {
		return this.nextCondition;
	}

	

	///////////////////////////////////////////////////////////////////////////
	// declared methods //
	/////////////////////

	/**
	 * Assemble intern.
	 * 
	 * @param dmlAssembler the dml assembler
	 * @param sb the sb
	 * @param indentLevel the indent level
	 * @param flags the flags
	 */
	protected void assembleIntern(
		final DbmsDMLAssembler<?> dmlAssembler, final StringBuilder sb, final int indentLevel, final int flags
	)
	{
		assembleObject(this.content, dmlAssembler,sb, indentLevel, flags);
	}


	/**
	 * Assemble next condition.
	 * 
	 * @param dmlAssembler the dml assembler
	 * @param sb the sb
	 * @param indentLevel the indent level
	 * @param flags the flags
	 */
	protected void assembleNextCondition(
		final DbmsDMLAssembler<?> dmlAssembler,
		final StringBuilder sb,
		final int indentLevel,
		final int flags
	)
	{
		if(this.nextCondition != null) {
			this.nextCondition.assembleArithmetic(
				dmlAssembler, sb, indentLevel, flags, false
			);
		}
	}

	/**
	 * @param dmlAssembler
	 * @param sb
	 * @param indentLevel
	 * @param flags
	 * @return
	 * @see com.xdev.jadoth.sqlengine.internal.QueryPart#assemble(com.xdev.jadoth.sqlengine.dbms.DbmsDMLAssembler, java.lang.StringBuilder, int, int)
	 */
	@Override
	protected StringBuilder assemble(
		final DbmsDMLAssembler<?> dmlAssembler, final StringBuilder sb, final int indentLevel, final int flags
	)
	{
		this.assembleIntern(dmlAssembler, sb, indentLevel, flags);
		this.assembleNextCondition(dmlAssembler, sb, indentLevel, flags);
		return sb;
	}


	/**
	 * OR.
	 * 
	 * @param orCondition the or condition
	 * @return the sql condition
	 */
	public SqlCondition OR(final Object orCondition)
	{
		return new SqlBooleanTerm(this, new OR(orCondition));
	}

	/**
	 * AND.
	 * 
	 * @param andCondition the and condition
	 * @return the sql boolean term
	 */
	public SqlBooleanTerm AND(final Object andCondition)
	{
		return new SqlBooleanTerm(this, new AND(andCondition));
	}



	///////////////////////////////////////////////////////////////////////////
	// override methods //
	/////////////////////

	/**
	 * @return
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return this.assemble(
			StandardDMLAssembler.getSingletonStandardDMLAssembler(),
			new StringBuilder(defaultExpressionStringBuilderLength),
			0,
			0
		).toString();
	}

	/**
	 * Note that this does CANNOT copy the <code>nextCondition</code> internally due to consistency reasons.
	 * @return a copy of this <code>SqlCondition</code> WITHOUT the code>nextCondition</code>
	 */
	@Override
	public SqlCondition copy()
	{
		return new SqlCondition(this, null);
	}
	
	protected static SqlCondition copy(final SqlCondition sqlConditionToCopy, final Boolean copyDeepNextCondition)
	{		
		SqlBooleanArithmeticCondition nextCondition = null;
		if(copyDeepNextCondition != null && sqlConditionToCopy.nextCondition != null){
			if(copyDeepNextCondition){
				nextCondition = sqlConditionToCopy.nextCondition.copy(copyDeepNextCondition);
			}
			else {
				nextCondition = sqlConditionToCopy.nextCondition;
			}
		}
		return new SqlCondition(sqlConditionToCopy, nextCondition);
	}
	
	public SqlCondition copy(final Boolean copyDeepNextCondition)
	{		
		return copy(this, copyDeepNextCondition);
	}

}
