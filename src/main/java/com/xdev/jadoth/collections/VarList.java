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

import java.util.Comparator;
import java.util.LinkedList;
import java.util.RandomAccess;

import com.xdev.jadoth.util.VarChar;


/**
 * Variable list implementation that is based on the LinkedList concept and extended by indexing and precaching of
 * entries to gain fast {@link RandomAccess} and increasing behaviour.
 * <p>
 * Settings for indexing and caching are highly customizable and quickly changeable during the lifetime of a
 * {@link VarList} instance to always achieve high performance in every situation.
 * <p>
 * The {@link VarList} implementation outperforms {@link LinkedList} in almost every situation.
 * <p>
 * <b>Comparison to {@link GrowList}</b>:
 * <ul>
 * <li>If enough initial capacity is reserved and no add~(index) or remove~() operations are needed,
 * {@link GrowList} is significantly faster than {@link VarList}.</li>
 * <li>{@link VarList} needs about 3 times as much memory as {@link GrowList} or more depending on the chosen
 * index and cache size </li>
 * <li>If heavy inserting/removing of elements is needed or if the type of use is unknown,
 * {@link VarList} is a good general purpose list implementation.</li>
 * </ul>
 *
 * @author Thomas Muenz
 *
 */
public final class VarList<E> implements VarChar.Appendable //XList<E>
{
	///////////////////////////////////////////////////////////////////////////
	// constants        //
	/////////////////////

	private static final int DEFAULT_INDEX_SIZE = 32;
	private static final int DEFAULT_INDEX_SPAN_EXPONENT = 2; //index span: every index entry stands for 4 entries (2^n)
	private static final int DEFAULT_INDEX_GROWTH_INDICATOR_EXPONENT = 5; //indexSize = one 32th of list size (2^5 = 32)
	private static final int DEFAULT_INDEX_MAX_SPAN_EXPONENT = 10; //max index span = 2^30 = 1 Bil (=~ turn off index)

	private static final int DEFAULT_CACHE_CAPACITY = 32;
	private static final int DEFAULT_CACHE_SIZE = 32;



	public static final class Configuration
	{
		private Configuration(){}



		///////////////////////////////////////////////////////////////////////////
		// static fields //
		//////////////////

		/**
		 * This threshold will probably be hardware dependant
		 */
		private static int DEFAULT_INLINE_SORT_THRESHOLD = 10000;
		private static char DEFAULT_LIST_SEPERATOR_CHAR = ',';
		private static String DEFAULT_LIST_SEPERATOR_STRING = ", ";



		///////////////////////////////////////////////////////////////////////////
		// getters          //
		/////////////////////

		public static int getDefaultInlineSortThreshold()
		{
			return DEFAULT_INLINE_SORT_THRESHOLD;
		}

		public static String getDefaultListSeperatorString()
		{
			return DEFAULT_LIST_SEPERATOR_STRING;
		}

		public static char getDefaultListSeperatorChar()
		{
			return DEFAULT_LIST_SEPERATOR_CHAR;
		}



		///////////////////////////////////////////////////////////////////////////
		// setters          //
		/////////////////////

		public static void setInlineSortThreshold(final int threshold)
		{
			DEFAULT_INLINE_SORT_THRESHOLD = threshold;
		}

		public static void setDefaultListSeperatorString(final String defaultListSeperatorString)
		{
			DEFAULT_LIST_SEPERATOR_STRING = defaultListSeperatorString;
		}

		public static void setDefaultListSeperatorChar(final char defaultListSeperatorChar)
		{
			DEFAULT_LIST_SEPERATOR_CHAR = defaultListSeperatorChar;
		}

	}



	///////////////////////////////////////////////////////////////////////////
	// instance fields //
	////////////////////


	// list core fields
	private final Entry head = new Entry();
	private int size;

	// index fields
	private Entry[] entryIndex;
	private boolean trivialIndex;
	private int indexSize = 0;
	private int indexSpanExponent;
	private int indexSpanModulo;
	private int currentMaxListSize;
	private int indexGrowthIndicator = DEFAULT_INDEX_GROWTH_INDICATOR_EXPONENT;
	private int maxIndexSpanExponent = DEFAULT_INDEX_MAX_SPAN_EXPONENT;

	private int nextGetIndex = -30091981;
	private Entry nextGetEntry = null;

	// cache fields
	private Entry[] cache;
	private int cacheSize;

	// string stuff
	private String listSeperatorString = Configuration.getDefaultListSeperatorString();
	private char listSeperatorChar = Configuration.getDefaultListSeperatorChar();

