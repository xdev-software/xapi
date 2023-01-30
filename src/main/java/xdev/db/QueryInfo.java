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
package xdev.db;


import java.util.HashMap;
import java.util.Map;

import xdev.db.sql.SELECT;
import xdev.lang.Copyable;


/**
 * 
 * A wrapper class that holds information of a query.
 * 
 * 
 * @author XDEV Software Corp.
 */
public class QueryInfo implements Copyable<QueryInfo>
{
	private final SELECT		select;
	private final Object[]		params;
	private Map<Object, Object>	clientProperties;
	

	/**
	 * Constructs a new {@link QueryInfo} with the given <code>select</code> and
	 * the <code>params</code>.
	 * 
	 * @param select
	 *            the {@link SELECT}
	 * @param params
	 *            the parameters for the {@link SELECT}
	 */
	public QueryInfo(SELECT select, Object... params)
	{
		this.select = select;
		this.params = params;
	}
	

	/**
	 * Returns the select object of this {@link QueryInfo}.
	 * 
	 * @return the {@link SELECT}
	 * 
	 */
	public SELECT getSelect()
	{
		return select;
	}
	

	/**
	 * Returns the parameter of this {@link QueryInfo}.
	 * 
	 * @return the parameters
	 */
	public Object[] getParameters()
	{
		return params;
	}
	

	/**
	 * {@inheritDoc}
	 */
	@Override
	public QueryInfo clone()
	{
		QueryInfo clone = new QueryInfo(select.clone(),params);
		if(clientProperties != null)
		{
			clone.clientProperties = new HashMap(clientProperties);
		}
		return clone;
	}
	

	/**
	 * Adds an arbitrary key/value "client property" to this query info.
	 * <p>
	 * The <code>get/putClientProperty</code> methods provide access to a small
	 * per-instance hashtable. Callers can use get/putClientProperty to annotate
	 * entries.
	 * <p>
	 * If value is <code>null</code> this method will remove the property.
	 * 
	 * @param key
	 *            the new client property key
	 * @param value
	 *            the new client property value; if <code>null</code> this
	 *            method will remove the property
	 * @see #getClientProperty
	 * @since 3.2
	 */
	public void putClientProperty(Object key, Object value)
	{
		if(value == null && clientProperties == null)
		{
			return;
		}
		
		if(clientProperties == null)
		{
			clientProperties = new HashMap();
		}
		
		if(value == null)
		{
			clientProperties.remove(key);
		}
		else
		{
			clientProperties.put(key,value);
		}
	}
	

	/**
	 * Returns the value of the property with the specified key. Only properties
	 * added with <code>putClientProperty</code> will return a non-
	 * <code>null</code> value.
	 * 
	 * @param key
	 *            the being queried
	 * @return the value of this property or <code>null</code>
	 * @see #putClientProperty
	 * @since 3.2
	 */
	public Object getClientProperty(Object key)
	{
		if(clientProperties == null)
		{
			return null;
		}
		else
		{
			return clientProperties.get(key);
		}
	}
}
