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


import java.awt.Component;
import java.awt.Point;
import java.awt.Rectangle;
import java.beans.Beans;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JViewport;
import javax.swing.ListSelectionModel;
import javax.swing.RowSorter;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import xdev.db.QueryInfo;
import xdev.db.locking.HybridVirtualTableLock;
import xdev.db.sql.Condition;
import xdev.db.sql.SELECT;
import xdev.db.sql.WHERE;
import xdev.io.csv.CSVWriter;
import xdev.ui.FormularComponent.ValueChangeListener;
import xdev.ui.MasterDetailComponent.DetailHandler;
import xdev.ui.paging.LazyLoadable;
import xdev.ui.paging.LazyLoadingTableModel;
import xdev.ui.paging.Pageable;
import xdev.ui.paging.TablePageControl;
import xdev.ui.table.DefaultTableColumnConverter;
import xdev.ui.table.DefaultTableModelWrapperLookup;
import xdev.ui.table.DefaultTableRowConverter;
import xdev.ui.table.ExtendedTable;
import xdev.ui.table.TableColumnConverter;
import xdev.ui.table.TableModelWrapperLookup;
import xdev.ui.table.TableRowConverter;
import xdev.ui.table.TableRowSelectionHandler;
import xdev.util.ArrayUtils;
import xdev.util.IntList;
import xdev.util.StringUtils;
import xdev.util.logging.LoggerFactory;
import xdev.util.logging.XdevLogger;
import xdev.vt.EntityRelationship;
import xdev.vt.EntityRelationship.Entity;
import xdev.vt.EntityRelationships;
import xdev.vt.KeyValues;
import xdev.vt.VirtualTable;
import xdev.vt.VirtualTable.VirtualTableRow;
import xdev.vt.VirtualTableColumn;
import xdev.vt.VirtualTableException;
import xdev.vt.VirtualTableModel;
import xdev.vt.VirtualTableWrapper;


/**
 * This class provides common functionality used by all table types of the XAPI.
 *
 * @param <T>
 *            type table
 * @author XDEV Software Corp.
 */
