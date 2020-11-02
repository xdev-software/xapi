
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

import static com.xdev.jadoth.sqlengine.SQL.LANG.CASE;
import static com.xdev.jadoth.sqlengine.SQL.LANG.ELSE;
import static com.xdev.jadoth.sqlengine.SQL.LANG.END;
import static com.xdev.jadoth.sqlengine.SQL.LANG.THEN;
import static com.xdev.jadoth.sqlengine.SQL.LANG.WHEN;
import static com.xdev.jadoth.sqlengine.SQL.Punctuation.NEW_LINE;
import static com.xdev.jadoth.sqlengine.SQL.Punctuation._;

import java.util.ArrayList;

import com.xdev.jadoth.sqlengine.dbms.DbmsDMLAssembler;



/**
 * The Class SqlFunctionCASE.
 */
public class SqlFunctionCASE extends SqlExpression
{
	///////////////////////////////////////////////////////////////////////////
	// constants        //
	/////////////////////

	/**
	 * 
	 */
	private static final long serialVersionUID = -3584146654417727160L;

	/** The Constant WHEN_. */
	protected static final String WHEN_ = WHEN+_;
	
	/** The Constant _THEN_. */
	protected static final String _THEN_ = _+THEN+_;
	
	/** The Constant ELSE_. */
	protected static final String ELSE_ = ELSE+_;

	///////////////////////////////////////////////////////////////////////////
	// instance fields  //
	/////////////////////
	/** The whens. */
	private ArrayList<Object[]> whens = new ArrayList<Object[]>(10);
	
	/** The else expression. */
	private Object elseExpression = null;



	///////////////////////////////////////////////////////////////////////////
	// constructors     //
	/////////////////////
	/**
	 * Instantiates a new sql function case.
	 */
	public SqlFunctionCASE() {
		super((Object)null);
	}

	/**
	 * Instantiates a new sql function case.
	 * 
	 * @param expression the expression
	 */
	public SqlFunctionCASE(final Object expression) {
		super(expression);
	}



	///////////////////////////////////////////////////////////////////////////
	// declared methods //
	/////////////////////
	/**
	 * WHEN.
	 * 
	 * @param whenExpression the when expression
	 * @param thenExpression the then expression
	 * @return the sql function case
	 */
	public SqlFunctionCASE WHEN(final Object whenExpression, final Object thenExpression)
	{
		this.whens.add(new Object[] {whenExpression, thenExpression});
		return this;
	}

	/**
	 * ELSE.
	 * 
	 * @param elseExpression the else expression
	 * @return the sql function case
	 */
	public SqlFunctionCASE ELSE(final Object elseExpression)
	{
		this.elseExpression = elseExpression;
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
	 * @see com.xdev.jadoth.sqlengine.internal.SqlExpression#assemble(com.xdev.jadoth.sqlengine.dbms.DbmsDMLAssembler, java.lang.StringBuilder, int, int)
	 */
	@Override
	protected StringBuilder assemble(
		final DbmsDMLAssembler<?> dmlAssembler, final StringBuilder sb, final int indentLevel, final int flags
	)
	{
		final boolean newLines = this.expression != null && !isSingleLine(flags) && this.whens.size() > 1;
		final int innerIndent = indentLevel+1;

		indent(sb, indentLevel, isSingleLine(flags));

		final int passFlags = flags | ASEXPRESSION;
		sb.append(CASE);
		if(this.expression != null) {
			sb.append(_);
			assembleObject(this.expression, dmlAssembler, sb, indentLevel, passFlags);
		}
		for (final Object[] whens : this.whens.toArray(new Object[this.whens.size()][])) {
			if(newLines) {
				sb.append(NEW_LINE);
				indent(sb, innerIndent, false);
			}
			else {
				sb.append(_);
			}
			sb.append(WHEN_);
			assembleObject(whens[0], dmlAssembler, sb, indentLevel, passFlags);
//			assembleExpression(whens[0], sb, singleLine, indentLevel, packed, false);
			sb.append(_THEN_);
			assembleObject(whens[1], dmlAssembler, sb, indentLevel, passFlags);
//			assembleExpression(whens[1], sb, singleLine, indentLevel, packed, false);
		}
		if(this.elseExpression != null) {
			if(newLines) {
				sb.append(NEW_LINE);
				indent(sb, indentLevel, false).append("  ");
			}
			else {
				sb.append(_);
			}
			sb.append(ELSE_);
//			assembleExpression(elseExpression, sb, singleLine, indentLevel, packed, false);
			assembleObject(this.elseExpression, dmlAssembler, sb, indentLevel, passFlags);
		}

		if(newLines) {
			sb.append(NEW_LINE);
			indent(sb, indentLevel, false).append("  ");
		}
		else {
			sb.append(_);
		}
		sb.append(END);

		return sb;
	}



}
