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


import xdev.lang.Copyable;


public class INSERT extends WritingQuery implements Copyable<INSERT>
{
	private static final long					serialVersionUID	= -3404879843322053332L;
	
	private final com.xdev.jadoth.sqlengine.INSERT	delegate;
	
	
	public INSERT()
	{
		this(new com.xdev.jadoth.sqlengine.INSERT());
	}
	
	
	INSERT(com.xdev.jadoth.sqlengine.INSERT delegate)
	{
		super(delegate);
		this.delegate = delegate;
	}
	
	
	public INSERT INTO(Table table)
	{
		delegate.INTO(table.delegate());
		return this;
	}
	
	
	public INSERT columns(Object... columns)
	{
		delegate.columns(expose(columns));
		return this;
	}
	
	
	public INSERT VALUES(Object... columns)
	{
		delegate.VALUES(expose(columns));
		return this;
	}
	
	
	public INSERT assign(Object column, Object value)
	{
		delegate.assign(expose(column),expose(value));
		return this;
	}
	
	
	public INSERT select(SELECT select)
	{
		delegate.select(select.delegate());
		return this;
	}
	
	
	public INSERT filter(Object... fields)
	{
		delegate.filter(expose(fields));
		return this;
	}
	
	
	@Override
	public INSERT clone()
	{
		return new INSERT(delegate.copy());
	}
}
