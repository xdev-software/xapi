/**
 * 
 */
package xdev.util;

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

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.util.Collection;

import org.junit.Test;


/**
 * Tests for the Class {@link MathUtils}.
 * 
 * @author XDEV Softare (FHAE)
 *
 */
public class MathUtilsTest
{
	
	public static final double DEFAULT_DOUBLE_DELTA = 0.00;
	
	/**
	 * Test method for {@link xdev.util.MathUtils#round(double, int)}.
	 */
	@Test
	public void testRound_defaultBehaviorPositiveValue()
	{
		final double expected = 1020.30;
		final double actual = MathUtils.round(1020.3044, 2);
		assertEquals(expected, actual, DEFAULT_DOUBLE_DELTA);
	}
	
	/**
	 * Test method for {@link xdev.util.MathUtils#round(double, int)}.
	 */
	@Test
	public void testRound_defaultBehaviorNegativeValue()
	{
		final double expected = -1020.30;
		final double actual = MathUtils.round(-1020.3044, 2);
		assertEquals(expected, actual, DEFAULT_DOUBLE_DELTA);
	}
	
	/**
	 * Test method for {@link xdev.util.MathUtils#getDecimals(double, int)}.
	 */
	@Test
	public void testGetDecimals_defaultBehavior()
	{
		final int expected = 43590;
		final int actual = MathUtils.getDecimals(203.4359, 5);
		assertEquals(expected, actual, DEFAULT_DOUBLE_DELTA);
	}
	
	/**
	 * Test method for {@link xdev.util.MathUtils#sum(java.util.Collection)}.
	 */
	@Test
	public void testSumCollectionOfQextendsNumber_PositiveValues()
	{
		final Collection<Number> colValues = new XdevList<>();
		colValues.add(200);
		colValues.add(5.20);
		colValues.add(20);
		colValues.add(0);
		
		assertEquals(225.20, MathUtils.sum(colValues), DEFAULT_DOUBLE_DELTA);
	}
	
	/**
	 * Test method for {@link xdev.util.MathUtils#sum(java.util.Collection)}.
	 */
	@Test
	public void testSumCollectionOfQextendsNumber_NegativeValues()
	{
		final Collection<Number> colValues = new XdevList<>();
		colValues.add(-200);
		colValues.add(-5.20);
		colValues.add(-20);
		colValues.add(0);
		
		assertEquals(-225.20, MathUtils.sum(colValues), DEFAULT_DOUBLE_DELTA);
	}
	
	/**
	 * Test method for {@link xdev.util.MathUtils#sum(java.util.Collection)}.
	 */
	@Test
	public void testSumCollectionOfQextendsNumber_MixedValues()
	{
		final Collection<Number> colValues = new XdevList<>();
		colValues.add(200);
		colValues.add(5.20);
		colValues.add(-20.25);
		colValues.add(-120);
		colValues.add(0);
		colValues.add(250.30);
		
		assertEquals(315.25, MathUtils.sum(colValues), DEFAULT_DOUBLE_DELTA);
	}
	
	/**
	 * Test method for {@link xdev.util.MathUtils#sum(java.util.Collection)}.
	 */
	@Test
	public void testSumCollectionOfQextendsNumber_NullValues()
	{
		final Collection<Number> colValues = new XdevList<>();
		colValues.add(200);
		colValues.add(null);
		colValues.add(5.20);
		colValues.add(null);
		colValues.add(20);
		colValues.add(null);
		colValues.add(0);
		
		assertEquals(225.20, MathUtils.sum(colValues), DEFAULT_DOUBLE_DELTA);
	}
	
	/**
	 * Test method for {@link xdev.util.MathUtils#sum(java.util.Collection)}.
	 */
	@Test
	public void testSumCollectionOfQextendsNumber_EmptyCollection()
	{
		final Collection<Number> colValues = new XdevList<>();
		
		assertEquals(0, MathUtils.sum(colValues), DEFAULT_DOUBLE_DELTA);
	}
	
	/**
	 * Test method for {@link xdev.util.MathUtils#sum(java.util.Collection)}.
	 */
	@Test
	public void testSumCollectionOfQextendsNumber_NullCollection()
	{
		assertEquals(0, MathUtils.sum((Collection<Number>)null), DEFAULT_DOUBLE_DELTA);
	}
	
	/**
	 * Test method for {@link xdev.util.MathUtils#sum(int[])}.
	 */
	@Test
	public void testSumIntArray_PositiveValues()
	{
		assertEquals(225, MathUtils.sum(new int[]{200, 5, 20, 0}), DEFAULT_DOUBLE_DELTA);
	}
	
	/**
	 * Test method for {@link xdev.util.MathUtils#sum(int[])}.
	 */
	@Test
	public void testSumIntArray_NegativeValues()
	{
		assertEquals(-225, MathUtils.sum(new int[]{-200, -5, -20, 0}), DEFAULT_DOUBLE_DELTA);
	}
	
	/**
	 * Test method for {@link xdev.util.MathUtils#sum(int[])}.
	 */
	@Test
	public void testSumIntArray_MixedValues()
	{
		assertEquals(315, MathUtils.sum(new int[]{200, 5, -20, -120, 250}), DEFAULT_DOUBLE_DELTA);
	}
	
	/**
	 * Test method for {@link xdev.util.MathUtils#sum(int[])}.
	 */
	@Test
	public void testSumIntArray_EmptyArray()
	{
		assertEquals(0, MathUtils.sum(new int[]{}), DEFAULT_DOUBLE_DELTA);
	}
	
	/**
	 * Test method for {@link xdev.util.MathUtils#sum(int[])}.
	 */
	@Test
	public void testSumIntArray_NullArray()
	{
		assertEquals(0, MathUtils.sum((int[])null), DEFAULT_DOUBLE_DELTA);
	}
	
