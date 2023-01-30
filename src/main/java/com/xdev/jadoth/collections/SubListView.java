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

import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import com.xdev.jadoth.lang.Equalator;
import com.xdev.jadoth.lang.functional.Operation;
import com.xdev.jadoth.lang.functional.Predicate;
import com.xdev.jadoth.lang.functional.aggregates.Aggregate;
import com.xdev.jadoth.lang.functional.aggregates.TAggregate;
import com.xdev.jadoth.lang.functional.controlflow.TOperation;
import com.xdev.jadoth.lang.functional.controlflow.TPredicate;
import com.xdev.jadoth.util.VarChar;


/**
 * @author Thomas Muenz
 *
 */
public class SubListView<E> implements XGettingList<E>
{

	/**
	 * @param e
	 * @return
	 * @see com.xdev.jadoth.collections.XGettingList#add(java.lang.Object)
	 */
	@Override
	public boolean add(final E e)
	{
		return false;
	}

	/**
	 * @param index
	 * @param element
	 * @see com.xdev.jadoth.collections.XGettingList#add(int, java.lang.Object)
	 */
	@Override
	public void add(final int index, final E element)
	{

	}

	/**
	 * @param c
	 * @return
	 * @see com.xdev.jadoth.collections.XGettingList#addAll(java.util.Collection)
	 */
	@Override
	public boolean addAll(final Collection<? extends E> c)
	{
		return false;
	}

	/**
	 * @param index
	 * @param c
	 * @return
	 * @see com.xdev.jadoth.collections.XGettingList#addAll(int, java.util.Collection)
	 */
	@Override
	public boolean addAll(final int index, final Collection<? extends E> c)
	{
		return false;
	}

	/**
	 * @param element
	 * @param comparator
	 * @return
	 * @see com.xdev.jadoth.collections.XGettingList#binarySearch(java.lang.Object, java.util.Comparator)
	 */
	@Override
	public int binarySearch(final E element, final Comparator<E> comparator)
	{
		return 0;
	}

	/**
	 *
	 * @see com.xdev.jadoth.collections.XGettingList#clear()
	 */
	@Override
	public void clear()
	{

	}

	/**
	 * @param o
	 * @return
	 * @deprecated
	 * @see com.xdev.jadoth.collections.XGettingList#contains(java.lang.Object)
	 */
	@Deprecated
	@Override
	public boolean contains(final Object o)
	{
		return false;
	}

	/**
	 * @param c
	 * @return
	 * @deprecated
	 * @see com.xdev.jadoth.collections.XGettingList#containsAll(java.util.Collection)
	 */
	@Deprecated
	@Override
	public boolean containsAll(final Collection<?> c)
	{
		return false;
	}

	/**
	 * @return
	 * @see com.xdev.jadoth.collections.XGettingList#copy()
	 */
	@Override
	public XList<E> copy()
	{
		return null;
	}

	/**
	 * @param <L>
	 * @param startIndex
	 * @param endIndex
	 * @param targetCollection
	 * @return
	 * @see com.xdev.jadoth.collections.XGettingList#copyRange(int, int, com.xdev.jadoth.collections.Collecting)
	 */
	@Override
	public <L extends Collecting<E>> L copyRange(final int startIndex, final int endIndex, final L targetCollection)
	{
		return null;
	}

	/**
	 * @param index
	 * @return
	 * @see com.xdev.jadoth.collections.XGettingList#get(int)
	 */
	@Override
	public E get(final int index)
	{
		return null;
	}

	/**
	 * @return
	 * @see com.xdev.jadoth.collections.XGettingList#getFirst()
	 */
	@Override
	public E getFirst()
	{
		return null;
	}

	/**
	 * @return
	 * @see com.xdev.jadoth.collections.XGettingList#getLast()
	 */
	@Override
	public E getLast()
	{
		return null;
	}

	/**
	 * @param o
	 * @return
	 * @see com.xdev.jadoth.collections.XGettingList#indexOf(java.lang.Object)
	 */
	@Override
	public int indexOf(final Object o)
	{
		return 0;
	}

	/**
	 * @param element
	 * @param equalator
	 * @return
	 * @see com.xdev.jadoth.collections.XGettingList#indexOf(java.lang.Object, com.xdev.jadoth.lang.Equalator)
	 */
	@Override
	public int indexOf(final E element, final Equalator<E> equalator)
	{
		return 0;
	}

	/**
	 * @param predicate
	 * @return
	 * @see com.xdev.jadoth.collections.XGettingList#indexOf(com.xdev.jadoth.lang.functional.Predicate)
	 */
	@Override
	public int indexOf(final Predicate<E> predicate)
	{
		return 0;
	}

	/**
	 * @param predicate
	 * @return
	 * @see com.xdev.jadoth.collections.XGettingList#indexOf(com.xdev.jadoth.lang.functional.controlflow.TPredicate)
	 */
	@Override
	public int indexOf(final TPredicate<E> predicate)
	{
		return 0;
	}

	/**
	 * @param element
	 * @return
	 * @see com.xdev.jadoth.collections.XGettingList#indexOfId(java.lang.Object)
	 */
	@Override
	public int indexOfId(final E element)
	{
		return 0;
	}

	/**
	 * @return
	 * @see com.xdev.jadoth.collections.XGettingList#isEmpty()
	 */
	@Override
	public boolean isEmpty()
	{
		return false;
	}

	/**
	 * @param comparator
	 * @return
	 * @see com.xdev.jadoth.collections.XGettingList#isSorted(java.util.Comparator)
	 */
	@Override
	public boolean isSorted(final Comparator<E> comparator)
	{
		return false;
	}

