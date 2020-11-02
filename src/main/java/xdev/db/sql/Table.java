package xdev.db.sql;

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


import com.xdev.jadoth.sqlengine.internal.tables.SqlTableIdentity;

import xdev.vt.VirtualTable;


/**
 * This class represents a table in the SQL API.
 * <p>
 * It can be constructed with a name only, or optionally with a schema and an
 * alias.
 * <p>
 * It is also possible to create a {@link Column} object, using the alias of
 * this table with {@link #column(String)}.
 * 
 * @see VirtualTable
 * 
 * @author XDEV Software
 * 
 */
public class Table extends SqlObject
{
	private static final long	serialVersionUID	= -2411981068658228612L;
	
	private SqlTableIdentity	delegate;
	
	
	/**
	 * Creates a new table object with a name only.
	 * 
	 * @param name
	 *            the name of the table
	 */
	public Table(String name)
	{
		this(null,name,null);
	}
	
	
	/**
	 * Creates a table object with a name and an alias, but without a schema.
	 * 
	 * @param name
	 *            the name of the table
	 * @param alias
	 *            the alias of the table
	 */
	public Table(String name, String alias)
	{
		this(null,name,alias);
	}
	
	
	/**
	 * Create a table object with a schema, name and an alias.
	 * 
	 * @param schema
	 *            the schema of the table
	 * @param name
	 *            the name of the table
	 * @param alias
	 *            the alias of the table
	 */
	public Table(String schema, String name, String alias)
	{
		this(new SqlTableIdentity(schema,name,alias));
	}
	
	
	Table(SqlTableIdentity delegate)
	{
		this.delegate = delegate;
	}
	
	
	protected void _updateDelegate(String name)
	{
		_updateDelegate(null,name,null);
	}
	
	
	protected void _updateDelegate(String name, String alias)
	{
		_updateDelegate(null,name,alias);
	}
	
	
	protected void _updateDelegate(String schema, String name, String alias)
	{
		delegate = new SqlTableIdentity(schema,name,alias);
	}
	
	
	@Override
	SqlTableIdentity delegate()
	{
		return delegate;
	}
	
	
	/**
	 * Returns a new table object with a new alias.
	 * <p>
	 * The name and schema are taken from this table object.
	 * 
	 * @param alias
	 *            the new alias.
	 * @return a new table object
	 */
	public Table AS(String alias)
	{
		return new Table(delegate.AS(alias));
	}
	
	
	/**
	 * Returns the schema of this table object, may be <code>null</code>.
	 * 
	 * @return the schema of this table object
	 */
	public String getSchema()
	{
		return delegate.sql().schema;
	}
	
	
	/**
	 * Returns the name of this table object.
	 * 
	 * @return the name of this table object
	 */
	public String getTableName()
	{
		return delegate.sql().name;
	}
	
	
	/**
	 * Returns the alias of this table object, may be <code>null</code>.
	 * 
	 * @return the alias of this table object
	 */
	public String getAlias()
	{
		return delegate.sql().alias;
	}
	
	
	/**
	 * Creates a column object with this table as prefix.
	 * <p>
	 * Useful if a query uses a table with more aliases.
	 * 
	 * @param name
	 *            the name of the column
	 * @return a new column object
	 * 
	 * @since 3.2
	 */
	public Column column(String name)
	{
		return new Column(this,name);
	}
}