	/**
	 * Test method for {@link xdev.util.MathUtils#sum(long[])}.
	 */
	@Test
	public void testSumLongArray_PositiveValues()
	{
		assertEquals(225, MathUtils.sum(new long[]{200, 5, 20, 0}), DEFAULT_DOUBLE_DELTA);
	}
	
	/**
	 * Test method for {@link xdev.util.MathUtils#sum(long[])}.
	 */
	@Test
	public void testSumLongArray_NegativeValues()
	{
		assertEquals(-225, MathUtils.sum(new long[]{-200, -5, -20, 0}), DEFAULT_DOUBLE_DELTA);
	}
	
	/**
	 * Test method for {@link xdev.util.MathUtils#sum(long[])}.
	 */
	@Test
	public void testSumLongArray_MixedValues()
	{
		assertEquals(315, MathUtils.sum(new long[]{200, 5, -20, -120, 250}), DEFAULT_DOUBLE_DELTA);
	}
	
	/**
	 * Test method for {@link xdev.util.MathUtils#sum(long[])}.
	 */
	@Test
	public void testSumLongArray_EmptyArray()
	{
		assertEquals(0, MathUtils.sum(new long[]{}), DEFAULT_DOUBLE_DELTA);
	}
	
	/**
	 * Test method for {@link xdev.util.MathUtils#sum(long[])}.
	 */
	@Test
	public void testSumLongArray_NullArray()
	{
		assertEquals(0, MathUtils.sum((long[])null), DEFAULT_DOUBLE_DELTA);
	}
	
	/**
	 * Test method for {@link xdev.util.MathUtils#sum(double[])}.
	 */
	@Test
	public void testSumDoubleArray_PositiveValues()
	{
		assertEquals(225, MathUtils.sum(new double[]{200, 5, 20, 0}), DEFAULT_DOUBLE_DELTA);
	}
	
	/**
	 * Test method for {@link xdev.util.MathUtils#sum(double[])}.
	 */
	@Test
	public void testSumDoubleArray_NegativeValues()
	{
		assertEquals(-225.20, MathUtils.sum(new double[]{-200, -5.20, -20, 0}), DEFAULT_DOUBLE_DELTA);
	}
	
	/**
	 * Test method for {@link xdev.util.MathUtils#sum(double[])}.
	 */
	@Test
	public void testSumDoubleArray_MixedValues()
	{
		assertEquals(315.25, MathUtils.sum(new double[]{200, 5.20, -20.25, -120, 250.30}), DEFAULT_DOUBLE_DELTA);
	}
	
	/**
	 * Test method for {@link xdev.util.MathUtils#sum(double[])}.
	 */
	@Test
	public void testSumDoubleArray_EmptyArray()
	{
		assertEquals(0, MathUtils.sum(new double[]{}), DEFAULT_DOUBLE_DELTA);
	}
	
	/**
	 * Test method for {@link xdev.util.MathUtils#sum(double[])}.
	 */
	@Test
	public void testSumDoubleArray_NullArray()
	{
		assertEquals(0, MathUtils.sum((double[])null), DEFAULT_DOUBLE_DELTA);
	}
	
	/**
	 * Test method for {@link xdev.util.MathUtils#average(java.util.Collection)}.
	 */
	@Test
	public void testAverageCollectionOfQextendsNumber_PositiveValues()
	{
		final Collection<Number> colValues = new XdevList<>();
		colValues.add(200);
		colValues.add(5.20);
		colValues.add(20);
		colValues.add(0);
		
		assertEquals(56.30, MathUtils.average(colValues), DEFAULT_DOUBLE_DELTA);
	}
	
	/**
	 * Test method for {@link xdev.util.MathUtils#average(java.util.Collection)}.
	 */
	@Test
	public void testAverageCollectionOfQextendsNumber_NegativeValues()
	{
		final Collection<Number> colValues = new XdevList<>();
		colValues.add(-200);
		colValues.add(-5.20);
		colValues.add(-20);
		colValues.add(0);
		
		assertEquals(-56.30, MathUtils.average(colValues), DEFAULT_DOUBLE_DELTA);
	}
	
	@Test
	public void testAverageCollectionOfQextendsNumber_MixedValues()
	{
		final Collection<Number> colValues = new XdevList<>();
		colValues.add(200);
		colValues.add(5.20);
		colValues.add(-20.25);
		colValues.add(-120);
		colValues.add(0);
		colValues.add(250.30);
		
		assertEquals(52.541666666666664, MathUtils.average(colValues), DEFAULT_DOUBLE_DELTA);
	}
	
	/**
	 * Test method for {@link xdev.util.MathUtils#average(java.util.Collection)}.
	 */
	@Test
	public void testAverageCollectionOfQextendsNumber_NullCollection()
	{
		assertEquals(0, MathUtils.average((Collection<Number>)null), DEFAULT_DOUBLE_DELTA);
	}
	
	/**
	 * Test method for {@link xdev.util.MathUtils#average(java.util.Collection)}.
	 */
	@Test
	public void testAverageCollectionOfQextendsNumber_NullValues()
	{
		final Collection<Number> colValues = new XdevList<>();
		colValues.add(200);
		colValues.add(null);
		colValues.add(5.20);
		colValues.add(null);
		colValues.add(20);
		colValues.add(null);
		colValues.add(0);
		
		assertEquals(56.30, MathUtils.average(colValues), DEFAULT_DOUBLE_DELTA);
	}
	
	/**
	 * Test method for {@link xdev.util.MathUtils#average(java.util.Collection)}.
	 */
	@Test
	public void testAverageCollectionOfQextendsNumber_EmptyCollection()
	{
		final Collection<Number> colValues = new XdevList<>();
		assertEquals(0, MathUtils.average(colValues), DEFAULT_DOUBLE_DELTA);
	}
	
