/**
 * 
 */

package com.xdev.jadoth.sqlengine.internal.tables;

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
			// (12.08.2009)FIXME: check strings for SQL keywords (i.e. Alias
			// "OR" etc)
			this.alias = sqlAlias;
		}
		
		// /**
		// * @return
		// * @see java.lang.Object#toString()
		// */
		// @Override
		// public String toString() {
		// return this.getClass()
		// +": "+this.schema+", "+this.name+", "+this.alias;
		// }
	}
	
}
