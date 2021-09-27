
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

import static com.xdev.jadoth.sqlengine.SQL.Punctuation.TAB;
import static com.xdev.jadoth.sqlengine.SQL.Punctuation._;
import static com.xdev.jadoth.sqlengine.SQL.Punctuation._eq_;
import static com.xdev.jadoth.sqlengine.SQL.Punctuation.comma_;
import static com.xdev.jadoth.sqlengine.SQL.Punctuation.par;
import static com.xdev.jadoth.sqlengine.SQL.Punctuation.quote;
import static com.xdev.jadoth.sqlengine.SQL.Punctuation.rap;
import static com.xdev.jadoth.sqlengine.SQL.Punctuation.scol;

import com.xdev.jadoth.sqlengine.SQL;
import com.xdev.jadoth.sqlengine.SQL.DATATYPE;
import com.xdev.jadoth.sqlengine.dbms.DbmsAdaptor;
import com.xdev.jadoth.sqlengine.exceptions.SQLEngineInvalidIdentifier;
import com.xdev.jadoth.sqlengine.internal.interfaces.TableExpression;
import com.xdev.jadoth.sqlengine.internal.tables.SqlTableIdentity;
import com.xdev.jadoth.sqlengine.retrospection.definition.ColumnDefinition;


/**
 * The Class SqlField.
 * 
 * @author Thomas Muenz
 */
public class SqlField extends SqlColumn
{
	///////////////////////////////////////////////////////////////////////////
	// instance fields //
	////////////////////

	/**
	 * 
	 */
	private static final long serialVersionUID = 4204968807566003757L;

	//SQL definition stuff
	/** The type. */
	private DATATYPE type;
	
	/** The default value. */
	private Object defaultValue;
	
	/** The type length. */
	private int typeLength;
	
	/** The precision. */
	private Integer precision;
	
	/** The scale. */
	private Integer scale;
	
	/** The not null. */
	private boolean notNull;
	
	/** The unique. */
	private boolean unique;

	//hashing
	/** The hash int. */
	protected int hashInt;
	
	/** The hash string. */
	protected String hashString;

	//additional fields
	/** The custom string. */
	private String customString;



	///////////////////////////////////////////////////////////////////////////
	// constructors //
	/////////////////
	/**
	 * Instantiates a new sql field.
	 * 
	 * @param fieldname the fieldname
	 * @param type the type
	 * @param typeLength the type length
	 * @param precision the precision
	 * @param scale the scale
	 * @param notNull the not null
	 * @param unique the unique
	 * @param defaultValue the default value
	 * @throws SQLEngineInvalidIdentifier the sQL engine invalid identifier
	 */
	public SqlField(
		final String fieldname,
		final DATATYPE type,
		final int typeLength,
		final Integer precision,
		final Integer scale,
		final boolean notNull,
		final boolean unique,
		final Object defaultValue
	)
		throws SQLEngineInvalidIdentifier
	{
		super(fieldname);
		this.type = type;
		this.notNull = notNull;
		this.unique = unique;
		this.defaultValue = defaultValue;
		this.typeLength = typeLength;
		this.resetHash();
	}



	///////////////////////////////////////////////////////////////////////////
	// getters          //
	/////////////////////
	/**
	 * Gets the custom string.
	 * 
	 * @return the custom string
	 */
	public String getCustomString() {
		return this.customString;
	}
	
	/**
	 * Gets the type.
	 * 
	 * @return the type
	 */
	public final DATATYPE getType() {
		return this.type;
	}
	
	/**
	 * Checks if is not null.
	 * 
	 * @return true, if is not null
	 */
	public final boolean isNotNull() {
		return this.notNull;
	}
	
	/**
	 * Checks if is unique.
	 * 
	 * @return true, if is unique
	 */
	public final boolean isUnique() {
		return this.unique;
	}
	
	/**
	 * Gets the name.
	 * 
	 * @return the name
	 */
	public final String getName() {
		return this.getExpression();
	}
	
	/**
	 * Gets the default value.
	 * 
	 * @return the default value
	 */
	public final Object getDefaultValue() {
		return this.defaultValue;
	}
	
	/**
	 * Gets the compare string.
	 * 
	 * @return the compare string
	 */
	public final String getCompareString() {
		if(this.hashString == null) {
			this.buildHash();
		}
		return this.hashString;
	}
	
	/**
	 * Gets the type length.
	 * 
	 * @return the type length
	 */
	public final int getTypeLength() {
		return this.typeLength;
	}


	///////////////////////////////////////////////////////////////////////////
	// setters          //
	/////////////////////
	/**
	 * Sets the custom string.
	 * 
	 * @param customString the custom string
	 * @return the sql field
	 */
	public SqlField setCustomString(final String customString) {
		this.customString = customString;
		return this;
	}
	
	/**
	 * Sets the not null.
	 * 
	 * @param notNull the not null
	 * @return the sql field
	 */
	public final SqlField setNotNull(final boolean notNull) {
		this.notNull = notNull;
		this.resetHash();
		return this;
	}
	
	/**
	 * @param owner
	 * @see com.xdev.jadoth.sqlengine.internal.SqlColumn#setOwner(com.xdev.jadoth.sqlengine.internal.interfaces.TableExpression)
	 */
	@Override
	public final void setOwner(final TableExpression owner) {
		super.setOwner(owner);
		this.resetHash();
	}
	
	/**
	 * Sets the name.
	 * 
	 * @param name the name
	 * @return the sql field
	 */
	public final SqlField setName(final String name) 
	{
		this.expression = name;
		this.setDelimited(needsDelimiting(name));
		this.resetHash();
		return this;
	}	
	