	/**
	 * @return
	 * @see com.xdev.jadoth.collections.XGettingList#iterator()
	 */
	@Override
	public Iterator<E> iterator()
	{
		return null;
	}

	/**
	 * @param o
	 * @return
	 * @see com.xdev.jadoth.collections.XGettingList#lastIndexOf(java.lang.Object)
	 */
	@Override
	public int lastIndexOf(final Object o)
	{
		return 0;
	}

	/**
	 * @param element
	 * @param equalator
	 * @return
	 * @see com.xdev.jadoth.collections.XGettingList#lastIndexOf(java.lang.Object, com.xdev.jadoth.lang.Equalator)
	 */
	@Override
	public int lastIndexOf(final E element, final Equalator<E> equalator)
	{
		return 0;
	}

	/**
	 * @param element
	 * @return
	 * @see com.xdev.jadoth.collections.XGettingList#lastIndexOfId(java.lang.Object)
	 */
	@Override
	public int lastIndexOfId(final E element)
	{
		return 0;
	}

	/**
	 * @return
	 * @see com.xdev.jadoth.collections.XGettingList#listIterator()
	 */
	@Override
	public ListIterator<E> listIterator()
	{
		return null;
	}

	/**
	 * @param index
	 * @return
	 * @see com.xdev.jadoth.collections.XGettingList#listIterator(int)
	 */
	@Override
	public ListIterator<E> listIterator(final int index)
	{
		return null;
	}

	/**
	 * @param comparator
	 * @return
	 * @see com.xdev.jadoth.collections.XGettingList#maxIndex(java.util.Comparator)
	 */
	@Override
	public int maxIndex(final Comparator<E> comparator)
	{
		return 0;
	}

	/**
	 * @param comparator
	 * @return
	 * @see com.xdev.jadoth.collections.XGettingList#minIndex(java.util.Comparator)
	 */
	@Override
	public int minIndex(final Comparator<E> comparator)
	{
		return 0;
	}

	/**
	 * @param o
	 * @return
	 * @see com.xdev.jadoth.collections.XGettingList#remove(java.lang.Object)
	 */
	@Override
	public boolean remove(final Object o)
	{
		return false;
	}

	/**
	 * @param index
	 * @return
	 * @see com.xdev.jadoth.collections.XGettingList#remove(int)
	 */
	@Override
	public E remove(final int index)
	{
		return null;
	}

	/**
	 * @param c
	 * @return
	 * @deprecated
	 * @see com.xdev.jadoth.collections.XGettingList#removeAll(java.util.Collection)
	 */
	@Deprecated
	@Override
	public boolean removeAll(final Collection<?> c)
	{
		return false;
	}

	/**
	 * @param c
	 * @return
	 * @deprecated
	 * @see com.xdev.jadoth.collections.XGettingList#retainAll(java.util.Collection)
	 */
	@Deprecated
	@Override
	public boolean retainAll(final Collection<?> c)
	{
		return false;
	}

	/**
	 * @param <R>
	 * @param startIndex
	 * @param endIndex
	 * @param aggregate
	 * @return
	 * @see com.xdev.jadoth.collections.XGettingList#rngAggregate(int, int, com.xdev.jadoth.lang.functional.aggregates.Aggregate)
	 */
	@Override
	public <R> R rngAggregate(final int startIndex, final int endIndex, final Aggregate<E, R> aggregate)
	{
		return null;
	}

	/**
	 * @param <R>
	 * @param startIndex
	 * @param endIndex
	 * @param aggregate
	 * @return
	 * @see com.xdev.jadoth.collections.XGettingList#rngAggregate(int, int, com.xdev.jadoth.lang.functional.aggregates.TAggregate)
	 */
	@Override
	public <R> R rngAggregate(final int startIndex, final int endIndex, final TAggregate<E, R> aggregate)
	{
		return null;
	}

	/**
	 * @param startIndex
	 * @param endIndex
	 * @param vc
	 * @see com.xdev.jadoth.collections.XGettingList#rngAppendTo(int, int, com.xdev.jadoth.util.VarChar)
	 */
	@Override
	public void rngAppendTo(final int startIndex, final int endIndex, final VarChar vc)
	{

	}

	/**
	 * @param startIndex
	 * @param endIndex
	 * @param vc
	 * @param seperator
	 * @see com.xdev.jadoth.collections.XGettingList#rngAppendTo(int, int, com.xdev.jadoth.util.VarChar, java.lang.String)
	 */
	@Override
	public void rngAppendTo(final int startIndex, final int endIndex, final VarChar vc, final String seperator)
	{

	}

	/**
	 * @param startIndex
	 * @param endIndex
	 * @param vc
	 * @param seperator
	 * @see com.xdev.jadoth.collections.XGettingList#rngAppendTo(int, int, com.xdev.jadoth.util.VarChar, char)
	 */
	@Override
	public void rngAppendTo(final int startIndex, final int endIndex, final VarChar vc, final char seperator)
	{

	}

	/**
	 * @param startIndex
	 * @param endIndex
	 * @param predicate
	 * @return
	 * @see com.xdev.jadoth.collections.XGettingList#rngApplies(int, int, com.xdev.jadoth.lang.functional.Predicate)
	 */
	@Override
	public boolean rngApplies(final int startIndex, final int endIndex, final Predicate<E> predicate)
	{
		return false;
	}

