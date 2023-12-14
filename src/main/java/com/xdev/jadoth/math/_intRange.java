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
package com.xdev.jadoth.math;

import static com.xdev.jadoth.util.VarChar.SmallVarChar;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import com.xdev.jadoth.lang.exceptions.NumberRangeException;
import com.xdev.jadoth.lang.functional._intOperation;
import com.xdev.jadoth.lang.functional.controlflow._intControllingProcessor;
import com.xdev.jadoth.lang.signalthrows.ThrowBreak;
import com.xdev.jadoth.lang.signalthrows.ThrowContinue;
import com.xdev.jadoth.lang.signalthrows.ThrowReturn;
import com.xdev.jadoth.lang.types.JaTypes;


/**
 * The Class Range.
 * 
 * @author Thomas Muenz
 */
public class _intRange implements Set<Integer> 
{	
	///////////////////////////////////////////////////////////////////////////
	// instance fields //
	////////////////////
	
	/** The from. */
	private int from;
	
	/** The to. */
	private int to;
	
	/** The direction. */
	private int direction;
	
	
	
	///////////////////////////////////////////////////////////////////////////
	// constructors //
	/////////////////
	
	/**
	 * Instantiates a new range.
	 * 
	 * @param from the from
	 * @param to the to
	 */
	public _intRange(final int from, final int to) 
	{
		super();
		this.from = from;
		this.to = to;
		this.direction = this.to < this.from ?-1 :1;
	}

	
	
	///////////////////////////////////////////////////////////////////////////
	// override methods //
	/////////////////////	
	
	/**
	 * Adds the.
	 * 
	 * @param e the e
	 * @return true, if successful
	 * @return
	 */
	@Override
	public boolean add(final Integer e) 
	{
		if(e == null) return false;
		
		final int value = e;
		if(this.direction == 1){
			if(value < this.from){
				this.from = value;
				return true;
			}
			else if(value > this.to){
				this.to = value;
				return true;
			}
		}
		else {
			if(value > this.from){
				this.from = value;
				return true;
			}
			else if(value < this.to){
				this.to = value;
				return true;
			}
		}
		return false;
	}

	/**
	 * Adds the all.
	 * 
	 * @param c the c
	 * @return true, if successful
	 * @return
	 */
	@Override
	public boolean addAll(final Collection<? extends Integer> c) 
	{
		int to = this.to;
		int from = this.from;
		
		if(this.direction == 1){
			for(final Integer i : c){
				final int iVal = i.intValue();
				if(iVal < from){
					from = iVal;
				}
				else if(iVal > to){
					to = iVal;
				}
			}
		}
		else {
			for(final Integer i : c){
				final int iVal = i.intValue();
				if(iVal > from){
					from = iVal;
				}
				else if(iVal < to){
					to = iVal;
				}
			}
		}
		final boolean hasChanged = this.to != to || this.from != from; 
		this.from = from;
		this.to = to;
		return hasChanged;
	}

	/**
	 * 
	 * @see java.util.Set#clear()
	 */
	@Override
	public void clear() {
		this.from = 0;
		this.to = 0;
		this.direction = 1;		
	}

	/**
	 * Contains.
	 * 
	 * @param o the o
	 * @return true, if successful
	 * @return
	 */
	@Override
	public boolean contains(final Object o) 
	{
		if(o == null) return false;
		
		//handle common case first
		int i = 0;
		if(o instanceof Integer){
			i = (Integer)o;
		}
		else if(o instanceof Short){
			i = (Short)o;
		}
		else if(o instanceof Byte){
			i = (Byte)o;
		}
		else {
			try {			
				if(o instanceof Double){
					i = JaTypes.to_int((Double)o);				
				}
				else if(o instanceof Long){
					i = JaTypes.to_int((Double)o);
				}
				else if(o instanceof Float){
					i = JaTypes.to_int((Double)o);
				}
				else if(o instanceof AtomicInteger){
					i = JaTypes.to_int((AtomicInteger)o);
				}
				else if(o instanceof AtomicLong){
					i = JaTypes.to_int((AtomicLong)o);
				}
				else if(o instanceof BigDecimal){
					i = JaTypes.to_int((BigDecimal)o);
				}
				else if(o instanceof BigInteger){
					i = JaTypes.to_int((BigInteger)o);
				}
				else {
					return false; //all non-number objects can't be removed at all, so return false
				}
			}
			catch(final RuntimeException e) {
				return false;
			}	
		}	
		return this.containsInt(i);
	}
	
