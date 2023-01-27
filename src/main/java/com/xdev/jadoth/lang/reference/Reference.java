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

/**
 * @author Thomas Muenz
 *
 */
/**
 * Simple Reference class to handle mutable references. Handle with care!
 * <p>
 * Note: In most cases, a mutable reference object like this should not be neccessary if the program is well
 * structured (that's why no such class exists in the Java API). 
 * Extensive use of this class where it would be better to restructure the program may end in even more structural 
 * problems.<br>
 * Yet in some cases, a mutable reference really is needed or at least helps in creating cleaner structures.<br>
 * So again, use wisely.
 */
public interface Reference<T> extends Referencing<T>
{	
	public void set(T object);
	
	
	
	public class Implementation<T> implements Reference<T>
	{
		private T ref;		
		
		public Implementation(final T ref)
		{
			super();
			this.ref = ref;
		}

		@Override
		public T get()
		{
			return this.ref;
		}
		
		@Override
		public void set(final T object)
		{
			this.ref = object;
		}
		
	}
	
}
