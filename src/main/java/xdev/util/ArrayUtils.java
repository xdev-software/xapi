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
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;

import xdev.lang.LibraryMember;
import xdev.lang.Nullable;


/**
 * <p>
 * The <code>ArrayUtils</code> class provides utility methods for array
 * handling.
 * </p>
 * 
 * @since 2.0
 * 
 * @author XDEV Software
 */
@LibraryMember
public final class ArrayUtils
{

	/**
	 * <p>
	 * <code>ArrayUtils</code> instances can not be instantiated. The class
	 * should be used as utility class:
	 * <code>ArrayUtils.contains(myArray, "bar");</code>.
	 * </p>
	 */
	private ArrayUtils()
	{
	}


	/**
	 * Checks if the two arrays are equal.<br>
	 * The arrays are sorted before comparison, if they are not
	 * <code>null</code>.
	 * 
	 * @throws NullPointerException
	 *             if an element of an array is <code>null</code>
	 * 
	 * @param array1
	 *            an array or <code>null</code>
	 * @param array2
	 *            an array or <code>null</code>
	 * @param <T>
	 *            type of the array to compare
	 * @return <code>true</code> if and only if the arrays have the same length
	 *         and the array's elements are equal, <code>false</code> otherwise
	 */
	public static <T extends Comparable<?>> boolean equals(T[] array1, T[] array2)
			throws NullPointerException
	{
		if(array1 == array2)
		{
			return true;
		}

		if(array1 == null || array2 == null)
		{
			return false;
		}

		if(array1.length != array2.length)
		{
			return false;
		}

		Arrays.sort(array1);
		Arrays.sort(array2);

		for(int i = 0; i < array1.length; i++)
		{
			if(!ObjectUtils.equals(array1[i],array2[i]))
			{
				return false;
			}
		}

		return true;
	}


	/**
	 * Applies to: <strong>Object array</strong>
	 * 
	 * <p>
	 * Returns <tt>true</tt> if the specified <code>array</code> contains the
	 * specified <code>value</code>. More formally, returns <tt>true</tt> if and
	 * only if the specified <code>array</code> contains at least one element
	 * <tt>value</tt>.
	 * 
	 * @param array
	 *            to search in
	 * 
	 * @param value
	 *            element whose presence in the specified <code>array</code> is
	 *            to be tested
	 * @return <tt>true</tt> if this <code>array</code> contains the specified
	 *         element
	 */
	public static boolean contains(Object[] array, Object value)
	{
		return indexOf(array,value) >= 0;
	}


	/**
	 * Applies to: <strong>Object array</strong>
	 * 
	 * <p>
	 * Returns the index of the first occurrence of the specified element
	 * <code>value</code> in the given array, or -1 if the given
	 * <code>array</code> does not contain the element <code>value</code>.
	 * </p>
	 * 
	 * 
	 * @param array
	 *            to search in
	 * 
	 * @param value
	 *            element to search for
	 * @return the index of the first occurrence of the specified element in the
	 *         given <code>array</code>, or -1 if the given <code>array</code>
	 *         does not contain the element
	 */
	public static int indexOf(Object[] array, @Nullable Object value)
	{
		if(value == null)
		{
			for(int i = 0; i < array.length; i++)
			{
				if(array[i] == null)
				{
					return i;
				}
			}
		}
		else
		{
			for(int i = 0; i < array.length; i++)
			{
				if(ObjectUtils.equals(array[i],value))
				{
					return i;
				}
			}
		}

		return -1;
	}


	/**
	 * Applies to: <strong>boolean array</strong>
	 * 
	 * <p>
	 * Returns <tt>true</tt> if the specified <code>array</code> contains the
	 * specified <code>value</code>. More formally, returns <tt>true</tt> if and
	 * only if the specified <code>array</code> contains at least one element
	 * <tt>value</tt>.
	 * 
	 * @param array
	 *            to search in
	 * 
	 * @param value
	 *            element whose presence in the specified <code>array</code> is
	 *            to be tested
	 * @return <tt>true</tt> if this <code>array</code> contains the specified
	 *         element
	 */
	public static boolean contains(boolean[] array, boolean value)
	{
		return indexOf(array,value) >= 0;
	}


	/**
	 * Applies to: <strong>boolean array</strong>
	 * 
	 * <p>
	 * Returns the index of the first occurrence of the specified element
	 * <code>value</code> in the given array, or -1 if the given
	 * <code>array</code> does not contain the element <code>value</code>.
	 * </p>
	 * 
	 * 
	 * @param array
	 *            to search in
	 * 
	 * @param value
	 *            element to search for
	 * @return the index of the first occurrence of the specified element in the
	 *         given <code>array</code>, or -1 if the given <code>array</code>
	 *         does not contain the element
	 */
	public static int indexOf(boolean[] array, boolean value)
	{
		for(int i = 0; i < array.length; i++)
		{
			if(array[i] == value)
			{
				return i;
			}
		}

		return -1;
	}


	/**
	 * Applies to: <strong>char array</strong>
	 * 
	 * <p>
	 * Returns <tt>true</tt> if the specified <code>array</code> contains the
	 * specified <code>value</code>. More formally, returns <tt>true</tt> if and
	 * only if the specified <code>array</code> contains at least one element
	 * <tt>value</tt>.
	 * 
	 * @param array
	 *            to search in
	 * 
	 * @param value
	 *            element whose presence in the specified <code>array</code> is
	 *            to be tested
	 * @return <tt>true</tt> if this <code>array</code> contains the specified
	 *         element
	 */
	public static boolean contains(char[] array, char value)
	{
		return indexOf(array,value) >= 0;
	}


	/**
	 * Applies to: <strong>char array</strong>
	 * 
	 * <p>
	 * Returns the index of the first occurrence of the specified element
	 * <code>value</code> in the given array, or -1 if the given
	 * <code>array</code> does not contain the element <code>value</code>.
	 * </p>
	 * 
	 * 
	 * @param array
	 *            to search in
	 * 
	 * @param value
	 *            element to search for
	 * @return the index of the first occurrence of the specified element in the
	 *         given <code>array</code>, or -1 if the given <code>array</code>
	 *         does not contain the element
	 */
	public static int indexOf(char[] array, char value)
	{
		for(int i = 0; i < array.length; i++)
		{
			if(array[i] == value)
			{
				return i;
			}
		}

		return -1;
	}