	private int inlineSortThreshold = Configuration.getDefaultInlineSortThreshold();



	///////////////////////////////////////////////////////////////////////////
	// constructors     //
	/////////////////////

	public VarList()
	{
		super();
		this.initializeCache(DEFAULT_CACHE_CAPACITY, DEFAULT_CACHE_SIZE);
		this.initializeIndex(DEFAULT_INDEX_SIZE, DEFAULT_INDEX_SPAN_EXPONENT);
	}

	public VarList(final int initialCacheSize)
	{
		super();
		this.initializeCache(initialCacheSize>0 ?initialCacheSize :0, initialCacheSize>0 ?initialCacheSize :0);
		this.initializeIndex(DEFAULT_INDEX_SIZE, DEFAULT_INDEX_SPAN_EXPONENT);
	}

	public VarList(
		final int initialCacheSize,
		final int indexSize,
		final int indexSpanExponent
	)
	{
		super();
		this.initializeCache(initialCacheSize>0 ?initialCacheSize :0, initialCacheSize>0 ?initialCacheSize :0);
		this.initializeIndex(indexSize, indexSpanExponent);
	}

	{
		this.initializeHead();
	}

	private void initializeHead()
	{
		this.head.next = this.head.prev = this.head;
	}



	///////////////////////////////////////////////////////////////////////////
	// getters          //
	/////////////////////

	/**
	 * @return the listSeperatorString
	 */
	public String getListSeperatorString()
	{
		return this.listSeperatorString;
	}
	/**
	 * @return the listSeperatorChar
	 */
	public char getListSeperatorChar()
	{
		return this.listSeperatorChar;
	}



	///////////////////////////////////////////////////////////////////////////
	// setters          //
	/////////////////////

	/**
	 * @param listSeperatorString the listSeperatorString to set
	 */
	public VarList<E> setListSeperatorString(final String listSeperatorString)
	{
		this.listSeperatorString = listSeperatorString;
		return this;
	}
	/**
	 * @param listSeperatorChar the listSeperatorChar to set
	 */
	public VarList<E> setListSeperatorChar(final char listSeperatorChar)
	{
		this.listSeperatorChar = listSeperatorChar;
		return this;
	}



	///////////////////////////////////////////////////////////////////////////
	// declared methods //
	/////////////////////

	private void initializeIndex(int indexSize, int indexSpanExponent)
	{
		if(indexSpanExponent > this.maxIndexSpanExponent){
			indexSpanExponent = this.maxIndexSpanExponent;
		}
		if(indexSize < 1){
			indexSize = 1;
		}
		this.entryIndex = new Entry[indexSize];
		this.indexSpanExponent = indexSpanExponent;
		this.indexSpanModulo = (1<<indexSpanExponent)-1;
		this.currentMaxListSize = indexSize << indexSpanExponent;
		this.trivialIndex = indexSize == 1;
	}

	private void initializeCache(final int cacheCapacity, final int cacheSize)
	{
		final Entry[] cache = new Entry[cacheCapacity];
		for(int i = 0; i < cacheSize; i++){
			cache[i] = new Entry();
		}
		this.cache = cache;
		this.cacheSize = cacheSize;
	}




	/**
	 * @return
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		if(this.size == 0){
			return "[]";
		}
		return this.appendTo(new VarChar(this.size*16).append('[')).append(']').toString();
	}

	/**
	 * Appends all of this list's elements to the passed {@link VarChar}, seperated by
	 * the list seperator string ({@link #setListSeperatorString(String)}, {@link #getListSeperatorString()})
	 * or if it is <tt>null</tt>, by the list seperator <tt>char</tt> ({@link #setListSeperatorChar(char)},
	 * {@link #getListSeperatorChar()}).<br>
	 * Note that using the single <tt>char</tt> as seperator is faster than using a {@link String}.
	 *
	 * @param vc the StringBuilder to append to.
	 * @throws NullPointerException if the passed {@link VarChar} object is <tt>null</tt>.
	 */
	@Override
	public VarChar appendTo(final VarChar vc) throws NullPointerException
	{
		if(this.size == 0) return vc;

		Entry e = this.entryIndex[0];
		vc.append(e.value);

		Object value;
		if(this.listSeperatorString != null){
			final char[] sepp = this.listSeperatorString.toCharArray();
			while((e = e.next) != null){
				value = e.value;
				vc.append(sepp).append(value == this ?super.toString() :value);
			}
		}
		else {
			final char sepp = this.listSeperatorChar;
			while((e = e.next) != null){
				value = e.value;
				vc.append(sepp).append(value == this ?super.toString() :value);
			}
		}
		return vc;
	}



