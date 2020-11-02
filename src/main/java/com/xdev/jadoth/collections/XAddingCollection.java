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

import com.xdev.jadoth.lang.functional.Predicate;
import com.xdev.jadoth.lang.functional.controlflow.TPredicate;



/**
 * @author Thomas Muenz
 *
 */
public interface XAddingCollection<E> extends XGettingCollection<E>, Collecting<E>
{
	public interface Factory<E> extends XGettingCollection.Factory<E>
	{
		@Override
		public XAddingCollection<E> createInstance();
	}



	// reasonable methods adapted from java.util.Collection. Provide modification information instead of chaining.
	public boolean add(E e);
	public boolean addAll(Collection<? extends E> c);



	public XAddingCollection<E> add(E... elements);
	public XAddingCollection<E> add(Iterable<? extends E> elements);

	/**
	 * Adds all of the <code>srcLength</code> elements in array <code>elements</code> at <code>srcIndex</code> for which
	 * <code>predicate</code> applies to this collection up to a maximum of <code>limit</code> elements.
	 * The first <code>skip</code> elements to which <code>predicate</code> applies are skipped. Skipped elements
	 * to NOT count towards limit.
	 * <p>
	 * Additional information on parameters:<br>
	 * <ul>
	 * <li><code>elements</code></li> may not be <tt>null</tt>.
	 * <li><code>srcIndex</code> may not exceed <code>elements</code>'s bounds.</li>
	 * <li><code>srcLength</code> may be negative, causing the elements to be added in reversed order.</li>
	 * <li><code>predicate</code></li> may not be <tt>null</tt>.
	 * <li><code>skip</code> may not be negative.</li>
	 * <li><code>limit</code> may be <tt>null</tt> to indicate that no limiting shall be applied.</li>
	 * <li><code>limit</code> may be equal to or less than 0, resulting in an immediate return of this method.</li>
	 * </ul>
	 * 
	 * @param elements the source array containing the elements to be added.
	 * @param srcIndex the source array index of the first element to be (potentially) added. 
	 * @param srcLength the amount of elements to be (potentially) added.
	 * @param predicate the predicate to evaluate if an element shall be added. May not be <tt>null</tt>.
	 * @param skip the number of positively evaluated elements to skip.
	 * @param limit the maximum number of elements to be actually added. A value of <tt>null</tt> indicates no limit.
	 * @return this collection
	 * @throws ArrayIndexOutOfBoundsException if the values for <code>srcIndex</code> and <code>srcLength</code> or 
	 * their combination exceed <code>elements</code>'s bounds.  
	 */
	public XAddingCollection<E> add(E[] elements, int srcIndex, int srcLength,  Predicate<E> predicate, int skip, Integer limit);
	/**
	 * Adds all of the <code>srcLength</code> elements in array <code>elements</code> at <code>srcIndex</code> for which
	 * <code>predicate</code> applies to this collection up to a maximum of <code>limit</code> elements.
	 * The first <code>skip</code> elements to which <code>predicate</code> applies are skipped. Skipped elements
	 * to NOT count towards limit.
	 * <p>
	 * Additional information on parameters:<br>
	 * <ul>
	 * <li><code>elements</code></li> may not be <tt>null</tt>.
	 * <li><code>srcIndex</code> may not exceed <code>elements</code>'s bounds.</li>
	 * <li><code>srcLength</code> may be negative, causing the elements to be added in reversed order.</li>
	 * <li><code>predicate</code></li> may not be <tt>null</tt>.
	 * <li><code>skip</code> may not be negative.</li>
	 * <li><code>limit</code> may be <tt>null</tt> to indicate that no limiting shall be applied.</li>
	 * <li><code>limit</code> may be equal to or less than 0, resulting in an immediate return of this method.</li>
	 * </ul>
	 * 
	 * @param elements the source array containing the elements to be added.
	 * @param srcIndex the source array index of the first element to be (potentially) added. 
	 * @param srcLength the amount of elements to be (potentially) added.
	 * @param predicate the predicate to evaluate if an element shall be added. May not be <tt>null</tt>.
	 * @param skip the number of positively evaluated elements to skip.
	 * @param limit the maximum number of elements to be actually added. A value of <tt>null</tt> indicates no limit.
	 * @return this collection
	 * @throws ArrayIndexOutOfBoundsException if the values for <code>srcIndex</code> and <code>srcLength</code> or 
	 * their combination exceed <code>elements</code>'s bounds.  
	 */
	public XAddingCollection<E> add(E[] elements, int srcIndex, int srcLength, TPredicate<E> predicate, int skip, Integer limit);
	
	public XAddingCollection<E> add(Iterable<? extends E> elements, int srcOffset, Integer srcLength,  Predicate<E> predicate, int skip, Integer limit);	
	public XAddingCollection<E> add(Iterable<? extends E> elements, int srcOffset, Integer srcLength, TPredicate<E> predicate, int skip, Integer limit);
	
}
