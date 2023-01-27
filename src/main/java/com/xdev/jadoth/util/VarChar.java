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
package com.xdev.jadoth.util;

import static com.xdev.jadoth.lang.reflection.JaReflect.accessCharArray;
import static com.xdev.jadoth.lang.reflection.JaReflect.accessOffset;
import static com.xdev.jadoth.util.JaChars.toHexadecimal;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.xdev.jadoth.lang.functional._charOperation;
import com.xdev.jadoth.lang.functional.controlflow._charControllingProcessor;
import com.xdev.jadoth.lang.signalthrows.ThrowBreak;
import com.xdev.jadoth.lang.signalthrows.ThrowContinue;
import com.xdev.jadoth.lang.signalthrows.ThrowReturn;



/**
 * Faster implementation of a StringBuilder. Note that this class is NOT synchronized and only meant for
 * single-thread or thread-safe (i.e. read-only) use.
 *
 * @author Thomas Muenz
 *
 */

public final class VarChar implements CharSequence, Appendable, Externalizable
{
	/**
	 * Implementors of this interface can handle their appending by themselves.<br>
	 * <br>
	 * Note that {@link java.lang.Appendable} has a misleading name: it should actually be called "Appending",
	 * as implementors of it can append things. Actually being "Appendable" is what is defined by THIS interface.
	 *
	 * @author Thomas Muenz
	 *
	 */
	public interface Appendable
	{
		public VarChar appendTo(VarChar vc);
	}



	///////////////////////////////////////////////////////////////////////////
	// constants        //
	/////////////////////

	private static final long serialVersionUID = 876130105778587634L;

	//have to be 2^n values
	private static final int   CAPACITY_MIN    =     4; //needed for appendNull algorithm (and performance)
	private static final short CAPACITY_TINY   =    16;
	private static final short CAPACITY_SMALL  =    64;
	private static final short CAPACITY_MEDIUM =  1024;
	private static final short CAPACITY_LARGE  = 16384;
	//larger sizes overtop the constructor capacity checks, so use standard constructor




	///////////////////////////////////////////////////////////////////////////
	// static methods   //
	/////////////////////

	private static final int boundPow2(final int n)
	{
		//if desired capacity is not boundable by shifting, max capacity is required
		if(n > 1073741824) {
			return Integer.MAX_VALUE;
		}

		//normal case: start at min capacity and double it until it fits the desired capacity
		int p2 = CAPACITY_MIN;
		while(p2 < n){
			p2 <<= 1;
		}
		return p2;
	}

	/**
	 * Use for single words, names, etc.
	 * @return
	 */
	public static final VarChar TinyVarChar()
	{
		return new VarChar(CAPACITY_TINY);
	}
	/**
	 * Use for short sentences, formulas, etc.
	 * @return
	 */
	public static final VarChar SmallVarChar()
	{
		return new VarChar(CAPACITY_SMALL);
	}
	/**
	 * Use for long sentences, short texts, reading config files, SQL queries, etc.
	 * @return
	 */
	public static final VarChar MediumVarChar()
	{
		return new VarChar(CAPACITY_MEDIUM);
	}
	/**
	 * Use for medium sized texts, java source code, reading text files, etc.
	 * @return
	 */
	public static final VarChar LargeVarChar()
	{
		return new VarChar(CAPACITY_LARGE);
	}



	///////////////////////////////////////////////////////////////////////////
	// instance fields //
	////////////////////

	private char[] data;
	private int size;
	private char listSeperator = ',';



	///////////////////////////////////////////////////////////////////////////
	// constructors //
	/////////////////

	public VarChar()
	{
		this(CAPACITY_SMALL);
	}
	/**
	 * Use this constructor only if really a specific size is needed or of the text to be handled is really big.<br>
	 * Otherwise, use the factory methods as they are faster due to skipping capacity checks and bounds adjustment.<br>
	 * <p>
	 * Note that the given <code>initialCapacity</code> will still be adjusted to the next higher 2^n bounding value.
	 * @param initialCapacity
	 */
	public VarChar(final int initialCapacity)
	{
		super();
		if(initialCapacity < 0){
			throw new IllegalArgumentException("initial capacity may not be negative: "+initialCapacity);
		}

		this.data = new char[boundPow2(initialCapacity)];
		this.size = 0;
	}

