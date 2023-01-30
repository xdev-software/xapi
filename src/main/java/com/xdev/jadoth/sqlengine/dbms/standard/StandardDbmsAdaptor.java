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


import com.xdev.jadoth.sqlengine.dbms.DbmsAdaptor;
import com.xdev.jadoth.sqlengine.dbms.DbmsConnectionInformation;
import com.xdev.jadoth.sqlengine.dbms.SQLExceptionParser;
import com.xdev.jadoth.sqlengine.internal.DatabaseGateway;
import com.xdev.jadoth.sqlengine.internal.tables.SqlTableIdentity;


/**
 * The Class StandardDbmsAdaptor.
 */

public class StandardDbmsAdaptor
		extends
		DbmsAdaptor.Implementation<StandardDbmsAdaptor, StandardDMLAssembler<StandardDbmsAdaptor>, StandardDDLMapper<StandardDbmsAdaptor>, StandardRetrospectionAccessor<StandardDbmsAdaptor>, StandardSyntax<StandardDbmsAdaptor>>
{
	// /////////////////////////////////////////////////////////////////////////
	// constants //
	// ///////////////////
	
	protected static final char								IDENTIFIER_DELIMITER	= '"';
	
	public static final StandardSyntax<StandardDbmsAdaptor>	SYNTAX					= new StandardSyntax<StandardDbmsAdaptor>();
	
	private static final StandardDbmsAdaptor				SINGLETON				= new StandardDbmsAdaptor();
	

	// /////////////////////////////////////////////////////////////////////////
	// static fields //
	// ///////////////////
	
	// /////////////////////////////////////////////////////////////////////////
	// static methods //
	// ///////////////////
	
	/**
	 * Gets the singleton standard dbms adaptor.
	 * 
	 * @return the singleton standard dbms adaptor
	 */
	public static final StandardDbmsAdaptor getSingletonStandardDbmsAdaptor()
	{
		return SINGLETON;
	}
	

	// /////////////////////////////////////////////////////////////////////////
	// constructors //
	// ///////////////////
	
	/**
	 * Instantiates a new standard dbms adaptor.
	 */
	public StandardDbmsAdaptor()
	{
		this(new SQLExceptionParser.Body());
	}
	

	/**
	 * Instantiates a new standard dbms adaptor.
	 * 
	 * @param sqlExceptionParser
	 *            the sql exception parser
	 */
	public StandardDbmsAdaptor(final SQLExceptionParser sqlExceptionParser)
	{
		super(sqlExceptionParser,true);
		this.setDMLAssembler(new StandardDMLAssembler<StandardDbmsAdaptor>(this));
		this.setDdlMapper(new StandardDDLMapper<StandardDbmsAdaptor>(this));
		this.setRetrospectionAccessor(null);
		this.setSyntax(SYNTAX);
	}
	

	// /////////////////////////////////////////////////////////////////////////
	// override methods //
	// ///////////////////
	
	/**
	 * @param host
	 * @param port
	 * @param user
	 * @param password
	 * @param catalog
	 * @param properties
	 * @return
	 * @see com.xdev.jadoth.sqlengine.dbms.DbmsAdaptor#createConnectionInformation(java.lang.String,
	 *      int, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public DbmsConnectionInformation<StandardDbmsAdaptor> createConnectionInformation(
			final String host, final int port, final String user, final String password,
			final String catalog, final String properties)
	{
		// TODO StandardDbmsAdaptor.createConnectionInformation
		return null;
	}
	

	/**
	 * @param bytes
	 * @param sb
	 * @return
	 * @see com.xdev.jadoth.sqlengine.dbms.DbmsAdaptor#assembleTransformBytes(byte[],
	 *      java.lang.StringBuilder)
	 */
	@Override
	public StringBuilder assembleTransformBytes(final byte[] bytes, final StringBuilder sb)
	{
		// TODO StandardDbmsAdaptor.assembleTransformBytes
		return null;
	}
	

	/**
	 * @return
	 * @see com.xdev.jadoth.sqlengine.dbms.DbmsAdaptor#getMaxVARCHARlength()
	 */
	@Override
	public int getMaxVARCHARlength()
	{
		return Integer.MAX_VALUE;
	}
	

	/**
	 * @param dbc
	 * @see com.xdev.jadoth.sqlengine.dbms.DbmsAdaptor#initialize(com.xdev.jadoth.sqlengine.internal.DatabaseGateway)
	 */
	@Override
	public void initialize(final DatabaseGateway<StandardDbmsAdaptor> dbc)
	{
		// empty
	}
	

	/**
	 * @param fullQualifiedTableName
	 * @return
	 * @see com.xdev.jadoth.sqlengine.dbms.DbmsAdaptor#rebuildAllIndices(java.lang.String)
	 */
	@Override
	public Object rebuildAllIndices(final String fullQualifiedTableName)
	{
		// empty
		return null;
	}
	

	/**
	 * @param table
	 * @return
	 * @see com.xdev.jadoth.sqlengine.dbms.DbmsAdaptor#updateSelectivity(com.xdev.jadoth.sqlengine.internal.tables.SqlTableIdentity)
	 */
	@Override
	public Object updateSelectivity(final SqlTableIdentity table)
	{
		// empty
		return null;
	}
	

	/**
	 * @param dbc
	 * @return
	 * @see com.xdev.jadoth.sqlengine.dbms.DbmsAdaptor.Implementation#setDatabaseGateway(com.xdev.jadoth.sqlengine.internal.DatabaseGateway)
	 */
	@Override
	public StandardDbmsAdaptor setDatabaseGateway(final DatabaseGateway<StandardDbmsAdaptor> dbc)
	{
		return super.setDatabaseGateway(dbc);
	}
	

	@Override
	public boolean supportsOFFSET_ROWS()
	{
		return true;
	}
	

	@Override
	public char getIdentifierDelimiter()
	{
		return IDENTIFIER_DELIMITER;
	}
}
