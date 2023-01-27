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

package com.xdev.jadoth.sqlengine.dbms;

import com.xdev.jadoth.sqlengine.SQL.DATATYPE;
import com.xdev.jadoth.sqlengine.SQL.INDEXTYPE;
import com.xdev.jadoth.sqlengine.exceptions.SQLEngineException;
import com.xdev.jadoth.sqlengine.internal.tables.SqlDdlTable;
import com.xdev.jadoth.sqlengine.internal.tables.SqlIndex;
import com.xdev.jadoth.sqlengine.internal.tables.SqlTableIdentity;
import com.xdev.jadoth.sqlengine.internal.tables.SqlTrigger;


/**
 * The Interface DbmsDDLMapper.
 * 
 * @param <A> the generic type
 */
public interface DbmsDDLMapper<A extends DbmsAdaptor<A>>
{
	// ddl mapping //

	/**
	 * Map data type.
	 * 
	 * @param dataTypeString the data type string
	 * @return the dATATYPE
	 */
	public DATATYPE mapDataType(String dataTypeString);
	
	/**
	 * Map index type.
	 * 
	 * @param indexTypeString the index type string
	 * @return the iNDEXTYPE
	 */
	public INDEXTYPE mapIndexType(String indexTypeString);

	/**
	 * Register custom data type mapping.
	 * 
	 * @param dataType the data type
	 * @param dataTypeString the data type string
	 * @return the string
	 */
	public String registerCustomDataTypeMapping(DATATYPE dataType, String dataTypeString);

	/**
	 * Lookup custom data type mapping.
	 * 
	 * @param dataType the data type
	 * @param table the table
	 * @return the string
	 */
	public String lookupCustomDataTypeMapping(DATATYPE dataType, SqlTableIdentity table);
	
	/**
	 * Lookup ddbms data type mapping.
	 * 
	 * @param dataType the data type
	 * @param table the table
	 * @return the string
	 */
	public String lookupDdbmsDataTypeMapping(DATATYPE dataType, SqlTableIdentity table);

	/**
	 * Gets the data type ddl string.
	 * 
	 * @param dataType the data type
	 * @param table the table
	 * @return the data type ddl string
	 */
	public String getDataTypeDDLString(DATATYPE dataType, SqlTableIdentity table);
	
	/**
	 * Gets the index type ddl string.
	 * 
	 * @param indexType the index type
	 * @param table the table
	 * @return the index type ddl string
	 */
	public String getIndexTypeDDLString(INDEXTYPE indexType, SqlTableIdentity table);



	// creation //

	/**
	 * Pre create table actions.
	 * 
	 * @param table the table
	 * @return the object
	 * @throws SQLEngineException the sQL engine exception
	 */
	public Object preCreateTableActions(SqlDdlTable table) throws SQLEngineException;
	
	/**
	 * Creates the table.
	 * 
	 * @param table the table
	 * @throws SQLEngineException the sQL engine exception
	 */
	public void createTable(SqlDdlTable table) throws SQLEngineException;
	
	/**
	 * Post create table actions.
	 * 
	 * @param table the table
	 * @return the object
	 * @throws SQLEngineException the sQL engine exception
	 */
	public Object postCreateTableActions(SqlDdlTable table) throws SQLEngineException;

	/**
	 * Creates the indices.
	 * 
	 * @param table the table
	 * @throws SQLEngineException the sQL engine exception
	 */
	public void createIndices(SqlDdlTable table) throws SQLEngineException;
	
	/**
	 * Creates the index.
	 * 
	 * @param index the index
	 * @param batchMode the batch mode
	 * @throws SQLEngineException the sQL engine exception
	 */
	public void createIndex(SqlIndex index, boolean batchMode) throws SQLEngineException;

	/**
	 * Creates the triggers.
	 * 
	 * @param table the table
	 * @throws SQLEngineException the sQL engine exception
	 */
	public void createTriggers(SqlDdlTable table) throws SQLEngineException;
	
	/**
	 * Creates the trigger.
	 * 
	 * @param trigger the trigger
	 * @param batchMode the batch mode
	 * @throws SQLEngineException the sQL engine exception
	 */
	public void createTrigger(SqlTrigger trigger, boolean batchMode) throws SQLEngineException;

}