	public VarChar(final String s)
	{
		super();
		if(s == null){
			this.data = new char[]{'n', 'u', 'l', 'l'};
			this.size = 4;
		}
		else {
			final int length = s.length();
			if(length == 0){
				this.data = new char[CAPACITY_MIN];
				this.size = 0;
			}
			else {
				this.data = new char[boundPow2(length)];
				this.internalAppend(s);
				this.size = length;
			}
		}
	}

	private VarChar(final short uncheckedInitialCapacity)
	{
		super();
		this.data = new char[uncheckedInitialCapacity];
		this.size = 0;
	}



	///////////////////////////////////////////////////////////////////////////
	// getters          //
	/////////////////////

	/**
	 * @return the listSeperator
	 */
	public char getListSeperator()
	{
		return this.listSeperator;
	}



	///////////////////////////////////////////////////////////////////////////
	// setters          //
	/////////////////////

	/**
	 * @param listSeperator the listSeperator to set
	 */
	public VarChar setListSeperator(final char listSeperator)
	{
		this.listSeperator = listSeperator;
		return this;
	}


	///////////////////////////////////////////////////////////////////////////
	// override methods //
	/////////////////////

	/**
	 * @param index
	 * @return
	 */
	@Override
	public char charAt(final int index)
	{
		if(index < 0 || index >= this.size){
			throw new StringIndexOutOfBoundsException(index);
		}

		return this.data[index];
	}
	/**
	 * @return
	 */
	@Override
	public int length()
	{
		return this.size;
	}
	/**
	 * @param start
	 * @param end
	 * @return
	 */
	@Override
	public VarChar subSequence(final int start, final int end)
	{
		final VarChar subSequence = new VarChar(end - start);
		System.arraycopy(this.data, start, subSequence.data, 0, end - start);
		return subSequence;
	}
	/**
	 * @param csq
	 * @return
	 * @throws IOException
	 */
	@Override
	public VarChar append(final CharSequence csq)
	{
		if(csq == null){
			this.internalAppendNull();
			return this;
		}

		return this.append(csq.toString());
	}
	/**
	 * @param c
	 * @return
	 * @throws IOException
	 */
	@Override
	public VarChar append(final char c)
	{
		if(this.size == this.data.length){
			//expandArray()
			final int length = this.data.length; //always double array size
			final char[] data = new char[length == 1073741824 ?Integer.MAX_VALUE :length<<1];
			System.arraycopy(this.data, 0, data, 0, this.size);
			this.data = data;
		}
		this.data[this.size++] = c;
		return this;
	}

	/**
	 * Useful for including seperator character to build lists like "a,b,c,d,e".
	 * @param c1
	 * @param c2
	 * @return
	 * @see VarChar#deleteLastChar()
	 */
	public VarChar append(final char c1, final char c2)
	{
		int size = this.size;
		char[] data = this.data;
		if(size+1 >= data.length){
			//expandArray()
			final int length = this.data.length; //always double array size
			data = new char[length == 1073741824 ?Integer.MAX_VALUE :length<<1];
			System.arraycopy(this.data, 0, data, 0, this.size);
			this.data = data;
		}
		data[size++] = c1;
		data[size++] = c2;
		this.size = size;
		return this;
	}

	public VarChar append(final char c1, final char c2, final char c3)
	{
		int size = this.size;
		char[] data = this.data;
		if(size+2 >= data.length){
			//expandArray()
			final int length = data.length; //always double array size
			data = new char[length == 1073741824 ?Integer.MAX_VALUE :length<<1];
			System.arraycopy(this.data, 0, data, 0, size);
			this.data = data;
		}
		data[size++] = c1;
		data[size++] = c2;
		data[size++] = c3;
		this.size = size;
		return this;
	}

	public VarChar append(final Character c)
	{
		if(c == null){
			this.internalAppendNull();
			return this;
		}
		return this.append(c.charValue());
	}


