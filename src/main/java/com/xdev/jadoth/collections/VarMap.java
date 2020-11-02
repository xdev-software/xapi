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

import java.util.Map;
import java.util.Set;

/**
 * @author Thomas Muenz
 *
 */
public final class VarMap<K, V> implements Map<K, V>
{
	///////////////////////////////////////////////////////////////////////////
	// constants        //
	/////////////////////

	public static final MapEntryProvider IDENTITY_STRONGKEYS_STRONGVALUES = new MapEntryProviderIdentity();

	static final Object NEW_MARKER = new Object();
	static final ThrowRemoveFirstEntry REMOVE_FIRST_ENTRY = new ThrowRemoveFirstEntry();

	static final int MAXIMUM_CAPACITY = 1 << 30;
	
	static final int DEFAULT_TABLE_SIZE = 32;
	static final float DEFAULT_LOAD_FACTOR = 0.75f;



	///////////////////////////////////////////////////////////////////////////
	// static methods //
	///////////////////

//	private static MapEntryProvider chooseMapEntryProvider(
//		final SelectionType selectionType,
//		final ReferenceStorageType keyReferenceType,
//		final ReferenceStorageType valueReferenceType
//	)
//	{
//		switch(selectionType){
//			case EQUALITY:{
//				switch(keyReferenceType){
//					case WEAK: return null;
//					default: return null;
//				}
//			}
//			default: { //IDENTITY
//				switch (valueReferenceType){
//					case WEAK: return null;
//					default: return  IDENTITY_STRONGKEYS_STRONGVALUES;
//				}
//			}
//		}
//	}



	///////////////////////////////////////////////////////////////////////////
	// instance fields //
	////////////////////

	private MapEntry[] table;
	private int size = 0;

	private final MapEntryProvider entryProvider;
	private transient int threshold;
	private float loadFactor;


	// (07.09.2010)FIXME: rehash

	public VarMap(final int tableSize, final float loadFactor, final MapEntryProvider entryProvider)
	{
		super();
		this.entryProvider = entryProvider;
		this.table = new MapEntry[tableSize];
		this.loadFactor = loadFactor;
		this.threshold = (int)(tableSize * loadFactor);
	}

	public VarMap(final MapEntryProvider entryProvider)
	{
		this(DEFAULT_TABLE_SIZE, DEFAULT_LOAD_FACTOR, entryProvider);
	}




//	public VarMap(
//		final SelectionType selectionType,
//		final ReferenceStorageType keyReferenceType,
//		final ReferenceStorageType valueReferenceType
//	)
//	{
//		this(
//			DEFAULT_TABLE_SIZE,
//			DEFAULT_LOAD_FACTOR,
//			chooseMapEntryProvider(selectionType, keyReferenceType, valueReferenceType)
//		);
//	}


	/**
	 *
	 * @see java.util.Map#clear()
	 */
	@Override
	public void clear()
	{
		final Object[] table = this.table;
		for (int i = 0, length = table.length; i < length; i++){
			table[i] = null;
		}
        this.size = 0;

	}

	/**
	 * @param key
	 * @return
	 * @see java.util.Map#containsKey(java.lang.Object)
	 */
	@Override
	public boolean containsKey(final Object key)
	{
		final int hash = this.entryProvider.hash(key);
		final MapEntry[] table = this.table;
		final MapEntry entry = table[hash & table.length-1];

		if(entry == null) return false;
		try {
			return entry.has(hash, key);
		}
		catch(final ThrowRemoveFirstEntry e) {
			table[hash & table.length-1] = entry.next();
			// (08.09.2010 TM)TODO: Maybe create private internalContainsKey() ?
			return this.containsKey(key);
		}
	}

	/**
	 * @param value
	 * @return
	 * @see java.util.Map#containsValue(java.lang.Object)
	 */
	@SuppressWarnings("null")
	@Override
	public boolean containsValue(final Object value)
	{
		final MapEntry[] table = this.table;
		final int length = table.length;
		int i = 0;
		MapEntry loopEntry = null;
		while(i < length){
			try {
				while(i < length){
					if((loopEntry = table[i++]) != null){
						if(loopEntry.has(value)) return true;
					}
				}
			}
			catch(final ThrowRemoveFirstEntry e) {
				// signaling throw can only occur if loopEntry is NOT null, so it's safe to ignore this NPE warning				
				table[i-1] = loopEntry.next();
				return this.containsValue(value);
			}
		}
		return false;
	}

	/**
	 * @return
	 * @see java.util.Map#entrySet()
	 */
	@Override
	public Set<java.util.Map.Entry<K, V>> entrySet()
	{
		return null;
	}

