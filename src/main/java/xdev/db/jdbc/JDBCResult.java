package xdev.db.jdbc;

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


import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import xdev.db.ColumnMetaData;
import xdev.db.DBException;
import xdev.db.DataType;
import xdev.db.Result;


/*
 * Modified 2010-02-22 by Thomas Muenz
 * - added a constructor with additional parameter Integer maxRowCount and implemented apropriate logic for it.
 * - added JavaDoc
 * - changed all "throws Exception" to "throws SQLException" 
 *   (completely backward-compatible with existing "catch(Exception e)" calls of this class' methods.
 */
public class JDBCResult extends Result
{
	private final ResultSet		rs;
	
	private int					colCount;
	private ColumnMetaData[]	metadata;
	
	private int					currentRow	= 0;
	
	
	/**
	 * @param rs
	 * @throws SQLException
	 */
	public JDBCResult(final ResultSet rs) throws SQLException
	{
		this(rs,0);
	}
	
	
	// FIXME workaround for SQLite, there is a bug in rsmd.getScale(col). check
	// new driver versions! if bug is fixed delete this constructor and also
	// SQLiteJDBCConnection.query(String sql, Integer offset, Integer
	// maxRowCount, Object... params). issue 13769
	/**
	 * @param rs
	 * @param scale
	 * @throws SQLException
	 */
	public JDBCResult(int scale, final ResultSet rs) throws SQLException
	{
		
		this.rs = rs;
		this.colCount = rs.getMetaData().getColumnCount() - 0;
		setMaxRowCount(null);
		
		final ResultSetMetaData rsmd = rs.getMetaData();
		this.metadata = new ColumnMetaData[this.colCount];
		
		for(int i = 0, col = 1; i < this.colCount; i++, col++)
		{
			metadata[i] = new ColumnMetaData(rsmd.getTableName(col),rsmd.getColumnName(col),
					rsmd.getColumnLabel(col),DataType.get(rsmd.getColumnType(col)),
					rsmd.getPrecision(col),scale,null,
					rsmd.isNullable(col) != ResultSetMetaData.columnNoNulls,
					rsmd.isAutoIncrement(col));
			metadata[i].setTypeName(rsmd.getColumnTypeName(col));
		}
	}
	
	
	/**
	 * @param rs
	 * @param skip
	 * @throws SQLException
	 */
	public JDBCResult(final ResultSet rs, final int skip) throws SQLException
	{
		this(rs,skip,0,null);
	}
	
	
	/**
	 * @param rs
	 * @param skip
	 * @param discardTrailingCols
	 * @throws SQLException
	 */
	public JDBCResult(final ResultSet rs, final int skip, final int discardTrailingCols)
			throws SQLException
	{
		this(rs,skip,discardTrailingCols,null);
	}
	
	
	/**
	 * Creates <code>ColumnMetadata</code> information for all accounted columns
	 * and skips rows (scrolls forward in rs) if specified.
	 * 
	 * @param rs
	 *            the <code>ResultSet</code> instance that shall be wrapped by
	 *            this <code>JDBCResult</code> instance. if rs is null, a
	 *            NullPointerException is thrown.
	 * @param skip
	 *            the number of rows to skip (scroll forward) of rs. Note that
	 *            skipping causes repeated calls of
	 *            <code>ResultSet.next()</code>, so DB network traffic is NOT
	 *            spared.
	 * @param discardTrailingCols
	 *            the number of columns that will be discarded from the end of
	 *            the column list.
	 * @param maxRowCount
	 *            the upmost number of rows to be returned. May be <tt>null</tt>
	 *            to disable row count limitation (return all rows the resultSet
	 *            provides).
	 * 
	 * @throws SQLException
	 *             SQLExceptions thrown by the <code>ResultSet</code>
	 *             implementation.
	 */
	public JDBCResult(final ResultSet rs, final int skip, final int discardTrailingCols,
			final Integer maxRowCount) throws SQLException
	{
		this.rs = rs;
		this.colCount = rs.getMetaData().getColumnCount() - discardTrailingCols;
		setMaxRowCount(maxRowCount);
		
		final ResultSetMetaData rsmd = rs.getMetaData();
		this.metadata = new ColumnMetaData[this.colCount];
		
		for(int i = 0, col = 1; i < this.colCount; i++, col++)
		{
			metadata[i] = new ColumnMetaData(rsmd.getTableName(col),rsmd.getColumnName(col),
					rsmd.getColumnLabel(col),DataType.get(rsmd.getColumnType(col)),
					rsmd.getPrecision(col),rsmd.getScale(col),null,
					rsmd.isNullable(col) != ResultSetMetaData.columnNoNulls,
					rsmd.isAutoIncrement(col));
			metadata[i].setTypeName(rsmd.getColumnTypeName(col));
		}
		
		if(skip > 0)
		{
			skipImpl(skip);
		}
	}
	
	
	public ResultSet getJdbcResultSet()
	{
		return rs;
	}
	
	
	/**
	 * @return the current count of already scrolled rows.
	 */
	public int getCurrentRow()
	{
		return currentRow;
	}
	
	
	/**
	 * This method is an alias for <code>ResultSet.next()</code> called on the
	 * <code>ResultSet</code> instance that was provided when creating this
	 * <code>JDBCResult</code> instance EXCEPT the following exceptions:<br>
	 * <ul>
	 * <li>The <code>ResultSet</code> instance provided when creating this
	 * <code>JDBCResult</code> instance was null, then <tt>false</tt> is
	 * returned.
	 * <li>A maxRowCount value has been provided (not null) and the limit has
	 * already been reached, then <tt>false</tt> is returned.
	 * </ul>
	 * 
	 * @return <tt>true</tt> if there is another row to be returned, otherwise
	 *         <tt>false</tt>.
	 * @throws SQLException
	 *             any SQLException that can be thrown by
	 *             <code>ResultSet.next()</code>
	 */
	/*
	 * (22.02.2010 TM)NOTE: next() is synchronized now E.g.: If multiple threads
	 * call this method simultaneously it can happen that currentRow becomes
	 * larger than maxRowCount and too much rows are returned.
	 */
	@Override
	public synchronized boolean next() throws DBException
	{
		try
		{
			Integer maxRowCount = getMaxRowCount();
			if(maxRowCount != null && this.currentRow >= maxRowCount)
			{
				return false;
			}
			
			if(rs.next())
			{
				this.currentRow++;
				return true;
			}
			
			return false;
		}
		catch(SQLException e)
		{
			throw new DBException(getDataSource(),e);
		}
	}
	
	
	@Override
	public synchronized int skip(int count) throws DBException
	{
		try
		{
			return skipImpl(count);
		}
		catch(SQLException e)
		{
			throw new DBException(getDataSource(),e);
		}
	}
	
	
	private int skipImpl(int count) throws SQLException
	{
		int skipped = 0;
		while(skipped < count)
		{
			if(rs.next())
			{
				skipped++;
			}
			else
			{
				break;
			}
		}
		return skipped;
	}
	
	
	/**
	 * Alias for <code>ResultSet.getObject(col + 1)</code> called on the
	 * <code>ResultSet</code> instance that was provided when creating this
	 * <code>JDBCResult</code> instance.
	 * 
	 * @param col
	 *            col the index of the column whose value shall be returned,
	 *            starting at 0
	 * @return the returned value of the
	 *         <code>ResultSet.getObject(col + 1)</code> call.
	 * @throws SQLException
	 *             any SQLExecption that can be thrown by
	 *             <code>ResultSet.getObject()</code>
	 */
	@Override
	public Object getObject(int col) throws DBException
	{
		try
		{
			try
			{
				DataType type = metadata[col].getType();
				if(type.isBlob())
				{
					return rs.getBlob(col + 1);
				}
				else if(type == DataType.CLOB)
				{
					return rs.getClob(col + 1);
				}
			}
			catch(SQLException e)
			{
			}
			
			return rs.getObject(col + 1);
		}
		catch(SQLException e)
		{
			throw new DBException(getDataSource(),e);
		}
	}
	
	
	/**
	 * @return the column count of this <code>JDBCResult instance</code>
	 */
	@Override
	public int getColumnCount()
	{
		return colCount;
	}
	
	
	@Override
	public ColumnMetaData getMetadata(int col)
	{
		return metadata[col];
	}
	
	
	/**
	 * Returns the column name with index <code>col</code> by calling<br>
	 * <code>rs.getMetaData().getColumnName(col + 1)</code>.<br>
	 * <p>
	 * If an SQLExceptions occurs, the return value is <code>"COL" + col</code>
	 * <p>
	 * Note that <code>col</code> starts with 0 for the first column.
	 * 
	 * @param col
	 *            the index of the column whose name shall be returned, starting
	 *            at 0
	 * @return the column name of column with index <code>col</code> or
	 *         <code>"COL"+col</code> on any exception.
	 */
	public String getColName(int col)
	{
		try
		{
			return rs.getMetaData().getColumnName(col + 1);
		}
		catch(SQLException e)
		{
			return "COL" + col;
		}
	}
	
	
	/**
	 * Closes the <code>ResultSet</code>.
	 */
	@Override
	public void close() throws DBException
	{
		JDBCUtils.closeSilent(rs);
	}
}
