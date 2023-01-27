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
package xdev.ui.paging;


import java.awt.Container;
import java.util.List;

import javax.swing.JTable;
import javax.swing.JViewport;
import javax.swing.RowSorter;
import javax.swing.RowSorter.SortKey;
import javax.swing.SortOrder;
import javax.swing.SwingUtilities;
import javax.swing.event.RowSorterEvent;
import javax.swing.event.RowSorterEvent.Type;
import javax.swing.event.RowSorterListener;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import xdev.db.DBException;
import xdev.db.sql.SELECT;
import xdev.ui.VirtualTableOwner;
import xdev.vt.VirtualTable;
import xdev.vt.VirtualTableColumn;


/**
 * @author XDEV Software (HL)
 * @since 4.0
 */
public class TablePageControl<T extends JTable & VirtualTableOwner> extends AbstractPageControl<T>
{
	protected SortKey	sortKey;
	private T			table;
	
	
	public TablePageControl(T table)
	{
		super(table);
		this.table = table;
	}
	
	
	@Override
	public boolean isSingleRowPager()
	{
		return false;
	}
	
	
	@Override
	public VirtualTable getVirtualTable()
	{
		return this.table.getVirtualTable();
	}
	
	
	public RowSorter createRowSorter(TableModel model)
	{
		TableRowSorter sorter = new TableRowSorter(model)
		{
			@Override
			public void sort()
			{
				fireRowSorterChanged(null);
			}
		};
		
		sorter.addRowSorterListener(new RowSorterListener()
		{
			int	call	= 1;
			
			
			@Override
			public void sorterChanged(RowSorterEvent e)
			{
				if(call % 2 == 0)
				{
					Type t = e.getType();
					if(t == Type.SORTED && call != 2)
					{
						return;
					}
					List<?> l = e.getSource().getSortKeys();
					if(l.isEmpty())
					{
						return;
					}
					sortKey = (SortKey)l.get(0);
					SwingUtilities.invokeLater(new Runnable()
					{
						@Override
						public void run()
						{
							refresh();
						}
					});
				}
				call++;
			}
		});
		
		return sorter;
	}
	
	
	@Override
	public void changeModel(SELECT select, Object[] params, int showPageIndex) throws DBException
	{
		if(sortKey != null)
		{
			int viewIndex = sortKey.getColumn();
			VirtualTable vt = getVirtualTable();
			int[] indizes = vt.getVisibleColumnIndices();
			int sortindex = indizes[viewIndex];
			VirtualTableColumn<?> col = vt.getColumnAt(sortindex);
			select.clear_ORDER_BY();
			select.ORDER_BY(col,sortKey.getSortOrder() == SortOrder.DESCENDING);
		}
		
		super.changeModel(select,params,showPageIndex);
	}
	
	
	@Override
	public int getVisibleRowCount()
	{
		JTable table = this.table;
		if(!table.isShowing())
		{
			return 0;
		}
		
		int rowHeight = table.getRowHeight();
		int place;
		Container parent = table.getParent();
		if(parent instanceof JViewport)
		{
			place = ((JViewport)parent).getExtentSize().height;
		}
		else
		{
			place = table.getPreferredScrollableViewportSize().height;
		}
		return place / rowHeight;
	}
}