	/**
	 * Test method for {@link xdev.util.MathUtils#average(int[])}.
	 */
	@Test
	public void testAverageIntArray_PositiveValues()
	{
		assertEquals(56.25, MathUtils.average(new int[]{200, 5, 20, 0}), 0);
	}
	
	/**
	 * Test method for {@link xdev.util.MathUtils#average(int[])}.
	 */
	@Test
	public void testAverageIntArray_NegativeValues()
	{
		assertEquals(-56.25, MathUtils.average(new int[]{-200, -5, -20, 0}), 0);
	}
	
	/**
	 * Test method for {@link xdev.util.MathUtils#average(int[])}.
	 */
	@Test
	public void testAverageIntArray_MixedValues()
	{
		assertEquals(63, MathUtils.average(new int[]{200, 5, -20, -120, 250}), DEFAULT_DOUBLE_DELTA);
	}
	
	/**
	 * Test method for {@link xdev.util.MathUtils#average(int[])}.
	 */
	@Test
	public void testAverageIntArray_EmptyArray()
	{
		assertEquals(0, MathUtils.average(new int[]{}), DEFAULT_DOUBLE_DELTA);
	}
	
	/**
	 * Test method for {@link xdev.util.MathUtils#average(int[])}.
	 */
	@Test
	public void testAverageIntArray_NullArray()
	{
		assertEquals(0, MathUtils.average((int[])null), DEFAULT_DOUBLE_DELTA);
	}
	
	/**
	 * Test method for {@link xdev.util.MathUtils#average(long[])}.
	 */
	@Test
	public void testAverageLongArray_PositiveValues()
	{
		assertEquals(555.25, MathUtils.average(new long[]{2005, 5, 210, 01}), 0);
	}
	
	/**
	 * Test method for {@link xdev.util.MathUtils#average(long[])}.
	 */
	@Test
	public void testAverageLongArray_NegativeValues()
	{
		assertEquals(-555.25, MathUtils.average(new long[]{-2005, -5, -210, -01}), 0);
	}
	
	/**
	 * Test method for {@link xdev.util.MathUtils#average(long[])}.
	 */
	@Test
	public void testAverageLongArray_MixedValues()
	{
		assertEquals(63, MathUtils.average(new long[]{200, 5, -20, -120, 250}), DEFAULT_DOUBLE_DELTA);
	}
	
	/**
	 * Test method for {@link xdev.util.MathUtils#average(long[])}.
	 */
	@Test
	public void testAverageLongArray_EmptyArray()
	{
		assertEquals(0, MathUtils.average(new long[]{}), DEFAULT_DOUBLE_DELTA);
	}
	
	/**
	 * Test method for {@link xdev.util.MathUtils#average(long[])}.
	 */
	@Test
	public void testAverageLongArray_NullArray()
	{
		assertEquals(0, MathUtils.average((long[])null), DEFAULT_DOUBLE_DELTA);
	}
	
	/**
	 * Test method for {@link xdev.util.MathUtils#average(double[])}.
	 */
	@Test
	public void testAverageDoubleArray_PositiveValues()
	{
		assertEquals(56.54, MathUtils.average(new double[]{200.35, 5, 20.81, 0}), 0);
	}
	
	/**
	 * Test method for {@link xdev.util.MathUtils#average(double[])}.
	 */
	@Test
	public void testAverageDoubleArray_NegativeValues()
	{
		assertEquals(-56.54, MathUtils.average(new double[]{-200.35, -5, -20.81, 0}), 0);
	}
	
	/**
	 * Test method for {@link xdev.util.MathUtils#average(double[])}.
	 */
	@Test
	public void testAverageDoubleArray_MixedValues()
	{
		assertEquals(
			13.089999999999998,
			MathUtils.average(new double[]{-200.36, -5, 20.81, 0, 250}),
			DEFAULT_DOUBLE_DELTA);
	}
	
	/**
	 * Test method for {@link xdev.util.MathUtils#average(double[])}.
	 */
	@Test
	public void testAverageDoubleArray_NullArray()
	{
		assertEquals(0, MathUtils.average((double[])null), DEFAULT_DOUBLE_DELTA);
	}
	
	/**
	 * Test method for {@link xdev.util.MathUtils#average(double[])}.
	 */
	@Test
	public void testAverageDoubleArray_EmptyArray()
	{
		assertEquals(0, MathUtils.average(new double[]{}), DEFAULT_DOUBLE_DELTA);
	}
	
	/**
	 * Test method for {@link xdev.util.MathUtils#min(byte[])}.
	 */
	@Test
	public void testMinByteArray_PositiveValues()
	{
		assertEquals((byte)0, MathUtils.min(new byte[]{25, 5, 20, 0, 1}));
		
	}
	
	/**
	 * Test method for {@link xdev.util.MathUtils#min(byte[])}.
	 */
	@Test
	public void testMinByteArray_NegativeValues()
	{
		assertEquals((byte)-25, MathUtils.min(new byte[]{-25, -5, -20, 0, -1}));
	}
	
	/**
	 * Test method for {@link xdev.util.MathUtils#min(byte[])}.
	 */
	@Test
	public void testMinByteArray_MixedValues()
	{
		assertEquals((byte)-25, MathUtils.min(new byte[]{-25, 5, 20, 0, -1, 25}));
	}
	
	/**
	 * Test method for {@link xdev.util.MathUtils#min(byte[])}.
	 */
	@Test
	public void testMinByteArray_EmptyArray()
	{
		assertEquals(Byte.MIN_VALUE, MathUtils.min(new byte[]{}), DEFAULT_DOUBLE_DELTA);
	}
	
