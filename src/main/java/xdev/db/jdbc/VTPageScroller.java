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
package xdev.db.jdbc;


import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import xdev.db.DBException;
import xdev.db.DelegatingResult;
import xdev.db.Result;
import xdev.vt.VirtualTable;


/**
 * This class wraps a scrollable <code>ResultSet</code> object and provides
 * methods to create "pages" of it as <code>VirtualTable</code> objects. A
 * "page" is a number of consecutive rows of the <code>ResultSet</code> object,
 * ranging from rows x to y.<br>
 * The <code>ResultSet</code> object object is kept open until closed
 * explicitely.
 * <p>
 * <b>Example</b>:<br>
 * <br>
 * <code>
 * //rs is any scrollable ResultSet object<br>
 * final VTPageScroller pager = new VTPageScroller(rs, 100, 1); 
 * //ResultSet rs, int rowsPerPage, int firstRowIndex<br>
 *  <br>
 * VirtualTable vt;<br>
 * vt = pager.currentPage();&nbsp 
 * //create VT instance for current page (rows 1 to 100)<br>
 * vt = pager.nextPage();&nbsp&nbsp&nbsp&nbsp 
 * //scroll page index forward and create new VT instance (rows 101 to 200)<br>
 * vt = pager.nextPage();&nbsp&nbsp&nbsp&nbsp 
 * //scroll page index forward and create new VT instance (rows 201 to 300)<br>
 * vt = pager.previousPage(); 
 * //scroll page index backward and create new VT instance (rows 101 to 200)<br>
 * <br>
 * pager.close(); //rs must be closed manually after use<br>
 * </code> <br>
 * 
 * @author Thomas Muenz
 * 
 */
public class VTPageScroller
{
	// /////////////////////////////////////////////////////////////////////////
	// instance fields //
	// //////////////////
	
	/**
	 * the <code>ResultSet</code> object wrapped by this wrapper object.
	 */
	private final ResultSet	rs;
	/**
	 * A hint to the total row count the wrapped <code>ResultSet</code> object
	 * can yield, may be null.
	 */
	private Integer			totalRows;
	/**
	 * The amount of rows to be retrieved per "page" (per scroll step) .
	 */
	private int				rowsPerPage;
	/**
	 * The index of the first row that will be contained in the next "page".
	 */
	private int				currentRowIndex;
	/**
	 * Flag to allow the page index to be negative (scroll rs from the end).
	 */
	private boolean			allowNegativeRowIndex	= true;
	

	// /////////////////////////////////////////////////////////////////////////
	// constructors //
	// ///////////////
	
	/**
	 * Alias for <code>VTPageScroller(rs, 10)</code>.
	 * 
	 * @param rs
	 *            the scrollable <code>ResultSet</code> object to be wrapped.
	 * @throws SQLException
	 *             if <code>rs</code> is not scrollable or
	 *             <code>rs.getType()</code> throws a <code>SQLExcpetion</code>
	 */
	public VTPageScroller(final ResultSet rs) throws SQLException
	{
		this(rs,10);
	}
	

	/**
	 * Alias for <code>VTPageScroller(rs, rowsPerPage, 1)</code>.
	 * 
	 * 
	 * @param rs
	 *            the scrollable <code>ResultSet</code> object to be wrapped.
	 * @param rowsPerPage
	 *            the number of rows to be retrieved per "page".
	 * @throws SQLException
	 *             if <code>rs</code> is not scrollable or
	 *             <code>rs.getType()</code> throws a <code>SQLExcpetion</code>
	 */
	public VTPageScroller(final ResultSet rs, final int rowsPerPage) throws SQLException
	{
		this(rs,rowsPerPage,1);
	}
	

