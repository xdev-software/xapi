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

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashSet;

import com.xdev.jadoth.lang.Equalator;


/**
 * @author Thomas Muenz
 *
 */
public class JaArrays
{
	public static final <T> T[] fill(final T[] array, final T fillElement, int fromIndex, final int toIndex)
	{
		if(fromIndex < 0 || fromIndex >= array.length) throw new ArrayIndexOutOfBoundsException(fromIndex);
		if(toIndex < 0 || toIndex >= array.length) throw new ArrayIndexOutOfBoundsException(toIndex);

		if(fromIndex < toIndex){
			while(fromIndex <= toIndex){
				array[fromIndex++] = fillElement;
			}
		}
		else {
			while(fromIndex >= toIndex){
				array[fromIndex--] = fillElement;
			}
		}
		return array;
	}


	public static final boolean equals(
		final Object[] array1,
		final int startIndex1,
		final Object[] array2,
		final int startIndex2,
		final int length,
		final Equalator<Object> comparator
	)
	{
		//all bounds exceptions are provoked intentionally because no harm will be done by this method in those cases
		int a = startIndex1, b = startIndex2;

		for(final int aBound = startIndex1 + length; a < aBound; a++){
			if(comparator.equal(array1[a], array2[b++])){
				return false;
			}
		}
		return true;
	}

	/**
	 * Adds the arrays.
	 *
	 * @param <T> the generic type
	 * @param c the c
	 * @param a1 the a1
	 * @param a2 the a2
	 * @return the t[]
	 */
	@SuppressWarnings("unchecked")
	public static final <T> T[] add(final T[] a1, final T... a2)
	{
		//escape conditions
		if(a1==null) return a2;
		if(a2==null) return a1;

		//variables
		final int len1 = a1.length;
		final int len2 = a2.length;
		final T[] a =  (T[])Array.newInstance(a1.getClass().getComponentType(), len1 + len2);

		//actual logic
		System.arraycopy(a1, 0, a,    0, len1);
		System.arraycopy(a2, 0, a, len1, len2);

		//result
		return a;
	}


	/**
	 * This method checks if <code>array</code> contains <code>element</code> by object identity
	 *
	 * @param <T> any type
	 * @param array the array to be searched in
	 * @param element the element to be searched (by identity)
	 * @return <tt>true</tt> if <code>array</code> contains <code>element</code> by object identity, else <tt>false</tt>
	 */
	public static final <T> boolean contains(final T[] array, final T element)
	{
		for(final T t : array) {
			if(t == element) return true;
		}
		return false;
	}

	public static final <T> boolean eqContains(final T[] array, final T element)
	{
		if(element == null){
			for(final T t : array) {
				if(t == null) return true;
			}
		}
		else {
			for(final T t : array) {
				if(element.equals(t)) return true;
			}
		}

		return false;
	}
	

	public static final <T> boolean contains(final T[] array, final T element, final Equalator<T> cmp)
	{
		for(final T t : array) {
			if(cmp.equal(element, t)) return true;
		}
		return false;
	}


	public static final <T> boolean idContains(final Collection<? extends T> c, final T element)
	{
		for(final T t : c) {
			if(t == element) return true;
		}
		return false;
	}