	/**
	 * Note that an invalid index value will cause a runtime exception either of the type
	 * {@link ArrayIndexOutOfBoundsException} when accessing the index or an {@link NullPointerException}
	 * when trying to scrolling to an entry that doesn't exist.<br>
	 * In either case, the information provided by the exception will not be of much use. Still, the
	 * calling context knows the value of the index it has passed to this method, so no information is lost.
	 *
	 * @param index
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public E get(final int index)
	{
		if(this.nextGetIndex == index){
			//sequential index access optimisation
			final Entry e = this.nextGetEntry;
			if(e.next != null){
				this.nextGetIndex++;
				this.nextGetEntry = e.next;
			}
			else {
				this.nextGetIndex = 0;
				this.nextGetEntry = this.head.next;
			}
			return (E)e.value;
		}
		else if(index >= this.size){
			// unnecessary but java.util.List-conform index checking
			throw new IndexOutOfBoundsException(Integer.toString(index));
		}

		// index check is not required here as any invalid index will cause an exception later without harm
		Entry hopEntry = this.entryIndex[index>>this.indexSpanExponent];
		for(int hops = index & this.indexSpanModulo; hops --> 0;){
			hopEntry = hopEntry.next;
		}

		if(hopEntry.next != null){
			this.nextGetIndex = index+1;
			this.nextGetEntry = hopEntry.next;
		}

		return (E)hopEntry.value;
	}

	/**
	 * In principle the same as {@link #get(int)}, only difference is that no sequential index access optimisation is
	 * done and invalid index values cause different types of exceptions instead of the standard
	 * {@link IndexOutOfBoundsException}.<br>
	 * Still, it is ensured that an invalid index value will cause an exception.
	 * <p>
	 * Prefer this method over {@link #get(int)} for unsequential (i.e. randomized) accesses.
	 *
	 * @param index
	 * @return the value
	 * @throws ArrayIndexOutOfBoundsException if index is negative or >= size()
	 * @throws NullPointerException if index is >= size()
	 */
	@SuppressWarnings("unchecked")
	public E peek(final int index)
	{
		// index check is not required here as any invalid index will cause an exception later without harm
		Entry hopEntry = this.entryIndex[index>>this.indexSpanExponent];
		for(int hops = index & this.indexSpanModulo; hops --> 0;){
			hopEntry = hopEntry.next;
		}
		return (E)hopEntry.value;
	}


	private Entry newEntry()
	{
		if(this.cacheSize == 0){
			return new Entry();
		}
		final Entry e = this.cache[--this.cacheSize];
		this.cache[this.cacheSize] = null;
		return e;
	}


	@SuppressWarnings("unchecked")
	public E prepend(final E element)
	{
		// update list size and index size if needed
		if(this.size == this.currentMaxListSize){
			this.enlargeIndex();
		}
		this.size++; //increment size only if index enlarging did not throw an exception

		// create entry and join in on the head of the list
		final Entry entry = this.newEntry();
		entry.value = element;
		entry.prev = this.head;
		this.head.next = entry;

		// update index for first entry
		final Entry[] entryIndex = this.entryIndex;
		entryIndex[0] = entry; //harcoded array index access yields better performance

		// store index size for special case check and later use for index update
		final int indexSize = this.indexSize;

		// special case: if this was the first element, set index size to one and return null
		if(indexSize == 0){
			this.indexSize = 1;
			return null;
		}

		// join in with so far first element in normal case
		entry.next = this.head.next;
		entry.next.prev = entry;

		// no trivial index case handling needed here because: if index is trivial but not size 0, it's already size 1.

		// update all existing indexed entries AFTER the one responsible for the new entry (thus pre-increment)

		int indexIndex = 0;
		while(++indexIndex < indexSize){
			entryIndex[indexIndex] = entryIndex[indexIndex].prev; //one read access, one write acces
		}

		// if the new size exceeds the index span by exactely one, a new index entry is required
		if((this.size & this.indexSpanModulo) == 1){
			// the enlargement check above ensured that in this case, the index now has spare room (pretty cool)
			entryIndex[indexIndex] = this.head.prev; //indexIndex is always indexSize+1 at this point
			this.indexSize++; //faster?: "this.indexSize = indexIndex"
		}
		return (E)entry.next.value;
	}

