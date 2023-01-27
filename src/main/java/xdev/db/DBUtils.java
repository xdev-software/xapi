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


import java.beans.Beans;

import xdev.db.sql.Column;
import xdev.db.sql.Functions;
import xdev.db.sql.SELECT;
import xdev.db.sql.Table;
import xdev.db.sql.WHERE;
import xdev.db.sql.WritingQuery;
import xdev.lang.LibraryMember;
import xdev.lang.Nullable;
import xdev.util.DataSource;
import xdev.util.DataSources;
import xdev.util.logging.LoggerFactory;
import xdev.util.logging.XdevLogger;
import xdev.vt.VirtualTable;


/**
 * <p>
 * The <code>DBUtils</code> class provides utility methods for handling database
 * access.
 * </p>
 * 
 * @since 2.0
 * 
 * @author XDEV Software
 */
@LibraryMember
public final class DBUtils
{
	
	/**
	 * Logger instance for this class.
	 */
	private static final XdevLogger	log	= LoggerFactory.getLogger(DBUtils.class);
	
	
	/**
	 * <p>
	 * <code>DBUtils</code> instances can not be instantiated. The class should
	 * be used as utility class: <code>DBUtils.getDataSource("bar");</code>.
	 * </p>
	 */
	private DBUtils()
	{
	}
	
	/**
	 * holds the current {@link DBDataSource}.
	 */
	private static DBDataSource<?>	currentDataSource	= null;
	
