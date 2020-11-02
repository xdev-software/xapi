
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

import static com.xdev.jadoth.sqlengine.SQL.Punctuation.par;
import static com.xdev.jadoth.sqlengine.SQL.Punctuation.rap;

import com.xdev.jadoth.sqlengine.dbms.DbmsDMLAssembler;



/**
 * The Class SqlBooleanTerm.
 */
public class SqlBooleanTerm extends SqlCondition
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -8525019207906910913L;
	///////////////////////////////////////////////////////////////////////////
	// instance fields  //
	/////////////////////
	/** The parenthesis. */
	private boolean parenthesis;



	///////////////////////////////////////////////////////////////////////////
	// constructors     //
	/////////////////////
	/**
	 * Instantiates a new sql boolean term.
	 * 
	 * @param expression the expression
	 */
	public SqlBooleanTerm(final Object expression) {
		this(expression, null);
	}
	
	/**
	 * Instantiates a new sql boolean term.
	 * 
	 * @param expression the expression
	 * @param nextCondition the next condition
	 */
	public SqlBooleanTerm(final Object expression, final SqlBooleanArithmeticCondition nextCondition) {
		this(expression, nextCondition, false);
	}
	
	/**
	 * Instantiates a new sql boolean term.
	 * 
	 * @param expression the expression
	 * @param nextCondition the next condition
	 * @param parenthesis the parenthesis
	 */
	public SqlBooleanTerm(final Object expression, final SqlBooleanArithmeticCondition nextCondition, final boolean parenthesis) {
		super(expression);
		this.nextCondition = nextCondition;
		this.parenthesis = parenthesis;
	}



	///////////////////////////////////////////////////////////////////////////
	// getters          //
	/////////////////////
	/**
	 * Checks if is parenthesis.
	 * 
	 * @return the parenthesis
	 */
	public boolean isParenthesis() {
		return this.parenthesis;
	}



	///////////////////////////////////////////////////////////////////////////
	// setters          //
	/////////////////////
	/**
	 * Sets the parenthesis.
	 * 
	 * @param parenthesis the parenthesis to set
	 * @return the sql boolean term
	 */
	public SqlBooleanTerm setParenthesis(final boolean parenthesis) {
		this.parenthesis = parenthesis;
		return this;
	}



	///////////////////////////////////////////////////////////////////////////
	// override methods //
	/////////////////////

	/**
	 * @param dmlAssembler
	 * @param sb
	 * @param indentLevel
	 * @param flags
	 * @return
	 * @see com.xdev.jadoth.sqlengine.internal.SqlCondition#assemble(com.xdev.jadoth.sqlengine.dbms.DbmsDMLAssembler, java.lang.StringBuilder, int, int)
	 */
	@Override
	protected StringBuilder assemble(
		final DbmsDMLAssembler<?> dmlAssembler, final StringBuilder sb, final int indentLevel, final int flags
	)
	{
		if(this.parenthesis) {
			sb.append(par);
		}
		this.assembleIntern(dmlAssembler, sb, indentLevel, flags);
		if(this.parenthesis) {
			sb.append(rap);
		}
		this.assembleNextCondition(dmlAssembler, sb, indentLevel, flags);
		return sb;
	}





}
