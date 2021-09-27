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

import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import com.xdev.jadoth.lang.Equalator;
import com.xdev.jadoth.lang.functional.Operation;
import com.xdev.jadoth.lang.functional.Predicate;
import com.xdev.jadoth.lang.functional.aggregates.Aggregate;
import com.xdev.jadoth.lang.functional.aggregates.TAggregate;
import com.xdev.jadoth.lang.functional.controlflow.TOperation;
import com.xdev.jadoth.lang.functional.controlflow.TPredicate;
import com.xdev.jadoth.util.VarChar;


/**
 * Public implementation of the Java Collection SubList concept for {@link XList} instances.<br>
 * See {@link List#subList(int, int)}
 *
 * @author Thomas Muenz
 *
 */
public class SubList<E> extends SubListAccessor<E> implements XList<E>
{
	/*
	 * Three variants:
	 * SubListReadOnlyView implements XGettingList
	 * SubListView extends SubListReadOnlyView implements XSettingList
	 * SubList extends SubListView implements XList
	 */

	/*
	 * Write mini parser and generator to generate appropriate delegation methods
	 */
	// example for adding index delegation
//	add() -> return this.insert(this.offset+this.size, c);

	//example for rng~ method delegation
//	return this.rangeCheck(startIndex, endIndex)
//	?this.list.idRngReplace(this.offset + startIndex, this.offset + endIndex - 1, oldElement, newElement)
//	:this.list.idRngReplace(this.offset + startIndex - 1, this.offset + endIndex, oldElement, newElement)
//	;

	// beware size modification methods!
	// beware special case methods (like truncate())!


	private final XList<E> list;
	private final int offset;
	private int size;

	public SubList(final XList<E> list, final int startIndex, final int endIndex)
	{
		if(startIndex < 0 || endIndex >= list.size() || startIndex > endIndex){
			throw new IndexOutOfBoundsException("Range ["+startIndex+';'+endIndex+"] not in [0;"+list.size()+"].");
		}
		this.list = list;
		this.offset = startIndex;
		this.size = endIndex - startIndex + 1;
	}


	private void rangeCheck(final int index)
	{
		if (index < 0 || index >= this.size){
			throw new IndexOutOfBoundsException("Index: "+index+",Size: "+this.size);
		}
	}

	private boolean rangeCheck(final int startIndex, final int endIndex)
	{
		if(startIndex < 0 || endIndex >= this.size || startIndex > endIndex){
			throw new IndexOutOfBoundsException("Range ["+startIndex+';'+endIndex+"] not in [0;"+this.size+"].");
		}
		return startIndex <= endIndex;
	}


	/**
	 * @param oldElement
	 * @param newElement
	 * @return
	 */
	@Override
	public int replace(final E oldElement, final E newElement, final Equalator<E> equalator)
	{
		return 0;
	}


	/**
	 * @param oldElement
	 * @param newElement
	 * @param limit
	 * @return
	 */
	@Override
	public int replace(final E oldElement, final E newElement,final int skip, final Integer limit, final Equalator<E> equalator)
	{
		return 0;
	}


	/**
	 * @param oldElement
	 * @param newElement
	 * @return
	 */
	@Override
	public int replaceOne(final E oldElement, final E newElement, final Equalator<E> equalator)
	{
		return 0;
	}


	/**
	 * @param oldElement
	 * @param newElement
	 * @return
	 */
	@Override
	public int replace(final E oldElement, final E newElement)
	{
		return 0;
	}


	/**
	 * @param oldElement
	 * @param newElement
	 * @param limit
	 * @return
	 */
	@Override
	public int replace(final E oldElement, final E newElement,final int skip, final Integer limit)
	{
		return 0;
	}


	/**
	 * @param oldElement
	 * @param newElement
	 * @return
	 */
	@Override
	public int replaceOne(final E oldElement, final E newElement)
	{
		return 0;
	}


	/**
	 * @param replacementMapping
	 * @return
	 */
	@Override
	public int replaceAll(final Map<E, E> replacementMapping)
	{
		return 0;
	}


	/**
	 * @param vc
	 */
	@Override
	public void appendTo(final VarChar vc)
	{

	}


	/**
	 * @param vc
	 * @param seperator
	 */
	@Override
	public void appendTo(final VarChar vc, final char seperator)
	{

	}


	/**
	 * @param vc
	 * @param seperator
	 */
	@Override
	public void appendTo(final VarChar vc, final String seperator)
	{

	}


	/**
	 * @param predicate
	 * @return
	 */
	@Override
	public boolean applies(final Predicate<E> predicate)
	{
		return false;
	}


	/**
	 * @param predicate
	 * @return
	 */
	@Override
	public boolean applies(final TPredicate<E> predicate)
	{
		return false;
	}


	/**
	 *
	 */
	@Override
	public void clear()
	{

	}


	/**
	 * @param predicate
	 * @return
	 */
	@Override
	public boolean contains(final Predicate<E> predicate)
	{
		return false;
	}


	/**
	 * @param predicate
	 * @return
	 */
	@Override
	public boolean contains(final TPredicate<E> predicate)
	{
		return false;
	}


	/**
	 * @param element
	 * @param equalator
	 * @return
	 */
	@Override
	public boolean contains(final E element, final Equalator<E> equalator)
	{
		return false;
	}


	/**
	 * @param predicate
	 * @return
	 */
	@Override
	public int count(final Predicate<E> predicate)
	{
		return 0;
	}


	/**
	 * @param predicate
	 * @return
	 */
	@Override
	public int count(final TPredicate<E> predicate)
	{
		return 0;
	}




	/**
	 * @param element
	 * @return
	 */
	@Override
	public int count(final E element, final Equalator<E> equalator)
	{
		return 0;
	}


	/**
	 * @param element
	 * @return
	 */
	@Override
	public E search(final E element, final Equalator<E> equalator)
	{
		return null;
	}


