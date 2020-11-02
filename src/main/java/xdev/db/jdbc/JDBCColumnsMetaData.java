package xdev.db.jdbc;

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


import java.sql.DatabaseMetaData;

import xdev.db.DataType;


/**
 * Helper class to work with the ResultSet of
 * {@link DatabaseMetaData#getProcedureColumns(String, String, String, String)}
 * or {link
 * {@link DatabaseMetaData#getFunctionColumns(String, String, String, String)}.
 * 
 * @author XDEV Software (MP)
 * 
 */
public class JDBCColumnsMetaData
{
	
	private DataType	dataType;
	private int			columnType;
	private String		columnName;
	
	
	/**
	 * Constructor to instantiate JDBCProcedureColumnsMetaData.
	 * 
	 * @param dataType
	 *            DataType => the DataType of this column
	 * @param columnType
	 *            int => kind of column/paramter name
	 * @param columnName
	 *            String => column/parameter name
	 */
	public JDBCColumnsMetaData(DataType dataType, int columnType, String columnName)
	{
		this.dataType = dataType;
		this.columnType = columnType;
		this.columnName = columnName;
	}
	
	
	/**
	 * 
	 * @return DataType data type
	 */
	public DataType getDataType()
	{
		return dataType;
	}
	
	
	/**
	 * 
	 * @param dataType
	 *            DataType => the DataType of this column
	 */
	public void setDataType(DataType dataType)
	{
		this.dataType = dataType;
	}
	
	
	/**
	 * 
	 * @return int column type
	 */
	public int getColumnType()
	{
		return columnType;
	}
	
	
	/**
	 * 
	 * @param columnType
	 *            int => kind of column/paramter name
	 */
	public void setColumnType(int columnType)
	{
		this.columnType = columnType;
	}
	
	
	/**
	 * 
	 * @return String column name
	 */
	public String getColumnName()
	{
		return columnName;
	}
	
	
	/**
	 * 
	 * @param columnName
	 *            String => column/parameter name
	 */
	public void setColumnName(String columnName)
	{
		this.columnName = columnName;
	}
	
}
