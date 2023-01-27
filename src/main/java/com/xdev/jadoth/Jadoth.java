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

package com.xdev.jadoth;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.RandomAccess;

import com.xdev.jadoth.collections.GrowList;
import com.xdev.jadoth.collections.Snake;
import com.xdev.jadoth.collections.XList;
import com.xdev.jadoth.criteria.AllCriteria;
import com.xdev.jadoth.criteria.AnyCriteria;
import com.xdev.jadoth.criteria.Criterion;
import com.xdev.jadoth.criteria.NotCriterion;
import com.xdev.jadoth.lang.ComparatorSequence;
import com.xdev.jadoth.lang.Equalator;
import com.xdev.jadoth.lang.EqualatorSequence;
import com.xdev.jadoth.lang.functional.Condition;
import com.xdev.jadoth.lang.functional.Predicate;
import com.xdev.jadoth.lang.functional.TCondition;
import com.xdev.jadoth.lang.functional.controlflow.TPredicate;
import com.xdev.jadoth.lang.functional.iterables.ArrayIterable;
import com.xdev.jadoth.lang.functional.iterables.ChainedArraysIterable;
import com.xdev.jadoth.lang.functional.iterables.ChainedIterables;
import com.xdev.jadoth.lang.reference.LinkReference;
import com.xdev.jadoth.lang.reference.Reference;
import com.xdev.jadoth.lang.reference._booleanReference;
import com.xdev.jadoth.lang.reference._byteReference;
import com.xdev.jadoth.lang.reference._charReference;
import com.xdev.jadoth.lang.reference._doubleReference;
import com.xdev.jadoth.lang.reference._floatReference;
import com.xdev.jadoth.lang.reference._intReference;
import com.xdev.jadoth.lang.reference._longReference;
import com.xdev.jadoth.lang.reference._shortReference;
import com.xdev.jadoth.lang.signalthrows.BranchingThrow;
import com.xdev.jadoth.lang.signalthrows.ThrowBreak;
import com.xdev.jadoth.lang.signalthrows.ThrowContinue;
import com.xdev.jadoth.lang.signalthrows.ThrowReturn;
import com.xdev.jadoth.math.JaMath;
import com.xdev.jadoth.util.KeyValue;
import com.xdev.jadoth.util.Pair;
import com.xdev.jadoth.util.file.FileException;
import com.xdev.jadoth.util.file.FolderException;



/**
 * This is a central framework util class containing all the framework's util methods. This approach is made to sustain
 * ease of use in distinction to the countless ambiguous "package.path.util.Util" classes of various other frameworks.
 */
public abstract class Jadoth
{
	  ////////////////
	 // File Utils //
	////////////////

  	/**
	 * Ensure writable file.
	 *
	 * @param parent the parent
	 * @param filename the filename
	 * @return the file
	 */
	public static final File ensureWritableFile(final File parent, final String filename) throws FileException
	{
		return ensureWriteableFile(new File(parent, filename));
	}

	/**
	 * Ensure folder.
	 *
	 * @param folder the f
	 * @return the file
	 */
	public static final File ensureFolder(final File folder) throws FolderException
	{
		boolean success = false;

		try {
			if(folder.exists()) {
				return folder;
			}
			success = folder.mkdirs();
		}
		catch (final Exception e) {
			throw new FolderException(e);
		}
		if(!success){
			throw new FolderException("Folder "+folder+" could not have been created.");
		}

		return folder;
	}

	/**
	 * Ensure writeable file.
	 *
	 * @param file the f
	 * @return the file
	 * @throws FileException if no writability of <code>file</code> could not have been ensured.
	 */
	public static final File ensureWriteableFile(final File file) throws FileException
	{
		try {
			file.createNewFile();
		}
		catch (final Exception e) {
			throw new FileException(e);
		}

		if(!file.canWrite()){
			throw new FileException("Unwritable file: "+file);
		}

		return file;
	}

	/**
	 * Package string to folder path string.
	 *
	 * @param packageString the package string
	 * @return the string
	 */
	public static String packageStringToFolderPathString(final String packageString) {
		return ensureCharAtEnd(packageString.replaceAll("\\.", "/"), '/');
	}

	public static final Appendable appendTextFromFile(final Appendable app, final File file) throws IOException
	{
		final BufferedReader reader = new BufferedReader(new FileReader(file));
		String line = null;

		boolean notEmpty = false;
		try {
			while((line = reader.readLine()) != null) {
				if(notEmpty) app.append('\n'); else notEmpty = true;
				app.append(line);
			}
		}
		finally {
			reader.close();
		}

		return app;
	}

	public static final String readTextFromFile(final File file) throws IOException
	{
		//initialize StringBuilder generously as io accessing etc takes already quite some time in comparison
		return appendTextFromFile(new StringBuilder(50*1024), file).toString();
	}




	public static final <E> Snake<E> snake()
	{
		return new Snake.Implementation<E>();
	}
	public static final <E> Snake<E> snake(final E value)
	{
		return new Snake.Implementation<E>(value);
	}
	public static final <E> Snake<E> snake(final E... values)
	{
		Snake<E> snake = new Snake.Implementation<E>();
		for(final E e : values){
			snake = snake.add(e);
		}
		return snake;
	}


