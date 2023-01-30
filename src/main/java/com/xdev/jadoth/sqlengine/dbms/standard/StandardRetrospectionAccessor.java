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
package com.xdev.jadoth.sqlengine.dbms.standard;

import static com.xdev.jadoth.sqlengine.SQL.LANG.AND;
import static com.xdev.jadoth.sqlengine.SQL.LANG.FROM;
import static com.xdev.jadoth.sqlengine.SQL.LANG.SELECT;
import static com.xdev.jadoth.sqlengine.SQL.LANG.WHERE;
import static com.xdev.jadoth.sqlengine.SQL.LANG._AS_;
import static com.xdev.jadoth.sqlengine.SQL.Punctuation.NEW_LINE;
import static com.xdev.jadoth.sqlengine.SQL.Punctuation._;
import static com.xdev.jadoth.sqlengine.SQL.Punctuation._eq_;
import static com.xdev.jadoth.sqlengine.SQL.Punctuation.apo;
import static com.xdev.jadoth.sqlengine.SQL.Punctuation.comma;
import static com.xdev.jadoth.sqlengine.SQL.Punctuation.dot;

import com.xdev.jadoth.sqlengine.dbms.DbmsAdaptor;
import com.xdev.jadoth.sqlengine.dbms.DbmsRetrospectionAccessor;
import com.xdev.jadoth.sqlengine.internal.tables.SqlTableIdentity;


/**
 * The Class StandardRetrospectionAccessor.
 * 
 * @param <A> the generic type
 */
public abstract class StandardRetrospectionAccessor<A extends DbmsAdaptor<A>>
extends DbmsRetrospectionAccessor.Implementation<A>
{
	///////////////////////////////////////////////////////////////////////////
	// constants        //
	/////////////////////
	/** The Constant Schema_INFORMATION_SCHEMA. */
	public static final String Schema_INFORMATION_SCHEMA = "INFORMATION_SCHEMA";

	/** The Constant Table_CONSTRAINT_COLUMN_USAGE. */
	public static final String Table_CONSTRAINT_COLUMN_USAGE = Schema_INFORMATION_SCHEMA+dot+"CONSTRAINT_COLUMN_USAGE";
	
	/** The Constant Table_CONSTRAINT_TABLE_CONSTRAINTS. */
	public static final String Table_CONSTRAINT_TABLE_CONSTRAINTS = Schema_INFORMATION_SCHEMA+dot+"TABLE_CONSTRAINTS";

	/** The Constant Column_ORDINAL_POSITION. */
	public static final String Column_ORDINAL_POSITION = "ORDINAL_POSITION";
	
	/** The Constant Column_TABLE_CATALOG. */
	public static final String Column_TABLE_CATALOG = "TABLE_CATALOG";

	/** The Constant Column_CONSTRAINT_CATALOG. */
	public static final String Column_CONSTRAINT_CATALOG = "CONSTRAINT_CATALOG";
	
	/** The Constant Column_CONSTRAINT_SCHEMA. */
	public static final String Column_CONSTRAINT_SCHEMA = "CONSTRAINT_SCHEMA";
	
	/** The Constant Column_CONSTRAINT_NAME. */
	public static final String Column_CONSTRAINT_NAME = "CONSTRAINT_NAME";
	
	/** The Constant Column_CONSTRAINT_TYPE. */
	public static final String Column_CONSTRAINT_TYPE = "CONSTRAINT_TYPE";

	/** The Constant SYSTEMTABLE_TABLES. */
	public static final SqlTableIdentity SYSTEMTABLE_TABLES = new SqlTableIdentity(
		Schema_INFORMATION_SCHEMA, "TABLES", "TABS"
	);
	
	/** The Constant SYSTEMTABLE_COLUMNS. */
	public static final SqlTableIdentity SYSTEMTABLE_COLUMNS = new SqlTableIdentity(
		Schema_INFORMATION_SCHEMA, "COLUMNS", "COLS"
	);



	///////////////////////////////////////////////////////////////////////////
	// static fields    //
	/////////////////////
	/** The Constant __. */
	private static final String __ = _+""+_;



	///////////////////////////////////////////////////////////////////////////
	// constructors     //
	/////////////////////
	/**
	 * Instantiates a new standard retrospection accessor.
	 * 
	 * @param dbmsadaptor the dbmsadaptor
	 */
	public StandardRetrospectionAccessor(final A dbmsadaptor) {
		super(dbmsadaptor);
	}


	///////////////////////////////////////////////////////////////////////////
	// override methods //
	/////////////////////
	/**
	 * @param schemaInclusionPatterns
	 * @param schemaExcluionPatterns
	 * @param tableIncluionPatterns
	 * @param tableExcluionPatterns
	 * @param additionalWHERECondition
	 * @return
	 * @see com.xdev.jadoth.sqlengine.dbms.DbmsRetrospectionAccessor#createSelect_INFORMATION_SCHEMA_TABLES(java.lang.String[], java.lang.String[], java.lang.String[], java.lang.String[], java.lang.String)
	 */
	@Override
	public String createSelect_INFORMATION_SCHEMA_TABLES(
		final String[] schemaInclusionPatterns,
		final String[] schemaExcluionPatterns,
		final String[] tableIncluionPatterns,
		final String[] tableExcluionPatterns,
		final String additionalWHERECondition
	)
	{
		final String queryLoadTables_WHERE_TABLE_TYPE_etc =
			SELECT+NEW_LINE+
			__+Column_TABLE_SCHEMA+_AS_+Column_TABLE_SCHEMA+comma+NEW_LINE+
			__+Column_TABLE_NAME+_AS_+Column_TABLE_NAME+NEW_LINE+

			FROM+_+getSystemTable_TABLES().util.toAliasString()+NEW_LINE+

			WHERE+_+Column_TABLE_TYPE+_eq_+apo+"BASE TABLE"+apo
		;

		final StringBuilder sb = new StringBuilder(1024).append(queryLoadTables_WHERE_TABLE_TYPE_etc);
		appendIncludeExcludeConditions(
			sb,
			schemaInclusionPatterns,
			schemaExcluionPatterns,
			tableIncluionPatterns,
			tableExcluionPatterns,
			Column_TABLE_SCHEMA,
			Column_TABLE_NAME
		);
		if(additionalWHERECondition != null && additionalWHERECondition.length() > 0) {
			sb.append(NEW_LINE).append(AND).append(_).append(additionalWHERECondition);
		}

		return sb.toString();
	}

	/**
	 * @return
	 * @see com.xdev.jadoth.sqlengine.dbms.DbmsRetrospectionAccessor#getRetrospectionCodeGenerationNote()
	 */
	@Override
	public String getRetrospectionCodeGenerationNote() {
		return "SQLEngine Retrospection for SQL Standard, 2010-02-06";
	}

	/**
	 * @return
	 * @see com.xdev.jadoth.sqlengine.dbms.DbmsRetrospectionAccessor#getSystemTable_COLUMNS()
	 */
	@Override
	public SqlTableIdentity getSystemTable_COLUMNS() {
		return SYSTEMTABLE_COLUMNS;
	}

	/**
	 * @return
	 * @see com.xdev.jadoth.sqlengine.dbms.DbmsRetrospectionAccessor#getSystemTable_TABLES()
	 */
	@Override
	public SqlTableIdentity getSystemTable_TABLES() {
		return SYSTEMTABLE_TABLES;
	}


}
