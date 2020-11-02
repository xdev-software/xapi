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


import java.text.DateFormat;
import java.util.Date;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.SwingConstants;

import xdev.vt.VirtualTable;
import xdev.vt.VirtualTableColumn;


/*
 * Since 3.1 JLabel, before JPanel
 */
public class DateRenderer extends JLabel implements RendererDelegate
{
	public DateRenderer()
	{
	}
	

	@Override
	public JComponent getRendererComponent(JTable table, Object value, int row, int column,
			VirtualTable vt, VirtualTableColumn vtColumn, boolean editable)
	{
		if(value instanceof Date)
		{
			if(vtColumn != null)
			{
				setText(vtColumn.getTextFormat().format(value));
			}
			else
			{
				setText(DateFormat.getDateTimeInstance().format((Date)value));
			}
		}
		else
		{
			setText("");
		}
		
		setHorizontalAlignment(vtColumn != null ? vtColumn.getHorizontalAlignment()
				: SwingConstants.LEADING);
		
		return this;
	}
}
