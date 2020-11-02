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


import com.xdev.jadoth.lang.Equalator;
import com.xdev.jadoth.lang.functional.Predicate;
import com.xdev.jadoth.lang.functional.controlflow.TPredicate;

/**
 * @author Thomas Muenz
 *
 */
public interface XRemovingList<E> extends XGettingList<E>, XRemovingCollection<E>
{
	public interface Factory<E> extends XGettingList.Factory<E>, XRemovingCollection.Factory<E>
	{
		@Override
		public XRemovingList<E> createInstance();
	}


	public E remove(int index);

	public XRemovingList<E> removeRange(int startIndex, int endIndex);

	public E removeFirst();
	public E removeLast();

	public E rngRemoveOne(int startIndex, int endIndex, E element);
	public E rngRemoveOne(int startIndex, int endIndex, E element, Equalator<E> equalator);

	public int rngRemove(int startIndex, int endIndex, E element);
	public int rngRemove(int startIndex, int endIndex, E element, int skip, Integer limit);

	public int rngRemove(int startIndex, int endIndex, E element, Equalator<E> equalator);
	public int rngRemove(int startIndex, int endIndex, E element, Equalator<E> equalator, int skip, Integer limit);

	public int rngRemoveAll(int startIndex, int endIndex, XCollection<? super E> c, boolean ignoreNulls, Equalator<E> equalator);
	public int rngRemoveAll(int startIndex, int endIndex, XCollection<? super E> c, boolean ignoreNulls);

	public int rngRetainAll(int startIndex, int endIndex, XCollection<E> c, Equalator<E> equalator);
	public int rngRetainAll(int startIndex, int endIndex, XCollection<E> c);

	public int rngRemoveDuplicates(int startIndex, int endIndex, boolean ignoreNulls, Equalator<E> equalator);
	public int rngRemoveDuplicates(int startIndex, int endIndex, boolean ignoreNulls);

	public int rngReduce (int startIndex, int endIndex,  Predicate<E> predicate);
	public int rngReduce (int startIndex, int endIndex, TPredicate<E> predicate);
	public int rngReduce (int startIndex, int endIndex,  Predicate<E> predicate, int skip, Integer limit);
	public int rngReduce (int startIndex, int endIndex, TPredicate<E> predicate, int skip, Integer limit);

	public <L extends Collecting<E>> L rngMoveTo(int startIndex, int endIndex, L target,  Predicate<E> predicate);
	public <L extends Collecting<E>> L rngMoveTo(int startIndex, int endIndex, L target, TPredicate<E> predicate);
	public <L extends Collecting<E>> L rngMoveTo(int startIndex, int endIndex, L target,  Predicate<E> predicate, int skip, Integer limit);
	public <L extends Collecting<E>> L rngMoveTo(int startIndex, int endIndex, L target, TPredicate<E> predicate, int skip, Integer limit);
}