	static
	{
		DBDataSource<?>[] dataSources = DataSources.getDataSources(DBDataSource.class);
		if(dataSources != null && dataSources.length > 0)
		{
			currentDataSource = dataSources[0];
		}
	}
	
	
	/**
	 * Sets the current {@link DBDataSource}.
	 * 
	 * @param name
	 *            name of the {@link DBDataSource} to be as current
	 *            {@link DBDataSource}
	 * @throws DBException
	 *             if there is no valid {@link DBDataSource} for the provided
	 *             <code>name</code>.
	 */
	public static void setDataSource(String name) throws DBException
	{
		setDataSource(getDataSource(name));
	}
	
	
	/**
	 * Sets the current {@link DBDataSource}.
	 * 
	 * @param dataSource
	 *            {@link DBDataSource} to be as current {@link DBDataSource}
	 * @throws DBException
	 *             if <code>dataSource</code> is not a valid
	 *             {@link DBDataSource}
	 */
	public static void setDataSource(DBDataSource<?> dataSource) throws DBException
	{
		currentDataSource = dataSource;
	}
	
	
	/**
	 * Returns the {@link DBDataSource} for the provided <code>name</code>.
	 * 
	 * @param name
	 *            name to get the {@link DBDataSource} for
	 * @return the {@link DBDataSource} for the provided <code>name</code>.
	 * @throws DBException
	 *             if there is no valid {@link DBDataSource} for the provided
	 *             <code>name</code>.
	 */
	private static DBDataSource getDataSource(String name) throws DBException
	{
		if(Beans.isDesignTime())
		{
			return NullDBDataSource.instance;
		}
		
		if(name == null || name.length() == 0)
		{
			return currentDataSource;
		}
		
		DataSource<?> dataSource = DataSources.getDataSource(name);
		
		if(dataSource == null)
		{
			throw new DBException(null,"DataSource '" + name + "' not found");
		}
		
		if(dataSource instanceof DBDataSource)
		{
			return (DBDataSource<?>)dataSource;
		}
		
		throw new DBException(null,"DataSource '" + name + "' is not a DBDataSource");
	}
	
	
	/**
	 * Returns the current {@link DBDataSource}.
	 * 
	 * @return the current {@link DBDataSource}.
	 * 
	 * @throws DBException
	 *             if the current {@link DBDataSource} is not specified.
	 * 
	 * @see #setDataSource(DBDataSource)
	 * @see #setDataSource(String)
	 */
	public static DBDataSource getCurrentDataSource() throws DBException
	{
		if(Beans.isDesignTime())
		{
			return NullDBDataSource.instance;
		}
		
		ensureDataSourceSpecified();
		
		return currentDataSource;
	}
	
	
	/**
	 * Checks if {@value #currentDataSource} is specified.
	 * 
	 * 
	 * @throws DBException
	 *             if the current {@link DBDataSource} is not specified.
	 * 
	 * @see #setDataSource(DBDataSource)
	 * @see #setDataSource(String)
	 */
	private static void ensureDataSourceSpecified() throws DBException
	{
		if(currentDataSource == null)
		{
			throw new DBException(null,"No data connection specified");
		}
	}
	
	
	/**
	 * Returns the maximum value of the provided column <code>column</code> of
	 * table <code>table</code>.
	 * 
	 * @param table
	 *            name of the table where <code>column</code> is located
	 * @param column
	 *            name of the column to get the maximum value of
	 * @return the maximum value of the provided column <code>column</code> of
	 *         table <code>table</code>.
	 * @throws DBException
	 *             if the maximum value of the provided column could not be
	 *             obtained
	 */
	public static int getMaxValue(String table, String column) throws DBException
	{
		SELECT select = new SELECT().columns(Functions.MAX(new Column(column))).FROM(
				new Table(table));
		DBConnection<?> connection = getCurrentDataSource().openConnection();
		try
		{
			Result result = connection.query(select);
			try
			{
				return getSingleInt(result);
			}
			finally
			{
				result.close();
			}
		}
		finally
		{
			connection.close();
		}
	}
	
	
	/**
	 * Returns the row count of for the provided table <code>table</code>.
	 * 
	 * @param table
	 *            name of table to get the row count for
	 * 
	 * 
	 * @return Returns the row count of for the provided table
	 *         <code>table</code>.
	 * 
	 * @throws DBException
	 *             if the row count for the provided table could not be obtained
	 */
	public static int getRowCount(String table) throws DBException
	{
		return getRowCount(table,null);
	}
	
	
	/**
	 * Returns the row count of for the provided table <code>table</code>
	 * Including only records to which the provided <code>filter</code> apply.
	 * 
	 * @param table
	 *            name of table to get the row count for
	 * @param filter
	 *            {@link WHERE} condition to which the record must apply.
	 * @return Returns the row count of for the provided table
	 *         <code>table</code> Including only records to which the provided
	 *         <code>filter</code> apply.
	 * @throws DBException
	 *             if the row count for the provided table could not be obtained
	 */
	public static int getRowCount(String table, WHERE filter) throws DBException
	{
		SELECT select = new SELECT().columns(Functions.COUNT()).FROM(new Table(table));
		if(filter != null)
		{
			select.WHERE(filter);
		}
		DBConnection<?> connection = getCurrentDataSource().openConnection();
		try
		{
			Result result = connection.query(select);
			try
			{
				return getSingleInt(result);
			}
			finally
			{
				result.close();
			}
		}
		finally
		{
			connection.close();
		}
	}
	
	
	/**
	 * Fills the provided {@link VirtualTable} <code>vt</code> with the result
	 * of the provided sql statement <code>sql</code>.
	 * 
	 * @param vt
	 *            {@link VirtualTable} to fill
	 * @param sql
	 *            {@link String} sql statement
	 * @param params
	 *            parameters for the sql stement <code>sql</code> [optional]
	 * @return the filled {@link VirtualTable} <code>vt</code>
	 * @throws DBException
	 *             if an error occurs executing the sql statement
	 */
	public static VirtualTable query(VirtualTable vt, String sql, Object... params)
			throws DBException
	{
		Result result = query(sql,params);
		try
		{
			return query(vt,result);
		}
		finally
		{
			result.close();
		}
	}
	
	
	/**
	 * Fills the provided {@link VirtualTable} <code>vt</code> with the result
	 * of the provided {@link SELECT} instance <code>select</code>.
	 * 
	 * @param vt
	 *            {@link VirtualTable} to fill
	 * @param select
	 *            {@link SELECT} sql statement
	 * @param params
	 *            parameters for the sql stement <code>select</code> [optional]
	 * @return the filled {@link VirtualTable} <code>vt</code>
	 * @throws DBException
	 *             if an error occurs executing the sql statement
	 */
	public static VirtualTable query(VirtualTable vt, SELECT select, Object... params)
			throws DBException
	{
		Result result = query(select,params);
		try
		{
			return query(vt,result);
		}
		finally
		{
			result.close();
		}
	}
	
	
	/**
	 * Fills the provided {@link VirtualTable} <code>vt</code> with the provided
	 * result <code>rs</code>.
	 * 
	 * @param vt
	 *            {@link VirtualTable} to fill
	 * @param rs
	 *            {@link Result} result
	 * @return the filled {@link VirtualTable} <code>vt</code>
	 * @throws DBException
	 *             if a database related error occurs while iterating over the
	 *             result
	 */
	public static VirtualTable query(VirtualTable vt, Result rs) throws DBException
	{
		int[] colsToFill = null;
		
		if(vt == null)
		{
			int cc = rs.getColumnCount();
			colsToFill = new int[cc];
			vt = new VirtualTable(rs);
			for(int col = 0; col < colsToFill.length; col++)
			{
				colsToFill[col] = col;
			}
		}
		
		vt.addData(rs,colsToFill);
		
		return vt;
	}
	
	
	/**
	 * Executes the provided SQL statement and returns a open {@link Result}.
	 * The {@link Result} must be closed by the caller.
	 * 
	 * @param sql
	 *            SQL statement to execute
	 * @param params
	 *            parameter for the SQL statement
	 * @return a open {@link Result}. The {@link Result} must be closed by the
	 *         caller.
	 * @throws DBException
	 *             if a database related error occurs
	 */
	public static Result query(String sql, Object... params) throws DBException
	{
		ensureDataSourceSpecified();
		
		DBConnection<?> connection = currentDataSource.openConnection();
		Result result = connection.query(sql,params);
		return new CloseConnectionResult(result,connection);
	}
	
	
	/**
	 * Executes the provided {@link SELECT} statement and returns a open
	 * {@link Result}. The {@link Result} must be closed by the caller.
	 * 
	 * @param select
	 *            {@link SELECT} statement to execute
	 * @param params
	 *            parameter for the {@link SELECT} statement
	 * @return a open {@link Result}. The {@link Result} must be closed by the
	 *         caller.
	 * @throws DBException
	 *             if a database related error occurs
	 */
	public static Result query(SELECT select, Object... params) throws DBException
	{
		ensureDataSourceSpecified();
		
		DBConnection<?> connection = currentDataSource.openConnection();
		Result result = connection.query(select,params);
		return new CloseConnectionResult(result,connection);
	}
	
	
	public static Object querySingleValue(SELECT select, Object... params) throws DBException
	{
		Result result = query(select,params);
		try
		{
			if(result.next())
			{
				return result.getObject(0);
			}
			
			return null;
		}
		finally
		{
			result.close();
		}
	}
	
	
	public static Object querySingleValue(String sql, Object... params) throws DBException
	{
		Result result = query(sql,params);
		try
		{
			if(result.next())
			{
				return result.getObject(0);
			}
		}
		finally
		{
			result.close();
		}
		
		return null;
	}
	
	
	public static int getSingleInt(Result result) throws DBException
	{
		int count = 0;
		
		try
		{
			if(result.getColumnCount() > 0 && result.next())
			{
				count = result.getInt(0);
			}
			else
			{
				throw new DBException(result.getDataSource(),"no data available");
			}
		}
		catch(DBException e)
		{
			throw e;
		}
		catch(Exception e)
		{
			throw new DBException(result.getDataSource(),e);
		}
		finally
		{
			result.close();
		}
		
		return count;
	}
	
	
	public static int write(String sql, Object... params) throws DBException
	{
		WriteResult result = write(new WriteRequest(sql,false,params));
		try
		{
			return result.getAffectedRows();
		}
		finally
		{
			result.close();
		}
	}
	
	
	/**
	 * @deprecated use {@link #write(WriteRequest)} instead
	 */
	@Deprecated
	public static WriteResult write(String sql, boolean returnGeneratedKeys, Object[] params)
			throws DBException
	{
		return write(new WriteRequest(sql,returnGeneratedKeys,params));
	}
	
	
	public static int write(WritingQuery query, Object... params) throws DBException
	{
		WriteResult result = write(new WriteRequest(query,false,params));
		try
		{
			return result.getAffectedRows();
		}
		finally
		{
			result.close();
		}
	}
	
	
	/**
	 * @deprecated use {@link #write(WriteRequest)} instead
	 */
	@Deprecated
	public static WriteResult write(WritingQuery query, boolean returnGeneratedKeys, Object[] params)
			throws DBException
	{
		return write(new WriteRequest(query,returnGeneratedKeys,params));
	}
	
	
	public static WriteResult write(WriteRequest request) throws DBException
	{
		return write(new WriteRequest[]{request})[0];
	}
	
	
	public static WriteResult[] write(WriteRequest... requests) throws DBException
	{
		ensureDataSourceSpecified();
		
		WriteResult[] results = new WriteResult[requests.length];
		DBConnection<?> connection = currentDataSource.openConnection();
		try
		{
			for(int i = 0; i < requests.length; i++)
			{
				results[i] = requests[i].execute(connection);
			}
		}
		finally
		{
			if(!connection.isInTransaction())
			{
				connection.close();
			}
		}
		return results;
	}
	
	
	public static void callStoredProcedure(StoredProcedure procedure, Object... params)
			throws DBException
	{
		ensureDataSourceSpecified();
		
		DBConnection<?> connection = currentDataSource.openConnection();
		try
		{
			connection.call(procedure,params);
		}
		finally
		{
			if(!connection.isInTransaction())
			{
				connection.close();
			}
		}
	}
	
	
	/**
	 * Closes <code>result</code> silently, throws no exception whether
	 * <code>result</code> is <code>null</code> nor if an error occurs.
	 * 
	 * @param result
	 *            {@link Result} to be closed
	 * 
	 * @since 3.2
	 */
	public static void closeSilent(final @Nullable Result result)
	{
		if(result != null)
		{
			try
			{
				result.close();
			}
			catch(DBException e)
			{
				log.warning("Could not close result",e);
			}
		}
	}
	
}
