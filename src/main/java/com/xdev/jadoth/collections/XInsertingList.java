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

import com.xdev.jadoth.lang.functional.Predicate;
import com.xdev.jadoth.lang.functional.controlflow.TPredicate;


/**
 * @author Thomas Muenz
 *
 */
public interface XInsertingList<E> extends XAddingList<E>
{
	public interface Factory<E> extends XAddingList.Factory<E>
	{
		@Override
		public XInsertingList<E> createInstance();
	}


	public XInsertingList<E> prepend(E element);

	// method signatures blocked by java.util.List
//	public void add(int index, E element);
//	public boolean addAll(int index, final Collection<? extends E> c);

	public XInsertingList<E> insert(int index, E element);
	public XInsertingList<E> insert(int index, E... elements);
	public XInsertingList<E> insert(int index, Iterable<? extends E> elements);

	public XInsertingList<E> insert(int index, E[] elements, int srcIndex, int srcLength, Predicate<E> predicate, int skip, Integer limit);
	public XInsertingList<E> insert(int index, E[] elements, int srcIndex, int srcLength, TPredicate<E> predicate, int skip, Integer limit);

	public XInsertingList<E> insert(int index, Iterable<? extends E> elements, int srcIndex, Integer srcLength, Predicate<E> predicate, int skip, Integer limit);
	public XInsertingList<E> insert(int index, Iterable<? extends E> elements, int srcIndex, Integer srcLength, TPredicate<E> predicate, int skip, Integer limit);

}
