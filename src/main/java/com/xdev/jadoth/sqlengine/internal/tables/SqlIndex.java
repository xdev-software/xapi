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
package com.xdev.jadoth.sqlengine.internal.tables;

import static com.xdev.jadoth.sqlengine.SQL.LANG.CREATE;
import static com.xdev.jadoth.sqlengine.SQL.LANG.DROP;
import static com.xdev.jadoth.sqlengine.SQL.LANG.INDEX;
import static com.xdev.jadoth.sqlengine.SQL.LANG.ON;
import static com.xdev.jadoth.sqlengine.SQL.LANG.TABLE;
import static com.xdev.jadoth.sqlengine.SQL.LANG._ON_;
import static com.xdev.jadoth.sqlengine.SQL.Punctuation.TAB;
import static com.xdev.jadoth.sqlengine.SQL.Punctuation._;
import static com.xdev.jadoth.sqlengine.SQL.Punctuation._eq_;
import static com.xdev.jadoth.sqlengine.SQL.Punctuation.comma_;
import static com.xdev.jadoth.sqlengine.SQL.Punctuation.par;
import static com.xdev.jadoth.sqlengine.SQL.Punctuation.rap;
import static com.xdev.jadoth.sqlengine.SQL.Punctuation.scol;

import java.util.ArrayList;
import java.util.List;

import com.xdev.jadoth.Jadoth;
import com.xdev.jadoth.sqlengine.SQL;
import com.xdev.jadoth.sqlengine.SQL.INDEXTYPE;
import com.xdev.jadoth.sqlengine.retrospection.definition.IndexDefinition;




/**
 * The Class SqlIndex.
 *
 * @author Thomas Muenz
 */
public class SqlIndex implements Iterable<Object>
{

	/** The Constant DROP_INDEX_. */
	protected static final String DROP_INDEX_ = DROP+_+INDEX+_;

	/** The Constant _ON_TABLE_. */
	protected static final String _ON_TABLE_ = _+ON+_+TABLE+_;


	/** The name. */
	protected String name;

	/** The owner. */
	protected SqlTableIdentity owner;

	/** The type. */
	protected INDEXTYPE type;

	/** The column list. */
	protected ArrayList<Object> columnList;

	/** The hash string. */
	protected String hashString;

	/** The hash int. */
	protected int hashInt;

	/**
	 * Instantiates a new sql index.
	 *
	 * @param name the name
	 * @param owner the owner
	 * @param type the type
	 * @param columnList the column list
	 */
	public SqlIndex(final String name, final SqlTableIdentity owner, final INDEXTYPE type, final Object... columnList) {
		super();
		this.name = name;
		this.owner = owner;
		this.type = type;
		this.columnList = new ArrayList<Object>(columnList.length);
		for (final Object object : columnList) {
			this.columnList.add(object);
		}
		this.resetHash();
	}

	/**
	 * Instantiates a new sql index.
	 *
	 * @param type the type
	 * @param columnList the column list
	 */
	public SqlIndex(final INDEXTYPE type, final Object... columnList) {
		this(null, null, type, columnList);
	}


	/**
	 * Adds the column.
	 *
	 * @param column the column
	 */
	public void addColumn(final Object column) {
		if(this.columnList.contains(column) || this.columnList.contains(SQL.util.getColumnName(column))) {
			return;
		}
		this.columnList.add(column);
	}

	/**
	 * CREAT e_ index.
	 *
	 * @return the string
	 */
	public String CREATE_INDEX()
	{
		final StringBuilder sb = new StringBuilder(256);

		sb.append(CREATE).append(_);
		if(this.type != null && this.type != SQL.INDEXTYPE.NORMAL) {
			sb.append(this.type.toDdlString()).append(_);
		}
		//explicitely the keyword String "INDEX" and NOT the INDEXTYPE in case it gets renamed
		sb.append(INDEX).append(_).append(this.name).append(_ON_).append(this.owner).append(_).append(par);
		this.assembleColumnListString(sb);
		sb.append(rap);
		return sb.toString();
	}

	/**
	 * DRO p_ index.
	 *
	 * @return the string
	 */
	public String DROP_INDEX(){
		return new StringBuilder(128).append(DROP_INDEX_).append(this.name).append(_ON_TABLE_).append(this.owner).toString();
	}




	/**
	 * Assemble table class code.
	 *
	 * @param sb the sb
	 * @return the string builder
	 */
	public StringBuilder assembleTableClassCode(StringBuilder sb)
	{
		if(sb == null) {
			sb = new StringBuilder(128);
		}
		sb.append("public final "+SqlIndex.class.getSimpleName());
		sb.append(this.assembleName());
		sb.append(_eq_);
		sb.append(this.type.getSqltableDefinitionMethodName());
		sb.append(par);
		sb.append(Jadoth.commaList((Object[])this.columnList.toArray(new String[this.columnList.size()])));
		sb.append(rap);
		sb.append(scol);

		return sb;
	}