	  //////////////////
	 // String Utils //
	//////////////////

	/**
  	 * Creates the medial capitals string.
  	 *
  	 * @param elements the elements
  	 * @return the string
  	 */
  	public static String createMedialCapitalsString(final String... elements){
		if(elements == null){
			return null;
		}
		if(elements.length == 0){
			return "";
		}
		if(elements.length == 1){
			return elements[0];
		}

		final StringBuilder sb = new StringBuilder(512); //should normally be sufficient for most identifiers
		sb.append(elements[0]);

		for(int i = 1; i < elements.length; i++) {
			final String element = elements[i];
			if(element == null || element.length() == 0) continue;

			final char firstLetter = element.charAt(0);
			if(Character.isUpperCase(firstLetter)){
				//nothing to "camelize"
				sb.append(element).toString();
				continue;
			}

			sb.append(Character.toUpperCase(firstLetter)).append(element.substring(1));
		}
		return sb.toString();
	}
	/**
	 * Explode.
	 *
	 * @param array the array
	 * @param seperator the seperator
	 * @return the string
	 */
	public static final String explode(final Object[] array, final String seperator)
	{
		if(array == null || seperator == null) return null;

		Object o = array[0];
		if(array.length == 1) {
			return o == null ?null :o.toString();
		}

		final StringBuilder explodedString = new StringBuilder(array.length*64)
			.append(o==null?null:o.toString());

		for(int i = 1; i < array.length; i++) {
			o = array[i];
			explodedString.append(seperator).append(o==null?null:o.toString());
		}
		return explodedString.toString();
	}
	/**
	 * Ensure char at start and end.
	 *
	 * @param sb the sb
	 * @param s the s
	 * @param startChar the start char
	 * @param endChar the end char
	 * @return the string builder
	 */
	public static final StringBuilder ensureCharAtStartAndEnd(
		StringBuilder sb,
		final String s,
		final char startChar,
		final char endChar
	)
	{
		final int length = s.length();
		final boolean startOk = s.charAt(0) == endChar;
		final boolean endOk = s.charAt(length-1) == endChar;
		if(sb == null) {
			sb = new StringBuilder(length+2);
		}
		if(!startOk) {
			sb.append(startChar);
		}
		sb.append(s);
		if(!endOk) {
			sb.append(endChar);
		}
		return sb;
	}
	/**
	 * Ensure char at start and end.
	 *
	 * @param s the s
	 * @param startChar the start char
	 * @param endChar the end char
	 * @return the string
	 */
	public static final String ensureCharAtStartAndEnd(
		final String s,
		final char startChar,
		final char endChar
	)
	{
		final int length = s.length();
		final boolean startOk = s.charAt(0) == endChar;
		final boolean endOk = s.charAt(length-1) == endChar;
		if(startOk && endOk) return s;

		final StringBuilder sb = new StringBuilder(length+2);
		if(!startOk) {
			sb.append(startChar);
		}
		sb.append(s);
		if(!endOk) {
			sb.append(endChar);
		}
		return sb.toString();
	}
	/**
	 * Ensure char at end.
	 *
	 * @param s the s
	 * @param c the c
	 * @return the string
	 */
	public static final String ensureCharAtEnd(final String s, final char c)
	{
		if(s.charAt(s.length()-1) == c) {
			return s;
		}
		return s + c;
	}

	public static final boolean isEmpty(final String s)
	{
		return s == null || s.length() == 0;
	}

	/**
	 * To single line.
	 *
	 * @param multiLineString the s
	 * @param lineBreakReplacement the replacement
	 * @return the string
	 */
	public static final String toSingleLine(final String multiLineString, final String lineBreakReplacement)
	{
		return multiLineString.replaceAll("((\\r)?\\n)+", lineBreakReplacement);
	}


	public static final String padSpace(final String s, final int chars)
	{
		final StringBuilder sb = new StringBuilder(chars).append(s);
		for(int i = s.length(); i < chars; i++){
			sb.append(' ');
		}
		return sb.toString();
	}


	/**
	 * Glue.
	 *
	 * @param parts the parts
	 * @return the string
	 */
	public static final String glue(final Object... parts) {
		return concat(' ', parts);
	}

	/**
	 * String list.
	 *
	 * @param parts the parts
	 * @return the string
	 */
	public static final String commaList(final Object... parts) {
		return concat(',', parts);
	}

	public static final String concat(final char concatenator, final Object... parts)
	{
		if(parts == null) return null;

		final StringBuilder sb = new StringBuilder(128);
		final int lastIndex = parts.length-1;
		for (int i = 0; i <= lastIndex ; i++) {
			if(parts[i] == null) {
				continue;
			}
			if(sb.length() > 0) {
				sb.append(concatenator);
			}
			sb.append(parts[i]);
		}
		return sb.toString();
	}

