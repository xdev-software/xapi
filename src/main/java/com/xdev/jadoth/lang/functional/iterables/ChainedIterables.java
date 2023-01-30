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

import java.util.ArrayList;
import java.util.Iterator;

/**
 * @author Thomas Muenz
 *
 */
public class ChainedIterables<T> implements Iterable<T>
{	
	private final ArrayList<Iterable<T>> iterables;

	public ChainedIterables(final Iterable<T>... iterables)
	{
		super();		
		final ArrayList<Iterable<T>> set = new ArrayList<Iterable<T>>(iterables.length);
		this.iterables = set;
		
		if(iterables == null) return;
		for(final Iterable<T> iterable : iterables) {
			if(iterable == null) continue;
			set.add(iterable);
		}		
		
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
	
	
	
	
	public Iterable<T> add(final Iterable<T> iterable)
	{
		this.iterables.add(iterable);
		return this;
	}
	public ChainedIterables<T> add(final Iterable<T>... iterables)
	{
		for(final Iterable<T> iterable : iterables) {
			this.iterables.add(iterable);
		}
		return this;
	}
	
	public Iterable<T> remove(final Iterable<T> iterable)
	{
		this.iterables.remove(iterable);
		return iterable;
	}
	
	public int remove(final Iterable<T>... iterables)
	{
		int removedCount = 0;
		for(final Iterable<T> i : iterables) {
			if(this.iterables.remove(i)) {
				removedCount++;
			}
		}
		return removedCount;
	}
	
	public boolean contains(final Iterable<T> iterable)
	{
		return this.iterables.contains(iterable);
	}
	
	protected class ChainedIterator implements Iterator<T>
	{
		///////////////////////////////////////////////////////////////////////////
		// instance fields  //
		/////////////////////
		
//		private Iterator<Iterable<T>> iterablesIterator; //o_0\\
		private Iterator<T> currentIterator = null;
		private int currentIndex = 0;

		
		
		///////////////////////////////////////////////////////////////////////////
		// constructors     //
		/////////////////////
		
		private ChainedIterator()
		{
			super();
			this.nextIterator();
		}

		/**
		 * @return
		 * @see java.util.Iterator#hasNext()
		 */
		@Override
		public boolean hasNext()
		{
			if(this.currentIterator.hasNext()) return true;
			
			while(this.nextIterator()){
				if(this.currentIterator.hasNext()) return true;
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
			return this.currentIterator.next();
		}
		
		
		protected boolean nextIterator()
		{			
			final ArrayList<Iterable<T>> iterables = ChainedIterables.this.iterables;
			
			Iterable<T> loopIterable = null;
			//the loops skips null elements until the first existing iterable is encountered
			while(loopIterable == null && this.currentIndex < iterables.size()){
				loopIterable = iterables.get(this.currentIndex++);				
			}
			//if either currentIndex was already at the end or loop scrolled to the end, there are no more iterables
			if(loopIterable == null) return false;
			
			//otherwise, the next iterable has been found.
			this.currentIterator = loopIterable.iterator();
			return true;
		}

		/**
		 * 
		 * @see java.util.Iterator#remove()
		 */
		@Override
		public void remove()
		{
			this.currentIterator.remove();			
		}
		
	}

}
