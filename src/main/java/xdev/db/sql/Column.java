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
package xdev.db.sql;


import com.xdev.jadoth.sqlengine.internal.SqlColumn;

import xdev.vt.VirtualTableColumn;


/**
 * This class represents a column expression in the SQL API.
 * <p>
 * It can be constructed with a name only or an optional {@link Table} as
 * prefix.
 * 
 * @see VirtualTableColumn
 * 
 * @author XDEV Software
 * 
 */
public class Column extends Expression
{
	private static final long	serialVersionUID	= -5495168226512593905L;
	
	
	/**
	 * Creates a new column object with a name only.
	 * 
	 * @param name
	 *            the name of the column
	 */
	public Column(String name)
	{
		this(new SqlColumn(name));
	}
	
	
	/**
	 * Creates a new column object with a table and a name
	 * 
	 * @param table
	 *            the prefix of the column
	 * @param name
	 *            the name of the column
	 */
	public Column(Table table, String name)
	{
		this(new SqlColumn(table.delegate(),name));
	}
	
	
	Column(SqlColumn delegate)
	{
		super(delegate);
	}
	
	
	protected void _updateDelegate(String name)
	{
		delegate = new SqlColumn(name);
	}
	
	
	protected void _updateDelegate(Table table, String name)
	{
		delegate = new SqlColumn(table.delegate(),name);
	}
}
