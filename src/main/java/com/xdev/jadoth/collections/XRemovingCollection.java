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


import com.xdev.jadoth.lang.Equalator;
import com.xdev.jadoth.lang.functional.Operation;
import com.xdev.jadoth.lang.functional.Predicate;
import com.xdev.jadoth.lang.functional.Processable;
import com.xdev.jadoth.lang.functional.controlflow.TOperation;
import com.xdev.jadoth.lang.functional.controlflow.TPredicate;
import com.xdev.jadoth.lang.functional.controlflow.TProcessable;

/**
 * @author Thomas Muenz
 *
 */
public interface XRemovingCollection<E>
extends
XGettingCollection<E>,
Processable<E, Operation<E>>,
TProcessable<E, TOperation<E>>
{
	public interface Factory<E> extends XGettingCollection.Factory<E>
	{
		@Override
		public XRemovingCollection<E> createInstance();
	}



	public XRemovingCollection<E> execute( Operation<E> operation);
	public XRemovingCollection<E> execute(TOperation<E> operation);

	public XRemovingCollection<E> process( Operation<E> operation);
	public XRemovingCollection<E> process(TOperation<E> operation);

	public void clear();

	public E removeOne(E element);
	public E removeOne(E element, Equalator<E> equalator);

	public int removeId(E element);
	public int remove  (E element, Equalator<E> equalator);

	public int removeId(E element, int skip, Integer limit);
	public int remove  (E element, int skip, Integer limit, Equalator<E> equalator);

	public int removeAllId(XCollection<E> c, boolean ignoreNulls);
	public int removeAll  (XCollection<E> c, boolean ignoreNulls, Equalator<E> equalator);

	public int retainAllId(XCollection<E> c);
	public int retainAll  (XCollection<E> c, Equalator<E> equalator);

	public int removeDuplicates(boolean ignoreNulls);
	public int removeDuplicates(boolean ignoreNulls, Equalator<E> equalator);
	// intentionally no removeDuplicates(..., skip, limit) as this wouldn't make much sense

	public int reduce( Predicate<E> predicate);
	public int reduce(TPredicate<E> predicate);
	public int reduce( Predicate<E> predicate, int skip, Integer limit);
	public int reduce(TPredicate<E> predicate, int skip, Integer limit);

	public <C extends Collecting<E>> C moveTo(C target,  Predicate<E> predicate);
	public <C extends Collecting<E>> C moveTo(C target, TPredicate<E> predicate);
	public <C extends Collecting<E>> C moveTo(C target,  Predicate<E> predicate, int skip, Integer limit);
	public <C extends Collecting<E>> C moveTo(C target, TPredicate<E> predicate, int skip, Integer limit);

	/**
	 * Clears (and reinitializes if needed) this collection in the fastest possible way, i.e. by allocating a new and
	 * empty internal storage. The collection will be empty after calling this method.
	 * @return this collection
	 */
	public XRemovingCollection<E> truncate();
}