	/**
	 * @param list
	 * @param comparator
	 * @return
	 */
	@Override
	public boolean equals(final XGettingCollection<E> list, final Equalator<E> comparator)
	{
		return false;
	}


	/**
	 * @param list
	 * @param comparator
	 * @return
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
	 */
	@Override
	public <C extends Collecting<E>> C except(final XGettingCollection<E> other, final Equalator<E> comparator, final C target)
	{
		return null;
	}


	/**
	 * @param operation
	 * @return
	 */
	@Override
	public SubList<E> execute(final Operation<E> operation)
	{
		return null;
	}


	/**
	 * @param operation
	 * @return
	 */
	@Override
	public SubList<E> execute(final TOperation<E> operation)
	{
		return null;
	}


	/**
	 * @param comparator
	 * @return
	 */
	@Override
	public boolean hasUniqueValues(final boolean ignoreMultipleNulls, final Equalator<E> comparator)
	{
		return false;
	}


	/**
	 * @param element
	 * @return
	 */
	@Override
	public boolean containsId(final E element)
	{
		return false;
	}


	/**
	 * @param element
	 * @return
	 */
	@Override
	public int count(final E element)
	{
		return 0;
	}


	/**
	 * @param ignoreMultipleNullValues
	 * @return
	 */
	@Override
	public boolean hasUniqueValues(final boolean ignoreMultipleNullValues)
	{
		return false;
	}


	/**
	 * @param <C>
	 * @param other
	 * @param comparator
	 * @param target
	 * @return
	 */
	@Override
	public <C extends Collecting<E>> C intersect(final XGettingCollection<E> other, final Equalator<E> comparator, final C target)
	{
		return null;
	}


	/**
	 * @return
	 */
	@Override
	public boolean isEmpty()
	{
		return false;
	}


	/**
	 * @param comparator
	 * @return
	 */
	@Override
	public E max(final Comparator<E> comparator)
	{
		return null;
	}


	/**
	 * @param comparator
	 * @return
	 */
	@Override
	public E min(final Comparator<E> comparator)
	{
		return null;
	}


	/**
	 * @param operation
	 * @return
	 */
	@Override
	public SubList<E> process(final Operation<E> operation)
	{
		return null;
	}


	/**
	 * @param operation
	 * @return
	 */
	@Override
	public SubList<E> process(final TOperation<E> operation)
	{
		return null;
	}


	/**
	 * @param predicate
	 * @return
	 */
	@Override
	public E search(final Predicate<E> predicate)
	{
		return null;
	}


	/**
	 * @param predicate
	 * @return
	 */
	@Override
	public E search(final TPredicate<E> predicate)
	{
		return null;
	}


	/**
	 * @return
	 */
	@Override
	public int size()
	{
		return 0;
	}


	/**
	 * @return
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
	 */
	@Override
	public <T> T[] toArray(final T[] a)
	{
		return null;
	}


	/**
	 * @param type
	 * @return
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
	 */
	@Override
	public <C extends Collecting<E>> C union(final XGettingCollection<E> other, final Equalator<E> comparator, final C target)
	{
		return null;
	}


	/**
	 * @return
	 */
	@Override
	public Iterator<E> iterator()
	{
		return null;
	}


	/**
	 * @param e
	 * @return
	 */
	@Override
	public boolean add(final E e)
	{
		return false;
	}


	/**
	 * @param elements
	 * @return
	 */
	@Override
	public XAddingCollection<E> add(final E... elements)
	{
		return null;
	}


	/**
	 * @param elements
	 * @return
	 */
	@Override
	public XAddingCollection<E> add(final Iterable<? extends E> elements)
	{
		return null;
	}


	/**
	 * @param elements
	 * @param srcIndex
	 * @param srcLength
	 * @param predicate
	 * @param limit
	 * @return
	 */
	@Override
	public XAddingCollection<E> add(final E[] elements, final int srcIndex, final int srcLength, final Predicate<E> predicate, final int skip, final Integer limit)
	{
		return null;
	}


	/**
	 * @param elements
	 * @param srcIndex
	 * @param srcLength
	 * @param predicate
	 * @param limit
	 * @return
	 */
	@Override
	public XAddingCollection<E> add(final Iterable<? extends E> elements, final int srcIndex, final Integer srcLength, final Predicate<E> predicate, final int skip, final Integer limit)
	{
		return null;
	}


	/**
	 * @param elements
	 * @param srcIndex
	 * @param srcLength
	 * @param predicate
	 * @param limit
	 * @return
	 */
	@Override
	public XAddingCollection<E> add(final E[] elements, final int srcIndex, final int srcLength, final TPredicate<E> predicate, final int skip, final Integer limit)
	{
		return null;
	}


	/**
	 * @param elements
	 * @param srcIndex
	 * @param srcLength
	 * @param predicate
	 * @param limit
	 * @return
	 */
	@Override
	public XAddingCollection<E> add(final Iterable<? extends E> elements, final int srcIndex, final Integer srcLength, final TPredicate<E> predicate, final int skip, final Integer limit)
	{
		return null;
	}


	/**
	 * @param c
	 * @return
	 */
	@Override
	public boolean addAll(final Collection<? extends E> c)
	{
		return false;
	}


	/**
	 * @param element
	 * @return
	 */
	@Override
	public int remove(final E element, final Equalator<E> equalator)
	{
		return 0;
	}


	/**
	 * @param element
	 * @param limit
	 * @return
	 */
	@Override
	public int remove(final E element,final int skip, final Integer limit, final Equalator<E> equalator)
	{
		return 0;
	}


	/**
	 * @param c
	 * @param ingoreNulls
	 * @return
	 */
	@Override
	public int removeAll(final XCollection<E> c, final boolean ingoreNulls, final Equalator<E> equalator)
	{
		return 0;
	}


	/**
	 * @param keepMultipleNullValues
	 * @return
	 */
	@Override
	public int removeDuplicates(final boolean keepMultipleNullValues, final Equalator<E> equalator)
	{
		return 0;
	}