	/**
	 * @param csq
	 * @param start
	 * @param end
	 * @return
	 * @throws IOException
	 */
	@Override
	public VarChar append(final CharSequence csq, final int start, final int end)
	{
		if(csq == null){
			this.internalAppendNull();
			return this;
		}

		return this.append(csq.subSequence(start, end));
	}
	/**
	 * @param in
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	@Override
	public void readExternal(final ObjectInput in) throws IOException, ClassNotFoundException
	{
		final int size = in.read();
		final char[] data = new char[boundPow2(size)];

		for(int i = 0; i < size; i++){
			data[i] = in.readChar();
		}
		this.data = data;
		this.size = size;
	}
	/**
	 * @param out
	 * @throws IOException
	 */
	@Override
	public void writeExternal(final ObjectOutput out) throws IOException
	{
		final int size = this.size;
		final char[] data = this.data;

		out.write(size);
		for(int i = 0; i < size; i++){
			out.writeChar(data[i]);
		}
	}


	/**
	 * @return
	 */
	@Override
	public String toString()
	{
		return new String(this.data, 0, this.size);
	}



	///////////////////////////////////////////////////////////////////////////
	// declared methods //
	/////////////////////



	public VarChar add(final Object o)
	{
		this.internalAdd(o);
		return this;
	}

	public VarChar add(final boolean b)
	{
		if(b){
			this.appendTrue();
		}
		else {
			this.appendFalse();
		}
		return this;
	}
	public VarChar add(final char c)
	{
		if(this.size == this.data.length){
			//expandArray()
			final int length = this.data.length; //always double array size
			final char[] data = new char[length == 1073741824 ?Integer.MAX_VALUE :length<<1];
			System.arraycopy(this.data, 0, data, 0, this.size);
			this.data = data;
		}
		this.data[this.size++] = c;
		return this;
	}
	public VarChar add(final byte b)
	{
		this.internalAppend(Byte.toString(b));
		return this;
	}
	public VarChar add(final short s)
	{
		this.internalAppend(Short.toString(s));
		return this;
	}
	public VarChar add(final int i)
	{
		this.internalAppend(Integer.toString(i));
		return this;
	}
	public VarChar add(final long l)
	{
		this.internalAppend(Long.toString(l));
		return this;
	}
	public VarChar add(final float f)
	{
		this.internalAppend(Float.toString(f));
		return this;
	}
	public VarChar add(final double d)
	{
		this.internalAppend(Double.toString(d));
		return this;
	}



	public VarChar append(final Object o)
	{
		this.internalAdd(o);
		return this;
	}

	private void internalAdd(final Object o)
	{
		//faster than String.valueOf(o) but same result
		if(o == null){
			this.internalAppendNull();
			return;
		}
		else if(o instanceof CharSequence){
			final int length = ((CharSequence)o).length();
			if(length == 0) return;

			if(o instanceof String){
				this.internalAppend((String)o);
				return;
			}
			else if(o instanceof VarChar){
				final VarChar vc = (VarChar)o;
				this.internalAppend(vc.data, 0, length); // length == vc.size
				return;
			}
			else if(o instanceof StringBuilder){
				final StringBuilder sb = (StringBuilder)o;
				sb.getChars(0, length, this.data, this.size);
				this.size += length;
				return;
			}
			else if(o instanceof StringBuffer){
				final StringBuffer sb = (StringBuffer)o;
				sb.getChars(0, length, this.data, this.size);
				this.size += length;
				return;
			}
			//drop to default
		}
		else if(o instanceof VarChar.Appendable){
			((VarChar.Appendable)o).appendTo(this);
			return;
		}
		else if(o instanceof char[]){
			this.internalAppend((char[])o);
			return;
		}
		this.internalAppend(o.toString());
	}




	public VarChar appendHexBytes(final byte... bytes)
	{
		this.ensureCapacity(this.size + bytes.length<<1);
		for(final byte b : bytes){
			this.append(toHexadecimal((b & 255) >> 4), toHexadecimal(b & 15));
		}
		return this;
	}
	public VarChar appendHexInts(final int... ints)
	{
		this.ensureCapacity(this.size + ints.length<<3);
		for(final int i : ints){
			this.append(toHexadecimal(i >> 28), toHexadecimal((i & 268435455) >> 24));
			this.append(toHexadecimal((i & 16777215) >> 20), toHexadecimal((i & 1048575) >> 16));
			this.append(toHexadecimal((i & 65535) >> 12), toHexadecimal((i & 4095) >> 8));
			this.append(toHexadecimal((i & 255) >> 4), toHexadecimal(i & 15));
		}
		return this;
	}


