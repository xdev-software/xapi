
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

import static com.xdev.jadoth.sqlengine.SQL.Punctuation._;
import static com.xdev.jadoth.sqlengine.SQL.Punctuation.comma_;
import static com.xdev.jadoth.sqlengine.SQL.Punctuation.par;
import static com.xdev.jadoth.sqlengine.SQL.Punctuation.rap;

import java.util.ArrayList;

import com.xdev.jadoth.Jadoth;
import com.xdev.jadoth.sqlengine.SQL.INDEXTYPE;
import com.xdev.jadoth.sqlengine.internal.tables.SqlIndex;
import com.xdev.jadoth.sqlengine.internal.tables.SqlPrimaryKey;



/**
 * The Class IndexDefinition.
 */
public class IndexDefinition extends AbstractTableElementDefinition<IndexDefinition>
{

	/** The type. */
	protected final INDEXTYPE type;
	
	/** The columns. */
	protected ArrayList<Object> columns;


	/**
	 * Instantiates a new index definition.
	 * 
	 * @param name the name
	 * @param type the type
	 */
	public IndexDefinition(final String name, final INDEXTYPE type) {
		this(name, type, null);

	}
	
	/**
	 * Instantiates a new index definition.
	 * 
	 * @param name the name
	 * @param type the type
	 * @param columns the columns
	 */
	public IndexDefinition(final String name, final INDEXTYPE type, final ArrayList<Object> columns) {
		super(name);
		this.type = type;
		this.columns = columns;
	}

	/**
	 * Adds the column.
	 * 
	 * @param column the column
	 * @return the index definition
	 */
	public IndexDefinition addColumn(final Object column){
		this.columns.add(column);
		return this;
	}
	
	/**
	 * Adds the columns.
	 * 
	 * @param column the column
	 * @return the index definition
	 */
	public IndexDefinition addColumns(final Object[] column){
		for(Object c : column) {
			this.columns.add(c);
		}
		return this;
	}
	
	/**
	 * Sets the columns.
	 * 
	 * @param column the column
	 * @return the index definition
	 */
	public IndexDefinition setColumns(final Object[] column){
		this.columns = new ArrayList<Object>(column.length);
		for(Object c : column) {
			this.columns.add(c);
		}
		return this;
	}



	///////////////////////////////////////////////////////////////////////////
	// getters          //
	/////////////////////
	/**
	 * Gets the columns.
	 * 
	 * @return the columnList
	 */
	public ArrayList<Object> getColumns() {
		return columns;
	}
	
	/**
	 * Checks if is primary key.
	 * 
	 * @return true, if is primary key
	 */
	public boolean isPrimaryKey(){
		return this.type==INDEXTYPE.PRIMARYKEY;
	}




	///////////////////////////////////////////////////////////////////////////
	// setters          //
	/////////////////////
	/**
	 * Sets the columns.
	 * 
	 * @param columns the columns
	 * @return the index definition
	 */
	public IndexDefinition setColumns(final ArrayList<Object> columns) {
		this.columns = columns;
		return this;
	}




	/**
	 * @param sb
	 * @return
	 * @see com.xdev.jadoth.sqlengine.retrospection.definition.AbstractTableElementDefinition#assembleJavaSourceDefinition(java.lang.StringBuilder)
	 */
	@Override
	public StringBuilder assembleJavaSourceDefinition(StringBuilder sb) {
		if(sb == null) {
			sb = new StringBuilder(128);
		}
		super.assembleJavaSourceDefinition(sb);

		sb.append(this.type.getSqltableDefinitionMethodName());
		sb.append(par);
		sb.append(Jadoth.commaList((Object[])columns.toArray(new String[columns.size()])));
		sb.append(rap);

		if(this.nameInDb != null && !this.nameInDb.equals(this.name)) {
			sb.append(".setName(\"").append(this.nameInDb).append("\")");
		}
		return sb;
	}




	/**
	 * @return
	 * @see com.xdev.jadoth.sqlengine.retrospection.definition.AbstractTableElementDefinition#getDefinitionType()
	 */
	@Override
	protected Class<?> getDefinitionType() {
		return isPrimaryKey()?SqlPrimaryKey.class:SqlIndex.class;
	}



	/**
	 * @return
	 * @see com.xdev.jadoth.sqlengine.retrospection.definition.AbstractTableElementDefinition#toString()
	 */
	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder(1024).append(super.toString())
		.append(_).append(this.type)
		;
		sb.append(_).append(par);
		for(int i = 0, len = this.columns.size(); i < len; i++) {
			if(i > 0){
				sb.append(comma_);
			}
			sb.append(columns.get(i));
		}
		sb.append(rap);

		return sb.toString();
	}

}
