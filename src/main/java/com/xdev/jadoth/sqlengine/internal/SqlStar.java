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

import static com.xdev.jadoth.sqlengine.SQL.Punctuation.star;

import com.xdev.jadoth.sqlengine.internal.interfaces.TableExpression;


/**
 * The Class SqlStar.
 * 
 * @author Thomas Muenz
 */
public class SqlStar extends SqlColumn
{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4505280590844068411L;

	/**
	 * Instantiates a new sql star.
	 */
	public SqlStar() {
		this(null);
	}

	/**
	 * Instantiates a new sql star.
	 * 
	 * @param owner the owner
	 */
	public SqlStar(final TableExpression owner)
	{
		super(owner, null);
		// bypass Identifier-validation (normally, "...*..." is invalid. But "*" itself is valid.
		this.expression = star;
	}

}