	/**
	 * @param startIndex
	 * @param endIndex
	 * @param predicate
	 * @return
	 * @see com.xdev.jadoth.collections.XGettingList#rngApplies(int, int, com.xdev.jadoth.lang.functional.controlflow.TPredicate)
	 */
	@Override
	public boolean rngApplies(final int startIndex, final int endIndex, final TPredicate<E> predicate)
	{
		return false;
	}

	/**
	 * @param startIndex
	 * @param endIndex
	 * @param element
	 * @param comparator
	 * @return
	 * @see com.xdev.jadoth.collections.XGettingList#rngBinarySearch(int, int, java.lang.Object, java.util.Comparator)
	 */
	@Override
	public int rngBinarySearch(final int startIndex, final int endIndex, final E element, final Comparator<E> comparator)
	{
		return 0;
	}

	/**
	 * @param startIndex
	 * @param endIndex
	 * @param predicate
	 * @return
	 * @see com.xdev.jadoth.collections.XGettingList#rngContains(int, int, com.xdev.jadoth.lang.functional.Predicate)
	 */
	@Override
	public boolean rngContains(final int startIndex, final int endIndex, final Predicate<E> predicate)
	{
		return false;
	}

	/**
	 * @param startIndex
	 * @param endIndex
	 * @param predicate
	 * @return
	 * @see com.xdev.jadoth.collections.XGettingList#rngContains(int, int, com.xdev.jadoth.lang.functional.controlflow.TPredicate)
	 */
	@Override
	public boolean rngContains(final int startIndex, final int endIndex, final TPredicate<E> predicate)
	{
		return false;
	}

	/**
	 * @param startIndex
	 * @param endIndex
	 * @param element
	 * @param comparator
	 * @return
	 * @see com.xdev.jadoth.collections.XGettingList#rngContains(int, int, java.lang.Object, com.xdev.jadoth.lang.Equalator)
	 */
	@Override
	public boolean rngContains(final int startIndex, final int endIndex, final E element, final Equalator<E> comparator)
	{
		return false;
	}

	/**
	 * @param startIndex
	 * @param endIndex
	 * @param c
	 * @param ignoreNulls
	 * @param equalator
	 * @return
	 * @see com.xdev.jadoth.collections.XGettingList#rngContainsAll(int, int, Collection, boolean, com.xdev.jadoth.lang.Equalator)
	 */
	@Override
	public boolean rngContainsAll(final int startIndex, final int endIndex, final Collection<E> c, final boolean ignoreNulls, final Equalator<E> equalator)
	{
		return false;
	}

	/**
	 * @param startIndex
	 * @param endIndex
	 * @param c
	 * @param ignoreNulls
	 * @return
	 * @see com.xdev.jadoth.collections.XGettingList#rngContainsAll(int, int, java.util.Collection, boolean)
	 */
	@Override
	public boolean rngContainsAll(final int startIndex, final int endIndex, final Collection<?> c, final boolean ignoreNulls)
	{
		return false;
	}

	/**
	 * @param startIndex
	 * @param endIndex
	 * @param o
	 * @return
	 * @see com.xdev.jadoth.collections.XGettingList#rngContainsId(int, int, java.lang.Object)
	 */
	@Override
	public boolean rngContainsId(final int startIndex, final int endIndex, final E o)
	{
		return false;
	}

	/**
	 * @param <L>
	 * @param startIndex
	 * @param endIndex
	 * @param targetCollection
	 * @param predicate
	 * @return
	 * @see com.xdev.jadoth.collections.XGettingList#rngCopyTo(int, int, com.xdev.jadoth.collections.Collecting, com.xdev.jadoth.lang.functional.Predicate)
	 */
	@Override
	public <L extends Collecting<E>> L rngCopyTo(final int startIndex, final int endIndex, final L targetCollection, final Predicate<E> predicate)
	{
		return null;
	}

	/**
	 * @param <L>
	 * @param startIndex
	 * @param endIndex
	 * @param targetCollection
	 * @param predicate
	 * @return
	 * @see com.xdev.jadoth.collections.XGettingList#rngCopyTo(int, int, com.xdev.jadoth.collections.Collecting, com.xdev.jadoth.lang.functional.controlflow.TPredicate)
	 */
	@Override
	public <L extends Collecting<E>> L rngCopyTo(final int startIndex, final int endIndex, final L targetCollection, final TPredicate<E> predicate)
	{
		return null;
	}

	/**
	 * @param <L>
	 * @param startIndex
	 * @param endIndex
	 * @param targetCollection
	 * @param predicate
	 * @param limit
	 * @return
	 * @see com.xdev.jadoth.collections.XGettingList#rngCopyTo(int, int, com.xdev.jadoth.collections.Collecting, com.xdev.jadoth.lang.functional.Predicate, int)
	 */
	@Override
	public <L extends Collecting<E>> L rngCopyTo(final int startIndex, final int endIndex, final L targetCollection, final Predicate<E> predicate,final int skip, final Integer limit)
	{
		return null;
	}

	/**
	 * @param <L>
	 * @param startIndex
	 * @param endIndex
	 * @param targetCollection
	 * @param predicate
	 * @param limit
	 * @return
	 * @see com.xdev.jadoth.collections.XGettingList#rngCopyTo(int, int, com.xdev.jadoth.collections.Collecting, com.xdev.jadoth.lang.functional.controlflow.TPredicate, int)
	 */
	@Override
	public <L extends Collecting<E>> L rngCopyTo(final int startIndex, final int endIndex, final L targetCollection, final TPredicate<E> predicate,final int skip, final Integer limit)
	{
		return null;
	}

