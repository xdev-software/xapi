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

import java.util.Comparator;
import java.util.List;
import java.util.Map;

import com.xdev.jadoth.lang.Equalator;
import com.xdev.jadoth.lang.functional.Operation;
import com.xdev.jadoth.lang.functional.Predicate;
import com.xdev.jadoth.lang.functional.controlflow.TOperation;
import com.xdev.jadoth.lang.functional.controlflow.TPredicate;


/**
 * @author Thomas Muenz
 *
 */
public class SubListAccessor<E> extends SubListView<E> implements XSettingList<E>
{

	@Override
	public SubListAccessor<E> execute(final Operation<E> operation)
	{
		return null;
	}

	@Override
	public SubListAccessor<E> execute(final TOperation<E> operation)
	{
		return null;
	}

	@Override
	public SubListAccessor<E> process(final Operation<E> operation)
	{
		return null;
	}

	@Override
	public SubListAccessor<E> process(final TOperation<E> operation)
	{
		return null;
	}

	/**
	 * @param startIndex
	 * @param endIndex
	 * @param element
	 * @return
	 * @see com.xdev.jadoth.collections.XSettingList#fill(int, int, java.lang.Object)
	 */
	@Override
	public XList<E> fill(final int startIndex, final int endIndex, final E element)
	{
		return null;
	}

	/**
	 * @return
	 * @see com.xdev.jadoth.collections.XSettingList#reverse()
	 */
	@Override
	public XList<E> reverse()
	{
		return null;
	}

	/**
	 * @param startIndex
	 * @param endIndex
	 * @param oldElement
	 * @param newElement
	 * @param equalator
	 * @return
	 * @see com.xdev.jadoth.collections.XSettingList#rngReplace(int, int, java.lang.Object, java.lang.Object, com.xdev.jadoth.lang.Equalator)
	 */
	@Override
	public int rngReplace(final int startIndex, final int endIndex, final E oldElement, final E newElement, final Equalator<E> equalator)
	{
		return 0;
	}

	/**
	 * @param startIndex
	 * @param endIndex
	 * @param oldElement
	 * @param newElement
	 * @return
	 * @see com.xdev.jadoth.collections.XSettingList#rngReplace(int, int, java.lang.Object, java.lang.Object)
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
	 * @param skip
	 * @param limit
	 * @param equalator
	 * @return
	 * @see com.xdev.jadoth.collections.XSettingList#rngReplace(int, int, java.lang.Object, java.lang.Object, int, int, com.xdev.jadoth.lang.Equalator)
	 */
	@Override
	public int rngReplace(final int startIndex, final int endIndex, final E oldElement, final E newElement, final int skip, final Integer limit, final Equalator<E> equalator)
	{
		return 0;
	}

	/**
	 * @param startIndex
	 * @param endIndex
	 * @param oldElement
	 * @param newElement
	 * @param skip
	 * @param limit
	 * @return
	 * @see com.xdev.jadoth.collections.XSettingList#rngReplace(int, int, java.lang.Object, java.lang.Object, int, int)
	 */
	@Override
	public int rngReplace(final int startIndex, final int endIndex, final E oldElement, final E newElement, final int skip, final Integer limit)
	{
		return 0;
	}

	/**
	 * @param startIndex
	 * @param endIndex
	 * @param replacementMapping
	 * @return
	 * @see com.xdev.jadoth.collections.XSettingList#rngReplaceAll(int, int, java.util.Map)
	 */
	@Override
	public int rngReplaceAll(final int startIndex, final int endIndex, final Map<E, E> replacementMapping)
	{
		return 0;
	}

	/**
	 * @param startIndex
	 * @param endIndex
	 * @param oldElement
	 * @param newElement
	 * @param equalator
	 * @return
	 * @see com.xdev.jadoth.collections.XSettingList#rngReplaceOne(int, int, java.lang.Object, java.lang.Object, com.xdev.jadoth.lang.Equalator)
	 */
	@Override
	public int rngReplaceOne(final int startIndex, final int endIndex, final E oldElement, final E newElement, final Equalator<E> equalator)
	{
		return 0;
	}

