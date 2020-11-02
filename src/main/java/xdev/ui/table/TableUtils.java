package xdev.ui.table;

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


import javax.swing.JTable;

import xdev.ui.VirtualTableEditor;
import xdev.ui.VirtualTableOwner;
import xdev.vt.VirtualTable;
import xdev.vt.VirtualTableColumn;
import xdev.vt.VirtualTableWrapper;


public final class TableUtils
{
	private TableUtils()
	{
	}


	public static VirtualTable getVirtualTable(JTable table)
	{
		if(table instanceof VirtualTableEditor)
		{
			VirtualTableWrapper wrapper = ((VirtualTableEditor)table).getVirtualTableWrapper();
			VirtualTable vt = wrapper.getVirtualTable();
			if(vt != null)
			{
				return vt;
			}
		}

		if(table instanceof VirtualTableOwner)
		{
			return ((VirtualTableOwner)table).getVirtualTable();
		}

		return null;
	}


	public static VirtualTableColumn getVirtualTableColumn(JTable table, int column)
	{
		if(table instanceof VirtualTableEditor)
		{
			VirtualTableWrapper wrapper = ((VirtualTableEditor)table).getVirtualTableWrapper();
			if(wrapper != null)
			{
				VirtualTable vt = wrapper.getVirtualTable();
				if(vt != null)
				{
					return vt.getColumnAt(wrapper.viewToModelColumn(column));
				}
			}
		}

		if(table instanceof VirtualTableOwner)
		{
			VirtualTable vt = ((VirtualTableOwner)table).getVirtualTable();
			if(vt != null)
			{
				return vt.getColumnAt(column);
			}
		}

		return null;
	}
}
