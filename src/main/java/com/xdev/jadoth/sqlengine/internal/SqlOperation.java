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

import static com.xdev.jadoth.sqlengine.SQL.Punctuation._;

import com.xdev.jadoth.sqlengine.SQL;
import com.xdev.jadoth.sqlengine.dbms.DbmsDMLAssembler;


/**
 * The Class SqlOperation.
 * 
 * @author Thomas Muenz
 */
public class SqlOperation extends SqlExpression
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 6099343255853061094L;

	///////////////////////////////////////////////////////////////////////////
	// instance fields  //
	/////////////////////
	/** The lefthand expression. */
	private final SqlExpression lefthandExpression;
	
	/** The righthand expression. */
	private final SqlExpression righthandExpression;
	
	/** The alias. */
	private String alias = null;



	///////////////////////////////////////////////////////////////////////////
	// constructors     //
	/////////////////////
	/**
	 * Instantiates a new sql operation.
	 * 
	 * @param expression the expression
	 * @param operator the operator
	 * @param righthandExpression the righthand expression
	 */
	public SqlOperation(final Object expression, final String operator, final Object righthandExpression) {
		super(operator);
		this.lefthandExpression = SQL.util.parseExpression(expression);
		this.righthandExpression = SQL.util.parseExpression(righthandExpression);
	}



	///////////////////////////////////////////////////////////////////////////
	// getters          //
	/////////////////////
	/**
	 * Gets the alias.
	 * 
	 * @return the alias
	 */
	public String getAlias() {
		return this.alias;
	}



	///////////////////////////////////////////////////////////////////////////
	// setters          //
	/////////////////////
	/**
	 * Sets the alias.
	 * 
	 * @param alias the alias to set
	 */
	public void setAlias(final String alias) {
		this.alias = alias;
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
		final DbmsDMLAssembler<?> dmlAssembler, final StringBuilder sb,final int indentLevel, final int flags
	)
	{
		final int passFlags = flags | OMITALIAS | ASEXPRESSION;

		assembleObject(this.lefthandExpression, dmlAssembler, sb, indentLevel, passFlags);
		sb.append(_).append(this.expression).append(_);
		assembleObject(this.righthandExpression, dmlAssembler, sb, indentLevel, passFlags);
		assembleAlias(sb, this.alias, !isOmitAlias(flags));
		return sb;
	}

}
