package com.xdev.jadoth.sqlengine.types;

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

import com.xdev.jadoth.lang.reflection.annotations.Label;
import com.xdev.jadoth.sqlengine.SQL;
import com.xdev.jadoth.sqlengine.dbms.DbmsAdaptor;
import com.xdev.jadoth.sqlengine.dbms.DbmsDMLAssembler;
import com.xdev.jadoth.sqlengine.dbms.standard.StandardDbmsAdaptor;
import com.xdev.jadoth.sqlengine.internal.DatabaseGateway;
import com.xdev.jadoth.sqlengine.internal.interfaces.TableExpression;
import com.xdev.jadoth.sqlengine.internal.interfaces.TableExpression.Utils;

public interface TableQuery extends Query
{
	public TableExpression getTable();

	
	
	abstract class Implementation extends Query.Implementation implements TableQuery
	{
		private static final long serialVersionUID = -6871759899231204565L;

		
		protected Implementation()
		{
			super();
		}
		
		protected Implementation(TableQuery.Implementation copySource)
		{
			super(copySource);
		}

		@Override
		public DatabaseGateway<?> getDatabaseGatewayForExecution()
		{
			final DatabaseGateway<?> dbg = super.getDatabaseGatewayForExecution();
			if(dbg!= null) return dbg;

			return Utils.getDatabaseGateway(this.getTable());
		}
		
		
		@Override
		@Label(LABEL_assembleQuery)
		protected StringBuilder assemble(
			DbmsDMLAssembler<?> dmlAssembler, final StringBuilder sb, final int indentLevel, final int flags
		)
		{			
			if(dmlAssembler == null){
				dmlAssembler = StandardDbmsAdaptor.getSingletonStandardDbmsAdaptor().getDMLAssembler();
				final DbmsAdaptor<?> staticDefaultDbms = SQL.getDefaultDBMS();
				if(staticDefaultDbms != null){
					dmlAssembler = staticDefaultDbms.getDMLAssembler();
				}
				lookupQueryAssembler: { //pretty cool: resolved the if nesting
					final TableExpression table = this.getTable();
					if(table == null) break lookupQueryAssembler;

					final DatabaseGateway<?> dbcon = TableExpression.Utils.getDatabaseGateway(table);
					if(dbcon == null) break lookupQueryAssembler;

					final DbmsAdaptor<?> dbmsa = dbcon.getDbmsAdaptor();
					if(dbmsa == null) break lookupQueryAssembler;

					dmlAssembler = dbmsa.getDMLAssembler();
				}
			}			
			
			final boolean reallySingleLine = this.isSingleLineMode() || isSingleLine(flags);
			final boolean reallyPacked = this.isPacked() || isPacked(flags);

			dmlAssembler.assembleQuery(this, sb, indentLevel, bitSingleLine(reallySingleLine)|bitPacked(reallyPacked));
			return sb;
		}
	
	}
	
	
	
}
