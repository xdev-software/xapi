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


import java.io.ByteArrayInputStream;

import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.SwingConstants;

import xdev.io.ByteHolder;
import xdev.io.IOUtils;
import xdev.vt.DataFlavor;
import xdev.vt.VirtualTable;
import xdev.vt.VirtualTableColumn;


public class BlobRenderer extends JLabel implements RendererDelegate
{
	@Override
	public JComponent getRendererComponent(JTable table, Object value, int row, int column,
			VirtualTable vt, VirtualTableColumn vtColumn, boolean editable)
	{
		Icon icon = getIcon();
		if(icon instanceof ImageIcon)
		{
			((ImageIcon)icon).getImage().flush();
		}

		icon = null;
		String text = "";

		DataFlavor flavor = DataFlavor.UNDEFINED;
		int horizontalAlignment = SwingConstants.LEADING;
		if(vtColumn != null)
		{
			flavor = vtColumn.getDataFlavor();
			horizontalAlignment = vtColumn.getHorizontalAlignment();
		}

		switch(flavor)
		{
			case IMAGE:
			{
				byte[] bytes = null;
				if(value instanceof ByteHolder)
				{
					bytes = ((ByteHolder)value).toByteArray();
				}
				else if(value instanceof byte[])
				{
					bytes = (byte[])value;
				}
				if(bytes != null && bytes.length > 0)
				{
					try
					{
						icon = new ImageIcon(ImageIO.read(new ByteArrayInputStream(bytes)));
					}
					catch(Exception e)
					{
					}
				}
			}
			break;

			case UNDEFINED:
			{
				long length = 0;

				if(value instanceof ByteHolder)
				{
					length = ((ByteHolder)value).length();
				}
				else if(value instanceof byte[])
				{
					length = ((byte[])value).length;
				}

				text = "BLOB: " + IOUtils.getFileSize(length);
			}
			break;
		}

		setIcon(icon);
		setText(text);

		setHorizontalAlignment(horizontalAlignment);

		return this;
	}
}
