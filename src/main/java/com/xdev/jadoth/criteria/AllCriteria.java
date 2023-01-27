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
package com.xdev.jadoth.criteria;

/**
 * 
 */

/**
 * @author Thomas Muenz
 *
 */
public class AllCriteria<T> extends Criteria<T>
{
	///////////////////////////////////////////////////////////////////////////
	// constructors //
	/////////////////
	
	/**
	 * Note that best performance is achieved when ordering criteria in ascending order of estimated probability
	 * (starting with the least probable criterion)
	 * 
	 * @param criteria
	 */
	public AllCriteria(final Criterion<T>... criteria)
	{
		super(criteria);
	}

	
	
	///////////////////////////////////////////////////////////////////////////
	// override methods //
	/////////////////////
	
	@Override
	public boolean evaluate(final T... criterionElements)
	{
		for(final Criterion<T> element : this.getCriteria()) {
			if(!element.evaluate(criterionElements)) return false;			
		}		
		return true;
	}


	
	//Below here are just String representation methods that don't influence actual logic	
	
	/**
	 * @param sb
	 * @return
	 * @see com.xdev.jadoth.criteria.Criteria#assembleOperator(java.lang.StringBuilder)
	 */	
	@Override
	protected StringBuilder assembleOperator(final StringBuilder sb)
	{
		return sb.append(' ').append('&').append('&').append(' ');
	}
	
}