	public VarChar listHex(final char seperator, final byte... bytes)
	{
		this.ensureCapacity(bytes.length*3);
		for(final byte b : bytes){
			this.append(toHexadecimal((b & 255) >> 4), toHexadecimal(b & 15), seperator);
		}
		return this;
	}
	public VarChar listHexInts(final char seperator, final int... ints)
	{
		this.ensureCapacity(this.size + ints.length<<3);
		for(final int i : ints){
			this.append(toHexadecimal(i >> 28), toHexadecimal((i & 268435455) >> 24));
			this.append(toHexadecimal((i & 16777215) >> 20), toHexadecimal((i & 1048575) >> 16));
			this.append(toHexadecimal((i & 65535) >> 12), toHexadecimal((i & 4095) >> 8));
			this.append(toHexadecimal((i & 255) >> 4), toHexadecimal(i & 15), seperator);
		}
		return this;
	}




	public VarChar appendArray(final Object... objects)
	{
		for(final Object o : objects){
			this.append(o);
		}
		return this;
	}


	public VarChar append(final String s)
	{
		if(s == null){
			this.internalAppendNull();
		}
		else {
			this.internalAppend(s);
		}
		return this;
	}

	public VarChar append(final boolean b)
	{
		this.internalAppend(Boolean.toString(b));
		return this;
	}
	public VarChar append(final byte b)
	{
		this.internalAppend(Byte.toString(b));
		return this;
	}
	public VarChar append(final short b)
	{
		this.internalAppend(Short.toString(b));
		return this;
	}
	public VarChar append(final int b)
	{
		this.internalAppend(Integer.toString(b));
		return this;
	}
	public VarChar append(final long b)
	{
		this.internalAppend(Long.toString(b));
		return this;
	}
	public VarChar append(final float b)
	{
		this.internalAppend(Float.toString(b));
		return this;
	}
	public VarChar append(final double b)
	{
		this.internalAppend(Double.toString(b));
		return this;
	}





	public VarChar append(final VarChar varChar)
	{
		if(varChar == null){
			this.internalAppendNull();
			return this;
		}
		else if(varChar.size == 0){
			return this;
		}
		this.internalAppend(varChar.data, 0, varChar.size);
		return this;
	}


	private void internalAppend(final String s)
	{
		final int length = s.length();
		if(length == 0) return;


		final int size = this.size;
		final int newSize = size + length;
		int capacity = this.data.length;
		if(newSize >= capacity){
			//expandArray()
			if(newSize > 1073741824) {
				//this case automatically handles (this.data.length == 1073741824)
				capacity = Integer.MAX_VALUE;
			}
			else {
				//normale case: capacity up to 1073741824 will suffice
				while(capacity < newSize){
					capacity <<= 1;
				}
			}
			final char[] data = new char[capacity];
			System.arraycopy(this.data, 0, data, 0, size);
			s.getChars(0, length, data, size);
			this.data = data;
		}
		else {
			s.getChars(0, length, this.data, size);
		}
		this.size = newSize;
	}

	private void internalAppend(final char[] chars, final int offset, final int length)
	{
		char[] data;

		final int neededSize = this.size + length;
		int capacity = this.data.length;
		if(neededSize >= capacity){
			//expandArray()
			if(neededSize > 1073741824) {
				//this case automatically handles (this.data.length == 1073741824)
				capacity = Integer.MAX_VALUE;
			}
			else {
				//normale case: capacity up to 1073741824 will suffice
				while(capacity < neededSize){
					capacity <<= 1;
				}
			}
			data = new char[capacity];
			System.arraycopy(this.data, 0, data, 0, this.size);
			this.data = data;
		}
		else {
			data = this.data;
		}

		System.arraycopy(chars, offset, data, this.size, length);
		this.size = neededSize;
	}
	private void internalAppend(final char[] chars)
	{
		char[] data;

		final int neededSize = this.size + chars.length;
		int capacity = this.data.length;
		if(neededSize >= capacity){
			//expandArray()
			if(neededSize > 1073741824) {
				//this case automatically handles (this.data.length == 1073741824)
				capacity = Integer.MAX_VALUE;
			}
			else {
				//normale case: capacity up to 1073741824 will suffice
				while(capacity < neededSize){
					capacity <<= 1;
				}
			}
			data = new char[capacity];
			System.arraycopy(this.data, 0, data, 0, this.size);
			this.data = data;
		}
		else {
			data = this.data;
		}

		System.arraycopy(chars, 0, data, this.size, chars.length);
		this.size = neededSize;
	}


