package xdev.util;

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


import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Map;
import java.util.NoSuchElementException;

import xdev.lang.Copyable;


/**
 * The standard {@link Map} in XDEV. Based on {@link Hashtable}.
 * 
 * @see Map
 * @see Hashtable
 * 
 * @author XDEV Software
 * @param <K>
 *            type of the <code>key</code> entries.
 * @param <V>
 * 
 * @since 2.0
 */
public class XdevHashtable<K, V> extends Hashtable<K, V> implements Copyable<XdevHashtable<K, V>>
{
	/**
	 * JavaDoc omitted for private field.
	 */
	private Enumeration<K>	cursor		= null;
	/**
	 * JavaDoc omitted for private field.
	 */
	private K				currentKey	= null;
	

	/**
	 * Constructs a new, empty {@link XdevHashtable} with a default initial
	 * capacity (11) and load factor (0.75).
	 */
	public XdevHashtable()
	{
		super();
	}
	

	/**
	 * Constructs a new, empty {@link XdevHashtable} with the specified initial
	 * capacity and default load factor (0.75).
	 * 
	 * @param initialCapacity
	 *            the initial capacity of the {@link XdevHashtable}.
	 * @exception IllegalArgumentException
	 *                if the initial capacity is less than zero.
	 */
	public XdevHashtable(int initialCapacity) throws IllegalArgumentException
	{
		super(initialCapacity);
	}
	

	/**
	 * Constructs a new {@link XdevHashtable} with the same mappings as the
	 * given Map. The XdevHashtable is created with an initial capacity
	 * sufficient to hold the mappings in the given Map and a default load
	 * factor (0.75).
	 * 
	 * @param m
	 *            the map whose mappings are to be placed in this map.
	 * @throws NullPointerException
	 *             if the specified map is null.
	 */
	public XdevHashtable(Map<? extends K, ? extends V> m) throws NullPointerException
	{
		super(m);
	}
	

	/**
	 * Constructs a new {@link XdevHashtable} with the given value pairs. The
	 * given parameters become paired in the order that they are passed to the
	 * method. The first parameter will be the key, the second parameter will be
	 * the value for the first pair (and so on)...
	 * 
	 * @param keysAndValues
	 *            key value pairs in sequence
	 * 
	 * @throws IllegalArgumentException
	 *             if the parameter count is not even.
	 */
	public XdevHashtable(Object... keysAndValues) throws IllegalArgumentException
	{
		super(keysAndValues.length / 2);
		
		if(keysAndValues.length % 2 != 0)
		{
			throw new IllegalArgumentException("keysAndValues.length % 2 != 0");
		}
		
		for(int i = 0; i < keysAndValues.length; i += 2)
		{
			put((K)keysAndValues[i],(V)keysAndValues[i + 1]);
		}
	}
	

	/**
	 * {@inheritDoc}
	 */
	@Override
	public synchronized V put(K key, V value)
	{
		resetCursor();
		return super.put(key,value);
	}
	

	/**
	 * {@inheritDoc}
	 */
	@Override
	public synchronized void putAll(Map<? extends K, ? extends V> t)
	{
		resetCursor();
		super.putAll(t);
	}
	

	/**
	 * {@inheritDoc}
	 */
	@Override
	public synchronized V remove(Object key)
	{
		resetCursor();
		return super.remove(key);
	}
	

	/**
	 * {@inheritDoc}
	 */
	@Override
	public synchronized void clear()
	{
		resetCursor();
		super.clear();
	}
	

	/**
	 * Returns a {@link XdevList} containing all the <code>keys</code> of this
	 * {@link XdevHashtable}.
	 * 
	 * @return a {@link XdevList} containing all the <code>keys</code> of this
	 *         {@link XdevHashtable}.
	 */
	public XdevList<K> getKeys()
	{
		return new XdevList(keySet());
	}
	

	/**
	 * Returns a {@link XdevList} containing all the <code>values</code> of this
	 * {@link XdevHashtable}.
	 * 
	 * @return a {@link XdevList} containing all the <code>values</code> of this
	 *         {@link XdevHashtable}.
	 */
	public XdevList<V> getValues()
	{
		return new XdevList(values());
	}
	

	/**
	 * Resets the cursor for this {@link XdevHashtable}.
	 */
	public void resetCursor()
	{
		cursor = null;
		currentKey = null;
	}
	

	/**
	 * Tests if this {@link XdevHashtable} contains more elements.
	 * 
	 * <p>
	 * If this {@link XdevHashtable} contains more elements, the cursor is set
	 * to next element; otherwise the cursor is reseted via
	 * {@link #resetCursor()}.
	 * 
	 * @return <code>true</code> if and only if this {@link XdevHashtable}
	 *         object contains at least one more element to provide;
	 *         <code>false</code> otherwise.
	 */
	public boolean next()
	{
		if(cursor == null)
		{
			cursor = keys();
			currentKey = null;
		}
		
		boolean next = cursor.hasMoreElements();
		
		if(next)
		{
			currentKey = cursor.nextElement();
		}
		else
		{
			resetCursor();
		}
		
		return next;
	}
	

	/**
	 * Returns the <code>key</code> at the current cursor position.
	 * 
	 * @return the <code>key</code> at the current cursor position.
	 * 
	 * @see #next()
	 * @see #resetCursor()
	 */
	public K getKey()
	{
		if(cursor == null)
		{
			throw new NoSuchElementException("Cursor out of Bounds");
		}
		
		return currentKey;
	}
	

	/**
	 * Returns the <code>value</code> at the current cursor position.
	 * 
	 * @return the <code>value</code> at the current cursor position.
	 * 
	 * @see #next()
	 * @see #resetCursor()
	 */
	public V getValue()
	{
		if(cursor == null)
		{
			throw new NoSuchElementException("Cursor out of Bounds");
		}
		
		return get(currentKey);
	}
	

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString()
	{
		return toString(", ");
	}
	

	/**
	 * Returns a {@link String} representation of all elements of this
	 * {@link XdevHashtable} separated by the specified separator.
	 * 
	 * @param separator
	 *            String to separated the list elements.
	 * @return a {@link String} representation of all elements of this
	 *         {@link XdevHashtable} separated by the specified separator.
	 */
	public String toString(String separator)
	{
		StringBuffer sb = new StringBuffer();
		Enumeration<K> e = keys();
		Object key, val;
		int i = 0;
		while(e.hasMoreElements())
		{
			if(i > 0)
			{
				sb.append(separator);
			}
			
			key = e.nextElement();
			sb.append(key == this ? "this" : key);
			
			sb.append("=");
			
			val = get(key);
			sb.append(val == this ? "this" : String.valueOf(val));
			
			i++;
		}
		return sb.toString();
	}
	

	/**
	 * Creates a (defensive) copy of this {@link XdevHashtable}.
	 * 
	 * @return a (defensive) copy of this {@link XdevHashtable}.
	 */
	public XdevHashtable<K, V> copy()
	{
		return new XdevHashtable<K, V>(this);
	}
	

	/**
	 * {@inheritDoc}
	 */
	@Override
	public synchronized XdevHashtable<K, V> clone()
	{
		return copy();
	}
}
