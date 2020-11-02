
package com.xdev.jadoth.sqlengine.internal;

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
 * The Class SqlxAggregateCOLLECT_asString.
 * 
 * @author Thomas Muenz
 */
public class SqlxAggregateCOLLECT_asString extends SqlAggregateCOLLECT
{
	///////////////////////////////////////////////////////////////////////////
	// instance fields  //
	/////////////////////

	/**
	 * 
	 */
	private static final long serialVersionUID = -479490625903366493L;
	/** The seperator. */
	private String seperator;



	///////////////////////////////////////////////////////////////////////////
	// constructors     //
	/////////////////////

	/**
	 * Instantiates a new sqlx aggregate collec t_as string.
	 * 
	 * @param innerExpression the inner expression
	 */
	public SqlxAggregateCOLLECT_asString(final Object innerExpression)
	{
		this(innerExpression, ",");
	}
	
	/**
	 * Instantiates a new sqlx aggregate collec t_as string.
	 * 
	 * @param innerExpression the inner expression
	 * @param seperator the seperator
	 */
	public SqlxAggregateCOLLECT_asString(final Object innerExpression, final String seperator)
	{
		super(innerExpression);
		this.seperator = seperator;
	}



	///////////////////////////////////////////////////////////////////////////
	// getters          //
	/////////////////////

	/**
	 * Gets the seperator.
	 * 
	 * @return the seperator
	 */
	public String getSeperator()
	{
		return this.seperator;
	}

}