	/**
	 * Applies to: <strong>byte array</strong>
	 * 
	 * <p>
	 * Returns <tt>true</tt> if the specified <code>array</code> contains the
	 * specified <code>value</code>. More formally, returns <tt>true</tt> if and
	 * only if the specified <code>array</code> contains at least one element
	 * <tt>value</tt>.
	 * 
	 * @param array
	 *            to search in
	 * 
	 * @param value
	 *            element whose presence in the specified <code>array</code> is
	 *            to be tested
	 * @return <tt>true</tt> if this <code>array</code> contains the specified
	 *         element
	 */
	public static boolean contains(byte[] array, byte value)
	{
		return indexOf(array,value) >= 0;
	}


	/**
	 * Applies to: <strong>byte array</strong>
	 * 
	 * <p>
	 * Returns the index of the first occurrence of the specified element
	 * <code>value</code> in the given array, or -1 if the given
	 * <code>array</code> does not contain the element <code>value</code>.
	 * </p>
	 * 
	 * 
	 * @param array
	 *            to search in
	 * 
	 * @param value
	 *            element to search for
	 * @return the index of the first occurrence of the specified element in the
	 *         given <code>array</code>, or -1 if the given <code>array</code>
	 *         does not contain the element
	 */
	public static int indexOf(byte[] array, byte value)
	{
		for(int i = 0; i < array.length; i++)
		{
			if(array[i] == value)
			{
				return i;
			}
		}

		return -1;
	}


	/**
	 * Applies to: <strong>short array</strong>
	 * 
	 * <p>
	 * Returns <tt>true</tt> if the specified <code>array</code> contains the
	 * specified <code>value</code>. More formally, returns <tt>true</tt> if and
	 * only if the specified <code>array</code> contains at least one element
	 * <tt>value</tt>.
	 * 
	 * @param array
	 *            to search in
	 * 
	 * @param value
	 *            element whose presence in the specified <code>array</code> is
	 *            to be tested
	 * @return <tt>true</tt> if this <code>array</code> contains the specified
	 *         element
	 */
	public static boolean contains(short[] array, short value)
	{
		return indexOf(array,value) >= 0;
	}


	/**
	 * Applies to: <strong>short array</strong>
	 * 
	 * <p>
	 * Returns the index of the first occurrence of the specified element
	 * <code>value</code> in the given array, or -1 if the given
	 * <code>array</code> does not contain the element <code>value</code>.
	 * </p>
	 * 
	 * 
	 * @param array
	 *            to search in
	 * 
	 * @param value
	 *            element to search for
	 * @return the index of the first occurrence of the specified element in the
	 *         given <code>array</code>, or -1 if the given <code>array</code>
	 *         does not contain the element
	 */
	public static int indexOf(short[] array, short value)
	{
		for(int i = 0; i < array.length; i++)
		{
			if(array[i] == value)
			{
				return i;
			}
		}

		return -1;
	}


	/**
	 * Applies to: <strong>int array</strong>
	 * 
	 * <p>
	 * Returns <tt>true</tt> if the specified <code>array</code> contains the
	 * specified <code>value</code>. More formally, returns <tt>true</tt> if and
	 * only if the specified <code>array</code> contains at least one element
	 * <tt>value</tt>.
	 * 
	 * @param array
	 *            to search in
	 * 
	 * @param value
	 *            element whose presence in the specified <code>array</code> is
	 *            to be tested
	 * @return <tt>true</tt> if this <code>array</code> contains the specified
	 *         element
	 */
	public static boolean contains(int[] array, int value)
	{
		return indexOf(array,value) >= 0;
	}


	/**
	 * Applies to: <strong>int array</strong>
	 * 
	 * <p>
	 * Returns the index of the first occurrence of the specified element
	 * <code>value</code> in the given array, or -1 if the given
	 * <code>array</code> does not contain the element <code>value</code>.
	 * </p>
	 * 
	 * 
	 * @param array
	 *            to search in
	 * 
	 * @param value
	 *            element to search for
	 * @return the index of the first occurrence of the specified element in the
	 *         given <code>array</code>, or -1 if the given <code>array</code>
	 *         does not contain the element
	 */
	public static int indexOf(int[] array, int value)
	{
		for(int i = 0; i < array.length; i++)
		{
			if(array[i] == value)
			{
				return i;
			}
		}

		return -1;
	}


	/**
	 * Applies to: <strong>long array</strong>
	 * 
	 * <p>
	 * Returns <tt>true</tt> if the specified <code>array</code> contains the
	 * specified <code>value</code>. More formally, returns <tt>true</tt> if and
	 * only if the specified <code>array</code> contains at least one element
	 * <tt>value</tt>.
	 * 
	 * @param array
	 *            to search in
	 * 
	 * @param value
	 *            element whose presence in the specified <code>array</code> is
	 *            to be tested
	 * @return <tt>true</tt> if this <code>array</code> contains the specified
	 *         element
	 */
	public static boolean contains(long[] array, long value)
	{
		return indexOf(array,value) >= 0;
	}


	/**
	 * Applies to: <strong>long array</strong>
	 * 
	 * <p>
	 * Returns the index of the first occurrence of the specified element
	 * <code>value</code> in the given array, or -1 if the given
	 * <code>array</code> does not contain the element <code>value</code>.
	 * </p>
	 * 
	 * 
	 * @param array
	 *            to search in
	 * 
	 * @param value
	 *            element to search for
	 * @return the index of the first occurrence of the specified element in the
	 *         given <code>array</code>, or -1 if the given <code>array</code>
	 *         does not contain the element
	 */
	public static int indexOf(long[] array, long value)
	{
		for(int i = 0; i < array.length; i++)
		{
			if(array[i] == value)
			{
				return i;
			}
		}

		return -1;
	}


	/**
	 * Applies to: <strong>float array</strong>
	 * 
	 * <p>
	 * Returns <tt>true</tt> if the specified <code>array</code> contains the
	 * specified <code>value</code>. More formally, returns <tt>true</tt> if and
	 * only if the specified <code>array</code> contains at least one element
	 * <tt>value</tt>.
	 * 
	 * @param array
	 *            to search in
	 * 
	 * @param value
	 *            element whose presence in the specified <code>array</code> is
	 *            to be tested
	 * @return <tt>true</tt> if this <code>array</code> contains the specified
	 *         element
	 */
	public static boolean contains(float[] array, float value)
	{
		return indexOf(array,value) >= 0;
	}


	/**
	 * Applies to: <strong>float array</strong>
	 * 
	 * <p>
	 * Returns the index of the first occurrence of the specified element
	 * <code>value</code> in the given array, or -1 if the given
	 * <code>array</code> does not contain the element <code>value</code>.
	 * </p>
	 * 
	 * 
	 * @param array
	 *            to search in
	 * 
	 * @param value
	 *            element to search for
	 * @return the index of the first occurrence of the specified element in the
	 *         given <code>array</code>, or -1 if the given <code>array</code>
	 *         does not contain the element
	 */
	public static int indexOf(float[] array, float value)
	{
		for(int i = 0; i < array.length; i++)
		{
			if(array[i] == value)
			{
				return i;
			}
		}

		return -1;
	}


