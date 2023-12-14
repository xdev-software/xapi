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


import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import xdev.lang.Copyable;
import xdev.lang.LibraryMember;
import xdev.lang.NotNull;
import xdev.lang.Nullable;


/**
 * 
 * <p>
 * The <code>ObjectUtils</code> class provides utility methods for
 * {@link Object} handling.
 * </p>
 * 
 * @author XDEV Software
 * 
 * @since 2.0
 */
@LibraryMember
public final class ObjectUtils
{
	
	/**
	 * <p>
	 * <code>ObjectUtils</code> instances can not be instantiated. The class
	 * should be used as utility class: <code>ObjectUtils.equals(o1, o2);</code>
	 * .
	 * </p>
	 */
	private ObjectUtils()
	{
	}
	
	
	/**
	 * <p>
	 * Compares two objects for equality, where either one or both objects may
	 * be <code>null</code>.
	 * </p>
	 * 
	 * <pre>
	 * ObjectUtils.equals(null, null)                  = true
	 * ObjectUtils.equals(null, "")                    = false
	 * ObjectUtils.equals("", null)                    = false
	 * ObjectUtils.equals("", "")                      = true
	 * ObjectUtils.equals(Boolean.TRUE, null)          = false
	 * ObjectUtils.equals(Boolean.TRUE, "true")        = false
	 * ObjectUtils.equals(Boolean.TRUE, Boolean.TRUE)  = true
	 * ObjectUtils.equals(Boolean.TRUE, Boolean.FALSE) = false
	 * </pre>
	 * 
	 * @param o1
	 *            the first object, may be <code>null</code>
	 * @param o2
	 *            the second object, may be <code>null</code>
	 * @return <code>true</code> if the values of both objects are the same
	 */
	public static boolean equals(@Nullable Object o1, @Nullable Object o2)
	{
		if(o1 == o2)
		{
			return true;
		}
		
		if(o1 == null || o2 == null)
		{
			return false;
		}
		
		if(o1.getClass().isArray() && o2.getClass().isArray())
		{
			int length = Array.getLength(o1);
			if(length != Array.getLength(o2))
			{
				return false;
			}
			
			for(int i = 0; i < length; i++)
			{
				if(!equals(Array.get(o1,i),Array.get(o2,i)))
				{
					return false;
				}
			}
			
			return true;
		}
		
		return o1.equals(o2);
	}
	
	
	/**
	 * 
	 * Creates a clone using the clone method of {@link Object} /
	 * {@link Cloneable}.
	 * 
	 * @param <T>
	 *            the type of the object that will be cloned
	 * 
	 * @param original
	 *            the original object that will be cloned
	 * 
	 * @return a clone of the <code>original</code> object
	 * 
	 * @throws RuntimeException
	 *             if clone process does not succeed
	 * 
	 * @see Copyable
	 */
	public static <T extends Cloneable> T clone(@Nullable T original) throws RuntimeException
	{
		if(original == null)
		{
			return null;
		}
		
		try
		{
			Class<?> clazz = original.getClass();
			if(clazz.isArray())
			{
				clazz = clazz.getComponentType();
				int length = Array.getLength(original);
				@SuppressWarnings("unchecked")
				// OK, because class is of type T
				T clone = (T)Array.newInstance(clazz,length);
				for(int i = 0; i < length; i++)
				{
					Array.set(clone,i,clone((Cloneable)Array.get(original,i)));
				}
				return clone;
			}
			
			if(original instanceof Copyable)
			{
				return ((Copyable<T>)original).clone();
			}
			
			@SuppressWarnings("unchecked")
			// OK, because class is of type T
			T clone = (T)clazz.getMethod("clone").invoke(original);
			while(clazz != null)
			{
				for(Field field : clazz.getDeclaredFields())
				{
					int modifiers = field.getModifiers();
					if(Modifier.isStatic(modifiers) || Modifier.isTransient(modifiers))
					{
						continue;
					}
					
					boolean accessible = field.isAccessible();
					try
					{
						if(!accessible)
						{
							field.setAccessible(true);
						}
						
						Object cloneValue = field.get(clone);
						if(cloneValue == null)
						{
							Object originalValue = field.get(original);
							if(originalValue instanceof Cloneable)
							{
								field.set(clone,clone((Cloneable)originalValue));
							}
						}
					}
					finally
					{
						if(!accessible)
						{
							field.setAccessible(false);
						}
					}
				}
				
				clazz = clazz.getSuperclass();
			}
			
			return clone;
		}
		catch(Throwable t)
		{
			throw new RuntimeException("Clone failed",t);
		}
	}
	
	
	/**
	 * 
	 * Checks if the provided <code>value</code> is <code>null</code>. In this
	 * case <code>ifNull</code> is returned. If <code>value</code> is not
	 * <code>null</code> <code>value</code> itself is returned.
	 * 
	 * @param <T>
	 *            type of the return value and all paramters
	 * @param value
	 *            to check for <code>null</code>
	 * @param ifNull
	 *            replacement if <code>value</code> is <code>null</code>
	 * 
	 * @return <code>ifNull</code> if the provided <code>value</code> is
	 *         <code>null</code>; otherwise <code>value</code>
	 * 
	 * @throws IllegalArgumentException
	 *             if <code>ifNull</code> is <code>null</code>
	 */
	@NotNull
	public static <T> T nullValue(@Nullable T value, @NotNull T ifNull)
			throws IllegalArgumentException
	{
		if(ifNull == null)
		{
			throw new IllegalArgumentException("ifNull cannot be null");
		}
		
		return value != null ? value : ifNull;
	}
	
	
	/**
	 * Throws a {@link NullPointerException} if <code>object</code> is
	 * <code>null</code>.
	 * 
	 * @param object
	 * @return object
	 * @since 4.0
	 */
	public static <T> T notNull(final T object)
	{
		if(object == null)
		{
			throw new NullPointerException();
		}
		return object;
	}
}