	public void ensureCapacity(final int minimumCapacity)
	{
		int capacity = this.data.length;
		if(minimumCapacity <= capacity) return;

		char[] data;
		//expandArray()
		if(minimumCapacity > 1073741824) {
			//this case automatically handles (this.data.length == 1073741824)
			capacity = Integer.MAX_VALUE;
		}
		else {
			//normale case: capacity up to 1073741824 will suffice
			while(capacity < minimumCapacity){
				capacity <<= 1;
			}
		}
		data = new char[capacity];
		System.arraycopy(this.data, 0, data, 0, this.size);
		this.data = data;
	}


	public VarChar appendArray(final char... chars)
	{
		this.internalAppend(chars, 0, chars.length);
		return this;
	}
	public VarChar append(final char[] chars)
	{
		this.internalAppend(chars, 0, chars.length);
		return this;
	}

	public VarChar appendArray(final CharSequence... csqs)
	{
		for(final CharSequence csq : csqs){
			this.append(csq);
		}
		return this;
	}

	private final void internalAppendNull()
	{
		int size = this.size;
		char[] data = this.data;
		if(size+4 > data.length){
			data = new char[data.length == 1073741824 ?Integer.MAX_VALUE :data.length<<1]; //will suffice as min size is 4, so left shift 1 yields at least 8.
			System.arraycopy(this.data, 0, data, 0, size);
			this.data = data;
		}
		//significantly faster than arraycopy
		data[size++] = 'n';
		data[size++] = 'u';
		data[size++] = 'l';
		data[size++] = 'l';
		this.size = size;
	}

	private final void internalAppendTrue()
	{
		int size = this.size;
		char[] data = this.data;
		if(size+4 > data.length){
			data = new char[data.length == 1073741824 ?Integer.MAX_VALUE :data.length<<1]; //will suffice as min size is 4, so left shift 1 yields at least 8.
			System.arraycopy(this.data, 0, data, 0, size);
			this.data = data;
		}
		//significantly faster than arraycopy
		data[size++] = 't';
		data[size++] = 'r';
		data[size++] = 'u';
		data[size++] = 'e';
		this.size = size;
	}

	private final void internalAppendFalse()
	{
		int size = this.size;
		char[] data = this.data;
		if(size+5 > data.length){
			data = new char[data.length == 1073741824 ?Integer.MAX_VALUE :size==4 ?16 :data.length<<1];
			System.arraycopy(this.data, 0, data, 0, size);
			this.data = data;
		}
		//significantly faster than arraycopy
		data[size++] = 'f';
		data[size++] = 'a';
		data[size++] = 'l';
		data[size++] = 's';
		data[size++] = 'e';
		this.size = size;
	}


	public final VarChar appendNull()
	{
		this.internalAppendNull();
		return this;
	}
	public final VarChar appendTrue()
	{
		this.internalAppendTrue();
		return this;
	}
	public final VarChar appendFalse()
	{
		this.internalAppendFalse();
		return this;
	}



	public VarChar append(final VarChar.Appendable appendable)
	{
		if(appendable == null){
			this.internalAppendNull();
		}
		else {
			appendable.appendTo(this);
		}
		return this;
	}

	public VarChar setChar(final int index, final char c)
	{
		if(index < 0 || index >= this.size){
			throw new StringIndexOutOfBoundsException(index);
		}

		this.data[index] = c;
		return this;
	}

	public VarChar setChars(final int index, final char... c)
	{
		if(index+c.length >= this.size){
			throw new IndexOutOfBoundsException("Index is out of length: "+index+" > "+this.size);
		}
		System.arraycopy(c, 0, this.data, index, c.length);
		return this;
	}

	public VarChar setLastChar(final char c)
	{
		this.data[this.size-1] = c;
		return this;
	}


	public VarChar reverse()
	{
		final char[] data = this.data;

		char loopSwapChar;
		//only swap until size/2 (rounded down, because center element can remain untouched)
		for(int i = this.size>>1, last = this.size-1; i != 0; i--){
			loopSwapChar = data[i];
			data[i] = data[last - i];
			data[last - i] = loopSwapChar;
		}
		return this;
	}

