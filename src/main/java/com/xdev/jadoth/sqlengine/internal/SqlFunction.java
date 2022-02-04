
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


/**
 * The Class SqlFunction.
 * 
 * @author Thomas Muenz
 */
public abstract class SqlFunction extends SqlExpression
{
	///////////////////////////////////////////////////////////////////////////
	// instance fields //
	////////////////////

	/**
	 * 
	 */
	private static final long serialVersionUID = 1779323673982927330L;
	/** The parameters. */
	protected SqlExpression[] parameters;



	///////////////////////////////////////////////////////////////////////////
	// constructors //
	/////////////////

	/**
	 * Instantiates a new sql function.
	 * 
	 * @param functionName the function name
	 * @param params the params
	 */
	public SqlFunction(final String functionName, final Object... params) {
		super(functionName);
		this.parameters = SQL.util.parseExpressionArray(params);
	}



	///////////////////////////////////////////////////////////////////////////
	// getters          //
	/////////////////////

	/**
	 * Gets the parameters.
	 * 
	 * @return the parameters
	 */
	public SqlExpression[] getParameters() {
		return this.parameters;
	}


	/**
	 * Gets the function name.
	 * 
	 * @return the function name
	 */
	public abstract String getFunctionName();

	/**
	 * Sets the parameter.
	 * 
	 * @param param the new parameter
	 */
	protected void setParameter(final Object... param) {
		this.parameters = SQL.util.parseExpressionArray(param);
	}

	/**
	 * @param dmlAssembler
	 * @param sb
	 * @param indentLevel
	 * @param flags
	 * @return
	 * @see com.xdev.jadoth.sqlengine.internal.SqlExpression#assemble(com.xdev.jadoth.sqlengine.dbms.DbmsDMLAssembler, java.lang.StringBuilder, int, int)
	 */
	@Override
	protected StringBuilder assemble(final DbmsDMLAssembler<?> dmlAssembler, final StringBuilder sb, final int indentLevel, final int flags){
		return assembleFunction(this.getFunctionName(), dmlAssembler, sb, indentLevel, flags, this.parameters);
	}


	/**
	 * Assemble function.
	 * 
	 * @param expression the expression
	 * @param dmlAssembler the dml assembler
	 * @param sb the sb
	 * @param indentLevel the indent level
	 * @param flags the flags
	 * @param parameters the parameters
	 * @return the string builder
	 */
	protected static StringBuilder assembleFunction(
		final String expression,
		final DbmsDMLAssembler<?> dmlAssembler,
		final StringBuilder sb,
		final int indentLevel,
		final int flags,
		final Object[] parameters
	)
	{
		return sb.append(QueryPart.function(dmlAssembler, expression, flags, parameters));
	}



}
