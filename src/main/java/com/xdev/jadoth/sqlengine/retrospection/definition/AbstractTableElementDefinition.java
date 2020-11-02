
package com.xdev.jadoth.sqlengine.retrospection.definition;

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

import static com.xdev.jadoth.codegen.java.Java.Lang.final_;
import static com.xdev.jadoth.codegen.java.Java.Lang.public_;
import static com.xdev.jadoth.codegen.java.Java.Punctuation._is_;
import static com.xdev.jadoth.sqlengine.SQL.Punctuation._;
import static com.xdev.jadoth.sqlengine.SQL.Punctuation.par;
import static com.xdev.jadoth.sqlengine.SQL.Punctuation.rap;

import com.xdev.jadoth.codegen.java.JavaSourceDefinable;
import com.xdev.jadoth.sqlengine.util.SqlEngineLabels;




/**
 * The Class AbstractTableElementDefinition.
 * 
 * @param <D> the generic type
 */
public abstract class AbstractTableElementDefinition<D extends AbstractTableElementDefinition<D>>
extends SqlEngineLabels implements JavaSourceDefinable
{
	
	/** The Constant paddingChar. */
	protected static final char paddingChar = ' ';

	// (06.02.2010)NOTE: not final anymore because RetrospectionIdentifierMapper has to be able to set it
	/** The name. */
	protected String name;
	
	/** The owner. */
	protected TableDefinition owner;
	
	/** The padding spaces. */
	protected int paddingSpaces = 0;
	
	/** The name in db. */
	protected String nameInDb = null;




	/**
	 * Instantiates a new abstract table element definition.
	 * 
	 * @param name the name
	 */
	public AbstractTableElementDefinition(final String name) {
		super();
		this.name = name;
		this.nameInDb = name;
	}


	///////////////////////////////////////////////////////////////////////////
	// getters          //
	/////////////////////
	/**
	 * Gets the name.
	 * 
	 * @return the name
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Gets the name in db.
	 * 
	 * @return the nameInDb
	 */
	public String getNameInDb() {
		return this.nameInDb != null ?this.nameInDb :this.name;
	}

	/**
	 * Gets the owner.
	 * 
	 * @return the owner
	 */
	public TableDefinition getOwner() {
		return this.owner;
	}

	/**
	 * Gets the padding spaces.
	 * 
	 * @return the paddingSpaces
	 */
	public int getPaddingSpaces() {
		return this.paddingSpaces;
	}



	/**
	 * Gets the definition type.
	 * 
	 * @return the definition type
	 */
	protected abstract Class<?> getDefinitionType();




	///////////////////////////////////////////////////////////////////////////
	// setters          //
	/////////////////////
	/**
	 * Sets the owner.
	 * 
	 * @param owner the owner to set
	 * @return the d
	 */
	@SuppressWarnings("unchecked")
	public D setOwner(final TableDefinition owner) {
		this.owner = owner;
		return (D)this;
	}
	
	/**
	 * Sets the padding spaces.
	 * 
	 * @param paddingSpaces the paddingSpaces to set
	 */
	public void setPaddingSpaces(final int paddingSpaces) {
		this.paddingSpaces = paddingSpaces;
	}

	/**
	 * Sets the name.
	 * 
	 * @param name the name to set
	 * @return the d
	 */
	@SuppressWarnings("unchecked")
	public D setName(final String name)
	{
		this.name = name;
		return (D)this;
	}

	/**
	 * Sets the name in db.
	 * 
	 * @param nameInDb the nameInDb to set
	 * @return the d
	 */
	@SuppressWarnings("unchecked")
	public D setNameInDb(final String nameInDb)
	{
		this.nameInDb = nameInDb;
		return (D)this;
	}


	/**
	 * Adds the variablename alignment.
	 * 
	 * @param sb the sb
	 * @return the string builder
	 */
	protected StringBuilder addVariablenameAlignment(final StringBuilder sb){
		if(sb == null) return null;
		int paddingCount = this.paddingSpaces;
		while(paddingCount-->0){
			sb.append(paddingChar);
		}
		return sb;
	}


	/**
	 * Assemble java source definition.
	 * 
	 * @param sb the sb
	 * @param paddingSpaces the padding spaces
	 * @return the string builder
	 */
	public StringBuilder assembleJavaSourceDefinition(final StringBuilder sb, final int paddingSpaces) {
		final int oldPaddingSpaces = this.paddingSpaces;
		this.setPaddingSpaces(paddingSpaces - this.getDefinitionType().getSimpleName().length() - 1 - this.getName().length());
		final StringBuilder returnSB = this.assembleJavaSourceDefinition(sb);
		this.setPaddingSpaces(oldPaddingSpaces);
		return returnSB;
	}

	/**
	 * @param sb
	 * @return
	 * @see com.xdev.jadoth.codegen.java.JavaSourceDefinable#assembleJavaSourceDefinition(java.lang.StringBuilder)
	 */
	@Override
	public StringBuilder assembleJavaSourceDefinition(StringBuilder sb) {
		if(sb == null) {
			sb = new StringBuilder(128);
		}
		sb.append(public_).append(final_).append(this.getDefinitionType().getSimpleName());
		sb.append(_).append(this.getName());
		sb.append(_is_);
		this.addVariablenameAlignment(sb);
		return sb;
	}




	/**
	 * @return
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() 
	{
		final StringBuilder sb = new StringBuilder(1024)
		.append(this.getClass().getSimpleName()).append(": ")
		.append(par).append(this.getOwner().getQualifiedTablename()).append(rap)
		.append(_).append(this.name);
		return sb.toString();
	}

}
