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


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.Vector;

import xdev.lang.LibraryMember;
import xdev.lang.NotNull;


/**
 * 
 * <p>
 * The <code>CollectionUtils</code> class provides utility methods for
 * {@link Collection} handling.
 * </p>
 * 
 * @author XDEV Software
 * 
 * @since 2.0
 */
@LibraryMember
public final class CollectionUtils
{
	private CollectionUtils()
	{
	}
	
	
	/**
	 * Copies collection elements into the given <code>array</code>.
	 * 
	 * <p>
	 * This method is a alias for
	 * <code>CollectionUtils.copyIntoArray(c,0,array,destOffset,c.size());</code>
	 * </p>
	 * 
	 * @param c
	 *            the collection to copy
	 * 
	 * @param array
	 *            the destination array
	 * 
	 * @param destOffset
	 *            starting position in the <code>array</code>
	 * 
	 * @throws IndexOutOfBoundsException
	 *             if copying would cause access of data outside array bounds.
	 * 
	 * @throws ArrayStoreException
	 *             if an element in the <code>c</code> could not be stored into
	 *             the <code>array</code> because of a type mismatch.
	 * 
	 * @throws NullPointerException
	 *             if either <code>c</code> or <code>array</code> is
	 *             <code>null</code>.
	 * 
	 * @see CollectionUtils#copyIntoArray(Collection, int, Object[], int, int)
	 * 
	 */
	public static void copyIntoArray(@NotNull Collection c, @NotNull Object[] array, int destOffset)
			throws IndexOutOfBoundsException, ArrayStoreException, NullPointerException
	{
		copyIntoArray(c,0,array,destOffset,c.size());
	}
	
	
	/**
	 * Copies an collection from the specified <code>c</code>, beginning at the
	 * specified position, to the specified position of the destination array.
	 * 
	 * @param c
	 *            the collection to copy
	 * 
	 * @param collectionOffset
	 *            starting position in the source collection
	 * 
	 * @param array
	 *            the destination array
	 * 
	 * @param destOffset
	 *            starting position in the <code>array</code>
	 * 
	 * @param count
	 *            the number of elements to be copied
	 * 
	 * @throws IndexOutOfBoundsException
	 *             if copying would cause access of data outside array bounds.
	 * 
	 * @throws ArrayStoreException
	 *             if an element in the <code>c</code> could not be stored into
	 *             the <code>array</code> because of a type mismatch.
	 * 
	 * @throws NullPointerException
	 *             if either <code>c</code> or <code>array</code> is
	 *             <code>null</code>.
	 * 
	 * @see System#arraycopy(Object, int, Object, int, int)
	 */
	public static void copyIntoArray(@NotNull Collection c, int collectionOffset,
			@NotNull Object[] array, int destOffset, int count) throws IndexOutOfBoundsException,
			ArrayStoreException, NullPointerException
	{
		System.arraycopy(c.toArray(),collectionOffset,array,destOffset,count);
	}
	
	
	/**
	 * 
	 * Returns an list containing all of the elements in this
	 * <code>values</code> in proper sequence (from first to last element).
	 * 
	 * <p>
	 * The runtime type of the returned {@link List} is that of the specified
	 * <code><b>T</b></code>.
	 * </p>
	 * 
	 * 
	 * @param <T>
	 *            any type
	 * 
	 * @param values
	 *            an array of type <code>T</code> elements
	 * 
	 * @return a list containing the elements of this <code>values</code>
	 * 
	 * @see Arrays#asList(Object...)
	 */
	public static <T> List<T> asList(T... values)
	{
		List<T> list = new ArrayList(values.length);
		addAll(list,values);
		return list;
	}
	
	
	/**
	 * 
	 * Returns an list containing all of the elements in this <code>e</code> in
	 * proper sequence (from first to last element).
	 * 
	 * <p>
	 * The runtime type of the returned {@link List} is that of the specified
	 * <code><b>T</b></code>.
	 * </p>
	 * 
	 * 
	 * @param <T>
	 *            any type
	 * 
	 * @param e
	 *            an {@link Enumeration} of type <code>T</code>
	 * 
	 * @return a list containing the elements of this <code>e</code>
	 * 
	 */
	public static <T> List<T> asList(Enumeration<T> e)
	{
		List<T> list = new ArrayList();
		while(e.hasMoreElements())
		{
			list.add(e.nextElement());
		}
		return list;
	}
	
	
	/**
	 * 
	 * Returns an list containing all of the elements in this <code>it</code> in
	 * proper sequence (from first to last element).
	 * 
	 * <p>
	 * The runtime type of the returned {@link List} is that of the specified
	 * <code><b>T</b></code>.
	 * </p>
	 * 
	 * 
	 * @param <T>
	 *            any type
	 * 
	 * @param it
	 *            an {@link Iterator} of type <code>T</code>
	 * 
	 * @return a list containing the elements of this <code>it</code>
	 * 
	 */
	public static <T> List<T> asList(Iterator<T> it)
	{
		List<T> list = new ArrayList();
		while(it.hasNext())
		{
			list.add(it.next());
		}
		return list;
	}
	
	
	/**
	 * 
	 * Returns an iterator over the specified enumeration.
	 * 
	 * 
	 * @param <T>
	 *            any type
	 * @param e
	 *            an {@link Enumeration} of type <code>T</code>
	 * 
	 * @return an {@link Iterator} over the specified enumeration
	 */
	public static <T> Iterator<T> asIterator(final Enumeration<T> e)
	{
		return new Iterator<T>()
		{
			@Override
			public boolean hasNext()
			{
				return e.hasMoreElements();
			}
			
			
			@Override
			public T next()
			{
				return e.nextElement();
			}
			
			
			@Override
			public void remove()
			{
				throw new UnsupportedOperationException();
			}
		};
	}
	
	
	/**
	 * 
	 * Returns an enumeration over the specified iterator.
	 * 
	 * 
	 * @param <T>
	 *            any type
	 * @param e
	 *            an {@link Iterator} of type <code>T</code>
	 * 
	 * @return an {@link Enumeration} over the specified iterator
	 */
	public static <T> Enumeration<T> asEnumeration(final Iterator<T> e)
	{
		return new Enumeration<T>()
		{
			@Override
			public boolean hasMoreElements()
			{
				return e.hasNext();
			}
			
			
			@Override
			public T nextElement()
			{
				return e.next();
			}
		};
	}
	
	
	/**
	 * 
	 * Create a {@link List} with the size of <code>count</code>. The
	 * <code>value</code> are added <code>count</code> times.
	 * 
	 * <p>
	 * The runtime type of the returned {@link List} is that of the specified
	 * <code><b>E</b></code>.
	 * </p>
	 * 
	 * 
	 * @param <E>
	 *            any type
	 * 
	 * @param value
	 *            the element to insert into the list
	 * 
	 * @param count
	 *            the number of <code>E</code> elements of the list
	 * 
	 * @return a list containing the <code>value</code>
	 * 
	 */
	public static <E> List<E> createList(E value, int count)
	{
		List<E> list = new ArrayList(count);
		for(int i = 0; i < count; i++)
		{
			list.add(value);
		}
		return list;
	}
	
	
	/**
	 * 
	 * Returns a set containing all of the elements in this <code>values</code>.<br>
	 * 
	 * It makes no guarantees as to the iteration order of the set; in
	 * particular, it does not guarantee that the order will remain constant
	 * over time.
	 * 
	 * <p>
	 * The runtime type of the returned {@link Set} is that of the specified
	 * <code><b>T</b></code>.
	 * </p>
	 * 
	 * 
	 * @param <T>
	 *            any type
	 * 
	 * @param values
	 *            an array of type <code>T</code> elements
	 * 
	 * @return a {@link Set} containing the elements of this <code>values</code>
	 * 
	 * @see Set
	 * @see HashSet
	 */
	public static <T> Set<T> asSet(T... values)
	{
		Set<T> set = new HashSet(values.length);
		addAll(set,values);
		return set;
	}
	
	
	/**
	 * 
	 * Returns a set containing all of the elements in this <code>values</code>.
	 * 
	 * with predictable iteration order
	 * 
	 * <p>
	 * The runtime type of the returned {@link Set} is that of the specified
	 * <code><b>T</b></code>.
	 * </p>
	 * 
	 * 
	 * @param <T>
	 *            any type
	 * 
	 * @param values
	 *            an array of type <code>T</code> elements
	 * 
	 * @return a {@link Set} containing the elements of this <code>values</code>
	 * 
	 * @see Set
	 */
	public static <T> Set<T> asLinkedSet(T... values)
	{
		Set<T> set = new LinkedHashSet(values.length);
		addAll(set,values);
		return set;
	}
	
	
	/**
	 * Appends the specified <code>elements</code> to the
	 * <code>collection</code>. The capacity of the <code>collection</code> is
	 * increased by the count of the <code>elements</code>.
	 * 
	 * 
	 * @param collection
	 *            the destination {@link Collection}
	 * 
	 * @param elements
	 *            the elements to be placed in the <code>collection</code>
	 */
	public static <E> void addAll(Collection<? super E> collection, E... elements)
	{
		if(elements != null && elements.length > 0)
		{
			if(collection instanceof List)
			{
				ensureCapacity((List)collection,elements.length);
			}
			
			for(int i = 0; i < elements.length; i++)
			{
				collection.add(elements[i]);
			}
		}
	}
	
	
	/**
	 * Removes the specified <code>elements</code> from the <code>list</code>,
	 * if they are present (optional operation).
	 * 
	 * 
	 * @param list
	 *            the destination {@link Collection}
	 * 
	 * @param elements
	 *            the elements to be removed from the <code>list</code>
	 * 
	 * @return the count of the removed elements
	 * 
	 * @see Collection#removeAll(Collection)
	 */
	public static <E> int removeAll(Collection<? super E> list, E... elements)
	{
		int count = 0;
		
		if(elements != null && elements.length > 0)
		{
			for(int i = 0; i < elements.length; i++)
			{
				if(list.remove(elements[i]))
				{
					count++;
				}
			}
		}
		
		return count;
	}
	
	
	/**
	 * Removes the specified <code>value</code> from the <code>map</code>, if
	 * they are present (optional operation).
	 * 
	 * @param map
	 *            the destination {@link Map}
	 * 
	 * @param value
	 *            the element to be removed from the <code>map</code>
	 * 
	 * @return the count of the removed elements
	 * 
	 * @see ObjectUtils#equals(Object)
	 * @see Iterator
	 */
	public static int remove(Map map, Object value)
	{
		int count = 0;
		Iterator it = map.keySet().iterator();
		while(it.hasNext())
		{
			if(ObjectUtils.equals(map.get(it.next()),value))
			{
				it.remove();
				count++;
			}
		}
		return count;
	}
	
	
	/**
	 * Increases the capacity of this <tt>List</tt> instance by <code>add</code>
	 * .
	 * 
	 * 
	 * @param list
	 *            the {@link List} to increase the size
	 * 
	 * @param add
	 *            the additional capacity
	 * 
	 */
	public static void ensureCapacity(List<?> list, int add)
	{
		if(list instanceof ArrayList)
		{
			((ArrayList)list).ensureCapacity(list.size() + add);
		}
		else if(list instanceof Vector)
		{
			((Vector)list).ensureCapacity(list.size() + add);
		}
	}
	
	
	/**
	 * 
	 * Appends the specified <code>elements</code> to the list if the
	 * <code>elements</code> are not already present in the list.
	 * 
	 * 
	 * @param list
	 *            the destination {@link List}
	 * 
	 * @param elements
	 *            the list of elements to add
	 * 
	 * @return the number of added elements
	 * 
	 */
	public static int addIfNotInList(List<?> list, List<?> elements)
	{
		int count = 0;
		
		for(Object element : elements)
		{
			if(addIfNotInList(list,element))
			{
				count++;
			}
		}
		
		return count;
	}
	
	
	/**
	 * Appends the specified <code>elements</code> to the list if the
	 * <code>elements</code> are not already present in the list.<br>
	 * The <code>comparer</code> is used to compare the elements.
	 * 
	 * @param <E>
	 *            any type
	 * 
	 * @param list
	 *            the destination {@link List}
	 * 
	 * @param elements
	 *            the list of elements to add
	 * 
	 * @param comparer
	 *            the comparer by which the element are compared.
	 * 
	 * @return the number of added elements as <code>int</code>
	 */
	public static <E> int addIfNotInList(List<E> list, List<E> elements, Comparer<E> comparer)
	{
		int count = 0;
		
		for(E element : elements)
		{
			if(addIfNotInList(list,element,comparer))
			{
				count++;
			}
		}
		
		return count;
	}
	
	
	/**
	 * 
	 * Appends the specified <code>element</code> to the list if the
	 * <code>element</code> is not already present in the list.
	 * 
	 * 
	 * @param list
	 *            the destination {@link List}
	 * 
	 * @param element
	 *            a <code>Object</code> to add
	 * 
	 * @return <code>true</code> if the <code>element</code> is added, otherwise
	 *         <code>flase</code>
	 * 
	 */
	public static boolean addIfNotInList(List list, Object element)
	{
		if(!list.contains(element))
		{
			list.add(element);
			
			return true;
		}
		
		return false;
	}
	
	
	/**
	 * 
	 * Appends the specified <code>element</code> to the list if the
	 * <code>element</code> is not already present in the list.
	 * 
	 * @param <E>
	 *            any type
	 * 
	 * @param list
	 *            the destination {@link List}
	 * 
	 * @param element
	 *            the element to be searched for.
	 * 
	 * @param comparer
	 *            the comparer by which the element are compared.
	 * 
	 * @return <code>true</code> if the <code>element</code> is added,
	 *         <code>false</code> if the <code>element</code> is already present
	 *         in the <code>list</code>
	 */
	public static <E> boolean addIfNotInList(List<E> list, E element, Comparer<E> comparer)
	{
		if(!contains(list,element,comparer))
		{
			list.add(element);
			
			return true;
		}
		
		return false;
	}
	
	
	/**
	 * Returns <tt>true</tt> if the <code>list</code> contains the specified
	 * element. The <code>comparer</code> is used to compare the elements.
	 * 
	 * 
	 * @param <E>
	 *            any type
	 * 
	 * @param list
	 *            the destination {@link List}
	 * 
	 * @param element
	 *            the element to be searched for.
	 * 
	 * @param comparer
	 *            the comparer by which the element are compared.
	 * 
	 * @return <code>true</code> if the list contains the <code>element</code>,
	 *         otherwise <code>false</code>
	 * 
	 * @throws NullPointerException
	 *             if the <code>comparer</code> is <code>null</code>
	 * 
	 * @see #indexOf(List, Object, Comparer)
	 */
	public static <E> boolean contains(List<E> list, E element, Comparer<E> comparer)
			throws NullPointerException
	{
		return indexOf(list,element,comparer) >= 0;
	}
	
	
	/**
	 * Returns the index of the first occurrence of the specified element in the
	 * <code>list</code>, or -1 if this list does not contain the element.
	 * 
	 * @param <E>
	 *            any type
	 * 
	 * @param list
	 *            the list to be searched.
	 * 
	 * @param element
	 *            the element to be searched for.
	 * 
	 * @param comparer
	 *            the comparer by which the element are compared.
	 * 
	 * @return the index of the search element, if it is contained in the list
	 *         otherwise <code>-1</code>.
	 * 
	 * @throws NullPointerException
	 *             if the <code>list</code> or <code>comparer</code> is
	 *             <code>null</code>
	 */
	public static <E> int indexOf(List<E> list, E element, @NotNull Comparer<E> comparer)
			throws NullPointerException
	{
		int size = list.size();
		for(int i = 0; i < size; i++)
		{
			if(comparer.equals(list.get(i),element))
			{
				return i;
			}
		}
		
		return -1;
	}
	
	
	/**
	 * Delete all double values of the <code>list</code>.
	 * 
	 * @param list
	 *            the destination list
	 * 
	 * @see #hasDoubleValues(List)
	 */
	public static void deleteDoubleValues(List list)
	{
		if(hasDoubleValues(list))
		{
			int c = list.size();
			ArrayList tmp = new ArrayList(c);
			for(int i = 0; i < c; i++)
			{
				addIfNotInList(tmp,list.get(i));
			}
			list.clear();
			list.addAll(tmp);
			tmp.clear();
		}
	}
	
	
	/**
	 * Returns <code>true</code> if the <code>list</code> has double elements.
	 * 
	 * @param list
	 *            the destination {@link List}
	 * 
	 * @return <code>true</code> if the <code>list</code> has double elements,
	 *         otherwise <code>false</code>
	 * 
	 */
	public static boolean hasDoubleValues(List list)
	{
		int c = list.size();
		if(c > 1)
		{
			for(int i = 0; i < c; i++)
			{
				if(indexOf(list,list.get(i),i) >= 0)
				{
					return true;
				}
			}
		}
		
		return false;
	}
	
	
	// TODO javadoc
	public static int indexOf(List list, Object element, int... skipIndices)
	{
		int c = list.size();
		if(c > skipIndices.length)
		{
			if(skipIndices.length > 1)
			{
				Arrays.sort(skipIndices);
			}
			
			for(int i = 0; i < c; i++)
			{
				if(Arrays.binarySearch(skipIndices,i) < 0
						&& ObjectUtils.equals(element,list.get(i)))
				{
					return i;
				}
				
			}
		}
		
		return -1;
	}
	
	
	/**
	 * Returns the indices of the occurrence of the specified elements in the
	 * <code>list</code>.
	 * 
	 * @param <T>
	 *            any type
	 * 
	 * @param list
	 *            the destination {@link List}
	 * 
	 * @param obj
	 *            the elements to searched for
	 * 
	 * @return the indices of the search elements as <code>int</code> array
	 * 
	 * @throws NullPointerException
	 *             if <code>list</code> is <code>null</code>
	 */
	public static <T> int[] indicesOf(List<T> list, T... obj) throws NullPointerException
	{
		int[] indices = new int[obj.length];
		for(int i = 0; i < obj.length; i++)
		{
			indices[i] = list.indexOf(obj[i]);
		}
		return indices;
	}
	
	
	/**
	 * 
	 * Returns the number of elements in the specified collection equal to the
	 * specified object.
	 * 
	 * @param c
	 *            the collection in which to determine the frequency of
	 *            <code>search</code>
	 * 
	 * @param search
	 *            the object whose frequency is to be determined
	 * 
	 * @return the number of elements in <code>c</code> equal to the specified
	 *         object.
	 * 
	 * @throws NullPointerException
	 *             if <code>c</code> is null
	 */
	public static int frequency(@NotNull Collection<?> c, Object search)
			throws NullPointerException
	{
		int freq = 0;
		
		for(Object o : c)
		{
			if(ObjectUtils.equals(o,search))
			{
				freq++;
			}
		}
		
		return freq;
	}
	
	
	/**
	 * Swaps the elements at the specified positions in the specified list.
	 * 
	 * @param list
	 *            The list in which to swap elements
	 * 
	 * @param index1
	 *            the index of one element to be swapped
	 * 
	 * @param index2
	 *            the index of the other element to be swapped
	 * 
	 * @throws IndexOutOfBoundsException
	 *             if either <tt>index1</tt> or <tt>index2</tt> is out of range
	 *             (index1 &lt; 0 || index1 &gt;= list.size() || index2 &lt; 0
	 *             || index2 &gt;= list.size()).
	 */
	public static void swap(List list, int index1, int index2) throws IndexOutOfBoundsException
	{
		Object tmp = list.get(index1);
		list.set(index1,list.get(index2));
		list.set(index2,tmp);
	}
	
	
	/**
	 * Move the element at the specified positions (<code>index</code>).
	 * 
	 * @param list
	 *            The list in which to move elements
	 * 
	 * @param index
	 *            the index of the element to be moved
	 * 
	 * @param up
	 *            define the elements new position; if <code>true</code> the new
	 *            index is increased by 1, otherwise the the new index is
	 *            reduced by 1
	 */
	public static void move(List list, int index, boolean up)
	{
		move(list,index,up ? index - 1 : index + 1);
	}
	
	
	/**
	 * Move the element at the specified positions (<code>from</code>) to the
	 * specified position (<code>to</code>) in the <code>list</code>.
	 * 
	 * @param list
	 *            The list in which to move elements
	 * 
	 * @param from
	 *            the index of the element to be moved
	 * 
	 * @param to
	 *            the index of the elements new position
	 * 
	 */
	public static void move(List list, int from, int to)
	{
		list.add(from > to ? to : to - 1,list.remove(from));
	}
	
	
	// TODO javadoc
	public static void move(List list, int firstIndex, int lastIndex, int to)
			throws IndexOutOfBoundsException
	{
		if((to >= firstIndex && to <= lastIndex) || to > list.size())
		{
			throw new IndexOutOfBoundsException();
		}
		
		int count = lastIndex - firstIndex + 1;
		List tmp = new ArrayList(count);
		for(int i = 0; i < count; i++)
		{
			tmp.add(list.remove(firstIndex));
		}
		
		if(to > lastIndex)
		{
			to -= count;
		}
		
		list.addAll(to,tmp);
	}
	
	
	// TODO javadoc
	public static <E extends Enum<E>> void sortByName(List<E> list)
	{
		Collections.sort(list,new Comparator<E>()
		{
			public int compare(E e1, E e2)
			{
				return e1.name().compareTo(e2.name());
			}
		});
	}
	
	
	/**
	 * Adds <code>value</code> to the list associated with <code>key</code> in
	 * <code>map</code>.<br>
	 * If <code>key</code> has no value yet, a list is created fist.
	 * 
	 * 
	 * @param <K>
	 *            The key's type
	 * @param <V>
	 *            The value's type
	 * @param map
	 *            The store
	 * @param key
	 *            The key associated with value
	 * @param value
	 *            The value to accumulate
	 */
	public static <K, V> void accumulate(Map<K, List<V>> map, K key, V value)
	{
		List<V> list = map.get(key);
		if(list == null)
		{
			list = new ArrayList();
			map.put(key,list);
		}
		list.add(value);
	}
	
	
	public static Map<String, Object> toMap(ResourceBundle bundle)
	{
		Set<String> keys = bundle.keySet();
		Map<String, Object> map = new Hashtable(keys.size());
		for(String key : keys)
		{
			map.put(key,bundle.getObject(key));
		}
		return map;
	}
}
