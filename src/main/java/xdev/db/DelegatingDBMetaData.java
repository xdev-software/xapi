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
package xdev.db;


import java.util.EnumSet;

import xdev.db.DBConnection.Query;
import xdev.util.ProgressMonitor;
import xdev.vt.EntityRelationshipModel;


/**
 * Abstract base class for delegating db metadata.
 * <p>
 * <i><b>This class is not intended to be subclassed by the user!</b></i>
 * 
 * @author XDEV Software
 * @since 4.0
 */
public abstract class DelegatingDBMetaData implements DBMetaData
{
	private final DBMetaData	delegate;
	
	
	public DelegatingDBMetaData(DBMetaData delegate)
	{
		this.delegate = delegate;
	}
	
	
	/**
	 * @return the metadata to which the calls are delegated to
	 */
	public DBMetaData getDelegate()
	{
		return delegate;
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getName()
	{
		return delegate.getName();
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getVersion()
	{
		return delegate.getVersion();
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isCaseSensitive() throws DBException
	{
		return delegate.isCaseSensitive();
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getMaxColumnNameLength() throws DBException
	{
		return delegate.getMaxColumnNameLength();
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public TableInfo[] getTableInfos(ProgressMonitor monitor, EnumSet<TableType> types)
			throws DBException
	{
		return delegate.getTableInfos(monitor,types);
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public TableMetaData[] getTableMetaData(ProgressMonitor monitor, int flags, TableInfo... tables)
			throws DBException
	{
		return delegate.getTableMetaData(monitor,flags,tables);
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public StoredProcedure[] getStoredProcedures(ProgressMonitor monitor) throws DBException
	{
		return delegate.getStoredProcedures(monitor);
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public EntityRelationshipModel getEntityRelationshipModel(ProgressMonitor monitor)
			throws DBException
	{
		return delegate.getEntityRelationshipModel(monitor);
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public EntityRelationshipModel getEntityRelationshipModel(ProgressMonitor monitor,
			TableInfo... tables) throws DBException
	{
		return delegate.getEntityRelationshipModel(monitor,tables);
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equalsType(ColumnMetaData clientColumn, ColumnMetaData dbColumn)
	{
		return delegate.equalsType(clientColumn,dbColumn);
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Query[] synchronizeTables(ProgressMonitor monitor, TableChange... changes)
			throws DBException
	{
		return delegate.synchronizeTables(monitor,changes);
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void dispose()
	{
		delegate.dispose();
	}
}