	/**
	 * Applies to: <strong>double array</strong>
	 * 
	 * <p>
	 * Returns <tt>true</tt> if the specified <code>array</code> contains the
	 * specified <code>value</code>. More formally, returns <tt>true</tt> if and
	 * only if the specified <code>array</code> contains at least one element
	 * <tt>value</tt>.
	 * 
	 * @param array
	 *            to search in
	 * 
	 * @param value
	 *            element whose presence in the specified <code>array</code> is
	 *            to be tested
	 * @return <tt>true</tt> if this <code>array</code> contains the specified
	 *         element
	 */
	public static boolean contains(double[] array, double value)
	{
		return indexOf(array,value) >= 0;
	}


	/**
	 * Applies to: <strong>double array</strong>
	 * 
	 * <p>
	 * Returns the index of the first occurrence of the specified element
	 * <code>value</code> in the given array, or -1 if the given
	 * <code>array</code> does not contain the element <code>value</code>.
	 * </p>
	 * 
	 * 
	 * @param array
	 *            to search in
	 * 
	 * @param value
	 *            element to search for
	 * @return the index of the first occurrence of the specified element in the
	 *         given <code>array</code>, or -1 if the given <code>array</code>
	 *         does not contain the element
	 */
	public static int indexOf(double[] array, double value)
	{
		for(int i = 0; i < array.length; i++)
		{
			if(array[i] == value)
			{
				return i;
			}
		}

		return -1;
	}


	/**
	 * Applies to: <strong>T array</strong>
	 * 
	 * <p>
	 * Returns <tt>true</tt> if the specified <code>array</code> contains the
	 * specified <code>value</code>. More formally, returns <tt>true</tt> if and
	 * only if the specified <code>array</code> contains at least one element
	 * <tt>value</tt>.
	 * 
	 * @param <T>
	 *            type of the array to search in
	 * @param array
	 *            to search in
	 * 
	 * @param value
	 *            element whose presence in the specified <code>array</code> is
	 *            to be tested
	 * 
	 * @param c
	 *            {@link Comparer} to use
	 * @return <tt>true</tt> if this <code>array</code> contains the specified
	 *         element
	 * 
	 * @see Comparer
	 */
	public static <T> boolean contains(T[] array, T value, Comparer<T> c)
	{
		return indexOf(array,value,c) >= 0;
	}


	/**
	 * Applies to: <strong>T array</strong>
	 * 
	 * <p>
	 * Returns the index of the first occurrence of the specified element
	 * <code>value</code> in the given array, or -1 if the given
	 * <code>array</code> does not contain the element <code>value</code>.
	 * </p>
	 * 
	 * @param <T>
	 *            type of the array to search in
	 * 
	 * @param array
	 *            to search in
	 * 
	 * @param value
	 *            element to search for
	 * 
	 * @param c
	 *            {@link Comparer} to use
	 * @return the index of the first occurrence of the specified element in the
	 *         given <code>array</code>, or -1 if the given <code>array</code>
	 *         does not contain the element
	 * @see Comparer
	 */
	public static <T> int indexOf(T[] array, T value, Comparer<T> c)
	{
		if(array == null)
		{
			return -1;
		}

		for(int i = 0; i < array.length; i++)
		{
			if(c.equals(array[i],value))
			{
				return i;
			}
		}

		return -1;
	}


	/**
	 * Applies to: <strong>T array</strong>
	 * 
	 * <p>
	 * Creates a (defensive) copy of the given <code>array</code>.
	 * </p>
	 * 
	 * @param <T>
	 *            type of the array to copy
	 * 
	 * @param data
	 *            <code>array</code> to copy
	 * 
	 * @return a (defensive) copy of the given <code>array</code>.
	 */
	public static <T> T[] copyOf(T[] data)
	{
		return Arrays.copyOf(data,data.length);
	}


	/**
	 * Applies to: <strong>boolean array</strong>
	 * 
	 * <p>
	 * Creates a (defensive) copy of the given <code>array</code>.
	 * </p>
	 * 
	 * 
	 * @param data
	 *            <code>array</code> to copy
	 * 
	 * @return a (defensive) copy of the given <code>array</code>.
	 */
	public static boolean[] copyOf(boolean[] data)
	{
		return Arrays.copyOf(data,data.length);
	}


	/**
	 * Applies to: <strong>char array</strong>
	 * 
	 * <p>
	 * Creates a (defensive) copy of the given <code>array</code>.
	 * </p>
	 * 
	 * 
	 * @param data
	 *            <code>array</code> to copy
	 * 
	 * @return a (defensive) copy of the given <code>array</code>.
	 */
	public static char[] copyOf(char[] data)
	{
		return Arrays.copyOf(data,data.length);
	}


	/**
	 * Applies to: <strong>byte array</strong>
	 * 
	 * <p>
	 * Creates a (defensive) copy of the given <code>array</code>.
	 * </p>
	 * 
	 * 
	 * @param data
	 *            <code>array</code> to copy
	 * 
	 * @return a (defensive) copy of the given <code>array</code>.
	 */
	public static byte[] copy(byte[] data)
	{
		return Arrays.copyOf(data,data.length);
	}


	/**
	 * Applies to: <strong>short array</strong>
	 * 
	 * <p>
	 * Creates a (defensive) copy of the given <code>array</code>.
	 * </p>
	 * 
	 * 
	 * @param data
	 *            <code>array</code> to copy
	 * 
	 * @return a (defensive) copy of the given <code>array</code>.
	 */
	public static short[] copyOf(short[] data)
	{
		return Arrays.copyOf(data,data.length);
	}


	/**
	 * Applies to: <strong>int array</strong>
	 * 
	 * <p>
	 * Creates a (defensive) copy of the given <code>array</code>.
	 * </p>
	 * 
	 * 
	 * @param data
	 *            <code>array</code> to copy
	 * 
	 * @return a (defensive) copy of the given <code>array</code>.
	 */
	public static int[] copyOf(int[] data)
	{
		return Arrays.copyOf(data,data.length);
	}


	/**
	 * Applies to: <strong>long array</strong>
	 * 
	 * <p>
	 * Creates a (defensive) copy of the given <code>array</code>.
	 * </p>
	 * 
	 * 
	 * @param data
	 *            <code>array</code> to copy
	 * 
	 * @return a (defensive) copy of the given <code>array</code>.
	 */
	public static long[] copyOf(long[] data)
	{
		return Arrays.copyOf(data,data.length);
	}