	/**
	 * @param element
	 * @return
	 */
	@Override
	public E removeOne(final E element, final Equalator<E> equalator)
	{
		return null;
	}


	/**
	 * @param c
	 * @return
	 */
	@Override
	public int retainAll(final XCollection<E> c, final Equalator<E> equalator)
	{
		return 0;
	}



	/**
	 * @param element
	 * @return
	 */
	@Override
	public int removeId(final E element)
	{
		return 0;
	}


	/**
	 * @param element
	 * @param limit
	 * @return
	 */
	@Override
	public int removeId(final E element,final int skip, final Integer limit)
	{
		return 0;
	}


	/**
	 * @param c
	 * @param ingoreNulls
	 * @return
	 */
	@Override
	public int removeAllId(final XCollection<E> c, final boolean ingoreNulls)
	{
		return 0;
	}


	/**
	 * @param keepMultipleNullValues
	 * @return
	 */
	@Override
	public int removeDuplicates(final boolean keepMultipleNullValues)
	{
		return 0;
	}


	/**
	 * @param element
	 * @return
	 */
	@Override
	public E removeOne(final E element)
	{
		return null;
	}


	/**
	 * @param c
	 * @return
	 */
	@Override
	public int retainAllId(final XCollection<E> c)
	{
		return 0;
	}


	/**
	 * @param predicate
	 * @return
	 */
	@Override
	public int reduce(final Predicate<E> predicate)
	{
		return 0;
	}


	/**
	 * @param predicate
	 * @return
	 */
	@Override
	public int reduce(final TPredicate<E> predicate)
	{
		return 0;
	}


	/**
	 * @param predicate
	 * @param limit
	 * @return
	 */
	@Override
	public int reduce(final Predicate<E> predicate,final int skip, final Integer limit)
	{
		return 0;
	}


	/**
	 * @param predicate
	 * @param limit
	 * @return
	 */
	@Override
	public int reduce(final TPredicate<E> predicate,final int skip, final Integer limit)
	{
		return 0;
	}



	/**
	 * @return
	 */
	@Override
	public XList<E> truncate()
	{
		return null;
	}


	/**
	 * @param startIndex
	 * @param endIndex
	 * @param oldElement
	 * @param newElement
	 * @return
	 */
	@Override
	public int rngReplace(
		final int startIndex,
		final int endIndex,
		final E oldElement,
		final E newElement,
		final Equalator<E> equalator
	)
	{
		return 0;
	}


	/**
	 * @param startIndex
	 * @param endIndex
	 * @param oldElement
	 * @param newElement
	 * @param limit
	 * @return
	 */
	@Override
	public int rngReplace(
		final int startIndex,
		final int endIndex,
		final E oldElement,
		final E newElement,
		final int skip,
		final Integer limit,
		final Equalator<E> equalator
		)
	{
		return 0;
	}


	/**
	 * @param startIndex
	 * @param endIndex
	 * @param oldElement
	 * @param newElement
	 * @return
	 */
	@Override
	public int rngReplaceOne(
		final int startIndex,
		final int endIndex,
		final E oldElement,
		final E newElement,
		final Equalator<E> equalator
	)
	{
		return 0;
	}


	/**
	 * @param startIndex
	 * @param endIndex
	 * @param element
	 * @return
	 */
	@Override
	public XList<E> fill(final int startIndex, final int endIndex, final E element)
	{
		return null;
	}


	/**
	 * @param startIndex
	 * @param endIndex
	 * @param oldElement
	 * @param newElement
	 * @return
	 */
	@Override
	public int rngReplace(final int startIndex, final int endIndex, final E oldElement, final E newElement)
	{
		return 0;
	}


	/**
	 * @param startIndex
	 * @param endIndex
	 * @param oldElement
	 * @param newElement
	 * @param limit
	 * @return
	 */
	@Override
	public int rngReplace(final int startIndex, final int endIndex, final E oldElement, final E newElement,final int skip, final Integer limit)
	{
		return 0;
	}


	/**
	 * @param startIndex
	 * @param endIndex
	 * @param oldElement
	 * @param newElement
	 * @return
	 */
	@Override
	public int rngReplaceOne(final int startIndex, final int endIndex, final E oldElement, final E newElement)
	{
		return 0;
	}


	/**
	 * @return
	 */
	@Override
	public XList<E> reverse()
	{
		return null;
	}


	/**
	 * @param startIndex
	 * @param endIndex
	 * @param replacementMapping
	 * @return
	 */
	@Override
	public int rngReplaceAll(final int startIndex, final int endIndex, final Map<E, E> replacementMapping)
	{
		return 0;
	}


	/**
	 * @param startIndex
	 * @param endIndex
	 * @return
	 */
	@Override
	public XList<E> rngReverse(final int startIndex, final int endIndex)
	{
		return null;
	}


	/**
	 * @param startIndex
	 * @param endIndex
	 * @return
	 */
	@Override
	public XList<E> rngShuffle(final int startIndex, final int endIndex)
	{
		return null;
	}


	/**
	 * @param startIndex
	 * @param endIndex
	 * @param comparator
	 * @return
	 */
	@Override
	public XList<E> rngSortMerge(final int startIndex, final int endIndex, final Comparator<E> comparator)
	{
		return null;
	}


	/**
	 * @param startIndex
	 * @param endIndex
	 * @param comparator
	 * @return
	 */
	@Override
	public XList<E> rngSortQuick(final int startIndex, final int endIndex, final Comparator<E> comparator)
	{
		return null;
	}


	/**
	 * @param index
	 * @param element
	 * @return
	 */
	@Override
	public E set(final int index, final E element)
	{
		return null;
	}


	/**
	 * @param startIndex
	 * @param elements
	 * @return
	 */
	@Override
	public XList<E> set(final int startIndex, final E... elements)
	{
		return null;
	}


	/**
	 * @param startIndex
	 * @param elements
	 * @param elementsStartIndex
	 * @param length
	 * @return
	 */
	@Override
	public XList<E> set(final int startIndex, final E[] elements, final int elementsStartIndex, final int length)
	{
		return null;
	}