	/**
	 * Concat.
	 *
	 * @param concatenator the concatenator
	 * @param parts the parts
	 * @return the string
	 */
	public static final String concat(final String concatenator, final Object... parts)
	{
		if(parts == null) return null;

		final StringBuilder sb = new StringBuilder(128);
		final int lastIndex = parts.length-1;
		for (int i = 0; i <= lastIndex ; i++) {
			if(parts[i] == null) {
				continue;
			}
			if(sb.length() > 0) {
				sb.append(concatenator);
			}
			sb.append(parts[i]);
		}
		return sb.toString();
	}

	public static final StringBuilder stringBuilder(final Object... elements)
	{
		return appendArray(new StringBuilder(1024), elements);
	}
	public static final StringBuilder stringBuilderSeperated(final char elementSeperator, final Object... elements)
	{
		return appendArraySeperated(new StringBuilder(1024), elementSeperator, elements);
	}
	public static final StringBuilder stringBuilderSeperated(final String elementSeperator, final Object... elements)
	{
		return appendArraySeperated(new StringBuilder(1024), elementSeperator, elements);
	}

	public static final StringBuilder appendArray(final StringBuilder sb, final Object... elements)
	{
		if(elements == null) return sb;

		for(final Object e : elements){
			sb.append(e);
		}
		return sb;
	}
	public static final StringBuilder appendArraySeperated(
		final StringBuilder sb, final char elementSeperator, final Object... elements
	)
	{
		if(elements == null) return sb;

		boolean notFirst = false;
		for(final Object e : elements){
			if(notFirst)
				sb.append(elementSeperator);
			else
				notFirst = true;
			sb.append(e);
		}
		return sb;
	}
	public static final StringBuilder appendArraySeperated(
		final StringBuilder sb, final String elementSeperator, final Object... elements
	)
	{
		if(elementSeperator == null) return appendArray(sb, elements);
		if(elements == null) return sb;

		boolean notFirst = false;
		for(final Object e : elements){
			if(notFirst)
				sb.append(elementSeperator);
			else
				notFirst = true;
			sb.append(e);
		}
		return sb;
	}

//	/**
//	 * Adds the array.
//	 *
//	 * @param sb the sb
//	 * @param array the array
//	 * @param seperator the seperator
//	 * @return the string builder
//	 */
//	public static final StringBuilder addArray(StringBuilder sb, final Object[] array, final String seperator) {
//		if(sb == null) {
//			sb = new StringBuilder(512);
//		}
//		final boolean sep = seperator != null;
//		if(array != null) {
//			for (int i = 0 ; i < array.length; i++) {
//				if(sep && i > 0) {
//					sb.append(seperator);
//				}
//				sb.append(array[i]);
//			}
//		}
//		return sb;
//	}
//	/**
//	 * Adds the array.
//	 *
//	 * @param sb the sb
//	 * @param array the array
//	 * @return the string builder
//	 */
//	public static final StringBuilder addArray(final StringBuilder sb, final Object[] array) {
//		return addArray(sb, array, null);
//	}
//	/**
//	 * Adds the listed array.
//	 *
//	 * @param sb the sb
//	 * @param array the array
//	 * @return the string builder
//	 */
//	public static final StringBuilder addListedArray(final StringBuilder sb, final Object[] array) {
//		return addArray(sb, array, ",");
//	}






	public static final StringBuilder appendIterable(final StringBuilder sb, final Iterable<?> iterable)
	{
		for(final Object e : iterable){
			sb.append(e);
		}
		return sb;
	}
	public static final StringBuilder appendIterableSeperated(
		final StringBuilder sb, final char elementSeperator, final Iterable<?> iterable
	)
	{
		boolean notFirst = false;
		for(final Object e : iterable){
			if(notFirst)
				sb.append(elementSeperator);
			else
				notFirst = true;
			sb.append(e);
		}
		return sb;
	}
	public static final StringBuilder appendIterableSeperated(
		final StringBuilder sb, final String elementSeperator, final Iterable<?> iterable
	)
	{
		if(elementSeperator == null) return appendArray(sb, iterable);

		boolean notFirst = false;
		for(final Object e : iterable){
			if(notFirst)
				sb.append(elementSeperator);
			else
				notFirst = true;
			sb.append(e);
		}
		return sb;
	}



	  //////////////////////
	 //   Thread Utils   //
	//////////////////////

	public static final Thread start(final Runnable r)
	{
		final Thread t = new Thread(r);
		t.start();
		return t;
	}



	  //////////////////////
	 // Collection Utils //
	//////////////////////

	/**
	 * Reduces <code>collection</code> by all elements that meet <code>reductionPredicate</code>.<br>
	 * Note that NO new collection instance is created but the collection itself is reduced.
	 *
	 * @param <T>
	 * @param collection the collection to be reduced
	 * @param reductionPredicate the predicate determining which elements shall be removed
	 * @return <code>collection</code> itself
	 */
	public static <C extends Collection<T>, T> C reduce(final C collection, final Predicate<T> reductionPredicate)
	{
		for(final T t : collection) {
			if(reductionPredicate.apply(t)){
				collection.remove(t);
			}
		}
		return collection;
	}
	public static <T> void reduce(final Iterator<T> iterator, final Predicate<T> reductionPredicate)
	{
		while(iterator.hasNext()){
			if(reductionPredicate.apply(iterator.next())){
				iterator.remove();
			}
		}
	}
	public static <T> ArrayList<T> reduce(final ArrayList<T> arrayList, final Predicate<T> reductionPredicate)
	{
		for(int i = 0, size = arrayList.size(); i< size; i++){
			final T element = arrayList.get(i);
			if(reductionPredicate.apply(element)){
				arrayList.remove(element);
			}
		}
		return arrayList;
	}

