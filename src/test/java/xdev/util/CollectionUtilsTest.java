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
package xdev.util;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import xdev.util.dummies.NameEnumeration;
import xdev.util.dummies.NameIterator;
import xdev.util.dummies.NameList;
import xdev.util.dummies.Person;
import xdev.util.dummies.PersonList;
import xdev.util.dummies.RichPerson;
import xdev.util.dummies.StringComparer;


/**
 * Tests for the Class {@link CollectionUtils}.
 * 
 * @author XDEV Software (FHAE)
 * 
 */
public class CollectionUtilsTest
{
	private final String[] arrTestData = {"Hans", "Fritz", "Egon", "Berta", "Sebi"};
	private Collection<String> collectionNames = null;
	private Enumeration<String> enumerationNames = null;
	private Iterator<String> iteratorNames = null;
	
	@Before
	public void init()
	{
		// collectionNames = new ArrayList<String>();
		this.collectionNames = new LinkedList<>();
		
		this.collectionNames.add(this.arrTestData[0]);
		this.collectionNames.add(this.arrTestData[1]);
		this.collectionNames.add(this.arrTestData[2]);
		this.collectionNames.add(this.arrTestData[3]);
		this.collectionNames.add(this.arrTestData[4]);
		
		this.enumerationNames = new NameEnumeration();
		
		this.iteratorNames = new NameIterator();
	}
	
	/**
	 * Test method for
	 * {@link xdev.util.CollectionUtils#copyIntoArray(java.util.Collection, java.lang.Object[], int)}
	 * .
	 */
	@Test
	public void testCopyIntoArrayCollectionObjectArrayInt_defaultBehavior()
	{
		final String[] destinationArray = new String[this.arrTestData.length];
		CollectionUtils.copyIntoArray(this.collectionNames, destinationArray, 0);
		assertArrayEquals(this.arrTestData, destinationArray);
	}
	
	/**
	 * Test method for
	 * {@link xdev.util.CollectionUtils#copyIntoArray(java.util.Collection, java.lang.Object[], int)}
	 * .
	 */
	@Test
	public void testCopyIntoArrayCollectionObjectArrayInt_ParamDestOffset()
	{
		final String[] destinationArray = new String[this.arrTestData.length + 2];
		destinationArray[0] = null;
		destinationArray[1] = null;
		
		CollectionUtils.copyIntoArray(this.collectionNames, destinationArray, 2);
		
		assertEquals(null, destinationArray[0]);
		assertEquals(null, destinationArray[1]);
		assertEquals(this.arrTestData[0], destinationArray[2]);
		assertEquals(this.arrTestData[1], destinationArray[3]);
	}
	
	/**
	 * Test method for
	 * {@link xdev.util.CollectionUtils#copyIntoArray(java.util.Collection, java.lang.Object[], int)}
	 * .
	 */
	@Test(expected = ArrayStoreException.class)
	public void testCopyIntoArrayCollectionObjectArrayInt_ArrayStoreException()
	{
		final Integer[] destinationArray = new Integer[2];
		final Collection<Object> toCopyCollection = new ArrayList<>();
		toCopyCollection.add("Test");
		toCopyCollection.add(1);
		
		CollectionUtils.copyIntoArray(toCopyCollection, destinationArray, 0);
	}
	
	/**
	 * Test method for
	 * {@link xdev.util.CollectionUtils#copyIntoArray(java.util.Collection, java.lang.Object[], int)}
	 * .
	 */
	@Test(expected = NullPointerException.class)
	public void testCopyIntoArrayCollectionObjectArrayInt_ParamCNullPointerException()
	{
		final String[] destinationArray = new String[this.arrTestData.length];
		CollectionUtils.copyIntoArray(null, destinationArray, 0);
	}
	
	/**
	 * Test method for
	 * {@link xdev.util.CollectionUtils#copyIntoArray(java.util.Collection, java.lang.Object[], int)}
	 * .
	 */
	@Test(expected = NullPointerException.class)
	public void testCopyIntoArrayCollectionObjectArrayInt_ParamArrayNullPointerException()
	{
		CollectionUtils.copyIntoArray(this.collectionNames, null, 0);
	}
	
