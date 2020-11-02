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


import java.io.Serializable;

import xdev.lang.NotNull;


/**
 * Abstract supertype of all SQL objects.
 * <p>
 * <strong>This class is not intended to be subclassed by the user.</strong>
 * </p>
 * 
 * 
 * @author XDEV Software
 * 
 */
public abstract class SqlObject implements Serializable
{
	abstract @NotNull
	Object delegate();
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString()
	{
		return delegate().toString();
	}
	
	
	static Object expose(Object obj)
	{
		if(obj instanceof SqlObject)
		{
			return ((SqlObject)obj).delegate();
		}
		
		return obj;
	}
	
	
	static Object[] expose(Object... objects)
	{
		if(objects == null)
		{
			return null;
		}
		
		int c = objects.length;
		Object[] exposed = new Object[c];
		for(int i = 0; i < c; i++)
		{
			exposed[i] = expose(objects[i]);
		}
		return exposed;
	}
}