	public void add(final E element)
	{
		// update list size and index size if necessary
		final int elementIndex = this.size;
		if(elementIndex == this.currentMaxListSize){
			this.enlargeIndex();
		}
		this.size++; //increment size only if index enlarging did not throw an exception

		// create entry and join in on the head of the list
		final Entry entry = this.newEntry();
		entry.value = element;
		entry.prev = this.head.prev;
		this.head.prev = entry;
		entry.prev.next = entry;
//		entry.next = head; //never reference head in forward direction

		// keep index updating simple and fast for the trivial case
		if(this.trivialIndex){
			if(this.entryIndex[0] == null){
				this.entryIndex[0] = entry;
				this.indexSize = 1;
			}
			return;
		}

		// update entryIndex if necessary (doesn't happen very often, so spare the temporary variable)
		if(this.entryIndex[elementIndex>>this.indexSpanExponent] == null){
			this.entryIndex[elementIndex>>this.indexSpanExponent] = entry;
			this.indexSize++;
		}
	}



	public void add(final int index, final E element)
	{
		if(index >= this.size){
			if(index > this.size){ //make unnecessary List-conform bounds check in here only
				throw new IndexOutOfBoundsException();
			}
			this.add(element); //to handle the "append" special case (index == size) correctly
			return;
		}

		// update list size and index size if needed
		if(this.size == this.currentMaxListSize){
			this.enlargeIndex();
		}

		// locate current entry at that index
		int indexIndex = index>>this.indexSpanExponent;
		Entry hopEntry = this.entryIndex[indexIndex];
		for(int hops = index & this.indexSpanModulo; hops --> 0;){
			hopEntry = hopEntry.next;
		}

		//join in new entry
		final int cacheSize = this.cacheSize;
		final Entry entry = cacheSize == 0 ? new Entry() :this.cache[--this.cacheSize];
		entry.prev = hopEntry.prev; //adding to this.size+1 will throw an NPE here (intentionally)
		entry.next = hopEntry;
		entry.prev.next = entry;
		hopEntry.prev = entry;

		// setting element, new size and removing cached entry not until valid insert is ensured!
		entry.value = element;
		this.size++;
		if(cacheSize > 0){
			this.cache[cacheSize] = null;
		}

		// set new entry as indexed entry if necessary
		final Entry[] entryIndex = this.entryIndex; //must be assigned AFTER enlargeIndex()!
		if((index & this.indexSpanModulo) == 0){
			entryIndex[indexIndex] = entry;
		}

		// update sequential access index if necessary
		if(index <= this.nextGetIndex){
			this.nextGetIndex++;
		}

		// update all existing indexed entries AFTER the one responsible for the new entry (thus pre-increment)
		//OLD version without indexSize helper variable
//		for(final int entryIndexLength = entryIndex.length; ++indexIndex < entryIndexLength;){
//			if((hopEntry = entryIndex[indexIndex]) == null){
//				// check if a new index entry is required: if size exceed span by exactely 1, index that last entry
//				if((this.size & this.indexSpanModulo) == 1){
//					entryIndex[indexIndex] = this.head.prev;
//				}
//				break; //as this was the last index entry, there's no need to iterate to the end of the index array
//			}
//			entryIndex[indexIndex] = hopEntry.prev;
//		}

		// update all existing indexed entries AFTER the one responsible for the new entry (thus pre-increment)
		final int indexSize = this.indexSize;
		while(++indexIndex < indexSize){
			entryIndex[indexIndex] = entryIndex[indexIndex].prev; //one read access, one write acces
		}

		// if the new size exceeds the index span by exactely one, a new index entry is required
		if((this.size & this.indexSpanModulo) == 1){
			// the enlargement check above ensured that in this case, the index now has spare room (pretty cool)
			entryIndex[indexIndex] = this.head.prev; //indexIndex is always indexSize+1 at this point
			this.indexSize++;
		}
	}

	@SuppressWarnings("unchecked")
	public E set(final int index, final E element) throws IndexOutOfBoundsException
	{
		if(index >= this.size){ //unnecessary List-conform bounds check
			throw new IndexOutOfBoundsException(Integer.toString(index));
		}

		// index check is not required here as any invalid index will cause an exception later without harm
		Entry hopEntry = this.entryIndex[index>>this.indexSpanExponent];
		for(int hops = index & this.indexSpanModulo; hops --> 0;){
			hopEntry = hopEntry.next;
		}
		final E oldValue = (E)hopEntry.value;
		hopEntry.value = element;
		return oldValue;
	}


