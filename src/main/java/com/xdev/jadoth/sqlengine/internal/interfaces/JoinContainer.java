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

import java.util.List;

import com.xdev.jadoth.sqlengine.internal.JoinClause;




/**
 * The Interface JoinContainer.
 * 
 * @param <J> the generic type
 */
public interface JoinContainer<J extends JoinContainer<J>>
{
	
	/**
	 * Gets the joins.
	 * 
	 * @return the joins
	 */
	public List<JoinClause> getJoins();
	
	/**
	 * Sets the joins.
	 * 
	 * @param joins the joins
	 * @return the j
	 */
	public J setJoins(List<JoinClause> joins);
	
	/**
	 * Gets the join count.
	 * 
	 * @return the join count
	 */
	public int getJoinCount();
	
	/**
	 * Adds the join clause.
	 * 
	 * @param join the join
	 * @return the j
	 */
	public J addJoinClause(JoinClause join);
	
	/**
	 * Register join clause.
	 * 
	 * @param <C> the generic type
	 * @param join the join
	 * @return the c
	 */
	public <C extends JoinClause> C registerJoinClause(C join);

}
