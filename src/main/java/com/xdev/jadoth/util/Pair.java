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
public interface Pair<F,S>
{
	public F first();
	public S second();
	
	
	public class Implementation<F,S> implements Pair<F,S>, KeyValue<F, S>
	{
		private final F first;
		private final S second;
		
		public Implementation(final F first, final S second)
		{
			super();
			this.first = first;
			this.second = second;
		}
			
		
		@Override
		public F first()
		{
			return this.first;
		}
		@Override
		public S second()
		{
			return this.second;
		}
		
		/**
		 * @return a String of pattern <code>(<i>first</i>,<i>second</i>)</code>
		 */
		@Override
		public String toString()
		{
			// self-reference can never occur as this implementation is immutable
			return '('+String.valueOf(this.first)+','+String.valueOf(this.second)+')';
		}


		/**
		 * @return
		 */
		@Override
		public F key()
		{
			return this.first;
		}


		/**
		 * @return
		 */
		@Override
		public S value()
		{
			return this.second;
		}
		
	}
}
