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


/**
 * Interface for result pagers. It provides methods for scrolling forward,
 * backward, jump to the first or the last page and others.
 * 
 * @author XDEV Software (HL)
 * @since 4.0
 */
public interface DBPager extends AutoCloseable
{
	/**
	 * @return the datasource of this pager
	 */
	public DBDataSource<?> getDataSource();
	
	
	/**
	 * @return <code>true</code> if the pager can go forward
	 */
	public boolean hasNextPage();
	
	
	/**
	 * @return <code>true</code> if the pager can go backward
	 */
	public boolean hasPreviousPage();
	
	
	/**
	 * Scrolls to the next Page
	 * 
	 * @throws DBException
	 */
	public Result nextPage() throws DBException;
	
	
	/**
	 * Scrolls to the previous Page
	 * 
	 * @throws DBException
	 */
	public Result previousPage() throws DBException;
	
	
	/**
	 * Scrolls to the first Page
	 * 
	 * @throws DBException
	 */
	public Result firstPage() throws DBException;
	
	
	/**
	 * Scrolls to the last Page
	 * 
	 * @throws DBException
	 */
	public Result lastPage() throws DBException;
	
	
	/**
	 * Scrolls to the given pageNumber
	 * 
	 * @param page
	 */
	public Result gotoPage(final int page) throws DBException;
	
	
	/**
	 * Scrolls to the given row
	 * 
	 * @param row
	 */
	public Result gotoRow(final int row) throws DBException;
	
	
	/**
	 * returns the number of the current Page
	 * 
	 * @return <code>int</code>
	 */
	public int getCurrentPageIndex();
	
	
	/**
	 * returns the number of the last Page
	 * 
	 * @return <code>int</code>
	 */
	public int getMaxPageIndex();
	
	
	/**
	 * Sets the number of rows to be shown on a page and returns the new result
	 * of the current page.
	 * 
	 * @param rowsPerPage
	 *            <code>int</code>
	 */
	public Result setRowsPerPage(final int rowsPerPage) throws DBException;
	
	
	/**
	 * Returns the number of rows to be shown on a page.
	 * 
	 * @return rowsPerPage <code>int</code>
	 */
	public int getRowsPerPage();
	
	
	/**
	 * Returns the number of all rows, returned from the database
	 * 
	 * @return totalRows <code>int</code>
	 */
	public int getTotalRows();
	
	
	/**
	 * Closes the pager and its underlying resources.
	 */
	public void close() throws DBException;
	
	
	/**
	 * @return <code>true</code> if this pager or an underlying resource has
	 *         been closed, <code>false</code> otherwise
	 */
	public boolean isClosed() throws DBException;
}
