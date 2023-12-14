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

import java.util.Iterator;
import java.util.NoSuchElementException;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import xdev.util.dummies.Person;


/**
 * 
 * Tests for {@link ArrayUtils}.
 * 
 * @author jwill
 * 
 */

@SuppressWarnings("null")
public class ArrayUtilsTest
{
	
	/**
	 * Test for method {@link ArrayUtils#equals(Comparable[], Comparable[]).
	 */
	@Test
	public void testEqualsTArrayTArray_True()
	{
		final String[] testarr1 = new String[2];
		
		Assert.assertTrue(ArrayUtils.equals(testarr1, testarr1));
		
	}
	
	/**
	 * Test for method {@link ArrayUtils#equals(Comparable[], Comparable[]).
	 */
	@Test
	public void testEqualsTArrayTArray_two()
	{
		final String[] testarr1 = new String[2];
		final String[] testarr2 = new String[3];
		
		Assert.assertFalse(ArrayUtils.equals(testarr2, testarr1));
		
	}
	
	/**
	 * Test for method {@link ArrayUtils#equals(Comparable[], Comparable[]).
	 */
	@Test
	public void testEqualsTArrayTArray_three()
	{
		final String[] testarr1 = new String[1];
		final String[] testarr2 = new String[1];
		
		testarr1[0] = "hello";
		testarr2[0] = "bye";
		
		Assert.assertFalse(ArrayUtils.equals(testarr2, testarr1));
		
	}
	
	/**
	 * Test for method {@link ArrayUtils#equals(Comparable[], Comparable[]).
	 */
	@Test
	public void testEqualsTArrayTArray_four()
	{
		final String[] testarr1 = new String[1];
		final String[] testarr2 = null;
		
		testarr1[0] = "hello";
		
		Assert.assertFalse(ArrayUtils.equals(testarr2, testarr1));
		
	}
	
	/**
	 * Test for method {@link ArrayUtils#equals(Comparable[], Comparable[]).
	 */
	@Test
	public void testEqualsTArrayTArray_five()
	{
		final String[] testarr1 = new String[1];
		
		testarr1[0] = "hello";
		
		final String[] testarr2 = new String[1];
		
		testarr2[0] = "hello";
		
		Assert.assertTrue(ArrayUtils.equals(testarr2, testarr1));
		
	}
	
	@Test(expected = NullPointerException.class)
	public void testEqualsTArrayTArray_Ex()
	{
		final String[] testarr1 = {"test", null};
		final String[] testarr2 = {null, "test"};
		
		ArrayUtils.equals(testarr1, testarr2);
		
	}
	
	/**
	 * Test for method {@link ArrayUtils#contains(Object[], Object).
	 */
	@Test
	public void testContainsObjectArrayObject()
	{
		final Object b = "hello";
		
		final Object[] array = {"hello"};
		
		Assert.assertTrue(ArrayUtils.contains(array, b));
		
	}
	
	/**
	 * Test for method {@link ArrayUtils#contains(Object[], Object).
	 */
	@Test
	public void testContainsObjectArrayObject_two()
	{
		final Object[] testarr1 = new Object[1];
		testarr1[0] = "hello";
		
		Object b = new Object();
		b = "bye";
		
		Assert.assertFalse(ArrayUtils.contains(testarr1, b));
		
	}
	
	/**
	 * Test for method {@link ArrayUtils#indexOf(Object[], Object).
	 */
	@Test
	public void testIndexOfObjectArrayObject()
	{
		Object value = new Object();
		value = "hallo";
		
		final Object[] testarr1 = new Object[1];
		testarr1[0] = "hallo";
		
		final int expected = 0;
		
		Assert.assertEquals(expected, ArrayUtils.indexOf(testarr1, value));
		
	}
	
	/**
	 * Test for method {@link ArrayUtils#indexOf(Object[], Object).
	 */
	@Test
	public void testIndexOfObjectArrayObject_two()
	{
		final Object value = new Object();
		
		final Object[] testarr1 = new String[1];
		
		final int expected = -1;
		
		Assert.assertEquals(expected, ArrayUtils.indexOf(testarr1, value));
		
	}
	
	/**
	 * Test for method {@link ArrayUtils#indexOf(Object[], Object).
	 */
	@Test
	public void testIndexOfObjectArrayObject_three()
	{
		final Object value = null;
		
		final Object[] testarr1 = new String[1];
		testarr1[0] = value;
		
		final int expected = 0;
		
		Assert.assertEquals(expected, ArrayUtils.indexOf(testarr1, value));
		
	}
	
	/**
	 * Test for method {@link ArrayUtils#contains(boolean[], boolean).
	 */
	@Test
	public void testContainsBooleanArrayBoolean()
	{
		final boolean[] testarr1 = new boolean[1];
		testarr1[0] = true;
		
		boolean b;
		b = true;
		
		Assert.assertTrue(ArrayUtils.contains(testarr1, b));
	}
	
	/**
	 * Test for method {@link ArrayUtils#contains(boolean[], boolean).
	 */
	@Test
	public void testContainsBooleanArrayBoolean_two()
	{
		final boolean[] testarr1 = new boolean[1];
		testarr1[0] = true;
		
		boolean b;
		b = false;
		
		Assert.assertFalse(ArrayUtils.contains(testarr1, b));
	}
	
	/**
	 * Test for method {@link ArrayUtils#indexOf(boolean[], boolean).
	 */
	@Test
	public void testIndexOfBooleanArrayBoolean()
	{
		
		final boolean[] testarr1 = {true, false};
		
		final boolean b = true;
		
		final int expected = 0;
		
		Assert.assertEquals(expected, ArrayUtils.indexOf(testarr1, b));
		
	}
	
	/**
	 * Test for method {@link ArrayUtils#indexOf(boolean[], boolean).
	 */
	@Test
	public void testIndexOfBooleanArrayBoolean_two()
	{
		final boolean[] testarr1 = new boolean[1];
		testarr1[0] = false;
		
		final boolean b = true;
		
		final int expected = -1;
		
		Assert.assertEquals(expected, ArrayUtils.indexOf(testarr1, b));
		
	}
	
	/**
	 * Test for method {@link ArrayUtils#contains(char[], char).
	 */
	@Test
	public void testContainsCharArrayChar()
	{
		final char[] array = new char[1];
		
		array[0] = 'b';
		
		final char value = 'b';
		
		Assert.assertTrue(ArrayUtils.contains(array, value));
		
	}
	
	/**
	 * Test for method {@link ArrayUtils#contains(char[], char).
	 */
	@Test
	public void testContainsCharArrayChar_two()
	{
		final char[] testarr = new char[1];
		
		testarr[0] = 'b';
		
		final char value = 'c';
		
		Assert.assertFalse(ArrayUtils.contains(testarr, value));
		
	}
	
	/**
	 * Test for method {@link ArrayUtils#indexOf(char[], char).
	 */
	@Test
	public void testIndexOfCharArrayChar()
	{
		final char[] testarr = new char[1];
		
		testarr[0] = 'b';
		
		final char value = 'b';
		
		final int expected = 0;
		
		Assert.assertEquals(expected, ArrayUtils.indexOf(testarr, value));
	}
	
	/**
	 * Test for method {@link ArrayUtils#contains(char[], char).
	 */
	@Test
	public void testIndexOfCharArrayChar_two()
	{
		final char[] testarr = new char[1];
		
		testarr[0] = 'b';
		
		final char value = 'c';
		
		final int expected = -1;
		
		Assert.assertEquals(expected, ArrayUtils.indexOf(testarr, value));
	}
	
	/**
	 * Test for method {@link ArrayUtils#contains(byte[], byte).
	 */
	@Test
	public void testContainsByteArrayByte()
	{
		final byte value = 2;
		
		final byte[] testarr = new byte[1];
		testarr[0] = value;
		
		Assert.assertTrue(ArrayUtils.contains(testarr, value));
	}
	
	/**
	 * Test for method {@link ArrayUtils#contains(byte[], byte).
	 */
	@Test
	public void testContainsByteArrayByte_two()
	{
		final byte value = 2;
		
		final byte[] testarr = new byte[1];
		testarr[0] = 3;
		
		Assert.assertFalse(ArrayUtils.contains(testarr, value));
	}
	
	/**
	 * Test for method {@link ArrayUtils#indexOf(byte[], byte).
	 */
	@Test
	public void testIndexOfByteArrayByte()
	{
		final byte value = 2;
		
		final byte[] testarr = new byte[1];
		testarr[0] = value;
		
		final int expected = 0;
		
		Assert.assertEquals(expected, ArrayUtils.indexOf(testarr, value));
		
	}
	
	/**
	 * Test for method {@link ArrayUtils#indexOf(byte[], byte).
	 */
	@Test
	public void testIndexOfByteArrayByte_two()
	{
		final byte value = 2;
		
		final byte[] testarr = new byte[1];
		testarr[0] = 3;
		
		final int expected = -1;
		
		Assert.assertEquals(expected, ArrayUtils.indexOf(testarr, value));
		
	}
	
	/**
	 * Test for method {@link ArrayUtils#contains(short[], short).
	 */
	@Test
	public void testContainsShortArrayShort()
	{
		final short value = 2;
		
		final short[] testarr = new short[1];
		testarr[0] = value;
		
		Assert.assertTrue(ArrayUtils.contains(testarr, value));
	}
	
	/**
	 * Test for method {@link ArrayUtils#contains(short[], short).
	 */
	@Test
	public void testContainsShortArrayShort_two()
	{
		final short value = 2;
		
		final short[] testarr = new short[1];
		testarr[0] = 3;
		
		Assert.assertFalse(ArrayUtils.contains(testarr, value));
	}
	
	/**
	 * Test for method {@link ArrayUtils#indexOf(short[], short).
	 */
	@Test
	public void testIndexOfShortArrayShort()
	{
		final short value = 2;
		
		final short[] testarr = new short[1];
		testarr[0] = value;
		
		final int expected = 0;
		
		Assert.assertEquals(expected, ArrayUtils.indexOf(testarr, value));
	}
	
