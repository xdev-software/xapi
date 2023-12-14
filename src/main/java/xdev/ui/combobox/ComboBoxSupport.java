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
package xdev.ui.combobox;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;

import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.ListModel;

import xdev.db.sql.Condition;
import xdev.ui.FormularComponent.ValueChangeListener;
import xdev.ui.FormularComponentSupport;
import xdev.ui.ItemList;
import xdev.ui.ItemList.Entry;
import xdev.ui.ItemListOwner;
import xdev.ui.MasterDetailComponent;
import xdev.ui.MasterDetailComponent.DetailHandler;
import xdev.ui.XdevComboBox;
import xdev.util.ObjectUtils;
import xdev.vt.VirtualTable;
import xdev.vt.VirtualTable.VirtualTableRow;


public class ComboBoxSupport<C extends JComboBox & MasterDetailComponent<C>> extends
		FormularComponentSupport<C>
{
	private static ComboBoxRowConverter			comboBoxRowConverter;
	private static ComboBoxModelWrapperLookup	comboBoxModelWrapperLookup;
	
	
	/**
	 * 
	 * @since 3.1
	 */
	public static void setComboBoxRowConverter(ComboBoxRowConverter comboBoxRowConverter)
	{
		ComboBoxSupport.comboBoxRowConverter = comboBoxRowConverter;
	}
	
	
	/**
	 * 
	 * @since 3.1
	 */
	public static ComboBoxRowConverter getComboBoxRowConverter()
	{
		if(comboBoxRowConverter == null)
		{
			comboBoxRowConverter = new DefaultComboBoxRowConverter();
		}
		
		return comboBoxRowConverter;
	}
	
	
	/**
	 * 
	 * @since 3.1
	 */
	public static void setComboBoxModelWrapperLookup(
			ComboBoxModelWrapperLookup comboBoxModelWrapperLookup)
	{
		ComboBoxSupport.comboBoxModelWrapperLookup = comboBoxModelWrapperLookup;
	}
	
	
	/**
	 * 
	 * @since 3.1
	 */
	public static ComboBoxModelWrapperLookup getComboBoxModelWrapperLookup()
	{
		if(comboBoxModelWrapperLookup == null)
		{
			comboBoxModelWrapperLookup = new DefaultComboBoxModelWrapperLookup();
		}
		
		return comboBoxModelWrapperLookup;
	}
	
	protected Object		savedValue	= null;
	protected DetailHandler	detailHandler;
	
	
	public ComboBoxSupport(C comboBox)
	{
		super(comboBox);
	}
	
	
	// *****************************************************
	// FormularComponent Support
	// *****************************************************
	
	public boolean isMultiSelect()
	{
		return false;
	}
	
	
	public void setFormularValue(VirtualTable vt, Map<String, Object> record)
	{
		if(!hasDataField())
		{
			return;
		}
		
		setValue(vt,getValuesForDataFields(record));
	}
	
	
	public void setValue(VirtualTable vt, Map<String, Object> value)
	{
		Object selectedValue = getFormularSelectionValue(value);
		if(selectedValue == null && detailHandler != null)
		{
			detailHandler.checkDetailView(value);
			selectedValue = getFormularSelectionValue(value);
		}
		component.setSelectedItem(selectedValue);
	}
	
	
	private Object getFormularSelectionValue(Map<String, Object> valueMap)
	{
		if(valueMap.size() > 0)
		{
			Object[] values = valueMap.values().toArray();
			
			ComboBoxModel model = component.getModel();
			
			if(component.isEditable() && values.length == 1)
			{
				String str = values[0].toString();
				for(int row = 0, size = model.getSize(); row < size; row++)
				{
					Object modelElem = model.getElementAt(row);
					if(String.valueOf(getValueObject(modelElem)).equals(str))
					{
						return modelElem;
					}
				}
			}
			
			for(int row = 0, size = model.getSize(); row < size; row++)
			{
				Object modelElem = model.getElementAt(row);
				if(ObjectUtils.equals(getValueObjectAsArray(modelElem),values))
				{
					return modelElem;
				}
			}
		}
		
		return null;
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
		return getValueObject(component.getSelectedItem());
	}
	
	
	public void saveState()
	{
		savedValue = component.getSelectedItem();
	}
	
	
	public void restoreState()
	{
		component.setSelectedItem(savedValue);
	}
	
	
	/**
	 * @since 3.1
	 */
	public boolean hasStateChanged()
	{
		return !ObjectUtils.equals(savedValue,component.getSelectedItem());
	}
	
	
	public final ItemListOwner getItemListOwner()
	{
		ListModel model = getComboBoxModelWrapperLookup().lookupComboBoxModel(component.getModel(),
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
	public void setSelectedVirtualTableRow(VirtualTableRow row)
	{
		if(row == null)
		{
			component.setSelectedItem(null);
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
	
	
	private int getSelectedModelRow() throws IndexOutOfBoundsException
	{
		int row = component.getSelectedIndex();
		if(row != -1)
		{
			row = getComboBoxRowConverter().viewToModel(component,row);
		}
		return row;
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
		component.setSelectedIndex(-1);
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
			component.setModel(new DefaultComboBoxModel());
		}
	}
	
	
	public void addValueChangeListener(final ValueChangeListener l)
	{
		component.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				l.valueChanged(e);
			}
		});
	}
	
	
	public void setDetailHandler(DetailHandler detailHandler)
	{
		this.detailHandler = detailHandler;
	}
	
	
	/**
	 * @see XdevComboBox#getSelectedModelIndex()
	 * 
	 * @param superIndex
	 *            the view index to get the modelindex from.
	 * @return the converted viewindex.
	 */
	public int getSelectedModelIndex(int superIndex)
	{
		int viewIndex = superIndex;
		
		if(superIndex != -1)
		{
			viewIndex = getComboBoxRowConverter().viewToModel(component,superIndex);
		}
		
		return viewIndex;
	}
	
	
	/**
	 * @see XdevComboBox#setSelectedModelIndex(int)
	 * 
	 * @param index
	 *            the modelindex to convert to view selection index.
	 */
	public void setSelectedModelIndex(int index)
	{
		int viewIndex = getComboBoxRowConverter().modelToView(component,index);
		this.component.setSelectedIndex(viewIndex);
	}
	
	
	/**
	 * @see XdevComboBox#setSelectedData(Object)
	 * @param o
	 *            the object to select.
	 */
	public void setSelectedData(Object o)
	{
		ItemList itemList = getItemList();
		if(itemList != null)
		{
			int viewIndex = getComboBoxRowConverter()
					.modelToView(component,itemList.indexOfData(o));
			component.setSelectedIndex(o == null ? -1 : viewIndex);
		}
		else
		{
			component.setSelectedItem(o);
		}
	}
	
	
	/**
	 * Converts a modelindex of this combobox to the combobox view index.
	 * 
	 * @param index
	 *            the model index
	 * @return the converted model index as view index
	 */
	public int modelToView(int index)
	{
		return getComboBoxRowConverter().modelToView(component,index);
	}
	
}