	/**
	 * Test method for
	 * {@link xdev.util.CollectionUtils#copyIntoArray(java.util.Collection, java.lang.Object[], int)}
	 * .
	 */
	@Test(expected = IndexOutOfBoundsException.class)
	public void testCopyIntoArrayCollectionObjectArrayInt_ParamDestOffsetIndexOutOfBoundsException()
	{
		final String[] destinationArray = new String[this.arrTestData.length];
		CollectionUtils.copyIntoArray(this.collectionNames, destinationArray, this.collectionNames.size() + 1);
	}
	
	/**
	 * Test method for
	 * {@link xdev.util.CollectionUtils#copyIntoArray(java.util.Collection, int, java.lang.Object[], int, int)}
	 * .
	 */
	@Test
	public void testCopyIntoArrayCollectionIntObjectArrayIntInt_defaultBehavior()
	{
		final String[] destinationArray = new String[this.arrTestData.length];
		CollectionUtils.copyIntoArray(this.collectionNames, 0, destinationArray, 0, this.arrTestData.length);
		assertArrayEquals(this.arrTestData, destinationArray);
	}
	
	/**
	 * Test method for
	 * {@link xdev.util.CollectionUtils#copyIntoArray(java.util.Collection, int, java.lang.Object[], int, int)}
	 * .
	 */
	@Test
	public void testCopyIntoArrayCollectionIntObjectArrayIntInt_ParamCollectionOffset()
	{
		final List<Integer> list = new LinkedList<>();
		list.add(1);
		list.add(3);
		list.add(5);
		
		final Integer[] arr = new Integer[list.size() - 1];
		CollectionUtils.copyIntoArray(list, 1, arr, 0, arr.length);
		
		assertEquals(3, arr[0].intValue());
		assertEquals(5, arr[1].intValue());
	}
	
	/**
	 * Test method for
	 * {@link xdev.util.CollectionUtils#copyIntoArray(java.util.Collection, int, java.lang.Object[], int, int)}
	 * .
	 */
	@Test
	public void testCopyIntoArrayCollectionIntObjectArrayIntInt_ParamDestOffset()
	{
		final String[] destinationArray = new String[this.arrTestData.length + 2];
		destinationArray[0] = null;
		destinationArray[1] = null;
		
		CollectionUtils.copyIntoArray(this.collectionNames, 0, destinationArray, 2, destinationArray.length - 2);
		
		assertEquals(null, destinationArray[0]);
		assertEquals(null, destinationArray[1]);
		assertEquals(this.arrTestData[0], destinationArray[2]);
		assertEquals(this.arrTestData[1], destinationArray[3]);
	}
	
	/**
	 * Test method for
	 * {@link xdev.util.CollectionUtils#copyIntoArray(java.util.Collection, int, java.lang.Object[], int, int)}
	 * .
	 */
	@Test(expected = ArrayStoreException.class)
	public void testCopyIntoArrayCollectionIntObjectArrayIntInt_ArrayStoreException()
	{
		final Integer[] destinationArray = new Integer[2];
		final Collection<Object> toCopyCollection = new ArrayList<>();
		toCopyCollection.add("Test");
		toCopyCollection.add(1);
		
		CollectionUtils.copyIntoArray(toCopyCollection, 0, destinationArray, 0, destinationArray.length);
	}
	
	/**
	 * Test method for
	 * {@link xdev.util.CollectionUtils#copyIntoArray(java.util.Collection, int, java.lang.Object[], int, int)}
	 * .
	 */
	@Test(expected = NullPointerException.class)
	public void testCopyIntoArrayCollectionIntObjectArrayIntInt_ParamCNullPointerException()
	{
		final String[] destinationArray = new String[this.arrTestData.length];
		CollectionUtils.copyIntoArray(null, 5, destinationArray, 0, 5);
	}
	