	/**
	 * Applies to: <strong>float array</strong>
	 * 
	 * <p>
	 * Creates a (defensive) copy of the given <code>array</code>.
	 * </p>
	 * 
	 * 
	 * @param data
	 *            <code>array</code> to copy
	 * 
	 * @return a (defensive) copy of the given <code>array</code>.
	 */
	public static float[] copyOf(float[] data)
	{
		return Arrays.copyOf(data,data.length);
	}


	/**
	 * Applies to: <strong>double array</strong>
	 * 
	 * <p>
	 * Creates a (defensive) copy of the given <code>array</code>.
	 * </p>
	 * 
	 * 
	 * @param data
	 *            <code>array</code> to copy
	 * 
	 * @return a (defensive) copy of the given <code>array</code>.
	 */
	public static double[] copyOf(double[] data)
	{
		return Arrays.copyOf(data,data.length);
	}


	/**
	 * Applies to: <strong>T array and T element</strong>
	 * 
	 * <p>
	 * Concatenates the given <code>array</code> with the given element. A new
	 * array is returned that contains all elements of the given
	 * <code>array</code> and as last element the given element
	 * <code>elem</code>. The length of the returned array will be
	 * <code>givenArray.length + 1</code>.
	 * </p>
	 * 
	 * @param <T>
	 *            type of the array's elements
	 * @param type
	 *            class of the array's elements
	 * @param array
	 *            array to concatenate
	 * @param elem
	 *            element to concatenate
	 * @return new array that contains all elements of the given
	 *         <code>array</code> and as last element the given element
	 *         <code>elem</code>.
	 */
	public static <T> T[] concat(Class<T> type, T[] array, T elem)
	{
		@SuppressWarnings("unchecked")
		// OK, because the new instance will always be T (or a subtype of T)
		T[] result = (T[])Array.newInstance(type,array.length + 1);
		System.arraycopy(array,0,result,0,array.length);
		result[result.length - 1] = elem;
		return result;
	}


	/**
	 * Applies to: <strong>boolean array and boolean element</strong>
	 * 
	 * <p>
	 * Concatenates the given <code>array</code> with the given element. A new
	 * array is returned that contains all elements of the given
	 * <code>array</code> and as last element the given element
	 * <code>elem</code>. The length of the returned array will be
	 * <code>givenArray.length + 1</code>.
	 * </p>
	 * 
	 * @param array
	 *            array to concatenate
	 * @param elem
	 *            element to concatenate
	 * @return new array that contains all elements of the given
	 *         <code>array</code> and as last element the given element
	 *         <code>elem</code>.
	 */
	public static boolean[] concat(boolean[] array, boolean elem)
	{
		boolean[] result = new boolean[array.length + 1];
		System.arraycopy(array,0,result,0,array.length);
		result[result.length - 1] = elem;
		return result;
	}


	/**
	 * Applies to: <strong>char array and char element</strong>
	 * 
	 * <p>
	 * Concatenates the given <code>array</code> with the given element. A new
	 * array is returned that contains all elements of the given
	 * <code>array</code> and as last element the given element
	 * <code>elem</code>. The length of the returned array will be
	 * <code>givenArray.length + 1</code>.
	 * </p>
	 * 
	 * @param array
	 *            array to concatenate
	 * @param elem
	 *            element to concatenate
	 * @return new array that contains all elements of the given
	 *         <code>array</code> and as last element the given element
	 *         <code>elem</code>.
	 */
	public static char[] concat(char[] array, char elem)
	{
		char[] result = new char[array.length + 1];
		System.arraycopy(array,0,result,0,array.length);
		result[result.length - 1] = elem;
		return result;
	}


	/**
	 * Applies to: <strong>byte array and byte element</strong>
	 * 
	 * <p>
	 * Concatenates the given <code>array</code> with the given element. A new
	 * array is returned that contains all elements of the given
	 * <code>array</code> and as last element the given element
	 * <code>elem</code>. The length of the returned array will be
	 * <code>givenArray.length + 1</code>.
	 * </p>
	 * 
	 * @param array
	 *            array to concatenate
	 * @param elem
	 *            element to concatenate
	 * @return new array that contains all elements of the given
	 *         <code>array</code> and as last element the given element
	 *         <code>elem</code>.
	 */
	public static byte[] concat(byte[] array, byte elem)
	{
		byte[] result = new byte[array.length + 1];
		System.arraycopy(array,0,result,0,array.length);
		result[result.length - 1] = elem;
		return result;
	}


	/**
	 * Applies to: <strong>short array and short element</strong>
	 * 
	 * <p>
	 * Concatenates the given <code>array</code> with the given element. A new
	 * array is returned that contains all elements of the given
	 * <code>array</code> and as last element the given element
	 * <code>elem</code>. The length of the returned array will be
	 * <code>givenArray.length + 1</code>.
	 * </p>
	 * 
	 * @param array
	 *            array to concatenate
	 * @param elem
	 *            element to concatenate
	 * @return new array that contains all elements of the given
	 *         <code>array</code> and as last element the given element
	 *         <code>elem</code>.
	 */
	public static short[] concat(short[] array, short elem)
	{
		short[] result = new short[array.length + 1];
		System.arraycopy(array,0,result,0,array.length);
		result[result.length - 1] = elem;
		return result;
	}


	/**
	 * Applies to: <strong>int array and int element</strong>
	 * 
	 * <p>
	 * Concatenates the given <code>array</code> with the given element. A new
	 * array is returned that contains all elements of the given
	 * <code>array</code> and as last element the given element
	 * <code>elem</code>. The length of the returned array will be
	 * <code>givenArray.length + 1</code>.
	 * </p>
	 * 
	 * @param array
	 *            array to concatenate
	 * @param elem
	 *            element to concatenate
	 * @return new array that contains all elements of the given
	 *         <code>array</code> and as last element the given element
	 *         <code>elem</code>.
	 */
	public static int[] concat(int[] array, int elem)
	{
		int[] result = new int[array.length + 1];
		System.arraycopy(array,0,result,0,array.length);
		result[result.length - 1] = elem;
		return result;
	}


	/**
	 * Applies to: <strong>long array and long element</strong>
	 * 
	 * <p>
	 * Concatenates the given <code>array</code> with the given element. A new
	 * array is returned that contains all elements of the given
	 * <code>array</code> and as last element the given element
	 * <code>elem</code>. The length of the returned array will be
	 * <code>givenArray.length + 1</code>.
	 * </p>
	 * 
	 * @param array
	 *            array to concatenate
	 * @param elem
	 *            element to concatenate
	 * @return new array that contains all elements of the given
	 *         <code>array</code> and as last element the given element
	 *         <code>elem</code>.
	 */
	public static long[] concat(long[] array, long elem)
	{
		long[] result = new long[array.length + 1];
		System.arraycopy(array,0,result,0,array.length);
		result[result.length - 1] = elem;
		return result;
	}


