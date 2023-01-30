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
package com.xdev.jadoth.math;

import java.awt.Point;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Random;


/**
 * @author Thomas Muenz
 *
 */
public abstract class JaMath
{
	private static final Random random = new Random();
	
	///////////////////////////////////////////////////////////////////////////
	// Math Utils       //
	/////////////////////	
	
	/**
	 * This method is an int version of <code>Math.pow(double, double)</code>, 
	 * using only integer iteration for calculation.
	 * <p>
	 * As a rule of thumb:<br>
	 * It is faster for <code>exponent</code> < 250 (significantly faster for exponents < 100)
	 * and slower for <code>exponent</code> >= 250 (significantly slower for exponents >= 500).<br>
	 * This may depend on the concrete system running the program, of course.
	 * <br>
	 * Note that <code>exponent</code> may not be negative, otherwise an <code>IllegalArgumentException</code> is 
	 * thrown.
	 * 
	 * (Why is there no Math.pow(int, int) in JDK ?)
	 * 
	 * @param base
	 * @param exponent my not be negative
	 * @return <code>base^exponent</code>
	 * @throws IllegalArgumentException if <code>exponent</code> is negative
	 */
	public static final int pow(final int base, int exponent) throws IllegalArgumentException
	{			
		if(exponent < 0) throw new IllegalArgumentException("exponent may not be negative: "+exponent);	
		if(exponent == 0) return 1; //return 1, even if base is 0!
		if(base == 0 || base == 1) return base;
		
		int result = 1;
		while(exponent --> 0){
			result *= base;
		}
		
		return result;
	}
	
	
	public static final int pow2Bound(final int n)
	{
		int i = 1;
		while(i < n){
			i <<= 1;
		}
		return i;
	}
	public static final int log2Bound(final int n)
	{
		int i = 1;
		int c = 0;
		while(i < n){
			i <<= 1;
			c++;
		}
		return c;
	}
	
	public static final int log2pow2(final int pow2Value)
	{
		switch(pow2Value) {
			case          0: return  0;
			case          2: return  1;
			case          4: return  2;
			case          8: return  3;
			case         16: return  4;
			case         32: return  5;
			case         64: return  6;
			case        128: return  7;
			case        256: return  8;
			case        512: return  9;
			case       1024: return 10;
			case       2048: return 11;
			case       4096: return 12;
			case       8192: return 13;
			case      16384: return 14;
			case      32768: return 15;
			case      65536: return 16;
			case     131072: return 17;
			case     262144: return 18;
			case     524288: return 19;
			case    1048576: return 20;
			case    2097152: return 21;
			case    4194304: return 22;
			case    8388608: return 23;
			case   16777216: return 24;
			case   33554432: return 25;
			case   67108864: return 26;
			case  134217728: return 27;
			case  268435456: return 28;
			case  536870912: return 29;
			case 1073741824: return 30;
			default:
				throw new IllegalArgumentException("Not a power-of-2 value: "+pow2Value);
		}
	}
	
	/**
	 * Determines if the passed value is a power-of-2 value.
	 * @param value
	 * @return <tt>true</tt> for any n in [1;30] that fulfills <code>value = 2^n</code>
	 */
	public static final boolean isPow2(final int value)
	{
		switch(value){
			case          2: return true;
			case          4: return true;
			case          8: return true;
			case         16: return true;
			case         32: return true;
			case         64: return true;
			case        128: return true;
			case        256: return true;
			case        512: return true;
			case       1024: return true;
			case       2048: return true;
			case       4096: return true;
			case       8192: return true;
			case      16384: return true;
			case      32768: return true;
			case      65536: return true;
			case     131072: return true;
			case     262144: return true;
			case     524288: return true;
			case    1048576: return true;
			case    2097152: return true;
			case    4194304: return true;
			case    8388608: return true;
			case   16777216: return true;
			case   33554432: return true;
			case   67108864: return true;
			case  134217728: return true;
			case  268435456: return true;
			case  536870912: return true;
			case 1073741824: return true;
			default: return false;
		}
	}
	
	
	public static final float pow(final float base, int exponent) throws IllegalArgumentException
	{			
		if(exponent < 0) throw new IllegalArgumentException("exponent may not be negative: "+exponent);	
		if(exponent == 0) return 1; //return 1, even if base is 0!
		if(base == 0 || base == 1) return base;
		
		float result = 1;
		while(exponent --> 0){
			result *= base;
		}
		
		return result;
	}
	
