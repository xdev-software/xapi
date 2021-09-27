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

import java.util.Arrays;
import java.util.Comparator;




/**
 * @author Thomas Muenz
 *
 */

public class JaSort
{
	///////////////////////////////////////////////////////////////////////////
	//    Quicksort     //
	/////////////////////

	/*
	Quicksort algorithm for int with comments
	---------------------------------

	/*
	 * The threshold for using insertion sort depends on the data type and maybe hardware as well.
	 * Current tests for int values showed values between 20 and 25 as best threshold, while for Objects, it's still 7.
	 *\
	if(endIndex - startIndex < 20) { //this actually is "length - 1"
		for(int i = startIndex; i <= endIndex; i++){
			for(int j = i; j > startIndex && values[j-1] > values[j]; j--){
				final int t = values[j];
				values[j] = values[j-1];
				values[j-1] = t;
			}
		}
	    return;
	}

	final int pivot = values[startIndex+endIndex >> 1];

	//loop indices
	int i = startIndex, j = endIndex;

	// divide into two lists
	while(i <= j) {
		// scroll forward left list to pivot element
		while(values[i] < pivot) i++;

		// scroll forward right list to pivot element
		while(values[j] > pivot) j--;

		/* All values in the left  list that are larger  then the pivot element and
	 	 * all values in the right list that are smaller then the pivot element
	 	 * get switched with each other.
	 	 *\
		if(i <= j) {
			final int t = values[i];
			values[i] = values[j];
			values[j] = t;
			i++;
			j--;
		}
	}

	// recursion
	if(j > startIndex) quicksort(values, startIndex, j);
	if(i < endIndex) quicksort(values, i, endIndex);
	 */

	/**
	 * Alias for <code>quicksort(values, 0, values.length - 1, comparator)</code>.
	 *
	 * @param <E> the element type
	 * @param values the array whose elements shall be sorted
	 * @param comparator the comparator to be used to sort the elements
	 * @throws NullPointerException if the provided array is <tt>null</tt>
	 * @see {@link #quicksort(boolean[], int, int, Comparator)}
	 */
	public static final void quicksort(final boolean[] values) throws NullPointerException
	{
		quicksort(values, 0, values.length - 1);
	}
	public static final void quicksort(final boolean[] values, final int startIndex, final int endIndex)
		throws NullPointerException, ArrayIndexOutOfBoundsException
	{
		final boolean pivot = values[startIndex+endIndex >> 1];
		int i = startIndex, j = endIndex;

		if(pivot){
			while(i <= j) {
				while(!values[i]) i++;
				if(i > j) break;
				final boolean t = values[i];
				values[i] = values[j];
				values[j] = t;
				i++;
				j--;
			}
		}
		else {
			while(i <= j) {
				while(values[j]) j--;
				if(i > j) break;
				final boolean t = values[i];
				values[i] = values[j];
				values[j] = t;
				i++;
				j--;
			}
		}

		if(j > startIndex) quicksort(values, startIndex, j);
		if(i < endIndex) quicksort(values, i, endIndex);
	}

	/**
	 * Alias for <code>quicksort(values, 0, values.length - 1, comparator)</code>.
	 *
	 * @param <E> the element type
	 * @param values the array whose elements shall be sorted
	 * @param comparator the comparator to be used to sort the elements
	 * @throws NullPointerException if the provided array is <tt>null</tt>
	 * @see {@link #quicksort(byte[], int, int, Comparator)}
	 */
	public static final void quicksort(final byte[] values) throws NullPointerException
	{
		quicksort(values, 0, values.length - 1);
	}
	public static final void quicksort(final byte[] values, final int startIndex, final int endIndex)
		throws NullPointerException, ArrayIndexOutOfBoundsException
	{
		final byte pivot = values[startIndex+endIndex >> 1]; //use middle element as default pivot
		int i = startIndex, j = endIndex;
		while(i <= j) {
			while(values[i] < pivot) i++;
			while(values[j] > pivot) j--;
			if(i > j) break;
			final byte t = values[i];
			values[i] = values[j];
			values[j] = t;
			i++;
			j--;
		}
		if(j > startIndex) quicksort(values, startIndex, j);
		if(i < endIndex) quicksort(values, i, endIndex);
	}

	/**
	 * Alias for <code>quicksort(values, 0, values.length - 1, comparator)</code>.
	 *
	 * @param <E> the element type
	 * @param values the array whose elements shall be sorted
	 * @param comparator the comparator to be used to sort the elements
	 * @throws NullPointerException if the provided array is <tt>null</tt>
	 * @see {@link #quicksort(short[], int, int, Comparator)}
	 */
	public static final void quicksort(final short[] values) throws NullPointerException
	{
		quicksort(values, 0, values.length - 1);
	}
	public static final void quicksort(final short[] values, final int startIndex, final int endIndex)
		throws NullPointerException, ArrayIndexOutOfBoundsException
	{
		final short pivot = values[startIndex+endIndex >> 1];
		int i = startIndex, j = endIndex;
		while(i <= j) {
			while(values[i] < pivot) i++;
			while(values[j] > pivot) j--;
			if(i > j) break;
			final short t = values[i];
			values[i] = values[j];
			values[j] = t;
			i++;
			j--;
		}
		if(j > startIndex) quicksort(values, startIndex, j);
		if(i < endIndex) quicksort(values, i, endIndex);
	}

