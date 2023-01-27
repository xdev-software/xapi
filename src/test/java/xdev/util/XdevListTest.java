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
package xdev.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.Collection;

import org.junit.Test;


/**
 * Tests for the Class {@link XdevList}.
 * 
 * @author XDEV Software (FHAE)
 */
public class XdevListTest
{
	
	/**
	 * Test method for {@link xdev.util.XdevList#XdevList()}.
	 */
	@Test
	public void testXdevList_defaultConstructor()
	{
		final XdevList<?> list = new XdevList<>();
		
		assertNotNull(list);
		assertEquals(10, list.capacity());
	}
	
	/**
	 * Test method for {@link xdev.util.XdevList#XdevList(V[])}.
	 */
	@Test
	public void testXdevListVArray_VarArgsConstructor()
	{
		final XdevList<Double> list = new XdevList<>(10.00, 20.00, 30.00);
		
		assertNotNull(list);
		assertEquals(3, list.size());
		assertEquals(10.00, list.get(0), 0.00);
		assertEquals(20.00, list.get(1), 0.00);
		assertEquals(30.00, list.get(2), 0.00);
	}
	
	/**
	 * Test method for {@link xdev.util.XdevList#XdevList(int)}.
	 */
	@Test
	public void testXdevListInt_CapacityConstructor()
	{
		final XdevList<?> list = new XdevList<>(50);
		
		assertNotNull(list);
		assertEquals(50, list.capacity());
		
	}
	
	/**
	 * Test method for {@link xdev.util.XdevList#XdevList(int)}.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testXdevListInt_ParamCapacityIllegalArgumentException()
	{
		new XdevList<>(-1);
	}
	
	/**
	 * Test method for {@link xdev.util.XdevList#XdevList(java.util.Collection)}.
	 */
	@Test
	public void testXdevListCollectionOfQextendsE_Constructor()
	{
		final Collection<Integer> intColl = new ArrayList<>();
		intColl.add(1);
		intColl.add(2);
		intColl.add(3);
		
		final XdevList<?> list = new XdevList<Object>(intColl);
		
		assertNotNull(list);
		assertEquals(3, list.capacity());
		assertEquals(3, list.size());
		assertEquals(1, list.get(0));
		assertEquals(2, list.get(1));
		assertEquals(3, list.get(2));
	}
	
	/**
	 * Test method for {@link xdev.util.XdevList#XdevList(java.util.Collection)}.
	 */
	@Test(expected = NullPointerException.class)
	public void testXdevListCollectionOfQextendsE_ParamCNullPointerException()
	{
		new XdevList<Object>((Collection<?>)null);
	}
	
	/**
	 * Test method for {@link xdev.util.XdevList#XdevList(int, int)}.
	 */
	@Test
	public void testXdevListIntInt_defaultBehavior()
	{
		final XdevList<Integer> list = new XdevList<>(2, 2);
		
		assertNotNull(list);
		assertEquals(2, list.capacity());
		
		list.add(1);
		list.add(2);
		assertEquals(2, list.size());
		
		list.add(3);
		assertEquals(4, list.capacity());
	}
	
	/**
	 * Test method for {@link xdev.util.XdevList#XdevList(int, int)}.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testXdevListIntInt_ParamCapacityIllegalArgumentException()
	{
		new XdevList<>(-1, -1);
	}
	
	/**
	 * Test method for {@link xdev.util.XdevList#deleteDoubleValues()}.
	 */
	@Test
	public void testDeleteDoubleValues_WithDoubleValues()
	{
		final XdevList<Integer> list = new XdevList<>(2, 3, 4, 4, 4);
		assertNotNull(list);
		assertEquals(2, list.deleteDoubleValues());
	}
	
	/**
	 * Test method for {@link xdev.util.XdevList#deleteDoubleValues()}.
	 */
	@Test
	public void testDeleteDoubleValues_NoDoubleValues()
	{
		final XdevList<Integer> list = new XdevList<>(2, 3, 4);
		assertNotNull(list);
		assertEquals(0, list.deleteDoubleValues());
	}
	
	/**
	 * Test method for {@link xdev.util.XdevList#insert(int, java.lang.Object)}.
	 */
	@Test
	public void testInsert_defaultBehavior()
	{
		final int index = 1;
		final XdevList<Integer> list = new XdevList<>(new Integer[]{1, 2});
		list.insert(index, 4);
		
		assertEquals(4, list.get(index).intValue());
	}
	
