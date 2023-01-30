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


import java.lang.ref.SoftReference;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import xdev.vt.VirtualTable;


/**
 * This class extends <code>VTPageScroller</code> by a means of caching page VTs
 * that have already been loaded.<br>
 * The page caching is based on start row index and page row count.<br>
 * Cached pages are held in soft references, meaning they are released as soon
 * as memory runs low.
 * 
 * @author Thomas Muenz
 */

public class CachedVTPageScroller extends VTPageScroller
{
	// /////////////////////////////////////////////////////////////////////////
	// instance fields //
	// //////////////////
	
	private final HashMap<Long, SoftReference<VirtualTable>>	cachedPages	= new HashMap<Long, SoftReference<VirtualTable>>();
	

	// /////////////////////////////////////////////////////////////////////////
	// static methods //
	// /////////////////
	
	/**
	 * Null safe retrieval of a <code>VirtualTable</code> object from a
	 * <code>SoftReference</code> object.
	 * 
	 * @param ref
	 *            the <code>SoftReference</code> object that points to a
	 *            <code>VirtualTable</code> object or null.
	 * @return <tt>null</tt> if <code>ref</code> is null or the
	 *         <code>VirtualTable</code> object that is referenced by
	 *         <code>ref</code>.
	 */
	private static final VirtualTable getVTFromReference(final SoftReference<VirtualTable> ref)
	{
		return ref == null ? null : ref.get();
	}
	

	/**
	 * Creates a new <code>Long</code> object from two ints
	 * <code>rowIndexFrom</code> and <code>rowCount</code> defining a page for
	 * use as a hash key.<br>
	 * 
	 * @param rowIndexFrom
	 *            the row index in the original <code>ResultSet</code> object of
	 *            the first row int the page
	 * @param rowCount
	 *            the number of rows in the page.
	 * @return a <code>Long</code> object combining both int values to a single
	 *         hash key value.
	 */
	protected static final Long createHashKey(final int rowIndexFrom, final int rowCount)
	{
		return (((long)rowIndexFrom) << 32) + rowCount;
	}
	

	// /////////////////////////////////////////////////////////////////////////
	// constructors //
	// ///////////////
	
	/**
	 * Creates a new <code>CachedVTPageScroller</code> object by calling
	 * <code>super(rs)</code>.
	 * 
	 * @param rs
	 *            the scrollable <code>ResultSet</code> object to be used for
	 *            the to be created <code>CachedVTPageScroller</code> object.
	 * @throws SQLException
	 *             if <code>rs</code> is not scrollable or
	 *             <code>rs.getType()</code> throws a <code>SQLExcpetion</code>
	 */
	public CachedVTPageScroller(final ResultSet rs) throws SQLException
	{
		super(rs);
	}
	

	/**
	 * Creates a new <code>CachedVTPageScroller</code> object by calling
	 * <code>super(rs, rowsPerPage)</code>.
	 * 
	 * @param rs
	 *            the scrollable <code>ResultSet</code> object to be used for
	 *            the to be created <code>CachedVTPageScroller</code> object.
	 * @param rowsPerPage
	 *            the number of rows to be retrieved per "page".
	 * @throws SQLException
	 *             if <code>rs</code> is not scrollable or
	 *             <code>rs.getType()</code> throws a <code>SQLExcpetion</code>
	 */
	public CachedVTPageScroller(final ResultSet rs, int rowsPerPage) throws SQLException
	{
		super(rs,rowsPerPage);
	}
	

	/**
	 * Creates a new <code>CachedVTPageScroller</code> object by calling
	 * <code>super(rs, rowsPerPage, firstRowIndex)</code>.
	 * 
	 * @param rs
	 *            the scrollable <code>ResultSet</code> object to be used for
	 *            the to be created <code>CachedVTPageScroller</code> object.
	 * @param rowsPerPage
	 *            the number of rows to be retrieved per "page".
	 * @param firstRowIndex
	 *            the current row index of the first row in the first "page" to
	 *            be created.
	 * @throws SQLException
	 *             if <code>rs</code> is not scrollable or
	 *             <code>rs.getType()</code> throws a <code>SQLExcpetion</code>
	 */
	public CachedVTPageScroller(final ResultSet rs, final int rowsPerPage, final int firstRowIndex)
			throws SQLException
	{
		super(rs,rowsPerPage,firstRowIndex);
	}
	