	/**
	 * Removed all occurances of <code>elementToRemove</code> from array <code>src</code>.<br>
	 * If <code>dest</code> is the same as <code>src</code>, the retained elements will be compressed in place.
	 * Otherwise all retained elements are copied to dest.<br>
	 * As the number of to be removed objects is not known beforehand, <code>dest</code> has to have enough length to
	 * receive all retained elements, otherwise an {@link ArrayIndexOutOfBoundsException} will be thrown.
	 * <p>
	 * Note that removing <tt>null</tt> will still set the trailing array positions to <tt>null</tt>.<br>
	 *
	 * @param elementToRemove
	 * @param src the source array containing all elements.
	 * @param srcStart
	 * @param srcBound
	 * @param dest the destination array to receive the retained objects. Can be <code>src</code> again.
	 * @param destStart
	 * @param destBound
	 * @return the number of removed elements
	 */
	public static int removeAllFromArray(
		final Object elementToRemove,
		final Object[] src,
		final int srcStart,
		final int srcBound,
		final Object[] dest,
		final int destStart,
		final int destBound
	)
		throws ArrayIndexOutOfBoundsException
	{

		//determine first move target index
		int currentMoveTargetIndex;
		if(src == dest){
			currentMoveTargetIndex = srcStart;
			//if dest is the same as src, skip all to be retained objects
			while(src[currentMoveTargetIndex] != elementToRemove){
				currentMoveTargetIndex++;
			}
		}
		else {
			currentMoveTargetIndex = destStart;
			//if dest is not the same as src, don't skip elements as dest is the target of a copying process
		}

		int currentMoveSourceIndex = 0;
		int currentMoveLength = 0;
		int seekIndex = currentMoveTargetIndex;

		while(seekIndex < srcBound){
			while(seekIndex < srcBound && src[seekIndex] == elementToRemove){
				seekIndex++;
			}
			currentMoveSourceIndex = seekIndex;

			while(seekIndex < srcBound && src[seekIndex] != elementToRemove){
				seekIndex++;
			}
			currentMoveLength = seekIndex - currentMoveSourceIndex;

			switch(currentMoveLength){
				//manual copying is faster than system arraycopy for very small array sizes
				case 4:
					dest[currentMoveTargetIndex  ] = src[currentMoveSourceIndex  ];
					dest[currentMoveTargetIndex+1] = src[currentMoveSourceIndex+1];
					dest[currentMoveTargetIndex+2] = src[currentMoveSourceIndex+2];
					dest[currentMoveTargetIndex+3] = src[currentMoveSourceIndex+3];
					break;
				case 3:
					dest[currentMoveTargetIndex  ] = src[currentMoveSourceIndex  ];
					dest[currentMoveTargetIndex+1] = src[currentMoveSourceIndex+1];
					dest[currentMoveTargetIndex+2] = src[currentMoveSourceIndex+2];
					break;
				case 2:
					dest[currentMoveTargetIndex  ] = src[currentMoveSourceIndex  ];
					dest[currentMoveTargetIndex+1] = src[currentMoveSourceIndex+1];
					break;
				case 1:
					dest[currentMoveTargetIndex] = src[currentMoveSourceIndex];
				case 0:
					break;
				default:
					System.arraycopy(src, currentMoveSourceIndex, dest, currentMoveTargetIndex, currentMoveLength);
			}
			currentMoveTargetIndex += currentMoveLength;
		}
		if(src == dest){
			for(int i = currentMoveTargetIndex; i < srcBound; i++){
				dest[i] = null;
			}
		}
		return srcBound - currentMoveTargetIndex;
	}

	//!\\ NOTE: copy of single-object version with only contains part changed! Maintain by copying!
	public static int removeAllFromArray(
		final Collection<?> elementsToRemove,
		final Object[] src,
		final int srcStart,
		final int srcBound,
		final Object[] dest,
		final int destStart,
		final int destBound,
		final boolean ignoreNulls
	)
		throws ArrayIndexOutOfBoundsException
	{

		//determine first move target index
		int currentMoveTargetIndex;
		Object e = null;
		if(src == dest){
			currentMoveTargetIndex = srcStart;
			//if dest is the same as src, skip all to be retained objects
			while((e = src[currentMoveTargetIndex]) == null && ignoreNulls || !idContains(elementsToRemove, e)){
				currentMoveTargetIndex++;
			}
		}
		else {
			currentMoveTargetIndex = destStart;
			//if dest is not the same as src, don't skip elements as dest is the target of a copying process
		}

		int currentMoveSourceIndex = 0;
		int currentMoveLength = 0;
		int seekIndex = currentMoveTargetIndex;

		while(seekIndex < srcBound){
			while(seekIndex < srcBound && ((e = src[seekIndex]) != null || !ignoreNulls) && idContains(elementsToRemove, e)){
				seekIndex++;
			}
			currentMoveSourceIndex = seekIndex;

			while(seekIndex < srcBound && ((e = src[seekIndex]) == null && ignoreNulls || !idContains(elementsToRemove, e))){
				seekIndex++;
			}
			currentMoveLength = seekIndex - currentMoveSourceIndex;

			switch(currentMoveLength){
				//manual copying is faster than system arraycopy for very small array sizes
				case 4:
					dest[currentMoveTargetIndex  ] = src[currentMoveSourceIndex  ];
					dest[currentMoveTargetIndex+1] = src[currentMoveSourceIndex+1];
					dest[currentMoveTargetIndex+2] = src[currentMoveSourceIndex+2];
					dest[currentMoveTargetIndex+3] = src[currentMoveSourceIndex+3];
					break;
				case 3:
					dest[currentMoveTargetIndex  ] = src[currentMoveSourceIndex  ];
					dest[currentMoveTargetIndex+1] = src[currentMoveSourceIndex+1];
					dest[currentMoveTargetIndex+2] = src[currentMoveSourceIndex+2];
					break;
				case 2:
					dest[currentMoveTargetIndex  ] = src[currentMoveSourceIndex  ];
					dest[currentMoveTargetIndex+1] = src[currentMoveSourceIndex+1];
					break;
				case 1:
					dest[currentMoveTargetIndex] = src[currentMoveSourceIndex];
				case 0:
					break;
				default:
					System.arraycopy(src, currentMoveSourceIndex, dest, currentMoveTargetIndex, currentMoveLength);
			}
			currentMoveTargetIndex += currentMoveLength;
		}
		if(src == dest){
			for(int i = currentMoveTargetIndex; i < srcBound; i++){
				dest[i] = null;
			}
		}
		return srcBound - currentMoveTargetIndex;
	}