	public static <C extends Collection<T>, T> C filter(
		final C sourceCollection, final Predicate<T> selectionPredicate, final C targetCollection
	)
	{
		for(final T t : sourceCollection) {
			if(selectionPredicate.apply(t)){
				targetCollection.add(t);
			}
		}
		return targetCollection;
	}
	public static <T, L extends List<T> & RandomAccess> L filter(
		final L list, final Predicate<T> selectionPredicate, final L targetList
	)
	{
		for(int i = 0, size = list.size(); i< size; i++){
			final T element = list.get(i);
			if(selectionPredicate.apply(element)){
				targetList.add(element);
			}
		}
		return targetList;
	}
	@SuppressWarnings("unchecked")
	public static <T> T[] filter(final T[] array, final Predicate<T> selectionPredicate)
	{
		final ArrayList<T> tempList = new ArrayList<T>(array.length);
		for(final T t : array) {
			if(selectionPredicate.apply(t)){
				tempList.add(t);
			}
		}
		return tempList.toArray((T[])Array.newInstance(array.getClass().getComponentType(), tempList.size()));
	}

	public static <T> GrowList<T> filter(final GrowList<T> GrowList, final Predicate<T> selectionPredicate)
	{
		final GrowList<T> newList = new GrowList<T>(GrowList.size());
		for(int i = 0, size = GrowList.size(); i< size; i++){
			final T element = GrowList.get(i);
			if(selectionPredicate.apply(element)){
				newList.add(element);
			}
		}
		return newList;
	}

	public static <T> ArrayList<T> filter(final ArrayList<T> arrayList, final Predicate<T> selectionPredicate)
	{
		final ArrayList<T> newList = new ArrayList<T>(arrayList.size());
		for(int i = 0, size = arrayList.size(); i< size; i++){
			final T element = arrayList.get(i);
			if(selectionPredicate.apply(element)){
				newList.add(element);
			}
		}
		return newList;
	}

	public static <C extends Collection<T>, T> C append(
		final C collectionToEnhance, final Predicate<T> selectionPredicate, final Iterable<T> collectionToAppend
	)
	{
		for(final T t : collectionToAppend) {
			if(selectionPredicate.apply(t)){
				collectionToEnhance.add(t);
			}
		}
		return collectionToEnhance;
	}
	public static <C extends Collection<T>, T> C append(
		final C collectionToEnhance, final Predicate<T> selectionPredicate, final Iterator<T> iterator
	)
	{
		while(iterator.hasNext()) {
			final T element = iterator.next();
			if(selectionPredicate.apply(element)){
				collectionToEnhance.add(element);
			}
		}
		return collectionToEnhance;
	}
	public static <C extends Collection<T>, T> C append(
		final C collectionToEnhance, final Predicate<T> selectionPredicate, final T[] arrayToAppend
	)
	{
		for(final T t : arrayToAppend) {
			if(selectionPredicate.apply(t)){
				collectionToEnhance.add(t);
			}
		}
		return collectionToEnhance;
	}

	public static final <T> boolean applies(final Iterable<T> elements, final Predicate<T> predicate)
	{
		for(final T t : elements) {
			if(predicate.apply(t)) return true;
		}
		return false;
	}
	public static final <T, L extends List<T> & RandomAccess> boolean applies(
		final L list,
		final Predicate<T> predicate
	)
	{
		for(int i = 0, size = list.size(); i< size; i++){
			if(predicate.apply(list.get(i))) return true;
		}
		return false;
	}
	public static final <T> boolean applies(final T[] array, final Predicate<T> predicate)
	{
		for(final T t : array) {
			if(predicate.apply(t)) return true;
		}
		return false;
	}

	public static final <T> T search(final Iterable<T> elements, final Predicate<T> predicate)
	{
		for(final T t : elements) {
			if(predicate.apply(t)) return t;
		}
		return null;
	}
	public static final <T, L extends List<T> & RandomAccess>  T search(
		final L list,
		final Predicate<T> predicate
	)
	{
		for(int i = 0, size = list.size(); i< size; i++){
			final T element = list.get(i);
			if(predicate.apply(element)) return element;
		}
		return null;
	}
	public static final <T> T search(final T[] array, final Predicate<T> predicate)
	{
		for(final T t : array) {
			if(predicate.apply(t)) return t;
		}
		return null;
	}