	/**
	 * Test method for {@link xdev.util.MathUtils#min(byte[])}.
	 */
	@Test
	public void testMinByteArray_NullArray()
	{
		assertEquals(Byte.MIN_VALUE, MathUtils.min((byte[])null), DEFAULT_DOUBLE_DELTA);
	}
	
	/**
	 * Test method for {@link xdev.util.MathUtils#max(byte[])}.
	 */
	@Test
	public void testMaxByteArray_PositiveValues()
	{
		assertEquals((byte)25, MathUtils.max(new byte[]{25, 5, 20, 0, 1}));
	}
	
	/**
	 * Test method for {@link xdev.util.MathUtils#max(byte[])}.
	 */
	@Test
	public void testMaxByteArray_NegativeValues()
	{
		assertEquals((byte)0, MathUtils.max(new byte[]{-25, -5, -20, 0, -1}));
	}
	
	/**
	 * Test method for {@link xdev.util.MathUtils#max(byte[])}.
	 */
	@Test
	public void testMaxByteArray_MixedValues()
	{
		assertEquals((byte)25, MathUtils.max(new byte[]{-25, 5, 20, -0, -1, 25}));
	}
	
	/**
	 * Test method for {@link xdev.util.MathUtils#max(byte[])}.
	 */
	@Test
	public void testMaxByteArray_EmptyArray()
	{
		assertEquals(Byte.MAX_VALUE, MathUtils.max(new byte[]{}), DEFAULT_DOUBLE_DELTA);
	}
	
	/**
	 * Test method for {@link xdev.util.MathUtils#max(byte[])}.
	 */
	@Test
	public void testMaxByteArray_NullArray()
	{
		assertEquals(Byte.MAX_VALUE, MathUtils.max((byte[])null), DEFAULT_DOUBLE_DELTA);
	}
	
	/**
	 * Test method for {@link xdev.util.MathUtils#min(short[])}.
	 */
	@Test
	public void testMinShortArray_PositiveValues()
	{
		assertEquals((short)0, MathUtils.min(new short[]{25, 5, 2058, 0, 1}));
	}
	
	/**
	 * Test method for {@link xdev.util.MathUtils#min(short[])}.
	 */
	@Test
	public void testMinShortArray_NegativeValues()
	{
		assertEquals((short)-2058, MathUtils.min(new short[]{-25, -5, -2058, 0, -1}));
	}
	
	/**
	 * Test method for {@link xdev.util.MathUtils#min(short[])}.
	 */
	@Test
	public void testMinShortArray_MixedValues()
	{
		assertEquals((short)-25877, MathUtils.min(new short[]{-25877, 5, 20, -0, -1, 25}));
	}
	
	/**
	 * Test method for {@link xdev.util.MathUtils#min(short[])}.
	 */
	@Test
	public void testMinShortArray_EmptyArray()
	{
		assertEquals(Short.MIN_VALUE, MathUtils.min(new short[]{}), DEFAULT_DOUBLE_DELTA);
	}
	
	/**
	 * Test method for {@link xdev.util.MathUtils#min(short[])}.
	 */
	@Test
	public void testMinShortArray_NullArray()
	{
		assertEquals(Short.MIN_VALUE, MathUtils.min((short[])null), DEFAULT_DOUBLE_DELTA);
	}
	
	/**
	 * Test method for {@link xdev.util.MathUtils#max(short[])}.
	 */
	@Test
	public void testMaxShortArray_PositiveValues()
	{
		assertEquals((short)2058, MathUtils.max(new short[]{25, 5, 2058, 0, 1}));
	}
	
	/**
	 * Test method for {@link xdev.util.MathUtils#max(short[])}.
	 */
	@Test
	public void testMaxShortArray_NegativeValues()
	{
		assertEquals((short)0, MathUtils.max(new short[]{-25, -5, -2058, 0, -1}));
	}
	
	/**
	 * Test method for {@link xdev.util.MathUtils#max(short[])}.
	 */
	@Test
	public void testMaxShortArray_MixedValues()
	{
		assertEquals((short)25, MathUtils.max(new short[]{-25877, 5, 20, -0, -1, 25}));
	}
	
	/**
	 * Test method for {@link xdev.util.MathUtils#max(short[])}.
	 */
	@Test
	public void testMaxShortArray_EmptyArray()
	{
		assertEquals(Short.MAX_VALUE, MathUtils.max(new short[]{}), DEFAULT_DOUBLE_DELTA);
	}
	
	/**
	 * Test method for {@link xdev.util.MathUtils#max(short[])}.
	 */
	@Test
	public void testMaxShortArray_NullArray()
	{
		assertEquals(Short.MAX_VALUE, MathUtils.max((short[])null), DEFAULT_DOUBLE_DELTA);
	}
	
	/**
	 * Test method for {@link xdev.util.MathUtils#max(int[])}.
	 */
	@Test
	public void testMaxIntArray_PositiveValues()
	{
		assertEquals(21474836, MathUtils.max(new int[]{25, 5, 2058, 0, 1, 21474836}));
	}
	
	/**
	 * Test method for {@link xdev.util.MathUtils#max(int[])}.
	 */
	@Test
	public void testMaxIntArray_NegativeValues()
	{
		assertEquals(0, MathUtils.max(new int[]{-25, -5, -2058, 0, -21474836, -1}));
	}
	
	/**
	 * Test method for {@link xdev.util.MathUtils#max(int[])}.
	 */
	@Test
	public void testMaxIntArray_MixedValues()
	{
		assertEquals(21474836, MathUtils.max(new int[]{-25877, 5, 20, -0, -1, 25, 21474836}));
	}
	