	/**
	 * Test for method {@link ArrayUtils#indexOf(short[], short).
	 */
	@Test
	public void testIndexOfShortArrayShort_two()
	{
		final short value = 2;
		
		final short[] testarr = new short[1];
		testarr[0] = 3;
		
		final int expected = -1;
		
		Assert.assertEquals(expected, ArrayUtils.indexOf(testarr, value));
	}
	
	/**
	 * Test for method {@link ArrayUtils#contains(int[], int).
	 */
	@Test
	public void testContainsIntArrayInt()
	{
		final int value = 2;
		
		final int[] testarr = new int[1];
		testarr[0] = value;
		
		Assert.assertTrue(ArrayUtils.contains(testarr, value));
	}
	
	/**
	 * Test for method {@link ArrayUtils#contains(int[], int).
	 */
	@Test
	public void testContainsIntArrayInt_two()
	{
		final int value = 2;
		
		final int[] testarr = new int[1];
		testarr[0] = 3;
		
		Assert.assertFalse(ArrayUtils.contains(testarr, value));
	}
	
	/**
	 * Test for method {@link ArrayUtils#indexOf(int[], int).
	 */
	@Test
	public void testIndexOfIntArrayInt()
	{
		final int value = 2;
		
		final int[] testarr = new int[1];
		testarr[0] = value;
		
		final int expected = 0;
		
		Assert.assertEquals(expected, ArrayUtils.indexOf(testarr, value));
	}
	
	/**
	 * Test for method {@link ArrayUtils#indexOf(int[], int).
	 */
	@Test
	public void testIndexOfIntArrayInt_two()
	{
		final int value = 2;
		
		final int[] testarr = new int[1];
		testarr[0] = 3;
		
		final int expected = -1;
		
		Assert.assertEquals(expected, ArrayUtils.indexOf(testarr, value));
	}
	
	/**
	 * Test for method {@link ArrayUtils#contains(long[], long).
	 */
	@Test
	public void testContainsLongArrayLong()
	{
		final long value = 2;
		
		final long[] testarr = new long[1];
		testarr[0] = value;
		
		Assert.assertTrue(ArrayUtils.contains(testarr, value));
		
	}
	
	/**
	 * Test for method {@link ArrayUtils#contains(long[], long).
	 */
	@Test
	public void testContainsLongArrayLong_two()
	{
		final long value = 2;
		
		final long[] testarr = new long[1];
		testarr[0] = 3;
		
		Assert.assertFalse(ArrayUtils.contains(testarr, value));
		
	}
	
	/**
	 * Test for method {@link ArrayUtils#indexOf(long[], long).
	 */
	@Test
	public void testIndexOfLongArrayLong()
	{
		final long value = 2;
		
		final long[] testarr = new long[1];
		testarr[0] = value;
		
		final int expected = 0;
		
		Assert.assertEquals(expected, ArrayUtils.indexOf(testarr, value));
	}
	
	/**
	 * Test for method {@link ArrayUtils#indexOf(long[], long).
	 */
	@Test
	public void testIndexOfLongArrayLong_two()
	{
		final long value = 2;
		
		final long[] testarr = new long[1];
		testarr[0] = 3;
		
		final int expected = -1;
		
		Assert.assertEquals(expected, ArrayUtils.indexOf(testarr, value));
	}
	
	/**
	 * Test for method {@link ArrayUtils#contains(float[], float).
	 */
	@Test
	public void testContainsFloatArrayFloat()
	{
		final float value = 2;
		
		final float[] testarr = new float[1];
		testarr[0] = value;
		
		Assert.assertTrue(ArrayUtils.contains(testarr, value));
		
	}
	
	/**
	 * Test for method {@link ArrayUtils#contains(float[], float).
	 */
	@Test
	public void testContainsFloatArrayFloat_two()
	{
		final float value = 2;
		
		final float[] testarr = new float[1];
		testarr[0] = 3;
		
		Assert.assertFalse(ArrayUtils.contains(testarr, value));
		
	}
	
	/**
	 * Test for method {@link ArrayUtils#indexOf(float[], float).
	 */
	@Test
	public void testIndexOfFloatArrayFloat()
	{
		final float value = 2;
		
		final float[] testarr = new float[1];
		
		testarr[0] = value;
		
		final int expected = 0;
		
		Assert.assertEquals(expected, ArrayUtils.indexOf(testarr, value));
	}
	
	/**
	 * Test for method {@link ArrayUtils#indexOf(float[], float).
	 */
	@Test
	public void testIndexOfFloatArrayFloat_two()
	{
		final float value = 2;
		
		final float[] testarr = new float[1];
		testarr[0] = 3;
		
		final int expected = -1;
		
		Assert.assertEquals(expected, ArrayUtils.indexOf(testarr, value));
	}
	
	/**
	 * Test for method {@link ArrayUtils#contains(double[], double).
	 */
	@Test
	public void testContainsDoubleArrayDouble()
	{
		final double value = 2;
		
		final double[] testarr = new double[1];
		testarr[0] = value;
		
		Assert.assertTrue(ArrayUtils.contains(testarr, value));
	}
	
	/**
	 * Test for method {@link ArrayUtils#contains(double[], double).
	 */
	@Test
	public void testContainsDoubleArrayDouble_two()
	{
		final double value = 2;
		
		final double[] testarr = new double[1];
		testarr[0] = 3;
		
		Assert.assertFalse(ArrayUtils.contains(testarr, value));
	}
	
	/**
	 * Test for method {@link ArrayUtils#indexOf(double[], double).
	 */
	@Test
	public void testIndexOfDoubleArrayDouble()
	{
		final double value = 2;
		
		final double[] testarr = new double[1];
		testarr[0] = value;
		
		final int expected = 0;
		
		Assert.assertEquals(expected, ArrayUtils.indexOf(testarr, value));
	}
	
	/**
	 * Test for method {@link ArrayUtils#indexOf(double[], double).
	 */
	@Test
	public void testIndexOfDoubleArrayDouble_two()
	{
		final double value = 2;
		
		final double[] testarr = new double[1];
		testarr[0] = 3;
		
		final int expected = -1;
		
		Assert.assertEquals(expected, ArrayUtils.indexOf(testarr, value));
	}
	
	/**
	 * Test for method {@link ArrayUtils#contains(Object[], Object, Comparer).
	 */
	@Test
	public void testContainsTArrayTComparerOfT()
	{
		final Person[] array = {new Person("Max", "Mustermann"), new Person("Maximilian", "Testnann")};
		
		final Person value = new Person("Max", "Mustermann");
		
		Assert.assertTrue(ArrayUtils.contains(array, value, value));
	}
	
	/**
	 * Test for method {@link ArrayUtils#contains(Object[], Object, Comparer).
	 */
	@Test
	public void testContainsTArrayTComparerOfT_two()
	{
		final Person[] array = {new Person("Max", "Mustermann"), new Person("Maximilian", "Testnann")};
		
		final Person value = new Person("Maximilian", "Mustermann");
		
		Assert.assertFalse(ArrayUtils.contains(array, value, value));
	}
	
	/**
	 * Test for method {@link ArrayUtils#indexOf(Object[], Object, Comparer).
	 */
	@Test
	public void testIndexOfTArrayTComparerOfT()
	{
		final Person[] array = {new Person("Max", "Mustermann"), new Person("Maximilian", "Testnann")};
		
		final Person value = new Person("Max", "Mustermann");
		
		Assert.assertEquals(ArrayUtils.indexOf(array, value, value), 0);
	}
	
	/**
	 * Test for method {@link ArrayUtils#indexOf(Object[], Object, Comparer).
	 */
	@Test
	public void testIndexOfTArrayTComparerOfT_two()
	{
		final Person[] array = null;
		
		final Person value = new Person("Max", "Mustermann");
		
		Assert.assertEquals(ArrayUtils.indexOf(array, value, value), -1);
	}
	
	/**
	 * Test for method {@link ArrayUtils#copyOf(Object[]).
	 */
	@Test
	public void testCopyOfTArray()
	{
		final String[] testarr = new String[1];
		testarr[0] = "a";
		
		final String[] copyarr = ArrayUtils.copyOf(testarr);
		
		Assert.assertEquals(testarr[0], copyarr[0]);
	}
	
	/**
	 * Test for method {@link ArrayUtils#copyOf(Object[]).
	 */
	@Test
	public void testCopyOfTArray_two()
	{
		final String[] testarr = new String[3];
		testarr[0] = "a";
		testarr[1] = "b";
		testarr[2] = "c";
		
		final String[] copyarr = ArrayUtils.copyOf(testarr);
		
		for(int i = 0; i < testarr.length; i++)
		{
			Assert.assertEquals(testarr[i], copyarr[i]);
		}
	}
	
	/**
	 * Test for method {@link ArrayUtils#copyOf(boolean[]).
	 */
	@Test
	public void testCopyOfBooleanArray()
	{
		final boolean[] testarr = new boolean[1];
		testarr[0] = true;
		
		final boolean[] copyarr = ArrayUtils.copyOf(testarr);
		
		Assert.assertEquals(testarr[0], copyarr[0]);
		
	}
	
	/**
	 * Test for method {@link ArrayUtils#copyOf(boolean[]).
	 */
	@Test
	public void testCopyOfBooleanArray_two()
	{
		final boolean[] testarr = new boolean[3];
		testarr[0] = true;
		testarr[1] = false;
		testarr[2] = true;
		
		final boolean[] copyarr = ArrayUtils.copyOf(testarr);
		
		for(int i = 0; i < testarr.length; i++)
		{
			Assert.assertEquals(testarr[i], copyarr[i]);
		}
		
	}
	
	/**
	 * Test for method {@link ArrayUtils#copyOf(boolean[]).
	 */
	@Test
	public void testCopyOfBooleanArray_three()
	{
		final boolean[] testarr = new boolean[1];
		testarr[0] = true;
		
		final boolean[] copyarr = ArrayUtils.copyOf(testarr);
		
		Assert.assertEquals(testarr.length, copyarr.length);
		
	}
	