	/**
	 * Contains all.
	 * 
	 * @param c the c
	 * @return true, if successful
	 * @return
	 */
	@Override
	public boolean containsAll(final Collection<?> c) 
	{
		for(final Object o : c){
			if(!this.contains(o)) return false;
		}
		return true;
	}

	/**
	 * Checks if is empty.
	 * 
	 * @return true, if is empty
	 * @return
	 */
	@Override
	public boolean isEmpty() {
		return this.from == this.to;
	}

	/**
	 * Iterator.
	 * 
	 * @return the iterator
	 * @return
	 */
	@Override
	public Iterator<Integer> iterator() {
		return new RangeIterator();
	}

	/**
	 * Removes the.
	 * 
	 * @param o the o
	 * @return true, if successful
	 * @return
	 */
	@Override
	public boolean remove(final Object o) 
	{
		if(o == null) return false;
		
		//handle common case first
		int i = 0;
		if(o instanceof Integer){
			i = (Integer)o;
		}
		else if(o instanceof Short){
			i = (Short)o;
		}
		else if(o instanceof Byte){
			i = (Byte)o;
		}
		else if(o instanceof Character){
			i = (Character)o;
		}
		else {
			try {			
				if(o instanceof Double){
					i = JaTypes.to_int((Double)o);				
				}
				else if(o instanceof Long){
					i = JaTypes.to_int((Double)o);
				}
				else if(o instanceof Float){
					i = JaTypes.to_int((Double)o);
				}
				else if(o instanceof AtomicInteger){
					i = JaTypes.to_int((AtomicInteger)o);
				}
				else if(o instanceof AtomicLong){
					i = JaTypes.to_int((AtomicLong)o);
				}
				else if(o instanceof BigDecimal){
					i = JaTypes.to_int((BigDecimal)o);
				}
				else if(o instanceof BigInteger){
					i = JaTypes.to_int((BigInteger)o);
				}
				else {
					return false; //all non-number objects can't be removed at all, so return false
				}
			}
			catch(final RuntimeException e) {
				return false;
			}	
		}		
		return this.internalRemove(i);
	}
	
	/**
	 * Removes the all.
	 * 
	 * @param c the c
	 * @return true, if successful
	 * @return
	 */
	@Override
	public boolean removeAll(final Collection<?> c) 
	{
		boolean changed = false;
		for(final Object o : c){
			changed |= this.remove(o);
		}
		return changed;
	}

	/**
	 * Retain all.
	 * 
	 * @param c the c
	 * @return true, if successful
	 * @return
	 */
	@Override
	public boolean retainAll(final Collection<?> c) 
	{		
		int collectionMin = 0;
		int collectionMax = 0;
		
		//determine min and max values in collection c
		for(final Object o : c){
			if(o == null || !(o instanceof Number)) continue;
			int i = 0;
			try {
				i = JaTypes.to_int((Number)o);
			}
			catch(final NumberRangeException e) {
				final long l = ((Number)o).longValue();
				if(l > Integer.MAX_VALUE){
					collectionMax = Integer.MAX_VALUE;
				}
				else if(l < Integer.MIN_VALUE){
					collectionMax = Integer.MIN_VALUE;
				}
				continue;
			}
			if(i < collectionMin){
				collectionMin = i;
			}
			else if(i > collectionMax){
				collectionMax = i;
			}
		}
				
		//depending on direction, determine if min and/or max is inside the current range
		boolean changed = false;
		if(this.direction == 1){			
			if(collectionMin > this.from){
				this.from = collectionMin;
				changed = true;
			}
			if(collectionMax < this.to){
				this.to = collectionMax;
				changed = true;
			}
		}
		else {
			if(collectionMin < this.from){
				this.from = collectionMin;
				changed = true;
			}
			if(collectionMax > this.to){
				this.to = collectionMax;
				changed = true;
			}
		}
		return changed;
	}

	/**
	 * Size.
	 * 
	 * @return the int
	 * @return
	 */
	@Override
	public int size() {
		return Math.abs(this.to - this.from) + 1;
	}

	/**
	 * To array.
	 * 
	 * @return the integer[]
	 * @return
	 */
	@Override
	public Integer[] toArray() 
	{
		final Integer[] array = new Integer[this.size()];
		final int to = this.to;
		final int dir = this.direction;
		
		int i = 0;
		int loopValue = this.from;		
		while(true){
			array[i++] = loopValue;
			if(loopValue == to) break;
			loopValue += dir;			
		}		
		return array;
	}
	
