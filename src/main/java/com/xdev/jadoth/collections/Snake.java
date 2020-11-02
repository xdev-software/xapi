/**
 * 
 */
package com.xdev.jadoth.collections;

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

import com.xdev.jadoth.util.VarChar;

/**
 * @author Thomas Muenz
 *
 */
public interface Snake<E> //extends VarChar.Appendable
{
	E value();
	
	Snake<E> tail();
	Snake<E> head();
	
	Snake<E> next();
	Snake<E> prev();
	
	boolean hasNext();
	boolean hasPrev();
		
	Snake<E> hop(int index);
	Snake<E> add(E value);
	E set(E value);
	
	
	

	
	
	class Implementation<E> implements Snake<E>
	{
		///////////////////////////////////////////////////////////////////////////
		// instance fields  //
		/////////////////////
		
		Object value;
		Snake.Implementation<E> next = null;
		Snake.Implementation<E> prev = null;
		
		
		///////////////////////////////////////////////////////////////////////////
		// constructors     //
		/////////////////////	
		
		public Implementation()
		{
			super();
			this.value = null;
		}		
		public Implementation(final Object value)
		{
			super();
			this.value = value;
		}
		
		/**
		 * @return
		 * @see com.xdev.jadoth.collections.Snake#head()
		 */
		@Override
		public Snake<E> head()
		{
			Snake.Implementation<E> link = this;
			while(link.next != null){
				link = link.next;
			}
			return link;
		}
		/**
		 * @param index
		 * @return
		 * @see com.xdev.jadoth.collections.Snake#hop(int)
		 */
		@Override
		public Snake<E> hop(int index)
		{
			Snake.Implementation<E> link = this;
			if(index < 0){
				while(index ++> 0){
					link = link.prev;
				}
			}
			else {
				while(index --> 0){
					link = link.next;
				}
			}
			return link;
		}
		/**
		 * @return
		 * @see com.xdev.jadoth.collections.Snake#tail()
		 */
		@Override
		public Snake<E> tail()
		{
			Snake.Implementation<E> link = this;
			while(link.prev != null){
				link = link.prev;
			}
			return link;
		}
		/**
		 * @return
		 * @see com.xdev.jadoth.collections.Snake#value()
		 */
		@SuppressWarnings("unchecked")
		@Override
		public E value()
		{
			return (E)this.value;
		}
		/**
		 * @return
		 * @see com.xdev.jadoth.collections.Snake#next()
		 */
		@Override
		public Snake<E> next()
		{
			return this.next();
		}
		/**
		 * @return
		 * @see com.xdev.jadoth.collections.Snake#prev()
		 */
		@Override
		public Snake<E> prev()
		{
			return this.prev();
		}
		/**
		 * @param value
		 * @return
		 * @see com.xdev.jadoth.collections.Snake#add(java.lang.Object)
		 */
		@Override
		public Snake<E> add(final E value)
		{
			final Snake.Implementation<E> newLink = new Snake.Implementation<E>();
			newLink.value = value;
			
			newLink.prev = this;
			this.next = newLink;	
			
			return newLink;
		}
		/**
		 * @return
		 * @see com.xdev.jadoth.collections.Snake#hasNext()
		 */
		@Override
		public boolean hasNext()
		{
			return this.next != null;
		}
		/**
		 * @return
		 * @see com.xdev.jadoth.collections.Snake#hasPrev()
		 */
		@Override
		public boolean hasPrev()
		{
			return this.prev != null;
		}
		/**
		 * @param value
		 * @return
		 * @see com.xdev.jadoth.collections.Snake#set(java.lang.Object)
		 */
		@Override
		public E set(final E value)
		{
			final E oldValue = (E)this.value;
			this.value = value;
			return oldValue;
		}
		
		
		
		/**
		 * @return
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString()
		{
			
			if(this.next == null){
				//head
				final VarChar vc = new VarChar().append('Ö');				
				Snake.Implementation<E> link = this;
				do {
					vc.append('(').append(link.value).append(')');
				}
				while((link = link.prev) != null);
				return vc.append('>').toString();
			}
			if(this.prev == null){
				//tail
				final VarChar vc = new VarChar().append('<');				
				Snake.Implementation<E> link = this;
				do {
					vc.append('(').append(link.value).append(')');
				}
				while((link = link.next) != null);
				return vc.append('Ö').toString();				
			}
			return String.valueOf(this.value);
		}
	}
}