	/**
	 * @param startIndex
	 * @param endIndex
	 * @param oldElement
	 * @param newElement
	 * @return
	 * @see com.xdev.jadoth.collections.XSettingList#rngReplaceOne(int, int, java.lang.Object, java.lang.Object)
	 */
	@Override
	public int rngReplaceOne(final int startIndex, final int endIndex, final E oldElement, final E newElement)
	{
		return 0;
	}

	/**
	 * @param startIndex
	 * @param endIndex
	 * @return
	 * @see com.xdev.jadoth.collections.XSettingList#rngReverse(int, int)
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
	 * @see com.xdev.jadoth.collections.XSettingList#rngShuffle(int, int)
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
	 * @see com.xdev.jadoth.collections.XSettingList#rngSortMerge(int, int, java.util.Comparator)
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
	 * @see com.xdev.jadoth.collections.XSettingList#rngSortQuick(int, int, java.util.Comparator)
	 */
	@Override
	public XList<E> rngSortQuick(final int startIndex, final int endIndex, final Comparator<E> comparator)
	{
		return null;
	}

	/**
	 * @param startIndex
	 * @param elements
	 * @return
	 * @see com.xdev.jadoth.collections.XSettingList#set(int, E[])
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
	 * @see com.xdev.jadoth.collections.XSettingList#set(int, E[], int, int)
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
	 * @see com.xdev.jadoth.collections.XSettingList#set(int, java.util.List, int, int)
	 */
	@Override
	public XList<E> set(final int startIndex, final List<E> elements, final int elementsStartIndex, final int length)
	{
		return null;
	}

	/**
	 * @param element
	 * @see com.xdev.jadoth.collections.XSettingList#setFirst(java.lang.Object)
	 */
	@Override
	public void setFirst(final E element)
	{
		
	}

	/**
	 * @param element
	 * @see com.xdev.jadoth.collections.XSettingList#setLast(java.lang.Object)
	 */
	@Override
	public void setLast(final E element)
	{
		
	}

