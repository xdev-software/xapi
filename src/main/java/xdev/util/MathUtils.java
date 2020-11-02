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


import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Collection;

import xdev.lang.LibraryMember;


/**
 * 
 * <p>
 * The <code>MathUtils</code> class provides utility methods for mathematics and
 * number handling.
 * </p>
 * 
 * @author XDEV Software
 * 
 * @since 2.0
 */
@LibraryMember
public final class MathUtils
{
	
	/**
	 * <p>
	 * <code>MathUtils</code> class can not be instantiated. The class should be
	 * used as utility class: <code>MathUtils.round(5.87654874, 2);</code>.
	 * </p>
	 */
	private MathUtils()
	{
	}
	
	
	/**
	 * 
	 * 
	 * <b> Examples </b>
	 * 
	 * <pre>
	 * MathUtils.round(1020.3044, 2);       returns 1020.30
	 * MathUtils.round(1020.3044323210, 4); returns 1020.3044
	 * MathUtils.round(-1020.3044,2);       returns -1020.30
	 * MathUtils.round(-1020.304446023,5);  returns 31020.30445
	 * </pre>
	 * 
	 * <b></b>
	 * 
	 * 
	 */
	public static double round(double value, int decimals)
	{
		if(!(Double.isInfinite(value) || Double.isNaN(value)))
		{
			decimals = (int)Math.pow(10,decimals);
			value = Math.round(value * decimals);
			value /= decimals;
		}
		
		return value;
	}
	
	
	/**
	 * 
	 * 
	 * <b> Examples </b>
	 * 
	 * <pre>
	 * MathUtils.getDecimals(1020.30552, 4); returns 3055.0
	 * MathUtils.getDecimals(1020.97018, 2); returns 97.0
	 * </pre>
	 * 
	 */
	public static int getDecimals(double value, int decimals)
	{
		StringBuffer sb = new StringBuffer("#.");
		for(int i = 0; i < decimals; i++)
		{
			sb.append("0");
		}
		
		String str = new DecimalFormat(sb.toString()).format(value);
		str = str.substring(str.length() - decimals,str.length());
		
		return Integer.parseInt(str);
	}
	
	
	/**
	 * Returns the sum of all values.
	 * 
	 * <b> Example </b>
	 * 
	 * <pre>
	 *  XdevList<Number> liste = new XdevList<Number>();
	 * liste.add(200);
	 * liste.add(5.20);
	 * liste.add(20);
	 * liste.add(0);							returns 225.2
	 * 
	 * XdevList<Number> liste = new XdevList<Number>();
	 * liste.add(200);
	 * liste.add(5.20);
	 * liste.add(20);
	 * liste.add(0);
	 * liste.add(-30);							returns 195.2
	 * 
	 * XdevList<Number> liste = new XdevList<Number>();
	 * liste.add(-200);
	 * liste.add(-5.20);
	 * liste.add(-20);
	 * liste.add(-0);							returns -225.2
	 * </pre>
	 * 
	 * <b>Hint</b>
	 * 
	 * <pre>
	 * If the single summands are already in the program 
	 * (as variable, method return in a loop, etc.), 
	 * then it has more performance to sum the values 
	 * yourself , than put them into a list and sum it 
	 * with this method.
	 * </pre>
	 * 
	 * @param values
	 *            Collection of <code>Number</code>s which will be summed up.
	 * @return The sum of all values as <code>double</code>.
	 */
	public static double sum(Collection<? extends Number> values)
	{
		if(values == null)
		{
			return 0d;
		}
		
		double sum = 0d;
		
		for(Number number : values)
		{
			if(number != null)
			{
				sum += number.doubleValue();
			}
		}
		
		return sum;
	}
	
	
	/**
	 * Returns the sum of all <code>Array</code> values. <b> Examples </b>
	 * 
	 * <pre>
	 * MathUtils.sum(new int[]{10,25,30,45});         returns 110
	 * MathUtils.sum(new int[]{10,25,30,45,-9});      returns 101
	 * MathUtils.sum(new int[]{-10,-25,-30,-45,-9});  returns -119
	 * </pre>
	 * 
	 * @param values
	 *            Array of <code>int</code> which will be sum
	 * @return the sum as <code>int</code>
	 * 
	 * @see MathUtils#sum(Collection)
	 * @see MathUtils#sum(double...)
	 * @see MathUtils#sum(long...)
	 * 
	 */
	public static long sum(int... values)
	{
		if(values == null)
		{
			return 0;
		}
		
		long sum = 0;
		for(int value : values)
		{
			sum += value;
		}
		
		return sum;
	}
	
	
	/**
	 * Returns the sum of all <code>Array</code> values.
	 * 
	 * <b> Examples </b>
	 * 
	 * <pre>
	 * MathUtils.sum(new long[]{58976,58895,9975,01,55});      returns 127902
	 * MathUtils.sum(new long[]{58976,58895,9975,01,55,-30});  returns 127872
	 * MathUtils.sum(new long[]{-58976,-58895,-9975,-01,-55}); returns -127902
	 * </pre>
	 * 
	 * @param values
	 *            Array of <code>long</code> which will be sum
	 * @return the sum as <code>long</code>
	 * 
	 * @see MathUtils#sum(Collection)
	 * @see MathUtils#sum(double...)
	 * @see MathUtils#sum(int...)
	 * 
	 */
	public static long sum(long... values)
	{
		if(values == null)
		{
			return 0;
		}
		
		long sum = 0;
		for(long value : values)
		{
			sum += value;
		}
		
		return sum;
	}
	
	
	/**
	 * Returns the sum of all <code>Array</code> values.
	 * 
	 * <b> Examples </b>
	 * 
	 * <pre>
	 * MathUtils.sum(new double[]{1.50,5.20,30});       returns 36.7
	 * MathUtils.sum(new double[]{1.50,5.20,-10.78});   returns -4.08
	 * MathUtils.sum(new double[]{-1.50,-5.20,-10.78}); returns -17.48
	 * </pre>
	 * 
	 * @param values
	 *            Array of <code>double</code> which will be sum
	 * @return the sum as <code>double</code>
	 * 
	 * @see MathUtils#sum(Collection)
	 * @see MathUtils#sum(int...)
	 * @see MathUtils#sum(long...)
	 * 
	 */
	public static double sum(double... values)
	{
		if(values == null)
		{
			return 0;
		}
		
		double sum = 0;
		for(double value : values)
		{
			sum += value;
		}
		
		return sum;
	}
	
	
	/**
	 * Returns the average of the values(the arithmetic average(sum of all
	 * values divided by the number of values)).
	 * 
	 * <b> Example </b>
	 * 
	 * <pre>
	 * XdevList<Number> expected = new XdevList<Number>();
	 * expected.add(200);
	 * expected.add(5.20);
	 * expected.add(20);
	 * expected.add(0);						returns 56.3
	 * 
	 * XdevList<Number> liste = new XdevList<Number>();
	 * liste.add(200);
	 * liste.add(5.20);
	 * liste.add(20);
	 * liste.add(0);
	 * liste.add(-30);							returns 39.04
	 * 
	 * XdevList<Number> liste = new XdevList<Number>();
	 * liste.add(-200);
	 * liste.add(-5.20);
	 * liste.add(-20);
	 * liste.add(-0);							returns -56.3
	 * 
	 * </pre>
	 * 
	 * 
	 * @param values
	 *            List of <code>Number</code>s which average will be calculated.
	 * @return Returns the average of the values. If <code>values</code> is
	 *         empty or <code>null</code> then it returns 0.0;
	 * 
	 * @see MathUtils#average(double...)
	 * @see MathUtils#average(int...)
	 * @see MathUtils#average(long...)
	 */
	public static double average(Collection<? extends Number> values)
	{
		if(values == null || values.size() == 0)
		{
			return 0d;
		}
		
		double sum = 0d;
		double count = 0d;
		for(Number number : values)
		{
			if(number != null)
			{
				sum += number.doubleValue();
				count++;
			}
		}
		
		return sum / count;
	}
	
	
	/**
	 * Returns the average of the values(the arithmetic average(sum of all
	 * values divided by the number of values)).
	 * 
	 * <br>
	 * <b> Examples: </b>
	 * 
	 * <pre>
	 * MathUtils.average(new int[]{10,25,30,45});         returns 27.5
	 * MathUtils.average(new int[]{10,25,30,45,-9});      returns 20.2
	 * MathUtils.average(new int[]{-10,-25,-30,-45,-9});  returns -23.8
	 * </pre>
	 * 
	 * @param values
	 *            List of <code>int</code> which average will be calculated.
	 * @return Returns the average of the <code>List</code> values. If
	 *         <code>values</code> is empty or <code>null</code> then it returns
	 *         0;
	 * 
	 * @see MathUtils#average(double...)
	 * @see MathUtils#average(Collection)
	 * @see MathUtils#average(long...)
	 */
	public static double average(int... values)
	{
		if(values == null || values.length == 0)
		{
			return 0;
		}
		
		return (double)sum(values) / (double)values.length;
	}
	
	
	/**
	 * Returns the average of the values(the arithmetic average(sum of all
	 * values divided by the number of values)).
	 * 
	 * <br>
	 * <b> Examples: </b>
	 * 
	 * <pre>
	 * MathUtils.average(new long[]{58976,58895,9975,04,55});       returns 25581.0
	 * MathUtils.average(new long[]{28976,58895,9975,01,55,-30});   returns 16312.0
	 * MathUtils.average(new long[]{-58976,-58895,-9975,-01,-55});  returns -25580.4
	 * </pre>
	 * 
	 * @param values
	 *            List of <code>long</code> which average will be calculated.
	 * @return Returns the average of the <code>List</code> values. If
	 *         <code>values</code> is empty or <code>null</code> then it returns
	 *         0;
	 * @see MathUtils#average(double...)
	 * @see MathUtils#average(int...)
	 * @see MathUtils#average(Collection)
	 */
	public static double average(long... values)
	{
		if(values == null || values.length == 0)
		{
			return 0;
		}
		
		return (double)sum(values) / (double)values.length;
	}
	
	
	/**
	 * Returns the average of the values(the arithmetic average(sum of all
	 * values divided by the number of values)).
	 * 
	 * <br>
	 * <b> Examples: </b>
	 * 
	 * <pre>
	 * MathUtils.average(new double[]{1.50,5.20,30});            returns 12.233333333333334
	 * MathUtils.average(new double[]{1.50,5.20,30,-10.78});     returns 6.48
	 * MathUtils.average(new double[]{-1.50,-5.20,-30,-10.78});  returns -11.870000000000001
	 * </pre>
	 * 
	 * @param values
	 *            List of <code>double</code> which average will be calculated.
	 * @return Returns the average of the <code>List</code> values. If
	 *         <code>values</code> is empty or <code>null</code> then it returns
	 *         0;
	 * @see MathUtils#average(Collection)
	 * @see MathUtils#average(int...)
	 * @see MathUtils#average(long...)
	 */
	public static double average(double... values)
	{
		if(values == null || values.length == 0)
		{
			return 0;
		}
		
		return sum(values) / (double)values.length;
	}
	
	
	/**
	 * Returns the smallest of all numbers.
	 * 
	 * <br>
	 * <b> Examples </b>
	 * 
	 * <pre>
	 * MathUtils.min(new byte[]{5,6,2,3});         returns 2
	 * MathUtils.min(new byte[]{5,-6,2,-3});       returns -6
	 * MathUtils.min(new byte[]{-5,-6,-2,-3});     returns -6
	 * </pre>
	 * 
	 * @param numbers
	 *            List of <code>byte</code> of which the minimum will be
	 *            calculated.
	 * @return Returns the minimum value of the <code>List</code> numbers. If
	 *         <code>numbers</code> is empty or <code>null</code> then it
	 *         returns the minimum value of the data type <code>byte</code>;
	 * 
	 * @see MathUtils#min(byte...)
	 * @see MathUtils#min(int...)
	 * @see MathUtils#min(short...)
	 * @see MathUtils#min(long...)
	 * @see MathUtils#min(double...)
	 */
	public static byte min(byte... numbers)
	{
		if(numbers == null || numbers.length == 0)
		{
			return Byte.MIN_VALUE;
		}
		
		byte min = numbers[0];
		for(int i = 1; i < numbers.length; i++)
		{
			if(numbers[i] < min)
			{
				min = numbers[i];
			}
		}
		
		return min;
	}
	
	
	/**
	 * Returns the biggest of all numbers.
	 * 
	 * <br>
	 * <b> Examples </b>
	 * 
	 * <pre>
	 * MathUtils.max(new byte[]{5,6,2,3});         returns 6
	 * MathUtils.max(new byte[]{5,-6,2,-3});       returns 5
	 * MathUtils.max(new byte[]{-5,-6,-2,-3});     returns -2
	 * </pre>
	 * 
	 * @param numbers
	 *            List of <code>byte</code> of which the maximum will be
	 *            calculated.
	 * @return Returns the maximum value of the <code>List</code> numbers. If
	 *         <code>numbers</code> is empty or <code>null</code> then it
	 *         returns the maximum value of the data type <code>byte</code>;
	 * 
	 * @see MathUtils#max(float...)
	 * @see MathUtils#max(int...)
	 * @see MathUtils#max(short...)
	 * @see MathUtils#max(long...)
	 * @see MathUtils#max(double...)
	 */
	public static byte max(byte... numbers)
	{
		if(numbers == null || numbers.length == 0)
		{
			return Byte.MAX_VALUE;
		}
		
		byte max = numbers[0];
		for(int i = 1; i < numbers.length; i++)
		{
			if(numbers[i] > max)
			{
				max = numbers[i];
			}
		}
		
		return max;
	}
	
	
	/**
	 * Returns the smallest of all numbers.
	 * 
	 * <br>
	 * <b> Examples </b>
	 * 
	 * <pre>
	 * MathUtils.min(new short[]{5,4,7,9,15,3});       returns 3
	 * MathUtils.min(new short[]{5,-4,7,-9,15,-3});    returns -9
	 * MathUtils.min(new short[]{-5,-4,-7,-9,-15,-3}); returns -15
	 * </pre>
	 * 
	 * @param numbers
	 *            List of <code>short</code> of which the minimum will be
	 *            calculated.
	 * @return Returns the minimum value of the <code>List</code> numbers. If
	 *         <code>numbers</code> is empty or <code>null</code> then it
	 *         returns the minimum value of the data type <code>short</code>;
	 * 
	 * @see MathUtils#min(byte...)
	 * @see MathUtils#min(int...)
	 * @see MathUtils#min(float...)
	 * @see MathUtils#min(long...)
	 * @see MathUtils#min(double...)
	 */
	public static short min(short... numbers)
	{
		if(numbers == null || numbers.length == 0)
		{
			return Short.MIN_VALUE;
		}
		
		short min = numbers[0];
		for(int i = 1; i < numbers.length; i++)
		{
			if(numbers[i] < min)
			{
				min = numbers[i];
			}
		}
		
		return min;
	}
	
	
	/**
	 * Returns the biggest of all numbers.
	 * 
	 * <br>
	 * <b> Examples </b>
	 * 
	 * <pre>
	 * MathUtils.max(new short[]{5,4,7,9,15,3});       returns 15
	 * MathUtils.max(new short[]{5,-4,7,-9,15,-3});    returns 15
	 * MathUtils.max(new short[]{-5,-4,-7,-9,-15,-3}); returns -3
	 * </pre>
	 * 
	 * @param numbers
	 *            List of <code>short</code> of which the maximum will be
	 *            calculated.
	 * @return Returns the maximum value of the <code>List</code> numbers. If
	 *         <code>numbers</code> is empty or <code>null</code> then it
	 *         returns the maximum value of the data type <code>short</code>;
	 * 
	 * @see MathUtils#max(float...)
	 * @see MathUtils#max(int...)
	 * @see MathUtils#max(byte...)
	 * @see MathUtils#max(long...)
	 * @see MathUtils#max(double...)
	 */
	public static short max(short... numbers)
	{
		if(numbers == null || numbers.length == 0)
		{
			return Short.MAX_VALUE;
		}
		
		short max = numbers[0];
		for(int i = 1; i < numbers.length; i++)
		{
			if(numbers[i] > max)
			{
				max = numbers[i];
			}
		}
		
		return max;
	}
	
	
	/**
	 * Returns the biggest of all numbers.
	 * 
	 * <br>
	 * <b> Examples </b>
	 * 
	 * <pre>
	 * MathUtils.max(new int[]{5,6,2,1,3});        returns 6
	 * MathUtils.max(new int[]{5,9,15,-5,-20});    returns 15
	 * MathUtils.max(new int[]{-5,-9,-15,-5,-20}); returns -5
	 * 
	 * </pre>
	 * 
	 * 
	 * 
	 * @param numbers
	 *            List of <code>int</code> of which the maximum will be
	 *            calculated.
	 * @return Returns the maximum value of the <code>List</code> numbers. If
	 *         <code>numbers</code> is empty or <code>null</code> then it
	 *         returns the maximum value of the data type <code>int</code>;
	 * 
	 * @see MathUtils#max(byte...)
	 * @see MathUtils#max(short...)
	 * @see MathUtils#max(float...)
	 * @see MathUtils#max(long...)
	 * @see MathUtils#max(double...)
	 */
	public static int max(int... numbers)
	{
		if(numbers == null || numbers.length == 0)
		{
			return Integer.MAX_VALUE;
		}
		
		int max = numbers[0];
		for(int i = 1; i < numbers.length; i++)
		{
			if(numbers[i] > max)
			{
				max = numbers[i];
			}
		}
		
		return max;
	}
	
	
	/**
	 * Returns the smallest of all numbers.
	 * 
	 * <blockquote> <b> Examples </b>
	 * 
	 * <pre>
	 * MathUtils.min(new int[]{5,6,2,1,3});        returns 1
	 * MathUtils.min(new int[]{5,9,15,-5,-20});    returns -20
	 * MathUtils.min(new int[]{-5,-9,-15,-5,-20}); returns -20
	 * </pre>
	 * 
	 * </blockquote>
	 * 
	 * @param numbers
	 *            List of <code>int</code> of which the minimum will be
	 *            calculated.
	 * @return Returns the minimum value of the <code>List</code> numbers. If
	 *         <code>numbers</code> is empty or <code>null</code> then it
	 *         returns the minimum value of the data type <code>int</code>;
	 * 
	 * @see MathUtils#min(byte...)
	 * @see MathUtils#min(short...)
	 * @see MathUtils#min(float...)
	 * @see MathUtils#min(long...)
	 * @see MathUtils#min(double...)
	 */
	public static int min(int... numbers)
	{
		if(numbers == null || numbers.length == 0)
		{
			return Integer.MIN_VALUE;
		}
		
		int min = numbers[0];
		for(int i = 1; i < numbers.length; i++)
		{
			if(numbers[i] < min)
			{
				min = numbers[i];
			}
		}
		
		return min;
	}
	
	
	/**
	 * Returns the biggest of all numbers.
	 * 
	 * <b> Examples </b>
	 * 
	 * <pre>
	 * MathUtils.max(new long[]{8,6,3,0,14,9});        returns 14
	 * MathUtils.max(new long[]{-8,6,-3,0,-14,9});     returns 9
	 * MathUtils.max(new long[]{-8,-6,-3,-0,-14,-9});  returns 0
	 * </pre>
	 * 
	 * <b>Hint</b>
	 * 
	 * <pre>
	 * As long as no absolute value over 2.000.000 exists, {@link MathUtils#max(int...)} works as well.
	 * </pre>
	 * 
	 * 
	 * 
	 * @param numbers
	 *            List of <code>long</code> which maximum will be calculated.
	 * @return Returns the maximum value of the <code>List</code> numbers. If
	 *         <code>numbers</code> is empty or <code>null</code> then it
	 *         returns the maximum value of the data type <code>long</code>;
	 * 
	 * @see MathUtils#max(byte...)
	 * @see MathUtils#max(short...)
	 * @see MathUtils#max(float...)
	 * @see MathUtils#max(int...)
	 * @see MathUtils#max(double...)
	 */
	public static long max(long... numbers)
	{
		if(numbers == null || numbers.length == 0)
		{
			return Long.MAX_VALUE;
		}
		
		long max = numbers[0];
		for(int i = 1; i < numbers.length; i++)
		{
			if(numbers[i] > max)
			{
				max = numbers[i];
			}
		}
		
		return max;
	}
	
	
	/**
	 * Returns the smallest of all numbers. <blockquote>
	 * 
	 * </blockquote> <b> Examples </b>
	 * 
	 * <pre>
	 * MathUtils.min(new long[]{8,6,3,0,14,9});           returns 0
	 * MathUtils.min(new long[]{-8,6,-3,0,-14,9});        returns -14
	 * MathUtils.min(new long[]{-8,-6,-3,-0,-14,-9});     returns -14
	 * </pre>
	 * 
	 * <b>Hint</b>
	 * 
	 * 
	 * <pre>
	 * As long as no absolute value over 2.000.000 exists, {@link MathUtils#min(int...)} works as well.
	 * </pre>
	 * 
	 * 
	 * 
	 * @param numbers
	 *            List of <code>long</code> which minimum will be calculated.
	 * @return Returns the minimum value of the <code>List</code> numbers. If
	 *         <code>numbers</code> is empty or <code>null</code> then it
	 *         returns the minimum value of the data type <code>long</code>;
	 * 
	 * @see MathUtils#min(byte...)
	 * @see MathUtils#min(short...)
	 * @see MathUtils#min(float...)
	 * @see MathUtils#min(int...)
	 * @see MathUtils#min(double...)
	 */
	public static long min(long... numbers)
	{
		if(numbers == null || numbers.length == 0)
		{
			return Long.MIN_VALUE;
		}
		
		long min = numbers[0];
		for(int i = 1; i < numbers.length; i++)
		{
			if(numbers[i] < min)
			{
				min = numbers[i];
			}
		}
		
		return min;
	}
	
	
	/**
	 * Returns the biggest of all numbers.
	 * 
	 * <br>
	 * <b> Examples </b>
	 * 
	 * <pre>
	 * MathUtils.max(new float[]{5,8,10,26,3,8,19});        returns 26
	 * MathUtils.max(new float[]{5,-8,10,-26,3,-8,19});     returns 19
	 * MathUtils.max(new float[]{-5,-8,-10,-26,-3,-8,-19}); returns -3
	 * </pre>
	 * 
	 * @param numbers
	 *            List of <code>float</code> of which the maximum will be
	 *            calculated.
	 * @return Returns the maximum value of the <code>List</code> numbers. If
	 *         <code>numbers</code> is empty or <code>null</code> then it
	 *         returns the maximum value of the data type <code>float</code>;
	 * 
	 * @see MathUtils#max(byte...)
	 * @see MathUtils#max(int...)
	 * @see MathUtils#max(short...)
	 * @see MathUtils#max(long...)
	 * @see MathUtils#max(double...)
	 */
	public static float max(float... numbers)
	{
		if(numbers == null || numbers.length == 0)
		{
			return Float.MAX_VALUE;
		}
		
		float max = numbers[0];
		for(int i = 1; i < numbers.length; i++)
		{
			if(numbers[i] > max)
			{
				max = numbers[i];
			}
		}
		
		return max;
	}
	
	
	/**
	 * Returns the smallest of all numbers.
	 * 
	 * <br>
	 * <b> Examples </b>
	 * 
	 * <pre>
	 * MathUtils.min(new float[]{5,8,10,26,3,8,19});         returns 3
	 * MathUtils.min(new float[]{5,-8,10,-26,3,-8,19});      returns -26
	 * MathUtils.min(new float[]{-5,-8,-10,-26,-3,-8,-19});  returns -26
	 * </pre>
	 * 
	 * @param numbers
	 *            List of <code>float</code> of which the minimum will be
	 *            calculated.
	 * @return Returns the minimum value of the <code>List</code> numbers. If
	 *         <code>numbers</code> is empty or <code>null</code> then it
	 *         returns the minimum value of the data type <code>float</code>;
	 * 
	 * @see MathUtils#min(byte...)
	 * @see MathUtils#min(int...)
	 * @see MathUtils#min(short...)
	 * @see MathUtils#min(long...)
	 * @see MathUtils#min(double...)
	 */
	public static float min(float... numbers)
	{
		if(numbers == null || numbers.length == 0)
		{
			return Float.MIN_VALUE;
		}
		
		float min = numbers[0];
		for(int i = 1; i < numbers.length; i++)
		{
			if(numbers[i] < min)
			{
				min = numbers[i];
			}
		}
		
		return min;
	}
	
	
	/**
	 * Returns the biggest of all numbers. <br>
	 * <b> Examples </b>
	 * 
	 * <pre>
	 * MathUtils.max(new double[]{5.5,9.8,1.5,5.2,2.9});         returns 9.8
	 * MathUtils.max(new double[]{-5.5,9.4,1.5,-5.4,-2.5});      returns 9.4
	 * MathUtils.max(new double[]{-5.5,-9.7,-15.2,-5.3,-20.4});  returns -5.5
	 * 
	 * 
	 * </pre>
	 * 
	 * 
	 * @param numbers
	 *            List of <code>double</code> which maximum will be calculated.
	 * @return Returns the maximum value of the <code>List</code> numbers. If
	 *         <code>numbers</code> is empty or <code>null</code> then it
	 *         returns the maximum value of the data type <code>double</code>;
	 * 
	 * @see MathUtils#max(byte...)
	 * @see MathUtils#max(short...)
	 * @see MathUtils#max(float...)
	 * @see MathUtils#max(int...)
	 * @see MathUtils#max(long...)
	 */
	public static double max(double... numbers)
	{
		if(numbers == null || numbers.length == 0)
		{
			return Double.MAX_VALUE;
		}
		
		double max = numbers[0];
		for(int i = 1; i < numbers.length; i++)
		{
			if(numbers[i] > max)
			{
				max = numbers[i];
			}
		}
		
		return max;
	}
	
	
	/**
	 * Returns the smallest of all numbers.
	 * 
	 * <br>
	 * <b> Examples </b>
	 * 
	 * <pre>
	 * MathUtils.min(new double[]{5.5,9.8,1.5,5.2,2.9});         returns 1.5
	 * MathUtils.min(new double[]{-5.5,9.4,1.5,-5.4,-2.5});      returns -5.5
	 * MathUtils.min(new double[]{-5.5,-9.7,-15.2,-5.3,-20.4});  returns -20.4
	 * 
	 * 
	 * </pre>
	 * 
	 * 
	 * 
	 * @param numbers
	 *            List of <code>double</code> which minimum will be calculated.
	 * @return Returns the minimum value of the <code>List</code> numbers. If
	 *         <code>numbers</code> is empty or <code>null</code> then it
	 *         returns the minimum value of the data type <code>double</code>;
	 * 
	 * @see MathUtils#min(byte...)
	 * @see MathUtils#min(short...)
	 * @see MathUtils#min(float...)
	 * @see MathUtils#min(int...)
	 * @see MathUtils#min(long...)
	 */
	public static double min(double... numbers)
	{
		if(numbers == null || numbers.length == 0)
		{
			return Double.MIN_VALUE;
		}
		
		double min = numbers[0];
		for(int i = 1; i < numbers.length; i++)
		{
			if(numbers[i] < min)
			{
				min = numbers[i];
			}
		}
		
		return min;
	}
	
	
	/**
	 * Returns a <code>double</code> whose value is (<code>addend</code> +
	 * <code>augend</code>).
	 * 
	 * <br>
	 * <b> Examples: </b>
	 * 
	 * <pre>
	 * MathUtils.bcdAdd(12.40, 130.98);   returns 143.38
	 * MathUtils.bcdAdd(25.40, 120.40);   returns 145.80
	 * MathUtils.bcdAdd(-12.40, 130.98);  returns 118,57
	 * MathUtils.bcdAdd(-12.40, -130.98); returns -143.38
	 * </pre>
	 * 
	 * 
	 * @param addend
	 * 
	 * @param augend
	 *            value to be added to <code>addend</code>
	 * 
	 * @return a <code>double</code> whose value is (<code>addend</code> +
	 *         <code>augend</code>)
	 */
	public static double bcdAdd(double addend, double augend)
	{
		return new BigDecimal(addend).add(new BigDecimal(augend)).doubleValue();
	}
	
	
	/**
	 * Returns a <code>double</code> whose value is (<code>minuend</code> -
	 * <code>augend</code>).
	 * 
	 * <br>
	 * <b> Examples: </b>
	 * 
	 * <pre>
	 * MathUtils.bcdSubtract(12.40, 130.98);   returns -118.58
	 * MathUtils.bcdSubtract(-12.40, 130.98);  returns -143.38
	 * MathUtils.bcdSubtract(-12.40, -130.98); returns 118.58
	 * </pre>
	 * 
	 * 
	 * @param minuend
	 * 
	 * @param subtrahend
	 *            value to be subtracted from <code>minuend</code>
	 * 
	 * @return a <code>double</code> whose value is (<code>minuend</code> -
	 *         <code>augend</code>)
	 */
	public static double bcdSubtract(double minuend, double subtrahend)
	{
		return new BigDecimal(minuend).subtract(new BigDecimal(subtrahend)).doubleValue();
	}
	
	
	/**
	 * Returns a <code>double</code> whose value is (<code>multiplier</code> x
	 * <code>multiplicand</code>).
	 * 
	 * <br>
	 * <b> Examples </b>
	 * 
	 * <pre>
	 * MathUtils.bcdMultiply(-12.40, 130.98);  returns -1624.1519999999998
	 * MathUtils.bcdMultiply(-12.40, -130.98); returns 1624.1519999999998
	 * MathUtils.bcdMultiply(-12.40, 130.98);  returns -1624.1519999999998
	 * </pre>
	 * 
	 * 
	 * @param multiplier
	 * 
	 * @param multiplicand
	 *            value to be multiplied by <code>multiplier</code>
	 * 
	 * @return a <code>double</code> whose value is (<code>multiplier</code> x
	 *         <code>multiplicand</code>)
	 */
	
