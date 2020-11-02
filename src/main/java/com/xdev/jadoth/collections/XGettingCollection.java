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

import com.xdev.jadoth.Jadoth;
import com.xdev.jadoth.lang.Equalator;
import com.xdev.jadoth.lang.functional.Executing;
import com.xdev.jadoth.lang.functional.Operation;
import com.xdev.jadoth.lang.functional.Predicate;
import com.xdev.jadoth.lang.functional.aggregates.Aggregate;
import com.xdev.jadoth.lang.functional.aggregates.TAggregate;
import com.xdev.jadoth.lang.functional.controlflow.TExecuting;
import com.xdev.jadoth.lang.functional.controlflow.TOperation;
import com.xdev.jadoth.lang.functional.controlflow.TPredicate;
import com.xdev.jadoth.util.VarChar;


/**
 * @author TM
 *
 */
public interface XGettingCollection<E>
extends
Collection<E>,
Executing<E, Operation<E>>,
TExecuting<E, TOperation<E>>
{
	// java.util.Collection Query Operations

	public int size();
	public boolean isEmpty();

	/**
	 * Deprecated {@link Collection} method kept for compatability reasons.<br>
	 * Alias for {@link #contains(Object, Jadoth.EQUALS)}.<br>
	 *
	 * Use the approriate choice between {@link #containsId(Object)} and {@link #contains(Object, Equalator)} instead.
	 *
	 * @param o
	 * @return
	 */
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



	// java.util.Collection Bulk Modification Operations

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



	// java.util.Collection Comparison and hashing

	/**
	 * Performs an equality comparison according to the specification in {@link Collection}.
	 * <p>
	 * Note that it is this interface's author opinion that the whole usage of equals() in standard Java, especially
	 * in the collection implementations, is deeply flawed.<br>
	 * The reason is because all kinds of comparison types are mixed up in one method, from identity comparison over
	 * data indentity comparison to content comparison. <br>
	 * In order to get the right behaviour in every situation, one has to distinct between those three different types
	 *  (and maybe more, but then that's what comparators are for).<br>
	 * <p>
	 * This means several things:<br>
	 * 1.) You can't just say for example an ArrayList is the "same" as a LinkedList just because they contain the
	 * same content.<br>
	 * There are different implementations for a good reason, so you have to distinct them when comparing.
	 * There are simple code examples which create massive misbehaviour that will catastrophically ruin the runtime
	 * behaviour of a programm due to this error in Java / JDK / SUN / whatever.<br>
	 * 2.) You can't always determine equality of two collections by determining equality of each element as
	 * {@link Collection} defines it.
	 * <p>
	 * As a conclusion: don't use this method!<br>
	 * Be clear what type of comparison you really need, then use one of the following methods
	 * and proper comparators:<br>
	 * {@link #equals(XGettingCollection, Equalator)}<br>
	 * {@link #equalsContent(Collection, Equalator)}<br>
	 * <p>
	 * {@inheritDoc}
	 *
	 * @param o
	 * @return
	 */
	@Deprecated
	public boolean equals(Object o);
	@Deprecated
	public int hashCode();



	public interface Factory<E>
	{
		public XGettingCollection<E> createInstance();
	}

	

	/**
	 *
	 * @param type
	 * @return
	 */
	public E[] toArray(Class<E> type);

	public E max(Comparator<E> comparator);
	public E min(Comparator<E> comparator);

	public boolean containsAll(Collection<E> c, boolean ignoreNulls, Equalator<E> equalator);
	public boolean containsAll(Collection<E> c, boolean ignoreNulls);

	public XGettingCollection<E> execute( Operation<E> operation);
	public XGettingCollection<E> execute(TOperation<E> operation);

	public XGettingCollection<E> process( Operation<E> operation);
	public XGettingCollection<E> process(TOperation<E> operation);

	public <R> R aggregate(Aggregate<E, R> aggregate);
	public <R> R aggregate(TAggregate<E, R> aggregate);

	/**
	 * Actual contains() method renamed to containsId() due to name clash with deprecated method
	 * {@link #contains(Object)}.
	 */
	public boolean containsId(E element);
	public boolean contains  (E element, Equalator<E> equalator);
	public boolean contains( Predicate<E> predicate);
	public boolean contains(TPredicate<E> predicate);

	public boolean applies( Predicate<E> predicate);
	public boolean applies(TPredicate<E> predicate);

	public int count(E element);
	public int count(E element, Equalator<E> equalator);
	public int count( Predicate<E> predicate);
	public int count(TPredicate<E> predicate);
	
	public <C extends Collecting<E>> C distinct(C targetCollection);
	public <C extends Collecting<E>> C distinct(C targetCollection, Equalator<E> equalator);

	public <C extends Collecting<E>> C copyTo(C targetCollection,  Predicate<E> predicate);
	public <C extends Collecting<E>> C copyTo(C targetCollection, TPredicate<E> predicate);
	public <C extends Collecting<E>> C copyTo(C targetCollection,  Predicate<E> predicate, int skip, Integer limit);
	public <C extends Collecting<E>> C copyTo(C targetCollection, TPredicate<E> predicate, int skip, Integer limit);

	public boolean hasUniqueValues(boolean ignoreMultipleNulls);
	public boolean hasUniqueValues(boolean ignoreMultipleNulls, Equalator<E> comparator);

	public E search(E element, Equalator<E> equalator);
	public E search( Predicate<E> predicate);
	public E search(TPredicate<E> predicate);

	/**
	 * Returns <tt>true</tt> if the passed list is of the same type as this list and
	 * <code>this.equalsContent(list, equalator)</code> yields <tt>true</tt>.
	 *
	 * @param list
	 * @param equalator
	 * @return
	 */
	public boolean equals(XGettingCollection<E> list, Equalator<E> equalator);
	/**
	 * Returns <tt>true</tt> if all elements of this list and the passed list are sequentially equal as defined 
	 * by the passed equalator.
	 * @param list the list that this list shall be compared to
	 * @param equalator the equalator to use to determine the equality of each element
	 * @return <tt>true</tt> if this list is equal to the passed list, <tt>false</tt> otherwise
	 */
	public boolean equalsContent(Collection<E> list, Equalator<E> comparator);

	public void appendTo(VarChar vc);
	public void appendTo(VarChar vc, char seperator);
	public void appendTo(VarChar vc, String seperator);

	public <C extends Collecting<E>> C union    (XGettingCollection<E> other, Equalator<E> comparator, C target);
	public <C extends Collecting<E>> C intersect(XGettingCollection<E> other, Equalator<E> comparator, C target);
	public <C extends Collecting<E>> C except   (XGettingCollection<E> other, Equalator<E> comparator, C target);

}
