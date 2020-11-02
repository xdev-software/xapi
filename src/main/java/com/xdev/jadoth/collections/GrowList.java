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

import static com.xdev.jadoth.Jadoth.EQUALS;
import static com.xdev.jadoth.collections.JaArrays.removeAllFromArray;
import static com.xdev.jadoth.collections.JaCollections.containsNull;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.ConcurrentModificationException;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Random;
import java.util.RandomAccess;

import com.xdev.jadoth.lang.Equalator;
import com.xdev.jadoth.lang.functional.Operation;
import com.xdev.jadoth.lang.functional.Predicate;
import com.xdev.jadoth.lang.functional.aggregates.Aggregate;
import com.xdev.jadoth.lang.functional.aggregates.TAggregate;
import com.xdev.jadoth.lang.functional.controlflow.TOperation;
import com.xdev.jadoth.lang.functional.controlflow.TPredicate;
import com.xdev.jadoth.lang.signalthrows.ThrowBreak;
import com.xdev.jadoth.lang.signalthrows.ThrowContinue;
import com.xdev.jadoth.lang.signalthrows.ThrowReturn;
import com.xdev.jadoth.math.JaMath;
import com.xdev.jadoth.util.VarChar;



/**
 * Advanced and faster implementation of {@link java.util.ArrayList}.<br>
 * For achieving best performance for lists being heavily structually manipulated, use {@link VarList}.
 * <p>
 * This implementation is NOT synchronized and thus should only be used by a
 * single thread or in a thread-safe manner (i.e. read-only as soon as multiple threads access it).<br>
 * Performance gain compared to Sun's JDK ArrayList implementation is around 10% to 20%
 * (memory use is 33% more in worst case, 50% less in best case, slightly more in average case)
 * <p>
 * Note that this List implementation does NOT keep track of modification count as Sun's collection implementations do
 * (and thus never throws a {@link ConcurrentModificationException}), for two reasons:<br>
 * 1.) It is already explicitely declared thread-unsafe and for single-thread (or thread-safe)
 * use only.<br>
 * 2.) The common modCount-concurrency exception behaviour ("failfast") throws {@link ConcurrentModificationException}
 * even in single thread use, i.e. when iterating over a collection and removing more than one element of it
 * without using the iterator's method.<br>
 * <br>
 * Current conclusion is that the JDK's failfast implementations buy unneeded (and even unreliable as stated by
 * official guides) currency modification recognition at the cost of performance loss and even a bug when already used
 * safely.<br>
 * If this kind of concurrent modification recognition is needed, it is recommended to use {@link java.util.ArrayList}.
 * <p>
 * Additionally, this class implements the extended List {@link XList} interface, thus providing higher order
 * functionality like {@link #execute(Operation)} or {@link #reduce(Predicate)}.
 *
 * @author Thomas Muenz
 */
public final class GrowList<E> implements XList<E>
{
	///////////////////////////////////////////////////////////////////////////
	// constants        //
	/////////////////////

	// have to be 2^n values
	static final int MINIMUM_DATA_SIZE =  8;
	static final int DEFAULT_DATA_SIZE = 32; //array allocation takes about the same time up to a size of 32
	static final int MAXIMUM_POW2_SIZE = 1<<30; //the highest possible int value that can be reached by bit shifting

	// internal marker object for marking to be removed bucckets for batch removal
	private static final Object REMOVE_MARKER = new Object();



	///////////////////////////////////////////////////////////////////////////
	// static methods   //
	/////////////////////

	private static final int calcCapacity(final int n)
	{
		if(n > MAXIMUM_POW2_SIZE){
			return Integer.MAX_VALUE; //cannot be reached by shifting
		}
		int p2 = MINIMUM_DATA_SIZE;
		while(p2 < n){
			p2 <<= 1;
		}
		return p2;
	}



	///////////////////////////////////////////////////////////////////////////
	// instance fields //
	////////////////////

	private Object[] data;
	private int size;



	///////////////////////////////////////////////////////////////////////////
	// constructors //
	/////////////////

	/**
	 * Instantiates a new {@link GrowList} object with the default capacity.
	 */
	public GrowList()
	{
		super();
		this.data = new Object[DEFAULT_DATA_SIZE];
		this.size = 0;
	}
	/**
	 * Instantiates a new {@link GrowList} object with a given minimal capacity.<br>
	 * Note that the actual capacity will propably be higher, as capacity ist always padded to be a 2^n value.
	 *
	 * @param minimalCapacity the desired minimal capacity
	 */
	public GrowList(final int minimalCapacity)
	{
		super();
		// will default to MINIMUM_DATA_SIZE and apart from that: is indicating those errors really task of this class?
//		if(initialCapacity < 0){
//			throw new IllegalArgumentException("initial capacity may not be negative: "+initialCapacity);
//		}
		this.data = new Object[calcCapacity(minimalCapacity)];
		this.size = 0;
	}
	/**
	 *
	 * @param original
	 * @throws NullPointerException
	 */
	public GrowList(final GrowList<? extends E> original) throws NullPointerException
	{
		super();
		this.size = original.size;
		this.data = new Object[original.data.length];
		System.arraycopy(original.data, 0, this.data, 0, original.size);
	}
	/**
	 *
	 * @param collection
	 * @throws NullPointerException
	 */
	public GrowList(final Collection<? extends E> collection) throws NullPointerException
	{
		this(collection.size());
		this.addAll(collection);
	}
	/**
	 *
	 * @param elements
	 * @throws NullPointerException
	 */
	public GrowList(final E... elements) throws NullPointerException
	{
		super();
		this.size = elements.length;
		this.data = new Object[calcCapacity(elements.length)];
		System.arraycopy(elements, 0, this.data, 0, this.size);
	}
	/**
	 * Note that the passed array MUST have a 2^n length. Otherwise the list will crash/fail/whatever
	 *
	 * @param internalData
	 */
	GrowList(final Object[] internalData, final int size)
	{
		super();
		this.data = internalData;
		this.size = size;
	}



	///////////////////////////////////////////////////////////////////////////
	// getters          //
	/////////////////////

	/**
	 * @return the data
	 */
	Object[] getData()
	{
		return this.data;
	}

	/**
	 * @return the size
	 */
	int getSize()
	{
		return this.size;
	}



	///////////////////////////////////////////////////////////////////////////
	// setters          //
	/////////////////////

	/**
	 * @param size the size to set
	 */
	void setSize(final int size)
	{
		this.size = size;
	}

	/**
	 *
	 * @param data
	 */
	void setData(final Object[] data)
	{
		this.data = data;
	}

	/**
	 *
	 * @param data
	 * @param size
	 */
	void setContent(final Object[] data, final int size)
	{
		this.data = data;
		this.size = size;
	}



	///////////////////////////////////////////////////////////////////////////
	// declared methods //
	/////////////////////

	private int calculateCapacityIncrease(final int increase)
	{
		if(Integer.MAX_VALUE - increase < this.size){
			throw new ArrayIndexOutOfBoundsException(this.exceptionStringArrayLengthExceeded(increase));
		}
		final int newSize = this.size + increase;
		if(newSize > MAXIMUM_POW2_SIZE){
			return Integer.MAX_VALUE; //cannot be reached by shifting
		}
		int c = this.data.length;
		while(c < newSize){
			c <<= 1;
		}
		return c;
	}

	private void internalSwap(final int i, final int j)
	{
		final Object[] data = this.data; //cache variable really helps, mostly for writing accesses
		final Object t = data[i];
		data[i] = data[j];
		data[j] = t;
	}

	/* (06.10.2010)NOTE:
	 * all uses of this method could be optimized by local iterative "free = data.length - size"
	 * plus "if(free == 0){enlarge}" lookaheads
	 */
	private void internalAdd(final Object e)
	{
		if(this.size == this.data.length){
			//expandArray()
			final int length = this.data.length;
			final Object[] data = new Object[length == MAXIMUM_POW2_SIZE ?Integer.MAX_VALUE : length<<1];
			System.arraycopy(this.data, 0, data, 0, length);
			this.data = data;
		}
		this.data[this.size++] = e;
	}

	private void internalInsertArray(final int index, final Object[] elements, final int elementsSize)
	{
		Object[] data = this.data;
		final int targetCapacity = this.calculateCapacityIncrease(elementsSize);
		if(targetCapacity > data.length){
			data = new Object[targetCapacity];
			/* copy elements in two steps:
			 * 1.) [    0; index] -> [          0;      index]
			 * 2.) [index; size ] -> [index+space; size+space]
			 *
			 * So it looks like this:
			 * ----1.)----       ----2.)----
			 * |||||||||||_______|||||||||||
			 * where this ^^^^^^^ is exactely enough space for inserting "elements"
			 *
			 * this way, all to be moved elements are only copied once
			 */
			System.arraycopy(this.data, 0, data, 0, index);
			System.arraycopy(this.data, index, data, index + elementsSize, this.size - index);
			this.data = data;
		}
		else {
			// free up enough space at index
			System.arraycopy(data, index, data, index + elementsSize, this.size - index);
		}

		System.arraycopy(elements, 0, data, index, elementsSize);
		this.size += elementsSize;
	}

	private String exceptionStringRange(final int startIndex, final int endIndex)
	{
		return "Range ["+endIndex+';'+startIndex+"] not in [0;"+(this.size-1)+"]";
	}

	private String exceptionStringSkipNegative(final int skip)
	{
		return "Skip count may not be negative: "+skip;
	}

	private String exceptionStringIndexOutOfBounds(final int index)
	{
		return "Index: "+index+", Size: "+this.size;
	}

	private String exceptionStringArrayLengthExceeded(final int desiredIncrease)
	{
		return "Required array size (" + ((long)desiredIncrease + this.size) + ") exceeds maximum java array size";
	}

	private String exceptionStringIterableOffsetNegative(final int srcOffset)
	{
		return "Iterable offset count may not be negative: "+srcOffset;
	}

	private String exceptionStringIterableLengthNegative(final int length)
	{
		return "Iterable length count may not be negative: "+length;
	}

	private String exceptionStringIllegalSwapBounds(final int indexA, final int indexB, final int length)
	{
		return "Illegal swap bounds: ("+indexA+"["+length+"] -> "+indexB+"["+length+"]) in range [0;"+this.size+"]";
	}

	/**
	 * Insertionsort is faster than Quick- or Mergesort for very small arrays.<br>
	 * Even though the implementations for those two more complex algorithms use a built-in Insertionsort for small
	 * lists (or sub lists) below a certain threshold length, it can still be faster for a GrowList to be sorted
	 * by Insertionsort explictely for the following reasons:
	 * <ul>
	 * <li>No threshold check has to be performed at all.</li>
	 * <li>Threshold for built-in Insertionsort decision is hardcoded, but depends on hardware,
	 * meaning future hardware can achieve better results with Insertionsort for higher thresholds.</li>
	 * <li>No method call and argument passing (array, startIndex, endIndex) needed, instead hardcoded bounds in
	 * Insertionsort's loop</li>
	 * </ul>
	 */
	@SuppressWarnings("unchecked")
	public void sortInsertion(final Comparator<E> comparator)
	{
		final Object[] data = this.data;
		for(int i = 0; i <= this.size; i++){
			for(int j = i; j != 0 && comparator.compare((E)data[j-1], (E)data[j]) > 0; j--){
				final Object t = data[j];
				data[j] = data[j-1];
				data[j-1] = t;
			}
		}
	}

	/**
	 *
	 * @param c
	 * @return
	 */
	public boolean addAll(final GrowList<? extends E> c)
	{
		if(c.size == 0) return false;

		final int targetCapacity = this.calculateCapacityIncrease(c.size);
		if(targetCapacity > this.data.length){
			final Object[] data = new Object[targetCapacity];
			System.arraycopy(this.data, 0, data, 0, this.size);
			this.data = data;
		}

		System.arraycopy(c.data, 0, this.data, this.size, c.size);
		this.size += c.size;
		return true;
	}

	/**
	 *
	 * @param index
	 * @param c
	 * @return
	 */
	public boolean addAll(final int index, final GrowList<? extends E> c)
	{
		if(index > this.size){
			throw new IndexOutOfBoundsException(this.exceptionStringIndexOutOfBounds(index));
		}
		if(index < 0){
			throw new ArrayIndexOutOfBoundsException(index);
		}
		if(c.size == 0) return false;
		if(index == this.size){
			return this.addAll(c);
		}

		this.internalInsertArray(index, c.data, c.size);
		return true;
	}

	/**
	 *
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<E> toArrayList()
	{
		// include no buffer here as this conversion will mostly be needed for reading the existing data
		final ArrayList<E> al = new ArrayList<E>(this.size);

		// intentionally don't use ArrayList.addAll() here as it creates an additional array copy.
		final Object[] data = this.data;
		for(int i = 0, size = this.size; i < size; i++){
			al.add((E)data[i]); //will never have to extend the elementData array as initialCapacity fits size.
		}
		return al;
	}

	/**
	 * Shrinks the buckets to the smallest possible size.<br>
	 * Note that this may not be the same as the element count due to implementation details
	 */
	public GrowList<E> shrink()
	{
		final int desiredDataLength = calcCapacity(this.size);
		if(desiredDataLength == this.data.length) return this;

		//expandArray() (sort of)
		final Object[] data = new Object[desiredDataLength];
		if(this.size > 0){
			System.arraycopy(this.data, 0, data, 0, this.size);
		}
		this.data = data;

		return this;
	}

	/**
	 *
	 * @param minCapacity
	 * @return
	 */
	public GrowList<E> ensureCapacity(final int minCapacity)
	{
		if(minCapacity <= this.data.length) return this;

		final Object[] data = new Object[calcCapacity(minCapacity)];
		System.arraycopy(this.data, 0, data, 0, this.size);
		this.data = data;

		return this;
	}

	/**
	 *
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public VarList<E> toVarList()
	{
		// (14.09.2010 TM)XXX: overhaul to use a more efficient method when available
		final VarList<E> vl = new VarList<E>(this.size);
		final Object[] data = this.data;
		for(int i = 0, size = this.size; i < size; i++){
			vl.add((E)data[i]);
		}
		return vl;
	}



	///////////////////////////////////////////////////////////////////////////
	// override methods //
	/////////////////////

	/**
	 * @param obj
	 * @return
	 */
	@Deprecated
	@Override
	public boolean equals(final Object o)
	{
		//trivial escape conditions
		if(o == this) return true;
		if(o == null || !(o instanceof List<?>)) return false;

		final List<?> list = (List<?>)o;
		if(this.size != list.size()) return false; //lists can only be equal if they have the same length

		final Object[] data = this.data;
		int i = 0;
		for(final Object e2 : list) { //use iterator for passed list as it could be a non-random-access list
			final Object e1 = data[i++];
			if(e1 == null){ //null-handling escape conditions
				if(e1 != null) return false;
				continue;
			}
			if(!e1.equals(e2)) return false;
		}
		return true; //no un-equal element found, so lists must be equal
	}

	/**
	 * @return
	 */
	@Deprecated
	@Override
	public int hashCode()
	{
		final Object[] data = this.data;
		int hashCode = 1;

		for(int i = 0, size = this.size; i < size; i++){
			final Object obj = data[i];
			hashCode = 31*hashCode + (obj==null ? 0 : obj.hashCode()); //copied from java.util.ArrayList
		}
		return hashCode;
	}

    /**
     * Returns a string representation of this list.  The string
     * representation consists of a list of the collection's elements in the
     * order they are contained in this list, enclosed in square brackets
     * (<tt>"[]"</tt>).  Adjacent elements are separated by the characters
     * <tt>", "</tt> (comma and space). Elements are converted to strings as
     * by {@link String#valueOf(Object)}.
     * <p>
     * If an element is this list itself, then the string produced by {@link Object#toString()} for this list
     * is used as its elemental string representation.
     *
     * @return a string representation of this collection
     */
	@Override
	public String toString()
	{
		final int size = this.size;
		if(size == 0){
			// array causes problems with escape condition otherwise
			return "[]";
		}

		final VarChar vc = new VarChar(size >= 1<<28 ?Integer.MAX_VALUE :size<<3).append('[');
		final Object[] data = this.data;
		Object e;
		for(int i = 0; i < size; i++){
			e = data[i];
			vc.append(e == this ?super.toString() :e).append(',', ' ');
		}
		vc.deleteLastChar();
		vc.setLastChar(']');
		return vc.toString();
	}

	/**
	 * @param e
	 * @return
	 */
	@Override
	public boolean add(final E e)
	{
		if(this.size == this.data.length){
			//expandArray()
			final int length = this.data.length;
			final Object[] data = new Object[length == MAXIMUM_POW2_SIZE ?Integer.MAX_VALUE : length<<1];
			System.arraycopy(this.data, 0, data, 0, length);
			this.data = data;
		}
		this.data[this.size++] = e;
		return true;
	}

	/**
	 * @param index
	 * @param element
	 */
	@Override
	public void add(final int index, final E element) throws IndexOutOfBoundsException
	{
		if(index >= this.size || index < 0){
			if(index == this.size){
				this.add(element);
				return;
			}
			throw new IndexOutOfBoundsException(this.exceptionStringIndexOutOfBounds(index));
		}

		Object[] data = this.data;
		if(this.size == data.length){
			//expandArray()
			final int length = this.data.length;
			data = new Object[length == MAXIMUM_POW2_SIZE ?Integer.MAX_VALUE : length<<1];
			System.arraycopy(this.data, 0, data, 0, index);
			//between here is exactely one free space at data[index]
			System.arraycopy(this.data, index, data, index+1, this.size - index);
			this.data = data;
		}
		else {
			System.arraycopy(data, index, data, index+1, this.size - index);
		}
		data[index] = element;
		this.size++;
	}

	/**
	 * @param c
	 * @return
	 */
	@Override
	public boolean addAll(final Collection<? extends E> c)
	{
		int size = this.size;
		Object[] data = this.data;
		final int cSize = c.size();
		if(cSize == 0) return false;

		final int targetCapacity = this.calculateCapacityIncrease(cSize);
		if(targetCapacity > data.length){
			data = new Object[targetCapacity];
			System.arraycopy(this.data, 0, data, 0, size);
			this.data = data;
		}

		for(final E ec: c){
			data[size++] = ec;
		}
		this.size = size;
		return true;
	}

	/**
	 * @param index
	 * @param c
	 * @return
	 */
	@Override
	public boolean addAll(final int index, final Collection<? extends E> c)
	{
		int size = this.size;
		if(index == size){
			return this.addAll(c);
		}
		else if(index > size || index < 0){
			throw new IndexOutOfBoundsException(this.exceptionStringIndexOutOfBounds(index));
		}

		final int cSize = c.size(); //just in case c's implementation has some stupid way to determine size
		if(cSize == 0) return false;

		if(c instanceof GrowList<?>){
			final GrowList<?> gl = (GrowList<?>)c;
			if(Integer.MAX_VALUE - cSize > this.size){
				throw new ArrayIndexOutOfBoundsException(this.exceptionStringArrayLengthExceeded(cSize));
			}
			this.internalInsertArray(index, gl.data, cSize);
			return true;
		}

		Object[] data = this.data;
		final int targetCapacity = this.calculateCapacityIncrease(cSize);
		if(targetCapacity > data.length){
			data = new Object[targetCapacity];
			System.arraycopy(this.data, 0, data, 0, index);
			System.arraycopy(this.data, index, data, index+cSize, size-index);
			this.data = data;
		}
		else {
			// make room for all of c's elements
			System.arraycopy(data, index, data, index+cSize, size-index);
		}

		// insert c's elements
		for(final E e : c){
			data[size++] = e;
		}
		this.size = size;
		return true;
	}

	/**
	 * Clears the list by subsequently setting all storage slots to <tt>null</tt>.<br>
	 * Note that {@link #truncate()} is considerably faster for large lists.
	 *
	 * @see #truncate()
	 */
	@Override
	public void clear()
	{
		final Object[] data = this.data;
		for(int i = this.size; i --> 0;){
			data[i] = null;
		}
		this.size = 0;
	}

	/**
	 * @param o
	 * @return
	 */
	@Deprecated
	@Override
	public boolean contains(final Object o)
	{
		final Object[] data = this.data;
		if(o == null){
			for(int i = 0, size = this.size; i < size; i++){
				if(data[i] == null) return true;
			}
		}
		else {
			for(int i = 0, size = this.size; i < size; i++){
				if(o.equals(data[i])) return true;
			}
		}
		return false;
	}

	/**
	 * @param c
	 * @return
	 */
	@Deprecated
	@Override
	public boolean containsAll(final Collection<?> c)
	{
		final Object[] data = this.data;
		final int size = this.size;
		boolean nullAreadyHandled = false;
		collectionLoop: for(final Object o : c) {
			if(o == null){
				if(nullAreadyHandled) continue;
				nullAreadyHandled = true;
				for(int i = 0; i < size; i++){
					if(data[i] == null) continue collectionLoop;
				}
			}
			else {
				for(int i = 0; i < size; i++){
					if(o.equals(data[i])) continue collectionLoop;
				}
			}
			return false;
		}
		//all elements of c have been found
		return true;
	}

	/**
	 * @param index
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Override
	public E get(final int index) throws ArrayIndexOutOfBoundsException
	{
		if(index >= this.size){
			throw new ArrayIndexOutOfBoundsException(index);
		}
		return (E)this.data[index];
	}

	/**
	 * @param o
	 * @return
	 */
	@Deprecated
	@Override
	public int indexOf(final Object o)
	{
		if(o == null){
			return this.indexOfId(null);
		}
		final Object[] data = this.data;
		for(int i = 0, size = this.size; i < size; i++){
			if(o.equals(data[i])){
				return i;
			}
		}
		return -1;
	}

	/**
	 * @return
	 */
	@Override
	public boolean isEmpty()
	{
		return this.size == 0;
	}

	/**
	 * @return
	 */
	@Override
	public Iterator<E> iterator()
	{
		return new Itr();
	}

	/**
	 * @param o
	 * @return
	 */
	@Deprecated
	@Override
	public int lastIndexOf(final Object o)
	{
		final Object[] data = this.data;
		if(o == null){
			for(int i = this.size; i --> 0;){
				if(data[i] == null){
					return i;
				}
			}
		}
		else {
			for(int i = this.size; i --> 0;){
				if(o.equals(data[i])){
					return i;
				}
			}
		}
		return -1;
	}

	/**
	 * @return
	 */
	@Override
	public ListIterator<E> listIterator()
	{
		return new ListItr();
	}

	/**
	 * @param index
	 * @return
	 */
	@Override
	public ListIterator<E> listIterator(final int index)
	{
		return new ListItr(index);
	}

	/**
	 * @param o
	 * @return
	 */
	@Deprecated
	@Override
	public boolean remove(final Object o)
	{
		return removeAllFromArray(o, this.data, 0, this.size, this.data, 0, this.size, EQUALS) > 0;
	}

	/**
	 * @param index
	 * @return
	 */
	@Override
	public E remove(final int index) throws IndexOutOfBoundsException, ArrayIndexOutOfBoundsException
	{
		if(index >= this.size){
			throw new IndexOutOfBoundsException(this.exceptionStringIndexOutOfBounds(index));
		}
		@SuppressWarnings("unchecked")
		final E oldValue = (E)this.data[index];

		final int numMoved = this.size-1 - index;
		if (numMoved > 0){
			System.arraycopy(this.data, index+1, this.data, index, numMoved);
		}
		this.data[--this.size] = null;

		return oldValue;
	}

	/**
	 * @param c
	 * @return
	 */
	@Deprecated
	@Override
	public boolean removeAll(final Collection<?> c)
	{
		final int removed = removeAllFromArray(c, this.data, 0, this.size, this.data, 0, this.size, EQUALS);
		this.size -= removed;
		return removed != 0;
	}

	/**
	 * @param c
	 * @return
	 */
	@Deprecated
	@Override
	public boolean retainAll(final Collection<?> c)
	{
		final Object[] data = this.data;
		final int size = this.size;

		final int removedCount;
		try{
			for(int i = 0; i < size; i++){
				if(!c.contains(data[i])){
					data[i] = REMOVE_MARKER;
				}
			}
		}
		finally{
			removedCount = JaArrays.removeAllFromArray(REMOVE_MARKER, this.data, 0, size, this.data, 0, size);
		}
		this.size -= removedCount;
		return removedCount != 0;
	}

	/**
	 * @return
	 */
	@Override
	public int removeDuplicates(final boolean keepMultipleNullValues, final Equalator<E> equalator)
	{
		return this.rngRemoveDuplicates(0, this.size-1, keepMultipleNullValues, equalator);
	}

	/**
	 * @param startIndex
	 * @param endIndex
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Override
	public int rngRemoveDuplicates(
		final int startIndex,
		final int endIndex,
		final boolean ignoreNulls,
		final Equalator<E> equalator
	)
	{
		final int d; //bi-directional index movement
		if(startIndex <= endIndex){
			if(startIndex < 0 || endIndex >= this.size){
				throw new IndexOutOfBoundsException(this.exceptionStringRange(startIndex, endIndex));
			}
			d = 1; //incrementing direction
		}
		else {
			if(startIndex >= this.size || endIndex < 0){
				throw new IndexOutOfBoundsException(this.exceptionStringRange(endIndex, startIndex));
			}
			d = -1; //decrementing direction
		}

		final int removedCount;
		final Object[] data = this.data;
		try{
			for(int i = startIndex-d; i != endIndex;){
				final Object ei = data[i+=d];
				if(ei == REMOVE_MARKER || ei == null && ignoreNulls) continue;
				for(int j = i+d; j != endIndex;){
					final Object ej = data[j+=d];
					if(ej == REMOVE_MARKER) continue;
					if(equalator.equal((E)ei, (E)ej)){
						data[j] = REMOVE_MARKER;
					}
				}
			}
		}
		finally{
			removedCount = JaArrays.removeAllFromArray(
				REMOVE_MARKER, this.data, startIndex, endIndex+1, this.data, startIndex, endIndex+1
			);
		}

		this.size -= removedCount;
		return removedCount;
	}

	/**
	 * @return
	 */
	@Override
	public int removeDuplicates(final boolean ignoreNulls)
	{
		return this.rngRemoveDuplicates(0, this.size-1, ignoreNulls);
	}

	/**
	 * @param startIndex
	 * @param endIndex
	 * @return
	 */
	@Override
	public int rngRemoveDuplicates(final int startIndex, final int endIndex, final boolean ignoreNulls)
	{
		final int d; //bi-directional index movement
		if(startIndex <= endIndex){
			if(startIndex < 0 || endIndex >= this.size){
				throw new IndexOutOfBoundsException(this.exceptionStringRange(startIndex, endIndex));
			}
			d = 1; //incrementing direction
		}
		else {
			if(startIndex >= this.size || endIndex < 0){
				throw new IndexOutOfBoundsException(this.exceptionStringRange(endIndex, startIndex));
			}
			d = -1; //decrementing direction
		}

		boolean alreadyFoundOneNull = false;
		final int removedCount;
		final Object[] data = this.data;
		try{
			for(int i = startIndex-d; i != endIndex;){
				final Object ei = data[i+=d];
				if(ei == REMOVE_MARKER) continue;
				if(ei == null){
					if(ignoreNulls) continue;
					else if(alreadyFoundOneNull){
						data[i] = REMOVE_MARKER;
					}
					else {
						alreadyFoundOneNull = true;
					}
					continue;
				}
				for(int j = i; j != endIndex;){
					final Object ej = data[j+=d];
					if(ej == REMOVE_MARKER) continue;
					if(ei == ej){
						data[j] = REMOVE_MARKER;
					}
				}
			}
		}
		finally{
			removedCount = JaArrays.removeAllFromArray(
				REMOVE_MARKER, this.data, startIndex, endIndex+1, this.data, startIndex, endIndex+1
			);
		}

		this.size -= removedCount;
		return removedCount;
	}

	/**
	 * @param fromIndex
	 * @param toIndex
	 * @return
	 */
	@Override
	public GrowList<E> removeRange(int startIndex, int endIndex)
	{
		if(startIndex > endIndex){
			final int t = startIndex;
			startIndex = endIndex;
			endIndex = t;
		}
		if(startIndex < 0 || endIndex >= this.size){
			this.exceptionStringRange(startIndex, endIndex);
		}
		final Object[] data = this.data;
		final int size = this.size;
		System.arraycopy(data, endIndex+1, data, startIndex, size - endIndex - 1);
		this.size -= endIndex - startIndex + 1;

		// free old array buckets
		for(int i = this.size; i < size; i++){
			data[i] = null;
		}
		return this;
	}

