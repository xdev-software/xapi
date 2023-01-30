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

import java.util.List;

import com.xdev.jadoth.collections.VarMap.ThrowRemoveFirstEntry;
import com.xdev.jadoth.util.KeyValue;


/**
 * @author Thomas Muenz
 *
 */
public interface MapEntry<K, V> extends KeyValue<K, V>
{
	// set doesn't need to signal to remove the first element, get is enough
	public Object set(int hash, K key, V value);

	/**
	 * Throws a {@link ThrowRemoveFirstEntry} signal throw if this entry is the first in the bucket but has become
	 * hollow
	 *
	 * @param hash
	 * @param key
	 * @return
	 * @throws ThrowRemoveFirstEntry
	 */
	public Object get(int hash, K key) throws ThrowRemoveFirstEntry;

	// remove doesn't need to signal to remove the first element as it does so by regular means anyway
	public MapEntry<K, V> remove(int hash, Object key);

	public MapEntry<K, V> next();

	public V value();

	public boolean has(int hash, K key) throws ThrowRemoveFirstEntry;
	public boolean has(Object value) throws ThrowRemoveFirstEntry;

	public int addValues(final List<Object> valuesList);
	
	public int getHash();
	
	public void setNext(MapEntry<K, V> entry);


	public boolean isHollow();
}