public class TableSupport<T extends JTable & MasterDetailComponent<T>>
		extends FormularComponentSupport<T>
{
	/**
	 * Logger instance for this class.
	 */
	private static final XdevLogger			log	= LoggerFactory.getLogger(TableSupport.class);

	private static TableRowConverter		tableRowConverter;
	private static TableColumnConverter		tableColumnConverter;
	private static TableModelWrapperLookup	tableModelWrapperLookup;
	private RowSelectionHandler				rowSelectionHandler;


	public void setRowSelectionHandler(final RowSelectionHandler rowSelectionHandler)
	{
		this.rowSelectionHandler = rowSelectionHandler;
	}


	public RowSelectionHandler getRowSelectionHandler()
	{
		if(this.rowSelectionHandler == null)
		{
			this.rowSelectionHandler = new TableRowSelectionHandler(this.component);
		}

		return this.rowSelectionHandler;
	}


	/**
	 * Sets a {@link TableRowConverter} to differ model data from view data.
	 *
	 * @since 3.1
	 */
	public static void setTableRowConverter(final TableRowConverter tableRowConverter)
	{
		TableSupport.tableRowConverter = tableRowConverter;
	}


	/**
	 * Returns the set {@link TableRowConverter} to differ model data from view
	 * data. Or if no {@link TableRowConverter} was set it returns
	 * {@link TableRowConverter} default implementation.
	 *
	 * * @return the set {@link TableRowConverter} or its default
	 * implementation.
	 *
	 * @since 3.1
	 */
	public static TableRowConverter getTableRowConverter()
	{
		if(tableRowConverter == null)
		{
			tableRowConverter = new DefaultTableRowConverter();
		}

		return tableRowConverter;
	}


	/**
	 * Sets a {@link TableRowConverter} to differ model data from view data.
	 *
	 * @since 3.2
	 */
	public static void setTableColumnConverter(final TableColumnConverter tableColumnConverter)
	{
		TableSupport.tableColumnConverter = tableColumnConverter;
	}


	/**
	 * Returns the set {@link TableColumnConverter} to differ model data from
	 * view data. Or if no {@link TableColumnConverter} was set it returns
	 * {@link TableColumnConverter} default implementation.
	 *
	 * @return the set {@link TableColumnConverter} or its default
	 *         implementation.
	 *
	 * @since 3.2
	 */
	public static TableColumnConverter getTableColumnConverter()
	{
		if(tableColumnConverter == null)
		{
			tableColumnConverter = new DefaultTableColumnConverter();
		}

		return tableColumnConverter;
	}


	/**
	 *
	 * @since 3.1
	 */
	public static void setTableModelWrapperLookup(
			final TableModelWrapperLookup tableModelWrapperLookup)
	{
		TableSupport.tableModelWrapperLookup = tableModelWrapperLookup;
	}


	/**
	 *
	 * @since 3.1
	 */
	public static TableModelWrapperLookup getTableModelWrapperLookup()
	{
		if(tableModelWrapperLookup == null)
		{
			tableModelWrapperLookup = new DefaultTableModelWrapperLookup();
		}

		return tableModelWrapperLookup;
	}

	protected int[]						savedValue				= new int[0];
	protected DetailHandler				detailHandler;
	protected String					modelColumns;

	// @since 4.0
	protected boolean					pagingEnabled			= false;
	private TablePageControl<T>			pageControl				= null;
	protected boolean					lazyLoadingEnabled		= false;
	protected LazyLoadingTableModel<T>	lazyLoadingTableModel	= null;

	protected boolean					autoLock				= false;
	protected XdevWindow				lockInUseDialog			= null;
	// protected RenewLockWindow renewDialog = new RenewLockWindowImpl();
	protected long						autoRenewLockTime		= 0;
	protected long						renewDialogTime			= 10000;
	protected long						lockingTime				= HybridVirtualTableLock.DEFAULT_TIMEOUT;

	protected boolean					showDialog				= false;
	protected JComponent				monitorCountdown		= null;

	protected List<JComponent>			countdownMonitorComponentList;


	protected TableSupport(final T table)
	{
		super(table);
		this.countdownMonitorComponentList = new ArrayList<JComponent>();
	}


	// *****************************************************
	// Common Support
	// *****************************************************
	/**
	 * Sets the model for the supported table based on a {@link VirtualTable}.
	 *
	 * @param vt
	 *            a {@link VirtualTable}.
	 */
	public void setModel(final VirtualTable vt)
	{
		setModel(vt,"*",false);
		this.countdownMonitorComponentList = new ArrayList<JComponent>();
	}


	/**
	 * Sets the model for the supported table based on a {@link VirtualTable}
	 * and additional configuration.
	 *
	 * @param vt
	 *            a {@link VirtualTable}.
	 * @param columns
	 *            a comma separated list of column names.
	 * @param queryData
	 *            if set to {@code true} the {@code vt} will be queried with the
	 *            default select for the {@code vt}.
	 */
	public void setModel(final VirtualTable vt, final String columns, final boolean queryData)
	{
		setModel(vt,columns,queryData,false);
	}


	/**
	 * Sets the model for the supported table based on a {@link VirtualTable}
	 * and additional configuration.
	 *
	 * @param vt
	 *            a {@link VirtualTable}.
	 * @param columns
	 *            a comma separated list of column names.
	 * @param queryData
	 *            if set to {@code true} the {@code vt} will be queried with the
	 *            default select for the {@code vt}.
	 * @param selectiveQuery
	 *            if {@code true}, only the display <code>columns</code> are
	 *            queried
	 * @since 5.0
	 */
	public void setModel(final VirtualTable vt, final String columns, final boolean queryData,
			final boolean selectiveQuery)
	{
		SELECT select = null;
		if(queryData)
		{
			select = createSelect(vt,selectiveQuery,columns);
		}

		setModel(vt,columns,select);
	}


	/**
	 * @since 5.0
	 */
	public static SELECT createSelect(final VirtualTable vt, final boolean selectiveQuery,
			final String... columns)
	{
		if(selectiveQuery)
		{
			final Set<String> columnNames = new LinkedHashSet<>();

			for(final String c : columns)
			{
				if(c != null && c.length() > 0)
				{
					Arrays.stream(c.split(",")).map(String::trim).forEach(columnNames::add);
				}
			}

			// always query primary key columns
			for(final VirtualTableColumn<?> pkColumn : vt.getPrimaryKeyColumns())
			{
				columnNames.add(pkColumn.getName());
			}

			return vt.getSelect(vt.getColumns(columnNames.toArray(new String[columnNames.size()])));
		}

		return vt.getSelect();
	}


	/**
	 * Sets the model for the supported table based on a {@link VirtualTable}
	 * and additional configuration.
	 *
	 * @param vt
	 *            a {@link VirtualTable}.
	 * @param columns
	 *            a comma separated list of column names.
	 * @param select
	 *            a custom {@link SELECT} used to query the {@link VirtualTable}
	 *            .
	 * @param params
	 *            a number of parameters for the custom select
	 */
	public void setModel(final VirtualTable vt, final String columns, final SELECT select,
			final Object... params)
	{
		this.modelColumns = columns.trim();

		if(this.lazyLoadingEnabled && !this.pagingEnabled && select != null)
		{
			int[] indices;
			if(this.modelColumns.equals("*"))
			{
				indices = vt.getVisibleColumnIndices();
			}
			else
			{
				final List<String> columnNames = new ArrayList();
				final StringTokenizer st = new StringTokenizer(this.modelColumns,",");
				while(st.hasMoreTokens())
				{
					columnNames.add(st.nextToken().trim());
				}
				final IntList list = new IntList();
				for(final String name : columnNames)
				{
					final int columnIndex = vt.getColumnIndex(name);
					if(columnIndex >= 0)
					{
						list.add(columnIndex);
					}
				}
				indices = list.toArray();
			}
			final LazyLoadingTableModel model = getLazyLoadingTableModel();
			model.setData(vt,indices,select,params);
			this.component.setModel(model);
			this.component.clearSelection();
		}
		else
		{
			if(this.modelColumns.equals("*"))
			{
				this.component.setModel(vt.createTableModel());
			}
			else if(this.modelColumns.length() > 0)
			{
				final List<String> columnNames = new ArrayList();
				final StringTokenizer st = new StringTokenizer(this.modelColumns,",");
				while(st.hasMoreTokens())
				{
					columnNames.add(st.nextToken().trim());
				}
				this.component.setModel(vt.createTableModel(columnNames));
			}

			this.component.clearSelection();

			if(select != null)
			{
				try
				{
					if(this.pagingEnabled)
					{
						getPageControl().changeModel(select,params,0);
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
		}
	}


	/**
	 * @since 4.0
	 */
	public void setPagingEnabled(final boolean pagingEnabled)
	{
		if(pagingEnabled && !(this.component instanceof Pageable))
		{
			throw new IllegalStateException("the table is not pageable");
		}

		this.pagingEnabled = pagingEnabled;

		if(!Beans.isDesignTime())
		{
			if(pagingEnabled)
			{
				this.component.refresh();
			}
		}
	}


	/**
	 * @since 4.0
	 */
	public boolean isPagingEnabled()
	{
		return this.pagingEnabled;
	}


	/**
	 * @since 4.0
	 */
	public TablePageControl<T> getPageControl()
	{
		if(this.pageControl == null)
		{
			this.pageControl = new TablePageControl(this.component);
		}

		return this.pageControl;
	}


	/**
	 * @since 4.0
	 */
	public void setLazyLoadingEnabled(final boolean lazyLoadingEnabled)
	{
		if(lazyLoadingEnabled && !(this.component instanceof LazyLoadable))
		{
			throw new IllegalStateException("the table is not lazy loadable");
		}

		this.lazyLoadingEnabled = lazyLoadingEnabled;
	}


	/**
	 * @since 4.0
	 */
	public boolean isLazyLoadingEnabled()
	{
		return this.lazyLoadingEnabled;
	}


	protected LazyLoadingTableModel<T> getLazyLoadingTableModel()
	{
		if(this.lazyLoadingTableModel == null)
		{
			this.lazyLoadingTableModel = new LazyLoadingTableModel<T>(this.component);
		}

		return this.lazyLoadingTableModel;
	}


	/**
	 * @since 4.0
	 */
	public RowSorter createRowSorter(final TableModel model)
	{
		if(this.pagingEnabled)
		{
			return getPageControl().createRowSorter(model);
		}
		else if(this.lazyLoadingEnabled)
		{
			return getLazyLoadingTableModel().createRowSorter(model);
		}

		return new TableRowSorter(model);
	}


	/**
	 * Sets the column width of each column to its preferred size.
	 */
	public void adjustColumnWidths()
	{
		final VirtualTableWrapper wrapper = getVirtualTableWrapper();
		if(wrapper == null)
		{
			return;
		}
		final VirtualTable vt = getVirtualTable();
		if(vt == null)
		{
			return;
		}

		final int cc = this.component.getModel().getColumnCount();
		for(int c = 0; c < cc; c++)
		{
			final TableColumn column = this.component.getColumnModel().getColumn(c);

			int modelColumnIndex = getTableColumnConverter().viewToModel(this.component,
					column.getModelIndex());
			modelColumnIndex = wrapper.viewToModelColumn(modelColumnIndex);

			// if the column could be found in the virtualTableModel ->
			// it can be resized
			if(modelColumnIndex != TableColumnConverter.NOT_IN_ORIGINAL_MODEL)
			{
				final int width = vt.getPreferredColWidth(modelColumnIndex,
						this.component.getTableHeader(),c);
				column.setPreferredWidth(width);
				column.setWidth(width);

				// min and max width support, added in 4.0
				final VirtualTableColumn vtColumn = vt.getColumnAt(modelColumnIndex);
				final int minWidth = vtColumn.getMinWidth();
				if(minWidth > 0)
				{
					column.setMinWidth(minWidth);
				}
				final int maxWidth = vtColumn.getMaxWidth();
				if(maxWidth > 0)
				{
					column.setMaxWidth(maxWidth);
				}
			}
		}
	}


	/**
	 * @see MasterDetailComponent#getVirtualTable()
	 */
	public VirtualTable getVirtualTable()
	{
		final VirtualTableWrapper wrapper = getVirtualTableWrapper();
		if(wrapper != null)
		{
			return wrapper.getVirtualTable();
		}

		return null;
	}


	/**
	 * Returns the underlying model which is a {@link VirtualTableWrapper}, or
	 * <code>null</code>.
	 *
	 * @return the {@link VirtualTableWrapper} model
	 */
	public final VirtualTableWrapper getVirtualTableWrapper()
	{
		final TableModel model = getTableModelWrapperLookup()
				.lookupTableModel(this.component.getModel(),VirtualTableWrapper.class);
		if(model instanceof VirtualTableWrapper)
		{
			return (VirtualTableWrapper)model;
		}

		return null;
	}


	/**
	 * @see ExtendedTable#getRowAtPoint(Point)
	 */
	public int getRowAtPoint(final Point location) throws IndexOutOfBoundsException
	{
		final int row = this.component.rowAtPoint(location);
		return row;
	}


	// *****************************************************
	// Selection Support
	// *****************************************************

	/**
	 * @see ExtendedTable#getSelectedModelRow()
	 */
	public final int getSelectedModelRow() throws IndexOutOfBoundsException
	{
		int row = this.component.getSelectedRow();
		if(row != -1)
		{
			row = getTableRowConverter().viewToModel(this.component,row);
		}
		return row;
	}


	/**
	 * @see ExtendedTable#getSelectedModelRows()
	 */
	public final int[] getSelectedModelRows() throws IndexOutOfBoundsException
	{
		final IntList list = new IntList();

		final int[] si = this.component.getSelectedRows();
		if(si != null)
		{
			for(int i = 0; i < si.length; i++)
			{
				si[i] = getTableRowConverter().viewToModel(this.component,si[i]);
				if(si[i] != -1)
				{
					list.add(si[i]);
				}
			}
		}

		return list.toArray();
	}


	/**
	 * @see ExtendedTable#setSelectedRows(int[])
	 */
	public void setSelectedRows(final int[] indices)
	{
		this.getRowSelectionHandler().selectRows(indices);
	}


	/**
	 * @see ExtendedTable#setSelectedModelRow(int)
	 */
	public final void setSelectedModelRow(int row)
			throws IndexOutOfBoundsException, IllegalArgumentException
	{
		if(row < 0)
		{
			this.component.clearSelection();
		}
		else
		{
			row = getTableRowConverter().modelToView(this.component,row);
			if(row != -1)
			{
				this.getRowSelectionHandler().selectRow(row);
			}
		}
	}


	/**
	 * @see ExtendedTable#setSelectedModelRows(int[])
	 */
	public final void setSelectedModelRows(final int[] rows)
			throws IndexOutOfBoundsException, IllegalArgumentException
	{
		if(rows == null)
		{
			this.component.clearSelection();
		}
		else
		{
			final IntList viewIndices = new IntList();
			for(int i = 0; i < rows.length; i++)
			{
				final int viewIndex = getTableRowConverter().modelToView(this.component,rows[i]);
				if(viewIndex != -1)
				{
					viewIndices.add(viewIndex);
				}
			}
			this.getRowSelectionHandler().selectRows(viewIndices.toArray());
		}
	}


	/**
	 * @see ExtendedTable#setSelectedModelRows(int, int)
	 */
	public final void setSelectedModelRows(final int start, final int end)
	{
		this.getRowSelectionHandler().selectRowInterval(
				getTableRowConverter().modelToView(this.component,start),
				getTableRowConverter().modelToView(this.component,end));
	}


	// *****************************************************
	// FormularComponent Support
	// *****************************************************

	/**
	 * @see FormularComponent#isMultiSelect()
	 */
	public boolean isMultiSelect()
	{
		return this.component.getSelectionModel()
				.getSelectionMode() != ListSelectionModel.SINGLE_SELECTION;
	}


	/**
	 * @see FormularComponent#setFormularValue(VirtualTable, Map)
	 */
	public void setFormularValue(final VirtualTable vt, final Map<String, Object> record)
	{
		if(!hasDataField())
		{
			return;
		}

		this.component.clearSelection();
		setValue(vt,getValuesForDataFields(record));
	}


	public void setValue(final VirtualTable vt, final Map<String, Object> value)
	{
		final VirtualTable tableVT = this.component.getVirtualTable();
		selectFormularValue(vt,tableVT,value);
		if(this.component.getSelectedRowCount() == 0 && this.detailHandler != null)
		{
			this.detailHandler.checkDetailView(value);
			selectFormularValue(vt,tableVT,value);
		}
	}


	protected void selectFormularValue(final VirtualTable formularVT, final VirtualTable tableVT,
			final Map<String, Object> values)
	{
		if(values.size() > 0)
		{
			final KeyValues keyValues = getKeyValues(formularVT,tableVT,values);
			final int rowCount = this.component.getModel().getRowCount();
			for(int rowVt = 0; rowVt < rowCount; rowVt++)
			{
				if(keyValues.equals(tableVT.getRow(rowVt)))
				{
					setSelectedModelRow(rowVt);
					break;
				}
			}
		}
	}


	protected KeyValues getKeyValues(final VirtualTable formularVT, final VirtualTable tableVT,
			final Map<String, Object> values) throws MasterDetailException, VirtualTableException
	{
		final VirtualTableColumn[] pkColumns = tableVT.getPrimaryKeyColumns();
		final int pkColumnCount = pkColumns.length;
		if(pkColumnCount == 0)
		{
			throw new MasterDetailException(formularVT,tableVT,
					"no primary key defined in '" + formularVT.getName() + "'");
		}

		if(pkColumnCount != values.size())
		{
			throw new IllegalArgumentException(
					"value count " + values.size() + " <> " + pkColumnCount + " key column count");
		}

		final String[] pkColumnNames = VirtualTableColumn.getNamesOf(pkColumns);

		if(formularVT.getName().equals(tableVT.getName()))
		{
			return new KeyValues(tableVT,values);
		}

		final EntityRelationship relation = EntityRelationships.getModel()
				.getRelationship(tableVT.getName(),pkColumnNames,formularVT.getName());
		if(relation == null)
		{
			throw new MasterDetailException(formularVT,tableVT,
					"no foreign key for " + formularVT.getName() + "("
							+ StringUtils.concat(",",(Object[])pkColumnNames) + ") found in '"
							+ tableVT.getName() + "'");
		}

		final Entity formularEntity = relation.getReferrer(formularVT.getName());
		final Entity tableEntity = relation.getOther(formularEntity);
		final VirtualTableColumn[] tableColumns = tableVT.getColumns(tableEntity.getColumnNames());
		final Map<String, Object> map = new HashMap();
		final Object[] valueObject = map.values().toArray();
		for(int i = 0; i < tableColumns.length; i++)
		{
			if(tableColumns[i] == null)
			{
				VirtualTableException.throwColumnNotFound(tableVT,tableEntity.getColumnName(i));
			}

			map.put(tableColumns[i].getName(),valueObject[i]);
		}

		return new KeyValues(tableVT,map);
	}


	/**
	 * @see FormularComponent#getFormularValue()
	 */
	public Object getFormularValue()
	{
		final VirtualTableRow vtRow = getSelectedVirtualTableRow();
		if(vtRow != null)
		{
			final VirtualTableColumn[] cols = vtRow.getVirtualTable().getPrimaryKeyColumns();
			if(cols.length > 0)
			{
				if(cols.length == 1)
				{
					return vtRow.get(cols[0]);
				}
				else
				{
					final Object[] values = new Object[cols.length];
					for(int i = 0; i < cols.length; i++)
					{
						values[i] = vtRow.get(cols[i]);
					}
					return values;
				}
			}
		}

		return null;
	}


	/**
	 * @see FormularComponent#saveState()
	 */
	public void saveState()
	{
		this.savedValue = this.component.getSelectedRows();
	}


	/**
	 * @see FormularComponent#restoreState()
	 */
	public void restoreState()
	{
		setSelectedRows(this.savedValue);
	}


	/**
	 * @see FormularComponent#hasStateChanged()
	 *
	 * @since 3.1
	 */
	public boolean hasStateChanged()
	{
		return !Arrays.equals(this.savedValue,this.component.getSelectedRows());
	}


	// *****************************************************
	// MasterDetailComponent Support
	// *****************************************************

	/**
	 * @see ExtendedTable#getSelectedVirtualTableRow()
	 */
	public VirtualTableRow getSelectedVirtualTableRow()
	{
		final int modelIndex = getSelectedModelRow();
		if(modelIndex >= 0)
		{
			final VirtualTableWrapper wrapper = getVirtualTableWrapper();
			if(wrapper != null)
			{
				return wrapper.getVirtualTableRow(modelIndex);
			}

			final VirtualTable vt = this.component.getVirtualTable();
			if(vt != null)
			{
				return vt.getRow(modelIndex);
			}
		}

		return null;
	}


	/**
	 * @see ExtendedTable#setSelectedVirtualTableRow(VirtualTableRow)
	 * @since 3.2
	 */
	public void setSelectedVirtualTableRow(final VirtualTableRow row)
	{
		if(row == null)
		{
			this.component.clearSelection();
		}
		else
		{
			final VirtualTableWrapper wrapper = getVirtualTableWrapper();
			if(wrapper != null)
			{
				final int rc = wrapper.getRowCount();
				for(int i = 0; i < rc; i++)
				{
					if(wrapper.getVirtualTableRow(i).equals(row))
					{
						setSelectedModelRow(i);
						break;
					}
				}
			}
		}
	}


	/**
	 * @see ExtendedTable#getSelectedVirtualTableRows()
	 */
	public VirtualTableRow[] getSelectedVirtualTableRows()
	{
		final int indices[] = getSelectedModelRows();
		final int c = indices.length;
		final VirtualTableRow[] rows = new VirtualTableRow[c];
		if(c > 0)
		{
			final VirtualTableWrapper wrapper = getVirtualTableWrapper();
			if(wrapper != null)
			{
				for(int i = 0; i < c; i++)
				{
					rows[i] = wrapper.getVirtualTableRow(indices[i]);
				}
			}
			else
			{
				final VirtualTable vt = this.component.getVirtualTable();
				if(vt != null)
				{
					for(int i = 0; i < c; i++)
					{
						rows[i] = vt.getRow(indices[i]);
					}
				}
			}
		}

		return rows;
	}


	/**
	 * @see ExtendedTable#setSelectedVirtualTableRows(VirtualTableRow[])
	 * @since 3.2
	 */
	public void setSelectedVirtualTableRows(final VirtualTableRow[] rows)
	{
		if(rows == null || rows.length == 0)
		{
			this.component.clearSelection();
		}
		else
		{
			final VirtualTableWrapper wrapper = getVirtualTableWrapper();
			if(wrapper != null)
			{
				final IntList rowIndices = new IntList();
				final int rc = wrapper.getRowCount();
				for(int i = 0; i < rc; i++)
				{
					final VirtualTableRow wrapperRow = wrapper.getVirtualTableRow(i);
					for(final VirtualTableRow row : rows)
					{
						if(wrapperRow.equals(row))
						{
							rowIndices.add(i);
							break;
						}
					}
				}
				setSelectedModelRows(rowIndices.toArray());
			}
		}
	}


	/**
	 * @see MasterDetailComponent#refresh()
	 */
	public void refresh()
	{
		final VirtualTable vt = this.component.getVirtualTable();
		if(vt != null)
		{
			try
			{
				final int[] selectedRows = this.component.getSelectedRows();

				if(this.pagingEnabled)
				{
					getPageControl().refresh();
				}
				else if(this.lazyLoadingEnabled)
				{
					getLazyLoadingTableModel().refresh();
				}
				else if(vt.getLastQuery() != null)
				{
					vt.reload();
				}
				else
				{
					vt.queryAndFill();
				}

				setSelectedRows(selectedRows);
			}
			catch(final Exception e)
			{
				log.error(e);
			}
		}
	}


	/**
	 * @see MasterDetailComponent#updateModel(Condition, Object...)
	 */
	public void updateModel(final Condition condition, Object... params)
	{
		this.component.clearSelection();
		final VirtualTable vt = this.component.getVirtualTable();
		if(vt != null && this.modelColumns != null)
		{
			final QueryInfo query = vt.getLastQuery();
			final QueryInfo queryClone = query != null ? query.clone() : null;
			final SELECT select;
			if(query != null)
			{
				select = query.getSelect();

				final Object[] lastParams = query.getParameters();
				if(lastParams.length > 0)
				{
					params = ArrayUtils.concat(Object.class,lastParams,params);
				}
			}
			else
			{
				select = vt.getSelect();
			}

			if(condition != null)
			{
				WHERE where = select.getWhere();
				if(where != null && !where.isEmpty())
				{
					where = where.encloseWithPars().and(condition);
				}
				else
				{
					where = new WHERE(condition);
				}

				select.WHERE(where);
			}

			setModel(vt,this.modelColumns,select,params);
			vt.setLastQuery(queryClone);
		}
	}


	/**
	 * @see MasterDetailComponent#clearModel()
	 */
	public void clearModel()
	{
		if(this.lazyLoadingEnabled && this.lazyLoadingTableModel != null)
		{
			this.lazyLoadingTableModel.clear();
		}
		else
		{
			final VirtualTable vt = this.component.getVirtualTable();
			if(vt != null)
			{
				vt.clear();
			}
		}
	}


	/**
	 * @see MasterDetailComponent#addValueChangeListener(ValueChangeListener)
	 */
	public void addValueChangeListener(final ValueChangeListener l)
	{
		this.component.getSelectionModel().addListSelectionListener(new ListSelectionListener()
		{
			@Override
			public void valueChanged(final ListSelectionEvent e)
			{
				if(!e.getValueIsAdjusting())
				{
					l.valueChanged(e);
				}
			}
		});
	}


	/**
	 * @see MasterDetailComponent#setDetailHandler(DetailHandler)
	 */
	public void setDetailHandler(final DetailHandler detailHandler)
	{
		this.detailHandler = detailHandler;
	}


	/**
	 * Exports data of this {@link XdevTable} as CSV information to a specified
	 * {@link Writer}.
	 * <p>
	 * This is an alias for {@link #exportCSV(Writer, char)}.
	 * </p>
	 *
	 * @param writer
	 *            the {@link Writer} to write CSV information to
	 *
	 *
	 * @throws IOException
	 *             if CSV file couldn't be written
	 */
	public void exportCSV(final Writer writer) throws IOException
	{
		exportCSV(writer,',');
	}


	/**
	 * Exports data of this {@link XdevTable} as CSV information to a specified
	 * {@link Writer}.
	 * <p>
	 * This is an alias for {@link #exportCSV(Writer, char, boolean)}.
	 * </p>
	 *
	 * @param writer
	 *            the {@link Writer} to write CSV information to
	 *
	 * @param delimiter
	 *            the delimiter to use for separating entries.
	 *
	 * @throws IOException
	 *             if CSV file couldn't be written
	 */
	public void exportCSV(final Writer writer, final char delimiter) throws IOException
	{
		exportCSV(writer,delimiter,true);
	}


	/**
	 * Exports data of this {@link XdevTable} as CSV information to a specified
	 * {@link Writer}.
	 *
	 * @param writer
	 *            the {@link Writer} to write CSV information to
	 *
	 * @param delimiter
	 *            the delimiter to use for separating entries.
	 *
	 * @param withColumnNames
	 *            a flag to control whether the columns are written
	 *
	 * @throws IOException
	 *             if CSV file couldn't be written
	 */
	public synchronized void exportCSV(final Writer writer, final char delimiter,
			final boolean withColumnNames) throws IOException
	{
		// caller must close the param writer
		@SuppressWarnings("resource")
		final CSVWriter csvWriter = new CSVWriter(writer,delimiter);

		final int columnCount = this.component.getColumnCount();
		final int[] viewColumns = new int[columnCount];
		for(int col = 0; col < columnCount; col++)
		{
			viewColumns[col] = col;// convertColumnIndexToView(col);
		}
		final String[] line = new String[columnCount];

		final int rowCount = this.component.getRowCount();

		if(withColumnNames)
		{
			final TableColumnModel columnModel = this.component.getColumnModel();
			for(int col = 0; col < columnCount; col++)
			{
				final int viewCol = viewColumns[col];
				line[col] = exportCSV_getColumnName(columnModel.getColumn(viewCol));
			}
			csvWriter.writeNext(line);
		}

		for(int row = 0; row < rowCount; row++)
		{
			final int viewRow = row;// convertRowIndexToView(row);
			for(int col = 0; col < columnCount; col++)
			{
				final int viewCol = viewColumns[col];
				line[col] = exportCSV_getValue(line,viewRow,viewCol);
			}
			csvWriter.writeNext(line);
		}
	}


	protected String exportCSV_getColumnName(final TableColumn column)
	{
		return String.valueOf(column.getHeaderValue());
	}


	protected String exportCSV_getValue(final Object value, final int row, final int col)
	{
		final Component cpn = this.component.getCellRenderer(row,col)
				.getTableCellRendererComponent(this.component,value,false,false,row,col);
		if(cpn instanceof JLabel)
		{
			return ((JLabel)cpn).getText();
		}

		return "";
	}


	/**
	 * Return <code>true</code> if something is selected, otherwise
	 * <code>false</code>.
	 *
	 * @return <code>true</code> if something is selected, otherwise
	 *         <code>false</code>
	 */
	public boolean isSomethingSelected()
	{
		return this.component.getSelectedRow() >= 0;
	}


	/**
	 * Sets the columns <code>preferredWidth</code> and the columns
	 * <code>width</code>.
	 *
	 * @param index
	 *            the index of the desired column
	 * @param width
	 *            the value for the column width
	 *
	 * @see TableColumn#setPreferredWidth(int)
	 * @see TableColumn#setWidth(int)
	 */
	public void setColumnWidth(final int index, final int width)
	{
		final TableColumn tc = this.component.getColumnModel().getColumn(index);
		tc.setPreferredWidth(width);
		tc.setWidth(width);
	}


	/**
	 * Sets the title of the desired column with the <code>index</code>.
	 *
	 * @param index
	 *            the index of the desired column
	 * @param title
	 *            the value for the column title
	 *
	 * @see TableColumn#setHeaderValue(Object)
	 */
	public void setColumnTitle(final int index, final String title)
	{
		final TableColumn tc = this.component.getColumnModel().getColumn(index);
		tc.setHeaderValue(title);
	}


	/**
	 *
	 * Returns the index of the row that x- and y-coordinate lies in. Maps the
	 * index of the row in terms of the view to the underlying
	 * {@link TableModel}.
	 *
	 * @param x
	 *            the <i>x</i> coordinate of the row
	 * @param y
	 *            the <i>y</i> coordinate of the row
	 *
	 * @return the index of the corresponding row in the model
	 *
	 * @throws IndexOutOfBoundsException
	 *             if sorting is enabled and passed an index outside the range
	 *             of the {@link JTable} as determined by the method
	 *             {@link JTable#getRowCount()}
	 *
	 * @see #getRowAtPoint(Point)
	 * @see JTable#convertRowIndexToModel(int)
	 */
	public int getRowAtPoint(final int x, final int y) throws IndexOutOfBoundsException
	{
		return getRowAtPoint(new Point(x,y));
	}


	/**
	 * Returns the index of the column that x- and y-coordinate lies in or -1 if
	 * the result is not in the range [0, {@link JTable#getColumnCount()} -1].
	 *
	 * @param x
	 *            the <i>x</i> coordinate of the column
	 * @param y
	 *            the <i>y</i> coordinate of the column
	 * @return the index of the column that x- and y-coordinate lies in or -1 if
	 *         the result is not in the range [0,
	 *         {@link JTable#getColumnCount()} -1]
	 *
	 * @see #getColumnAtPoint(Point)
	 */
	public int getColumnAtPoint(final int x, final int y)
	{
		return getColumnAtPoint(new Point(x,y));
	}


	/**
	 * This method is a alias for {@link JTable#columnAtPoint(Point)}.
	 *
	 * @param location
	 *            of interest
	 *
	 * @return the index of the column that <code>location</code> lies in or -1
	 *         if the result is not in the range [0,
	 *         {@link JTable#getColumnCount()} -1]
	 *
	 * @see #getColumnAtPoint(int, int)
	 */
	public int getColumnAtPoint(final Point location)
	{
		return this.component.columnAtPoint(location);
	}


	/**
	 * Scrolls to the desired cell. After the call the desired cell is visible
	 * in the viewport.
	 *
	 * <p>
	 * <strong>Note:</strong> Works only if the table is contained in a
	 * {@link JScrollPane}.
	 * </p>
	 *
	 * @param rowIndex
	 *            index of row to scroll to
	 * @param columnIndex
	 *            index of the column to scroll to
	 */
	public void ensureCellIsVisible(final int rowIndex, final int columnIndex)
	{
		if(!(this.component.getParent() instanceof JViewport))
		{
			// no scroller - no scrolling!
			return;
		}

		final JViewport viewport = (JViewport)this.component.getParent();

		// calculate rect that should be displayed
		final Rectangle rect = this.component.getCellRect(rowIndex,columnIndex,true);
		final Point point = viewport.getViewPosition();
		rect.setLocation(rect.x - point.x,rect.y - point.y);

		// scroll!
		viewport.scrollRectToVisible(rect);
	}


	/**
	 * Scrolls to the desired row. After the call the desired row is visible in
	 * the viewport. The selected column will be preserved during the process.
	 *
	 * <p>
	 * <strong>Note:</strong> Works only if the table is contained in a
	 * {@link JScrollPane}.
	 * </p>
	 *
	 * @param rowIndex
	 *            index of row to scroll to
	 */
	public void ensureRowIsVisible(final int rowIndex)
	{
		this.ensureCellIsVisible(rowIndex,this.component.getSelectedColumn());
	}


	/**
	 * Scrolls to the desired column. After the call the desired column is
	 * visible in the viewport. The selected row will be preserved during the
	 * process.
	 *
	 * <p>
	 * <strong>Note:</strong> Works only if the table is contained in a
	 * {@link JScrollPane}.
	 * </p>
	 *
	 * @param columnIndex
	 *            index of the column to scroll to
	 */
	public void ensureColumnIsVisible(final int columnIndex)
	{
		this.ensureCellIsVisible(this.component.getSelectedRow(),columnIndex);
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
		final VirtualTableWrapper wrapper = getVirtualTableWrapper();
		if(wrapper == null)
		{
			throw new IllegalStateException("There is no VirtualTableModel for this table.");
		}

		final VirtualTable modelVT = wrapper.getVirtualTable();

		final VirtualTable subsettedVt = createSubsettedVT(modelVT);

		fillSubsettedVT(modelVT,subsettedVt);

		return subsettedVt;
	}


	/**
	 * Creates a subsetted Virtual Table.
	 * <p>
	 * A new Virtual Table is created, that contains only those columns that are
	 * currently visible in the table.
	 * </p>
	 *
	 * @param modelVT
	 *            the Virtual Table representing the model for reading column
	 *            information
	 * @return
	 */
	private VirtualTable createSubsettedVT(final VirtualTable modelVT)
	{
		final int columnCount = this.component.getColumnCount();
		final List<String> visibleColumnNames = getVisibleColumnNames();
		@SuppressWarnings("rawtypes")
		final VirtualTableColumn[] vtColumns = new VirtualTableColumn[columnCount];

		for(int j = 0; j < columnCount; j++)
		{
			VirtualTableColumn<?> modelColumn = null;
			for(final VirtualTableColumn<?> column : modelVT)
			{
				if(column.getName().equals(visibleColumnNames.get(j)))
				{
					modelColumn = column;
					break;
				}
			}
			vtColumns[j] = modelColumn.clone();
		}
		final VirtualTable subsettedVt = new VirtualTable(modelVT.getName(),
				modelVT.getDatabaseAlias(),vtColumns);
		return subsettedVt;
	}


	/**
	 * Fills the values of the subsetted Virtual Table with values from the
	 * models Virtual Table.
	 * <p>
	 * Only values are filled in that are currently visible in the table. The
	 * order of rows and columns equals the displayed order.
	 * </p>
	 *
	 * @param modelVT
	 *            the underlying Virtual Table representing the model for
	 *            reading the values
	 * @param subsettedVT
	 *            the subsetted Virtual Table representling the view for writing
	 *            the values
	 */
	private void fillSubsettedVT(final VirtualTable modelVT, final VirtualTable subsettedVt)
	{
		final int rowCount = this.component.getModel().getRowCount();
		final List<String> visibleColumnNames = getVisibleColumnNames();

		for(int row = 0; row < rowCount; row++)
		{
			final List<Object> values = new ArrayList<Object>();
			for(int col = 0; col < this.component.getColumnCount(); col++)
			{
				VirtualTableColumn<?> modelColumn = null;
				for(final VirtualTableColumn<?> column : modelVT)
				{
					if(column.getName().equals(visibleColumnNames.get(col)))
					{
						modelColumn = column;
						break;
					}
				}

				final int modelRow = this.component.convertRowIndexToModel(row);

				final Object value = modelVT.getValueAt(modelRow,modelColumn);
				values.add(value);
			}
			try
			{
				subsettedVt.addRow(values,false);
			}
			catch(final Exception e)
			{
				throw new IllegalStateException("creation of virtual table failed. ",e);
			}
		}
	}


	/**
	 * @return a {@link List} of all currently visible column names ordered from
	 *         left to right
	 */
	protected List<String> getVisibleColumnNames()
	{
		final int columnCount = this.component.getColumnCount();
		final List<String> visibibleColumnNames = new ArrayList<String>();
		for(int c = 0; c < columnCount; c++)
		{
			visibibleColumnNames.add(this.component.getColumnName(c));
		}
		return visibibleColumnNames;
	}
}