	/**
	 * @param startIndex
	 * @param endIndex
	 * @param element
	 * @param equalator
	 * @return
	 * @see com.xdev.jadoth.collections.XGettingList#rngCount(int, int, java.lang.Object, com.xdev.jadoth.lang.Equalator)
	 */
	@Override
	public int rngCount(final int startIndex, final int endIndex, final E element, final Equalator<E> equalator)
	{
		return 0;
	}

	/**
	 * @param startIndex
	 * @param endIndex
	 * @param element
	 * @return
	 * @see com.xdev.jadoth.collections.XGettingList#rngCount(int, int, java.lang.Object)
	 */
	@Override
	public int rngCount(final int startIndex, final int endIndex, final E element)
	{
		return 0;
	}

	/**
	 * @param startIndex
	 * @param endIndex
	 * @param predicate
	 * @return
	 * @see com.xdev.jadoth.collections.XGettingList#rngCount(int, int, com.xdev.jadoth.lang.functional.Predicate)
	 */
	@Override
	public int rngCount(final int startIndex, final int endIndex, final Predicate<E> predicate)
	{
		return 0;
	}

	/**
	 * @param startIndex
	 * @param endIndex
	 * @param predicate
	 * @return
	 * @see com.xdev.jadoth.collections.XGettingList#rngCount(int, int, com.xdev.jadoth.lang.functional.controlflow.TPredicate)
	 */
	@Override
	public int rngCount(final int startIndex, final int endIndex, final TPredicate<E> predicate)
	{
		return 0;
	}

	/**
	 * @param startIndex
	 * @param list
	 * @param length
	 * @param comparator
	 * @return
	 * @see com.xdev.jadoth.collections.XGettingList#rngEqualsContent(int, java.util.List, int, com.xdev.jadoth.lang.Equalator)
	 */
	@Override
	public boolean rngEqualsContent(final int startIndex, final List<E> list, final int length, final Equalator<E> comparator)
	{
		return false;
	}

	/**
	 * @param <C>
	 * @param startIndex
	 * @param endIndex
	 * @param c
	 * @param comparatorr
	 * @param targetCollection
	 * @return
	 * @see com.xdev.jadoth.collections.XGettingList#rngExcept(int, int, com.xdev.jadoth.collections.XGettingCollection, com.xdev.jadoth.lang.Equalator, com.xdev.jadoth.collections.Collecting)
	 */
	@Override
	public <C extends Collecting<E>> C rngExcept(final int startIndex, final int endIndex, final XGettingCollection<E> c, final Equalator<E> comparatorr, final C targetCollection)
	{
		return null;
	}

	/**
	 * @param startIndex
	 * @param endIndex
	 * @param operation
	 * @return
	 * @see com.xdev.jadoth.collections.XGettingList#rngExecute(int, int, com.xdev.jadoth.lang.functional.Operation)
	 */
	@Override
	public XList<E> rngExecute(final int startIndex, final int endIndex, final Operation<E> operation)
	{
		return null;
	}

	/**
	 * @param startIndex
	 * @param endIndex
	 * @param operation
	 * @return
	 * @see com.xdev.jadoth.collections.XGettingList#rngExecute(int, int, com.xdev.jadoth.lang.functional.controlflow.TOperation)
	 */
	@Override
	public XList<E> rngExecute(final int startIndex, final int endIndex, final TOperation<E> operation)
	{
		return null;
	}

	/**
	 * @param startIndex
	 * @param endIndex
	 * @param ignoreMultipleNulls
	 * @return
	 * @see com.xdev.jadoth.collections.XGettingList#rngHasUniqueValues(int, int, boolean)
	 */
	@Override
	public boolean rngHasUniqueValues(final int startIndex, final int endIndex, final boolean ignoreMultipleNulls)
	{
		return false;
	}

	/**
	 * @param startIndex
	 * @param endIndex
	 * @param ignoreMultipleNulls
	 * @param comparator
	 * @return
	 * @see com.xdev.jadoth.collections.XGettingList#rngHasUniqueValues(int, int, boolean, com.xdev.jadoth.lang.Equalator)
	 */
	@Override
	public boolean rngHasUniqueValues(final int startIndex, final int endIndex, final boolean ignoreMultipleNulls, final Equalator<E> comparator)
	{
		return false;
	}

	/**
	 * @param startIndex
	 * @param endIndex
	 * @param element
	 * @param equalator
	 * @return
	 * @see com.xdev.jadoth.collections.XGettingList#rngIndexOf(int, int, java.lang.Object, com.xdev.jadoth.lang.Equalator)
	 */
	@Override
	public int rngIndexOf(final int startIndex, final int endIndex, final E element, final Equalator<E> equalator)
	{
		return 0;
	}

	/**
	 * @param startIndex
	 * @param endIndex
	 * @param predicate
	 * @return
	 * @see com.xdev.jadoth.collections.XGettingList#rngIndexOf(int, int, com.xdev.jadoth.lang.functional.Predicate)
	 */
	@Override
	public int rngIndexOf(final int startIndex, final int endIndex, final Predicate<E> predicate)
	{
		return 0;
	}

	/**
	 * @param startIndex
	 * @param endIndex
	 * @param predicate
	 * @return
	 * @see com.xdev.jadoth.collections.XGettingList#rngIndexOf(int, int, com.xdev.jadoth.lang.functional.controlflow.TPredicate)
	 */
	@Override
	public int rngIndexOf(final int startIndex, final int endIndex, final TPredicate<E> predicate)
	{
		return 0;
	}