	public static final double pow(final double base, int exponent) throws IllegalArgumentException
	{			
		if(exponent < 0) throw new IllegalArgumentException("exponent may not be negative: "+exponent);	
		if(exponent == 0) return 1; //return 1, even if base is 0!
		if(base == 0 || base == 1) return base;
		
		double result = 1;
		while(exponent --> 0){
			result *= base;
		}
		
		return result;
	}
	
	public static final float square(final float f)
	{
		return f*f;
	}
	public static final long square(final long l)
	{
		return l*l;
	}
	public static final int square(final int i)
	{
		return i*i;
	}
	public static final double square(final double d)
	{
		return d*d;
	}
	
	public static final float cube(final float f)
	{
		return f*f*f;
	}
	public static final long cube(final long l)
	{
		return l*l*l;
	}
	public static final int cube(final int i)
	{
		return i*i*i;
	}
	public static final double cube(final double d)
	{
		return d*d*d;
	}

	/**
	 * Normalizes <code>value</code> to the actual closest value for <code>decimals</code> decimals.<br>
	 * This is useful if multiple subsequent calulations with double values accumulate rounding errors that drift the
	 * value away from the value it actually should (could) be.<br>
	 * See the "candy" example in Joshua Bloch's "Effective Java": this method fixes the problem.
	 * <p>
	 * Note that <code>decimals</code> may not be negative.<br>
	 * And note that while a value of 0 for <code>decimals</code> will yield the correct result, it makes not much
	 * sense to call this method for it in the first place.
	 * 
	 * @param value any double value
	 * @param decimals the number of decimals. May not be negative.
	 * @return the normalized value for <code>value</code>
	 */
	public static final double round(final double value, int decimals)
	{
		switch (decimals){
			//common cases are hardcoded for performance reasons, inlined rounding code (Math.round(value*d)/d)
			case 0: return ((long)StrictMath.floor(value + 0.5d));
			case 1: return ((long)StrictMath.floor(value*10.0d + 0.5d))/10.0d;
			case 2: return ((long)StrictMath.floor(value*100.0d + 0.5d))/100.0d;
			case 3: return ((long)StrictMath.floor(value*1000.0d + 0.5d))/1000.0d;
			case 4: return ((long)StrictMath.floor(value*10000.0d + 0.5d))/10000.0d;
			case 5: return ((long)StrictMath.floor(value*100000.0d + 0.5d))/100000.0d;
			case 6: return ((long)StrictMath.floor(value*1000000.0d + 0.5d))/1000000.0d;
			default:{
				//generic algorithm, including range checks
				if(decimals < 0) {
					throw new IllegalArgumentException("No negative values allowed for decimals: "+decimals);
				}
				//No idea if 308 is right, tbh. At least it's a check for values like million etc.
				if(decimals > 308) {
					throw new IllegalArgumentException("Exponent out of range: "+decimals);
				}
				
				//inlined pow(double,int) without checks
				double factor = 1.0d;
				while(decimals --> 0){
					factor *= 10.0d;
				}				
				return ((long)StrictMath.floor(value*factor + 0.5d))/factor;
			}
			
		}
	}
	
	
	/**
	 * Range.
	 *
	 * @param from the from
	 * @param to the to
	 * @return the range
	 */
	public static _intRange range(final int from, final int to){
		return new _intRange(from, to);
	}

