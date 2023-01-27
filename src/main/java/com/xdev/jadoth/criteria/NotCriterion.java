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
/**
 * 
 */
package com.xdev.jadoth.criteria;

/**
 * @author Thomas Muenz
 *
 */
public class NotCriterion<T> implements Criterion<T>
{
	///////////////////////////////////////////////////////////////////////////
	// instance fields //
	////////////////////
	
	private final Criterion<T> criterion;
	
	
	
	///////////////////////////////////////////////////////////////////////////
	// constructors //
	/////////////////
	
	public NotCriterion(final Criterion<T> criterion)
	{
		this.criterion = criterion;
	}

	
	
	///////////////////////////////////////////////////////////////////////////
	// override methods //
	/////////////////////

	/**
	 * @param criterionElements
	 * @return
	 */
	@Override
	public boolean evaluate(final T... criterionElements)
	{
		return !this.criterion.evaluate(criterionElements);
	}

	
	
	//Below here are just String representation methods that don't influence actual logic	

	@Override
	public String toString()
	{
		return '!'+this.criterion.toString();
	}	
	
}
