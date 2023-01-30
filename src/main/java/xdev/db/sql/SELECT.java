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


import java.util.List;

import com.xdev.jadoth.sqlengine.internal.ORDER_BY;
import com.xdev.jadoth.sqlengine.internal.SqlColumn;
import com.xdev.jadoth.sqlengine.internal.SqlExpression;
import com.xdev.jadoth.sqlengine.internal.SqlOrderItem;
import com.xdev.jadoth.sqlengine.internal.interfaces.SelectItem;

import xdev.db.DBConnection;
import xdev.lang.Copyable;


/**
 * A java representation of a SQL SELECT statement which is used by the
 * {@link DBConnection} class to perform reading queries.
 * <p>
 * {@link DBConnection#query(SELECT, Object...)}
 * <p>
 * The methods of the SELECT class return the object back so you can chain the
 * method calls:
 * 
 * <pre>
 * VirtualTable vt;
 * VirtualTableColumn column;
 * new SELECT().FROM(vt.toSqlTable()).WHERE(column.toSqlColumn().LIKE(&quot;Bla%&quot;));
 * </pre>
 * 
 * @author XDEV Software
 * @since 3.0
 */
public class SELECT extends SqlObject implements Copyable<SELECT>
{
	private static final long						serialVersionUID	= 4439261355691848030L;
	
	private final com.xdev.jadoth.sqlengine.SELECT	delegate;
	
	
	/**
	 * Creates a new empty SELECT object.
	 */
	public SELECT()
	{
		this(new com.xdev.jadoth.sqlengine.SELECT());
	}
	
	
	SELECT(com.xdev.jadoth.sqlengine.SELECT delegate)
	{
		this.delegate = delegate;
	}
	
	
	@Override
	com.xdev.jadoth.sqlengine.SELECT delegate()
	{
		return delegate;
	}
	
	
	/**
	 * Adds the distinct option to this SELECT statement.
	 */
	public SELECT DISTINCT()
	{
		delegate.DISTINCT();
		return this;
	}
	
	
	/**
	 * Adds columns to this SELECT statement.
	 * 
	 * @param columns
	 *            the columns to add
	 */
	public SELECT columns(Object... columns)
	{
		delegate.columns(expose(columns));
		return this;
	}
	
	
	/**
	 * Gets the select items.
	 * 
	 * @return the selectItems
	 */
	public Object[] getSelectItems()
	{
		return delegate.getSelectItems().toArray();
	}
	
	
	public void ensureNameOfLastColumn(String name)
	{
		List<SelectItem> items = delegate.getSelectItems();
		if(items != null && items.size() > 0)
		{
			int index = items.size() - 1;
			SelectItem item = items.get(index);
			if(item instanceof SqlExpression)
			{
				char[] ch = name.toCharArray();
				for(int i = 0; i < ch.length; i++)
				{
					if(i == 0)
					{
						if(!Character.isJavaIdentifierStart(ch[i]))
						{
							ch[i] = '_';
						}
					}
					else
					{
						if(!Character.isJavaIdentifierPart(ch[i]))
						{
							ch[i] = '_';
						}
					}
				}
				name = new String(ch);
				if(!(item instanceof SqlColumn && name.equals(((SqlColumn)item).getColumnName())))
				{
					// Don't add alias if it has the same name
					items.set(index,((SqlExpression)item).AS(name));
				}
			}
		}
	}
	
	
	public SELECT FROM(Table table)
	{
		delegate.FROM(table.delegate());
		return this;
	}
	
	
	public SELECT FROM(SELECT select)
	{
		delegate.FROM(select.delegate);
		return this;
	}
	
	
	public SELECT AND(Object condition)
	{
		delegate.AND(expose(condition));
		return this;
	}
	
	
	public SELECT OR(Object condition)
	{
		delegate.OR(expose(condition));
		return this;
	}
	
	
	public SELECT INNER_JOIN(Table table, Object condition)
	{
		delegate.INNER_JOIN(table.delegate(),expose(condition));
		return this;
	}
	
	
	public SELECT OUTER_JOIN(Table table, Object condition)
	{
		delegate.OUTER_JOIN(table.delegate(),expose(condition));
		return this;
	}
	
	
	public SELECT LEFT_JOIN(Table table, Object condition)
	{
		delegate.LEFT_JOIN(table.delegate(),expose(condition));
		return this;
	}
	
	
	public SELECT RIGHT_JOIN(Table table, Object condition)
	{
		delegate.RIGHT_JOIN(table.delegate(),expose(condition));
		return this;
	}
	
	
	public SELECT WHERE(Object condition)
	{
		delegate.WHERE(expose(condition));
		return this;
	}
	
	
	public WHERE getWhere()
	{
		com.xdev.jadoth.sqlengine.internal.WHERE where = delegate.getWhereClause();
		return where != null ? new WHERE(where) : null;
	}
	
	
	public SELECT ORDER_BY(Object item)
	{
		return ORDER_BY(item,null,null);
	}
	
	
	public SELECT ORDER_BY(Object item, Boolean desc)
	{
		return ORDER_BY(item,desc,null);
	}
	
	
	public SELECT ORDER_BY(Object item, Boolean desc, Boolean nullsFirst)
	{
		return orderByImpl(new SqlOrderItem(expose(item),desc,nullsFirst));
	}
	
	
	private SELECT orderByImpl(Object item)
	{
		ORDER_BY orderBy = delegate.getOrderByClause();
		if(orderBy == null)
		{
			delegate.setOrderByClause(orderBy = new ORDER_BY());
		}
		orderBy.add(expose(item));
		return this;
	}
	
	
	/**
	 * @since 4.0
	 */
	public void clear_ORDER_BY()
	{
		delegate.setOrderByClause(null);
	}
	
	
	public SELECT GROUP_BY(Object... fields)
	{
		delegate.GROUP_BY(expose(fields));
		return this;
	}
	
	
	/**
	 * @since 4.0
	 */
	public void clear_GROUP_BY()
	{
		delegate.setGroupByClause(null);
	}
	
	
	public SELECT HAVING(Object condition)
	{
		delegate.HAVING(expose(condition));
		return this;
	}
	
	
	/**
	 * @since 4.0
	 */
	public void clear_HAVING()
	{
		com.xdev.jadoth.sqlengine.internal.HAVING having = delegate.getHavingClause();
		if(having != null)
		{
			having.getBody().clear();
		}
	}
	
	
	public SELECT OFFSET(Integer offset)
	{
		delegate.OFFSET(offset);
		return this;
	}
	
	
	public Integer getOffsetSkipCount()
	{
		return delegate.getOffsetSkipCount();
	}
	
	
	public SELECT FETCH_FIRST(Integer limit)
	{
		delegate.FETCH_FIRST(limit);
		return this;
	}
	
	
	public Integer getFetchFirstRowCount()
	{
		return delegate.getFetchFirstRowCount();
	}
	
	
	public SELECT UNION(SELECT unionSelect)
	{
		delegate.UNION(unionSelect.delegate);
		return this;
	}
	
	
	public SELECT UNION_All(SELECT unionAllSelect)
	{
		delegate.UNION_All(unionAllSelect.delegate);
		return this;
	}
	
	
	public SELECT AS(String newAlias)
	{
		delegate.AS(newAlias);
		return this;
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public SELECT clone()
	{
		return new SELECT(delegate.copy());
	}
	
	
	@Override
	public String toString()
	{
		return delegate.toString();
	}
}
