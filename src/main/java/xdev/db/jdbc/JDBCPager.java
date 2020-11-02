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
import java.sql.SQLException;

import xdev.db.DBDataSource;
import xdev.db.DBException;
import xdev.db.DBPager;
import xdev.db.DelegatingResult;
import xdev.db.QueryInfo;
import xdev.db.Result;
import xdev.util.MathUtils;


/**
 * 
 * @author XDEV Software (HL)
 * @since 4.0
 */
public class JDBCPager implements DBPager
{
	private final JDBCConnection	connection;
	private final QueryInfo			queryInfo;
	private final ResultSet			resultSet;
	
	private int						rowsPerPage;
	private int						totalRows;
	private int						maxPageIndex;
	private int						currentPageIndex	= -1;
	private int						currentRowIndex		= -1;
	
	
	public JDBCPager(JDBCConnection connection, QueryInfo queryInfo, ResultSet resultSet,
			int rowsPerPage) throws DBException
	{
		this.connection = connection;
		this.queryInfo = queryInfo;
		this.resultSet = resultSet;
		
		try
		{
			resultSet.last();
			totalRows = resultSet.getRow();
		}
		catch(SQLException e)
		{
			throw new DBException(connection.getDataSource(),e);
		}
		
		setRowsPerPageValues(rowsPerPage);
	}
	
	
	@Override
	public DBDataSource<?> getDataSource()
	{
		return connection.getDataSource();
	}
	
	
	@Override
	public Result setRowsPerPage(int rowsPerPage) throws DBException
	{
		setRowsPerPageValues(rowsPerPage);
		
		if(currentRowIndex == -1)
		{
			return nextPage();
		}
		
		return gotoPage(rowsPerPage == 0 ? 0 : currentRowIndex / rowsPerPage);
	}
	
	
	private void setRowsPerPageValues(int rowsPerPage)
	{
		this.rowsPerPage = rowsPerPage;
		
		if(rowsPerPage > 0)
		{
			maxPageIndex = totalRows / rowsPerPage;
			if(totalRows % rowsPerPage == 0)
			{
				maxPageIndex--;
			}
		}
		else
		{
			maxPageIndex = 0;
		}
	}
	
	
	@Override
	public int getRowsPerPage()
	{
		return rowsPerPage;
	}
	
	
	@Override
	public int getTotalRows()
	{
		return totalRows;
	}
	
	
	@Override
	public int getCurrentPageIndex()
	{
		return currentPageIndex;
	}
	
	
	@Override
	public int getMaxPageIndex()
	{
		return maxPageIndex;
	}
	
	
	@Override
	public boolean hasNextPage()
	{
		return Math.max(currentRowIndex,0) + rowsPerPage < totalRows;
	}
	
	
	@Override
	public boolean hasPreviousPage()
	{
		return this.currentRowIndex > 0;
	}
	
	
	@Override
	public Result nextPage() throws DBException
	{
		if(!hasNextPage())
		{
			return null;
		}
		
		if(currentRowIndex == -1 || currentPageIndex == -1)
		{
			currentRowIndex = currentPageIndex = 0;
		}
		else
		{
			currentRowIndex += rowsPerPage;
			currentPageIndex++;
		}
		
		return getResult();
	}
	
	
	@Override
	public Result previousPage() throws DBException
	{
		if(!hasPreviousPage())
		{
			return null;
		}
		
		currentRowIndex -= rowsPerPage;
		if(currentRowIndex < 0)
		{
			currentRowIndex = 0;
		}
		currentPageIndex--;
		
		return getResult();
	}
	
	
	@Override
	public Result firstPage() throws DBException
	{
		if(currentRowIndex == 0 || totalRows <= 0)
		{
			return null;
		}
		
		currentRowIndex = currentPageIndex = 0;
		
		return getResult();
	}
	
	
	@Override
	public Result lastPage() throws DBException
	{
		if(!hasNextPage())
		{
			return null;
		}
		
		currentRowIndex = maxPageIndex * rowsPerPage;
		currentPageIndex = maxPageIndex;
		
		return getResult();
	}
	
	
	@Override
	public Result gotoPage(int page) throws DBException
	{
		MathUtils.checkRange(page,0,maxPageIndex);
		
		currentRowIndex = page * rowsPerPage;
		currentPageIndex = page;
		
		return getResult();
	}
	
	
	@Override
	public Result gotoRow(int row) throws DBException
	{
		MathUtils.checkRange(row,0,totalRows - 1);
		
		currentRowIndex = row;
		currentPageIndex = row / rowsPerPage;
		
		return getResult();
	}
	
	
	private Result getResult() throws DBException
	{
		try
		{
			if(currentRowIndex == 0)
			{
				resultSet.beforeFirst();
			}
			else
			{
				resultSet.absolute(currentRowIndex);
			}
			
			Result r = new JDBCResult(resultSet,0,0,rowsPerPage);
			r.setDataSource(connection.getDataSource());
			r.setQueryInfo(queryInfo);
			return new DelegatingResult(r)
			{
				@Override
				public void close() throws DBException
				{
					// don't close the resultset
				}
			};
		}
		catch(SQLException e)
		{
			throw new DBException(connection.getDataSource(),e);
		}
	}
	
	
	@Override
	public void close() throws DBException
	{
		JDBCUtils.closeSilent(resultSet);
	}
	
	
	@Override
	public boolean isClosed() throws DBException
	{
		try
		{
			return resultSet.isClosed();
		}
		catch(SQLException e)
		{
			throw new DBException(connection.getDataSource(),e);
		}
	}
}