	/**
	 * Test method for {@link xdev.util.XdevList#insert(int, java.lang.Object)}.
	 */
	@Test(expected = ArrayIndexOutOfBoundsException.class)
	public void testInsert_ParamIndexNegativeArrayIndexOutOfBoundsException()
	{
		final XdevList<Integer> list = new XdevList<>();
		list.insert(-1, 1);
	}
	
	/**
	 * Test method for {@link xdev.util.XdevList#insert(int, java.lang.Object)}.
	 */
	@Test(expected = ArrayIndexOutOfBoundsException.class)
	public void testInsert_ParamIndexGreaterArrayIndexOutOfBoundsException()
	{
		final XdevList<Integer> list = new XdevList<>();
		list.insert(100, 1);
	}
	
	/**
	 * Test method for {@link xdev.util.XdevList#removeObject(java.lang.Object)}.
	 */
	@Test
	public void testRemoveObject_defaultBehavior()
	{
		final Integer toRemoveObj = 10;
		final XdevList<Integer> list = new XdevList<>(new Integer[]{1, 2, toRemoveObj});
		assertEquals(toRemoveObj, list.removeObject(toRemoveObj));
	}
	
	/**
	 * Test method for {@link xdev.util.XdevList#removeObject(java.lang.Object)}.
	 */
	@Test
	public void testRemoveObject_ObjectNotPresent()
	{
		final XdevList<Integer> list = new XdevList<>(new Integer[]{1, 2});
		assertEquals(null, list.removeObject(10));
	}
	
	/**
	 * Test method for {@link xdev.util.XdevList#getSeparator()}.
	 */
	@Test
	public void testGetSeparator_defaultBehavior()
	{
		final XdevList<Integer> list = new XdevList<>(new Integer[]{1, 2});
		final String separatorString = "/";
		list.setSeparator(separatorString);
		
		assertEquals(separatorString, list.getSeparator());
		assertEquals(list.get(0) + separatorString + list.get(1), list.toString());
	}
	
	/**
	 * Test method for {@link xdev.util.XdevList#setSeparator(java.lang.String)}.
	 */
	@Test
	public void testSetSeparator_defaultBehavior()
	{
		final XdevList<Integer> list = new XdevList<>(new Integer[]{1, 2});
		final String separatorString = "/";
		list.setSeparator(separatorString);
		
		assertEquals(separatorString, list.getSeparator());
		assertEquals(list.get(0) + separatorString + list.get(1), list.toString());
	}
	
	/**
	 * Test method for {@link xdev.util.XdevList#toString()}.
	 */
	@Test
	public void testToString_defaultBehavior()
	{
		final XdevList<Integer> list = new XdevList<>(new Integer[]{1, 2});
		final String separatorString = "/";
		list.setSeparator(separatorString);
		
		assertEquals(list.get(0) + separatorString + list.get(1), list.toString());
	}
	
	/**
	 * Test method for {@link xdev.util.XdevList#toString(java.lang.String)}.
	 */
	@Test
	public void testToStringString_defaultBehavior()
	{
		final XdevList<Integer> list = new XdevList<>(new Integer[]{1, 2});
		final String separatorString = "/";
		list.setSeparator(separatorString);
		
		assertEquals(list.get(0) + separatorString + list.get(1), list.toString());
	}
	
	/**
	 * Test method for {@link xdev.util.XdevList#copy()}.
	 */
	@Test
	public void testCopy_defaultBehavior()
	{
		final XdevList<Integer> listOrg = new XdevList<>(new Integer[]{1, 2});
		final XdevList<Integer> listCopy = listOrg.copy();
		
		assertEquals(listCopy.size(), listOrg.size());
		assertEquals(listCopy.get(0), listOrg.get(0));
		assertEquals(listCopy.get(1), listOrg.get(1));
		
	}
	
	/**
	 * Test method for {@link xdev.util.XdevList#clone()}.
	 */
	@Test
	public void testClone_defaultBehavior()
	{
		final XdevList<Integer> listOrg = new XdevList<>(new Integer[]{1, 2});
		final XdevList<Integer> listCopy = listOrg.clone();
		
		assertEquals(listCopy.size(), listOrg.size());
		assertEquals(listCopy.get(0), listOrg.get(0));
		assertEquals(listCopy.get(1), listOrg.get(1));
	}
	
}
