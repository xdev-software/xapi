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


import java.text.ParseException;
import java.util.Date;
import java.util.Map;

import xdev.db.event.DBConnectionListener;
import xdev.db.sql.SELECT;
import xdev.db.sql.WritingQuery;


/**
 * Abstract base class for delegating db connections.
 * <p>
 * <i><b>This class is not intended to be subclassed by the user!</b></i>
 * 
 * @author XDEV Software
 * @since 3.1
 */
public abstract class DelegatingDBConnection implements DBConnection
{
	private final DBConnection	delegate;
	
	
	public DelegatingDBConnection(DBConnection delegate)
	{
		this.delegate = delegate;
	}
	
	
	/**
	 * @return the connection to which the calls are delegated to
	 */
	public DBConnection getDelegate()
	{
		return delegate;
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void addDBConnectionListener(DBConnectionListener l)
	{
		delegate.addDBConnectionListener(l);
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void removeDBConnectionListener(DBConnectionListener l)
	{
		delegate.removeDBConnectionListener(l);
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public DBConnectionListener[] getDBConnectionListeners()
	{
		return delegate.getDBConnectionListeners();
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void testConnection() throws DBException
	{
		delegate.testConnection();
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setStoreQueries(boolean store)
	{
		delegate.setStoreQueries(store);
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Query[] getStoredQueries()
	{
		return delegate.getStoredQueries();
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Result query(SELECT select, Object... params) throws DBException
	{
		return delegate.query(select,params);
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Result query(String sql, Object... params) throws DBException
	{
		return delegate.query(sql,params);
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public DBPager getPager(int rowsPerPage, SELECT select, Object... params) throws DBException
	{
		return delegate.getPager(rowsPerPage,select,params);
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getQueryRowCount(String select) throws DBException
	{
		return delegate.getQueryRowCount(select);
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public WriteResult write(WritingQuery query, boolean returnGeneratedKeys, Object... params)
			throws DBException
	{
		return delegate.write(query,returnGeneratedKeys,params);
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public WriteResult write(String sql, boolean returnGeneratedKeys, Object... params)
			throws DBException
	{
		return delegate.write(sql,returnGeneratedKeys,params);
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public WriteResult write(WritingQuery query, String[] columnNames, Object... params)
			throws DBException
	{
		return delegate.write(query,columnNames,params);
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public WriteResult write(String sql, String[] columnNames, Object... params) throws DBException
	{
		return delegate.write(sql,columnNames,params);
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void write(String sql) throws DBException
	{
		delegate.write(sql);
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void call(StoredProcedure procedure, Object... params) throws DBException
	{
		delegate.call(procedure,params);
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void beginTransaction() throws DBException
	{
		delegate.beginTransaction();
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isInTransaction() throws DBException
	{
		return delegate.isInTransaction();
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Savepoint setSavepoint() throws DBException
	{
		return delegate.setSavepoint();
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Savepoint setSavepoint(String name) throws DBException
	{
		return delegate.setSavepoint(name);
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void releaseSavepoint(Savepoint savepoint) throws DBException
	{
		delegate.releaseSavepoint(savepoint);
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void commit() throws DBException
	{
		delegate.commit();
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void rollback() throws DBException
	{
		delegate.rollback();
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void rollback(Savepoint savepoint) throws DBException
	{
		delegate.rollback(savepoint);
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void close() throws DBException
	{
		delegate.close();
	}
	
	
	@Override
	public void createTable(String tableName, String primaryKey, Map columnMap,
			boolean isAutoIncrement, Map foreignKeys) throws Exception
	{
		delegate.createTable(tableName,primaryKey,columnMap,isAutoIncrement,foreignKeys);
		
	}
	
	
	@Override
	public Date getServerTime() throws DBException, ParseException
	{
		return delegate.getServerTime();
	}
}
