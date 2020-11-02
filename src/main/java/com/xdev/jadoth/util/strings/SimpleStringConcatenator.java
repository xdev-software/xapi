
package com.xdev.jadoth.util.strings;

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
 * The Class SimpleStringConcatenator.
 * 
 * @author Thomas Muenz
 */
public class SimpleStringConcatenator
{
	///////////////////////////////////////////////////////////////////////////
	// static methods   //
	/////////////////////

	/**
	 * Count total chars.
	 * 
	 * @param parts the parts
	 * @return the int
	 */
	private static int countTotalChars(final String[] parts)
	{
		int totalCharCount = 0;
		for (String s : parts) {
			if(s == null) continue;
			totalCharCount += s.length();
		}
		return totalCharCount;
	}

	/**
	 * Assemble.
	 * 
	 * @param sb the sb
	 * @param parts the parts
	 * @param values the values
	 * @return the string builder
	 */
	public static StringBuilder assemble(final StringBuilder sb, final String[] parts, final Object... values)
	{
		int i = 0;

		//merge parts and values as long as there are enough values
		while(i < parts.length && i < values.length) {
			sb.append(parts[i]).append(values[i++]);
		}

		//if there are too few values, add up the remaining parts (does nothing otherwise)
		while(i < parts.length) {
			sb.append(parts[i++]);
		}

		return sb;
	}



	///////////////////////////////////////////////////////////////////////////
	// instance fields  //
	/////////////////////

	/** The reserved char count. */
	private int reservedCharCount;
	
	/** The parts. */
	private final String[] parts;



	///////////////////////////////////////////////////////////////////////////
	// constructors     //
	/////////////////////

	/**
	 * Instantiates a new simple string concatenator.
	 * 
	 * @param parts the parts
	 */
	public SimpleStringConcatenator(final String[] parts) {
		this(parts, parts.length);
	}

	/**
	 * Instantiates a new simple string concatenator.
	 * 
	 * @param parts the parts
	 * @param reservedValueCharacterCount the reserved value character count
	 */
	public SimpleStringConcatenator(final String[] parts, final int reservedValueCharacterCount)
	{
		super();
		this.reservedCharCount = countTotalChars(parts)+reservedValueCharacterCount < 0 ?0 :reservedValueCharacterCount;
		this.parts = parts;
	}

	/**
	 * Instantiates a new simple string concatenator.
	 * 
	 * @param parts the parts
	 * @param reservedValueCharacterFactor the reserved value character factor
	 */
	public SimpleStringConcatenator(final String[] parts, final float reservedValueCharacterFactor)
	{
		this(
			parts,
			(int)(countTotalChars(parts)*(reservedValueCharacterFactor < 1.0f ? 1.0f:reservedValueCharacterFactor))
		);
	}



	///////////////////////////////////////////////////////////////////////////
	// getters          //
	/////////////////////

	/**
	 * Gets the reserved char count.
	 * 
	 * @return the reserved char count
	 */
	public int getReservedCharCount() {
		return reservedCharCount;
	}
	
	/**
	 * Gets the parts.
	 * 
	 * @return the parts
	 */
	public String[] getParts() {
		return parts;
	}



	///////////////////////////////////////////////////////////////////////////
	// setters          //
	/////////////////////

	/**
	 * Sets the reserved char count.
	 * 
	 * @param reservedCharCount the new reserved char count
	 */
	public void setReservedCharCount(final int reservedCharCount) {
		this.reservedCharCount = reservedCharCount;
	}



	///////////////////////////////////////////////////////////////////////////
	// declared methods //
	/////////////////////

	/**
	 * Assemble.
	 * 
	 * @param values the values
	 * @return the string builder
	 */
	public StringBuilder assemble(final Object... values){
		return assemble(new StringBuilder(this.reservedCharCount), parts, values);
	}

	/**
	 * Assemble.
	 * 
	 * @param sb the sb
	 * @param values the values
	 * @return the string builder
	 */
	public StringBuilder assemble(final StringBuilder sb, final Object... values){
		return assemble(sb, parts, values);
	}

}