	/**
	 * Not implemented yet.
	 * @return currenty exactely what {@link #reverse()} returns.
	 * @deprecated not implemented yet. Currently just does {@link #reverse()}.
	 * @see #reverse()
	 */
	@Deprecated
	public VarChar surrogateCharReverse()
	{
		return this.reverse();
	}


	public int indexOf(final char c)
	{
		final char[] data = this.data;
		for(int i = 0, size = this.size; i < size; i++){
			if(data[i] == c){
				return i;
			}
		}
		return -1;
	}

	public int indexOf(final char c, final int fromIndex)
	{
		final int size = this.size;
		if(fromIndex < 0 || fromIndex >= size){
			throw new StringIndexOutOfBoundsException(fromIndex);
		}

		final char[] data = this.data;
		for(int i = fromIndex; i < size; i++){
			if(data[i] == c){
				return i;
			}
		}
		return -1;
	}



	public int indexOf(final char[] chars)
	{
		return JaChars.indexOf(this.data, this.size, chars, chars.length, 0);
	}

	public int indexOf(final char[] chars, final int fromIndex)
	{
		return JaChars.indexOf(this.data, this.size, chars, chars.length, fromIndex);
	}

	public int indexOf(final String s)
	{
		return JaChars.indexOf(this.data, 0, this.size, accessCharArray(s), accessOffset(s), s.length(), 0);
	}

	public int indexOf(final String s, final int fromIndex)
	{
		return JaChars.indexOf(this.data, 0, this.size, accessCharArray(s), accessOffset(s), s.length(), fromIndex);
	}

	public int indexOf(final VarChar vc)
	{
		return JaChars.indexOf(this.data, this.size, vc.data, vc.size, 0);
	}

	public int indexOf(final VarChar vc, final int fromIndex)
	{
		return JaChars.indexOf(this.data, this.size, vc.data, vc.size, fromIndex);
	}

	public int indexOf(final StringBuilder sb)
	{
		return JaChars.indexOf(this.data, this.size, accessCharArray(sb), sb.length(), 0);
	}

	public int indexOf(final StringBuilder sb, final int fromIndex)
	{
		return JaChars.indexOf(this.data, this.size, accessCharArray(sb), sb.length(), fromIndex);
	}

	public int indexOf(final StringBuffer sb)
	{
		return JaChars.indexOf(this.data, this.size, accessCharArray(sb), sb.length(), 0);
	}

	public int indexOf(final StringBuffer sb, final int fromIndex)
	{
		return JaChars.indexOf(this.data, this.size, accessCharArray(sb), sb.length(), fromIndex);
	}


	public boolean contains(final char c)
	{
		final char[] data = this.data;
		for(int i = 0, size = this.size; i < size; i++){
			if(data[i] == c){
				return true;
			}
		}
		return false;
	}

	public boolean contains(final char[] chars)
	{
		return JaChars.indexOf(this.data, this.size, chars, chars.length, 0) != -1;
	}

	public boolean contains(final String s)
	{
		return JaChars.indexOf(this.data, 0, this.size, accessCharArray(s), accessOffset(s), s.length(), 0) != -1;
	}

	public boolean contains(final VarChar vc)
	{
		return JaChars.indexOf(this.data, this.size, vc.data, vc.size, 0) != -1;
	}

	public boolean contains(final StringBuilder sb)
	{
		return JaChars.indexOf(this.data, this.size, accessCharArray(sb), sb.length(), 0) != -1;
	}

	public boolean contains(final StringBuffer sb)
	{
		return JaChars.indexOf(this.data, this.size, accessCharArray(sb), sb.length(), 0) != -1;
	}



	public int lastIndexOf(final char c)
	{
		final char[] data = this.data;
		for(int i = this.size; i --> 0;){
			if(data[i] == c){
				return i;
			}
		}
		return -1;
	}

	public int lastIndexOf(final char c, final int fromIndex)
	{
		if(fromIndex < 0 || fromIndex >= this.size){
			throw new StringIndexOutOfBoundsException(fromIndex);
		}

		final char[] data = this.data;
		for(int i = fromIndex; i > 0; i--){
			if(data[i] == c){
				return i;
			}
		}
		return -1;
	}



	public int count(final char c)
	{
		return JaChars.count(this.data, 0, this.size, c);
	}


