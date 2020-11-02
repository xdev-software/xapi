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


import java.util.List;

import com.xdev.jadoth.sqlengine.SQL;
import com.xdev.jadoth.sqlengine.internal.SqlCondition;

import xdev.lang.Copyable;


public class WHERE extends SqlObject implements Copyable<WHERE>
{
	private static final long					serialVersionUID	= 7582189814876485153L;
	
	private com.xdev.jadoth.sqlengine.internal.WHERE	delegate;
	
	
	public WHERE()
	{
		this(new com.xdev.jadoth.sqlengine.internal.WHERE());
	}
	
	
	public WHERE(Object condition)
	{
		this(new com.xdev.jadoth.sqlengine.internal.WHERE(expose(condition)));
	}
	
	
	WHERE(com.xdev.jadoth.sqlengine.internal.WHERE delegate)
	{
		this.delegate = delegate;
	}
	
	
	@Override
	com.xdev.jadoth.sqlengine.internal.WHERE delegate()
	{
		return delegate;
	}
	
	
	public WHERE and(Object condition)
	{
		delegate.AND(expose(condition));
		return this;
	}
	
	
	public WHERE or(Object condition)
	{
		delegate.OR(expose(condition));
		return this;
	}
	
	
	public boolean isEmpty()
	{
		return delegate.isEmpty();
	}
	
	
	public WHERE encloseWithPars()
	{
		SqlCondition par = SQL.par(delegate.getLastElement());
		List<SqlCondition> body = delegate.getBody();
		body.clear();
		body.add(par);
		return this;
	}
	
	
	@Override
	public WHERE clone()
	{
		return new WHERE(delegate.copy());
	}
}
