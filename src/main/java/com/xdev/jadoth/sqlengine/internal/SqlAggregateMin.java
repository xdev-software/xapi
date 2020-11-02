
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

import static com.xdev.jadoth.sqlengine.SQL.LANG.MIN;


/**
 * The Class SqlAggregateMin.
 * 
 * @author Thomas Muenz
 */
public class SqlAggregateMin extends SqlFunction
{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7939555183490918679L;

	/**
	 * Instantiates a new sql aggregate min.
	 * 
	 * @param innerExpression the inner expression
	 */
	public SqlAggregateMin(final Object innerExpression) {
		super(null, innerExpression);
	}

	/**
	 * @return
	 * @see com.xdev.jadoth.sqlengine.internal.SqlFunction#getFunctionName()
	 */
	@Override
	public String getFunctionName() {
		return MIN;
	}
}