	public static final <T> int count(final Iterable<T> collection, final Predicate<T> predicate)
	{
		int count = 0;
		for(final T t : collection) {
			if(predicate.apply(t)) count++;
		}
		return count;
	}
	public static final <T, L extends List<T> & RandomAccess> int count(
		final L list,
		final Predicate<T> predicate
	)
	{
		int count = 0;
		for(int i = 0, size = list.size(); i< size; i++){
			final T element = list.get(i);
			if(predicate.apply(element)) count++;
		}
		return count;
	}
	public static final <T> int count(final T[] array, final Predicate<T> predicate)
	{
		int count = 0;
		for(final T t : array) {
			if(predicate.apply(t)) count++;
		}
		return count;
	}

	public static<K, V, T extends Map<K,V>, S extends Map<? extends K,? extends V>> T mergeInto(
		final T target, final S... maps
	)
	{
		for(final S map : maps) {
			target.putAll(map);
		}
		return target;
	}





	/**
	 *
	 * @param <T>
	 * @param initialCapacity
	 * @param elements
	 * @return
	 */
	public static final <T> ArrayList<T> ArrayList(final T... elements)
	{
		if(elements == null || elements.length == 0){
			return new ArrayList<T>();
		}
		return addArray(new ArrayList<T>(elements.length), elements);
	}

	public static final <T> GrowList<T> GrowList(final T... elements)
	{
		if(elements == null || elements.length == 0){
			return new GrowList<T>();
		}
		return new GrowList<T>(elements.length).add(elements);
	}
	public static final <T> XList<T> JaList(final List<T> list)
	{
		return new GrowList<T>(list);
	}

	public static final <T> ArrayList<T> ArrayList(final int initialCapacity, final Iterable<T> elements)
	{
		return addBatch(new ArrayList<T>(initialCapacity), elements);
	}

	public static final <T> LinkedList<T> LinkedList(final T...elements)
	{
		return addArray(new LinkedList<T>(), elements);
	}

//	public static final <T> StreamList<T> StreamList(final T...elements)
//	{
//		return new StreamList<T>().addArray(elements);
//	}


	/**
	 * Populate collection.
	 *
	 * @param <T> the generic type
	 * @param c the c
	 * @param elements the elements
	 * @return the collection
	 */
	public static final <C extends Collection<T>, T> C addArray(final C c, final T... elements)
	{
		if(elements != null) {
			for(final T t : elements){
				c.add(t);
			}
		}
	    return c;
	}
	public static final <C extends Collection<T>, T> C addBatch(final C c, final Iterable<T> elements)
	{
		if(elements != null) {
			for(final T t : elements){
				c.add(t);
			}
		}
	    return c;
	}





	@SuppressWarnings("unchecked")
	public static final <T> T[] newArray(final Class<T> componenType, final int length)
	{
		return (T[])Array.newInstance(componenType, length);
	}

	@SuppressWarnings("unchecked")
	public static final <T> T[] replicate(final T element, final int amount) throws NullPointerException
	{
		if(element == null) throw new NullPointerException("element may not be null");
		if(amount < 0) throw new IllegalArgumentException("amount may not be negative: "+amount);

		final T[] replicates = (T[])Array.newInstance(element.getClass(), amount);
		for(int i = 0; i < replicates.length; i++) {
			replicates[i] = element;
		}
		return replicates;
	}


	/**
	 * List.
	 *
	 * @param elements the elements
	 * @return the boolean[]
	 */
	public static boolean[] booleans(final boolean... elements){
		return elements;
	}
	/**
	 * List.
	 *
	 * @param elements the elements
	 * @return the byte[]
	 */
	public static byte[] bytes(final byte... elements){
		return elements;
	}
	/**
	 * List.
	 *
	 * @param elements the elements
	 * @return the short[]
	 */
	public static short[] shorts(final short... elements){
		return elements;
	}
	/**
	 * List.
	 *
	 * @param elements the elements
	 * @return the int[]
	 */
	public static int[] ints(final int... elements){
		return elements;
	}
	/**
	 * List.
	 *
	 * @param elements the elements
	 * @return the long[]
	 */
	public static long[] longs(final long... elements){
		return elements;
	}
	/**
	 * List.
	 *
	 * @param elements the elements
	 * @return the float[]
	 */
	public static float[] floats(final float... elements){
		return elements;
	}
	/**
	 * List.
	 *
	 * @param elements the elements
	 * @return the double[]
	 */
	public static double[] doubles(final double... elements){
		return elements;
	}
	/**
	 * List.
	 *
	 * @param elements the elements
	 * @return the char[]
	 */
	public static char[] chars(final char... elements){
		return elements;
	}
	/**
	 * List.
	 *
	 * @param <T> the generic type
	 * @param elements the elements
	 * @return the t[]
	 */
	public static <T> T[] array(final T... elements){
		return elements;
	}
	/**
	 * List.
	 *
	 * @param <T> the generic type
	 * @param elements the elements
	 * @return the t[]
	 */
	public static <T> GrowList<T> list(final T... elements)
	{
		if(elements == null || elements.length == 0){
			return new GrowList<T>();
		}
		return new GrowList<T>(elements.length).add(elements);
	}

