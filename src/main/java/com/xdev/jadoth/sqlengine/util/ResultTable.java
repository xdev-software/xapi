
package com.xdev.jadoth.sqlengine.util;

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


import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.xdev.jadoth.sqlengine.SELECT;
import com.xdev.jadoth.sqlengine.exceptions.SQLEngineException;




/**
 * The Class ResultTable.
 *
 * @author Thomas Muenz
 */
public class ResultTable
{
	///////////////////////////////////////////////////////////////////////////
	// instance fields  //
	/////////////////////

	/** The column names. */
	private String[] columnNames;

	/** The rows. */
	private ArrayList<Object[]> rows;

	/** The column sql types. */
	private int[] columnSqlTypes;



	///////////////////////////////////////////////////////////////////////////
	// constructors     //
	/////////////////////
	/**
	 * Instantiates a new result table.
	 */
	public ResultTable() {
		this.columnNames = null;
		this.rows = null;
	}

	/**
	 * Uses the ResultSet <code>rs</code> to fill the created ResultTable instance.<br>
	 * Automatically closes ResultSet <code>rs</code> and the statement that created it.
	 *
	 * @param rs the rs
	 * @throws SQLEngineException the sQL engine exception
	 */
	public ResultTable(final ResultSet rs) throws SQLEngineException 
	{
		super();
		this.fill(rs, true, true);
	}
	
	public ResultTable(final Object resultValue) throws SQLEngineException 
	{
		super();
		if(resultValue instanceof ResultSet){
			this.fill((ResultSet)resultValue, true, true);
		}
		else {
			this.columnNames = new String[]{"value"};
			this.rows = new ArrayList<Object[]>(1);
			this.rows.add(new Object[]{resultValue});
			this.columnSqlTypes = new int[]{java.sql.Types.JAVA_OBJECT};
		}		
	}

	/**
	 * Uses the ResultSet <code>rs</code> to fill the created ResultTable instance.<br>
	 * Automatically closes ResultSet <code>rs</code> and the statement that created it.
	 *
	 * @param s the s
	 * @throws SQLEngineException the sQL engine exception
	 */
	public ResultTable(final SELECT s) throws SQLEngineException {
		final ResultSet rs = s.execute();
		this.fill(rs, true, true);
	}


	///////////////////////////////////////////////////////////////////////////
	// declared methods //
	/////////////////////

	/**
	 * Fills this ResultTable instance by the given ResultSet.<br>
	 * Closes neither the ResultSet <code>rs</code> nor the Statement that created it.<br>
	 * (as opposed to constructor)
	 *
	 * @param rs the rs
	 * @return the result table
	 * @throws SQLEngineException the sQL engine exception
	 * @return
	 */
	public ResultTable fill(final ResultSet rs) throws SQLEngineException
	{
		return this.fill(rs, false, false);
	}


	/**
	 * Fills this ResultTable instance by the given ResultSet.<br>
	 * Does not close the Statement that created it.<br>
	 * (as opposed to constructor)
	 *
	 * @param rs the rs
	 * @param closeResultSet the close result set
	 * @return the result table
	 * @throws SQLEngineException the sQL engine exception
	 * @return
	 */
	public ResultTable fill(
		final ResultSet rs, final boolean closeResultSet
	)
		throws SQLEngineException
	{
		return this.fill(rs, closeResultSet, false);
	}

