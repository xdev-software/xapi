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


import static com.xdev.jadoth.sqlengine.SQL.LANG.TRUNCATE_TABLE;
import static com.xdev.jadoth.sqlengine.SQL.Punctuation._;

import com.xdev.jadoth.sqlengine.INSERT;
import com.xdev.jadoth.sqlengine.SELECT;
import com.xdev.jadoth.sqlengine.SQL;
import com.xdev.jadoth.sqlengine.UPDATE;
import com.xdev.jadoth.sqlengine.exceptions.SQLEngineException;
import com.xdev.jadoth.sqlengine.exceptions.SQLEngineInvalidIdentifier;
import com.xdev.jadoth.sqlengine.internal.SqlColumn;
import com.xdev.jadoth.sqlengine.internal.SqlStar;
import com.xdev.jadoth.sqlengine.internal.interfaces.TableExpression;


/**
 * The Class SqlTableIdentity.
 * 
 * @author Thomas Muenz
 */
public class SqlTableIdentity extends SqlQualifiedIdentityWithAlias implements TableExpression
{
	// /////////////////////////////////////////////////////////////////////////
	// constants //
	// ///////////////////
	
	/**
	 *
	 */
	private static final long	serialVersionUID	= 257988136710513851L;
	
	// /////////////////////////////////////////////////////////////////////////
	// instance fields //
	// //////////////////
	
	/** The util. */
	public final Util			util				= new Util();
	
	
	// /////////////////////////////////////////////////////////////////////////
	// constructors //
	// ///////////////
	/**
	 * Instantiates a new sql table identity.
	 * 
	 * @param schema
	 *            the schema
	 * @param name
	 *            the name
	 * @param alias
	 *            the alias
	 */
	public SqlTableIdentity(final String schema, final String name, final String alias)
	{
		super(new Sql(schema,name,alias));
		this.sql().this$ = this; // workaround inner class instantiation
									// conflict
	}
	
	
	protected SqlTableIdentity(final Sql sql)
	{
		super(sql);
	}
	
	
	protected SqlTableIdentity()
	{
	}
	
	
	// /////////////////////////////////////////////////////////////////////////
	// override methods //
	// ///////////////////
	
	@Override
	public Sql sql()
	{
		return (Sql)super.sql();
	}
	
	
	/**
	 * @param obj
	 * @return
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(final Object obj)
	{
		if(obj == null)
		{
			return false;
		}
		if(obj instanceof SqlTableIdentity)
		{
			final SqlTableIdentity t = (SqlTableIdentity)obj;
			if(this.sql().schema.equals(t.sql().schema) && this.sql().name.equals(t.sql().name))
			{
				return true;
			}
		}
		return false;
	}
	
	
	@Override
	public int hashCode()
	{
		return super.hashCode();
	}
	
	
	// /**
	// * @return
	// * @see java.lang.Object#toString()
	// */
	// @Override
	// public String toString() {
	// return this.util.assembleName(null, false).toString();
	// }
	
	// /////////////////////////////////////////////////////////////////////////
	// declared methods //
	// ///////////////////
	/**
	 * @param newAlias
	 * @return
	 * @throws SQLEngineException
	 * @see com.xdev.jadoth.sqlengine.internal.interfaces.TableExpression#AS(java.lang.String)
	 */
	public SqlTableIdentity AS(final String newAlias) throws SQLEngineException
	{
		return new SqlTableIdentity(this.sql().schema,this.sql().name,newAlias);
	}
	
	
	/**
	 * Gets the top level instance.
	 * 
	 * @return the top level instance
	 */
	protected SqlTableIdentity getTopLevelInstance()
	{
		return this;
	}
	
	
	
	/**
	 * The Class Sql.
	 */
	public static class Sql extends SqlQualifiedIdentityWithAlias.Sql
	{
		private static final long	serialVersionUID	= -127618610696075878L;
		
