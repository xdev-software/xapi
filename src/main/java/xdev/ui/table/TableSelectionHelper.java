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
package xdev.ui.table;


import javax.swing.JTable;

import xdev.ui.XdevTable;


/**
 * Helps with selection on {@link JTable}.
 * 
 * <p>
 * Immutable
 * </p>
 * 
 * @author XDEV Software
 * 
 * @see JTable
 * @see XdevTable
 * 
 * @since 3.0
 * 
 */
public class TableSelectionHelper
{
	/**
	 * holds the {@link JTable} internally
	 */
	private final JTable	table;
	

	/**
	 * Creates a new immutable {@link TableSelectionHelper}.
	 * 
	 * @param table
	 *            that needs help on selection
	 */
	public TableSelectionHelper(final JTable table)
	{
		this.table = table;
	}
	

	/**
	 * Selects the next row of the table. If there is currently no row selected,
	 * the first row of the table will be selected. If the last row is selected,
	 * the first row of the table will be next.
	 * 
	 * 
	 * <p>
	 * Works only for tables in single selection mode.
	 * </p>
	 * 
	 * 
	 * 
	 * @return index of the row that is selected by the method call
	 */
	public int selectNextRow()
	{
		
		int selRowIndex = table.getSelectedRow();
		
		if(selRowIndex == -1 || selRowIndex >= table.getRowCount() - 1)
		{
			
			// start form the top of the table
			selRowIndex = 0;
		}
		else
		{
			selRowIndex++;
		}
		
		table.setRowSelectionInterval(selRowIndex,selRowIndex);
		
		return selRowIndex;
	}
	

	/**
	 * Selects the previous row of the table. If there is currently no row
	 * selected, the last row of the table will be selected. If the first row is
	 * selected, the first row of the table will be "previous".
	 * 
	 * 
	 * <p>
	 * Works only for tables in single selection mode.
	 * </p>
	 * 
	 * 
	 * 
	 * @return index of the row that is selected by the method call
	 */
	public int selectPreviousRow()
	{
		
		int selRowIndex = table.getSelectedRow();
		
		if(selRowIndex == -1 || selRowIndex == 0)
		{
			
			// start form the bottom of the table
			selRowIndex = table.getRowCount() - 1;
		}
		else
		{
			selRowIndex--;
		}
		
		table.setRowSelectionInterval(selRowIndex,selRowIndex);
		
		return selRowIndex;
	}
	
}
