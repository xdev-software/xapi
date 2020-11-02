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


import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Point;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.swing.DefaultRowSorter;
import javax.swing.JTable;
import javax.swing.JViewport;
import javax.swing.ListSelectionModel;
import javax.swing.RowSorter;
import javax.swing.RowSorter.SortKey;
import javax.swing.SortOrder;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import xdev.db.Operator;
import xdev.db.sql.Condition;
import xdev.db.sql.SELECT;
import xdev.ui.paging.LazyLoadable;
import xdev.ui.paging.PageControl;
import xdev.ui.paging.Pageable;
import xdev.ui.persistence.Persistable;
import xdev.ui.table.EditorDelegate;
import xdev.ui.table.ExtendedTable;
import xdev.ui.table.XdevTableEditor;
import xdev.ui.table.XdevTableHeaderRenderer;
import xdev.ui.table.XdevTableRenderer;
import xdev.util.ObjectUtils;
import xdev.vt.VirtualTable;
import xdev.vt.VirtualTable.VirtualTableRow;
import xdev.vt.VirtualTableModel;
import xdev.vt.VirtualTableWrapper;


/**
 * The standard table in XDEV. Based on {@link JTable}.
 *
 * @see JTable
 *
 * @author XDEV Software
 *
 * @since 2.0
 */
