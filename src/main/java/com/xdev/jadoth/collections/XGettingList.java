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
import java.util.RandomAccess;

import com.xdev.jadoth.lang.Equalator;
import com.xdev.jadoth.lang.functional.Operation;
import com.xdev.jadoth.lang.functional.Predicate;
import com.xdev.jadoth.lang.functional.aggregates.Aggregate;
import com.xdev.jadoth.lang.functional.aggregates.TAggregate;
import com.xdev.jadoth.lang.functional.controlflow.TOperation;
import com.xdev.jadoth.lang.functional.controlflow.TPredicate;
import com.xdev.jadoth.util.VarChar;


/**
 * @author TM
 *
 */
public interface XGettingList<E> extends RandomAccess, XGettingCollection<E>, List<E>
{
	// java.util.List Query Operations

	public int size();
	public boolean isEmpty();
	@Deprecated
	public boolean contains(Object o);
	public Iterator<E> iterator();
	public Object[] toArray();
	public <T> T[] toArray(T[] a);



	/**
	 * Throws an immediate {@link UnsupportedOperationException}.
	 * @param e
	 * @return
	 */
	public boolean add(E e);
	/**
	 * Throws an immediate {@link UnsupportedOperationException}.
	 * @param o
	 * @return
	 */
	public boolean remove(Object o);



	// java.util.List Bulk Modification Operations

	@Deprecated
	public boolean containsAll(Collection<?> c);
	/**
	 * Throws an immediate {@link UnsupportedOperationException}.
	 * @param c
	 * @return
	 */
	public boolean addAll(Collection<? extends E> c);
	/**
	 * Throws an immediate {@link UnsupportedOperationException}.
	 * @param index
	 * @param c
	 * @return
	 */
	public boolean addAll(int index, Collection<? extends E> c);
	/**
	 * Throws an immediate {@link UnsupportedOperationException}.
	 * @param c
	 * @return
	 */
	@Deprecated
	public boolean removeAll(Collection<?> c);
	/**
	 * Throws an immediate {@link UnsupportedOperationException}.
	 * @param c
	 * @return
	 */
	@Deprecated
	public boolean retainAll(Collection<?> c);
	/**
	 * Throws an immediate {@link UnsupportedOperationException}.
	 */
	public void clear();


	// java.util.List Positional Access Operations
	public E get(int index);
	/**
	 *
	 * @param index
	 * @param element
	 * @return
	 */
	public E set(int index, E element);
	/**
	 * Throws an immediate {@link UnsupportedOperationException}.
	 * @param index
	 * @param element
	 */
	public void add(int index, E element);
	/**
	 * Throws an immediate {@link UnsupportedOperationException}.
	 * @param index
	 * @return
	 */
	public E remove(int index);



	// java.util.List Search Operations
	@Deprecated
	public int indexOf(Object o);
	@Deprecated
	public int lastIndexOf(Object o);

	// java.util.List List Iterators
	public ListIterator<E> listIterator();
	public ListIterator<E> listIterator(int index);

	public SubListView<E> subList(int fromIndex, int toIndex);


	public interface Factory<E> extends XGettingCollection.Factory<E>
	{
		@Override
		public XGettingList<E> createInstance();
	}




	public E getFirst();
	public E getLast();

	public E rngMax(int startIndex, int endIndex, Comparator<E> comparator);
	public E rngMin(int startIndex, int endIndex, Comparator<E> comparator);

	public int maxIndex(Comparator<E> comparator);
	public int minIndex(Comparator<E> comparator);
	public int rngMaxIndex(int startIndex, int endIndex, Comparator<E> comparator);
	public int rngMinIndex(int startIndex, int endIndex, Comparator<E> comparator);



	public boolean rngContainsAll(int startIndex, int endIndex, Collection<E> c, boolean ignoreNulls, Equalator<E> equalator);
	public boolean rngContainsAll(int startIndex, int endIndex, Collection<?> c, boolean ignoreNulls);

	public int indexOfId(E element);
	public int indexOf  (E element, Equalator<E> equalator);
	public int rngIndexOf  (int startIndex, int endIndex, E element, Equalator<E> equalator);
	public int rngIndexOfId(int startIndex, int endIndex, E element);

	public int lastIndexOfId(E element);
	public int lastIndexOf  (E element, Equalator<E> equalator);

	public int rngCount(int startIndex, int endIndex, E element, Equalator<E> equalator);
	public int rngCount(int startIndex, int endIndex, E element);

	public boolean isSorted(Comparator<E> comparator);
	public boolean rngIsSorted(int startIndex, int endIndex, Comparator<E> comparator);

	/**
	 * Only works if the list is ordered according to the passed comparator.
	 *
	 * @see #isOrdered(Comparator)
	 * @param element
	 * @param comparator
	 * @return
	 */
	public int binarySearch(E element, Comparator<E> comparator);
	public int rngBinarySearch(int startIndex, int endIndex, E element, Comparator<E> comparator);

