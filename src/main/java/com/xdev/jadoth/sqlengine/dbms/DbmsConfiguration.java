/**
 * 
 */
package com.xdev.jadoth.sqlengine.dbms;

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
public class DbmsConfiguration<A extends DbmsAdaptor<A>>
{
	///////////////////////////////////////////////////////////////////////////
	// static methods //
	///////////////////
	
	public static final <A extends DbmsAdaptor<A>> DbmsConfiguration<A> config(
		final Class<A> dbmsAdaptorType,
		final boolean  delimitTableIdentifiers,
		final boolean  delimitColumnIdentifiers,
		final boolean  delimitAliases,
		final boolean  autoEscapeReservedWords
	)
	{
		return new DbmsConfiguration<A>()
			.setDelimitTableIdentifiers(delimitTableIdentifiers)
			.setDelimitColumnIdentifiers(delimitTableIdentifiers)
			.setDelimitAliases(delimitTableIdentifiers)
			.setAutoDelimitReservedWords(autoEscapeReservedWords)
		;
	}
	
	
	///////////////////////////////////////////////////////////////////////////
	// instance fields //
	////////////////////
	
	private boolean delimitTableIdentifiers = false;
	private boolean delimitColumnIdentifiers = false;
	private boolean delimitAliases = false;
	private boolean autoEscapeReservedWords = false;

	
	
	///////////////////////////////////////////////////////////////////////////
	// getters          //
	/////////////////////
	
	/**
	 * @return the delimitTableIdentifiers
	 */
	public boolean isDelimitTableIdentifiers()
	{
		return delimitTableIdentifiers;
	}
	/**
	 * @return the delimitColumnIdentifiers
	 */
	public boolean isDelimitColumnIdentifiers()
	{
		return delimitColumnIdentifiers;
	}
	/**
	 * @return the delimitAliases
	 */
	public boolean isDelimitAliases()
	{
		return delimitAliases;
	}
	/**
	 * @return the autoEscapeReservedWords
	 */
	public boolean isAutoEscapeReservedWords()
	{
		return this.autoEscapeReservedWords;
	}

	
	
	
	///////////////////////////////////////////////////////////////////////////
	// setters          //
	/////////////////////
	
	/**
	 * @param delimitTableIdentifiers the delimitTableIdentifiers to set
	 */
	public DbmsConfiguration<A> setDelimitTableIdentifiers(final boolean delimitTableIdentifiers)
	{
		this.delimitTableIdentifiers = delimitTableIdentifiers;
		return this;
	}
	/**
	 * @param autoEscapeReservedWords the autoEscapeReservedWords to set
	 */
	public DbmsConfiguration<A> setAutoDelimitReservedWords(final boolean autoEscapeReservedWords)
	{
		this.autoEscapeReservedWords = autoEscapeReservedWords;
		return this;
	}
	/**
	 * @param delimitColumnIdentifiers the delimitColumnIdentifiers to set
	 */
	public DbmsConfiguration<A> setDelimitColumnIdentifiers(boolean delimitColumnIdentifiers)
	{
		this.delimitColumnIdentifiers = delimitColumnIdentifiers;
		return this;
	}
	/**
	 * @param delimitAliases the delimitAliases to set
	 */
	public DbmsConfiguration<A> setDelimitAliases(boolean delimitAliases)
	{
		this.delimitAliases = delimitAliases;
		return this;
	}
	
	


}
