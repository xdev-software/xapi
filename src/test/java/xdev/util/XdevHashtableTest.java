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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;


/**
 * Tests for the Class {@link XdevHashtable}.
 * 
 * @author XDEV Software (FHAE)
 */
public class XdevHashtableTest
{
	
	public XdevHashtable<Integer, String> testHashtable = null;
	
	public final Integer KEY_1 = 1;
	public final Integer KEY_2 = 2;
	
	public final String VALUE_1 = "Value1";
	public final String VALUE_2 = "Value2";
	
	public final int SIZE_COUNT = 2;
	
	public final String SEPARATOR = "|";
	
	@Before
	public void init()
	{
		this.testHashtable = new XdevHashtable<>();
		this.testHashtable.put(this.KEY_1, this.VALUE_1);
		this.testHashtable.put(this.KEY_2, this.VALUE_2);
	}
	
	/**
	 * Test method for {@link xdev.util.XdevHashtable#clear()}.
	 */
	@Test
	public void testClear()
	{
		assertEquals(this.SIZE_COUNT, this.testHashtable.size());
		
		this.testHashtable.clear();
		
		assertEquals(0, this.testHashtable.size());
		
	}
	
	/**
	 * Test method for {@link xdev.util.XdevHashtable#XdevHashtable()}.
	 */
	@Test
	public void testXdevHashtable_defaultConstructor()
	{
		final XdevHashtable<String, String> xHashTable = new XdevHashtable<>();
		assertNotNull(xHashTable);
	}
	
	/**
	 * Test method for {@link xdev.util.XdevHashtable#XdevHashtable(int)}.
	 */
	@Test
	public void testXdevHashtableInt()
	{
		final XdevHashtable<String, String> xHashTable = new XdevHashtable<>(10);
		assertNotNull(xHashTable);
	}
	
	/**
	 * Test method for {@link xdev.util.XdevHashtable#XdevHashtable(int)}.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testXdevHashtableInt_ParamCapacityIllegalArgumentException()
	{
		new XdevHashtable<String, String>(-1);
	}
	
	/**
	 * Test method for {@link xdev.util.XdevHashtable#XdevHashtable(java.util.Map)}.
	 */
	@Test
	public void testXdevHashtableMapOfQextendsKQextendsV()
	{
		final Map<Integer, String> hashMap = new HashMap<>();
		hashMap.put(this.KEY_1, this.VALUE_1);
		hashMap.put(this.KEY_2, this.VALUE_2);
		
		final XdevHashtable<Integer, String> xHashtable = new XdevHashtable<>(hashMap);
		
		assertNotNull(xHashtable);
		assertEquals(this.SIZE_COUNT, xHashtable.size());
		assertEquals(this.VALUE_1, xHashtable.get(this.KEY_1));
		assertEquals(this.VALUE_2, xHashtable.get(this.KEY_2));
	}
	
	/**
	 * Test method for {@link xdev.util.XdevHashtable#XdevHashtable(java.util.Map)}.
	 */
	@Test(expected = NullPointerException.class)
	public void testXdevHashtableMapOfQextendsKQextendsV_ParamMNullPointerException()
	{
		new XdevHashtable<>((Map<Integer, String>)null);
	}
	
	/**
	 * Test method for {@link xdev.util.XdevHashtable#XdevHashtable(java.lang.Object[])}.
	 */
	@Test
	public void testXdevHashtableObjectArray()
	{
		final XdevHashtable<Integer, String> xHashtable =
			new XdevHashtable<>(new Object[]{this.KEY_1, this.VALUE_1, this.KEY_2, this.VALUE_2});
		
		assertNotNull(xHashtable);
		assertEquals(this.SIZE_COUNT, xHashtable.size());
		assertEquals(this.VALUE_1, xHashtable.get(this.KEY_1));
		assertEquals(this.VALUE_2, xHashtable.get(this.KEY_2));
	}
	
	/**
	 * Test method for {@link xdev.util.XdevHashtable#XdevHashtable(java.lang.Object[])}.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testXdevHashtableObjectArray_ParamKeysAndValuesIllegalArgumentException()
	{
		new XdevHashtable<Integer, String>(new Object[]{this.KEY_1, this.VALUE_1, this.KEY_2});
	}
	
	/**
	 * Test method for {@link xdev.util.XdevHashtable#put(java.lang.Object, java.lang.Object)}.
	 */
	@Test
	public void testPutKV_defaultBehavior()
	{
		final XdevHashtable<Integer, String> xHashtable = new XdevHashtable<>();
		assertEquals(null, xHashtable.put(this.KEY_1, this.VALUE_1));
		assertEquals(null, xHashtable.put(this.KEY_2, this.VALUE_2));
		assertEquals(this.VALUE_2, xHashtable.put(this.KEY_2, "Value3"));
		
		assertNotNull(xHashtable);
		assertEquals(this.SIZE_COUNT, xHashtable.size());
		assertEquals(this.VALUE_1, xHashtable.get(this.KEY_1));
		assertEquals("Value3", xHashtable.get(this.KEY_2));
	}
	