	/**
	 * @param startIndex
	 * @param elements
	 * @param elementsStartIndex
	 * @param length
	 * @return
	 */
	@Override
	public XList<E> set(final int startIndex, final List<E> elements, final int elementsStartIndex, final int length)
	{
		return null;
	}


	/**
	 * @param element
	 */
	@Override
	public void setFirst(final E element)
	{

	}


	/**
	 * @param element
	 */
	@Override
	public void setLast(final E element)
	{

	}


	/**
	 * @return
	 */
	@Override
	public XList<E> shuffle()
	{
		return null;
	}


	/**
	 * @param comparator
	 * @return
	 */
	@Override
	public XList<E> sortMerge(final Comparator<E> comparator)
	{
		return null;
	}


	/**
	 * @param comparator
	 * @return
	 */
	@Override
	public XList<E> sortQuick(final Comparator<E> comparator)
	{
		return null;
	}


	/**
	 * @param indexA
	 * @param indexB
	 */
	@Override
	public void swap(final int indexA, final int indexB)
	{

	}


	/**
	 * @param indexA
	 * @param indexB
	 * @param length
	 */
	@Override
	public void swap(final int indexA, final int indexB, final int length)
	{

	}


	/**
	 * @param element
	 * @param comparator
	 * @return
	 */
	@Override
	public int binarySearch(final E element, final Comparator<E> comparator)
	{
		return 0;
	}


	/**
	 * @return
	 */
	@Override
	public XList<E> copy()
	{
		return null;
	}


	/**
	 * @param o
	 * @return
	 */
	@Override
	public int indexOf(final E o, final Equalator<E> equalator)
	{
		return 0;
	}


	/**
	 * @param o
	 * @return
	 */
	@Override
	public int lastIndexOf(final E o, final Equalator<E> equalator)
	{
		return 0;
	}




	/**
	 * @param startIndex
	 * @param endIndex
	 * @param c
	 * @param ignoreNulls
	 * @return
	 */
	@Override
	public boolean rngContainsAll(final int startIndex, final int endIndex, final Collection<E> c, final boolean ignoreNulls, final Equalator<E> equalator)
	{
		return false;
	}


	/**
	 * @param startIndex
	 * @param endIndex
	 * @param element
	 * @return
	 */
	@Override
	public int rngCount(final int startIndex, final int endIndex, final E element, final Equalator<E> equalator)
	{
		return 0;
	}



	/**
	 * @param startIndex
	 * @param endIndex
	 * @param o
	 * @return
	 */
	@Override
	public int rngIndexOf(final int startIndex, final int endIndex, final E o, final Equalator<E> equalator)
	{
		return 0;
	}


	/**
	 * @param index
	 * @return
	 */
	@Override
	public E get(final int index)
	{
		return null;
	}


	/**
	 * @return
	 */
	@Override
	public E getFirst()
	{
		return null;
	}


	/**
	 * @return
	 */
	@Override
	public E getLast()
	{
		return null;
	}


	/**
	 * @param o
	 * @return
	 */
	@Override
	public int indexOfId(final E o)
	{
		return 0;
	}


	/**
	 * @param o
	 * @return
	 */
	@Override
	public int lastIndexOfId(final E o)
	{
		return 0;
	}


	/**
	 * @param startIndex
	 * @param endIndex
	 * @param o
	 * @return
	 */
	@Override
	public boolean rngContainsId(final int startIndex, final int endIndex, final E o)
	{
		return false;
	}


	/**
	 * @param startIndex
	 * @param endIndex
	 * @param c
	 * @param ignoreNulls
	 * @return
	 */
	@Override
	public boolean rngContainsAll(final int startIndex, final int endIndex, final Collection<?> c, final boolean ignoreNulls)
	{
		return false;
	}


	/**
	 * @param startIndex
	 * @param endIndex
	 * @param element
	 * @return
	 */
	@Override
	public int rngCount(final int startIndex, final int endIndex, final E element)
	{
		return 0;
	}


	/**
	 * @param startIndex
	 * @param endIndex
	 * @param ignoreMultipleNullValues
	 * @return
	 */
	@Override
	public boolean rngHasUniqueValues(final int startIndex, final int endIndex, final boolean ignoreMultipleNullValues)
	{
		return false;
	}


	/**
	 * @param startIndex
	 * @param endIndex
	 * @param o
	 * @return
	 */
	@Override
	public int rngIndexOfId(final int startIndex, final int endIndex, final E o)
	{
		return 0;
	}


	/**
	 * @param predicate
	 * @return
	 */
	@Override
	public int indexOf(final Predicate<E> predicate)
	{
		return 0;
	}


	/**
	 * @param predicate
	 * @return
	 */
	@Override
	public int indexOf(final TPredicate<E> predicate)
	{
		return 0;
	}


	/**
	 * @param comparator
	 * @return
	 */
	@Override
	public boolean isSorted(final Comparator<E> comparator)
	{
		return false;
	}


	/**
	 * @return
	 */
	@Override
	public ListIterator<E> listIterator()
	{
		return null;
	}


	/**
	 * @param i
	 * @return
	 */
	@Override
	public ListIterator<E> listIterator(final int i)
	{
		return null;
	}


	/**
	 * @param comparator
	 * @return
	 */
	@Override
	public int maxIndex(final Comparator<E> comparator)
	{
		return 0;
	}


	/**
	 * @param comparator
	 * @return
	 */
	@Override
	public int minIndex(final Comparator<E> comparator)
	{
		return 0;
	}


	/**
	 * @param startIndex
	 * @param endIndex
	 * @param vc
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
	 */
	@Override
	public boolean rngContains(final int startIndex, final int endIndex, final E element, final Equalator<E> comparator)
	{
		return false;
	}