	/**
	 * @param startIndex
	 * @param endIndex
	 * @param element
	 * @return
	 * @see com.xdev.jadoth.collections.XGettingList#rngIndexOfId(int, int, java.lang.Object)
	 */
	@Override
	public int rngIndexOfId(final int startIndex, final int endIndex, final E element)
	{
		return 0;
	}

	/**
	 * @param <C>
	 * @param startIndex
	 * @param endIndex
	 * @param c
	 * @param comparator
	 * @param targetCollection
	 * @return
	 * @see com.xdev.jadoth.collections.XGettingList#rngIntersect(int, int, com.xdev.jadoth.collections.XGettingCollection, com.xdev.jadoth.lang.Equalator, com.xdev.jadoth.collections.Collecting)
	 */
	@Override
	public <C extends Collecting<E>> C rngIntersect(final int startIndex, final int endIndex, final XGettingCollection<E> c, final Equalator<E> comparator, final C targetCollection)
	{
		return null;
	}

	/**
	 * @param startIndex
	 * @param endIndex
	 * @param comparator
	 * @return
	 * @see com.xdev.jadoth.collections.XGettingList#rngIsSorted(int, int, java.util.Comparator)
	 */
	@Override
	public boolean rngIsSorted(final int startIndex, final int endIndex, final Comparator<E> comparator)
	{
		return false;
	}

	/**
	 * @param startIndex
	 * @param endIndex
	 * @param comparator
	 * @return
	 * @see com.xdev.jadoth.collections.XGettingList#rngMax(int, int, java.util.Comparator)
	 */
	@Override
	public E rngMax(final int startIndex, final int endIndex, final Comparator<E> comparator)
	{
		return null;
	}

	/**
	 * @param startIndex
	 * @param endIndex
	 * @param comparator
	 * @return
	 * @see com.xdev.jadoth.collections.XGettingList#rngMaxIndex(int, int, java.util.Comparator)
	 */
	@Override
	public int rngMaxIndex(final int startIndex, final int endIndex, final Comparator<E> comparator)
	{
		return 0;
	}

	/**
	 * @param startIndex
	 * @param endIndex
	 * @param comparator
	 * @return
	 * @see com.xdev.jadoth.collections.XGettingList#rngMin(int, int, java.util.Comparator)
	 */
	@Override
	public E rngMin(final int startIndex, final int endIndex, final Comparator<E> comparator)
	{
		return null;
	}

	/**
	 * @param startIndex
	 * @param endIndex
	 * @param comparator
	 * @return
	 * @see com.xdev.jadoth.collections.XGettingList#rngMinIndex(int, int, java.util.Comparator)
	 */
	@Override
	public int rngMinIndex(final int startIndex, final int endIndex, final Comparator<E> comparator)
	{
		return 0;
	}

	/**
	 * @param startIndex
	 * @param endIndex
	 * @param operation
	 * @return
	 * @see com.xdev.jadoth.collections.XGettingList#rngProcess(int, int, com.xdev.jadoth.lang.functional.Operation)
	 */
	@Override
	public XList<E> rngProcess(final int startIndex, final int endIndex, final Operation<E> operation)
	{
		return null;
	}

	/**
	 * @param startIndex
	 * @param endIndex
	 * @param operation
	 * @return
	 * @see com.xdev.jadoth.collections.XGettingList#rngProcess(int, int, com.xdev.jadoth.lang.functional.controlflow.TOperation)
	 */
	@Override
	public XList<E> rngProcess(final int startIndex, final int endIndex, final TOperation<E> operation)
	{
		return null;
	}

	/**
	 * @param startIndex
	 * @param endIndex
	 * @param predicate
	 * @return
	 * @see com.xdev.jadoth.collections.XGettingList#rngScan(int, int, com.xdev.jadoth.lang.functional.Predicate)
	 */
	@Override
	public int rngScan(final int startIndex, final int endIndex, final Predicate<E> predicate)
	{
		return 0;
	}

	/**
	 * @param startIndex
	 * @param endIndex
	 * @param predicate
	 * @return
	 * @see com.xdev.jadoth.collections.XGettingList#rngScan(int, int, com.xdev.jadoth.lang.functional.controlflow.TPredicate)
	 */
	@Override
	public int rngScan(final int startIndex, final int endIndex, final TPredicate<E> predicate)
	{
		return 0;
	}

	/**
	 * @param startIndex
	 * @param endIndex
	 * @param element
	 * @param equalator
	 * @return
	 * @see com.xdev.jadoth.collections.XGettingList#rngSearch(int, int, java.lang.Object, com.xdev.jadoth.lang.Equalator)
	 */
	@Override
	public E rngSearch(final int startIndex, final int endIndex, final E element, final Equalator<E> equalator)
	{
		return null;
	}

	/**
	 * @param startIndex
	 * @param endIndex
	 * @param predicate
	 * @return
	 * @see com.xdev.jadoth.collections.XGettingList#rngSearch(int, int, com.xdev.jadoth.lang.functional.Predicate)
	 */
	@Override
	public E rngSearch(final int startIndex, final int endIndex, final Predicate<E> predicate)
	{
		return null;
	}

	/**
	 * @param startIndex
	 * @param endIndex
	 * @param predicate
	 * @return
	 * @see com.xdev.jadoth.collections.XGettingList#rngSearch(int, int, com.xdev.jadoth.lang.functional.controlflow.TPredicate)
	 */
	@Override
	public E rngSearch(final int startIndex, final int endIndex, final TPredicate<E> predicate)
	{
		return null;
	}

	/**
	 * @param startIndex
	 * @param endIndex
	 * @param type
	 * @return
	 * @see com.xdev.jadoth.collections.XGettingList#rngToArray(int, int, java.lang.Class)
	 */
	@Override
	public E[] rngToArray(final int startIndex, final int endIndex, final Class<E> type)
	{
		return null;
	}