	public static byte[] sequence(byte from, final byte to)
	{		
		byte[] range;
		if(from < to){
			range = new byte[to-from + 1];		
			for(int i = 0; i < range.length; i++) {
				range[i] = from++;
			}
		}
		else {
			range = new byte[from-to + 1];		
			for(int i = 0; i < range.length; i++) {
				range[i] = from--;
			}
		}		
		return range;
	}
	public static short[] sequence(short from, final short to)
	{		
		short[] range;		
		if(from < to){
			range = new short[to-from + 1];		
			for(int i = 0; i < range.length; i++) {
				range[i] = from++;
			}
		}
		else {
			range = new short[from-to + 1];		
			for(int i = 0; i < range.length; i++) {
				range[i] = from--;
			}
		}		
		return range;
	}
	/**
	 * Sequence.
	 *
	 * @param from the from
	 * @param to the to
	 * @return the int[]
	 */
	public static int[] sequence(int from, final int to)
	{		
		int[] range;	
		if(from < to){
			range = new int[to-from + 1];
			for(int i = 0; i < range.length; i++) {
				range[i] = from++;
			}
		}
		else {
			range = new int[from-to + 1];
			for(int i = 0; i < range.length; i++) {
				range[i] = from--;
			}
		}		
		return range;
	}
	/**
	 * 
	 * @param from
	 * @param to
	 * @return
	 * @throws IllegalArgumentException if the range [from;to] is greater than Integer.MAX_VALUE
	 */
	public static long[] sequence(long from, final long to) throws IllegalArgumentException
	{		
		long[] range;	
		if(from < to){
			final long elementCount = to-from+1;
			if(elementCount > Integer.MAX_VALUE){
				throw new IllegalArgumentException(
					"Range ["+from+";"+to+"] exceeds array range limit: "+elementCount+" > "+Integer.MAX_VALUE
				);
			}
			range = new long[(int)elementCount];		
			for(int i = 0; i < range.length; i++) {
				range[i] = from++;
			}
		}
		else {
			final long elementCount = from-to+1;
			if(elementCount > Integer.MAX_VALUE){
				throw new IllegalArgumentException(
					"Range ["+from+";"+to+"] exceeds array range limit: "+elementCount+" > "+Integer.MAX_VALUE
				);
			}
			range = new long[(int)elementCount];		
			for(int i = 0; i < range.length; i++) {
				range[i] = from--;
			}
		}		
		return range;
	}
	
	
	public static final double max(final double... values)
	{
		if(values == null) throw new IllegalArgumentException("values may not be null");
		if(values.length == 0) throw new IllegalArgumentException("values may not be empty");
		
		double currentMax = -Double.MIN_VALUE;
		for(final double d : values) {
			if(d > currentMax) currentMax = d;
		}
		return currentMax;
	}
	public static final float max(final float... values)
	{
		if(values == null) throw new IllegalArgumentException("values may not be null");
		if(values.length == 0) throw new IllegalArgumentException("values may not be empty");
		
		float currentMax = -Float.MIN_VALUE;
		for(final float f : values) {
			if(f > currentMax) currentMax = f;
		}
		return currentMax;
	}
	public static final int max(final int... values)
	{
		if(values == null) throw new IllegalArgumentException("values may not be null");
		if(values.length == 0) throw new IllegalArgumentException("values may not be empty");
		
		int currentMax = Integer.MIN_VALUE;
		for(final int i : values) {
			if(i > currentMax) currentMax = i;
		}
		return currentMax;
	}
	public static final long max(final long... values)
	{
		if(values == null) throw new IllegalArgumentException("values may not be null");
		if(values.length == 0) throw new IllegalArgumentException("values may not be empty");
		
		long currentMax = Long.MIN_VALUE;
		for(final long l : values) {
			if(l > currentMax) currentMax = l;
		}
		return currentMax;
	}
	
	public static final double min(final double... values)
	{
		if(values == null) throw new IllegalArgumentException("values may not be null");
		if(values.length == 0) throw new IllegalArgumentException("values may not be empty");
		
		double currentMin = -Double.MAX_VALUE;
		for(final double d : values) {
			if(d < currentMin) currentMin = d;
		}
		return currentMin;
	}
	public static final float min(final float... values)
	{
		if(values == null) throw new IllegalArgumentException("values may not be null");
		if(values.length == 0) throw new IllegalArgumentException("values may not be empty");
		
		float currentMin = -Float.MAX_VALUE;
		for(final float f : values) {
			if(f < currentMin) currentMin = f;
		}
		return currentMin;
	}
	public static final int min(final int... values)
	{
		if(values == null) throw new IllegalArgumentException("values may not be null");
		if(values.length == 0) throw new IllegalArgumentException("values may not be empty");
		
		int currentMin = Integer.MAX_VALUE;
		for(final int i : values) {
			if(i < currentMin) currentMin = i;
		}
		return currentMin;
	}
	public static final long min(final long... values)
	{
		if(values == null) throw new IllegalArgumentException("values may not be null");
		if(values.length == 0) throw new IllegalArgumentException("values may not be empty");
		
		long currentMin = Long.MAX_VALUE;
		for(final long l : values) {
			if(l < currentMin) currentMin = l;
		}
		return currentMin;
	}
	
