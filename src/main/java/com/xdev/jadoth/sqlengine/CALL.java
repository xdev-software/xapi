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

import static com.xdev.jadoth.Jadoth.addArray;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;

import com.xdev.jadoth.collections.GrowList;
import com.xdev.jadoth.lang.wrapperexceptions.SQLRuntimeException;
import com.xdev.jadoth.sqlengine.internal.procedures.PROCEDURE;
import com.xdev.jadoth.sqlengine.types.Query;
import com.xdev.jadoth.sqlengine.types.ValueReadingQuery;


/**
 * @author Thomas Muenz
 *
 */
public class CALL<R> extends Query.Implementation implements ValueReadingQuery<R> 
{
	///////////////////////////////////////////////////////////////////////////
	// constants        //
	/////////////////////
	
	private static final long serialVersionUID = 902214029210905157L;
	
	
	
	///////////////////////////////////////////////////////////////////////////
	// instance fields //
	////////////////////
	
	private final Class<R> returnType;
	private final PROCEDURE<R> storedProcedure; 
	private final GrowList<Object> arguments;
	

	
	///////////////////////////////////////////////////////////////////////////
	// constructors //
	/////////////////
	
	public CALL(final Class<R> returnType, final PROCEDURE<R> storedProcedure)
	{
		super();
		this.returnType = returnType;
		this.storedProcedure = storedProcedure;
		this.arguments = new GrowList<Object>(8);
	}
	
	public CALL(final PROCEDURE<R> storedProcedure)
	{
		this(null, storedProcedure);
	}
	
	private CALL(final CALL<R> copySource)
	{
		super();
		this.returnType = copySource.returnType;
		this.storedProcedure = copySource.storedProcedure;
		this.arguments = new GrowList<Object>(copySource.arguments);
	}
	
	
	
	///////////////////////////////////////////////////////////////////////////
	// declared methods //
	/////////////////////
	
	public CALL<R> addParameter(final Object parameter)
	{
		this.arguments.add(parameter);
		return this;
	}
	
	public CALL<R> addParameters(final Object... parameters)
	{
		addArray(this.arguments, parameters);
		return this;
	}
	
	public CALL<R> parameters(final Object... parameters)
	{
		this.clearParameters();
		this.addParameters(parameters);
		return this;
	}
	
	public CALL<R> clearParameters()
	{
		this.arguments.clear();
		return this;
	}

	

	///////////////////////////////////////////////////////////////////////////
	// override methods //
	/////////////////////
	
	/**
	 * @return
	 */
	@Override
	public String keyword()
	{
		return SQL.LANG.CALL;
	}

	/**
	 * @return
	 */
	@Override
	public CALL<R> copy()
	{
		return new CALL<R>(this);
	}
	

	@Override
	public String assemble(final Object... prepareParameters)
	{
		final StringBuilder sb = new StringBuilder(512).append('{');
		if(this.returnType == Void.class){
			sb.append('?').append(' ').append('=');
		}
		sb.append(' ').append(this.keyword()).append(' ').append(this.storedProcedure.toString()).append('(');
		
		
		boolean notEmpty = false;
		
		if(prepareParameters == null){
			//simple mode: no additional arguments passed
			for(final Object o : this.arguments){
				if(notEmpty) sb.append(','); else notEmpty = true;
				sb.append(o);				
			}
		}
		else {
			//complex mode: combine instance arguments and passed arguments: replace instance argument "null" with next passed argument or ultimately "NULL". 
			int p = 0;		
			
			for(final Object o : this.arguments){
				if(notEmpty) sb.append(','); else notEmpty = true;
			
				if(o != null){
					sb.append(o.toString());
				}
				else if(p < prepareParameters.length){
					sb.append(prepareParameters[p++]);
				}
				else {
					sb.append(SQL.LANG.NULL);
				}				
			}
			//append remaining passed argument, if available 
			while(p < prepareParameters.length){
				if(notEmpty) sb.append(','); else notEmpty = true;				
				sb.append(prepareParameters[p++]);
			}
		}
				
		sb.append(')').append(' ').append('}');
		return sb.toString();
	}	
	
	@Override
	public R execute(final Object... prepareParameters) throws SQLRuntimeException
	{
		try{
			return this.jdbcGetReturnValue(this.prepare(prepareParameters));
		}
		catch(final SQLException e){
			throw new SQLRuntimeException(e);
		}
	}
	
	
	/**
	 * Calls <code>callableStatement.getObject(1)</code> by default. Override this method to specify the return value retrieval
	 */
	@SuppressWarnings("unchecked")
	protected R jdbcGetReturnValue(final CallableStatement callableStatement) throws SQLException
	{
		return (R)callableStatement.getObject(1);
	}
	
	protected CallableStatement jdbcPrepareCall(final Connection connection, final String sqlQuery) throws SQLException
	{
		return connection.prepareCall(sqlQuery);
	}
	
	
	public CallableStatement prepare(final Object... prepareParameters) throws SQLRuntimeException
	{
		final Connection con = this.getDatabaseGatewayForExecution().getConnection();
		try {
			return this.jdbcPrepareCall(con, this.assemble(prepareParameters));
		}
		catch(final SQLException e){
			throw new SQLRuntimeException(e);
		}
	}	
	

}
