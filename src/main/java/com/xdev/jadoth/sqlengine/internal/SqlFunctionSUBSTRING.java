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

package com.xdev.jadoth.sqlengine.internal;

import static com.xdev.jadoth.sqlengine.SQL.LANG.SUBSTRING;


/**
 * The Class SqlFunctionSUBSTRING.
 * 
 * @author Thomas Muenz
 */
public class SqlFunctionSUBSTRING extends SqlFunction
{
	///////////////////////////////////////////////////////////////////////////
	// instance fields  //
	/////////////////////

	/**
	 * 
	 */
	private static final long serialVersionUID = -3387570291349387520L;

	/** The start position. */
	final private Integer startPosition;
	
	/** The string length. */
	final private Integer stringLength;
	
	/** The char length units. */
	final private Integer charLengthUnits;



	///////////////////////////////////////////////////////////////////////////
	// constructors     //
	/////////////////////


	/**
	 * Instantiates a new sql function substring.
	 * 
	 * @param characterValueExpression the character value expression
	 * @param startPosition the start position
	 * @param stringLength the string length
	 * @param charLengthUnits the char length units
	 */
	public SqlFunctionSUBSTRING(
		final Object characterValueExpression,
		final Integer startPosition,
		final Integer stringLength,
		final Integer charLengthUnits
	)
	{
		super(null, characterValueExpression);
		this.startPosition = startPosition;
		this.stringLength = stringLength;
		this.charLengthUnits = charLengthUnits;
	}

	/**
	 * Instantiates a new sql function substring.
	 * 
	 * @param characterValueExpression the character value expression
	 * @param startPosition the start position
	 * @param stringLength the string length
	 */
	public SqlFunctionSUBSTRING(
		final Object characterValueExpression,
		final Integer startPosition,
		final Integer stringLength
	)
	{
		this(characterValueExpression, startPosition, stringLength, null);
	}

	/**
	 * Instantiates a new sql function substring.
	 * 
	 * @param characterValueExpression the character value expression
	 * @param startPosition the start position
	 */
	public SqlFunctionSUBSTRING(final Object characterValueExpression, final Integer startPosition)
	{
		this(characterValueExpression, startPosition, null, null);
	}

	/**
	 * Instantiates a new sql function substring.
	 * 
	 * @param characterValueExpression the character value expression
	 */
	public SqlFunctionSUBSTRING(final Object characterValueExpression)
	{
		this(characterValueExpression, null, null, null);
	}



	///////////////////////////////////////////////////////////////////////////
	// override methods //
	/////////////////////

	/**
	 * @return
	 * @see com.xdev.jadoth.sqlengine.internal.SqlFunction#getFunctionName()
	 */
	@Override
	public String getFunctionName() {
		return SUBSTRING;
	}



	///////////////////////////////////////////////////////////////////////////
	// getters          //
	/////////////////////

	/**
	 * Gets the character value expression.
	 * 
	 * @return the characterValueExpression
	 */
	public Object getCharacterValueExpression() {
		return this.parameters[0];
	}

	/**
	 * Gets the start position.
	 * 
	 * @return the startPosition
	 */
	public Integer getStartPosition() {
		return this.startPosition;
	}

	/**
	 * Gets the string length.
	 * 
	 * @return the stringLength
	 */
	public Integer getStringLength() {
		return this.stringLength;
	}

	/**
	 * Gets the char length units.
	 * 
	 * @return the charLengthUnits
	 */
	public Integer getCharLengthUnits() {
		return this.charLengthUnits;
	}

}
