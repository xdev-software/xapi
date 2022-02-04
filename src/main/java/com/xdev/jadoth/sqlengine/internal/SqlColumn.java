
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

import com.xdev.jadoth.sqlengine.SQL;
import com.xdev.jadoth.sqlengine.dbms.DbmsDMLAssembler;
import com.xdev.jadoth.sqlengine.exceptions.SQLEngineInvalidIdentifier;
import com.xdev.jadoth.sqlengine.exceptions.SQLEngineRuntimeException;
import com.xdev.jadoth.sqlengine.internal.interfaces.TableExpression;


/**
 * The Class SqlColumn.
 *
 * @author Thomas Muenz
 */
public class SqlColumn extends SqlIdentifier
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -5828040781284264929L;

	/**
	 * Wraps <code>column</code> in an instance of <code>SqlColumn</code> with owner <tt>null</tt>.<br>
	 * If <code>column</code> already is an instance of <code>SqlColumn</code>, it is returned directly.
	 * <p>
	 * if <code>column</code> is not an instance of <code>SqlColumn</code>, the value of <code>toString()</code> is used
	 * when the query is assembled.
	 * 
	 * @param column any object whose <code>toString()</code> method returns a desired, 
	 * apropriate qualified column name during query assembly.
	 * @return <code>column</code> wrapped as a <code>SqlColumn</code> object.
	 */
	public static final SqlColumn wrap(final Object column)
	{
		return column instanceof SqlColumn ?(SqlColumn)column :new SqlColumn(null, column);
	}
	
	public static final SqlColumn[] wrap(final Object[] columns)
	{
		if(columns == null) return null;
		
		if(columns instanceof SqlColumn[]){
			return (SqlColumn[]) columns;
		}
		
		final SqlColumn[] newColumns = new SqlColumn[columns.length];
		for(int i = 0; i < newColumns.length; i++) {
			newColumns[i] = wrap(columns[i]);
		}		
		return newColumns;
	}
	
	/**
	 * Note that this method can only contain logic that applies to ALL DBMS 
	 * (such as recognition of spaces in the column name). Otherwise the result would no longer be DBMS-independant.
	 * Recognition of DBMS-proprietary keywords etc. must be done by the DBMS at assembly time.
	 *  
	 * @param columnName
	 * @return
	 */
	public static boolean needsDelimiting(final String columnName)
	{
		return columnName == null ?false :columnName.indexOf(' ') != -1;
	}
	


	/** The owner. */
	private TableExpression owner;
	
	private boolean delimited;

	/**
	 * Creates the sql column.
	 *
	 * @param expression the expression
	 * @return the sql column
	 * @throws SQLEngineRuntimeException the sQL engine runtime exception
	 */
	public static SqlColumn createSqlColumn(final String expression) throws SQLEngineRuntimeException {
		try {
			return new SqlColumn(expression);
		}
		catch(final SQLEngineInvalidIdentifier e){
			throw new SQLEngineRuntimeException(e);
		}
	}

	/**
	 * Instantiates a new sql column.
	 */
	public SqlColumn() {
		super();
		this.owner = null;
	}

	/**
	 * Instantiates a new sql column.
	 *
	 * @param columnName the columnName
	 * @throws SQLEngineInvalidIdentifier the sQL engine invalid identifier
	 */
	public SqlColumn(final String columnName) throws SQLEngineInvalidIdentifier {
		super(columnName);
		this.owner = null;
		this.delimited = needsDelimiting(columnName);
	}

	/**
	 * Instantiates a new sql column.
	 *
	 * @param owner the owner
	 * @param expression the expression
	 * @throws SQLEngineInvalidIdentifier the sQL engine invalid identifier
	 */
	public SqlColumn(final TableExpression owner, final String columnName) throws SQLEngineInvalidIdentifier 
	{
		super(columnName);
		this.owner = owner;
		this.delimited = needsDelimiting(columnName);
		
	}

	/**
	 * Instantiates a new sql column.
	 *
	 * @param owner the owner
	 * @param expression the expression
	 */
	public SqlColumn(final TableExpression owner, final Object columnName) 
	{
		super(columnName);
		this.owner = owner;
		this.delimited = false; //the object can change the value until query assembly, so can't be checked here.
	}

	/**
	 * Returns the actual name of the column without leading alias.
	 *
	 * @return the column name
	 * @return
	 */
	public String getColumnName() {
		Object o = this.expression;

		/* in case expression is a SqlColumn itself
		 * (meaning THIS SqlColumn is only used as a reference wrapper),
		 * go on fetching the expression recursively until something else than a
		 * SqlColumn is found.
		 *
		 */
		while(o instanceof SqlColumn) {
			o = ((SqlColumn)o).expression;
		}
		return o.toString();
	}
	
	
	public boolean isDelimited()
	{
		return this.delimited;
	}



	/**
	 * Gets the owner.
	 *
	 * @return the owner
	 */
	public TableExpression getOwner() {
		return this.owner;
	}

	/**
	 * Sets the owner.
	 *
	 * @param owner the new owner
	 */
	public void setOwner(final TableExpression owner) {
		this.owner = owner;
	}
	
	public SqlColumn setDelimited(final boolean delimited)
	{
		this.delimited = delimited;
		return this;
	}

	
	public ColumnValueAssignment value(final Object value)
	{
		return SQL.assign(this, value);
	}

	/**
	 * @param dmlAssembler
	 * @param sb
	 * @param indentLevel
	 * @param flags
	 * @return
	 * @see com.xdev.jadoth.sqlengine.internal.SqlExpression#assemble(com.xdev.jadoth.sqlengine.dbms.DbmsDMLAssembler, java.lang.StringBuilder, int, int)
	 */
	@Override
	protected StringBuilder assemble(
		final DbmsDMLAssembler<?> dmlAssembler, final StringBuilder sb, final int indentLevel, final int flags
	)
	{
		return dmlAssembler.assembleColumn(this, sb, indentLevel, flags);
	}

}