	/**
	 * Assemble name.
	 *
	 * @return the string
	 */
	public String assembleName() {
		if(this.name == null) {
			this.name = createGenericName(this.columnList.toArray(new String[this.columnList.size()]));
		}
		return this.name;
	}


	/**
	 * Creates the generic name.
	 *
	 * @param columnList the column list
	 * @return the string
	 */
	public static String createGenericName(final Object[] columnList) {
		final StringBuilder sb = new StringBuilder(64);
		sb.append(SQL.config.IndexPrefix);
		if(columnList.length == 1) {
			sb.append(SQL.util.getColumnName(columnList[0]));
		}
		else {
			for (final Object o : columnList) {
				sb.append(SQL.util.getColumnName(o).replaceAll("\\W", "").substring(0, 2));
			}
		}
		sb.append(SQL.config.IndexSuffix);
		return sb.toString();
	}

	/**
	 * Ensure index pre and suffix.
	 *
	 * @param customIndexName the custom index name
	 * @return the string
	 */
	public static String ensureIndexPreAndSuffix(String customIndexName) {
		if(SQL.config.IndexPrefix != null && SQL.config.IndexPrefix != "") {
			if(customIndexName.indexOf(SQL.config.IndexPrefix) != 0) {
				customIndexName = SQL.config.IndexPrefix + customIndexName;
			}
		}
		if(SQL.config.IndexSuffix != null && SQL.config.IndexSuffix != "") {
			final int pos = customIndexName.lastIndexOf(SQL.config.IndexSuffix);
			if(pos == -1 || pos != customIndexName.length() - SQL.config.IndexSuffix.length()) {
				customIndexName = customIndexName + SQL.config.IndexSuffix;
			}
		}
		return customIndexName;
	}


	/**
	 * Gets the name.
	 *
	 * @return the name
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Sets the name.
	 *
	 * @param name the name
	 * @return the sql index
	 */
	public SqlIndex setName(final String name) {
		this.name = name;
		this.resetHash();
		return this;
	}

	/**
	 * Gets the owner.
	 *
	 * @return the owner
	 */
	public SqlTableIdentity getOwner() {
		return this.owner;
	}

	/**
	 * Sets the owner.
	 *
	 * @param owner the new owner
	 */
	public void setOwner(final SqlTableIdentity owner) {
		this.owner = owner;
		this.resetHash();
	}

	/**
	 * Assemble column list string.
	 *
	 * @param sb the sb
	 * @return the string builder
	 */
	protected StringBuilder assembleColumnListString(StringBuilder sb) {
		if(sb == null) {
			sb = new StringBuilder(256);
		}
		final int lastIndex = this.columnList.size()-1;
		for(int i = 0; i <= lastIndex; i++) {
			final String col = SQL.util.getColumnName(this.columnList.get(i));
			sb.append(col);
			if(i < lastIndex) {
				sb.append(comma_);
			}
		}
		return sb;
	}




	/**
	 * Gets the column list string.
	 *
	 * @return the column list string
	 */
	public String getColumnListString() {
		return this.assembleColumnListString(null).toString();
	}

	/**
	 * Gets the compare string.
	 *
	 * @return the compare string
	 */
	public String getCompareString() {
		if(this.hashString == null) {
			this.buildHash();
		}
		return this.hashString;
	}


	/**
	 * @return
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		if(this.hashInt == 0) {
			this.buildHash();
		}
		return this.hashInt;
	}



	/**
	 * Builds the hash.
	 *
	 * @return the string
	 */
	public String buildHash() {
		final StringBuilder sb = new StringBuilder(256);
		sb.append(this.name);
		sb.append(TAB);
		sb.append(this.type);
		sb.append(TAB);
		sb.append(this.owner);
		sb.append(TAB);
		sb.append(this.getColumnListString());
		this.hashString = sb.toString();
		this.hashInt = this.hashString.hashCode();
		return this.hashString;
	}

	/**
	 * Reset hash.
	 */
	public void resetHash() {
		this.hashString = null;
		this.hashInt = 0;
	}


	/**
	 * @param obj
	 * @return
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(final Object obj) {
		if(obj == null) {
			return false;
		}
		if(obj instanceof SqlIndex) {
			return this.hashCode() == ((SqlIndex)obj).hashCode();
		}
		return false;
	}

	/**
	 * Gets the type.
	 *
	 * @return the type
	 */
	public INDEXTYPE getType() {
		return this.type;
	}


	/**
	 * To index definition.
	 *
	 * @return the index definition
	 */
	public IndexDefinition toIndexDefinition(){
		return new IndexDefinition(
				this.name, this.type, this.columnList
		);
	}

	/**
	 * Gets the column list.
	 *
	 * @return the columnList
	 */
	public List<Object> getColumnList() {
		return this.columnList;
	}


	/**
	 * @return
	 * @see java.lang.Iterable#iterator()
	 */
	@Override
	public java.util.Iterator<Object> iterator()
	{
		return this.columnList.iterator();
	}


}
