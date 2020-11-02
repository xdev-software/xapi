

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


import java.util.regex.Pattern;

import com.xdev.jadoth.sqlengine.exceptions.SQLEngineInvalidIdentifier;



/**
 * The Class SqlIdentifier.
 * 
 * @author Thomas Muenz
 */
public class SqlIdentifier extends SqlExpression
{

	/**
	 * 
	 */
	private static final long	serialVersionUID	= -2694088528836891387L;
	/*
	 * http://localhost:57772/csp/docbook/DocBook.UI.Page.cls?KEY=GSQL_identifiers
	 */
	/** The Constant validationPattern. */
	public static final String	validationPattern	= "\\A[a-zA-Z%\\_]?[a-zA-Z0-9_@#$]*\\Z";


	/**
	 * Instantiates a new sql identifier.
	 */
	public SqlIdentifier()
	{
		super(null);
	}


	/**
	 * Instantiates a new sql identifier.
	 * 
	 * @param expression
	 *            the expression
	 * @throws SQLEngineInvalidIdentifier
	 *             the sQL engine invalid identifier
	 */
	public SqlIdentifier(final String expression) throws SQLEngineInvalidIdentifier
	{
//		super(validateIdentifierString(expression));
		super(expression);
	}


	/**
	 * This constructor is meant for use with an object that provides the
	 * expression to some later point in time or that can change it's expression
	 * dynamically.
	 * <p>
	 * Note that no identifier validation can be done here!
	 * 
	 * @param expression
	 *            the expression
	 */
	protected SqlIdentifier(final Object expression)
	{
		super(expression);
	}


	/**
	 * Validate identifier string.
	 * 
	 * @param identifierCandidate
	 *            the identifier candidate
	 * @return the string
	 * @throws SQLEngineInvalidIdentifier
	 *             the sQL engine invalid identifier
	 */
	public static String validateIdentifierString(final String identifierCandidate)
			throws SQLEngineInvalidIdentifier
	{
		if(identifierCandidate == null)
		{
			return null;
		}
		else if(Pattern.matches(validationPattern,identifierCandidate))
		{
			return identifierCandidate;
		}
		else
		{
			throw new SQLEngineInvalidIdentifier();
		}
	}

}