	/**
	 * Test for method {@link ArrayUtils#copyOf(char[]).
	 */
	@Test
	public void testCopyOfCharArray()
	{
		final char[] testarr = new char[1];
		testarr[0] = 'd';
		
		final char[] copyarr = ArrayUtils.copyOf(testarr);
		
		Assert.assertEquals(testarr[0], copyarr[0]);
	}
	
	/**
	 * Test for method {@link ArrayUtils#copyOf(char[]).
	 */
	@Test
	public void testCopyOfCharArray_two()
	{
		final char[] testarr = new char[1];
		
		final char[] copyarr = ArrayUtils.copyOf(testarr);
		
		Assert.assertEquals(testarr.length, copyarr.length);
	}
	
	/**
	 * Test for method {@link ArrayUtils#copyOf(char[]).
	 */
	@Test
	public void testCopyOfCharArray_three()
	{
		final char[] testarr = new char[3];
		testarr[0] = 'd';
		testarr[1] = 'e';
		testarr[2] = 'f';
		
		final char[] copyarr = ArrayUtils.copyOf(testarr);
		
		for(int i = 0; i < testarr.length; i++)
		{
			Assert.assertEquals(testarr[i], copyarr[i]);
		}
	}
	
	/**
	 * Test for method {@link ArrayUtils#copy(byte[]).
	 */
	@Test
	public void testCopyofbyteArray()
	{
		final byte[] testarr = new byte[1];
		testarr[0] = 3;
		
		final byte[] copyarr = ArrayUtils.copy(testarr);
		
		Assert.assertEquals(testarr[0], copyarr[0]);
	}
	
	/**
	 * Test for method {@link ArrayUtils#copy(byte[]).
	 */
	@Test
	public void testCopyofbyteArray_two()
	{
		final byte[] testarr = new byte[3];
		testarr[0] = 'd';
		testarr[1] = 'e';
		testarr[2] = 'f';
		
		final byte[] copyarr = ArrayUtils.copy(testarr);
		
		for(int i = 0; i < testarr.length; i++)
		{
			Assert.assertEquals(testarr[i], copyarr[i]);
		}
	}
	
	/**
	 * Test for method {@link ArrayUtils#copy(byte[]).
	 */
	@Test
	public void testCopyofbyteArray_three()
	{
		final byte[] testarr = new byte[1];
		
		final byte[] copyarr = ArrayUtils.copy(testarr);
		
		Assert.assertEquals(testarr.length, copyarr.length);
	}
	
	/**
	 * Test for method {@link ArrayUtils#copyOf(short[])}.
	 */
	@Test
	public void testCopyOfShortArray()
	{
		final short[] testarr = new short[1];
		testarr[0] = 3;
		
		final short[] copyarr = ArrayUtils.copyOf(testarr);
		
		Assert.assertEquals(testarr[0], copyarr[0]);
	}
	
	/**
	 * Test for method {@link ArrayUtils#copyOf(short[])}.
	 */
	@Test
	public void testCopyOfShortArray_two()
	{
		final short[] testarr = new short[3];
		testarr[0] = 3;
		testarr[1] = 2;
		testarr[2] = 3;
		
		final short[] copyarr = ArrayUtils.copyOf(testarr);
		
		for(int i = 0; i < testarr.length; i++)
		{
			Assert.assertEquals(testarr[i], copyarr[i]);
		}
	}
	
	/**
	 * Test for method {@link ArrayUtils#copyOf(short[])}.
	 */
	@Test
	public void testCopyOfShortArray_three()
	{
		final short[] testarr = new short[1];
		
		final short[] copyarr = ArrayUtils.copyOf(testarr);
		
		Assert.assertEquals(testarr.length, copyarr.length);
	}
	
	/**
	 * Test for method {@link ArrayUtils#copyOf(int[]).
	 */
	@Test
	public void testCopyOfIntArray()
	{
		final int[] testarr = new int[1];
		testarr[0] = 3;
		
		final int[] copyarr = ArrayUtils.copyOf(testarr);
		
		Assert.assertEquals(testarr[0], copyarr[0]);
	}
	
	/**
	 * Test for method {@link ArrayUtils#copyOf(int[]).
	 */
	@Test
	public void testCopyOfIntArray_two()
	{
		final int[] testarr = new int[3];
		testarr[0] = 3;
		testarr[1] = 2;
		testarr[2] = 5;
		
		final int[] copyarr = ArrayUtils.copyOf(testarr);
		
		for(int i = 0; i < testarr.length; i++)
		{
			Assert.assertEquals(testarr[i], copyarr[i]);
		}
	}
	
	/**
	 * Test for method {@link ArrayUtils#copyOf(int[]).
	 */
	@Test
	public void testCopyOfIntArray_three()
	{
		final int[] testarr = new int[1];
		testarr[0] = 3;
		
		final int[] copyarr = ArrayUtils.copyOf(testarr);
		
		Assert.assertEquals(testarr.length, copyarr.length);
	}
	
	/**
	 * Test for method {@link ArrayUtils#copyOf(long[]).
	 */
	@Test
	public void testCopyOfLongArray()
	{
		final long[] testarr = new long[1];
		testarr[0] = 3;
		
		final long[] copyarr = ArrayUtils.copyOf(testarr);
		
		Assert.assertEquals(testarr[0], copyarr[0]);
	}
	
	/**
	 * Test for method {@link ArrayUtils#copyOf(long[]).
	 */
	@Test
	public void testCopyOfLongArray_two()
	{
		final long[] testarr = new long[3];
		testarr[0] = 8;
		testarr[1] = 2;
		testarr[2] = 1;
		
		final long[] copyarr = ArrayUtils.copyOf(testarr);
		
		for(int i = 0; i < testarr.length; i++)
		{
			Assert.assertEquals(testarr[i], copyarr[i]);
		}
	}
	
	/**
	 * Test for method {@link ArrayUtils#copyOf(long[]).
	 */
	@Test
	public void testCopyOfLongArray_three()
	{
		final long[] testarr = new long[1];
		testarr[0] = 3;
		
		final long[] copyarr = ArrayUtils.copyOf(testarr);
		
		Assert.assertEquals(testarr.length, copyarr.length);
	}
	
	/**
	 * Test for method {@link ArrayUtils#copyOf(float[]).
	 */
	@Test
	public void testCopyOfFloatArray()
	{
		final float[] testarr = new float[1];
		testarr[0] = 3;
		
		final float[] copyarr = ArrayUtils.copyOf(testarr);
		
		Assert.assertEquals(testarr[0], copyarr[0], 0);
	}
	
	/**
	 * Test for method {@link ArrayUtils#copyOf(float[]).
	 */
	@Test
	public void testCopyOfFloatArray_two()
	{
		final float[] testarr = new float[3];
		testarr[0] = 3;
		testarr[1] = 11;
		testarr[2] = 346;
		
		final float[] copyarr = ArrayUtils.copyOf(testarr);
		
		for(int i = 0; i < testarr.length; i++)
		{
			Assert.assertEquals(testarr[i], copyarr[i], 0);
		}
	}
	
	/**
	 * Test for method {@link ArrayUtils#copyOf(float[]).
	 */
	@Test
	public void testCopyOfFloatArray_three()
	{
		final float[] testarr = new float[1];
		testarr[0] = 3;
		
		final float[] copyarr = ArrayUtils.copyOf(testarr);
		
		Assert.assertEquals(testarr.length, copyarr.length);
	}
	
	/**
	 * Test for method {@link ArrayUtils#copyOf(double[]).
	 */
	@Test
	public void testCopyOfDoubleArray()
	{
		
		final double[] testarr = new double[1];
		testarr[0] = 3;
		
		final double[] copyarr = ArrayUtils.copyOf(testarr);
		
		Assert.assertEquals(testarr[0], copyarr[0], 0);
	}
	
	/**
	 * Test for method {@link ArrayUtils#copyOf(double[]).
	 */
	@Test
	public void testCopyOfDoubleArray_two()
	{
		
		final double[] testarr = new double[5];
		testarr[0] = 1;
		testarr[0] = 35;
		testarr[0] = 35;
		testarr[0] = 476;
		testarr[0] = 858;
		
		final double[] copyarr = ArrayUtils.copyOf(testarr);
		
		for(int i = 0; i < testarr.length; i++)
		{
			Assert.assertEquals(testarr[i], copyarr[i], 0);
		}
	}
	
	/**
	 * Test for method {@link ArrayUtils#copyOf(double[]).
	 */
	@Test
	public void testCopyOfDoubleArray_three()
	{
		
		final double[] testarr = new double[1];
		testarr[0] = 3;
		
		final double[] copyarr = ArrayUtils.copyOf(testarr);
		
		Assert.assertEquals(testarr.length, copyarr.length);
	}
	
	/**
	 * Test for method {@link ArrayUtils#concat(Class, Object[], Object).
	 */
	@Test
	public void testConcatClassOfTTArrayT()
	{
		final String[] testarr = new String[2];
		final String concatString = new String("a");
		
		final String[] added = ArrayUtils.concat(String.class, testarr, concatString);
		
		Assert.assertEquals(testarr.length, added.length - 1);
		
	}
	
	/**
	 * Test for method {@link ArrayUtils#concat(Class, Object[], Object).
	 */
	@Test
	public void testConcatClassOfTTArrayT_two()
	{
		final String[] testarr = new String[5];
		
		final String concatString = new String("a");
		testarr[0] = concatString;
		
		final String[] added = ArrayUtils.concat(String.class, testarr, concatString);
		
		Assert.assertEquals(testarr[0], added[added.length - 1]);
		
	}
	
	/**
	 * Test for method {@link ArrayUtils#concat(boolean[], boolean).
	 */
	@Test
	public void testConcatBooleanArrayBoolean()
	{
		final boolean[] testarr = new boolean[2];
		final boolean add = true;
		final boolean[] added = ArrayUtils.concat(testarr, add);
		
		Assert.assertEquals(testarr.length, added.length - 1);
		
	}
	
