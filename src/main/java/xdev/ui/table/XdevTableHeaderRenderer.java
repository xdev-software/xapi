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


import java.awt.Component;

import javax.swing.AbstractButton;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.plaf.UIResource;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;

import xdev.vt.VirtualTableColumn;


public class XdevTableHeaderRenderer implements TableCellRenderer, UIResource
{
	private TableCellRenderer	defaultRenderer;
	
	
	public XdevTableHeaderRenderer(JTable table)
	{
		defaultRenderer = table.getTableHeader().getDefaultRenderer();
	}
	
	
	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
			boolean hasFocus, int row, int column)
	{
		TableCellRenderer originalRenderer = null;
		TableColumnModel columnModel = table != null ? table.getColumnModel() : null;
		if(columnModel != null && column >= 0)
		{
			originalRenderer = columnModel.getColumn(column).getHeaderRenderer();
		}
		if(originalRenderer == null)
		{
			originalRenderer = defaultRenderer;
		}
		
		Component cpn = originalRenderer.getTableCellRendererComponent(table,value,isSelected,
				hasFocus,row,column);
		
		int align = SwingConstants.LEADING;
		if(column >= 0)
		{
			VirtualTableColumn vtColumn = TableUtils.getVirtualTableColumn(table,column);
			if(vtColumn != null)
			{
				align = vtColumn.getHorizontalAlignment();
			}
		}
		
		if(cpn instanceof JLabel)
		{
			((JLabel)cpn).setHorizontalAlignment(align);
		}
		else if(cpn instanceof AbstractButton)
		{
			((AbstractButton)cpn).setHorizontalAlignment(align);
		}
		
		return cpn;
	}
}