	private void enlargeIndex()
	{
		final Entry[] entryIndex = this.entryIndex;
		final int indexLength = entryIndex.length;
		final int idxExp = this.indexSpanExponent;

		if(1<<idxExp<<this.indexGrowthIndicator >= indexLength || idxExp == this.maxIndexSpanExponent){
			// case 1: grow index size (by factor of 2)
			this.entryIndex = new Entry[indexLength<<1]; //provoke Exception if indexLength is 1<<30 (=2^31)
			System.arraycopy(entryIndex, 0, this.entryIndex, 0, indexLength);
			if(this.trivialIndex){
				this.trivialIndex = false;
			}
		}
		else {
			// case 2: grow spanExponent (by factor of 2)
			this.indexSpanExponent++; // exponent can never grow beyond maxIndexSpanExponent in here
			this.indexSpanModulo = (this.indexSpanModulo<<1) + 1;

			int i = 1; //leave out first index entry as it always points to the very first entry
			// half the index entry count but double their segment count (by assigning the doubled so far indexed entry)
			for(final int halfIndexIndex = indexLength>>1; i < halfIndexIndex; i++){
				entryIndex[i] = entryIndex[i<<1]; //nice: null references don't cause any harm here
			}
			this.indexSize = i;
			while(i < indexLength){ // clear the rest of the index array
				entryIndex[i++] = null;
			}
		}
		this.currentMaxListSize <<= 1; //either case has increased max size by a factor of 2
	}


	/**
	 * Required to rebuild the index after operations where updating the index on each change makes no sense,
	 * i.e. sorting or adding, inserting, removing of large numbers of data.
	 *
	 */
	private void rebuildIndex()
	{
		// preliminary special inlined simplified version of enlargeIndex()
		final int size = this.size;
		int indexLength = this.entryIndex.length;
		while(size > this.currentMaxListSize){
			final int idxExp = this.indexSpanExponent;
			if(1<<idxExp<<this.indexGrowthIndicator >= indexLength || idxExp == this.maxIndexSpanExponent){
				// case 1: grow index size (by factor of 2)
				if((indexLength <<= 1) > size){ //only allocate new index array if new length is long enough
					this.entryIndex = new Entry[indexLength<<1]; //provoke Exception if indexLength is 1<<30 (=2^31)
					// no index entry copiing needed as it will be rebuilt anyway
					if(this.trivialIndex){
						this.trivialIndex = false;
					}
				}
			}
			else {
				// case 2: grow spanExponent (by factor of 2)
				this.indexSpanExponent++; // exponent can never grow beyond maxIndexSpanExponent in here
				this.indexSpanModulo = (this.indexSpanModulo<<1) + 1;
			}
			this.currentMaxListSize <<= 1; //either case has increased max size by a factor of 2
		}

		// actual index rebuild
		final Entry[] entryIndex = this.entryIndex;
		final int m = this.indexSpanModulo;
		int j = 0;
		Entry e = this.head;
		for(int i = 0; (e = e.next) != null; i++){
			if((i & m) == 0){ //each entry whose i(ndex) fits exactely to the index span is an indexed entry
				entryIndex[j++] = e;
			}
		}
		this.indexSize = j;
		for(final int indexSize = this.entryIndex.length; j < indexSize; j++){
			entryIndex[j] = null; //the rest of the index buckets are free
		}
	}

	public void cacheEnsureSize(final int minimalCacheSize)
	{

	}
	public void cacheSetSize(final int minimalCacheSize)
	{

	}
	public void cacheClear()
	{
		final Entry[] cache = this.cache;
		for(int i = cache.length; i --> 0;){
			cache[i] = null;
		}
		this.cacheSize = 0;
	}
	public void cacheTruncate()
	{
		this.cache = new Entry[DEFAULT_CACHE_CAPACITY];
		this.cacheSize = 0;
	}


	public int size()
	{
		return this.size;
	}


	private void releaseIntermediateEntry(final Entry entry)
	{
		// disjoin from list
		entry.prev.next = entry.next;
		entry.next.prev = entry.prev; //intermediate entries always have a next entry (as opposed to the last entry)

		if(this.cacheSize < this.cache.length){
			//return entry back to the pool
			this.cache[this.cacheSize++] = entry;
			//reset the entry and enable GC to collect the stuff entry references so far
			entry.value = null;
			entry.next = null;
			entry.prev = null;
		}
		//otherwise just let the nwo unrefernced entry lay around and wait for GC to kill it
	}