	/**
	 * Test for method {@link ArrayUtils#concat(boolean[], boolean).
	 */
	@Test
	public void testConcatBooleanArrayBoolean_two()
	{
		final boolean[] testarr = new boolean[2];
		final boolean add = true;
		final boolean[] added = ArrayUtils.concat(testarr, add);
		
		Assert.assertEquals(add, added[added.length - 1]);
		
	}
	
	/**
	 * Test for method {@link ArrayUtils#concat(char[], char).
	 */
	@Test
	public void testConcatCharArrayChar()
	{
		final char[] testarr = new char[1];
		final char add = 'a';
		final char[] added = ArrayUtils.concat(testarr, add);
		
		Assert.assertEquals(testarr.length, added.length - 1);
	}
	
	/**
	 * Test for method {@link ArrayUtils#concat(char[], char).
	 */
	@Test
	public void testConcatCharArrayChar_two()
	{
		final char[] testarr = new char[1];
		final char add = 'a';
		final char[] added = ArrayUtils.concat(testarr, add);
		
		Assert.assertEquals(add, added[added.length - 1]);
	}
	
	/**
	 * Test for method {@link ArrayUtils#concat(byte[], byte).
	 */
	@Test
	public void testConcatByteArrayByte()
	{
		final byte[] testarr = new byte[1];
		final byte add = 'a';
		final byte[] added = ArrayUtils.concat(testarr, add);
		
		Assert.assertEquals(testarr.length, added.length - 1);
	}
	
	/**
	 * Test for method {@link ArrayUtils#concat(byte[], byte).
	 */
	@Test
	public void testConcatByteArrayByte_two()
	{
		final byte[] testarr = new byte[1];
		final byte add = 'b';
		final byte[] added = ArrayUtils.concat(testarr, add);
		
		Assert.assertEquals(add, added[added.length - 1]);
	}
	
	/**
	 * Test for method {@link ArrayUtils#concat(short[], short).
	 */
	@Test
	public void testConcatShortArrayShort()
	{
		final short[] testarr = new short[1];
		final short add = 1;
		final short[] added = ArrayUtils.concat(testarr, add);
		
		Assert.assertEquals(testarr.length, added.length - 1);
	}
	
	/**
	 * Test for method {@link ArrayUtils#concat(short[], short).
	 */
	@Test
	public void testConcatShortArrayShort_two()
	{
		final short[] testarr = new short[1];
		final short add = 1;
		final short[] added = ArrayUtils.concat(testarr, add);
		
		Assert.assertEquals(add, added[added.length - 1]);
	}
	
	/**
	 * Test for method {@link ArrayUtils#concat(int[], int).
	 */
	@Test
	public void testConcatIntArrayInt()
	{
		final int[] testarr = new int[1];
		final int add = 1;
		final int[] added = ArrayUtils.concat(testarr, add);
		
		Assert.assertEquals(testarr.length, added.length - 1);
	}
	
	/**
	 * Test for method {@link ArrayUtils#concat(int[], int).
	 */
	@Test
	public void testConcatIntArrayInt_two()
	{
		final int[] testarr = new int[1];
		
		final int add = 1;
		final int[] added = ArrayUtils.concat(testarr, add);
		
		Assert.assertEquals(add, added[added.length - 1]);
		
	}
	
	/**
	 * Test for method {@link ArrayUtils#concat(long[], long).
	 */
	@Test
	public void testConcatLongArrayLong()
	{
		final long[] testarr = new long[1];
		final long add = 1;
		final long[] added = ArrayUtils.concat(testarr, add);
		
		Assert.assertEquals(testarr.length, added.length - 1);
	}
	
	/**
	 * Test for method {@link ArrayUtils#concat(long[], long).
	 */
	@Test
	public void testConcatLongArrayLong_two()
	{
		final long[] testarr = new long[1];
		final long add = 1;
		final long[] added = ArrayUtils.concat(testarr, add);
		
		Assert.assertEquals(add, added[added.length - 1]);
	}
	
	/**
	 * Test for method {@link ArrayUtils#concat(float[], float).
	 */
	@Test
	public void testConcatFloatArrayFloat()
	{
		final float[] testarr = new float[1];
		final float add = 1;
		final float[] added = ArrayUtils.concat(testarr, add);
		
		Assert.assertEquals(testarr.length, added.length - 1);
	}
	
	/**
	 * Test for method {@link ArrayUtils#concat(float[], float).
	 */
	@Test
	public void testConcatFloatArrayFloat_two()
	{
		final float[] testarr = new float[1];
		final float add = 1;
		final float[] added = ArrayUtils.concat(testarr, add);
		
		Assert.assertEquals(add, added[added.length - 1], 0);
	}
	
	/**
	 * Test for method {@link ArrayUtils#concat(double[], double).
	 */
	@Test
	public void testConcatDoubleArrayDouble()
	{
		final double[] testarr = new double[1];
		final double add = 1.2;
		final double[] added = ArrayUtils.concat(testarr, add);
		
		Assert.assertEquals(testarr.length, added.length - 1);
	}
	
	/**
	 * Test for method {@link ArrayUtils#concat(double[], double).
	 */
	@Test
	public void testConcatDoubleArrayDouble_two()
	{
		final double[] testarr = new double[1];
		final double add = 1.2;
		final double[] added = ArrayUtils.concat(testarr, add);
		
		Assert.assertEquals(add, added[added.length - 1], 0);
	}
	
	/**
	 * Test for method {@link ArrayUtils#concat(Class, Object[]...).
	 */
	@Test
	public void testConcatClassOfTTArrayArray()
	{
		final String[] testarr = new String[2];
		final String[] testarr2 = new String[2];
		
		final String[] added = ArrayUtils.concat(String.class, testarr, testarr2);
		
		Assert.assertEquals(testarr.length + testarr2.length, added.length);
	}
	
	/**
	 * Test for method {@link ArrayUtils#concat(Class, Object[]...).
	 */
	@Test
	public void testConcatClassOfTTArrayArray_two()
	{
		final String[] testarr1 = new String[2];
		testarr1[0] = "hallo";
		testarr1[1] = "halloa";
		
		final String[] testarr2 = new String[2];
		testarr2[0] = "hallob";
		testarr2[1] = "halloc";
		
		int e = 0;
		
		final String[] added = ArrayUtils.concat(String.class, testarr1, testarr2);
		
		for(int i = 1; i <= added.length; i++)
		{
			if(i <= testarr1.length)
			{
				Assert.assertEquals(testarr1[i - 1], added[i - 1]);
				
			}
			else
			{
				
				Assert.assertEquals(testarr2[e], added[i - 1]);
				
				e++;
			}
		}
	}
	
	/**
	 * Test for method {@link ArrayUtils#concat(Class, Object[]...).
	 */
	@Test
	public void testConcatClassOfTTArrayArray_three()
	{
		final String[] testarr1 = new String[2];
		testarr1[0] = "a";
		testarr1[1] = "b";
		
		final String[] testarr2 = new String[5];
		testarr2[0] = "c";
		testarr2[1] = "d";
		testarr2[2] = "d";
		testarr2[3] = "d";
		testarr2[4] = "d";
		
		final String[] testarr3 = new String[2];
		testarr3[0] = "e";
		testarr3[1] = "f";
		
		int e = 0;
		int f = 0;
		
		final String[] added = ArrayUtils.concat(String.class, testarr1, testarr2, testarr3);
		
		for(int i = 1; i <= added.length; i++)
		{
			
			if(i <= testarr1.length)
			{
				Assert.assertEquals(testarr1[i - 1], added[i - 1]);
				
			}
			else if(i - testarr1.length <= testarr2.length)
			{
				
				Assert.assertEquals(testarr2[e], added[i - 1]);
				
				e++;
			}
			
			else
			{
				
				Assert.assertEquals(testarr3[f], added[i - 1]);
				
				f++;
			}
		}
	}
	
	/**
	 * Test for method {@link ArrayUtils#concat(boolean[]...).
	 */
	@Test
	public void testConcatBooleanArrayArray()
	{
		final boolean[] testarr1 = new boolean[2];
		
		final boolean[] testarr2 = new boolean[2];
		
		final boolean[] added = ArrayUtils.concat(testarr1, testarr2);
		
		Assert.assertEquals(testarr1.length + testarr2.length, added.length);
		
	}
	
	/**
	 * Test for method {@link ArrayUtils#concat(boolean[]...).
	 */
	@Test
	public void testConcatBooleanArrayArray_two()
	{
		final boolean[] testarr1 = new boolean[2];
		testarr1[0] = true;
		testarr1[1] = false;
		
		final boolean[] testarr2 = new boolean[2];
		testarr2[0] = true;
		testarr2[1] = false;
		
		int e = 0;
		
		final boolean[] added = ArrayUtils.concat(testarr1, testarr2);
		
		for(int i = 1; i <= added.length; i++)
		{
			if(i <= testarr1.length)
			{
				Assert.assertEquals(testarr1[i - 1], added[i - 1]);
				
			}
			else
			{
				
				Assert.assertEquals(testarr2[e], added[i - 1]);
				
				e++;
			}
		}
		
	}
	
	/**
	 * Test for method {@link ArrayUtils#concat(boolean[]...).
	 */
	@Test
	public void testConcatBooleanArrayArray_three()
	{
		final boolean[] testarr1 = new boolean[2];
		testarr1[0] = true;
		testarr1[1] = false;
		
		final boolean[] testarr2 = new boolean[2];
		testarr2[0] = true;
		testarr2[1] = false;
		
		final boolean[] testarr3 = new boolean[2];
		testarr3[0] = true;
		testarr3[1] = false;
		
		int e = 0;
		int f = 0;
		
		final boolean[] added = ArrayUtils.concat(testarr1, testarr2, testarr3);
		
		for(int i = 1; i <= added.length; i++)
		{
			
			if(i <= testarr1.length)
			{
				Assert.assertEquals(testarr1[i - 1], added[i - 1]);
				
			}
			else if(i - testarr1.length <= testarr2.length)
			{
				
				Assert.assertEquals(testarr2[e], added[i - 1]);
				
				e++;
			}
			
			else
			{
				
				Assert.assertEquals(testarr3[f], added[i - 1]);
				
				f++;
			}
		}
		
	}
	
