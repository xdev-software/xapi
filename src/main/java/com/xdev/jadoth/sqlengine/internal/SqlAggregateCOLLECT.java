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

import static com.xdev.jadoth.sqlengine.SQL.LANG.COLLECT;


/**
 * The Class SqlAggregateCOLLECT.
 * 
 * @author Thomas Muenz
 */
public class SqlAggregateCOLLECT extends SqlFunction
{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8515548581649328020L;

	/**
	 * Instantiates a new sql aggregate collect.
	 * 
	 * @param innerExpression the inner expression
	 */
	public SqlAggregateCOLLECT(final Object innerExpression) {
		super(null, innerExpression);
	}

	/**
	 * @return
	 * @see com.xdev.jadoth.sqlengine.internal.SqlFunction#getFunctionName()
	 */
	@Override
	public String getFunctionName() {
		return COLLECT;
	}
}