	/**
	 * Creates a new <code>CachedVTPageScroller</code> object by calling
	 * <code>super(rs, rowsPerPage, firstRowIndex, totalRows)</code>.
	 * 
	 * @param rs
	 *            the scrollable <code>ResultSet</code> object to be used for
	 *            the to be created <code>CachedVTPageScroller</code> object.
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
	public CachedVTPageScroller(final ResultSet rs, final int rowsPerPage, final int firstRowIndex,
			final Integer totalRows) throws SQLException
	{
		super(rs,rowsPerPage,firstRowIndex,totalRows);
	}
	

	// /////////////////////////////////////////////////////////////////////////
	// getters //
	// ///////////////////
	
	/**
	 * @return the cachedPages
	 */
	protected HashMap<Long, SoftReference<VirtualTable>> getCachedPages()
	{
		return this.cachedPages;
	}
	

	// /////////////////////////////////////////////////////////////////////////
	// setters //
	// ///////////////////
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public CachedVTPageScroller setAllowNegativeRowIndex(boolean allowNegativePosition)
	{
		super.setAllowNegativeRowIndex(allowNegativePosition);
		return this;
	}
	

	/**
	 * {@inheritDoc}
	 */
	@Override
	public CachedVTPageScroller setCurrentRowIndex(int currentRowIndex)
	{
		super.setCurrentRowIndex(currentRowIndex);
		return this;
	}
	

	/**
	 * {@inheritDoc}
	 */
	@Override
	public CachedVTPageScroller setRowsPerPage(int rowsPerPage)
	{
		super.setRowsPerPage(rowsPerPage);
		return this;
	}
	

	/**
	 * {@inheritDoc}
	 */
	@Override
	public CachedVTPageScroller setTotalRows(Integer totalRows)
	{
		super.setTotalRows(totalRows);
		return this;
	}
	

	// /////////////////////////////////////////////////////////////////////////
	// override methods //
	// ///////////////////
	
	/**
	 * Sets the cursor (current row) of the wrapped scrollable
	 * <code>ResultSet</code> object to <code>rowIndexFrom</code> and creates a
	 * new <code>VirtualTable</code> object from that row on with a length of
	 * <code>rowCount</code> rows.
	 * <p>
	 * Additionally, the created <code>VirtualTable</code> object is cached.<br>
	 * Cached VT objects will be reused by methods like
	 * <code>currentPage()</code>, <code>nextPage()</code>,
	 * <code>previousPage()</code> if <code>rowIndexFrom</code> and
	 * <code>rowCount</code> of the page they request fit to a already cached VT
	 * object.
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
	@Override
	public VirtualTable createVirtualTable(final int rowIndexFrom, final int rowCount)
			throws SQLException
	{
		final VirtualTable vt = super.createVirtualTable(rowIndexFrom,rowCount);
		this.putCachedVirtualTable(vt,rowIndexFrom,rowCount);
		return vt;
	}
	

	/**
	 * Returns the page with the current <code>getRowsPerPage()</code> rows as a
	 * <code>VirtualTable</code> object.<br>
	 * If a fitting VT object is found in the cache, it is returned. Otherwise a
	 * new <code>VirtualTable</code> object is created.
	 * 
	 * @return a fitting already existing or else newly created
	 *         <code>VirtualTable</code> object with the current
	 *         <code>getRowsPerPage()</code> rows.
	 * @throws SQLException
	 *             any <code>SQLException</code> that can be thrown by the
	 *             <code>ResultSet</code> implementation in the process.
	 */
	@Override
	public VirtualTable currentPage() throws SQLException
	{
		final VirtualTable vt = this.getCachedVirtualTable(this.getCurrentRowIndex(),
				this.getRowsPerPage());
		return vt != null ? vt : super.currentPage();
	}
	

