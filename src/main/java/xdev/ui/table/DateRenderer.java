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