	/**
	 * Alias for
	 * <code>VTPageScroller(rs, rowsPerPage, firstRowIndex, null)</code>.
	 * 
	 * @param rs
	 *            the scrollable <code>ResultSet</code> object to be wrapped.
	 * @param rowsPerPage
	 *            the number of rows to be retrieved per "page".
	 * @param firstRowIndex
	 *            the current row index of the first row in the first "page" to
	 *            be created.
	 * @throws SQLException
	 *             if <code>rs</code> is not scrollable or
	 *             <code>rs.getType()</code> throws a <code>SQLExcpetion</code>
	 */
	public VTPageScroller(final ResultSet rs, final int rowsPerPage, final int firstRowIndex)
			throws SQLException
	{
		this(rs,rowsPerPage,firstRowIndex,null);
	}
	

	/**
	 * Creates a new <code>VTPageScroller</code> object with <code>rs</code> as
	 * its wrapped scrollable <code>ResultSet</code> object. If <code>rs</code>
	 * is not scrollable, a is thrown. Also, the call of
	 * <code>rs.getType()</code> my throw a SQLException depending on the state
	 * and implementation of <code>rs</code>.
	 * 
	 * @param rs
	 *            the scrollable <code>ResultSet</code> object to be wrapped.
	 * @param rowsPerPage
	 *            the number of rows to be retrieved per "page".
	 * @param firstRowIndex
	 *            the current row index of the first row in the first "page" to
	 *            be created.
	 * @param totalRows
	 *            a hint to how much rows the <code>ResultSet</code> object
	 *            provides in total.
	 * @throws SQLException
	 *             if <code>rs</code> is not scrollable or
	 *             <code>rs.getType()</code> throws a <code>SQLExcpetion</code>
	 */
	public VTPageScroller(final ResultSet rs, final int rowsPerPage, final int firstRowIndex,
			final Integer totalRows) throws SQLException
	{
		super();
		
		final int rsType = rs.getType();
		if(rsType != ResultSet.TYPE_SCROLL_SENSITIVE && rsType != ResultSet.TYPE_SCROLL_INSENSITIVE)
		{
			throw new SQLException("ResultSet of type " + rsType + " is not scrollable!");
		}
		this.rs = rs;
		this.totalRows = totalRows;
		this.rowsPerPage = rowsPerPage;
		this.currentRowIndex = firstRowIndex;
	}
	

	// /////////////////////////////////////////////////////////////////////////
	// getters //
	// ///////////////////
	
	/**
	 * @return the totalRows
	 */
	public Integer getTotalRows()
	{
		return totalRows;
	}
	

	/**
	 * @return the rowsPerPage
	 */
	public int getRowsPerPage()
	{
		return rowsPerPage;
	}
	

	/**
	 * @return the currentRowIndex
	 */
	public int getCurrentRowIndex()
	{
		return currentRowIndex;
	}
	

	/**
	 * @return the allowNegativePosition
	 */
	public boolean isAllowNegativeRowIndex()
	{
		return allowNegativeRowIndex;
	}
	

	// /////////////////////////////////////////////////////////////////////////
	// setters //
	// ///////////////////
	
	/**
	 * @param totalRows
	 *            the totalRows to set
	 */
	public VTPageScroller setTotalRows(Integer totalRows)
	{
		this.totalRows = totalRows;
		return this;
	}
	

	/**
	 * @param rowsPerPage
	 *            the rowsPerPage to set
	 */
	public VTPageScroller setRowsPerPage(int rowsPerPage)
	{
		this.rowsPerPage = rowsPerPage;
		return this;
	}
	

	/**
	 * @param currentRowIndex
	 *            the currentRowIndex to set
	 */
	public VTPageScroller setCurrentRowIndex(int currentRowIndex)
	{
		this.currentRowIndex = currentRowIndex;
		return this;
	}
	

	/**
	 * @param allowNegativeRowIndex
	 *            the allowNegativeRowIndex to set
	 */
	public VTPageScroller setAllowNegativeRowIndex(boolean allowNegativeRowIndex)
	{
		this.allowNegativeRowIndex = allowNegativeRowIndex;
		return this;
	}
	

