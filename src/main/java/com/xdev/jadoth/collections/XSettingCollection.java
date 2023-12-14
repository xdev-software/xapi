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
public interface XSettingCollection<E> extends XGettingCollection<E>
{
	public interface Factory<E> extends XGettingCollection.Factory<E>
	{
		@Override
		public XSettingCollection<E> createInstance();
	}


	public XSettingCollection<E> execute(Operation<E> operation);
	public XSettingCollection<E> execute(TOperation<E> operation);

	public XSettingCollection<E> process(Operation<E> operation);
	public XSettingCollection<E> process(TOperation<E> operation);


	/**
	 * 
	 * @param oldElement
	 * @param newElement
	 * @return a value greater than or equal to 0 if an element has been found and replaced, a negative value otherwise.
	 */
	public int replaceOne(E oldElement, E newElement);
	/**
	 * 
	 * @param oldElement
	 * @param newElement
	 * @param equalator
	 * @return a value greater than or equal to 0 if an element has been found and replaced, a negative value otherwise.
	 */
	public int replaceOne(E oldElement, E newElement, Equalator<E> equalator);
	/**
	 * 
	 * @param oldElement
	 * @param newElement
	 * @return the amount of replacements that has been executed by the call of this method.
	 */
	public int replace(E oldElement, E newElement);
	/**
	 * 
	 * @param oldElement
	 * @param newElement
	 * @param equalator
	 * @return the amount of replacements that has been executed by the call of this method.
	 */
	public int replace(E oldElement, E newElement, Equalator<E> equalator);
	/**
	 * 
	 * @param oldElement
	 * @param newElement
	 * @param limit
	 * @return the amount of replacements that has been executed by the call of this method.
	 */
	public int replace(E oldElement, E newElement, int skip, Integer limit);
	/**
	 * 
	 * @param oldElement
	 * @param newElement
	 * @param limit
	 * @param equalator
	 * @return the amount of replacements that has been executed by the call of this method.
	 */
	public int replace(E oldElement, E newElement, int skip, Integer limit, Equalator<E> equalator);



	//ignores null values in multiples
	
	/**
	 * 
	 * @param replacementMapping
	 * @return the amount of replacements that has been executed by the call of this method.
	 */
	public int replaceAll(Map<E, E> replacementMapping);
	
	public int replaceAll(E[] oldElements, E newElement);
	public int replaceAll(E[] oldElements, E newElement, Equalator<E> equalator);
	public int replaceAll(XGettingCollection<E> oldElements, E newElement);
	public int replaceAll(XGettingCollection<E> oldElements, E newElement, Equalator<E> equalator);
	
	public int replaceAll(E[] oldElements, E newElement, int skip, Integer limit);
	public int replaceAll(E[] oldElements, E newElement, int skip, Integer limit, Equalator<E> equalator);
	public int replaceAll(XGettingCollection<E> oldElements, E newElement, int skip, Integer limit);
	public int replaceAll(XGettingCollection<E> oldElements, E newElement, int skip, Integer limit, Equalator<E> equalator);

	public int replaceOne( Predicate<E> predicate, E newElement);
	public int replaceOne(TPredicate<E> predicate, E newElement);

	public int replace( Predicate<E> predicate, E newElement);
	public int replace(TPredicate<E> predicate, E newElement);
	public int replace( Predicate<E> predicate, E newElement, int skip, Integer limit);
	public int replace(TPredicate<E> predicate, E newElement, int skip, Integer limit);
}
