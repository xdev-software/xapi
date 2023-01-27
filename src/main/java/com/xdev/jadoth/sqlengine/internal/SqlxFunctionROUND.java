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

import static com.xdev.jadoth.sqlengine.SQL.LANG.ROUND;



/**
 * The Class SqlxFunctionROUND.
 * 
 * @author Thomas Muenz
 */
public class SqlxFunctionROUND extends SqlFunction
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -9163055053752132743L;


	/**
	 * Instantiates a new sqlx function round.
	 * 
	 * @param innerExpression the inner expression
	 * @param decimals the decimals
	 */
	public SqlxFunctionROUND(final Object innerExpression, final int decimals) {
		super(null, innerExpression, decimals);
	}


	/**
	 * @return
	 * @see com.xdev.jadoth.sqlengine.internal.SqlFunction#getFunctionName()
	 */
	@Override
	public String getFunctionName() {
		return ROUND;
	}

}