	/**
	 * @param startIndex
	 * @param endIndex
	 * @return
	 * @see com.xdev.jadoth.collections.XGettingList#rngToArray(int, int)
	 */
	@Override
	public Object[] rngToArray(final int startIndex, final int endIndex)
	{
		return null;
	}

	/**
	 * @param <T>
	 * @param startIndex
	 * @param endIndex
	 * @param a
	 * @return
	 * @see com.xdev.jadoth.collections.XGettingList#rngToArray(int, int, T[])
	 */
	@Override
	public <T> T[] rngToArray(final int startIndex, final int endIndex, final T[] a)
	{
		return null;
	}

	/**
	 * @param <C>
	 * @param startIndex
	 * @param endIndex
	 * @param c
	 * @param comparatorr
	 * @param targetCollection
	 * @return
	 * @see com.xdev.jadoth.collections.XGettingList#rngUnion(int, int, com.xdev.jadoth.collections.XGettingCollection, com.xdev.jadoth.lang.Equalator, com.xdev.jadoth.collections.Collecting)
	 */
	@Override
	public <C extends Collecting<E>> C rngUnion(final int startIndex, final int endIndex, final XGettingCollection<E> c, final Equalator<E> comparatorr, final C targetCollection)
	{
		return null;
	}

	/**
	 * @param predicate
	 * @return
	 * @see com.xdev.jadoth.collections.XGettingList#scan(com.xdev.jadoth.lang.functional.Predicate)
	 */
	@Override
	public int scan(final Predicate<E> predicate)
	{
		return 0;
	}

	/**
	 * @param predicate
	 * @return
	 * @see com.xdev.jadoth.collections.XGettingList#scan(com.xdev.jadoth.lang.functional.controlflow.TPredicate)
	 */
	@Override
	public int scan(final TPredicate<E> predicate)
	{
		return 0;
	}

	/**
	 * @param index
	 * @param element
	 * @return
	 * @see com.xdev.jadoth.collections.XGettingList#set(int, java.lang.Object)
	 */
	@Override
	public E set(final int index, final E element)
	{
		return null;
	}

	/**
	 * @return
	 * @see com.xdev.jadoth.collections.XGettingList#size()
	 */
	@Override
	public int size()
	{
		return 0;
	}

	/**
	 * @param fromIndex
	 * @param toIndex
	 * @return
	 * @see com.xdev.jadoth.collections.XGettingList#subList(int, int)
	 */
	@Override
	public SubList<E> subList(final int fromIndex, final int toIndex)
	{
		return null;
	}

	/**
	 * @return
	 * @see com.xdev.jadoth.collections.XGettingList#toArray()
	 */
	@Override
	public Object[] toArray()
	{
		return null;
	}

	/**
	 * @param <T>
	 * @param a
	 * @return
	 * @see com.xdev.jadoth.collections.XGettingList#toArray(T[])
	 */
	@Override
	public <T> T[] toArray(final T[] a)
	{
		return null;
	}

	/**
	 * @return
	 * @see com.xdev.jadoth.collections.XGettingList#toReversed()
	 */
	@Override
	public XList<E> toReversed()
	{
		return null;
	}

	/**
	 * @param <R>
	 * @param aggregate
	 * @return
	 * @see com.xdev.jadoth.collections.XGettingCollection#aggregate(com.xdev.jadoth.lang.functional.aggregates.Aggregate)
	 */
	@Override
	public <R> R aggregate(final Aggregate<E, R> aggregate)
	{
		return null;
	}

	/**
	 * @param <R>
	 * @param aggregate
	 * @return
	 * @see com.xdev.jadoth.collections.XGettingCollection#aggregate(com.xdev.jadoth.lang.functional.aggregates.TAggregate)
	 */
	@Override
	public <R> R aggregate(final TAggregate<E, R> aggregate)
	{
		return null;
	}

	/**
	 * @param vc
	 * @see com.xdev.jadoth.collections.XGettingCollection#appendTo(com.xdev.jadoth.util.VarChar)
	 */
	@Override
	public void appendTo(final VarChar vc)
	{

	}

	/**
	 * @param vc
	 * @param seperator
	 * @see com.xdev.jadoth.collections.XGettingCollection#appendTo(com.xdev.jadoth.util.VarChar, char)
	 */
	@Override
	public void appendTo(final VarChar vc, final char seperator)
	{

	}

	/**
	 * @param vc
	 * @param seperator
	 * @see com.xdev.jadoth.collections.XGettingCollection#appendTo(com.xdev.jadoth.util.VarChar, java.lang.String)
	 */
	@Override
	public void appendTo(final VarChar vc, final String seperator)
	{

	}

	/**
	 * @param predicate
	 * @return
	 * @see com.xdev.jadoth.collections.XGettingCollection#applies(com.xdev.jadoth.lang.functional.Predicate)
	 */
	@Override
	public boolean applies(final Predicate<E> predicate)
	{
		return false;
	}

	/**
	 * @param predicate
	 * @return
	 * @see com.xdev.jadoth.collections.XGettingCollection#applies(com.xdev.jadoth.lang.functional.controlflow.TPredicate)
	 */
	@Override
	public boolean applies(final TPredicate<E> predicate)
	{
		return false;
	}

	/**
	 * @param element
	 * @param equalator
	 * @return
	 * @see com.xdev.jadoth.collections.XGettingCollection#contains(java.lang.Object, com.xdev.jadoth.lang.Equalator)
	 */
	@Override
	public boolean contains(final E element, final Equalator<E> equalator)
	{
		return false;
	}