	/**
	 * Test for method {@link ArrayUtils#concat(char[]...).
	 */
	@Test
	public void testConcatCharArrayArray()
	{
		final char[] testarr1 = new char[2];
		
		final char[] testarr2 = new char[2];
		
		final char[] added = ArrayUtils.concat(testarr1, testarr2);
		
		Assert.assertEquals(testarr1.length + testarr2.length, added.length);
	}
	
	/**
	 * Test for method {@link ArrayUtils#concat(char[]...).
	 */
	@Test
	public void testConcatCharArrayArray_two()
	{
		final char[] testarr1 = new char[2];
		testarr1[0] = 'a';
		testarr1[1] = 'b';
		
		final char[] testarr2 = new char[2];
		testarr2[0] = 'c';
		testarr2[1] = 'd';
		
		int e = 0;
		
		final char[] added = ArrayUtils.concat(testarr1, testarr2);
		
		for(int i = 1; i <= added.length; i++)
		{
			if(i <= testarr1.length)
			{
				Assert.assertEquals(testarr1[i - 1], added[i - 1]);
				
			}
			else
			{
				
				Assert.assertEquals(testarr2[e], added[i - 1]);
				
				e++;
			}
		}
	}
	
	/**
	 * Test for method {@link ArrayUtils#concat(char[]...).
	 */
	@Test
	public void testConcatCharArrayArray_three()
	{
		final char[] testarr1 = new char[2];
		testarr1[0] = 'a';
		testarr1[1] = 'b';
		
		final char[] testarr2 = new char[2];
		testarr2[0] = 'c';
		testarr2[1] = 'd';
		
		final char[] testarr3 = new char[2];
		testarr3[0] = 'e';
		testarr3[1] = 'f';
		
		int e = 0;
		int f = 0;
		
		final char[] added = ArrayUtils.concat(testarr1, testarr2, testarr3);
		
		for(int i = 1; i <= added.length; i++)
		{
			
			if(i <= testarr1.length)
			{
				Assert.assertEquals(testarr1[i - 1], added[i - 1]);
				
			}
			else if(i - testarr1.length <= testarr2.length)
			{
				
				Assert.assertEquals(testarr2[e], added[i - 1]);
				
				e++;
			}
			
			else
			{
				
				Assert.assertEquals(testarr3[f], added[i - 1]);
				
				f++;
			}
		}
	}
	
	/**
	 * Test for method {@link ArrayUtils#concat(byte[]...).
	 */
	@Test
	public void testConcatByteArrayArray()
	{
		final byte[] testarr1 = new byte[2];
		
		final byte[] testarr2 = new byte[2];
		
		final byte[] added = ArrayUtils.concat(testarr1, testarr2);
		
		Assert.assertEquals(testarr1.length + testarr2.length, added.length);
	}
	
	/**
	 * Test for method {@link ArrayUtils#concat(byte[]...).
	 */
	@Test
	public void testConcatByteArrayArray_two()
	{
		final byte[] testarr1 = new byte[2];
		testarr1[0] = 'a';
		testarr1[1] = 'b';
		
		final byte[] testarr2 = new byte[2];
		testarr2[0] = 'c';
		testarr2[1] = 'd';
		
		int e = 0;
		
		final byte[] added = ArrayUtils.concat(testarr1, testarr2);
		
		for(int i = 1; i <= added.length; i++)
		{
			if(i <= testarr1.length)
			{
				Assert.assertEquals(testarr1[i - 1], added[i - 1]);
				
			}
			else
			{
				
				Assert.assertEquals(testarr2[e], added[i - 1]);
				
				e++;
			}
		}
	}
	
	/**
	 * Test for method {@link ArrayUtils#concat(byte[]...).
	 */
	@Test
	public void testConcatByteArrayArray_three()
	{
		final byte[] testarr1 = new byte[2];
		testarr1[0] = 'a';
		testarr1[1] = 'b';
		
		final byte[] testarr2 = new byte[2];
		testarr2[0] = 'c';
		testarr2[1] = 'd';
		
		final byte[] testarr3 = new byte[2];
		testarr3[0] = 'e';
		testarr3[1] = 'f';
		
		int e = 0;
		int f = 0;
		
		final byte[] added = ArrayUtils.concat(testarr1, testarr2, testarr3);
		
		for(int i = 1; i <= added.length; i++)
		{
			
			if(i <= testarr1.length)
			{
				Assert.assertEquals(testarr1[i - 1], added[i - 1]);
				
			}
			else if(i - testarr1.length <= testarr2.length)
			{
				
				Assert.assertEquals(testarr2[e], added[i - 1]);
				
				e++;
			}
			
			else
			{
				
				Assert.assertEquals(testarr3[f], added[i - 1]);
				
				f++;
			}
		}
	}
	
	/**
	 * Test for method {@link ArrayUtils#concat(short[]...).
	 */
	@Test
	public void testConcatShortArrayArray()
	{
		final short[] testarr1 = new short[2];
		
		final short[] testarr2 = new short[2];
		
		final short[] added = ArrayUtils.concat(testarr1, testarr2);
		
		Assert.assertEquals(testarr1.length + testarr2.length, added.length);
	}
	
	/**
	 * Test for method {@link ArrayUtils#concat(short[]...).
	 */
	@Test
	public void testConcatShortArrayArray_two()
	{
		final short[] testarr1 = new short[2];
		testarr1[0] = 'a';
		testarr1[1] = 'b';
		
		final short[] testarr2 = new short[2];
		testarr2[0] = 'c';
		testarr2[1] = 'd';
		
		int e = 0;
		
		final short[] added = ArrayUtils.concat(testarr1, testarr2);
		
		for(int i = 1; i <= added.length; i++)
		{
			if(i <= testarr1.length)
			{
				Assert.assertEquals(testarr1[i - 1], added[i - 1]);
				
			}
			else
			{
				
				Assert.assertEquals(testarr2[e], added[i - 1]);
				
				e++;
			}
		}
	}
	
	/**
	 * Test for method {@link ArrayUtils#concat(short[]...).
	 */
	@Test
	public void testConcatShortArrayArray_three()
	{
		final short[] testarr1 = new short[2];
		testarr1[0] = 'a';
		testarr1[1] = 'b';
		
		final short[] testarr2 = new short[2];
		testarr2[0] = 'c';
		testarr2[1] = 'd';
		
		final short[] testarr3 = new short[2];
		testarr3[0] = 'e';
		testarr3[1] = 'f';
		
		int e = 0;
		int f = 0;
		
		final short[] added = ArrayUtils.concat(testarr1, testarr2, testarr3);
		
		for(int i = 1; i <= added.length; i++)
		{
			
			if(i <= testarr1.length)
			{
				Assert.assertEquals(testarr1[i - 1], added[i - 1]);
				
			}
			else if(i - testarr1.length <= testarr2.length)
			{
				
				Assert.assertEquals(testarr2[e], added[i - 1]);
				
				e++;
			}
			
			else
			{
				
				Assert.assertEquals(testarr3[f], added[i - 1]);
				
				f++;
			}
		}
	}
	
	/**
	 * Test for method {@link ArrayUtils#concat(int[]...).
	 */
	@Test
	public void testConcatIntArrayArray()
	{
		final int[] testarr1 = new int[2];
		
		final int[] testarr2 = new int[2];
		
		final int[] added = ArrayUtils.concat(testarr1, testarr2);
		
		Assert.assertEquals(testarr1.length + testarr2.length, added.length);
	}
	
	/**
	 * Test for method {@link ArrayUtils#concat(int[]...).
	 */
	@Test
	public void testConcatIntArrayArray_two()
	{
		final int[] testarr1 = new int[2];
		testarr1[0] = 1;
		testarr1[1] = 2;
		
		final int[] testarr2 = new int[2];
		testarr2[0] = 3;
		testarr2[1] = 4;
		
		int e = 0;
		
		final int[] added = ArrayUtils.concat(testarr1, testarr2);
		
		for(int i = 1; i <= added.length; i++)
		{
			if(i <= testarr1.length)
			{
				Assert.assertEquals(testarr1[i - 1], added[i - 1]);
				
			}
			else
			{
				
				Assert.assertEquals(testarr2[e], added[i - 1]);
				
				e++;
			}
		}
	}
	
	/**
	 * Test for method {@link ArrayUtils#concat(int[]...).
	 */
	@Test
	public void testConcatIntArrayArray_three()
	{
		final int[] testarr1 = new int[2];
		testarr1[0] = 1;
		testarr1[1] = 2;
		
		final int[] testarr2 = new int[2];
		testarr2[0] = 3;
		testarr2[1] = 4;
		
		final int[] testarr3 = new int[2];
		testarr3[0] = 5;
		testarr3[1] = 6;
		
		int e = 0;
		int f = 0;
		
		final int[] added = ArrayUtils.concat(testarr1, testarr2, testarr3);
		
		for(int i = 1; i <= added.length; i++)
		{
			
			if(i <= testarr1.length)
			{
				Assert.assertEquals(testarr1[i - 1], added[i - 1]);
				
			}
			else if(i - testarr1.length <= testarr2.length)
			{
				
				Assert.assertEquals(testarr2[e], added[i - 1]);
				
				e++;
			}
			
			else
			{
				
				Assert.assertEquals(testarr3[f], added[i - 1]);
				
				f++;
			}
		}
	}
	
	/**
	 * Test for method {@link ArrayUtils#concat(long[]...).
	 */
	@Test
	public void testConcatLongArrayArray()
	{
		final long[] testarr1 = new long[2];
		
		final long[] testarr2 = new long[2];
		
		final long[] added = ArrayUtils.concat(testarr1, testarr2);
		
		Assert.assertEquals(testarr1.length + testarr2.length, added.length);
	}
	
