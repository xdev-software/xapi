
package com.xdev.jadoth.sqlengine.internal.tables;

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

import static com.xdev.jadoth.sqlengine.SQL.LANG.CONSTRAINT;
import static com.xdev.jadoth.sqlengine.SQL.Punctuation._;
import static com.xdev.jadoth.sqlengine.SQL.Punctuation.par;
import static com.xdev.jadoth.sqlengine.SQL.Punctuation.rap;

import com.xdev.jadoth.sqlengine.SQL.INDEXTYPE;
import com.xdev.jadoth.sqlengine.exceptions.SQLEngineException;
import com.xdev.jadoth.sqlengine.internal.SqlField;


/**
 * The Class SqlPrimaryKey.
 *
 * @author Thomas Muenz
 */
public class SqlPrimaryKey extends SqlIndex
{

	/**
	 * Instantiates a new sql primary key.
	 *
	 * @param columnList the column list
	 */
	public SqlPrimaryKey(final Object... columnList) {
		this(null, null, columnList);
	}

	/**
	 * Instantiates a new sql primary key.
	 *
	 * @param name the name
	 * @param table the table
	 * @param columnList the column list
	 */
	public SqlPrimaryKey(final String name, final SqlTableIdentity table, final Object... columnList) {
		super(name, table, INDEXTYPE.PRIMARYKEY, columnList);

		for (Object o : columnList) {
			if(o instanceof SqlField) {
				((SqlField)o).setNotNull(true);
			}
		}
	}

	/**
	 * @return
	 * @see com.xdev.jadoth.sqlengine.internal.tables.SqlIndex#CREATE_INDEX()
	 */
	@Override
	public String CREATE_INDEX() {
		StringBuilder sb = new StringBuilder(128);
		sb.append(CONSTRAINT);
		sb.append(_);
		sb.append(this.name);
		sb.append(_);
		// (10.11.2009 TM)FIXME: will probably crash since change from String to enum
		sb.append(this.type.toDdlString());
		sb.append(_);
		sb.append(par);
		assembleColumnListString(sb);
		sb.append(rap);
		return sb.toString();
	}

	/**
	 * Creates the index.
	 *
	 * @throws SQLEngineException the sQL engine exception
	 */
	public void createIndex() throws SQLEngineException {
		//PRIMARY KEY is created at table creation, not seperately. Do Nothing.
	}

	/**
	 * @param name
	 * @return
	 * @see com.xdev.jadoth.sqlengine.internal.tables.SqlIndex#setName(java.lang.String)
	 */
	@Override
	public SqlPrimaryKey setName(final String name) {
		super.setName(name.toUpperCase());
		return this;
	}




}