	/**
	 * @param predicate
	 * @return
	 * @see com.xdev.jadoth.collections.XGettingCollection#contains(com.xdev.jadoth.lang.functional.Predicate)
	 */
	@Override
	public boolean contains(final Predicate<E> predicate)
	{
		return false;
	}

	/**
	 * @param predicate
	 * @return
	 * @see com.xdev.jadoth.collections.XGettingCollection#contains(com.xdev.jadoth.lang.functional.controlflow.TPredicate)
	 */
	@Override
	public boolean contains(final TPredicate<E> predicate)
	{
		return false;
	}

	/**
	 * @param c
	 * @param ignoreNulls
	 * @param equalator
	 * @return
	 * @see com.xdev.jadoth.collections.XGettingCollection#containsAll(java.util.Collection, boolean, com.xdev.jadoth.lang.Equalator)
	 */
	@Override
	public boolean containsAll(final Collection<E> c, final boolean ignoreNulls, final Equalator<E> equalator)
	{
		return false;
	}

	/**
	 * @param c
	 * @param ignoreNulls
	 * @return
	 * @see com.xdev.jadoth.collections.XGettingCollection#containsAll(java.util.Collection, boolean)
	 */
	@Override
	public boolean containsAll(final Collection<E> c, final boolean ignoreNulls)
	{
		return false;
	}

	/**
	 * @param element
	 * @return
	 * @see com.xdev.jadoth.collections.XGettingCollection#containsId(java.lang.Object)
	 */
	@Override
	public boolean containsId(final E element)
	{
		return false;
	}

	/**
	 * @param <C>
	 * @param targetCollection
	 * @param predicate
	 * @return
	 * @see com.xdev.jadoth.collections.XGettingCollection#copyTo(com.xdev.jadoth.collections.Collecting, com.xdev.jadoth.lang.functional.Predicate)
	 */
	@Override
	public <C extends Collecting<E>> C copyTo(final C targetCollection, final Predicate<E> predicate)
	{
		return null;
	}

	/**
	 * @param <C>
	 * @param targetCollection
	 * @param predicate
	 * @return
	 * @see com.xdev.jadoth.collections.XGettingCollection#copyTo(com.xdev.jadoth.collections.Collecting, com.xdev.jadoth.lang.functional.controlflow.TPredicate)
	 */
	@Override
	public <C extends Collecting<E>> C copyTo(final C targetCollection, final TPredicate<E> predicate)
	{
		return null;
	}

	/**
	 * @param <C>
	 * @param targetCollection
	 * @param predicate
	 * @param limit
	 * @return
	 * @see com.xdev.jadoth.collections.XGettingCollection#copyTo(com.xdev.jadoth.collections.Collecting, com.xdev.jadoth.lang.functional.Predicate, int)
	 */
	@Override
	public <C extends Collecting<E>> C copyTo(final C targetCollection, final Predicate<E> predicate,final int skip, final Integer limit)
	{
		return null;
	}

	/**
	 * @param <C>
	 * @param targetCollection
	 * @param predicate
	 * @param limit
	 * @return
	 * @see com.xdev.jadoth.collections.XGettingCollection#copyTo(com.xdev.jadoth.collections.Collecting, com.xdev.jadoth.lang.functional.controlflow.TPredicate, int)
	 */
	@Override
	public <C extends Collecting<E>> C copyTo(final C targetCollection, final TPredicate<E> predicate,final int skip, final Integer limit)
	{
		return null;
	}

	/**
	 * @param element
	 * @return
	 * @see com.xdev.jadoth.collections.XGettingCollection#count(java.lang.Object)
	 */
	@Override
	public int count(final E element)
	{
		return 0;
	}

	/**
	 * @param element
	 * @param equalator
	 * @return
	 * @see com.xdev.jadoth.collections.XGettingCollection#count(java.lang.Object, com.xdev.jadoth.lang.Equalator)
	 */
	@Override
	public int count(final E element, final Equalator<E> equalator)
	{
		return 0;
	}

	/**
	 * @param predicate
	 * @return
	 * @see com.xdev.jadoth.collections.XGettingCollection#count(com.xdev.jadoth.lang.functional.Predicate)
	 */
	@Override
	public int count(final Predicate<E> predicate)
	{
		return 0;
	}

	/**
	 * @param predicate
	 * @return
	 * @see com.xdev.jadoth.collections.XGettingCollection#count(com.xdev.jadoth.lang.functional.controlflow.TPredicate)
	 */
	@Override
	public int count(final TPredicate<E> predicate)
	{
		return 0;
	}

	/**
	 * @param <C>
	 * @param targetCollection
	 * @return
	 * @see com.xdev.jadoth.collections.XGettingCollection#distinct(com.xdev.jadoth.collections.Collecting)
	 */
	@Override
	public <C extends Collecting<E>> C distinct(final C targetCollection)
	{
		return null;
	}

	/**
	 * @param <C>
	 * @param targetCollection
	 * @param equalator
	 * @return
	 * @see com.xdev.jadoth.collections.XGettingCollection#distinct(com.xdev.jadoth.collections.Collecting, com.xdev.jadoth.lang.Equalator)
	 */
	@Override
	public <C extends Collecting<E>> C distinct(final C targetCollection, final Equalator<E> equalator)
	{
		return null;
	}