	//!\\ NOTE: copy of single-object version with only contains part changed! Maintain by copying!
	public static int removeAllFromArray(
		final Object[] elementsToRemove,
		final Object[] src,
		final int srcStart,
		final int srcBound,
		final Object[] dest,
		final int destStart,
		final int destBound,
		final boolean ignoreNulls
	)
		throws ArrayIndexOutOfBoundsException
	{
		//determine first move target index
		int currentMoveTargetIndex;
		Object e = null;
		if(src == dest){
			currentMoveTargetIndex = srcStart;
			//if dest is the same as src, skip all to be retained objects
			while((e = src[currentMoveTargetIndex]) == null && ignoreNulls || !contains(elementsToRemove, e)){
				currentMoveTargetIndex++;
			}
		}
		else {
			currentMoveTargetIndex = destStart;
			//if dest is not the same as src, don't skip elements as dest is the target of a copying process
		}

		int currentMoveSourceIndex = 0;
		int currentMoveLength = 0;
		int seekIndex = currentMoveTargetIndex;

		while(seekIndex < srcBound){
			while(seekIndex < srcBound && ((e = src[seekIndex]) != null || !ignoreNulls) && contains(elementsToRemove, e)){
				seekIndex++;
			}
			currentMoveSourceIndex = seekIndex;

			while(seekIndex < srcBound && ((e = src[seekIndex]) == null && ignoreNulls || !contains(elementsToRemove, e))){
				seekIndex++;
			}
			currentMoveLength = seekIndex - currentMoveSourceIndex;

			switch(currentMoveLength){
				//manual copying is faster than system arraycopy for very small array sizes
				case 4:
					dest[currentMoveTargetIndex  ] = src[currentMoveSourceIndex  ];
					dest[currentMoveTargetIndex+1] = src[currentMoveSourceIndex+1];
					dest[currentMoveTargetIndex+2] = src[currentMoveSourceIndex+2];
					dest[currentMoveTargetIndex+3] = src[currentMoveSourceIndex+3];
					break;
				case 3:
					dest[currentMoveTargetIndex  ] = src[currentMoveSourceIndex  ];
					dest[currentMoveTargetIndex+1] = src[currentMoveSourceIndex+1];
					dest[currentMoveTargetIndex+2] = src[currentMoveSourceIndex+2];
					break;
				case 2:
					dest[currentMoveTargetIndex  ] = src[currentMoveSourceIndex  ];
					dest[currentMoveTargetIndex+1] = src[currentMoveSourceIndex+1];
					break;
				case 1:
					dest[currentMoveTargetIndex] = src[currentMoveSourceIndex];
				case 0:
					break;
				default:
					System.arraycopy(src, currentMoveSourceIndex, dest, currentMoveTargetIndex, currentMoveLength);
			}
			currentMoveTargetIndex += currentMoveLength;
		}
		if(src == dest){
			for(int i = currentMoveTargetIndex; i < srcBound; i++){
				dest[i] = null;
			}
		}
		return srcBound - currentMoveTargetIndex;
	}