	/**
	 * Applies to: <strong>float array and float element</strong>
	 * 
	 * <p>
	 * Concatenates the given <code>array</code> with the given element. A new
	 * array is returned that contains all elements of the given
	 * <code>array</code> and as last element the given element
	 * <code>elem</code>. The length of the returned array will be
	 * <code>givenArray.length + 1</code>.
	 * </p>
	 * 
	 * @param array
	 *            array to concatenate
	 * @param elem
	 *            element to concatenate
	 * @return new array that contains all elements of the given
	 *         <code>array</code> and as last element the given element
	 *         <code>elem</code>.
	 */
	public static float[] concat(float[] array, float elem)
	{
		float[] result = new float[array.length + 1];
		System.arraycopy(array,0,result,0,array.length);
		result[result.length - 1] = elem;
		return result;
	}


	/**
	 * Applies to: <strong>double array and double element</strong>
	 * 
	 * <p>
	 * Concatenates the given <code>array</code> with the given element. A new
	 * array is returned that contains all elements of the given
	 * <code>array</code> and as last element the given element
	 * <code>elem</code>. The length of the returned array will be
	 * <code>givenArray.length + 1</code>.
	 * </p>
	 * 
	 * @param array
	 *            array to concatenate
	 * @param elem
	 *            element to concatenate
	 * @return new array that contains all elements of the given
	 *         <code>array</code> and as last element the given element
	 *         <code>elem</code>.
	 */
	public static double[] concat(double[] array, double elem)
	{
		double[] result = new double[array.length + 1];
		System.arraycopy(array,0,result,0,array.length);
		result[result.length - 1] = elem;
		return result;
	}


	/**
	 * Applies to: <strong>T arrays</strong>
	 * 
	 * <p>
	 * Concatenates the given <code>arrays</code>. A new array is returned that
	 * contains all elements of the given <code>arrays</code> in the order the
	 * <code>arrays</code> were passed to the method.
	 * </p>
	 * 
	 * 
	 * @param <T>
	 *            type of the array elements
	 * @param type
	 *            class of the array elements
	 * 
	 * @param arrays
	 *            arrays to concatenate
	 * 
	 * @return new array is returned that contains all elements of the given
	 *         <code>arrays</code> in the order the <code>arrays</code> were
	 *         passed to the method.
	 */
	public static <T> T[] concat(Class<T> type, T[]... arrays)
	{
		int len = 0;
		for(int i = 0; i < arrays.length; i++)
		{
			len += arrays[i].length;
		}
		@SuppressWarnings("unchecked")
		// OK, because the new instance will always be T (or a subtype of T)
		T[] result = (T[])Array.newInstance(type,len);
		for(int i = 0, offset = 0; i < arrays.length; i++)
		{
			System.arraycopy(arrays[i],0,result,offset,arrays[i].length);
			offset += arrays[i].length;
		}
		return result;
	}


	/**
	 * Applies to: <strong>boolean arrays</strong>
	 * 
	 * <p>
	 * Concatenates the given <code>arrays</code>. A new array is returned that
	 * contains all elements of the given <code>arrays</code> in the order the
	 * <code>arrays</code> were passed to the method.
	 * </p>
	 * 
	 * @param arrays
	 *            arrays to concatenate
	 * 
	 * @return new array is returned that contains all elements of the given
	 *         <code>arrays</code> in the order the <code>arrays</code> were
	 *         passed to the method.
	 */
	public static boolean[] concat(boolean[]... arrays)
	{
		int len = 0;
		for(int i = 0; i < arrays.length; i++)
		{
			len += arrays[i].length;
		}
		boolean[] result = new boolean[len];
		for(int i = 0, offset = 0; i < arrays.length; i++)
		{
			System.arraycopy(arrays[i],0,result,offset,arrays[i].length);
			offset += arrays[i].length;
		}
		return result;
	}


	/**
	 * Applies to: <strong>char arrays</strong>
	 * 
	 * <p>
	 * Concatenates the given <code>arrays</code>. A new array is returned that
	 * contains all elements of the given <code>arrays</code> in the order the
	 * <code>arrays</code> were passed to the method.
	 * </p>
	 * 
	 * @param arrays
	 *            arrays to concatenate
	 * 
	 * @return new array is returned that contains all elements of the given
	 *         <code>arrays</code> in the order the <code>arrays</code> were
	 *         passed to the method.
	 */
	public static char[] concat(char[]... arrays)
	{
		int len = 0;
		for(int i = 0; i < arrays.length; i++)
		{
			len += arrays[i].length;
		}
		char[] result = new char[len];
		for(int i = 0, offset = 0; i < arrays.length; i++)
		{
			System.arraycopy(arrays[i],0,result,offset,arrays[i].length);
			offset += arrays[i].length;
		}
		return result;
	}


	/**
	 * Applies to: <strong>byte arrays</strong>
	 * 
	 * <p>
	 * Concatenates the given <code>arrays</code>. A new array is returned that
	 * contains all elements of the given <code>arrays</code> in the order the
	 * <code>arrays</code> were passed to the method.
	 * </p>
	 * 
	 * @param arrays
	 *            arrays to concatenate
	 * 
	 * @return new array is returned that contains all elements of the given
	 *         <code>arrays</code> in the order the <code>arrays</code> were
	 *         passed to the method.
	 */
	public static byte[] concat(byte[]... arrays)
	{
		int len = 0;
		for(int i = 0; i < arrays.length; i++)
		{
			len += arrays[i].length;
		}
		byte[] result = new byte[len];
		for(int i = 0, offset = 0; i < arrays.length; i++)
		{
			System.arraycopy(arrays[i],0,result,offset,arrays[i].length);
			offset += arrays[i].length;
		}
		return result;
	}


	/**
	 * Applies to: <strong>short arrays</strong>
	 * 
	 * <p>
	 * Concatenates the given <code>arrays</code>. A new array is returned that
	 * contains all elements of the given <code>arrays</code> in the order the
	 * <code>arrays</code> were passed to the method.
	 * </p>
	 * 
	 * @param arrays
	 *            arrays to concatenate
	 * 
	 * @return new array is returned that contains all elements of the given
	 *         <code>arrays</code> in the order the <code>arrays</code> were
	 *         passed to the method.
	 */
	public static short[] concat(short[]... arrays)
	{
		int len = 0;
		for(int i = 0; i < arrays.length; i++)
		{
			len += arrays[i].length;
		}
		short[] result = new short[len];
		for(int i = 0, offset = 0; i < arrays.length; i++)
		{
			System.arraycopy(arrays[i],0,result,offset,arrays[i].length);
			offset += arrays[i].length;
		}
		return result;
	}