	// /////////////////////////////////////////////////////////////////////////
	// declared methods //
	// ///////////////////
	
	/**
	 * Sets the cursor (current row) of the wrapped scrollable
	 * <code>ResultSet</code> object to <code>rowIndexFrom</code> and creates a
	 * new <code>VirtualTable</code> object from that row on with a length of
	 * <code>rowCount</code> rows.
	 * 
	 * @param rowIndexFrom
	 *            the index of the <code>ResultSet</code> object row to be the
	 *            first row of the created "page".
	 * @param rowCount
	 *            the amount of rows to be contained in the created "page"
	 * @return a newly created <code>VirtualTable</code> object with rows from
	 *         <code>rowIndexFrom</code> till
	 *         <code>(rowIndexFrom + rowCount)</code>.
	 * @throws SQLException
	 *             any SQLException that can be thrown in the process by the
	 *             <code>ResultSet</code> implementation
	 */
	public VirtualTable createVirtualTable(final int rowIndexFrom, final int rowCount)
			throws SQLException
	{
		if(rowIndexFrom < 0 && !this.allowNegativeRowIndex)
		{
			throw new IndexOutOfBoundsException("No negative rowIndex allowed by setting");
		}
		rs.absolute(rowIndexFrom);
		
		Result r = new JDBCResult(rs,0,0,rowsPerPage);
		
		r = new DelegatingResult(r)
		{
			public void close() throws DBException
			{
				// do nothing
			}
		};
		
		return new VirtualTable(r,true);
	}
	

	/**
	 * Alias for <code>createVirtualTable(rowIndexFrom, getRowsPerPage())</code>
	 * .
	 * 
	 * @param rowIndexFrom
	 * @return a newly created <code>VirtualTable</code> object with rows from
	 *         <code>rowIndexFrom</code> till
	 *         <code>(rowIndexFrom + getRowsPerPage())</code>.
	 * @throws SQLException
	 *             any SQLException that can be thrown in the process by the
	 *             <code>ResultSet</code> implementation
	 */
	public VirtualTable createVirtualTable(final int rowIndexFrom) throws SQLException
	{
		return createVirtualTable(rowIndexFrom,getRowsPerPage());
	}
	

	/**
	 * Alias for
	 * <code>createVirtualTable(getCurrentRowIndex(), getRowsPerPage())</code>.
	 * 
	 * @return a newly created <code>VirtualTable</code> object with rows from
	 *         <code>getCurrentRowIndex()</code> till
	 *         <code>(getCurrentRowIndex() + getRowsPerPage())</code>.
	 * @throws SQLException
	 *             any SQLException that can be thrown in the process by the
	 *             <code>ResultSet</code> implementation.
	 */
	public VirtualTable currentPage() throws SQLException
	{
		return createVirtualTable(getCurrentRowIndex(),getRowsPerPage());
	}
	

	/**
	 * 
	 * @return <tt>true</tt> if either <code>allowNegativeRowIndex</code> or
	 *         <code>currentRowIndex - rowsPerPage >= 0</code> is <tt>true</tt>.
	 */
	public boolean hasPreviousPage()
	{
		return this.allowNegativeRowIndex || this.currentRowIndex - this.rowsPerPage >= 0;
	}
	

	/**
	 * Decrements <code>currentRowIndex</code> by <code>rowsPerPage</code> and
	 * calls <code>createVirtualTable(currentRowIndex, rowsPerPage)</code>.
	 * 
	 * @return a newly created VirtualTable containing the previous "page" (data
	 *         from <code>currentRowIndex</code> with a length of
	 *         <code>rowsPerPage</code>)
	 * @throws SQLException
	 *             any SQLException that can be thrown in the process by the
	 *             <code>ResultSet</code> implementation.
	 */
	public VirtualTable previousPage() throws SQLException
	{
		if(!this.hasPreviousPage())
			return null;
		
		return this.previousPageNoCheck();
	}
	

