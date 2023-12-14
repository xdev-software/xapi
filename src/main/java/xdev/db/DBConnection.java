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


import java.io.Serializable;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Date;
import java.util.Map;

import xdev.db.event.DBConnectionListener;
import xdev.db.sql.SELECT;
import xdev.db.sql.WritingQuery;


/**
 * <P>
 * A connection (session) with a specific database. SQL statements are executed
 * and results are returned within the context of a connection.
 * </P>
 * 
 * @param <DS>
 *            the generic type
 * 
 * @see DBDataSource
 * 
 * @author XDEV Software
 * 
 */
public interface DBConnection<DS extends DBDataSource<DS>> extends AutoCloseable
{
	/**
	 * Registers a {@link DBConnectionListener} at this connection.
	 * 
	 * @param l
	 *            the listener to register
	 */
	public void addDBConnectionListener(DBConnectionListener l);
	
	
	/**
	 * Unregisters a {@link DBConnectionListener}.
	 * 
	 * @param l
	 *            the listener to unregister
	 */
	public void removeDBConnectionListener(DBConnectionListener l);
	
	
	/**
	 * Retrieves all registered {@link DBConnectionListener}s.
	 * 
	 * @return a array of {@link DBConnectionListener}s, or a null-length array
	 *         if no listener is registered
	 */
	public DBConnectionListener[] getDBConnectionListeners();
	
	
	/**
	 * Verify if this {@link DBConnection} is available.
	 * 
	 * @throws DBException
	 *             if this {@link DBConnection} is not available.
	 */
	public void testConnection() throws DBException;
	
	
	/**
	 * Stores all queries which are executed from now on if <code>store</code>
	 * is <code>true</code>, otherwise all stored queries will be deleted.
	 * 
	 * @param store
	 *            if <code>true</code> all queries which are executed from now
	 *            are stored, otherwise all stored queries will be deleted
	 * 
	 * @see #getStoredQueries()
	 */
	public void setStoreQueries(boolean store);
	
	
	/**
	 * Returns all stored queries since the <code>setStoreQueries(true)</code>
	 * was called. If the flag is <code>false</code> a empty array is returned.
	 * 
	 * @return all stored queries since setStoreQueries(true) was called.
	 * 
	 * @see #setStoreQueries(boolean)
	 */
	public Query[] getStoredQueries();
	
	
	
	public static class Query implements Serializable
	{
		private static final long	serialVersionUID	= -8378705696102327320L;
		
