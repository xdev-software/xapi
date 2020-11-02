
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

import static com.xdev.jadoth.sqlengine.SQL.Punctuation._;
import static com.xdev.jadoth.sqlengine.SQL.Punctuation.comma_;

import com.xdev.jadoth.sqlengine.SQL;
import com.xdev.jadoth.sqlengine.dbms.DbmsDMLAssembler;


/**
 * The Class SqlComparison.
 * 
 * @author Thomas Muenz
 */
public class SqlComparison extends SqlCondition {

	/**
	 * 
	 */
	private static final long serialVersionUID = -774576967971093931L;

	/** The lefthand expression. */
	protected SqlExpression lefthandExpression;
	
	/** The righthand expressions. */
	protected SqlExpression[] righthandExpressions;

	/**
	 * Instantiates a new sql comparison.
	 * 
	 * @param lefthandExpression the lefthand expression
	 * @param operator the operator
	 * @param righthandExpression the righthand expression
	 */
	public SqlComparison(final Object lefthandExpression, final String operator, final Object... righthandExpression) 
	{
		super(operator);
		this.lefthandExpression = SQL.util.parseExpression(lefthandExpression);
		if(righthandExpression instanceof SqlExpression[]) {
			this.righthandExpressions = (SqlExpression[])righthandExpression;
		}
		else {
			this.righthandExpressions = SQL.util.parseExpressionArray(righthandExpression);
		}
	}


	/**
	 * Assemble righthand expression.
	 * 
	 * @param dmlAssembler the dml assembler
	 * @param sb the sb
	 * @param indentLevel the indent level
	 * @param flags the flags
	 * @return the string builder
	 */
	protected StringBuilder assembleRighthandExpression(
		final DbmsDMLAssembler<?> dmlAssembler,
		final StringBuilder sb,
		final int indentLevel,
		final int flags
	)
	{
		QueryPart.concatSqlExpressions(dmlAssembler, sb, indentLevel, flags,comma_, this.righthandExpressions);
		return sb;
	}



	/**
	 * @param dmlAssembler
	 * @param sb
	 * @param indentLevel
	 * @param flags
	 * @see com.xdev.jadoth.sqlengine.internal.SqlCondition#assembleIntern(com.xdev.jadoth.sqlengine.dbms.DbmsDMLAssembler, java.lang.StringBuilder, int, int)
	 */
	@Override
	protected void assembleIntern(
		final DbmsDMLAssembler<?> dmlAssembler,
		final StringBuilder sb,
		final int indentLevel,
		final int flags
	) {
		if(this.lefthandExpression != null) {
			this.lefthandExpression.assemble(dmlAssembler, sb, indentLevel, flags).append(_);
		}
		super.assembleIntern(dmlAssembler, sb, indentLevel, flags);
		sb.append(_);
		this.assembleRighthandExpression(
			dmlAssembler, sb, indentLevel, flags
		);
		if(this.nextCondition != null) {
			sb.append(_);
			this.nextCondition.assemble(
				dmlAssembler, sb, indentLevel, flags
			);
		}
	}






}
