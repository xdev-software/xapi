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

package com.xdev.jadoth.sqlengine.internal.tables;


import com.xdev.jadoth.sqlengine.SQL;


/**
 * @author Thomas Muenz
 * 
 */
public class SqlQualifiedIdentityWithAlias extends SqlQualifiedIdentity
{
	private static final long	serialVersionUID	= -6005387749244898441L;
	
	
	public SqlQualifiedIdentityWithAlias(final String schema, final String name, final String alias)
	{
		super(new Sql(schema,name,SQL.util.escapeReservedWord(((alias != null) ? alias : SQL.util
				.guessAlias(name)))));
	}
	
	
	public SqlQualifiedIdentityWithAlias(final Sql sql)
	{
		super(sql);
	}
	
	
	protected SqlQualifiedIdentityWithAlias()
	{
	}
	
	
	@Override
	public Sql sql()
	{
		return (Sql)super.sql();
	}
	
	
	
	/**
	 * The Class Sql.
	 */
	public static class Sql extends SqlQualifiedIdentity.Sql
	{
		private static final long	serialVersionUID	= -2031407307705928472L;
		
		/** The alias. */
		public final String			alias;
		
		
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
		protected Sql(final String sqlSchema, final String sqlName, final String sqlAlias)
		{
			super(sqlSchema,sqlName);
			this.alias = sqlAlias;
		}
		
	}
	
}