	public static double bcdMultiply(double multiplier, double multiplicand)
	{
		return new BigDecimal(multiplier).multiply(new BigDecimal(multiplicand)).doubleValue();
	}
	
	
	/**
	 * Calls {@link #bcdDivide(double, double, int)} with
	 * {@link BigDecimal#ROUND_HALF_EVEN}. <br>
	 * <b> Examples </b>
	 * 
	 * <pre>
	 * MathUtils.bcdDivide(150.56, 78.25, 1);     returns 1.9240894568690097
	 * MathUtils.bcdDivide(-120.56, 45.20, 0);    returns -2.6672566371681414
	 * MathUtils.bcdDivide(-2589.23, -235.56, 1); returns 10.99180675836305
	 * </pre>
	 * 
	 * 
	 */
	public static double bcdDivide(double dividend, double divisor)
	{
		return bcdDivide(dividend,divisor,BigDecimal.ROUND_HALF_EVEN);
	}
	
	
	/**
	 * @see BigDecimal#divide(BigDecimal, RoundingMode)
	 */
	public static double bcdDivide(double dividend, double divisor, int roundingMode)
	{
		return new BigDecimal(dividend).divide(new BigDecimal(divisor),roundingMode).doubleValue();
	}
	
	
	/**
	 * * Checks if the variable <code>val</code> is a value between
	 * <code>min</code> and <code>max</code>. With the condition:
	 * <code>min</code> <= <code>val</code> <= <code>max</code>.
	 * 
	 * 
	 * @param val
	 *            the value which will be checked
	 * @param min
	 *            the(inclusive)lower border for <code>value</code>
	 * @param max
	 *            the(inclusive)upper border for <code>value</code>
	 * 
	 * @throws IndexOutOfBoundsException
	 *             if <code>val</code> not between <code>min</code> and
	 *             <code>max</code>
	 * 
	 * @see MathUtils#isInRange(int, int, int)
	 * 
	 */
	public static void checkRange(int val, int min, int max) throws IndexOutOfBoundsException
	{
		if(!isInRange(val,min,max))
		{
			throw new IndexOutOfBoundsException(val + " != (" + min + "-" + max + ")");
		}
	}
	
	
	/**
	 * * Checks if the variable <code>val</code> is a value between
	 * <code>min</code> and <code>max</code>. With the condition:
	 * <code>min</code> <= <code>val</code> <= <code>max</code>.
	 * 
	 * <br>
	 * <b> Examples </b>
	 * 
	 * <pre>
	 * MathUtils.isInRange(42, 10, 50);        	returns true
	 * MathUtils.isInRange(80, 10, 50);			returns false
	 * MathUtils.isInRange(80, 10, -80);		returns false
	 * </pre>
	 * 
	 * @param val
	 *            the value which will be checked
	 * @param min
	 *            the(inclusive)lower border for <code>value</code>
	 * @param max
	 *            the(inclusive)upper border for <code>value</code>
	 * @return true if <code>min</code> <= <code>value</code> <=
	 *         <code>max</code>
	 * 
	 * @see MathUtils#checkRange(int, int, int)
	 */
	public static boolean isInRange(int val, int min, int max)
	{
		return val >= min && val <= max;
	}
	
	
	/**
	 * @see Arrays#hashCode(Object[])
	 * 
	 * <br>
	 *      <b> Examples </b>
	 * 
	 *      <pre>
	 * MathUtils.computeHash(5987);				returns 6018
	 * MathUtils.computeHash("XDEV");			returns 2689212
	 * MathUtils.computeHash(-545689);			returns -545658
	 * </pre>
	 * 
	 * @param objects
	 *            the array whose content-based hash code to compute
	 * @return content-based hash code for <code>objects</code>
	 */
	
	public static int computeHash(Object... objects)
	{
		return Arrays.hashCode(objects);
	}
	
	
	/**
	 * 
	 * 
	 * <b> Examples. </b>
	 * 
	 * <pre>
	 * MathUtils.computeHashDeep(87956);		returns 87987
	 * MathUtils.computeHashDeep(-5646);        returns -5615
	 * MathUtils.computeHashDeep(54);			returns 85
	 * </pre>
	 * 
	 * @param objects
	 *            the array whose deep-content-based hash code to compute
	 * @return deep-content-based hash code for <code>objects</code>
	 * 
	 * @see Arrays#deepHashCode(Object[])
	 */
	
	public static int computeHashDeep(Object... objects)
	{
		return Arrays.deepHashCode(objects);
	}
}