@BeanSettings(useXdevCustomizer = true)
public class XdevTable extends JTable implements ExtendedTable<XdevTable>, XdevFocusCycleComponent,
		VirtualTableEditor, Persistable, PopupRowSelectionHandler, Pageable, LazyLoadable
{
	private static final long				serialVersionUID			= -5259133447843483181L;
	
	public final static String				VISIBLE_ROWS_PROPERTY		= "visibleRows";
	public final static String				EVEN_BACKGROUND_PROPERTY	= "evenBackground";
	public final static String				ODD_BACKGROUND_PROPERTY		= "oddBackground";
	
	public static final String				PERSISTENT_COLUMN_SEPARATOR	= "#";
	
	private boolean							editable					= true;
	private int								visibleRows					= 8;
	private Color							evenBackground				= null;
	private Color							oddBackground				= null;
	
	/**
	 * tabIndex is used to store the index for {@link XdevFocusCycleComponent}
	 * functionality.
	 */
	private int								tabIndex					= -1;
	
	/**
	 * Should the gui state be persisted? Defaults to {@code true}.
	 */
	private boolean							persistenceEnabled			= true;
	
	protected final TableSupport<XdevTable>	support						= new TableSupport(this);
	
	
	/**
	 * Constructs a default {@link XdevTable} that is initialized with a default
	 * data model, a default column model, and a default selection model.
	 *
	 * @see JTable
	 */
	public XdevTable()
	{
		super();
		init();
	}
	
	
	/**
	 * Constructs a {@link XdevTable} that is initialized with
	 * {@link DefaultTableModel} as the data model, a default column model, and
	 * a default selection model. <br>
	 * The first index in the <code>Object[][]</code> array is the row index and
	 * the second is the column index.
	 *
	 * @param data
	 *            the data of the table
	 *
	 * @param columnNames
	 *            the names of the columns
	 *
	 * @see JTable
	 */
	public XdevTable(final Object[][] data, final Object[] columnNames)
	{
		this(new DefaultTableModel(data,columnNames));
	}
	
	
	/**
	 * Constructs a {@link XdevTable} that is initialized with
	 * {@link DefaultTableModel} as the data model, a default column model, and
	 * a default selection model. <br>
	 *
	 * @param data
	 *            the data of the table, a <code>Vector</code> of
	 *            <code>Vector</code>s of <code>Object</code> values
	 *
	 * @param columnNames
	 *            <code>vector</code> containing the names of the new columns
	 */
	public XdevTable(final Vector data, final Vector columnNames)
	{
		this(new DefaultTableModel(data,columnNames));
	}
	
	
	/**
	 * Constructs a {@link XdevTable} that is initialized with
	 * <code>model</code> as the data model, a default column model, and a
	 * default selection model.
	 *
	 * @param model
	 *            the data model for the table
	 *
	 * @see JTable
	 */
	public XdevTable(final TableModel model)
	{
		super(model);
		init();
	}
	
	
	private void init()
	{
		setFillsViewportHeight(true);
		setEditable(false);
		
		putClientProperty("terminateEditOnFocusLost",Boolean.TRUE);
		
		final JTableHeader header = getTableHeader();
		if(header != null)
		{
			header.setReorderingAllowed(true);
			header.setDefaultRenderer(new XdevTableHeaderRenderer(this));
		}
		
		setDefaultRenderer(new XdevTableRenderer());
		setDefaultEditor(new XdevTableEditor());
	}
	
	
	/**
	 * Sets the <code>data</code> and <code>columnNames</code> for this
	 * {@link XdevTable}.
	 *
	 * @param data
	 *            the data of the table. <b>NOTE:</b> The first index in the
	 *            Object[][] array is the row index and the second is the column
	 *            index.
	 *
	 * @param columnNames
	 *            the names of the columns
	 */
	public void setData(final Object[][] data, final Object[] columnNames)
	{
		setModel(new DefaultTableModel(data,columnNames));
	}
	
	
	/**
	 * Sets the <code>data</code> and <code>columnNames</code> for this
	 * {@link XdevTable}.
	 *
	 * @param data
	 *            the data of the table, a <code>Vector</code> of
	 *            <code>Vector</code>s of <code>Object</code> values
	 * @param columnNames
	 *            <code>vector</code> containing the names of the new columns
	 */
	public void setData(final Vector data, final Vector columnNames)
	{
		setModel(new DefaultTableModel(data,columnNames));
	}
	
	
	/**
	 * Add a {@link ListSelectionListener} to the list that's notified each time
	 * a change to the selection occurs.
	 *
	 * @param listener
	 *            the ListSelectionListener
	 *
	 * @see #removeListSelectionListener(ListSelectionListener)
	 */
	public void addListSelectionListener(final ListSelectionListener listener)
	{
		getSelectionModel().addListSelectionListener(listener);
	}
	
	
	/**
	 * Remove a listener from the {@link ListSelectionModel} that's notified
	 * each time a change to the selection occurs.
	 *
	 * @param listener
	 *            the {@link ListSelectionListener}
	 *
	 * @see #addListSelectionListener(ListSelectionListener)
	 */
	public void removeListSelectionListener(final ListSelectionListener listener)
	{
		getSelectionModel().removeListSelectionListener(listener);
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setModel(final VirtualTable vt)
	{
		this.support.setModel(vt);
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setModel(final VirtualTable vt, final String columns, final boolean queryData)
	{
		this.support.setModel(vt,columns,queryData);
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setModel(final VirtualTable vt, final String columns, final boolean queryData,
			final boolean selectiveQuery)
	{
		this.support.setModel(vt,columns,queryData,selectiveQuery);
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setModel(final VirtualTable vt, final String columns, final SELECT select,
			final Object... params)
	{
		this.support.setModel(vt,columns,select,params);
	}
	
	
	/**
	 * {@inheritDoc}
	 *
	 * @since 4.0
	 */
	@Override
	public void setPagingEnabled(final boolean b)
	{
		this.support.setPagingEnabled(b);
	}
	
	
	/**
	 * {@inheritDoc}
	 *
	 * @since 4.0
	 */
	@Override
	public boolean isPagingEnabled()
	{
		return this.support.isPagingEnabled();
	}
	
	
	/**
	 * {@inheritDoc}
	 *
	 * @since 4.0
	 */
	@Override
	public PageControl getPageControl()
	{
		return this.support.getPageControl();
	}
	
	
	/**
	 * Enabled or disables the lazy loading feature.
	 * <p>
	 * If lazy loading is enabled only the displayed rows are loaded.
	 *
	 * @since 4.0
	 */
	@Override
	public void setLazyLoadingEnabled(final boolean lazyLoadingEnabled)
	{
		this.support.setLazyLoadingEnabled(lazyLoadingEnabled);
	}
	
	
	/**
	 * @return if the lazy loading feature is enabled
	 *
	 * @since 4.0
	 */
	@Override
	public boolean isLazyLoadingEnabled()
	{
		return this.support.isLazyLoadingEnabled();
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
	 * {@inheritDoc}
	 */
	@Override
	public void setVisibleRowCount(final int visibleRows)
	{
		if(this.visibleRows != visibleRows)
		{
			final int oldValue = this.visibleRows;
			this.visibleRows = visibleRows;
			firePropertyChange(VISIBLE_ROWS_PROPERTY,oldValue,visibleRows);
			revalidate();
		}
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getVisibleRowCount()
	{
		return this.visibleRows;
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
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
	 * {@inheritDoc}
	 */
	@Override
	public Color getEvenBackground()
	{
		return this.evenBackground;
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
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
	 * {@inheritDoc}
	 */
	@Override
	public Color getOddBackground()
	{
		return this.oddBackground;
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
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void exportCSV(final Writer writer) throws IOException
	{
		this.support.exportCSV(writer);
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void exportCSV(final Writer writer, final char delimiter) throws IOException
	{
		this.support.exportCSV(writer,delimiter);
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public synchronized void exportCSV(final Writer writer, final char delimiter,
			final boolean withColumnNames) throws IOException
	{
		this.support.exportCSV(writer,delimiter,withColumnNames);
	}
	
	
	protected String exportCSV_getColumnName(final TableColumn column)
	{
		return this.support.exportCSV_getColumnName(column);
	}
	
	
	protected String exportCSV_getValue(final Object value, final int row, final int col)
	{
		return this.support.exportCSV_getValue(value,row,col);
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
	public final void setFormularValue(final VirtualTable vt, final int col, final Object value)
	{
		this.support.setFormularValue(vt,col,value);
	}
	
	
	/**
	 * {@inheritDoc}
	 *
	 * @since 3.2
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
		setModel(vt);
	}
	
	
	/**
	 * Returns the {@link VirtualTable} that provides the data displayed by this
	 * {@link XdevTable}.
	 *
	 * @return a {@link VirtualTable} that provides the data of this
	 *         {@link XdevTable}.<br>
	 *         If the {@link TableModel} is not a {@link VirtualTableWrapper}
	 *         <code>null</code> is returned.
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
	public VirtualTableWrapper getVirtualTableWrapper()
	{
		return this.support.getVirtualTableWrapper();
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
	public void clear()
	{
		setModel(new DefaultTableModel());
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isEditable()
	{
		return this.editable;
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setEditable(final boolean editable)
	{
		if(this.editable != editable)
		{
			final boolean oldValue = this.editable;
			this.editable = editable;
			firePropertyChange(EDITABLE_PROPERTY,oldValue,editable);
		}
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isCellEditable(final int row, final int column)
	{
		
		return isEditable() && super.isCellEditable(row,column);
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void addImpl(final Component comp, final Object constraints, final int index)
	{
		super.addImpl(comp,constraints,index);
		
		if(comp instanceof EditorDelegate)
		{
			((EditorDelegate)comp).getFocusComponent().requestFocus();
		}
	}
	
	
	/**
	 * {@inheritDoc}
	 *
	 */
	@Override
	public void setModel(final TableModel model)
	{
		super.setModel(model);
		
		setRowSorter(this.support != null ? this.support.createRowSorter(model)
				: new TableRowSorter(model));
		
		if(this.support != null)
		{
			this.support.adjustColumnWidths();
		}
	}
	
	
	/**
	 * Clears all previous set renderers and registers <code>renderer</code> as
	 * default renderer for <code>Object.class</code>, which makes it the
	 * default renderer as long as no other renderer is set for a specific
	 * column class.
	 *
	 * @param renderer
	 *            the new default renderer
	 * @see #setDefaultRenderer(Class, TableCellRenderer)
	 * @see TableColumn#setCellRenderer(TableCellRenderer)
	 */
	@Override
	public void setDefaultRenderer(final TableCellRenderer renderer)
	{
		this.defaultRenderersByColumnClass.clear();
		setDefaultRenderer(Object.class,renderer);
	}
	
	
	/**
	 * Clears all previous set editors and registers <code>editor</code> as
	 * default editor for <code>Object.class</code>, which makes it the default
	 * editor as long as no other editor is set for a specific column class.
	 *
	 * @param editor
	 *            the new default editor
	 * @see #setDefaultEditor(Class, TableCellEditor)
	 * @see TableColumn#setCellEditor(TableCellEditor)
	 */
	@Override
	public void setDefaultEditor(final TableCellEditor editor)
	{
		this.defaultEditorsByColumnClass.clear();
		setDefaultEditor(Object.class,editor);
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isSomethingSelected()
	{
		return this.support.isSomethingSelected();
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getSelectedModelRow() throws IndexOutOfBoundsException
	{
		return this.support.getSelectedModelRow();
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public int[] getSelectedModelRows() throws IndexOutOfBoundsException
	{
		return this.support.getSelectedModelRows();
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
	 * {@inheritDoc}
	 *
	 * @since 3.2
	 */
	@Override
	public void setSelectedVirtualTableRow(final VirtualTableRow row)
	{
		this.support.setSelectedVirtualTableRow(row);
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public VirtualTableRow[] getSelectedVirtualTableRows()
	{
		return this.support.getSelectedVirtualTableRows();
	}
	
	
	/**
	 * {@inheritDoc}
	 *
	 * @since 3.2
	 */
	@Override
	public void setSelectedVirtualTableRows(final VirtualTableRow[] rows)
	{
		this.support.setSelectedVirtualTableRows(rows);
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setSelectedModelRow(final int row)
			throws IndexOutOfBoundsException, IllegalArgumentException
	{
		this.support.setSelectedModelRow(row);
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setSelectedModelRows(final int[] rows)
			throws IndexOutOfBoundsException, IllegalArgumentException
	{
		this.support.setSelectedModelRows(rows);
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setSelectedModelRows(final int start, final int end)
	{
		this.support.setSelectedModelRows(start,end);
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setSelectedRows(final int[] indices)
	{
		this.support.setSelectedRows(indices);
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getRowAtPoint(final int x, final int y) throws IndexOutOfBoundsException
	{
		return this.support.getRowAtPoint(x,y);
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getRowAtPoint(final Point location) throws IndexOutOfBoundsException
	{
		return this.support.getRowAtPoint(location);
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getColumnAtPoint(final int x, final int y)
	{
		return this.support.getColumnAtPoint(x,y);
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getColumnAtPoint(final Point location)
	{
		return this.support.getColumnAtPoint(location);
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setColumnWidth(final int index, final int width)
	{
		this.support.setColumnWidth(index,width);
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setColumnTitle(final int index, final String title)
	{
		this.support.setColumnTitle(index,title);
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean getScrollableTracksViewportHeight()
	{
		if(getParent() instanceof JViewport)
		{
			return getParent().getHeight() > getPreferredSize().height;
		}
		return false;
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Dimension getPreferredScrollableViewportSize()
	{
		final Dimension d = super.getPreferredScrollableViewportSize();
		if(this.visibleRows > 0)
		{
			d.height = this.visibleRows * (getRowHeight() + getRowMargin());
		}
		
		return d;
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
	 *
	 * @see #savePersistentState()
	 */
	@Override
	public void loadPersistentState(final String persistentState)
	{
		final String[] persistentColumns = persistentState.split(PERSISTENT_COLUMN_SEPARATOR);
		final String[][] persistentColumnValues = new String[persistentColumns.length][];
		for(int i = 0; i < persistentColumns.length; i++)
		{
			final String[] persistentValues = persistentColumns[i]
					.split(Persistable.VALUE_SEPARATOR);
			persistentColumnValues[i] = persistentValues;
		}
		
		final TableColumnModel columnModel = this.getColumnModel();
		for(int i = 0; i < persistentColumnValues.length; i++)
		{
			final String[] persistentValues = persistentColumnValues[i];
			for(int j = 0; j < columnModel.getColumnCount(); j++)
			{
				final TableColumn column = columnModel.getColumn(j);
				final Object identifier = column.getIdentifier();
				if(identifier.equals(persistentValues[0]))
				{
					
					column.setPreferredWidth(Integer.parseInt(persistentValues[1]));
					
					final int currentIndex = columnModel.getColumnIndex(column.getIdentifier());
					final int newIndex = Integer.parseInt(persistentValues[2]);
					columnModel.moveColumn(currentIndex,newIndex);
					
					if(persistentValues.length > 3)
					{
						final int sortOrderOrdinal = Integer.parseInt(persistentValues[3]);
						final SortOrder sortOrder = SortOrder.values()[sortOrderOrdinal];
						final int modelIndex = column.getModelIndex();
						final DefaultRowSorter<?, ?> sorter = ((DefaultRowSorter<?, ?>)this
								.getRowSorter());
						final List<SortKey> sortKeys = new ArrayList<SortKey>();
						sortKeys.add(new SortKey(modelIndex,sortOrder));
						sorter.setSortKeys(sortKeys);
						sorter.sort();
					}
					break;
				}
			}
		}
	}
	
	
	/**
	 * {@inheritDoc}
	 * <p>
	 * Persisted properties:
	 * <ul>
	 * <li>column ordering</li>
	 * <li>column widths</li>
	 * <li>sorting direction</li>
	 * </ul>
	 * </p>
	 */
	@Override
	public String savePersistentState()
	{
		final StringBuilder persistentState = new StringBuilder();
		
		final TableColumnModel columnModel = this.getColumnModel();
		
		for(int i = 0; i < columnModel.getColumnCount(); i++)
		{
			if(i != 0)
			{
				persistentState.append(PERSISTENT_COLUMN_SEPARATOR);
			}
			final TableColumn column = columnModel.getColumn(i);
			persistentState.append(column.getIdentifier()); // position 0 for
															// identifier
			persistentState.append(Persistable.VALUE_SEPARATOR);
			persistentState.append(column.getWidth()); // position 1 for width
			persistentState.append(Persistable.VALUE_SEPARATOR);
			persistentState.append(columnModel.getColumnIndex(column.getIdentifier())); // position
																						// 2
																						// for
																						// index
			
			final int modelIndex = column.getModelIndex();
			final RowSorter<?> rowSorter = this.getRowSorter();
			final List<?> sortKeys = rowSorter.getSortKeys();
			
			if(!sortKeys.isEmpty())
			{
				final SortKey sortKey = (SortKey)sortKeys.get(0);
				if(sortKey.getColumn() == modelIndex)
				{
					persistentState.append(Persistable.VALUE_SEPARATOR);
					persistentState.append("" + sortKey.getSortOrder().ordinal()); // position
																					// 3
																					// for
																					// sortorder
				}
			}
		}
		
		return persistentState.toString();
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getPersistentId()
	{
		return (this.getName() != null) ? this.getName() : this.getClass().getSimpleName();
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isPersistenceEnabled()
	{
		return this.persistenceEnabled;
	}
	
	
	/**
	 * Sets the persistenceEnabled flag.
	 *
	 * @param persistenceEnabled
	 *            the state for this instance
	 */
	public void setPersistenceEnabled(final boolean persistenceEnabled)
	{
		this.persistenceEnabled = persistenceEnabled;
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void ensureCellIsVisible(final int rowIndex, final int columnIndex)
	{
		this.support.ensureCellIsVisible(rowIndex,columnIndex);
		
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void ensureRowIsVisible(final int rowIndex)
	{
		this.support.ensureRowIsVisible(rowIndex);
		
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void ensureColumnIsVisible(final int columnIndex)
	{
		this.support.ensureColumnIsVisible(columnIndex);
	}
	
	
	/**
	 * Creates a Virtual Table from the currently displayed values of this
	 * supported table.
	 * <p>
	 * The generated Virtual Table has the same order of columns and rows as
	 * currently displayed in the view.
	 * </p>
	 * <p>
	 * The supported table must have a referenced VirtualTable
	 * {@link VirtualTableModel} for this method to work)
	 * </p>
	 *
	 * @return a {@link VirtualTable} representing the currently displayed
	 *         results of the table
	 * @throws IllegalStateException
	 *             if the supported table is not based on a VirtualTableModel or
	 *             if a new VirtualTable could not be created.
	 */
	public VirtualTable createSubsettedVirtualTable()
	{
		return this.support.createSubsettedVirtualTable();
	}
	
	
	/**
	 * Returns the current selection mode.
	 *
	 * @return the current selection mode
	 * @see #setSelectionMode(int)
	 */
	public int getSelectionMode()
	{
		return getSelectionModel().getSelectionMode();
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