	/**
	 * Test method for
	 * {@link xdev.util.CollectionUtils#copyIntoArray(java.util.Collection, int, java.lang.Object[], int, int)}
	 * .
	 */
	@Test(expected = NullPointerException.class)
	public void testCopyIntoArrayCollectionIntObjectArrayIntInt_ParamArrayNullPointerException()
	{
		CollectionUtils.copyIntoArray(this.collectionNames, this.collectionNames.size(), null, 0, 1);
	}
	
	/**
	 * Test method for
	 * {@link xdev.util.CollectionUtils#copyIntoArray(java.util.Collection, int, java.lang.Object[], int, int)}
	 * .
	 */
	@Test(expected = IndexOutOfBoundsException.class)
	public void testCopyIntoArrayCollectionIntObjectArrayIntInt_ParamCollectionOffsetIndexOutOfBoundsException()
	{
		final String[] destinationArray = new String[this.arrTestData.length];
		CollectionUtils.copyIntoArray(
			this.collectionNames,
			100,
			destinationArray,
			this.collectionNames.size(),
			this.arrTestData.length);
	}
	
	/**
	 * Test method for
	 * {@link xdev.util.CollectionUtils#copyIntoArray(java.util.Collection, int, java.lang.Object[], int, int)}
	 * .
	 */
	@Test(expected = IndexOutOfBoundsException.class)
	public void testCopyIntoArrayCollectionIntObjectArrayIntInt_ParamDestOffsetIndexOutOfBoundsException()
	{
		final String[] destinationArray = new String[this.arrTestData.length];
		CollectionUtils.copyIntoArray(
			this.collectionNames,
			this.collectionNames.size(),
			destinationArray,
			destinationArray.length + 1,
			this.arrTestData.length);
	}
	
	/**
	 * Test method for
	 * {@link xdev.util.CollectionUtils#copyIntoArray(java.util.Collection, int, java.lang.Object[], int, int)}
	 * .
	 */
	@Test(expected = IndexOutOfBoundsException.class)
	public void testCopyIntoArrayCollectionIntObjectArrayIntInt_ParamCountIndexOutOfBoundsException()
	{
		final String[] destinationArray = new String[this.arrTestData.length];
		CollectionUtils.copyIntoArray(
			this.collectionNames,
			this.collectionNames.size(),
			destinationArray,
			this.collectionNames.size(),
			100);
	}
	
	/**
	 * Test method for {@link xdev.util.CollectionUtils#asList(T[])}.
	 */
	@Test
	public void testAsListTArray_defaultBehavior()
	{
		final List<Integer> expected = new ArrayList<>();
		expected.add(1);
		expected.add(2);
		expected.add(3);
		expected.add(4);
		expected.add(5);
		
		final List<Integer> actual = CollectionUtils.asList(new Integer[]{1, 2, 3, 4, 5});
		assertNotNull(actual);
		assertTrue(expected.equals(actual));
	}
	
	/**
	 * Test method for
	 * {@link xdev.util.CollectionUtils#asList(java.util.Enumeration)}.
	 */
	@Test
	public void testAsListEnumerationOfT_defaultBehavior()
	{
		final List<String> actual = CollectionUtils.asList(this.enumerationNames);
		this.verifyContent(actual, this.arrTestData);
	}
	
	/**
	 * Test method for
	 * {@link xdev.util.CollectionUtils#asList(java.util.Iterator)}.
	 */
	@Test
	public void testAsListIteratorOfT_defaultBehavior()
	{
		final List<String> actual = CollectionUtils.asList(this.iteratorNames);
		this.verifyContent(actual, this.arrTestData);
	}
	
	/**
	 * Test method for
	 * {@link xdev.util.CollectionUtils#asIterator(java.util.Enumeration)}.
	 */
	@Test
	public void testAsIterator_defaultBehavior()
	{
		final Iterator<String> actual = CollectionUtils.asIterator(this.enumerationNames);
		
		assertEquals(actual.next(), this.arrTestData[0]);
		assertEquals(actual.next(), this.arrTestData[1]);
		assertEquals(actual.next(), this.arrTestData[2]);
		assertEquals(actual.next(), this.arrTestData[3]);
		assertEquals(actual.next(), this.arrTestData[4]);
		assertFalse(actual.hasNext());
	}
	
