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

import com.xdev.jadoth.sqlengine.SQL;
import com.xdev.jadoth.sqlengine.dbms.DbmsDMLAssembler;

/**
 * @author Thomas Muenz
 *
 */
public class DISTINCT extends SqlExpression
{
	///////////////////////////////////////////////////////////////////////////
	// constants        //
	/////////////////////
	/**
	 * 
	 */
	private static final long serialVersionUID = 2338538827171762740L;
	
	
	
	///////////////////////////////////////////////////////////////////////////
	// constructors //
	/////////////////
	/**
	 * @param expression
	 */
	public DISTINCT(final Object expression)
	{
		super(expression);
	}

	
	
	///////////////////////////////////////////////////////////////////////////
	// override methods //
	/////////////////////
	/**
	 * 
	 */
	@Override
	protected StringBuilder assemble(
		final DbmsDMLAssembler<?> dmlAssembler, final StringBuilder sb, final int indentLevel, final int flags
	)
	{
		sb.append(SQL.LANG.DISTINCT).append(' ');
		return super.assemble(dmlAssembler, sb, indentLevel, flags);
	}
	
}
