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

import java.lang.ref.WeakReference;

import com.xdev.jadoth.lang.reflection.Instantiator;


/**
 * @author Thomas Muenz
 *
 */
public class ThreadContext<E>
{
	///////////////////////////////////////////////////////////////////////////
	// constants        //
	/////////////////////
	
	private static final int LENGTH = 128;
	private static final int LENGTH_MINUS_ONE = LENGTH-1;
	
	
	
	///////////////////////////////////////////////////////////////////////////
	// instance fields  //
	/////////////////////
	
	private Entry[] table = new Entry[LENGTH];
	private final Instantiator<E> instantiator;
	
	
	
	///////////////////////////////////////////////////////////////////////////
	// constructors     //
	/////////////////////
	
	public ThreadContext(final Instantiator<E> instantiator)
	{
		super();
		this.instantiator = instantiator;
	}

	
	
	///////////////////////////////////////////////////////////////////////////
	// declared methods //
	/////////////////////

	@SuppressWarnings("unchecked")
	public E get()
	{
		return null;		
	}
	
}



final class Entry 
{
	WeakReference<Thread> keyThreadRef;
	Object value;
	Entry prev;
	Entry next;
	
	Entry(final Thread thread, final Object value)
	{
		super();
		this.keyThreadRef = new WeakReference<Thread>(thread);
		this.value = value;
		this.prev = null;
		this.next = null;
	}
	
	Entry(final Entry prev, final Entry next, final Thread thread, final Object value)
	{
		super();
		this.keyThreadRef = new WeakReference<Thread>(thread);
		this.value = value;
		this.prev = prev;
		this.next = next;
	}
	
}