	/**
	 * Returns the page with the next <code>getRowsPerPage()</code> rows as a
	 * <code>VirtualTable</code> object.<br>
	 * If a fitting VT object is found in the cache, it is returned. Otherwise a
	 * new <code>VirtualTable</code> object is created.
	 * 
	 * @return a fitting already existing or else newly created
	 *         <code>VirtualTable</code> object with the next
	 *         <code>getRowsPerPage()</code> rows or <tt>null</tt> if no next
	 *         page is available according to <code>totalRows</code>.
	 * @throws SQLException
	 *             any <code>SQLException</code> that can be thrown by the
	 *             <code>ResultSet</code> implementation in the process.
	 */
	@Override
	public VirtualTable nextPage() throws SQLException
	{
		final Boolean b = this.hasNextPage();
		if(b == null ? false : !b)
			return null;
		// if(isFalse(this.hasNextPage())) return null;
		
		final int rowsPerPage = this.getRowsPerPage();
		final int nextPageIndex = this.getCurrentRowIndex() + rowsPerPage;
		final VirtualTable vt = this.getCachedVirtualTable(nextPageIndex,rowsPerPage);
		
		if(vt != null)
		{
			this.setCurrentRowIndex(nextPageIndex);
			return vt;
		}
		else
		{
			return this.nextPageNoCheck();
		}
	}
	

	/**
	 * Returns the page with the previous <code>getRowsPerPage()</code> rows as
	 * a <code>VirtualTable</code> object.<br>
	 * If a fitting VT object is found in the cache, it is returned. Otherwise a
	 * new <code>VirtualTable</code> object is created.
	 * 
	 * @return a fitting already existing or else newly created
	 *         <code>VirtualTable</code> object with the previous
	 *         <code>getRowsPerPage()</code> rows.
	 * @throws SQLException
	 *             any <code>SQLException</code> that can be thrown by the
	 *             <code>ResultSet</code> implementation in the process.
	 */
	@Override
	public VirtualTable previousPage() throws SQLException
	{
		if(!this.hasPreviousPage())
			return null;
		
		final int rowsPerPage = this.getRowsPerPage();
		final int prevPageIndex = this.getCurrentRowIndex() - rowsPerPage;
		final VirtualTable vt = this.getCachedVirtualTable(prevPageIndex,rowsPerPage);
		
		if(vt != null)
		{
			this.setCurrentRowIndex(prevPageIndex);
			return vt;
		}
		else
		{
			return this.previousPageNoCheck();
		}
	}
	

	// /////////////////////////////////////////////////////////////////////////
	// declared methods //
	// ///////////////////
	
	/**
	 * Puts <code>vt</code> into the cache with a key derived from
	 * <code>rowIndexFrom</code> and <code>rowCount</code>.
	 * 
	 * @param vt
	 *            the <code>VirtualTable</code> object to be cached.
	 * @param rowIndexFrom
	 *            the row index in the scrollable <code>ResultSet</code> object
	 *            used to create <code>vt</code> that describes the first row of
	 *            <code>vt</code>.
	 * @param rowCount
	 *            the number of rows for which <code>vt</code> has been created.
	 * @return the <code>VirtualTable</code> object that was cached for the
	 *         equal key before.
	 */
	protected VirtualTable putCachedVirtualTable(final VirtualTable vt, final int rowIndexFrom,
			final int rowCount)
	{
		synchronized(this.cachedPages)
		{
			final Long hashKey = createHashKey(rowIndexFrom,rowCount);
			return getVTFromReference(this.cachedPages.put(hashKey,new SoftReference<VirtualTable>(
					vt)));
		}
	}
	

	/**
	 * Retrieves the <code>VirtualTable</code> object from the cache that has
	 * been cached for <code>rowIndexFrom</code> and <code>rowCount</code>.
	 * 
	 * @param rowIndexFrom
	 *            the row index in the scrollable <code>ResultSet</code> object
	 *            used to create <code>vt</code> that describes the first row of
	 *            <code>vt</code>.
	 * @param rowCount
	 *            the number of rows the searched <code>vt</code> shall contain.
	 * @return
	 */
	protected VirtualTable getCachedVirtualTable(final int rowIndexFrom, final int rowCount)
	{
		synchronized(this.cachedPages)
		{
			return getVTFromReference(this.cachedPages.get(createHashKey(rowIndexFrom,rowCount)));
		}
	}
	
}
