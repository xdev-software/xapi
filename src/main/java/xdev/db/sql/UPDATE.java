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


import xdev.lang.Copyable;


public class UPDATE extends WritingQuery implements Copyable<UPDATE>
{
	private static final long					serialVersionUID	= 8807037736433491953L;
	
	private final com.xdev.jadoth.sqlengine.UPDATE	delegate;
	
	
	public UPDATE(Table table)
	{
		this(new com.xdev.jadoth.sqlengine.UPDATE(table.delegate()));
	}
	
	
	UPDATE(com.xdev.jadoth.sqlengine.UPDATE delegate)
	{
		super(delegate);
		this.delegate = delegate;
	}
	
	
	public UPDATE SET(Object column, Object value)
	{
		delegate.SET(expose(column),expose(value));
		return this;
	}
	
	
	public UPDATE AND(Object condition)
	{
		delegate.AND(expose(condition));
		return this;
	}
	
	
	public UPDATE OR(Object condition)
	{
		delegate.OR(expose(condition));
		return this;
	}
	
	
	public UPDATE FROM(SELECT select)
	{
		delegate.FROM(select.delegate());
		return this;
	}
	
	
	public UPDATE INNER_JOIN(Table table, Object condition)
	{
		delegate.INNER_JOIN(table.delegate(),expose(condition));
		return this;
	}
	
	
	public UPDATE OUTER_JOIN(Table table, Object condition)
	{
		delegate.OUTER_JOIN(table.delegate(),expose(condition));
		return this;
	}
	
	
	public UPDATE LEFT_JOIN(Table table, Object condition)
	{
		delegate.LEFT_JOIN(table.delegate(),expose(condition));
		return this;
	}
	
	
	public UPDATE RIGHT_JOIN(Table table, Object condition)
	{
		delegate.RIGHT_JOIN(table.delegate(),expose(condition));
		return this;
	}
	
	
	public UPDATE WHERE(Object condition)
	{
		delegate.WHERE(expose(condition));
		return this;
	}
	
	
	@Override
	public UPDATE clone()
	{
		return new UPDATE(delegate.copy());
	}
}
