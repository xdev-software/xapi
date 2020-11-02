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

import java.util.Iterator;

/**
 * @author Thomas Muenz
 *
 */
public class ArrayIterator<E> implements Iterator<E>
{
	///////////////////////////////////////////////////////////////////////////
	// instance fields  //
	/////////////////////
	
	private final E[] array;
	
	int index = 0;

	
	
	///////////////////////////////////////////////////////////////////////////
	// constructors     //
	/////////////////////
	
	public ArrayIterator(final E[] array)
	{
		super();
		this.array = array;
	}



	/**
	 * @return
	 * @see java.util.Iterator#hasNext()
	 */
	@Override
	public boolean hasNext()
	{
		return this.index < this.array.length;
	}



	/**
	 * @return
	 * @see java.util.Iterator#next()
	 */
	@Override
	public E next()
	{
		return this.array[this.index++];
	}



	/**
	 * 
	 * @see java.util.Iterator#remove()
	 */
	@Override
	public void remove()
	{
		throw new UnsupportedOperationException();		
	}	
	
}
