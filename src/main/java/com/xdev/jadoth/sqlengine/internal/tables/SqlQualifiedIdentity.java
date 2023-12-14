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
package com.xdev.jadoth.sqlengine.internal.tables;


import java.io.Serializable;


/**
 * @author Thomas Muenz
 * 
 */
public class SqlQualifiedIdentity implements Serializable
{
	private static final long	serialVersionUID	= 4005919971795331900L;
	
	private Sql					sql;
	public final Util			util				= new Util();
	
	
	/**
	 * @param sql
	 */
	public SqlQualifiedIdentity(final String sqlSchema, final String sqlName)
	{
		super();
		this.sql = new Sql(sqlSchema,sqlName);
	}
	
	
	public SqlQualifiedIdentity(final Sql sql)
	{
		super();
		this.sql = sql;
	}
	
	
	protected SqlQualifiedIdentity()
	{
	}
	
	
	public Sql sql()
	{
		return this.sql;
	}
	
	
	/**
	 * @return
	 */
	@Override
	public String toString()
	{
		return this.sql.cachedFullString;
	}
	
	
	
	/**
	 * The Class Sql.
	 */
	public static class Sql implements Serializable
	{
		private static final long	serialVersionUID	= 1628269331731654935L;
		
		/** The schema. */
		public final String			schema;
		
		/** The name. */
		public final String			name;
		
		private final String		cachedFullString;
		
		
		/**
		 * Instantiates a new sql.
		 * 
		 * @param sqlSchema
		 *            the sql schema
		 * @param sqlName
		 *            the sql name
		 * @param sqlAlias
		 *            the sql alias
		 */
		protected Sql(final String sqlSchema, final String sqlName)
		{
			super();
			this.schema = sqlSchema;
			this.name = sqlName;
			this.cachedFullString = sqlSchema == null || sqlSchema.length() == 0 ? sqlName
					: sqlSchema + '.' + sqlName;
		}
		
		
		/**
		 * @return
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString()
		{
			return this.cachedFullString;
		}
	}
	
	
	
	public class Util implements Serializable
	{
		private static final long	serialVersionUID	= -6056381022961127711L;
		
		
		/**
		 * Assemble name.
		 * 
		 * @param sb
		 *            the sb
		 * @param withAlias
		 *            the with alias
		 * @return the string builder
		 */
		public StringBuilder assembleName(final StringBuilder sb)
		{
			return sb.append(SqlQualifiedIdentity.this.sql.toString());
		}
		
	}
	
}
