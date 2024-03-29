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
import com.xdev.jadoth.sqlengine.dbms.DbmsDMLAssembler;


/**
 * The Class DoubleQuotedExpression.
 * 
 * @author Thomas Muenz
 */
public class DoubleQuotedExpression extends SqlExpression
{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2985693167788057901L;


	/**
	 * Instantiates a new double quoted expression.
	 * 
	 * @param characterValueExpression the character value expression
	 */
	public DoubleQuotedExpression(final Object characterValueExpression)
	{
		super(characterValueExpression);
	}


	/**
	 * Assemble.
	 * 
	 * @param dmlAssembler the dml assembler
	 * @param sb the sb
	 * @param indentLevel the indent level
	 * @param flags the flags
	 * @return the string builder
	 * @return
	 * @see com.xdev.jadoth.sqlengine.internal.SqlExpression#assemble(com.xdev.jadoth.sqlengine.dbms.DbmsDMLAssembler, java.lang.StringBuilder, int, int)
	 */
	@Override
	protected StringBuilder assemble(
		final DbmsDMLAssembler<?> dmlAssembler, final StringBuilder sb, final int indentLevel, final int flags
	)
	{
		return dmlAssembler.assembleDoubleQuotedExpression(this, sb, indentLevel, flags);
	}


	/**
	 * To string.
	 * 
	 * @return the string
	 * @return
	 * @see com.xdev.jadoth.sqlengine.internal.SqlExpression#toString()
	 */
	@Override
	public String toString()
	{
		return this.assemble(
			SQL.getDefaultDMLAssembler(),
			new StringBuilder(QueryPart.defaultExpressionStringBuilderLength),
			0, ESCAPE_DBL_QUOTES
		).toString();
	}



}
