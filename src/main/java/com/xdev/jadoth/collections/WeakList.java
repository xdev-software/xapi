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
package com.xdev.jadoth.collections;

import java.lang.ref.WeakReference;
import java.util.Collection;
import java.util.Iterator;

/**
 * @author Thomas Muenz
 *
 */
@Deprecated
public class WeakList<E>
//implements XList<E>
{
	///////////////////////////////////////////////////////////////////////////
	// instance fields //
	////////////////////

	private final XList<WeakReference<E>> list;



	///////////////////////////////////////////////////////////////////////////
	// constructors //
	/////////////////

	/**
	 * @param list
	 */
	public WeakList(final XList<WeakReference<E>> list)
	{
		super();
		this.list = list;
	}










	/**
	 * @param c
	 * @return
	 * @see java.util.List#addAll(java.util.Collection)
	 */
	public boolean addAll(final Collection<? extends E> c)
	{
		return this.list.addAll(new WeakRefView<E>(c));
	}
}


final class WeakRefView<E> implements Collection<WeakReference<E>>
{
	private final Collection<? extends E> collection;

	WeakRefView(final Collection<? extends E> collection)
	{
		super();
		this.collection = collection;
	}

	@Override
	public Iterator<WeakReference<E>> iterator()
	{
		return new Itr();
	}
	public boolean add(final WeakReference<E> e)
	{
		return false;
	}
	public boolean addAll(final Collection<? extends WeakReference<E>> c)
	{
		return false;
	}
	public void clear()
	{

	}
	public boolean contains(final Object o)
	{
		return this.collection.contains(o);
	}
	public boolean containsAll(final Collection<?> c)
	{
		return this.collection.containsAll(c);
	}
	@Override
	public boolean equals(final Object o)
	{
		return this.collection.equals(o);
	}
	@Override
	public int hashCode()
	{
		return this.collection.hashCode();
	}
	public boolean isEmpty()
	{
		return this.collection.isEmpty();
	}
	public boolean remove(final Object o)
	{
		return false;
	}
	public boolean removeAll(final Collection<?> c)
	{
		return false;
	}
	public boolean retainAll(final Collection<?> c)
	{
		return false;
	}
	public int size()
	{
		return this.collection.size();
	}
	public Object[] toArray()
	{
		return this.collection.toArray();
	}
	public <T> T[] toArray(final T[] a)
	{
		return this.collection.toArray(a);
	}

	class Itr implements Iterator<WeakReference<E>>
	{
		private final Iterator<? extends E> itr = WeakRefView.this.collection.iterator();

		@Override
		public boolean hasNext()
		{
			return this.itr.hasNext();
		}
		@Override
		public WeakReference<E> next()
		{
			final E element = this.itr.next();
			return element == null ?null :new WeakReference<E>(element);
		}
		@Override
		public void remove() throws UnsupportedOperationException
		{
			throw new UnsupportedOperationException("Just a wrapper, don't try removing stuff with it!");
		}

	}

}