	/**
	 * @param startIndex
	 * @param endIndex
	 * @param predicate
	 * @return
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
	 */
	@Override
	public int rngCount(final int startIndex, final int endIndex, final TPredicate<E> predicate)
	{
		return 0;
	}


	/**
	 * @param startIndex
	 * @param endIndex
	 * @param element
	 * @return
	 */
	@Override
	public E rngSearch(final int startIndex, final int endIndex, final E element, final Equalator<E> equalator)
	{
		return null;
	}


	/**
	 * @param startIndex
	 * @param list
	 * @param length
	 * @param comparator
	 * @return
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
	 */
	@Override
	public XList<E> rngExecute(final int startIndex, final int endIndex, final TOperation<E> operation)
	{
		return null;
	}


	/**
	 * @param startIndex
	 * @param endIndex
	 * @param comparator
	 * @return
	 */
	@Override
	public boolean rngHasUniqueValues(final int startIndex, final int endIndex, final boolean ignoreMultipleNulls, final Equalator<E> comparator)
	{
		return false;
	}


	/**
	 * @param startIndex
	 * @param endIndex
	 * @param predicate
	 * @return
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
	 */
	@Override
	public int rngIndexOf(final int startIndex, final int endIndex, final TPredicate<E> predicate)
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
	 */
	@Override
	public int rngScan(final int startIndex, final int endIndex, final TPredicate<E> predicate)
	{
		return 0;
	}


	/**
	 * @param startIndex
	 * @param endIndex
	 * @param predicate
	 * @return
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
	 */
	@Override
	public <C extends Collecting<E>> C rngUnion(final int startIndex, final int endIndex, final XGettingCollection<E> c, final Equalator<E> comparatorr, final C targetCollection)
	{
		return null;
	}


	/**
	 * @param predicate
	 * @return
	 */
	@Override
	public int scan(final Predicate<E> predicate)
	{
		return 0;
	}


	/**
	 * @param predicate
	 * @return
	 */
	@Override
	public int scan(final TPredicate<E> predicate)
	{
		return 0;
	}


	/**
	 * @param fromIndex
	 * @param toIndex
	 * @return
	 */
	@Override
	public SubList<E> subList(final int fromIndex, final int toIndex)
	{
		return null;
	}


	/**
	 * @return
	 */
	@Override
	public XList<E> toReversed()
	{
		return null;
	}


	/**
	 * @param index
	 * @param element
	 * @return
	 */
	@Override
	public XInsertingList<E> insert(final int index, final E element)
	{
		return null;
	}


	/**
	 * @param index
	 * @param elements
	 * @return
	 */
	@Override
	public XInsertingList<E> insert(final int index, final E... elements)
	{
		return null;
	}


	/**
	 * @param index
	 * @param elements
	 * @return
	 */
	@Override
	public XInsertingList<E> insert(final int index, final Iterable<? extends E> elements)
	{
		return null;
	}


	/**
	 * @param index
	 * @param elements
	 * @param srcIndex
	 * @param srcLength
	 * @param predicate
	 * @param limit
	 * @return
	 */
	@Override
	public XInsertingList<E> insert(final int index, final E[] elements, final int srcIndex, final int srcLength, final Predicate<E> predicate, final int skip, final Integer limit)
	{
		return null;
	}


	/**
	 * @param index
	 * @param elements
	 * @param srcIndex
	 * @param srcLength
	 * @param predicate
	 * @param limit
	 * @return
	 */
	@Override
	public XInsertingList<E> insert(final int index, final Iterable<? extends E> elements, final int srcIndex, final Integer srcLength, final Predicate<E> predicate, final int skip, final Integer limit)
	{
		return null;
	}


	/**
	 * @param index
	 * @param elements
	 * @param srcIndex
	 * @param srcLength
	 * @param predicate
	 * @param limit
	 * @return
	 */
	@Override
	public XInsertingList<E> insert(final int index, final E[] elements, final int srcIndex, final int srcLength, final TPredicate<E> predicate, final int skip, final Integer limit)
	{
		return null;
	}


	/**
	 * @param index
	 * @param elements
	 * @param srcIndex
	 * @param srcLength
	 * @param predicate
	 * @param limit
	 * @return
	 */
	@Override
	public XInsertingList<E> insert(final int index, final Iterable<? extends E> elements, final int srcIndex, final Integer srcLength, final TPredicate<E> predicate, final int skip, final Integer limit)
	{
		return null;
	}


	/**
	 * @param element
	 * @return
	 */
	@Override
	public XInsertingList<E> prepend(final E element)
	{
		return null;
	}


	/**
	 * @param startIndex
	 * @param endIndex
	 * @param element
	 * @return
	 */
	@Override
	public int rngRemove(final int startIndex, final int endIndex, final E element, final Equalator<E> equalator)
	{
		return 0;
	}


	/**
	 * @param startIndex
	 * @param endIndex
	 * @param element
	 * @param limit
	 * @return
	 */
	@Override
	public int rngRemove(final int startIndex, final int endIndex, final E element, final Equalator<E> equalator,final int skip, final Integer limit)
	{
		return 0;
	}


	/**
	 * @param startIndex
	 * @param endIndex
	 * @param c
	 * @param ignoreNulls
	 * @return
	 */
	@Override
	public int rngRemoveAll(final int startIndex, final int endIndex, final XCollection<? super E> c, final boolean ignoreNulls, final Equalator<E> equalator)
	{
		return 0;
	}


	/**
	 * @param startIndex
	 * @param endIndex
	 * @param keepMultipleNulls
	 * @return
	 */
	@Override
	public int rngRemoveDuplicates(final int startIndex, final int endIndex, final boolean keepMultipleNulls, final Equalator<E> equalator)
	{
		return 0;
	}


	/**
	 * @param startIndex
	 * @param endIndex
	 * @param o
	 * @return
	 */
	@Override
	public E rngRemoveOne(final int startIndex, final int endIndex, final E o, final Equalator<E> equalator)
	{
		return null;
	}


