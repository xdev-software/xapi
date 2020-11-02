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
public class DummyIterable<T> implements Iterable<T>
{
	private T element;

	/**
	 * @return
	 * @see java.lang.Iterable#iterator()
	 */
	@Override
	public Iterator<T> iterator()
	{
		return new DummyIterator();
	}
	
	
	public void set(final T element)
	{
		this.element = element;
	}
	
	public T get()
	{
		return this.element;
	}
	
	
	
	private class DummyIterator implements Iterator<T>
	{
		private boolean hasNext = true;
		
		/**
		 * @return
		 * @see java.util.Iterator#hasNext()
		 */
		@Override
		public boolean hasNext()
		{
			return this.hasNext;
		}

		/**
		 * @return
		 * @see java.util.Iterator#next()
		 */
		@Override
		public T next()
		{
			this.hasNext = false;
			return DummyIterable.this.element;
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
}
