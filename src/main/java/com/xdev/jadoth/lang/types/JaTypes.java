
package com.xdev.jadoth.lang.types;

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
import java.math.BigInteger;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import com.xdev.jadoth.lang.exceptions.NumberRangeException;



/**
 * The Class TypeUtils.
 *
 * @author Thomas Muenz
 */
public abstract class JaTypes
{
	///////////////////////////////////////////////////////////////////////////
	// Class Type Classifiers //
	///////////////////////////
	/**
	 * Checks if is boolean.
	 *
	 * @param c the c
	 * @return true, if is boolean
	 */
	public static boolean isBoolean(final Class<?> c) {
		return c == boolean.class || c == Boolean.class;
	}

	/**
	 * Checks if is byte.
	 *
	 * @param c the c
	 * @return true, if is byte
	 */
	public static boolean isByte(final Class<?> c) {
		return c == byte.class || c == Byte.class;
	}

	/**
	 * Checks if is short.
	 *
	 * @param c the c
	 * @return true, if is short
	 */
	public static boolean isShort(final Class<?> c) {
		return c == short.class || c == Short.class;
	}

	/**
	 * Checks if is integer.
	 *
	 * @param c the c
	 * @return true, if is integer
	 */
	public static boolean isInteger(final Class<?> c) {
		return c == int.class || c == Integer.class;
	}

	/**
	 * Checks if is long.
	 *
	 * @param c the c
	 * @return true, if is long
	 */
	public static boolean isLong(final Class<?> c) {
		return c == long.class || c == Long.class;
	}

	/**
	 * Checks if is float.
	 *
	 * @param c the c
	 * @return true, if is float
	 */
	public static boolean isFloat(final Class<?> c) {
		return c == float.class || c == Float.class;
	}

	/**
	 * Checks if is double.
	 *
	 * @param c the c
	 * @return true, if is double
	 */
	public static boolean isDouble(final Class<?> c) {
		return c == double.class || c == Double.class;
	}

	/**
	 * Checks if is character.
	 *
	 * @param c the c
	 * @return true, if is character
	 */
	public static boolean isCharacter(final Class<?> c) {
		return c == char.class || c == Character.class;
	}
	//just for conformity in use along with the other ones
	/**
	 * Checks if is string.
	 *
	 * @param c the c
	 * @return true, if is string
	 */
	public static boolean isString(final Class<?> c) {
		return c == String.class;
	}



	///////////////////////////////////////////////////////////////////////////
	// Class Category Classifiers //
	///////////////////////////////
	/**
	 * Checks if is natural number.
	 *
	 * @param c the c
	 * @return true, if is natural number
	 */
	public static boolean isNaturalNumber(final Class<?> c) {
		return c == byte.class 	|| c == Byte.class
			|| c == short.class || c == Short.class
			|| c == int.class 	|| c == Integer.class
			|| c == long.class 	|| c == Long.class
			|| c == BigInteger.class
			|| c == AtomicInteger.class
			|| c == AtomicLong.class
		;
	}

	/**
	 * Checks if is decimal.
	 *
	 * @param c the c
	 * @return true, if is decimal
	 */
	public static boolean isDecimal(final Class<?> c) {
		return c == float.class || c == Float.class
			|| c == double.class || c == Double.class
			|| c == BigDecimal.class
		;
	}

	/**
	 * Checks if is number.
	 *
	 * @param c the c
	 * @return true, if is number
	 */
	public static boolean isNumber(final Class<?> c) {
		return c == byte.class 	|| c == Byte.class
			|| c == short.class || c == Short.class
			|| c == int.class 	|| c == Integer.class
			|| c == long.class 	|| c == Long.class
			|| c == float.class || c == Float.class
			|| c == double.class || c == Double.class
			|| c == BigInteger.class
			|| c == AtomicInteger.class
			|| c == AtomicLong.class
			|| c == BigDecimal.class
		;
	}

	/**
	 * Checks if is literal.
	 *
	 * @param c the c
	 * @return true, if is literal
	 */
	public static boolean isLiteral(final Class<?> c) {
		return c == String.class || c == char.class || c == Character.class;
	}



	///////////////////////////////////////////////////////////////////////////
	// Object Category Classifiers //
	////////////////////////////////
	/**
	 * Checks if is natural number.
	 *
	 * @param o the o
	 * @return true, if is natural number
	 */
	public static boolean isNaturalNumber(final Object o) {
		return o instanceof Byte
			|| o instanceof Short
			|| o instanceof Integer
			|| o instanceof Long
			|| o instanceof BigInteger
			|| o instanceof AtomicInteger
			|| o instanceof AtomicLong
		;
	}
	//just for conformity in use along with the other ones
	/**
	 * Checks if is number.
	 *
	 * @param o the o
	 * @return true, if is number
	 */
	public static boolean isNumber(final Object o) {
		return o instanceof Number;
	}

