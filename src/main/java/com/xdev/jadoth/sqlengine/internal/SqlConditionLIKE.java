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

import static com.xdev.jadoth.sqlengine.SQL.LANG.LIKE;

import com.xdev.jadoth.sqlengine.SQL;


/**
 * The Class SqlConditionLIKE.
 * 
 * @author Thomas Muenz
 */
public class SqlConditionLIKE extends SqlComparison {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8971296921225328248L;

	/**
	 * Instantiates a new sql condition like.
	 * 
	 * @param lefthandExpression the lefthand expression
	 * @param parameter the parameter
	 */
	public SqlConditionLIKE(final Object lefthandExpression, final Object parameter) {
		super(lefthandExpression, LIKE, SQL.util.parseExpression(parameter));
	}

}
