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
package xdev.db.servlet;


import java.util.EnumSet;

import xdev.db.AbstractDBMetaData;
import xdev.db.ColumnMetaData;
import xdev.db.DBException;
import xdev.db.DBMetaData;
import xdev.db.StoredProcedure;
import xdev.db.DBConnection.Query;
import xdev.util.ProgressMonitor;
import xdev.vt.EntityRelationshipModel;


public class ServletDBMetaData extends AbstractDBMetaData
{
	private static final long			serialVersionUID	= 1L;
	
	private final ServletDBDataSource	dataSource;
	/**
	 * disconnected original datasource.
	 */
	private final DBMetaData			original;
	
	
	public ServletDBMetaData(ServletDBDataSource dataSource, DBMetaData original)
	{
		this.dataSource = dataSource;
		this.original = original;
	}
	
	
	@Override
	public String getName()
	{
		return original.getName();
	}
	
	
	@Override
	public String getVersion()
	{
		return original.getVersion();
	}
	
	
	@Override
	public boolean isCaseSensitive() throws DBException
	{
		return original.isCaseSensitive();
	}
	
	
	@Override
	public int getMaxColumnNameLength() throws DBException
	{
		return original.getMaxColumnNameLength();
	}
	
	
	@Override
	public TableInfo[] getTableInfos(ProgressMonitor monitor, EnumSet<TableType> types)
			throws DBException
	{
		ServletDBConnection connection = (ServletDBConnection)dataSource.openConnection();
		try
		{
			return connection.getTableInfos(types);
		}
		finally
		{
			connection.close();
		}
	}
	
	
	@Override
	public TableMetaData[] getTableMetaData(ProgressMonitor monitor, int flags, TableInfo... tables)
			throws DBException
	{
		ServletDBConnection connection = (ServletDBConnection)dataSource.openConnection();
		try
		{
			return connection.getTableMetaData(flags,tables);
		}
		finally
		{
			connection.close();
		}
	}
	
	
	@Override
	public StoredProcedure[] getStoredProcedures(ProgressMonitor monitor) throws DBException
	{
		ServletDBConnection connection = (ServletDBConnection)dataSource.openConnection();
		try
		{
			return connection.getStoredProcedures();
		}
		finally
		{
			connection.close();
		}
	}
	
	
	@Override
	public EntityRelationshipModel getEntityRelationshipModel(ProgressMonitor monitor,
			TableInfo... tables) throws DBException
	{
		ServletDBConnection connection = (ServletDBConnection)dataSource.openConnection();
		try
		{
			return connection.getEntityRelationshipModel(tables);
		}
		finally
		{
			connection.close();
		}
	}
	
	
	@Override
	public boolean equalsType(ColumnMetaData clientColumn, ColumnMetaData dbColumn)
	{
		return original.equalsType(clientColumn,dbColumn);
	}
	
	
	@Override
	public Query[] synchronizeTables(ProgressMonitor monitor, TableChange... changes)
			throws DBException
	{
		if(!dataSource.canExport())
		{
			throw new UnsupportedOperationException();
		}
		
		ServletDBConnection connection = (ServletDBConnection)dataSource.openConnection();
		try
		{
			return connection.synchronizeTables(changes);
		}
		finally
		{
			connection.close();
		}
	}
}