	/**
	 * Checks if is decimal.
	 *
	 * @param o the o
	 * @return true, if is decimal
	 */
	public static boolean isDecimal(final Object o) {
		return o instanceof Float || o instanceof Double || o instanceof BigDecimal;
	}

	/**
	 * Checks if is literal.
	 *
	 * @param o the o
	 * @return true, if is literal
	 */
	public static boolean isLiteral(final Object o) {
		return o instanceof String || o instanceof Character;
	}
	//just for conformity in use along with the other ones
	/**
	 * Checks if is boolean.
	 *
	 * @param o the o
	 * @return true, if is boolean
	 */
	public static boolean isBoolean(final Object o) {
		return o instanceof Boolean;
	}




	public static final int parseIntFrom1Bytes(final byte[] b, final int offset)
	{
		return (b[offset]&0xff);
	}
	public static final int parseIntFrom2Bytes(final byte[] b, int offset)
	{
		return (b[offset++]&0xff)<<8 | (b[offset]&0xff);
	}
	public static final int parseIntFrom3Bytes(final byte[] b, int offset)
	{
		return (b[offset++]&0xff)<<16 | (b[offset++]&0xff)<<8 | (b[offset]&0xff);
	}
	public static final int parseIntFrom4Bytes(final byte[] b, int offset)
	{
		return b[offset++]<<24 | (b[offset++]&0xff)<<16 | (b[offset++]&0xff)<<8 | (b[offset]&0xff);
	}

	public static final int parseIntFromBytes(final byte[] b, int offset, final int byteCount)
	{
		switch(byteCount){
			case 1:
				return (b[offset]&0xff);
			case 2:
				return (b[offset++]&0xff)<<8 | (b[offset]&0xff);
			case 3:
				return (b[offset++]&0xff)<<16 | (b[offset++]&0xff)<<8 | (b[offset]&0xff);
			default:
				return b[offset++]<<24 | (b[offset++]&0xff)<<16 | (b[offset++]&0xff)<<8 | (b[offset]&0xff);
		}
	}

	public static final int parseIntFrom1Bytes(final byte[] b)
	{
		return b[0]&0xff;
	}
	public static final int parseIntFrom2Bytes(final byte[] b)
	{
		return (b[0]&0xff)<<8 | (b[1]&0xff);
	}
	public static final int parseIntFrom3Bytes(final byte[] b)
	{
		return (b[0]&0xff)<<16 | (b[1]&0xff)<<8 | (b[2]&0xff);
	}
	public static final int parseIntFrom4Bytes(final byte[] b)
	{
		return b[0]<<24 | (b[1]&0xff)<<16 | (b[2]&0xff)<<8 | (b[3]&0xff);
	}

	public static final byte[] convertIntTo1Bytes(final int i)
	{
		return new byte[]{(byte)i};
	}
	public static final byte[] convertIntTo2Bytes(final int i)
	{
		return new byte[]{(byte)(i>>8), (byte)i};
	}
	public static final byte[] convertIntTo3Bytes(final int i)
	{
		return new byte[]{(byte)(i>>16), (byte)(i>>8), (byte)i};
	}
	public static final byte[] convertIntTo4Bytes(final int i)
	{
		return new byte[]{(byte)(i>>24), (byte)(i>>16), (byte)(i>>8), (byte)i};
	}

	public static final byte[] convertIntTo4Bytes(final int i, final int byteCount)
	{
		switch(byteCount){
			case 1:  return new byte[]{(byte)i};
			case 2:  return new byte[]{(byte)(i>>8), (byte)i};
			case 3:  return new byte[]{(byte)(i>>16), (byte)(i>>8), (byte)i};
			default: return new byte[]{(byte)(i>>24), (byte)(i>>16), (byte)(i>>8), (byte)i};
		}
	}

	public static final byte[] writeIntTo1Bytes(final int i, final byte[] bytes, final int offset)
	{
		bytes[offset] = (byte)i;
		return bytes;
	}
	public static final byte[] writeIntTo2Bytes(final int i, final byte[] bytes, int offset)
	{
		if(offset + 2 > bytes.length){
			throw new ArrayIndexOutOfBoundsException(offset+3);
		}

		bytes[offset++] = (byte)(i>>8);
		bytes[offset] = (byte)i;
		return bytes;
	}
	public static final byte[] writeIntTo3Bytes(final int i, final byte[] bytes, int offset)
	{
		if(offset + 3 > bytes.length){
			throw new ArrayIndexOutOfBoundsException(offset+3);
		}

		bytes[offset++] = (byte)(i>>16);
		bytes[offset++] = (byte)(i>>8);
		bytes[offset] = (byte)i;
		return bytes;
	}
	public static final byte[] writeIntTo4Bytes(final int i, final byte[] bytes, int offset)
	{
		if(offset + 4 > bytes.length){
			throw new ArrayIndexOutOfBoundsException(offset+3);
		}

		bytes[offset++] = (byte)(i>>24);
		bytes[offset++] = (byte)(i>>16);
		bytes[offset++] = (byte)(i>>8);
		bytes[offset] = (byte)i;
		return bytes;
	}