	/**
	 * @param index
	 * @param element
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Override
	public E set(final int index, final E element) throws IndexOutOfBoundsException, ArrayIndexOutOfBoundsException
	{
		if(index >= this.size){
			throw new IndexOutOfBoundsException(this.exceptionStringIndexOutOfBounds(index));
		}
		final E old = (E)this.data[index];
		this.data[index] = element;
		return old;
	}

	/**
	 * @return
	 */
	@Override
	public int size()
	{
		return this.size;
	}

	/**
	 * @param fromIndex
	 * @param toIndex
	 * @return
	 */
	@Override
	public SubList<E> subList(final int fromIndex, final int toIndex)
	{
		// range check is done in Constructor already
		return new SubList<E>(this, fromIndex, toIndex);
	}

	/**
	 * @return
	 */
	@Override
	public Object[] toArray()
	{
		final Object[] array = new Object[this.size];
		System.arraycopy(this.data, 0, array, 0, this.size);
		return array;
	}

	/**
	 * @param <E>
	 * @param a
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Override
	public <T> T[] toArray(final T[] a)
	{
		final int size = this.size;
		if(a.length < size){
			// Make a new array of a's runtime type, but this list's contents:
			return (T[])Arrays.copyOf(this.data, size, a.getClass());
		}

		System.arraycopy(this.data, 0, a, 0, size);
		if(a.length > size){
			a[size] = null;
		}
		return a;
	}

	/**
	 * @param startIndex
	 * @param endIndex
	 * @param a
	 * @param <T>
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Override
	public <T> T[] rngToArray(final int startIndex, final int endIndex, T[] a)
	{
		final Object[] data = this.data;
		final boolean incrementing;
		final int targetLength;
		if(startIndex <= endIndex){
			if(startIndex < 0 || endIndex >= this.size){
				throw new IndexOutOfBoundsException(this.exceptionStringRange(startIndex, endIndex));
			}
			incrementing = true;
			targetLength = endIndex - startIndex + 1;
		}
		else {
			if(startIndex >= this.size || endIndex < 0){
				throw new IndexOutOfBoundsException(this.exceptionStringRange(endIndex, startIndex));
			}
			incrementing = false;
			targetLength = startIndex - endIndex + 1;
		}

		if(a.length < targetLength){
			a = (T[])Array.newInstance(a.getClass().getComponentType(), targetLength);
		}

		if(incrementing){
			System.arraycopy(data, startIndex, a, 0, targetLength);
		}
		else {
			for(int i = startIndex, j = 0; i >= endIndex; i--){
				a[j++] = (T)data[i];
			}
		}
		if(a.length > this.size){
			a[this.size] = null;
		}
		return a;
	}

	/**
	 *
	 * @param elements
	 * @return
	 */
	@Override
	public GrowList<E> add(final E... elements)
	{
		Object[] data = this.data;
		final int targetCapacity = this.calculateCapacityIncrease(elements.length);
		if(targetCapacity > data.length){
			data = new Object[targetCapacity];
			System.arraycopy(this.data, 0, data, 0, this.size);
			this.data = data;
		}
		System.arraycopy(elements, 0, data, this.size, elements.length);
		this.size += elements.length;
		return this;
	}

	/**
	 * @param index
	 * @param elements
	 * @return
	 */
	@Override
	public GrowList<E> insert(final int index, final E... elements) throws IndexOutOfBoundsException
	{
		if(index < 0){
			throw new ArrayIndexOutOfBoundsException(index);
		}
		if(Integer.MAX_VALUE - elements.length > this.size){
			throw new ArrayIndexOutOfBoundsException(this.exceptionStringArrayLengthExceeded(elements.length));
		}
		if(index == this.size){
			return this.add(elements);
		}
		if(index > this.size){
			throw new IndexOutOfBoundsException(this.exceptionStringIndexOutOfBounds(index));
		}
		this.internalInsertArray(index, elements, elements.length);
		return this;
	}

	/**
	 * @param elements
	 * @return
	 */
	@Override
	public GrowList<E> add(final Iterable<? extends E> elements)
	{
		if(elements instanceof Collection<?>){
			this.addAll((Collection<? extends E>)elements);
			return this;
		}

		Object[] data = this.data;
		int dataLength = data.length;
		int size = this.size;

		try{
			for(final E t : elements) {
				//can't foresee elements' size, so do it the hard way
				if(size == dataLength){
					//expandArray()
					dataLength = dataLength == MAXIMUM_POW2_SIZE ?Integer.MAX_VALUE : dataLength<<1;
					data = new Object[dataLength];
					System.arraycopy(this.data, 0, data, 0, size);
					this.data = data;
				}
				data[size++] = t;
			}
		}
		finally{
			// in case elements will overflow the max array size, size still has to be updated correctly
			this.size = size;
		}
		return this;
	}

	/**
	 * @param index
	 * @param elements
	 * @return this instance.
	 */
	@Override
	public GrowList<E> insert(final int index, final Iterable<? extends E> elements)
	{
		if(index < 0){
			throw new ArrayIndexOutOfBoundsException(index);
		}
		if(index > this.size){
			throw new IndexOutOfBoundsException(this.exceptionStringIndexOutOfBounds(index));
		}
		if(index == this.size){
			return this.add(elements);
		}
		if(elements instanceof Collection<?>){
			this.addAll(index, (Collection<? extends E>)elements);
			return this;
		}

		/* Can't determine elements' length beforehand, so put all elements in a buffer and then copy the buffer array.
		 * Will very likely be faster than shifting the rest of the array on every element.
		 */
		final GrowList<E> buffer = new GrowList<E>();
		buffer.add(elements);
		if(Integer.MAX_VALUE - buffer.size > this.size){
			throw new ArrayIndexOutOfBoundsException(this.exceptionStringArrayLengthExceeded(buffer.size));
		}
		this.internalInsertArray(index, buffer.data, buffer.size);
		return this;
	}

	/**
	 * @param element
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Override
	public int count(final E element, final Equalator<E> equalator)
	{
		int count = 0;
		final Object[] data = this.data;		
		for(int i = 0, size = this.size; i < size; i++){
			if(equalator.equal(element, (E)data[i])){
				count++;
			}
		}
		return count;
	}

	/**
	 * @param startIndex
	 * @param endIndex
	 * @param element
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Override
	public int rngCount(final int startIndex, final int endIndex, final E element, final Equalator<E> equalator)
	{
		final int d; //bi-directional index movement
		if(startIndex <= endIndex){
			if(startIndex < 0 || endIndex >= this.size){
				throw new IndexOutOfBoundsException(this.exceptionStringRange(startIndex, endIndex));
			}
			d = 1; //incrementing direction
		}
		else {
			if(startIndex >= this.size || endIndex < 0){
				throw new IndexOutOfBoundsException(this.exceptionStringRange(endIndex, startIndex));
			}
			d = -1; //decrementing direction
		}

		int count = 0;
		final Object[] data = this.data;
		for(int i = startIndex; i != endIndex;){
			if(equalator.equal(element, (E)data[i+=d])){
				count++;
			}
		}
		return count;
	}

	/**
	 * @param startIndex
	 * @param endIndex
	 * @param element
	 * @return
	 */
	@Override
	public GrowList<E> fill(int startIndex, int endIndex, final E element)
	{
		if(startIndex > endIndex){
			final int t = startIndex;
			startIndex = endIndex;
			endIndex = t;
		}
		if(startIndex < 0 || endIndex >= this.size){
			this.exceptionStringRange(startIndex, endIndex);
		}

		final Object[] data = this.data;
		for(int i = startIndex; i <= endIndex; i++){
			data[i] = element;
		}
		return this;
	}

	/**
	 * @param startIndex
	 * @param endIndex
	 * @param c
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Override
	public boolean rngContainsAll(
		final int startIndex,
		final int endIndex,
		final Collection<E> c,
		final boolean ignoreNulls,
		final Equalator<E> equalator
	)
	{
		final int d; //bi-directional index movement
		if(startIndex <= endIndex){
			if(startIndex < 0 || endIndex >= this.size){
				throw new IndexOutOfBoundsException(this.exceptionStringRange(startIndex, endIndex));
			}
			d = 1; //incrementing direction
		}
		else {
			if(startIndex >= this.size || endIndex < 0){
				throw new IndexOutOfBoundsException(this.exceptionStringRange(endIndex, startIndex));
			}
			d = -1; //decrementing direction
		}

		boolean nullAreadyHandled = ignoreNulls;
		final Object[] data = this.data;
		collectionLoop: for(final E ec : c) {
			if(ec == null){
				if(nullAreadyHandled) continue;
				nullAreadyHandled = true;
			}
			for(int i = startIndex-d; i != endIndex;){
				if(equalator.equal(ec, (E)data[i+=d])) continue collectionLoop;
			}
			return false;
		}
		return true; //all elements of c have been found
	}

	/**
	 *
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public E getFirst()
	{
		return (E)this.data[0];
	}

	/**
	 *
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public E getLast()
	{
		return (E)this.data[this.size-1];
	}

	/**
	 *
	 * @param element
	 */
	public void setFirst(final E element)
	{
		this.data[0] = element;
	}

	/**
	 *
	 * @param element
	 */
	public void setLast(final E element)
	{
		this.data[this.size-1] = element;
	}

