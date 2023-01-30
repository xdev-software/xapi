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
package com.xdev.jadoth.lang.functional.iterables;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * @author Thomas Muenz
 *
 */
public class ChainedArraysIterable<T> implements Iterable<T>
{
	private final T[][] iterables;

	public ChainedArraysIterable(final T[]... iterables)
	{
		super();
		this.iterables = iterables;
	}

	/**
	 * @return
	 * @see java.lang.Iterable#iterator()
	 */
	@Override
	public Iterator<T> iterator()
	{
		return new ChainedIterator();
	}
	
	
	protected class ChainedIterator implements Iterator<T>
	{
		private int currentArrayMasterIndex = -1;
		private int currentArrayAccessIndex = 0;
		private T[] currentArray = null;
		{
			this.nextArray();
		}
		



		/**
		 * @return
		 * @see java.util.Iterator#hasNext()
		 */
		@Override
		public boolean hasNext()
		{
			if(this.currentArrayAccessIndex < this.currentArray.length) return true;
			
			while(this.nextArray()){
				if(this.currentArrayAccessIndex < this.currentArray.length) return true;
			}
			return false;
		}
		
		/**
		 * @return
		 * @see java.util.Iterator#next()
		 */
		@Override
		public T next()
		{
			try {
				return this.currentArray[this.currentArrayAccessIndex++];	
			}
			catch(final ArrayIndexOutOfBoundsException e) {
				throw new NoSuchElementException();
			}			
		}
		
		protected boolean nextArray()
		{
			this.currentArrayAccessIndex = 0;
			final T[][] iterables = ChainedArraysIterable.this.iterables;
			int loopIndex = this.currentArrayMasterIndex;
			T[] loopIterable = null;
			while(loopIterable == null){
				loopIndex++;
				if(loopIndex == iterables.length){
					return false;
				}
				loopIterable = iterables[loopIndex];				
			}
			this.currentArray = loopIterable;
			this.currentArrayMasterIndex = loopIndex;

			return true;
		}

		/**
		 * 
		 * @see java.util.Iterator#remove()
		 */
		@Override
		public void remove()
		{
			this.currentArray[this.currentArrayAccessIndex] = null;		
		}
		
	}

}