	public static final byte[] writeIntTo4Bytes(final int i, final byte[] bytes, int offset, final int byteCount)
	{
		if(offset + byteCount > bytes.length){
			throw new ArrayIndexOutOfBoundsException(offset+(byteCount-1));
		}

		switch(byteCount){
			case 4:	bytes[offset++] = (byte)(i>>24);
			case 3: bytes[offset++] = (byte)(i>>16);
			case 2: bytes[offset++] = (byte)(i>>8);
			case 1: bytes[offset] = (byte)i;
				break;
			default:
				throw new ArrayIndexOutOfBoundsException(offset);
		}
		return bytes;
	}
	
	/**
	 * Helper method to project ternary values to binary logic.<br>
	 * Useful for checking "really true" (not false and not unknown).
	 * 
	 * @param b a <code>Boolean</code> object.<br>
	 * @return <tt>false</tt> if <code>b</code> is <tt>null</tt> or <tt>false</tt>
	 */
	public static final boolean isTrue(final Boolean b)
	{
		return b == null ?false :b;
	}
	/**
	 * Helper method to project ternary values to binary logic.<br>
	 * Useful for checking "really false" (not true and not unknown).
	 * 
	 * @param b a <code>Boolean</code> object.
	 * @return <tt>false</tt> if <code>b</code> is <tt>null</tt> or <tt>true</tt>, otherwise <tt>true</tt>
	 */
	public static final boolean isFalse(final Boolean b)
	{
		return b == null ?false :!b;
	}
	/**
	 * Helper method to project ternary values to binary logic.<br>
	 * Useful for checking "unknown" (not true and not false).
	 * 
	 * @param b a <code>Boolean</code> object.
	 * @return <tt>false</tt> if <code>b</code> is <tt>null</tt>, otherwise <tt>true</tt>
	 */
	public static final boolean isNull(final Boolean b)
	{
		return b == null;
	}
	/**
	 * Helper method to project ternary values to binary logic.<br>
	 * Useful for checking "really not true" (either false or unknown).
	 * 
	 * @param b a <code>Boolean</code> object.
	 * @return <tt>true</tt> if <code>b</code> is <tt>null</tt> or <tt>false</tt>, otherwise <tt>false</tt>
	 */
	public static final boolean isNotTrue(final Boolean b)
	{
		return b == null ?true :!b;
	}
	/**
	 * Helper method to project ternary values to binary logic.<br>
	 * Useful for checking "really not false" (either true or unknown).
	 * 
	 * @param b a <code>Boolean</code> object.
	 * @return <tt>true</tt> if <code>b</code> is <tt>null</tt> or <tt>true</tt>, otherwise <tt>false</tt>
	 */
	public static final boolean isNotFalse(final Boolean b)
	{
		return b == null ?true :b;
	}
	/**
	 * Helper method to project ternary values to binary logic.<br>
	 * Useful for checking "known" (either true or false).
	 * 
	 * @param b a <code>Boolean</code> object.
	 * @return <tt>true</tt> if <code>b</code> is not <tt>null</tt>, otherwise <tt>false</tt>
	 */
	public static final boolean isNotNull(final Boolean b)
	{
		return b != null;
	}

	
	
	public static final int to_int(final long value) throws NumberRangeException
	{
		if(value > Integer.MAX_VALUE){
			throw new IllegalArgumentException(value+" > "+Integer.MAX_VALUE);
		}
		else if(value < Integer.MIN_VALUE){
			throw new IllegalArgumentException(value+" < "+Integer.MIN_VALUE);
		}
		return (int)value;
	}
	
	public static final int to_int(final float value) throws NumberRangeException
	{
		if(value > Integer.MAX_VALUE){
			throw new IllegalArgumentException(value+" > "+Integer.MAX_VALUE);
		}
		else if(value < Integer.MIN_VALUE){
			throw new IllegalArgumentException(value+" < "+Integer.MIN_VALUE);
		}
		return (int)value;
	}
	
	public static final int to_int(final double value) throws NumberRangeException
	{
		if(value > Integer.MAX_VALUE){
			throw new IllegalArgumentException(value+" > "+Integer.MAX_VALUE);
		}
		else if(value < Integer.MIN_VALUE){
			throw new IllegalArgumentException(value+" < "+Integer.MIN_VALUE);
		}
		return (int)value;
	}
	
	public static final int to_int(final Number value) throws NumberRangeException, NullPointerException
	{
		return to_int(value.longValue());
	}
	
	
	
}
