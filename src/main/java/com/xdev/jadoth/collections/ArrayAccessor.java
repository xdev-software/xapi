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


/**
 * A {@link XList} implementation view on an array.<br>
 * <br>
 * Note that any interface method that would have to modify the length of the array (add, remove, insert, clear, etc.)
 * will throw an {@link UnsupportedOperationException} without further ado.
 *
 * @author Thomas Muenz
 *
 */
public class ArrayAccessor<E> //implements XGetSetList<E>, List<E>
{
	// (13.09.2010 TM)FIXME: copy from GrowList when complete and replace length modifying code with throw ~Exception

	///////////////////////////////////////////////////////////////////////////
	// instance fields //
	////////////////////

	private E[] data;



	///////////////////////////////////////////////////////////////////////////
	// constructors //
	/////////////////

	/**
	 * @param data
	 */
	public ArrayAccessor(final E[] data)
	{
		super();
		this.data = data;
	}



	///////////////////////////////////////////////////////////////////////////
	// getters          //
	/////////////////////

	/**
	 *
	 * @return
	 */

	public E[] getArray()
	{
		return this.data;
	}



	///////////////////////////////////////////////////////////////////////////
	// setters          //
	/////////////////////

	/**
	 * @param array the data to set
	 */
	public void setArray(final E[] array)
	{
		this.data = array;
	}

	public ArrayAccessor<E> wrap(final E[] array)
	{
		this.data = array;
		return this;
	}


	///////////////////////////////////////////////////////////////////////////
	// declared methods //
	/////////////////////

	public GrowList<E> toGrowList()
	{
		return new GrowList<E>(this.data);
	}

//	private String excRangeString(final int startIndex, final int endIndex)
//	{
//		return "Range ["+endIndex+';'+startIndex+"] not in [0;"+(this.data.length-1)+"].";
//	}



	///////////////////////////////////////////////////////////////////////////
	// override methods //
	/////////////////////


}