	/**
	 * @param startIndex
	 * @param endIndex
	 * @param c
	 * @return
	 */
	@Override
	public int rngRetainAll(final int startIndex, final int endIndex, final XCollection<E> c, final Equalator<E> equalator)
	{
		return 0;
	}


	/**
	 * @param startIndex
	 * @param endIndex
	 * @param element
	 * @return
	 */
	@Override
	public int rngRemove(final int startIndex, final int endIndex, final E element)
	{
		return 0;
	}


	/**
	 * @param startIndex
	 * @param endIndex
	 * @param element
	 * @param limit
	 * @return
	 */
	@Override
	public int rngRemove(final int startIndex, final int endIndex, final E element,final int skip, final Integer limit)
	{
		return 0;
	}


	/**
	 * @param startIndex
	 * @param endIndex
	 * @param c
	 * @param ignoreNulls
	 * @return
	 */
	@Override
	public int rngRemoveAll(final int startIndex, final int endIndex, final XCollection<? super E> c, final boolean ignoreNulls)
	{
		return 0;
	}


	/**
	 * @param startIndex
	 * @param endIndex
	 * @param keepMultipleNulls
	 * @return
	 */
	@Override
	public int rngRemoveDuplicates(final int startIndex, final int endIndex, final boolean keepMultipleNulls)
	{
		return 0;
	}


	/**
	 * @param startIndex
	 * @param endIndex
	 * @param o
	 * @return
	 */
	@Override
	public E rngRemoveOne(final int startIndex, final int endIndex, final E o)
	{
		return null;
	}


	/**
	 * @param startIndex
	 * @param endIndex
	 * @param c
	 * @return
	 */
	@Override
	public int rngRetainAll(final int startIndex, final int endIndex, final XCollection<E> c)
	{
		return 0;
	}


	/**
	 * @param index
	 * @return
	 */
	@Override
	public E remove(final int index)
	{
		return null;
	}


	/**
	 * @return
	 */
	@Override
	public E removeFirst()
	{
		return null;
	}


	/**
	 * @return
	 */
	@Override
	public E removeLast()
	{
		return null;
	}


	/**
	 * @param startIndex
	 * @param endIndex
	 * @return
	 */
	@Override
	public XList<E> removeRange(final int startIndex, final int endIndex)
	{
		return null;
	}


	/**
	 * @param startIndex
	 * @param endIndex
	 * @param predicate
	 * @return
	 */
	@Override
	public int rngReduce(final int startIndex, final int endIndex, final Predicate<E> predicate)
	{
		return 0;
	}


	/**
	 * @param startIndex
	 * @param endIndex
	 * @param predicate
	 * @return
	 */
	@Override
	public int rngReduce(final int startIndex, final int endIndex, final TPredicate<E> predicate)
	{
		return 0;
	}


	/**
	 * @param startIndex
	 * @param endIndex
	 * @param predicate
	 * @param limit
	 * @return
	 */
	@Override
	public int rngReduce(final int startIndex, final int endIndex, final Predicate<E> predicate,final int skip, final Integer limit)
	{
		return 0;
	}


	/**
	 * @param startIndex
	 * @param endIndex
	 * @param predicate
	 * @param limit
	 * @return
	 */
	@Override
	public int rngReduce(final int startIndex, final int endIndex, final TPredicate<E> predicate,final int skip, final Integer limit)
	{
		return 0;
	}




	/**
	 * @param index
	 * @param element
	 */
	@Override
	public void add(final int index, final E element)
	{

	}


	/**
	 * @param index
	 * @param c
	 * @return
	 */
	@Override
	public boolean addAll(final int index, final Collection<? extends E> c)
	{
		return false;
	}


	/**
	 * @param o
	 * @return
	 */
	@Override
	public boolean contains(final Object o)
	{
		return false;
	}


	/**
	 * @param c
	 * @return
	 */
	@Override
	public boolean containsAll(final Collection<?> c)
	{
		return false;
	}


	/**
	 * @param o
	 * @return
	 */
	@Override
	public int indexOf(final Object o)
	{
		return 0;
	}


	/**
	 * @param o
	 * @return
	 */
	@Override
	public int lastIndexOf(final Object o)
	{
		return 0;
	}


	/**
	 * @param o
	 * @return
	 */
	@Override
	public boolean remove(final Object o)
	{
		return false;
	}


	/**
	 * @param c
	 * @return
	 */
	@Override
	public boolean removeAll(final Collection<?> c)
	{
		return false;
	}


