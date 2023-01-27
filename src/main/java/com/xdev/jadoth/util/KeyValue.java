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
package com.xdev.jadoth.util;

/**
 * @author Thomas Muenz
 *
 */
public interface KeyValue<K,V>
{
	public K key();
	public V value();
	
	
	public class Implementation<K,V> extends Pair.Implementation<K, V>
	{		
		public Implementation(final K key, final V val)
		{
			super(key, val);
		}
		/**
		 * @return a String of pattern <code>[<i>key</i> -> <i>value</i>]</code>
		 */
		@Override
		public String toString()
		{
			// self-reference can never occur as this implementation is immutable
			return '['+String.valueOf(this.key())+" -> "+String.valueOf(this.value())+']';
		}
		
	}
}
