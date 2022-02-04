
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

import com.xdev.jadoth.sqlengine.SELECT;
import com.xdev.jadoth.sqlengine.internal.SqlCondition;
import com.xdev.jadoth.sqlengine.internal.SqlField;
import com.xdev.jadoth.sqlengine.internal.WHERE;


/**
 * The Class PrimaryKeyValue.
 * 
 * @param <T> the generic type
 * @author Thomas Muenz
 */
public class PrimaryKeyValue<T extends SqlDdlTable> 
{
	/** The table. */
	private final SqlTable table;
	
	/** The columns. */
	private final SqlField[] columns;
	
	/** The values. */
	private final Object[] values;

	/** The cached conditions. */
	private final SqlCondition[] cachedConditions;
	
	/** The cached where clause. */
	private final WHERE cachedWHEREClause;
	
	/** The cached where string. */
	private final String cachedWHEREString;


	/**
	 * Instantiates a new primary key value.
	 * 
	 * @param table the table
	 * @param columns the columns
	 * @param values the values
	 */
	protected PrimaryKeyValue(final SqlDdlTable table, final SqlField[] columns, final Object[] values) {
		super();
		this.table = table;
		this.columns = columns;
		this.values = values;

		this.cachedWHEREClause = new WHERE(columns[0].c("=", values[0]));
		this.cachedConditions = new SqlCondition[columns.length];
		this.cachedConditions[0] = columns[0].c("=", values[0]);

		if(columns.length > 1) {
			for (int i = 1, l = columns.length; i < l; i++) {
				if(values[i] == null) {
					throw new NullPointerException(columns[i] + " is null");
				}
				this.cachedConditions[i] = columns[i].c("=", values[i]);
				this.cachedWHEREClause.AND(this.cachedConditions[i]);
			}
		}
		this.cachedWHEREString = cachedWHEREClause.toString();
	}

	/**
	 * Gets the table.
	 * 
	 * @return the table
	 */
	public SqlTable getTable() {
		return table;
	}

	/**
	 * Gets the where clause.
	 * 
	 * @return the where clause
	 */
	public WHERE getWhereClause() {
		return this.cachedWHEREClause;
	}

	/**
	 * Gets the where string.
	 * 
	 * @return the where string
	 */
	public String getWhereString() {
		return this.cachedWHEREString;
	}

	/**
	 * Select.
	 * 
	 * @return the sELECT
	 */
	public SELECT select() {
		return new SELECT().FROM(table).WHERE(cachedWHEREClause);
	}

	/**
	 * @return the columns
	 */
	public SqlField[] getColumns()
	{
		return columns;
	}

	/**
	 * @return the values
	 */
	public Object[] getValues()
	{
		return values;
	}


}