	public boolean rngHasUniqueValues(int startIndex, int endIndex, boolean ignoreMultipleNulls);
	public boolean rngHasUniqueValues(int startIndex, int endIndex, boolean ignoreMultipleNulls, Equalator<E> comparator);

	/**
	 * Creates a true copy of this list which references th same elements in the same order as this list does
	 * at the time the method is called. The elements themselves are NOT copied (no deep copying).<br>
	 * The type of the returned list is the same as of this list if possible (i.e.: a SubList can not meaningful
	 * return a true copy that references its elements but still is a SubList)
	 *
	 * @return a copy of this list
	 */
	public XList<E> copy();
	public XList<E> toReversed();
	public <L extends Collecting<E>> L copyRange(int startIndex, int endIndex, L targetCollection);

	public <L extends Collecting<E>> L rngCopyTo(int startIndex, int endIndex, L targetCollection,  Predicate<E> predicate);
	public <L extends Collecting<E>> L rngCopyTo(int startIndex, int endIndex, L targetCollection, TPredicate<E> predicate);
	public <L extends Collecting<E>> L rngCopyTo(int startIndex, int endIndex, L targetCollection,  Predicate<E> predicate, int skip, Integer limit);
	public <L extends Collecting<E>> L rngCopyTo(int startIndex, int endIndex, L targetCollection, TPredicate<E> predicate, int skip, Integer limit);

	public XList<E> rngExecute(int startIndex, int endIndex,  Operation<E> operation);
	public XList<E> rngExecute(int startIndex, int endIndex, TOperation<E> operation);

	public XList<E> rngProcess(int startIndex, int endIndex,  Operation<E> operation);
	public XList<E> rngProcess(int startIndex, int endIndex, TOperation<E> operation);

	public E rngSearch(int startIndex, int endIndex, E element, Equalator<E> equalator);

	public boolean rngContainsId(int startIndex, int endIndex, E o);
	public boolean rngContains(int startIndex, int endIndex,  Predicate<E> predicate);
	public boolean rngContains(int startIndex, int endIndex, TPredicate<E> predicate);
	public boolean rngContains(int startIndex, int endIndex, E element, Equalator<E> comparator);

	//true if predicate applies to ALL element
	public boolean rngApplies(int startIndex, int endIndex,  Predicate<E> predicate);
	public boolean rngApplies(int startIndex, int endIndex, TPredicate<E> predicate);

	public int rngCount(int startIndex, int endIndex,  Predicate<E> predicate);
	public int rngCount(int startIndex, int endIndex, TPredicate<E> predicate);

	public int    indexOf( Predicate<E> predicate);
	public int    indexOf(TPredicate<E> predicate);
	public int rngIndexOf(int startIndex, int endIndex,  Predicate<E> predicate);
	public int rngIndexOf(int startIndex, int endIndex, TPredicate<E> predicate);

	public int    scan(Predicate<E> predicate);
	public int    scan(TPredicate<E> predicate);
	public int rngScan(int startIndex, int endIndex,  Predicate<E> predicate);
	public int rngScan(int startIndex, int endIndex, TPredicate<E> predicate);

	public E rngSearch(int startIndex, int endIndex,  Predicate<E> predicate);
	public E rngSearch(int startIndex, int endIndex, TPredicate<E> predicate);

	public E[]      rngToArray(int startIndex, int endIndex, Class<E> type);
	public Object[] rngToArray(int startIndex, int endIndex);
	public <T>  T[] rngToArray(int startIndex, int endIndex, T[] a);

	public boolean rngEqualsContent(int startIndex, List<E> list, int length, Equalator<E> comparator);


	public <R> R rngAggregate(int startIndex, int endIndex,  Aggregate<E, R> aggregate);
	public <R> R rngAggregate(int startIndex, int endIndex, TAggregate<E, R> aggregate);

	public void rngAppendTo(int startIndex, int endIndex, VarChar vc);
	public void rngAppendTo(int startIndex, int endIndex, VarChar vc, String seperator);
	public void rngAppendTo(int startIndex, int endIndex, VarChar vc, char seperator);

	public <C extends Collecting<E>> C rngIntersect(
		int startIndex, int endIndex, XGettingCollection<E> c, Equalator<E> comparator, C targetCollection
	);
	public <C extends Collecting<E>> C rngUnion(
		int startIndex, int endIndex, XGettingCollection<E> c, Equalator<E> comparatorr, C targetCollection
	);
	public <C extends Collecting<E>> C rngExcept(
		int startIndex, int endIndex, XGettingCollection<E> c, Equalator<E> comparatorr, C targetCollection
	);

}
