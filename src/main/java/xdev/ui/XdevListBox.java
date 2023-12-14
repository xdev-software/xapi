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
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.Beans;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Collection;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListSelectionModel;
import javax.swing.Icon;
import javax.swing.JCheckBox;
import javax.swing.JList;
import javax.swing.JViewport;
import javax.swing.ListCellRenderer;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import xdev.db.Operator;
import xdev.db.sql.Condition;
import xdev.ui.ItemList.Entry;
import xdev.ui.listbox.ListBoxSupport;
import xdev.ui.paging.ListBoxPageControl;
import xdev.ui.paging.Pageable;
import xdev.util.IntList;
import xdev.util.ObjectUtils;
import xdev.util.StringUtils;
import xdev.util.XdevList;
import xdev.vt.VirtualTable;
import xdev.vt.VirtualTable.VirtualTableRow;
import xdev.vt.VirtualTableColumn;


/**
 * The standard listbox in XDEV. Based on {@link JList}.
 *
 * @see JList
 *
 * @author XDEV Software
 *
 * @since 2.0
 */
@BeanSettings(useXdevCustomizer = true)
public class XdevListBox extends JList implements ExtendedList<XdevListBox>,
		XdevFocusCycleComponent, PopupRowSelectionHandler, Pageable
{
	public final static String					CHECKBOX_LIST_PROPERTY		= "checkBoxList";
	public final static String					EVEN_BACKGROUND_PROPERTY	= "evenBackground";
	public final static String					ODD_BACKGROUND_PROPERTY		= "oddBackground";

	private boolean								checkBoxList				= false;
	private Color								evenBackground				= null;
	private Color								oddBackground				= null;

	/**
	 * tabIndex is used to store the index for {@link XdevFocusCycleComponent}
	 * functionality.
	 */
	private int									tabIndex					= -1;

	// @since 4.0
	private boolean								pagingEnabled				= false;
	private ListBoxPageControl					pageControl					= null;

	private final ListBoxSupport<XdevListBox>	support						= new ListBoxSupport(
			this);


	/**
	 * Constructs a {@link XdevListBox} that is initialized with a default
	 * {@link ListCellRenderer}.
	 *
	 *
	 * @see #setCheckBoxList(boolean)
	 * @see #setItemList(ItemList)
	 */
	public XdevListBox()
	{
		this(false);
	}


	/**
	 * Constructs a {@link XdevListBox} that is initialized with a default
	 * {@link ListCellRenderer}.
	 *
	 *
	 * @param checkable
	 *            if <code>true</code> the {@link XdevListBox} items are
	 *            initialized with a checkbox style
	 *
	 * @see #setItemList(ItemList)
	 * @see #setCheckBoxList(boolean)
	 */
	public XdevListBox(final boolean checkable)
	{
		this(new ItemList(),checkable);
	}


	/**
	 * Constructs a {@link XdevListBox} that is initialized with the
	 * <code>il</code> and a default {@link ListCellRenderer}.
	 *
	 *
	 * @param il
	 *            represents the contents or "value" of this {@link XdevListBox}
	 *
	 * @see #setItemList(ItemList)
	 * @see #setCheckBoxList(boolean)
	 */
	public XdevListBox(final ItemList il)
	{
		this(il,false);
	}


	/**
	 * Constructs a {@link XdevListBox} that is initialized with the
	 * <code>model</code> and a default {@link ListCellRenderer}.
	 *
	 *
	 * @param model
	 *            the {@link ListModel} for this {@link XdevListBox}
	 *
	 * @see #setModel(ListModel)
	 * @see #setCheckBoxList(boolean)
	 */
	public XdevListBox(final ListModel model)
	{
		this(model,false);
	}


	/**
	 * Constructs a {@link XdevListBox} that is initialized with the
	 * <code>il</code> and a default {@link ListCellRenderer}.
	 *
	 *
	 * @param il
	 *            represents the contents or "value" of this {@link XdevListBox}
	 *
	 * @param checkable
	 *            if <code>true</code> the {@link XdevListBox} items are
	 *            initialized with a checkbox style
	 *
	 * @see #setCheckBoxList(boolean)
	 * @see #setItemList(ItemList)
	 */
	public XdevListBox(final ItemList il, final boolean checkable)
	{
		super();
		setSelectionModel(createListSelectionModel());
		setItemList(il);
		setCheckBoxList0(checkable,true);
	}


	/**
	 * Constructs a {@link XdevListBox} that is initialized with the
	 * <code>model</code> and a default {@link ListCellRenderer}.
	 *
	 *
	 * @param model
	 *            the {@link ListModel} for this {@link XdevListBox}
	 *
	 * @param checkable
	 *            if <code>true</code> the {@link XdevListBox} items are
	 *            initialized with a checkbox style
	 *
	 * @see #setCheckBoxList(boolean)
	 * @see #setModel(ListModel)
	 */
	public XdevListBox(final ListModel model, final boolean checkable)
	{
		super();
		setSelectionModel(createListSelectionModel());
		setModel(model);
		setCheckBoxList0(checkable,true);
	}


	protected boolean isElementSelectable(final Object item)
	{
		if(item instanceof Entry)
		{
			return ((Entry)item).isEnabled();
		}

		return true;
	}


	/**
	 * Sets if this list should use checkboxes to render the list entries.
	 *
	 * @param checkBoxList
	 *            <code>true</code> if this list should use checkboxes
	 */
	public void setCheckBoxList(final boolean checkBoxList)
	{
		setCheckBoxList0(checkBoxList,false);
	}


	private void setCheckBoxList0(final boolean checkBoxList, final boolean force)
	{
		if(this.checkBoxList != checkBoxList || force)
		{
			final boolean oldValue = this.checkBoxList;
			this.checkBoxList = checkBoxList;

			UIUtils.removeMouseListener(this,CheckableHandler.class);
			UIUtils.removeMouseMotionListener(this,CheckableHandler.class);

			if(checkBoxList)
			{
				setCellRenderer(createCheckBoxListCellRenderer());

				final CheckableHandler checkableHandler = new CheckableHandler();
				UIUtils.insertMouseListener(this,checkableHandler,0);
				UIUtils.insertMouseMotionListener(this,checkableHandler,0);
			}
			else
			{
				setCellRenderer(createDefaultListCellRenderer());
			}

			firePropertyChange(CHECKBOX_LIST_PROPERTY,oldValue,checkBoxList);
		}
	}


	protected ListSelectionModel createListSelectionModel()
	{
		return new XdevListSelectionModel();
	}


	protected ListCellRenderer createDefaultListCellRenderer()
	{
		return new XdevListCellRenderer();
	}


	protected ListCellRenderer createCheckBoxListCellRenderer()
	{
		return new XdevCheckBoxListCellRenderer();
	}


	/**
	 * Returns <code>true</code> if the <code>items</code> of this
	 * {@link XdevListBox} were drawn with a checkbox style.
	 *
	 * @return <code>true</code> if the <code>items</code> of this
	 *         {@link XdevListBox} were drawn with a checkbox style, otherwise
	 *         <code>false</code>.
	 */
	public boolean isCheckBoxList()
	{
		return this.checkBoxList;
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public ItemList getItemList()
	{
		return this.support.getItemList();
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
	 *
	 * Create a new {@link ItemList} based on the provided <code>model</code>.
	 * The data are the model's values and the item's are the model's values as
	 * {@link String}.
	 *
	 * @param model
	 *            The {@link ListModel}
	 *
	 * @return the new {@link ItemList}
	 */
	public ItemList modelToItemList(final ListModel model)
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
		clearSelection();
	}


	/**
	 * {@inheritDoc}
	 *
	 * @since 4.0
	 */
	@Override
	public void setPagingEnabled(final boolean pagingEnabled)
	{
		this.pagingEnabled = pagingEnabled;

		if(!Beans.isDesignTime())
		{
			if(pagingEnabled)
			{
				getItemList().setPageControl(getPageControl());
			}
			else
			{
				getItemList().setPageControl(null);
			}

			refresh();
		}
	}


	/**
	 * {@inheritDoc}
	 *
	 * @since 4.0
	 */
	@Override
	public boolean isPagingEnabled()
	{
		return this.pagingEnabled;
	}


	/**
	 * {@inheritDoc}
	 *
	 * @since 4.0
	 */
	@Override
	public ListBoxPageControl getPageControl()
	{
		if(this.pageControl == null)
		{
			this.pageControl = new ListBoxPageControl(this);
		}

		return this.pageControl;
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public void refresh()
	{
		this.support.refresh();
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public void updateModel(final Condition condition, final Object... params)
	{
		this.support.updateModel(condition,params);
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public void clearModel()
	{
		this.support.clearModel();
	}


	/**
	 * Sets the background {@link Color} of even rows.
	 *
	 * @param evenBackground
	 *            The background of even rows as type {@link Color}.
	 */
	@BeanProperty(category = DefaultBeanCategories.DESIGN)
	public void setEvenBackground(final Color evenBackground)
	{
		if(!ObjectUtils.equals(this.evenBackground,evenBackground))
		{
			final Object oldValue = this.evenBackground;
			this.evenBackground = evenBackground;
			firePropertyChange(EVEN_BACKGROUND_PROPERTY,oldValue,evenBackground);
		}
	}


	/**
	 * Returns the background {@link Color} of even rows.
	 *
	 * @return The background of even rows as type {@link Color}.
	 */
	public Color getEvenBackground()
	{
		return this.evenBackground;
	}


	/**
	 * Sets the background {@link Color} of odd rows.
	 *
	 * @param oddBackground
	 *            The background of odd rows as type {@link Color}.
	 */
	@BeanProperty(category = DefaultBeanCategories.DESIGN)
	public void setOddBackground(final Color oddBackground)
	{
		if(!ObjectUtils.equals(this.oddBackground,oddBackground))
		{
			final Object oldValue = this.oddBackground;
			this.oddBackground = oddBackground;
			firePropertyChange(ODD_BACKGROUND_PROPERTY,oldValue,oddBackground);
		}
	}


	/**
	 * Returns the background {@link Color} of odd rows.
	 *
	 * @return The background of odd rows as type {@link Color}.
	 */
	public Color getOddBackground()
	{
		return this.oddBackground;
	}


	/**
	 * Select all items in the {@link XdevListBox}.
	 *
	 * @see ListSelectionModel#clearSelection()
	 * @see ListSelectionModel#setSelectionInterval(int, int)
	 */
	public void selectAll()
	{
		final int size = getModel().getSize();
		if(size > 0)
		{
			getSelectionModel().clearSelection();
			getSelectionModel().setSelectionInterval(0,size);
		}
	}


	/**
	 * Verify if a item is selected.
	 *
	 * @return <code>true</code> if a item is selected, otherwise
	 *         <code>false</code>.
	 */
	public boolean isSomethingSelected()
	{
		return getSelectedIndex() >= 0;
	}


	/**
	 * Changes the selection to be the set of indices specified by the given
	 * {@link XdevList}. Indices greater than or equal to the model size are
	 * ignored. This is a convenience method that clears the selection and then
	 * uses {@code addSelectionInterval} on the selection model to add the
	 * indices. Refer to the documentation of the selection model class being
	 * used for details on how values less than {@code 0} are handled.
	 *
	 * @param indices
	 *            an {@link XdevList} of the indices of the cells to select,
	 *            {@code non-null}
	 *
	 * @see #setSelectedIndices(int[])
	 */
	public void setSelectedIndices(final Collection<Integer> indices)
	{
		final IntList intList = new IntList();
		for(final Integer index : indices)
		{
			if(index != null)
			{
				intList.add(index);
			}
		}
		setSelectedIndices(intList.toArray());
	}


	/**
	 * Returns a {@link XdevList} of all of the selected indices, in increasing
	 * order.
	 *
	 * @return all of the selected indices, in increasing order, or an empty
	 *         {@link XdevList} if nothing is selected
	 *
	 */
	public XdevList<Integer> getSelectedIndicesAsList()
	{
		final XdevList<Integer> list = new XdevList<Integer>();

		final int[] si = getSelectedIndices();
		for(int i = 0; i < si.length; i++)
		{
			list.add(si[i]);
		}

		return list;
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
		return this.support.getSelectedModelIndex(super.getSelectedIndex());
	}


	/**
	 * Returns the modelindices of the selected items.
	 * <p>
	 * For example the indices of the selected items in their model
	 * {@link VirtualTable}.
	 * </p>
	 *
	 * @return an integer specifying the currently selected list item, where 0
	 *         specifies the first item in the list; or -1 if no item is
	 *         selected or if the currently selected item is not in the list
	 */
	public int[] getSelectedModelIndices()
	{
		return this.support.getSelectedModelIndices(super.getSelectedIndices());
	}


	/**
	 * Selects the items at their modelindices position <code>indices</code>.
	 * <p>
	 * For example the given indices of the items in their model
	 * {@link VirtualTable} .
	 * </p>
	 *
	 * @param indices
	 *            an integer specifying the list item to select, where 0
	 *            specifies the first item in the list and -1 indicates no
	 *            selection.
	 */
	public void setSelectedModelIndices(final int[] indices)
	{
		this.support.setSelectedModelIndices(indices);
	}


	/**
	 * Selects the item at modelindex <code>anIndex</code>.
	 * <p>
	 * For example the given index of an item in it is model {@link VirtualTable}
	 * .
	 * </p>
	 *
	 * @param index
	 *            an integer specifying the list item to select, where 0
	 *            specifies the first item in the list and -1 indicates no
	 *            selection
	 */
	public void setSelectedModelIndex(final int index)
	{
		this.support.setSelectedModelIndex(index);
	}


	/**
	 * Selects all entries with an item in the list <code>items</code>.
	 *
	 * @param items
	 *            the items to select
	 *
	 * @since 3.1
	 */
	public void setSelectedItems(final Collection<?> items)
	{
		this.support.setSelectedItems(items);
	}


	/**
	 * Returns the item for the selected value from {@link ItemList}.
	 *
	 * @return the item for the selected value from {@link ItemList}
	 */
	public Object getSelectedItem()
	{
		return getSelectedValue();
	}


	/**
	 * Returns all items of the selected list entries as a list.
	 * <p>
	 * If nothing is selected an empty list is returned.
	 * </p>
	 *
	 * @return all selected items or an empty list if nothing is selected.
	 *
	 * @since 3.1
	 */
	public XdevList<Object> getSelectedItemsAsList()
	{
		return this.support.getSelectedItemsAsList();
	}


	/**
	 * Selects the specified item. The {@link XdevListBox} scrolls to the
	 * selected item.
	 *
	 * @param o
	 *            the item to select
	 *
	 * @see #setSelectedValue(Object, boolean)
	 */
	public void setSelectedItem(Object o)
	{
		// impl/fix for #13435
		if(o != null && !(o instanceof Entry))
		{
			final ItemList itemList = getItemList();
			if(itemList != null)
			{
				final int i = itemList.indexOfItem(o);
				if(i != -1)
				{
					o = itemList.get(i);
				}
			}
		}

		setSelectedValue(o,true);
	}


	/**
	 * Returns the data for the selected value from {@link ItemList}.
	 *
	 * @return the data for the selected value from {@link ItemList}
	 */
	public Object getSelectedData()
	{
		final Entry entry = (Entry)getSelectedItem();
		return entry != null ? entry.getData() : null;
	}


	/**
	 * Returns all data of the selected list entries as a list.
	 * <p>
	 * If nothing is selected an empty list is returned.
	 * </p>
	 *
	 * @return all selected data or an empty list if nothing is selected.
	 *
	 * @since 3.1
	 */
	public XdevList<Object> getSelectedDataAsList()
	{
		return this.support.getSelectedDataAsList();
	}


	/**
	 * Selects the specified data from the {@link XdevList}. If <code>o</code>
	 * doesn't exist in the {@link ItemList} the selection is cleared.
	 *
	 * @param o
	 *            the data to select
	 *
	 * @see #setSelectedIndex(int)
	 */
	public void setSelectedData(final Object o)
	{
		this.support.setSelectedData(o);
	}


	/**
	 * Selects all entries with a data in the list <code>dataList</code>.
	 *
	 * @param dataList
	 *            the data to select
	 *
	 * @since 3.1
	 */
	public void setSelectedData(final Collection<?> dataList)
	{
		this.support.setSelectedData(dataList);
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public VirtualTableRow getSelectedVirtualTableRow()
	{
		return this.support.getSelectedVirtualTableRow();
	}


	/**
	 * Selects the row in the list if present.
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
	 * Returns the selected {@link VirtualTableRow}s of this component, or an
	 * empty array if nothing is selected.
	 *
	 * @return the selected {@link VirtualTableRow}s of this component
	 * @since 3.2
	 */
	public VirtualTableRow[] getSelectedVirtualTableRows()
	{
		return this.support.getSelectedVirtualTableRows();
	}


	/**
	 * Selects the rows in the list if present.
	 *
	 * @param rows
	 *            the rows to select
	 * @since 3.2
	 */
	public void setSelectedVirtualTableRows(final VirtualTableRow[] rows)
	{
		this.support.setSelectedVirtualTableRows(rows);
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getFormularName()
	{
		return this.support.getFormularName();
	}


	/**
	 * {@inheritDoc}
	 *
	 * @since 3.1
	 */
	@Override
	@BeanProperty(category = DefaultBeanCategories.DATA)
	public void setDataField(final String dataField)
	{
		this.support.setDataField(dataField);
	}


	/**
	 * {@inheritDoc}
	 *
	 * @since 3.1
	 */
	@Override
	public String getDataField()
	{
		return this.support.getDataField();
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	@Deprecated
	public void setFormularValue(final VirtualTable vtRow, final int col, final Object value)
	{
		this.support.setFormularValue(vtRow,col,value);
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setFormularValue(final VirtualTable vt, final Map<String, Object> record)
	{
		this.support.setFormularValue(vt,record);
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
		return this.support.getFormularValue();
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public void saveState()
	{
		this.support.saveState();
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public void restoreState()
	{
		this.support.restoreState();
	}


	/**
	 * {@inheritDoc}
	 *
	 * @since 3.1
	 */
	@Override
	public boolean hasStateChanged()
	{
		return this.support.hasStateChanged();
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isMultiSelect()
	{
		return this.support.isMultiSelect();
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean verify()
	{
		return this.support.verify();
	}


	/**
	 * {@inheritDoc}
	 *
	 * @since 3.1
	 */
	@Override
	public void addValidator(final Validator validator)
	{
		this.support.addValidator(validator);
	}


	/**
	 * {@inheritDoc}
	 *
	 * @since 3.1
	 */
	@Override
	public void removeValidator(final Validator validator)
	{
		this.support.removeValidator(validator);
	}


	/**
	 * {@inheritDoc}
	 *
	 * @since 3.1
	 */
	@Override
	public Validator[] getValidators()
	{
		return this.support.getValidators();
	}


	/**
	 * {@inheritDoc}
	 *
	 * @since 3.1
	 */
	@Override
	public void validateState() throws ValidationException
	{
		this.support.validateState();
	}


	/**
	 * {@inheritDoc}
	 *
	 * @since 3.1
	 */
	@Override
	public void validateState(final Validation validation) throws ValidationException
	{
		this.support.validateState(validation);
	}


	/**
	 * {@inheritDoc}
	 *
	 * @since 3.1
	 */
	@Override
	public void setFilterOperator(final Operator filterOperator)
	{
		this.support.setFilterOperator(filterOperator);
	}


	/**
	 * {@inheritDoc}
	 *
	 * @since 3.1
	 */
	@Override
	public Operator getFilterOperator()
	{
		return this.support.getFilterOperator();
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
		return this.support.getVirtualTable();
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public void addValueChangeListener(final ValueChangeListener l)
	{
		this.support.addValueChangeListener(l);
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setDetailHandler(final DetailHandler detailHandler)
	{
		this.support.setDetailHandler(detailHandler);
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public Dimension getPreferredSize()
	{
		final Dimension d = super.getPreferredSize();

		if(getModel().getSize() == 0 && !isPreferredSizeSet() && !isInNullLayout())
		{
			d.width = 150;
		}

		return d;
	}


	private boolean isInNullLayout()
	{
		Container parent = getParent();
		if(parent instanceof JViewport)
		{
			parent = parent.getParent().getParent();
		}
		return parent != null && parent.getLayout() == null;
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public Dimension getPreferredScrollableViewportSize()
	{
		if(getLayoutOrientation() == JList.VERTICAL
				&& (getFixedCellWidth() <= 0 || getFixedCellHeight() <= 0)
				&& getModel().getSize() == 0)
		{
			return new Dimension(150,16 * getVisibleRowCount());
		}

		return super.getPreferredScrollableViewportSize();
	}


	/**
	 * This method is a alias for {@link #getRowAtPoint(Point)}.
	 *
	 * @param x
	 *            the X coordinate of the row
	 * @param y
	 *            the Y coordinate of the row
	 *
	 * @return the row index closest to the given location, or {@code -1}
	 *
	 *
	 * @see #locationToIndex(Point)
	 */
	public int getRowAtPoint(final int x, final int y)
	{
		return getRowAtPoint(new Point(x,y));
	}


	/**
	 * Returns the row index closest to the given location in the list's
	 * coordinate system. To determine if the row actually contains the
	 * specified location, compare the point against the cell's bounds, as
	 * provided by {@code getCellBounds}. This method returns {@code -1} if the
	 * model is empty
	 * <p>
	 * This is a cover method that delegates to the method of the same name in
	 * the list's {@code ListUI}. It returns {@code -1} if the list has no
	 * {@code ListUI}.
	 *
	 * @param location
	 *            the coordinates of the point
	 *
	 * @return the row index closest to the given location, or {@code -1}
	 *
	 * @see #locationToIndex(Point)
	 */
	public int getRowAtPoint(final Point location)
	{
		return locationToIndex(location);
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	@BeanProperty(intMin = 0)
	public void setVisibleRowCount(final int visibleRowCount)
	{
		super.setVisibleRowCount(visibleRowCount);
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString()
	{
		final String toString = UIUtils.toString(this);
		if(toString != null)
		{
			return toString;
		}

		return super.toString();
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


	/*
	 * Overridden because some extensions crash if this method returns null or
	 * an empty rectangle
	 */
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Rectangle getCellBounds(final int index0, final int index1)
	{
		Rectangle r = super.getCellBounds(index0,index1);
		if(r == null)
		{
			r = new Rectangle(0,0,1,1);
		}
		return r;
	}



	public static class ItemListModelWrapper implements ListModel, ItemListOwner
	{
		private final ItemList itemList;


		public ItemListModelWrapper(final ItemList itemList)
		{
			this.itemList = itemList;
		}


		@Override
		public ItemList getItemList()
		{
			return this.itemList;
		}


		/**
		 * {@inheritDoc}
		 */
		@Override
		public int getSize()
		{
			return this.itemList.size();
		}


		/**
		 * {@inheritDoc}
		 */
		@Override
		public Object getElementAt(final int index)
		{
			return this.itemList.get(index);
		}


		/**
		 * {@inheritDoc}
		 */
		@Override
		public void addListDataListener(final ListDataListener l)
		{
			this.itemList.addListDataListener(l);
		}


		/**
		 * {@inheritDoc}
		 */
		@Override
		public void removeListDataListener(final ListDataListener l)
		{
			this.itemList.removeListDataListener(l);
		}
	}



	protected class XdevListSelectionModel extends DefaultListSelectionModel
	{
		public XdevListSelectionModel()
		{
			final ListDataListener listDataListener = new ListDataListener()
			{
				/**
				 * {@inheritDoc}
				 */
				@Override
				public void contentsChanged(final ListDataEvent e)
				{
					listDataChanged();
				}


				/**
				 * {@inheritDoc}
				 */
				@Override
				public void intervalAdded(final ListDataEvent e)
				{
					listDataChanged();
				}


				/**
				 * {@inheritDoc}
				 */
				@Override
				public void intervalRemoved(final ListDataEvent e)
				{
					listDataChanged();
				}


				void listDataChanged()
				{
					try
					{
						setValueIsAdjusting(true);
						checkSelection();
					}
					finally
					{
						setValueIsAdjusting(false);
					}
				}
			};

			final ListModel model = getModel();
			if(model != null)
			{
				model.addListDataListener(listDataListener);
			}

			addPropertyChangeListener("model",new PropertyChangeListener()
			{
				@Override
				public void propertyChange(final PropertyChangeEvent evt)
				{
					Object o = evt.getOldValue();
					if(o instanceof ListModel)
					{
						((ListModel)o).removeListDataListener(listDataListener);
					}
					o = evt.getNewValue();
					if(o instanceof ListModel)
					{
						((ListModel)o).addListDataListener(listDataListener);
					}
				}
			});

			addListSelectionListener(new ListSelectionListener()
			{
				@Override
				public void valueChanged(final ListSelectionEvent e)
				{
					checkSelection();
				}
			});
		}


		protected void checkSelection()
		{
			final int[] si = getSelectedIndices();
			for(int i = si.length; --i >= 0;)
			{
				final int index = si[i];
				try
				{
					if(!isElementSelectable(getModel().getElementAt(index)))
					{
						removeSelectionInterval(index,index);
					}
				}
				catch(final IndexOutOfBoundsException e)
				{
					// Proble Bug in JList, getSelectedIndices() returns too
					// much values
				}
			}
		}
	}



	protected class XdevListCellRenderer extends DefaultListCellRenderer
	{
		/**
		 * {@inheritDoc}
		 */
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
			if(value instanceof Entry)
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
			setOpaque(list.isOpaque());
			setEnabled(list.isEnabled() && selectable);

			return this;
		}
	}



	protected class XdevCheckBoxListCellRenderer extends JCheckBox implements ListCellRenderer
	{
		public XdevCheckBoxListCellRenderer()
		{
			super();
			setBorder(BorderFactory.createEmptyBorder(1,1,1,1));
			setOpaque(true);
		}


		@Override
		public Component getListCellRendererComponent(final JList list, final Object value,
				final int index, boolean isSelected, final boolean cellHasFocus)
		{
			final boolean selectable = isElementSelectable(value);
			if(isSelected && !selectable)
			{
				isSelected = false;
			}

			String text;
			Icon icon = null;
			if(value instanceof Entry)
			{
				final Entry entry = (Entry)value;
				text = entry.toString();
				icon = entry.getIcon();
			}
			else if(value instanceof Icon)
			{
				text = "";
				icon = (Icon)value;
			}
			else
			{
				text = value == null ? "" : value.toString();
			}

			setText(text);
			setIcon(icon);

			setComponentOrientation(list.getComponentOrientation());
			setBackground(index % 2 == 0 ? getEvenBackground() : getOddBackground());
			setForeground(list.getForeground());
			setSelected(isSelected);
			setEnabled(list.isEnabled() && selectable);
			setFont(list.getFont());
			setOpaque(list.isOpaque());

			return this;
		}


		/**
		 * {@inheritDoc}
		 */
		@Override
		public boolean isOpaque()
		{
			final Color back = getBackground();
			Component p = getParent();
			if(p != null)
			{
				p = p.getParent();
			}

			final boolean colorMatch = (back != null) && (p != null)
					&& back.equals(p.getBackground()) && p.isOpaque();
			return !colorMatch && super.isOpaque();
		}


		/**
		 * Overridden for performance reasons.
		 */
		@Override
		public void validate()
		{
		}


		/**
		 * Overridden for performance reasons.
		 */
		@Override
		public void invalidate()
		{
		}


		/**
		 * Overridden for performance reasons.
		 */
		@Override
		public void repaint()
		{
		}


		/**
		 * Overridden for performance reasons.
		 */
		@Override
		public void revalidate()
		{
		}


		/**
		 * Overridden for performance reasons.
		 */
		@Override
		public void repaint(final long tm, final int x, final int y, final int width,
				final int height)
		{
		}


		/**
		 * Overridden for performance reasons.
		 */
		@Override
		public void repaint(final Rectangle r)
		{
		}


		/**
		 * Overridden for performance reasons.
		 */
		@Override
		protected void firePropertyChange(final String propertyName, final Object oldValue,
				final Object newValue)
		{
			if("text".equals(propertyName)
					|| (("font".equals(propertyName) || "foreground".equals(propertyName))
							&& oldValue != newValue && getClientProperty(
									javax.swing.plaf.basic.BasicHTML.propertyKey) != null))
			{
				super.firePropertyChange(propertyName,oldValue,newValue);
			}
		}


		/**
		 * Overridden for performance reasons.
		 */
		@Override
		public void firePropertyChange(final String propertyName, final byte oldValue,
				final byte newValue)
		{
		}


		/**
		 * Overridden for performance reasons.
		 */
		@Override
		public void firePropertyChange(final String propertyName, final char oldValue,
				final char newValue)
		{
		}


		/**
		 * Overridden for performance reasons.
		 */
		@Override
		public void firePropertyChange(final String propertyName, final short oldValue,
				final short newValue)
		{
		}


		/**
		 * Overridden for performance reasons.
		 */
		@Override
		public void firePropertyChange(final String propertyName, final int oldValue,
				final int newValue)
		{
		}


		/**
		 * Overridden for performance reasons.
		 */
		@Override
		public void firePropertyChange(final String propertyName, final long oldValue,
				final long newValue)
		{
		}


		/**
		 * Overridden for performance reasons.
		 */
		@Override
		public void firePropertyChange(final String propertyName, final float oldValue,
				final float newValue)
		{
		}


		/**
		 * Overridden for performance reasons.
		 */
		@Override
		public void firePropertyChange(final String propertyName, final double oldValue,
				final double newValue)
		{
		}


		/**
		 * Overridden for performance reasons.
		 */
		@Override
		public void firePropertyChange(final String propertyName, final boolean oldValue,
				final boolean newValue)
		{
		}
	}



	private class CheckableHandler extends MouseAdapter
	{
		@Override
		public void mousePressed(final MouseEvent e)
		{
			if(SwingUtilities.isLeftMouseButton(e) && XdevListBox.this.isEnabled())
			{
				final int index = locationToIndex(e.getPoint());
				if(index >= 0)
				{
					final int selectedIndex = getSelectedIndex();

					if(e.isShiftDown() && selectedIndex >= 0)
					{
						if(isSelectedIndex(index))
						{
							removeSelectionInterval(Math.min(index,selectedIndex),
									Math.max(index,selectedIndex));

						}
						else
						{
							addSelectionInterval(Math.min(index,selectedIndex),
									Math.max(index,selectedIndex));
						}
					}
					else
					{
						if(isSelectedIndex(index))
						{
							removeSelectionInterval(index,index);
						}
						else
						{
							addSelectionInterval(index,index);
						}
					}

					XdevListBox.this.requestFocus();

					e.consume();
				}
			}

		}


		@Override
		public void mouseDragged(final MouseEvent e)
		{
			e.consume();
		}
	}


	/**
	 * {@inheritDoc}
	 *
	 * @since 3.2
	 */
	@Override
	public void handlePopupRowSelection(final int x, final int y)
	{
		final int row = this.getRowAtPoint(x,y);
		if(!this.getSelectionModel().isSelectedIndex(row) && row >= 0)
		{
			this.support.getRowSelectionHandler().selectRow(row);
		}
	}
}
