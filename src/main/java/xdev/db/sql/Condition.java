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
import com.xdev.jadoth.sqlengine.internal.SqlCondition;


/**
 * This class represents a condition in the SQL API.
 * <p>
 * Conditions can be connected via the {@link #AND(Object)} and
 * {@link #OR(Object)} connector methods.
 * 
 * @see #valueOf(Object)
 * 
 * @author XDEV Software
 * 
 */
public class Condition extends SqlObject
{
	private static final long	serialVersionUID	= -2773446832713739963L;
	
	
	/**
	 * Wraps a plain object into a condition to be used in the SQL API.
	 * 
	 * @param condition
	 *            the condition to wrap.
	 * @return a condition object
	 */
	public static Condition valueOf(Object condition)
	{
		if(condition instanceof Condition)
		{
			return (Condition)condition;
		}
		return new Condition(SQL.condition(expose(condition)));
	}
	
	
	/**
	 * Encloses a condition object in parenthesis, for conditional grouping
	 * purposes.
	 * 
	 * @param condition
	 *            the object to enclose
	 * @return a new condition object
	 */
	public static Condition par(Object condition)
	{
		return new Condition(SQL.par(expose(condition)));
	}
	
	
	/**
	 * Creates an inverted condition (SQL NOT).
	 * 
	 * @param condition
	 *            the condition to invert.
	 * @return the inverted condition
	 */
	public static Condition NOT(Condition condition)
	{
		return new Condition(SQL.NOT(condition.delegate()));
	}
	
	
	/**
	 * Creates an exists-condition, based on a subquery.
	 * 
	 * @param subquery
	 *            the subquery to be met
	 * @return an exists-condition object
	 */
	public static Condition EXISTS(SELECT subquery)
	{
		return new Condition(SQL.EXISTS(subquery.delegate()));
	}
	
	private final SqlCondition	delegate;
	
	
	Condition(SqlCondition delegate)
	{
		this.delegate = delegate;
	}
	
	
	@Override
	SqlCondition delegate()
	{
		return delegate;
	}
	
	
	/**
	 * The AND connector for conditions.
	 * <p>
	 * Connects this condition object and the condition parameter via the 'and'
	 * connector and returns the connection as a new condition object.
	 * 
	 * @param condition
	 *            the right side of the connection.
	 * @return a new condition object
	 */
	public Condition AND(Object condition)
	{
		return new Condition(delegate.AND(expose(condition)));
	}
	
	
	/**
	 * The OR connector for conditions.
	 * <p>
	 * Connects this condition object and the condition parameter via the 'or'
	 * connector and returns the connection as a new condition object.
	 * 
	 * @param condition
	 *            the right side of the connection.
	 * @return a new condition object
	 */
	public Condition OR(Object condition)
	{
		return new Condition(delegate.OR(expose(condition)));
	}
}