	/**
	 * Test method for
	 * {@link xdev.util.CollectionUtils#asEnumeration(java.util.Iterator)}.
	 */
	@Test
	public void testAsEnumeration_defaultBehavior()
	{
		final java.util.Enumeration<String> actual = CollectionUtils.asEnumeration(this.iteratorNames);
		
		assertEquals(actual.nextElement(), this.arrTestData[0]);
		assertEquals(actual.nextElement(), this.arrTestData[1]);
		assertEquals(actual.nextElement(), this.arrTestData[2]);
		assertEquals(actual.nextElement(), this.arrTestData[3]);
		assertEquals(actual.nextElement(), this.arrTestData[4]);
		assertFalse(actual.hasMoreElements());
	}
	
	/**
	 * Test method for
	 * {@link xdev.util.CollectionUtils#createList(java.lang.Object, int)}.
	 */
	@Test
	public void testCreateList_defaultBehavior()
	{
		final List<String> actual = CollectionUtils.createList(this.arrTestData[0], this.arrTestData.length);
		
		assertEquals(actual.size(), this.arrTestData.length);
		
		for(int i = 0; i < this.arrTestData.length; i++)
		{
			assertEquals(actual.get(i), this.arrTestData[0]);
		}
	}
	
	/**
	 * Test method for {@link xdev.util.CollectionUtils#asSet(T[])}.
	 */
	@Test
	public void testAsSet_defaultBehavior()
	{
		final Set<String> actual = CollectionUtils.asSet(this.arrTestData);
		
		this.verifyContent(actual, this.arrTestData);
	}
	
	/**
	 * Test method for {@link xdev.util.CollectionUtils#asLinkedSet(T[])}.
	 */
	@Test
	public void testAsLinkedSet_defaultBehavior()
	{
		final Set<String> actual = CollectionUtils.asLinkedSet(this.arrTestData);
		
		this.verifyContent(actual, this.arrTestData);
	}
	
	/**
	 * Test method for
	 * {@link xdev.util.CollectionUtils#addAll(java.util.Collection, E[])}.
	 */
	@Test
	public void testAddAll_defaultBehavior()
	{
		final Person p1 = new Person(this.arrTestData[0], this.arrTestData[1]);
		final Person p2 = new Person(this.arrTestData[0], this.arrTestData[1]);
		
		final RichPerson rp1 = new RichPerson();
		final RichPerson rp2 = new RichPerson();
		
		final Collection<Person> c = new ArrayList<>();
		c.add(p1);
		c.add(p2);
		
		CollectionUtils.addAll(c, rp1, rp2);
		// verify size
		assertEquals(c.size(), 4);
	}
	
	/**
	 * Test method for
	 * {@link xdev.util.CollectionUtils#removeAll(java.util.Collection, E[])}.
	 */
	@Test
	public void testRemoveAll()
	{
		final Person p1 = new Person(this.arrTestData[0], this.arrTestData[1]);
		final Person p2 = new Person(this.arrTestData[0], this.arrTestData[1]);
		
		final RichPerson rp1 = new RichPerson();
		final RichPerson rp2 = new RichPerson();
		
		final Collection<Person> c = new ArrayList<>();
		
		assertEquals(CollectionUtils.removeAll(c, rp1, rp2), 0);
		
		c.add(p1);
		c.add(rp2);
		c.add(p2);
		c.add(rp1);
		
		assertEquals(CollectionUtils.removeAll(c, rp1, rp2), 2);
	}
	
	/**
	 * Test method for
	 * {@link xdev.util.CollectionUtils#remove(java.util.Map, java.lang.Object)}
	 * .
	 */
	@Test
	public void testRemove()
	{
		final Map<Integer, String> map = new HashMap<>();
		map.put(0, this.arrTestData[0]);
		map.put(1, this.arrTestData[1]);
		map.put(2, this.arrTestData[2]);
		map.put(3, this.arrTestData[1]);
		
		int removed = CollectionUtils.remove(map, this.arrTestData[0]);
		assertEquals(map.size(), 3);
		assertEquals(removed, 1);
		
		removed = CollectionUtils.remove(map, this.arrTestData[1]);
		assertEquals(map.size(), 1);
		assertEquals(removed, 2);
	}
	
