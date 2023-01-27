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
import javax.swing.ListSelectionModel;

import xdev.ui.RowSelectionHandler;


/**
 * Default implementation for {@link JTable} selection handling.
 * 
 * @author XDEV Software (JWill,FH)
 * @since 1.1
 */
public class TableRowSelectionHandler implements RowSelectionHandler
{
	/**
	 * 
	 */
	private JTable	table;
	
	
	/**
	 * Default implemenation of a {@link JTable}s selection process.
	 * 
	 * @param table
	 *            the table which is affected by the selection.
	 */
	public TableRowSelectionHandler(JTable table)
	{
		this.table = table;
	}
	
	
	public JTable getTable()
	{
		return table;
	}
	
	
	@Override
	public void selectRow(int row)
	{
		table.setRowSelectionInterval(row,row);
	}
	
	
	@Override
	public void selectRowInterval(int row0, int row1)
	{
		table.setRowSelectionInterval(row0,row1);
	}
	
	
	@Override
	public void selectRows(int... rows)
	{
		ListSelectionModel model = table.getSelectionModel();
		model.setValueIsAdjusting(true);
		model.clearSelection();
		for(int i = 0; i < rows.length; i++)
		{
			model.addSelectionInterval(rows[i],rows[i]);
		}
		model.setValueIsAdjusting(false);
	}
}