	@Override
	public SqlField setDelimited(final boolean delimited)
	{
		super.setDelimited(delimited);
		return this;
	}
	



	/**
	 * Sets the unique.
	 * 
	 * @param unique the new unique
	 */
	public final void setUnique(final boolean unique) {
		this.unique = unique;
	}




	///////////////////////////////////////////////////////////////////////////
	// override methods //
	/////////////////////
	/**
	 * @param obj
	 * @return
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public final boolean equals(final Object obj) {
		if(obj == null || !(obj instanceof SqlField)) return false;
		return this.hashCode() == ((SqlField)obj).hashCode();
	}

	/**
	 * @return
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public final int hashCode() {
		if(this.hashInt == 0) {
			this.buildHash();
		}
		return this.hashInt;
	}



	///////////////////////////////////////////////////////////////////////////
	// declared methods //
	/////////////////////
	/**
	 * Assemble type.
	 * 
	 * @param sb the sb
	 * @param dbmsAdaptor the dbms adaptor
	 * @return the string builder
	 */
	public final StringBuilder assembleType(StringBuilder sb, final DbmsAdaptor<?> dbmsAdaptor) 
	{
		if(sb == null) {
			sb = new StringBuilder(64);
		}
		final TableExpression owner = this.getOwner();
		sb.append(dbmsAdaptor == null
			?this.type.toDdlString()
			:dbmsAdaptor.getDdlMapper().getDataTypeDDLString(
				this.type, (SqlTableIdentity)(owner instanceof SqlTableIdentity ?owner :null)
			)
		);
		if(this.type.isLengthed()/* && this.typeLength > 0*/) {
			sb.append(par).append(this.typeLength).append(rap);
		}
		else if(this.type.isPrecisioned() || this.type.isScaled()){
			if(this.precision != null || this.scale != null){
				sb.append(par);
				if(this.precision != null){
					sb.append(this.precision);
					if(this.scale != null){
						sb.append(comma_).append(this.scale);
					}
				}
				else if(this.scale != null){
					// i.e.: "myNumColumn NUMERIC(*,2)"
					sb.append("*").append(comma_).append(this.scale);
				}
				sb.append(rap);
			}
		}
		return sb;
	}

	/**
	 * Gets the creation check string.
	 * 
	 * @param dbmsAdaptor the dbms adaptor
	 * @return the creation check string
	 */
	public final String getCreationCheckString(final DbmsAdaptor<?> dbmsAdaptor)
	{
		final StringBuilder sb = new StringBuilder(256).append(this.expression);
		sb.append(TAB);
		this.assembleType(sb, dbmsAdaptor);
		sb.append(TAB).append(this.notNull);
		sb.append(TAB).append(this.unique);
		sb.append(TAB).append(this.defaultValue);
		sb.append(TAB);
		return sb.toString();
	}

	/**
	 * Builds the hash.
	 * 
	 * @return the string
	 */
	public final String buildHash() {
		final StringBuilder sb = new StringBuilder(256).append(this.expression);
		sb.append(TAB).append(this.type);
		sb.append(TAB).append(this.getOwner());
		sb.append(TAB).append(this.notNull);
		sb.append(TAB).append(this.unique);
		this.hashString = sb.toString();
		this.hashInt = this.hashString.hashCode();
		return this.hashString;
	}

	/**
	 * Reset hash.
	 */
	public final void resetHash() {
		this.hashString = null;
		this.hashInt = 0;
	}

	/**
	 * Assemble table class code.
	 * 
	 * @param sb the sb
	 * @param paddingSpaces the padding spaces
	 * @return the string builder
	 */
	public final StringBuilder assembleTableClassCode(StringBuilder sb, int paddingSpaces) {
		if(sb == null) {
			sb = new StringBuilder(64);
		}
		boolean needsComma = false;
		if(paddingSpaces < 0) {
			paddingSpaces = 0;
		}

		sb.append("public final "+SqlField.class.getSimpleName()+" ");
		sb.append(this.expression);
		for(int i = 0; i < paddingSpaces; i++) {
			sb.append(_);
		}
		sb.append(_eq_);
		sb.append(this.type);
		sb.append(par);
		if(this.typeLength > 0) {
			sb.append(this.typeLength);
			needsComma = true;
		}
		if(this.notNull) {
			if(needsComma) {
				sb.append(comma_);
			}
			sb.append("NOT_NULL");
			needsComma = true;
		}
		if(this.unique) {
			if(needsComma) {
				sb.append(comma_);
			}
			sb.append("UNIQUE");
			needsComma = true;
		}
		if(this.defaultValue != null) {
			if(needsComma) {
				sb.append(comma_);
			}
			sb.append(quote);
			final String defaultString = this.defaultValue.toString();

			if(defaultString.equals("false")) {
				sb.append("1");
			}
			else if(defaultString.equals("true")) {
				sb.append("0");
			}
			else {
				sb.append(SQL.util.escapeIfNecessary(defaultString));
			}
			sb.append(quote);
			needsComma = true;
		}
		sb.append(rap);
		sb.append(scol);

		return sb;
	}

	/**
	 * To column definition.
	 * 
	 * @return the column definition
	 */
	public ColumnDefinition toColumnDefinition(){
		final Object def = this.getDefaultValue();
		return new ColumnDefinition(
			this.getColumnName(),
			this.getType(),
			this.getTypeLength(),
			this.isNotNull(),
			this.isUnique(),
			def==null?null:def.toString()
		);
	}

}
