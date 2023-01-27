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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;


/**
 * Test for Class {@link ObjectUtils}.
 * 
 * @author XDEV Software (FHAE)
 *
 */
public class ObjectUtilsTest
{
	
	/**
	 * Test method for {@link xdev.util.ObjectUtils#equals(java.lang.Object, java.lang.Object)}.
	 * Both parameter o1 and o2 are null.
	 */
	@Test
	public void testEquals_NullValues()
	{
		assertTrue(ObjectUtils.equals(null, null));
	}
	
	/**
	 * Test method for {@link xdev.util.ObjectUtils#equals(java.lang.Object, java.lang.Object)}.
	 */
	@Test
	public void testEquals_defaultBehaviorWithArray()
	{
		final Integer[] arr1 = {1, 2, 3, 4};
		final Integer[] arr2 = {1, 2, 3, 4};
		
		assertTrue(ObjectUtils.equals(arr1, arr2));
	}
	
	/**
	 * Test method for {@link xdev.util.ObjectUtils#equals(java.lang.Object, java.lang.Object)}.
	 */
	@Test
	public void testEquals_defaultBehavior()
	{
		assertFalse(ObjectUtils.equals(null, ""));
		assertFalse(ObjectUtils.equals("", null));
	}
	
	/**
	 * Test method for {@link xdev.util.ObjectUtils#clone(java.lang.Cloneable)}.
	 */
	@Test
	public void testClone_NullValue()
	{
		assertNull(ObjectUtils.clone(null));
	}
	
	/**
	 * Test method for {@link xdev.util.ObjectUtils#clone(java.lang.Cloneable)}.
	 */
	@Test(expected = RuntimeException.class)
	public void testClone_RuntimeException()
	{
		class CloneTestClass implements Cloneable
		{
			@Override
			protected Object clone() throws CloneNotSupportedException
			{
				throw new CloneNotSupportedException("Method clone is not Supported");
			}
		}
		
		assertNull(ObjectUtils.clone(new CloneTestClass()));
	}
	
	/**
	 * Test method for {@link xdev.util.ObjectUtils#nullValue(java.lang.Object, java.lang.Object)}.
	 */
	@Test
	public void testNullValue_defaultBehaviorReturnValue()
	{
		final Double value = 20.10;
		final Double ifNull = 10.00;
		
		assertEquals(value, ObjectUtils.nullValue(value, ifNull));
	}
	
	/**
	 * Test method for {@link xdev.util.ObjectUtils#nullValue(java.lang.Object, java.lang.Object)}.
	 */
	@Test
	public void testNullValue_defaultBehaviorReturnIfNull()
	{
		final Double ifNull = 10.00;
		
		assertEquals(ifNull, ObjectUtils.nullValue(null, ifNull));
	}
	
	/**
	 * Test method for {@link xdev.util.ObjectUtils#nullValue(java.lang.Object, java.lang.Object)}.
	 * The {@link IllegalArgumentException} of this method is triggered.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testNullValue_IllegalArgumentException()
	{
		ObjectUtils.nullValue(null, null);
	}
	
}
