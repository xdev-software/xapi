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

package com.xdev.jadoth.sqlengine.internal.interfaces;


/**
 * The Interface Joining.
 * 
 * @param <J> the generic type
 */
public interface Joining<J extends Joining<J>>
{
	
	/**
	 * INNE r_ join.
	 * 
	 * @param table the table
	 * @param joinCondition the join condition
	 * @return the j
	 */
	public J INNER_JOIN(TableExpression table, Object joinCondition);
	
	/**
	 * LEF t_ join.
	 * 
	 * @param table the table
	 * @param joinCondition the join condition
	 * @return the j
	 */
	public J LEFT_JOIN(TableExpression table, Object joinCondition);
	
	/**
	 * RIGH t_ join.
	 * 
	 * @param table the table
	 * @param joinCondition the join condition
	 * @return the j
	 */
	public J RIGHT_JOIN(TableExpression table, Object joinCondition);
	
	/**
	 * OUTE r_ join.
	 * 
	 * @param table the table
	 * @param joinCondition the join condition
	 * @return the j
	 */
	public J OUTER_JOIN(TableExpression table, Object joinCondition);	
}