	@SuppressWarnings("unchecked")
	public E removeFirst()
	{
		final Entry[] entryIndex = this.entryIndex;

		// update ALL index entries
		final int indexSize = this.indexSize;
		if(indexSize == 0){ //empty index ALWAYS means empty list, even for trivial index case
			throw new IndexOutOfBoundsException("0");
		}

		// update all index entries (except last one special case)
		final int lastIndexEntryIndex = indexSize-1;
		for(int i = 0; i < lastIndexEntryIndex; i++){
			entryIndex[i] = entryIndex[i].next; //this is also correct if the index entry is the very last list entry
		}

		// last index entry special case
		if((this.size-- & this.indexSpanModulo) == 0){
			// if size so far was "even" in terms of the index span, then the last index entry has to be removed
			entryIndex[--this.indexSize] = null;
		}
		else {
			// normal case: same as for other indexed entries
			entryIndex[lastIndexEntryIndex] = entryIndex[lastIndexEntryIndex].next;
		}

		// finally disjoin and remove the first entry
		final Entry entryToRemove = this.head.next; //head still knows the "still first" entry
		final E value = (E)entryToRemove.value;
		this.releaseIntermediateEntry(entryToRemove); //first entry is intermediate beteween head and second entry
		return value;
	}

	@SuppressWarnings("unchecked")
	public E removeLast()
	{
		if(this.size == 0){
			throw new IndexOutOfBoundsException();
		}

		// last index entry special case
		if((this.size-- & this.indexSpanModulo) == 0){
			// if size so far was "even" in terms of the index span, then the last index entry has to be removed
			this.entryIndex[--this.indexSize] = null;
		}
		else {
			// normal case: right shift the last index entry
			final int lastIndexEntryIndex = this.indexSize-1;
			this.entryIndex[lastIndexEntryIndex] = this.entryIndex[lastIndexEntryIndex].next;
		}

		final Entry entryToRemove = this.head.prev; //head still knows the "still first" entry
		final E value = (E)entryToRemove.value;
		entryToRemove.prev.next = null; //head is never referenced in forward direction
		this.head.prev = entryToRemove.prev;

		if(this.cacheSize < this.cache.length){
			//return entry back to the pool
			this.cache[this.cacheSize++] = entryToRemove;
			//reset the entry and enable GC to collect the stuff entry references so far
			entryToRemove.value = null;
//			entryToRemove.next = null; //must have been null anyway
			entryToRemove.prev = null;
		}
		//otherwise just let the nwo unrefernced entry lay around and wait for GC to kill it

		return value;
	}

	@SuppressWarnings("unchecked")
	public E remove(final int index)
	{
		if(index == 0){
			return this.removeFirst(); //does the bounds check itself
		}
		else if(index+1 >= this.size){
			if(index >= this.size){
				throw new IndexOutOfBoundsException(Integer.toString(index));
			}
			return this.removeLast();
		}
		// index being actually intermediate is ensured here

		// determine index entry index for finding the entry to remove and for later index update
		final int indexIndex = index>>this.indexSpanExponent;

		// find the entry to be removed
		final Entry[] entryIndex = this.entryIndex;
		Entry hopEntry = entryIndex[indexIndex];
		final int neededHops = index & this.indexSpanModulo;
		for(int hops = neededHops; hops --> 0;){
			hopEntry = hopEntry.next;
		}
		final Entry entryToRemove = hopEntry; // remember the found to-be-removed entry for later removal

		/* update the index entry responsible for the to-be-removed entry
		 * if exactely one hop was needed earlier, then skip the to-be-removed entry
		 */
		entryIndex[indexIndex] = neededHops == 1 ?entryToRemove.next :entryIndex[indexIndex].next;

		// update all remaining index entries (except last one special case)
		final int lastIndexEntryIndex = this.indexSize-1;
		for(int i = indexIndex+1; i < lastIndexEntryIndex; i++){
			entryIndex[i] = entryIndex[i].next; //this is also correct if the index entry is the very last list entry
		}

		// last index entry special case
		if((this.size-- & this.indexSpanModulo) == 0){
			// if size so far was "even" in terms of the index span, then the last index entry has to be removed
			entryIndex[--this.indexSize] = null;
		}
		else {
			// normal case: same as for other indexed entries
			entryIndex[lastIndexEntryIndex] = entryIndex[lastIndexEntryIndex].next;
		}

		final E oldValue = (E)entryToRemove.value;
		this.releaseIntermediateEntry(entryToRemove); //remove entry AFTER the index update, because index update check may need it.
		return oldValue;
	}






