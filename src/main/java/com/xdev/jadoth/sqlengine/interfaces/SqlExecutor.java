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
package com.xdev.jadoth.sqlengine.interfaces;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.xdev.jadoth.sqlengine.internal.DatabaseGateway;



public interface SqlExecutor<R>
{	
	public R execute(DatabaseGateway<?> dbc, Statement statement, String sqlStatment) throws SQLException;
	
	
	public static final SqlExecutor<Integer> update = new SqlExecutor<Integer>() {
		@Override
		public Integer execute(DatabaseGateway<?> dbc, Statement statement, String sqlStatment) throws SQLException
		{
			if(statement != null) {
				return statement.executeUpdate(sqlStatment);
			}
			
			try(Statement createdStatement = dbc.getConnection().createStatement()) {
				return createdStatement.executeUpdate(sqlStatment);
			}
		}
	};
	
	public static final SqlExecutor<ResultSet> query = new SqlExecutor<ResultSet>() {
		@Override
		public ResultSet execute(DatabaseGateway<?> dbc, Statement statement, String sqlStatment) throws SQLException
		{
			if(statement == null){
				statement = dbc.getConnection().createStatement();
			}
			return statement.executeQuery(sqlStatment);
		}
	};
	
	public static final SqlExecutor<Object> singleResultQuery = new SqlExecutor<Object>() {
		@Override
		public Object execute(DatabaseGateway<?> dbc, Statement statement, String sqlStatment) throws SQLException
		{
			final ResultSet rs = query.execute(dbc, statement, sqlStatment);
			try {
				if(!rs.next()) return null;
				return rs.getObject(1);
			}
			finally{
				if(rs != null){
					try {
						rs.close();
					}
					catch(final SQLException e) {
						throw dbc.getDbmsAdaptor().parseSqlException(e);
					}
				}
			}
		}
	};
}