	/**
	 * Test method for
	 * {@link xdev.util.CollectionUtils#addIfNotInList(java.util.List, java.util.List)}
	 * .
	 */
	@Test
	public void testAddIfNotInListListOfQListOfQ()
	{
		final ArrayList<String> names = new NameList();
		final ArrayList<String> newNames = new ArrayList<>();
		newNames.add("XDEV3");
		newNames.add("XDEV2");
		newNames.add(names.get(0));
		
		final int actual = CollectionUtils.addIfNotInList(names, newNames);
		assertEquals(2, actual);
	}
	
	/**
	 * Test method for
	 * {@link xdev.util.CollectionUtils#addIfNotInList(java.util.List, java.util.List, xdev.util.Comparer)}
	 * .
	 */
	@Test
	public void testAddIfNotInListListOfEListOfEComparerOfE()
	{
		final ArrayList<String> names = new NameList();
		final ArrayList<String> newNames = new ArrayList<>();
		newNames.add("XDEV3");
		newNames.add("XDEV2");
		newNames.add(names.get(0));
		
		final int actual = CollectionUtils.addIfNotInList(names, newNames, new StringComparer());
		assertEquals(2, actual);
	}
	
	/**
	 * Test method for
	 * {@link xdev.util.CollectionUtils#addIfNotInList(java.util.List, java.lang.Object)}
	 * .
	 */
	@Test
	public void testAddIfNotInListListObject()
	{
		final ArrayList<String> names = new NameList();
		final String addedString = "XDEV3";
		
		assertEquals(true, CollectionUtils.addIfNotInList(names, addedString));
		assertEquals(false, CollectionUtils.addIfNotInList(names, addedString));
	}
	
	/**
	 * Test method for
	 * {@link xdev.util.CollectionUtils#addIfNotInList(java.util.List, java.lang.Object, xdev.util.Comparer)}
	 * .
	 */
	@Test
	public void testAddIfNotInListListOfEEComparerOfE()
	{
		final ArrayList<String> names = new NameList();
		
		boolean actual = CollectionUtils.addIfNotInList(names, "XDEV3", new StringComparer());
		assertEquals(true, actual);
		actual = CollectionUtils.addIfNotInList(names, "XDEV3", new StringComparer());
		assertEquals(false, actual);
	}
	
	/**
	 * Test method for
	 * {@link xdev.util.CollectionUtils#contains(java.util.List, java.lang.Object, xdev.util.Comparer)}
	 * .
	 */
	@Test
	public void testContains()
	{
		final ArrayList<Person> persons = new PersonList();
		Person searchPerson = new Person("Egon", "Mustermann");
		
		assertEquals(false, CollectionUtils.contains(persons, searchPerson, searchPerson));
		
		searchPerson = new Person("Felix", "Mustermann");
		assertEquals(true, CollectionUtils.contains(persons, searchPerson, searchPerson));
	}
	
	/**
	 * Test method for
	 * {@link xdev.util.CollectionUtils#contains(java.util.List, java.lang.Object, xdev.util.Comparer)}
	 * .
	 */
	@Test(expected = NullPointerException.class)
	public void testContains_ParamComparerNullPointerException()
	{
		final ArrayList<Person> persons = new PersonList();
		final Person searchPerson = new Person("Egon", "Mustermann");
		CollectionUtils.contains(persons, searchPerson, null);
	}
	
	/**
	 * Test method for
	 * {@link xdev.util.CollectionUtils#indexOf(java.util.List, java.lang.Object, xdev.util.Comparer)}
	 * .
	 */
	@Test
	public void testIndexOfListOfEEComparerOfE()
	{
		final int indexOfElement = 2;
		final List<Person> persons = new PersonList();
		final Person p = persons.get(indexOfElement);
		
		assertEquals(indexOfElement, CollectionUtils.indexOf(persons, p, p));
	}
	