	/**
	 * Applies to: <strong>int arrays</strong>
	 * 
	 * <p>
	 * Concatenates the given <code>arrays</code>. A new array is returned that
	 * contains all elements of the given <code>arrays</code> in the order the
	 * <code>arrays</code> were passed to the method.
	 * </p>
	 * 
	 * @param arrays
	 *            arrays to concatenate
	 * 
	 * @return new array is returned that contains all elements of the given
	 *         <code>arrays</code> in the order the <code>arrays</code> were
	 *         passed to the method.
	 */
	public static int[] concat(int[]... arrays)
	{
		int len = 0;
		for(int i = 0; i < arrays.length; i++)
		{
			len += arrays[i].length;
		}
		int[] result = new int[len];
		for(int i = 0, offset = 0; i < arrays.length; i++)
		{
			System.arraycopy(arrays[i],0,result,offset,arrays[i].length);
			offset += arrays[i].length;
		}
		return result;
	}


	/**
	 * Applies to: <strong>long arrays</strong>
	 * 
	 * <p>
	 * Concatenates the given <code>arrays</code>. A new array is returned that
	 * contains all elements of the given <code>arrays</code> in the order the
	 * <code>arrays</code> were passed to the method.
	 * </p>
	 * 
	 * @param arrays
	 *            arrays to concatenate
	 * 
	 * @return new array is returned that contains all elements of the given
	 *         <code>arrays</code> in the order the <code>arrays</code> were
	 *         passed to the method.
	 */
	public static long[] concat(long[]... arrays)
	{
		int len = 0;
		for(int i = 0; i < arrays.length; i++)
		{
			len += arrays[i].length;
		}
		long[] result = new long[len];
		for(int i = 0, offset = 0; i < arrays.length; i++)
		{
			System.arraycopy(arrays[i],0,result,offset,arrays[i].length);
			offset += arrays[i].length;
		}
		return result;
	}


	/**
	 * Applies to: <strong>float arrays</strong>
	 * 
	 * <p>
	 * Concatenates the given <code>arrays</code>. A new array is returned that
	 * contains all elements of the given <code>arrays</code> in the order the
	 * <code>arrays</code> were passed to the method.
	 * </p>
	 * 
	 * @param arrays
	 *            arrays to concatenate
	 * 
	 * @return new array is returned that contains all elements of the given
	 *         <code>arrays</code> in the order the <code>arrays</code> were
	 *         passed to the method.
	 */
	public static float[] concat(float[]... arrays)
	{
		int len = 0;
		for(int i = 0; i < arrays.length; i++)
		{
			len += arrays[i].length;
		}
		float[] result = new float[len];
		for(int i = 0, offset = 0; i < arrays.length; i++)
		{
			System.arraycopy(arrays[i],0,result,offset,arrays[i].length);
			offset += arrays[i].length;
		}
		return result;
	}


	/**
	 * Applies to: <strong>double arrays</strong>
	 * 
	 * <p>
	 * Concatenates the given <code>arrays</code>. A new array is returned that
	 * contains all elements of the given <code>arrays</code> in the order the
	 * <code>arrays</code> were passed to the method.
	 * </p>
	 * 
	 * @param arrays
	 *            arrays to concatenate
	 * 
	 * @return new array is returned that contains all elements of the given
	 *         <code>arrays</code> in the order the <code>arrays</code> were
	 *         passed to the method.
	 */
	public static double[] concat(double[]... arrays)
	{
		int len = 0;
		for(int i = 0; i < arrays.length; i++)
		{
			len += arrays[i].length;
		}
		double[] result = new double[len];
		for(int i = 0, offset = 0; i < arrays.length; i++)
		{
			System.arraycopy(arrays[i],0,result,offset,arrays[i].length);
			offset += arrays[i].length;
		}
		return result;
	}


	/**
	 * Applies to: <strong>T arrays</strong>
	 * 
	 * <p>
	 * Removes the element at the specified position from the given
	 * <code>array</code>. Shifts any subsequent elements to the left (subtracts
	 * one from their indices). Returns the element that was removed from the
	 * given <code>array</code>.
	 * </p>
	 * 
	 * @param <T>
	 *            type of the array elements
	 * @param type
	 *            class of the array elements
	 * 
	 * @param array
	 *            to remove element from
	 * @param index
	 *            the index of the element to be removed
	 * @return the element previously at the specified position
	 */
	public static <T> T[] remove(Class<T> type, T[] array, int index)
	{
		@SuppressWarnings("unchecked")
		// OK, because the new instance will always be T (or a subtype of T)
		T[] result = (T[])Array.newInstance(type,array.length - 1);
		if(index == 0)
		{
			System.arraycopy(array,1,result,0,array.length - 1);
		}
		else if(index == array.length - 1)
		{
			System.arraycopy(array,0,result,0,array.length - 1);
		}
		else
		{
			System.arraycopy(array,0,result,0,index);
			System.arraycopy(array,index + 1,result,index,array.length - index - 1);
		}
		return result;
	}


	/**
	 * Applies to: <strong>boolean arrays</strong>
	 * 
	 * <p>
	 * Removes the element at the specified position from the given
	 * <code>array</code>. Shifts any subsequent elements to the left (subtracts
	 * one from their indices). Returns the element that was removed from the
	 * given <code>array</code>.
	 * </p>
	 * 
	 * 
	 * @param array
	 *            to remove element from
	 * @param index
	 *            the index of the element to be removed
	 * @return the element previously at the specified position
	 */
	public static boolean[] remove(boolean[] array, int index)
	{
		boolean[] result = new boolean[array.length - 1];
		if(index == 0)
		{
			System.arraycopy(array,1,result,0,array.length - 1);
		}
		else if(index == array.length - 1)
		{
			System.arraycopy(array,0,result,0,array.length - 1);
		}
		else
		{
			System.arraycopy(array,0,result,0,index);
			System.arraycopy(array,index + 1,result,index,array.length - index - 1);
		}
		return result;
	}