	public static final double sum(final double... values)
	{
		if(values == null) throw new IllegalArgumentException("values may not be null");
		if(values.length == 0) throw new IllegalArgumentException("values may not be empty");
		
		double sum = 0d;
		for(final double d : values) {
			sum += d;
		}
		return sum;
	}
	public static final float sum(final float... values)
	{
		if(values == null) throw new IllegalArgumentException("values may not be null");
		if(values.length == 0) throw new IllegalArgumentException("values may not be empty");
		
		float sum = 0f;
		for(final float f : values) {
			sum += f;
		}
		return sum;
	}
	public static final int sum(final int... values)
	{
		if(values == null) throw new IllegalArgumentException("values may not be null");
		if(values.length == 0) throw new IllegalArgumentException("values may not be empty");
		
		int sum = 0;
		for(final int i : values) {
			sum += i;
		}
		return sum;
	}
	public static final long sum(final long... values)
	{
		if(values == null) throw new IllegalArgumentException("values may not be null");
		if(values.length == 0) throw new IllegalArgumentException("values may not be empty");
		
		long sum = 0;
		for(final long l : values) {
			sum += l;
		}
		return sum;
	}
		
	public static final double avg(final double... values)
	{
		if(values == null) throw new IllegalArgumentException("values may not be null");
		if(values.length == 0) throw new IllegalArgumentException("values may not be empty");
		
		double sum = 0d;
		for(final double d : values) {
			sum += d;
		}
		return sum/values.length;
	}
	public static final float avg(final float... values)
	{
		if(values == null) throw new IllegalArgumentException("values may not be null");
		if(values.length == 0) throw new IllegalArgumentException("values may not be empty");
		
		float sum = 0f;
		for(final float f : values) {
			sum += f;
		}
		return sum/values.length;
	}
	public static final int avg(final int... values)
	{
		if(values == null) throw new IllegalArgumentException("values may not be null");
		if(values.length == 0) throw new IllegalArgumentException("values may not be empty");
		
		int sum = 0;
		for(final int i : values) {
			sum += i;
		}
		return sum/values.length;
	}
	public static final long avg(final long... values)
	{
		if(values == null) throw new IllegalArgumentException("values may not be null");
		if(values.length == 0) throw new IllegalArgumentException("values may not be empty");
		
		long sum = 0;
		for(final long l : values) {
			sum += l;
		}
		return sum/values.length;
	}
	
	
	
	///////////////////////////////////////////////////////////////////////////
	// Bresenham        //
	/////////////////////
	
	
	/**
	 * Determines the amount of discrete steps from (x1,y1) to (x2,y2), where one step is a change of coordinates
	 * in either straight or diagonal direction.<p>
	 * Examples:<br>
	 * (0,0) to (2,0) = 2 steps<br>
	 * (0,0) to (2,2) = 2 steps<br>
	 * (5,18) to (10,9) = 9 steps<br>
	 * 
	 * @param x1
	 * @param y1
	 * @param x2
	 * @param y2
	 * @return
	 */
	public static final int stepCountDistance(final int x1, final int y1, final int x2, final int y2)
	{
		return Math.max(Math.abs(x1-x2), Math.abs(y1-y2));
	}
	
	public static final Point[] linePoints(final int x1, final int y1, final int x2, final int y2)
	{
		int x = x1, y = y1, d = 0, hx = x2-x1, hy = y2-y1, c, m, xInc = 1, yInc = 1;
		final Point[] points = new Point[Math.max(Math.abs(x1-x2), Math.abs(y1-y2))+1];
		int idx = 0;
		
		if(hx < 0) {
			xInc = -1;
			hx = -hx;
		}
		if(hy < 0) {
			yInc = -1;
			hy = -hy;
		}
		if(hy <= hx) {
			c = 2 * hx;
			m = 2 * hy;
			while(true){
				points[idx++] = new Point(x, y);
				if(x == x2) break;
				x += xInc;
				d += m;
				if(d > hx) {
					y += yInc;
					d -= c;
				}
			}
		}
		else {
			c = 2 * hy;
			m = 2 * hx;
			while(true){
				points[idx++] = new Point(x, y);
				if(y == y2) break;
				y += yInc;
				d += m;
				if(d > hy) {
					x += xInc;
					d -= c;
				}
			}
		}
		return points;
	}
	
