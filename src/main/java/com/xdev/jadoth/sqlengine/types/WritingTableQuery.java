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
package com.xdev.jadoth.sqlengine.types;

import com.xdev.jadoth.sqlengine.exceptions.SQLEngineException;
import com.xdev.jadoth.sqlengine.internal.tables.SqlTableIdentity;

/**
 * @author Thomas Muenz
 *
 */
public interface WritingTableQuery extends TableQuery
{
	@Override
	public SqlTableIdentity getTable();
	
	@Override
	public Integer execute(final Object... parameters) throws SQLEngineException;
	
	
	
	abstract class Implementation implements WritingTableQuery
	{		
		///////////////////////////////////////////////////////////////////////////
		// instance fields //
		////////////////////
		private SqlTableIdentity table;
		
		
		
		///////////////////////////////////////////////////////////////////////////
		// constructors //
		/////////////////
		protected Implementation()
		{
			this((SqlTableIdentity)null);
		}
		protected Implementation(final SqlTableIdentity table)
		{
			super();
			this.table = table;
		}		
		protected Implementation(final Implementation copySource)
		{
			super();
			this.table = copySource.table;
		}
		

		
		///////////////////////////////////////////////////////////////////////////
		// getters          //
		/////////////////////
		@Override
		public SqlTableIdentity getTable()
		{
			return this.table;
		}
		
		
		
		///////////////////////////////////////////////////////////////////////////
		// setters          //
		/////////////////////
		protected void setTable(final SqlTableIdentity table)
		{
			this.table = table;
		}
		
	}
	
}