	/**
	 * @param list
	 * @param equalator
	 * @return
	 * @see com.xdev.jadoth.collections.XGettingCollection#equals(com.xdev.jadoth.collections.XGettingCollection, com.xdev.jadoth.lang.Equalator)
	 */
	@Override
	public boolean equals(final XGettingCollection<E> list, final Equalator<E> equalator)
	{
		return false;
	}

	/**
	 * @param list
	 * @param comparator
	 * @return
	 * @see com.xdev.jadoth.collections.XGettingCollection#equalsContent(java.util.Collection, com.xdev.jadoth.lang.Equalator)
	 */
	@Override
	public boolean equalsContent(final Collection<E> list, final Equalator<E> comparator)
	{
		return false;
	}

	/**
	 * @param <C>
	 * @param other
	 * @param comparator
	 * @param target
	 * @return
	 * @see com.xdev.jadoth.collections.XGettingCollection#except(com.xdev.jadoth.collections.XGettingCollection, com.xdev.jadoth.lang.Equalator, com.xdev.jadoth.collections.Collecting)
	 */
	@Override
	public <C extends Collecting<E>> C except(final XGettingCollection<E> other, final Equalator<E> comparator, final C target)
	{
		return null;
	}

	/**
	 * @param operation
	 * @return
	 * @see com.xdev.jadoth.collections.XGettingCollection#execute(com.xdev.jadoth.lang.functional.Operation)
	 */
	@Override
	public XGettingCollection<E> execute(final Operation<E> operation)
	{
		return null;
	}

	/**
	 * @param operation
	 * @return
	 * @see com.xdev.jadoth.collections.XGettingCollection#execute(com.xdev.jadoth.lang.functional.controlflow.TOperation)
	 */
	@Override
	public XGettingCollection<E> execute(final TOperation<E> operation)
	{
		return null;
	}

	/**
	 * @param ignoreMultipleNulls
	 * @return
	 * @see com.xdev.jadoth.collections.XGettingCollection#hasUniqueValues(boolean)
	 */
	@Override
	public boolean hasUniqueValues(final boolean ignoreMultipleNulls)
	{
		return false;
	}

	/**
	 * @param ignoreMultipleNulls
	 * @param comparator
	 * @return
	 * @see com.xdev.jadoth.collections.XGettingCollection#hasUniqueValues(boolean, com.xdev.jadoth.lang.Equalator)
	 */
	@Override
	public boolean hasUniqueValues(final boolean ignoreMultipleNulls, final Equalator<E> comparator)
	{
		return false;
	}

	/**
	 * @param <C>
	 * @param other
	 * @param comparator
	 * @param target
	 * @return
	 * @see com.xdev.jadoth.collections.XGettingCollection#intersect(com.xdev.jadoth.collections.XGettingCollection, com.xdev.jadoth.lang.Equalator, com.xdev.jadoth.collections.Collecting)
	 */
	@Override
	public <C extends Collecting<E>> C intersect(final XGettingCollection<E> other, final Equalator<E> comparator, final C target)
	{
		return null;
	}

	/**
	 * @param comparator
	 * @return
	 * @see com.xdev.jadoth.collections.XGettingCollection#max(java.util.Comparator)
	 */
	@Override
	public E max(final Comparator<E> comparator)
	{
		return null;
	}

	/**
	 * @param comparator
	 * @return
	 * @see com.xdev.jadoth.collections.XGettingCollection#min(java.util.Comparator)
	 */
	@Override
	public E min(final Comparator<E> comparator)
	{
		return null;
	}

	/**
	 * @param operation
	 * @return
	 * @see com.xdev.jadoth.collections.XGettingCollection#process(com.xdev.jadoth.lang.functional.Operation)
	 */
	@Override
	public XGettingCollection<E> process(final Operation<E> operation)
	{
		return null;
	}

	/**
	 * @param operation
	 * @return
	 * @see com.xdev.jadoth.collections.XGettingCollection#process(com.xdev.jadoth.lang.functional.controlflow.TOperation)
	 */
	@Override
	public XGettingCollection<E> process(final TOperation<E> operation)
	{
		return null;
	}

	/**
	 * @param element
	 * @param equalator
	 * @return
	 * @see com.xdev.jadoth.collections.XGettingCollection#search(java.lang.Object, com.xdev.jadoth.lang.Equalator)
	 */
	@Override
	public E search(final E element, final Equalator<E> equalator)
	{
		return null;
	}

	/**
	 * @param predicate
	 * @return
	 * @see com.xdev.jadoth.collections.XGettingCollection#search(com.xdev.jadoth.lang.functional.Predicate)
	 */
	@Override
	public E search(final Predicate<E> predicate)
	{
		return null;
	}

	/**
	 * @param predicate
	 * @return
	 * @see com.xdev.jadoth.collections.XGettingCollection#search(com.xdev.jadoth.lang.functional.controlflow.TPredicate)
	 */
	@Override
	public E search(final TPredicate<E> predicate)
	{
		return null;
	}

	/**
	 * @param type
	 * @return
	 * @see com.xdev.jadoth.collections.XGettingCollection#toArray(java.lang.Class)
	 */
	@Override
	public E[] toArray(final Class<E> type)
	{
		return null;
	}

	/**
	 * @param <C>
	 * @param other
	 * @param comparator
	 * @param target
	 * @return
	 * @see com.xdev.jadoth.collections.XGettingCollection#union(com.xdev.jadoth.collections.XGettingCollection, com.xdev.jadoth.lang.Equalator, com.xdev.jadoth.collections.Collecting)
	 */
	@Override
	public <C extends Collecting<E>> C union(final XGettingCollection<E> other, final Equalator<E> comparator, final C target)
	{
		return null;
	}
}
