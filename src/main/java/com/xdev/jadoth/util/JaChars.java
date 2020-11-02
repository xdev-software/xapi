/**
 * 
 */
package com.xdev.jadoth.util;

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


/**
 * @author Thomas Muenz
 *
 */
public final class JaChars
{	
	private JaChars(){}
	
	
//	private static final char[] NULL = {'n','u','l','l'};
//	private static final char[] TRUE = {'t','r','u','e'};
//	private static final char[] FALSE = {'f','a','l','s','e'};
	
	
	public static final char toHexadecimal(final int b) throws IllegalArgumentException
	{
		switch (b){
		case  0: return '0';
		case  1: return '1';
		case  2: return '2';
		case  3: return '3';
		case  4: return '4';
		case  5: return '5';
		case  6: return '6';
		case  7: return '7';
		case  8: return '8';
		case  9: return '9';
		case 10: return 'A';
		case 11: return 'B';
		case 12: return 'C';
		case 13: return 'D';
		case 14: return 'E';
		case 15: return 'F';		
		default:
			throw new IllegalArgumentException(b+" is no positive hexadecimal digit value");
	}
	}
	
	
	public static final int indexOf(
		final char[] source, 
		final int sourceOffset, 
		final int sourceCount,
		final char[] target, 
		final int targetOffset, 
		final int targetCount,
		int fromIndex
	) {
		// (14.08.2010)NOTE: Not sure why sourceOffset and fromIndex are two parameters
		if(fromIndex >= sourceCount) {
			return (targetCount == 0 ?sourceCount :-1);
		}
		if(fromIndex < 0) {
			fromIndex = 0;
		}
		if(targetCount == 0) {
			return fromIndex;
		}

		final char first  = target[targetOffset];
		final int maxFirstIndex = sourceOffset + (sourceCount - targetCount);

		for(int i = sourceOffset + fromIndex; i <= maxFirstIndex; i++) {
			// Look for first character. 
			if(source[i] != first) {
				while (++i <= maxFirstIndex && source[i] != first){}
			}

			// Found first character, now look at the rest of v2 
			if(i <= maxFirstIndex) {
				int j = i + 1;
				final int end = j + targetCount - 1;
				for (int k = targetOffset + 1; j < end && source[j] == target[k]; j++, k++){}

				if(j == end) {
					// Found whole string
					return i - sourceOffset;
				}
			}
		}
		return -1;
	}
	
	
	public static final int indexOf(
		final char[] source,  
		final int sourceCount,
		final char[] target, 
		final int targetCount,
		int fromIndex
	) {
		if (fromIndex >= sourceCount) {
			return (targetCount == 0 ? sourceCount : -1);
		}
		if (fromIndex < 0) {
			fromIndex = 0;
		}
		if (targetCount == 0) {
			return fromIndex;
		}

		final char first  = target[0];
		final int max = (sourceCount - targetCount);

		for (int i = fromIndex; i <= max; i++) {
			/* Look for first character. */
			if (source[i] != first) {
				while (++i <= max && source[i] != first){}
			}

			/* Found first character, now look at the rest of v2 */
			if (i <= max) {
				int j = i + 1;
				final int end = j + targetCount - 1;
				for (int k = 1; j < end && source[j] == target[k]; j++, k++){}

				if (j == end) {
					/* Found whole string. */
					return i;
				}
			}
		}
		return -1;
	}
	
	
	public static final int count(
		final char[] haystack,
		final int haystackOffset,
		final int haystackCount, 
		final char needle
	)
	{
		int count = 0;	
		for(int i = haystackOffset; i < haystackCount; i++){
			if(haystack[i] == needle){
				count++;
			}
		}
		return count;
	}
	

	public static final int count(
		final char[] haystack,
		final int haystackOffset,
		final int haystackCount, 
		final char[] needle,
		final int needleOffset,
		final int needleCount
	)
	{
		int count = 0;			
		int foundIndex = -1;
		while(
			(foundIndex = indexOf(
				haystack, haystackOffset, haystackCount, needle, needleOffset, needleCount, foundIndex+1)
			) != -1
		){
			count++;
		}		
		return count;
	}
	
	
	/**
	 * 
	 * @param s
	 * @return <tt>true</tt> if <code>s</code> is not <tt>null</tt> and not empty.
	 */
	public static final boolean hasContent(final String s)
	{
		return s != null && s.length() != 0;
	}
	
	public static final boolean hasNoContent(final String s)
	{
		return s == null || s.length() == 0;
	}
}
