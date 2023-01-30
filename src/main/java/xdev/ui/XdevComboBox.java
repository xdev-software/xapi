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
package xdev.ui;


import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Map;

import javax.accessibility.Accessible;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.Icon;
import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.JPopupMenu;
import javax.swing.ListCellRenderer;
import javax.swing.MutableComboBoxModel;
import javax.swing.event.ListDataListener;
import javax.swing.plaf.ComboBoxUI;

import xdev.db.Operator;
import xdev.db.sql.Condition;
import xdev.ui.ItemList.Entry;
import xdev.ui.combobox.ComboBoxSupport;
import xdev.util.ObjectUtils;
import xdev.util.StringUtils;
import xdev.vt.VirtualTable;
import xdev.vt.VirtualTable.VirtualTableRow;
import xdev.vt.VirtualTableColumn;


/**
 * The standard combobox in XDEV. Based on {@link JComboBox}.
 *
 * @see JComboBox
 * @see FormularComponent
 * @see ItemListOwner
 * @see XdevFocusCycleComponent
 * @see MasterDetailComponent
 *
 * @author XDEV Software
 *
 * @since 2.0
 */
@BeanSettings(useXdevCustomizer = true)
public class XdevComboBox extends JComboBox
		implements ExtendedList<XdevComboBox>, XdevFocusCycleComponent
{
	private String							preSelectionValue	= null;
	private Color							evenBackground		= null;
	private Color							oddBackground		= null;

	/**
	 * tabIndex is used to store the index for {@link XdevFocusCycleComponent}
	 * functionality.
	 */
	private int								tabIndex			= -1;

	private ComboBoxSupport<XdevComboBox>	support;


	private ComboBoxSupport getSupport()
	{
		if(this.support == null)
		{
			this.support = new ComboBoxSupport(this);
		}

		return this.support;
	}


	/**
	 * Constructs a new {@link XdevComboBox}.
	 *
	 *
	 * @see #XdevComboBox(ItemList)
	 */
	public XdevComboBox()
	{
		this(new ItemList());
	}


	/**
	 * Constructs a {@link XdevComboBox} that is initialized with
	 * {@link ItemList} as the data model.
	 *
	 * @param il
	 *            the {@link ItemList} that provides the displayed and data
	 *            items
	 *
	 * @see #setItemList(ItemList)
	 * @see #setRenderer(javax.swing.ListCellRenderer)
	 */
	public XdevComboBox(final ItemList il)
	{
		super();
		setItemList(il);
		setRenderer(createListCellRenderer());
	}


	/**
	 * Constructs a {@link XdevComboBox} that is initialized with
	 * {@link ComboBoxModel} as the data model.
	 *
	 * @param model
	 *            the {@link ComboBoxModel} with data
	 *
	 * @see #setModel(ComboBoxModel)
	 * @see #setRenderer(javax.swing.ListCellRenderer)
	 */
	public XdevComboBox(final ComboBoxModel model)
	{
		super();
		setModel(model);
		setRenderer(createListCellRenderer());
	}

	{
		addItemListener(new ItemListener()
		{
			Object oldItem = getSelectedItem();


			@Override
			public void itemStateChanged(final ItemEvent e)
			{
				if(e.getStateChange() == ItemEvent.SELECTED)
				{
					final Object item = e.getItem();
					if(isElementSelectable(item))
					{
						this.oldItem = item;
					}
					else
					{
						setSelectedItem(this.oldItem);
					}
				}
			}
		});
	}


	protected boolean isElementSelectable(final Object item)
	{
		if(item instanceof Entry)
		{
			return ((Entry)item).isEnabled();
		}

		return true;
	}


	protected ListCellRenderer createListCellRenderer()
	{
		return new XdevComboBoxListCellRenderer();
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public ItemList getItemList()
	{
		return getSupport().getItemList();
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	@BeanProperty(category = DefaultBeanCategories.DATA)
	public void setItemList(final ItemList itemList)
	{
		setModel(new ItemListModelWrapper(itemList));
	}


	/**
	 * Create a new {@link ItemList} based on the provided {@link ComboBoxModel}
	 * .
	 *
	 * @param model
	 *            The data model
	 *
	 * @return a {@link ItemList}, the data are the model's values and the
	 *         item's are the model's values as {@link String}.
	 */
	public ItemList modelToItemList(final ComboBoxModel model)
	{
		return new ItemList(model);
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setModel(final VirtualTable vt, final String itemCol, final String dataCol)
	{
		setModel(vt,itemCol,dataCol,false);
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setModel(final VirtualTable vt, final String itemCol, final String dataCol,
			final boolean queryData)
	{
		setModel(vt,itemCol,dataCol,queryData,false);
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setModel(final VirtualTable vt, final String itemCol, final String dataCol,
			final boolean queryData, final boolean selectiveQuery)
	{
		if(getItemList() == null)
		{
			setItemList(new ItemList());
		}

		getItemList().setModel(vt,itemCol,dataCol,queryData,selectiveQuery);
		setSelectedItem(null);
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public void refresh()
	{
		getSupport().refresh();
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public void updateModel(final Condition condition, final Object... params)
	{
		getSupport().updateModel(condition,params);
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public void clearModel()
	{
		getSupport().clearModel();
	}


	/**
	 * Sets the value which is displayed before the user selects a value, e.g.
	 * "- Select your country -".
	 *
	 * @param s
	 *            the {@link String} to display in this {@link XdevComboBox}
	 */
	public void setPreSelectionValue(final String s)
	{
		this.preSelectionValue = s;
		repaint();
	}


	/**
	 * Returns the string which is displayed before the user selects a value.
	 *
	 * @return the string which is displayed before the user selects a value.
	 */
	public String getPreSelectionValue()
	{
		return this.preSelectionValue;
	}


	/**
	 * Sets the background {@link Color} of even rows.
	 *
	 * @param evenBackground
	 *            the background of even rows as {@link Color}.
	 */
	public void setEvenBackground(final Color evenBackground)
	{
		this.evenBackground = evenBackground;
	}


	/**
	 * Returns the background {@link Color} of even rows.
	 *
	 * @return the background of even rows as {@link Color}.
	 */
	public Color getEvenBackground()
	{
		return this.evenBackground;
	}


	/**
	 * Sets the background {@link Color} of odd rows.
	 *
	 * @param oddBackground
	 *            the background of odd rows as {@link Color}.
	 */
	public void setOddBackground(final Color oddBackground)
	{
		this.oddBackground = oddBackground;
	}


	/**
	 * Returns the background {@link Color} of odd rows.
	 *
	 * @return the background of odd rows as {@link Color}.
	 */
	public Color getOddBackground()
	{
		return this.oddBackground;
	}


	/**
	 * Verify if a item is selected.
	 *
	 * @return <code>true</code> if a item is selected, otherwise
	 *         <code>false</code>.
	 */
	public boolean isSomethingSelected()
	{
		return getSelectedItem() != null;
	}


	/**
	 * Returns the selected data.
	 *
	 * @return the selected data, if no data is selected <code>null</code>
	 */
	public Object getSelectedData()
	{
		if(getItemList() != null)
		{
			final Entry entry = getSelectedEntry();
			return entry != null ? entry.getData() : null;
		}
		else
		{
			return getSelectedItem();
		}
	}


	/**
	 * Returns the modelindex of the selected item.
	 * <p>
	 * For example the index of the selected item in it is {@link VirtualTable}.
	 * </p>
	 *
	 * @return an integer specifying the currently selected list item, where 0
	 *         specifies the first item in the list; or -1 if no item is
	 *         selected or if the currently selected item is not in the list
	 */
	public int getSelectedModelIndex()
	{
		final int viewIndex = getSupport().getSelectedModelIndex(super.getSelectedIndex());
		return viewIndex;
	}


	/**
	 * Select the index of the first occurrence of the specified <code>o</code>
	 * in this list. If the {@link ItemList} of this {@link XdevComboBox} does
	 * not contain the <code>o</code>, the selection is canceled.
	 *
	 * @param o
	 *            the {@link Object} to select
	 */
	public void setSelectedData(final Object o)
	{
		getSupport().setSelectedData(o);
	}

	private boolean callSuperSetSelectedItem = false;


	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setSelectedItem(final Object anObject)
	{
		ItemList itemList;
		if(this.callSuperSetSelectedItem || anObject == null || anObject instanceof Entry
				|| (itemList = getItemList()) == null)
		{
			super.setSelectedItem(anObject);
		}
		else
		{
			final int index = itemList.indexOfItem(anObject);
			if(index == -1)
			{
				super.setSelectedItem(null);
			}
			else
			{
				try
				{
					this.callSuperSetSelectedItem = true;
					super.setSelectedIndex(
							getSupport().modelToView(itemList.indexOfItem(anObject)));
				}
				finally
				{
					this.callSuperSetSelectedItem = false;
				}
			}
		}
	}


	/**
	 * Selects the item at modelindex <code>anIndex</code>.
	 * <p>
	 * For example the given index of an item in it is model {@link VirtualTable}
	 * .
	 * </p>
	 *
	 * @param anIndex
	 *            an integer specifying the list item to select, where 0
	 *            specifies the first item in the list and -1 indicates no
	 *            selection
	 */
	public void setSelectedModelIndex(final int anIndex)
	{
		this.getSupport().setSelectedModelIndex(anIndex);
	}


	/**
	 * Returns the selected {@link ItemList}-{@link Entry}.
	 *
	 * @return the selected {@link ItemList}-{@link Entry}
	 */
	public Entry getSelectedEntry()
	{
		return (Entry)getSelectedItem();
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getFormularName()
	{
		return getSupport().getFormularName();
	}


	/**
	 * {@inheritDoc}
	 *
	 * @since 3.1
	 */
	@Override
	public void setDataField(final String dataField)
	{
		getSupport().setDataField(dataField);
	}


	/**
	 * {@inheritDoc}
	 *
	 * @since 3.1
	 */
	@Override
	public String getDataField()
	{
		return getSupport().getDataField();
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	@Deprecated
	public void setFormularValue(final VirtualTable vt, final int col, final Object value)
	{
		getSupport().setFormularValue(vt,col,value);
	}


	/**
	 * {@inheritDoc}
	 *
	 * @since 3.2
	 */
	@Override
	public void setFormularValue(final VirtualTable vt, final Map<String, Object> record)
	{
		getSupport().setFormularValue(vt,record);
	}


	/**
	 * {@inheritDoc}
	 *
	 * @since 3.2
	 */
	@Override
	public void setMasterValue(final VirtualTable vt, final Map<String, Object> record)
	{
		this.support.setValue(vt,record);
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object getFormularValue()
	{
		return getSupport().getFormularValue();
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public void saveState()
	{
		getSupport().saveState();
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public void restoreState() throws IllegalArgumentException
	{
		getSupport().restoreState();
	}


	/**
	 * {@inheritDoc}
	 *
	 * @since 3.1
	 */
	@Override
	public boolean hasStateChanged()
	{
		return getSupport().hasStateChanged();
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isMultiSelect()
	{
		return getSupport().isMultiSelect();
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean verify()
	{
		return getSupport().verify();
	}


	/**
	 * {@inheritDoc}
	 *
	 * @since 3.1
	 */
	@Override
	public void addValidator(final Validator validator)
	{
		getSupport().addValidator(validator);
	}


	/**
	 * {@inheritDoc}
	 *
	 * @since 3.1
	 */
	@Override
	public void removeValidator(final Validator validator)
	{
		getSupport().removeValidator(validator);
	}


	/**
	 * {@inheritDoc}
	 *
	 * @since 3.1
	 */
	@Override
	public Validator[] getValidators()
	{
		return getSupport().getValidators();
	}


	/**
	 * {@inheritDoc}
	 *
	 * @since 3.1
	 */
	@Override
	public void validateState() throws ValidationException
	{
		getSupport().validateState();
	}


	/**
	 * {@inheritDoc}
	 *
	 * @since 3.1
	 */
	@Override
	public void validateState(final Validation validation) throws ValidationException
	{
		getSupport().validateState(validation);
	}


	/**
	 * {@inheritDoc}
	 *
	 * @since 3.1
	 */
	@Override
	public void setFilterOperator(final Operator filterOperator)
	{
		getSupport().setFilterOperator(filterOperator);
	}


	/**
	 * {@inheritDoc}
	 *
	 * @since 3.1
	 */
	@Override
	public Operator getFilterOperator()
	{
		return getSupport().getFilterOperator();
	}


	/**
	 * {@inheritDoc}
	 *
	 * @since 3.2
	 */
	@Override
	public void setReadOnly(final boolean readOnly)
	{
		this.support.setReadOnly(readOnly);
	}


	/**
	 * {@inheritDoc}
	 *
	 * @since 3.2
	 */
	@Override
	public boolean isReadOnly()
	{
		return this.support.isReadOnly();
	}


	/**
	 * {@inheritDoc}
	 *
	 * @since 4.0
	 */
	@Override
	public void setVirtualTable(final VirtualTable vt)
	{
		VirtualTableColumn<?> primaryColumn = vt.getPrimaryColumn();
		if(primaryColumn == null)
		{
			primaryColumn = vt.getColumnAt(0);
		}

		final String itemCol = primaryColumn.getName();
		String dataCol = null;

		final VirtualTableColumn<?>[] primaryKeyColumns = vt.getPrimaryKeyColumns();
		if(primaryKeyColumns != null && primaryKeyColumns.length > 0)
		{
			final String[] names = VirtualTableColumn.getNamesOf(primaryKeyColumns);
			dataCol = StringUtils.concat(",",(Object[])names);
		}

		setModel(vt,itemCol,dataCol,false);
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public VirtualTable getVirtualTable()
	{
		return getSupport().getVirtualTable();
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public VirtualTableRow getSelectedVirtualTableRow()
	{
		return getSupport().getSelectedVirtualTableRow();
	}


	/**
	 * Selects the row in the combo box if present.
	 *
	 * @param row
	 *            the row to select
	 * @since 3.2
	 */
	public void setSelectedVirtualTableRow(final VirtualTableRow row)
	{
		this.support.setSelectedVirtualTableRow(row);
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public void addValueChangeListener(final ValueChangeListener l)
	{
		getSupport().addValueChangeListener(l);
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setDetailHandler(final DetailHandler detailHandler)
	{
		getSupport().setDetailHandler(detailHandler);
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public Dimension getPreferredSize()
	{
		final Dimension d = super.getPreferredSize();

		if(d.width < 50)
		{
			d.width = 150;
		}

		return d;
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString()
	{
		final Object o = getSelectedItem();
		if(o != null)
		{
			return String.valueOf(o);
		}

		return UIUtils.toString(this);
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getTabIndex()
	{
		return this.tabIndex;
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setTabIndex(final int tabIndex)
	{
		if(this.tabIndex != tabIndex)
		{
			final int oldValue = this.tabIndex;
			this.tabIndex = tabIndex;
			firePropertyChange(TAB_INDEX_PROPERTY,oldValue,tabIndex);
		}
	}



	public static class ItemListModelWrapper implements MutableComboBoxModel, ItemListOwner
	{
		private final ItemList	itemList;
		private Object			selectedItem;


		public ItemListModelWrapper(final ItemList itemList)
		{
			this.itemList = itemList;

			if(itemList.size() > 0)
			{
				this.selectedItem = itemList.get(0);
			}
		}


		@Override
		public ItemList getItemList()
		{
			return this.itemList;
		}


		@Override
		public void setSelectedItem(final Object o)
		{
			if(!ObjectUtils.equals(this.selectedItem,o))
			{
				this.selectedItem = o;
				this.itemList.fireContentsChanged(-1,-1);
			}
		}


		@Override
		public Object getSelectedItem()
		{
			return this.selectedItem;
		}


		@Override
		public int getSize()
		{
			return this.itemList.size();
		}


		@Override
		public Object getElementAt(final int index)
		{
			return this.itemList.get(index);
		}


		@Override
		public void addElement(final Object obj)
		{
			this.itemList.add(String.valueOf(obj),obj);
		}


		@Override
		public void insertElementAt(final Object obj, final int index)
		{
			this.itemList.add(index,String.valueOf(obj),obj);
		}


		@Override
		public void removeElement(final Object obj)
		{
			final int index = this.itemList.indexOfItem(obj);
			if(index >= 0)
			{
				this.itemList.remove(index);
			}
		}


		@Override
		public void removeElementAt(final int index)
		{
			this.itemList.remove(index);
		}


		@Override
		public void addListDataListener(final ListDataListener l)
		{
			this.itemList.addListDataListener(l);
		}


		@Override
		public void removeListDataListener(final ListDataListener l)
		{
			this.itemList.removeListDataListener(l);
		}
	}



	protected class XdevComboBoxListCellRenderer extends DefaultListCellRenderer
	{
		@Override
		public Component getListCellRendererComponent(final JList list, Object value,
				final int index, boolean isSelected, final boolean cellHasFocus)
		{
			final boolean selectable = isElementSelectable(value);
			if(isSelected && !selectable)
			{
				isSelected = false;
			}

			Icon icon = null;
			if(index == -1 && getSelectedItem() == null
					&& XdevComboBox.this.preSelectionValue != null)
			{
				value = XdevComboBox.this.preSelectionValue;
			}
			else if(value instanceof Entry)
			{
				final Entry entry = (Entry)value;
				value = entry.getItem();
				icon = entry.getIcon();
			}

			super.getListCellRendererComponent(list,value,index,isSelected,cellHasFocus);

			setIcon(icon);

			if(!isSelected)
			{
				setBackground(index % 2 == 0 ? getEvenBackground() : getOddBackground());
			}

			setEnabled(selectable);

			return this;
		}
	}

	/**
	 * Workaround for a JDK bug.
	 *
	 *
	 * @see <a href="http://issues.xdev-software.de/view.php?id=11041">XDEV
	 *      Issue</a>
	 * @see <a href=
	 *      "http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=4618607">SUN
	 *      Issue</a>
	 */
	private boolean layouting = false;


	/**
	 * {@inheritDoc}
	 */
	@Override
	public void doLayout()
	{
		try
		{
			this.layouting = true;
			super.doLayout();
		}
		finally
		{
			this.layouting = false;
		}
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public Dimension getSize()
	{
		final Dimension d = super.getSize();
		if(!this.layouting)
		{
			d.width = Math.max(d.width,getPopup().getPreferredSize().width);
		}
		return d;
	}


	private JPopupMenu getPopup()
	{
		final ComboBoxUI ui = getUI();
		final int c = ui.getAccessibleChildrenCount(this);
		for(int i = 0; i < c; i++)
		{
			final Accessible a = ui.getAccessibleChild(this,i);
			if(a instanceof JPopupMenu)
			{
				return (JPopupMenu)a;
			}
		}

		return null;
	}
}