	/**
	 * Fill.
	 *
	 * @param rs the rs
	 * @param closeResultSet the close result set
	 * @param closeStatement the close statement
	 * @return the result table
	 * @throws SQLEngineException the sQL engine exception
	 */
	public ResultTable fill(
		final ResultSet rs, final boolean closeResultSet, final boolean closeStatement
	)
		throws SQLEngineException
	{
		if(rs == null) {
			throw new SQLEngineException("No ResultSet given");
		}

		final int columnCount;
		final ResultSetMetaData rsmd;
		final int[] colTypes;
		final String[] colNames;

		try {
			rsmd = rs.getMetaData();
			columnCount = rsmd.getColumnCount();

			colTypes = new int[columnCount];
			colNames = new String[columnCount];

			for (int i = 0; i < columnCount; i++) {
				colNames[i] = rsmd.getColumnName(i+1);
				colTypes[i] = rsmd.getColumnType(i+1);
			}
		}
		catch (final SQLException e) {
			throw new SQLEngineException(e);
		}

		final ArrayList<Object[]> rows = new ArrayList<Object[]>(1024);
		Object[] row;
		Object cellValue;
		try {
			while(rs.next()) {
				row = new Object[columnCount];
				for(int c = 0; c < columnCount; c++) {
					//inlined convertData() for performance only
					switch (colTypes[c]) {
					case Types.BOOLEAN:
						cellValue = new Boolean(rs.getBoolean(c+1));
						break;
					case Types.TINYINT:
						cellValue = new Byte(rs.getByte(c+1));
						break;
					case Types.SMALLINT:
						cellValue = new Short(rs.getShort(c+1));
						break;
					case Types.INTEGER:
						cellValue = new Integer(rs.getInt(c+1));
						break;
					case Types.BIGINT:
						cellValue = new Long(rs.getLong(c+1));
						break;
					case Types.FLOAT:
						cellValue = new Float(rs.getFloat(c+1));
						break;
					case Types.DOUBLE:
						cellValue = new Double(rs.getDouble(c+1));
						break;
					default:
						cellValue = rs.getObject(c+1);
					}
					row[c] = rs.wasNull()?null:cellValue;
				}
				rows.add(row);
			}

			if(closeStatement) {
				rs.getStatement().close();
			}
			if(closeResultSet) {
				rs.close();
			}
		}
		catch (final SQLException e) {
			throw new SQLEngineException(e);
		}

		//this.columnTypes = newColumnTypes;
		this.columnNames = colNames;
		this.rows = rows;
		this.columnSqlTypes = colTypes;
		return this;
	}


	/**
	 * Convert data.
	 *
	 * @param rs the rs
	 * @param colType the col type
	 * @param col the col
	 * @return the object
	 * @throws SQLEngineException the sQL engine exception
	 */
	public static Object convertData(final ResultSet rs, final int colType, final int col) throws SQLEngineException {
		try {
			Object cellValue;
			switch (colType) {
				case Types.BOOLEAN:
					cellValue = new Boolean(rs.getBoolean(col+1));
					break;
				case Types.TINYINT:
					cellValue = new Byte(rs.getByte(col+1));
					break;
				case Types.SMALLINT:
					cellValue = new Short(rs.getShort(col+1));
					break;
				case Types.INTEGER:
					cellValue = new Integer(rs.getInt(col+1));
					break;
				case Types.BIGINT:
					cellValue = new Long(rs.getLong(col+1));
					break;
				case Types.FLOAT:
					cellValue = new Float(rs.getFloat(col+1));
					break;
				case Types.DOUBLE:
					cellValue = new Double(rs.getDouble(col+1));
					break;
				default:
					cellValue = rs.getObject(col+1);
			}
			return rs.wasNull()?null:cellValue;
		} catch (final SQLException e) {
			throw new SQLEngineException(e);
		}

	}


	/**
	 * Gets the row.
	 *
	 * @param row the row
	 * @return the row
	 */
	public Object[] getRow(final int row) {
		return this.rows.get(row);
	}



	/**
 * Gets the value.
 *
 * @param row the row
 * @param column the column
 * @return the value
 */
public Object getValue(final int row, final int column) {
		return this.rows.get(row)[column];
	}

	/**
	 * Gets the value.
	 *
	 * @param row the row
	 * @param column the column
	 * @return the value
	 */
	public Object getValue(final int row, final String column) {
		final int columnIndex = this.getColumnIndex(column);
		if(columnIndex >= 0) {
			return this.rows.get(row)[columnIndex];
		}
		return null;
	}

	/**
	 * Gets the column index.
	 *
	 * @param column the column
	 * @return the column index
	 */
	public int getColumnIndex(final String column) {
		if(column == null || column == "") {
			return -1;
		}
		for(int c = 0; c < this.columnNames.length; c++) {
			if(this.columnNames[c].equals(column)) {
				return c;
			}
		}
		return -1;
	}



