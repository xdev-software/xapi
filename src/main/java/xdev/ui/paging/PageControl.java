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
package xdev.ui.paging;


import javax.swing.event.ChangeListener;

import xdev.vt.VirtualTable;


/**
 * @author XDEV Software (HL)
 * @since 4.0
 */
public interface PageControl
{
	public boolean isSingleRowPager();
	
	/**
	 * constant for the rowsPerPage property to display as much rows as can be
	 * visible
	 */
	public final static int	DYNAMIC_ROW_COUNT	= 0;
	
	
	/**
	 * @param rows
	 */
	public void setRowsPerPage(int rows);
	
	
	/**
	 * @return the number of rows to display
	 * @see #DYNAMIC_ROW_COUNT
	 */
	public int getRowsPerPage();
	
	
	/**
	 * @return <code>true</code> if the pager can go forward
	 */
	public boolean hasNextPage();
	
	
	/**
	 * @return <code>true</code> if the pager can go backward
	 */
	public boolean hasPreviousPage();
	
	
	/**
	 * Scrolls to the next page
	 */
	public void nextPage();
	
	
	/**
	 * Scrolls to the previous page
	 */
	public void previousPage();
	
	
	/**
	 * Scrolls to the first page
	 */
	public void firstPage();
	
	
	/**
	 * Scrolls to the last page
	 */
	public void lastPage();
	
	
	/**
	 * Scrolls to the given pageNumber
	 * 
	 * @param page
	 */
	public void gotoPage(int page);
	
	
	/**
	 * @return the index of the current page
	 */
	public int getCurrentPageIndex();
	
	
	/**
	 * @return the index of the last page
	 */
	public int getMaxPageIndex();
	
	
	/**
	 * refreshes the current view
	 */
	public void refresh();
	
	
	/**
	 * Adds a changelistener which listens for data or position changes
	 * 
	 * @param l
	 */
	public void addChangeListener(ChangeListener l);
	
	
	/**
	 * @return the underlying virtual table of the controlled component
	 */
	public VirtualTable getVirtualTable();
}
