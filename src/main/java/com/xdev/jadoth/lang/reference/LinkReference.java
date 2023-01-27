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
package com.xdev.jadoth.lang.reference;

import java.util.Iterator;


/**
 * @author Thomas Muenz
 *
 */
public interface LinkReference<T> extends LinkingReference<T>
{
	public LinkReference<T> next();
	
	/**
	 * Sets <code>linkedReference</code> as this <code>LinkedReference</code> object's 
	 * linked <code>LinkedReference</code> object.
	 * <p>
	 * Note that the so far linked <code>LinkedReference</code> object is returned, not this object itself! 
	 * @param linkedReference
	 * @return
	 */
	public LinkReference<T> setNext(LinkReference<T> linkedReference);
	
	
	/**
	 * Sets <code>linkedReference</code> as this <code>LinkedReference</code> object's 
	 * linked <code>LinkedReference</code> object.
	 * <p>
	 * Note that the reference is returned, not this object itself! 
	 * @param linkedReference
	 * @return the linked <code>LinkedReference</code> object (NOT this!)
	 */
	public LinkReference<T> link(LinkReference<T> linkedReference);
		
	/**
	 * Alias for <code>link(new LinkedReference(nextRef))</code>.
	 * @param nextRef
	 * @return
	 */
	public LinkReference<T> link(T nextRef);
	
	
	public LinkReference<T> insert(LinkReference<T> linkedReference);
	public LinkReference<T> removeNext();
		
	
	public class Implementation<T> extends Reference.Implementation<T> implements LinkReference<T>
	{		

		private LinkReference<T> next = null;

		
		/**
		 * @param ref
		 */
		public Implementation(final T ref)
		{
			super(ref);
		}
		
		
		/**
		 * @return
		 */
		@Override
		public LinkReference<T> next()
		{
			return this.next;
		}

		/**
		 * @return
		 */
		@Override
		public boolean hasNext()
		{
			return this.next != null;
		}

		/**
		 * @param linkedReference
		 * @return
		 */
		@Override
		public boolean isNext(final LinkingReferencing<T> linkedReference)
		{
			return this.next == linkedReference;
		}

		/**
		 * @param linkedReference
		 * @return
		 */
		@Override
		public LinkReference<T> link(final LinkReference<T> linkedReference)
		{
			this.next = linkedReference;
			return linkedReference;
		}


		/**
		 * @param linkedReference
		 * @return
		 */
		@Override
		public LinkReference<T> setNext(final LinkReference<T> linkedReference)
		{
			final LinkReference<T> old = this.next;
			this.next = linkedReference;
			return old;
		}


		/**
		 * @param nextRef
		 * @return
		 */
		@Override
		public LinkReference<T> link(final T nextRef)
		{
			return this.link(new LinkReference.Implementation<T>(nextRef));
		}


		/**
		 * @param linkedReference
		 * @return
		 * @throws NullPointer, if <code>linkedReference</code> is <tt>null</tt>.
		 */
		@Override
		public LinkReference<T> insert(final LinkReference<T> linkedReference)
		{
			final LinkReference<T> next = this.next;
			this.next = linkedReference;
			
			if(next != null){
				linkedReference.setNext(next); //provoke NullPointer for argument
			}			
			return this;
		}


		/**
		 * @return
		 */
		@Override
		public LinkReference<T> removeNext()
		{
			final LinkReference<T> next = this.next;			
			this.next = next==null ?null :next.next();			
			return next;
		}


		/**
		 * @return
		 */
		@Override
		public Object[] toArray()
		{			
			LinkReference<T> loopNext = this;			
			int i = 1;
			while((loopNext = loopNext.next()) != null){
				i++; //this is presumable faster than using an ArrayList or LinkedList for collection
			}
			
			final Object[] array = new Object[i];			
			loopNext = this;
			i = 0;
			do{
				array[i++] = loopNext.get();
			}
			while((loopNext = loopNext.next()) != null);
			
			return array;
		}


		/**
		 * @return
		 */
		@Override
		public Iterator<T> iterator()
		{
			return new ChainIterator<T>(this);
		}
		
		
		/**
		 * @return
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString()
		{
			final String e = String.valueOf(this.get());
			final StringBuilder sb = new StringBuilder(e.length()+3);
			return sb.append('(').append(e).append(')').append(this.hasNext() ?'-' :'x').toString();
		}
		
		/**
		 * @return
		 * @see com.xdev.jadoth.lang.reference.LinkingReferencing#toChainString()
		 */
		@Override
		public String toChainString()
		{
			final StringBuilder sb = new StringBuilder(1024);
			sb.append('(').append(this.get()).append(')');
			for(LinkingReferencing<T> r = this.next; r != null; r = r.next()){
				sb.append('-').append('(').append(r.get()).append(')');
			}
			return sb.toString();
		}
		
	}
	
	
	final class ChainIterator<T> implements Iterator<T>
	{
		private LinkReference<T> current;
		
		

		/**
		 * @param current
		 */
		private ChainIterator(final LinkReference<T> current)
		{
			super();
			this.current = current;
		}

		@Override
		public boolean hasNext()
		{
			return current.next() != null;
		}

		@Override
		public T next()
		{
			final LinkReference<T> currentCurrent = this.current;
			this.current = currentCurrent.next();
			return currentCurrent.get();
		}

		@Override
		public void remove() throws UnsupportedOperationException
		{
			throw new UnsupportedOperationException(
				"Can't remove current element in a one directional chain"
			);					
		}
	}
}
