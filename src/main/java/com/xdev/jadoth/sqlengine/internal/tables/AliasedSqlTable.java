
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


/**
 * The Class AliasedSqlTable.
 */
public class AliasedSqlTable
{
	///////////////////////////////////////////////////////////////////////////
	// instance fields  //
	/////////////////////
	/** The sqltable. */
	private SqlTableIdentity sqltable;



	///////////////////////////////////////////////////////////////////////////
	// constructors     //
	/////////////////////
	/**
	 * Instantiates a new aliased sql table.
	 * 
	 * @param sqltable the sqltable
	 */
	public AliasedSqlTable(final SqlTableIdentity sqltable) {
		super();
		this.sqltable = sqltable;
	}



	///////////////////////////////////////////////////////////////////////////
	// getters          //
	/////////////////////
	/**
	 * Gets the sqltable.
	 * 
	 * @return the sqltable
	 */
	public SqlTableIdentity getSqltable() {
		return sqltable;
	}



	///////////////////////////////////////////////////////////////////////////
	// setters          //
	/////////////////////
	/**
	 * Sets the sqltable.
	 * 
	 * @param sqltable the new sqltable
	 */
	public void setSqltable(final SqlTableIdentity sqltable) {
		this.sqltable = sqltable;
	}



	///////////////////////////////////////////////////////////////////////////
	// override methods //
	/////////////////////
	/**
	 * @return
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return sqltable.util.toAliasString();
	}

}