	/**
	 * Test method for
	 * {@link xdev.util.CollectionUtils#indexOf(java.util.List, java.lang.Object, xdev.util.Comparer)}
	 * .
	 */
	@Test(expected = NullPointerException.class)
	public void testIndexOfListOfEEComparerOfE_ParamComparerNullPointerException()
	{
		final List<Person> persons = new PersonList();
		final Person p = persons.get(2);
		CollectionUtils.indexOf(persons, p, (Comparer<Person>)null);
	}
	
	/**
	 * Test method for
	 * {@link xdev.util.CollectionUtils#deleteDoubleValues(java.util.List)}.
	 */
	@Test
	public void testDeleteDoubleValues()
	{
		final List<String> list = new NameList();
		final int normalSize = list.size();
		list.addAll(list);
		
		assertEquals(normalSize * 2, list.size());
		CollectionUtils.deleteDoubleValues(list);
		assertEquals(normalSize, list.size());
	}
	
	/**
	 * Test method for
	 * {@link xdev.util.CollectionUtils#hasDoubleValues(java.util.List)}.
	 */
	@Test
	public void testHasDoubleValues()
	{
		final List<String> list = new NameList();
		assertFalse(CollectionUtils.hasDoubleValues(list));
		list.addAll(list);
		assertTrue(CollectionUtils.hasDoubleValues(list));
	}
	
	/**
	 * Test method for
	 * {@link xdev.util.CollectionUtils#indexOf(java.util.List, java.lang.Object, int[])}
	 * .
	 */
	@Test
	@Ignore
	public void testIndexOfListObjectIntArray()
	{
		fail("Not yet implemented - No JavaDoc"); // TODO
	}
	
	/**
	 * Test method for
	 * {@link xdev.util.CollectionUtils#indicesOf(java.util.List, T[])}.
	 */
	@Test
	public void testIndicesOf()
	{
		final List<String> list = new NameList();
		final int index1 = 2;
		final String expected1 = list.get(index1);
		final int index2 = 3;
		final String expected2 = list.get(index2);
		
		final int[] indices = CollectionUtils.indicesOf(list, new String[]{expected1, expected2});
		
		assertEquals(2, indices.length);
		assertEquals(index1, indices[0]);
		assertEquals(index2, indices[1]);
	}
	
	/**
	 * Test method for
	 * {@link xdev.util.CollectionUtils#indicesOf(java.util.List, T[])}.
	 */
	@Test(expected = NullPointerException.class)
	public void testIndicesOf_ParamListNullPointerException()
	{
		CollectionUtils.indicesOf(null, "Test");
	}
	
	/**
	 * Test method for
	 * {@link xdev.util.CollectionUtils#frequency(java.util.Collection, java.lang.Object)}
	 * .
	 */
	@Test
	public void testFrequency()
	{
		this.collectionNames.addAll(this.collectionNames);
		assertEquals(2, CollectionUtils.frequency(this.collectionNames, this.arrTestData[0]));
	}
	
	/**
	 * Test method for
	 * {@link xdev.util.CollectionUtils#frequency(java.util.Collection, java.lang.Object)}
	 * .
	 */
	@Test(expected = NullPointerException.class)
	public void testFrequency_ParamCNullPointerException()
	{
		CollectionUtils.frequency(null, null);
	}
	
	/**
	 * Test method for
	 * {@link xdev.util.CollectionUtils#swap(java.util.List, int, int)}.
	 */
	@Test
	public void testSwap()
	{
		final List<String> names = new NameList();
		
		final int index1 = 0;
		final int index2 = 1;
		
		final String name1 = names.get(index1);
		final String name2 = names.get(index2);
		
		CollectionUtils.swap(names, index1, index2);
		
		final String name1After = names.get(index1);
		final String name2After = names.get(index2);
		
		assertEquals(name1, name2After);
		assertEquals(name2, name1After);
	}
	