		private final String		statement;
		private final Object[]		params;
		
		
		public Query(String statement, Object... params)
		{
			this.statement = statement;
			this.params = params;
		}
		
		
		public String getStatement()
		{
			return statement;
		}
		
		
		public Object[] getParams()
		{
			return params;
		}
		
		
		@Override
		public String toString()
		{
			if(params != null && params.length > 0)
			{
				return statement + " " + Arrays.toString(params);
			}
			
			return statement;
		}
	}
	
	
	/**
	 * Executes the given {@link SELECT}, which returns a <code>Result</code>
	 * object.
	 * 
	 * @param select
	 *            an {@link SELECT} statement to be sent to the database,
	 *            typically a static SQL <code>SELECT</code> statement
	 * 
	 * @param params
	 *            the parameter values for the given <code>select</code>
	 * 
	 * @return a <code>Result</code> object that contains the data produced by
	 *         the given {@link SELECT}; never <code>null</code>
	 * 
	 * @throws DBException
	 *             if a database access error occurs
	 */
	public Result query(SELECT select, Object... params) throws DBException;
	
	
	/**
	 * Executes the given SQL String, which returns a <code>Result</code>
	 * object.
	 * 
	 * @param sql
	 *            an SQL String to be sent to the database
	 * 
	 * @param params
	 *            the parameter values for the given <code>sql</code>
	 * 
	 * @return a <code>Result</code> object that contains the data produced by
	 *         the given SQL String; never <code>null</code>
	 * 
	 * @throws DBException
	 *             if a database access error occurs
	 */
	public Result query(String sql, Object... params) throws DBException;
	
	
	/**
	 * Creates a pager for a specified query.
	 * 
	 * @param rowsPerPage
	 *            initial number of rows per page, see
	 *            {@link DBPager#setRowsPerPage(int)},
	 *            {@link DBPager#getRowsPerPage()}
	 * @param select
	 *            an {@link SELECT} statement to be sent to the database,
	 *            typically a static SQL <code>SELECT</code> statement
	 * @param params
	 *            the parameter values for the given <code>sql</code>
	 * @return a new pager to scroll page by page over the {@link Result}
	 *         returned by the query.
	 * @throws DBException
	 *             if a database access error occurs
	 * 
	 * @since 4.0
	 */
	public DBPager getPager(int rowsPerPage, SELECT select, Object... params) throws DBException;
	
	
	/**
	 * Executes a query that detect the row count of the given
	 * <code>select</code>.
	 * 
	 * @param select
	 *            the SQL String which declare the row count
	 * 
	 * @return the row count of the given <code>select</code>
	 * 
	 * @throws DBException
	 *             if a database access error occurs
	 */
	public int getQueryRowCount(String select) throws DBException;
	
	
	/**
	 * 
	 * @param query
	 *            the query to execute
	 * 
	 * @param returnGeneratedKeys
	 *            a flag indicating whether auto-generated keys should be
	 *            returned
	 * 
	 * @param params
	 *            the parameter values for the given <code>query</code>
	 * 
	 * @return a {@link WriteResult} that include the affected rows and the
	 *         generated keys (if the <code>returnGeneratedKeys</code> flag is
	 *         <code>true</code>)
	 * 
	 * @throws DBException
	 *             if a database access error occurs or this method is called on
	 *             a closed <code>DBConnection</code>
	 * 
	 */
	public WriteResult write(WritingQuery query, boolean returnGeneratedKeys, Object... params)
			throws DBException;
	
	
	/**
	 * 
	 * 
	 * 
	 * @param sql
	 *            any SQL String
	 * 
	 * @param returnGeneratedKeys
	 *            a flag indicating whether auto-generated keys should be
	 *            returned
	 * 
	 * @param params
	 *            the parameter values for the given <code>sql</code>
	 * 
	 * @return a {@link WriteResult} that include the affected rows and the
	 *         generated keys (if the <code>returnGeneratedKeys</code> flag is
	 *         <code>true</code>)
	 * 
	 * @throws DBException
	 *             if a database access error occurs or this method is called on
	 *             a closed <code>DBConnection</code>
	 */
	public WriteResult write(String sql, boolean returnGeneratedKeys, Object... params)
			throws DBException;
	
	
	/**
	 * 
	 * @param query
	 *            the query to execute
	 * 
	 * @param columnNames
	 *            an array of column names indicating the columns that should be
	 *            returned from the inserted row or rows
	 * 
	 * @param params
	 *            the parameter values for the given <code>query</code>
	 * 
	 * @return a {@link WriteResult} that include the affected rows and the
	 *         generated keys (if the <code>returnGeneratedKeys</code> flag is
	 *         <code>true</code>)
	 * 
	 * @throws DBException
	 *             if a database access error occurs or this method is called on
	 *             a closed <code>DBConnection</code>
	 * 
	 */
	public WriteResult write(WritingQuery query, String[] columnNames, Object... params)
			throws DBException;
	
	
	/**
	 * 
	 * 
	 * 
	 * @param sql
	 *            any SQL String
	 * 
	 * @param columnNames
	 *            an array of column names indicating the columns that should be
	 *            returned from the inserted row or rows
	 * 
	 * @param params
	 *            the parameter values for the given <code>sql</code>
	 * 
	 * @return a {@link WriteResult} that include the affected rows and the
	 *         generated keys (if the <code>returnGeneratedKeys</code> flag is
	 *         <code>true</code>)
	 * 
	 * @throws DBException
	 *             if a database access error occurs or this method is called on
	 *             a closed <code>DBConnection</code>
	 */
	public WriteResult write(String sql, String[] columnNames, Object... params) throws DBException;
	
	
	/**
	 * Executes the given SQL String with this {@link DBConnection}.
	 * 
	 * @param sql
	 *            any SQL String
	 * 
	 * @throws DBException
	 *             if a database access error occurs or this method is called on
	 *             a closed <code>DBConnection</code>
	 * 
	 * @see #write(String, boolean, Object...)
	 * @see #write(WritingQuery, boolean, Object...)
	 */
	public void write(String sql) throws DBException;
	
	
	/**
	 * Call the <code>procedure</code> with given parameter values.
	 * 
	 * @param procedure
	 *            the {@link StoredProcedure} to call
	 * 
	 * @param params
	 *            the parameter values for the given <code>procedure</code>
	 * 
	 * @throws DBException
	 *             if a database access error occurs
	 * 
	 */
	public void call(StoredProcedure procedure, Object... params) throws DBException;
	
	
	/**
	 * Sets this connection's auto-commit mode to <code>false</code>. If a
	 * connection is in auto-commit mode, then all its SQL statements will be
	 * executed and committed as individual transactions. Otherwise, its SQL
	 * statements are grouped into transactions that are terminated by a call to
	 * either the method <code>commit</code> or the method <code>rollback</code>
	 * . By default, new connections are in auto-commit mode.
	 * <P>
	 * The commit occurs when the statement completes. The time when the
	 * statement completes depends on the type of SQL Statement:
	 * <ul>
	 * <li>For DML statements, such as Insert, Update or Delete, and DDL
	 * statements, the statement is complete as soon as it has finished
	 * executing.
	 * <li>For Select statements, the statement is complete when the associated
	 * result set is closed.
	 * <li>For <code>CallableStatement</code> objects or for statements that
	 * return multiple results, the statement is complete when all of the
	 * associated result sets have been closed, and all update counts and output
	 * parameters have been retrieved.
	 * </ul>
	 * <P>
	 * <B>NOTE:</B> If this method is called during a transaction and the
	 * auto-commit mode is changed, the transaction is committed. If
	 * <code>setAutoCommit</code> is called and the auto-commit mode is not
	 * changed, the call is a no-op.
	 * 
	 * 
	 * @throws DBException
	 *             if a database access error occurs, or this method is called
	 *             on a closed connection
	 * 
	 * @see #isInTransaction()
	 * @see #setSavepoint()
	 * @see #setSavepoint(String)
	 * @see #releaseSavepoint(Savepoint)
	 */
	public void beginTransaction() throws DBException;
	
	
	/**
	 * Retrieves the current auto-commit mode for this <code>DBConnection</code>
	 * object.
	 * 
	 * @return the current state of this <code>DBConnection</code> object's
	 *         auto-commit mode; <code>true</code> if this
	 *         <code>DBConnection</code> is in a transaction otherwise
	 *         <code>false</code>
	 * 
	 * @throws DBException
	 *             if a database access error occurs or this method is called on
	 *             a closed connection
	 * 
	 * @see #beginTransaction
	 * @see #setSavepoint()
	 * @see #setSavepoint(String)
	 * @see #releaseSavepoint(Savepoint)
	 */
	public boolean isInTransaction() throws DBException;
	
	
	/**
	 * Creates an unnamed savepoint in the current transaction and returns the
	 * new <code>Savepoint</code> object that represents it.
	 * 
	 * <p>
	 * if setSavepoint is invoked outside of an active transaction, a
	 * transaction will be started at this newly created savepoint.
	 * 
	 * @return the new <code>Savepoint</code> object
	 * 
	 * @throws DBException
	 *             if a database access error occurs, this method is called
	 *             while participating in a distributed transaction, this method
	 *             is called on a closed connection or this
	 *             <code>Connection</code> object is currently in auto-commit
	 *             mode; if the JDBC driver does not support this method
	 * 
	 * @see #setSavepoint(String)
	 * @see #releaseSavepoint(Savepoint)
	 * @see #beginTransaction()
	 * @see #isInTransaction()
	 */
	public Savepoint setSavepoint() throws DBException;
	
	
	/**
	 * Creates a {@link Savepoint} with the given name in the current
	 * transaction and returns the new <code>Savepoint</code> object that
	 * represents it.
	 * 
	 * <p>
	 * if setSavepoint is invoked outside of an active transaction, a
	 * transaction will be started at this newly created savepoint.
	 * 
	 * @param name
	 *            a <code>String</code> containing the name of the savepoint
	 * 
	 * @return the new <code>Savepoint</code> object
	 * 
	 * @throws DBException
	 *             if a database access error occurs, this method is called
	 *             while participating in a distributed transaction, this method
	 *             is called on a closed connection or this
	 *             <code>Connection</code> object is currently in auto-commit
	 *             mode; if the JDBC driver does not support this method
	 * 
	 * @see #setSavepoint()
	 * @see #releaseSavepoint(Savepoint)
	 * @see #beginTransaction()
	 * @see #isInTransaction()
	 */
	public Savepoint setSavepoint(String name) throws DBException;
	
	
	/**
	 * Removes the specified <code>Savepoint</code> and subsequent
	 * <code>Savepoint</code> objects from the current transaction. Any
	 * reference to the savepoint after it have been removed will cause an
	 * <code>DBException</code> to be thrown.
	 * 
	 * @param savepoint
	 *            the <code>Savepoint</code> object to be removed
	 * 
	 * @throws DBException
	 *             if a database access error occurs, this method is called on a
	 *             closed connection or the given <code>Savepoint</code> object
	 *             is not a valid savepoint in the current transaction; if the
	 *             JDBC driver does not support this method
	 * 
	 * @see #setSavepoint()
	 * @see #setSavepoint(String)
	 * @see #beginTransaction()
	 * @see #isInTransaction()
	 */
	public void releaseSavepoint(Savepoint savepoint) throws DBException;
	
	
	/**
	 * Makes all changes made since the previous commit/rollback permanent and
	 * releases any database locks currently held by this
	 * <code>DBConnection</code> object. This method should be used only when
	 * auto-commit mode has been disabled.
	 * 
	 * @throws DBException
	 *             if a database access error occurs, this method is called
	 *             while participating in a distributed transaction, if this
	 *             method is called on a closed conection or this
	 *             <code>DBConnection</code> object is in auto-commit mode
	 * 
	 * @see #beginTransaction()
	 * @see #isInTransaction()
	 */
	public void commit() throws DBException;
	
	
	/**
	 * Undoes all changes made in the current transaction and releases any
	 * database locks currently held by this DBConnection object.
	 * 
	 * @throws DBException
	 *             if a database access error occurs
	 */
	public void rollback() throws DBException;
	
	
	/**
	 * Undoes all changes made after the given Savepoint object was set.<br>
	 * If <code>savepoint</code> is <code>null</code> then all changes will be
	 * undone - similiar to {@link #rollback()}.
	 * 
	 * @param savepoint
	 *            The set <code>savepoint</code> or <code>null</code>
	 * 
	 * @throws DBException
	 *             if a database access error occurs
	 */
	public void rollback(Savepoint savepoint) throws DBException;
	
	
	/**
	 * Releases this <code>DBConnection</code> object's database and JDBC
	 * resources immediately instead of waiting for them to be automatically
	 * released.
	 * <P>
	 * Calling the method <code>close</code> on a <code>Connection</code> object
	 * that is already closed is a no-op.
	 * <P>
	 * It is <b>strongly recommended</b> that an application explicitly commits
	 * or rolls back an active transaction prior to calling the
	 * <code>close</code> method. If the <code>close</code> method is called and
	 * there is an active transaction, the results are implementation-defined.
	 * <P>
	 * 
	 * @throws DBException
	 *             if a database access error occurs
	 */
	public void close() throws DBException;
	
	
	/**
	 * Create a table on database.
	 * 
	 * @param tableName
	 *            , name of the table
	 * @param primaryKey
	 *            , the primary key of the table
	 * @param columnMap
	 *            , a map with column name as key and as its value some extended
	 *            like 'INTEGER NOT NULL'
	 * @param isAutoIncrement
	 *            , boolean is primary key auto increment
	 * @param foreignKeys
	 *            , a map with foreignkey name as key and as its value some
	 *            extended
	 */
	void createTable(String tableName, String primaryKey, Map<String, String> columnMap,
			boolean isAutoIncrement, Map<String, String> foreignKeys) throws Exception;
	
	
	/**
	 * Get server time returns the current timestamp.
	 * 
	 * @throws DBException
	 *             if a database access error occurs
	 * @throws ParseException
	 *             if timestamp cannot parse to Date
	 * @return current server Date
	 */
	Date getServerTime() throws DBException, ParseException;
	
}