	public static <T> HashSet<T> set(final T... elements)
	{
		if(elements == null || elements.length == 0) return new HashSet<T>();
		return addArray(new HashSet<T>(elements.length), elements);
	}

	public static <K, V> HashMap<K, V> map(final KeyValue<? extends K, ? extends V>... keyValueTuples)
	{
		if(keyValueTuples == null || keyValueTuples.length == 0) return new HashMap<K,V>();
		final HashMap<K, V> map = new HashMap<K, V>(keyValueTuples.length);

		for(final KeyValue<? extends K, ? extends V> kv : keyValueTuples){
			map.put(kv.key(), kv.value());
		}

		return map;
	}
//	public static <K, V> HashMap<K, V> map(final KeyValueTuple<K, V>... keyValueTuples)
//	{
//		if(keyValueTuples == null || keyValueTuples.length == 0) return new HashMap<K,V>();
//		final HashMap<K, V> map = new HashMap<K, V>(keyValueTuples.length);
//
//		for(final KeyValueTuple<K, V> kv : keyValueTuples){
//			map.put(kv.key(), kv.value());
//		}
//
//		return map;
//	}


	/**
	 * Alias for the annoying <code>collection.toArray((T[])Array.newInstance(elementType, collection.size()))</code>
	 *
	 * @param <T> the element type parameter of the list
	 * @param collection the collection whose elements shall be copied to an array
	 * @param elementType the type of the elements contained in <code>collection</code>.
	 * @return a new array object of type <T> containing all elements of <code>collection</code>.
	 */
	@SuppressWarnings("unchecked")
	public static final <T> T[] toArray(final Collection<T> collection, final Class<T> elementType)
	{
		return collection.toArray((T[])Array.newInstance(elementType, collection.size()));
	}

	@SuppressWarnings("unchecked")
	public static final <T> T[] toArray(final Iterable<T> iterable, final Class<T> elementType)
	{
		final ArrayList<T> list = ArrayList(1024, iterable);
		return list.toArray((T[])Array.newInstance(elementType, list.size()));
	}



	public static <T> Iterable<T> iterate(final Iterable<T>... iterables)
	{
		return new ChainedIterables<T>(iterables);
	}
	public static <T> Iterable<T> iterate(final T[]... arrays)
	{
		return new ChainedArraysIterable<T>(arrays);
	}


	/**
	 * Wraps <code>array</code> of type <code>T[]</code> in an instance of <code>Iterable<T></code>
	 *
	 * @param <T> the type of <code>array</code>'s elements and of the created <code>Iterable</code>
	 * @param array the array to be wrapped
	 * @return the <code>Iterable<T></code> wrapping <code>T[] array</code>.
	 */
	public static <T> Iterable<T> iterate(final T[] array)
	{
		return new ArrayIterable<T>(array);
	}



	///////////////////////////////////////////////////////////////////////////
	// Language Utils   //
	/////////////////////

	public static final <T> T notNull(final T object)
	{
		if(object == null){
			throw new NullPointerException();
		}
		return object;
	}

	/**
  	 * Coalesce.
  	 *
  	 * @param <T> the generic type
  	 * @param firstElement the first element
  	 * @param secondElement the second element
  	 * @return the t
  	 */
  	public static final <T> T coalesce(final T firstElement, final T secondElement)
	{
		return firstElement == null ?secondElement :firstElement;
	}
	/**
	 * Coalesce.
	 *
	 * @param <T> the generic type
	 * @param elements the elements
	 * @return the t
	 */
	public static final <T> T coalesce(final T... elements)
	{
		for(final T e : elements){
			if(e != null) return e;
		}
		return null;
	}

	public static <K,V> KeyValue<K,V> keyValue(final K key, final V value)
	{
		return new KeyValue.Implementation<K,V>(key, value);
	}

	public static <F,S> Pair<F,S> pair(final F first, final S second)
	{
		return new Pair.Implementation<F,S>(first, second);
	}