	/**
	 * Test for method {@link ArrayUtils#concat(long[]...).
	 */
	@Test
	public void testConcatLongArrayArray_two()
	{
		final long[] testarr1 = new long[2];
		testarr1[0] = 1;
		testarr1[1] = 2;
		
		final long[] testarr2 = new long[2];
		testarr2[0] = 3;
		testarr2[1] = 4;
		
		int e = 0;
		
		final long[] added = ArrayUtils.concat(testarr1, testarr2);
		
		for(int i = 1; i <= added.length; i++)
		{
			if(i <= testarr1.length)
			{
				Assert.assertEquals(testarr1[i - 1], added[i - 1]);
				
			}
			else
			{
				
				Assert.assertEquals(testarr2[e], added[i - 1]);
				
				e++;
			}
		}
	}
	
	/**
	 * Test for method {@link ArrayUtils#concat(long[]...).
	 */
	@Test
	public void testConcatLongArrayArray_three()
	{
		final long[] testarr1 = new long[2];
		testarr1[0] = 1;
		testarr1[1] = 2;
		
		final long[] testarr2 = new long[2];
		testarr2[0] = 3;
		testarr2[1] = 4;
		
		final long[] testarr3 = new long[2];
		testarr3[0] = 5;
		testarr3[1] = 6;
		
		int e = 0;
		int f = 0;
		
		final long[] added = ArrayUtils.concat(testarr1, testarr2, testarr3);
		
		for(int i = 1; i <= added.length; i++)
		{
			
			if(i <= testarr1.length)
			{
				Assert.assertEquals(testarr1[i - 1], added[i - 1]);
				
			}
			else if(i - testarr1.length <= testarr2.length)
			{
				
				Assert.assertEquals(testarr2[e], added[i - 1]);
				
				e++;
			}
			
			else
			{
				
				Assert.assertEquals(testarr3[f], added[i - 1]);
				
				f++;
			}
		}
	}
	
	/**
	 * Test for method {@link ArrayUtils#concat(float[]...).
	 */
	@Test
	public void testConcatFloatArrayArray()
	{
		final float[] testarr1 = new float[2];
		
		final float[] testarr2 = new float[2];
		
		final float[] added = ArrayUtils.concat(testarr1, testarr2);
		
		Assert.assertEquals(testarr1.length + testarr2.length, added.length);
	}
	
	/**
	 * Test for method {@link ArrayUtils#concat(float[]...).
	 */
	@Test
	public void testConcatFloatArrayArray_two()
	{
		final float[] testarr1 = new float[2];
		testarr1[0] = 1;
		testarr1[1] = 2;
		
		final float[] testarr2 = new float[2];
		testarr2[0] = 3;
		testarr2[1] = 4;
		
		int e = 0;
		
		final float[] added = ArrayUtils.concat(testarr1, testarr2);
		
		for(int i = 1; i <= added.length; i++)
		{
			if(i <= testarr1.length)
			{
				Assert.assertEquals(testarr1[i - 1], added[i - 1], 0);
				
			}
			else
			{
				
				Assert.assertEquals(testarr2[e], added[i - 1], 0);
				
				e++;
			}
		}
	}
	
	/**
	 * Test for method {@link ArrayUtils#concat(float[]...).
	 */
	@Test
	public void testConcatFloatArrayArray_three()
	{
		final float[] testarr1 = new float[2];
		testarr1[0] = 1;
		testarr1[1] = 2;
		
		final float[] testarr2 = new float[2];
		testarr2[0] = 3;
		testarr2[1] = 4;
		
		final float[] testarr3 = new float[2];
		testarr3[0] = 5;
		testarr3[1] = 6;
		
		int e = 0;
		int f = 0;
		
		final float[] added = ArrayUtils.concat(testarr1, testarr2, testarr3);
		
		for(int i = 1; i <= added.length; i++)
		{
			
			if(i <= testarr1.length)
			{
				Assert.assertEquals(testarr1[i - 1], added[i - 1], 0);
				
			}
			else if(i - testarr1.length <= testarr2.length)
			{
				
				Assert.assertEquals(testarr2[e], added[i - 1], 0);
				
				e++;
			}
			
			else
			{
				
				Assert.assertEquals(testarr3[f], added[i - 1], 0);
				
				f++;
			}
		}
	}
	
	/**
	 * Test for method {@link ArrayUtils#concat(double[]...).
	 */
	@Test
	public void testConcatDoubleArrayArray()
	{
		final double[] testarr1 = new double[2];
		
		final double[] testarr2 = new double[2];
		
		final double[] added = ArrayUtils.concat(testarr1, testarr2);
		
		Assert.assertEquals(testarr1.length + testarr2.length, added.length);
	}
	
	/**
	 * Test for method {@link ArrayUtils#concat(double[]...).
	 */
	@Test
	public void testConcatDoubleArrayArray_two()
	{
		final double[] testarr1 = new double[2];
		testarr1[0] = 1;
		testarr1[1] = 2;
		
		final double[] testarr2 = new double[2];
		testarr2[0] = 3;
		testarr2[1] = 4;
		
		int e = 0;
		
		final double[] added = ArrayUtils.concat(testarr1, testarr2);
		
		for(int i = 1; i <= added.length; i++)
		{
			if(i <= testarr1.length)
			{
				Assert.assertEquals(testarr1[i - 1], added[i - 1], 0);
				
			}
			else
			{
				
				Assert.assertEquals(testarr2[e], added[i - 1], 0);
				
				e++;
			}
		}
	}
	
	/**
	 * Test for method {@link ArrayUtils#concat(double[]...).
	 */
	@Test
	public void testConcatDoubleArrayArray_three()
	{
		final double[] testarr1 = new double[2];
		testarr1[0] = 1;
		testarr1[1] = 2;
		
		final double[] testarr2 = new double[2];
		testarr2[0] = 3;
		testarr2[1] = 4;
		
		final double[] testarr3 = new double[2];
		testarr3[0] = 5;
		testarr3[1] = 6;
		
		int e = 0;
		int f = 0;
		
		final double[] added = ArrayUtils.concat(testarr1, testarr2, testarr3);
		
		for(int i = 1; i <= added.length; i++)
		{
			
			if(i <= testarr1.length)
			{
				Assert.assertEquals(testarr1[i - 1], added[i - 1], 0);
				
			}
			else if(i - testarr1.length <= testarr2.length)
			{
				
				Assert.assertEquals(testarr2[e], added[i - 1], 0);
				
				e++;
			}
			
			else
			{
				
				Assert.assertEquals(testarr3[f], added[i - 1], 0);
				
				f++;
			}
		}
	}
	
	/**
	 * Test for method {@link ArrayUtils#remove(Class, Object[], int).
	 */
	@Test
	public void testRemoveClassOfTTArrayInt()
	{
		final String[] testarr = new String[2];
		testarr[0] = "a";
		testarr[1] = "b";
		
		final String[] test = ArrayUtils.remove(String.class, testarr, 0);
		
		Assert.assertEquals(testarr[1], test[0]);
	}
	
	/**
	 * Test for method {@link ArrayUtils#remove(Class, Object[], int).
	 */
	@Test
	public void testRemoveClassOfTTArrayInt_two()
	{
		final String[] testarr = new String[2];
		testarr[0] = "a";
		testarr[1] = "b";
		
		final String[] test = ArrayUtils.remove(String.class, testarr, testarr.length - 1);
		
		Assert.assertEquals(testarr[0], test[0]);
	}
	
	/**
	 * Test for method {@link ArrayUtils#remove(Class, Object[], int).
	 */
	@Test
	public void testRemoveClassOfTTArrayInt_three()
	{
		final String[] testarr = new String[4];
		testarr[0] = "a";
		testarr[1] = "b";
		testarr[2] = "c";
		testarr[3] = "d";
		
		final String[] test = ArrayUtils.remove(String.class, testarr, 2);
		
		Assert.assertEquals(testarr[3], test[2]);
	}
	
	/**
	 * Test for method {@link ArrayUtils#remove(boolean[], int).
	 */
	@Test
	public void testRemoveBooleanArrayInt()
	{
		final boolean[] testarr = new boolean[2];
		testarr[0] = true;
		testarr[1] = false;
		
		final boolean[] test = ArrayUtils.remove(testarr, 0);
		
		Assert.assertEquals(testarr[1], test[0]);
	}
	
	/**
	 * Test for method {@link ArrayUtils#remove(boolean[], int).
	 */
	@Test
	public void testRemoveBooleanArrayInt_two()
	{
		final boolean[] testarr = new boolean[2];
		testarr[0] = true;
		testarr[1] = false;
		
		final boolean[] test = ArrayUtils.remove(testarr, testarr.length - 1);
		
		Assert.assertEquals(testarr[0], test[0]);
	}
	
	/**
	 * Test for method {@link ArrayUtils#remove(boolean[], int).
	 */
	@Test
	public void testRemoveBooleanArrayInt_three()
	{
		final boolean[] testarr = new boolean[4];
		testarr[0] = true;
		testarr[1] = false;
		testarr[2] = false;
		testarr[3] = true;
		
		final boolean[] test = ArrayUtils.remove(testarr, 2);
		
		Assert.assertEquals(testarr[3], test[2]);
	}
	
	/**
	 * Test for method {@link ArrayUtils#remove(char[], int).
	 */
	@Test
	public void testRemoveCharArrayInt()
	{
		final char[] testarr = new char[2];
		testarr[0] = 'a';
		testarr[1] = 'b';
		
		final char[] test = ArrayUtils.remove(testarr, 0);
		
		Assert.assertEquals(testarr[1], test[0]);
	}
	
	/**
	 * Test for method {@link ArrayUtils#remove(char[], int).
	 */
	@Test
	public void testRemoveCharArrayInt_two()
	{
		final char[] testarr = new char[2];
		testarr[0] = 'a';
		testarr[1] = 'b';
		
		final char[] test = ArrayUtils.remove(testarr, testarr.length - 1);
		
		Assert.assertEquals(testarr[0], test[0]);
	}
	
