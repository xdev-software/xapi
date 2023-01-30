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
package com.xdev.jadoth.sqlengine;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.xdev.jadoth.lang.wrapperexceptions.SQLRuntimeException;
import com.xdev.jadoth.sqlengine.internal.SqlField;
import com.xdev.jadoth.sqlengine.internal.procedures.PROCEDURE;
import com.xdev.jadoth.sqlengine.types.TableReadingQuery;


/**
 * @author Thomas Muenz
 *
 */
public class CALL_ResultSet extends CALL<ResultSet> implements TableReadingQuery
{
	private static final long serialVersionUID = 26639199643273481L;
	
	
	
	private final int resultSetType;
	

	public CALL_ResultSet(final PROCEDURE<ResultSet> storedProcedure)
	{
		this(storedProcedure, ResultSet.TYPE_FORWARD_ONLY);
	}
	
	public CALL_ResultSet(final PROCEDURE<ResultSet> storedProcedure, final int resultSetType)
	{
		super(ResultSet.class, storedProcedure);
		this.resultSetType = resultSetType;
	}


	@Override
	public CALL_ResultSet addParameter(final Object parameter)
	{
		super.addParameter(parameter);
		return this;
	}
	
	@Override
	public CALL_ResultSet addParameters(final Object... parameters)
	{
		super.addParameters(parameters);
		return this;
	}
	
	
	
	
	@Override
	protected CallableStatement jdbcPrepareCall(final Connection connection, final String sqlQuery) throws SQLException
	{
		return connection.prepareCall(sqlQuery, this.resultSetType, ResultSet.CONCUR_READ_ONLY);
	}
	
	@Override
	protected ResultSet jdbcGetReturnValue(final CallableStatement callableStatement) throws SQLException
	{
		return callableStatement.getResultSet();
	}
	
	@Override
	public ResultSet execute(final Object... prepareParameters) throws SQLRuntimeException
	{
		//safety of this cast is ensured by getReturnValue() defined above.
		return super.execute(prepareParameters);
	}
	
	
	
	public Iterable<String> iterateColumnNames()
	{
		return null;
	}
	
	public Iterable<SqlField> iterateSqlFields()
	{
		return null;
	}
	
	
	
	
}