	public int count(final char[] chars)
	{
		return JaChars.count(this.data, 0, this.size, chars, 0, chars.length);
	}

	public int count(final String s)
	{
		return JaChars.count(this.data, 0, this.size, accessCharArray(s), accessOffset(s), s.length());
	}

	public int count(final VarChar vc)
	{
		return JaChars.count(this.data, 0, this.size, vc.data, 0, vc.size);
	}

	public int count(final StringBuilder sb)
	{
		return JaChars.count(this.data, 0, this.size, accessCharArray(sb), 0, sb.length());
	}

	public int count(final StringBuffer sb)
	{
		return JaChars.count(this.data, 0, this.size, accessCharArray(sb), 0, sb.length());
	}



	public VarChar deleteCharAt(final int index)
	{
		final int lastIndex = this.size - 1;
		if(index < 0 || index > lastIndex){
			throw new StringIndexOutOfBoundsException(index);
		}
		//intentionally don't check for index != lastIndex, as there's an extra method for that.
		System.arraycopy(this.data, index+1, this.data, index, lastIndex-index);
		this.size--;
		return this;
	}

	public VarChar deleteLastChar()
	{
		if(this.size == 0){
			throw new StringIndexOutOfBoundsException("Cannot delete last char of no chars");
		}
		this.size--;
		return this;
	}
	public VarChar deleteLast(final int n)
	{
		if(this.size < n){
			throw new StringIndexOutOfBoundsException(n+" chars cannot be deleted from "+this.size+" chars");
		}
		this.size -= n;
		return this;
	}

	public char[] toCharArray()
	{
		final char[] chars = new char[this.size];
		System.arraycopy(this.data, 0, chars, 0, this.size);
		return chars;
	}

	public StringBuilder toStringBuilder()
	{
		return new StringBuilder(this.size).append(this.data);
	}

	public StringBuffer toStringBuffer()
	{
		return new StringBuffer(this.size).append(this.data);
	}




	public void getChars(final int srcBegin, final int srcEnd, final char dst[], final int dstBegin)
	{
		if (srcBegin < 0) {
			throw new StringIndexOutOfBoundsException(srcBegin);
		}
		if (srcEnd > this.size) {
			throw new StringIndexOutOfBoundsException(srcEnd);
		}
		if (srcBegin > srcEnd) {
			throw new StringIndexOutOfBoundsException(srcEnd - srcBegin);
		}
		System.arraycopy(this.data, srcBegin, dst, dstBegin, srcEnd - srcBegin);
	}

	public boolean isEmpty()
	{
		return this.size == 0;
	}


	public void trimToSize()
	{
		final int size = this.size;
		if(size<<1 > this.data.length || size > 1073741824){
			return; //not shrinkable, abort
		}

		//only run from 4 to 1073741824
		int newCapacity = CAPACITY_MIN;
		while(newCapacity < size){
			newCapacity <<= 1;
		}
		final char[] data = new char[newCapacity];
		System.arraycopy(this.data, 0, data, 0, size);
		this.data = data;
	}

	/**
	 *
	 * @param varChar
	 * @return <tt>true</tt> if <code>varChar</code> is either <tt>null</tt> or empty.
	 * @see {@link VarChar#isEmpty()}
	 */
	public static final boolean hasNoContent(final VarChar varChar)
	{
		return varChar == null || varChar.size == 0;
	}

	/**
	 *
	 * @param varChar
	 * @return <tt>true</tt> if <code>varChar</code> is not <tt>null</tt> and not empty.
	 * @see {@link VarChar#isEmpty()}
	 */
	public static final boolean hasContent(final VarChar varChar)
	{
		return varChar != null && varChar.size != 0;
	}

	public String replaceAll(final String regex, final String replacement)
	{
		return Pattern.compile(regex).matcher(this).replaceAll(replacement);
	}


	public String replace(final CharSequence target, final CharSequence replacement)
	{
		return Pattern.compile(target.toString(), Pattern.LITERAL).matcher(
			this).replaceAll(Matcher.quoteReplacement(replacement.toString()));
	}

	public String[] split(final String regex, final int limit)
	{
		return Pattern.compile(regex).split(this, limit);
	}

	public String[] split(final String regex)
	{
		return this.split(regex, 0);
	}


