/**
 * 
 */
package com.xdev.jadoth.lang.functional.iterables;

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

/**
 * @author Thomas Muenz
 *
 */
public class RandomArrayIterator<E> //implements Iterator<E>
{
	private final E[] array;
	
	int count;

	public RandomArrayIterator(final E[] array, final int count)
	{
		super();
		this.array = array;
		this.count = count;
	}
	
	// (28.08.2010)TODO RandomArrayIterator
	
	
}