	/**
	 * Alias for <code>quicksort(values, 0, values.length - 1, comparator)</code>.
	 *
	 * @param <E> the element type
	 * @param values the array whose elements shall be sorted
	 * @param comparator the comparator to be used to sort the elements
	 * @throws NullPointerException if the provided array is <tt>null</tt>
	 * @see {@link #quicksort(int[], int, int, Comparator)}
	 */
	public static final void quicksort(final int[] values) throws NullPointerException
	{
		quicksort(values, 0, values.length - 1);
	}

	/**
	 * 	(Comparison with JDK implementation showed around 20% better performance of the direct implementation in
	 *  common cases and 60% better performance for bad cases, i.e. presorted list).
	 *
	 * @param values
	 * @param startIndex
	 * @param endIndex
	 * @throws NullPointerException
	 * @throws ArrayIndexOutOfBoundsException
	 */
	public static final void quicksort(final int[] values, final int startIndex, final int endIndex)
		throws NullPointerException, ArrayIndexOutOfBoundsException
	{
		if(endIndex - startIndex < 20) { //this is actually "length - 1"
			for(int i = startIndex; i <= endIndex; i++){
				for(int j = i; j > startIndex && values[j-1] > values[j]; j--){
					final int t = values[j];
					values[j] = values[j-1];
					values[j-1] = t;
				}
			}
		    return;
		}


		final int pivot = values[startIndex+endIndex >> 1];
		int l = startIndex, r = endIndex;
		while(l <= r) {
			while(values[l] < pivot) l++;
			while(values[r] > pivot) r--;
			if(l > r) break;
			final int t = values[l];
			values[l] = values[r];
			values[r] = t;
			swapCount++;
			l++;
			r--;
		}
		if(r > startIndex) quicksort(values, startIndex, r);
		if(l < endIndex) quicksort(values, l, endIndex);
	}
	public static int swapCount = 0;

	/**
	 * Alias for <code>quicksort(values, 0, values.length - 1, comparator)</code>.
	 *
	 * @param <E> the element type
	 * @param values the array whose elements shall be sorted
	 * @param comparator the comparator to be used to sort the elements
	 * @throws NullPointerException if the provided array is <tt>null</tt>
	 * @see {@link #quicksort(long[], int, int, Comparator)}
	 */
	public static final void quicksort(final long[] values) throws NullPointerException
	{
		quicksort(values, 0, values.length - 1);
	}
	public static final void quicksort(final long[] values, final int startIndex, final int endIndex)
		throws NullPointerException, ArrayIndexOutOfBoundsException
	{
		final long pivot = values[startIndex+endIndex >> 1];
		int i = startIndex, j = endIndex;
		while(i <= j) {
			while(values[i] < pivot) i++;
			while(values[j] > pivot) j--;
			if(i > j) break;
			final long t = values[i];
			values[i] = values[j];
			values[j] = t;
			i++;
			j--;
		}
		if(j > startIndex) quicksort(values, startIndex, j);
		if(i < endIndex) quicksort(values, i, endIndex);
	}

	/**
	 * Alias for <code>quicksort(values, 0, values.length - 1, comparator)</code>.
	 *
	 * @param <E> the element type
	 * @param values the array whose elements shall be sorted
	 * @param comparator the comparator to be used to sort the elements
	 * @throws NullPointerException if the provided array is <tt>null</tt>
	 * @see {@link #quicksort(float[], int, int, Comparator)}
	 */
	public static final void quicksort(final float[] values) throws NullPointerException
	{
		quicksort(values, 0, values.length - 1);
	}
	public static final void quicksort(final float[] values, final int startIndex, final int endIndex)
		throws NullPointerException, ArrayIndexOutOfBoundsException
	{
		final float pivot = values[startIndex+endIndex >> 1];
		int i = startIndex, j = endIndex;
		while(i <= j) {
			while(values[i] < pivot) i++;
			while(values[j] > pivot) j--;
			if(i > j) break;
			final float t = values[i];
			values[i] = values[j];
			values[j] = t;
			i++;
			j--;
		}
		if(j > startIndex) quicksort(values, startIndex, j);
		if(i < endIndex) quicksort(values, i, endIndex);
	}

