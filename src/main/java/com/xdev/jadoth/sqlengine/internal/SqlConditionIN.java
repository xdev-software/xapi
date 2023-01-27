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

import static com.xdev.jadoth.sqlengine.SQL.LANG.IN;
import static com.xdev.jadoth.sqlengine.SQL.Punctuation.par;
import static com.xdev.jadoth.sqlengine.SQL.Punctuation.rap;

import com.xdev.jadoth.sqlengine.dbms.DbmsDMLAssembler;


/**
 * The Class SqlConditionIN.
 * 
 * @author Thomas Muenz
 */
public class SqlConditionIN extends SqlComparison {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2884155585264185865L;

	/**
	 * Instantiates a new sql condition in.
	 * 
	 * @param lefthandExpression the lefthand expression
	 * @param innerExpression the inner expression
	 */
	public SqlConditionIN(final Object lefthandExpression, final Object... innerExpression) {
		super(lefthandExpression, IN, innerExpression);
	}

	/**
	 * @param dmlAssembler
	 * @param sb
	 * @param indentLevel
	 * @param flags
	 * @return
	 * @see com.xdev.jadoth.sqlengine.internal.SqlComparison#assembleRighthandExpression(com.xdev.jadoth.sqlengine.dbms.DbmsDMLAssembler, java.lang.StringBuilder, int, int)
	 */
	@Override
	protected StringBuilder assembleRighthandExpression(
		final DbmsDMLAssembler<?> dmlAssembler,
		final StringBuilder sb,
		final int indentLevel,
		final int flags
	)
	{
		sb.append(par);
		super.assembleRighthandExpression(dmlAssembler, sb, indentLevel, flags);
		sb.append(rap);
		return sb;
	}

}
