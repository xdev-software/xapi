
package com.xdev.jadoth.sqlengine.internal.interfaces;

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

import java.io.Serializable;

import com.xdev.jadoth.sqlengine.SELECT;
import com.xdev.jadoth.sqlengine.SQL;
import com.xdev.jadoth.sqlengine.exceptions.SQLEngineException;
import com.xdev.jadoth.sqlengine.internal.DatabaseGateway;
import com.xdev.jadoth.sqlengine.internal.tables.SqlQualifiedIdentityWithAlias;
import com.xdev.jadoth.sqlengine.internal.tables.SqlTable;
import com.xdev.jadoth.sqlengine.internal.tables.SqlTableIdentity;




/**
 * The Interface TableExpression.
 * A <code>TableExpression</code> instance is any object that can resolve to a valid table identifier for 
 * specifying a certain table in the database.
 * <p>
 * Not that this is NOT equivalent to the "<table expression>" defined in the SQL Standard.<br>. 
 * 
 * @author Thomas Muenz
 */
public interface TableExpression extends Serializable
{
	
	/**
	 * AS.
	 * 
	 * @param newAlias the new alias
	 * @return the table expression
	 * @throws SQLEngineException the sQL engine exception
	 */
	public TableExpression AS(String newAlias) throws SQLEngineException;

	/**
	 * The Class TableExpressionUtils.
	 */
	public static class Utils
	{
		/*
		 * To avoid the need for a public getAlias() in SqlTable
		 */
		/**
		 * Gets the alias.
		 * 
		 * @param tableExpression the table expression
		 * @return the alias
		 */
		public static String getAlias(final TableExpression tableExpression)
		{
			if(tableExpression instanceof SqlQualifiedIdentityWithAlias){
				return ((SqlQualifiedIdentityWithAlias)tableExpression).sql().alias;
			}
			else if(tableExpression instanceof SELECT){
				return ((SELECT)tableExpression).getAlias();
			}
			else {
				return null;
			}
		}

		/*
		 * To avoid the need for a public getDatabaseGateway() in SqlTable
		 */
		/**
		 * Gets the database gateway.
		 * 
		 * @param tableExpression the table expression
		 * @return the database gateway
		 */
		public static DatabaseGateway<?> getDatabaseGateway(final TableExpression tableExpression)
		{
			if(tableExpression instanceof SqlTable){
				return ((SqlTable)tableExpression).db.getDatabaseGateway();
			}
			else if(tableExpression instanceof SELECT){
				return ((SELECT)tableExpression).getDatabaseGatewayForExecution();
			}
			else {
				return SQL.getDefaultDatabaseGateway();
			}
		}
		
		/**
		 * Use with care, as this method swallows ClassCastExceptions. 
		 * 
		 * @param tableExpression
		 * 
		 * @return <code>tableExpression</code> as an <code>SqlTableIdentity</code> if it is one, 
		 * otherwise <tt>null</tt>.
		 */
		public static SqlTableIdentity toSqlTableIdentity(final TableExpression tableExpression)
		{
			if(tableExpression instanceof SqlTableIdentity){
				return (SqlTableIdentity)tableExpression;
			}
			// (19.04.2010 TM)XXX: Maybe call recursively for SELECTs ?
			return null;
		}


	}
}