	public static int removeAllFromArray(
		final Object elementToRemove,
		final Object[] src,
		final int srcStart,
		final int srcBound,
		final Object[] dest,
		final int destStart,
		final int destBound, 
		final Equalator<Object> equalator
	)
		throws ArrayIndexOutOfBoundsException
	{
		if(elementToRemove == null){
			return removeAllFromArray(elementToRemove, src, srcStart, srcBound, dest, destStart, destBound);
		}

		//determine first move target index
		int currentMoveTargetIndex;
		if(src == dest){
			currentMoveTargetIndex = srcStart;
			//if dest is the same as src, skip all to be retained objects
			while(!equalator.equal(elementToRemove, src[currentMoveTargetIndex])){
				currentMoveTargetIndex++;
			}
		}
		else {
			currentMoveTargetIndex = destStart;
			//if dest is not the same as src, don't skip elements as dest is the target of a copying process
		}

		int currentMoveSourceIndex = 0;
		int currentMoveLength = 0;
		int seekIndex = currentMoveTargetIndex;

		while(seekIndex < srcBound){
			while(seekIndex < srcBound && equalator.equal(elementToRemove,src[seekIndex])){
				seekIndex++;
			}
			currentMoveSourceIndex = seekIndex;

			while(seekIndex < srcBound && !equalator.equal(elementToRemove,src[seekIndex])){
				seekIndex++;
			}
			currentMoveLength = seekIndex - currentMoveSourceIndex;

			switch(currentMoveLength){
				//manual copying is faster than system arraycopy for very small array sizes
				case 4:
					dest[currentMoveTargetIndex  ] = src[currentMoveSourceIndex  ];
					dest[currentMoveTargetIndex+1] = src[currentMoveSourceIndex+1];
					dest[currentMoveTargetIndex+2] = src[currentMoveSourceIndex+2];
					dest[currentMoveTargetIndex+3] = src[currentMoveSourceIndex+3];
					break;
				case 3:
					dest[currentMoveTargetIndex  ] = src[currentMoveSourceIndex  ];
					dest[currentMoveTargetIndex+1] = src[currentMoveSourceIndex+1];
					dest[currentMoveTargetIndex+2] = src[currentMoveSourceIndex+2];
					break;
				case 2:
					dest[currentMoveTargetIndex  ] = src[currentMoveSourceIndex  ];
					dest[currentMoveTargetIndex+1] = src[currentMoveSourceIndex+1];
					break;
				case 1:
					dest[currentMoveTargetIndex] = src[currentMoveSourceIndex];
				case 0:
					break;
				default:
					System.arraycopy(src, currentMoveSourceIndex, dest, currentMoveTargetIndex, currentMoveLength);
			}
			currentMoveTargetIndex += currentMoveLength;
		}
		if(src == dest){
			for(int i = currentMoveTargetIndex; i < srcBound; i++){
				dest[i] = null;
			}
		}
		return srcBound - currentMoveTargetIndex;
	}

	//!\\ NOTE: copy of single-object version with only contains part changed! Maintain by copying!
	public static int removeAllFromArray(
		final Collection<?> elementsToRemove,
		final Object[] src,
		final int srcStart,
		final int srcBound,
		final Object[] dest,
		final int destStart,
		final int destBound,
		final boolean ignoreNulls, 
		final Equalator<Object> equalator
	)
		throws ArrayIndexOutOfBoundsException
	{
		Object e = null;
		//determine first move target index
		int currentMoveTargetIndex;
		if(src == dest){
			currentMoveTargetIndex = srcStart;
			//if dest is the same as src, skip all to be retained objects
			while((e = src[currentMoveTargetIndex]) == null && ignoreNulls || !elementsToRemove.contains(e)){
				currentMoveTargetIndex++;
			}
		}
		else {
			currentMoveTargetIndex = destStart;
			//if dest is not the same as src, don't skip elements as dest is the target of a copying process
		}

		int currentMoveSourceIndex = 0;
		int currentMoveLength = 0;
		int seekIndex = currentMoveTargetIndex;


		while(seekIndex < srcBound){

			while(seekIndex < srcBound && ((e = src[seekIndex]) != null || !ignoreNulls) && elementsToRemove.contains(e)){
				seekIndex++;
			}
			currentMoveSourceIndex = seekIndex;

			while(seekIndex < srcBound && ((e = src[seekIndex]) == null && ignoreNulls || !elementsToRemove.contains(e))){
				seekIndex++;
			}
			currentMoveLength = seekIndex - currentMoveSourceIndex;

			switch(currentMoveLength){
				//manual copying is faster than system arraycopy for very small array sizes
				case 4:
					dest[currentMoveTargetIndex  ] = src[currentMoveSourceIndex  ];
					dest[currentMoveTargetIndex+1] = src[currentMoveSourceIndex+1];
					dest[currentMoveTargetIndex+2] = src[currentMoveSourceIndex+2];
					dest[currentMoveTargetIndex+3] = src[currentMoveSourceIndex+3];
					break;
				case 3:
					dest[currentMoveTargetIndex  ] = src[currentMoveSourceIndex  ];
					dest[currentMoveTargetIndex+1] = src[currentMoveSourceIndex+1];
					dest[currentMoveTargetIndex+2] = src[currentMoveSourceIndex+2];
					break;
				case 2:
					dest[currentMoveTargetIndex  ] = src[currentMoveSourceIndex  ];
					dest[currentMoveTargetIndex+1] = src[currentMoveSourceIndex+1];
					break;
				case 1:
					dest[currentMoveTargetIndex] = src[currentMoveSourceIndex];
				case 0:
					break;
				default:
					System.arraycopy(src, currentMoveSourceIndex, dest, currentMoveTargetIndex, currentMoveLength);
			}
			currentMoveTargetIndex += currentMoveLength;
		}
		if(src == dest){
			for(int i = currentMoveTargetIndex; i < srcBound; i++){
				dest[i] = null;
			}
		}
		return srcBound - currentMoveTargetIndex;
	}