	protected VirtualTable previousPageNoCheck() throws SQLException
	{
		this.currentRowIndex -= this.rowsPerPage;
		return createVirtualTable(this.currentRowIndex,this.rowsPerPage);
	}
	

	/**
	 * If the <code>totalRows</code> value of this <code>VTPageScroller</code>
	 * object is <tt>null</tt> (<i>unknown</i>), then <tt>null</tt>
	 * (<i>unknown</i>) is returned.<br>
	 * Otherwise, if <code>currentRowIndex</code> + <code>rowsPerPage</code> <=
	 * <code>totalRows</code>, then <tt>TRUE</tt> is returned. Else
	 * <tt>FALSE</tt>.
	 * 
	 * @return either <tt>null</tt> (<i>unknown</i>), <tt>TRUE</tt> or
	 *         <tt>FALSE</tt>describing if there is a next page.
	 */
	public Boolean hasNextPage()
	{
		if(this.totalRows == null)
			return null;
		
		return this.currentRowIndex + this.rowsPerPage <= this.totalRows ? TRUE : FALSE;
	}
	

	/**
	 * Increments <code>currentRowIndex</code> by <code>rowsPerPage</code> and
	 * calls <code>createVirtualTable(currentRowIndex, rowsPerPage)</code>.
	 * 
	 * @return a newly created VirtualTable containing the next "page" (data
	 *         from <code>currentRowIndex</code> with a length of
	 *         <code>rowsPerPage</code>) or <tt>null</tt> if no next page is
	 *         available according to <code>totalRows</code>.
	 * @throws SQLException
	 */
	public VirtualTable nextPage() throws SQLException
	{
		final Boolean b = this.hasNextPage();
		if(b == null ? false : !b)
			return null;
		// if(isFalse(this.hasNextPage())) return null;
		return nextPageNoCheck();
	}
	

	protected VirtualTable nextPageNoCheck() throws SQLException
	{
		this.currentRowIndex += this.rowsPerPage;
		return createVirtualTable(this.currentRowIndex,this.rowsPerPage);
	}
	

	/**
	 * Tries to call <code>ResultSet.close()</code> on the used
	 * <code>ResultSet</code> object.<br>
	 * If a SQLexception occurs, it is ignored. To preserve the exception
	 * without throwing it to the outer context, the occured SQLException is
	 * returned. If not SQLException occured, <tt>null</tt> is returned.
	 * 
	 * @return the occured SQLException or <tt>null</tt> if no exception
	 *         occured.
	 */
	public SQLException close()
	{
		try
		{
			this.rs.close();
			return null;
		}
		catch(SQLException e)
		{
			return e;
		}
	}
	

	/**
	 * If <code>closeStatment</code> is <tt>false</tt> this method behaves
	 * exactely like <code>close()</code>.<br>
	 * Otherwise, the wrapped <code>ResultSet</code> object's statement is
	 * closed (which automatically closes the <code>ResultSet</code> object as
	 * specified in <code>Statement.close()</code>). If the wrapped
	 * <code>ResultSet</code> object does not have an associated statment, again
	 * simply <code>close()</code> is called.
	 * <p>
	 * If a SQLexception occurs, it is ignored. To preserve the exception
	 * without throwing it to the outer context, the occured SQLException is
	 * returned. If no SQLException occured, <tt>null</tt> is returned.
	 * 
	 * @param closeStatement
	 *            flag indicating whether the <code>ResultSet</code> object's
	 *            statement shall be closed instead.
	 * @return any SQLException occured during the process to preserve that
	 *         information.
	 */
	public SQLException close(final boolean closeStatement)
	{
		if(!closeStatement)
		{
			return this.close();
		}
		
		try
		{
			final Statement stmt = this.rs.getStatement();
			if(stmt == null)
			{
				return this.close();
			}
			stmt.close(); // resultSet is closed automatically as demanded by
							// JDBC standard
			return null;
		}
		catch(SQLException e)
		{
			return e;
		}
	}
	
}
