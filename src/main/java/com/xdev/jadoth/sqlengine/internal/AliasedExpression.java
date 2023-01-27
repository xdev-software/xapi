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

import com.xdev.jadoth.sqlengine.dbms.DbmsDMLAssembler;



/**
 * The Class AliasedExpression.
 */
public class AliasedExpression extends SqlExpression
{

	///////////////////////////////////////////////////////////////////////////
	// constants        //
	/////////////////////

	private static final long serialVersionUID = 5524898879245460865L;



	///////////////////////////////////////////////////////////////////////////
	// instance fields  //
	/////////////////////
	/** The alias. */
	private final String alias;



	///////////////////////////////////////////////////////////////////////////
	// constructors     //
	/////////////////////
	/**
	 * Instantiates a new aliased expression.
	 *
	 * @param expression the expression
	 * @param alias the alias
	 */
	public AliasedExpression(final Object expression, final String alias) {
		super(expression);
		this.alias = alias;
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
		return alias;
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
		super.assemble(dmlAssembler, sb, indentLevel, flags);
		assembleAlias(sb, alias, !isOmitAlias(flags));
		return sb;
	}


}
