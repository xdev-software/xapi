package xdev.ui;

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


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import javax.swing.Icon;
import javax.swing.ListModel;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

import xdev.db.QueryInfo;
import xdev.db.sql.Condition;
import xdev.db.sql.SELECT;
import xdev.db.sql.WHERE;
import xdev.lang.Copyable;
import xdev.lang.NotNull;
import xdev.ui.paging.ItemListPageControl;
import xdev.util.ArrayUtils;
import xdev.util.IntList;
import xdev.util.MathUtils;
import xdev.util.ObjectUtils;
import xdev.util.StringUtils;
import xdev.util.logging.LoggerFactory;
import xdev.util.logging.XdevLogger;
import xdev.vt.VirtualTable;
import xdev.vt.VirtualTableColumn;


/**
 *
 * The {@link ItemList} is the backend for some {@link ListModel}s in XDEV.
 *
 * <p>
 * The {@link ItemList} consists of entries ({@link Entry}) which provide two
 * stores: <code>item</code> and <code>data</code>. The <code>item</code> is
 * used to display the list entry in a GUI component. The <code>data</code>
 * object is not displayed and used as a hidden store for data like <i>ids</i>
 * or <i>references</i>.
 * </p>
 *
 * @see XdevListBox
 * @see XdevComboBox
 *
 * @see XdevListBox#setItemList(ItemList)
 * @see XdevComboBox#setItemList(ItemList)
 *
 * @author XDEV Software
 *
 * @since 2.0
 *
 */
public class ItemList implements Copyable<ItemList>
{
	
	/**
	 * Logger instance for this class.
	 */
	private static final XdevLogger log = LoggerFactory.getLogger(ItemList.class);
	
	
	