	/**
	 * Alias for <code>quicksort(values, 0, values.length - 1, comparator)</code>.
	 *
	 * @param <E> the element type
	 * @param values the array whose elements shall be sorted
	 * @param comparator the comparator to be used to sort the elements
	 * @throws NullPointerException if the provided array is <tt>null</tt>
	 * @see {@link #quicksort(double[], int, int, Comparator)}
	 */
	public static final void quicksort(final double[] values) throws NullPointerException
	{
		quicksort(values, 0, values.length - 1);
	}
	public static final void quicksort(final double[] values, final int startIndex, final int endIndex)
		throws NullPointerException, ArrayIndexOutOfBoundsException
	{
		final double pivot = values[startIndex+endIndex >> 1];
		int i = startIndex, j = endIndex;
		while(i <= j) {
			while(values[i] < pivot) i++;
			while(values[j] > pivot) j--;
			if(i > j) break;
			final double t = values[i];
			values[i] = values[j];
			values[j] = t;
			i++;
			j--;
		}
		if(j > startIndex) quicksort(values, startIndex, j);
		if(i < endIndex) quicksort(values, i, endIndex);
	}

	/**
	 * Alias for <code>quicksort(values, 0, values.length - 1, comparator)</code>.
	 *
	 * @param <E> the element type
	 * @param values the array whose elements shall be sorted
	 * @param comparator the comparator to be used to sort the elements
	 * @throws NullPointerException if the provided array is <tt>null</tt>
	 * @see {@link #quicksort(E[], int, int, Comparator)}
	 */
	public static final <E> void quicksort(final E[] values, final Comparator<E> comparator) throws NullPointerException
	{
		quicksort0(values, 0, values.length - 1, comparator);
	}
	/**
	 * Direct general purpose sort-unstable implementation of the Quicksort algorithm with insertion sort for arrays
	 * up to size 8.
	 * <p>
	 * The pivot element is always exactely the middle element ((startIndex+endIndex)/2) for the following reasons:
	 * <ul>
	 * <li>median3() selection did not yield better performance in practice.</li>
	 * <li>using middle element is optimal for presorted lists (see below).</li>
	 * <li>using middle element minimizes stacktrace (approx. (8*log(n) - 4) with n being the sort range).</li>
	 * </ul>
	 * <p>
	 * Comparison with JDK's Mergesort implementation showed up to 20% better performance
	 * on randomly filled arrays of length up to 1 million elements (about equal performance on bigger arrays)
	 * and 300% worse performance on worst cases, i.e. presorted list.<br>
	 * If presorted lists can happen to be sorted often, Mergesort should be used instead.
	 * <p>
	 * Note that no worst-case optimisations like 3-way partitioning is done.
	 * <p>
	 * If a special implementation is needed, it should be implemented specifically.
	 *
	 * @param <E>  the element type
	 * @param values the array whose elements shall be sorted
	 * @param startIndex the index of the first element to be sorted
	 * @param endIndex the index of the last element to be sorted
	 * @param comparator the comparator to be used to sort the elements.
	 * @throws NullPointerException if either array or comparator is null
	 * @throws ArrayIndexOutOfBoundsException if the start and end indices exceed the array bounds.
	 * @see {@link #quicksort(E[], Comparator)}
	 */

	public static final <E> void quicksort(final E[] values, final int startIndex, final int endIndex, final Comparator<E> comparator) throws NullPointerException, ArrayIndexOutOfBoundsException
	{
		if(comparator == null) return;		
		quicksort0(values, startIndex, endIndex, comparator);
	}
	private static <E> void quicksort0(final E[] values, final int startIndex, final int endIndex, final Comparator<E> comparator) throws NullPointerException, ArrayIndexOutOfBoundsException
	{
		//count(new Throwable().getStackTrace().length - 3);
		if(endIndex - startIndex < 8) { //this is actually "length - 1", so length 8 and below are insertionsorted
			for(int i = startIndex; i <= endIndex; i++){
				for(int j = i; j > startIndex && comparator.compare(values[j-1], values[j]) > 0; j--){
					final E t = values[j];
					values[j] = values[j-1];
					values[j-1] = t;
				}
			}
		    return;
		}

		/*
		 * Note about pivot element selection:
		 * - values[startIndex] would be most stupid thing to do for presorted arrays
		 * - values[med3(start, mid, end)] did not yield better performance in any case than just using mid
		 *
		 * => so simply use middle element as default pivot.
		 * yields very good performance in common case and acceptable performance in worst case
		 * (around 3 times slower than Mergesort for presorted arrays but by no means O(n^2))
		 *
		 * Additionally:
		 * - Using mid index is the optimal pivot element for presorted lists afaik
		 * - Using mid index minimizes stack depth (around (8*log(n) - 4) in tests. i.e. 53 for 10 mil elements)
		 *
		 * If in any event exactely mid element turns out to be bad (e.g. sawtooth data), an alternative could be:
		 * pivot = values[(startIndex+endIndex >> 1)-4 + random(8)]
		 *
		 * Note that the Insertionsort-check above ensures that quicksort range is at least 9.
		 * So [mid-4;mid+4] will always be safe.
		 * Maybe random(8) could be performance optimized, like by (System.nanoTime() & 7)
		 *
		 * pivot = values[(startIndex+endIndex >> 1)-4 + (int)(System.nanoTime() & 7)] //funny
		 */
		final E pivot = values[startIndex+endIndex >> 1];
		int l = startIndex, r = endIndex;
		while(l <= r) {
			while(comparator.compare(values[l], pivot) < 0) l++;
			while(comparator.compare(values[r], pivot) > 0) r--;
			if(l > r) break;
			final E t = values[l];
			values[l] = values[r];
			values[r] = t;
			l++;
			r--;
		}
		if(r > startIndex) quicksort0(values, startIndex, r, comparator);
		if(l < endIndex) quicksort0(values, l, endIndex, comparator);
	}