	/**
	 * @return
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder(1024);
		final ArrayList<Object[]> rows = this.rows;
		final int cc = this.columnNames.length;

		for(int c = 0; c < cc; c++) {
			sb.append(this.columnNames[c]+"\t");
		}
		sb.append("\n");

		for(int r = 0, rc = rows.size(); r < rc; r++) {
			for(int c = 0; c < cc ; c++) {
				sb.append(rows.get(r)[c]+"\t");
			}
			sb.append("\n");
			if(rc == 20) {
				sb.append("...");
				r = rc;
			}
		}
		return sb.toString();
	}



	/**
	 * Prints the column names.
	 */
	public void printColumnNames() {
		for(int c = 0; c < this.columnNames.length; c++) {
			System.out.print(this.columnNames[c]+"\t");
		}
		System.out.print("\n");
	}

	/**
	 * Prints the data.
	 */
	public void printData() {
		final ArrayList<Object[]> rows = this.rows;
		final int cc = this.columnNames.length;
		for(int r = 0, rc = rows.size(); r < rc; r++) {
			for(int c = 0; c < cc; c++) {
				System.out.print(rows.get(r)[c]+"\t");
			}
			System.out.print("\n");
		}
	}

	/**
	 * Prints the.
	 */
	public void print() {
		this.printColumnNames();
		this.printData();
	}

	/**
	 * Gets the column count.
	 *
	 * @return the column count
	 */
	public int getColumnCount() {
		return this.columnNames==null?0:this.columnNames.length;
	}

	/**
	 * Gets the column names.
	 *
	 * @return the column names
	 */
	public String[] getColumnNames() {
		return this.columnNames;
	}


	/**
	 * Gets the row count.
	 *
	 * @return the row count
	 */
	public int getRowCount() {
		return this.rows.size();
	}

	/**
	 * Gets the column sql types.
	 *
	 * @return the column sql types
	 */
	public int[] getColumnSqlTypes() {
		return this.columnSqlTypes;
	}



	/**
	 * Gets the column as set.
	 *
	 * @param <D> the generic type
	 * @param columnIndex the column index
	 * @param set the set
	 * @return the column as set
	 */
	@SuppressWarnings("unchecked")
	public <D> Set<D> getColumnAsSet(final int columnIndex, final Set<D> set) {
		final ArrayList<Object[]> rows = this.rows;
		for(int r = 0, len = rows.size(); r < len; r++) {
			set.add((D)rows.get(r)[columnIndex]);
		}
		return set;
	}


	/**
	 * Gets the column as list.
	 *
	 * @param <D> the generic type
	 * @param columnIndex the column index
	 * @param list the list
	 * @return the column as list
	 */
	@SuppressWarnings("unchecked")
	public <D> List<D> getColumnAsList(final int columnIndex, final List<D> list){
		final ArrayList<Object[]> rows = this.rows;
		for(int r = 0, len = rows.size(); r < len; r++) {
			list.add(r, (D)rows.get(r)[columnIndex]);
		}
		return list;
	}

	/**
	 * Gets the columns as map.
	 *
	 * @param <KC> the generic type
	 * @param <VC> the generic type
	 * @param keyColumnIndex the key column index
	 * @param valueColumnIndex the value column index
	 * @param map the map
	 * @return the columns as map
	 */
	@SuppressWarnings("unchecked")
	public <KC, VC> Map<KC, VC> getColumnsAsMap(
		final int keyColumnIndex, final int valueColumnIndex, final Map<KC, VC> map
	)
	{
		final ArrayList<Object[]> rows = this.rows;
		for(int r = 0, len = rows.size(); r < len; r++) {
			map.put((KC)rows.get(r)[keyColumnIndex], (VC)rows.get(r)[valueColumnIndex]);
		}
		return map;
	}

	/**
	 * Gets the row as map.
	 *
	 * @param <V> the value type
	 * @param rowIndex the row index
	 * @param map the map
	 * @return the row as map
	 */
	@SuppressWarnings("unchecked")
	public <V> Map<String, V> getRowAsMap(final int rowIndex ,final Map<String, V> map){
		if(rowIndex < 0) {
			throw new IndexOutOfBoundsException();
		}

		final Object[] row = this.rows.get(rowIndex);
		for (int c = 0, len = row.length; c < len; c++) {
			map.put(this.columnNames[c], (V)row[c]);
		}
		return map;
	}


}

