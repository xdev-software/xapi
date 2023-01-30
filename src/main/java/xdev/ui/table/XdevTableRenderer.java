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


import java.awt.Color;
import java.awt.Component;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;

import xdev.ui.TableSupport;
import xdev.ui.XdevTable;
import xdev.vt.VirtualTable;
import xdev.vt.VirtualTableColumn;
import xdev.vt.VirtualTableWrapper;
import xdev.vt.XdevBlob;


public class XdevTableRenderer implements TableCellRenderer
{
	private static final Border						DEFAULT_NO_FOCUS_BORDER;
	
	private static RendererDelegate					_defaultDelegate;
	private static Map<Class<?>, RendererDelegate>	_delegates;
	
	static
	{
		DEFAULT_NO_FOCUS_BORDER = new EmptyBorder(1,1,1,1);
		
		setPublicDefaultDelegate(new DefaultRenderer());
		
		_delegates = new HashMap();
		setPublicDelegate(Date.class,new DateRenderer());
		setPublicDelegate(XdevBlob.class,new BlobRenderer());
		
		BooleanRenderer booleanRenderer = new BooleanRenderer();
		setPublicDelegate(boolean.class,booleanRenderer);
		setPublicDelegate(Boolean.class,booleanRenderer);
	}
	
	
	public static void setPublicDefaultDelegate(RendererDelegate defaultDelegate)
	{
		_defaultDelegate = defaultDelegate;
	}
	
	
	public static void setPublicDelegate(Class<?> type, RendererDelegate delegate)
	{
		if(delegate == null)
		{
			_delegates.remove(type);
		}
		else
		{
			_delegates.put(type,delegate);
		}
	}
	
	private RendererDelegate				defaultDelegate;
	private Map<Class<?>, RendererDelegate>	delegates;
	
	
	public XdevTableRenderer()
	{
		defaultDelegate = _defaultDelegate;
		delegates = new HashMap(_delegates);
	}
	
	
	public void setPrivateDefaultDelegate(RendererDelegate defaultDelegate)
	{
		this.defaultDelegate = defaultDelegate;
	}
	
	
	public void setPrivateDelegate(Class<?> type, RendererDelegate delegate)
	{
		if(delegate == null)
		{
			delegates.remove(type);
		}
		else
		{
			delegates.put(type,delegate);
		}
	}
	
	
	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
			boolean hasFocus, int row, int viewColumn)
	{
		boolean editable = table.isCellEditable(row,viewColumn);
		int column = TableSupport.getTableColumnConverter().viewToModel(table,viewColumn);
		
		RendererDelegate delegate = null;
		JComponent cpn = null;
		VirtualTable vt = null;
		VirtualTableColumn<?> vtColumn = null;
		
		TableModel actualModel = TableSupport.getTableModelWrapperLookup().lookupTableModel(
				table.getModel(),VirtualTableWrapper.class);
		
		// in case no VirtualTableWrapper was found
		if(actualModel == null)
		{
			actualModel = table.getModel();
		}
		
		// -1 if column is not in the associated VT
		if(!(column == -1))
		{
			vtColumn = TableUtils.getVirtualTableColumn(table,column);
			vt = vtColumn != null ? vtColumn.getVirtualTable() : null;
			Class<?> clazz = Object.class;
			if(vtColumn != null)
			{
				clazz = vtColumn.getType().getJavaClass();
			}
			else
			{
				clazz = actualModel.getColumnClass(column);
			}
			delegate = this.getDelegate(clazz);
		}
		else
		{
			delegate = defaultDelegate;
		}
		
		cpn = delegate.getRendererComponent(table,value,row,column,vt,vtColumn,editable);
		
		Color fg = null;
		Color bg = null;
		
		JTable.DropLocation dropLocation = table.getDropLocation();
		if(dropLocation != null && !dropLocation.isInsertRow() && !dropLocation.isInsertColumn()
				&& dropLocation.getRow() == row && dropLocation.getColumn() == viewColumn)
		{
			fg = UIManager.getColor("Table.dropCellForeground");
			bg = UIManager.getColor("Table.dropCellBackground");
			
			isSelected = true;
		}
		
		if(isSelected)
		{
			cpn.setForeground(fg != null ? fg : table.getSelectionForeground());
			cpn.setBackground(bg != null ? bg : table.getSelectionBackground());
		}
		else
		{
			if(table instanceof XdevTable)
			{
				XdevTable xtable = (XdevTable)table;
				bg = row % 2 == 0 ? xtable.getEvenBackground() : xtable.getOddBackground();
			}
			else
			{
				bg = null;
			}
			cpn.setForeground(table.getForeground());
			cpn.setBackground(bg != null ? bg : table.getBackground());
		}
		
		cpn.setOpaque(isSelected || table.isOpaque());
		cpn.setFont(table.getFont());
		
		Border border = null;
		if(hasFocus)
		{
			if(isSelected)
			{
				border = UIManager.getBorder("Table.focusSelectedCellHighlightBorder");
			}
			if(border == null)
			{
				border = UIManager.getBorder("Table.focusCellHighlightBorder");
			}
			
			if(!isSelected && editable)
			{
				Color col;
				col = UIManager.getColor("Table.focusCellForeground");
				if(col != null)
				{
					cpn.setForeground(col);
				}
				col = UIManager.getColor("Table.focusCellBackground");
				if(col != null)
				{
					cpn.setBackground(col);
				}
			}
		}
		else
		{
			border = UIManager.getBorder("Table.cellNoFocusBorder");
			if(border == null)
			{
				border = DEFAULT_NO_FOCUS_BORDER;
			}
		}
		cpn.setBorder(border);
		
		int height = cpn.getPreferredSize().height;
		if(table.getRowHeight(row) < height)
		{
			table.setRowHeight(row,height);
		}
		
		return cpn;
	}
	
	
	private RendererDelegate getDelegate(Class<?> clazz)
	{
		RendererDelegate delegate = this.delegates.get(clazz);
		
		if(delegate == null)
		{
			return defaultDelegate;
		}
		
		return delegate;
	}
}