	/**
	 * Tries to cast <code>keyValue</code> to <code>Pair</code>
	 * or instantiates a new <code>Pair</code> instance of failure.
	 * @param <F>
	 * @param <S>
	 * @param keyValue
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <F,S> Pair<F,S> toPair(final KeyValue<F, S> keyValue)
	{
		try {
			return (Pair<F,S>)keyValue;
		}
		catch(final ClassCastException e) {
			return new Pair.Implementation<F,S>(keyValue.key(), keyValue.value());
		}
	}

	@SuppressWarnings("unchecked")
	public static <K,V> KeyValue<K,V> toKeyValue(final Pair<K,V> pair)
	{
		try {
			return (KeyValue<K,V>)pair;
		}
		catch(final ClassCastException e) {
			return new KeyValue.Implementation<K,V>(pair.first(), pair.second());
		}
	}

	public static <T> Reference<T> ref(final T object)
	{
		return new Reference.Implementation<T>(object);
	}

	public static <T> LinkReference<T> linkRef(final T object)
	{
		return new LinkReference.Implementation<T>(object);
	}

	public static <T> LinkReference<T> chain(final T... objects)
	{
		if(objects == null) return null;

		final LinkReference<T> chain = new LinkReference.Implementation<T>(objects[0]);

		if(objects.length > 1){
			LinkReference<T> loopRef = chain;
			for(int i = 1; i < objects.length; i++){
				loopRef = loopRef.link(objects[i]);
			}
		}
		return chain;
	}

	public static _booleanReference ref(final boolean value)
	{
		return new _booleanReference(value);
	}
	public static _byteReference ref(final byte value)
	{
		return new _byteReference(value);
	}
	public static _shortReference ref(final short value)
	{
		return new _shortReference(value);
	}
	public static _intReference ref(final int value)
	{
		return new _intReference(value);
	}
	public static _longReference ref(final long value)
	{
		return new _longReference(value);
	}
	public static _floatReference ref(final float value)
	{
		return new _floatReference(value);
	}
	public static _doubleReference ref(final double value)
	{
		return new _doubleReference(value);
	}
	public static _charReference ref(final char value)
	{
		return new _charReference(value);
	}




	public static final <T> AllCriteria<T> all(final Criterion<T>... criteria)
	{
		return new AllCriteria<T>(criteria);
	}
	public static final <T> AnyCriteria<T> any(final Criterion<T>... criteria)
	{
		return new AnyCriteria<T>(criteria);
	}
	public static final <T> NotCriterion<T> not(final Criterion<T> criteria)
	{
		return new NotCriterion<T>(criteria);
	}




	///////////////////////////////////////////////////////////////////////////
	// Throwable Utils  //
	/////////////////////


	public static final <T extends Throwable> T removeHighestStrackTraceElement(final T throwable)
	{
		final StackTraceElement[] stackTrace = throwable.getStackTrace();
		final StackTraceElement[] newStackTrace = new StackTraceElement[stackTrace.length - 1];
		System.arraycopy(stackTrace, 1, newStackTrace, 0, newStackTrace.length);
		throwable.setStackTrace(newStackTrace);
		return throwable;
	}

	public static final <T extends Throwable> T removeHighestStrackTraceElement(final T throwable, final int n)
	{
		final StackTraceElement[] stackTrace = throwable.getStackTrace();
		final StackTraceElement[] newStackTrace = new StackTraceElement[stackTrace.length - n];
		System.arraycopy(stackTrace, n, newStackTrace, 0, newStackTrace.length);
		throwable.setStackTrace(newStackTrace);
		return throwable;
	}

	public static final <T extends Throwable> T removeHighestStrackTraceElementUntil(
		final T throwable, final Class<?> c, final String methodname
	)
	{
		final StackTraceElement[] stackTrace = throwable.getStackTrace();
		final String cName = c.getName();

		int stackTracesToSkip = 0;
		for(final StackTraceElement ste : stackTrace) {
			if(ste.getClassName().equals(cName) && ste.getMethodName().equals(methodname)) break;
			stackTracesToSkip++;
		}
		return removeHighestStrackTraceElement(throwable, stackTracesToSkip);
	}




	/**
	 * Not implemented yet. DO NOT USE!
	 * @param o1
	 * @param o2
	 * @return always <tt>false</tt> at the moment
	 */
	@Deprecated
	public static final boolean equalContent(final Object o1, final Object o2)
	{
		//trivial cases
		if(o1 == null) return o2 == null;
		else if(o2 == null) return false;
		else if(o1 == o2) return true;

		final Class<?> c = o1.getClass();
		if(o2.getClass() != c) return false;


		/*complex case:
		 * 1.) for each instance field in class c:
		 *    - if value1 != value2 return false
		 * 2.) return true
		 */

		return false;
	}

	/**
	 * Not implemented yet. DO NOT USE!
	 * @param o1
	 * @param o2
	 * @return always <tt>false</tt> at the moment
	 */
	@Deprecated
	public static final boolean equalCommonContent(final Object o1, final Object o2)
	{
		//trivial cases
		if(o1 == null) return o2 == null;
		else if(o2 == null) return false;
		else if(o1 == o2) return true;

		/*complex case:
		 * 1.) determine most specific common class
		 *    - if it is Object.class, return false
		 * 2.) for each instance field in commmon class:
		 *    - if value1 != value2 return false
		 * 3.) return true
		 */

		return false;
	}


	public static final <E> E[] shuffle(final E[] array, final int startIndex, final int endIndex)
	{
		if(startIndex < 0 || endIndex >= array.length || startIndex > endIndex){
			throw new IndexOutOfBoundsException("Range ["+startIndex+';'+endIndex+"] not in [0;"+(array.length-1)+"].");
		}
		final Random r = JaMath.random();
		for (int i = endIndex; i > startIndex; i--){
			 swap(array, i, r.nextInt(i));
		}
		return array;
	}
	public static final <E> E[] shuffle(final E[] array)
	{
		final Random r = JaMath.random();
		for (int i = array.length; i > 1; i--){
			 swap(array, i-1, r.nextInt(i));
		}
		return array;

	}
	private static void swap(final Object[] arr, final int i, final int j)
	{
        final Object tmp = arr[i];
        arr[i] = arr[j];
        arr[j] = tmp;
    }


