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

import java.util.Comparator;
import java.util.List;
import java.util.Map;

import com.xdev.jadoth.lang.Equalator;
import com.xdev.jadoth.lang.functional.Predicate;
import com.xdev.jadoth.lang.functional.controlflow.TPredicate;


/**
 * @author Thomas Muenz
 *
 */
public interface XSettingList<E> extends XGettingList<E>, XSettingCollection<E>
{
	public interface Factory<E> extends XGettingList.Factory<E>, XSettingCollection.Factory<E>
	{
		@Override
		public XSettingList<E> createInstance();
	}

	
	
	public E set(int index, E element);

	public void setFirst(E element);
	public void setLast(E element);

	public XList<E> set(int startIndex, E... elements);
	public XList<E> set(int startIndex, E[] elements, int elementsStartIndex, int length);
	public XList<E> set(int startIndex, List<E> elements, int elementsStartIndex, int length);

	public XList<E> fill(int startIndex, int endIndex, E element);

	public void swap(int indexA, int indexB);
	public void swap(int indexA, int indexB, int length);

	public XList<E>    reverse();
	public XList<E> rngReverse(int startIndex, int endIndex);
	
	public SubListAccessor<E> subList(int fromIndex, int toIndex);
		
	public void sort(Comparator<? super E> comparator);
	
	/**
	 * Sorts this list using the Mergesort algorithm.<br>
	 * As a rule of thumb, use this algorithm for good results in all situations.<br>
	 * If ALL of the following conditions are met, use {@link #sortQuick(Comparator)} instead:
	 * <ul>
	 * <li>Best performance for average case is needed.</li>
	 * <li>Sorting stability is not relevant.</li>
	 * <li>The list doesn't happen to be presorted (often).</li>
	 * </ul>
	 * <b>Performance</b><ul>
	 * <li>Best case &#160;: fast, but slower than Quicksort [O(n log n)]</li>
	 * <li>Average &#160;&#160; : fast, but slower than Quicksort [O(n log n)]</li>
	 * <li>Worst case: fast, faster than Quicksort [O(n log n)]</li>
	 * <li>Presorted&#160; : very fast [O(n)]</li>
	 * </ul>
	 * <b>Characteristics</b><ul>
	 * <li>Stable &#160;&#160; : yes</li>
	 * <li>In-place : no (array instantiations)</li>
	 * </ul>
	 * @param comparator the comparator to use for sorting the elements.
	 * @return this list.
	 * @see JaSort#mergesort(Object[], Comparator)
	 */
	public XList<E> sortMerge(Comparator<E> comparator);

	public XList<E> rngSortMerge(int startIndex, int endIndex, Comparator<E> comparator);

	/**
	 * Sorts this list using the Quicksort algorithm.<br>
	 * As a rule of thumb, use this algorithm if sorting stability is not relevant
	 * and if the list doesn't happen to be presorted often.<br>
	 * Otherwise, use {@link #sortMerge(Comparator)}.
	 * <p>
	 * <b>Performance</b><ul>
	 * <li>Best case &#160;: fastest known algorithm [O(n log n)]</li>
	 * <li>Average &#160;&#160; : fastest known algorithm [O(n log n)]</li>
	 * <li>Worst case: theoretically slow [O(n^2)], but hardly relevant in practice. More like O(k * n log n)</li>
	 * <li>Presorted&#160; : Depends on pivot element.
	 * Choosing the middle element yields like O(k * n log n) in tests.</li>
	 * </ul>
	 * <b>Characteristics</b><ul>
	 * <li>Stable &#160;&#160; : no</li>
	 * <li>In-place : yes (plus recursive call stack)</li>
	 * </ul>
	 * @param comparator the comparator to use for sorting the elements.
	 * @return this list.
	 * @see JaSort#quicksort(Object[], Comparator)
	 */
	public XList<E>    sortQuick(final Comparator<E> comparator);
	public XList<E> rngSortQuick(int startIndex, int endIndex, final Comparator<E> comparator);