	@SuppressWarnings("unchecked")
	public E getFirst()
	{
		return (E)this.head.next.value;
	}
	@SuppressWarnings("unchecked")
	public E getLast()
	{
		return (E)this.head.prev.value;
	}

	public void setFirst(final E element)
	{
		this.head.next.value = element;
	}
	public void setLast(final E element)
	{
		this.head.prev.value = element;
	}


	/**
	 * Note: setting index size to one before and back to some higher value after the call to this method can speed
	 * up the operation.
	 *
	 * @param elements
	 * @return
	 */
	public VarList<E> addIterable(final Iterable<? extends E> elements)
	{
		for(final E t : elements) {
			//can't foresee elements' size, so do it the hard way
			this.add(t);
		}
		return this;
	}

	/**
	 * Note: setting index size to one before and back to some higher value after the call to this method can speed
	 * up the operation.
	 *
	 * @param index
	 * @param elements
	 * @return
	 */
	public VarList<E> insertIterable(int index, final Iterable<? extends E> elements)
	{
		for(final E t : elements) {
			//can't foresee elements' size, so do it the hard way
			this.add(index++, t);
		}
		return this;
	}


	/**
	 * Special version of Mergesort that sorts the list in place without any additional memory allocation or
	 * recursion stacktrace.<br>
	 * Performance comparison to normal Mergesort with array allocation:
	 * <ul>
	 * <li>Considerably faster for small list sizes.</li>
	 * <li>Considerably slower for large list sizes.</li>
	 * </ul>
	 * (where "small" and "large" depends on hardware. Threshold was 10000 in tests.)
	 *
	 * @param comparator
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public VarList<E> sortInPlace(final Comparator<E> comparator)
	{
		if(this.size <= 1) return this;
		this.mergeSortInPlace((Comparator<Object>)comparator);
		this.rebuildIndex();
		return this;
	}



	@SuppressWarnings("null")
	private void mergeSortInPlace(final Comparator<Object> comparator)
	{
		int listSize = 1, numMerges, leftSize, rightSize;
		Entry first = this.head.next, tail, left, right, next;

		do { //for each power of two<=list length
			numMerges = 0;
			left = first;
			tail = first = null; //start at the start

			while(left != null) { //do this list_len/listSize times:
				numMerges++;
				right = left;
				leftSize = 0;
				rightSize = listSize;

				// cut list into two halves (but don't overrun)
				while(right != null && leftSize < listSize){
					leftSize++;
					right = right.next;
				}
				// run through the lists appending onto what has already been sorted
				while(leftSize > 0 || rightSize > 0 && right != null) {
					// left empty, take right OR Right empty, take left, OR compare.
					if(leftSize == 0){
						next = right;
						right = right.next; //potential NPE warning is wrong here
						rightSize--;
					}
					else if(rightSize == 0 || right == null){
						next = left;
						left = left.next;
						leftSize--;
					}
					else if (comparator.compare(left.value, right.value)<0) {
						next = left;
						left = left.next;
						leftSize--;
					}
					else{
						next = right;
						right = right.next;
						rightSize--;
					}
					// update pointers to keep track of current entry:
					if(tail != null){
						tail.next = next;
					}
					else {
						first = next;
					}
					// sort prev pointer
					next.prev = tail; // optional
					tail = next;
				}
				// right is now AFTER the list that has just been sorted, so start the next sort there.
				left=right;
			}
			// terminate the list, double the list-sort size.
			if(tail != null){
				tail.next = null; //potential NPE warning seems to be right here, surrended with check-if
			}
			listSize <<= 1;
		}
		while(numMerges > 1); //if only one merge happened, then the whole list has been sorted

		this.head.next = first;
		this.head.prev = tail;
	}



	/**
	 * Performs an inplace sort (see {@link #sortInPlace(Comparator)}) for a list size up to
	 * {@link #thresholdInlineSort()} as inplace sorting amazingly turned out to be faster for SMALLER sizes
	 * (default 10000).<br>
	 * Performs a standard Mergesort with temporary array allocation for lists bigger than that.<br>
	 * Use {@link #thresholdInlineSort(int)} to modify the static threshold.
	 * <p>
	 * To sort larger arrays in place and prevent additional memory allocation, call {@link #sortInPlace(Comparator)}
	 * explicitely.
	 *
	 * @param comparator the comparator to be used to sort this list's elements
	 * @return this instance
	 * @see #sortInPlace(Comparator)
	 */
	@SuppressWarnings("unchecked")
	public VarList<E> sortMerge(final Comparator<E> comparator)
	{
		if(this.size <= 1) return this;
		if(this.size <= this.inlineSortThreshold){
			this.mergeSortInPlace((Comparator<Object>)comparator);
			this.rebuildIndex();
			return this;
		}

		// inlined toArray()
		final int size = this.size;
		final Object[] array = new Object[size];
		Entry loopEntry = this.head;
		for(int i = 0; i < size; i++){
			array[i] = (loopEntry = loopEntry.next).value;
		}

		// actual sort
		JaSort.mergesort(array, (Comparator<Object>)comparator);

		// set values in sorted order back to list (no index update needed)
		loopEntry = this.head;
		for(int i = 0; i < size; i++){
			(loopEntry = loopEntry.next).value = array[i];
		}
		return this;
	}