	public static final int[] linePointsInt1D(final int x1, final int y1, final int x2, final int y2)
	{
		int x = x1, y = y1, d = 0, hx = x2-x1, hy = y2-y1, c, m, xInc = 1, yInc = 1;
		final int[] points = new int[(Math.max(Math.abs(x1-x2), Math.abs(y1-y2))+1)*2];
		int idx = 0;
		
		if(hx < 0) {
			xInc = -1;
			hx = -hx;
		}
		if(hy < 0) {
			yInc = -1;
			hy = -hy;
		}
		if(hy <= hx) {
			c = 2 * hx;
			m = 2 * hy;
			while(true){
				points[idx++] = x;
				points[idx++] = y;
				if(x == x2) break;
				x += xInc;
				d += m;
				if(d > hx) {
					y += yInc;
					d -= c;
				}
			}
		}
		else {
			c = 2 * hy;
			m = 2 * hx;
			while(true){
				points[idx++] = x;
				points[idx++] = y;
				if(y == y2) break;
				y += yInc;
				d += m;
				if(d > hy) {
					x += xInc;
					d -= c;
				}
			}
		}
		return points;
	}
	
	public static final int[][] linePointsInt2D(final int x1, final int y1, final int x2, final int y2)
	{
		int x = x1, y = y1, d = 0, hx = x2-x1, hy = y2-y1, c, m, xInc = 1, yInc = 1;
		final int[][] points = new int[Math.max(Math.abs(x1-x2), Math.abs(y1-y2))+1][];
		int idx = 0;
		
		if(hx < 0) {
			xInc = -1;
			hx = -hx;
		}
		if(hy < 0) {
			yInc = -1;
			hy = -hy;
		}
		if(hy <= hx) {
			c = 2 * hx;
			m = 2 * hy;
			while(true){
				points[idx++] = new int[]{x, y};
				if(x == x2) break;
				x += xInc;
				d += m;
				if(d > hx) {
					y += yInc;
					d -= c;
				}
			}
		}
		else {
			c = 2 * hy;
			m = 2 * hx;
			while(true){
				points[idx++] = new int[]{x, y};
				if(y == y2) break;
				y += yInc;
				d += m;
				if(d > hy) {
					x += xInc;
					d -= c;
				}
			}
		}
		return points;
	}
		
