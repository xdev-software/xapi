
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

import static com.xdev.jadoth.sqlengine.SQL.LANG.EXISTS;
import static com.xdev.jadoth.sqlengine.SQL.Punctuation._;

import com.xdev.jadoth.sqlengine.SELECT;
import com.xdev.jadoth.sqlengine.dbms.DbmsDMLAssembler;


/**
 * The Class SqlConditionEXISTS.
 */
public class SqlConditionEXISTS extends SqlCondition {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1753814578964426900L;


	/**
	 * Instantiates a new sql condition exists.
	 * 
	 * @param s the s
	 */
	public SqlConditionEXISTS(final SELECT s) {
		super(s);
	}

	/**
	 * Gets the keyword.
	 * 
	 * @return the keyword
	 */
	protected String getKeyword() {
		return EXISTS;
	}


	/**
	 * @param dmlAssembler
	 * @param sb
	 * @param indentLevel
	 * @param flags
	 * @return
	 * @see com.xdev.jadoth.sqlengine.internal.SqlCondition#assemble(com.xdev.jadoth.sqlengine.dbms.DbmsDMLAssembler, java.lang.StringBuilder, int, int)
	 */
	@Override
	protected StringBuilder assemble(final DbmsDMLAssembler<?> dmlAssembler, final StringBuilder sb, final int indentLevel, final int flags)
	{
		sb.append(this.getKeyword()).append(_);
		return super.assemble(dmlAssembler, sb, indentLevel, flags|QueryPart.ASEXPRESSION);
	}









}