	/**
	 * Test method for {@link xdev.util.MathUtils#max(int[])}.
	 */
	@Test
	public void testMaxIntArray_EmptyArray()
	{
		assertEquals(Integer.MAX_VALUE, MathUtils.max(new int[]{}), DEFAULT_DOUBLE_DELTA);
	}
	
	/**
	 * Test method for {@link xdev.util.MathUtils#max(int[])}.
	 */
	@Test
	public void testMaxIntArray_NullArray()
	{
		assertEquals(Integer.MAX_VALUE, MathUtils.max((int[])null), DEFAULT_DOUBLE_DELTA);
	}
	
	/**
	 * Test method for {@link xdev.util.MathUtils#min(int[])}.
	 */
	@Test
	public void testMinIntArray_PositiveValues()
	{
		assertEquals(0, MathUtils.min(new int[]{25, 5, 2058, 0, 1, 21474836}));
	}
	
	/**
	 * Test method for {@link xdev.util.MathUtils#min(int[])}.
	 */
	@Test
	public void testMinIntArray_NegativeValues()
	{
		assertEquals(-21474836, MathUtils.min(new int[]{-25, -5, -2058, 0, -21474836, -1}));
	}
	
	/**
	 * Test method for {@link xdev.util.MathUtils#min(int[])}.
	 */
	@Test
	public void testMinIntArray_MixedValues()
	{
		assertEquals(-25877, MathUtils.min(new int[]{-25877, 5, 20, 0, -1, 25, 21474836}));
	}
	
	/**
	 * Test method for {@link xdev.util.MathUtils#min(int[])}.
	 */
	@Test
	public void testMinIntArray_EmptyArray()
	{
		assertEquals(Integer.MIN_VALUE, MathUtils.min(new int[]{}), DEFAULT_DOUBLE_DELTA);
	}
	
	/**
	 * Test method for {@link xdev.util.MathUtils#min(int[])}.
	 */
	@Test
	public void testMinIntArray_NullArray()
	{
		assertEquals(Integer.MIN_VALUE, MathUtils.min((int[])null), DEFAULT_DOUBLE_DELTA);
	}
	
	/**
	 * Test method for {@link xdev.util.MathUtils#max(long[])}.
	 */
	@Test
	public void testMaxLongArray_PositiveValues()
	{
		assertEquals(92233720368547758l, MathUtils.max(new long[]{25, 5, 2058, 0, 1, 92233720368547758l}));
	}
	
	/**
	 * Test method for {@link xdev.util.MathUtils#max(long[])}.
	 */
	@Test
	public void testMaxLongArray_NegativeValues()
	{
		assertEquals(0, MathUtils.max(new long[]{-25, -5, -2058, 0, -92233720368547758l, -1}));
	}
	
	/**
	 * Test method for {@link xdev.util.MathUtils#max(long[])}.
	 */
	@Test
	public void testMaxLongArray_MixedValues()
	{
		assertEquals(92233720368547758l, MathUtils.max(new long[]{-25877, 5, 20, -0, -1, 25, 92233720368547758l}));
	}
	
	/**
	 * Test method for {@link xdev.util.MathUtils#max(long[])}.
	 */
	@Test
	public void testMaxLongArray_EmptyArray()
	{
		assertEquals(Long.MAX_VALUE, MathUtils.max(new long[]{}), DEFAULT_DOUBLE_DELTA);
	}
	
	/**
	 * Test method for {@link xdev.util.MathUtils#max(long[])}.
	 */
	@Test
	public void testMaxLongArray_NullArray()
	{
		assertEquals(Long.MAX_VALUE, MathUtils.max((long[])null), DEFAULT_DOUBLE_DELTA);
	}
	
	/**
	 * Test method for {@link xdev.util.MathUtils#min(long[])}.
	 */
	@Test
	public void testMinLongArray_PositiveValues()
	{
		assertEquals(0, MathUtils.min(new long[]{25, 5, 2058, 0, 1, 92233720368547758l}));
	}
	
	/**
	 * Test method for {@link xdev.util.MathUtils#min(long[])}.
	 */
	@Test
	public void testMinLongArray_NegativeValues()
	{
		assertEquals(-92233720368547758l, MathUtils.min(new long[]{-25, -5, -2058, 0, -92233720368547758l, -1}));
	}
	
	/**
	 * Test method for {@link xdev.util.MathUtils#min(long[])}.
	 */
	@Test
	public void testMinLongArray_MixedValues()
	{
		assertEquals(-25877, MathUtils.min(new long[]{-25877, 5, 20, 0, -1, 25, 92233720368547758l}));
	}
	
	/**
	 * Test method for {@link xdev.util.MathUtils#min(long[])}.
	 */
	@Test
	public void testMinLongArray_EmptyArray()
	{
		assertEquals(Long.MIN_VALUE, MathUtils.min(new long[]{}), DEFAULT_DOUBLE_DELTA);
	}
	
	/**
	 * Test method for {@link xdev.util.MathUtils#min(long[])}.
	 */
	@Test
	public void testMinLongArray_NullArray()
	{
		assertEquals(Long.MIN_VALUE, MathUtils.min((long[])null), DEFAULT_DOUBLE_DELTA);
	}
	
	/**
	 * Test method for {@link xdev.util.MathUtils#max(float[])}.
	 */
	@Test
	public void testMaxFloatArray_PositiveValues()
	{
		assertEquals(250.8577F, MathUtils.max(new float[]{250.8577F, 5, 2.718F, 0, 1, 20.187F}), 0);
	}
	
	/**
	 * Test method for {@link xdev.util.MathUtils#max(float[])}.
	 */
	@Test
	public void testMaxFloatArray_NegativeValues()
	{
		assertEquals(20.187F, MathUtils.max(new float[]{-250.8577F, -5, -2.718F, 0, -1, 20.187F}), 0);
	}
	