	/**
	 * Instantiates a new {@link Predicate} that delegates calls to the passed {@link Predicate} but inverts the result.
	 * <p>
	 * Code: <code>!predicate.apply(t)</code>
	 * @param <T>
	 * @param predicate the predicate whose result shall be inverted by the created not-wrapper-predicate
	 * @return a new not-wrapper-predicate for the passed predicate
	 */
	public static <T> Condition<T> not(final Predicate<T> predicate)
	{
		return new Condition<T>(){
			@Override public boolean apply(final T t)
			{
				return !predicate.apply(t);
			}
		};
	}
	/**
	 * Instantiates a new {@link TPredicate} that delegates calls
	 * to the passed {@link TPredicate} but inverts the result.
	 * <p>
	 * Code: <code>!predicate.apply(t)</code>
	 * @param <T>
	 * @param predicate the predicate whose result shall be inverted by the created not-wrapper-predicate
	 * @return a new not-wrapper-predicate for the passed predicate
	 */
	public static <T> TCondition<T> not(final TPredicate<T> predicate)
	{
		return new TCondition<T>(){
			@Override public boolean apply(final T t) throws ThrowBreak, ThrowContinue, ThrowReturn
			{
				return !predicate.apply(t);
			}
		};
	}



	public static final <T> Condition<T> and(final Predicate<T> predicate1, final Predicate<T> predicate2)
	{
		return new Condition<T>(){
			@Override public boolean apply(final T t)
			{
				return predicate1.apply(t) && predicate2.apply(t);
			}
		};
	}
	public static final <T> TCondition<T> and(final TPredicate<T> predicate1, final TPredicate<T> predicate2)
	{
		return new TCondition<T>(){
			@Override public boolean apply(final T t) throws ThrowBreak, ThrowContinue, ThrowReturn
			{
				return predicate1.apply(t) && predicate2.apply(t);
			}
		};
	}

	/**
	 * Instantiates a new {@link Condition} that wraps the passed predicate as {@link Condition}.
	 * @param <T>
	 * @param predicate
	 * @return a new predicate-wrapper-condition instance.
	 */
	public static final <T> Condition<T> condition(final Predicate<T> predicate)
	{
		return new Condition<T>(){
			@Override public boolean apply(final T t)
			{
				return predicate.apply(t);
			}
		};
	}



	public static final <T>  Condition<T> or(final  Predicate<T> predicate1, final  Predicate<T> predicate2)
	{
		return new Condition<T>(){
			@Override public boolean apply(final T t)
			{
				return predicate1.apply(t) || predicate2.apply(t);
			}
		};
	}
	public static final <T> TCondition<T> or(final TPredicate<T> predicate1, final TPredicate<T> predicate2)
	{
		return new TCondition<T>(){
			@Override public boolean apply(final T t) throws ThrowBreak, ThrowContinue, ThrowReturn
			{
				return predicate1.apply(t) || predicate2.apply(t);
			}
		};
	}


	public static final <T> TCondition<T> throwing(final Predicate<T> predicate)
	{
		return new TCondition<T>() {
			@Override
			public boolean apply(final T t) throws ThrowBreak, ThrowContinue, ThrowReturn
			{
				return predicate.apply(t);
			}
		};
	}
	public static final <T> Predicate<T> unthrowing(final TPredicate<T> predicate)
	{
		return new Predicate<T>() {
			@Override
			public boolean apply(final T t)
			{
				try {
					return predicate.apply(t);
				}
				catch(final BranchingThrow e) {
					return false;
				}
			}
		};
	}

//	public static final Comparator<Object> EQUALS = new Comparator<Object>(){
//		@Override
//		public int compare(final Object o1, final Object o2)
//		{
//			if(o1 == o2)   return  0;
//			if(o1 == null) return o2 == null ?0 :-1;
//			return o1.equals(o2) ?0 :1;
//		}
//	};

	public static final <E> Equalator<E> chain(final Equalator<E>... equalators)
	{
		return new EqualatorSequence<E>(equalators);
	}

	public static final <E> Comparator<E> chain(final Comparator<E>... comparators)
	{
		return new ComparatorSequence<E>(comparators);
	}

	public static final <E> Equalator<E> equalator(final Comparator<E> comparator)
	{
		return new Equalator<E>(){
			@Override
			public boolean equal(final E object1, final E object2){
				return comparator.compare(object1, object2) == 0;
			}
		};
	}

	public static final <E> Equalator<E> equality(final Class<E> type)
	{
		return new Equalator<E>(){
			@Override
			public boolean equal(final E o1, final E o2){
				return o1 == null ?o2 == null :o1.equals(o2);
			}
		};
	}

	public static final Equalator<Object> EQUALS = new Equalator<Object>(){
		@Override
		public boolean equal(final Object o1, final Object o2)
		{
//			if(o1 == o2)   return true; //leave that to .equals() implementation
			if(o1 == null) return o2 == null;
			return o1.equals(o2);
		}
	};


	private Jadoth(){}
}