	//only for testing puposes
//	private static <E> int med3(final E x[], final int a, final int b, final int c, final Comparator<E> cmp) {
//		return cmp.compare(x[a], x[b]) < 0
//			?(cmp.compare(x[b], x[c]) < 0 ? b : cmp.compare(x[a], x[c]) < 0 ? c : a)
//			:cmp.compare(x[b], x[c]) > 0 ? b : cmp.compare(x[a], x[c]) > 0 ? c : a
//		;
//	}

	//only for measuring puposes
//	public static HashMap<Integer, Integer> stackStats = new HashMap<Integer, Integer>();
//	static void count(final int stackDepth)
//	{
//		final Integer stackCount = stackStats.get(stackDepth);
//		stackStats.put(stackDepth, stackCount == null ?1 :stackCount+1);
//	}


	///////////////////////////////////////////////////////////////////////////
	//    Mergesort     //
	/////////////////////

	public static final <E> void mergesort(final E[] values, final Comparator<E> comparator) throws NullPointerException
	{
		if(comparator == null) return;
		
//		final Object[] aux = new Object[values.length];
//		System.arraycopy(values, 0, aux, 0, values.length);
//		mergesort0(aux, values, 0, values.length, 0, comparator);

		// no idea why, but the very inefficient copyofRange() call is much faster than directly creating the array...
		mergesort0(Arrays.copyOfRange(values, 0, values.length), values, 0, values.length, 0, comparator);
	}
	public static final <E> void mergesort(final E[] values, final int startIndex, final int endIndex, final Comparator<E> comparator)
		throws NullPointerException, ArrayIndexOutOfBoundsException
	{
		if(comparator == null) return;
		
//		final Object[] aux = new Object[values.length];
//		System.arraycopy(values, 0, aux, 0, values.length);
//		mergesort0(aux, values, startIndex, endIndex, -startIndex, comparator);

		// no idea why, but the very inefficient copyofRange() call is much faster than directly creating the array...
		mergesort0(
			Arrays.copyOfRange(values, startIndex, endIndex+1),
			values, startIndex, endIndex+1, -startIndex, comparator
		);
	}

	/*
	 * Copied from JDK to ensure mergesort
	 * (JDK's Mergesort in sort() may get replaced by Timsort in Java 7 as it would appear.)
	 */
	@SuppressWarnings("unchecked")
	private static <E> void mergesort0(
		final Object[] src,
		final Object[] dest,
		int low,
		int high,
		final int off,
		final Comparator c
	)
		throws NullPointerException, ArrayIndexOutOfBoundsException
	{
		final int length = high - low;

		if (length < 7) {
			for (int i = low; i < high; i++){
				for (int j = i; j > low && c.compare(dest[j-1], dest[j]) > 0; j--){
					final Object t = dest[j-1];
					dest[j-1] = dest[j];
					dest[j] = t;

				}
			}
			return;
		}

		// Recursively sort halves of dest into src
		final int destLow  = low;
		final int destHigh = high;
		low  += off;
		high += off;
		final int mid = low + high >>> 1;
		mergesort0(dest, src, low, mid, -off, c);
		mergesort0(dest, src, mid, high, -off, c);

		/* If list is already sorted, just copy from src to dest.
		 * This is an optimization that results in faster sorts for nearly ordered lists.
		 */
		if (c.compare(src[mid-1], src[mid]) <= 0) {
			System.arraycopy(src, low, dest, destLow, length);
			return;
		}

		// Merge sorted halves (now in src) into dest
		for(int i = destLow, p = low, q = mid; i < destHigh; i++) {
			if (q >= high || p < mid && c.compare(src[p], src[q]) <= 0)
				dest[i] = src[p++];
			else
				dest[i] = src[q++];
		}

	}

}
