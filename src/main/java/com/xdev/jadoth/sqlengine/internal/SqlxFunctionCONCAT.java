
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



/**
 * The Class SqlxFunctionCONCAT.
 * 
 * @author Thomas Muenz
 */
public class SqlxFunctionCONCAT extends SqlFunction
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 9125184992265254814L;

	/**
	 * Instantiates a new sqlx function concat.
	 * 
	 * @param lhsExpression the lhs expression
	 * @param rhsExpression the rhs expression
	 */
	public SqlxFunctionCONCAT(final Object lhsExpression, final Object rhsExpression) {
		super(null, lhsExpression, rhsExpression);
	}

	/**
	 * @return
	 * @see com.xdev.jadoth.sqlengine.internal.SqlFunction#getFunctionName()
	 */
	@Override
	public String getFunctionName() {
		return "CONCAT";
	}

}