	/**
	 * Test for method {@link ArrayUtils#remove(char[], int).
	 */
	@Test
	public void testRemoveCharArrayInt_three()
	{
		final char[] testarr = new char[4];
		testarr[0] = 'a';
		testarr[1] = 'b';
		testarr[2] = 'c';
		testarr[3] = 'd';
		
		final char[] test = ArrayUtils.remove(testarr, 2);
		
		Assert.assertEquals(testarr[3], test[2]);
	}
	
	/**
	 * Test for method {@link ArrayUtils#remove(byte[], int).
	 */
	@Test
	public void testRemoveByteArrayInt()
	{
		final byte[] testarr = new byte[2];
		testarr[0] = 'a';
		testarr[1] = 'b';
		
		final byte[] test = ArrayUtils.remove(testarr, 0);
		
		Assert.assertEquals(testarr[1], test[0]);
	}
	
	/**
	 * Test for method {@link ArrayUtils#remove(byte[], int).
	 */
	@Test
	public void testRemoveByteArrayInt_two()
	{
		final byte[] testarr = new byte[2];
		testarr[0] = 'a';
		testarr[1] = 'b';
		
		final byte[] test = ArrayUtils.remove(testarr, testarr.length - 1);
		
		Assert.assertEquals(testarr[0], test[0]);
	}
	
	/**
	 * Test for method {@link ArrayUtils#remove(byte[], int).
	 */
	@Test
	public void testRemoveByteArrayInt_three()
	{
		final byte[] testarr = new byte[4];
		testarr[0] = 'a';
		testarr[1] = 'b';
		testarr[2] = 'c';
		testarr[3] = 'd';
		
		final byte[] test = ArrayUtils.remove(testarr, 2);
		
		Assert.assertEquals(testarr[3], test[2]);
	}
	
	/**
	 * Test for method {@link ArrayUtils#remove(short[], int).
	 */
	@Test
	public void testRemoveShortArrayInt()
	{
		final short[] testarr = new short[2];
		testarr[0] = 'a';
		testarr[1] = 'b';
		
		final short[] test = ArrayUtils.remove(testarr, 0);
		
		Assert.assertEquals(testarr[1], test[0]);
	}
	
	/**
	 * Test for method {@link ArrayUtils#remove(short[], int).
	 */
	@Test
	public void testRemoveShortArrayInt_two()
	{
		final short[] testarr = new short[2];
		testarr[0] = 'a';
		testarr[1] = 'b';
		
		final short[] test = ArrayUtils.remove(testarr, testarr.length - 1);
		
		Assert.assertEquals(testarr[0], test[0]);
	}
	
	/**
	 * Test for method {@link ArrayUtils#remove(short[], int).
	 */
	@Test
	public void testRemoveShortArrayInt_three()
	{
		final short[] testarr = new short[4];
		testarr[0] = 'a';
		testarr[1] = 'b';
		testarr[2] = 'c';
		testarr[3] = 'd';
		
		final short[] test = ArrayUtils.remove(testarr, 2);
		
		Assert.assertEquals(testarr[3], test[2]);
	}
	
	/**
	 * Test for method {@link ArrayUtils#remove(int[], int).
	 */
	@Test
	public void testRemoveIntArrayInt()
	{
		final int[] testarr = new int[2];
		testarr[0] = 1;
		testarr[1] = 2;
		
		final int[] test = ArrayUtils.remove(testarr, 0);
		
		Assert.assertEquals(testarr[1], test[0]);
	}
	
	/**
	 * Test for method {@link ArrayUtils#remove(int[], int).
	 */
	@Test
	public void testRemoveIntArrayInt_two()
	{
		final int[] testarr = new int[2];
		testarr[0] = 1;
		testarr[1] = 2;
		
		final int[] test = ArrayUtils.remove(testarr, testarr.length - 1);
		
		Assert.assertEquals(testarr[0], test[0]);
	}
	
	/**
	 * Test for method {@link ArrayUtils#remove(int[], int).
	 */
	@Test
	public void testRemoveIntArrayInt_three()
	{
		final int[] testarr = new int[4];
		testarr[0] = 1;
		testarr[1] = 2;
		testarr[2] = 3;
		testarr[3] = 4;
		
		final int[] test = ArrayUtils.remove(testarr, 2);
		
		Assert.assertEquals(testarr[3], test[2]);
	}
	
	/**
	 * Test for method {@link ArrayUtils#remove(long[], int).
	 */
	@Test
	public void testRemoveLongArrayInt()
	{
		final long[] testarr = new long[2];
		testarr[0] = 1;
		testarr[1] = 2;
		
		final long[] test = ArrayUtils.remove(testarr, 0);
		
		Assert.assertEquals(testarr[1], test[0]);
	}
	
	/**
	 * Test for method {@link ArrayUtils#remove(long[], int).
	 */
	@Test
	public void testRemoveLongArrayInt_two()
	{
		final long[] testarr = new long[2];
		testarr[0] = 1;
		testarr[1] = 2;
		
		final long[] test = ArrayUtils.remove(testarr, testarr.length - 1);
		
		Assert.assertEquals(testarr[0], test[0]);
	}
	
	/**
	 * Test for method {@link ArrayUtils#remove(long[], int).
	 */
	@Test
	public void testRemoveLongArrayInt_three()
	{
		final long[] testarr = new long[4];
		testarr[0] = 1;
		testarr[1] = 2;
		testarr[2] = 3;
		testarr[3] = 4;
		
		final long[] test = ArrayUtils.remove(testarr, 2);
		
		Assert.assertEquals(testarr[3], test[2]);
	}
	
	/**
	 * Test for method {@link ArrayUtils#remove(float[], int).
	 */
	@Test
	public void testRemoveFloatArrayInt()
	{
		final float[] testarr = new float[2];
		testarr[0] = 1;
		testarr[1] = 2;
		
		final float[] test = ArrayUtils.remove(testarr, 0);
		
		Assert.assertEquals(testarr[1], test[0], 0);
	}
	
	/**
	 * Test for method {@link ArrayUtils#remove(float[], int).
	 */
	@Test
	public void testRemoveFloatArrayInt_two()
	{
		final float[] testarr = new float[2];
		testarr[0] = 1;
		testarr[1] = 2;
		
		final float[] test = ArrayUtils.remove(testarr, testarr.length - 1);
		
		Assert.assertEquals(testarr[0], test[0], 0);
	}
	
	/**
	 * Test for method {@link ArrayUtils#remove(float[], int).
	 */
	@Test
	public void testRemoveFloatArrayInt_three()
	{
		final float[] testarr = new float[4];
		testarr[0] = 1;
		testarr[1] = 2;
		testarr[2] = 3;
		testarr[3] = 4;
		
		final float[] test = ArrayUtils.remove(testarr, 2);
		
		Assert.assertEquals(testarr[3], test[2], 0);
	}
	
	/**
	 * Test for method {@link ArrayUtils#remove(double[], int).
	 */
	@Test
	public void testRemoveDoubleArrayInt()
	{
		final double[] testarr = new double[2];
		testarr[0] = 1.2;
		testarr[1] = 2.2;
		
		final double[] test = ArrayUtils.remove(testarr, 0);
		
		Assert.assertEquals(testarr[1], test[0], 0);
	}
	
	/**
	 * Test for method {@link ArrayUtils#remove(double[], int).
	 */
	@Test
	public void testRemoveDoubleArrayInt_two()
	{
		final double[] testarr = new double[2];
		testarr[0] = 1.2;
		testarr[1] = 2.2;
		
		final double[] test = ArrayUtils.remove(testarr, testarr.length - 1);
		
		Assert.assertEquals(testarr[0], test[0], 0);
	}
	
	/**
	 * Test for method {@link ArrayUtils#remove(double[], int).
	 */
	@Test
	public void testRemoveDoubleArrayInt_three()
	{
		final double[] testarr = new double[4];
		testarr[0] = 1.2;
		testarr[1] = 2.2;
		testarr[2] = 3.2;
		testarr[3] = 4.2;
		
		final double[] test = ArrayUtils.remove(testarr, 2);
		
		Assert.assertEquals(testarr[3], test[2], 0);
	}
	
	/**
	 * Test for method {@link ArrayUtils#getIterable(Object[]).
	 */
	@Test
	public void testGetIterable()
	{
		final String[] testarr = new String[2];
		Iterator<String> iter;
		
		// fill iterator
		iter = ArrayUtils.getIterable(testarr).iterator();
		
		Assert.assertTrue(iter.hasNext());
		
	}
	
	/**
	 * Test for method {@link ArrayUtils#getIterable(Object[]).
	 */
	@Test(expected = NoSuchElementException.class)
	public void testGetIterable_two()
	{
		final String[] testarr = new String[1];
		Iterator<String> iter;
		
		// fill iterator
		iter = ArrayUtils.getIterable(testarr).iterator();
		
		for(int i = 0; i <= testarr.length; i++)
		{
			iter.next();
		}
		
	}
	
	/**
	 * Test for method {@link ArrayUtils#getIterable(Object[]).
	 */
	@Test
	public void testGetIterable_three()
	{
		final String[] testarr = new String[2];
		testarr[0] = "test0";
		testarr[1] = "test1";
		
		Iterator<String> iter;
		
		// fill iterator
		iter = ArrayUtils.getIterable(testarr).iterator();
		
		Assert.assertEquals(testarr[0], iter.next());
		
	}
	
	/**
	 * Test for method {@link ArrayUtils#getIterable(Object[]).
	 */
	@Test(expected = UnsupportedOperationException.class)
	public void testGetIterable_four()
	{
		final String[] testarr = new String[2];
		testarr[0] = "test0";
		testarr[1] = "test1";
		
		Iterator<String> iter;
		
		// fill iterator
		iter = ArrayUtils.getIterable(testarr).iterator();
		iter.remove();
		
	}
	