	/**
	 * @return
	 * @see com.xdev.jadoth.collections.XSettingList#shuffle()
	 */
	@Override
	public XList<E> shuffle()
	{
		return null;
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
	 * @param comparator
	 * @return
	 * @see com.xdev.jadoth.collections.XSettingList#sortMerge(java.util.Comparator)
	 */
	@Override
	public XList<E> sortMerge(final Comparator<E> comparator)
	{
		return null;
	}

	/**
	 * @param comparator
	 * @return
	 * @see com.xdev.jadoth.collections.XSettingList#sortQuick(java.util.Comparator)
	 */
	@Override
	public XList<E> sortQuick(final Comparator<E> comparator)
	{
		return null;
	}

	/**
	 * @param indexA
	 * @param indexB
	 * @see com.xdev.jadoth.collections.XSettingList#swap(int, int)
	 */
	@Override
	public void swap(final int indexA, final int indexB)
	{
		
	}

	/**
	 * @param indexA
	 * @param indexB
	 * @param length
	 * @see com.xdev.jadoth.collections.XSettingList#swap(int, int, int)
	 */
	@Override
	public void swap(final int indexA, final int indexB, final int length)
	{
		
	}

	/**
	 * @param oldElement
	 * @param newElement
	 * @return
	 * @see com.xdev.jadoth.collections.XSettingCollection#replace(java.lang.Object, java.lang.Object)
	 */
	@Override
	public int replace(final E oldElement, final E newElement)
	{
		return 0;
	}

	/**
	 * @param oldElement
	 * @param newElement
	 * @param equalator
	 * @return
	 * @see com.xdev.jadoth.collections.XSettingCollection#replace(java.lang.Object, java.lang.Object, com.xdev.jadoth.lang.Equalator)
	 */
	@Override
	public int replace(final E oldElement, final E newElement, final Equalator<E> equalator)
	{
		return 0;
	}

	/**
	 * @param oldElement
	 * @param newElement
	 * @param skip
	 * @param limit
	 * @return
	 * @see com.xdev.jadoth.collections.XSettingCollection#replace(java.lang.Object, java.lang.Object, int, int)
	 */
	@Override
	public int replace(final E oldElement, final E newElement, final int skip, final Integer limit)
	{
		return 0;
	}

	/**
	 * @param oldElement
	 * @param newElement
	 * @param skip
	 * @param limit
	 * @param equalator
	 * @return
	 * @see com.xdev.jadoth.collections.XSettingCollection#replace(java.lang.Object, java.lang.Object, int, int, com.xdev.jadoth.lang.Equalator)
	 */
	@Override
	public int replace(final E oldElement, final E newElement, final int skip, final Integer limit, final Equalator<E> equalator)
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
	 * @param replacementMapping
	 * @return
	 * @see com.xdev.jadoth.collections.XSettingCollection#replaceAll(java.util.Map)
	 */
	@Override
	public int replaceAll(final Map<E, E> replacementMapping)
	{
		return 0;
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

	/**
	 * @param oldElement
	 * @param newElement
	 * @return
	 * @see com.xdev.jadoth.collections.XSettingCollection#replaceOne(java.lang.Object, java.lang.Object)
	 */
	@Override
	public int replaceOne(final E oldElement, final E newElement)
	{
		return 0;
	}

	/**
	 * @param oldElement
	 * @param newElement
	 * @param equalator
	 * @return
	 * @see com.xdev.jadoth.collections.XSettingCollection#replaceOne(java.lang.Object, java.lang.Object, com.xdev.jadoth.lang.Equalator)
	 */
	@Override
	public int replaceOne(final E oldElement, final E newElement, final Equalator<E> equalator)
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
	 * @param startIndex
	 * @param endIndex
	 * @param predicate
	 * @param newElement
	 * @return
	 * @see com.xdev.jadoth.collections.XSettingList#rngReplace(int, int, com.xdev.jadoth.lang.functional.Predicate, java.lang.Object)
	 */
	@Override
	public int rngReplace(final int startIndex, final int endIndex, final Predicate<E> predicate, final E newElement)
	{
		return 0;
	}

	/**
	 * @param startIndex
	 * @param endIndex
	 * @param predicate
	 * @param newElement
	 * @return
	 * @see com.xdev.jadoth.collections.XSettingList#rngReplace(int, int, com.xdev.jadoth.lang.functional.controlflow.TPredicate, java.lang.Object)
	 */
	@Override
	public int rngReplace(final int startIndex, final int endIndex, final TPredicate<E> predicate, final E newElement)
	{
		return 0;
	}

	/**
	 * @param startIndex
	 * @param endIndex
	 * @param predicate
	 * @param newElement
	 * @param skip
	 * @param limit
	 * @return
	 * @see com.xdev.jadoth.collections.XSettingList#rngReplace(int, int, com.xdev.jadoth.lang.functional.Predicate, java.lang.Object, int, int)
	 */
	@Override
	public int rngReplace(final int startIndex, final int endIndex, final Predicate<E> predicate, final E newElement, final int skip, final Integer limit)
	{
		return 0;
	}

	/**
	 * @param startIndex
	 * @param endIndex
	 * @param predicate
	 * @param newElement
	 * @param skip
	 * @param limit
	 * @return
	 * @see com.xdev.jadoth.collections.XSettingList#rngReplace(int, int, com.xdev.jadoth.lang.functional.controlflow.TPredicate, java.lang.Object, int, int)
	 */
	@Override
	public int rngReplace(final int startIndex, final int endIndex, final TPredicate<E> predicate, final E newElement, final int skip, final Integer limit)
	{
		return 0;
	}

	/**
	 * @param startIndex
	 * @param endIndex
	 * @param oldElements
	 * @param newElement
	 * @return
	 * @see com.xdev.jadoth.collections.XSettingList#rngReplaceAll(int, int, E[], java.lang.Object)
	 */
	@Override
	public int rngReplaceAll(final int startIndex, final int endIndex, final E[] oldElements, final E newElement)
	{
		return 0;
	}

	/**
	 * @param startIndex
	 * @param endIndex
	 * @param oldElements
	 * @param newElement
	 * @param equalator
	 * @return
	 * @see com.xdev.jadoth.collections.XSettingList#rngReplaceAll(int, int, E[], java.lang.Object, com.xdev.jadoth.lang.Equalator)
	 */
	@Override
	public int rngReplaceAll(final int startIndex, final int endIndex, final E[] oldElements, final E newElement, final Equalator<E> equalator)
	{
		return 0;
	}

	/**
	 * @param startIndex
	 * @param endIndex
	 * @param oldElements
	 * @param newElement
	 * @return
	 * @see net.jadoth.collections.XSettingList#rngReplaceAll(int, int, XGettingCollection<E>, java.lang.Object)
	 */
	@Override
	public int rngReplaceAll(final int startIndex, final int endIndex, final XGettingCollection<E> oldElements, final E newElement)
	{
		return 0;
	}

	/**
	 * @param startIndex
	 * @param endIndex
	 * @param oldElements
	 * @param newElement
	 * @param equalator
	 * @return
	 * @see com.xdev.jadoth.collections.XSettingList#rngReplaceAll(int, int, com.xdev.jadoth.collections.XGettingCollection, java.lang.Object, com.xdev.jadoth.lang.Equalator)
	 */
	@Override
	public int rngReplaceAll(final int startIndex, final int endIndex, final XGettingCollection<E> oldElements, final E newElement, final Equalator<E> equalator)
	{
		return 0;
	}

	/**
	 * @param startIndex
	 * @param endIndex
	 * @param oldElements
	 * @param newElement
	 * @param skip
	 * @param limit
	 * @return
	 * @see com.xdev.jadoth.collections.XSettingList#rngReplaceAll(int, int, E[], java.lang.Object, int, int)
	 */
	@Override
	public int rngReplaceAll(final int startIndex, final int endIndex, final E[] oldElements, final E newElement, final int skip, final Integer limit)
	{
		return 0;
	}

	/**
	 * @param startIndex
	 * @param endIndex
	 * @param oldElements
	 * @param newElement
	 * @param skip
	 * @param limit
	 * @param equalator
	 * @return
	 * @see com.xdev.jadoth.collections.XSettingList#rngReplaceAll(int, int, E[], java.lang.Object, int, int, com.xdev.jadoth.lang.Equalator)
	 */
	@Override
	public int rngReplaceAll(final int startIndex, final int endIndex, final E[] oldElements, final E newElement, final int skip, final Integer limit, final Equalator<E> equalator)
	{
		return 0;
	}

	/**
	 * @param startIndex
	 * @param endIndex
	 * @param oldElements
	 * @param newElement
	 * @param skip
	 * @param limit
	 * @return
	 * @see net.jadoth.collections.XSettingList#rngReplaceAll(int, int, XGettingCollection<E>, java.lang.Object, int, int)
	 */
	@Override
	public int rngReplaceAll(final int startIndex, final int endIndex, final XGettingCollection<E> oldElements, final E newElement, final int skip, final Integer limit)
	{
		return 0;
	}

	/**
	 * @param startIndex
	 * @param endIndex
	 * @param oldElements
	 * @param newElement
	 * @param skip
	 * @param limit
	 * @param equalator
	 * @return
	 * @see com.xdev.jadoth.collections.XSettingList#rngReplaceAll(int, int, com.xdev.jadoth.collections.XGettingCollection, java.lang.Object, int, int, com.xdev.jadoth.lang.Equalator)
	 */
	@Override
	public int rngReplaceAll(final int startIndex, final int endIndex, final XGettingCollection<E> oldElements, final E newElement, final int skip, final Integer limit, final Equalator<E> equalator)
	{
		return 0;
	}

	/**
	 * @param startIndex
	 * @param endIndex
	 * @param predicate
	 * @param newElement
	 * @return
	 * @see com.xdev.jadoth.collections.XSettingList#rngReplaceOne(int, int, com.xdev.jadoth.lang.functional.Predicate, java.lang.Object)
	 */
	@Override
	public int rngReplaceOne(final int startIndex, final int endIndex, final Predicate<E> predicate, final E newElement)
	{
		return 0;
	}

	/**
	 * @param startIndex
	 * @param endIndex
	 * @param predicate
	 * @param newElement
	 * @return
	 * @see com.xdev.jadoth.collections.XSettingList#rngReplaceOne(int, int, com.xdev.jadoth.lang.functional.controlflow.TPredicate, java.lang.Object)
	 */
	@Override
	public int rngReplaceOne(final int startIndex, final int endIndex, final TPredicate<E> predicate, final E newElement)
	{
		return 0;
	}

	
}
