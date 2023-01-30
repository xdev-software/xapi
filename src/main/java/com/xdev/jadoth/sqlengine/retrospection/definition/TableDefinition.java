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
package com.xdev.jadoth.sqlengine.retrospection.definition;

import static com.xdev.jadoth.sqlengine.SQL.Punctuation.NEW_LINE;
import static com.xdev.jadoth.sqlengine.SQL.Punctuation._;
import static com.xdev.jadoth.sqlengine.SQL.Punctuation.par;
import static com.xdev.jadoth.sqlengine.SQL.Punctuation.rap;

import java.util.ArrayList;





/**
 * The Class TableDefinition.
 */
public class TableDefinition
{
	
	/** The schema. */
	protected String schema;
	
	/** The name. */
	protected String name;
	
	/** The columns. */
	protected ArrayList<ColumnDefinition> columns;
	
	/** The indices. */
	protected ArrayList<IndexDefinition> indices;

	/** The schema in db. */
	protected String schemaInDb;
	
	/** The name in db. */
	protected String nameInDb;


	///////////////////////////////////////////////////////////////////////////
	// constructors //
	/////////////////
	/**
	 * Instantiates a new table definition.
	 * 
	 * @param schema the schema
	 * @param name the name
	 */
	public TableDefinition(final String schema, final String name) {
		super();
		this.schema = schema;
		this.name = name;
		this.columns = new ArrayList<ColumnDefinition>();
		this.indices = new ArrayList<IndexDefinition>();
	}

	/**
	 * Instantiates a new table definition.
	 * 
	 * @param schema the schema
	 * @param name the name
	 * @param columns the columns
	 * @param indices the indices
	 */
	public TableDefinition(final String schema, final String name, final ArrayList<ColumnDefinition> columns, final ArrayList<IndexDefinition> indices) {
		super();
		this.schema = schema;
		this.name = name;
		this.schemaInDb = schema;
		this.nameInDb = name;
		this.columns = columns;
		this.indices = indices;

		if(columns != null){
			for(ColumnDefinition c : columns) {
				c.setOwner(this);
			}
		}
		if(indices != null){
			for(IndexDefinition i : indices) {
				i.setOwner(this);
			}
		}
	}



	///////////////////////////////////////////////////////////////////////////
	// getters          //
	/////////////////////
	/**
	 * Gets the schema.
	 * 
	 * @return the schema
	 */
	public String getSchema() {
		return schema;
	}
	
	/**
	 * Gets the name.
	 * 
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Gets the schema in db.
	 * 
	 * @return the schemaInDb
	 */
	public String getSchemaInDb() {
		return schemaInDb != null ?schemaInDb :schema;
	}

	/**
	 * Gets the name in db.
	 * 
	 * @return the nameInDb
	 */
	public String getNameInDb() {
		return nameInDb != null ?nameInDb :name;
	}
	
	/**
	 * Gets the columns.
	 * 
	 * @return the columns
	 */
	public ArrayList<ColumnDefinition> getColumns() {
		return columns;
	}
	
	/**
	 * Gets the indices.
	 * 
	 * @return the indices
	 */
	public ArrayList<IndexDefinition> getIndices() {
		return indices;
	}
	
	/**
	 * Gets the qualified tablename.
	 * 
	 * @return the qualified tablename
	 */
	public String getQualifiedTablename(){
		return this.schema+"."+this.name;
	}


	///////////////////////////////////////////////////////////////////////////
	// setters          //
	/////////////////////
	/**
	 * Sets the schema.
	 * 
	 * @param schema the schema to set
	 * @return the table definition
	 */
	public TableDefinition setSchema(final String schema) {
		this.schema = schema;
		return this;
	}

	/**
	 * Sets the name.
	 * 
	 * @param name the name to set
	 * @return the table definition
	 */
	public TableDefinition setName(final String name) {
		this.name = name;
		return this;
	}
	
	/**
	 * Sets the schema in db.
	 * 
	 * @param schemaInDb the schemaInDb to set
	 * @return the table definition
	 */
	public TableDefinition setSchemaInDb(final String schemaInDb)
	{
		this.schemaInDb = schemaInDb;
		return this;
	}
	
	/**
	 * Sets the name in db.
	 * 
	 * @param nameInDb the nameInDb to set
	 * @return the table definition
	 */
	public TableDefinition setNameInDb(final String nameInDb)
	{
		this.nameInDb = nameInDb;
		return this;
	}
	
	/**
	 * Checks for indices.
	 * 
	 * @return true, if successful
	 */
	public boolean hasIndices(){
		return this.indices.size() > 0;
	}
	
	/**
	 * Checks for primary key.
	 * 
	 * @return true, if successful
	 */
	public boolean hasPrimaryKey(){
		if(indices == null) return false;
		for(IndexDefinition i : indices) {
			if(i.isPrimaryKey()) return true;
		}
		return false;
	}
	
	/**
	 * Checks for non pkey indices.
	 * 
	 * @return true, if successful
	 */
	public boolean hasNonPkeyIndices(){
		if(indices == null) return false;
		for(IndexDefinition i : indices) {
			if(!i.isPrimaryKey()) return true;
		}
		return false;
	}
	
	/**
	 * Checks for unique column.
	 * 
	 * @return true, if successful
	 */
	public boolean hasUniqueColumn(){
		if(columns == null) return false;
		for(ColumnDefinition c : columns) {
			if(c.isUnique()) return true;
		}
		return false;
	}
	
	/**
	 * Checks for not null column.
	 * 
	 * @return true, if successful
	 */
	public boolean hasNotNullColumn(){
		if(columns == null) return false;
		for(ColumnDefinition c : columns) {
			if(c.isNotNull()) return true;
		}
		return false;
	}



	///////////////////////////////////////////////////////////////////////////
	// declared methods //
	/////////////////////
	/**
	 * Determine padding spaces.
	 * 
	 * @param <D> the generic type
	 * @param elements the elements
	 * @return the int
	 */
	protected static <D extends AbstractTableElementDefinition<D>>
	int determinePaddingSpaces(final ArrayList<D> elements){
		int currentLongestVariableName = 0;
		for(D e : elements) {
			final int currentLength = e.getDefinitionType().getSimpleName().length()+1+e.getName().length();
			if(currentLength > currentLongestVariableName){
				currentLongestVariableName = currentLength;
			}
		}
		return currentLongestVariableName;
	}

	/**
	 * Determine padding spaces columns.
	 * 
	 * @return the int
	 */
	public int determinePaddingSpacesColumns(){
		return determinePaddingSpaces(this.columns);
	}
	
	/**
	 * Determine padding spaces indices.
	 * 
	 * @return the int
	 */
	public int determinePaddingSpacesIndices(){
		return determinePaddingSpaces(this.indices);
	}


	/**
	 * @return
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder(2048)
		.append(getQualifiedTablename()).append(_)
		.append(par).append(NEW_LINE);
		for(ColumnDefinition c : this.columns) {
			sb.append(c.toString()).append(NEW_LINE);
		}
		for(IndexDefinition i : this.indices) {
			sb.append(i.toString()).append(NEW_LINE);
		}
		sb.append(rap);
		return sb.toString();
	}




}
