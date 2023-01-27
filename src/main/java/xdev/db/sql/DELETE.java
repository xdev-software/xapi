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


public class DELETE extends WritingQuery implements Copyable<DELETE>
{
	private static final long					serialVersionUID	= -7733356856585775427L;
	
	private final com.xdev.jadoth.sqlengine.DELETE	delegate;
	
	
	public DELETE()
	{
		this(new com.xdev.jadoth.sqlengine.DELETE());
	}
	
	
	DELETE(com.xdev.jadoth.sqlengine.DELETE delegate)
	{
		super(delegate);
		this.delegate = delegate;
	}
	
	
	public DELETE FROM(Table table)
	{
		delegate.FROM(table.delegate());
		return this;
	}
	
	
	public DELETE FROM(SELECT select)
	{
		delegate.FROM(select.delegate());
		return this;
	}
	
	
	public DELETE AND(Object condition)
	{
		delegate.AND(expose(condition));
		return this;
	}
	
	
	public DELETE OR(Object condition)
	{
		delegate.OR(expose(condition));
		return this;
	}
	
	
	public DELETE INNER_JOIN(Table table, Object condition)
	{
		delegate.INNER_JOIN(table.delegate(),expose(condition));
		return this;
	}
	
	
	public DELETE OUTER_JOIN(Table table, Object condition)
	{
		delegate.OUTER_JOIN(table.delegate(),expose(condition));
		return this;
	}
	
	
	public DELETE LEFT_JOIN(Table table, Object condition)
	{
		delegate.LEFT_JOIN(table.delegate(),expose(condition));
		return this;
	}
	
	
	public DELETE RIGHT_JOIN(Table table, Object condition)
	{
		delegate.RIGHT_JOIN(table.delegate(),expose(condition));
		return this;
	}
	
	
	public DELETE WHERE(Object condition)
	{
		delegate.WHERE(expose(condition));
		return this;
	}
	
	
	@Override
	public DELETE clone()
	{
		return new DELETE(delegate.copy());
	}
}
