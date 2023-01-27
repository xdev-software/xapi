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
package xdev.ui.listbox;


import java.util.Arrays;
import java.util.Collection;
import java.util.Map;

import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import xdev.db.sql.Condition;
import xdev.ui.FormularComponent.ValueChangeListener;
import xdev.ui.FormularComponentSupport;
import xdev.ui.ItemList;
import xdev.ui.ItemList.Entry;
import xdev.ui.ItemListOwner;
import xdev.ui.MasterDetailComponent;
import xdev.ui.MasterDetailComponent.DetailHandler;
import xdev.ui.RowSelectionHandler;
import xdev.ui.XdevListBox;
import xdev.util.IntList;
import xdev.util.ObjectUtils;
import xdev.util.XdevList;
import xdev.vt.VirtualTable;
import xdev.vt.VirtualTable.VirtualTableRow;


public class ListBoxSupport<L extends JList & MasterDetailComponent<L>> extends
		FormularComponentSupport<L>
{
	private static ListBoxRowConverter		listRowConverter;
	private static ListModelWrapperLookup	listModelWrapperLookup;
	private RowSelectionHandler				rowSelectionHandler;
	
	
	public void setRowSelectionHandler(RowSelectionHandler rowSelectionHandler)
	{
		this.rowSelectionHandler = rowSelectionHandler;
	}
	
	
	public RowSelectionHandler getRowSelectionHandler()
	{
		if(rowSelectionHandler == null)
		{
			rowSelectionHandler = new ListRowSelectionHandler(this.component);
		}
		
		return rowSelectionHandler;
	}
	
	
	/**
	 * 
	 * @since 3.1
	 */
	public static void setListBoxRowConverter(ListBoxRowConverter listRowConverter)
	{
		ListBoxSupport.listRowConverter = listRowConverter;
	}
	
	
	/**
	 * 
	 * @since 3.1
	 */
	public static ListBoxRowConverter getListBoxRowConverter()
	{
		if(listRowConverter == null)
		{
			listRowConverter = new DefaultListBoxRowConverter();
		}
		
		return listRowConverter;
	}
	
	
	/**
	 * 
	 * @since 3.1
	 */
	public static void setListModelWrapperLookup(ListModelWrapperLookup listModelWrapperLookup)
	{
		ListBoxSupport.listModelWrapperLookup = listModelWrapperLookup;
	}
	
	
	/**
	 * 
	 * @since 3.1
	 */
	public static ListModelWrapperLookup getListModelWrapperLookup()
	{
		if(listModelWrapperLookup == null)
		{
			listModelWrapperLookup = new DefaultListModelWrapperLookup();
		}
		
		return listModelWrapperLookup;
	}
	
	protected int[]			savedValue	= new int[0];
	protected DetailHandler	detailHandler;
	
	
	public ListBoxSupport(L list)
	{
		super(list);
	}
	
	
	// *****************************************************
	// FormularComponent Support
	// *****************************************************
	
	public boolean isMultiSelect()
	{
		return component.getSelectionMode() != ListSelectionModel.SINGLE_SELECTION;
	}
	
	
	public void setFormularValue(VirtualTable vt, Map<String, Object> record)
	{
		if(!hasDataField())
		{
			return;
		}
		
		component.clearSelection();
		setValue(vt,getValuesForDataFields(record));
	}
	
	
	public void setValue(VirtualTable vt, Map<String, Object> value)
	{
		selectFormularValue(value);
		if(component.isSelectionEmpty() && detailHandler != null)
		{
			detailHandler.checkDetailView(value);
			selectFormularValue(value);
		}
	}
	
	
	private void selectFormularValue(Map<String, Object> valueMap)
	{
		if(valueMap.size() > 0)
		{
			Object[] values = valueMap.values().toArray();
			
			ListModel model = component.getModel();
			
			for(int row = 0, size = model.getSize(); row < size; row++)
			{
				Object modelElem = model.getElementAt(row);
				if(ObjectUtils.equals(getValueObjectAsArray(modelElem),values))
				{
					setSelectedModelIndex(row);
					break;
				}
			}
		}
	}
	
	
	private Object getValueObject(Object value)
	{
		if(value instanceof Entry)
		{
			value = ((Entry)value).getData();
		}
		return value;
	}
	
	
	private Object getValueObjectAsArray(Object value)
	{
		if(value instanceof Entry)
		{
			value = ((Entry)value).getData();
		}
		if(value == null || !value.getClass().isArray())
		{
			value = new Object[]{value};
		}
		return value;
	}
	
	
	public Object getFormularValue()
	{
		Object[] values = component.getSelectedValues();
		for(int i = 0; i < values.length; i++)
		{
			values[i] = getValueObject(values[i]);
		}
		
		if(values.length == 0)
		{
			return null;
		}
		else if(values.length == 1 && !isMultiSelect())
		{
			return values[0];
		}
		return values;
	}
	
	
	public void saveState()
	{
		savedValue = component.getSelectedIndices();
	}
	
	
	public void restoreState()
	{
		component.setSelectedIndices(savedValue);
	}
	
	
	/**
	 * @since 3.1
	 */
	public boolean hasStateChanged()
	{
		return !Arrays.equals(savedValue,component.getSelectedIndices());
	}
	
	
	public final ItemListOwner getItemListOwner()
	{
		ListModel model = getListModelWrapperLookup().lookupListModel(component.getModel(),
				ItemListOwner.class);
		if(model instanceof ItemListOwner)
		{
			return (ItemListOwner)model;
		}
		
		return null;
	}
	
	
	public final ItemList getItemList()
	{
		ItemListOwner owner = getItemListOwner();
		if(owner != null)
		{
			return owner.getItemList();
		}
		
		return null;
	}
	
	
	public VirtualTable getVirtualTable()
	{
		ItemList itemList = getItemList();
		if(itemList != null)
		{
			return itemList.getVirtualTable();
		}
		
		return null;
	}
	
	
	// *****************************************************
	// MasterDetailComponent Support
	// *****************************************************
	
	public VirtualTableRow getSelectedVirtualTableRow()
	{
		int modelIndex = getSelectedModelRow();
		if(modelIndex >= 0)
		{
			VirtualTable vt = getVirtualTable();
			if(vt != null)
			{
				return vt.getRow(modelIndex);
			}
		}
		
		return null;
	}
	
	
	/**
	 * @since 3.2
	 */
	public VirtualTableRow[] getSelectedVirtualTableRows()
	{
		VirtualTable vt = getVirtualTable();
		if(vt != null)
		{
			int[] modelIndices = getSelectedModelRows();
			VirtualTableRow[] rows = new VirtualTableRow[modelIndices.length];
			for(int i = 0; i < modelIndices.length; i++)
			{
				rows[i] = vt.getRow(modelIndices[i]);
			}
			return rows;
		}
		
		return null;
	}
	
	
	/**
	 * @since 3.2
	 */
	public void setSelectedVirtualTableRow(VirtualTableRow row)
	{
		if(row == null)
		{
			component.clearSelection();
		}
		else
		{
			VirtualTable vt = getVirtualTable();
			if(vt != null)
			{
				int rc = vt.getRowCount();
				for(int i = 0; i < rc; i++)
				{
					if(vt.getRow(i).equals(row))
					{
						setSelectedModelIndex(i);
						break;
					}
				}
			}
		}
	}
	
	
	/**
	 * @since 3.2
	 */
	public void setSelectedVirtualTableRows(VirtualTableRow[] rows)
	{
		if(rows == null || rows.length == 0)
		{
			component.clearSelection();
		}
		else
		{
			VirtualTable vt = getVirtualTable();
			if(vt != null)
			{
				IntList rowIndices = new IntList();
				int rc = vt.getRowCount();
				for(int i = 0; i < rc; i++)
				{
					VirtualTableRow listRow = vt.getRow(i);
					for(VirtualTableRow row : rows)
					{
						if(listRow.equals(row))
						{
							rowIndices.add(i);
							break;
						}
					}
				}
				setSelectedModelIndices(rowIndices.toArray());
			}
		}
	}
	
	
	private int getSelectedModelRow() throws IndexOutOfBoundsException
	{
		int row = component.getSelectedIndex();
		if(row != -1)
		{
			row = getListBoxRowConverter().viewToModel(component,row);
		}
		return row;
	}
	
	
	private int[] getSelectedModelRows() throws IndexOutOfBoundsException
	{
		ListBoxRowConverter rowConverter = getListBoxRowConverter();
		int[] rows = component.getSelectedIndices();
		for(int i = 0; i < rows.length; i++)
		{
			rows[i] = rowConverter.viewToModel(component,rows[i]);
		}
		return rows;
	}
	
	
	public void refresh()
	{
		ItemList itemList = getItemList();
		if(itemList != null)
		{
			itemList.refresh();
		}
	}
	
	
	public void updateModel(Condition condition, Object... params)
	{
		component.clearSelection();
		ItemList itemList = getItemList();
		if(itemList != null)
		{
			itemList.updateModel(condition,params);
		}
	}
	
	
	public void clearModel()
	{
		ItemList itemList = getItemList();
		if(itemList != null)
		{
			itemList.clear();
		}
		else
		{
			component.setModel(new DefaultListModel());
		}
	}
	
	
	public void addValueChangeListener(final ValueChangeListener l)
	{
		component.addListSelectionListener(new ListSelectionListener()
		{
			@Override
			public void valueChanged(ListSelectionEvent e)
			{
				if(!e.getValueIsAdjusting())
				{
					l.valueChanged(e);
				}
			}
		});
	}
	
	
	public void setDetailHandler(DetailHandler detailHandler)
	{
		this.detailHandler = detailHandler;
	}
	
	
	/**
	 * @see XdevListBox#setSelectedData(Object)
	 * 
	 * @param o
	 *            the object to select.
	 */
	public void setSelectedData(Object o)
	{
		ItemList itemList = getItemList();
		if(itemList != null)
		{
			int viewIndex = getListBoxRowConverter().modelToView(component,itemList.indexOfData(o));
			component.setSelectedIndex(o == null ? -1 : viewIndex);
		}
	}
	
	
	/**
	 * @see XdevListBox#setSelectedData(Collection)
	 * 
	 * @param dataList
	 *            the list to get the data to select from.
	 */
	public void setSelectedData(Collection<?> dataList)
	{
		ItemList itemList = getItemList();
		if(itemList != null)
		{
			IntList indices = new IntList();
			
			for(Object data : dataList)
			{
				int viewIndex = getListBoxRowConverter().modelToView(component,
						itemList.indexOfData(data));
				if(viewIndex != -1)
				{
					indices.add(viewIndex);
				}
			}
			
			component.setSelectedIndices(indices.toArray());
		}
	}
	
	
	/**
	 * @see XdevListBox#setSelectedItems(Collection)
	 * 
	 * @param items
	 *            the list to get the items to select from.
	 */
	public void setSelectedItems(Collection<?> items)
	{
		ItemList itemList = getItemList();
		if(itemList != null)
		{
			IntList indices = new IntList();
			
			for(Object item : items)
			{
				int index = getListBoxRowConverter().modelToView(component,
						itemList.indexOfItem(item));
				if(index != -1)
				{
					indices.add(index);
				}
			}
			
			component.setSelectedIndices(indices.toArray());
		}
	}
	
	
	/**
	 * @see XdevListBox#setSelectedModelIndices(int[])
	 * 
	 * @param indices
	 *            containing the modelindices to select.
	 */
	public void setSelectedModelIndices(int[] indices)
	{
		ItemList itemList = getItemList();
		if(itemList != null)
		{
			IntList viewIndices = new IntList();
			for(int i = 0; i < indices.length; i++)
			{
				int viewIndex = getListBoxRowConverter().modelToView(component,indices[i]);
				if(viewIndex != -1)
				{
					viewIndices.add(viewIndex);
				}
			}
			this.getRowSelectionHandler().selectRows(viewIndices.toArray());
			int[] selectedIndices = component.getSelectedIndices();
			if(selectedIndices != null && selectedIndices.length > 0)
			{
				component.ensureIndexIsVisible(selectedIndices[0]);
			}
		}
	}
	
	
	/**
	 * @see XdevListBox#setSelectedModelIndex(int)
	 * 
	 * @param index
	 *            the modelindex to select.
	 */
	public void setSelectedModelIndex(int index)
	{
		int viewIndex = getListBoxRowConverter().modelToView(component,index);
		this.getRowSelectionHandler().selectRow(viewIndex);
		int selectedIndex = component.getSelectedIndex();
		if(selectedIndex != -1)
		{
			component.ensureIndexIsVisible(selectedIndex);
		}
	}
	
	
	/**
	 * @see XdevListBox#getSelectedModelIndices()
	 * 
	 * @param viewIndices
	 *            the view indices to convert into modelindices.
	 * 
	 * @return the converted modelindices.
	 */
	public int[] getSelectedModelIndices(int[] viewIndices)
	{
		int[] modelIndices = new int[viewIndices.length];
		
		for(int i = 0; i < viewIndices.length; i++)
		{
			modelIndices[i] = getListBoxRowConverter().viewToModel(this.component,viewIndices[i]);
		}
		
		return modelIndices;
	}
	
	
	/**
	 * @see XdevListBox#getSelectedModelIndex()
	 * 
	 * @param viewIndex
	 *            the index to get the modelindex from.
	 * @return the converted modelindex.
	 */
	public int getSelectedModelIndex(int viewIndex)
	{
		int modelIndex = viewIndex;
		
		if(viewIndex != -1)
		{
			modelIndex = getListBoxRowConverter().viewToModel(this.component,viewIndex);
		}
		
		return modelIndex;
	}
	
	
	/**
	 * @see XdevListBox#getSelectedItemsAsList()
	 * 
	 * @return the selected items as list.
	 */
	public XdevList<Object> getSelectedItemsAsList()
	{
		XdevList<Object> list = new XdevList<Object>();
		
		ItemList itemList = getItemList();
		if(itemList != null)
		{
			for(int i : this.component.getSelectedIndices())
			{
				list.add(itemList.getItem(getListBoxRowConverter().viewToModel(this.component,i)));
			}
		}
		else
		{
			for(Object o : this.component.getSelectedValues())
			{
				list.add(o);
			}
		}
		
		return list;
	}
	
	
	/**
	 * @see XdevListBox#getSelectedDataAsList()
	 * 
	 * @return the selected data as list.
	 */
	public XdevList<Object> getSelectedDataAsList()
	{
		XdevList<Object> list = new XdevList<Object>();
		
		ItemList itemList = getItemList();
		if(itemList != null)
		{
			for(int i : this.component.getSelectedIndices())
			{
				list.add(itemList.getData(getListBoxRowConverter().viewToModel(this.component,i)));
			}
		}
		
		return list;
	}
}
