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
//		final Thread currentThread = Thread.currentThread();
//		final int hash = System.identityHashCode(currentThread);
//		final Entry[] table = this.table;
//		Entry e = table[hash & LENGTH_MINUS_ONE];
//		
//		if(e == null){
//			synchronized(table) {
//				final E newInstance = this.instantiator.newInstance();
//				table[hash & LENGTH_MINUS_ONE] = new Entry(currentThread, newInstance);
//				return newInstance;	
//			}			
//		}
//		
//		synchronized(e) {
//			Thread entryThread;
//			do{
//				if((entryThread = e.keyThreadRef.get()) == currentThread){
//					return (E)e.value;				
//				}
//				else if(entryThread == null){
//					table[hash & LENGTH_MINUS_ONE] = (e = e.next);
//				}
//			}
//			while(e != null);
//			
//			
//			Entry nextEntry;
//			while((nextEntry = e.next) != null){
//				
//				if((entryThread = e.keyThreadRef.get()) == currentThread) break;
//				e = e.next;
//			}
//			if(e != null){
//				return (E)e.value;
//			}
//		}
		// (20.09.2010)FIXME: ThreadContext
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