	/**
	 * Test method for {@link xdev.util.MathUtils#max(float[])}.
	 */
	@Test
	public void testMaxFloatArray_MixedValues()
	{
		assertEquals(250.8577F, MathUtils.max(new float[]{-25877.08F, 5, 250.8577F, 0, -1, 25, 20.187F}), 0);
	}
	
	/**
	 * Test method for {@link xdev.util.MathUtils#max(float[])}.
	 */
	@Test
	public void testMaxFloatArray_EmptyArray()
	{
		assertEquals(Float.MAX_VALUE, MathUtils.max(new float[]{}), DEFAULT_DOUBLE_DELTA);
	}
	
	/**
	 * Test method for {@link xdev.util.MathUtils#max(float[])}.
	 */
	@Test
	public void testMaxFloatArray_NullArray()
	{
		assertEquals(Float.MAX_VALUE, MathUtils.max((float[])null), DEFAULT_DOUBLE_DELTA);
	}
	
	/**
	 * Test method for {@link xdev.util.MathUtils#min(float[])}.
	 */
	@Test
	public void testMinFloatArray_PositiveValues()
	{
		assertEquals(0, MathUtils.min(new float[]{250.8577F, 5, 2.718F, 0, 1, 20.187F}), 0);
	}
	
	/**
	 * Test method for {@link xdev.util.MathUtils#min(float[])}.
	 */
	@Test
	public void testMinFloatArray_NegativeValues()
	{
		assertEquals(-250.8577F, MathUtils.min(new float[]{-250.8577F, -5, -2.718F, 0, -1, 20.187F}), 0);
	}
	
	/**
	 * Test method for {@link xdev.util.MathUtils#min(float[])}.
	 */
	@Test
	public void testMinFloatArray_MixedValues()
	{
		assertEquals(-25877.08F, MathUtils.min(new float[]{-25877.08F, 5, 250.8577F, 0, -1, 25, 20.187F}), 0);
	}
	
	/**
	 * Test method for {@link xdev.util.MathUtils#min(float[])}.
	 */
	@Test
	public void testMinFloatArray_EmptyArray()
	{
		assertEquals(Float.MIN_VALUE, MathUtils.min(new float[]{}), DEFAULT_DOUBLE_DELTA);
	}
	
	/**
	 * Test method for {@link xdev.util.MathUtils#min(float[])}.
	 */
	@Test
	public void testMinFloatArray_NullArray()
	{
		assertEquals(Float.MIN_VALUE, MathUtils.min((float[])null), DEFAULT_DOUBLE_DELTA);
	}
	
	/**
	 * Test method for {@link xdev.util.MathUtils#max(double[])}.
	 */
	@Test
	public void testMaxDoubleArray_PositiveValues()
	{
		assertEquals(250.8577, MathUtils.max(new double[]{250.8577, 5, 2.718F, 0, 1, 20.187}), 0);
	}
	
	/**
	 * Test method for {@link xdev.util.MathUtils#max(double[])}.
	 */
	@Test
	public void testMaxDoubleArray_NegativeValues()
	{
		assertEquals(20.187, MathUtils.max(new double[]{-250.8577, -5, -2.718, 0, -1, 20.187}), 0);
	}
	
	/**
	 * Test method for {@link xdev.util.MathUtils#max(double[])}.
	 */
	@Test
	public void testMaxDoubleArray_MixedValues()
	{
		assertEquals(250.8577, MathUtils.max(new double[]{-25877.08, 5, 250.8577, 0, -1, 25, 20.187}), 0);
	}
	
	/**
	 * Test method for {@link xdev.util.MathUtils#max(double[])}.
	 */
	@Test
	public void testMaxDoubleArray_EmptyArray()
	{
		assertEquals(Double.MAX_VALUE, MathUtils.max(new double[]{}), DEFAULT_DOUBLE_DELTA);
	}
	
	/**
	 * Test method for {@link xdev.util.MathUtils#max(double[])}.
	 */
	@Test
	public void testMaxDoubleArray_NullArray()
	{
		assertEquals(Double.MAX_VALUE, MathUtils.max((double[])null), DEFAULT_DOUBLE_DELTA);
	}
	
	/**
	 * Test method for {@link xdev.util.MathUtils#min(double[])}.
	 */
	@Test
	public void testMinDoubleArray_PositiveValues()
	{
		assertEquals(0, MathUtils.min(new double[]{250.8577, 5, 2.718, 0, 1, 20.187}), 0);
	}
	
	/**
	 * Test method for {@link xdev.util.MathUtils#min(double[])}.
	 */
	@Test
	public void testMinDoubleArray_NegativeValues()
	{
		assertEquals(-250.8577, MathUtils.min(new double[]{-250.8577, -5, -2.718, 0, -1, 20.187}), 0);
	}
	
	/**
	 * Test method for {@link xdev.util.MathUtils#min(double[])}.
	 */
	@Test
	public void testMinDoubleArray_MixedValues()
	{
		assertEquals(-25877.08, MathUtils.min(new double[]{-25877.08, 5, 250.8577, 0, -1, 25, 20.187}), 0);
	}
	
	/**
	 * Test method for {@link xdev.util.MathUtils#min(double[])}.
	 */
	@Test
	public void testMinDoubleArray_EmptyArray()
	{
		assertEquals(Double.MIN_VALUE, MathUtils.min(new double[]{}), DEFAULT_DOUBLE_DELTA);
	}
	
	/**
	 * Test method for {@link xdev.util.MathUtils#min(double[])}.
	 */
	@Test
	public void testMinDoubleArray_NullArray()
	{
		assertEquals(Double.MIN_VALUE, MathUtils.min((double[])null), DEFAULT_DOUBLE_DELTA);
	}
	
