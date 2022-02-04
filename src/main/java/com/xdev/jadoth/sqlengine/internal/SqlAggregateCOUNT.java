
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

import static com.xdev.jadoth.sqlengine.SQL.LANG.COUNT;
import static com.xdev.jadoth.sqlengine.SQL.Punctuation.star;


/**
 * The Class SqlAggregateCOUNT.
 * 
 * @author Thomas Muenz
 */
public class SqlAggregateCOUNT extends SqlFunction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7936307383827892802L;

	/**
	 * Instantiates a new sql aggregate count.
	 */
	public SqlAggregateCOUNT() {
		super(null, new SqlStar());
	}

	/**
	 * Instantiates a new sql aggregate count.
	 * 
	 * @param innerExpression the inner expression
	 */
	public SqlAggregateCOUNT(final Object innerExpression) {
		this();
		if(innerExpression.toString().trim().equals(star)) {
			this.setParameter(new SqlStar());
		}
		this.setParameter(innerExpression);
	}

	/**
	 * @return
	 * @see com.xdev.jadoth.sqlengine.internal.SqlFunction#getFunctionName()
	 */
	@Override
	public String getFunctionName() {
		return COUNT;
	}
}