	/**
	 * @param c
	 * @return
	 */
	@Override
	public boolean retainAll(final Collection<?> c)
	{
		return false;
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
	 * @param <L>
	 * @param targetCollection
	 * @param predicate
	 * @return
	 * @see com.xdev.jadoth.collections.XGettingCollection#copyTo(com.xdev.jadoth.collections.XGettingCollection, com.xdev.jadoth.lang.functional.Predicate)
	 */
	@Override
	public <L extends Collecting<E>> L copyTo(final L targetCollection, final Predicate<E> predicate)
	{
		return null;
	}


	/**
	 * @param <L>
	 * @param targetCollection
	 * @param predicate
	 * @return
	 * @see com.xdev.jadoth.collections.XGettingCollection#copyTo(com.xdev.jadoth.collections.XGettingCollection, com.xdev.jadoth.lang.functional.controlflow.TPredicate)
	 */
	@Override
	public <L extends Collecting<E>> L copyTo(final L targetCollection, final TPredicate<E> predicate)
	{
		return null;
	}


	/**
	 * @param <L>
	 * @param targetCollection
	 * @param predicate
	 * @param limit
	 * @return
	 * @see com.xdev.jadoth.collections.XGettingCollection#copyTo(com.xdev.jadoth.collections.XGettingCollection, com.xdev.jadoth.lang.functional.Predicate, int)
	 */
	@Override
	public <L extends Collecting<E>> L copyTo(final L targetCollection, final Predicate<E> predicate,final int skip, final Integer limit)
	{
		return null;
	}


	/**
	 * @param <L>
	 * @param targetCollection
	 * @param predicate
	 * @param limit
	 * @return
	 * @see com.xdev.jadoth.collections.XGettingCollection#copyTo(com.xdev.jadoth.collections.XGettingCollection, com.xdev.jadoth.lang.functional.controlflow.TPredicate, int)
	 */
	@Override
	public <L extends Collecting<E>> L copyTo(final L targetCollection, final TPredicate<E> predicate,final int skip, final Integer limit)
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
	 * @param <L>
	 * @param target
	 * @param predicate
	 * @return
	 * @see com.xdev.jadoth.collections.XRemovingCollection#moveTo(com.xdev.jadoth.collections.XGettingCollection, com.xdev.jadoth.lang.functional.Predicate)
	 */
	@Override
	public <L extends Collecting<E>> L moveTo(final L target, final Predicate<E> predicate)
	{
		return null;
	}


	/**
	 * @param <L>
	 * @param target
	 * @param predicate
	 * @return
	 * @see com.xdev.jadoth.collections.XRemovingCollection#moveTo(com.xdev.jadoth.collections.XGettingCollection, com.xdev.jadoth.lang.functional.controlflow.TPredicate)
	 */
	@Override
	public <L extends Collecting<E>> L moveTo(final L target, final TPredicate<E> predicate)
	{
		return null;
	}


	/**
	 * @param <L>
	 * @param target
	 * @param predicate
	 * @param limit
	 * @return
	 * @see com.xdev.jadoth.collections.XRemovingCollection#moveTo(com.xdev.jadoth.collections.XGettingCollection, com.xdev.jadoth.lang.functional.Predicate, int)
	 */
	@Override
	public <L extends Collecting<E>> L moveTo(final L target, final Predicate<E> predicate,final int skip, final Integer limit)
	{
		return null;
	}


	/**
	 * @param <L>
	 * @param target
	 * @param predicate
	 * @param limit
	 * @return
	 * @see com.xdev.jadoth.collections.XRemovingCollection#moveTo(com.xdev.jadoth.collections.XGettingCollection, com.xdev.jadoth.lang.functional.controlflow.TPredicate, int)
	 */
	@Override
	public <L extends Collecting<E>> L moveTo(final L target, final TPredicate<E> predicate,final int skip, final Integer limit)
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
	 * @see com.xdev.jadoth.collections.XGettingList#rngCopyTo(int, int, com.xdev.jadoth.collections.XAddingCollection, com.xdev.jadoth.lang.functional.Predicate)
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
	 * @see com.xdev.jadoth.collections.XGettingList#rngCopyTo(int, int, com.xdev.jadoth.collections.XAddingCollection, com.xdev.jadoth.lang.functional.controlflow.TPredicate)
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
	 * @see com.xdev.jadoth.collections.XGettingList#rngCopyTo(int, int, com.xdev.jadoth.collections.XAddingCollection, com.xdev.jadoth.lang.functional.Predicate, int)
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
	 * @see com.xdev.jadoth.collections.XGettingList#rngCopyTo(int, int, com.xdev.jadoth.collections.XAddingCollection, com.xdev.jadoth.lang.functional.controlflow.TPredicate, int)
	 */
	@Override
	public <L extends Collecting<E>> L rngCopyTo(final int startIndex, final int endIndex, final L targetCollection, final TPredicate<E> predicate,final int skip, final Integer limit)
	{
		return null;
	}


	/**
	 * @param <L>
	 * @param startIndex
	 * @param endIndex
	 * @param targetCollection
	 * @return
	 * @see com.xdev.jadoth.collections.XGettingList#copyRange(int, int, com.xdev.jadoth.collections.XAddingCollection)
	 */
	@Override
	public <L extends Collecting<E>> L copyRange(final int startIndex, final int endIndex, final L targetCollection)
	{
		return null;
	}


	/**
	 * @param <L>
	 * @param startIndex
	 * @param endIndex
	 * @param target
	 * @param predicate
	 * @return
	 * @see com.xdev.jadoth.collections.XRemovingList#rngMoveTo(int, int, com.xdev.jadoth.collections.XAddingCollection, com.xdev.jadoth.lang.functional.Predicate)
	 */
	@Override
	public <L extends Collecting<E>> L rngMoveTo(final int startIndex, final int endIndex, final L target, final Predicate<E> predicate)
	{
		return null;
	}


	/**
	 * @param <L>
	 * @param startIndex
	 * @param endIndex
	 * @param target
	 * @param predicate
	 * @return
	 * @see com.xdev.jadoth.collections.XRemovingList#rngMoveTo(int, int, com.xdev.jadoth.collections.XAddingCollection, com.xdev.jadoth.lang.functional.controlflow.TPredicate)
	 */
	@Override
	public <L extends Collecting<E>> L rngMoveTo(final int startIndex, final int endIndex, final L target, final TPredicate<E> predicate)
	{
		return null;
	}


	/**
	 * @param <L>
	 * @param startIndex
	 * @param endIndex
	 * @param target
	 * @param predicate
	 * @param limit
	 * @return
	 * @see com.xdev.jadoth.collections.XRemovingList#rngMoveTo(int, int, com.xdev.jadoth.collections.XAddingCollection, com.xdev.jadoth.lang.functional.Predicate, int)
	 */
	@Override
	public <L extends Collecting<E>> L rngMoveTo(final int startIndex, final int endIndex, final L target, final Predicate<E> predicate,final int skip, final Integer limit)
	{
		return null;
	}


	/**
	 * @param <L>
	 * @param startIndex
	 * @param endIndex
	 * @param target
	 * @param predicate
	 * @param limit
	 * @return
	 * @see com.xdev.jadoth.collections.XRemovingList#rngMoveTo(int, int, com.xdev.jadoth.collections.XAddingCollection, com.xdev.jadoth.lang.functional.controlflow.TPredicate, int)
	 */
	@Override
	public <L extends Collecting<E>> L rngMoveTo(final int startIndex, final int endIndex, final L target, final TPredicate<E> predicate,final int skip, final Integer limit)
	{
		return null;
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
	 * @param comparator
	 * @return
	 * @see com.xdev.jadoth.collections.XSettingList#sort(java.util.Comparator)
	 */
	@Override
	public void sort(final Comparator<? super E> comparator)
	{
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
	 * @param oldElements
	 * @param newElement
	 * @return
	 * @see com.xdev.jadoth.collections.XSettingCollection#replaceAll(E[], java.lang.Object)
	 */
	@Override
	public int replaceAll(final E[] oldElements, final E newElement)
	{
		return 0;
	}


	/**
	 * @param oldElements
	 * @param newElement
	 * @param equalator
	 * @return
	 * @see com.xdev.jadoth.collections.XSettingCollection#replaceAll(E[], java.lang.Object, com.xdev.jadoth.lang.Equalator)
	 */
	@Override
	public int replaceAll(final E[] oldElements, final E newElement, final Equalator<E> equalator)
	{
		return 0;
	}


	/**
	 * @param oldElements
	 * @param newElement
	 * @return
	 * @see com.xdev.jadoth.collections.XSettingCollection#replaceAll(XGettingCollection, java.lang.Object)
	 */
	@Override
	public int replaceAll(final XGettingCollection<E> oldElements, final E newElement)
	{
		return 0;
	}


	/**
	 * @param oldElements
	 * @param newElement
	 * @param equalator
	 * @return
	 * @see com.xdev.jadoth.collections.XSettingCollection#replaceAll(com.xdev.jadoth.collections.XGettingCollection, java.lang.Object, com.xdev.jadoth.lang.Equalator)
	 */
	@Override
	public int replaceAll(final XGettingCollection<E> oldElements, final E newElement, final Equalator<E> equalator)
	{
		return 0;
	}


	/**
	 * @param predicate
	 * @param newElement
	 * @return
	 * @see com.xdev.jadoth.collections.XSettingCollection#replace(com.xdev.jadoth.lang.functional.Predicate, java.lang.Object)
	 */
	@Override
	public int replace(final Predicate<E> predicate, final E newElement)
	{
		return 0;
	}


	/**
	 * @param predicate
	 * @param newElement
	 * @return
	 * @see com.xdev.jadoth.collections.XSettingCollection#replace(com.xdev.jadoth.lang.functional.controlflow.TPredicate, java.lang.Object)
	 */
	@Override
	public int replace(final TPredicate<E> predicate, final E newElement)
	{
		return 0;
	}


	/**
	 * @param predicate
	 * @param newElement
	 * @param skip
	 * @param limit
	 * @return
	 * @see com.xdev.jadoth.collections.XSettingCollection#replace(com.xdev.jadoth.lang.functional.Predicate, java.lang.Object, int, int)
	 */
	@Override
	public int replace(final Predicate<E> predicate, final E newElement, final int skip, final Integer limit)
	{
		return 0;
	}


	/**
	 * @param predicate
	 * @param newElement
	 * @param skip
	 * @param limit
	 * @return
	 * @see com.xdev.jadoth.collections.XSettingCollection#replace(com.xdev.jadoth.lang.functional.controlflow.TPredicate, java.lang.Object, int, int)
	 */
	@Override
	public int replace(final TPredicate<E> predicate, final E newElement, final int skip, final Integer limit)
	{
		return 0;
	}


	/**
	 * @param predicate
	 * @param newElement
	 * @return
	 * @see com.xdev.jadoth.collections.XSettingCollection#replaceOne(com.xdev.jadoth.lang.functional.Predicate, java.lang.Object)
	 */
	@Override
	public int replaceOne(final Predicate<E> predicate, final E newElement)
	{
		return 0;
	}


	/**
	 * @param predicate
	 * @param newElement
	 * @return
	 * @see com.xdev.jadoth.collections.XSettingCollection#replaceOne(com.xdev.jadoth.lang.functional.controlflow.TPredicate, java.lang.Object)
	 */
	@Override
	public int replaceOne(final TPredicate<E> predicate, final E newElement)
	{
		return 0;
	}


	/**
	 * @param oldElements
	 * @param newElement
	 * @param skip
	 * @param limit
	 * @return
	 * @see com.xdev.jadoth.collections.XSettingCollection#replaceAll(E[], java.lang.Object, int, int)
	 */
	@Override
	public int replaceAll(final E[] oldElements, final E newElement, final int skip, final Integer limit)
	{
		return 0;
	}


	/**
	 * @param oldElements
	 * @param newElement
	 * @param skip
	 * @param limit
	 * @param equalator
	 * @return
	 * @see com.xdev.jadoth.collections.XSettingCollection#replaceAll(E[], java.lang.Object, int, int, com.xdev.jadoth.lang.Equalator)
	 */
	@Override
	public int replaceAll(final E[] oldElements, final E newElement, final int skip, final Integer limit, final Equalator<E> equalator)
	{
		return 0;
	}


	/**
	 * @param oldElements
	 * @param newElement
	 * @param skip
	 * @param limit
	 * @return
	 * @see com.xdev.jadoth.collections.XSettingCollection#replaceAll(XGettingCollection, java.lang.Object, int, int)
	 */
	@Override
	public int replaceAll(final XGettingCollection<E> oldElements, final E newElement, final int skip, final Integer limit)
	{
		return 0;
	}


	/**
	 * @param oldElements
	 * @param newElement
	 * @param skip
	 * @param limit
	 * @param equalator
	 * @return
	 * @see com.xdev.jadoth.collections.XSettingCollection#replaceAll(com.xdev.jadoth.collections.XGettingCollection, java.lang.Object, int, int, com.xdev.jadoth.lang.Equalator)
	 */
	@Override
	public int replaceAll(final XGettingCollection<E> oldElements, final E newElement, final int skip, final Integer limit, final Equalator<E> equalator)
	{
		return 0;
	}







}
