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


import java.awt.Component;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.util.Date;
import java.util.EventObject;
import java.util.HashMap;
import java.util.Map;

import javax.swing.AbstractCellEditor;
import javax.swing.JTable;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableModel;

import xdev.ui.TableSupport;
import xdev.vt.VirtualTable;
import xdev.vt.VirtualTableColumn;
import xdev.vt.VirtualTableWrapper;
import xdev.vt.XdevBlob;


public class XdevTableEditor extends AbstractCellEditor implements TableCellEditor, KeyListener
{
	public static final Border						DEFAULT_BORDER;
	
	private static EditorDelegate					_defaultDelegate;
	private static EditorDelegate					_detailDelegate;
	private static Map<Class<?>, EditorDelegate>	_delegates;
	
	static
	{
		DEFAULT_BORDER = new EmptyBorder(1,1,1,1);
		
		setPublicDefaultDelegate(new DefaultEditor());
		setPublicDetailDelegate(new DetailEditor());
		
		_delegates = new HashMap();
		setPublicDelegate(Date.class,new DateEditor());
		setPublicDelegate(XdevBlob.class,new BlobEditor());
		
		BooleanEditor booleanEditor = new BooleanEditor();
		setPublicDelegate(boolean.class,booleanEditor);
		setPublicDelegate(Boolean.class,booleanEditor);
		
		NumberEditor numberEditor = new NumberEditor();
		setPublicDelegate(int.class,numberEditor);
		setPublicDelegate(Integer.class,numberEditor);
		setPublicDelegate(double.class,numberEditor);
		setPublicDelegate(Double.class,numberEditor);
	}
	
	
	public static void setPublicDefaultDelegate(EditorDelegate defaultDelegate)
	{
		_defaultDelegate = defaultDelegate;
	}
	
	
	public static void setPublicDetailDelegate(EditorDelegate detailDelegate)
	{
		_detailDelegate = detailDelegate;
	}
	
	
	public static void setPublicDelegate(Class<?> type, EditorDelegate delegate)
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
	
	private static XdevTableEditor			currentEditor;
	
	private EditorDelegate					defaultDelegate;
	private EditorDelegate					detailDelegate;
	private Map<Class<?>, EditorDelegate>	delegates;
	private int								clickCountToStart	= 1;
	private EditorDelegate					delegate;
	
	
	public XdevTableEditor()
	{
		defaultDelegate = _defaultDelegate;
		detailDelegate = _detailDelegate;
		delegates = new HashMap(_delegates);
	}
	
	
	public void setPrivateDefaultDelegate(EditorDelegate defaultDelegate)
	{
		this.defaultDelegate = defaultDelegate;
	}
	
	
	public void setPrivateDetailDelegate(EditorDelegate detailDelegate)
	{
		this.detailDelegate = detailDelegate;
	}
	
	
	public void setPrivateDelegate(Class<?> type, EditorDelegate delegate)
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
	
	
	public int getClickCountToStart()
	{
		return clickCountToStart;
	}
	
	
	public void setClickCountToStart(int clickCountToStart)
	{
		this.clickCountToStart = clickCountToStart;
	}
	
	
	@Override
	public boolean isCellEditable(EventObject anEvent)
	{
		if(anEvent instanceof MouseEvent)
		{
			return ((MouseEvent)anEvent).getClickCount() >= clickCountToStart;
		}
		return true;
	}
	
	
	public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected,
			int viewRow, int viewColumn)
	{
		if(currentEditor != null)
		{
			currentEditor.stopCellEditing();
		}
		
		Component cpn = null;
		VirtualTable vt = null;
		VirtualTableColumn<?> vtColumn = null;
		
		int row = TableSupport.getTableRowConverter().viewToModel(table,viewRow);
		int column = TableSupport.getTableColumnConverter().viewToModel(table,viewColumn);
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
			
			if(vtColumn != null && !vtColumn.isPersistent()
					&& vtColumn.getTableColumnLink() != null && this.detailDelegate != null)
			{
				this.delegate = this.detailDelegate;
			}
			else
			{
				Class<?> clazz = Object.class;
				if(vtColumn != null)
				{
					clazz = vtColumn.getType().getJavaClass();
				}
				else
				{
					clazz = actualModel.getColumnClass(column);
				}
				this.delegate = this.getDelegate(clazz);
			}
		}
		else
		{
			this.delegate = this.defaultDelegate;
		}
		
		cpn = this.delegate.getEditorComponent(this,table,value,row,column,vt,vtColumn);
		this.delegate.getFocusComponent().addKeyListener(this);
		
		currentEditor = this;
		
		return cpn;
	}
	
	
	public Object getCellEditorValue()
	{
		return delegate.getEditorValue();
	}
	
	
	@Override
	public void cancelCellEditing()
	{
		if(currentEditor == this)
		{
			currentEditor = null;
			
			delegate.getFocusComponent().removeKeyListener(this);
			delegate.editingCanceled();
			
			super.cancelCellEditing();
		}
	}
	
	
	@Override
	public boolean stopCellEditing()
	{
		if(currentEditor == this)
		{
			currentEditor = null;
			
			delegate.getFocusComponent().removeKeyListener(this);
			delegate.editingStopped();
			
			return super.stopCellEditing();
		}
		
		return true;
	}
	
	
	public void keyPressed(KeyEvent e)
	{
		if(e.getKeyCode() == KeyEvent.VK_ESCAPE)
		{
			cancelCellEditing();
		}
	}
	
	
	public void keyTyped(KeyEvent e)
	{
	}
	
	
	public void keyReleased(KeyEvent e)
	{
	}
	
	
	private EditorDelegate getDelegate(Class<?> clazz)
	{
		EditorDelegate delegate = this.delegates.get(clazz);
		
		if(delegate == null)
		{
			return defaultDelegate;
		}
		
		return delegate;
	}
}