	/**
	 * Test method for
	 * {@link xdev.util.CollectionUtils#swap(java.util.List, int, int)}.
	 */
	@Test(expected = IndexOutOfBoundsException.class)
	public void testSwap_ParamIndex1GreaterIndexOutOfBoundsException()
	{
		final List<String> names = new NameList();
		CollectionUtils.swap(names, names.size() + 1, 1);
	}
	
	/**
	 * Test method for
	 * {@link xdev.util.CollectionUtils#swap(java.util.List, int, int)}.
	 */
	@Test(expected = IndexOutOfBoundsException.class)
	public void testSwap_ParamIndex1NegativeIndexOutOfBoundsException()
	{
		final List<String> names = new NameList();
		CollectionUtils.swap(names, -1, 1);
	}
	
	/**
	 * Test method for
	 * {@link xdev.util.CollectionUtils#swap(java.util.List, int, int)}.
	 */
	@Test(expected = IndexOutOfBoundsException.class)
	public void testSwap_ParamIndex2GreaterIndexOutOfBoundsException()
	{
		final List<String> names = new NameList();
		CollectionUtils.swap(names, 0, names.size() + 1);
	}
	
	/**
	 * Test method for
	 * {@link xdev.util.CollectionUtils#swap(java.util.List, int, int)}.
	 */
	@Test(expected = IndexOutOfBoundsException.class)
	public void testSwap_ParamIndex2NegativeIndexOutOfBoundsException()
	{
		final List<String> names = new NameList();
		CollectionUtils.swap(names, 0, -1);
	}
	
	/**
	 * Test method for
	 * {@link xdev.util.CollectionUtils#move(java.util.List, int, boolean)}.
	 */
	@Test
	@Ignore
	public void testMoveListIntBoolean()
	{
		fail("Not yet implemented"); // TODO
	}
	
	/**
	 * Test method for
	 * {@link xdev.util.CollectionUtils#move(java.util.List, int, int)}.
	 */
	@Test
	@Ignore
	public void testMoveListIntInt()
	{
		fail("Not yet implemented"); // TODO
	}
	
	/**
	 * Test method for
	 * {@link xdev.util.CollectionUtils#move(java.util.List, int, int, int)}.
	 */
	@Test
	@Ignore
	public void testMoveListIntIntInt()
	{
		fail("Not yet implemented  - No JavaDoc"); // TODO
	}
	
	/**
	 * Test method for
	 * {@link xdev.util.CollectionUtils#sortByName(java.util.List)}.
	 */
	@Test
	@Ignore
	public void testSortByName()
	{
		fail("Not yet implemented - No JavaDoc"); // TODO
	}
	
	/**
	 * Test method for
	 * {@link xdev.util.CollectionUtils#accumulate(java.util.Map, java.lang.Object, java.lang.Object)}
	 * .
	 */
	@Test
	public void testAccumulate()
	{
		final Map<Integer, List<Person>> map = new HashMap<>();
		final int key = 1;
		final Person value = new Person("Felix", "Mustermann");
		
		CollectionUtils.accumulate(map, key, value);
		
		assertEquals(1, map.size());
		assertTrue(map.get(key).contains(value));
	}
	
	/**
	 * Test method for
	 * {@link xdev.util.CollectionUtils#toMap(java.util.ResourceBundle)}.
	 */
	@Test
	@Ignore
	public void testToMap()
	{
		fail("Not yet implemented - No JavaDoc"); // TODO
	}
	
	/**
	 * Verify if the content of the <code>collection</code> and the
	 * <code>array</code> is the same.
	 * 
	 * @param <T>
	 * @param collection
	 * @param array
	 */
	private <T> void verifyContent(final Collection<T> collection, final T[] array)
	{
		assertEquals(collection.size(), array.length);
		
		for(int i = 0; i < array.length; i++)
		{
			this.verifyContent(collection, array[i]);
		}
	}
	
	/**
	 * Verify if the <code>collection</code> contains the <code>element</code>.
	 * The verification is done with {@link Assert#assertTrue(boolean)}.
	 * 
	 * @param <T>
	 * @param collection
	 * @param array
	 */
	private <T> void verifyContent(final Collection<T> collection, final T element)
	{
		assertTrue(collection.contains(element));
	}
}
