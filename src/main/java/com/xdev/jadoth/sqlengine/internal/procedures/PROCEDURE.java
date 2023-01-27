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
/**
 * 
 */
package com.xdev.jadoth.sqlengine.internal.procedures;

import static com.xdev.jadoth.lang.reflection.JaReflect.isSubClassOf;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;

import com.xdev.jadoth.Jadoth;
import com.xdev.jadoth.lang.reflection.JaReflect;
import com.xdev.jadoth.sqlengine.CALL;
import com.xdev.jadoth.sqlengine.internal.tables.SqlQualifiedIdentity;


/**
 * @author Thomas Muenz
 *
 */
public class PROCEDURE<R> extends SqlQualifiedIdentity
{
	///////////////////////////////////////////////////////////////////////////
	// static methods   //
	/////////////////////
	
	/**
	 * Alias for <code>prepareParametrizedCallString(storedProcedureName, parameterCount, false)</code>.
	 */
	public static final String prepareParametrizedCallString(
		final String storedProcedureName, final int argumentParameterCount
	)
	{
		return prepareParametrizedCallString(storedProcedureName, argumentParameterCount, false);
	}
	
	/**
	 * @param storedProcedureName the name of the stored procedure to be called
	 * @param argumentParameterCount the amount of argument parameters ("?") to be set.
	 * @param whether or not the call shall contain a result parameters ("? = call ...") 
	 * @return a string suitable for use in a JDBC CallableStatement representing the 
	 * call of <code>storedProcedureName</code> with parameter placeholders.
	 */
	public static final String prepareParametrizedCallString(
		final String storedProcedureName, int argumentParameterCount, final boolean hasResultParameter
	)
	{
		//call query start
		final StringBuilder sb = new StringBuilder(1024).append('{');
		if(hasResultParameter){
			sb.append("? = ");
		}
		sb.append("call ").append(storedProcedureName.toString()).append('(');
		
		//argument parameters assembly, if any
		if(argumentParameterCount-- > 0){
			sb.append('?');
			while(argumentParameterCount --> 0){
				sb.append(',').append('?');
			}
		}
		
		//finalize call query
		sb.append(')').append('}');		
		return sb.toString();		
	}
	
	
	
	
	
	///////////////////////////////////////////////////////////////////////////
	// instance fields  //
	/////////////////////
	
	private final ArrayList<SqlParameter> parameters = new ArrayList<SqlParameter>();
	public final Jdbc jdbc = new Jdbc();
	public final Sql sql;
	
	
	///////////////////////////////////////////////////////////////////////////
	// constructors     //
	/////////////////////

	/**
	 * @param sqlName
	 */
	public PROCEDURE(final String sqlName)
	{
		this(null, sqlName);
	}
	
	/**
	 * @param sqlSchema
	 * @param sqlName
	 */
	public PROCEDURE(final String sqlSchema, final String sqlName)
	{
		super(sqlSchema, sqlName);
		this.sql = new Sql(sqlSchema, sqlName);
	}
	

	
	/**
	 * @param sqlSchema
	 * @param sqlName
	 */
	public PROCEDURE(final String sqlSchema, final String sqlName, final SqlParameter... parameters)
	{
		this(sqlSchema, sqlName);
		Jadoth.addArray(this.parameters, parameters);
	}
	



	protected void initializeParameterFields(final Class<? extends PROCEDURE<?>> declaringClass)
	{
		for(final Field f : declaringClass.getDeclaredFields()){
			if(Modifier.isStatic(f.getModifiers()) || !isSubClassOf(f.getType(), SqlParameter.class)) continue;
				
			final SqlParameter param = (SqlParameter)JaReflect.getFieldValue(f, this);
			param.setOwner(this);
			if(param.getName() == null){
				param.setName(f.getName());
			}			
		}
	}
	
	
	
	///////////////////////////////////////////////////////////////////////////
	// getters          //
	/////////////////////
	
	
	
	
	
	///////////////////////////////////////////////////////////////////////////
	// override methods //
	/////////////////////
	
	
	
	
	///////////////////////////////////////////////////////////////////////////
	// declared methods //
	/////////////////////
		
	
	
	
	
	
	public class Jdbc
	{	
		protected Jdbc()
		{
			super();
		}
		
		public String prepareCallString()
		{
			return prepareParametrizedCallString(PROCEDURE.this.sql.toString(), PROCEDURE.this.parameters.size());		
		}
	}
	
	public class Sql extends SqlQualifiedIdentity.Sql
	{

		/**
		 * @param sqlSchema
		 * @param sqlName
		 */
		protected Sql(final String sqlSchema, final String sqlName)
		{
			super(sqlSchema, sqlName);
		}
		
		public CALL<R> CALL()
		{
			return new CALL<R>(PROCEDURE.this);
		}
		public CALL<R> CALL(final Object... instancePrepareParameters)
		{
			return new CALL<R>(PROCEDURE.this).addParameters(instancePrepareParameters);
		}
		
	}
	
}