	@Test
	public void testGetIterable_five()
	{
		final String[] array = {"a", "b", "c", "d", "e", "f"};
		final StringBuilder sb = new StringBuilder();
		final Iterator<String> iter = ArrayUtils.getIterator(array, 2, 3);
		while(iter.hasNext())
		{
			sb.append(iter.next());
		}
		Assert.assertEquals("cde", sb.toString());
	}
	
	/*
	 * @Test public void testGetIterator() { //schon in getIterable getestet
	 * weil getIterable getIterator aufruft fail("Not yet implemented"); }
	 */
	
	@Ignore
	public static enum NAMES
	{
		HANS,
		FRITZ,
		FELIX,
		THOMAS;
	}
	
	/**
	 * Test for method {@link ArrayUtils#sortByName(Enum[]).
	 */
	@Test
	public void testSortByName()
	{
		// Enum[] arr = new Enum[2];
		// arr[0] = ArrayUtilsEnum2.TEST;
		// arr[1] = ArrayUtilsEnum2.C;
		
		final NAMES[] nameArr = new NAMES[4];
		nameArr[0] = NAMES.THOMAS;
		nameArr[1] = NAMES.HANS;
		nameArr[2] = NAMES.FRITZ;
		nameArr[3] = NAMES.FELIX;
		
		ArrayUtils.sortByName(nameArr);
		
		Assert.assertEquals(NAMES.FELIX, nameArr[0]);
		Assert.assertEquals(NAMES.FRITZ, nameArr[1]);
		Assert.assertEquals(NAMES.HANS, nameArr[2]);
		Assert.assertEquals(NAMES.THOMAS, nameArr[3]);
		
		// ArrayUtils.sortByName(arr);
		
	}
	
	/**
	 * Test for method {@link ArrayUtils#unwrap(Boolean[]).
	 */
	@Test
	public void testUnwrapBoolean()
	{
		
		final Boolean[] actual = {true, false};
		final boolean[] expected = ArrayUtils.unwrap(actual);
		
		Assert.assertEquals(true, expected.getClass().getComponentType().isPrimitive());
	}
	
	/**
	 * Test for method {@link ArrayUtils#unwrap(Boolean[]).
	 */
	@Test
	public void testUnwrapBoolean2()
	{
		
		final Boolean[] actual = {true, false};
		
		Assert.assertFalse(actual.getClass().equals(ArrayUtils.unwrap(actual).getClass()));
		
	}
	
	/**
	 * Test for method {@link ArrayUtils#unwrap(Boolean[]).
	 */
	@Test
	public void testUnwrapBoolean3()
	{
		
		final Boolean[] actual = {true, false};
		final boolean[] expected = ArrayUtils.unwrap(actual);
		
		Assert.assertEquals(actual[0], expected[0]);
		
	}
	
	/**
	 * Test for method {@link ArrayUtils#unwrap(Boolean[]).
	 */
	@Test(expected = NullPointerException.class)
	public void testUnwrapBoolean4()
	{
		
		final Boolean[] actual = null;
		final boolean[] expected = ArrayUtils.unwrap(actual);
		
		Assert.assertEquals(actual[0], expected[0]);
		
	}
	
	/**
	 * Test for method {@link ArrayUtils#unwrap(Byte[]).
	 */
	@Test
	public void testUnwrapByte()
	{
		
		final Byte[] actual = {1, 2};
		final byte[] expected = ArrayUtils.unwrap(actual);
		Assert.assertEquals(true, expected.getClass().getComponentType().isPrimitive());
		
	}
	
	/**
	 * Test for method {@link ArrayUtils#unwrap(Byte[]).
	 */
	@Test
	public void testUnwrapByte2()
	{
		
		final Byte[] actual = {1, 2};
		
		Assert.assertFalse(actual.getClass().equals(ArrayUtils.unwrap(actual).getClass()));
		
	}
	
	/**
	 * Test for method {@link ArrayUtils#unwrap(Byte[]).
	 */
	@Test
	public void testUnwrapByte3()
	{
		
		final Byte[] actual = {1, 2};
		final byte[] expected = ArrayUtils.unwrap(actual);
		
		Assert.assertEquals(actual[0], (Byte)expected[0]);
		
	}
	
	/**
	 * Test for method {@link ArrayUtils#unwrap(Byte[]).
	 */
	@Test(expected = NullPointerException.class)
	public void testUnwrapByte4()
	{
		
		final Byte[] actual = null;
		final byte[] expected = ArrayUtils.unwrap(actual);
		
		Assert.assertEquals(actual[0], (Byte)expected[0]);
		
	}
	
	/**
	 * Test for method {@link ArrayUtils#unwrap(Character[]).
	 */
	@Test
	public void testUnwrapCharacter()
	{
		
		final Character[] actual = {'b', 'a'};
		final char[] expected = ArrayUtils.unwrap(actual);
		Assert.assertEquals(true, expected.getClass().getComponentType().isPrimitive());
		
	}
	
	/**
	 * Test for method {@link ArrayUtils#unwrap(Character[]).
	 */
	@Test
	public void testUnwrapCharacter2()
	{
		
		final Character[] actual = {'b', 'a'};
		
		Assert.assertFalse(actual.getClass().equals(ArrayUtils.unwrap(actual).getClass()));
		
	}
	
	/**
	 * Test for method {@link ArrayUtils#unwrap(Character[]).
	 */
	@Test
	public void testUnwrapCharacter3()
	{
		
		final Character[] actual = {'b', 'a'};
		final char[] expected = ArrayUtils.unwrap(actual);
		
		Assert.assertEquals(actual[0], (Character)expected[0]);
		
	}
	
	/**
	 * Test for method {@link ArrayUtils#unwrap(Character[]).
	 */
	@Test(expected = NullPointerException.class)
	public void testUnwrapCharacter4()
	{
		
		final Character[] actual = null;
		final char[] expected = ArrayUtils.unwrap(actual);
		
		Assert.assertEquals(actual[0], (Character)expected[0]);
		
	}
	
	/**
	 * Test for method {@link ArrayUtils#unwrap(Integer[]).
	 */
	@Test
	public void testUnwrapInteger()
	{
		
		final Integer[] actual = {1, 2};
		final int[] expected = ArrayUtils.unwrap(actual);
		Assert.assertEquals(true, expected.getClass().getComponentType().isPrimitive());
		
	}
	
	/**
	 * Test for method {@link ArrayUtils#unwrap(Integer[]).
	 */
	@Test
	public void testUnwrapInteger2()
	{
		
		final Integer[] actual = {1, 2};
		
		Assert.assertFalse(actual.getClass().equals(ArrayUtils.unwrap(actual).getClass()));
		
	}
	
	/**
	 * Test for method {@link ArrayUtils#unwrap(Integer[]).
	 */
	@Test
	public void testUnwrapInteger3()
	{
		
		final Integer[] actual = {1, 2};
		final int[] expected = ArrayUtils.unwrap(actual);
		
		Assert.assertEquals(actual[0], (Integer)expected[0]);
		
	}
	
	/**
	 * Test for method {@link ArrayUtils#unwrap(Integer[]).
	 */
	@Test(expected = NullPointerException.class)
	public void testUnwrapInteger4()
	{
		
		final Integer[] actual = null;
		final int[] expected = ArrayUtils.unwrap(actual);
		
		Assert.assertEquals(actual[0], (Integer)expected[0]);
		
	}
	
	/**
	 * Test for method {@link ArrayUtils#unwrap(Long[]).
	 */
	@Test
	public void testUnwrapLong()
	{
		
		final Long[] actual = {(long)1, (long)2};
		final long[] expected = ArrayUtils.unwrap(actual);
		Assert.assertEquals(true, expected.getClass().getComponentType().isPrimitive());
		
	}
	
	/**
	 * Test for method {@link ArrayUtils#unwrap(Long[]).
	 */
	@Test
	public void testUnwrapLong2()
	{
		
		final Long[] actual = {(long)1, (long)2};
		
		Assert.assertFalse(actual.getClass().equals(ArrayUtils.unwrap(actual).getClass()));
		
	}
	
	/**
	 * Test for method {@link ArrayUtils#unwrap(Long[]).
	 */
	@Test
	public void testUnwrapLong3()
	{
		
		final Long[] actual = {(long)1, (long)2};
		final long[] expected = ArrayUtils.unwrap(actual);
		
		Assert.assertEquals(actual[0], (Long)expected[0]);
		
	}
	
	/**
	 * Test for method {@link ArrayUtils#unwrap(Long[]).
	 */
	@Test(expected = NullPointerException.class)
	public void testUnwrapLong4()
	{
		
		final Long[] actual = null;
		final long[] expected = ArrayUtils.unwrap(actual);
		
		Assert.assertEquals(actual[0], (Long)expected[0]);
		
	}
	
	/**
	 * Test for method {@link ArrayUtils#unwrap(Short[]).
	 */
	@Test
	public void testUnwrapShort()
	{
		
		final Short[] actual = {1, 2};
		final short[] expected = ArrayUtils.unwrap(actual);
		
		Assert.assertEquals(true, expected.getClass().getComponentType().isPrimitive());
		
	}
	
	/**
	 * Test for method {@link ArrayUtils#unwrap(Short[]).
	 */
	@Test
	public void testUnwrapShort2()
	{
		
		final Short[] actual = {1, 2};
		
		Assert.assertFalse(actual.getClass().equals(ArrayUtils.unwrap(actual).getClass()));
		
	}
	
	/**
	 * Test for method {@link ArrayUtils#unwrap(Short[]).
	 */
	@Test
	public void testUnwrapShort3()
	{
		
		final Short[] actual = {1, 2};
		final short[] expected = ArrayUtils.unwrap(actual);
		
		Assert.assertEquals(actual[0], (Short)expected[0]);
		
	}
	
	/**
	 * Test for method {@link ArrayUtils#unwrap(Short[]).
	 */
	@Test(expected = NullPointerException.class)
	public void testUnwrapShort4()
	{
		
		final Short[] actual = null;
		final short[] expected = ArrayUtils.unwrap(actual);
		
		Assert.assertEquals(actual[0], (Short)expected[0]);
		
	}
	
}