	/**
	 * An entry in an {@link ItemList} holding
	 * <ul>
	 * <li>a data object</li>
	 * <li>a display item</li>
	 * <li>an icon</li>
	 * <li>settings: enabled</li>
	 * <li>and client properties</li>
	 * </ul>
	 *
	 * @author XDEV Software
	 *
	 */
	public static class Entry implements Copyable<Entry>
	{
		private Object				item;
		private Object				data;
		private Icon				icon;
		private boolean				enabled	= true;
		private Map<Object, Object>	clientProperties;
		
		
		/**
		 * Creates a new entry with the specified <code>data</code> object, the
		 * display item is the String value of <code>data</code>.
		 *
		 * @param data
		 *            the data object
		 */
		public Entry(final Object data)
		{
			this(String.valueOf(data),data);
		}
		
		
		/**
		 * Creates a new entry with the specified <code>data</code> object and
		 * display <code>item</code>.
		 *
		 * @param item
		 *            the display item
		 * @param data
		 *            the data object
		 */
		public Entry(final Object item, final Object data)
		{
			this(item,data,null);
		}
		
		
		/**
		 * Creates a new entry with the specified <code>data</code> object, a
		 * display <code>item</code> and an <code>icon</code>.
		 *
		 * @param item
		 *            the display item
		 * @param data
		 *            the data object
		 * @param icon
		 *            the display icon
		 */
		public Entry(final Object item, final Object data, final Icon icon)
		{
			this.item = item;
			this.data = data;
			this.icon = icon;
		}
		
		
		/**
		 * Returns the diplay item of this entry.
		 *
		 * @return the display item
		 */
		public Object getItem()
		{
			return this.item;
		}
		
		
		/**
		 * Sets a new display item for this entry.
		 *
		 * @param item
		 *            the new display item
		 */
		public void setItem(final Object item)
		{
			this.item = item;
		}
		
		
		/**
		 * Returns the data object of this entry.
		 *
		 * @return the data object
		 */
		public Object getData()
		{
			return this.data;
		}
		
		
		/**
		 * Sets a new data object for this entry.
		 *
		 * @param data
		 *            the new data object
		 */
		public void setData(final Object data)
		{
			this.data = data;
		}
		
		
		/**
		 * Returns the optional icon of this entry.
		 *
		 * @return the icon (may be <code>null</code>)
		 */
		public Icon getIcon()
		{
			return this.icon;
		}
		
		
		/**
		 * Sets the display icon of this entry.
		 *
		 * @param icon
		 *            the new diplay icon
		 */
		public void setIcon(final Icon icon)
		{
			this.icon = icon;
		}
		
		
		/**
		 * Determines whether this entry is enabled.
		 *
		 * @return <code>true</code> if the entry is enabled, <code>false</code>
		 *         otherwise
		 */
		public boolean isEnabled()
		{
			return this.enabled;
		}
		
		
		/**
		 * Enables or disables this entry, depending on the value of the
		 * parameter <code>enabled</code>.
		 * <p>
		 * A disabled entry is renderered disabled and cannot be selected.
		 *
		 * @param enabled
		 *            If <code>true</code>, this entry is enabled; otherwise
		 *            this entry is disabled
		 */
		public void setEnabled(final boolean enabled)
		{
			this.enabled = enabled;
		}
		
		
		/**
		 * Adds an arbitrary key/value "client property" to this entry.
		 * <p>
		 * The <code>get/putClientProperty</code> methods provide access to a
		 * small per-instance hashtable. Callers can use get/putClientProperty
		 * to annotate entries.
		 * <p>
		 * If value is <code>null</code> this method will remove the property.
		 *
		 * @param key
		 *            the new client property key
		 * @param value
		 *            the new client property value; if <code>null</code> this
		 *            method will remove the property
		 * @see #getClientProperty
		 */
		public void putClientProperty(final Object key, final Object value)
		{
			if(value == null && this.clientProperties == null)
			{
				return;
			}
			
			if(this.clientProperties == null)
			{
				this.clientProperties = new HashMap();
			}
			
			if(value == null)
			{
				this.clientProperties.remove(key);
			}
			else
			{
				this.clientProperties.put(key,value);
			}
		}
		
		
		/**
		 * Returns the value of the property with the specified key. Only
		 * properties added with <code>putClientProperty</code> will return a
		 * non-<code>null</code> value.
		 *
		 * @param key
		 *            the being queried
		 * @return the value of this property or <code>null</code>
		 * @see #putClientProperty
		 */
		public Object getClientProperty(final Object key)
		{
			if(this.clientProperties == null)
			{
				return null;
			}
			else
			{
				return this.clientProperties.get(key);
			}
		}
		
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		public String toString()
		{
			return String.valueOf(this.item);
		}
		
		
		/**
		 * Compares <code>obj</code> with this entry.
		 * <p>
		 * Two entries are considered equal if their item and data objects are
		 * equal.
		 *
		 * @param obj
		 *            the <code>Object</code> to compare
		 *
		 * @return <code>true</code> if equal, otherwise <code>false</code>
		 *
		 * @see #getData()
		 */
		@Override
		public boolean equals(final Object obj)
		{
			if(obj == this)
			{
				return true;
			}
			
			if(obj instanceof Entry)
			{
				final Entry other = (Entry)obj;
				return ObjectUtils.equals(this.item,other.item)
						&& ObjectUtils.equals(this.data,other.data);
			}
			
			return false;
		}
		
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		public int hashCode()
		{
			return MathUtils.computeHash(this.item,this.data);
		}
		
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		public Entry clone()
		{
			final Entry entry = new Entry(this.item,this.data,this.icon);
			if(this.clientProperties != null)
			{
				entry.clientProperties = new HashMap(this.clientProperties);
			}
			entry.enabled = this.enabled;
			return entry;
		}
	}
	
	private final List<ListDataListener>	listeners	= new Vector();
	
	private final List<Entry>				entries		= new ArrayList();
	
	private VirtualTable					virtualTable;
	private String							itemCol, dataCol;
	
