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

import static com.xdev.jadoth.codegen.java.Java.Lang.$false;
import static com.xdev.jadoth.codegen.java.Java.Lang.$true;
import static com.xdev.jadoth.sqlengine.SQL.LANG.DEFAULT;
import static com.xdev.jadoth.sqlengine.SQL.LANG.NOT_NULL;
import static com.xdev.jadoth.sqlengine.SQL.LANG.UNIQUE;
import static com.xdev.jadoth.sqlengine.SQL.Punctuation._;
import static com.xdev.jadoth.sqlengine.SQL.Punctuation.cma;
import static com.xdev.jadoth.sqlengine.SQL.Punctuation.par;
import static com.xdev.jadoth.sqlengine.SQL.Punctuation.quote;
import static com.xdev.jadoth.sqlengine.SQL.Punctuation.rap;

import java.lang.reflect.Field;

import com.xdev.jadoth.lang.reflection.JaReflect;
import com.xdev.jadoth.sqlengine.SQL;
import com.xdev.jadoth.sqlengine.SQL.DATATYPE;
import com.xdev.jadoth.sqlengine.internal.SqlField;



/**
 * The Class ColumnDefinition.
 */
public class ColumnDefinition extends AbstractTableElementDefinition<ColumnDefinition>
{
	
	/** The Constant VARIABLENAME_NOT_NULL. */
	public static final String VARIABLENAME_NOT_NULL;
	
	/** The Constant VARIABLENAME_UNIQUE. */
	public static final String VARIABLENAME_UNIQUE;
	static {
		final Field[] sqlKeywords = SQL.LANG.class.getDeclaredFields();
		VARIABLENAME_NOT_NULL = JaReflect.getMemberByLabel(LABEL_NOTNULL, sqlKeywords).getName();
		VARIABLENAME_UNIQUE = JaReflect.getMemberByLabel(LABEL_UNIQUE, sqlKeywords).getName();
	}

	/** The type. */
	protected final DATATYPE type;
	
	/** The type length. */
	protected final int typeLength;
	
	/** The not null. */
	protected final boolean notNull;
	
	/** The unique. */
	protected final boolean unique;
	
	/** The default value. */
	protected final String defaultValue;





	/**
	 * Instantiates a new column definition.
	 * 
	 * @param name the name
	 * @param type the type
	 * @param typeLength the type length
	 * @param notNull the not null
	 * @param unique the unique
	 * @param defaultValue the default value
	 */
	public ColumnDefinition(final String name, final DATATYPE type, final int typeLength, final boolean notNull, final boolean unique, final String defaultValue) {
		super(name);
		this.typeLength = typeLength;
		this.type = type;
		this.notNull = notNull;
		this.unique = unique;
		this.defaultValue = defaultValue;
	}




	///////////////////////////////////////////////////////////////////////////
	// getters          //
	/////////////////////
	/**
	 * Gets the type length.
	 * 
	 * @return the typeLength
	 */
	public int getTypeLength() {
		return this.typeLength;
	}
	
	/**
	 * Checks if is not null.
	 * 
	 * @return the notNull
	 */
	public boolean isNotNull() {
		return this.notNull;
	}
	
	/**
	 * Checks if is unique.
	 * 
	 * @return the unique
	 */
	public boolean isUnique() {
		return this.unique;
	}
	
	/**
	 * Gets the default value.
	 * 
	 * @return the defaultValue
	 */
	public String getDefaultValue() {
		return this.defaultValue;
	}



	///////////////////////////////////////////////////////////////////////////
	// setters          //
	/////////////////////




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

		boolean needsComma = false;
		sb.append(this.type);
		sb.append(par);
		
		if(this.type != null && this.type.isLengthed()){
			sb.append(this.typeLength);
			needsComma = true;
		}
		if(this.notNull) {
			if(needsComma) {
				sb.append(cma).append(_);
			}
			sb.append(VARIABLENAME_NOT_NULL);
			needsComma = true;
		}
		if(this.unique) {
			if(needsComma) {
				sb.append(cma).append(_);
			}
			sb.append(VARIABLENAME_UNIQUE);
			needsComma = true;
		}
		if(this.defaultValue != null) {
			if(needsComma) {
				sb.append(cma).append(_);
			}
			/* Escape all values, even numbers, for two reasons:
			 * 1.) The method parameters are of type String, not Object, so they must be Strings
			 * 2.) DDL default values are always Strings from a reflection point of view.
			 */
			sb.append(quote);
			String defaultString = this.defaultValue.toString();

			if(this.type != null && this.type.isLiteral()){
				SQL.util.assembleEscape(sb, defaultString.replaceAll("\n", "\\\\n"));
			}
			else {
				defaultString = defaultString.trim();
				if(this.type == SQL.DATATYPE.BOOLEAN){
					if(defaultString.equals($false)) {
						sb.append("1");
					}
					else if(defaultString.equals($true)) {
						sb.append("0");
					}
				}
				else {
					sb.append(SQL.util.escapeIfNecessary(defaultString));
				}
			}

			sb.append(quote);
			needsComma = true;
		}
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
		return SqlField.class;
	}


	/**
	 * @return
	 * @see com.xdev.jadoth.sqlengine.retrospection.definition.AbstractTableElementDefinition#toString()
	 */
	@Override
	public String toString() 
	{
		final StringBuilder sb = new StringBuilder(1024)
		.append(super.toString())
		.append(_).append(this.type).append(par).append(this.typeLength).append(rap);
		if(this.notNull){
			sb.append(_).append(NOT_NULL);
		}
		if(this.unique){
			sb.append(_).append(UNIQUE);
		}
		if(this.defaultValue != null){
			sb.append(_).append(DEFAULT).append(_).append(this.defaultValue);
		}
		return sb.toString();
	}

}
