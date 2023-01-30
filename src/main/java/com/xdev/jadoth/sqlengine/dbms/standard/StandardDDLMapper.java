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

import java.util.HashMap;

import com.xdev.jadoth.sqlengine.SQL;
import com.xdev.jadoth.sqlengine.SQL.DATATYPE;
import com.xdev.jadoth.sqlengine.SQL.INDEXTYPE;
import com.xdev.jadoth.sqlengine.dbms.DbmsAdaptor;
import com.xdev.jadoth.sqlengine.dbms.DbmsDDLMapper;
import com.xdev.jadoth.sqlengine.exceptions.SQLEngineException;
import com.xdev.jadoth.sqlengine.interfaces.SqlExecutor;
import com.xdev.jadoth.sqlengine.internal.tables.SqlDdlTable;
import com.xdev.jadoth.sqlengine.internal.tables.SqlIndex;
import com.xdev.jadoth.sqlengine.internal.tables.SqlPrimaryKey;
import com.xdev.jadoth.sqlengine.internal.tables.SqlTableIdentity;
import com.xdev.jadoth.sqlengine.internal.tables.SqlTrigger;



/**
 * The Class StandardDDLMapper.
 * 
 * @param <A> the generic type
 */
public class StandardDDLMapper<A extends DbmsAdaptor<A>> 
extends DbmsAdaptor.Member.Implementation<A> 
implements DbmsDDLMapper<A>
{
	///////////////////////////////////////////////////////////////////////////
	// static fields    //
	/////////////////////

	/** The Constant dataTypeStringMapping. */
	private static final HashMap<String, DATATYPE> dataTypeStringMapping = createDataTypeStrings();
	
	/**
	 * Creates the data type strings.
	 * 
	 * @return the hash map
	 */
	private static final HashMap<String, DATATYPE> createDataTypeStrings()
	{
		final HashMap<String, DATATYPE> c = new HashMap<String, DATATYPE>(20);

		c.put(SQL.DATATYPE.BOOLEAN.toDdlString(), SQL.DATATYPE.BOOLEAN);

		c.put(SQL.DATATYPE.TINYINT.toDdlString(), SQL.DATATYPE.TINYINT);
		c.put(SQL.DATATYPE.SMALLINT.toDdlString(), SQL.DATATYPE.SMALLINT);
		c.put(SQL.DATATYPE.INT.name(), SQL.DATATYPE.INT);
		c.put("INTEGER", SQL.DATATYPE.INT);
		c.put(SQL.DATATYPE.BIGINT.toDdlString(), SQL.DATATYPE.BIGINT);

		c.put(SQL.DATATYPE.REAL.toDdlString(), SQL.DATATYPE.REAL);
		c.put(SQL.DATATYPE.FLOAT.toDdlString(), SQL.DATATYPE.FLOAT);
		c.put(SQL.DATATYPE.DOUBLE.toDdlString(), SQL.DATATYPE.DOUBLE);

		c.put(SQL.DATATYPE.TIME.toDdlString(), SQL.DATATYPE.TIME);
		c.put(SQL.DATATYPE.DATE.toDdlString(), SQL.DATATYPE.DATE);
		c.put(SQL.DATATYPE.TIMESTAMP.toDdlString(), SQL.DATATYPE.TIMESTAMP);

		c.put(SQL.DATATYPE.CHAR.toDdlString(), SQL.DATATYPE.CHAR);
		c.put(SQL.DATATYPE.VARCHAR.toDdlString(), SQL.DATATYPE.VARCHAR);
		c.put(SQL.DATATYPE.CLOB.toDdlString(), SQL.DATATYPE.CLOB);

		c.put(SQL.DATATYPE.NCHAR.toDdlString(), SQL.DATATYPE.NCHAR);
		c.put(SQL.DATATYPE.NVARCHAR.toDdlString(), SQL.DATATYPE.NVARCHAR);
		c.put(SQL.DATATYPE.NCLOB.toDdlString(), SQL.DATATYPE.NCLOB);

		c.put(SQL.DATATYPE.BINARY.toDdlString(), SQL.DATATYPE.BINARY);
		c.put(SQL.DATATYPE.VARBINARY.toDdlString(), SQL.DATATYPE.VARBINARY);
		c.put(SQL.DATATYPE.BLOB.toDdlString(), SQL.DATATYPE.BLOB);

		c.put(SQL.DATATYPE.LONGVARCHAR.toDdlString(), SQL.DATATYPE.LONGVARCHAR);
		c.put(SQL.DATATYPE.LONGVARBINARY.toDdlString(), SQL.DATATYPE.LONGVARBINARY);

		//replicate the whole stuff for lower case, just in case
		for(final String s : c.keySet().toArray(new String[c.size()])) {
			c.put(s.toLowerCase(), c.get(s));
		}

		return c;
	}

	/** The Constant indexTypeStringMapping. */
	private static final HashMap<String, INDEXTYPE> indexTypeStringMapping = createCacheDictionaryIndexTypes();
	
	/**
	 * Creates the cache dictionary index types.
	 * 
	 * @return the hash map
	 */
	private static final HashMap<String, INDEXTYPE> createCacheDictionaryIndexTypes()
	{
		final HashMap<String, INDEXTYPE> c = new HashMap<String, INDEXTYPE>(20);
		c.put(SQL.LANG.INDEX, INDEXTYPE.NORMAL);
		c.put(SQL.LANG.BITMAP, INDEXTYPE.BITMAP);
		c.put(SQL.LANG.UNIQUE, INDEXTYPE.UNIQUE);
		return c;
	}

	
	
	///////////////////////////////////////////////////////////////////////////
	// instance fields  //
	/////////////////////

	/** The custom data type mapping. */
	private HashMap<DATATYPE, String> customDataTypeMapping = null;

	
	
	///////////////////////////////////////////////////////////////////////////
	// constructors     //
	/////////////////////

	/**
	 * Instantiates a new standard ddl mapper.
	 * 
	 * @param dbmsAdaptor the dbms adaptor
	 */
	public StandardDDLMapper(final A dbmsAdaptor) {
		super(dbmsAdaptor);
	}

	
	
///////////////////////////////////////////////////////////////////////////
	// override methods //
	/////////////////////

	/**
	 * @param type
	 * @param table
	 * @return
	 * @see com.xdev.jadoth.sqlengine.dbms.DbmsDDLMapper#lookupDdbmsDataTypeMapping(com.xdev.jadoth.sqlengine.SQL.DATATYPE, com.xdev.jadoth.sqlengine.internal.tables.SqlTableIdentity)
	 */
	@Override
	public String lookupDdbmsDataTypeMapping(final DATATYPE type, final SqlTableIdentity table) {
		return type.toDdlString();
	}

	/**
	 * @param type
	 * @param table
	 * @return
	 * @see com.xdev.jadoth.sqlengine.dbms.DbmsDDLMapper#lookupCustomDataTypeMapping(com.xdev.jadoth.sqlengine.SQL.DATATYPE, com.xdev.jadoth.sqlengine.internal.tables.SqlTableIdentity)
	 */
	@Override
	public String lookupCustomDataTypeMapping(final DATATYPE type, final SqlTableIdentity table) {
		return this.customDataTypeMapping == null ?null :this.customDataTypeMapping.get(type);
	}

	/**
	 * @param type
	 * @param ddlDataTypeString
	 * @return
	 * @see com.xdev.jadoth.sqlengine.dbms.DbmsDDLMapper#registerCustomDataTypeMapping(com.xdev.jadoth.sqlengine.SQL.DATATYPE, java.lang.String)
	 */
	@Override
	public String registerCustomDataTypeMapping(final DATATYPE type, final String ddlDataTypeString)
	{
		if(this.customDataTypeMapping == null) {
			this.customDataTypeMapping = new HashMap<DATATYPE, String>();
		}
		return this.customDataTypeMapping.put(type, ddlDataTypeString);
	}

	/**
	 * @param dataTypeString
	 * @return
	 * @see com.xdev.jadoth.sqlengine.dbms.DbmsDDLMapper#mapDataType(java.lang.String)
	 */
	@Override
	public DATATYPE mapDataType(final String dataTypeString) {
		return dataTypeStringMapping.get(dataTypeString.toUpperCase());
	}


	/**
	 * Map index type.
	 * 
	 * @param indexTypeString the index type string
	 * @return the iNDEXTYPE
	 * @return
	 * @see com.xdev.jadoth.sqlengine.dbms.DbmsDDLMapper#mapIndexType(java.lang.String)
	 */
	@Override
	public INDEXTYPE mapIndexType(final String indexTypeString){
		final INDEXTYPE mappedType = indexTypeStringMapping.get(indexTypeString.toUpperCase());
		return mappedType != null ?mappedType :INDEXTYPE.NORMAL;
	}



	/**
	 * Gets the index type ddl string.
	 * 
	 * @param indexType the index type
	 * @param table the table
	 * @return the index type ddl string
	 * @return
	 * @see com.xdev.jadoth.sqlengine.dbms.DbmsDDLMapper#getIndexTypeDDLString(com.xdev.jadoth.sqlengine.SQL.INDEXTYPE, com.xdev.jadoth.sqlengine.internal.tables.SqlTableIdentity)
	 */
	@Override
	public String getIndexTypeDDLString(final INDEXTYPE indexType, final SqlTableIdentity table) {
		return indexType.toDdlString();
	}




	/**
	 * @param table
	 * @throws SQLEngineException
	 * @see com.xdev.jadoth.sqlengine.dbms.DbmsDDLMapper#createTable(com.xdev.jadoth.sqlengine.internal.tables.SqlDdlTable)
	 */
	@Override
	public void createTable(final SqlDdlTable table) throws SQLEngineException 
	{
		this.getDbmsAdaptor().getDatabaseGateway().execute(SqlExecutor.update, table.ddl.CREATE_TABLE());
	}

	/**
	 * @param index
	 * @param tableCreation
	 * @throws SQLEngineException
	 * @see com.xdev.jadoth.sqlengine.dbms.DbmsDDLMapper#createIndex(com.xdev.jadoth.sqlengine.internal.tables.SqlIndex, boolean)
	 */
	@Override
	public void createIndex(final SqlIndex index, final boolean tableCreation) throws SQLEngineException
	{
		if(tableCreation && index instanceof SqlPrimaryKey) return;
		
		this.getDbmsAdaptor().getDatabaseGateway().execute(SqlExecutor.update, index.CREATE_INDEX());	
	}

	/**
	 * @param trigger
	 * @param tableCreation
	 * @throws SQLEngineException
	 * @see com.xdev.jadoth.sqlengine.dbms.DbmsDDLMapper#createTrigger(com.xdev.jadoth.sqlengine.internal.tables.SqlTrigger, boolean)
	 */
	@Override
	public void createTrigger(final SqlTrigger trigger, final boolean tableCreation) throws SQLEngineException 
	{
		this.getDbmsAdaptor().getDatabaseGateway().execute(SqlExecutor.update, trigger.CREATE_TRIGGER());
	}

	/* (09.11.2009 TM)NOTE:
	 * Should be extended by some "boolean batchMode" and "boolean tableCreation" parameters.
	 */
	/**
	 * @param table
	 * @throws SQLEngineException
	 * @see com.xdev.jadoth.sqlengine.dbms.DbmsDDLMapper#createIndices(com.xdev.jadoth.sqlengine.internal.tables.SqlDdlTable)
	 */
	@Override
	public void createIndices(final SqlDdlTable table) throws SQLEngineException
	{
		// escape condition
		if(this.getDbmsAdaptor().getDatabaseGateway() == null || table == null) return;

		final SqlDdlTable.Indices indices = table.util.getIndices();
		if(indices == null) return;

		for(final SqlIndex sqlIndex : indices.listIndices()) {
			this.createIndex(sqlIndex, true);
		}
	}

	/* (09.11.2009 TM)NOTE:
	 * Should be extended by some "boolean batchMode" and "boolean tableCreation" parameters.
	 */
	/**
	 * @param table
	 * @throws SQLEngineException
	 * @see com.xdev.jadoth.sqlengine.dbms.DbmsDDLMapper#createTriggers(com.xdev.jadoth.sqlengine.internal.tables.SqlDdlTable)
	 */
	@Override
	public void createTriggers(final SqlDdlTable table) throws SQLEngineException
	{
		if(this.getDbmsAdaptor().getDatabaseGateway() == null || table == null) {
			return;
		}
		final SqlDdlTable.Triggers triggers = table.util.getTriggers();
		if(triggers == null) return;

		for(final SqlTrigger trigger : triggers.listTriggers()) {
			this.createTrigger(trigger, true);
		}
	}

	/**
	 * @param type
	 * @param table
	 * @return
	 * @see com.xdev.jadoth.sqlengine.dbms.DbmsDDLMapper#getDataTypeDDLString(com.xdev.jadoth.sqlengine.SQL.DATATYPE, com.xdev.jadoth.sqlengine.internal.tables.SqlTableIdentity)
	 */
	@Override
	public String getDataTypeDDLString(final DATATYPE type, final SqlTableIdentity table)
	{
		final String customDatatypeString = this.lookupCustomDataTypeMapping(type, table);
		if(customDatatypeString != null) {
			return customDatatypeString;
		}

		return this.lookupDdbmsDataTypeMapping(type, table);
	}

	/**
	 * @param table
	 * @return
	 * @throws SQLEngineException
	 * @see com.xdev.jadoth.sqlengine.dbms.DbmsDDLMapper#postCreateTableActions(com.xdev.jadoth.sqlengine.internal.tables.SqlDdlTable)
	 */
	@Override
	public Object postCreateTableActions(final SqlDdlTable table) throws SQLEngineException {
		return null;
	}

	/**
	 * @param table
	 * @return
	 * @throws SQLEngineException
	 * @see com.xdev.jadoth.sqlengine.dbms.DbmsDDLMapper#preCreateTableActions(com.xdev.jadoth.sqlengine.internal.tables.SqlDdlTable)
	 */
	@Override
	public Object preCreateTableActions(final SqlDdlTable table) throws SQLEngineException {
		return null;
	}

}