	/**
	 * Test method for {@link xdev.util.MathUtils#bcdAdd(double, double)}.
	 */
	@Test
	public void testBcdAdd_PositiveValues()
	{
		assertEquals(2106.7000000000003, MathUtils.bcdAdd(105.80, 2000.90), DEFAULT_DOUBLE_DELTA);
	}
	
	/**
	 * Test method for {@link xdev.util.MathUtils#bcdAdd(double, double)}.
	 */
	@Test
	public void testBcdAdd_NegativeValues()
	{
		assertEquals(-2106.7000000000003, MathUtils.bcdAdd(-105.80, -2000.90), DEFAULT_DOUBLE_DELTA);
	}
	
	/**
	 * Test method for {@link xdev.util.MathUtils#bcdAdd(double, double)}.
	 */
	@Test
	public void testBcdAdd_MixedValues()
	{
		assertEquals(1895.1000000000001, MathUtils.bcdAdd(-105.80, 2000.90), DEFAULT_DOUBLE_DELTA);
	}
	
	/**
	 * Test method for {@link xdev.util.MathUtils#bcdAdd(double, double)}.
	 */
	@Test
	public void testBcdAdd_ZeroValue()
	{
		assertEquals(2000.90, MathUtils.bcdAdd(2000.90, 0), DEFAULT_DOUBLE_DELTA);
	}
	
	/**
	 * Test method for {@link xdev.util.MathUtils#bcdSubtract(double, double)}.
	 */
	@Test
	public void testBcdSubtract_PositiveValues()
	{
		assertEquals(-1895.1000000000001, MathUtils.bcdSubtract(105.80, 2000.90), DEFAULT_DOUBLE_DELTA);
	}
	
	/**
	 * Test method for {@link xdev.util.MathUtils#bcdSubtract(double, double)}.
	 */
	@Test
	public void testBcdSubtract_NegativeValues()
	{
		assertEquals(1895.1000000000001, MathUtils.bcdSubtract(-105.80, -2000.90), DEFAULT_DOUBLE_DELTA);
	}
	
	/**
	 * Test method for {@link xdev.util.MathUtils#bcdSubtract(double, double)}.
	 */
	@Test
	public void testBcdSubtract_MixedValues()
	{
		assertEquals(2106.7000000000003, MathUtils.bcdSubtract(105.80, -2000.90), DEFAULT_DOUBLE_DELTA);
	}
	
	/**
	 * Test method for {@link xdev.util.MathUtils#bcdSubtract(double, double)}.
	 */
	@Test
	public void testBcdSubtract_ZeroValue()
	{
		assertEquals(-105.80, MathUtils.bcdSubtract(-105.80, 0), DEFAULT_DOUBLE_DELTA);
	}
	
	/**
	 * Test method for {@link xdev.util.MathUtils#bcdMultiply(double, double)}.
	 */
	@Test
	public void testBcdMultiply_PositiveValues()
	{
		assertEquals(1669.24, MathUtils.bcdMultiply(287.80, 5.8), DEFAULT_DOUBLE_DELTA);
	}
	
	/**
	 * Test method for {@link xdev.util.MathUtils#bcdMultiply(double, double)}.
	 */
	@Test
	public void testBcdMultiply_NegativeValues()
	{
		assertEquals(1669.24, MathUtils.bcdMultiply(-287.80, -5.8), DEFAULT_DOUBLE_DELTA);
	}
	
	/**
	 * Test method for {@link xdev.util.MathUtils#bcdMultiply(double, double)}.
	 */
	@Test
	public void testBcdMultiply_MixedValues()
	{
		assertEquals(-1669.24, MathUtils.bcdMultiply(-287.80, 5.8), DEFAULT_DOUBLE_DELTA);
	}
	
	/**
	 * Test method for {@link xdev.util.MathUtils#bcdMultiply(double, double)}.
	 */
	@Test
	public void testBcdMultiply_ZeroValue()
	{
		assertEquals(0.00, MathUtils.bcdMultiply(-287.80, 0), DEFAULT_DOUBLE_DELTA);
	}
	
	/**
	 * Test method for {@link xdev.util.MathUtils#bcdDivide(double, double)}.
	 */
	@Test
	public void testBcdDivideDoubleDouble_PositiveValues()
	{
		assertEquals(49.62068965517242, MathUtils.bcdDivide(287.80, 5.8), DEFAULT_DOUBLE_DELTA);
	}
	
	/**
	 * Test method for {@link xdev.util.MathUtils#bcdDivide(double, double)}.
	 */
	@Test
	public void testBcdDivideDoubleDouble_NegativeValues()
	{
		assertEquals(49.62068965517242, MathUtils.bcdDivide(-287.80, -5.8), DEFAULT_DOUBLE_DELTA);
	}
	
	/**
	 * Test method for {@link xdev.util.MathUtils#bcdDivide(double, double)}.
	 */
	@Test
	public void testBcdDivideDoubleDouble_MixedValues()
	{
		assertEquals(-49.62068965517242, MathUtils.bcdDivide(-287.80, 5.8), DEFAULT_DOUBLE_DELTA);
	}
	
	/**
	 * Test method for {@link xdev.util.MathUtils#bcdDivide(double, double)}.
	 */
	@Test(expected = ArithmeticException.class)
	public void testBcdDivideDoubleDouble_ParamDivisorZeroValueArithmeticException()
	{
		assertEquals(49.62068965517242, MathUtils.bcdDivide(287.80, 0), DEFAULT_DOUBLE_DELTA);
	}
	
