package xdev.ui.listbox;

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


import javax.swing.JList;
import javax.swing.ListSelectionModel;

import xdev.ui.RowSelectionHandler;


/**
 * Default implementation vor {@link JList} selection handling.
 * 
 * @author XDEV Software JWill
 * @since 3.2
 */
public class ListRowSelectionHandler implements RowSelectionHandler
{
	/**
	 * 
	 */
	private JList	list;
	
	
	/**
	 * Default implemenation of {@link JList} selection process.
	 * 
	 * @param list
	 *            the list which is affected by the selection.
	 */
	public ListRowSelectionHandler(JList list)
	{
		this.list = list;
	}
	
	
	public JList getList()
	{
		return list;
	}
	
	
	@Override
	public void selectRow(int row)
	{
		list.setSelectionInterval(row,row);
	}
	
	
	@Override
	public void selectRowInterval(int row0, int row1)
	{
		list.setSelectionInterval(row0,row1);
	}
	
	//master-detail event management
	@Override
	public void selectRows(int... rows)
	{
		ListSelectionModel model = list.getSelectionModel();
		model.setValueIsAdjusting(true);
		model.clearSelection();
		for(int i = 0; i < rows.length; i++)
		{
			model.addSelectionInterval(rows[i],rows[i]);
		}
		model.setValueIsAdjusting(false);
	}
}
