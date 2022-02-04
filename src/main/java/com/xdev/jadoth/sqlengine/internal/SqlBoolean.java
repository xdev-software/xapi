
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

import com.xdev.jadoth.sqlengine.dbms.DbmsDMLAssembler;


/**
 * The Class SqlBoolean.
 * 
 * @author Thomas Muenz
 */
public class SqlBoolean extends SqlExpression
{
	///////////////////////////////////////////////////////////////////////////
	// constants        //
	/////////////////////

	/**
	 * 
	 */
	private static final long serialVersionUID = -174548008136517689L;

	//see 4.5.1 Introduction to Boolean types
	/** The Constant True. */
	public static final Character TRUE = '1';
	
	/** The Constant False. */
	public static final Character FALSE = '0';



	///////////////////////////////////////////////////////////////////////////
	// constructors     //
	/////////////////////

	/**
	 * Instantiates a new sql boolean.
	 * 
	 * @param expression the expression
	 */
	public SqlBoolean(final Boolean expression) {
		super(expression);
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
	protected StringBuilder assemble(
		final DbmsDMLAssembler<?> dmlAssembler,
		final StringBuilder sb,
		final int indentLevel,
		final int flags
	)
	{
		return assembleObject(
			this.expression==null ?null :((Boolean)this.expression)==false ?FALSE:TRUE, dmlAssembler, sb, indentLevel, flags
		);
	}

}