	/**
	 * Applies to: <strong>char arrays</strong>
	 * 
	 * <p>
	 * Removes the element at the specified position from the given
	 * <code>array</code>. Shifts any subsequent elements to the left (subtracts
	 * one from their indices). Returns the element that was removed from the
	 * given <code>array</code>.
	 * </p>
	 * 
	 * 
	 * @param array
	 *            to remove element from
	 * @param index
	 *            the index of the element to be removed
	 * @return the element previously at the specified position
	 */
	public static char[] remove(char[] array, int index)
	{
		char[] result = new char[array.length - 1];
		if(index == 0)
		{
			System.arraycopy(array,1,result,0,array.length - 1);
		}
		else if(index == array.length - 1)
		{
			System.arraycopy(array,0,result,0,array.length - 1);
		}
		else
		{
			System.arraycopy(array,0,result,0,index);
			System.arraycopy(array,index + 1,result,index,array.length - index - 1);
		}
		return result;
	}


	/**
	 * Applies to: <strong>byte arrays</strong>
	 * 
	 * <p>
	 * Removes the element at the specified position from the given
	 * <code>array</code>. Shifts any subsequent elements to the left (subtracts
	 * one from their indices). Returns the element that was removed from the
	 * given <code>array</code>.
	 * </p>
	 * 
	 * 
	 * @param array
	 *            to remove element from
	 * @param index
	 *            the index of the element to be removed
	 * @return the element previously at the specified position
	 */
	public static byte[] remove(byte[] array, int index)
	{
		byte[] result = new byte[array.length - 1];
		if(index == 0)
		{
			System.arraycopy(array,1,result,0,array.length - 1);
		}
		else if(index == array.length - 1)
		{
			System.arraycopy(array,0,result,0,array.length - 1);
		}
		else
		{
			System.arraycopy(array,0,result,0,index);
			System.arraycopy(array,index + 1,result,index,array.length - index - 1);
		}
		return result;
	}


	/**
	 * Applies to: <strong>short arrays</strong>
	 * 
	 * <p>
	 * Removes the element at the specified position from the given
	 * <code>array</code>. Shifts any subsequent elements to the left (subtracts
	 * one from their indices). Returns the element that was removed from the
	 * given <code>array</code>.
	 * </p>
	 * 
	 * 
	 * @param array
	 *            to remove element from
	 * @param index
	 *            the index of the element to be removed
	 * @return the element previously at the specified position
	 */
	public static short[] remove(short[] array, int index)
	{
		short[] result = new short[array.length - 1];
		if(index == 0)
		{
			System.arraycopy(array,1,result,0,array.length - 1);
		}
		else if(index == array.length - 1)
		{
			System.arraycopy(array,0,result,0,array.length - 1);
		}
		else
		{
			System.arraycopy(array,0,result,0,index);
			System.arraycopy(array,index + 1,result,index,array.length - index - 1);
		}
		return result;
	}


	/**
	 * Applies to: <strong>int arrays</strong>
	 * 
	 * <p>
	 * Removes the element at the specified position from the given
	 * <code>array</code>. Shifts any subsequent elements to the left (subtracts
	 * one from their indices). Returns the element that was removed from the
	 * given <code>array</code>.
	 * </p>
	 * 
	 * 
	 * @param array
	 *            to remove element from
	 * @param index
	 *            the index of the element to be removed
	 * @return the element previously at the specified position
	 */
	public static int[] remove(int[] array, int index)
	{
		int[] result = new int[array.length - 1];
		if(index == 0)
		{
			System.arraycopy(array,1,result,0,array.length - 1);
		}
		else if(index == array.length - 1)
		{
			System.arraycopy(array,0,result,0,array.length - 1);
		}
		else
		{
			System.arraycopy(array,0,result,0,index);
			System.arraycopy(array,index + 1,result,index,array.length - index - 1);
		}
		return result;
	}


	/**
	 * Applies to: <strong>long arrays</strong>
	 * 
	 * <p>
	 * Removes the element at the specified position from the given
	 * <code>array</code>. Shifts any subsequent elements to the left (subtracts
	 * one from their indices). Returns the element that was removed from the
	 * given <code>array</code>.
	 * </p>
	 * 
	 * 
	 * @param array
	 *            to remove element from
	 * @param index
	 *            the index of the element to be removed
	 * @return the element previously at the specified position
	 */
	public static long[] remove(long[] array, int index)
	{
		long[] result = new long[array.length - 1];
		if(index == 0)
		{
			System.arraycopy(array,1,result,0,array.length - 1);
		}
		else if(index == array.length - 1)
		{
			System.arraycopy(array,0,result,0,array.length - 1);
		}
		else
		{
			System.arraycopy(array,0,result,0,index);
			System.arraycopy(array,index + 1,result,index,array.length - index - 1);
		}
		return result;
	}


	/**
	 * Applies to: <strong>float arrays</strong>
	 * 
	 * <p>
	 * Removes the element at the specified position from the given
	 * <code>array</code>. Shifts any subsequent elements to the left (subtracts
	 * one from their indices). Returns the element that was removed from the
	 * given <code>array</code>.
	 * </p>
	 * 
	 * 
	 * @param array
	 *            to remove element from
	 * @param index
	 *            the index of the element to be removed
	 * @return the element previously at the specified position
	 */
	public static float[] remove(float[] array, int index)
	{
		float[] result = new float[array.length - 1];
		if(index == 0)
		{
			System.arraycopy(array,1,result,0,array.length - 1);
		}
		else if(index == array.length - 1)
		{
			System.arraycopy(array,0,result,0,array.length - 1);
		}
		else
		{
			System.arraycopy(array,0,result,0,index);
			System.arraycopy(array,index + 1,result,index,array.length - index - 1);
		}
		return result;
	}


	/**
	 * Applies to: <strong>double arrays</strong>
	 * 
	 * <p>
	 * Removes the element at the specified position from the given
	 * <code>array</code>. Shifts any subsequent elements to the left (subtracts
	 * one from their indices). Returns the element that was removed from the
	 * given <code>array</code>.
	 * </p>
	 * 
	 * 
	 * @param array
	 *            to remove element from
	 * @param index
	 *            the index of the element to be removed
	 * @return the element previously at the specified position
	 */
	public static double[] remove(double[] array, int index)
	{
		double[] result = new double[array.length - 1];
		if(index == 0)
		{
			System.arraycopy(array,1,result,0,array.length - 1);
		}
		else if(index == array.length - 1)
		{
			System.arraycopy(array,0,result,0,array.length - 1);
		}
		else
		{
			System.arraycopy(array,0,result,0,index);
			System.arraycopy(array,index + 1,result,index,array.length - index - 1);
		}
		return result;
	}