	/**
	 * @since 4.0
	 */
	private ItemListPageControl<?>			pageControl;
	
	
	/**
	 * Creates a new empty {@link ItemList}.
	 */
	public ItemList()
	{
	}
	
	
	/**
	 * Creates a new {@link ItemList} based on the provided {@link List}s
	 * <code>item</code> and <code>data</code>. <code>Item</code> and
	 * <code>data</code> must be non null and have an equal size. The first
	 * object in <code>item</code> is mapped to first object in
	 * <code>data</code> ... and so on.
	 *
	 * @param item
	 *            {@link List} to use as items
	 * @param data
	 *            {@link List} to use as data
	 */
	public ItemList(@NotNull final Collection<?> item, @NotNull final Collection<?> data)
	{
		if(item == null || data == null || item.size() != data.size())
		{
			throw new IllegalArgumentException(
					"item and data must be non null and have an equal size");
		}
		
		addAll(item,data);
	}
	
	
	/**
	 * Creates a new {@link ItemList} based on the provided arrays
	 * <code>item</code> and <code>data</code>. <code>Item</code> and
	 * <code>data</code> must be non null and have an equal size. The first
	 * object in <code>item</code> is mapped to first object in
	 * <code>data</code> ... and so on.
	 *
	 * @param item
	 *            array to use as items
	 * @param data
	 *            array to use as data
	 */
	public ItemList(@NotNull final Object[] item, @NotNull final Object[] data)
	{
		if(item == null || data == null || item.length != data.length)
		{
			throw new IllegalArgumentException(
					"item and data must be non null and have an equal size");
		}
		
		final int c = item.length;
		for(int i = 0; i < c; i++)
		{
			this.entries.add(new Entry(String.valueOf(item[i]),data[i]));
		}
	}
	
	
	/**
	 * Create a new {@link ItemList} based on the provided {@link ListModel}.
	 * The data are the model's values and the item's are the model's values as
	 * {@link String}.
	 *
	 * @param model
	 *            The data model
	 */
	public ItemList(@NotNull final ListModel model)
	{
		addAll(model);
	}
	
	
	public ItemList(@NotNull final Collection<? extends Entry> entries)
	{
		addAll(entries);
	}
	
	
	/**
	 * Adds a {@link ListDataListener} to this {@link ItemList}.
	 *
	 * @param listener
	 *            the {@link ListDataListener} to add
	 */
	public void addListDataListener(final ListDataListener listener)
	{
		this.listeners.add(listener);
	}
	
	
	/**
	 * Removes the {@link ListDataListener} from this {@link ItemList}.
	 *
	 * @param listener
	 *            the {@link ListDataListener} to remove
	 */
	public void removeListDataListener(final ListDataListener listener)
	{
		this.listeners.remove(listener);
	}
	
	
	protected void fireContentsChanged(final int index0, final int index1)
	{
		if(this.listeners.size() > 0)
		{
			final ListDataEvent event = new ListDataEvent(this,ListDataEvent.CONTENTS_CHANGED,
					index0,index1);
			for(final ListDataListener listener : this.listeners
					.toArray(new ListDataListener[this.listeners.size()]))
			{
				listener.contentsChanged(event);
			}
		}
	}
	
	
	protected void fireIntervalAdded(final int index0, final int index1)
	{
		if(this.listeners.size() > 0)
		{
			final ListDataEvent event = new ListDataEvent(this,ListDataEvent.INTERVAL_ADDED,index0,
					index1);
			for(final ListDataListener listener : this.listeners
					.toArray(new ListDataListener[this.listeners.size()]))
			{
				listener.intervalAdded(event);
			}
		}
	}
	
	
	protected void fireIntervalRemoved(final int index0, final int index1)
	{
		if(this.listeners.size() > 0)
		{
			final ListDataEvent event = new ListDataEvent(this,ListDataEvent.INTERVAL_REMOVED,
					index0,index1);
			for(final ListDataListener listener : this.listeners
					.toArray(new ListDataListener[this.listeners.size()]))
			{
				listener.intervalRemoved(event);
			}
		}
	}
	
	
	/**
	 * Creates a (defensive) copy of this {@link ItemList}.
	 *
	 * @return a (defensive) copy of this {@link ItemList}.
	 */
	public ItemList copy()
	{
		return new ItemList(this.entries);
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public ItemList clone()
	{
		return this.copy();
	}
	
	
	/**
	 * Adds a new row / item data pair to the end of this {@link ItemList}.
	 *
	 * @param item
	 *            to add
	 * @param data
	 *            to add
	 */
	public void add(final String item, final Object data)
	{
		add(size(),item,data);
	}
	
	
	/**
	 * Adds a new row / item data pair at the specified <code>index</code> to
	 * this {@link ItemList}.
	 *
	 * @param index
	 *            index at which the specified element is to be inserted
	 * @param item
	 *            the display item to add
	 * @param data
	 *            the data object to add
	 */
	public void add(final int index, final String item, final Object data)
	{
		add(index,new Entry(item,data));
	}
	
	
	/**
	 * Adds the <code>entry</code> to the end of this {@link ItemList}.
	 *
	 * @param entry
	 *            the entry to add
	 */
	public void add(final Entry entry)
	{
		add(size(),entry);
	}
	
	
	/**
	 * Adds the entry at the specified <code>index</code> to this
	 * {@link ItemList}.
	 *
	 * @param index
	 *            index at which the specified element is to be inserted
	 * @param entry
	 *            the entry to add
	 */
	public void add(final int index, final Entry entry)
	{
		this.entries.add(index,entry);
		fireIntervalAdded(index,index);
	}
	
	
	/**
	 * Adds all entries of <code>item</code> and <code>data</code> to this
	 * {@link ItemList}.
	 *
	 * @param item
	 *            {@link Collection} of items to add
	 * @param data
	 *            {@link Collection} of data to add
	 */
	public void addAll(final Collection<?> item, final Collection<?> data)
	{
		final int oldSize = this.entries.size();
		
		final Object[] itemElems = item.toArray();
		final Object[] dataElems = data.toArray();
		
		final int max = Math.min(itemElems.length,dataElems.length);
		for(int i = 0; i < max; i++)
		{
			this.entries.add(new Entry(String.valueOf(itemElems[i]),dataElems[i]));
		}
		
		fireIntervalAdded(oldSize,this.entries.size() - 1);
	}
	
	
	/**
	 * Adds all <code>entries</code> to this {@link ItemList}.
	 *
	 * @param entries
	 *            the entries to add
	 */
	public void addAll(final Collection<? extends Entry> entries)
	{
		final int oldSize = entries.size();
		
		for(final Entry entry : entries)
		{
			this.entries.add(entry.clone());
		}
		
		fireIntervalAdded(oldSize,entries.size() - 1);
	}
	
	
	/**
	 * Adds all entries of given {@link ItemList} <code>il</code> to this
	 * {@link ItemList}.
	 *
	 * @param il
	 *            {@link ItemList} to add
	 */
	public void addAll(final ItemList il)
	{
		addAll(il.entries);
	}
	
	
	/**
	 * Adds all entries of the <code>model</code> to this {@link ItemList}.
	 *
	 * @param model
	 *            the data model
	 */
	public void addAll(final ListModel model)
	{
		final int oldSize = this.entries.size();
		
		final int c = model.getSize();
		for(int i = 0; i < c; i++)
		{
			this.entries.add(new Entry(model.getElementAt(i)));
		}
		
		fireIntervalAdded(oldSize,this.entries.size() - 1);
	}
	
	
	/**
	 * Removes the element at the specified position in this {@link ItemList}.
	 * Shifts any subsequent elements to the top (subtracts one from their
	 * indices). Returns the element that was removed from the list.
	 *
	 * @param index
	 *            the index of the element to be removed
	 *
	 * @return the element previously at the specified position
	 *
	 * @throws IndexOutOfBoundsException
	 *             if the index is out of range (
	 *             <tt>index &lt; 0 || index &gt;= size()</tt>)
	 */
	public Entry remove(final int index) throws IndexOutOfBoundsException
	{
		final Entry entry = this.entries.remove(index);
		fireIntervalRemoved(index,index);
		return entry;
	}
	
	
	/**
	 * Removes the elements at the specified positions in this {@link ItemList}.
	 * Shifts any subsequent elements to the top (subtracts one from their
	 * indices). Returns the elements that were removed from the list.
	 *
	 * @param indices
	 *            the indices of the elements to be removed
	 *
	 * @return a array of element's previously at the specified position's
	 *
	 * @throws IndexOutOfBoundsException
	 *             if an index is out of range (
	 *             <tt>index &lt; 0 || index &gt;= size()</tt>)
	 */
	public Entry[] removeAll(final int... indices) throws IndexOutOfBoundsException
	{
		final int[] sorted = ArrayUtils.copyOf(indices);
		Arrays.sort(sorted);
		final Entry[] entries = new Entry[sorted.length];
		for(int i = sorted.length, ei = 0; --i >= 0; ei++)
		{
			entries[ei] = remove(sorted[i]);
		}
		return entries;
	}
	
	
	/**
	 * Returns the entry at the specified position in this {@link ItemList}.
	 *
	 * @param index
	 *            index of the entry to return
	 * @return the entry at the specified position in this list
	 * @throws IndexOutOfBoundsException
	 *             if the index is out of range (
	 *             <tt>index &lt; 0 || index &gt;= size()</tt>)
	 */
	public Entry get(final int index) throws IndexOutOfBoundsException
	{
		return this.entries.get(index);
	}
	
	
	/**
	 * Returns the item at the specified position in this {@link ItemList}.
	 *
	 * @param index
	 *            index of the item to return
	 * @return the element at the specified position in this list
	 * @throws IndexOutOfBoundsException
	 *             if the index is out of range (
	 *             <tt>index &lt; 0 || index &gt;= size()</tt>)
	 */
	public Object getItem(final int index) throws IndexOutOfBoundsException
	{
		return get(index).getItem();
	}
	
	
	/**
	 * Returns the data at the specified position in this {@link ItemList}.
	 *
	 * @param index
	 *            index of the data to return
	 * @return the element at the specified position in this list
	 * @throws IndexOutOfBoundsException
	 *             if the index is out of range (
	 *             <tt>index &lt; 0 || index &gt;= size()</tt>)
	 */
	public Object getData(final int index) throws IndexOutOfBoundsException
	{
		return get(index).getData();
	}
	
	
	/**
	 * Returns the size of the list.
	 *
	 * @return the size of the list
	 */
	public int size()
	{
		return this.entries.size();
	}
	
	
	/**
	 * Removes all of the elements from this {@link ItemList}. The
	 * {@link ItemList} will be empty after this call returns.
	 *
	 */
	public void clear()
	{
		final int size = size();
		if(size > 0)
		{
			this.entries.clear();
			fireIntervalRemoved(0,size - 1);
		}
	}
	
	
	/**
	 * @since 4.0
	 */
	void setPageControl(final ItemListPageControl<?> pageControl)
	{
		this.pageControl = pageControl;
	}
	
	
	/**
	 * public @since 4.0
	 */
	public void syncWithVT()
	{
		setModelImpl(this.virtualTable,this.itemCol,this.dataCol,null);
	}
	
	
	/***
	 * Adds all row data (formatted) of the given {@link VirtualTable}
	 * <code>vt</code> to this {@link ItemList}.
	 *
	 * @param vt
	 *            {@link VirtualTable} to read the rows from.
	 * @param itemCol
	 *            columnname to fill <code>item</code> from or string with
	 *            variables like <code>"{%SURNAME} {%NAME} - {%AGE}"</code>
	 * @param dataCol
	 *            column name to fill <code>data</code> from, or multiple
	 *            columns names, comma-separated
	 */
	public void setModel(final VirtualTable vt, final String itemCol, final String dataCol)
	{
		setModel(vt,itemCol,dataCol,false);
	}
	
	
	/***
	 * Adds all row data (formatted) of the given {@link VirtualTable}
	 * <code>vt</code> to this {@link ItemList}.
	 *
	 * @param vt
	 *            {@link VirtualTable} to read the rows from.
	 * @param itemCol
	 *            columnname to fill <code>item</code> from or string with
	 *            variables like <code>"{%SURNAME} {%NAME} - {%AGE}"</code>
	 * @param dataCol
	 *            column name to fill <code>data</code> from, or multiple
	 *            columns names, comma-separated
	 * @param queryData
	 *            if {@code true}, the best fitting select for this {@code vt}
	 *            is used
	 */
	public void setModel(final VirtualTable vt, final String itemCol, final String dataCol,
			final boolean queryData)
	{
		setModel(vt,itemCol,dataCol,queryData,false);
	}
	
	
	/***
	 * Adds all row data (formatted) of the given {@link VirtualTable}
	 * <code>vt</code> to this {@link ItemList}.
	 *
	 * @param vt
	 *            {@link VirtualTable} to read the rows from.
	 * @param itemCol
	 *            columnname to fill <code>item</code> from or string with
	 *            variables like <code>"{%SURNAME} {%NAME} - {%AGE}"</code>
	 * @param dataCol
	 *            column name to fill <code>data</code> from, or multiple
	 *            columns names, comma-separated
	 * @param queryData
	 *            if {@code true}, the best fitting select for this {@code vt}
	 *            is used
	 * @param selectiveQuery
	 *            if {@code true}, only the used <code>columns</code> are
	 *            queried
	 * @since 5.0
	 */
	public void setModel(final VirtualTable vt, final String itemCol, final String dataCol,
			final boolean queryData, final boolean selectiveQuery)
	{
		SELECT select = null;
		if(queryData)
		{
			select = getDefaultModelQuery(vt,itemCol,dataCol,selectiveQuery);
		}
		
		setModel(vt,itemCol,dataCol,select);
	}
	
	
	/***
	 * Adds all row data (formatted) of the given {@link VirtualTable}
	 * <code>vt</code> to this {@link ItemList}.
	 *
	 * @param vt
	 *            {@link VirtualTable} to read the rows from.
	 * @param itemCol
	 *            columnname to fill <code>item</code> from or string with
	 *            variables like <code>"{%SURNAME} {%NAME} - {%AGE}"</code>
	 * @param dataCol
	 *            column name to fill <code>data</code> from, or multiple
	 *            columns names, comma-separated
	 * @param select
	 *            a custom {@link SELECT} for filtering or ordering the results
	 *            of the {@code vt}, may be null for the default select
	 * @param params
	 *            a set of parameters related to the param {@code select}, may
	 *            be null
	 */
	public void setModel(final VirtualTable vt, final String itemCol, final String dataCol,
			final SELECT select, final Object... params)
	{
		setModelImpl(vt.clone(true),itemCol,dataCol,select,params);
	}
	
	
	private void setModelImpl(final VirtualTable vt, final String itemCol, final String dataCol,
			final SELECT select, final Object... params)
	{
		this.virtualTable = vt;
		this.itemCol = itemCol;
		this.dataCol = dataCol;
		
		if(select != null)
		{
			try
			{
				if(this.pageControl != null)
				{
					this.pageControl.changeModel(select,params,0);
				}
				else
				{
					vt.queryAndFill(select,params);
				}
			}
			catch(final Exception e)
			{
				log.error(e);
			}
		}
		
		clear();
		if(vt.getRowCount() == 0)
		{
			return;
		}
		
		final int itemColumnIndex = vt.getColumnIndex(itemCol);
		
		final String[] dataColNames = FormularComponentSupport.getDataFields(dataCol);
		final IntList dataColIndexes = new IntList();
		for(final String dataColName : dataColNames)
		{
			final int index = vt.getColumnIndex(dataColName);
			if(index != -1)
			{
				dataColIndexes.add(index);
			}
		}
		final int dataColIndexCount = dataColIndexes.size();
		
		final int c = vt.getRowCount();
		for(int row = 0; row < c; row++)
		{
			String item;
			if(itemColumnIndex != -1)
			{
				item = vt.getFormattedValueAt(row,itemColumnIndex);
			}
			else
			{
				item = vt.format(row,itemCol);
			}
			
			Object data;
			if(dataColIndexCount == 1)
			{
				data = vt.getValueAt(row,dataColIndexes.get(0));
			}
			else if(dataColIndexCount > 1)
			{
				final Object[] array = new Object[dataColIndexCount];
				for(int i = 0; i < dataColIndexCount; i++)
				{
					array[i] = vt.getValueAt(row,dataColIndexes.get(i));
				}
				data = array;
			}
			else
			{
				data = null;
			}
			
			this.entries.add(new Entry(item,data));
		}
		
		fireIntervalAdded(0,size() - 1);
	}
	
	
	/**
	 * Reloads the data from the underlying data source with the last executed
	 * resp. default query.
	 */
	public void refresh()
	{
		if(this.virtualTable != null && this.itemCol != null)
		{
			try
			{
				if(this.pageControl != null)
				{
					this.pageControl.refresh();
				}
				else if(this.virtualTable.getLastQuery() != null)
				{
					this.virtualTable.reload();
				}
				else
				{
					this.virtualTable.queryAndFill();
				}
				
				syncWithVT();
			}
			catch(final Exception e)
			{
				log.error(e);
			}
		}
	}
	
	
	/**
	 * Reloads the data form the underlying data source.
	 * <p>
	 * The last executed query is extended with <code>condition</code>.
	 *
	 * @param condition
	 *            The additional filter for the query
	 * @param params
	 *            param objects used in <code>condition</code>
	 */
	public void updateModel(final Condition condition, Object... params)
	{
		if(this.virtualTable != null && this.itemCol != null)
		{
			final QueryInfo lastQuery = this.virtualTable.getLastQuery();
			final QueryInfo queryClone = lastQuery != null ? lastQuery.clone() : null;
			SELECT select;
			if(lastQuery != null)
			{
				select = lastQuery.getSelect();
				
				final Object[] lastParams = lastQuery.getParameters();
				if(lastParams.length > 0)
				{
					params = ArrayUtils.concat(Object.class,lastParams,params);
				}
			}
			else
			{
				select = getDefaultModelQuery(this.virtualTable,this.itemCol,this.dataCol,false);
			}
			
			if(condition != null)
			{
				WHERE where = select.getWhere();
				if(where != null && !where.isEmpty())
				{
					where = new WHERE(where.encloseWithPars().and(condition));
				}
				else
				{
					where = new WHERE(condition);
				}
				select.WHERE(where);
			}
			
			setModel(this.virtualTable,this.itemCol,this.dataCol,select,params);
			
			this.virtualTable.setLastQuery(queryClone);
		}
	}
	
	
	static SELECT getDefaultModelQuery(final VirtualTable vt, final String itemCol,
			final String dataCol, final boolean selectiveQuery)
	{
		final SELECT select;
		if(selectiveQuery)
		{
			final Set<String> columnNames = new LinkedHashSet<>();
			
			// always query primary key columns
			for(final VirtualTableColumn<?> pkColumn : vt.getPrimaryKeyColumns())
			{
				columnNames.add(pkColumn.getName());
			}
			
			if(vt.getColumn(itemCol) != null)
			{
				columnNames.add(itemCol);
			}
			else
			{
				StringUtils.format(itemCol,key -> {
					columnNames.add(key);
					return key;
				});
			}
			
			if(dataCol != null && dataCol.length() > 0)
			{
				for(final String name : FormularComponentSupport.getDataFields(dataCol))
				{
					columnNames.add(name);
				}
			}
			
			select = vt
					.getSelect(vt.getColumns(columnNames.toArray(new String[columnNames.size()])));
		}
		else
		{
			select = vt.getSelect();
		}
		
		int itemColumnIndex = vt.getColumnIndex(itemCol);
		if(itemColumnIndex == -1)
		{
			int start;
			int searchStart = 0;
			while(itemColumnIndex == -1 && (start = itemCol.indexOf("{%",searchStart)) >= 0)
			{
				final int end = itemCol.indexOf("}",start + 2);
				if(end > start)
				{
					final String col = itemCol.substring(start + 2,end);
					itemColumnIndex = vt.getColumnIndex(col);
					searchStart = end;
				}
			}
		}
		if(itemColumnIndex != -1)
		{
			select.ORDER_BY(vt.getColumnAt(itemColumnIndex));
		}
		
		return select;
	}
	
	
	/**
	 * Returns the {@link VirtualTable} which was associated via
	 * {@link #setModel(VirtualTable, String, String)}, or <code>null</code> if
	 * no {@link VirtualTable} has been associated yet.
	 *
	 *
	 * @return The {@link VirtualTable} which was associated via
	 *         {@link #setModel(VirtualTable, String, String)}, or
	 *         <code>null</code>
	 */
	
	public VirtualTable getVirtualTable()
	{
		return this.virtualTable;
	}
	
	
	/**
	 * Returns <tt>true</tt> if this {@link ItemList} contains the specified
	 * <code>item</code>. More formally, returns <tt>true</tt> if and only if
	 * this list contains at least one element <tt>e</tt> such that
	 * <tt>(o==null&nbsp;?&nbsp;e==null&nbsp;:&nbsp;o.equals(e))</tt>.
	 *
	 * @param item
	 *            <code>item</code> whose presence in this list is to be tested
	 * @return <tt>true</tt> if this list contains the specified element
	 */
	public boolean containsItem(final Object item)
	{
		return indexOfItem(item) != -1;
	}
	
	
	/**
	 * Returns <tt>true</tt> if this {@link ItemList} contains the specified
	 * <code>entry</code>. More formally, returns <tt>true</tt> if and only if
	 * this list contains at least one element <tt>e</tt> such that
	 * <tt>(o==null&nbsp;?&nbsp;e==null&nbsp;:&nbsp;o.equals(e))</tt>.
	 *
	 * @param entry
	 *            <code>item</code> whose presence in this list is to be tested
	 * @return <tt>true</tt> if this list contains the specified element
	 */
	public boolean contains(final Entry entry)
	{
		return this.entries.contains(entry);
	}
	
	
	/**
	 * Returns the index of the first occurrence of the specified
	 * <code>item</code> in this list, or -1 if this {@link ItemList} does not
	 * contain the <code>item</code>. More formally, returns the lowest index
	 * <tt>i</tt> such that
	 * <tt>(o==null&nbsp;?&nbsp;get(i)==null&nbsp;:&nbsp;o.equals(get(i)))</tt>,
	 * or -1 if there is no such index.
	 *
	 * @param item
	 *            <code>item</code> to search for
	 * @return the index of the first occurrence of the specified
	 *         <code>item</code> in this list, or -1 if this list does not
	 *         contain the <code>item</code>
	 */
	public int indexOfItem(final Object item)
	{
		final int c = size();
		for(int i = 0; i < c; i++)
		{
			if(ObjectUtils.equals(item,get(i).getItem()))
			{
				return i;
			}
		}
		return -1;
	}
	
	
	/**
	 * Returns <tt>true</tt> if this {@link ItemList} contains the specified
	 * <code>data</code>. More formally, returns <tt>true</tt> if and only if
	 * this list contains at least one element <tt>e</tt> such that
	 * <tt>(o==null&nbsp;?&nbsp;e==null&nbsp;:&nbsp;o.equals(e))</tt>.
	 *
	 * @param data
	 *            <code>data</code> whose presence in this list is to be tested
	 * @return <tt>true</tt> if this list contains the specified element
	 */
	public boolean containsData(final Object data)
	{
		return indexOfData(data) != -1;
	}
	
	
	/**
	 * Returns the index of the first occurrence of the specified
	 * <code>data</code> in this list, or -1 if this {@link ItemList} does not
	 * contain the <code>data</code>. More formally, returns the lowest index
	 * <tt>i</tt> such that
	 * <tt>(o==null&nbsp;?&nbsp;get(i)==null&nbsp;:&nbsp;o.equals(get(i)))</tt>,
	 * or -1 if there is no such index.
	 *
	 * @param data
	 *            <code>data</code> to search for
	 * @return the index of the first occurrence of the specified
	 *         <code>item</code> in this list, or -1 if this list does not
	 *         contain the <code>data</code>
	 */
	public int indexOfData(final Object data)
	{
		final int c = size();
		for(int i = 0; i < c; i++)
		{
			if(ObjectUtils.equals(data,get(i).getData()))
			{
				return i;
			}
		}
		return -1;
	}
	
	
	/**
	 * Returns a {@link List} containing all <code>item</code>s of this
	 * {@link ItemList}.
	 *
	 * @return a {@link List} containing all <code>item</code>s of this
	 *         {@link ItemList}.
	 */
	public List getItemsAsList()
	{
		final int c = size();
		final List items = new ArrayList(c);
		for(int i = 0; i < c; i++)
		{
			items.add(getItem(i));
		}
		return items;
	}
	
	
	/**
	 * Returns a {@link List} containing all <code>data</code>s of this
	 * {@link ItemList}.
	 *
	 * @return a {@link List} containing all <code>data</code>s of this
	 *         {@link ItemList}.
	 */
	public List getDataAsList()
	{
		final int c = size();
		final List data = new ArrayList(c);
		for(int i = 0; i < c; i++)
		{
			data.add(getData(i));
		}
		return data;
	}
	
	
	/**
	 * Sets the size of this {@link ItemList}. If the {@link ItemList} has more
	 * entries that the specified <code>size</code>, list entries will be
	 * removed beginning with the largest index. If the {@link ItemList} has
	 * less than the specified <code>size</code>, list entries (item = "", data
	 * = "") will be added at the end of this {@link ItemList}.
	 *
	 * @param size
	 *            new size of the {@link ItemList}
	 */
	public void setSize(final int size)
	{
		final int thisSize = this.entries.size();
		
		if(thisSize < size)
		{
			while(this.entries.size() < size)
			{
				this.entries.add(new Entry("",""));
			}
			
			fireIntervalAdded(thisSize,size() - 1);
		}
		else if(thisSize > size)
		{
			while(this.entries.size() > size)
			{
				this.entries.remove(this.entries.size() - 1);
			}
			
			fireIntervalRemoved(size - 1,thisSize - 1);
		}
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString()
	{
		return StringUtils.concat(", ",this.entries);
	}
}