	/**
	 * @param key
	 * @return
	 * @see java.util.Map#get(java.lang.Object)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public V get(final Object key)
	{
		final int hash = this.entryProvider.hash(key);
		final MapEntry[] table = this.table;
		final MapEntry entry = table[hash & table.length-1];

		if(entry == null) return null;
		try {
			return (V)entry.get(hash, key);
		}
		catch(final ThrowRemoveFirstEntry e) {
			table[hash & table.length-1] = entry.next();
			return this.get(key);
		}
	}

	/**
	 * @return
	 * @see java.util.Map#isEmpty()
	 */
	@Override
	public boolean isEmpty()
	{
		return this.size == 0;
	}

	/**
	 * @return
	 * @see java.util.Map#keySet()
	 */
	@Override
	public Set<K> keySet()
	{
		return null;
	}

	/**
	 * @param key
	 * @param value
	 * @return
	 * @see java.util.Map#put(java.lang.Object, java.lang.Object)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public V put(final K key, final V value)
	{
		final int hash = this.entryProvider.hash(key);
		final MapEntry[] table = this.table;
		final int i =  hash & table.length-1;
		final MapEntry entry = table[i];

		if(entry == null){
			table[i] = this.entryProvider.createEntry(hash, key, value);
			if(this.size++ > this.threshold){
				this.resize();
			}
			return null;
		}
		final Object returnValue = entry.set(hash, key, value);
		if(returnValue == NEW_MARKER){
			if(this.size++ > this.threshold){
				this.resize();
			}
			return null;
		}
		return (V)returnValue;
	}
	
	
	private void resize()
	{
		final MapEntry[] oldTable = this.table;
		final int oldCapacity = oldTable.length;
		if (oldCapacity == MAXIMUM_CAPACITY) {
			this.threshold = Integer.MAX_VALUE;
			return;
		}
		
		final int newCapacity = oldCapacity<<1;
		final MapEntry[] newTable = new MapEntry[newCapacity];		
		
		MapEntry e;
		for (int j = 0; j < oldCapacity; j++) {
			if ((e = oldTable[j]) != null) {
				oldTable[j] = null;
				
				do {
					final MapEntry next = e.next();
					final int i =  e.getHash() & newCapacity-1;
                    e.setNext(newTable[i]);
                    newTable[i] = e;
                    e = next;
                } while (e != null);
			}
		}
		
		this.table = newTable;
		this.threshold = (int)(newCapacity * this.loadFactor);
	}


	/**
	 * @param m
	 * @see java.util.Map#putAll(java.util.Map)
	 */
	@Override
	public void putAll(final Map<? extends K, ? extends V> m)
	{
		for(final Entry<? extends K, ? extends V> e : m.entrySet()){
			this.put(e.getKey(), e.getValue());
		}
	}

	/**
	 * @param key
	 * @return
	 * @see java.util.Map#remove(java.lang.Object)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public V remove(final Object key)
	{
		final int hash = this.entryProvider.hash(key);
		final MapEntry[] table = this.table;
		final int i = hash & table.length-1;
		final MapEntry entry = table[i];

		if(entry == null) return null;

		final MapEntry entryToRemove = entry.remove(hash, key);
		if(entryToRemove == entry){
			table[i] = entry.next();
		}
		else if(entryToRemove == null){
			// can this happen? Should it be possible to happen? Or does it indicate a bug?
			return null;
		}
		return (V)entryToRemove.value();
	}

	/**
	 * @return
	 * @see java.util.Map#size()
	 */
	@Override
	public int size()
	{
		return this.size;
	}

	/**
	 * @return
	 * @see java.util.Map#values()
	 */
	@SuppressWarnings("unchecked")
	@Override
	public GrowList<V> values()
	{
		final int size = this.size;
		final GrowList<V> list = new GrowList<V>(size);

		final MapEntry[] table = this.table;
		MapEntry e;
		for(int i = 0, s = 0; s < size; i++){
			if((e = table[i]) != null){
				s += e.addValues((GrowList<Object>)list);
			}
		}
		return list;
	}


	public static final class ThrowRemoveFirstEntry extends Throwable
	{
		private static final long serialVersionUID = 6079333054388431310L;

		private ThrowRemoveFirstEntry()
		{
			super();
		}

		@Override
		public synchronized Throwable fillInStackTrace()
		{
			return this;
		}
	}


	/**
	 * Removes all hollow entries and recalculates the new size of the map.
	 *
	 */
	public void consolidate()
	{
		// (08.09.2010 TM)TODO: consolidate()
	}

	public void setLoadFactor(final float loadFactor)
	{
		if(loadFactor <= 0.0f || loadFactor > 1.0f){
			throw new IllegalArgumentException("load factor must be in ]0.0; 1.0]");
		}
		
		this.loadFactor = loadFactor;
		this.threshold = (int)(this.table.length * loadFactor);
		if(this.table.length > this.threshold){
			this.resize();
		}
	}

}