	public void clear()
	{
		final int recacheCount = Math.min(this.cache.length - this.cacheSize, this.size);
		final Entry[] cache = this.cache;
		Entry loopEntry = this.head;

		// recache entries into pool as long as there's room
		for(int i = 0; i < recacheCount; i++){
			loopEntry = loopEntry.next;
			cache[this.cacheSize++] = loopEntry;
			//reset the entry and enable GC to collect the stuff entry references so far
			loopEntry.value = null;
			loopEntry.next = null;
			loopEntry.prev = null;
		}

		// shouldn't be necessary, should it? Or does it help the GC to collect the entry chain faster?
		while(loopEntry.next != null){
			loopEntry = loopEntry.next;
			loopEntry.prev = null;
			loopEntry.next = null;
			loopEntry.value = null;
		}

		// clear index as well
		final Entry[] entryIndex = this.entryIndex;
		for(int i = 0, size = this.indexSize; i < size; i++){
			entryIndex[i] = null;
		}

		// reinitialize head to reference itself
		this.initializeHead();
	}


	/**
	 * Performs a Quicksort by copying all elements of this list into a newly allocated array, sorting the array
	 * (see {@link JaSort#quicksort(Object[], Comparator)}) and setting the elements in sorted order back to this list.
	 * @param comparator
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public VarList<E> sortQuick(final Comparator<E> comparator)
	{
		// inlined toArray()
		final int size = this.size;
		final Object[] array = new Object[size];
		Entry loopEntry = this.head;
		for(int i = 0; i < size; i++){
			array[i] = (loopEntry = loopEntry.next).value;
		}

		// actual sort
		JaSort.quicksort(array, (Comparator<Object>)comparator);

		// set values in sorted order back to list (no index update needed)
		loopEntry = this.head;
		for(int i = 0; i < size; i++){
			(loopEntry = loopEntry.next).value = array[i];
		}
		return this;
	}


	public Object[] toArray()
	{
		final int size = this.size;
		final Object[] array = new Object[size];
		Entry loopEntry = this.head;
		for(int i = 0; i < size; i++){
			array[i] = (loopEntry = loopEntry.next).value;
		}
		return array;
	}




	public VarList<E> fill(int startIndex, int endIndex, final E element) throws IndexOutOfBoundsException
	{
		// sort the indices as processing order does not matter here
		if(startIndex > endIndex){
			final int t = startIndex;
			startIndex = endIndex;
			endIndex = t;
		}
		if(startIndex >= this.size || endIndex >= this.size){
			throw new IndexOutOfBoundsException(this.excRangeString(startIndex, endIndex));
		}

		Entry hopEntry;
		if(this.nextGetIndex == startIndex){
			// conveniently get starting entry from nextGet cache
			hopEntry = this.nextGetEntry;
		}
		else {
			// find starting entry
			hopEntry = this.entryIndex[startIndex>>this.indexSpanExponent];
			for(int hops = startIndex & this.indexSpanModulo; hops --> 0;){
				hopEntry = hopEntry.next;
			}
		}

		// hop through all entries specified by the range and set their value to the given element
		for(int i = startIndex; i <= endIndex; i++){
			hopEntry.value = element;
			hopEntry = hopEntry.next;
		}

		return this;
	}


	private String excRangeString(final int startIndex, final int endIndex)
	{
		return "Range ["+endIndex+';'+startIndex+"] not in [0;"+(this.size-1)+"].";
	}


	final static class Entry
	{
		Entry prev;
		Entry next;
		Object value;

		@Override
		public String toString()
		{
			return String.valueOf(this.value);
		}
	}


}