		private SqlTableIdentity	this$;
		
		
		protected SqlTableIdentity this$()
		{
			return this.this$;
		}
		
		
		protected void this$(final SqlTableIdentity this$)
		{
			this.this$ = this$;
		}
		
		
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
			super(sqlSchema,sqlName,sqlAlias);
		}
		
		
		/**
		 * INSERT.
		 * 
		 * @param columns
		 *            the columns
		 * @param values
		 *            the values
		 * @return the iNSERT
		 */
		public INSERT INSERT(final Object[] columns, final Object... values)
		{
			return new INSERT().INTO(this.this$.getTopLevelInstance()).columns(columns)
					.VALUES(values);
		}
		
		
		/**
		 * UPDATE.
		 * 
		 * @param columns
		 *            the columns
		 * @param values
		 *            the values
		 * @return the uPDATE
		 */
		public UPDATE UPDATE(final Object[] columns, final Object... values)
		{
			return new UPDATE(this.this$.getTopLevelInstance()).columns(columns).setValues(values);
		}
		
		
		/**
		 * TRUNCATE.
		 * 
		 * @return the string
		 */
		public String TRUNCATE()
		{
			return com.xdev.jadoth.Jadoth.glue(TRUNCATE_TABLE,this.this$.getTopLevelInstance()
					.toString());
		}
		
		
		/**
		 * ALL.
		 * 
		 * @return the sql star
		 */
		public SqlStar ALL()
		{
			final SqlStar star = new SqlStar(this.this$.getTopLevelInstance());
			return star;
		}
		
		
		/**
		 * SELECT.
		 * 
		 * @return the sELECT
		 */
		public SELECT SELECT()
		{
			return new SELECT().FROM(this.this$.getTopLevelInstance());
		}
		
		
		/**
		 * COUNT.
		 * 
		 * @return the sELECT
		 */
		public SELECT COUNT()
		{
			final SELECT y = new SELECT().items(SQL.COUNT("*")).FROM(
					this.this$.getTopLevelInstance());
			return y;
		}
		
		
		/**
		 * Qualify column.
		 * 
		 * @param columnName
		 *            the column name
		 * @return the sql column
		 * @throws SQLEngineInvalidIdentifier
		 *             the sQL engine invalid identifier
		 */
		public SqlColumn qualifyColumn(final String columnName) throws SQLEngineInvalidIdentifier
		{
			return new SqlColumn(this.this$.getTopLevelInstance(),columnName);
		}
		
		
		/**
		 * Col.
		 * 
		 * @param expression
		 *            the expression
		 * @return the sql column
		 */
		public SqlColumn col(final Object expression)
		{
			return new SqlColumn(this.this$.getTopLevelInstance(),expression);
		}
		
		
		/**
		 * @return
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString()
		{
			return this.getClass() + ": " + this.schema + ", " + this.name + ", " + this.alias;
		}
	}
	
	
	
	/**
	 * The Class Util.
	 */
	public class Util extends SqlQualifiedIdentity.Util
	{
		private static final long	serialVersionUID	= 6247300956216693085L;
		
		
		/**
		 * Gets the sql.
		 * 
		 * @return the sql
		 */
		public Sql getSql()
		{
			return SqlTableIdentity.this.sql();
		}
		
		
		/**
		 * Wrap with alias.
		 * 
		 * @return the aliased sql table
		 */
		public AliasedSqlTable wrapWithAlias()
		{
			return new AliasedSqlTable(SqlTableIdentity.this.getTopLevelInstance());
		}
		
		
		/**
		 * Assemble name.
		 * 
		 * @param sb
		 *            the sb
		 * @param withAlias
		 *            the with alias
		 * @return the string builder
		 */
		public StringBuilder assembleName(StringBuilder sb, final boolean withAlias)
		{
			sb = super.assembleName(sb);
			if(withAlias)
			{
				final String alias = SqlTableIdentity.this.sql().alias;
				if(alias != null && alias.length() > 0)
				{
					sb.append(_).append(alias);
				}
			}
			return sb;
		}
		
		
		/**
		 * To alias string.
		 * 
		 * @return the string
		 */
		public String toAliasString()
		{
			return this.assembleName(null,true).toString();
		}
	}
	
}