	/**
	 * Test method for {@link xdev.util.MathUtils#bcdDivide(double, double, int)}.
	 */
	@Test
	public void testBcdDivideDoubleDoubleInt_PositiveValues()
	{
		assertEquals(57.56, MathUtils.bcdDivide(287.80, 5, BigDecimal.ROUND_UP), DEFAULT_DOUBLE_DELTA);
		assertEquals(57.56, MathUtils.bcdDivide(287.80, 5, BigDecimal.ROUND_DOWN), DEFAULT_DOUBLE_DELTA);
	}
	
	/**
	 * Test method for {@link xdev.util.MathUtils#bcdDivide(double, double, int)}.
	 */
	@Test
	public void testBcdDivideDoubleDoubleInt_NegativeValues()
	{
		assertEquals(57.56, MathUtils.bcdDivide(-287.80, -5, BigDecimal.ROUND_UP), DEFAULT_DOUBLE_DELTA);
		assertEquals(57.56, MathUtils.bcdDivide(-287.80, -5, BigDecimal.ROUND_DOWN), DEFAULT_DOUBLE_DELTA);
	}
	
	/**
	 * Test method for {@link xdev.util.MathUtils#bcdDivide(double, double, int)}.
	 */
	@Test
	public void testBcdDivideDoubleDoubleInt_MixedValues()
	{
		assertEquals(-57.56, MathUtils.bcdDivide(-287.80, 5, BigDecimal.ROUND_UP), DEFAULT_DOUBLE_DELTA);
		assertEquals(-57.56, MathUtils.bcdDivide(-287.80, 5, BigDecimal.ROUND_DOWN), DEFAULT_DOUBLE_DELTA);
	}
	
	/**
	 * Test method for {@link xdev.util.MathUtils#bcdDivide(double, double, int)}.
	 */
	@Test(expected = ArithmeticException.class)
	public void testBcdDivideDoubleDoubleInt__ParamDivisorZeroValueArithmeticException()
	{
		MathUtils.bcdDivide(287.80, 0, BigDecimal.ROUND_UP);
	}
	
	/**
	 * Test method for {@link xdev.util.MathUtils#checkRange(int, int, int)}.
	 */
	@Test
	public void testCheckRange_defaultBehavior()
	{
		MathUtils.checkRange(50, 1, 50);
		MathUtils.checkRange(50, 0, 50);
		MathUtils.checkRange(50, 1, 60);
		MathUtils.checkRange(50, 50, 55);
	}
	
	/**
	 * Test method for {@link xdev.util.MathUtils#checkRange(int, int, int)}.
	 */
	@Test(expected = IndexOutOfBoundsException.class)
	public void testCheckRange_ParamMinNegativeIndexOutOfBoundsException()
	{
		MathUtils.checkRange(50, -1, 0);
	}
	
	/**
	 * Test method for {@link xdev.util.MathUtils#checkRange(int, int, int)}.
	 */
	@Test(expected = IndexOutOfBoundsException.class)
	public void testCheckRange_ParamMinGreaterIndexOutOfBoundsException()
	{
		MathUtils.checkRange(50, 51, 60);
	}
	
	/**
	 * Test method for {@link xdev.util.MathUtils#checkRange(int, int, int)}.
	 */
	@Test(expected = IndexOutOfBoundsException.class)
	public void testCheckRange_ParamMaxNegativeIndexOutOfBoundsException()
	{
		MathUtils.checkRange(50, 0, -1);
	}
	
	/**
	 * Test method for {@link xdev.util.MathUtils#isInRange(int, int, int)}.
	 */
	@Test
	public void testIsInRange_PositiveValues()
	{
		assertEquals(Boolean.TRUE, MathUtils.isInRange(50, 1, 50));
		assertEquals(Boolean.TRUE, MathUtils.isInRange(50, 0, 50));
		assertEquals(Boolean.TRUE, MathUtils.isInRange(50, 1, 60));
		assertEquals(Boolean.TRUE, MathUtils.isInRange(50, 50, 55));
		
		assertEquals(Boolean.FALSE, MathUtils.isInRange(1, 50, 55));
		assertEquals(Boolean.FALSE, MathUtils.isInRange(20, 50, 55));
		assertEquals(Boolean.FALSE, MathUtils.isInRange(60, 50, 55));
		assertEquals(Boolean.FALSE, MathUtils.isInRange(80, 50, 55));
	}
	
	/**
	 * Test method for {@link xdev.util.MathUtils#isInRange(int, int, int)}.
	 */
	@Test
	public void testIsInRange_NegativeValues()
	{
		assertEquals(Boolean.TRUE, MathUtils.isInRange(50, -1, 50));
		assertEquals(Boolean.TRUE, MathUtils.isInRange(-50, -100, 50));
		assertEquals(Boolean.TRUE, MathUtils.isInRange(-150, -250, -100));
		
		assertEquals(Boolean.FALSE, MathUtils.isInRange(-1, 50, 55));
		assertEquals(Boolean.FALSE, MathUtils.isInRange(-20, -10, 55));
		assertEquals(Boolean.FALSE, MathUtils.isInRange(-60, -150, -70));
	}
	
	/**
	 * Test method for {@link xdev.util.MathUtils#computeHash(java.lang.Object[])}.
	 */
	@Test
	public void testComputeHash_defaultBehavior()
	{
		assertEquals(2689212, MathUtils.computeHash("XDEV"));
		assertEquals(6018, MathUtils.computeHash(5987));
		
	}
	
	/**
	 * Test method for {@link xdev.util.MathUtils#computeHashDeep(java.lang.Object[])}.
	 */
	@Test
	public void testComputeHashDeep_defaultBehavior()
	{
		assertEquals(87987, MathUtils.computeHashDeep(87956));
	}
	
}