	/**
	 * @param predicate
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Override
	public int count(final Predicate<E> predicate)
	{
		int count = 0;
		final Object[] data = this.data;	
		for(int i = 0, size = this.size; i < size; i++){
			if(predicate.apply((E)data[i])){
				count++;
			}
		}
		return count;
	}

	/**
	 * @param predicate
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Override
	public int count(final TPredicate<E> predicate)
	{
		int count = 0;

		final Object[] data = this.data;
		final int size = this.size;
		int i = 0;
		while(i < size){ //while(true) astonishingly is significantly slower than duplicated inner condition
			try {
				while(i < size){
					if(predicate.apply((E)data[i++])){
						count++;
					}
				}
				break;
			}
			catch(final ThrowBreak b)   { break; }
			catch(final ThrowReturn r)  { return count; }
			catch(final ThrowContinue c){ /*Nothing*/ }
		}

		return count;
	}

	/**
	 * Immediately releases the current internal data array and initializes a new one with the default data size.<br>
	 * The new size of this liste is 0.
	 * <p>
	 * If the data is only to be cleared without new storage allocation, see {@link #clear()}.<br>
	 * Note though, that reinitializing the list may be considerably faster than clearing for large lists.<br>
	 * For smaller lists, {@link #clear()} will most probably be faster than {@link #truncate()}.
	 *
	 * @return this instance
	 * @see #clear()
	 */
	@Override
	public GrowList<E> truncate()
	{
		this.data = new Object[DEFAULT_DATA_SIZE];
		this.size = 0;
		return this;
	}

	/**
	 * @param startIndex
	 * @param o
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Override
	public int rngIndexOf(final int startIndex, final int endIndex, final E o, final Equalator<E> equalator)
	{		
		final int d; //bi-directional index movement
		if(startIndex <= endIndex){
			if(startIndex < 0 || endIndex >= this.size){
				throw new IndexOutOfBoundsException(this.exceptionStringRange(startIndex, endIndex));
			}
			d = 1; //incrementing direction
		}
		else {
			if(startIndex >= this.size || endIndex < 0){
				throw new IndexOutOfBoundsException(this.exceptionStringRange(endIndex, startIndex));
			}
			d = -1; //decrementing direction
		}

		final Object[] data = this.data;
		for(int i = startIndex-d; i != endIndex;){
			if(equalator.equal(o, (E)data[i+=d])){
				return i;
			}
		}
		return -1;
	}
	
	/**
	 * @param startIndex
	 * @param endIndex
	 * @param element
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Override
	public E rngRemoveOne(final int startIndex, final int endIndex, final E element, final Equalator<E> equalator)
	{
		final int d; //bi-directional index movement
		if(startIndex <= endIndex){
			if(startIndex < 0 || endIndex >= this.size){
				throw new IndexOutOfBoundsException(this.exceptionStringRange(startIndex, endIndex));
			}
			d = 1; //incrementing direction
		}
		else {
			if(startIndex >= this.size || endIndex < 0){
				throw new IndexOutOfBoundsException(this.exceptionStringRange(endIndex, startIndex));
			}
			d = -1; //decrementing direction
		}

		final Object[] data = this.data;
		for(int i = startIndex-d; i != endIndex;){
			if(equalator.equal(element, (E)data[i+=d])){
				final E oldValue = (E)data[i];
				if(i < --this.size){
					System.arraycopy(data, i+1, data, i, this.size - i);
				}				
				data[this.size] = null;
				return oldValue;
			}
		}
		return null;
	}
	
	/**
	 * @param startIndex
	 * @param endIndex
	 * @param c
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Override
	public int rngRetainAll(
		final int startIndex, final int endIndex, final XCollection<E> c, final Equalator<E> equalator
	)
	{
		final int d; //bi-directional index movement
		if(startIndex <= endIndex){
			if(startIndex < 0 || endIndex >= this.size){
				throw new IndexOutOfBoundsException(this.exceptionStringRange(startIndex, endIndex));
			}
			d = 1; //incrementing direction
		}
		else {
			if(startIndex >= this.size || endIndex < 0){
				throw new IndexOutOfBoundsException(this.exceptionStringRange(endIndex, startIndex));
			}
			d = -1; //decrementing direction
		}

		final int removedCount;
		final Object[] data = this.data;	
		boolean skipNulls = false;
		Object e;		
		try {
			for(int i = startIndex-d; i != endIndex;){
				if((e = data[i+=d]) == null){
					if(skipNulls) continue;
					skipNulls = true;
				}
				if(c.contains((E)e, equalator)) continue;
				data[i] = REMOVE_MARKER;
			}
		}
		finally {
			removedCount = d == -1
			?removeAllFromArray(REMOVE_MARKER, this.data, endIndex, startIndex+1, this.data, endIndex, startIndex+1)
			:removeAllFromArray(REMOVE_MARKER, this.data, startIndex, endIndex+1, this.data, startIndex, endIndex+1)
			;
			this.size -= removedCount;
		}
		return removedCount;
	}
	
	/**
	 * @param startIndex
	 * @param endIndex
	 * @return
	 */
	@Override
	public Object[] rngToArray(final int startIndex, final int endIndex)
	{
		final Object[] data = this.data;
		final boolean incrementing;
		final int targetLength;
		if(startIndex <= endIndex){
			if(startIndex < 0 || endIndex >= this.size){
				throw new IndexOutOfBoundsException(this.exceptionStringRange(startIndex, endIndex));
			}
			incrementing = true;
			targetLength = endIndex - startIndex + 1;
		}
		else {
			if(startIndex >= this.size || endIndex < 0){
				throw new IndexOutOfBoundsException(this.exceptionStringRange(endIndex, startIndex));
			}
			incrementing = false;
			targetLength = startIndex - endIndex + 1;
		}

		final Object[] array = new Object[targetLength];
		if(incrementing){
			System.arraycopy(data, startIndex, array, 0, targetLength);
		}
		else {
			for(int i = startIndex, j = 0; i >= endIndex; i--){
				array[j++] = data[i];
			}
		}
		return array;
	}

	/**
	 * @param index
	 * @param element
	 * @return
	 */
	@Override
	public GrowList<E> insert(final int index, final E element)
	{
		this.add(index, element);
		return this;
	}
	
	/**
	 * @param startIndex
	 * @param endIndex
	 * @param operation
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Override
	public GrowList<E> rngExecute(
		final int startIndex, final int endIndex, final TOperation<E> operation
	)
	{
		final int d; //bi-directional index movement
		if(startIndex <= endIndex){
			if(startIndex < 0 || endIndex >= this.size){
				throw new IndexOutOfBoundsException(this.exceptionStringRange(startIndex, endIndex));
			}
			d = 1; //incrementing direction
		}
		else {
			if(startIndex >= this.size || endIndex < 0){
				throw new IndexOutOfBoundsException(this.exceptionStringRange(endIndex, startIndex));
			}
			d = -1; //decrementing direction
		}		

		final Object[] data = this.data;

		//this construction yields the same performance as the loop without inner try, but can still continue the loop
		int i = startIndex-d; //start i one before startIndex as (i+=d) only works as prefix ~crement
		while(i != endIndex){ //while(true) astonishingly is significantly slower than duplicated inner condition
			try {
				while(i != endIndex){
					operation.execute((E)data[i+=d]);
				}
				break;
			}
			catch(final ThrowBreak b)   { break; }
			catch(final ThrowReturn r)  { return this; }
			catch(final ThrowContinue c){ /*Nothing*/ }
		}
		return this;
	}
	
	/**
	 * @param operation
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Override
	public GrowList<E> execute(final TOperation<E> operation)
	{
		final Object[] data = this.data;
		final int size = this.size;
		int i = 0;
		while(i < size){ //while(true) astonishingly is significantly slower than duplicated inner condition
			try {
				while(i < size){
					operation.execute((E)data[i++]);
				}
				break;
			}
			catch(final ThrowBreak b)   { break; }
			catch(final ThrowReturn r)  { return this; }
			catch(final ThrowContinue c){ /*Nothing*/ }
		}
		return this;
	}

	/**
	 * @param operation
	 */
	@SuppressWarnings("unchecked")
	public GrowList<E> execute(final Operation<E> operation)
	{
		final Object[] data = this.data;
		for(int i = 0, size = this.size; i < size; i++){
			operation.execute((E)data[i]);
		}
		return this;
	}
	
	/**
	 * @param startIndex
	 * @param endIndex
	 * @param operation
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Override
	public GrowList<E> rngExecute(
		final int startIndex, final int endIndex, final Operation<E> operation
	)
	{
		final int d; //bi-directional index movement
		if(startIndex <= endIndex){
			if(startIndex < 0 || endIndex >= this.size){
				throw new IndexOutOfBoundsException(this.exceptionStringRange(startIndex, endIndex));
			}
			d = 1; //incrementing direction
		}
		else {
			if(startIndex >= this.size || endIndex < 0){
				throw new IndexOutOfBoundsException(this.exceptionStringRange(endIndex, startIndex));
			}
			d = -1; //decrementing direction
		}

		final Object[] data = this.data;
		for(int i = startIndex-d; i != endIndex;){
			operation.execute((E)data[i+=d]);
		}
		return this;
	}

	/**
	 * @param predicate
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Override
	public int indexOf(final Predicate<E> predicate)
	{
		final Object[] data = this.data;
		for(int i = 0, size = this.size; i < size; i++){
			if(predicate.apply((E)data[i])){
				return i;
			}
		}
		return -1;
	}
	
	/**
	 * @param predicate
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Override
	public int indexOf(final TPredicate<E> predicate)
	{
		//this construction yields the same performance as a loop without inner try, but can still continue the loop
		final Object[] data = this.data;
		final int size = this.size;
		int i = 0;
		while(i < size){ //while(true) astonishingly is significantly slower than duplicated inner condition
			try {
				while(i < size){
					if(predicate.apply((E)data[i++])){
						return i;
					}
				}
				break;
			}
			catch(final ThrowBreak b)   { break; }
			catch(final ThrowReturn r)  { return -1; }
			catch(final ThrowContinue c){ /*Nothing*/ }
		}
		return -1;
	}
	
	/**
	 * @param startIndex
	 * @param endIndex
	 * @param predicate
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Override
	public int rngCount(final int startIndex, final int endIndex, final Predicate<E> predicate)
	{
		final int d; //bi-directional index movement
		if(startIndex <= endIndex){
			if(startIndex < 0 || endIndex >= this.size){
				throw new IndexOutOfBoundsException(this.exceptionStringRange(startIndex, endIndex));
			}
			d = 1; //incrementing direction
		}
		else {
			if(startIndex >= this.size || endIndex < 0){
				throw new IndexOutOfBoundsException(this.exceptionStringRange(endIndex, startIndex));
			}
			d = -1; //decrementing direction
		}		

		int count = 0;
		final Object[] data = this.data;		
		for(int i = startIndex-d; i != endIndex;){
			if(predicate.apply((E)data[i+=d])){
				count++;
			}
		}
		return count;
	}
	
	/**
	 * @param startIndex
	 * @param endIndex
	 * @param predicate
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Override
	public int rngCount(final int startIndex, final int endIndex, final TPredicate<E> predicate)
	{
		final int d; //bi-directional index movement
		if(startIndex <= endIndex){
			if(startIndex < 0 || endIndex >= this.size){
				throw new IndexOutOfBoundsException(this.exceptionStringRange(startIndex, endIndex));
			}
			d = 1; //incrementing direction
		}
		else {
			if(startIndex >= this.size || endIndex < 0){
				throw new IndexOutOfBoundsException(this.exceptionStringRange(endIndex, startIndex));
			}
			d = -1; //decrementing direction
		}

		int count = 0;
		final Object[] data = this.data;
		int i = startIndex-d;
		while(i != endIndex){ //while(true) astonishingly is significantly slower than duplicated inner condition
			try {
				while(i != endIndex){
					if(predicate.apply((E)data[i+=d])){
						count++;
					}
				}
				break;
			}
			catch(final ThrowBreak b)   { break; }
			catch(final ThrowReturn r)  { return count; }
			catch(final ThrowContinue c){ /*Nothing*/ }
		}
		return count;
	}
	
	/**
	 * @param startIndex
	 * @param endIndex
	 * @param predicate
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Override
	public int rngIndexOf(final int startIndex, final int endIndex, final Predicate<E> predicate)
	{
		final int d; //bi-directional index movement
		if(startIndex <= endIndex){
			if(startIndex < 0 || endIndex >= this.size){
				throw new IndexOutOfBoundsException(this.exceptionStringRange(startIndex, endIndex));
			}
			d = 1; //incrementing direction
		}
		else {
			if(startIndex >= this.size || endIndex < 0){
				throw new IndexOutOfBoundsException(this.exceptionStringRange(endIndex, startIndex));
			}
			d = -1; //decrementing direction
		}

		final Object[] data = this.data;
		for(int i = startIndex-d; i != endIndex;){
			if(predicate.apply((E)data[i+=d])){
				return i;
			}
		}
		return -1;
	}
	
	/**
	 * @param startIndex
	 * @param endIndex
	 * @param predicate
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Override
	public int rngIndexOf(final int startIndex, final int endIndex, final TPredicate<E> predicate)
	{
		final int d; //bi-directional index movement
		if(startIndex <= endIndex){
			if(startIndex < 0 || endIndex >= this.size){
				throw new IndexOutOfBoundsException(this.exceptionStringRange(startIndex, endIndex));
			}
			d = 1; //incrementing direction
		}
		else {
			if(startIndex >= this.size || endIndex < 0){
				throw new IndexOutOfBoundsException(this.exceptionStringRange(endIndex, startIndex));
			}
			d = -1; //decrementing direction
		}

		final Object[] data = this.data;
		int i = startIndex-d; //start i one before startIndex as (i+=d) only works as prefix ~crement
		while(i != endIndex){ //while(true) astonishingly is significantly slower than duplicated inner condition
			try {
				while(i != endIndex){
					if(predicate.apply((E)data[i+=d])){
						return i;
					}
				}
				break;
			}
			catch(final ThrowBreak b)   { break; }
			catch(final ThrowReturn r)  { return -1; }
			catch(final ThrowContinue c){ /*Nothing*/ }
		}
		return -1;
	}
	
	/**
	 * @param startIndex
	 * @param endIndex
	 * @param predicate
	 */
	@SuppressWarnings("unchecked")
	@Override
	public int rngReduce(final int startIndex, final int endIndex, final Predicate<E> predicate)
	{
		final int d; //bi-directional index movement
		if(startIndex <= endIndex){
			if(startIndex < 0 || endIndex >= this.size){
				throw new IndexOutOfBoundsException(this.exceptionStringRange(startIndex, endIndex));
			}
			d = 1; //incrementing direction
		}
		else {
			if(startIndex >= this.size || endIndex < 0){
				throw new IndexOutOfBoundsException(this.exceptionStringRange(endIndex, startIndex));
			}
			d = -1; //decrementing direction
		}

		final int removedCount;
		final Object[] data = this.data;
		try {
			for(int i = startIndex-d; i != endIndex;) {
				if(predicate.apply((E)data[i+=d])) {
					data[i] = REMOVE_MARKER;
				}
			}
		}
		finally {
			removedCount = d == -1
			?removeAllFromArray(REMOVE_MARKER, this.data, endIndex, startIndex+1, this.data, endIndex, startIndex+1)
			:removeAllFromArray(REMOVE_MARKER, this.data, startIndex, endIndex+1, this.data, startIndex, endIndex+1)
			;
			this.size -= removedCount;
		}
		return removedCount;
	}
	
	/**
	 * @param startIndex
	 * @param endIndex
	 * @param predicate
	 */
	@SuppressWarnings("unchecked")
	@Override
	public int rngReduce(final int startIndex, final int endIndex, final TPredicate<E> predicate)
	{
		final int d; //bi-directional index movement
		if(startIndex <= endIndex){
			if(startIndex < 0 || endIndex >= this.size){
				throw new IndexOutOfBoundsException(this.exceptionStringRange(startIndex, endIndex));
			}
			d = 1; //incrementing direction
		}
		else {
			if(startIndex >= this.size || endIndex < 0){
				throw new IndexOutOfBoundsException(this.exceptionStringRange(endIndex, startIndex));
			}
			d = -1; //decrementing direction
		}

		final int removedCount;
		final Object[] data = this.data;
		try {
			//this construction yields the same performance as the loop without inner try, but can still continue the loop
			int i = startIndex-d; //start i one before startIndex as (i+=d) only works as prefix ~crement
			while(i != endIndex){ //while(true) astonishingly is significantly slower than duplicated inner condition
				try {
					while(i != endIndex){
						if(predicate.apply((E)data[i+=d])){
							data[i] = REMOVE_MARKER;
						}
					}
					break;
				}
				catch(final ThrowBreak b)   { break; }
				catch(final ThrowContinue c){ /*Nothing*/ }
				catch(final ThrowReturn r)  { break; } //must still execute finally code and return removedCount
			}
		}
		finally {
			//can't abort/return until remove markers are cleared, so do this in any case
			removedCount = d == -1
			?removeAllFromArray(REMOVE_MARKER, this.data, endIndex, startIndex+1, this.data, endIndex, startIndex+1)
			:removeAllFromArray(REMOVE_MARKER, this.data, startIndex, endIndex+1, this.data, startIndex, endIndex+1)
			;
			this.size -= removedCount;
		}
		return removedCount;
	}
	
	/**
	 * @param startIndex
	 * @param endIndex
	 * @param predicate
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Override
	public E rngSearch(final int startIndex, final int endIndex, final Predicate<E> predicate)
	{
		final int d; //bi-directional index movement
		if(startIndex <= endIndex){
			if(startIndex < 0 || endIndex >= this.size){
				throw new IndexOutOfBoundsException(this.exceptionStringRange(startIndex, endIndex));
			}
			d = 1; //incrementing direction
		}
		else {
			if(startIndex >= this.size || endIndex < 0){
				throw new IndexOutOfBoundsException(this.exceptionStringRange(endIndex, startIndex));
			}
			d = -1; //decrementing direction
		}

		final Object[] data = this.data;
		for(int i = startIndex-d; i != endIndex;){
			final E e = (E)data[i+=d];
			if(e != null && predicate.apply(e)){
				return e;
			}
		}
		return null;
	}
	
	/**
	 * @param startIndex
	 * @param endIndex
	 * @param predicate
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Override
	public E rngSearch(final int startIndex, final int endIndex, final TPredicate<E> predicate)
	{
		final int d; //bi-directional index movement
		if(startIndex <= endIndex){
			if(startIndex < 0 || endIndex >= this.size){
				throw new IndexOutOfBoundsException(this.exceptionStringRange(startIndex, endIndex));
			}
			d = 1; //incrementing direction
		}
		else {
			if(startIndex >= this.size || endIndex < 0){
				throw new IndexOutOfBoundsException(this.exceptionStringRange(endIndex, startIndex));
			}
			d = -1; //decrementing direction
		}

		//this construction yields the same performance as the loop without inner try, but can still continue the loop
		final Object[] data = this.data;
		int i = startIndex-d; //start i one before startIndex as (i+=d) only works as prefix ~crement
		while(i != endIndex){ //while(true) astonishingly is significantly slower than duplicated inner condition
			try {
				while(i != endIndex){
					final E e = (E)data[i+=d];
					if(e != null && predicate.apply(e)){
						return e;
					}
				}
				break;
			}
			catch(final ThrowBreak b)   { break; }
			catch(final ThrowReturn r)  { return null; }
			catch(final ThrowContinue c){ /*Nothing*/ }
		}
		return null;
	}
	
	/**
	 * @param predicate
	 */
	@SuppressWarnings("unchecked")
	@Override
	public int reduce(final Predicate<E> predicate)
	{
		final int size = this.size;
		if(size == 0){
			return 0;
		}

		final int removedCount;
		final Object[] data = this.data;
		try {
			for(int i = 0; i < size; i++){
				if(predicate.apply((E)data[i])){
					data[i] = REMOVE_MARKER;
				}
			}
		}
		finally {
			//even if predicate throws an execption, the remove markers have to be cleared
			removedCount = JaArrays.removeAllFromArray(REMOVE_MARKER, data, 0, this.size, data, 0, this.size);
		}
		this.size -= removedCount;
		return removedCount;
	}
	
	/**
	 * @param predicate
	 */
	@SuppressWarnings("unchecked")
	@Override
	public int reduce(final TPredicate<E> predicate)
	{
		final int size = this.size;
		if(size == 0){
			return 0; // spare unnecessary traversal of the whole list in array util method
		}

		final int removedCount;
		//this construction yields the same performance as a loop without inner try, but can still continue the loop
		final Object[] data = this.data;
		int i = -1;
		try {
			while(i < size){ //while(true) astonishingly is significantly slower than duplicated inner condition
				try {
					while(i < size){
						if(predicate.apply((E)data[++i])){
							data[i] = REMOVE_MARKER;
						}
					}
					break;
				}
				catch(final ThrowBreak b)   { break; }
				catch(final ThrowReturn r)  { break; }
				catch(final ThrowContinue c){ /*Nothing*/ }
			}
		}
		finally {
			//can't return until remove markers are cleared, so do this in any case
			removedCount = JaArrays.removeAllFromArray(REMOVE_MARKER, data, 0, size, data, 0, size);
			this.size -= removedCount;
		}
		return removedCount;
	}
	
	/**
	 * @param predicate
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Override
	public E search(final Predicate<E> predicate)
	{
		final Object[] data = this.data;
		for(int i = 0, size = this.size; i < size; i++){
			final E element = (E)data[i];
			if(element != null && predicate.apply(element)){
				return element;
			}
		}
		return null;
	}
	
	/**
	 * @param predicate
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Override
	public E search(final TPredicate<E> predicate)
	{
		//this construction yields the same performance as a loop without inner try, but can still continue the loop
		final Object[] data = this.data;
		final int size = this.size;
		int i = 0;
		while(i < size){ //while(true) astonishingly is significantly slower than duplicated inner condition
			try {
				while(i < size){
					final E e = (E)data[i++];
					if(e != null && predicate.apply(e)){
						return e;
					}
				}
				break;
			}
			catch(final ThrowBreak b)   { break; }
			catch(final ThrowReturn r)  { return null; }
			catch(final ThrowContinue c){ /*Nothing*/ }
		}
		return null;
	}
	
	/**
	 * @param predicate
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Override
	public boolean contains(final Predicate<E> predicate)
	{
		final Object[] data = this.data;
		for(int i = 0, size = this.size; i < size; i++){
			final E element = (E)data[i];
			if(predicate.apply(element)){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * @param predicate
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Override
	public boolean contains(final TPredicate<E> predicate)
	{
		//this construction yields the same performance as a loop without inner try, but can still continue the loop
		final Object[] data = this.data;
		final int size = this.size;
		int i = 0;
		while(i < size){ //while(true) astonishingly is significantly slower than duplicated inner condition
			try {
				while(i < size){
					if(predicate.apply((E)data[i++])){
						return true;
					}
				}
				break;
			}
			catch(final ThrowBreak b)   { break; }
			catch(final ThrowReturn r)  { return false; }
			catch(final ThrowContinue c){ /* Nothing */ }
		}
		return false;
	}
	
	/**
	 * @param startIndex
	 * @param endIndex
	 * @param predicate
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Override
	public boolean rngContains(final int startIndex, final int endIndex, final Predicate<E> predicate)
	{
		final int d; //bi-directional index movement
		if(startIndex <= endIndex){
			if(startIndex < 0 || endIndex >= this.size){
				throw new IndexOutOfBoundsException(this.exceptionStringRange(startIndex, endIndex));
			}
			d = 1; //incrementing direction
		}
		else {
			if(startIndex >= this.size || endIndex < 0){
				throw new IndexOutOfBoundsException(this.exceptionStringRange(endIndex, startIndex));
			}
			d = -1; //decrementing direction
		}

		final Object[] data = this.data;
		for(int i = startIndex-d; i != endIndex;){
			if(predicate.apply((E)data[i+=d])){
				return true;
			}
		}
		return false;
	}

	/**
	 * @param startIndex
	 * @param endIndex
	 * @param predicate
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Override
	public boolean rngContains(final int startIndex, final int endIndex, final TPredicate<E> predicate)
	{
		final int d; //bi-directional index movement
		if(startIndex <= endIndex){
			if(startIndex < 0 || endIndex >= this.size){
				throw new IndexOutOfBoundsException(this.exceptionStringRange(startIndex, endIndex));
			}
			d = 1; //incrementing direction
		}
		else {
			if(startIndex >= this.size || endIndex < 0){
				throw new IndexOutOfBoundsException(this.exceptionStringRange(endIndex, startIndex));
			}
			d = -1; //decrementing direction
		}

		//this construction yields the same performance as the loop without inner try, but can still continue the loop
		final Object[] data = this.data;
		int i = startIndex-d; //start i one before startIndex as (i+=d) only works as prefix ~crement
		while(i != endIndex){ //while(true) astonishingly is significantly slower than duplicated inner condition
			try {
				while(i != endIndex){
					if(predicate.apply((E)data[i+=d])){
						return true;
					}
				}
				break;
			}
			catch(final ThrowBreak b)   { break; }
			catch(final ThrowReturn r)  { return false; }
			catch(final ThrowContinue c){ /* Nothing */ }
		}
		return false;
	}
	
	/**
	 * @param startIndex
	 * @param elements
	 * @return
	 * @see com.xdev.jadoth.collections.XList#set(int, E[])
	 */
	@Override
	public GrowList<E> set(final int startIndex, final E... elements)
	{
		if(startIndex < 0 || startIndex + elements.length > this.size){
			throw new IndexOutOfBoundsException(this.exceptionStringRange(startIndex, startIndex + elements.length - 1));
		}
		System.arraycopy(elements, 0, this.data, startIndex, elements.length);
		return this;
	}
	
	/**
	 * @param startIndex
	 * @param elements
	 * @param elementsStartIndex
	 * @param length
	 * @return
	 * @see com.xdev.jadoth.collections.XList#set(int, E[], int, int)
	 */
	@Override
	public GrowList<E> set(final int startIndex, final E[] elements, final int elementsStartIndex, final int length)
	{
		if(startIndex < 0 || startIndex + length > this.size){
			throw new IndexOutOfBoundsException(this.exceptionStringRange(startIndex, startIndex + elements.length - 1));
		}
		System.arraycopy(elements, elementsStartIndex, this.data, startIndex, length);
		return this;
	}
	
	/**
	 * @param startIndex
	 * @param elements
	 * @param elementsStartIndex
	 * @param length
	 * @return
	 * @see com.xdev.jadoth.collections.XList#set(int, List, int, int)
	 */
	@Override
	public GrowList<E> set(final int startIndex, final List<E> elements, final int elementsStartIndex, final int length)
	{
		if(startIndex < 0 || startIndex + length > this.size){
			throw new IndexOutOfBoundsException(this.exceptionStringRange(startIndex, startIndex + elements.size()-1));
		}
		final Object[] data = this.data;
		int i = startIndex;
		if(elements instanceof RandomAccess){
			for(int ei = elementsStartIndex, bound = elementsStartIndex + length; ei < bound; ei++){
				data[i++] = elements.get(ei);
			}
		}
		else {
			int ei = 0;
			int count = 0;
			for(final E e : elements){
				if(ei++ < elementsStartIndex) continue;
				data[i++] = e;
				if(++count == length) break;
			}
		}
		return this;
	}

	/**
	 * Sorts this list using the Quicksort algorithm.<br>
	 * As a rule of thumb, use this algorithm if sorting stability is not relevant
	 * and if the list doesn't happen to be presorted often.<br>
	 * Otherwise, use {@link #sortMerge(Comparator)}.
	 * <p>
	 * <b>Performance</b><ul>
	 * <li>Best case &#160;: fastest known algorithm [O(n log n)]</li>
	 * <li>Average &#160;&#160; : fastest known algorithm [O(n log n)]</li>
	 * <li>Worst case: theoretically slow [O(n^2)], but hardly relevant in practice. More like O(k * n log n)</li>
	 * <li>Presorted&#160; : Depends on pivot element.
	 * Choosing the middle element yields like O(k * n log n) in tests.</li>
	 * </ul>
	 * <b>Characteristics</b><ul>
	 * <li>Stable &#160;&#160; : no</li>
	 * <li>In-place : yes (plus recursive call stack)</li>
	 * </ul>
	 * @param comparator the comparator to use for sorting the elements.
	 * @return this list.
	 * @see JaSort#quicksort(Object[], Comparator)
	 */
	@SuppressWarnings("unchecked")
	public GrowList<E> sortQuick(final Comparator<E> comparator)
	{
		JaSort.quicksort(this.data, 0, this.size-1, (Comparator<Object>)comparator);
		return this;
	}

	/**
	 *
	 * @param startIndex
	 * @param endIndex
	 * @param comparator
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public GrowList<E> rngSortQuick(int startIndex, int endIndex, final Comparator<E> comparator)
	{
		if(startIndex > endIndex){
			final int t = startIndex;
			startIndex = endIndex;
			endIndex = t;
		}

		if(startIndex < 0 || endIndex >= this.size){
			throw new IndexOutOfBoundsException(this.exceptionStringRange(startIndex, endIndex));
		}
		JaSort.quicksort(this.data, startIndex, endIndex, (Comparator<Object>)comparator);
		return this;
	}

	/**
	 * @return
	 * @see com.xdev.jadoth.collections.XList#binarySearch()
	 */
	@SuppressWarnings("unchecked")
	@Override
	public int binarySearch(final E element, final Comparator<E> comparator)
	{
		int low = 0;
		int high = this.size - 1;
		final Object[] data = this.data;
		while (low <= high) {
		    final int mid = low + high >>> 1;
		    final Object midVal = data[mid];

		    final int cmp = comparator.compare((E)midVal, element);
		    if(cmp < 0)
		    	low = mid + 1;
		    else if (cmp > 0)
		    	high = mid - 1;
		    else
		    	return mid; // key found
		}
		return -(low + 1);  // key not found.
	}

	/**
	 * @param startIndex
	 * @param endIndex
	 * @param element
	 * @param comparator
	 * @return
	 * @see com.xdev.jadoth.collections.XList#rngBinarySearch(int, int, java.lang.Object, java.util.Comparator)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public int rngBinarySearch(final int startIndex, final int endIndex, final E element, final Comparator<E> comparator)
	{
		int low, high;

		if(startIndex <= endIndex){
			if(startIndex < 0 || endIndex >= this.size){
				throw new IndexOutOfBoundsException(this.exceptionStringRange(startIndex, endIndex));
			}
			low = startIndex;
			high = endIndex;
		}
		else {
			if(startIndex >= this.data.length || endIndex < 0){
				throw new IndexOutOfBoundsException(this.exceptionStringRange(endIndex, startIndex));
			}
			low = endIndex;
			high = startIndex;
		}

		final Object[] data = this.data;

		while (low <= high) {
		    final int mid = low + high >>> 1;
		    final Object midVal = data[mid];

		    final int cmp = comparator.compare((E)midVal, element);
		    if(cmp < 0)
		    	low = mid + 1;
		    else if (cmp > 0)
		    	high = mid - 1;
		    else
		    	return mid; // key found
		}
		return -(low + 1);  // key not found.
	}

	/**
	 * Sorts this list using the Mergesort algorithm.<br>
	 * As a rule of thumb, use this algorithm for good results in all situations.<br>
	 * If ALL of the following conditions are met, use {@link #sortQuick(Comparator)} instead:
	 * <ul>
	 * <li>Best performance for average case is needed.</li>
	 * <li>Sorting stability is not relevant.</li>
	 * <li>The list doesn't happen to be presorted (often).</li>
	 * </ul>
	 * <b>Performance</b><ul>
	 * <li>Best case &#160;: fast, but slower than Quicksort [O(n log n)]</li>
	 * <li>Average &#160;&#160; : fast, but slower than Quicksort [O(n log n)]</li>
	 * <li>Worst case: fast, faster than Quicksort [O(n log n)]</li>
	 * <li>Presorted&#160; : very fast [O(n)]</li>
	 * </ul>
	 * <b>Characteristics</b><ul>
	 * <li>Stable &#160;&#160; : yes</li>
	 * <li>In-place : no (array instantiations)</li>
	 * </ul>
	 * @param comparator the comparator to use for sorting the elements.
	 * @return this list.
	 * @see JaSort#mergesort(Object[], Comparator)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public GrowList<E> sortMerge(final Comparator<E> comparator)
	{
		JaSort.mergesort(this.data, 0, this.size-1, (Comparator<Object>)comparator);
		return this;
	}

	/**
	 * @param startIndex
	 * @param endIndex
	 * @param comparator
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Override
	public GrowList<E> rngSortMerge(final int startIndex, final int endIndex, final Comparator<E> comparator)
	{
		if(startIndex <= endIndex){
			if(startIndex < 0 || endIndex >= this.size){
				throw new IndexOutOfBoundsException(this.exceptionStringRange(startIndex, endIndex));
			}
			JaSort.mergesort(this.data, startIndex, endIndex, (Comparator<Object>)comparator);
		}
		else {
			if(startIndex >= this.size || endIndex < 0){
				throw new IndexOutOfBoundsException(this.exceptionStringRange(endIndex, startIndex));
			}
			JaSort.mergesort(this.data, endIndex, startIndex, (Comparator<Object>)comparator);
		}
		return this;
	}

	/**
	 * @param startIndex
	 * @param endIndex
	 * @return
	 */
	@Override
	public GrowList<E> rngShuffle(int startIndex, int endIndex)
	{
		if(startIndex > endIndex){
			final int t = startIndex;
			startIndex = endIndex;
			endIndex = t;
		}
		if(startIndex < 0 || endIndex >= this.size){
			throw new IndexOutOfBoundsException(this.exceptionStringRange(startIndex, endIndex));
		}

		final Random r = JaMath.random();
		for (int i = endIndex; i > startIndex; i--){
			 this.internalSwap(i, r.nextInt(i));
		}
		return this;
	}

	/**
	 * @return
	 */
	@Override
	public GrowList<E> shuffle()
	{
		final Random r = JaMath.random();
		for (int i = this.size; i > 1; i--){
			 this.internalSwap(i-1, r.nextInt(i));
		}
		return this;
	}

	/**
	 * @param indexA
	 * @param indexB
	 */
	@Override
	public void swap(final int indexA, final int indexB)
	throws IndexOutOfBoundsException, ArrayIndexOutOfBoundsException
	{
		if(indexA >= this.size){
			throw new IndexOutOfBoundsException(this.exceptionStringIndexOutOfBounds(indexA));
		}
		if(indexB >= this.size){
			throw new IndexOutOfBoundsException(this.exceptionStringIndexOutOfBounds(indexB));
		}
		final Object[] data = this.data; //cache variable really helps, mostly for writing accesses
		final Object t = data[indexA];
		data[indexA] = data[indexB];
		data[indexB] = t;
	}
	
	/**
	 * @param comparator
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Override
	public boolean isSorted(final Comparator<E> comparator)
	{
		final Object[] data = this.data;
		if(this.size <= 1) return true;

		Object loopLastElement = data[0];
		for(int i = 1; i < this.size; i++){
			final Object e = data[i];
			if(comparator.compare((E)loopLastElement, (E)e) > 0) return false;
			loopLastElement = e;
		}
		return true;
	}

	/**
	 * @param startIndex
	 * @param endIndex
	 * @param comparator
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Override
	public boolean rngIsSorted(final int startIndex, final int endIndex, final Comparator<E> comparator)
	{
		final int d; //bi-directional index movement
		if(startIndex <= endIndex){
			if(startIndex < 0 || endIndex >= this.size){
				throw new IndexOutOfBoundsException(this.exceptionStringRange(startIndex, endIndex));
			}
			d = 1; //incrementing direction
		}
		else {
			if(startIndex >= this.size || endIndex < 0){
				throw new IndexOutOfBoundsException(this.exceptionStringRange(endIndex, startIndex));
			}
			d = -1; //decrementing direction
		}

		final Object[] data = this.data;
		E loopLastElement = (E)data[startIndex];		
		for(int i = startIndex-d; i != endIndex;){
			final E e = (E)data[i+=d];
			if(comparator.compare(loopLastElement, e) > 0) return false;
			loopLastElement = e;
		}
		return true;
	}
	
	/**
	 * @param element
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Override
	public int indexOf(final E element, final Equalator<E> equalator)
	{
		final Object[] data = this.data;
		for(int i = 0, size = this.size; i < size; i++){
			if(equalator.equal(element, (E)data[i])){
				return i;
			}
		}
		return -1;
	}

	/**
	 * @param element
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Override
	public int lastIndexOf(final E element, final Equalator<E> equalator)
	{
		final Object[] data = this.data;
		for(int i = this.size; i --> 0;){
			if(equalator.equal(element, (E)data[i])){
				return i;
			}
		}
		return -1;
	}
	
	/**
	 * @param element
	 * @return
	 */
	@Override
	public E removeOne(final E element)
	{
		final Object[] data = this.data;
		for(int i = 0, size = this.size; i < size; i++){
			if(element == data[i]){
				if(i < --size){
					System.arraycopy(data, i+1, data, i, size - i);
				}
				data[size] = null;
				this.size = size;
				return element;
			}
		}
		return null;
	}

	/**
	 * @param o
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Override
	public E removeOne(final E element, final Equalator<E> equalator)
	{
		final Object[] data = this.data;
		for(int i = 0, size = this.size; i < size; i++){
			if(equalator.equal(element, (E)data[i])){
				final E oldElement = (E)data[i];
				if(i < --size){
					System.arraycopy(data, i+1, data, i, size - i);
				}
				data[size] = null;
				this.size = size;
				return oldElement;
			}
		}
		return null;
	}
	
	/**
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Override
	public E removeFirst()
	{
		final Object e = this.data[0];
		System.arraycopy(this.data, 1, this.data, 0, --this.size);
		this.data[this.size] = null;
		return (E)e;
	}

	/**
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Override
	public E removeLast()
	{
		final Object e = this.data[--this.size];
		this.data[this.size] = null;
		return (E)e;
	}

	/**
	 * @param o
	 * @return
	 */
	@Override
	public boolean containsId(final E o)
	{
		final Object[] data = this.data;
		for(int i = 0, size = this.size; i < size; i++){
			if(o == data[i]) return true;
		}
		return false;
	}

	/**
	 * @param startIndex
	 * @param endIndex
	 * @param element
	 * @return
	 */
	@Override
	public boolean rngContainsId(final int startIndex, final int endIndex, final E element)
	{
		final int d;
		if(startIndex <= endIndex){
			if(startIndex < 0 || endIndex >= this.size){
				throw new IndexOutOfBoundsException(this.exceptionStringRange(startIndex, endIndex));
			}
			d = 1;
		}
		else {
			if(startIndex >= this.size || endIndex < 0){
				throw new IndexOutOfBoundsException(this.exceptionStringRange(endIndex, startIndex));
			}
			d = -1;
		}
		
		final Object[] data = this.data;
		for(int i = startIndex-d; i != endIndex;){
			if(data[i+=d] == element) return true;
		}
		return false;
	}
	
	/**
	 * @param predicate
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Override
	public int scan(final Predicate<E> predicate)
	{
		int loopIndex = -1;
		final Object[] data = this.data;		
		for(int i = 0, size = this.size; i < size; i++){
			if(predicate.apply((E)data[i])){
				loopIndex = i;
			}
		}
		return loopIndex;
	}

	/**
	 * @param startIndex
	 * @param endIndex
	 * @param predicate
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Override
	public int rngScan(final int startIndex, final int endIndex, final Predicate<E> predicate)
	{
		final int d;
		if(startIndex <= endIndex){
			if(startIndex < 0 || endIndex >= this.size){
				throw new IndexOutOfBoundsException(this.exceptionStringRange(startIndex, endIndex));
			}
			d = 1;
		}
		else {
			if(startIndex >= this.size || endIndex < 0){
				throw new IndexOutOfBoundsException(this.exceptionStringRange(endIndex, startIndex));
			}
			d = -1;
		}

		int foundIndex = -1;
		final Object[] data = this.data;		
		for(int i = startIndex-d; i != endIndex;){
			if(predicate.apply((E)data[i+=d])){
				foundIndex = i;
			}
		}
		return foundIndex;
	}
		
	/**
	 * @param equalator
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Override
	public boolean hasUniqueValues(final boolean ignoreNulls, final Equalator<E> equalator)
	{
		final Object[] data = this.data;

		for(int i = 0, size = this.size; i < size; i++){
			final Object e = data[i];
			if(e == null && ignoreNulls) continue;
			for(int j = i + 1; j < size; j++){
				if(equalator.equal((E)e, (E)data[j])) return false;
			}
		}
		return true;
	}
	
	//reviewed ######

	/**
	 * @param startIndex
	 * @param endIndex
	 * @param comparator
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Override
	public boolean rngHasUniqueValues(final int startIndex, final int endIndex, final boolean ignoreMultipleNulls, final Equalator<E> comparator)
	{
		final int d; //bi-directional index movement
		if(startIndex <= endIndex){
			if(startIndex < 0 || endIndex >= this.size){
				throw new IndexOutOfBoundsException(this.exceptionStringRange(startIndex, endIndex));
			}
			d = 1; //incrementing direction
		}
		else {
			if(startIndex >= this.size || endIndex < 0){
				throw new IndexOutOfBoundsException(this.exceptionStringRange(endIndex, startIndex));
			}
			d = -1; //decrementing direction
		}

		final Object[] data = this.data;
		for(int i = startIndex-d; i != endIndex;){
			final Object e = data[i+=d];
			if(e == null && ignoreMultipleNulls) continue;
			for(int j = i; j != endIndex;){
				if(comparator.equal((E)e, (E)data[j+=d])) return false;
			}
		}
		return true;
	}

	/**
	 * @param c
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Override
	public boolean containsAll(final Collection<E> c, final boolean ignoreNulls, final Equalator<E> equalator)
	{
		final Object[] data = this.data;
		final int size = this.size;
		boolean nullAreadyHandled = ignoreNulls;
		collectionLoop: for(final E o : c) {
			if(o == null){
				if(nullAreadyHandled) continue;
				nullAreadyHandled = true;
				for(int i = 0; i < size; i++){
					if(data[i] == null) continue collectionLoop;
				}
			}
			else {
				for(int i = 0; i < size; i++){
					if(equalator.equal(o,(E)data[i])) continue collectionLoop;
				}
			}
			return false;
		}
		//all elements of c have been found
		return true;
	}

	/**
	 * @param c
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Override
	public int retainAll(final XCollection<E> c, final Equalator<E> equalator)
	{
		final int removedCount;
		final Object[] data = this.data;
		final int size = this.size;		
		try{
			for(int i = 0; i < size; i++){
				if(!c.contains((E)data[i], equalator)){
					data[i] = REMOVE_MARKER;
				}
			}
		}
		finally{
			removedCount = JaArrays.removeAllFromArray(REMOVE_MARKER, this.data, 0, size, this.data, 0, size);
		}
		this.size -= removedCount;
		return removedCount;
	}

	/**
	 * @param element
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Override
	public int remove(final E element, final int skip, final Integer limit, final Equalator<E> equalator)
	{
		final int size = this.size;

		int lim = limit == null ?Integer.MAX_VALUE :limit.intValue();

		// limit checking
		if(lim >= size){
			// no limit, delegate to util method (does element null checking on its own)
			return removeAllFromArray(element, this.data, 0, this.size, this.data, 0, this.size, (Equalator<Object>)equalator);
		}
		else if(lim <= 0){
			// spare unnecessary traversal of array in util method
			return 0;
		}
		else if(element == null){
			// element null checking for limited removals
			return this.removeId(null, skip, limit);
		}
		// normal cases starts here

		// mark to be removed buckets as long as limit permits, then remove the markers
		final Object[] data = this.data;
		final int removeCount;
		try{
			for(int i = 0; i < size && lim > 0; i++){
				if(element.equals(data[i])){
					data[i] = REMOVE_MARKER;
					lim--;
				}
			}
		}
		finally{
			removeCount = removeAllFromArray(REMOVE_MARKER, this.data, 0, this.size, this.data, 0, this.size);
		}

		this.size -= removeCount;
		return removeCount;
	}

	/**
	 * @param indexA
	 * @param indexB
	 * @param range
	 */
	@Override
	public void swap(final int indexA, final int indexB, final int length)
	{
		final int low, high;
		if(indexA <= indexB){
			low = indexA;
			high = indexB;
		}
		else {
			low = indexB;
			high = indexA;
		}
		final Object[] data = this.data;
		final int size = this.size;

		if(low < 0 || length < 0 || low+length >= high || high+length >= size ){
			throw new IndexOutOfBoundsException(this.exceptionStringIllegalSwapBounds(indexA, indexB, length));
		}
		final int bound = low + length;
		for(int l = low, h = high; l < bound;){
			final Object t = data[l];
			data[l++] = data[h];
			data[h++] = t;
		}
	}

	/**
	 * @param startIndex
	 * @param endIndex
	 * @param predicate
	 * @return
	 * @see com.xdev.jadoth.collections.XList#rngScan(int, int, com.xdev.jadoth.lang.functional.controlflow.TPredicate)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public int rngScan(final int startIndex, final int endIndex, final TPredicate<E> predicate)
	{
		final int d; //bi-directional index movement
		if(startIndex <= endIndex){
			if(startIndex < 0 || endIndex >= this.size){
				throw new IndexOutOfBoundsException(this.exceptionStringRange(startIndex, endIndex));
			}
			d = 1; //incrementing direction
		}
		else {
			if(startIndex >= this.size || endIndex < 0){
				throw new IndexOutOfBoundsException(this.exceptionStringRange(endIndex, startIndex));
			}
			d = -1; //decrementing direction
		}

		int foundIndex = -1;

		//this construction yields the same performance as the loop without inner try, but can still continue the loop
		final Object[] data = this.data;
		int i = startIndex-d; //start i one before startIndex as (i+=d) only works as prefix ~crement
		while(i != endIndex){ //while(true) astonishingly is significantly slower than duplicated inner condition
			try {
				while(i != endIndex){
					if(predicate.apply((E)data[i+=d])){
						foundIndex = i;
					}
				}
				break;
			}
			catch(final ThrowBreak b)   { break; }
			catch(final ThrowContinue c){ /*Nothing*/ }
			catch(final ThrowReturn r)  { return foundIndex; }
		}
		return foundIndex;
	}

	/**
	 * @param predicate
	 * @return
	 * @see com.xdev.jadoth.collections.XList#scan(com.xdev.jadoth.lang.functional.controlflow.TPredicate)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public int scan(final TPredicate<E> predicate)
	{
		//this construction yields the same performance as the loop without try, but can still continue the loop
		final Object[] data = this.data;
		final int size = this.size;
		int i = -1;
		int loopIndex = -1;
		while(i < size){ //while(true) astonishingly is significantly slower than duplicated inner condition
			try {
				while(i < size){
					if(predicate.apply((E)data[++i])){
						loopIndex = i;
					}
				}
				break;
			}
			catch(final ThrowBreak b)   { break; }
			catch(final ThrowReturn r)  { return loopIndex; }
			catch(final ThrowContinue c){ /*Nothing*/ }
		}
		return loopIndex;
	}

	/**
	 * @param startIndex
	 * @param endIndex
	 * @param element
	 * @return
	 * @see com.xdev.jadoth.collections.XList#rngIndexOfId(int, int, java.lang.Object)
	 */
	@Override
	public int rngIndexOfId(final int startIndex, final int endIndex, final E element)
	{
		final int d; //bi-directional index movement
		if(startIndex <= endIndex){
			if(startIndex < 0 || endIndex >= this.size){
				throw new IndexOutOfBoundsException(this.exceptionStringRange(startIndex, endIndex));
			}
			d = 1; //incrementing direction
		}
		else {
			if(startIndex >= this.size || endIndex < 0){
				throw new IndexOutOfBoundsException(this.exceptionStringRange(endIndex, startIndex));
			}
			d = -1; //decrementing direction
		}

		final Object[] data = this.data;
		for(int i = startIndex-d; i != endIndex;){
			if(element == data[i+=d]){
				return i;
			}
		}
		return -1;
	}

	/**
	 * @param startIndex
	 * @param endIndex
	 * @param element
	 * @return
	 * @see com.xdev.jadoth.collections.XList#rngRemoveOne(int, int, java.lang.Object)
	 */
	@Override
	public E rngRemoveOne(final int startIndex, final int endIndex, final E element)
	{
		final int d; //bi-directional index movement
		if(startIndex <= endIndex){
			if(startIndex < 0 || endIndex >= this.size){
				throw new IndexOutOfBoundsException(this.exceptionStringRange(startIndex, endIndex));
			}
			d = 1; //incrementing direction
		}
		else {
			if(startIndex >= this.size || endIndex < 0){
				throw new IndexOutOfBoundsException(this.exceptionStringRange(endIndex, startIndex));
			}
			d = -1; //decrementing direction
		}

		final Object[] data = this.data;
		for(int i = startIndex-d; i != endIndex;){
			if(element == data[i+=d]){
				if(i < --this.size){
					System.arraycopy(data, i+1, data, i, this.size - i);
				}				
				data[this.size] = null;
				return element;
			}
		}
		return null;
	}

	/**
	 * @param startIndex
	 * @param endIndex
	 * @param type
	 * @return
	 * @see com.xdev.jadoth.collections.XList#rngToArray(int, int, java.lang.Class)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public E[] rngToArray(final int startIndex, final int endIndex, final Class<E> type)
	{
		final Object[] data = this.data;
		final boolean incrementing;
		final int targetLength;
		if(startIndex <= endIndex){
			if(startIndex < 0 || endIndex >= this.size){
				throw new IndexOutOfBoundsException(this.exceptionStringRange(startIndex, endIndex));
			}
			incrementing = true;
			targetLength = endIndex - startIndex + 1;
		}
		else {
			if(startIndex >= this.size || endIndex < 0){
				throw new IndexOutOfBoundsException(this.exceptionStringRange(endIndex, startIndex));
			}
			incrementing = false;
			targetLength = startIndex - endIndex + 1;
		}

		final E[] array = (E[])Array.newInstance(type, targetLength);


		if(incrementing){
			System.arraycopy(data, startIndex, array, 0, targetLength);
		}
		else {
			for(int i = startIndex, j = 0; i >= endIndex; i--){
				array[j++] = (E)data[i];
			}
		}
		return array;
	}

	/**
	 * @param type
	 * @return
	 * @see com.xdev.jadoth.collections.XList#toArray(java.lang.Class)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public E[] toArray(final Class<E> type)
	{
		final E[] array = (E[])Array.newInstance(type, this.size);
		System.arraycopy(this.data, 0, array, 0, this.size);
		return array;
	}

	/**
	 * Processes each element in this list by executing the passed operation on it and then removes the element.<br>
	 * Thus, the list will be empty after this method has finished.
	 * <p>
	 * Note that this method has special behaviour by processing the elements BACKWARDS for implementation-dependant
	 * performance reasons.<br>
	 * Use <code>rngProcess(0, list.size()-1, operation)</code> for forward processing but
	 * <b>beware of its horrible performance</b>.
	 * <p>
	 * <b>It is strongly recommended to use a combination of {@link #execute(Operation)} and {@link #clear()}
	 * for any other processing strategies distinct from the upper end backwards processing of this method.</b>
	 * <p>
	 * Note that the interal storage size remains the same. To reduce it as well, call
	 * {@link #shrink()} (will create new storage with minimal size)
	 * or {@link #truncate()} (will create a new storage with default size).
	 *
	 * @param operation the operation by which every element in the list shall be processed
	 * @return this (then empty) list
	 * @see com.xdev.jadoth.collections.XList#process(com.xdev.jadoth.lang.functional.Operation)
	 * @see #process(TOperation)
	 * @see #rngProcess(int, int, Operation)
	 * @see #rngProcess(int, int, TOperation)
	 * @see #clear()
	 * @see #shrink()
	 * @see #truncate()
	 */
	@SuppressWarnings("unchecked")
	@Override
	public GrowList<E> process(final Operation<E> operation)
	{
		final Object[] data = this.data;
		final int size = this.size;

		int i = size;
		while(i --> 0){
			operation.execute((E)data[i]);
			data[i] = null;
			this.size--;
		}
		return this;
	}

	/**
	 * Processes each element in this list by executing the passed operation on it and then removes the element.<br>
	 * Thus, the list will be empty after this method has finished.
	 * <p>
	 * Note that this method has special behaviour by processing the elements BACKWARDS for implementation-dependant
	 * performance reasons.<br>
	 * Use <code>rngProcess(0, list.size()-1, operation)</code> for forward processing but
	 * <b>beware of its horrible performance</b>.
	 * <p>
	 * <b>It is strongly recommended to use a combination of {@link #execute(ControllingOperation))} and
	 * {@link #clear()} for any other processing strategies distinct from the upper end backwards processing of this
	 * method.</b>
	 * <p>
	 * Note that the interal storage size remains the same. To reduce it as well, call
	 * {@link #shrink()} (will create new storage with minimal size)
	 * or {@link #truncate()} (will create a new storage with default size).
	 *
	 * @param operation
	 * @return
	 * @see com.xdev.jadoth.collections.XList#process(com.xdev.jadoth.lang.functional.controlflow.TOperation)
	 * @see #process(Operation)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public GrowList<E> process(final TOperation<E> operation)
	{
		final Object[] data = this.data;
		final int size = this.size;

		int i = size;
		E e;
		while(i > 0){ //while(true) astonishingly is significantly slower than duplicated inner condition
			try {
				while(i > 0){
					e = (E)data[--i];
					// note that element must be removed before executing operation
					data[i] = null;
					this.size--;
					// even if this throws a controlthrow, the current element may not remain!
					operation.execute(e);
				}
				break;
			}
			catch(final ThrowBreak b)   { break; }
			catch(final ThrowReturn r)  { return this; }
			catch(final ThrowContinue c){ /*Nothing*/ }
		}
		return this;
	}

	/**
	 * Note that this method will have <b>horrible</b> performance vor any start index other than <code>size - 1</code>
	 * due to the array list character of this list's implementation. It is strongly recommended to substitute
	 * calls to this method by a combination of {@link #rngExecute(int, int, Operation)} and
	 * {@link #removeRange(int, int)}
	 *
	 * @param startIndex
	 * @param endIndex
	 * @param operation
	 * @return
	 * @see com.xdev.jadoth.collections.XList#rngProcess(int, int, com.xdev.jadoth.lang.functional.Operation)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public GrowList<E> rngProcess(final int startIndex, final int endIndex, final Operation<E> operation)
	{
		final int d; //bi-directional index movement
		if(startIndex <= endIndex){
			if(startIndex < 0 || endIndex >= this.size){
				throw new IndexOutOfBoundsException(this.exceptionStringRange(startIndex, endIndex));
			}
			d = 1; //incrementing direction
		}
		else {
			if(startIndex >= this.size || endIndex < 0){
				throw new IndexOutOfBoundsException(this.exceptionStringRange(endIndex, startIndex));
			}
			d = -1; //decrementing direction
		}

		final Object[] data = this.data;
		for(int i = startIndex-d; i != endIndex;){
			operation.execute((E)data[i+=d]);
			this.remove(i);
		}
		return this;
	}

	/**
	 * Note that this method will have <b>horrible</b> performance vor any start index other than <code>size - 1</code>
	 * due to the array list character of this list's implementation. It is strongly recommended to substitute
	 * calls to this method by a combination of {@link #rngExecute(int, int, TOperation)} and
	 * {@link #removeRange(int, int)}
	 *
	 * @param startIndex
	 * @param endIndex
	 * @param operation
	 * @return
	 * @see com.xdev.jadoth.collections.XList#rngProcess(int, int, com.xdev.jadoth.lang.functional.controlflow.TOperation)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public GrowList<E> rngProcess(final int startIndex, final int endIndex, final TOperation<E> operation)
	{
		final int d; //bi-directional index movement
		if(startIndex <= endIndex){
			if(startIndex < 0 || endIndex >= this.size){
				throw new IndexOutOfBoundsException(this.exceptionStringRange(startIndex, endIndex));
			}
			d = 1; //incrementing direction
		}
		else {
			if(startIndex >= this.size || endIndex < 0){
				throw new IndexOutOfBoundsException(this.exceptionStringRange(endIndex, startIndex));
			}
			d = -1; //decrementing direction
		}


		//this construction yields the same performance as the loop without inner try, but can still continue the loop
		final Object[] data = this.data;
		int i = startIndex-d; //start i one before startIndex as (i+=d) only works as prefix ~crement
		while(i != endIndex){ //while(true) astonishingly is significantly slower than duplicated inner condition
			try {
				while(i != endIndex){
					operation.execute((E)data[i+=d]);
					this.remove(i);
				}
				break;
			}
			catch(final ThrowBreak b)   { break; }
			catch(final ThrowContinue c){ /*Nothing*/ }
			catch(final ThrowReturn r)  { return this; }
		}
		return this;
	}

	/**
	 *
	 * @param predicate
	 * @return
	 * @see com.xdev.jadoth.collections.XList#applies(com.xdev.jadoth.lang.functional.Predicate)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public boolean applies(final Predicate<E> predicate)
	{
		final Object[] data = this.data;
		for(int i = 0, size = this.size; i < size; i++){
			if(!predicate.apply((E)data[i])){
				return false;
			}
		}
		return true;
	}

	/**
	 * @param predicate
	 * @return
	 * @see com.xdev.jadoth.collections.XList#applies(com.xdev.jadoth.lang.functional.controlflow.TPredicate)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public boolean applies(final TPredicate<E> predicate)
	{
		final Object[] data = this.data;
		final int size = this.size;

		int i = 0;
		while(i < size){ //while(true) astonishingly is significantly slower than duplicated inner condition
			try {
				while(i < size){
					if(!predicate.apply((E)data[i++])){
						return false;
					}
				}
				break;
			}
			catch(final ThrowBreak b)   { return false; } //interesting, too: the break must be compromised
			catch(final ThrowReturn r)  { return true; } //interesting: this throw means: 'it applies enough'
			catch(final ThrowContinue c){ /*Nothing*/ }
		}
		return true;
	}

	/**
	 * @param startIndex
	 * @param endIndex
	 * @param predicate
	 * @return
	 * @see com.xdev.jadoth.collections.XList#rngApplies(int, int, com.xdev.jadoth.lang.functional.Predicate)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public boolean rngApplies(final int startIndex, final int endIndex, final Predicate<E> predicate)
	{
		final int d; //bi-directional index movement
		if(startIndex <= endIndex){
			if(startIndex < 0 || endIndex >= this.size){
				throw new IndexOutOfBoundsException(this.exceptionStringRange(startIndex, endIndex));
			}
			d = 1; //incrementing direction
		}
		else {
			if(startIndex >= this.size || endIndex < 0){
				throw new IndexOutOfBoundsException(this.exceptionStringRange(endIndex, startIndex));
			}
			d = -1; //decrementing direction
		}

		final Object[] data = this.data;
		for(int i = startIndex-d; i != endIndex;){
			if(!predicate.apply((E)data[i+=d])) return false;
		}

		return true;
	}

	/**
	 * @param startIndex
	 * @param endIndex
	 * @param predicate
	 * @return
	 * @see com.xdev.jadoth.collections.XList#rngApplies(int, int, com.xdev.jadoth.lang.functional.controlflow.TPredicate)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public boolean rngApplies(final int startIndex, final int endIndex, final TPredicate<E> predicate)
	{
		final int d; //bi-directional index movement
		if(startIndex <= endIndex){
			if(startIndex < 0 || endIndex >= this.size){
				throw new IndexOutOfBoundsException(this.exceptionStringRange(startIndex, endIndex));
			}
			d = 1; //incrementing direction
		}
		else {
			if(startIndex >= this.size || endIndex < 0){
				throw new IndexOutOfBoundsException(this.exceptionStringRange(endIndex, startIndex));
			}
			d = -1; //decrementing direction
		}


		//this construction yields the same performance as the loop without inner try, but can still continue the loop
		final Object[] data = this.data;
		int i = startIndex-d; //start i one before startIndex as (i+=d) only works as prefix ~crement
		while(i != endIndex){ //while(true) astonishingly is significantly slower than duplicated inner condition
			try {
				while(i != endIndex){
					if(!predicate.apply((E)data[i+=d])) return false;
				}
				break;
			}
			catch(final ThrowBreak b)   { break; }
			catch(final ThrowContinue c){ /*Nothing*/ }
			catch(final ThrowReturn r)  { return false; }
		}
		return false;
	}

	/**
	 * @param startIndex
	 * @param endIndex
	 * @param predicate
	 * @return
	 * @see com.xdev.jadoth.collections.XList#rngMoveTo(int, int, com.xdev.jadoth.lang.functional.Predicate)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public <L extends Collecting<E>> L rngMoveTo(
		final int startIndex, final int endIndex, final L target, final Predicate<E> predicate
	)
	{
		final int d; //bi-directional index movement
		if(startIndex <= endIndex){
			if(startIndex < 0 || endIndex >= this.size){
				throw new IndexOutOfBoundsException(this.exceptionStringRange(startIndex, endIndex));
			}
			d = 1; //incrementing direction
		}
		else {
			if(startIndex >= this.size || endIndex < 0){
				throw new IndexOutOfBoundsException(this.exceptionStringRange(endIndex, startIndex));
			}
			d = -1; //decrementing direction
		}

		final Object[] data = this.data;
		E e;
		for(int i = startIndex-d; i != endIndex;){
			if(predicate.apply(e = (E)data[i+=d])){
				target.add(e);
			}
		}
		return target;
	}

	/**
	 * @param startIndex
	 * @param endIndex
	 * @param predicate
	 * @return
	 * @see com.xdev.jadoth.collections.XList#rngMoveTo(int, int, com.xdev.jadoth.lang.functional.controlflow.TPredicate)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public <L extends Collecting<E>> L rngMoveTo(
		final int startIndex, final int endIndex, final L target, final TPredicate<E> predicate
	)
	{
		final int d; //bi-directional index movement
		if(startIndex <= endIndex){
			if(startIndex < 0 || endIndex >= this.size){
				throw new IndexOutOfBoundsException(this.exceptionStringRange(startIndex, endIndex));
			}
			d = 1; //incrementing direction
		}
		else {
			if(startIndex >= this.size || endIndex < 0){
				throw new IndexOutOfBoundsException(this.exceptionStringRange(endIndex, startIndex));
			}
			d = -1; //decrementing direction
		}

		//this construction yields the same performance as the loop without inner try, but can still continue the loop
		final Object[] data = this.data;
		int i = startIndex-d; //start i one before startIndex as (i+=d) only works as prefix ~crement
		E e;
		while(i != endIndex){ //while(true) astonishingly is significantly slower than duplicated inner condition
			try {
				while(i != endIndex){
					if(predicate.apply(e = (E)data[i+=d])){
						target.add(e);
					}
				}
				break;
			}
			catch(final ThrowBreak b)   { break; }
			catch(final ThrowContinue c){ /*Nothing*/ }
			catch(final ThrowReturn r)  { return target; }
		}
		return target;
	}

	/**
	 * @param comparator
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Override
	public E max(final Comparator<E> comparator)
	{
		final Object[] data = this.data;
		final int size = this.size;

		if(size == 0) return null;

		E loopMaxElement = (E)data[0];
		E e;
		for(int i = 1; i < size; i++){
			e = (E)data[i];
			if(comparator.compare(loopMaxElement, e) < 0){
				loopMaxElement = e;
			}
		}
		return loopMaxElement;
	}

	/**
	 * @param startIndex
	 * @param endIndex
	 * @param comparator
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Override
	public E rngMax(final int startIndex, final int endIndex, final Comparator<E> comparator)
	{
		final int d; //bi-directional index movement
		if(startIndex <= endIndex){
			if(startIndex < 0 || endIndex >= this.size){
				throw new IndexOutOfBoundsException(this.exceptionStringRange(startIndex, endIndex));
			}
			d = 1; //incrementing direction
		}
		else {
			if(startIndex >= this.size || endIndex < 0){
				throw new IndexOutOfBoundsException(this.exceptionStringRange(endIndex, startIndex));
			}
			d = -1; //decrementing direction
		}
		if(endIndex == startIndex) return null;


		final Object[] data = this.data;
		E loopMaxElement = (E)data[startIndex];
		E e;
		for(int i = startIndex-d; i != endIndex;){
			e = (E)data[i+=d];
			if(comparator.compare(loopMaxElement, e) < 0){
				loopMaxElement = e;
			}
		}
		return loopMaxElement;
	}

	/**
	 * @param comparator
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Override
	public E min(final Comparator<E> comparator)
	{
		final Object[] data = this.data;
		final int size = this.size;

		if(size == 0) return null;

		E loopMinElement = (E)data[0];
		E e;
		for(int i = 1; i < size; i++){
			e = (E)data[i];
			if(comparator.compare(loopMinElement, e) > 0){
				loopMinElement = e;
			}
		}
		return loopMinElement;
	}

	/**
	 * @param startIndex
	 * @param endIndex
	 * @param comparator
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Override
	public E rngMin(final int startIndex, final int endIndex, final Comparator<E> comparator)
	{
		final int d; //bi-directional index movement
		if(startIndex <= endIndex){
			if(startIndex < 0 || endIndex >= this.size){
				throw new IndexOutOfBoundsException(this.exceptionStringRange(startIndex, endIndex));
			}
			d = 1; //incrementing direction
		}
		else {
			if(startIndex >= this.size || endIndex < 0){
				throw new IndexOutOfBoundsException(this.exceptionStringRange(endIndex, startIndex));
			}
			d = -1; //decrementing direction
		}
		if(endIndex == startIndex) return null;

		E loopMinElement = (E)this.data[startIndex];
		final Object[] data = this.data;
		for(int i = startIndex; i != endIndex;){
			final E e = (E)data[i+=d];
			if(comparator.compare(loopMinElement, e) > 0){
				loopMinElement = e;
			}
		}
		return loopMinElement;
	}

	/**
	 * @param comparator
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Override
	public int maxIndex(final Comparator<E> comparator)
	{
		final Object[] data = this.data;
		final int size = this.size;

		if(size == 0) return -1;

		E loopMaxElement = (E)data[0];
		int loopMaxIndex = 0;
		E e;
		for(int i = 1; i < size; i++){
			e = (E)data[i];
			if(comparator.compare(loopMaxElement, e) < 0){
				loopMaxElement = e;
				loopMaxIndex = i;
			}
		}
		return loopMaxIndex;
	}

	/**
	 * @param startIndex
	 * @param endIndex
	 * @param comparator
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Override
	public int rngMaxIndex(final int startIndex, final int endIndex, final Comparator<E> comparator)
	{
		final int d; //bi-directional index movement
		if(startIndex <= endIndex){
			if(startIndex < 0 || endIndex >= this.size){
				throw new IndexOutOfBoundsException(this.exceptionStringRange(startIndex, endIndex));
			}
			d = 1; //incrementing direction
		}
		else {
			if(startIndex >= this.size || endIndex < 0){
				throw new IndexOutOfBoundsException(this.exceptionStringRange(endIndex, startIndex));
			}
			d = -1; //decrementing direction
		}
		if(endIndex == startIndex) return -1;
		
		E loopMaxElement = (E)this.data[startIndex];
		int loopMaxIndex = startIndex;
		final Object[] data = this.data;
		for(int i = startIndex; i != endIndex;){
			final E e = (E)data[i+=d];
			if(comparator.compare(loopMaxElement, e) < 0){
				loopMaxElement = e;
				loopMaxIndex = i;
			}
		}
		return loopMaxIndex;
	}

	/**
	 * @param comparator
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Override
	public int minIndex(final Comparator<E> comparator)
	{
		final Object[] data = this.data;
		final int size = this.size;

		if(size == 0) return -1;

		E loopMinElement = (E)data[0];
		int loopMinIndex = 0;
		E e;
		for(int i = 1; i < size; i++){
			e = (E)data[i];
			if(comparator.compare(loopMinElement, e) > 0){
				loopMinElement = e;
				loopMinIndex = i;
			}
		}
		return loopMinIndex;
	}

	/**
	 * @param startIndex
	 * @param endIndex
	 * @param comparator
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Override
	public int rngMinIndex(final int startIndex, final int endIndex, final Comparator<E> comparator)
	{
		final int d; //bi-directional index movement
		if(startIndex <= endIndex){
			if(startIndex < 0 || endIndex >= this.size){
				throw new IndexOutOfBoundsException(this.exceptionStringRange(startIndex, endIndex));
			}
			d = 1; //incrementing direction
		}
		else {
			if(startIndex >= this.size || endIndex < 0){
				throw new IndexOutOfBoundsException(this.exceptionStringRange(endIndex, startIndex));
			}
			d = -1; //decrementing direction
		}
		if(endIndex == startIndex) return -1;

		E loopMinElement = (E)this.data[startIndex];
		int loopMinIndex = startIndex;

		final Object[] data = this.data;
		for(int i = startIndex; i != endIndex;){
			final E e = (E)data[i+=d];
			if(comparator.compare(loopMinElement, e) > 0){
				loopMinElement = e;
				loopMinIndex = i;
			}
		}
		return loopMinIndex;
	}

	/**
	 * @param c
	 * @param ignoreNulls
	 * @return
	 */
	@Override
	public boolean containsAll(final Collection<E> c, final boolean ignoreNulls)
	{
		final Object[] data = this.data;
		final int size = this.size;
		boolean nullAreadyHandled = ignoreNulls;
		collectionLoop: for(final E o : c) {
			if(o == null){
				if(nullAreadyHandled) continue;
				nullAreadyHandled = true;
				for(int i = 0; i < size; i++){
					if(data[i] == null) continue collectionLoop;
				}
			}
			else {
				for(int i = 0; i < size; i++){
					if(o == data[i]) continue collectionLoop;
				}
			}
			return false;
		}
		//all elements of c have been found
		return true;
	}

	/**
	 * @param startIndex
	 * @param endIndex
	 * @param c
	 * @param ignoreNulls
	 * @return
	 */
	@Override
	public boolean rngContainsAll(final int startIndex, final int endIndex, final Collection<?> c, final boolean ignoreNulls)
	{
		final int d; //bi-directional index movement
		if(startIndex <= endIndex){
			if(startIndex < 0 || endIndex >= this.size){
				throw new IndexOutOfBoundsException(this.exceptionStringRange(startIndex, endIndex));
			}
			d = 1; //incrementing direction
		}
		else {
			if(startIndex >= this.size || endIndex < 0){
				throw new IndexOutOfBoundsException(this.exceptionStringRange(endIndex, startIndex));
			}
			d = -1; //decrementing direction
		}

		final Object[] data = this.data;
		boolean nullAreadyHandled = ignoreNulls;
		collectionLoop: for(final Object o : c) {
			if(o == null){
				if(nullAreadyHandled) continue;
				nullAreadyHandled = true;
				for(int i = startIndex-d; i != endIndex;){
					if(data[i+=d] == null) continue collectionLoop;
				}
			}
			else {
				for(int i = startIndex-d; i != endIndex;){
					if(o == data[i+=d]) continue collectionLoop;
				}
			}
			return false;
		}		
		return true; //all elements of c have been found
	}

	/**
	 * @param element
	 * @return
	 */
	@Override
	public int count(final E element)
	{
		final Object[] data = this.data;

		int count = 0;
		for(int i = 0, size = this.size; i < size; i++){
			if(data[i] == element){
				count++;
			}
		}
		return count;
	}

	/**
	 * @param startIndex
	 * @param endIndex
	 * @param element
	 * @return
	 */
	@Override
	public int rngCount(int startIndex, int endIndex, final E element)
	{
		if(startIndex > endIndex){
			final int t = startIndex;
			startIndex = endIndex;
			endIndex = t;
		}
		if(startIndex < 0 || endIndex >= this.size){
			this.exceptionStringRange(startIndex, endIndex);
		}

		int count = 0;
		final Object[] data = this.data;
		for(int i = startIndex; i <= endIndex; i++){
			if(element == data[i]){
				count++;
			}
		}
		return count;
	}

	/**
	 * @param o
	 * @return
	 */
	@Override
	public int indexOfId(final E o)
	{
		final Object[] data = this.data;
		for(int i = 0, size = this.size; i < size; i++){
			if(o == data[i]){
				return i;
			}
		}
		return -1;
	}

	/**
	 * @param o
	 * @return
	 */
	@Override
	public int lastIndexOfId(final E o)
	{
		return this.rngIndexOfId(this.size - 1, 0, o);
	}

	/**
	 * @param ignoreMultipleNullValues
	 * @return
	 */
	@Override
	public boolean hasUniqueValues(final boolean ignoreMultipleNullValues)
	{
		final Object[] data = this.data;
		final int size = this.size;

		if(!ignoreMultipleNullValues){
			boolean foundNull = false;
			for(int i = 0; i < size; i++){
				if(data[i] == null){
					if(foundNull) return false;
					foundNull = true;
				}
			}
		}

		for(int i = 0; i < size; i++){
			final Object e = data[i];
			if(e == null) continue;
			for(int j = i + 1; j < size; j++){
				if(e == data[j]) return false;
			}
		}
		return true;
	}

	/**
	 * @param startIndex
	 * @param endIndex
	 * @param ignoreMultipleNullValues
	 * @return
	 */
	@Override
	public boolean rngHasUniqueValues(final int startIndex, final int endIndex, final boolean ignoreMultipleNullValues)
	{
		final int d;
		if(startIndex <= endIndex){
			if(startIndex < 0 || endIndex >= this.size){
				throw new IndexOutOfBoundsException(this.exceptionStringRange(startIndex, endIndex));
			}
			d = 1;
		}
		else {
			if(startIndex >= this.size || endIndex < 0){
				throw new IndexOutOfBoundsException(this.exceptionStringRange(endIndex, startIndex));
			}
			d = -1;
		}
		
		final Object[] data = this.data;
		if(!ignoreMultipleNullValues){
			boolean foundNull = false;
			for(int i = startIndex-d; i != endIndex;){
				if(data[i+=d] == null){
					if(foundNull) return false;
					foundNull = true;
				}
			}
		}
		for(int i = startIndex-d; i != endIndex;){
			final Object e = data[i+=d];
			if(e == null) continue;
			for(int j = i; j != endIndex;){
				if(e == data[j+=d]) return false;
			}
		}
		return true;
	}

	/**
	 * @param c
	 * @param ignoreNulls
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Override
	public int removeAll(final XCollection<E> c, final boolean ignoreNulls, final Equalator<E> equalator)
	{
		final int removed = removeAllFromArray(
			c, this.data, 0, this.size, this.data, 0, this.size, ignoreNulls, (Equalator<Object>)equalator
		);
		this.size -= removed;
		return removed;
	}

	/**
	 * @param startIndex
	 * @param endIndex
	 * @param c
	 * @param ignoreNulls
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Override
	public int rngRemoveAll(
		int startIndex,
		int endIndex,
		final XCollection<? super E> c,
		final boolean ignoreNulls,
		final Equalator<E> equalator
	)
	{
		if(startIndex > endIndex){
			final int t = startIndex;
			startIndex = endIndex;
			endIndex = t;
		}
		endIndex++;
		final int removed = removeAllFromArray(
			c, this.data, startIndex, endIndex, this.data, startIndex, endIndex, ignoreNulls, (Equalator<Object>)equalator
		);
		this.size -= removed;
		return removed;
	}

	/**
	 * @param c
	 * @param ignoreNulls
	 * @return
	 */
	@Override
	public int removeAllId(final XCollection<E> c, final boolean ignoreNulls)
	{
		final int removed = removeAllFromArray(c, this.data, 0, this.size, this.data, 0, this.size, ignoreNulls);
		this.size -= removed;
		return removed;
	}

	/**
	 * @param startIndex
	 * @param endIndex
	 * @param c
	 * @param ignoreNulls
	 * @return
	 */
	@Override
	public int rngRemoveAll(int startIndex, int endIndex, final XCollection<? super E> c, final boolean ignoreNulls)
	{
		if(startIndex > endIndex){
			final int t = startIndex;
			startIndex = endIndex;
			endIndex = t;
		}
		endIndex++;
		final int removed = removeAllFromArray(
			c, this.data, startIndex, endIndex, this.data, startIndex, endIndex, ignoreNulls
		);
		this.size -= removed;
		return removed;
	}

	/**
	 * @param c
	 * @return
	 */
	@Override
	public int retainAllId(final XCollection<E> c)
	{
		final Object[] data = this.data;
		final int size = this.size;
		for(int i = 0; i < size; i++){
			if(JaArrays.idContains(c, data[i])){
				data[i] = REMOVE_MARKER;
			}
		}
		final int removedCount = JaArrays.removeAllFromArray(REMOVE_MARKER, this.data, 0, size, this.data, 0, size);
		this.size -= removedCount;
		return removedCount;
	}

	/**
	 * @param startIndex
	 * @param endIndex
	 * @param c
	 * @return
	 */
	@Override
	public int rngRetainAll(final int startIndex, final int endIndex, final XCollection<E> c)
	{
		final int d; //bi-directional index movement
		if(startIndex <= endIndex){
			if(startIndex < 0 || endIndex >= this.size){
				throw new IndexOutOfBoundsException(this.exceptionStringRange(startIndex, endIndex));
			}
			d = 1; //incrementing direction
		}
		else {
			if(startIndex >= this.size || endIndex < 0){
				throw new IndexOutOfBoundsException(this.exceptionStringRange(endIndex, startIndex));
			}
			d = -1; //decrementing direction
		}

		final int removedCount;
		final boolean removeNulls = !containsNull(c);		
		final Object[] data = this.data;
		try {
			for(int i = startIndex-d; i != endIndex;){
				final Object e = data[i+=d];
				if(e == null){
					if(removeNulls){
						data[i] = REMOVE_MARKER;
					}
					continue;
				}
				if(!JaArrays.idContains(c, e) ){
					data[i] = REMOVE_MARKER;
				}
			}
		}
		finally {
			removedCount = d == -1
			?removeAllFromArray(REMOVE_MARKER, this.data, endIndex, startIndex+1, this.data, endIndex, startIndex+1)
			:removeAllFromArray(REMOVE_MARKER, this.data, startIndex, endIndex+1, this.data, startIndex, endIndex+1)
			;
			this.size -= removedCount;
		}
		return removedCount;
	}

	/**
	 * @param list
	 * @param comparator
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Override
	public boolean equals(final XGettingCollection<E> list, final Equalator<E> comparator)
	{
		if(list == null || !(list instanceof GrowList)) return false;
		if(list == this) return true;
		final int listSize = list.size();

		// equivalent to equalsContent()
		if(listSize != this.size) return false;
		return JaArrays.equals(
			this.data, 0, ((GrowList<?>)list).data, 0, listSize, (Equalator<Object>)comparator
		);
	}

	/**
	 * @param list may not be null
	 * @param comparator
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Override
	public boolean equalsContent(final Collection<E> list, final Equalator<E> comparator)
	{
		if(list == this) return true;
		final int listSize = list.size();
		if(listSize != this.size) return false;

		if(list instanceof XGettingList){
			if(list instanceof GrowList){
				return JaArrays.equals(
					this.data, 0, ((GrowList<?>)list).data, 0, listSize, (Equalator<Object>)comparator
				);
			}
			else if(list instanceof ArrayAccessor){
				return JaArrays.equals(
					this.data, 0, ((ArrayAccessor<?>)list).getArray(), 0, listSize, (Equalator<Object>)comparator
				);
			}
			// else if ConstList then check with package-private array getter?
		}

		final Object[] data = this.data;
		int i = 0;
		for(final E e : list){
			if(comparator.equal(e, (E)data[i++])) return false;
		}
		return true;
	}

	/**
	 * @param startIndex
	 * @param list
	 * @param length
	 * @param comparator
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Override
	public boolean rngEqualsContent(
		final int startIndex,
		final List<E> list,
		final int length,
		final Equalator<E> comparator
	)
	{
		if(list == null || list.size() != length) return false;
		if(startIndex >= this.size){
			throw new IndexOutOfBoundsException(Integer.toString(startIndex));
		}

		final Object[] data = this.data;
		final int d = length < 0 ?-1 :1, bound = startIndex+length;
		int i = startIndex;
		for(final E e : list){
			if(comparator.equal(e, (E)data[i])) return false;
			if((i+=d) == bound) break;
		}
		return true;
	}

	/**
	 * Note that prepanding elements to an array list can cause massive overhead
	 * @param element
	 * @return
	 */
	@Override
	public GrowList<E> prepend(final E element)
	{
		if(this.size == 0){
			//as data length can never be 0, it doesn't have to be checked here
			this.data[this.size++] = element;
			return this;
		}

		Object[] data = this.data;
		if(this.size == data.length){
			//expandArray()
			final int length = this.data.length;
			data = new Object[length == MAXIMUM_POW2_SIZE ?Integer.MAX_VALUE : length<<1];
			System.arraycopy(this.data, 0, data, 1, length);
			this.data = data;
		}
		else {
			System.arraycopy(data, 0, data, 1, this.size);
		}
		data[0] = element;
		this.size++;
		return this;
	}

	/**
	 * @param vc
	 * @see com.xdev.jadoth.collections.XList#appendTo(com.xdev.jadoth.util.VarChar)
	 */
	@Override
	public void appendTo(final VarChar vc)
	{
		final Object[] data = this.data;
		Object e;
		for(int i = 0, size = this.size; i < size; i++){
			e = data[i];
			vc.append(e == this ?super.toString() :e).append(',');
		}
		vc.deleteLastChar();
	}

	/**
	 * @param vc
	 * @param seperator
	 * @see com.xdev.jadoth.collections.XList#appendTo(com.xdev.jadoth.util.VarChar, java.lang.String)
	 */
	@Override
	public void appendTo(final VarChar vc, final String seperator)
	{
		final char[] sepp = seperator.toCharArray();
		final Object[] data = this.data;
		Object e;
		for(int i = 0, size = this.size; i < size; i++){
			e = data[i];
			vc.append(e == this ?super.toString() :e).append(sepp);
		}
		vc.deleteLast(sepp.length);

	}

	/**
	 * @param vc
	 * @param seperator
	 * @see com.xdev.jadoth.collections.XList#appendTo(com.xdev.jadoth.util.VarChar, char)
	 */
	@Override
	public void appendTo(final VarChar vc, final char seperator)
	{
		final Object[] data = this.data;
		Object e;
		for(int i = 0, size = this.size; i < size; i++){
			e = data[i];
			vc.append(e == this ?super.toString() :e).append(seperator);
		}
		vc.deleteLastChar();
	}

	/**
	 * @param startIndex
	 * @param endIndex
	 * @param vc
	 * @see com.xdev.jadoth.collections.XList#rngAppendTo(int, int, com.xdev.jadoth.util.VarChar)
	 */
	@Override
	public void rngAppendTo(final int startIndex, final int endIndex, final VarChar vc)
	{
		final int d; //bi-directional index movement
		if(startIndex <= endIndex){
			if(startIndex < 0 || endIndex >= this.size){
				throw new IndexOutOfBoundsException(this.exceptionStringRange(startIndex, endIndex));
			}
			d = 1; //incrementing direction
		}
		else {
			if(startIndex >= this.size || endIndex < 0){
				throw new IndexOutOfBoundsException(this.exceptionStringRange(endIndex, startIndex));
			}
			d = -1; //decrementing direction
		}

		final Object[] data = this.data;
		Object loopObject;
		for(int i = startIndex-d; i != endIndex;){
			loopObject = data[i+=d];
			vc.append(loopObject == this ?super.toString() :loopObject).append(',');
		}
		vc.deleteLastChar();

	}

	/**
	 * @param startIndex
	 * @param endIndex
	 * @param vc
	 * @param seperator
	 * @see com.xdev.jadoth.collections.XList#rngAppendTo(int, int, com.xdev.jadoth.util.VarChar, java.lang.String)
	 */
	@Override
	public void rngAppendTo(final int startIndex, final int endIndex, final VarChar vc, final String seperator)
	{
		final int d; //bi-directional index movement
		if(startIndex <= endIndex){
			if(startIndex < 0 || endIndex >= this.size){
				throw new IndexOutOfBoundsException(this.exceptionStringRange(startIndex, endIndex));
			}
			d = 1; //incrementing direction
		}
		else {
			if(startIndex >= this.size || endIndex < 0){
				throw new IndexOutOfBoundsException(this.exceptionStringRange(endIndex, startIndex));
			}
			d = -1; //decrementing direction
		}

		final Object[] data = this.data;
		final char[] sepp = seperator.toCharArray();
		Object loopObject;
		for(int i = startIndex-d; i != endIndex;){
			loopObject = data[i+=d];
			vc.append(loopObject == this ?super.toString() :loopObject).append(sepp);
		}
		vc.deleteLast(sepp.length);

	}

	/**
	 * @param startIndex
	 * @param endIndex
	 * @param vc
	 * @param seperator
	 * @see com.xdev.jadoth.collections.XList#rngAppendTo(int, int, com.xdev.jadoth.util.VarChar, char)
	 */
	@Override
	public void rngAppendTo(final int startIndex, final int endIndex, final VarChar vc, final char seperator)
	{
		final int d; //bi-directional index movement
		if(startIndex <= endIndex){
			if(startIndex < 0 || endIndex >= this.size){
				throw new IndexOutOfBoundsException(this.exceptionStringRange(startIndex, endIndex));
			}
			d = 1; //incrementing direction
		}
		else {
			if(startIndex >= this.size || endIndex < 0){
				throw new IndexOutOfBoundsException(this.exceptionStringRange(endIndex, startIndex));
			}
			d = -1; //decrementing direction
		}

		final Object[] data = this.data;
		Object loopObject;
		for(int i = startIndex-d; i != endIndex;){
			loopObject = data[i+=d];
			vc.append(loopObject == this ?super.toString() :loopObject).append(seperator);
		}
		vc.deleteLastChar();
	}

	/**
	 * @param c
	 * @param comparator
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Override
	public <C extends Collecting<E>> C intersect(
		final XGettingCollection<E> c, final Equalator<E> comparator, final C target
	)
	{
		final Object[] data = this.data;
		E e;
		for(int i = 0, size = this.size; i < size; i++){
			if(c.contains(e = (E)data[i], comparator)){
				target.add(e);
			}
		}
		return target;
	}

	/**
	 * @param startIndex
	 * @param endIndex
	 * @param c
	 * @param comparator
	 * @return
	 * @see com.xdev.jadoth.collections.XList#intersect(XCollection, Comparator)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public <C extends Collecting<E>> C rngIntersect(
		final int startIndex,
		final int endIndex,
		final XGettingCollection<E> c,
		final Equalator<E> comparator,
		final C target
	)
	{
		final int d; //bi-directional index movement
		if(startIndex <= endIndex){
			if(startIndex < 0 || endIndex >= this.size){
				throw new IndexOutOfBoundsException(this.exceptionStringRange(startIndex, endIndex));
			}
			d = 1; //incrementing direction
		}
		else {
			if(startIndex >= this.size || endIndex < 0){
				throw new IndexOutOfBoundsException(this.exceptionStringRange(endIndex, startIndex));
			}
			d = -1; //decrementing direction
		}

		final Object[] data = this.data;
		for(int i = startIndex-d; i != endIndex;){
			final E e = (E)data[i+=d];
			if(c.contains(e, comparator)){
				target.add(e);
			}
		}
		return target;
	}

	/**
	 * @param c
	 * @param comparator
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Override
	public <C extends Collecting<E>> C except(
		final XGettingCollection<E> c,
		final Equalator<E> comparator,
		final C target
	)
	{
		final Object[] data = this.data;
		E e;
		for(int i = 0, size = this.size; i < size; i++){
			if(c.contains(e = (E)data[i], comparator)) continue;
			target.add(e);
		}
		return target;
	}

	/**
	 * @param startIndex
	 * @param endIndex
	 * @param c
	 * @param comparator
	 * @return
	 * @see com.xdev.jadoth.collections.XList#rngExcept(int, int, XCollection, java.util.Comparator)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public <C extends Collecting<E>> C rngExcept(
		final int startIndex,
		final int endIndex,
		final XGettingCollection<E> c,
		final Equalator<E> comparator,
		final C target
	)
	{
		final int d; //bi-directional index movement
		if(startIndex <= endIndex){
			if(startIndex < 0 || endIndex >= this.size){
				throw new IndexOutOfBoundsException(this.exceptionStringRange(startIndex, endIndex));
			}
			d = 1; //incrementing direction
		}
		else {
			if(startIndex >= this.size || endIndex < 0){
				throw new IndexOutOfBoundsException(this.exceptionStringRange(endIndex, startIndex));
			}
			d = -1; //decrementing direction
		}

		final Object[] data = this.data;
		for(int i = startIndex-d; i != endIndex;){
			final E e = (E)data[i+=d];
			if(c.contains(e, comparator)) continue;
			target.add(e);
		}
		return target;
	}

	/**
	 * @param c
	 * @param comparator
	 * @return
	 * @see com.xdev.jadoth.collections.XList#except(java.util.Collection, java.util.Comparator)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public <C extends Collecting<E>> C union(
		final XGettingCollection<E> c,
		final Equalator<E> comparator,
		final C target
	)
	{
		final Object[] data = this.data;
		for(int i = 0, size = this.size; i < size; i++){
			final E e = (E)data[i];
			if(!c.contains(e, comparator)){
				target.add(e);
			}
		}
		return target;
	}

	/**
	 * @param startIndex
	 * @param endIndex
	 * @param c
	 * @param comparator
	 * @return
	 * @see com.xdev.jadoth.collections.XList#rngExcept(int, int, XCollection, java.util.Comparator)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public <C extends Collecting<E>> C rngUnion(
		final int startIndex,
		final int endIndex,
		final XGettingCollection<E> c,
		final Equalator<E> comparator,
		final C target
	)
	{
		final int d; //bi-directional index movement
		if(startIndex <= endIndex){
			if(startIndex < 0 || endIndex >= this.size){
				throw new IndexOutOfBoundsException(this.exceptionStringRange(startIndex, endIndex));
			}
			d = 1; //incrementing direction
		}
		else {
			if(startIndex >= this.size || endIndex < 0){
				throw new IndexOutOfBoundsException(this.exceptionStringRange(endIndex, startIndex));
			}
			d = -1; //decrementing direction
		}

		final Object[] data = this.data;
		for(int i = startIndex-d; i != endIndex;){
			final E e = (E)data[i+=d];
			if(!c.contains(e, comparator)){
				target.add(e);
			}
		}
		return target;
	}

	/**
	 * @param element
	 * @param equalator
	 * @return
	 * @see com.xdev.jadoth.collections.XCollection#contains(java.lang.Object, Equalator)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public boolean contains(final E element, final Equalator<E> equalator)
	{
		final Object[] data = this.data;
		for(int i = 0, size = this.size; i < size; i++){
			if(equalator.equal(element, (E)data[i])) return true;
		}
		return false;

	}

	/**
	 * @param startIndex
	 * @param endIndex
	 * @param element
	 * @param comparator
	 * @return
	 * @see com.xdev.jadoth.collections.XList#rngContains(int, int, java.lang.Object, java.util.Comparator)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public boolean rngContains(
		final int startIndex, final int endIndex, final E element, final Equalator<E> comparator
	)
	{
		final int d; //bi-directional index movement
		if(startIndex <= endIndex){
			if(startIndex < 0 || endIndex >= this.size){
				throw new IndexOutOfBoundsException(this.exceptionStringRange(startIndex, endIndex));
			}
			d = 1; //incrementing direction
		}
		else {
			if(startIndex >= this.size || endIndex < 0){
				throw new IndexOutOfBoundsException(this.exceptionStringRange(endIndex, startIndex));
			}
			d = -1; //decrementing direction
		}

		final Object[] data = this.data;
		for(int i = startIndex-d; i != endIndex;){
			if(comparator.equal(element, (E)data[i+=d])) return true;
		}
		return false;
	}

	/**
	 * @param startIndex
	 * @param endIndex
	 * @param oldElement
	 * @param newElement
	 * @return
	 * @see com.xdev.jadoth.collections.XList#eqRngReplace(int, int, java.lang.Object, java.lang.Object, int)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public int rngReplace(
		final int startIndex,
		final int endIndex,
		final E oldElement,
		final E newElement,
		int skip,
		final Integer limit,
		final Equalator<E> equalator
	)
	{
		final int d; //bi-directional index movement
		if(startIndex <= endIndex){
			if(startIndex < 0 || endIndex >= this.size){
				throw new IndexOutOfBoundsException(this.exceptionStringRange(startIndex, endIndex));
			}
			d = 1; //incrementing direction
		}
		else {
			if(startIndex >= this.size || endIndex < 0){
				throw new IndexOutOfBoundsException(this.exceptionStringRange(endIndex, startIndex));
			}
			d = -1; //decrementing direction
		}
		int lim = limit == null ?Integer.MAX_VALUE :limit.intValue();


		final Object[] data = this.data;
		int i = startIndex-d;
		while(skip != 0){
			if(equalator.equal(oldElement, (E)data[i+=d])){
				skip--;
			}
			if(i == endIndex){
				return 0;
			}
		}

		int replaceCount = 0;
		while(i != endIndex && lim > 0){
			if(equalator.equal(oldElement, (E)data[i+=d])){
				data[i] = newElement;
				replaceCount++;
				lim--;
			}
		}
		return replaceCount;
	}

	/**
	 * @param oldElement
	 * @param newElement
	 * @return
	 * @see com.xdev.jadoth.collections.XCollection#eqReplace(java.lang.Object, java.lang.Object, int)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public int replace(final E oldElement, final E newElement, final int skip, Integer limit, final Equalator<E> equalator)
	{
		int replaceCount = 0;
		final Object[] data = this.data;		
		for(int i = 0, size = this.size; i < size && limit > 0; i++){
			if(equalator.equal(oldElement, (E)data[i])){
				data[i] = newElement;
				replaceCount++;
				limit--;
			}
		}
		return replaceCount;
	}

	/**
	 * @param startIndex
	 * @param endIndex
	 * @param oldElement
	 * @param newElement
	 * @return
	 * @see com.xdev.jadoth.collections.XList#rngReplace(int, int, java.lang.Object, java.lang.Object, int)
	 */
	@Override
	public int rngReplace(
		final int startIndex, final int endIndex, final E oldElement, final E newElement, final int skip, Integer limit
	)
	{
		final int d; //bi-directional index movement
		if(startIndex <= endIndex){
			if(startIndex < 0 || endIndex >= this.size){
				throw new IndexOutOfBoundsException(this.exceptionStringRange(startIndex, endIndex));
			}
			d = 1; //incrementing direction
		}
		else {
			if(startIndex >= this.size || endIndex < 0){
				throw new IndexOutOfBoundsException(this.exceptionStringRange(endIndex, startIndex));
			}
			d = -1; //decrementing direction
		}

		int replaceCount = 0;
		final Object[] data = this.data;		
		for(int i = startIndex-d; i != endIndex && limit > 0;){
			if(oldElement == data[i+=d]){
				data[i] = newElement;
				replaceCount++;
				limit--;
			}
		}
		return replaceCount;
	}

	/**
	 * @param oldElement
	 * @param newElement
	 * @return
	 * @see com.xdev.jadoth.collections.XCollection#replace(java.lang.Object, java.lang.Object, int)
	 */
	@Override
	public int replace(final E oldElement, final E newElement, final int skip, Integer limit)
	{
		int replaceCount = 0;
		final Object[] data = this.data;
		for(int i = 0, size = this.size; i < size && limit > 0; i++){
			if(oldElement == data[i]){
				data[i] = newElement;
				replaceCount++;
				limit--;
			}
		}
		return replaceCount;
	}

	/**
	 * @param startIndex
	 * @param endIndex
	 * @param replacementMapping
	 * @return
	 * @see com.xdev.jadoth.collections.XList#rngReplaceAll(int, int, java.util.Map)
	 */
	@Override
	public int rngReplaceAll(final int startIndex, final int endIndex, final Map<E, E> replacementMapping)
	{
		final int d; //bi-directional index movement
		if(startIndex <= endIndex){
			if(startIndex < 0 || endIndex >= this.size){
				throw new IndexOutOfBoundsException(this.exceptionStringRange(startIndex, endIndex));
			}
			d = 1; //incrementing direction
		}
		else {
			if(startIndex >= this.size || endIndex < 0){
				throw new IndexOutOfBoundsException(this.exceptionStringRange(endIndex, startIndex));
			}
			d = -1; //decrementing direction
		}

		int replaceCount = 0;
		final Object[] data = this.data;
		for(int i = startIndex-d; i != endIndex;){
			final E replacement = replacementMapping.get(data[i+=d]);
			if(replacement != null){
				data[i] = replacement;
				replaceCount++;
			}
		}
		return replaceCount;
	}

	/**
	 * @param replacementMapping
	 * @return
	 * @see com.xdev.jadoth.collections.XCollection#replaceAll(java.util.Map)
	 */
	@Override
	public int replaceAll(final Map<E, E> replacementMapping)
	{
		final Object[] data = this.data;
		E replacement;
		int replaceCount = 0;
		for(int i = 0, size = this.size; i < size; i++){
			if((replacement = replacementMapping.get(data[i])) != null){
				data[i] = replacement;
				replaceCount++;
			}
		}
		return replaceCount;
	}

	/**
	 * @param startIndex
	 * @param endIndex
	 * @param oldElement
	 * @param newElement
	 * @return
	 * @see com.xdev.jadoth.collections.XList#eqRngReplaceFirst(int, int, java.lang.Object, java.lang.Object)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public int rngReplaceOne(
		final int startIndex,
		final int endIndex,
		final E oldElement,
		final E newElement,
		final Equalator<E> equalator
	)
	{
		final int d; //bi-directional index movement
		if(startIndex <= endIndex){
			if(startIndex < 0 || endIndex >= this.size){
				throw new IndexOutOfBoundsException(this.exceptionStringRange(startIndex, endIndex));
			}
			d = 1; //incrementing direction
		}
		else {
			if(startIndex >= this.size || endIndex < 0){
				throw new IndexOutOfBoundsException(this.exceptionStringRange(endIndex, startIndex));
			}
			d = -1; //decrementing direction
		}

		int replaceCount = 0;
		final Object[] data = this.data;
		for(int i = startIndex-d; i != endIndex;){
			if(equalator.equal(oldElement, (E)data[i+=d])){
				data[i] = newElement;
				replaceCount++;
			}
		}
		return replaceCount;
	}

	/**
	 * @param startIndex
	 * @param endIndex
	 * @param oldElement
	 * @param newElement
	 * @return
	 * @see com.xdev.jadoth.collections.XList#rngReplaceOne(int, int, java.lang.Object, java.lang.Object)
	 */
	@Override
	public int rngReplaceOne(final int startIndex, final int endIndex, final E oldElement, final E newElement)
	{
		final int d; //bi-directional index movement
		if(startIndex <= endIndex){
			if(startIndex < 0 || endIndex >= this.size){
				throw new IndexOutOfBoundsException(this.exceptionStringRange(startIndex, endIndex));
			}
			d = 1; //incrementing direction
		}
		else {
			if(startIndex >= this.size || endIndex < 0){
				throw new IndexOutOfBoundsException(this.exceptionStringRange(endIndex, startIndex));
			}
			d = -1; //decrementing direction
		}

		int replaceCount = 0;
		final Object[] data = this.data;
		for(int i = startIndex-d; i != endIndex;){
			if(oldElement == data[i+=d]){
				data[i] = newElement;
				replaceCount++;
			}
		}
		return replaceCount;
	}

	/**
	 * @param oldElement
	 * @param newElement
	 * @return
	 * @see com.xdev.jadoth.collections.XCollection#eqReplaceFirst(java.lang.Object, java.lang.Object)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public int replaceOne(final E oldElement, final E newElement, final Equalator<E> equalator)
	{
		if(oldElement == null){
			return this.replaceOne((E)null, newElement);
		}

		final Object[] data = this.data;
		int replaceCount = 0;
		for(int i = 0, size = this.size; i < size; i++){
			if(equalator.equal(oldElement, (E)data[i])){
				data[i] = newElement;
				replaceCount++;
			}
		}
		return replaceCount;
	}

	/**
	 * @param oldElement
	 * @param newElement
	 * @return
	 * @see com.xdev.jadoth.collections.XCollection#replaceOne(java.lang.Object, java.lang.Object)
	 */
	@Override
	public int replaceOne(final E oldElement, final E newElement)
	{
		final Object[] data = this.data;
		int replaceCount = 0;
		for(int i = 0, size = this.size; i < size; i++){
			if(oldElement == data[i]){
				data[i] = newElement;
				replaceCount++;
			}
		}
		return replaceCount;
	}

	/**
	 * @param startIndex
	 * @param endIndex
	 * @param element
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Override
	public E rngSearch(final int startIndex, final int endIndex, final E element, final Equalator<E> equalator)
	{
		final int d; //bi-directional index movement
		if(startIndex <= endIndex){
			if(startIndex < 0 || endIndex >= this.size){
				throw new IndexOutOfBoundsException(this.exceptionStringRange(startIndex, endIndex));
			}
			d = 1; //incrementing direction
		}
		else {
			if(startIndex >= this.size || endIndex < 0){
				throw new IndexOutOfBoundsException(this.exceptionStringRange(endIndex, startIndex));
			}
			d = -1; //decrementing direction
		}

		final Object[] data = this.data;
		for(int i = startIndex-d; i != endIndex;){
			if(equalator.equal(element, (E)data[i+=d])) return (E)data[i];
		}
		return null;
	}

	/**
	 * @param element
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Override
	public E search(final E element, final Equalator<E> equalator)
	{
		if(element == null){
			throw new NullPointerException();
		}
		final Object[] data = this.data;
		for(int i = 0, size = this.size; i < size; i++){
			if(equalator.equal(element, (E)data[i])) return (E)data[i];
		}
		return null;
	}

	/**
	 * Note that passing a higher start index than end index will consequently create a list in reversed order.
	 * For reversing the whole list conveniently, see {@link #toReversed()}
	 *
	 * @param startIndex
	 * @param endIndex
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Override
	public <L extends Collecting<E>> L copyRange(final int startIndex, final int endIndex, final L targetCollection)
	{
		final int d;
		if(startIndex <= endIndex){
			if(startIndex < 0 || endIndex >= this.size){
				throw new IndexOutOfBoundsException(this.exceptionStringRange(startIndex, endIndex));
			}
			d = 1; //incrementing direction
		}
		else {
			if(startIndex >= this.size || endIndex < 0){
				throw new IndexOutOfBoundsException(this.exceptionStringRange(endIndex, startIndex));
			}
			d = -1;
		}

		final Object[] data = this.data;
		for(int i = startIndex-d; i != endIndex;){
			targetCollection.add((E)data[i+=d]);
		}
		return targetCollection;
	}

	/**
	 * @return
	 */
	@Override
	public GrowList<E> toReversed()
	{
		final GrowList<E> reversed = new GrowList<E>(this.size);
		final Object[] rData = reversed.data;
		final Object[] data = this.data;
		for(int i = this.size, r = 0; i --> 0;){
			rData[r++] = data[i];
		}
		reversed.size = this.size;
		return reversed;
	}

	/**
	 * @return
	 */
	@Override
	public GrowList<E> copy()
	{
		return new GrowList<E>(this);
	}

	/**
	 * @return
	 */
	@Override
	public GrowList<E> reverse()
	{
		final Object[] data = this.data;
		final int lastIndex = this.size - 1;
		final int halfSize = this.size>>1;

		for(int i = 0; i < halfSize; i++){
			final Object e = data[i];
			data[i] = data[lastIndex-i];
			data[lastIndex-i] = e;
		}
		return this;
	}

	/**
	 * @param startIndex
	 * @param endIndex
	 * @return
	 */
	@Override
	public GrowList<E> rngReverse(int startIndex, int endIndex)
	{
		if(startIndex > endIndex){
			final int t = startIndex;
			startIndex = endIndex;
			endIndex = t;
		}

		final Object[] data = this.data;
		while(startIndex < endIndex){
			final Object e = data[startIndex];
			data[startIndex] = data[endIndex];
			data[endIndex] = e;
			startIndex++;
			endIndex--;
		}
		return this;
	}

	/**
	 * @param startIndex
	 * @param endIndex
	 * @param predicate
	 * @param limit
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Override
	public int rngReduce(final int startIndex, final int endIndex, final Predicate<E> predicate, final int skip, Integer limit)
	{
		if(limit <= 0){
			return 0; // spare unnecessary traversal of the whole list in array util method
		}		

		final int d; //bi-directional index movement
		if(startIndex <= endIndex){
			if(startIndex < 0 || endIndex >= this.size){
				throw new IndexOutOfBoundsException(this.exceptionStringRange(startIndex, endIndex));
			}
			d = 1; //incrementing direction
		}
		else {
			if(startIndex >= this.size || endIndex < 0){
				throw new IndexOutOfBoundsException(this.exceptionStringRange(endIndex, startIndex));
			}
			d = -1; //decrementing direction
		}

		final int removedCount;
		final Object[] data = this.data;
		try {
			for(int i = startIndex-d; i != endIndex && limit > 0;) {
				if(predicate.apply((E)data[i+=d])) {
					data[i] = REMOVE_MARKER;
					limit--;
				}
			}
		}
		finally {
			removedCount = d == -1
			?removeAllFromArray(REMOVE_MARKER, this.data, endIndex, startIndex+1, this.data, endIndex, startIndex+1)
			:removeAllFromArray(REMOVE_MARKER, this.data, startIndex, endIndex+1, this.data, startIndex, endIndex+1)
			;
			this.size -= removedCount;
		}
		return removedCount;
	}

	/**
	 * @param startIndex
	 * @param endIndex
	 * @param predicate
	 * @param limit
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Override
	public int rngReduce(final int startIndex, final int endIndex, final TPredicate<E> predicate, final int skip, Integer limit)
	{
		if(limit <= 0){
			return 0; // spare unnecessary traversal of the whole list in array util method
		}
		
		final int d; //bi-directional index movement
		if(startIndex <= endIndex){
			if(startIndex < 0 || endIndex >= this.size){
				throw new IndexOutOfBoundsException(this.exceptionStringRange(startIndex, endIndex));
			}
			d = 1; //incrementing direction
		}
		else {
			if(startIndex >= this.size || endIndex < 0){
				throw new IndexOutOfBoundsException(this.exceptionStringRange(endIndex, startIndex));
			}
			d = -1; //decrementing direction
		}
		
		final int removedCount;
		final Object[] data = this.data;
		try {
			//this construction yields the same performance as the loop without inner try, but can still continue the loop
			int i = startIndex-d; //start i one before startIndex as (i+=d) only works as prefix ~crement
			while(i != endIndex && limit > 0){ //while(true) astonishingly is significantly slower than duplicated inner condition
				try {
					while(i != endIndex && limit > 0){
						if(predicate.apply((E)data[i+=d])){
							data[i] = REMOVE_MARKER;
							limit--;
						}
					}
					break;
				}
				catch(final ThrowBreak b)   { break; }
				catch(final ThrowContinue c){ /*Nothing*/ }
				catch(final ThrowReturn r)  { break; } //must still execute finally code and return removedCount
			}
		}
		finally {
			//can't abort/return until remove markers are cleared, so do this in any case
			removedCount = JaArrays.removeAllFromArray(
				REMOVE_MARKER, data, startIndex, endIndex+1, data, startIndex, endIndex+1
			);
			this.size -= removedCount;
		}
		return removedCount;
	}

	/**
	 * @param startIndex
	 * @param endIndex
	 * @param predicate
	 * @param limit
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Override
	public <L extends Collecting<E>> L rngMoveTo(
		final int startIndex, final int endIndex, final L target, final Predicate<E> predicate, final int skip, Integer limit
	)
	{
		final int d; //bi-directional index movement
		if(startIndex <= endIndex){
			if(startIndex < 0 || endIndex >= this.size){
				throw new IndexOutOfBoundsException(this.exceptionStringRange(startIndex, endIndex));
			}
			d = 1; //incrementing direction
		}
		else {
			if(startIndex >= this.size || endIndex < 0){
				throw new IndexOutOfBoundsException(this.exceptionStringRange(endIndex, startIndex));
			}
			d = -1; //decrementing direction
		}

		final Object[] data = this.data;
		for(int i = startIndex; i != endIndex && limit > 0;){
			final E e = (E)data[i+=d];
			if(predicate.apply(e)){
				target.add(e);
				limit--;
			}
		}
		return target;
	}

	/**
	 * @param startIndex
	 * @param endIndex
	 * @param predicate
	 * @param limit
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Override
	public <L extends Collecting<E>> L rngMoveTo(
		final int startIndex, final int endIndex, final L target, final TPredicate<E> predicate, final int skip, Integer limit
	)
	{
		final int d; //bi-directional index movement
		if(startIndex <= endIndex){
			if(startIndex < 0 || endIndex >= this.size){
				throw new IndexOutOfBoundsException(this.exceptionStringRange(startIndex, endIndex));
			}
			d = 1; //incrementing direction
		}
		else {
			if(startIndex >= this.size || endIndex < 0){
				throw new IndexOutOfBoundsException(this.exceptionStringRange(endIndex, startIndex));
			}
			d = -1; //decrementing direction
		}

		//this construction yields the same performance as the loop without inner try, but can still continue the loop
		final Object[] data = this.data;
		int i = startIndex-d; //start i one before startIndex as (i+=d) only works as prefix ~crement
		while(i != endIndex && limit > 0){ //while(true) astonishingly is significantly slower than duplicated inner condition
			try {
				while(i != endIndex && limit > 0){
					final E e = (E)data[i+=d];
					if(predicate.apply(e)){
						target.add(e);
						limit--;
					}
				}
				break;
			}
			catch(final ThrowBreak b)   { break; }
			catch(final ThrowContinue c){ /*Nothing*/ }
			catch(final ThrowReturn r)  { return target; }
		}
		return target;
	}

	/**
	 * @param <L>
	 * @param target
	 * @param predicate
	 * @return
	 * @see com.xdev.jadoth.collections.XRemovingCollection#moveTo(com.xdev.jadoth.collections.XGettingCollection, com.xdev.jadoth.lang.functional.Predicate)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public <L extends Collecting<E>> L moveTo(final L target, final Predicate<E> predicate)
	{
		final Object[] data = this.data;
		final int size = this.size;

		E e;
		try {
			for(int i = 0; i < size; i++){
				if(predicate.apply(e = (E)data[i])){
					target.add(e);
					data[i] = REMOVE_MARKER;
				}
			}
		}
		finally{
			//can't return until remove markers are cleared, so do this in any case
			this.size -= JaArrays.removeAllFromArray(REMOVE_MARKER, data, 0, size, data, 0, size);
		}
		return target;
	}

	/**
	 * @param <L>
	 * @param target
	 * @param predicate
	 * @return
	 * @see com.xdev.jadoth.collections.XRemovingCollection#moveTo(com.xdev.jadoth.collections.XGettingCollection, com.xdev.jadoth.lang.functional.controlflow.TPredicate)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public <L extends Collecting<E>> L moveTo(final L target, final TPredicate<E> predicate)
	{
		final Object[] data = this.data;
		final int size = this.size;

		E e;
		int i = -1;
		try {
			while(i < size){ //while(true) astonishingly is significantly slower than duplicated inner condition
				try {
					while(i < size){
						if(predicate.apply(e = (E)data[++i])){
							target.add(e);
							data[i] = REMOVE_MARKER;
						}
					}
					break;
				}
				catch(final ThrowBreak b)   { break; }
				catch(final ThrowReturn r)  { return target; }
				catch(final ThrowContinue c){ /*Nothing*/ }
			}
		}
		finally{
			//can't return until remove markers are cleared, so do this in any case
			this.size -= JaArrays.removeAllFromArray(REMOVE_MARKER, data, 0, size, data, 0, size);
		}
		return target;
	}

	/**
	 * @param <L>
	 * @param target
	 * @param predicate
	 * @param limit
	 * @return
	 * @see com.xdev.jadoth.collections.XRemovingCollection#moveTo(com.xdev.jadoth.collections.XGettingCollection, com.xdev.jadoth.lang.functional.Predicate, int)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public <L extends Collecting<E>> L moveTo(final L target, final Predicate<E> predicate, final int skip, Integer limit)
	{
		final int size = this.size;
		if(size == 0 || limit <= 0){
			return target; // spare unnecessary traversal of the whole list in array util method
		}

		final Object[] data = this.data;

		E e;
		try {
			for(int i = 0; i < size && limit > 0; i++){
				if(predicate.apply(e = (E)data[i])){
					target.add(e);
					data[i] = REMOVE_MARKER;
					limit--;
				}
			}
		}
		finally{
			//can't return until remove markers are cleared, so do this in any case
			this.size -= JaArrays.removeAllFromArray(REMOVE_MARKER, data, 0, size, data, 0, size);
		}
		return target;
	}

	/**
	 * @param <L>
	 * @param target
	 * @param predicate
	 * @param limit
	 * @return
	 * @see com.xdev.jadoth.collections.XRemovingCollection#moveTo(com.xdev.jadoth.collections.XGettingCollection, com.xdev.jadoth.lang.functional.controlflow.TPredicate, int)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public <L extends Collecting<E>> L moveTo(final L target, final TPredicate<E> predicate, final int skip, Integer limit)
	{
		final int size = this.size;
		if(size == 0 || limit <= 0){
			return target; // spare unnecessary traversal of the whole list in array util method
		}

		final Object[] data = this.data;
		E e;
		int i = -1;
		try {
			while(i < size && limit > 0){ //while(true) astonishingly is significantly slower than duplicated inner condition
				try {
					while(i  < size && limit > 0){
						if(predicate.apply(e = (E)data[++i])){
							target.add(e);
							data[i] = REMOVE_MARKER;
							limit--;
						}
					}
					break;
				}
				catch(final ThrowBreak b)   { break; }
				catch(final ThrowReturn r)  { return target; }
				catch(final ThrowContinue c){ /*Nothing*/ }
			}
		}
		finally{
			//can't return until remove markers are cleared, so do this in any case
			this.size -= JaArrays.removeAllFromArray(REMOVE_MARKER, data, 0, size, data, 0, size);
		}
		return target;
	}

	/**
	 * @param predicate
	 * @param limit
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Override
	public int reduce(final Predicate<E> predicate, final int skip, Integer limit)
	{
		final int size = this.size;
		if(size == 0 || limit <= 0){
			return 0; // spare unnecessary traversal of the whole list in array util method
		}

		final int removedCount;
		final Object[] data = this.data;
		try {
			for(int i = 0; i < size && limit > 0; i++){
				if(predicate.apply((E)data[i])){
					data[i] = REMOVE_MARKER;
					limit--;
				}
			}
		}
		finally {
			//even if predicate throws an execption, the remove markers have to be cleared
			removedCount = JaArrays.removeAllFromArray(REMOVE_MARKER, data, 0, size, data, 0, size);
			this.size -= removedCount;
		}
		return removedCount;
	}

	/**
	 * @param predicate
	 * @param limit
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Override
	public int reduce(final TPredicate<E> predicate, final int skip, Integer limit)
	{
		final int size = this.size;
		if(size == 0 || limit <= 0){
			return 0; // spare unnecessary traversal of the whole list in array util method
		}

		final int removedCount;

		//this construction yields the same performance as a loop without inner try, but can still continue the loop
		final Object[] data = this.data;
		int i = -1;
		try {
			while(i < size && limit > 0){
				try {
					while(i < size && limit > 0){
						if(predicate.apply((E)data[++i])){
							data[i] = REMOVE_MARKER;
							limit--;
						}
					}
					break;
				}
				catch(final ThrowBreak b)   { break; }
				catch(final ThrowReturn r)  { break; }
				catch(final ThrowContinue c){ /*Nothing*/ }
			}
		}
		finally {
			//can't return until remove markers are cleared, so do this in any case
			removedCount = removeAllFromArray(REMOVE_MARKER, data, 0, size, data, 0, size);
			this.size -= removedCount;
		}
		return removedCount;
	}

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
	@Override
	public GrowList<E> add(
		final E[] elements,
		final int srcIndex,
		final int srcLength,
		final Predicate<E> predicate,
		int skip,
		final Integer limit
	)
	{
		// check limit escape condition intentionally before index bounds check
		int lim;
		if(limit == null){
			lim = Integer.MAX_VALUE;
		}
		else {
			if((lim = limit.intValue()) <= 0) return this;
		}

		// determine traversal direction and check array bounds
		if(srcIndex < 0 || srcIndex >= elements.length){
			throw new ArrayIndexOutOfBoundsException(srcIndex);
		}
		if(skip < 0){
			throw new IllegalArgumentException(this.exceptionStringSkipNegative(skip));
		}

		final int d;
		if(srcLength == 0) return this;
		else if(srcLength > 0){
			if(srcIndex + srcLength >= elements.length){
				throw new ArrayIndexOutOfBoundsException(srcIndex + srcLength);
			}
			d = 1;
		}
		else {
			if(srcIndex + srcLength < 0){
				throw new ArrayIndexOutOfBoundsException(srcIndex + srcLength);
			}
			d = -1;
		}

		// local helper variables
		E element;
		int i = srcIndex;
		final int bound = srcIndex+srcLength;

		// prepend skipping to spare the check inside the main loop
		while(i != bound && skip != 0){
			if(predicate.apply(element = elements[i])){
				skip--;
			}
			i+=d;
		}

		// main loop with limit
		while(i != bound && lim != 0){
			if(predicate.apply(element = elements[i])){
				this.add(element);
				lim--;
			}
			i+=d;
		}

		return this;
	}

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
	@Override
	public GrowList<E> add(
		final E[] elements,
		final int srcIndex,
		final int srcLength,
		final TPredicate<E> predicate,
		int skip,
		final Integer limit
	)
	{
		// check limit escape condition intentionally before index bounds check
		int lim;
		if(limit == null){
			lim = Integer.MAX_VALUE;
		}
		else {
			if((lim = limit.intValue()) <= 0) return this;
		}

		// determine traversal direction and check array bounds
		if(srcIndex < 0 || srcIndex >= elements.length){
			throw new ArrayIndexOutOfBoundsException(srcIndex);
		}
		if(skip < 0){
			throw new IllegalArgumentException(this.exceptionStringSkipNegative(skip));
		}

		final int d;
		if(srcLength == 0) return this;
		else if(srcLength > 0){
			if(srcIndex + srcLength >= elements.length){
				throw new ArrayIndexOutOfBoundsException(srcIndex + srcLength);
			}
			d = 1;
		}
		else {
			if(srcIndex + srcLength < 0){
				throw new ArrayIndexOutOfBoundsException(srcIndex + srcLength);
			}
			d = -1;
		}

		// local helper variables
		E element;
		int i = srcIndex-d;
		final int bound = srcIndex + srcLength;

		// prepend skipping ato spare the check inside the main loop
		while(skip != 0){
			try {
				while(skip != 0){
					if((i+=d) == bound) return this;
					if(predicate.apply(element = elements[i])){
						skip--;
					}
				}
				break;
			}
			catch(final ThrowBreak e)    { return this; /*extracted skip-loop is an implementation detail, so break means return */}
			catch(final ThrowContinue e) { /*nothing*/ }
			catch(final ThrowReturn e)   { return this; }
		}

		// main loop with limit
		while(i != bound && lim != 0){
			try {
				while(i != bound && lim != 0){
					if(predicate.apply(element = elements[i+=d])){
						this.internalAdd(element);
						lim--;
					}
				}
				break;
			}
			catch(final ThrowBreak e)    { return this; }
			catch(final ThrowContinue e) { /*nothing*/ }
			catch(final ThrowReturn e)   { return this; }
		}

		return this;
	}

	/**
	 * @param elements
	 * @param srcOffset
	 * @param srcLength
	 * @param predicate
	 * @param limit
	 * @return
	 */
	@Override
	public GrowList<E> add(
		final Iterable<? extends E> elements,
		int srcOffset,
		final Integer srcLength,
		final Predicate<E> predicate,
		int skip,
		final Integer limit
	)
	{
		if(srcOffset < 0){
			throw new IllegalArgumentException(this.exceptionStringIterableOffsetNegative(srcOffset));
		}
		if(skip < 0){
			throw new IllegalArgumentException(this.exceptionStringSkipNegative(skip));
		}

		int lim, len;
		E element;

		if(limit == null){
			// no limitation whatsoever, so use simple successive add()
			if(srcLength == null){
				// exclusive iterator instantiation for this block
				final Iterator<? extends E> iter = elements.iterator();
				// offset scrolling
				while(srcOffset --> 0){
					if(!iter.hasNext()) return this; // not even enough elements for offset scrolling, so return.
					iter.next();
				}
				// skip scrolling
				while(skip != 0){
					if(!iter.hasNext()) return this; // not even enough elements for skip scrolling, so return.
					if(predicate.apply(iter.next())){
						skip--;
					}
				}
				// actual adding, unlimited
				while(iter.hasNext()){
					if(predicate.apply(element = iter.next())){
						this.internalAdd(element);
					}
				}
				return this;
			}

			len = srcLength.intValue();
			lim = Integer.MAX_VALUE;
		}
		else {
			if((lim = limit.intValue()) <= 0) return this;
			len = srcLength == null ?Integer.MAX_VALUE :srcLength.intValue();
		}
		if(len < 0){
			throw new IllegalArgumentException(this.exceptionStringIterableLengthNegative(len));
		}
		if(len == 0) return this;

		final Iterator<? extends E> iter = elements.iterator();
		// offset scrolling
		while(srcOffset --> 0){
			if(!iter.hasNext()) return this; // not even enough elements for offset scrolling, so return.
			iter.next();
		}
		// skip scrolling
		while(skip != 0){
			if(!iter.hasNext() || len-- == 0) return this; // not even enough elements for skip scrolling, so return.
			if(predicate.apply(iter.next())){
				skip--;
			}
		}
		// actuall adding, unlimited
		while(iter.hasNext() && len --> 0 && lim != 0){
			if(predicate.apply(element = iter.next())){
				lim--;
				this.internalAdd(element);
			}
		}
		return this;
	}

	/**
	 * @param elements
	 * @param srcIndex
	 * @param srcLength
	 * @param predicate
	 * @param limit
	 * @return
	 */
	@Override
	public GrowList<E> add(
		final Iterable<? extends E> elements,
		int srcOffset,
		final Integer srcLength,
		final TPredicate<E> predicate,
		int skip,
		final Integer limit
	)
	{
		if(srcOffset < 0){
			throw new IllegalArgumentException(this.exceptionStringIterableOffsetNegative(srcOffset));
		}
		if(skip < 0){
			throw new IllegalArgumentException(this.exceptionStringSkipNegative(skip));
		}

		int lim, len;
		E element;

		if(limit == null){
			// no limitation whatsoever, so use simple successive add()
			if(srcLength == null){
				// exclusive iterator instantiation for this block
				final Iterator<? extends E> iter = elements.iterator();
				// offset scrolling
				while(srcOffset --> 0){
					if(!iter.hasNext()) return this; // not even enough elements for offset scrolling, so return.
					iter.next();
				}
				// skip scrolling
				while(skip != 0){
					try{
						while(skip != 0){
							if(!iter.hasNext()) return this; // not even enough elements for skip scrolling, so return.
							if(predicate.apply(iter.next())){
								skip--;
							}
						}
						break;
					}
					catch(final ThrowBreak e)    { return this; /*extracted skip-loop is an implementation detail, so break means return */}
					catch(final ThrowContinue e) { /*nothing*/ }
					catch(final ThrowReturn e)   { return this; }
				}

				// actual adding, unlimited
				while(iter.hasNext()){
					try{
						while(iter.hasNext()){
							if(predicate.apply(element = iter.next())){
								this.internalAdd(element);
							}
						}
						break;
					}
					catch(final ThrowBreak e)    { return this; /*extracted skip-loop is an implementation detail, so break means return */}
					catch(final ThrowContinue e) { /*nothing*/ }
					catch(final ThrowReturn e)   { return this; }
				}
				return this;
			}

			len = srcLength.intValue();
			lim = Integer.MAX_VALUE;
		}
		else {
			if((lim = limit.intValue()) <= 0) return this;
			len = srcLength == null ?Integer.MAX_VALUE :srcLength.intValue();
		}
		if(len < 0){
			throw new IllegalArgumentException(this.exceptionStringIterableLengthNegative(len));
		}
		if(len == 0) return this;

		final Iterator<? extends E> iter = elements.iterator();
		// offset scrolling
		while(srcOffset --> 0){
			if(!iter.hasNext()) return this; // not even enough elements for offset scrolling, so return.
			iter.next();
		}
		// skip scrolling
		while(skip != 0){
			try{
				while(skip != 0){
					if(!iter.hasNext() || len-- == 0) return this; // not even enough elements for skip scrolling, so return.
					if(predicate.apply(iter.next())){
						skip--;
					}
				}
				break;
			}
			catch(final ThrowBreak e)    { return this; /*extracted skip-loop is an implementation detail, so break means return */}
			catch(final ThrowContinue e) { /*nothing*/ }
			catch(final ThrowReturn e)   { return this; }
		}
		// actuall adding, unlimited
		while(iter.hasNext() && len != 0 && lim != 0){
			try{
				while(iter.hasNext() && len != 0 && lim != 0){
					len--;
					if(predicate.apply(element = iter.next())){
						lim--;
						this.internalAdd(element);
					}
				}
				break;
			}
			catch(final ThrowBreak e)    { return this; /*extracted skip-loop is an implementation detail, so break means return */}
			catch(final ThrowContinue e) { /*nothing*/ }
			catch(final ThrowReturn e)   { return this; }
		}
		return this;
	}

	/**
	 * @param index
	 * @param elements
	 * @param srcIndex
	 * @param srcLength
	 * @param predicate
	 * @param limit
	 * @return
	 */
	@Override
	public GrowList<E> insert(
		final int index,
		final E[] elements,
		final int srcIndex,
		final int srcLength,
		final Predicate<E> predicate,
		final int skip,
		final Integer limit
	)
	{
		// sanity checks
		if(index < 0){
			throw new ArrayIndexOutOfBoundsException(index);
		}
		if(index == this.size){
			return this.add(elements, srcIndex, srcLength, predicate, skip, limit);
		}
		if(index > this.size){
			throw new IndexOutOfBoundsException(this.exceptionStringIndexOutOfBounds(index));
		}

		// select elements into buffer
		final GrowList<E> buffer = new GrowList<E>().add(elements, srcLength, srcLength, predicate, skip, limit);

		// internal copying of selected elements
		if(buffer.size == 0) return this; //no element has been selected to be added, so return
		if(Integer.MAX_VALUE - buffer.size > this.size){
			throw new ArrayIndexOutOfBoundsException(this.exceptionStringArrayLengthExceeded(buffer.size));
		}
		this.internalInsertArray(index, buffer.data, buffer.size);
		return this;
	}

	/**
	 * @param index
	 * @param elements
	 * @param srcIndex
	 * @param srcLength
	 * @param predicate
	 * @param limit
	 * @return
	 */
	@Override
	public GrowList<E> insert(
		final int index,
		final E[] elements,
		final int srcIndex,
		final int srcLength,
		final TPredicate<E> predicate,
		final int skip,
		final Integer limit
	)
	{
		// sanity checks
		if(index < 0){
			throw new ArrayIndexOutOfBoundsException(index);
		}
		if(index == this.size){
			return this.add(elements, srcIndex, srcLength, predicate, skip, limit);
		}
		if(index > this.size){
			throw new IndexOutOfBoundsException(this.exceptionStringIndexOutOfBounds(index));
		}

		// select elements into buffer
		final GrowList<E> buffer = new GrowList<E>().add(elements, srcLength, srcLength, predicate, skip, limit);

		// internal copying of selected elements
		if(buffer.size == 0) return this; //no element has been selected to be added, so return
		if(Integer.MAX_VALUE - buffer.size > this.size){
			throw new ArrayIndexOutOfBoundsException(this.exceptionStringArrayLengthExceeded(buffer.size));
		}
		this.internalInsertArray(index, buffer.data, buffer.size);
		return this;
	}

	/**
	 * @param index
	 * @param elements
	 * @param srcOffset
	 * @param srcLength
	 * @param predicate
	 * @param limit
	 * @return
	 */
	@Override
	public GrowList<E> insert(
		final int index,
		final Iterable<? extends E> elements,
		final int srcOffset,
		final Integer srcLength,
		final Predicate<E> predicate,
		final int skip,
		final Integer limit
	)
	{
		// sanity checks
		if(index < 0){
			throw new ArrayIndexOutOfBoundsException(index);
		}
		if(index == this.size){
			return this.add(elements, srcOffset, srcLength, predicate, skip, limit);
		}
		if(index > this.size){
			throw new IndexOutOfBoundsException(this.exceptionStringIndexOutOfBounds(index));
		}

		// select elements into buffer
		final GrowList<E> buffer = new GrowList<E>().add(elements, srcOffset, srcLength, predicate, skip, limit);

		// internal copying of selected elements
		if(buffer.size == 0) return this; //no element has been selected to be added, so return
		if(Integer.MAX_VALUE - buffer.size > this.size){
			throw new ArrayIndexOutOfBoundsException(this.exceptionStringArrayLengthExceeded(buffer.size));
		}
		this.internalInsertArray(index, buffer.data, buffer.size);
		return this;
	}

	/**
	 * @param index
	 * @param elements
	 * @param srcIndex
	 * @param srcLength
	 * @param predicate
	 * @param limit
	 * @return
	 */
	@Override
	public GrowList<E> insert(
		final int index,
		final Iterable<? extends E> elements,
		final int srcOffset,
		final Integer srcLength,
		final TPredicate<E> predicate,
		final int skip,
		final Integer limit
	)
	{
		// sanity checks
		if(index < 0){
			throw new ArrayIndexOutOfBoundsException(index);
		}
		if(index == this.size){
			return this.add(elements, srcOffset, srcLength, predicate, skip, limit);
		}
		if(index > this.size){
			throw new IndexOutOfBoundsException(this.exceptionStringIndexOutOfBounds(index));
		}

		// select elements into buffer
		final GrowList<E> buffer = new GrowList<E>().add(elements, srcOffset, srcLength, predicate, skip, limit);

		// internal copying of selected elements
		if(buffer.size == 0) return this; //no element has been selected to be added, so return
		if(Integer.MAX_VALUE - buffer.size > this.size){
			throw new ArrayIndexOutOfBoundsException(this.exceptionStringArrayLengthExceeded(buffer.size));
		}
		this.internalInsertArray(index, buffer.data, buffer.size);
		return this;
	}

	/**
	 * @param element
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Override
	public int remove(final E element, final Equalator<E> equalator)
	{
		return removeAllFromArray(
			element, this.data, 0, this.size, this.data, 0, this.size, (Equalator<Object>)equalator
		);
	}

	/**
	 * @param element
	 * @return
	 */
	@Override
	public int removeId(final E element)
	{
		return removeAllFromArray(element, this.data, 0, this.size, this.data, 0, this.size);

	}

	/**
	 * @param element
	 * @param limit
	 * @return
	 */
	@Override
	public int removeId(final E element, int skip, final Integer limit)
	{
		if(skip < 0){
			throw new IllegalArgumentException(this.exceptionStringSkipNegative(skip));
		}
		int lim = limit < 0 ?0 :limit;
		if(lim == 0){
			return 0;
		}

		final int size = this.size;
		final Object[] data = this.data;
		int i = 0;

		while(skip != 0){
			if(data[i] == element){
				skip--;
			}
			if(++i >= size){
				return 0;
			}
		}

		final int removeStartIndex = i;
		while(i < size){
			if(data[i] == element){
				data[i] = REMOVE_MARKER;
			}
			i++;
			if(--lim == 0) break;
		}
		final int removeCount = removeAllFromArray(REMOVE_MARKER, data, removeStartIndex, i, data, removeStartIndex, i);
		this.size -= removeCount;
		return removeCount;
	}

	/**
	 * @param startIndex
	 * @param endIndex
	 * @param element
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Override
	public int rngRemove(final int startIndex, final int endIndex, final E element, final Equalator<E> equalator)
	{
		final Object[] data = this.data;
		final int removeStart, removeEnd; //bi-directional index movement
		if(startIndex <= endIndex){
			if(startIndex < 0 || endIndex >= this.size){
				throw new IndexOutOfBoundsException(this.exceptionStringRange(startIndex, endIndex));
			}
			removeStart = startIndex;
			removeEnd = endIndex+1;
		}
		else {
			if(startIndex >= this.size || endIndex < 0){
				throw new IndexOutOfBoundsException(this.exceptionStringRange(endIndex, startIndex));
			}
			removeStart = endIndex;
			removeEnd = startIndex+1;
		}

		final int removeCount = removeAllFromArray(
			element, data, removeStart, removeEnd, data, removeStart, removeEnd, (Equalator<Object>)equalator
		);
		this.size -= removeCount;
		return removeCount;
	}

	/**
	 * @param startIndex
	 * @param endIndex
	 * @param element
	 * @return
	 */
	@Override
	public int rngRemove(final int startIndex, final int endIndex, final E element)
	{
		final int removeStart, removeEnd; //bi-directional index movement
		if(startIndex <= endIndex){
			if(startIndex < 0 || endIndex >= this.size){
				throw new IndexOutOfBoundsException(this.exceptionStringRange(startIndex, endIndex));
			}
			removeStart = startIndex;
			removeEnd = endIndex+1;
		}
		else {
			if(startIndex >= this.size || endIndex < 0){
				throw new IndexOutOfBoundsException(this.exceptionStringRange(endIndex, startIndex));
			}
			removeStart = endIndex;
			removeEnd = startIndex+1;
		}

		final int removeCount = removeAllFromArray(
			element, this.data, removeStart, removeEnd, this.data, removeStart, removeEnd
		);
		this.size -= removeCount;
		return removeCount;
	}

	/**
	 * @param startIndex
	 * @param endIndex
	 * @param o
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Override
	public int rngRemove(
		final int startIndex,
		final int endIndex,
		final E element,
		final Equalator<E> equalator,
		int skip,
		final Integer limit
	)
	{
		final int d; //bi-directional index movement
		if(startIndex <= endIndex){
			if(startIndex < 0 || endIndex >= this.size){
				throw new IndexOutOfBoundsException(this.exceptionStringRange(startIndex, endIndex));
			}
			d = 1; //incrementing direction
		}
		else {
			if(startIndex >= this.size || endIndex < 0){
				throw new IndexOutOfBoundsException(this.exceptionStringRange(endIndex, startIndex));
			}
			d = -1; //decrementing direction
		}

		if(skip < 0){
			throw new IllegalArgumentException(this.exceptionStringSkipNegative(skip));
		}

		int lim = limit == null ? Integer.MAX_VALUE :limit.intValue();


		final Object[] data = this.data;
		int i = startIndex-d;
		while(skip != 0){
			if(equalator.equal((E)data[i+=d], element)){
				skip--;
			}
			if(i == endIndex){
				return 0;
			}
		}

		final int remStart, remBound, removeCount;
		if(d == 1){
			remStart = i;
			remBound = endIndex+1;
		}
		else {
			remStart = endIndex;
			remBound = i+1;
		}
		try{
			while(i != endIndex && lim > 0){
				if(equalator.equal((E)data[i+=d], element)){
					data[i] = REMOVE_MARKER;
					lim--;
				}
			}
		}
		finally{
			removeCount = removeAllFromArray(REMOVE_MARKER, data, remStart, remBound, data, remStart, remBound);
		}

		this.size -= removeCount;
		return removeCount;
	}

	/**
	 * @param startIndex
	 * @param endIndex
	 * @param element
	 * @return
	 */
	@Override
	public int rngRemove(final int startIndex, final int endIndex, final E element, int skip, final Integer limit)
	{
		final int d, remStart, remEnd; //bi-directional index movement
		if(startIndex <= endIndex){
			if(startIndex < 0 || endIndex >= this.size){
				throw new IndexOutOfBoundsException(this.exceptionStringRange(startIndex, endIndex));
			}
			d = 1; //incrementing direction
			remStart = startIndex;
			remEnd = endIndex+1;
		}
		else {
			if(startIndex >= this.size || endIndex < 0){
				throw new IndexOutOfBoundsException(this.exceptionStringRange(endIndex, startIndex));
			}
			d = -1; //decrementing direction
			remStart = endIndex;
			remEnd = startIndex+1;
		}

		int lim = limit == null ?Integer.MAX_VALUE :limit.intValue();
		final Object[] data = this.data;
		int i = startIndex-d;
		while(skip != 0){
			if(element == data[i+=d]){
				skip--;
			}
			if(i == endIndex) return 0;
		}
		while(i != endIndex && lim != 0){
			if(element == data[i+=d]){
				data[i] = REMOVE_MARKER;
				lim--;
			}
		}
		final int removedCount = removeAllFromArray(element, data, remStart, remEnd, data, remStart, remEnd);
		this.size -= removedCount;
		return removedCount;
	}

	/**
	 * @param oldElement
	 * @param newElement
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Override
	public int replace(final E oldElement, final E newElement, final Equalator<E> equalator)
	{
		int replaceCount = 0;
		final Object[] data = this.data;		
		for(int i = 0, size = this.size; i < size; i++){
			if(equalator.equal((E)data[i], oldElement)){
				data[i] = newElement;
				replaceCount++;
			}
		}
		return replaceCount;
	}

	/**
	 * @param oldElement
	 * @param newElement
	 * @return
	 */
	@Override
	public int replace(final E oldElement, final E newElement)
	{
		int replaceCount = 0;
		final Object[] data = this.data;
		for(int i = 0, size = this.size; i < size; i++){
			if(data[i] == oldElement){
				data[i] = newElement;
				replaceCount++;
			}
		}
		return replaceCount;
	}

	/**
	 * @param startIndex
	 * @param endIndex
	 * @param oldElement
	 * @param newElement
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Override
	public int rngReplace(
		final int startIndex,
		final int endIndex,
		final E oldElement,
		final E newElement,
		final Equalator<E> equalator
	)
	{
		final int d;
		if(startIndex <= endIndex){
			if(startIndex < 0 || endIndex >= this.size){
				throw new IndexOutOfBoundsException(this.exceptionStringRange(startIndex, endIndex));
			}
			d = 1;
		}
		else {
			if(endIndex < 0 || startIndex >= this.size){
				throw new IndexOutOfBoundsException(this.exceptionStringRange(endIndex, startIndex));
			}
			d = -1;
		}

		int replaceCount = 0;
		final Object[] data = this.data;		
		for(int i = startIndex-d; i != endIndex;){
			if(equalator.equal((E)data[i+=d], oldElement)){
				data[i] = newElement;
				replaceCount++;
			}
		}
		return replaceCount;
	}

	/**
	 * @param startIndex
	 * @param endIndex
	 * @param oldElement
	 * @param newElement
	 * @return
	 */
	@Override
	public int rngReplace(final int startIndex, final int endIndex, final E oldElement, final E newElement)
	{
		final int d;
		if(startIndex <= endIndex){
			if(startIndex < 0 || endIndex >= this.size){
				throw new IndexOutOfBoundsException(this.exceptionStringRange(startIndex, endIndex));
			}
			d = 1;
		}
		else {
			if(endIndex < 0 || startIndex >= this.size){
				throw new IndexOutOfBoundsException(this.exceptionStringRange(endIndex, startIndex));
			}
			d = -1;
		}

		int replaceCount = 0;
		final Object[] data = this.data;
		for(int i = startIndex-d; i != endIndex;){
			if(data[i+=d] == oldElement){
				data[i] = newElement;
				replaceCount++;
			}
		}
		return replaceCount;
	}

	/**
	 * @param <R>
	 * @param aggregate
	 * @return
	 * @see com.xdev.jadoth.collections.XGettingCollection#aggregate(com.xdev.jadoth.lang.functional.aggregates.Aggregate)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public <R> R aggregate(final Aggregate<E, R> aggregate)
	{
		final Object[] data = this.data;
		for(int i = 0, size = this.size; i < size; i++){
			aggregate.execute((E)data[i]);
		}
		return aggregate.yield();
	}

	/**
	 * @param <R>
	 * @param aggregate
	 * @return
	 * @see com.xdev.jadoth.collections.XGettingCollection#aggregate(com.xdev.jadoth.lang.functional.aggregates.TAggregate)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public <R> R aggregate(final TAggregate<E, R> aggregate)
	{
		final Object[] data = this.data;
		final int size = this.size;
		for(int i = 0; i < size; i++) {
			try {
				aggregate.execute((E)data[i]);
			}
			catch(final ThrowBreak b)   { break; }
			catch(final ThrowContinue c){ /*Nothing*/ }
			catch(final ThrowReturn r)  { return aggregate.yield(); }
		}
		return aggregate.yield();
	}

	/**
	 * @param <R>
	 * @param startIndex
	 * @param endIndex
	 * @param aggregate
	 * @return
	 * @see com.xdev.jadoth.collections.XGettingList#rngAggregate(int, int, com.xdev.jadoth.lang.functional.aggregates.Aggregate)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public <R> R rngAggregate(final int startIndex, final int endIndex, final Aggregate<E, R> aggregate)
	{
		final int d; //bi-directional index movement
		if(startIndex <= endIndex){
			if(startIndex < 0 || endIndex >= this.size){
				throw new IndexOutOfBoundsException(this.exceptionStringRange(startIndex, endIndex));
			}
			d = 1; //incrementing direction
		}
		else {
			if(startIndex >= this.size || endIndex < 0){
				throw new IndexOutOfBoundsException(this.exceptionStringRange(endIndex, startIndex));
			}
			d = -1; //decrementing direction
		}

		final Object[] data = this.data;
		for(int i = startIndex-d; i != endIndex;){
			aggregate.execute((E)data[i+=d]);
		}
		return aggregate.yield();
	}

	/**
	 *
	 * @param <R>
	 * @param startIndex
	 * @param endIndex
	 * @param aggregate
	 * @return
	 * @see com.xdev.jadoth.collections.XGettingList#rngAggregate(int, int, com.xdev.jadoth.lang.functional.aggregates.TAggregate)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public <R> R rngAggregate(final int startIndex, final int endIndex, final TAggregate<E, R> aggregate)
	{
		final int d; //bi-directional index movement
		if(startIndex <= endIndex){
			if(startIndex < 0 || endIndex >= this.size){
				throw new IndexOutOfBoundsException(this.exceptionStringRange(startIndex, endIndex));
			}
			d = 1; //incrementing direction
		}
		else {
			if(startIndex >= this.size || endIndex < 0){
				throw new IndexOutOfBoundsException(this.exceptionStringRange(endIndex, startIndex));
			}
			d = -1; //decrementing direction
		}

		//this construction yields the same performance as the loop without inner try, but can still continue the loop
		final Object[] data = this.data;
		int i = startIndex-d; //start i one before startIndex as (i+=d) only works as prefix ~crement
		while(i != endIndex){ //while(true) astonishingly is significantly slower than duplicated inner condition
			try {
				while(i != endIndex){
					aggregate.execute((E)data[i+=d]);
				}
				break;
			}
			catch(final ThrowBreak b)   { break; }
			catch(final ThrowContinue c){ /*Nothing*/ }
			catch(final ThrowReturn r)  { aggregate.yield(); }
		}
		return aggregate.yield();
	}

	/**
	 * @param <L>
	 * @param targetCollection
	 * @param predicate
	 * @return
	 * @see com.xdev.jadoth.collections.XGettingCollection#copyTo(com.xdev.jadoth.collections.XGettingCollection, com.xdev.jadoth.lang.functional.Predicate)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public <L extends Collecting<E>> L copyTo(final L targetCollection, final Predicate<E> predicate)
	{
		final Object[] data = this.data;
		for(int i = 0, size = this.size; i < size; i++){
			final E element = (E)data[i];
			if(predicate.apply(element)){
				targetCollection.add(element);
			}
		}
		return targetCollection;
	}

	/**
	 * @param <L>
	 * @param targetCollection
	 * @param predicate
	 * @return
	 * @see com.xdev.jadoth.collections.XGettingCollection#copyTo(com.xdev.jadoth.collections.XGettingCollection, com.xdev.jadoth.lang.functional.controlflow.TPredicate)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public <L extends Collecting<E>> L copyTo(final L targetCollection, final TPredicate<E> predicate)
	{
		final int size = this.size;
		final Object[] data = this.data;

		//this construction yields the same performance as a loop without inner try, but can still continue the loop
		int i = 0;
		while(i < size){ //while(true) astonishingly is significantly slower than duplicated inner condition
			try {
				while(i < size){
					final E element = (E)data[i++];
					if(predicate.apply(element)){
						targetCollection.add(element);
					}
				}
				break;
			}
			catch(final ThrowBreak b)   { break; }
			catch(final ThrowContinue c){ /*Nothing*/ }
			catch(final ThrowReturn r)  { return targetCollection; }
		}
		return targetCollection;
	}

	/**
	 * @param <L>
	 * @param targetCollection
	 * @param predicate
	 * @param limit
	 * @return
	 * @see com.xdev.jadoth.collections.XGettingCollection#copyTo(com.xdev.jadoth.collections.XGettingCollection, com.xdev.jadoth.lang.functional.Predicate, int)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public <L extends Collecting<E>> L copyTo(
		final L targetCollection,
		final Predicate<E> predicate,
		final int skip,
		final Integer limit
	)
	{
		int lim = limit == null ? Integer.MAX_VALUE :limit.intValue();
		final Object[] data = this.data;
		for(int i = 0, size = this.size; i < size && lim > 0; i++){
			final E element = (E)data[i];
			if(predicate.apply(element)){
				targetCollection.add(element);
				lim--;
			}
		}
		return targetCollection;
	}

	/**
	 * @param <L>
	 * @param targetCollection
	 * @param predicate
	 * @param limit
	 * @return
	 * @see com.xdev.jadoth.collections.XGettingCollection#copyTo(com.xdev.jadoth.collections.XGettingCollection, com.xdev.jadoth.lang.functional.controlflow.TPredicate, int)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public <L extends Collecting<E>> L copyTo(
		final L targetCollection, final TPredicate<E> predicate, final int skip, final Integer limit
	)
	{
		final int size = this.size;
		final Object[] data = this.data;
		int lim = limit == null ? Integer.MAX_VALUE :limit.intValue();

		//this construction yields the same performance as a loop without inner try, but can still continue the loop
		int i = 0;
		while(i < size && lim > 0){
			try {
				while(i < size && lim > 0){
					final E element = (E)data[i++];
					if(predicate.apply(element)){
						targetCollection.add(element);
						lim--;
					}
				}
				break;
			}
			catch(final ThrowBreak b)   { break; }
			catch(final ThrowContinue c){ /*Nothing*/ }
			catch(final ThrowReturn r)  { return targetCollection; }
		}
		return targetCollection;
	}

	/**
	 * @param <L>
	 * @param startIndex
	 * @param endIndex
	 * @param targetCollection
	 * @param predicate
	 * @return
	 * @see com.xdev.jadoth.collections.XGettingList#rngCopyTo(int, int, com.xdev.jadoth.collections.XAddingCollection, com.xdev.jadoth.lang.functional.Predicate)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public <L extends Collecting<E>> L rngCopyTo(
		final int startIndex,
		final int endIndex,
		final L targetCollection,
		final Predicate<E> predicate
	)
	{
		final int d; //bi-directional index movement
		if(startIndex <= endIndex){
			if(startIndex < 0 || endIndex >= this.size){
				throw new IndexOutOfBoundsException(this.exceptionStringRange(startIndex, endIndex));
			}
			d = 1; //incrementing direction
		}
		else {
			if(startIndex >= this.size || endIndex < 0){
				throw new IndexOutOfBoundsException(this.exceptionStringRange(endIndex, startIndex));
			}
			d = -1; //decrementing direction
		}

		final Object[] data = this.data;
		for(int i = startIndex-d; i != endIndex;){
			final E e = (E)data[i+=d];
			if(predicate.apply(e)){
				targetCollection.add(e);
			}
		}
		return targetCollection;
	}

	/**
	 * @param <L>
	 * @param startIndex
	 * @param endIndex
	 * @param targetCollection
	 * @param predicate
	 * @return
	 * @see com.xdev.jadoth.collections.XGettingList#rngCopyTo(int, int, com.xdev.jadoth.collections.XAddingCollection, com.xdev.jadoth.lang.functional.controlflow.TPredicate)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public <L extends Collecting<E>> L rngCopyTo(
		final int startIndex,
		final int endIndex,
		final L targetCollection,
		final TPredicate<E> predicate
	)
	{
		final int d; //bi-directional index movement
		if(startIndex <= endIndex){
			if(startIndex < 0 || endIndex >= this.size){
				throw new IndexOutOfBoundsException(this.exceptionStringRange(startIndex, endIndex));
			}
			d = 1; //incrementing direction
		}
		else {
			if(startIndex >= this.size || endIndex < 0){
				throw new IndexOutOfBoundsException(this.exceptionStringRange(endIndex, startIndex));
			}
			d = -1; //decrementing direction
		}

		final Object[] data = this.data;
		int i = startIndex-d;
		while(i != endIndex){
			try{
				while(i != endIndex){
					final E e = (E)data[i+=d];
					if(predicate.apply(e)){
						targetCollection.add(e);
					}
				}
				break;
			}
			catch(final ThrowBreak b)   { break; }
			catch(final ThrowReturn r)  { return targetCollection; }
			catch(final ThrowContinue c){ /*Nothing*/ }
		}
		return targetCollection;
	}

	/**
	 * @param <L>
	 * @param startIndex
	 * @param endIndex
	 * @param targetCollection
	 * @param predicate
	 * @param limit
	 * @return
	 * @see com.xdev.jadoth.collections.XGettingList#rngCopyTo(int, int, com.xdev.jadoth.collections.XAddingCollection, com.xdev.jadoth.lang.functional.Predicate, int)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public <L extends Collecting<E>> L rngCopyTo(
		final int startIndex,
		final int endIndex,
		final L targetCollection,
		final Predicate<E> predicate,
		int skip,
		final Integer limit
	)
	{
		final int d; //bi-directional index movement
		if(startIndex <= endIndex){
			if(startIndex < 0 || endIndex >= this.size){
				throw new IndexOutOfBoundsException(this.exceptionStringRange(startIndex, endIndex));
			}
			d = 1; //incrementing direction
		}
		else {
			if(startIndex >= this.size || endIndex < 0){
				throw new IndexOutOfBoundsException(this.exceptionStringRange(endIndex, startIndex));
			}
			d = -1; //decrementing direction
		}
		int lim = limit == null ?Integer.MAX_VALUE :limit.intValue();
		final Object[] data = this.data;
		int i = startIndex-d;
		while(skip != 0){
			if(predicate.apply((E)data[i+=d])){
				skip--;
			}
			if(i == endIndex){
				return targetCollection;
			}
		}
		while(i != endIndex && lim > 0){
			final E e = (E)data[i+=d];
			if(predicate.apply(e)){
				targetCollection.add(e);
				lim--;
			}
		}
		return targetCollection;
	}

	/**
	 * @param <L>
	 * @param startIndex
	 * @param endIndex
	 * @param targetCollection
	 * @param predicate
	 * @param limit
	 * @return
	 * @see com.xdev.jadoth.collections.XGettingList#rngCopyTo(int, int, com.xdev.jadoth.collections.XAddingCollection, com.xdev.jadoth.lang.functional.controlflow.TPredicate, int)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public <L extends Collecting<E>> L rngCopyTo(
		final int startIndex,
		final int endIndex,
		final L targetCollection,
		final TPredicate<E> predicate,
		int skip,
		final Integer limit
	)
	{
		final int d; //bi-directional index movement
		if(startIndex <= endIndex){
			if(startIndex < 0 || endIndex >= this.size){
				throw new IndexOutOfBoundsException(this.exceptionStringRange(startIndex, endIndex));
			}
			d = 1; //incrementing direction
		}
		else {
			if(startIndex >= this.size || endIndex < 0){
				throw new IndexOutOfBoundsException(this.exceptionStringRange(endIndex, startIndex));
			}
			d = -1; //decrementing direction
		}
		int lim = limit == null ?Integer.MAX_VALUE :limit.intValue();
		final Object[] data = this.data;
		int i = startIndex-d;
		while(skip != 0){
			try{
				while(skip != 0){
					if(predicate.apply((E)data[i+=d])){
						skip--;
					}
					if(i == endIndex){
						return targetCollection;
					}
				}
				break;
			}
			catch(final ThrowBreak b)   { return targetCollection; }
			catch(final ThrowReturn r)  { return targetCollection; }
			catch(final ThrowContinue c){ /*Nothing*/ }
		}

		while(i != endIndex && lim > 0){
			try{
				while(i != endIndex && lim > 0){
					final E e = (E)data[i+=d];
					if(predicate.apply(e)){
						targetCollection.add(e);
						lim--;
					}
				}
				break;
			}
			catch(final ThrowBreak b)   { return targetCollection; }
			catch(final ThrowReturn r)  { return targetCollection; }
			catch(final ThrowContinue c){ /*Nothing*/ }
		}
		return targetCollection;
	}

	/**
	 * @param comparator
	 * @return
	 * @see com.xdev.jadoth.collections.XSettingList#sort(java.util.Comparator)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void sort(final Comparator<? super E> comparator)
	{
		JaSort.mergesort(this.data, 0, this.size-1, (Comparator<Object>)comparator);
	}

	/**
	 * @param oldElements
	 * @param newElement
	 * @return
	 * @see com.xdev.jadoth.collections.XSettingCollection#replaceAll(E[], java.lang.Object)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public int replaceAll(final E[] oldElements, final E newElement)
	{
		int replaceCount = 0;
		final Object[] data = this.data;
		for(int i = 0, size = this.size; i < size; i++){
			if(JaArrays.contains(oldElements, (E)data[i])){
				data[i] = newElement;
				replaceCount++;
			}
		}
		return replaceCount;
	}

	/**
	 * @param oldElements
	 * @param newElement
	 * @param equalator
	 * @return
	 * @see com.xdev.jadoth.collections.XSettingCollection#replaceAll(E[], java.lang.Object, com.xdev.jadoth.lang.Equalator)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public int replaceAll(final E[] oldElements, final E newElement, final Equalator<E> equalator)
	{
		int replaceCount = 0;
		final Object[] data = this.data;
		for(int i = 0, size = this.size; i < size; i++){
			if(JaArrays.contains(oldElements, (E)data[i], equalator)){
				data[i] = newElement;
				replaceCount++;
			}
		}
		return replaceCount;
	}

	/**
	 * @param oldElements
	 * @param newElement
	 * @return
	 * @see com.xdev.jadoth.collections.XSettingCollection#replaceAll(XGettingCollection, java.lang.Object)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public int replaceAll(final XGettingCollection<E> oldElements, final E newElement)
	{
		int replaceCount = 0;
		final Object[] data = this.data;
		for(int i = 0, size = this.size; i < size; i++){
			if(oldElements.containsId((E)data[i])){
				data[i] = newElement;
				replaceCount++;
			}
		}
		return replaceCount;
	}

	/**
	 * @param oldElements
	 * @param newElement
	 * @param equalator
	 * @return
	 * @see com.xdev.jadoth.collections.XSettingCollection#replaceAll(com.xdev.jadoth.collections.XGettingCollection, java.lang.Object, com.xdev.jadoth.lang.Equalator)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public int replaceAll(final XGettingCollection<E> oldElements, final E newElement, final Equalator<E> equalator)
	{
		int replaceCount = 0;
		final Object[] data = this.data;
		for(int i = 0, size = this.size; i < size; i++){
			if(oldElements.contains((E)data[i], equalator)){
				data[i] = newElement;
				replaceCount++;
			}
		}
		return replaceCount;
	}

	/**
	 * @param predicate
	 * @param newElement
	 * @return
	 * @see com.xdev.jadoth.collections.XSettingCollection#replace(com.xdev.jadoth.lang.functional.Predicate, java.lang.Object)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public int replace(final Predicate<E> predicate, final E newElement)
	{
		int replaceCount = 0;
		final Object[] data = this.data;
		for(int i = 0, size = this.size; i < size; i++){
			if(predicate.apply((E)data[i])){
				data[i] = newElement;
				replaceCount++;
			}
		}
		return replaceCount;
	}

	/**
	 * @param predicate
	 * @param newElement
	 * @return
	 * @see com.xdev.jadoth.collections.XSettingCollection#replace(com.xdev.jadoth.lang.functional.controlflow.TPredicate, java.lang.Object)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public int replace(final TPredicate<E> predicate, final E newElement)
	{
		final Object[] data = this.data;
		final int lastIndex = this.size-1;
		int replaceCount = 0;
		int i = -1;
		while(i != lastIndex){
			try{
				while(i != lastIndex){
					if(predicate.apply((E)data[++i])){
						data[i] = newElement;
						replaceCount++;
					}
				}
				break;
			}
			catch(final ThrowBreak b)   { break; }
			catch(final ThrowReturn r)  { return replaceCount; }
			catch(final ThrowContinue c){ /*Nothing*/ }
		}
		return replaceCount;
	}

	/**
	 * @param predicate
	 * @param newElement
	 * @param skip
	 * @param limit
	 * @return
	 * @see com.xdev.jadoth.collections.XSettingCollection#replace(com.xdev.jadoth.lang.functional.Predicate, java.lang.Object, int, int)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public int replace(final Predicate<E> predicate, final E newElement, int skip, final Integer limit)
	{
		if(skip < 0){
			throw new IllegalArgumentException(this.exceptionStringSkipNegative(skip));
		}
		int lim = limit == null ?Integer.MAX_VALUE :limit.intValue();

		final Object[] data = this.data;
		final int size = this.size;

		int i = 0;
		while(skip != 0){
			if(predicate.apply((E)data[i])){
				skip--;
			}
			if(++i >= size){
				return 0;
			}
		}

		int replaceCount = 0;
		while(i < size && lim > 0){
			if(predicate.apply((E)data[i])){
				data[i] = newElement;
				lim--;
				replaceCount++;
			}
			i++;
		}
		return replaceCount;
	}

	/**
	 * @param predicate
	 * @param newElement
	 * @param skip
	 * @param limit
	 * @return
	 * @see com.xdev.jadoth.collections.XSettingCollection#replace(com.xdev.jadoth.lang.functional.controlflow.TPredicate, java.lang.Object, int, int)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public int replace(final TPredicate<E> predicate, final E newElement, int skip, final Integer limit)
	{
		if(skip < 0){
			throw new IllegalArgumentException(this.exceptionStringSkipNegative(skip));
		}
		int lim = limit == null ?Integer.MAX_VALUE :limit.intValue();

		final Object[] data = this.data;
		final int size = this.size;

		int i = -1;
		while(skip != 0){
			try{
				while(skip != 0){
					if(predicate.apply((E)data[++i])){
						skip--;
					}
					if(i == size){
						return 0;
					}
				}
				break;
			}
			catch(final ThrowBreak b)   { break; }
			catch(final ThrowReturn r)  { return 0; }
			catch(final ThrowContinue c){ /*Nothing*/ }
		}

		int replaceCount = 0;
		while(i < size && lim > 0){
			try{
				while(i < size && lim > 0){
					if(predicate.apply((E)data[++i])){
						data[i] = newElement;
						lim--;
						replaceCount++;
					}
				}
				break;
			}
			catch(final ThrowBreak b)   { break; }
			catch(final ThrowReturn r)  { return replaceCount; }
			catch(final ThrowContinue c){ /*Nothing*/ }
		}
		return replaceCount;
	}

	/**
	 * @param predicate
	 * @param newElement
	 * @return
	 * @see com.xdev.jadoth.collections.XSettingCollection#replaceOne(com.xdev.jadoth.lang.functional.Predicate, java.lang.Object)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public int replaceOne(final Predicate<E> predicate, final E newElement)
	{
		final Object[] data = this.data;
		for(int i = 0, size = this.size; i < size; i++){
			if(predicate.apply((E)data[i])){
				data[i] = newElement;
				return i;
			}
		}
		return -1;
	}

	/**
	 * @param predicate
	 * @param newElement
	 * @return
	 * @see com.xdev.jadoth.collections.XSettingCollection#replaceOne(com.xdev.jadoth.lang.functional.controlflow.TPredicate, java.lang.Object)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public int replaceOne(final TPredicate<E> predicate, final E newElement)
	{
		final Object[] data = this.data;
		final int lastIndex = this.size-1;
		int i = -1;
		while(i != lastIndex){
			try{
				while(i != lastIndex){
					if(predicate.apply((E)data[++i])){
						data[i] = newElement;
						return i;
					}
				}
				break;
			}
			catch(final ThrowBreak b)   { break; }
			catch(final ThrowReturn r)  { return -1; }
			catch(final ThrowContinue c){ /*Nothing*/ }
		}
		return -1;
	}

	/**
	 * @param oldElements
	 * @param newElement
	 * @param skip
	 * @param limit
	 * @return
	 * @see com.xdev.jadoth.collections.XSettingCollection#replaceAll(E[], java.lang.Object, int, int)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public int replaceAll(final E[] oldElements, final E newElement, int skip, final Integer limit)
	{
		if(skip < 0){
			throw new IllegalArgumentException(this.exceptionStringSkipNegative(skip));
		}
		final Object[] data = this.data;
		final int size = this.size;
		int i = 0;
		while(skip != 0){
			if(JaArrays.contains(oldElements, (E)data[i])){
				skip--;
			}
			if(++i >= size){
				return 0;
			}
		}
		int replaceCount = 0;
		int lim = limit == null ?Integer.MAX_VALUE :limit.intValue();
		while(i != size && lim > 0){
			if(JaArrays.contains(oldElements, (E)data[i])){
				data[i] = newElement;
				lim--;
				replaceCount++;
			}
			i++;
		}
		return replaceCount;
	}

	/**
	 * @param oldElements
	 * @param newElement
	 * @param skip
	 * @param limit
	 * @param equalator
	 * @return
	 * @see com.xdev.jadoth.collections.XSettingCollection#replaceAll(E[], java.lang.Object, int, int, com.xdev.jadoth.lang.Equalator)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public int replaceAll(
		final E[] oldElements,
		final E newElement,
		int skip,
		final Integer limit,
		final Equalator<E> equalator
	)
	{
		if(skip < 0){
			throw new IllegalArgumentException(this.exceptionStringSkipNegative(skip));
		}
		final Object[] data = this.data;
		final int size = this.size;
		int i = 0;
		while(skip != 0){
			if(JaArrays.contains(oldElements, (E)data[i], equalator)){
				skip--;
			}
			if(++i >= size){
				return 0;
			}
		}
		int replaceCount = 0;
		int lim = limit == null ?Integer.MAX_VALUE :limit.intValue();
		while(i != size && lim > 0){
			if(JaArrays.contains(oldElements, (E)data[i], equalator)){
				data[i] = newElement;
				lim--;
				replaceCount++;
			}
			i++;
		}
		return replaceCount;
	}

	/**
	 * @param oldElements
	 * @param newElement
	 * @param skip
	 * @param limit
	 * @return
	 * @see com.xdev.jadoth.collections.XSettingCollection#replaceAll(XGettingCollection, java.lang.Object, int, int)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public int replaceAll(final XGettingCollection<E> oldElements, final E newElement, int skip, final Integer limit)
	{
		if(skip < 0){
			throw new IllegalArgumentException(this.exceptionStringSkipNegative(skip));
		}
		final Object[] data = this.data;
		final int size = this.size;
		int i = 0;
		while(skip != 0){
			if(oldElements.containsId((E)data[i])){
				skip--;
			}
			if(++i >= size){
				return 0;
			}
		}
		int replaceCount = 0;
		int lim = limit == null ?Integer.MAX_VALUE :limit.intValue();
		while(i != size && lim > 0){
			if(oldElements.containsId((E)data[i])){
				data[i] = newElement;
				lim--;
				replaceCount++;
			}
			i++;
		}
		return replaceCount;
	}

	/**
	 * @param oldElements
	 * @param newElement
	 * @param skip
	 * @param limit
	 * @param equalator
	 * @return
	 * @see com.xdev.jadoth.collections.XSettingCollection#replaceAll(com.xdev.jadoth.collections.XGettingCollection, java.lang.Object, int, int, com.xdev.jadoth.lang.Equalator)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public int replaceAll(
		final XGettingCollection<E> oldElements,
		final E newElement,
		int skip,
		final Integer limit,
		final Equalator<E> equalator
	)
	{
		if(skip < 0){
			throw new IllegalArgumentException(this.exceptionStringSkipNegative(skip));
		}
		final Object[] data = this.data;
		final int size = this.size;
		int i = 0;
		while(skip != 0){
			if(oldElements.contains((E)data[i], equalator)){
				skip--;
			}
			if(++i >= size){
				return 0;
			}
		}
		int replaceCount = 0;
		int lim = limit == null ?Integer.MAX_VALUE :limit.intValue();
		while(i != size && lim > 0){
			if(oldElements.contains((E)data[i], equalator)){
				data[i] = newElement;
				lim--;
				replaceCount++;
			}
			i++;
		}
		return replaceCount;
	}

	/**
	 * @param startIndex
	 * @param endIndex
	 * @param predicate
	 * @param newElement
	 * @return
	 * @see com.xdev.jadoth.collections.XSettingList#rngReplace(int, int, com.xdev.jadoth.lang.functional.Predicate, java.lang.Object)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public int rngReplace(final int startIndex, final int endIndex, final Predicate<E> predicate, final E newElement)
	{
		final int d; //bi-directional index movement
		if(startIndex <= endIndex){
			if(startIndex < 0 || endIndex >= this.size){
				throw new IndexOutOfBoundsException(this.exceptionStringRange(startIndex, endIndex));
			}
			d = 1; //incrementing direction
		}
		else {
			if(startIndex >= this.size || endIndex < 0){
				throw new IndexOutOfBoundsException(this.exceptionStringRange(endIndex, startIndex));
			}
			d = -1; //decrementing direction
		}

		int replaceCount = 0;
		final Object[] data = this.data;		
		for(int i = startIndex-d; i != endIndex;){
			if(predicate.apply((E)data[i+=d])){
				data[i] = newElement;
				replaceCount++;
			}
		}
		return replaceCount;
	}

	/**
	 * @param startIndex
	 * @param endIndex
	 * @param predicate
	 * @param newElement
	 * @return
	 * @see com.xdev.jadoth.collections.XSettingList#rngReplace(int, int, com.xdev.jadoth.lang.functional.controlflow.TPredicate, java.lang.Object)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public int rngReplace(final int startIndex, final int endIndex, final TPredicate<E> predicate, final E newElement)
	{
		final int d; //bi-directional index movement
		if(startIndex <= endIndex){
			if(startIndex < 0 || endIndex >= this.size){
				throw new IndexOutOfBoundsException(this.exceptionStringRange(startIndex, endIndex));
			}
			d = 1; //incrementing direction
		}
		else {
			if(startIndex >= this.size || endIndex < 0){
				throw new IndexOutOfBoundsException(this.exceptionStringRange(endIndex, startIndex));
			}
			d = -1; //decrementing direction
		}

		int replaceCount = 0;
		final Object[] data = this.data;
		int i = startIndex-d;
		while(i != endIndex){
			try{
				while(i != endIndex){
					if(predicate.apply((E)data[i+=d])){
						data[i] = newElement;
						replaceCount++;
					}
				}
				break;
			}
			catch(final ThrowBreak b)   { break; }
			catch(final ThrowReturn r)  { return replaceCount; }
			catch(final ThrowContinue c){ /*Nothing*/ }
		}
		return replaceCount;
	}

	/**
	 * @param startIndex
	 * @param endIndex
	 * @param predicate
	 * @param newElement
	 * @param skip
	 * @param limit
	 * @return
	 * @see com.xdev.jadoth.collections.XSettingList#rngReplace(int, int, com.xdev.jadoth.lang.functional.Predicate, java.lang.Object, int, int)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public int rngReplace(
		final int startIndex,
		final int endIndex,
		final Predicate<E> predicate,
		final E newElement,
		int skip,
		final Integer limit
	)
	{
		if(skip < 0){
			throw new IllegalArgumentException(this.exceptionStringSkipNegative(skip));
		}

		final int d; //bi-directional index movement
		if(startIndex <= endIndex){
			if(startIndex < 0 || endIndex >= this.size){
				throw new IndexOutOfBoundsException(this.exceptionStringRange(startIndex, endIndex));
			}
			d = 1; //incrementing direction
		}
		else {
			if(startIndex >= this.size || endIndex < 0){
				throw new IndexOutOfBoundsException(this.exceptionStringRange(endIndex, startIndex));
			}
			d = -1; //decrementing direction
		}

		int lim = limit == null ?Integer.MAX_VALUE :limit.intValue();
		int replaceCount = 0;
		final Object[] data = this.data;
		int i = startIndex-d;
		
		while(skip != 0){
			if(predicate.apply((E)data[i+=d])){
				skip--;
			}
			if(i == endIndex){
				return 0;
			}
		}
		while(i != endIndex && lim > 0){
			if(predicate.apply((E)data[i+=d])){
				data[i] = newElement;
				replaceCount++;
				lim--;
			}
		}
		return replaceCount;
	}

	/**
	 * @param startIndex
	 * @param endIndex
	 * @param predicate
	 * @param newElement
	 * @param skip
	 * @param limit
	 * @return
	 * @see com.xdev.jadoth.collections.XSettingList#rngReplace(int, int, com.xdev.jadoth.lang.functional.controlflow.TPredicate, java.lang.Object, int, int)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public int rngReplace(
		final int startIndex,
		final int endIndex,
		final TPredicate<E> predicate,
		final E newElement,
		int skip,
		final Integer limit
	)
	{
		if(skip < 0){
			throw new IllegalArgumentException(this.exceptionStringSkipNegative(skip));
		}

		final int d; //bi-directional index movement
		if(startIndex <= endIndex){
			if(startIndex < 0 || endIndex >= this.size){
				throw new IndexOutOfBoundsException(this.exceptionStringRange(startIndex, endIndex));
			}
			d = 1; //incrementing direction
		}
		else {
			if(startIndex >= this.size || endIndex < 0){
				throw new IndexOutOfBoundsException(this.exceptionStringRange(endIndex, startIndex));
			}
			d = -1; //decrementing direction
		}

		int lim = limit == null ?Integer.MAX_VALUE :limit.intValue();
		int replaceCount = 0;
		final Object[] data = this.data;
		int i = startIndex-d;

		while(skip != 0){
			try{
				while(skip != 0){					
					if(predicate.apply((E)data[i+=d])){
						skip--;
					}
					if(i == endIndex){
						return 0;
					}
				}
				break;
			}
			catch(final ThrowBreak b)   { break; }
			catch(final ThrowReturn r)  { return 0; }
			catch(final ThrowContinue c){ /*Nothing*/ }
		}
		while(i != endIndex && lim > 0){
			try{
				while(i != endIndex && lim > 0){
					if(predicate.apply((E)data[i+=d])){
						data[i] = newElement;
						replaceCount++;
						lim--;
					}
				}
				break;
			}
			catch(final ThrowBreak b)   { break; }
			catch(final ThrowReturn r)  { return replaceCount; }
			catch(final ThrowContinue c){ /*Nothing*/ }

		}
		return replaceCount;
	}

	/**
	 * @param startIndex
	 * @param endIndex
	 * @param oldElements
	 * @param newElement
	 * @return
	 * @see com.xdev.jadoth.collections.XSettingList#rngReplaceAll(int, int, E[], java.lang.Object)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public int rngReplaceAll(final int startIndex, final int endIndex, final E[] oldElements, final E newElement)
	{
		final int d; //bi-directional index movement
		if(startIndex <= endIndex){
			if(startIndex < 0 || endIndex >= this.size){
				throw new IndexOutOfBoundsException(this.exceptionStringRange(startIndex, endIndex));
			}
			d = 1; //incrementing direction
		}
		else {
			if(startIndex >= this.size || endIndex < 0){
				throw new IndexOutOfBoundsException(this.exceptionStringRange(endIndex, startIndex));
			}
			d = -1; //decrementing direction
		}

		int replaceCount = 0;
		final Object[] data = this.data;		
		for(int i = startIndex-d; i != endIndex;){
			if(JaArrays.contains(oldElements, (E)data[i+=d])){
				data[i] = newElement;
				replaceCount++;
			}
		}
		return replaceCount;
	}

	/**
	 * @param startIndex
	 * @param endIndex
	 * @param oldElements
	 * @param newElement
	 * @param equalator
	 * @return
	 * @see com.xdev.jadoth.collections.XSettingList#rngReplaceAll(int, int, E[], java.lang.Object, com.xdev.jadoth.lang.Equalator)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public int rngReplaceAll(
		final int startIndex,
		final int endIndex,
		final E[] oldElements,
		final E newElement,
		final Equalator<E> equalator
	)
	{
		final int d; //bi-directional index movement
		if(startIndex <= endIndex){
			if(startIndex < 0 || endIndex >= this.size){
				throw new IndexOutOfBoundsException(this.exceptionStringRange(startIndex, endIndex));
			}
			d = 1; //incrementing direction
		}
		else {
			if(startIndex >= this.size || endIndex < 0){
				throw new IndexOutOfBoundsException(this.exceptionStringRange(endIndex, startIndex));
			}
			d = -1; //decrementing direction
		}

		int replaceCount = 0;
		final Object[] data = this.data;		
		for(int i = startIndex-d; i != endIndex;){
			if(JaArrays.contains(oldElements, (E)data[i+=d], equalator)){
				data[i] = newElement;
				replaceCount++;
			}
		}
		return replaceCount;
	}

	/**
	 * @param startIndex
	 * @param endIndex
	 * @param oldElements
	 * @param newElement
	 * @return
	 * @see net.jadoth.collections.XSettingList#rngReplaceAll(int, int, XGettingCollection<E>, java.lang.Object)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public int rngReplaceAll(
		final int startIndex,
		final int endIndex,
		final XGettingCollection<E> oldElements,
		final E newElement
	)
	{
		final int d; //bi-directional index movement
		if(startIndex <= endIndex){
			if(startIndex < 0 || endIndex >= this.size){
				throw new IndexOutOfBoundsException(this.exceptionStringRange(startIndex, endIndex));
			}
			d = 1; //incrementing direction
		}
		else {
			if(startIndex >= this.size || endIndex < 0){
				throw new IndexOutOfBoundsException(this.exceptionStringRange(endIndex, startIndex));
			}
			d = -1; //decrementing direction
		}

		int replaceCount = 0;
		final Object[] data = this.data;		
		for(int i = startIndex-d; i != endIndex;){
			if(oldElements.containsId((E)data[i+=d])){
				data[i] = newElement;
				replaceCount++;
			}
		}
		return replaceCount;
	}

	/**
	 * @param startIndex
	 * @param endIndex
	 * @param oldElements
	 * @param newElement
	 * @param equalator
	 * @return
	 * @see com.xdev.jadoth.collections.XSettingList#rngReplaceAll(int, int, com.xdev.jadoth.collections.XGettingCollection, java.lang.Object, com.xdev.jadoth.lang.Equalator)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public int rngReplaceAll(
		final int startIndex,
		final int endIndex,
		final XGettingCollection<E> oldElements,
		final E newElement,
		final Equalator<E> equalator
	)
	{
		final int d; //bi-directional index movement
		if(startIndex <= endIndex){
			if(startIndex < 0 || endIndex >= this.size){
				throw new IndexOutOfBoundsException(this.exceptionStringRange(startIndex, endIndex));
			}
			d = 1; //incrementing direction
		}
		else {
			if(startIndex >= this.size || endIndex < 0){
				throw new IndexOutOfBoundsException(this.exceptionStringRange(endIndex, startIndex));
			}
			d = -1; //decrementing direction
		}

		int replaceCount = 0;
		final Object[] data = this.data;		
		for(int i = startIndex-d; i != endIndex;){
			if(oldElements.contains((E)data[i+=d], equalator)){
				data[i] = newElement;
				replaceCount++;
			}
		}
		return replaceCount;
	}

	/**
	 * @param startIndex
	 * @param endIndex
	 * @param oldElements
	 * @param newElement
	 * @param skip
	 * @param limit
	 * @return
	 * @see com.xdev.jadoth.collections.XSettingList#rngReplaceAll(int, int, E[], java.lang.Object, int, int)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public int rngReplaceAll(
		final int startIndex,
		final int endIndex,
		final E[] oldElements,
		final E newElement,
		int skip,
		final Integer limit
	)
	{
		if(skip < 0){
			throw new IllegalArgumentException(this.exceptionStringSkipNegative(skip));
		}

		final int d; //bi-directional index movement
		if(startIndex <= endIndex){
			if(startIndex < 0 || endIndex >= this.size){
				throw new IndexOutOfBoundsException(this.exceptionStringRange(startIndex, endIndex));
			}
			d = 1; //incrementing direction
		}
		else {
			if(startIndex >= this.size || endIndex < 0){
				throw new IndexOutOfBoundsException(this.exceptionStringRange(endIndex, startIndex));
			}
			d = -1; //decrementing direction
		}

		int lim = limit == null ?Integer.MAX_VALUE :limit.intValue();
		int replaceCount = 0;
		final Object[] data = this.data;
		int i = startIndex-d;

		while(skip != 0){
			if(JaArrays.contains(oldElements, (E)data[i+=d])){
				skip--;
			}
			if(i == endIndex){
				return 0;
			}
		}
		while(i != endIndex && lim > 0){
			if(JaArrays.contains(oldElements, (E)data[i+=d])){
				data[i] = newElement;
				replaceCount++;
				lim--;
			}
		}
		return replaceCount;
	}

	/**
	 * @param startIndex
	 * @param endIndex
	 * @param oldElements
	 * @param newElement
	 * @param skip
	 * @param limit
	 * @param equalator
	 * @return
	 * @see com.xdev.jadoth.collections.XSettingList#rngReplaceAll(int, int, E[], java.lang.Object, int, int, com.xdev.jadoth.lang.Equalator)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public int rngReplaceAll(
		final int startIndex,
		final int endIndex,
		final E[] oldElements,
		final E newElement,
		int skip,
		final Integer limit,
		final Equalator<E> equalator
	)
	{
		if(skip < 0){
			throw new IllegalArgumentException(this.exceptionStringSkipNegative(skip));
		}

		final int d; //bi-directional index movement
		if(startIndex <= endIndex){
			if(startIndex < 0 || endIndex >= this.size){
				throw new IndexOutOfBoundsException(this.exceptionStringRange(startIndex, endIndex));
			}
			d = 1; //incrementing direction
		}
		else {
			if(startIndex >= this.size || endIndex < 0){
				throw new IndexOutOfBoundsException(this.exceptionStringRange(endIndex, startIndex));
			}
			d = -1; //decrementing direction
		}

		int lim = limit == null ?Integer.MAX_VALUE :limit.intValue();
		final Object[] data = this.data;
		int replaceCount = 0;
		int i = startIndex-d;

		while(skip != 0){
			if(JaArrays.contains(oldElements, (E)data[i+=d], equalator)){
				skip--;
			}
			if(i == endIndex){
				return 0;
			}
		}
		while(i != endIndex && lim > 0){
			if(JaArrays.contains(oldElements, (E)data[i+=d], equalator)){
				data[i] = newElement;
				replaceCount++;
				lim--;
			}
		}
		return replaceCount;
	}

	/**
	 * @param startIndex
	 * @param endIndex
	 * @param oldElements
	 * @param newElement
	 * @param skip
	 * @param limit
	 * @return
	 * @see net.jadoth.collections.XSettingList#rngReplaceAll(int, int, XGettingCollection<E>, java.lang.Object, int, int)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public int rngReplaceAll(
		final int startIndex,
		final int endIndex,
		final XGettingCollection<E> oldElements,
		final E newElement,
		int skip,
		final Integer limit
	)
	{
		if(skip < 0){
			throw new IllegalArgumentException(this.exceptionStringSkipNegative(skip));
		}

		final int d; //bi-directional index movement
		if(startIndex <= endIndex){
			if(startIndex < 0 || endIndex >= this.size){
				throw new IndexOutOfBoundsException(this.exceptionStringRange(startIndex, endIndex));
			}
			d = 1; //incrementing direction
		}
		else {
			if(startIndex >= this.size || endIndex < 0){
				throw new IndexOutOfBoundsException(this.exceptionStringRange(endIndex, startIndex));
			}
			d = -1; //decrementing direction
		}

		int lim = limit == null ?Integer.MAX_VALUE :limit.intValue();
		int replaceCount = 0;
		final Object[] data = this.data;
		int i = startIndex-d;

		while(skip != 0){
			if(oldElements.containsId((E)data[i+=d])){
				skip--;
			}
			if(i == endIndex){
				return 0;
			}
		}
		while(i != endIndex && lim > 0){
			if(oldElements.containsId((E)data[i+=d])){
				data[i] = newElement;
				replaceCount++;
				lim--;
			}
		}
		return replaceCount;
	}

	/**
	 * @param startIndex
	 * @param endIndex
	 * @param oldElements
	 * @param newElement
	 * @param skip
	 * @param limit
	 * @param equalator
	 * @return
	 * @see com.xdev.jadoth.collections.XSettingList#rngReplaceAll(int, int, com.xdev.jadoth.collections.XGettingCollection, java.lang.Object, int, int, com.xdev.jadoth.lang.Equalator)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public int rngReplaceAll(
		final int startIndex,
		final int endIndex,
		final XGettingCollection<E> oldElements,
		final E newElement,
		int skip,
		final Integer limit,
		final Equalator<E> equalator
	)
	{
		if(skip < 0){
			throw new IllegalArgumentException(this.exceptionStringSkipNegative(skip));
		}

		final int d; //bi-directional index movement
		if(startIndex <= endIndex){
			if(startIndex < 0 || endIndex >= this.size){
				throw new IndexOutOfBoundsException(this.exceptionStringRange(startIndex, endIndex));
			}
			d = 1; //incrementing direction
		}
		else {
			if(startIndex >= this.size || endIndex < 0){
				throw new IndexOutOfBoundsException(this.exceptionStringRange(endIndex, startIndex));
			}
			d = -1; //decrementing direction
		}

		int lim = limit == null ?Integer.MAX_VALUE :limit.intValue();
		final Object[] data = this.data;
		int replaceCount = 0;
		int i = startIndex-d;

		while(skip != 0){
			if(oldElements.contains((E)data[i+=d], equalator)){
				skip--;
			}
			if(i == endIndex){
				return 0;
			}
		}
		while(i != endIndex && lim > 0){
			if(oldElements.contains((E)data[i+=d], equalator)){
				data[i] = newElement;
				replaceCount++;
				lim--;
			}
		}
		return replaceCount;
	}

	/**
	 * @param startIndex
	 * @param endIndex
	 * @param predicate
	 * @param newElement
	 * @return
	 * @see com.xdev.jadoth.collections.XSettingList#rngReplaceOne(int, int, com.xdev.jadoth.lang.functional.Predicate, java.lang.Object)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public int rngReplaceOne(
		final int startIndex,
		final int endIndex,
		final Predicate<E> predicate,
		final E newElement
	)
	{
		final int d; //bi-directional index movement
		if(startIndex <= endIndex){
			if(startIndex < 0 || endIndex >= this.size){
				throw new IndexOutOfBoundsException(this.exceptionStringRange(startIndex, endIndex));
			}
			d = 1; //incrementing direction
		}
		else {
			if(startIndex >= this.size || endIndex < 0){
				throw new IndexOutOfBoundsException(this.exceptionStringRange(endIndex, startIndex));
			}
			d = -1; //decrementing direction
		}

		final Object[] data = this.data;
		for(int i = startIndex-d; i != endIndex;){
			if(predicate.apply((E)data[i+=d])){
				data[i] = newElement;
				return i;
			}
		}
		return -1;
	}

	/**
	 * @param startIndex
	 * @param endIndex
	 * @param predicate
	 * @param newElement
	 * @return
	 * @see com.xdev.jadoth.collections.XSettingList#rngReplaceOne(int, int, com.xdev.jadoth.lang.functional.controlflow.TPredicate, java.lang.Object)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public int rngReplaceOne(
		final int startIndex,
		final int endIndex,
		final TPredicate<E> predicate,
		final E newElement
	)
	{
		final int d; //bi-directional index movement
		if(startIndex <= endIndex){
			if(startIndex < 0 || endIndex >= this.size){
				throw new IndexOutOfBoundsException(this.exceptionStringRange(startIndex, endIndex));
			}
			d = 1; //incrementing direction
		}
		else {
			if(startIndex >= this.size || endIndex < 0){
				throw new IndexOutOfBoundsException(this.exceptionStringRange(endIndex, startIndex));
			}
			d = -1; //decrementing direction
		}

		final Object[] data = this.data;
		int i = startIndex-d;
		while(i != endIndex){
			try{
				while(i != endIndex){
					if(predicate.apply((E)data[i+=d])){
						data[i] = newElement;
						return i;
					}
				}
			}
			catch(final ThrowBreak b)   { break; }
			catch(final ThrowReturn r)  { return -1; }
			catch(final ThrowContinue c){ /*Nothing*/ }
		}
		return -1;
	}

	/**
	 * @param <C>
	 * @param targetCollection
	 * @return
	 * @see com.xdev.jadoth.collections.XGettingCollection#distinct(com.xdev.jadoth.collections.Collecting)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public <C extends Collecting<E>> C distinct(final C targetCollection)
	{
		final IdentityHashMap<Object, Object> elements = new IdentityHashMap<Object, Object>();
		final Object[] data = this.data;
		for(final Object e : data) {
			if(elements.containsKey(e)) continue;
			elements.put(e, null);
			targetCollection.add((E)e);
		}
		return targetCollection;
	}

	/**
	 * @param <C>
	 * @param targetCollection
	 * @param equalator
	 * @return
	 * @see com.xdev.jadoth.collections.XGettingCollection#distinct(com.xdev.jadoth.collections.Collecting, com.xdev.jadoth.lang.Equalator)
	 */
	@Override
	public <C extends Collecting<E>> C distinct(final C targetCollection, final Equalator<E> equalator)
	{
		// TODO distinct() when XSet is available
		return null;
	}



	///////////////////////////////////////////////////////////////////////////
	// inner classes //
	//////////////////

	class Itr implements Iterator<E>
	{
		int index;

		public Itr()
		{
			super();
			this.index = 0;
		}

		/**
		 * @param index
		 */
		public Itr(final int index)
		{
			super();
			this.index = index;
		}

		/**
		 * @return
		 */
		@Override
		public boolean hasNext()
		{
			return this.index < GrowList.this.size;
		}

		/**
		 * @return
		 */
		@SuppressWarnings("unchecked")
		@Override
		public E next()
		{
			return (E)GrowList.this.data[this.index++];
		}

		/**
		 *
		 */
		@Override
		public void remove()
		{
			GrowList.this.remove(this.index--);
		}

	}

	class ListItr extends Itr implements ListIterator<E>
	{
		/**
		 *
		 */
		public ListItr()
		{
			super();
		}

		/**
		 * @param index
		 */
		public ListItr(final int index)
		{
			super(index);
		}

		/**
		 * @param e
		 */
		@Override
		public void add(final E e)
		{
			GrowList.this.add(e);
		}

		/**
		 * @return
		 */
		@Override
		public boolean hasPrevious()
		{
			return this.index > 0;
		}

		/**
		 * @return
		 */
		@Override
		public int nextIndex()
		{
			return this.index;
		}

		/**
		 * @return
		 */
		@SuppressWarnings("unchecked")
		@Override
		public E previous()
		{
			return (E)GrowList.this.data[--this.index];
		}

		/**
		 * @return
		 */
		@Override
		public int previousIndex()
		{
			return this.index - 1;
		}

		/**
		 * @param e
		 */
		@Override
		public void set(final E e)
		{
			GrowList.this.set(this.index, e);
		}

	}

	public static class Factory<E> implements XList.Factory<E>
	{
		///////////////////////////////////////////////////////////////////////////
		// instance fields //
		////////////////////

		private int initialCapacity;



		///////////////////////////////////////////////////////////////////////////
		// constructors //
		/////////////////

		/**
		 * @param initialCapacity
		 */
		public Factory(final int initialCapacity)
		{
			super();
			this.initialCapacity = initialCapacity;
		}

		/**
		 * @return
		 */
		@Override
		public GrowList<E> createInstance()
		{
			return new GrowList<E>(this.initialCapacity);
		}

	}

}
