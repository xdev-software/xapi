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
package xdev.util;


import java.util.Collection;
import java.util.List;
import java.util.Vector;

import xdev.lang.Copyable;


/**
 * The standard {@link List} in XDEV. Based on {@link Vector}.
 * 
 * @see List
 * @see Vector
 * 
 * @author XDEV Software
 * @param <E>
 *            type of the list's entries.
 * @since 2.0
 */
public class XdevList<E> extends Vector<E> implements Copyable<XdevList<E>>
{
	/**
	 * JavaDoc omitted for private field.
	 */
	private String	separator	= ",";
	

	/**
	 * Constructs an empty {@link XdevList} so that its internal data array has
	 * size {@code 10} and its standard capacity increment is zero.
	 */
	public XdevList()
	{
		super();
	}
	

	/**
	 * Constructs a {@link XdevList} containing the elements of the specified
	 * values, in the order they are given to this method.
	 * 
	 * @param values
	 *            the values to be placed into this {@link XdevList}
	 * 
	 * @param <V>
	 *            type of the values to be added
	 */
	public <V extends E> XdevList(V... values)
	{
		this(values.length);
		
		for(int i = 0; i < values.length; i++)
		{
			add(values[i]);
		}
	}
	

	/**
	 * Constructs an empty {@link XdevList} with the specified initial capacity
	 * and with its capacity increment equal to zero.
	 * 
	 * @param initialCapacity
	 *            the initial capacity of the {@link XdevList}
	 * @throws IllegalArgumentException
	 *             if the specified initial capacity is negative
	 */
	public XdevList(int initialCapacity) throws IllegalArgumentException
	{
		super(initialCapacity);
	}
	

	/**
	 * Constructs a {@link XdevList} containing the elements of the specified
	 * collection, in the order they are returned by the collection's iterator.
	 * 
	 * @param c
	 *            the collection whose elements are to be placed into this
	 *            {@link XdevList}
	 * @throws NullPointerException
	 *             if the specified collection is null
	 */
	public XdevList(Collection<? extends E> c) throws NullPointerException
	{
		super(c);
	}
	

	/**
	 * Constructs an empty {@link XdevList} with the specified initial capacity
	 * and capacity increment.
	 * 
	 * @param initialCapacity
	 *            the initial capacity of the {@link XdevList}
	 * @param capacityIncrement
	 *            the amount by which the capacity is increased when the
	 *            {@link XdevList} overflows
	 * @throws IllegalArgumentException
	 *             if the specified initial capacity is negative
	 */
	public XdevList(int initialCapacity, int capacityIncrement) throws IllegalArgumentException
	{
		super(initialCapacity,capacityIncrement);
	}
	

	/**
	 * Deletes all duplicate values from this {@link XdevList}. A
	 * <code>value</code> is delete if there is a other <code>value</code> in
	 * this {@link XdevList} where
	 * 
	 * <pre>
	 * value1.equals(value2) == true
	 * </pre>
	 * 
	 * 
	 * 
	 * @return the number of deletes values
	 */
	@SuppressWarnings("unchecked")
	// OK, because o is always of type E (or a subtype of E)
	public int deleteDoubleValues()
	{
		int size = size();
		
		Object[] o = toArray();
		clear();
		
		for(int i = 0; i < o.length; i++)
		{
			if(!contains(o[i]))
			{
				
				add((E)o[i]);
			}
		}
		
		return size - size();
	}
	

	/**
	 * Inserts the specified element <code>value</code> at the specified
	 * position in this {@link XdevList}. Shifts the element currently at that
	 * position (if any) and any subsequent elements to the right (adds one to
	 * their indices).
	 * 
	 * @param index
	 *            index at which the specified element is to be inserted
	 * @param value
	 *            element to be inserted
	 * @throws ArrayIndexOutOfBoundsException
	 *             if the index is out of range (
	 *             {@code index < 0 || index > size()})
	 */
	public void insert(int index, E value) throws ArrayIndexOutOfBoundsException
	{
		add(index,value);
	}
	

	/**
	 * Removes the first occurrence of the specified element in this
	 * {@link XdevList} If the {@link XdevList} does not contain the element, it
	 * is unchanged. More formally, removes the element with the lowest index i
	 * such that {@code (object==null ? get(i)==null : object.equals(get(i)))}
	 * (if such an element exists).
	 * 
	 * @param object
	 *            element to be removed from this {@link XdevList}, if present
	 * @return the removed object if the {@link XdevList} contained the
	 *         specified element; otherwise <code>null</code>
	 */
	public Object removeObject(Object object)
	{
		if(remove(object))
		{
			return object;
		}
		
		return null;
	}
	

	/**
	 * Return the separator of this {@link XdevList}.
	 * 
	 * @return the separator of this {@link XdevList}.
	 */
	public String getSeparator()
	{
		return separator;
	}
	

	/**
	 * Sets the separator of this {@link XdevList}.
	 * 
	 * @param separator
	 *            the separator of this {@link XdevList}.
	 */
	public void setSeparator(String separator)
	{
		this.separator = separator;
	}
	

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString()
	{
		return toString(separator);
	}
	

	/**
	 * Returns a {@link String} representation of all elements of this
	 * {@link XdevList} separated by the specified separator.
	 * 
	 * @param separator
	 *            String to separated the list elements.
	 * @return a {@link String} representation of all elements of this
	 *         {@link XdevList} separated by the specified separator.
	 */
	public String toString(String separator)
	{
		StringBuffer sb = new StringBuffer();
		int c = size();
		for(int i = 0; i < c; i++)
		{
			if(i > 0)
			{
				sb.append(separator);
			}
			
			Object o = get(i);
			if(o == this)
			{
				sb.append("this");
			}
			else
			{
				sb.append(String.valueOf(get(i)));
			}
		}
		return sb.toString();
	}
	

	/**
	 * Creates a (defensive) copy of this {@link XdevList}.
	 * 
	 * @return a (defensive) copy of this {@link XdevList}.
	 */
	public XdevList<E> copy()
	{
		XdevList<E> list = new XdevList<E>(this);
		list.separator = separator;
		return list;
	}
	

	/**
	 * {@inheritDoc}
	 */
	@Override
	public synchronized XdevList<E> clone()
	{
		return copy();
	}
}
