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

import com.xdev.jadoth.sqlengine.INSERT;
import com.xdev.jadoth.sqlengine.SELECT;
import com.xdev.jadoth.sqlengine.exceptions.SQLEngineException;


/**
 * Not finished yet. DO NOT USE.
 *
 * @author Thomas Muenz
 *
 */
@Deprecated
public class SqlTMPTable extends SqlDdlTable
{
	///////////////////////////////////////////////////////////////////////////
	// constants        //
	/////////////////////

	/**
	 *
	 */
	private static final long serialVersionUID = 5584778967869120095L;



	///////////////////////////////////////////////////////////////////////////
	// instance fields  //
	/////////////////////

	/** The s. */
	protected SELECT s;



	///////////////////////////////////////////////////////////////////////////
	// constructors     //
	/////////////////////

	/**
	 * Instantiates a new sql tmp table.
	 *
	 * @param schema the schema
	 * @param name the name
	 * @param alias the alias
	 */
	public SqlTMPTable(final String schema, final String name, final String alias) {
		super(schema, name, alias);
		this.s = null;
	}



	///////////////////////////////////////////////////////////////////////////
	// setters          //
	/////////////////////

	/**
	 * Sets the fill select.
	 *
	 * @param s the s
	 * @return the sql tmp table
	 */
	public SqlTMPTable setFillSELECT(final SELECT s){
		this.s = s;
		return this;
	}



	///////////////////////////////////////////////////////////////////////////
	// override methods //
	/////////////////////

	/**
	 * AS.
	 *
	 * @param newAlias the new alias
	 * @return the sql tmp table
	 * @throws SQLEngineException the sQL engine exception
	 * @return
	 * @see com.xdev.jadoth.sqlengine.internal.tables.SqlDdlTable#AS(java.lang.String)
	 */
	@Override
	public synchronized SqlTMPTable AS(final String newAlias) throws SQLEngineException {
		return (SqlTMPTable)super.AS(newAlias);
	}



	///////////////////////////////////////////////////////////////////////////
	// declared methods //
	/////////////////////

	/**
	 * Fill.
	 *
	 * @throws SQLEngineException the sQL engine exception
	 */
	public void fill() throws SQLEngineException {
		if(this.s != null) {
			new INSERT().INTO(this).select(this.s).execute();
			this.s = null;
		}
	}

}
