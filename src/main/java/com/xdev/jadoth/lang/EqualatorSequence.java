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
package com.xdev.jadoth.lang;


/**
 * Useful for implementing SQL-like "GROUP BY" for collections. 
 * 
 * @author Thomas Muenz
 *
 */
public class EqualatorSequence<T> implements Equalator<T>
{
	final private Equalator<T>[] equalators;

	public EqualatorSequence(final Equalator<T>... equalators)
	{
		super();
		this.equalators = equalators;
	}

	@Override
	public boolean equal(final T o1, final T o2)
	{			
		for(final Equalator<T> eq : this.equalators){
			if(!eq.equal(o1, o2)) return false;
		}
		return true;
	}	
	
}
