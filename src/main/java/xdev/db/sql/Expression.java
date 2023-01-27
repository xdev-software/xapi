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


import com.xdev.jadoth.sqlengine.SQL;
import com.xdev.jadoth.sqlengine.internal.SqlExpression;


/**
 * This class represents a expression in the SQL API.
 * <p>
 * Expressions can be plain {@link String}s or SQL objects like a {@link Column}.
 * <p>
 * To connect expression objects use the operator methods:
 * <ul>
 * <li>{@link #eq(Object)}</li>
 * <li>{@link #ne(Object)}</li>
 * <li>{@link #gt(Object)}</li>
 * <li>{@link #gte(Object)}</li>
 * <li>{@link #lt(Object)}</li>
 * <li>{@link #lte(Object)}</li>
 * <li>...</li>
 * </ul>
 * 
 * @author XDEV Software
 * 
 */
public class Expression extends SqlObject
{
	private static final long	serialVersionUID	= -155553178714173198L;
	
	
	/**
	 * Wraps a plain object into a expression to be used in the SQL API.
	 * 
	 * @param expression
	 *            the expression to wrap.
	 * @return a expression object
	 */
	public static Expression valueOf(Object expression)
	{
		if(expression instanceof Expression)
		{
			return (Expression)expression;
		}
		return new Expression(SQL.exp(expose(expression)));
	}
	
	protected SqlExpression	delegate;
	
	
	Expression(SqlExpression delegate)
	{
		this.delegate = delegate;
	}
	
	
	@Override
	SqlExpression delegate()
	{
		return delegate;
	}
	
	
	/**
	 * @deprecated replaced by {@link #AS(String)}
	 */
	@Deprecated
	public Expression as(String alias)
	{
		return AS(alias);
	}
	
	
	/**
	 * Creates an aliased version of this expression object.
	 * 
	 * @param alias
	 *            the new alias
	 * @return a new expression object with an alias
	 */
	public Expression AS(String alias)
	{
		return new Expression(delegate.AS(alias));
	}
	
	
	/**
	 * The equals operator (=) for expressions.
	 * <p>
	 * Connects this expression object and the expression parameter via the '='
	 * operator and returns the connection as a new condition object.
	 * 
	 * @param value
	 *            the right side of the connection
	 * @return a new condition object
	 */
	public Condition eq(Object value)
	{
		return new Condition(delegate.eq(expose(value)));
	}
	
	
	/**
	 * The not equals operator (!= | &lt;&gt;) for expressions.
	 * <p>
	 * Connects this expression object and the expression parameter via the '!='
	 * operator and returns the connection as a new condition object.
	 * 
	 * @param value
	 *            the right side of the connection
	 * @return a new condition object
	 */
	public Condition ne(Object value)
	{
		return new Condition(delegate.ne(expose(value)));
	}
	
	
	/**
	 * The greater than operator (&gt;) for expressions.
	 * <p>
	 * Connects this expression object and the expression parameter via the
	 * '&gt;' operator and returns the connection as a new condition object.
	 * 
	 * @param value
	 *            the right side of the connection
	 * @return a new condition object
	 */
	public Condition gt(Object value)
	{
		return new Condition(delegate.gt(expose(value)));
	}
	
	
	/**
	 * The lower than operator (&lt;) for expressions.
	 * <p>
	 * Connects this expression object and the expression parameter via the
	 * '&lt;' operator and returns the connection as a new condition object.
	 * 
	 * @param value
	 *            the right side of the connection
	 * @return a new condition object
	 */
	public Condition lt(Object value)
	{
		return new Condition(delegate.lt(expose(value)));
	}
	
	
	/**
	 * The greater than equals operator (&gt;=) for expressions.
	 * <p>
	 * Connects this expression object and the expression parameter via the
	 * '&gt;=' operator and returns the connection as a new condition object.
	 * 
	 * @param value
	 *            the right side of the connection
	 * @return a new condition object
	 */
	public Condition gte(Object value)
	{
		return new Condition(delegate.gte(expose(value)));
	}
	
	
	/**
	 * The lower than equals operator (&lt;=) for expressions.
	 * <p>
	 * Connects this expression object and the expression parameter via the
	 * '&lt;=' operator and returns the connection as a new condition object.
	 * 
	 * @param value
	 *            the right side of the connection
	 * @return a new condition object
	 */
	public Condition lte(Object value)
	{
		return new Condition(delegate.lte(expose(value)));
	}
	
	
	public Condition IS_NULL()
	{
		return new Condition(delegate.IS_NULL());
	}
	
	
	public Condition IS_NOT_NULL()
	{
		return new Condition(delegate.IS_NOT_NULL());
	}
	
	
	public Condition IN(Object... values)
	{
		return new Condition(delegate.IN(expose(values)));
	}
	
	
	public Condition NOT_IN(Object... values)
	{
		return new Condition(delegate.NOT_IN(expose(values)));
	}
	
	
	public Condition LIKE(Object value)
	{
		return new Condition(delegate.LIKE(expose(value)));
	}
	
	
	public Condition NOT_LIKE(Object value)
	{
		return new Condition(delegate.NOT_LIKE(expose(value)));
	}
	
	
	public Condition BETWEEN(Object lower, Object upper)
	{
		return new Condition(delegate.BETWEEN(expose(lower),expose(upper)));
	}
	
	
	public Condition NOT_BETWEEN(Object lower, Object upper)
	{
		return new Condition(delegate.NOT_BETWEEN(expose(lower),expose(upper)));
	}
}
