package xdev.ui.paging;

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


import java.awt.Container;

import javax.swing.JViewport;

import xdev.db.DBException;
import xdev.db.Result;
import xdev.ui.XdevListBox;
import xdev.vt.VirtualTable;
import xdev.vt.VirtualTableException;


/**
 * @author XDEV Software (HL)
 * @since 4.0
 */
public class ListBoxPageControl extends ItemListPageControl<XdevListBox>
{
	private XdevListBox	listbox;
	
	
	public ListBoxPageControl(XdevListBox listBox)
	{
		super(listBox);
		this.listbox = listBox;
	}
	
	
	@Override
	public boolean isSingleRowPager()
	{
		return false;
	}
	
	
	@Override
	public VirtualTable getVirtualTable()
	{
		return this.listbox.getVirtualTable();
	}
	
	
	@Override
	protected void updateModel(Result data) throws VirtualTableException, DBException
	{
		super.updateModel(data);
		this.listbox.getItemList().syncWithVT();
	}
	
	
	@Override
	public int getVisibleRowCount()
	{
		XdevListBox list = this.listbox;
		if(!list.isShowing())
		{
			return 0;
		}
		
		int rowHeight;
		int fixed = list.getFixedCellHeight();
		if(fixed > 0)
		{
			rowHeight = fixed;
		}
		else
		{
			rowHeight = list.getCellRenderer().getListCellRendererComponent(list,"@",0,false,false)
					.getPreferredSize().height;
		}
		
		int place;
		Container parent = list.getParent();
		if(parent instanceof JViewport)
		{
			place = ((JViewport)parent).getExtentSize().height;
		}
		else
		{
			place = list.getPreferredScrollableViewportSize().height;
		}
		
		return place / rowHeight;
	}
}