	/**
	 * Returns a {@link Iterable} for the given <code>array</code>.
	 * 
	 * @param <T>
	 *            type of the array elements
	 * 
	 * 
	 * @param array
	 *            to get the {@link Iterable} for
	 * @return a {@link Iterable} for the given <code>array</code>.
	 */
	public static <T> Iterable<T> getIterable(final T[] array)
	{
		return getIterable(array,0,array.length);
	}


	/**
	 * Returns a {@link Iterable} for the given <code>array</code>.
	 * 
	 * @param <T>
	 *            type of the array elements
	 * @param offset
	 *            the start offset of the iterator
	 * @param length
	 *            the length of the iterator
	 * 
	 * @param array
	 *            to get the {@link Iterable} for
	 * @return a {@link Iterable} for the given <code>array</code>.
	 */
	public static <T> Iterable<T> getIterable(final T[] array, final int offset, final int length)
	{
		return new Iterable<T>()
		{
			public Iterator<T> iterator()
			{
				return getIterator(array,offset,length);
			}
		};
	}


	/**
	 * Returns a {@link Iterator} for the given <code>array</code>.
	 * <p>
	 * {@link Iterator#remove()} is not supported.
	 * 
	 * @param <T>
	 *            type of the array elements
	 * 
	 * 
	 * @param array
	 *            to get the {@link Iterator} for
	 * @return a {@link Iterator} for the given <code>array</code>.
	 */
	public static <T> Iterator<T> getIterator(final T[] array)
	{
		return getIterator(array,0,array.length);
	}


	/**
	 * Returns a {@link Iterator} for the given <code>array</code>.
	 * <p>
	 * {@link Iterator#remove()} is not supported.
	 * 
	 * @param <T>
	 *            type of the array elements
	 * @param array
	 *            to get the {@link Iterator} for
	 * @param offset
	 *            the start offset of the iterator
	 * @param length
	 *            the length of the iterator
	 * @return a {@link Iterator} for the given <code>array</code>.
	 */
	public static <T> Iterator<T> getIterator(final T[] array, final int offset, final int length)
	{
		return new Iterator<T>()
		{
			int	index		= offset;
			int	maxIndex	= offset + length - 1;


			@Override
			public boolean hasNext()
			{
				return index <= maxIndex;
			}


			@Override
			public T next()
			{
				if(index > maxIndex)
				{
					throw new NoSuchElementException();
				}

				return array[index++];
			}


			/**
			 * Always throws an {@link UnsupportedOperationException}
			 */
			@Override
			public void remove()
			{
				throw new UnsupportedOperationException();
			}
		};
	}


	/**
	 * 
	 * Sorts the given <code>array</code> of {@link Enum}s by name.
	 * 
	 * @param <E>
	 *            type of the array elements
	 * @param array
	 *            to sort
	 * @see Enum
	 * @see Enum#name()
	 * @see String#compareTo(String)
	 */
	public static <E extends Enum<E>> void sortByName(E[] array)
	{
		Arrays.sort(array,new Comparator<E>()
		{
			public int compare(E e1, E e2)
			{
				return e1.name().compareTo(e2.name());
			}
		});
	}


	/**
	 * Converts a array of wrapped {@link Byte}s to a primitive byte array.
	 * 
	 * @param wrapper
	 *            the wrapped array of {@link Byte}s
	 * @return a unwrapped array of bytes
	 * @throws NullPointerException
	 *             if wrapper itself or one element of wrapper is
	 *             <code>null</code>
	 */
	public static byte[] unwrap(Byte[] wrapper) throws NullPointerException
	{
		int c = wrapper.length;
		byte[] primitive = new byte[c];
		for(int i = 0; i < c; i++)
		{
			primitive[i] = wrapper[i];
		}
		return primitive;
	}


	/**
	 * Converts a array of wrapped {@link Short}s to a primitive short array.
	 * 
	 * @param wrapper
	 *            the wrapped array of {@link Short}s
	 * @return a unwrapped array of shorts
	 * @throws NullPointerException
	 *             if wrapper itself or one element of wrapper is
	 *             <code>null</code>
	 */
	public static short[] unwrap(Short[] wrapper) throws NullPointerException
	{
		int c = wrapper.length;
		short[] primitive = new short[c];
		for(int i = 0; i < c; i++)
		{
			primitive[i] = wrapper[i];
		}
		return primitive;
	}


	/**
	 * Converts a array of wrapped {@link Integer}s to a primitive int array.
	 * 
	 * @param wrapper
	 *            the wrapped array of {@link Integer}s
	 * @return a unwrapped array of ints
	 * @throws NullPointerException
	 *             if wrapper itself or one element of wrapper is
	 *             <code>null</code>
	 */
	public static int[] unwrap(Integer[] wrapper) throws NullPointerException
	{
		int c = wrapper.length;
		int[] primitive = new int[c];
		for(int i = 0; i < c; i++)
		{
			primitive[i] = wrapper[i];
		}
		return primitive;
	}


	/**
	 * Converts a array of wrapped {@link Long}s to a primitive long array.
	 * 
	 * @param wrapper
	 *            the wrapped array of {@link Long}s
	 * @return a unwrapped array of longs
	 * @throws NullPointerException
	 *             if wrapper itself or one element of wrapper is
	 *             <code>null</code>
	 */
	public static long[] unwrap(Long[] wrapper) throws NullPointerException
	{
		int c = wrapper.length;
		long[] primitive = new long[c];
		for(int i = 0; i < c; i++)
		{
			primitive[i] = wrapper[i];
		}
		return primitive;
	}


	/**
	 * Converts a array of wrapped {@link Boolean}s to a primitive boolean
	 * array.
	 * 
	 * @param wrapper
	 *            the wrapped array of {@link Boolean}s
	 * @return a unwrapped array of booleans
	 * @throws NullPointerException
	 *             if wrapper itself or one element of wrapper is
	 *             <code>null</code>
	 */
	public static boolean[] unwrap(Boolean[] wrapper) throws NullPointerException
	{
		int c = wrapper.length;
		boolean[] primitive = new boolean[c];
		for(int i = 0; i < c; i++)
		{
			primitive[i] = wrapper[i];
		}
		return primitive;
	}


	/**
	 * Converts a array of wrapped {@link Character}s to a primitive char array.
	 * 
	 * @param wrapper
	 *            the wrapped array of {@link Character}s
	 * @return a unwrapped array of chars
	 * @throws NullPointerException
	 *             if wrapper itself or one element of wrapper is
	 *             <code>null</code>
	 */
	public static char[] unwrap(Character[] wrapper) throws NullPointerException
	{
		int c = wrapper.length;
		char[] primitive = new char[c];
		for(int i = 0; i < c; i++)
		{
			primitive[i] = wrapper[i];
		}
		return primitive;
	}
}