	//!\\ NOTE: copy of single-object version with only contains part changed! Maintain by copying!
	public static int removeAllFromArray(
		final Object[] elementsToRemove,
		final Object[] src,
		final int srcStart,
		final int srcBound,
		final Object[] dest,
		final int destStart,
		final int destBound,
		final boolean ignoreNulls, 
		final Equalator<Object> eq
	)
		throws ArrayIndexOutOfBoundsException
	{
		//determine first move target index
		int currentMoveTargetIndex;
		Object e = null;
		if(src == dest){
			currentMoveTargetIndex = srcStart;
			//if dest is the same as src, skip all to be retained objects
			while((e = src[currentMoveTargetIndex]) == null && ignoreNulls || !contains(elementsToRemove, e, eq)){
				currentMoveTargetIndex++;
			}
		}
		else {
			currentMoveTargetIndex = destStart;
			//if dest is not the same as src, don't skip elements as dest is the target of a copying process
		}

		int currentMoveSourceIndex = 0;
		int currentMoveLength = 0;
		int seekIndex = currentMoveTargetIndex;


		while(seekIndex < srcBound){
			while(seekIndex < srcBound &&  ((e = src[seekIndex]) != null || !ignoreNulls) && contains(elementsToRemove, e, eq)){
				seekIndex++;
			}
			currentMoveSourceIndex = seekIndex;

			while(seekIndex < srcBound &&  ((e = src[seekIndex]) == null && ignoreNulls || !contains(elementsToRemove, e, eq))){
				seekIndex++;
			}
			currentMoveLength = seekIndex - currentMoveSourceIndex;

			switch(currentMoveLength){
				//manual copying is faster than system arraycopy for very small array sizes
				case 4:
					dest[currentMoveTargetIndex  ] = src[currentMoveSourceIndex  ];
					dest[currentMoveTargetIndex+1] = src[currentMoveSourceIndex+1];
					dest[currentMoveTargetIndex+2] = src[currentMoveSourceIndex+2];
					dest[currentMoveTargetIndex+3] = src[currentMoveSourceIndex+3];
					break;
				case 3:
					dest[currentMoveTargetIndex  ] = src[currentMoveSourceIndex  ];
					dest[currentMoveTargetIndex+1] = src[currentMoveSourceIndex+1];
					dest[currentMoveTargetIndex+2] = src[currentMoveSourceIndex+2];
					break;
				case 2:
					dest[currentMoveTargetIndex  ] = src[currentMoveSourceIndex  ];
					dest[currentMoveTargetIndex+1] = src[currentMoveSourceIndex+1];
					break;
				case 1:
					dest[currentMoveTargetIndex] = src[currentMoveSourceIndex];
				case 0:
					break;
				default:
					System.arraycopy(src, currentMoveSourceIndex, dest, currentMoveTargetIndex, currentMoveLength);
			}
			currentMoveTargetIndex += currentMoveLength;
		}
		if(src == dest){
			for(int i = currentMoveTargetIndex; i < srcBound; i++){
				dest[i] = null;
			}
		}
		return srcBound - currentMoveTargetIndex;
	}




	// (03.09.2010 TM)TODO: removeDuplicates(): check for better algorithms for eq and id
	@SuppressWarnings("unchecked")
	public static final <T> T[] eqRemoveDuplicates(final T[] array)
	{
		final LinkedHashSet<T> singletons = new LinkedHashSet<T>(Arrays.asList(array));
		return singletons.toArray((T[])Array.newInstance(array.getClass().getComponentType(), singletons.size()));
	}

	@SuppressWarnings("unchecked")
	public static final <T> T[] idRemoveDuplicates(final T[] array)
	{
		final ArrayList<T> singletons = new ArrayList<T>(array.length);

		addSourceElement: for(final T t : array) {
			//ArrayList-index access is significantly faster then iteration
			for(int i = 0, size = singletons.size(); i < size; i++) {
				if(t == singletons.get(i)) continue addSourceElement;
			}
			singletons.add(t);
		}

		return singletons.toArray((T[])Array.newInstance(array.getClass().getComponentType(), singletons.size()));
	}

}