	public XList<E>    shuffle();
	public XList<E> rngShuffle(int startIndex, int endIndex);

	/**
	 * 
	 * @param startIndex
	 * @param endIndex
	 * @param oldElement
	 * @param newElement
	 * @param equalator
	 * @return a value greater than or equal to 0 if an element has been found and replaced, a negative value otherwise.
	 */
	public int rngReplaceOne(int startIndex, int endIndex, E oldElement, E newElement, Equalator<E> equalator);
	/**
	 * 
	 * @param startIndex
	 * @param endIndex
	 * @param oldElement
	 * @param newElement
	 * @return a value greater than or equal to 0 if an element has been found and replaced, a negative value otherwise.
	 */
	public int rngReplaceOne(int startIndex, int endIndex, E oldElement, E newElement);
	/**
	 * 
	 * @param startIndex
	 * @param endIndex
	 * @param oldElement
	 * @param newElement
	 * @param equalator
	 * @return the amount of replacements that has been executed by the call of this method.
	 */
	public int rngReplace(int startIndex, int endIndex, E oldElement, E newElement, Equalator<E> equalator);
	/**
	 * 
	 * @param startIndex
	 * @param endIndex
	 * @param oldElement
	 * @param newElement
	 * @return the amount of replacements that has been executed by the call of this method.
	 */
	public int rngReplace(int startIndex, int endIndex, E oldElement, E newElement);
	/**
	 * 
	 * @param startIndex
	 * @param endIndex
	 * @param oldElement
	 * @param newElement
	 * @param limit
	 * @param equalator
	 * @return the amount of replacements that has been executed by the call of this method.
	 */
	public int rngReplace(int startIndex, int endIndex, E oldElement, E newElement, int skip, Integer limit, Equalator<E> equalator);
	/**
	 * 
	 * @param startIndex
	 * @param endIndex
	 * @param oldElement
	 * @param newElement
	 * @param limit
	 * @return the amount of replacements that has been executed by the call of this method.
	 */
	public int rngReplace(int startIndex, int endIndex, E oldElement, E newElement, int skip, Integer limit);
	/**
	 * 
	 * @param startIndex
	 * @param endIndex
	 * @param replacementMapping
	 * @return the amount of replacements that has been executed by the call of this method.
	 */
	public int rngReplaceAll(int startIndex, int endIndex, Map<E, E> replacementMapping);

	public int rngReplaceAll(int startIndex, int endIndex, E[] oldElements, E newElement);
	public int rngReplaceAll(int startIndex, int endIndex, E[] oldElements, E newElement, Equalator<E> equalator);
	public int rngReplaceAll(int startIndex, int endIndex, XGettingCollection<E> oldElements, E newElement);
	public int rngReplaceAll(int startIndex, int endIndex, XGettingCollection<E> oldElements, E newElement, Equalator<E> equalator);
	
	public int rngReplaceAll(int startIndex, int endIndex, E[] oldElements, E newElement, int skip, Integer limit);
	public int rngReplaceAll(int startIndex, int endIndex, E[] oldElements, E newElement, int skip, Integer limit, Equalator<E> equalator);
	public int rngReplaceAll(int startIndex, int endIndex, XGettingCollection<E> oldElements, E newElement, int skip, Integer limit);
	public int rngReplaceAll(int startIndex, int endIndex, XGettingCollection<E> oldElements, E newElement, int skip, Integer limit, Equalator<E> equalator);

	public int rngReplaceOne(int startIndex, int endIndex,  Predicate<E> predicate, E newElement);
	public int rngReplaceOne(int startIndex, int endIndex, TPredicate<E> predicate, E newElement);

	public int rngReplace(int startIndex, int endIndex,  Predicate<E> predicate, E newElement);
	public int rngReplace(int startIndex, int endIndex, TPredicate<E> predicate, E newElement);
	public int rngReplace(int startIndex, int endIndex,  Predicate<E> predicate, E newElement, int skip, Integer limit);
	public int rngReplace(int startIndex, int endIndex, TPredicate<E> predicate, E newElement, int skip, Integer limit);

}