	/**
	 * To array.
	 * 
	 * @param <T> the generic type
	 * @param a the a
	 * @return the t[]
	 */
	@SuppressWarnings("unchecked")
	@Override
	public <T> T[] toArray(final T[] a) 
	{
		if(!(a instanceof Integer[])){
			throw new IllegalArgumentException("Array must be of type "+Integer.class.getSimpleName()+"[]");
		}
		final int size = this.size();
		final Integer[] array = a.length >= size ?(Integer[])a : new Integer[size];
		final int to = this.to;
		final int dir = this.direction;
		
		int i = 0;
		int loopValue = this.from;		
		while(true){
			array[i++] = loopValue;
			if(loopValue == to) break;
			loopValue += dir;			
		}		
		return (T[])array;
	}
	
	/**
	 * @return
	 */
	@Override
	public String toString()
	{
		return SmallVarChar().append('[').append(this.from).append(';').append(this.to).append(']').toString();
	}
	
	
	
	
	///////////////////////////////////////////////////////////////////////////
	// declared methods //
	/////////////////////
	
	/**
	 * Contains.
	 * 
	 * @param i the i
	 * @return true, if successful
	 */
	public boolean contains(final int i)
	{
		return this.containsInt(i);
	}
	
	/**
	 * Contains int.
	 * 
	 * @param i the i
	 * @return true, if successful
	 */
	private boolean containsInt(final int i)
	{
		return this.direction > 0 && i >= this.from && i <= this.to 
		    || this.direction < 0 && i <= this.from && i >= this.to
		;
	}
	
	public int[] toArray_int() 
	{
		final int[] array = new int[this.size()];
		final int to = this.to;
		final int dir = this.direction;
		
		int i = 0;
		int loopValue = this.from;		
		while(true){
			array[i++] = loopValue;
			if(loopValue == to) break;
			loopValue += dir;			
		}		
		return array;
	}
	
	public void process(final _intOperation processor)
	{
		final int to = this.to;
		final int dir = this.direction;
		
		int loopValue = this.from;		
		while(true){
			processor.execute(loopValue);
			if(loopValue == to) break;
			loopValue += dir;			
		}
	}
	
	/**
	 * {@link ThrowContinue} causes the loop to remain at the current value.
	 * {@link ThrowBreak} causes the processing to stop immediately.
	 * {@link ThrowReturn} causes the processing to stop immediately.
	 * @param processor
	 */
	public void process(final _intControllingProcessor processor)
	{
		final int to = this.to;
		final int dir = this.direction;
		
		int loopValue = this.from;		
		
		try {
			while(true){
				try {
					processor.process(loopValue);
				}				
				catch(final ThrowContinue e) { continue; }				
				if(loopValue == to) break;
				loopValue += dir;			
			}
		}
		catch(final ThrowBreak e)  { return; }
		catch(final ThrowReturn e) { return; }
	}
	
	private boolean internalRemove(final int value)
	{
		if(this.from == value){
			if(this.direction == 1){
				this.from++;
			}
			else {
				this.from--;
			}
			return true;
		}
		else if(this.to == value){
			if(this.direction == 1){
				this.to--;
			}
			else {
				this.to++;
			}
			return true;
		}
		return false;
	}
	
	
	///////////////////////////////////////////////////////////////////////////
	// inner classes //
	//////////////////	
	
	/**
	 * The Class RangeIterator.
	 */
	private class RangeIterator implements Iterator<Integer>
	{
		
		/** The cursor. */
		private int cursor = _intRange.this.from;
		
		/**
		 * Checks for next.
		 * 
		 * @return true, if successful
		 * @return
		 */
		@Override
		public boolean hasNext() 
		{
			if(_intRange.this.direction == 1){
				return this.cursor <= _intRange.this.to;
			}
			return this.cursor >= _intRange.this.to;
		}

		/**
		 * Next.
		 * 
		 * @return the integer
		 * @return
		 */
		@Override
		public Integer next() 
		{
			//funny, isn't it?
			try{
				return this.cursor;
			}
			finally {
				this.cursor += _intRange.this.direction;
			}
		}

		/**
		 * Makes no sense as arbitrary element of a range cannot be removed
		 * @see java.util.Iterator#remove()
		 */
		@Override
		public void remove() 
		{
			//Makes no sense as arbitrary element of a range cannot be removed		
		}
		
	}
}