	/**
	 * Test method for {@link xdev.util.XdevHashtable#putAll(java.util.Map)}.
	 */
	@Test
	public void testPutAllMapOfQextendsKQextendsV_defaultBehavior()
	{
		final Map<Integer, String> hashMap = new HashMap<>();
		hashMap.put(this.KEY_1, this.VALUE_1);
		hashMap.put(this.KEY_2, this.VALUE_2);
		
		final XdevHashtable<Integer, String> xHashtable = new XdevHashtable<>();
		xHashtable.putAll(hashMap);
		
		assertNotNull(xHashtable);
		assertEquals(this.SIZE_COUNT, xHashtable.size());
		assertEquals(this.VALUE_1, xHashtable.get(this.KEY_1));
		assertEquals(this.VALUE_2, xHashtable.get(this.KEY_2));
	}
	
	/**
	 * Test method for {@link xdev.util.XdevHashtable#remove(java.lang.Object)}.
	 */
	@Test
	public void testRemoveObject_defaultBehavior()
	{
		assertEquals(null, this.testHashtable.remove(0));
		assertEquals(this.VALUE_1, this.testHashtable.remove(this.KEY_1));
		assertEquals(this.VALUE_2, this.testHashtable.remove(this.KEY_2));
		
		assertNotNull(this.testHashtable);
		assertEquals(0, this.testHashtable.size());
	}
	
	/**
	 * Test method for {@link xdev.util.XdevHashtable#getKeys()}.
	 */
	@Test
	public void testGetKeys_defaultBehavior()
	{
		final XdevList<Integer> keys = this.testHashtable.getKeys();
		
		assertNotNull(keys);
		assertEquals(this.SIZE_COUNT, keys.size());
	}
	
	/**
	 * Test method for {@link xdev.util.XdevHashtable#getValues()}.
	 */
	@Test
	public void testGetValues_defaultBehavior()
	{
		final XdevList<String> values = this.testHashtable.getValues();
		
		assertNotNull(values);
		assertEquals(this.SIZE_COUNT, values.size());
	}
	
	/**
	 * Test method for {@link xdev.util.XdevHashtable#resetCursor()}.
	 */
	@Test
	public void testResetCursor_defaultBehavior()
	{
		this.testHashtable.next();
		final Integer firstKey = this.testHashtable.getKey();
		
		this.testHashtable.resetCursor();
		
		this.testHashtable.next();
		final Integer secondKey = this.testHashtable.getKey();
		
		assertEquals(firstKey, secondKey);
	}
	
	/**
	 * Test method for {@link xdev.util.XdevHashtable#next()}.
	 */
	@Test
	public void testNext_defaultBehavior()
	{
		assertTrue(this.testHashtable.next());
		this.testHashtable.clear();
		assertFalse(this.testHashtable.next());
	}
	
	/**
	 * Test method for {@link xdev.util.XdevHashtable#getKey()}.
	 */
	@Test
	public void testGetKey()
	{
		this.testHashtable.next();
		final Integer key = this.testHashtable.getKey();
		assertNotNull(key);
	}
	
	/**
	 * Test method for {@link xdev.util.XdevHashtable#getValue()}.
	 */
	@Test
	public void testGetValue()
	{
		this.testHashtable.next();
		final String val = this.testHashtable.getValue();
		assertNotNull(val);
	}
	
	/**
	 * Test method for {@link xdev.util.XdevHashtable#toString()}.
	 */
	@Test
	public void testToString_defaultBehavior()
	{
		assertEquals("2=Value2, 1=Value1", this.testHashtable.toString());
	}
	
	/**
	 * Test method for {@link xdev.util.XdevHashtable#toString(java.lang.String)}.
	 */
	@Test
	public void testToStringString_defaultBehavior()
	{
		
		assertEquals("2=Value2|1=Value1", this.testHashtable.toString(this.SEPARATOR));
	}
	
	/**
	 * Test method for {@link xdev.util.XdevHashtable#copy()}.
	 */
	@Test
	public void testCopy_defaultBehavior()
	{
		final XdevHashtable<Integer, String> copyHashtable = this.testHashtable.copy();
		
		assertNotNull(copyHashtable);
		assertEquals(this.SIZE_COUNT, copyHashtable.size());
		assertEquals(this.VALUE_1, copyHashtable.get(this.KEY_1));
		assertEquals(this.VALUE_2, copyHashtable.get(this.KEY_2));
	}
	
	/**
	 * Test method for {@link xdev.util.XdevHashtable#clone()}.
	 */
	@Test
	public void testClone_defaultBehavior()
	{
		final XdevHashtable<Integer, String> cloneHashtable = this.testHashtable.clone();
		
		assertNotNull(cloneHashtable);
		assertEquals(this.SIZE_COUNT, cloneHashtable.size());
		assertEquals(this.VALUE_1, cloneHashtable.get(this.KEY_1));
		assertEquals(this.VALUE_2, cloneHashtable.get(this.KEY_2));
	}
	
}
