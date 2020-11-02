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

import java.util.List;

/**
 * @author Thomas Muenz
 *
 */


public final class MapEntryProviderIdentity implements MapEntryProvider
{

	/**
	 * @param hash
	 * @param key
	 * @param value
	 * @return
	 * @see com.xdev.jadoth.collections.MapEntryProvider#createEntry(int, java.lang.Object, java.lang.Object)
	 */
	@Override
	public <K, V> IdentityMapEntry<K, V> createEntry(final int hash, final K key, final V value)
	{
		return new IdentityMapEntry<K, V>(/*hash, */key, value);
	}

	/**
	 * @param key
	 * @return
	 * @see com.xdev.jadoth.collections.MapEntryProvider#hash(java.lang.Object)
	 */
	@Override
	public int hash(final Object key)
	{
		return System.identityHashCode(key);
	}

}


final class IdentityMapEntry<K, V> implements MapEntry<K, V>
{
	private final Object key;
	private Object value;
//	private final int hash;
	private IdentityMapEntry<K, V> next = null;

	IdentityMapEntry(/*final int hash, */final Object key, final Object value)
	{
		super();
//		this.hash = hash;
		this.key = key;
		this.value = value;
	}

	/**
	 * @param hash
	 * @param key
	 * @param value
	 * @param provider
	 * @return
	 * @see com.xdev.jadoth.collections.MapEntry#set(int, java.lang.Object, java.lang.Object, com.xdev.jadoth.collections.MapEntryProvider)
	 */
	@Override
	public Object set(final int hash, final Object key, final Object value)
	{
		if(this.key == key){
			final Object oldValue = this.value;
			this.value = value;
			return oldValue;
		}

		IdentityMapEntry<K,V> current = this;
		while(current.next != null){
			current = current.next;
			if(current.key == key){
				final Object oldValue = current.value;
				current.value = value;
				return oldValue;
			}
		}
		current.next = new IdentityMapEntry<K,V>(/*hash, */key, value);
		return VarMap.NEW_MARKER;
	}

	/**
	 * @param hash
	 * @param key
	 * @return
	 * @see com.xdev.jadoth.collections.MapEntry#get(int, java.lang.Object)
	 */
	@Override
	public Object get(final int hash, final Object key)
	{
		if(this.key == key){
			return this.value;
		}
		IdentityMapEntry<K,V> current = this;
		while((current = current.next) != null){
			if(current.key == key){
				return current.value;
			}
		}
		return null;
	}

	/**
	 * @return
	 * @see com.xdev.jadoth.collections.MapEntry#next()
	 */
	@Override
	public MapEntry<K,V> next()
	{
		return this.next();
	}

	/**
	 * @param hash
	 * @param key
	 * @return
	 * @see com.xdev.jadoth.collections.MapEntry#remove(int, java.lang.Object)
	 */
	@Override
	public MapEntry<K,V> remove(final int hash, final Object key)
	{
		if(this.key == key){
			return this;
		}
		IdentityMapEntry<K,V> current = this;
		while((current = current.next) != null){
			if(current.key == key){
				return current;
			}
		}
		return null;
	}

	/**
	 * @return
	 * @see com.xdev.jadoth.collections.MapEntry#value()
	 */
	@SuppressWarnings("unchecked")
	@Override
	public V value()
	{
		return (V)this.value;
	}

	/**
	 * @param hash
	 * @param key
	 * @return
	 * @see com.xdev.jadoth.collections.MapEntry#has(int, java.lang.Object)
	 */
	@Override
	public boolean has(final int hash, final Object key)
	{
		if(this.key == key){
			return true;
		}
		IdentityMapEntry<K,V> current = this;
		while((current = current.next) != null){
			if(current.key == key){
				return true;
			}
		}
		return false;
	}

	/**
	 * @param value
	 * @return
	 * @see com.xdev.jadoth.collections.MapEntry#has(java.lang.Object)
	 */
	@Override
	public boolean has(final Object value)
	{
		if(this.value == value){
			return true;
		}
		IdentityMapEntry<K,V> current = this;
		while((current = current.next) != null){
			if(current.value == value){
				return true;
			}
		}
		return false;
	}

	/**
	 * @param valuesList
	 * @return
	 * @see com.xdev.jadoth.collections.MapEntry#addValues(java.util.List)
	 */
	@Override
	public int addValues(final List<Object> valuesList)
	{
		valuesList.add(this.value);
		int count = 1;
		IdentityMapEntry<K,V> current = this;
		while((current = current.next) != null){
			valuesList.add(current.value);
			count++;
		}
		return count;
	}

	/**
	 * @return
	 */
	@Override
	public boolean isHollow()
	{
		// concrete reference entries can never become hollow
		return false;
	}

	
	/**
	 * @return
	 * @see com.xdev.jadoth.collections.MapEntry#getHash()
	 */
	@Override
	public int getHash()
	{
		return System.identityHashCode(this.key);
	}

	/**
	 * @param entry
	 * @see com.xdev.jadoth.collections.MapEntry#setNext(com.xdev.jadoth.collections.MapEntry)
	 */
	@Override
	public void setNext(final MapEntry<K,V> entry)
	{
		this.next = (IdentityMapEntry<K,V>)entry;	
	}

	/**
	 * @return
	 * @see com.xdev.jadoth.util.KeyValue#key()
	 */
	@SuppressWarnings("unchecked")
	@Override
	public K key()
	{
		return (K)this.key;
	}

}
