
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

import static com.xdev.jadoth.sqlengine.SQL.Punctuation.NEW_LINE;
import static com.xdev.jadoth.sqlengine.SQL.Punctuation.SPACE;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import com.xdev.jadoth.sqlengine.dbms.DbmsDMLAssembler;
import com.xdev.jadoth.sqlengine.dbms.standard.StandardDMLAssembler;




/**
 * The Class SqlClause.
 *
 * @author Thomas Muenz
 */
public abstract class SqlClause<E> extends QueryPart implements Iterable<E>
{
	///////////////////////////////////////////////////////////////////////////
	// constants        //
	/////////////////////	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8445250661518426043L;

	
	
	
	@SuppressWarnings("unchecked")
	public static <C extends SqlClause<?>> C copySqlClause(
		final C sqlClause, final HashMap<SqlClause<?>, SqlClause<?>> alreadyCopied
	)
	{
		if(sqlClause == null) return null;

		synchronized(alreadyCopied) {
			SqlClause<?> copiedSqlClause = alreadyCopied.get(sqlClause);
			if(copiedSqlClause == null){
				//clause has not been copied yet, so create and register copy.
				copiedSqlClause = sqlClause.copy(alreadyCopied); //pass backtracking map for internal use (for joins) 
				alreadyCopied.put(sqlClause, copiedSqlClause);
			}			
			return (C)copiedSqlClause;
		}
	}
	
	
	
	///////////////////////////////////////////////////////////////////////////
	// instance fields //
	////////////////////	
	/** The body. */
	protected final List<E> body;

	/** The body element seperator. */
	protected String bodyElementSeperator = NEW_LINE;

	/** The key word seperator. */
	protected String keyWordSeperator = SPACE;

	/** The indent first body element. */
	protected boolean indentFirstBodyElement = false; //SET needs true because of new line after keyword

	/** The indentation allowed. */
	protected boolean indentationAllowed = true; //ListClauses like GROUP and ORDER disallow this



	///////////////////////////////////////////////////////////////////////////
	// constructors //
	/////////////////

	/**
	 * Instantiates a new sql clause.
	 */
	public SqlClause() {
		super();
		this.body = new ArrayList<E>();
	}
	/**
	 * Instantiates a new sql clause.
	 *
	 * @param firstElement the first element
	 */
	public SqlClause(final E firstElement) {
		this();
		if(firstElement != null) {
			this.body.add(firstElement);
		}
	}	
	protected SqlClause(final SqlClause<E> copySource)
	{
		super();
		this.body = new ArrayList<E>(copySource.body);
		this.bodyElementSeperator = copySource.bodyElementSeperator;
		this.keyWordSeperator = copySource.keyWordSeperator;
		this.indentFirstBodyElement = copySource.indentFirstBodyElement;
		this.indentationAllowed = copySource.indentationAllowed;
	}



	///////////////////////////////////////////////////////////////////////////
	// getters          //
	/////////////////////

	/**
	 * Gets the body.
	 *
	 * @return the body
	 */
	public List<E> getBody() {
		return this.body;
	}


	/**
	 * Gets the body element seperator.
	 *
	 * @param singleLineMode the single line mode
	 * @return the body element seperator
	 */
	public String getBodyElementSeperator(final boolean singleLineMode) {
		return singleLineMode?" ":this.bodyElementSeperator;
	}

	/**
	 * Gets the body element seperator.
	 *
	 * @return the body element seperator
	 */
	public String getBodyElementSeperator() {
		return this.bodyElementSeperator;
	}

	/**
	 * Checks if is indent first body element.
	 *
	 * @return the indentFirstBodyElement
	 */
	public boolean isIndentFirstBodyElement() {
		return this.indentFirstBodyElement;
	}

	/**
	 * Checks if is indentation allowed.
	 *
	 * @return the indentationAllowed
	 */
	public boolean isIndentationAllowed() {
		return this.indentationAllowed;
	}

	/**
	 * Gets the key word seperator.
	 *
	 * @return the keyWordSeperator
	 */
	public String getKeyWordSeperator() {
		return this.keyWordSeperator;
	}

	@Override
	public Iterator<E> iterator()
	{
		return this.body.iterator();
	}
	
	/**
	 * @param dmlAssembler
	 * @param sb
	 * @param indentLevel
	 * @param flags
	 * @return
	 * @see com.xdev.jadoth.sqlengine.internal.QueryPart#assemble(com.xdev.jadoth.sqlengine.dbms.DbmsDMLAssembler, java.lang.StringBuilder, int, int)
	 */
	@Override
	protected StringBuilder assemble(final DbmsDMLAssembler<?> dmlAssembler, final StringBuilder sb, final int indentLevel, final int flags)
	{		
		return dmlAssembler.assembleSqlClause(this, sb, indentLevel, flags);		
	}

	/**
	 * @return
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return this.assemble(
			StandardDMLAssembler.getSingletonStandardDMLAssembler(),
			new StringBuilder(defaultClauseStringBuilderLength), 0, 0
		).toString();
	}


	/**
	 * Checks if is empty.
	 *
	 * @return true, if is empty
	 */
	public boolean isEmpty(){
		return this.body==null || this.body.size() == 0;
	}

	/**
	 * Gets the element count.
	 *
	 * @return the element count
	 */
	public int getElementCount(){
		return this.body==null?0:this.body.size();
	}

	/**
	 * Gets the element.
	 *
	 * @param i the i
	 * @return the element
	 */
	public Object getElement(final int i){
		return this.body==null?null:this.body.get(i);
	}

	/**
	 * Gets the last element.
	 *
	 * @return the last element
	 */
	public Object getLastElement()
	{
		if(this.body == null || this.body.size() == 0) return null;

		return this.body.get(this.body.size()-1);
	}

	/**
	 * Sets the body element seperator.
	 *
	 * @param bodyElementSeperator the new body element seperator
	 */
	public void setBodyElementSeperator(final String bodyElementSeperator) {
		this.bodyElementSeperator = bodyElementSeperator;
	}

	
	/**
	 * @return
	 */
	@Override
	public abstract SqlClause<E> copy();
	
	protected abstract SqlClause<E> copy(final HashMap<SqlClause<?>, SqlClause<?>> alreadyCopied);

}
