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

package com.xdev.jadoth.sqlengine.internal.procedures;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.sql.ResultSet;
import java.util.ArrayList;

import com.xdev.jadoth.lang.reflection.JaReflect;
import com.xdev.jadoth.sqlengine.CALL_ResultSet;
import com.xdev.jadoth.sqlengine.exceptions.SQLEngineException;
import com.xdev.jadoth.sqlengine.interfaces.ColumnQualifier;
import com.xdev.jadoth.sqlengine.internal.SqlField;
import com.xdev.jadoth.sqlengine.internal.interfaces.TableExpression;



public class PROCEDURE_ResultSet extends PROCEDURE<ResultSet> implements TableExpression, ColumnQualifier 
{	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3089859376748729378L;


	///////////////////////////////////////////////////////////////////////////
	// static methods   //
	/////////////////////
	
	protected static SqlField SqlField()
	{
		return SqlField(null);
	}
	protected static SqlField SqlField(final String name)
	{
		return new SqlField(name, null, 1, null, null, false, false, null);
	}
	


	///////////////////////////////////////////////////////////////////////////
	// instance fields  //
	/////////////////////
	
	private final ArrayList<String> columnNames = new ArrayList<String>(30);
	private final ArrayList<SqlField> sqlFields = new ArrayList<SqlField>(30);

	
	
	///////////////////////////////////////////////////////////////////////////
	// constructors     //
	/////////////////////
	
	public PROCEDURE_ResultSet(final Class<?> storedProcedureClass)
	{
		this(null, storedProcedureClass.getSimpleName());
	}	
	public PROCEDURE_ResultSet(final String name)
	{
		this(null, name);
	}
	public PROCEDURE_ResultSet(final String schema, final String name)
	{
		super(schema, name);
	}
	
	
	
	///////////////////////////////////////////////////////////////////////////
	// getters          //
	/////////////////////
	

	

	///////////////////////////////////////////////////////////////////////////
	// override methods //
	/////////////////////
	
	@Override
	public String qualifyColumnName(final String unqualifiedColumnName)
	{
		return this.toString()+'.'+unqualifiedColumnName;
	}
	
	@Override
	public TableExpression AS(final String newAlias) throws SQLEngineException
	{
		throw new UnsupportedOperationException(
			PROCEDURE_ResultSet.class.getSimpleName() +" is a "+TableExpression.class.getSimpleName()+" only as a workaround currently"
		);
	}
	
	
	
	public class Sql extends PROCEDURE<ResultSet>.Sql
	{

		/**
		 * @param procedure
		 * @param sqlSchema
		 * @param sqlName
		 */
		protected Sql(final PROCEDURE_ResultSet procedure, final String sqlSchema, final String sqlName)
		{
			procedure.super(sqlSchema, sqlName);
		}
		
		@Override	
		public CALL_ResultSet CALL()
		{
			return new CALL_ResultSet(PROCEDURE_ResultSet.this);
		}
		
		@Override	
		public CALL_ResultSet CALL(final Object... instancePrepareParameters)
		{
			return new CALL_ResultSet(PROCEDURE_ResultSet.this).addParameters(instancePrepareParameters);
		}
		
	}
	
	
	
	
	///////////////////////////////////////////////////////////////////////////
	// declared methods //
	/////////////////////
		
	
	protected void initializeSqlFields()
	{
		for (final Field f : JaReflect.getAllFields(this.getClass())) {
			try {
				if(f.getType() != SqlField.class || Modifier.isStatic(f.getModifiers())) continue; //skip all non-SqlField and all static fields
		
				final SqlField sf = ((SqlField)JaReflect.getFieldValue(f, this));
				sf.setOwner(this);
				if(sf.getName() == null) {
					sf.setName(f.getName());
				}
				this.columnNames.add(sf.getName());
				this.sqlFields.add(sf);
			}
			catch (final NullPointerException e) {
				//if anything is missing, just skip this sql field.
			}
		}
	}
	
	public Iterable<String> iterateColumnNames()
	{
		return this.columnNames;
	}
	
	public Iterable<SqlField> iterateSqlFields()
	{
		return this.sqlFields;
	}
	
	

	
}
