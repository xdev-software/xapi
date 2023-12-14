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
package xdev.ui;


/**
 * Components which have a selectionModel can implement this class to customize
 * the selection process.
 * 
 * The indices handled by this class must be the ensured model rows (for example
 * unfiltered and unsorted).
 * 
 * 
 * @author XDEV Software (JWill,FH)
 * @since 1.1
 */
public interface RowSelectionHandler
{
	/**
	 * A customized selection process for one index to select.
	 * 
	 * @param row
	 *            the rowIndex to select.
	 */
	public void selectRow(int row);
	
	
	/**
	 * A customized selection process for multiple indices to select.
	 * 
	 * @param row0
	 *            one end of the interval.
	 * @param row1
	 *            other end of the interval
	 */
	public void selectRowInterval(int row0, int row1);
	
	
	/**
	 * A customized selection process for multiple indices to select.
	 * 
	 * @param rows
	 *            the indices to select
	 */
	public void selectRows(int... rows);
}