	public static final void line(
		final int x1, final int y1, final int x2, final int y2, final IntCoordinateManipulator manipulator
	)
		throws InvalidCoordinateException
	{
		int x = x1, y = y1, d = 0, hx = x2-x1, hy = y2-y1, c, m, xInc = 1, yInc = 1;
		
		if(hx < 0) {
			xInc = -1;
			hx = -hx;
		}
		if(hy < 0) {
			yInc = -1;
			hy = -hy;
		}
		if(hy <= hx) {
			c = 2 * hx;
			m = 2 * hy;
			while(true){
				manipulator.manipulateCoordinate(x, y);
				if(x == x2) break;
				x += xInc;
				d += m;
				if(d > hx) {
					y += yInc;
					d -= c;
				}
			}
		}
		else {
			c = 2 * hy;
			m = 2 * hx;
			while(true){
				manipulator.manipulateCoordinate(x, y);
				if(y == y2) break;
				y += yInc;
				d += m;
				if(d > hy) {
					x += xInc;
					d -= c;
				}
			}
		}
	}
	
	
	/**
	 * Use <code>factorial(long)</code> for n in [0;20].<br>
	 * Use <code>factorial(BigInteger)</code> for any n > 0.
	 * @param n natural number in [0;12]
	 * @return n!
	 * @throws IllegalArgumentException for n < 0 or n > 12
	 */
	public static final int factorial(final int n) throws IllegalArgumentException
	{
		//honestly: calculate the loop everytime for a value set of only 13 elements?
		switch(n){
			case 12: return 479001600;
			case 11: return 39916800;
			case 10: return 3628800;
			case  9: return 362880;
			case  8: return 40320;
			case  7: return 5040;
			case  6: return 720;
			case  5: return 120;
			case  4: return 24;
			case  3: return 6;
			case  2: return 2;
			case  1: return 1;
			case  0: return 1;
			default: throw new IllegalArgumentException("n not in [0;12]: "+n);
		}
		//calculation
//		if(n <  0) throw new IllegalArgumentException("n may not be negative: "+n);
//		if(n > 12) throw new IllegalArgumentException("n may not be greater 12: "+n);
//		int result = 1;
//		while(n > 1) result *= n--;		
//		return result;
	}
	/**
	 * Use <code>factorial(BigInteger)</code> for any n > 0.
	 * 
	 * @param n natural number in [0;20]
	 * @return n!
	 * @throws IllegalArgumentException for n < 0 or n > 20
	 */
	public static final long factorial(final long n) throws IllegalArgumentException
	{
		//honestly: calculate the loop everytime for a value set of only 21 elements?
		switch((int)n){
			case 20: return 2432902008176640000L;
			case 19: return 121645100408832000L;
			case 18: return 6402373705728000L;
			case 17: return 355687428096000L;
			case 16: return 20922789888000L;
			case 15: return 1307674368000L;
			case 14: return 87178291200L;
			case 13: return 6227020800L;
			case 12: return 479001600L;
			case 11: return 39916800L;
			case 10: return 3628800L;
			case  9: return 362880L;
			case  8: return 40320L;
			case  7: return 5040L;
			case  6: return 720L;
			case  5: return 120L;
			case  4: return 24L;
			case  3: return 6L;
			case  2: return 2L;
			case  1: return 1L;
			case  0: return 1L;
			default: throw new IllegalArgumentException("n not in [0;20]: "+n);
		}		
		//calculation
//		if(n <  0) throw new IllegalArgumentException("n may not be negative: "+n);
//		if(n > 20) throw new IllegalArgumentException("n may not be greater 20: "+n);
//		int i = (int)n;
//		long result = 1;
//		while(i > 1) result *= i--;		
//		return result;
	}
	
	/**
	 * 
	 * @param n any natural number >= 0
	 * @return n!
	 * @throws IllegalArgumentException for n < 0
	 */
	public static final BigInteger factorial(BigInteger n) throws IllegalArgumentException
	{
		//recursive algorithms are nonsense here as the doy method call and range checking overhead in every step!
		final long nValue = n.longValue();
		if(nValue < 0) throw new IllegalArgumentException("n may not be negative: "+nValue);
		if(nValue <= 20) return BigInteger.valueOf(factorial(nValue));
		
		BigInteger result = BigInteger.valueOf(factorial(20L));
		while(n.longValue() > 20){
			result = result.multiply(n);		
			n = n.subtract(BigInteger.ONE);
		}
		return result;
	}
	
	/**
	 * Alias for <code>BigInteger.valueOf(value)</code>
	 * @param value any value
	 * @return a <code>BigInteger</code> representing <code>value</code>
	 */
	public static final BigInteger bigInt(final int value)
	{
		return BigInteger.valueOf(value);
	}
	/**
	 * Alias for <code>BigInteger.valueOf(value)</code>
	 * @param value any value
	 * @return a <code>BigInteger</code> representing <code>value</code>
	 */
	public static final BigInteger bigInt(final long value)
	{
		return BigInteger.valueOf(value);
	}
	
	
	/**
	 * Alias for <code>BigDecimal.valueOf(value)</code>
	 * @param value any value
	 * @return a <code>BigDecimal</code> representing <code>value</code>
	 */
	public static final BigDecimal bigDec(final long value)
	{
		return BigDecimal.valueOf(value);
	}
	/**
	 * Alias for <code>BigDecimal.valueOf(value)</code>
	 * @param value any value
	 * @return a <code>BigDecimal</code> representing <code>value</code>
	 */
	public static final BigDecimal bigDec(final double value)
	{
		return BigDecimal.valueOf(value);
	}
	
	
	
	
	///////////////////////////////////////////////////////////////////////////
	// constructors     //
	/////////////////////
	
	private JaMath(){}


	/**
	 * @return the random
	 */
	public static final Random random()
	{
		return random;
	}
	
	public static final int random(final int n)
	{
		return random.nextInt(n);
	}

	
}