	public VarChar trim()
	{
		int size = this.size;
		int start = 0;
		final char[] data = this.data;

		while(start < size && data[start] <= ' '){
			start++;
		}
		while(start < size && data[size - 1] <= ' '){
			size--;
		}
		return start > 0 || size < this.size ? this.subsequence(start, size) : this;
	}


	public VarChar subsequence(final int beginIndex, final int endIndex)
	{
		if (beginIndex < 0) {
			throw new StringIndexOutOfBoundsException(beginIndex);
		}
		if (endIndex > this.size) {
			throw new StringIndexOutOfBoundsException(endIndex);
		}
		if (beginIndex > endIndex) {
			throw new StringIndexOutOfBoundsException(endIndex - beginIndex);
		}
		final int length = endIndex - beginIndex;
		final VarChar vc = new VarChar(length);
		System.arraycopy(this.data, beginIndex, vc.data, 0, length);
		vc.size = length;
		return vc;
	}

	public String substring(final int beginIndex, final int endIndex)
	{
		if (beginIndex < 0) {
			throw new StringIndexOutOfBoundsException(beginIndex);
		}
		if (endIndex > this.size) {
			throw new StringIndexOutOfBoundsException(endIndex);
		}
		if (beginIndex > endIndex) {
			throw new StringIndexOutOfBoundsException(endIndex - beginIndex);
		}
		return new String(this.data, beginIndex, endIndex - beginIndex);
	}

	public void process(final _charControllingProcessor processor)
	{
		final char[] data = this.data;
		for(int i = 0, size = this.size; i < size; i++){
			try {
				processor.process(data[i]);
			}
			catch(final ThrowBreak e)   { break; }
			catch(final ThrowContinue e){ continue; }
			catch(final ThrowReturn e)  { return; }
		}
	}

	public void process(final _charOperation processor)
	{
		final char[] data = this.data;
		for(int i = 0, size = this.size; i < size; i++){
			processor.execute(data[i]);
		}
	}


	public VarChar list(final Object... listElements)
	{
		final char sep = this.listSeperator;
		for(final Object e : listElements){
			this.append(e).append(sep);
		}
		return this;
	}
	public VarChar list(final String... listElements)
	{
		final char sep = this.listSeperator;
		for(final String e : listElements){
			this.append(e).append(sep);
		}
		return this;
	}
	public VarChar list(final VarChar... listElements)
	{
		final char sep = this.listSeperator;
		for(final VarChar e : listElements){
			this.append(e).append(sep);
		}
		return this;
	}
	public VarChar list(final Appendable... listElements)
	{
		final char sep = this.listSeperator;
		for(final Appendable e : listElements){
			this.append(e).append(sep);
		}
		return this;
	}

	public VarChar list(final boolean... listElements)
	{
		final char sep = this.listSeperator;
		for(final boolean e : listElements){
			this.append(e).append(sep);
		}
		return this;
	}
	public VarChar list(final byte... listElements)
	{
		final char sep = this.listSeperator;
		for(final byte e : listElements){
			this.append(e).append(sep);
		}
		return this;
	}
	public VarChar list(final short... listElements)
	{
		final char sep = this.listSeperator;
		for(final short e : listElements){
			this.append(e).append(sep);
		}
		return this;
	}
	public VarChar list(final int... listElements)
	{
		final char sep = this.listSeperator;
		for(final int e : listElements){
			this.append(e).append(sep);
		}
		return this;
	}
	public VarChar list(final long... listElements)
	{
		final char sep = this.listSeperator;
		for(final long e : listElements){
			this.append(e).append(sep);
		}
		return this;
	}
	public VarChar list(final float... listElements)
	{
		final char sep = this.listSeperator;
		for(final float e : listElements){
			this.append(e).append(sep);
		}
		return this;
	}
	public VarChar list(final double... listElements)
	{
		final char sep = this.listSeperator;
		for(final double e : listElements){
			this.append(e).append(sep);
		}
		return this;
	}
	public VarChar list(final char... listElements)
	{
		final char sep = this.listSeperator;
		for(final char e : listElements){
			this.append(e).append(sep);
		}
		return this;
	}


	public VarChar listIterable(final Iterable<?> iterable)
	{
		final char sep = this.listSeperator;
		for(final Object e : iterable){
			this.append(e).append(sep);
		}
		return this;
	}

}
