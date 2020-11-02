package xdev.vt;

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


import java.beans.Beans;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Reader;
import java.io.Serializable;
import java.io.Writer;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import javax.swing.JFileChooser;
import javax.swing.JTable;
import javax.swing.event.EventListenerList;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;

import xdev.db.ColumnMetaData;
import xdev.db.DBConnection;
import xdev.db.DBDataSource;
import xdev.db.DBException;
import xdev.db.DBMetaData.TableInfo;
import xdev.db.DBMetaData.TableMetaData;
import xdev.db.DBMetaData.TableType;
import xdev.db.DBUtils;
import xdev.db.DataType;
import xdev.db.Index;
import xdev.db.Index.IndexType;
import xdev.db.JoinType;
import xdev.db.NullDBDataSource;
import xdev.db.QueryInfo;
import xdev.db.Result;
import xdev.db.Transaction;
import xdev.db.WriteRequest;
import xdev.db.WriteResult;
import xdev.db.locking.LockFactory;
import xdev.db.locking.LockingException;
import xdev.db.locking.PessimisticLock;
import xdev.db.sql.DELETE;
import xdev.db.sql.INSERT;
import xdev.db.sql.SELECT;
import xdev.db.sql.Table;
import xdev.db.sql.UPDATE;
import xdev.db.sql.WHERE;
import xdev.db.sql.WritingQuery;
import xdev.io.ByteHolder;
import xdev.io.CharHolder;
import xdev.io.XdevObjectInputStream;
import xdev.io.XdevObjectOutputStream;
import xdev.io.csv.CSVParser;
import xdev.io.csv.CSVReader;
import xdev.io.csv.CSVWriter;
import xdev.lang.Copyable;
import xdev.lang.XDEV;
import xdev.ui.Formular;
import xdev.ui.ItemList;
import xdev.ui.UIUtils;
import xdev.ui.XdevTable;
import xdev.ui.XdevTree;
import xdev.ui.text.TextFormat;
import xdev.ui.tree.DefaultXdevTreeManager;
import xdev.ui.tree.XdevTreeModel;
import xdev.ui.tree.XdevTreeNode;
import xdev.ui.tree.XdevTreeRenderer;
import xdev.util.ArrayUtils;
import xdev.util.ConversionUtils;
import xdev.util.DataSource;
import xdev.util.IntList;
import xdev.util.MathUtils;
import xdev.util.ObjectConversionException;
import xdev.util.Settings;
import xdev.util.StringUtils;
import xdev.util.StringUtils.ParameterProvider;
import xdev.util.XdevList;
import xdev.util.logging.LoggerFactory;
import xdev.util.logging.XdevLogger;
import xdev.vt.VirtualTableEvent.Type;


/**
 * <p>
 * A Virtual Table is the core element of the data layer of the XDEV Application
 * Framework and the connector of the presentation layer (GUI) and a
 * {@link DataSource}.
 * </p>
 * <p>
 * In most cases a Virtual Table has a similiar counterpart in a
 * {@link DataSource}, normally a table in a database which it can be
 * synchronized with.
 * </p>
 * <p>
 * The Virtual Table stores data in a relational way, organized in
 * {@link VirtualTableColumn}s and {@link VirtualTableRow}s.
 * </p>
 * <p>
 * Several connectors to gui beans are provided by default, e.g.
 * {@link VirtualTableModel}. All updates in the VirtualTable are automatically
 * reflected in the gui model and vice versa. To connect a Virtual Table with a
 * gui bean use the bean's setModel(...) methods, e.g.
 * {@link XdevTable#setModel(VirtualTable)}.
 * </p>
 * <p>
 * There are several ways to get data into a Virtual Table:<br>
 * - use one of the {@link #queryAndFill()} methods,<br>
 * - via the {@link XDEV#Query(xdev.lang.cmd.Query)} wizard of the XDEV IDE,<br>
 * - call the e.g. {@link XdevTable#setModel(VirtualTable, String, boolean)}
 * with the <code>queryData</code> parameter set to <code>true</code><br>
 * - and many more ...
 * </p>
 * <p>
 * If you want to override the default {@link SELECT} statement which is used to
 * fill this Virtual Table ({@link #getSelect()}), use
 * {@link #setSelect(SELECT)}.
 * </p>
 * <p>
 * If you want to provide your own validation of inserted values, see
 * {@link VirtualTableColumn#addValidator(Validator)}.
 * </p>
 * <p>
 * To synchronize the Virtual Table with the underlying data source either<br>
 * - call the data manipulation methods, e.g {@link #addRow(List, boolean)} with
 * the <code>synchronizedDB</code> parameter set to <code>true</code><br>
 * - or invoke {@link #synchronizeChangedRows()}.
 * </p>
 * <p>
 * Virtual Tables can reflect an entire database. Therefor the tables can be
 * connected via {@link EntityRelationshipModel}s.
 * </p>
 *
 * @see VirtualTableColumn
 * @see VirtualTableRow
 * @see KeyValues
 * @see EntityRelationships
 * @see DataSouce
 * @see DBDataSource
 * @see Table
 *
 * @author XDEV Software
 */
public class VirtualTable extends Table
		implements Serializable, Iterable<VirtualTableColumn>, Copyable<VirtualTable>
{
	private static final long							serialVersionUID					= -8132819103802989330L;

	/**
	 * Logger instance for this class.
	 */
	private static final XdevLogger						log									= LoggerFactory
			.getLogger(VirtualTable.class);

	/**
	 * the default Character set used within Virtual Tables.
	 */
	public final static String							CHARSET								= "UTF-8";
	/**
	 * the default DateFormat used within Virtual Tables.
	 */
	public final static String							DATE_FORMAT							= "yyyy-MM-dd HH:mm:ss";

	private final static String							FILE_HEADER							= "%CX VIRTUAL TABLE%";

	private String										name;
	private String										dbSchema;
	private String										dbAlias;
	private transient DBDataSource<?>					dataSource;
	private VirtualTableColumn							primaryColumn;

	private int											cursorpos							= -1;

	private VirtualTableData							data;

	private final static int							FLAG_UNIQUE							= 0x0001;

	// columns + info
	protected VirtualTableColumn[]						columns;
	// private long[] maxVal;
	private int[]										columnFlags;

	private final List<VirtualTableIndex>				indices								= new ArrayList(
			3);

	// behaviour configuration
	private boolean										checkUniqueIndexDoubleValues		= true;

	/**
	 * @since 4.1
	 */
	private boolean										allowMultipleNullsInUniqueIndices	= false;

	/**
	 * @since 3.1
	 */
	private boolean										allowMissingFillColumns				= true;

	/**
	 * @since 3.1
	 */
	private boolean										allowSuperfluousFillColumns			= true;

	/**
	 * @since 4.0
	 */
	private SELECT										userDefinedSelect;

	// caches
	private final Map<Integer, List<VirtualTableIndex>>	columnToIndex						= new Hashtable();
	private final Map<String, Integer>					columnNameToIndex					= new Hashtable();
	private final Map<String, Integer>					columnSimpleNameToIndex				= new Hashtable();
	private String[]									autoValueColumns					= null;

	private transient QueryInfo							lastQuery;
	private transient int[]								lastQueryIndices;

	private final Collection<KeyValues>					removedRowKeys						= new HashSet();

	private transient EventListenerList					listenerList						= new EventListenerList();


	// Serialization stuff
	private void readObject(final java.io.ObjectInputStream in)
			throws IOException, ClassNotFoundException
	{
		in.defaultReadObject();

		this.listenerList = new EventListenerList();
	}


	/**
	 * Constructor for creating a new instance of a {@link VirtualTable}.
	 *
	 * @param rs
	 *            a {@link Result} containing the data to be wrapped by this
	 *            {@link VirtualTable}
	 */
	public VirtualTable(final Result rs)
	{
		this(rs,false);
	}


	/**
	 * Constructor for creating a new instance of a {@link VirtualTable}.
	 *
	 * @param rs
	 *            a {@link Result} containing the data to be wrapped by this
	 *            {@link VirtualTable}
	 * @param withData
	 *            boolean flag, indicating if the provided Result contains data
	 */
	public VirtualTable(final Result rs, final boolean withData)
	{
		super("");

		this.name = "";

		this.data = new VirtualTableData(withData ? 100 : 10);

		final int cols = rs.getColumnCount();
		// maxVal = new long[cols];
		this.columns = new VirtualTableColumn[cols];
		this.columnFlags = new int[cols];
		final int[] columnIndices = new int[cols];
		for(int i = 0; i < cols; i++)
		{
			final ColumnMetaData meta = rs.getMetadata(i);
			this.columns[i] = new VirtualTableColumn(meta);
			this.columns[i].setNullable(true);
			if(i == 0)
			{
				this.name = meta.getTable();
				if(this.name == null)
				{
					this.name = "";
				}
			}
			else if(this.name.length() > 0 && !this.name.equals(meta.getTable()))
			{
				this.name = "";
			}

			// maxVal[i] = 0l;
			this.columnFlags[i] = 0;
			columnIndices[i] = i;
		}

		this.dbAlias = this.name;

		connectWithColumns();

		this.primaryColumn = this.columns[0];

		if(withData)
		{
			try
			{
				addData(rs,columnIndices);
			}
			catch(final Exception e)
			{
				throw new VirtualTableException(this,e);
			}
		}
	}


	/**
	 * Constructor for creating a new instance of a {@link VirtualTable}.
	 *
	 * @param name
	 *            {@link String} containing the name of this Virtual Table.
	 * @param dbAlias
	 *            {@link String} containing the dbAlias
	 * @param columns
	 *            a number of {@link VirtualTableColumn} instances containing
	 *            all columns for this Virtual Table
	 */
	public VirtualTable(final String name, final String dbAlias,
			final VirtualTableColumn... columns)
	{
		this(name,null,dbAlias,columns);
	}


	/**
	 * Constructor for creating a new instance of a {@link VirtualTable}.
	 *
	 * @param name
	 *            {@link String} containing the name of this Virtual Table.
	 * @param dbSchema
	 *            {@link String} containing the dbSchema, may be
	 *            <code>null</code>
	 * @param dbAlias
	 *            {@link String} containing the dbAlias
	 * @param columns
	 *            a number of {@link VirtualTableColumn} instances containing
	 *            all columns for this Virtual Table
	 */
	public VirtualTable(final String name, final String dbSchema, final String dbAlias,
			final VirtualTableColumn... columns)
	{
		this(name,dbSchema,dbAlias,columns,columns[0],null);
	}


	/**
	 * Constructor for creating a new instance of a {@link VirtualTable}.
	 *
	 * @param name
	 *            {@link String} containing the name of this Virtual Table.
	 * @param dbAlias
	 *            {@link String} containing the dbAlias
	 * @param columns
	 *            an array of {@link VirtualTableColumn} containing all columns
	 *            for this Virtual Table.
	 * @param primaryColumn
	 *            {@link VirtualTableColumn} containing the primary (key) column
	 *            for this Virtual Table
	 * @param data
	 *            a two-dimensional array of {@link Object} instances containing
	 *            the data for this Virtual Table
	 */
	public VirtualTable(final String name, final String dbAlias, final VirtualTableColumn[] columns,
			final VirtualTableColumn primaryColumn, final Object[][] data)
	{
		this(name,null,dbAlias,columns,primaryColumn,data);
	}


	/**
	 * Constructor for creating a new instance of a {@link VirtualTable}.
	 *
	 * @param name
	 *            {@link String} containing the name of this Virtual Table.
	 * @param dbSchema
	 *            {@link String} containing the dbSchema, may be
	 *            <code>null</code>
	 * @param dbAlias
	 *            {@link String} containing the dbAlias
	 * @param columns
	 *            an array of {@link VirtualTableColumn} containing all columns
	 *            for this Virtual Table.
	 * @param primaryColumn
	 *            {@link VirtualTableColumn} containing the primary (key) column
	 *            for this Virtual Table
	 * @param data
	 *            a two-dimensional array of {@link Object} instances containing
	 *            the data for this Virtual Table
	 */
	public VirtualTable(final String name, final String dbSchema, final String dbAlias,
			final VirtualTableColumn[] columns, final VirtualTableColumn primaryColumn,
			final Object[][] data)
	{
		super(dbSchema != null && dbSchema.length() > 0 ? dbSchema : null,dbAlias,null);

		this.name = name;
		this.dbSchema = "".equals(dbSchema) ? null : dbSchema;
		this.dbAlias = dbAlias;

		this.columns = columns;
		connectWithColumns();

		final int cols = columns.length;

		// this.maxVal = new long[cols];
		this.columnFlags = new int[cols];
		for(int i = 0; i < cols; i++)
		{
			// this.maxVal[i] = 0l;
			this.columnFlags[i] = 0;
		}

		this.primaryColumn = primaryColumn != null ? primaryColumn : columns[0];

		if(data != null)
		{
			this.data = new VirtualTableData(data.length);
			for(int r = 0; r < data.length; r++)
			{
				final VirtualTableRow row = new VirtualTableRow(cols);
				for(int c = 0; c < cols; c++)
				{
					row.set(c,data[r][c],false,false);
				}
				this.data.add(row,false);
			}
		}
		else
		{
			this.data = new VirtualTableData(10);
		}
	}


	/**
	 * This method returns a copy of this {@link VirtualTable} instance without
	 * the data.
	 *
	 * @return a copied {@link VirtualTable} of this instance
	 */
	public VirtualTable copyHeader()
	{
		return clone(false);
	}


	/**
	 * This method returns a copy of this {@link VirtualTable} instance. The
	 * data of the table won't be copied
	 *
	 * @return a copied {@link VirtualTable} of this instance
	 */
	@Override
	public VirtualTable clone()
	{
		return clone(false);
	}


	/**
	 * This method returns a copy of this {@link VirtualTable} instance.
	 *
	 * @param withData
	 *            boolean flag, indicating if data should be copied, too.
	 * @return a copied {@link VirtualTable} of this instance
	 */
	public VirtualTable clone(final boolean withData)
	{
		try
		{
			final VirtualTable vt = getClass().newInstance();
			vt.takeValues(this,withData);

			// reconnect static columns (see issue 11929)
			connectWithColumns();

			return vt;
		}
		catch(final Exception e)
		{
			// shouldn't happen
			log.error(e);
			throw new RuntimeException(e);
		}
	}


	/*
	 * Used by #clone
	 */
	private VirtualTable()
	{
		super("");
	}


	private void takeValues(final VirtualTable vt, final boolean withData)
	{
		this.name = vt.name;
		this.dataSource = vt.dataSource;
		this.dbSchema = vt.dbSchema;
		this.dbAlias = vt.dbAlias;
		this.lastQuery = vt.lastQuery;

		this.columns = new VirtualTableColumn[vt.columns.length];
		for(int i = 0; i < vt.columns.length; i++)
		{
			this.columns[i] = vt.columns[i].clone();
		}
		connectWithColumns();

		this.primaryColumn = this.columns[vt.getColumnIndex(vt.primaryColumn)];

		// maxVal = new long[columns.length];
		this.columnFlags = new int[this.columns.length];

		for(int i = 0; i < this.columns.length; i++)
		{
			// maxVal[i] = 0l;
			this.columnFlags[0] = 0;
		}

		this.indices.clear();
		for(final VirtualTableIndex vtIndex : vt.indices)
		{
			addIndex(vtIndex._originalIndex);
		}

		this.checkUniqueIndexDoubleValues = vt.checkUniqueIndexDoubleValues;
		this.allowMultipleNullsInUniqueIndices = vt.allowMultipleNullsInUniqueIndices;
		this.allowMissingFillColumns = vt.allowMissingFillColumns;
		this.allowSuperfluousFillColumns = vt.allowSuperfluousFillColumns;

		if(vt.userDefinedSelect != null)
		{
			this.userDefinedSelect = vt.userDefinedSelect.clone();
		}

		if(withData)
		{
			final int rc = vt.getRowCount();
			this.data = new VirtualTableData(rc);
			for(int i = 0; i < rc; i++)
			{
				this.data.add(new VirtualTableRow(vt.getRow(i)),false);
			}
		}
		else
		{
			this.data = new VirtualTableData(10);
		}
	}


	void connectWithColumns()
	{
		_updateDelegate(this.dbSchema != null && this.dbSchema.length() > 0 ? this.dbSchema : null,
				this.dbAlias,null);

		for(final VirtualTableColumn column : this.columns)
		{
			column.setVirtualTable(this);
		}
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(final Object obj)
	{
		if(obj == this)
		{
			return true;
		}

		if(obj instanceof VirtualTable)
		{
			return _getIdentifier().equals(((VirtualTable)obj)._getIdentifier());
		}

		return false;
	}


	/**
	 * used by {@link #equals(Object)}
	 *
	 * @since 3.2
	 */
	private String _getIdentifier()
	{
		String name = getName();
		if(name == null || name.length() == 0)
		{
			name = getClass().getName();
		}
		return name;
	}


	/**
	 * Adds an {@link Index} to this {@link VirtualTable}.
	 *
	 * @param index
	 *            the {@link Index} to be added.
	 * @throws NullPointerException
	 *             if the Index contains columns not found in this Virtual Table
	 */
	public void addIndex(final Index index) throws NullPointerException
	{
		final VirtualTableIndex vtIndex = new VirtualTableIndex(index);
		this.indices.add(vtIndex);
		for(final String columnName : index.getColumns())
		{
			final int colIndex = getColumnIndex(columnName);
			if(colIndex == -1)
			{
				throw new NullPointerException("Column '" + columnName + "' not found");
			}

			List<VirtualTableIndex> columnIndices = this.columnToIndex.get(colIndex);
			if(columnIndices == null)
			{
				columnIndices = new ArrayList(2);
				this.columnToIndex.put(colIndex,columnIndices);
			}
			columnIndices.add(vtIndex);

			if(index.isUnique())
			{
				this.columnFlags[colIndex] |= FLAG_UNIQUE;
			}
		}
	}


	/**
	 * Returns true if all elements of this {@link VirtualTableColumn} are
	 * unique.
	 *
	 * @param column
	 *            the {@link VirtualTableColumn} to be checked.
	 * @return true, if unique
	 */
	public boolean isUnique(final VirtualTableColumn column)
	{
		return (this.columnFlags[getColumnIndex(column)] & FLAG_UNIQUE) == FLAG_UNIQUE;
	}


	void checkColumnDefaults()
	{
		for(int i = 0; i < this.columns.length; i++)
		{
			final Object def = this.columns[i].getDefaultValue();
			this.columns[i].setDefaultValue(checkValue(i,def));
		}
	}


	/**
	 * Adds a {@link VirtualTableListener} to this {@link VirtualTable}.
	 * <ul>
	 * <li>a row is inserted, updated or deleted</li>
	 * <li>the data of the Virtual Table changes</li>
	 * <li>a structural change of the Virtual Table happens, e.g. a column gets
	 * removed</li>
	 * </ul>
	 *
	 * @param l
	 *            {@link VirtualTableListener} to be added
	 */
	public void addVirtualTableListener(final VirtualTableListener l)
	{
		this.listenerList.add(VirtualTableListener.class,l);
	}


	/**
	 * Removes a {@link VirtualTableListener} from this {@link VirtualTable}.
	 * The listener gets notified, whenever:
	 * <ul>
	 * <li>a row is inserted, updated or deleted</li>
	 * <li>the data of the Virtual Table changes</li>
	 * <li>a structural change of the Virtual Table happens, e.g. a column gets
	 * removed</li>
	 * </ul>
	 *
	 * @param l
	 *            {@link VirtualTableListener} to be removed
	 */
	public void removeVirtualTableListener(final VirtualTableListener l)
	{
		this.listenerList.remove(VirtualTableListener.class,l);
	}


	/**
	 * This method notifies all registered listeners, whenever a row gets
	 * deleted.
	 *
	 * @param row
	 *            the {@link VirtualTableRow} to be deleted,
	 * @param rowIndex
	 *            the index of the row to be deleted within the Virtual Table.
	 */
	protected void fireRowDeleted(final VirtualTableRow row, final int rowIndex)
	{
		this.cursorpos = -1;

		final VirtualTableListener[] listeners = this.listenerList
				.getListeners(VirtualTableListener.class);
		if(listeners != null && listeners.length > 0)
		{
			final VirtualTableEvent event = new VirtualTableEvent(this,Type.REMOVED,row,rowIndex);
			for(final VirtualTableListener listener : listeners)
			{
				listener.virtualTableRowDeleted(event);
			}
		}
	}


	/**
	 * This method notifies all registered listeners, whenever a row gets
	 * inserted.
	 *
	 * @param row
	 *            the {@link VirtualTableRow} to be deleted,
	 * @param rowIndex
	 *            the index where the row gets inserted to the Virtual Table.
	 */
	protected void fireRowInserted(final VirtualTableRow row, final int rowIndex)
	{
		this.cursorpos = -1;

		final VirtualTableListener[] listeners = this.listenerList
				.getListeners(VirtualTableListener.class);
		if(listeners != null && listeners.length > 0)
		{
			final VirtualTableEvent event = new VirtualTableEvent(this,Type.INSERTED,row,rowIndex);
			for(final VirtualTableListener listener : listeners)
			{
				listener.virtualTableRowInserted(event);
			}
		}
	}


	/**
	 * This method notifies all registered listeners, whenever a row gets
	 * updated.
	 *
	 * @param row
	 *            the {@link VirtualTableRow} to be updated,
	 * @param rowIndex
	 *            the index of the row to be updated within the Virtual Table.
	 */
	protected void fireRowUpdated(final VirtualTableRow row, final int rowIndex)
	{
		final VirtualTableListener[] listeners = this.listenerList
				.getListeners(VirtualTableListener.class);
		if(listeners != null && listeners.length > 0)
		{
			final VirtualTableEvent event = new VirtualTableEvent(this,Type.UPDATED,row,rowIndex);
			for(final VirtualTableListener listener : listeners)
			{
				listener.virtualTableRowUpdated(event);
			}
		}
	}


	/**
	 * This method notifies all registered listeners, whenever a cell gets
	 * updated.
	 *
	 * @param row
	 *            the {@link VirtualTableRow} to be updated,
	 * @param rowIndex
	 *            the index of the row to be updated within the Virtual Table.
	 * @param columnIndex
	 *            the index of the column to be updated within the Virtual
	 *            Table.
	 * @since 3.2
	 */
	protected void fireCellUpdated(final VirtualTableRow row, final int rowIndex,
			final int columnIndex)
	{
		final VirtualTableListener[] listeners = this.listenerList
				.getListeners(VirtualTableListener.class);
		if(listeners != null && listeners.length > 0)
		{
			final VirtualTableEvent event = new VirtualTableEvent(this,Type.UPDATED,row,rowIndex,
					columnIndex);
			for(final VirtualTableListener listener : listeners)
			{
				listener.virtualTableRowUpdated(event);
			}
		}
	}


	/**
	 * This method notifies all registered listeners, whenever the data of this
	 * Virtual Table changes.
	 */
	protected void fireDataChanged()
	{
		this.cursorpos = -1;

		final VirtualTableListener[] listeners = this.listenerList
				.getListeners(VirtualTableListener.class);
		if(listeners != null && listeners.length > 0)
		{
			final VirtualTableEvent event = new VirtualTableEvent(this);
			for(final VirtualTableListener listener : listeners)
			{
				listener.virtualTableDataChanged(event);
			}
		}
	}


	/**
	 * This method notifies all registered listeners, whenever the structure of
	 * this Virtual Table is modified. One example for such a modification is
	 * the removal of a column.
	 */
	protected void fireStructureChanged()
	{
		this.cursorpos = -1;

		final VirtualTableListener[] listeners = this.listenerList
				.getListeners(VirtualTableListener.class);
		if(listeners != null && listeners.length > 0)
		{
			final VirtualTableEvent event = new VirtualTableEvent(this);
			for(final VirtualTableListener listener : listeners)
			{
				listener.virtualTableStructureChanged(event);
			}
		}
	}


	/**
	 * Returns the name of this {@link VirtualTable}.
	 *
	 * @return {@link String} containing the name
	 */
	public String getName()
	{
		return this.name;
	}


	/**
	 * Sets an explicit {@link DataSource} for this <code>VirtualTable</code>.
	 *
	 * @param dataSource
	 *            the new data source, or <code>null</code> if the application's
	 *            default data source should be used
	 */
	public void setDataSource(final DBDataSource<?> dataSource)
	{
		this.dataSource = dataSource;
	}


	/**
	 * Returns <code>true</code> if {@link #setDataSource(DBDataSource)} has
	 * been invoked with a non-null value., <code>false</code> otherwise.
	 *
	 * @return <code>true</code> if {@link #setDataSource(DBDataSource)} has
	 *         been invoked with a non-null value.
	 */
	public boolean isDataSourceSet()
	{
		return this.dataSource != null;
	}


	/**
	 * Returns the {@link DBDataSource} of this {@link VirtualTable}. If this
	 * {@link VirtualTable} has no <code>DBDataSource</code>, the current
	 * <code>DBDataSource</code> will be returned.
	 *
	 * @return a {@link DBDataSource}
	 * @throws DBException
	 */
	public DBDataSource<?> getDataSource() throws DBException
	{
		if(Beans.isDesignTime())
		{
			return NullDBDataSource.instance;
		}

		if(this.dataSource != null)
		{
			return this.dataSource;
		}

		return DBUtils.getCurrentDataSource();
	}


	/**
	 * Returns the database schema of this {@link VirtualTable}, may be
	 * <code>null</code>.
	 *
	 * @return the database schema of this {@link VirtualTable}.
	 */
	public String getDatabaseSchema()
	{
		return this.dbSchema;
	}


	/**
	 * Returns the database alias of this {@link VirtualTable}.
	 *
	 * @return the database alias of this {@link VirtualTable}.
	 */
	public String getDatabaseAlias()
	{
		return this.dbAlias;
	}


	/**
	 * Returns the primary column of this {@link VirtualTable}.
	 *
	 * @return the {@link VirtualTableColumn} instance of the primary column.
	 *         May be null.
	 */
	public VirtualTableColumn<?> getPrimaryColumn()
	{
		return this.primaryColumn;
	}


	/**
	 * Sets the primary column of this {@link VirtualTable}. The previous value
	 * get overwritten.
	 *
	 * @param primaryColumn
	 *            a {@link VirtualTableColumn} instance for the new primary
	 *            column
	 * @throws IllegalArgumentException
	 *             if the primary column is not contained in this Virtual Table.
	 */
	public void setPrimaryColumn(final VirtualTableColumn primaryColumn)
			throws IllegalArgumentException
	{
		if(!ArrayUtils.contains(this.columns,primaryColumn))
		{
			throw new IllegalArgumentException("Not a column of this VirtualTable");
		}

		this.primaryColumn = primaryColumn;
	}


	/**
	 * Returns the number of columns.
	 *
	 * @return the number of columns as int value.
	 */
	public int getColumnCount()
	{
		return this.columns.length;
	}


	/**
	 * Returns the {@link VirtualTableColumn} with the specified name.
	 *
	 * @param name
	 *            as a {@link String}
	 * @return the {@link VirtualTableColumn} for the specified name, or null if
	 *         no column is found.
	 */
	public VirtualTableColumn<?> getColumn(final String name)
	{
		final int i = getColumnIndex(name);
		if(i == -1)
		{
			return null;
		}

		return this.columns[i];
	}


	/**
	 * Returns the {@link VirtualTableColumn}s with the specified names.
	 *
	 * @param names
	 *            as a {@link String} array
	 * @return the {@link VirtualTableColumn}s for the specified names, if a
	 *         column couldn't be found, the array's element if
	 *         <code>null</code>
	 * @since 3.2
	 */
	public VirtualTableColumn[] getColumns(final String[] names)
	{
		final int c = names.length;
		final VirtualTableColumn[] columns = new VirtualTableColumn[c];
		for(int i = 0; i < c; i++)
		{
			columns[i] = getColumn(names[i]);
		}
		return columns;
	}


	/**
	 * Returns the index of the column with the specified name.
	 *
	 * @param name
	 *            as a {@link String}
	 * @return the index for the specified column as int value, -1 is returned
	 *         if no column is found.
	 */
	public int getColumnIndex(String name)
	{
		name = name.toUpperCase();

		Integer index = this.columnNameToIndex.get(name);
		if(index != null)
		{
			return index;
		}

		int i;

		for(i = 0; i < this.columns.length; i++)
		{
			if(this.columns[i].getName().equalsIgnoreCase(name))
			{
				this.columnNameToIndex.put(name,i);
				return i;
			}
		}

		i = name.lastIndexOf('.');
		if(i >= 0)
		{
			name = name.substring(i + 1);

			index = this.columnSimpleNameToIndex.get(name);
			if(index != null)
			{
				return index;
			}

			for(i = 0; i < this.columns.length; i++)
			{
				if(this.columns[i].getName().equalsIgnoreCase(name))
				{
					this.columnSimpleNameToIndex.put(name,i);
					return i;
				}
			}
		}

		return -1;
	}


	/**
	 * Returns a {@link List} of {@link String} objects with the column names of
	 * this {@link VirtualTable}.
	 *
	 * @param exceptCols
	 *            indices of columns, not to be included within the list of
	 *            columns.
	 * @return a {@link List} with all column names, except the ones specified
	 *         by param exceptCols.
	 */
	public List<String> getColumnNames(final int... exceptCols)
	{
		final int c = this.columns.length;
		final List<String> list = new ArrayList(c);
		for(int i = 0; i < c; i++)
		{
			if(exceptCols == null || !ArrayUtils.contains(exceptCols,i))
			{
				list.add(this.columns[i].getName());
			}
		}

		return list;
	}


	/**
	 * Returns the name of the column with the specified index. The first column
	 * has the index 0.
	 * <p>
	 * Example:
	 *
	 * <pre>
	 * ExampleVT.getColumnName(3);
	 * </pre>
	 *
	 * </p>
	 *
	 * @param col
	 *            the index of the column, which name is to be returned
	 * @return the name of the column at the specified index
	 * @see #getCaptions()
	 * @see #getColumnIndex(String)
	 * @see #getRowCount()
	 */
	public String getColumnName(final int col)
	{
		return this.columns[col].getName();
	}


	/**
	 * Returns the caption of the column with the specified index. The first
	 * column has index 0. If the caption is empty, the name of the column will
	 * be returned.
	 *
	 * @param col
	 *            the index of the column, which name is to be returned
	 * @return the caption of the column at the specified index
	 */
	public String getColumnCaption(final int col)
	{
		final String caption = this.columns[col].getCaption();
		if(caption != null && caption.length() > 0)
		{
			return caption;
		}

		return this.columns[col].getName();
	}


	/**
	 * Returns a array of {@link String} objects with all captions.
	 *
	 * @return all captions as String array.
	 */
	public String[] getColumnCaptions()
	{
		final int len = this.columns.length;
		final String[] s = new String[len];
		for(int col = 0; col < len; col++)
		{
			s[col] = getColumnCaption(col);
		}
		return s;
	}


	/**
	 * Return the index for the specified {@link VirtualTableColumn}.
	 *
	 * @param col
	 *            the {@link VirtualTableColumn}, whose index is to be returned
	 * @return the index for the specified column. -1 is returned if
	 *         VirtualTableColumn is not found.
	 */
	public int getColumnIndex(final VirtualTableColumn col)
	{
		for(int i = 0; i < this.columns.length; i++)
		{
			if(this.columns[i].equals(col))
			{
				return i;
			}
		}

		return -1;
	}


	/**
	 * Return the indices for the specified {@link VirtualTableColumn}.
	 *
	 * @param columns
	 *            the {@link VirtualTableColumn}s, whose index is to be returned
	 * @return the indices for the specified columns.
	 */
	public int[] getColumnIndices(final VirtualTableColumn... columns)
	{
		final int[] indices = new int[columns.length];
		for(int i = 0; i < columns.length; i++)
		{
			indices[i] = getColumnIndex(columns[i]);
		}
		return indices;
	}


	/**
	 * Returns the {@link VirtualTableColumn} at the specified index position.
	 *
	 * @param index
	 *            the index of the {@link VirtualTableColumn} to be returned
	 * @return the {@link VirtualTableColumn} at the specified index
	 * @throws ArrayIndexOutOfBoundsException
	 *             if <code>index &lt; -1</code> or
	 *             <code>index > getColumnCount-1</code>
	 */
	public VirtualTableColumn<?> getColumnAt(final int index) throws ArrayIndexOutOfBoundsException
	{
		return this.columns[index];
	}


	/**
	 * Returns the {@link VirtualTableColumn}s at the specified positions.
	 *
	 * @param indices
	 *            the indices of the {@link VirtualTableColumn}s to be returned
	 * @return the {@link VirtualTableColumn}s at the specified indices
	 * @throws ArrayIndexOutOfBoundsException
	 *             if <code>indices[i] &lt; -1</code> or
	 *             <code>indices[i] > getColumnCount-1</code>
	 */
	public VirtualTableColumn<?>[] getColumnsAt(final int[] indices)
			throws ArrayIndexOutOfBoundsException
	{
		final VirtualTableColumn[] columns = new VirtualTableColumn[indices.length];
		for(int i = 0; i < indices.length; i++)
		{
			columns[i] = this.columns[indices[i]];
		}
		return columns;
	}


	/**
	 * Returns an iterator over all columns of this VirtualTable in their
	 * natural order.
	 * <p>
	 * Note: The returned iterator doesn't support {@link Iterator#remove()}.
	 * </p>
	 *
	 *
	 * @return An interator over the columns
	 */

	@Override
	public Iterator<VirtualTableColumn> iterator()
	{
		return ArrayUtils.getIterator(this.columns);
	}


	/**
	 * Returns an iterator over all columns of this VirtualTable in their
	 * natural order.
	 * <p>
	 * Note: The returned iterator doesn't support {@link Iterator#remove()}.
	 * </p>
	 *
	 *
	 * @return An interator over the columns
	 */

	public Iterable<VirtualTableColumn> columns()
	{
		return this;
	}


	/**
	 * Returns an iterator over all rows of this VirtualTable in their natural
	 * order.
	 * <p>
	 * Note: The returned iterator doesn't support {@link Iterator#remove()}.
	 * </p>
	 *
	 *
	 * @return An interator over the rows
	 */
	public Iterable<VirtualTableRow> rows()
	{
		return this.data;
	}


	/**
	 * Returns the number of rows for this {@link VirtualTable}.
	 *
	 * @return the number of rows
	 */
	public int getRowCount()
	{
		return this.data.size();
	}


	/**
	 * Returns the value of the cell specified by column name and the row of the
	 * current cursor position.
	 *
	 * @param columnName
	 *            the name of the column
	 * @return the value of the cell at the specified position.
	 */
	public Object getValueAt(final String columnName)
	{
		return getValueAt(getColumnIndex(columnName));
	}


	/**
	 * Returns the value of the cell specified by column index and the row of
	 * the current cursor position.
	 *
	 * @param col
	 *            the index of the column (0-based).
	 * @return the value of the cell at the specified position.
	 */
	public Object getValueAt(final int col)
	{
		return getValueAt(this.cursorpos,col);
	}


	/**
	 * Returns the value of the cell specified by the row index and column
	 * index.
	 * <p>
	 * Example:
	 *
	 * <pre>
	 * ExampleVT.addRow();
	 * ExampleVT.getValueAt(0,2);
	 * </pre>
	 *
	 * </p>
	 *
	 * @param row
	 *            the index of the row (0-based)
	 * @param col
	 *            the index of the column (0-based)
	 * @return the value of the cell at the specified position
	 */
	public Object getValueAt(final int row, final int col)
	{
		return this.data.get(row).get(col);
	}


	/**
	 * Returns the value of the cell specified by column name and the row of the
	 * current cursor position.
	 *
	 * @param row
	 *            the index of the row (0-based)
	 * @param columnName
	 *            the name of the column
	 * @return the value of the cell at the specified position.
	 */
	public Object getValueAt(final int row, final String columnName)
	{
		final int col = getColumnIndex(columnName);
		if(col == -1)
		{
			throw new IllegalArgumentException("Column '" + columnName + "' not found");
		}

		return getValueAt(row,col);
	}


	/**
	 * Returns the value of the cell specified by a {@link VirtualTableColumn}
	 * and the row of the current cursor position.
	 *
	 * @param row
	 *            the index of the row (0-based)
	 * @param column
	 *            {@link VirtualTableColumn}
	 *
	 * @return the value of the cell at the specified position.
	 *
	 * @throws IllegalArgumentException
	 *             if column is not not within this Virtual Table
	 */
	public <T> T getValueAt(final int row, final VirtualTableColumn<T> column)
			throws IllegalArgumentException
	{
		final int columnIndex = ArrayUtils.indexOf(this.columns,column);
		if(columnIndex == -1)
		{
			throw new IllegalArgumentException("Column doesn't belong to this VirtualTable");
		}

		return (T)getValueAt(row,columnIndex);
	}


	/**
	 * Returns a formatted representation for the value at the cell position
	 * specified by row and col. The {@link TextFormat} of the
	 * {@link VirtualTableColumn} at index col is used to format the value.
	 *
	 * @param row
	 *            the index of the row (0-based)
	 * @param col
	 *            the index of the column (0-based)
	 * @return a formatted {@link String} representation of the specified value.
	 */
	public String getFormattedValueAt(final int row, final int col)
	{
		return formatValue(getValueAt(row,col),col);
	}


	/**
	 * Returns a formatted string, with the {@link VirtualTableRow} at
	 * <code>row</code> used as ParameterProvider.
	 * <p>
	 * This is a shortcut for <code>getRow(row).format(str)</code>.
	 *
	 * @param row
	 *            the row index
	 * @param str
	 *            the {@link String} to format
	 * @return a formatted {@link String}
	 * @see VirtualTableRow#format(String)
	 */
	public String format(final int row, final String str)
	{
		return getRow(row).format(str);
	}


	/**
	 * Returns a formatted representation for the value at the cell position
	 * specified by row and column name. The {@link TextFormat} of the
	 * {@link VirtualTableColumn} with name columnName is used to format the
	 * value.
	 *
	 * @param row
	 *            the index of the row (0-based)
	 * @param columnName
	 *            the name of the column
	 * @return a formatted {@link String} representation of the specified value.
	 */
	public String getFormattedValueAt(final int row, final String columnName)
	{
		final int col = getColumnIndex(columnName);
		if(col == -1)
		{
			return "";
		}

		return getFormattedValueAt(row,col);
	}


	/**
	 * Returns a formatted representation for the value at the cell position
	 * specified by row and a {@link VirtualTableColumn}. The {@link TextFormat}
	 * of the {@link VirtualTableColumn} is used to format the value.
	 *
	 * @param row
	 *            the index of the row (0-based)
	 * @param column
	 *            the {@link VirtualTableColumn}
	 * @return a formatted {@link String} representation of the specified value.
	 */
	public String getFormattedValueAt(final int row, final VirtualTableColumn column)
	{
		final int columnIndex = ArrayUtils.indexOf(this.columns,column);
		if(columnIndex == -1)
		{
			throw new IllegalArgumentException("Column doesn't belong to this VirtualTable");
		}

		return getFormattedValueAt(row,columnIndex);
	}


	/**
	 * Returns a formatted representation of the specified value using the
	 * {@link TextFormat} of the {@link VirtualTableColumn} specified by index
	 * col.
	 *
	 * @param value
	 *            the value to be formatted
	 * @param col
	 *            the index of the {@link VirtualTableColumn}
	 * @return a formatted {@link String} representation of the specified value.
	 */
	public String formatValue(final Object value, final int col)
	{
		if(value == null)
		{
			return "";
		}

		return this.columns[col].getTextFormat().format(value);
	}


	/**
	 * Returns a formatted representation of the specified value using the
	 * {@link TextFormat} of the {@link VirtualTableColumn} specified by
	 * <code>columnName</code>.
	 *
	 * @param value
	 *            the value to be formatted
	 * @param columnName
	 *            the name of the {@link VirtualTableColumn}
	 * @return a formatted {@link String} representation of the specified value.
	 * @since 3.2
	 */
	public String formatValue(final Object value, final String columnName)
	{
		if(value == null)
		{
			return "";
		}

		return getColumn(columnName).getTextFormat().format(value);
	}


	/**
	 * Sets the value of a cell at the position specified by the current cursor
	 * position and the column name.
	 *
	 * @param val
	 *            the value to be set
	 * @param columnName
	 *            the name of the column
	 * @return the new value of the cell
	 * @throws VirtualTableException
	 *             if the value is not appropriate for the specified column
	 * @throws IllegalArgumentException
	 *             if the column <code>columnName</code> does not exist in this
	 *             {@link VirtualTable}
	 */
	public Object setValueAt(final Object val, final String columnName)
			throws VirtualTableException, IllegalArgumentException
	{
		return setValueAt(val,this.cursorpos,columnName);
	}


	/**
	 * Sets the value of a cell at the position specified by the row and the
	 * column name.
	 *
	 * @param val
	 *            the value to be set
	 * @param row
	 *            the index of the row (0-based)
	 * @param columnName
	 *            the name of the column
	 * @return the new value of the the cell
	 * @throws VirtualTableException
	 *             if the value is not appropriate for the specified column
	 * @throws IllegalArgumentException
	 *             if the column <code>columnName</code> does not exist in this
	 *             {@link VirtualTable}
	 */
	public Object setValueAt(final Object val, final int row, final String columnName)
			throws VirtualTableException, IllegalArgumentException
	{
		final int col = getColumnIndex(columnName);
		if(col == -1)
		{
			throw new IllegalArgumentException("column '" + columnName + "' not found");
		}

		return setValueAt(val,row,col);
	}


	/**
	 * Sets the value of a cell at the position specified by the current cursor
	 * position and the column index.
	 *
	 * @param val
	 *            the value to be set
	 * @param col
	 *            the index of the column (0-based)
	 * @return the new value of the cell
	 * @throws VirtualTableException
	 *             if the value is not appropriate for the specified column
	 */
	public Object setValueAt(final Object val, final int col) throws VirtualTableException
	{
		return setValueAt(val,this.cursorpos,col);
	}


	/**
	 * Sets the value of a cell at the position specified by the row index and
	 * the column index.
	 *
	 * @param val
	 *            the value to be set
	 * @param row
	 *            the index of the row (0-based).
	 * @param col
	 *            the index of the column (0-based).
	 *
	 * @return the new value of the cell
	 *
	 * @throws VirtualTableException
	 *             if the value is not appropriate for the specified column
	 */
	public Object setValueAt(final Object val, final int row, final int col)
			throws VirtualTableException
	{
		return setValueAt(val,row,col,false);
	}


	/**
	 * Sets the value of a cell at the position specified by the row index and
	 * the column index.
	 *
	 * @param val
	 *            the value to be set
	 * @param row
	 *            the index of the row (0-based).
	 * @param col
	 *            the index of the column (0-based).
	 * @param synchronizeDB
	 *            boolean, if set to true, value will be written to the
	 *            underlying data source
	 *
	 * @return the new value of the cell
	 *
	 * @throws VirtualTableException
	 *             if <code>synchronizeDB</code> is <code>true</code> and no
	 *             primary key is specified for this Virtual Table, or if the
	 *             value is not appropriate for the specified column
	 */
	public Object setValueAt(final Object val, final int row, final int col,
			final boolean synchronizeDB) throws VirtualTableException
	{
		return setValueAt(val,row,col,synchronizeDB,true);
	}


	/**
	 * Sets the value of a cell at the position specified by the row index and
	 * the column index.
	 *
	 * @param val
	 *            the value to be set
	 *
	 * @param row
	 *            the index of the row (0-based).
	 *
	 * @param col
	 *            the index of the column (0-based).
	 *
	 * @param synchronizeDB
	 *            boolean, if set to true, value will be written to the
	 *            underlying data source
	 *
	 * @param markAsUpdated
	 *            boolean, if set to true, the row containing the cell will be
	 *            marked as changed.
	 *
	 * @return the new value of the cell
	 *
	 * @throws VirtualTableException
	 *             if <code>synchronizeDB</code> is <code>true</code> and no
	 *             primary key is specified for this Virtual Table, or if the
	 *             value is not appropriate for the specified column
	 */
	public Object setValueAt(final Object val, final int row, final int col,
			final boolean synchronizeDB, final boolean markAsUpdated) throws VirtualTableException
	{
		return setValueAt(val,row,col,synchronizeDB,markAsUpdated,true);
	}


	/**
	 * Sets the value of a cell at the position specified by the row index and
	 * the column index.
	 *
	 * @param val
	 *            the value to be set
	 *
	 * @param row
	 *            the index of the row (0-based).
	 *
	 * @param col
	 *            the index of the column (0-based).
	 *
	 * @param synchronizeDB
	 *            boolean, if set to true, value will be written to the
	 *            underlying data source
	 *
	 * @param markAsUpdated
	 *            boolean, if set to true, the row containing the cell will be
	 *            marked as changed.
	 *
	 * @param checkUnique
	 *            boolean, if set to true, the value will checked for
	 *            uniqueness. If the value ist not unique a
	 *            UniqueIndexDoubleValuesException is thrown.
	 *
	 * @return the new value of the cell
	 *
	 * @throws VirtualTableException
	 *             if <code>synchronizeDB</code> is <code>true</code> and no
	 *             primary key is specified for this Virtual Table, or if the
	 *             value is not appropriate for the specified column
	 */
	public Object setValueAt(Object val, final int row, final int col, final boolean synchronizeDB,
			final boolean markAsUpdated, final boolean checkUnique) throws VirtualTableException
	{
		final Object old = getValueAt(row,col);
		if(equals(val,old,getColumnAt(col).getType()))
		{
			return old;
		}

		val = checkValue(col,val);

		final VirtualTableRow vtRow = this.data.get(row);
		vtRow.set(col,val,markAsUpdated,checkUnique);

		if(markAsUpdated)
		{
			fireCellUpdated(vtRow,row,col);
		}
		if(synchronizeDB)
		{
			final VirtualTableColumn[] pk = getPrimaryKeyColumns();
			if(pk.length > 0)
			{
				final UPDATE update = new UPDATE(this);
				final List values = new ArrayList(pk.length);

				update.SET(this.columns[col],"?");
				values.add(val);

				final WHERE where = new WHERE();
				for(int i = 0; i < pk.length; i++)
				{
					where.and(pk[i].toSqlField().eq("?"));
					values.add(getValueAt(row,getColumnIndex(pk[i])));
				}
				update.WHERE(where);

				final Object[] params = values.toArray(new Object[values.size()]);
				try
				{
					writeDB(null,update,false,params);
				}
				catch(final DBException e)
				{
					throw new VirtualTableException(this,e);
				}
			}
			else
			{
				throw new VirtualTableException(this,"No primary key specified");
			}
		}

		return val;
	}


	/**
	 * Clears the data of this {@link VirtualTable}.
	 * <P>
	 * This method is an alias for {@link #clearData()}.
	 * <p>
	 */
	public void clear()
	{
		clearData();
	}


	/**
	 * Clears the data of this {@link VirtualTable}.
	 */
	public void clearData()
	{
		this.data.clear();

		// for(int i = 0; i < maxVal.length; i++)
		// {
		// maxVal[i] = 0l;
		// }

		for(final VirtualTableIndex index : this.indices)
		{
			index.clear();
		}

		this.removedRowKeys.clear();

		fireDataChanged();
	}


	/**
	 * Returns the preferred column width within a JTable for a specified
	 * column. If the column has a preferred width set, this width gets used.
	 * Otherwise the maximum of the two following values gets returned:
	 * <ul>
	 * <li>a default value depending on the type of the specified column</li>
	 * <li>the width of the column of the specified JTable</li>
	 * </ul>
	 *
	 * @param col
	 *            the index of the column (0-based)
	 * @param header
	 *            {@link JTableHeader} instance of the JTable
	 *
	 * @param tableCol
	 *            the index of the column within the JTable
	 *
	 * @return the preferred width
	 */
	public int getPreferredColWidth(final int col, final JTableHeader header, final int tableCol)
	{
		int width = this.columns[col].getPreferredWidth();
		if(width > 0)
		{
			return width;
		}

		switch(this.columns[col].getType())
		{
			case CHAR:
			case VARCHAR:
			case LONGVARCHAR:
			case CLOB:
				width = Math.min(300,Math.max(50,this.columns[col].getLength() * 7));
			break;
			
			case TINYINT:
			case SMALLINT:
				width = 50;
			break;
			
			case INTEGER:
			case BIGINT:
			case REAL:
			case FLOAT:
			case DOUBLE:
			case DECIMAL:
			case NUMERIC:
				width = 100;
			break;
			
			case DATE:
			case TIME:
				width = 100;
			break;
			
			case TIMESTAMP:
				width = 150;
			break;
			
			default:
				width = 100;
		}

		TableCellRenderer renderer = header.getColumnModel().getColumn(tableCol)
				.getHeaderRenderer();
		if(renderer == null)
		{
			renderer = header.getDefaultRenderer();
		}

		return Math
				.max(width,
						renderer.getTableCellRendererComponent(header.getTable(),
								getColumnCaption(col),false,false,0,tableCol)
								.getPreferredSize().width);
	}


	/**
	 * Creates and returns a new {@link VirtualTableRow}. The values within the
	 * row get set to their default values. <br>
	 * Autoincremented values are set to -1.
	 *
	 * @return a new {@link VirtualTableRow}
	 */
	public VirtualTableRow createRow()
	{
		return createRowImpl(null);
	}


	private VirtualTableRow createRowForInsert(final List<VirtualTableColumn<?>> except)
	{
		return createRowImpl(except);
	}


	private VirtualTableRow createRowImpl(final List<VirtualTableColumn<?>> except)
	{
		final VirtualTableRow row = new VirtualTableRow(this.columns.length);

		for(int col = 0; col < this.columns.length; col++)
		{
			if(this.columns[col].isAutoIncrement())
			{
				row.set(col,getNextAutoVal(this.columns[col]),false,false);
			}
			else if(except == null || !except.contains(this.columns[col]))
			{
				// row.set(col,checkValue(col,columns[col].getDefaultValue()),false,false);
				// Don't check, this is done when the row is inserted in the VT
				row.set(col,this.columns[col].getDefaultValue(),false,false);
			}
		}

		return row;
	}


	/**
	 * Adds the data contained in the specified result to the
	 * {@link VirtualTable}.
	 * <p>
	 * Alias method for
	 * {@link #addData(Result, VirtualTableColumn[], VirtualTableFillMethod)}.
	 * </p>
	 *
	 * @param result
	 *            a {@link Result} containing data to be added
	 * @param columns
	 *            array of {@link VirtualTableColumn} objects for retrieving
	 *            column indices to be used for this add operation. If columns
	 *            is null, the indices of the result are used.
	 * @throws VirtualTableException
	 * @throws DBException
	 *             if access to the result fails
	 * @see VirtualTableFillMethod
	 */
	public void addData(final Result result, final VirtualTableColumn[] columns)
			throws VirtualTableException, DBException
	{
		addData(result,columns,VirtualTableFillMethod.APPEND);
	}


	/**
	 * Adds the data contained in the specified result to the
	 * {@link VirtualTable}.
	 * <p>
	 * Alias method for {@link #addData(Result, int[], VirtualTableFillMethod)}.
	 * </p>
	 *
	 * @param result
	 *            a {@link Result} containing data to be added
	 * @param columns
	 *            array of {@link VirtualTableColumn} objects for retrieving
	 *            column indices to be used for this add operation. If columns
	 *            is null, the indices of the result are used.
	 * @param method
	 *            depending of the value of this enum:
	 *            <ul>
	 *            <li>existing data get overwritten</li>
	 *            <li>new data gets prepended in front of existing data</li>
	 *            <li>new data gets appended after existing data</li>
	 *            </ul>
	 * @throws VirtualTableException
	 * @throws DBException
	 *             if access to the result fails
	 * @see VirtualTableFillMethod
	 */
	public void addData(final Result result, final VirtualTableColumn[] columns,
			final VirtualTableFillMethod method) throws VirtualTableException, DBException
	{
		final int[] colsToFill = new int[columns.length];
		for(int i = 0; i < columns.length; i++)
		{
			colsToFill[i] = getColumnIndex(columns[i]);
		}

		addData(result,colsToFill,method);
	}


	/**
	 * Adds the data contained in the specified result to the
	 * {@link VirtualTable}. The new data gets appended in front of existing
	 * data.
	 * <p>
	 * Alias method for {@link #addData(Result, VirtualTableFillMethod)}.
	 * </p>
	 *
	 * @param result
	 *            a {@link Result} containing data to be added
	 * @throws VirtualTableException
	 * @throws DBException
	 *             if access to the result fails
	 */
	public void addData(final Result result) throws VirtualTableException, DBException
	{
		addData(result,VirtualTableFillMethod.APPEND);
	}


	/**
	 * Adds the data contained in the specified result to the
	 * {@link VirtualTable}.
	 * <p>
	 * Alias method for {@link #addData(Result, int[], VirtualTableFillMethod)}.
	 * </p>
	 *
	 * @param result
	 *            a {@link Result} containing data to be added
	 * @param method
	 *            depending of the value of this enum:
	 *            <ul>
	 *            <li>existing data get overwritten</li>
	 *            <li>new data gets prepended in front of existing data</li>
	 *            <li>new data gets appended after existing data</li>
	 *            </ul>
	 *
	 * @throws VirtualTableException
	 *
	 * @throws DBException
	 *             if access to the result fails
	 *
	 * @see VirtualTableFillMethod
	 */
	public void addData(final Result result, final VirtualTableFillMethod method)
			throws VirtualTableException, DBException
	{
		addData(result,getColumnIndices(result,true),method);
	}


	/**
	 * Adds the data contained in the specified result to the
	 * {@link VirtualTable}. The new data gets appended in front of existing
	 * data.
	 * <p>
	 * Alias method for
	 * {@link #addData(Result, VirtualTableColumn[], VirtualTableFillMethod)}.
	 * </p>
	 *
	 * @param result
	 *            result a {@link Result} containing data to be added
	 * @param colIndices
	 *            column indices to be used for this add operation. If
	 *            colIndices is null, the indices of the result are used.
	 * @throws VirtualTableException
	 * @throws DBException
	 *             if access to the result fails
	 */
	public void addData(final Result result, final int[] colIndices)
			throws VirtualTableException, DBException
	{
		addData(result,colIndices,VirtualTableFillMethod.APPEND);
	}


	/**
	 * Adds the data contained in the specified result to the
	 * {@link VirtualTable}.
	 *
	 * @param result
	 *            a {@link Result} containing data to be added
	 * @param colIndices
	 *            column indices to be used for this add operation. If
	 *            colIndices is null, the indices of the result are used.
	 * @param method
	 *            depending of the value of this enum:
	 *            <ul>
	 *            <li>existing data get overwritten</li>
	 *            <li>new data gets prepended in front of existing data</li>
	 *            <li>new data gets appended after existing data</li>
	 *            </ul>
	 *
	 * @throws VirtualTableException
	 *
	 * @throws DBException
	 *             if access to the result fails
	 *
	 * @see VirtualTableFillMethod
	 */
	public void addData(final Result result, final int[] colIndices,
			final VirtualTableFillMethod method) throws VirtualTableException, DBException
	{
		try
		{
			addDataImpl(result,colIndices,method);
		}
		finally
		{
			result.close();
		}
	}


	private void addDataImpl(final Result result, int[] colIndices,
			final VirtualTableFillMethod method) throws VirtualTableException, DBException
	{
		setLastQuery(result.getQueryInfo());

		if(colIndices == null)
		{
			colIndices = getColumnIndices(result,true);
		}
		this.lastQueryIndices = colIndices;

		final int[] srcCols = new int[this.columns.length];
		// long[] nextAutoVal = new long[columns.length];

		for(int c = 0; c < this.columns.length; c++)
		{
			srcCols[c] = -1;
			// nextAutoVal[c] = 0;
		}

		for(int c = 0; c < colIndices.length; c++)
		{
			if(colIndices[c] != -1)
			{
				srcCols[colIndices[c]] = c;
			}
		}

		// for(int c = 0; c < columns.length; c++)
		// {
		// if(srcCols[c] == -1 && columns[c].isAutoIncrement())
		// {
		// nextAutoVal[c] = getNextAutoVal(c);
		// }
		// }

		if(method == VirtualTableFillMethod.OVERWRITE)
		{
			clear();
		}

		VirtualTableRow row;
		Object val;
		int index = 0;
		while(result.next())
		{
			row = new VirtualTableRow(this.columns.length);
			for(int c = 0; c < this.columns.length; c++)
			{
				if(srcCols[c] == -1)
				{
					if(this.columns[c].isAutoIncrement())
					{
						// long auto_val = nextAutoVal[c]++;
						row.set(c,getNextAutoVal(this.columns[c]),false,false);
					}
					else
					{
						row.set(c,this.columns[c].getDefaultValue(),false,false);
					}
				}
				else
				{
					val = result.getObject(srcCols[c]);

					row.set(c,checkValue(c,val),false,false);
				}
			}

			switch(method)
			{
				case PREPEND:
					this.data.insert(row,index,false);
				break;
				
				default:
					this.data.add(row,false);
				break;
			}

			index++;
		}

		fireDataChanged();
	}


	/**
	 * Returns always -1.<br>
	 * It's inconsistent with the database anyway -> get the original values.
	 *
	 * @return -1
	 * @see VTWriteDBHandler
	 */
	private Number getNextAutoVal(final VirtualTableColumn col)
	{
		switch(col.getType())
		{
			case TINYINT:
				return (byte)-1;
			case SMALLINT:
				return (short)-1;
			case INTEGER:
				return -1;
			case BIGINT:
				return (long)-1;
		}

		throw new VirtualTableException(this,
				col.getName() + "(" + col.getType() + ") is not auto incrementable");
		// return ++maxVal[col];
	}


	/**
	 * Returns a {@link XdevList} of the column captions for this
	 * <codeVirtualTable</code>.
	 *
	 * @return the column captions
	 */
	public XdevList getCaptions()
	{
		final XdevList captions = new XdevList(this.columns.length);
		for(int i = 0; i < this.columns.length; i++)
		{
			captions.addElement(this.columns[i].getName());
		}

		return captions;
	}


	/**
	 * Fills the elements of a form, with the values of a specified row.
	 *
	 * @param form
	 *            the {@link Formular} to be filled
	 * @param rowForForm
	 *            the index (0-based) of the row, which value is to be used for
	 *            filling the form
	 *
	 * @throws VirtualTableException
	 *
	 * @throws IndexOutOfBoundsException
	 *             if rowForForm is not within the range of rows for this
	 *             Virtual Table.
	 */
	public void fillFormular(final Formular form, final int rowForForm)
			throws VirtualTableException, IndexOutOfBoundsException
	{
		MathUtils.checkRange(rowForForm,0,getRowCount() - 1);
		form.setModel(this.data.get(rowForForm));
	}


	/**
	 * Stores the data of this Virtual Table in a local file specified by the
	 * user. This method opens a file chosser and lets the user specify the
	 * file.
	 *
	 * @throws IOException
	 *
	 * @see #loadLocal()
	 */
	public void saveLocal() throws IOException
	{
		try
		{
			final JFileChooser fc = UIUtils.getSharedFileChooser();
			if(fc.showSaveDialog(UIUtils.getActiveWindow()) == JFileChooser.APPROVE_OPTION)
			{
				final File f = fc.getSelectedFile();

				final XdevObjectOutputStream out = new XdevObjectOutputStream(
						new GZIPOutputStream(new FileOutputStream(f)));

				try
				{
					write(out);
				}
				finally
				{
					out.close();
				}
			}
		}
		catch(final IOException e)
		{
			throw new IOException(e);
		}
	}


	/**
	 * Writes the data of this VirtualTable to a specified
	 * XdevObjectOutputStream.
	 *
	 * @param out
	 *            {@link XdevObjectOutputStream} to write the data to
	 * @throws IOException
	 *             if data can't be written
	 */
	public void write(final XdevObjectOutputStream out) throws IOException
	{
		try
		{
			out.writeUTF(FILE_HEADER);
			out.writeInt(0);
			out.writeUTF(this.name);

			final int rowc = getRowCount();
			out.writeInt(rowc);
			out.writeInt(this.columns.length);
			for(int ri = 0; ri < rowc; ri++)
			{
				for(int ci = 0; ci < this.columns.length; ci++)
				{
					try
					{
						out.writeObject(getValueAt(ri,ci));
					}
					catch(final Exception e)
					{
						throw new IOException(e.getMessage());
					}
				}
			}
		}
		catch(final IOException e)
		{
			throw new IOException(e);
		}
	}


	/**
	 * Loads stored data from a user specified file into this
	 * {@link VirtualTable}. This method opens a file chosser and lets the user
	 * specify the file.
	 *
	 * @throws IOException
	 *             if data can't be read
	 * @see #saveLocal()
	 */
	public void loadLocal() throws IOException
	{
		try
		{
			final JFileChooser fc = UIUtils.getSharedFileChooser();
			if(fc.showOpenDialog(UIUtils.getActiveWindow()) == JFileChooser.APPROVE_OPTION)
			{
				final File f = fc.getSelectedFile();

				final XdevObjectInputStream in = new XdevObjectInputStream(
						new GZIPInputStream(new FileInputStream(f)));

				try
				{
					read(in);
				}
				finally
				{
					in.close();
				}
			}
		}
		catch(final IOException e)
		{
			throw new IOException(e);
		}
	}


	/**
	 * Reads data from an {@link XdevObjectInputStream}.
	 *
	 * @param in
	 *            {@link XdevObjectInputStream} to read from
	 * @throws IOException
	 *             if file couldn't be opened for loading data
	 */
	public void read(final XdevObjectInputStream in) throws IOException
	{
		try
		{
			String error = null;
			if(in.readUTF().equals(FILE_HEADER))
			{
				in.readInt();
				in.readUTF();

				final int rowc = in.readInt(), colc = in.readInt();
				if(colc == this.columns.length)
				{
					this.data = new VirtualTableData(rowc);
					for(int i = 0; i < rowc; i++)
					{
						final VirtualTableRow rowData = new VirtualTableRow(colc);
						for(int j = 0; j < colc; j++)
						{
							try
							{
								rowData.set(j,in.readObject(),false,false);
							}
							catch(final Exception e)
							{
								throw new IOException(e.getMessage());
							}
						}
						this.data.add(rowData,false);
					}
				}
				else
				{
					error = "Wrong VT";
				}
			}
			else
			{
				error = "Invalid file";
			}

			if(error != null)
			{
				throw new IOException(error);
			}
		}
		catch(final IOException e)
		{
			throw new IOException(e);
		}
	}


	/**
	 * Imports data from CSV (Comma Separated Values) information.
	 * <p>
	 * This is an alias for
	 * {@link #importCSV(Reader, char, String, boolean, int)}.
	 * </p>
	 *
	 * @param reader
	 *            {@link Reader} to read the CSV information from
	 * @throws IOException
	 *             if reading of CSV file fails
	 * @throws VirtualTableException
	 */
	public void importCSV(final Reader reader) throws IOException, VirtualTableException
	{
		importCSV(reader,',',"null",true,0);
	}


	/**
	 * Imports data from CSV (Comma Separated Values) information.
	 * <p>
	 * This is an alias for
	 * {@link #importCSV(Reader, char, String, boolean, int)}.
	 * </p>
	 *
	 * @param reader
	 *            {@link Reader} to read the CSV information from
	 * @param separator
	 *            char used to separate the column values (defaults to comma)
	 * @throws IOException
	 *             if reading of CSV file fails
	 * @throws VirtualTableException
	 */
	public void importCSV(final Reader reader, final char separator)
			throws IOException, VirtualTableException
	{
		importCSV(reader,separator,"null",true,0);
	}


	/**
	 * Imports data from CSV (Comma Separated Values) information.
	 * <p>
	 * This is an alias for
	 * {@link #importCSV(Reader, char, String, boolean, int)}.
	 * </p>
	 *
	 * @param reader
	 *            {@link Reader} to read the CSV information from
	 * @param separator
	 *            char used to separate the column values (defaults to comma)
	 * @param nullValue
	 *            a {@link String} which is interpreted as <code>null</code> by
	 *            the VirtualTable
	 * @throws IOException
	 *             if reading of CSV file fails
	 * @throws VirtualTableException
	 */
	public void importCSV(final Reader reader, final char separator, final String nullValue)
			throws IOException, VirtualTableException
	{
		importCSV(reader,separator,nullValue,true,0);
	}


	/**
	 * Imports data from CSV (Comma Separated Values) information.
	 * <p>
	 * This is an alias for
	 * {@link #importCSV(Reader, char, String, boolean, int)}.
	 * </p>
	 *
	 * @param reader
	 *            {@link Reader} to read the CSV information from
	 * @param separator
	 *            char used to separate the column values (defaults to comma)
	 * @param nullValue
	 *            a {@link String} which is interpreted as <code>null</code> by
	 *            the VirtualTable
	 * @param useCsvHeaders
	 *            if set to true, the first line of the CSV file is interpreted
	 *            as column names
	 * @throws IOException
	 *             if reading of CSV file fails
	 * @throws VirtualTableException
	 */
	public void importCSV(final Reader reader, final char separator, final String nullValue,
			final boolean useCsvHeaders) throws IOException, VirtualTableException
	{
		importCSV(reader,separator,nullValue,useCsvHeaders,0);
	}


	/**
	 * Imports data from CSV (Comma Separated Values) information.
	 *
	 * @param reader
	 *            {@link Reader} to read the CSV information from
	 * @param separator
	 *            char used to separate the column values (defaults to comma)
	 * @param nullValue
	 *            a {@link String} which is interpreted as <code>null</code> by
	 *            the VirtualTable
	 * @param useCsvHeaders
	 *            if set to true, the first line of the CSV file is interpreted
	 *            as column names
	 * @param skipLines
	 *            int value, skips the specified number of lines at the start,
	 *            when reading the CSV file
	 * @throws IOException
	 *             if reading of CSV file fails
	 *
	 * @throws VirtualTableException
	 *
	 * @see #exportCSV(Writer, int, String, char, List, boolean)
	 */
	public synchronized void importCSV(final Reader reader, final char separator,
			final String nullValue, final boolean useCsvHeaders, final int skipLines)
			throws IOException, VirtualTableException
	{
		importCSV(reader,separator,CSVParser.DEFAULT_QUOTE_CHARACTER,nullValue,useCsvHeaders,
				skipLines);
	}


	/**
	 * Imports data from CSV (Comma Separated Values) information.
	 *
	 * @param reader
	 *            {@link Reader} to read the CSV information from
	 * @param separator
	 *            char used to separate the column values (defaults to comma)
	 * @param quotechar
	 *            the character to use for quoted elements
	 * @param nullValue
	 *            a {@link String} which is interpreted as <code>null</code> by
	 *            the VirtualTable
	 * @param useCsvHeaders
	 *            if set to true, the first line of the CSV file is interpreted
	 *            as column names
	 * @param skipLines
	 *            int value, skips the specified number of lines at the start,
	 *            when reading the CSV file
	 * @throws IOException
	 *             if reading of CSV file fails
	 *
	 * @throws VirtualTableException
	 *
	 * @see #exportCSV(Writer, int, String, char, List, boolean)
	 *
	 * @since 3.1
	 */
	public synchronized void importCSV(final Reader reader, final char separator,
			final char quotechar, final String nullValue, final boolean useCsvHeaders,
			final int skipLines) throws IOException, VirtualTableException
	{
		final CSVReader csvReader = new CSVReader(reader,separator,quotechar);
		importCSV(csvReader,nullValue,useCsvHeaders,skipLines);
	}


	/**
	 * Imports data from CSV (Comma Separated Values) information.
	 *
	 * @param csvReader
	 *            the input source
	 * @param nullValue
	 *            a {@link String} which is interpreted as <code>null</code> by
	 *            the VirtualTable
	 * @param useCsvHeaders
	 *            if set to true, the first line of the CSV file is interpreted
	 *            as column names
	 * @param skipLines
	 *            int value, skips the specified number of lines at the start,
	 *            when reading the CSV file
	 * @throws IOException
	 *             if reading of CSV file fails
	 *
	 * @throws VirtualTableException
	 *
	 * @see #exportCSV(Writer, int, String, char, List, boolean)
	 *
	 * @since 3.1
	 */
	public synchronized void importCSV(final CSVReader csvReader, final String nullValue,
			final boolean useCsvHeaders, final int skipLines)
			throws IOException, VirtualTableException
	{
		final List<VirtualTableColumn<?>> columns = new ArrayList();
		if(useCsvHeaders)
		{
			final String[] record = csvReader.readNext();
			if(record == null)
			{
				return;
			}

			for(final String name : record)
			{
				final VirtualTableColumn<?> col = getColumn(name);
				if(col != null)
				{
					columns.add(col);
				}
			}
		}
		else
		{
			for(int i = 0; i < this.columns.length; i++)
			{
				columns.add(this.columns[i]);
			}
		}

		for(int i = 0; i < skipLines; i++)
		{
			if(csvReader.readNext() == null)
			{
				return;
			}
		}

		final int vtColumnCount = columns.size();
		final int[] vtColumnIndex = new int[vtColumnCount];
		for(int i = 0; i < vtColumnCount; i++)
		{
			vtColumnIndex[i] = getColumnIndex(columns.get(i));
		}

		final List rowData = new ArrayList();
		String[] record;
		while((record = csvReader.readNext()) != null)
		{
			final VirtualTableRow row = createRowForInsert(columns);

			final int readerColumnCount = record.length;
			for(int i = 0; i < readerColumnCount; i++)
			{
				Object value;
				final String str = record[i];
				if(nullValue.equals(str))
				{
					value = null;
				}
				else
				{
					value = checkValue(vtColumnIndex[i],str);
				}

				row.set(vtColumnIndex[i],value,false,true);
			}

			this.data.add(row,true);
			fireRowInserted(row,row.index);

			rowData.clear();
		}
	}


	/**
	 * Exports data as CSV information to a specified {@link Writer}.
	 * <p>
	 * This is an alias for {@link #exportCSV(Writer, int)}.
	 * </p>
	 *
	 * @param writer
	 *            a {@link Writer} to write CSV information to
	 * @throws IOException
	 *             if CSV file couldn't be written
	 */
	public void exportCSV(final Writer writer) throws IOException
	{
		exportCSV(writer,0);
	}


	/**
	 * Exports data as CSV information to a specified {@link Writer}.
	 * <p>
	 * This is an alias for {@link #exportCSV(Writer, int, String)}.
	 * </p>
	 *
	 * @param writer
	 *            a {@link Writer} to write CSV information to
	 * @param startRow
	 *            the start row within the data of the Virtual Table
	 * @throws IOException
	 *             if CSV file couldn't be written
	 */
	public void exportCSV(final Writer writer, final int startRow) throws IOException
	{
		exportCSV(writer,startRow,"null");
	}


	/**
	 * Exports data as CSV information to a specified {@link Writer}.
	 * <p>
	 * This is an alias for {@link #exportCSV(Writer, int, String, char)}.
	 * </p>
	 *
	 * @param writer
	 *            a {@link Writer} to write CSV information to
	 * @param startRow
	 *            the start row within the data of the Virtual Table
	 * @param nullValue
	 *            a {@link String} representation for <code>null</code>-values
	 *            in the VirtualTable
	 * @throws IOException
	 *             if CSV file couldn't be written
	 */
	public void exportCSV(final Writer writer, final int startRow, final String nullValue)
			throws IOException
	{
		exportCSV(writer,startRow,nullValue,',');
	}


	/**
	 * Exports data as CSV information to a specified {@link Writer}.
	 * <p>
	 * This is an alias for {@link #exportCSV(Writer, int, String, char, List)}.
	 * </p>
	 *
	 * @param writer
	 *            a {@link Writer} to write CSV information to
	 * @param startRow
	 *            the start row within the data of the Virtual Table
	 * @param nullValue
	 *            a {@link String} representation for <code>null</code>-values
	 *            in the VirtualTable
	 * @param delimiter
	 *            the delimiter used to separate fields in the CSV file
	 * @throws IOException
	 *             if CSV file couldn't be written
	 */
	public void exportCSV(final Writer writer, final int startRow, final String nullValue,
			final char delimiter) throws IOException
	{
		exportCSV(writer,startRow,nullValue,delimiter,getColumnNames());
	}


	/**
	 * Exports data as CSV information to a specified {@link Writer}.
	 * <p>
	 * This is an alias for
	 * {@link #exportCSV(Writer, int, String, char, List, boolean)}.
	 * </p>
	 *
	 * @param writer
	 *            a {@link Writer} to write CSV information to
	 * @param startRow
	 *            the start row within the data of the Virtual Table
	 * @param nullValue
	 *            a {@link String} representation for <code>null</code>-values
	 *            in the VirtualTable
	 * @param delimiter
	 *            the delimiter used to separate fields in the CSV file
	 * @param columnNames
	 *            a list of the columnNames used as headers in the CSV file. No
	 *            header is generated if this value is <code>null</code>.
	 * @throws IOException
	 *             if CSV file couldn't be written
	 */
	public void exportCSV(final Writer writer, final int startRow, final String nullValue,
			final char delimiter, final List<String> columnNames) throws IOException
	{
		exportCSV(writer,startRow,nullValue,delimiter,columnNames,true);
	}


	/**
	 * Exports data as CSV information to a specified {@link Writer}.
	 *
	 * @param writer
	 *            a {@link Writer} to write CSV information to
	 * @param startRow
	 *            the start row within the data of the Virtual Table
	 * @param nullValue
	 *            a {@link String} representation for <code>null</code>-values
	 *            in the VirtualTable
	 * @param delimiter
	 *            the delimiter used to separate fields in the CSV file
	 * @param columnNames
	 *            a list of the columnNames used as headers in the CSV file. No
	 *            header is generated if this value is <code>null</code>.
	 * @param writeColumnNames
	 *            if set to true, a header line is generated in the CSV-file
	 * @throws IOException
	 *             if CSV file couldn't be written
	 */
	public synchronized void exportCSV(final Writer writer, final int startRow,
			final String nullValue, final char delimiter, final List<String> columnNames,
			final boolean writeColumnNames) throws IOException
	{
		exportCSV(writer,startRow,nullValue,delimiter,CSVParser.DEFAULT_QUOTE_CHARACTER,columnNames,
				writeColumnNames);
	}


	/**
	 * Exports data as CSV information to a specified {@link Writer}.
	 *
	 * @param writer
	 *            a {@link Writer} to write CSV information to
	 * @param startRow
	 *            the start row within the data of the Virtual Table
	 * @param nullValue
	 *            a {@link String} representation for <code>null</code>-values
	 *            in the VirtualTable
	 * @param delimiter
	 *            the delimiter used to separate fields in the CSV file
	 * @param quotechar
	 *            the character to use for quoted elements
	 * @param columnNames
	 *            a list of the columnNames used as headers in the CSV file. No
	 *            header is generated if this value is <code>null</code>.
	 * @param writeColumnNames
	 *            if set to true, a header line is generated in the CSV-file
	 * @throws IOException
	 *             if CSV file couldn't be written
	 *
	 * @since 3.1
	 */
	public synchronized void exportCSV(final Writer writer, final int startRow,
			final String nullValue, final char delimiter, final char quotechar,
			final List<String> columnNames, final boolean writeColumnNames) throws IOException
	{
		final CSVWriter csvWriter = new CSVWriter(writer,delimiter,quotechar);
		exportCSV(csvWriter,startRow,nullValue,columnNames,writeColumnNames);
	}


	/**
	 * Exports data as CSV information to a specified {@link Writer}.
	 *
	 * @param csvWriter
	 *            the csv writer
	 * @param startRow
	 *            the start row within the data of the Virtual Table
	 * @param nullValue
	 *            a {@link String} representation for <code>null</code>-values
	 *            in the VirtualTable
	 * @param columnNames
	 *            a list of the columnNames used as headers in the CSV file. No
	 *            header is generated if this value is <code>null</code>.
	 * @param writeColumnNames
	 *            if set to true, a header line is generated in the CSV-file
	 * @throws IOException
	 *             if CSV file couldn't be written
	 *
	 * @since 3.1
	 */
	public synchronized void exportCSV(final CSVWriter csvWriter, final int startRow,
			final String nullValue, List<String> columnNames, boolean writeColumnNames)
			throws IOException
	{
		if(columnNames == null)
		{
			writeColumnNames = false;
			columnNames = getColumnNames();
		}

		final int columnCount = columnNames.size();
		final int[] colIndices = new int[columnCount];
		for(int i = 0; i < columnCount; i++)
		{
			final String columnName = columnNames.get(i).toString();
			colIndices[i] = getColumnIndex(columnName);
			if(colIndices[i] == -1)
			{
				throw new VirtualTableException(this,"Column '" + columnName + "' not found");
			}
		}

		if(writeColumnNames)
		{
			csvWriter.writeNext(columnNames.toArray(new String[columnNames.size()]));
		}

		final String[] line = new String[columnCount];
		final int rowCount = getRowCount();
		for(int row = startRow; row < rowCount; row++)
		{
			for(int col = 0; col < columnCount; col++)
			{
				final Object o = getValueAt(row,colIndices[col]);
				line[col] = o == null ? nullValue : formatValue(o,colIndices[col]);
			}
			csvWriter.writeNext(line);
		}
	}


	/**
	 * Returns the current cursor position (row number) of this
	 * {@link VirtualTable}.
	 *
	 * @return the current cursor position
	 */
	public int getCursorPos()
	{
		return this.cursorpos;
	}


	/**
	 * Sets the current cursor position (row number) for this
	 * {@link VirtualTable}.
	 *
	 * @param cursorpos
	 *            the current cursor position
	 */
	public void setCursorPos(int cursorpos)
	{
		if(cursorpos < -1)
		{
			cursorpos = -1;
		}

		this.cursorpos = cursorpos;
	}


	/**
	 * Resets the current cursor position (row number) for this
	 * {@link VirtualTable}. This positions the cursor in front of the first row
	 * in the table
	 */
	public void resetCursor()
	{
		this.cursorpos = -1;
	}


	/**
	 * Positions the cursor at the next row.
	 *
	 * @return true, if the next position points at a valid row
	 */
	public boolean next()
	{
		this.cursorpos++;
		return this.cursorpos < getRowCount();
	}


	/**
	 * Returns the row index of the first occurence of the specified value
	 * within the specified column.
	 *
	 * @param columnName
	 *            the name of the column to search for the value
	 * @param val
	 *            the value to search for
	 * @return the row index or -1, if no occurence of value is found
	 */
	public int getRowIndex(final String columnName, final Object val)
	{
		return getRowIndex(columnName,val,0);
	}


	/**
	 * Returns the row index of the first occurence of the specified value
	 * within the specified column.
	 *
	 * @param columnName
	 *            the name of the column to search for the value
	 * @param val
	 *            the value to search for
	 * @param startIndex
	 *            the row index to start the search from
	 * @return the row index or -1, if no occurence of value is found
	 */
	public int getRowIndex(final String columnName, final Object val, final int startIndex)
	{
		int index = -1;
		final int col = getColumnIndex(columnName);
		if(col != -1)
		{
			final DataType type = getColumnAt(col).getType();
			final int max = getRowCount();
			Object o;
			for(int row = startIndex; row < max && index == -1; row++)
			{
				o = getValueAt(row,col);
				if(equals(o,val,type))
				{
					index = row;
				}
			}
		}
		return index;
	}


	/**
	 * Returns an array of all primary key columns for this {@link VirtualTable}
	 * .
	 *
	 * @return an array of {@link VirtualTableColumn} instances representing all
	 *         primary key columns
	 */
	public VirtualTableColumn<?>[] getPrimaryKeyColumns()
	{
		for(final VirtualTableIndex vtIndex : this.indices)
		{
			if(vtIndex.type == IndexType.PRIMARY_KEY)
			{
				return vtIndex.columns;
			}
		}

		return VirtualTableColumn.NO_COLUMN;
	}


	/**
	 * This is a synonym for
	 * {@link #setModelFor(XdevTree, String, Object, String, String, String, String)}
	 *
	 * @param tree
	 *            the target tree
	 * @param rootsColumnName
	 *            the column name for the root condition
	 * @param rootsID
	 *            the value for the root condition
	 * @param idColumnName
	 *            the id column name
	 * @param ownerColumnName
	 *            the referenced owner column name
	 * @param captionColumnName
	 *            the column name for the caption of the nodes
	 * @param dataColumnName
	 *            the data column name, or <code>null</code> if the
	 *            {@link VirtualTableRow}s should be used as data object
	 *
	 * @throws VirtualTableException
	 *             if one of the columns cannot be found
	 */
	public void fillTree(final XdevTree tree, final String rootsColumnName, final Object rootsID,
			final String idColumnName, final String ownerColumnName, final String captionColumnName,
			final String dataColumnName) throws VirtualTableException
	{
		setModelFor(tree,rootsColumnName,rootsID,idColumnName,ownerColumnName,captionColumnName,
				dataColumnName);
	}


	/**
	 * This is a synonym for
	 * {@link #setModelFor(XdevTree, String, Object, String, String, String, String, boolean)}
	 *
	 * @param tree
	 *            the target tree
	 * @param rootsColumnName
	 *            the column name for the root condition
	 * @param rootsID
	 *            the value for the root condition
	 * @param idColumnName
	 *            the id column name
	 * @param ownerColumnName
	 *            the referenced owner column name
	 * @param captionColumnName
	 *            the column name for the caption of the nodes
	 * @param dataColumnName
	 *            the data column name, or <code>null</code> if the
	 *            {@link VirtualTableRow}s should be used as data object
	 * @param multipleRoots
	 *            <code>true</code> for a dummy root with multiple sub-roots, or
	 *            <code>false</code> for a single root
	 *
	 * @throws VirtualTableException
	 *             if one of the columns cannot be found
	 *
	 * @since 3.1
	 */
	public void fillTree(final XdevTree tree, final String rootsColumnName, final Object rootsID,
			final String idColumnName, final String ownerColumnName, final String captionColumnName,
			final String dataColumnName, final boolean multipleRoots) throws VirtualTableException
	{
		setModelFor(tree,rootsColumnName,rootsID,idColumnName,ownerColumnName,captionColumnName,
				dataColumnName,multipleRoots);
	}


	/**
	 * Creates a tree model for the <code>tree</code> containing data of this
	 * VirtualTable by creating {@link XdevTreeNode}s out of
	 * {@link VirtualTableRow}s.
	 * <p>
	 * This is a synonym for:
	 *
	 * <pre>
	 * setModelFor(tree,rootsColumnName,rootsID,idColumnName,ownerColumnName,captionColumnName,
	 * 		dataColumnName,false);
	 * </pre>
	 * <p>
	 * For a detailed description see
	 * {@link #createTree(String, Object, String, String, String, String, boolean)}
	 *
	 * @param tree
	 *            the target tree
	 * @param rootsColumnName
	 *            the column name for the root condition
	 * @param rootsID
	 *            the value for the root condition
	 * @param idColumnName
	 *            the id column name
	 * @param ownerColumnName
	 *            the referenced owner column name
	 * @param captionColumnName
	 *            the column name for the caption of the nodes
	 * @param dataColumnName
	 *            the data column name, or <code>null</code> if the
	 *            {@link VirtualTableRow}s should be used as data object
	 *
	 * @throws VirtualTableException
	 *             if one of the columns cannot be found
	 */
	public void setModelFor(final XdevTree tree, final String rootsColumnName, final Object rootsID,
			final String idColumnName, final String ownerColumnName, final String captionColumnName,
			final String dataColumnName) throws VirtualTableException
	{
		setModelFor(tree,rootsColumnName,rootsID,idColumnName,ownerColumnName,captionColumnName,
				dataColumnName,false);
	}


	/**
	 * Creates a tree model for the <code>tree</code> containing data of this
	 * VirtualTable by creating {@link XdevTreeNode}s out of
	 * {@link VirtualTableRow}s.
	 * <p>
	 * For a detailed description see
	 * {@link #createTree(String, Object, String, String, String, String, boolean)}
	 *
	 * @param tree
	 *            the target tree
	 * @param rootsColumnName
	 *            the column name for the root condition
	 * @param rootsID
	 *            the value for the root condition
	 * @param idColumnName
	 *            the id column name
	 * @param ownerColumnName
	 *            the referenced owner column name
	 * @param captionColumnName
	 *            the column name for the caption of the nodes
	 * @param dataColumnName
	 *            the data column name, or <code>null</code> if the
	 *            {@link VirtualTableRow}s should be used as data object
	 * @param multipleRoots
	 *            <code>true</code> for a dummy root with multiple sub-roots, or
	 *            <code>false</code> for a single root
	 *
	 * @throws VirtualTableException
	 *             if one of the columns cannot be found
	 *
	 * @since 3.1
	 */
	public void setModelFor(final XdevTree tree, final String rootsColumnName, final Object rootsID,
			final String idColumnName, final String ownerColumnName, final String captionColumnName,
			final String dataColumnName, final boolean multipleRoots) throws VirtualTableException
	{
		if(tree == null)
		{
			throw new NullPointerException();
		}

		final XdevTreeNode root = createTree(rootsColumnName,rootsID,idColumnName,ownerColumnName,
				captionColumnName,dataColumnName,multipleRoots);
		if(root != null)
		{
			tree.setCellRenderer(new XdevTreeRenderer(new DefaultXdevTreeManager(tree)));
			tree.setRootVisible(!multipleRoots);
			tree.setModel(new XdevTreeModel(root));
		}
	}


	/**
	 * Creates a tree structure containing data of this VirtualTable by creating
	 * {@link XdevTreeNode}s out of {@link VirtualTableRow}s.
	 * <p>
	 * This is a synonym for:
	 *
	 * <pre>
	 * createTree(rootsColumnName,rootsID,idColumnName,ownerColumnName,captionColumnName,
	 * 		dataColumnName,false);
	 * </pre>
	 * <p>
	 * For a detailed description see
	 * {@link #createTree(String, Object, String, String, String, String, boolean)}
	 *
	 * @param rootsColumnName
	 *            the column name for the root condition
	 * @param rootsID
	 *            the value for the root condition
	 * @param idColumnName
	 *            the id column name
	 * @param ownerColumnName
	 *            the referenced owner column name
	 * @param captionColumnName
	 *            the column name for the caption of the nodes
	 * @param dataColumnName
	 *            the data column name, or <code>null</code> if the
	 *            {@link VirtualTableRow}s should be used as data object
	 *
	 * @return the root node of the created tree
	 *
	 * @throws VirtualTableException
	 *             if one of the columns cannot be found
	 */
	public XdevTreeNode createTree(final String rootsColumnName, final Object rootsID,
			final String idColumnName, final String ownerColumnName, final String captionColumnName,
			final String dataColumnName) throws VirtualTableException
	{
		return createTree(rootsColumnName,rootsID,idColumnName,ownerColumnName,captionColumnName,
				dataColumnName,false);
	}


	/**
	 * Creates a tree structure containing data of this VirtualTable by creating
	 * {@link XdevTreeNode}s out of {@link VirtualTableRow}s.
	 * <p>
	 * First you have to define which rows should be the root of the tree. This
	 * is done with the first two parameters: <code>rootsColumnName</code> and
	 * <code>rootsID</code>. Meaning every row where the value of the column
	 * <code>rootsColumnName</code> equals the value <code>rootsID</code>.<br>
	 * If <code>multipleRoots</code> is <code>true</code>, all columns where
	 * this condition is met are taken and connected to a dummy root node which
	 * will be hidden in the tree. Otherwise only the first matching row will be
	 * the root itself.
	 * <p>
	 * The subtrees are created by connecting the id column (
	 * <code>idColumnName</code>) to the owner column (
	 * <code>ownerColumnName</code>). In other words every row is connected as a
	 * child node to another node where the value in the column
	 * <code>idColumnName</code> matches the owner rows' value in the column
	 * <code>ownerColumnName</code>.
	 * <p>
	 * Each created {@link XdevTreeNode} consist of a caption, which is taken
	 * from the column <code>captionColumnnName</code>, and a data object which
	 * is taken from the column <code>dataColumnName</code>. If
	 * <code>dataColumnName</code> is <code>null</code> or the column is not
	 * found, the {@link VirtualTableRow} itself is used as data object.
	 * <p>
	 * Example:
	 * <p>
	 * <table border="0" cellspacing="10">
	 * <tr>
	 * <td valign="top">
	 * <table border="1" cellspacing="0">
	 * <tr>
	 * <th>ID</th>
	 * <th>OWNER_ID</th>
	 * <th>NAME</th>
	 * </tr>
	 * <tr>
	 * <td>1</td>
	 * <td>0</td>
	 * <td>Africa</td>
	 * </tr>
	 * <tr>
	 * <td>2</td>
	 * <td>0</td>
	 * <td>America</td>
	 * </tr>
	 * <tr>
	 * <td>3</td>
	 * <td>0</td>
	 * <td>Asia</td>
	 * </tr>
	 * <tr>
	 * <td>4</td>
	 * <td>0</td>
	 * <td>Europe</td>
	 * </tr>
	 * <tr>
	 * <td>5</td>
	 * <td>1</td>
	 * <td>Egypt</td>
	 * </tr>
	 * <tr>
	 * <td>6</td>
	 * <td>1</td>
	 * <td>Cameroon</td>
	 * </tr>
	 * <tr>
	 * <td>7</td>
	 * <td>2</td>
	 * <td>Canada</td>
	 * </tr>
	 * <tr>
	 * <td>8</td>
	 * <td>2</td>
	 * <td>USA</td>
	 * </tr>
	 * <tr>
	 * <td>9</td>
	 * <td>3</td>
	 * <td>Japan</td>
	 * </tr>
	 * <tr>
	 * <td>10</td>
	 * <td>3</td>
	 * <td>Thailand</td>
	 * </tr>
	 * <tr>
	 * <td>11</td>
	 * <td>4</td>
	 * <td>Germany</td>
	 * </tr>
	 * <tr>
	 * <td>11</td>
	 * <td>4</td>
	 * <td>Spain</td>
	 * </tr>
	 * <tr>
	 * <td>12</td>
	 * <td>8</td>
	 * <td>California</td>
	 * </tr>
	 * <tr>
	 * <td>13</td>
	 * <td>8</td>
	 * <td>Colorado</td>
	 * </tr>
	 * </table>
	 * </td>
	 * <td valign="top">
	 *
	 * <table cellspacing="20">
	 * <tr>
	 * <td valign="top"><code>
	 * rootsColumnName = "OWNER_ID"<br>
	 * rootsID = 0<br>
	 * idColumnName = "ID"<br>
	 * ownerColumnName = "OWNER_ID"<br>
	 * captionColumnName = "NAME"<br>
	 * dataColumnName = null<br>
	 * multipleRoots = true<br>
	 * <br>
	 * ------------------------&gt;
	 * </code></td>
	 * <td valign="top"><code>
	 * root<br>
	 * +-- Africa<br>
	 * |&nbsp;&nbsp;&nbsp;+-- Egypt <br>
	 * |&nbsp;&nbsp;&nbsp;+-- Cameroon<br>
	 * +-- America<br>
	 * |&nbsp;&nbsp;&nbsp;+-- Canada<br>
	 * |&nbsp;&nbsp;&nbsp;+-- USA<br>
	 * |&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;+-- California<br>
	 * |&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;+-- Colorado<br>
	 * +-- Asia<br>
	 * |&nbsp;&nbsp;&nbsp;+-- Japan<br>
	 * |&nbsp;&nbsp;&nbsp;+-- Thailand<br>
	 * +-- Europe<br>
	 * &nbsp;&nbsp;&nbsp;&nbsp;+-- Germany<br>
	 * &nbsp;&nbsp;&nbsp;&nbsp;+-- Spain
	 * </code>
	 * </tr>
	 * <tr>
	 * <td colspan="2">
	 * <hr>
	 * </td>
	 * </tr>
	 * <tr>
	 * <td valign="top"><code>
	 * rootsColumnName = "ID"<br>
	 * rootsID = 2<br>
	 * idColumnName = "ID"<br>
	 * ownerColumnName = "OWNER_ID"<br>
	 * captionColumnName = "NAME"<br>
	 * dataColumnName = null<br>
	 * multipleRoots = false<br>
	 * <br>
	 * ------------------------&gt;
	 * </code></td>
	 * <td valign="top"><code>
	 * America (single root)<br>
	 * +-- Canada<br>
	 * +-- USA<br>
	 * &nbsp;&nbsp;&nbsp;&nbsp;+-- California<br>
	 * &nbsp;&nbsp;&nbsp;&nbsp;+-- Colorado
	 * </code></td>
	 * </tr>
	 * </table>
	 * </td>
	 * </tr>
	 * </table>
	 *
	 * @param rootsColumnName
	 *            the column name for the root condition
	 * @param rootsID
	 *            the value for the root condition
	 * @param idColumnName
	 *            the id column name
	 * @param ownerColumnName
	 *            the referenced owner column name
	 * @param captionColumnName
	 *            the column name for the caption of the nodes
	 * @param dataColumnName
	 *            the data column name, or <code>null</code> if the
	 *            {@link VirtualTableRow}s should be used as data object
	 * @param multipleRoots
	 *            <code>true</code> for a dummy root with multiple sub-roots, or
	 *            <code>false</code> for a single root
	 *
	 * @return the root node of the created tree
	 *
	 * @throws VirtualTableException
	 *             if one of the columns cannot be found
	 *
	 * @since 3.1
	 */
	public XdevTreeNode createTree(final String rootsColumnName, final Object rootsID,
			final String idColumnName, final String ownerColumnName, final String captionColumnName,
			final String dataColumnName, final boolean multipleRoots) throws VirtualTableException
	{
		final int rootsCol = getColumnIndex(rootsColumnName);
		if(rootsCol == -1)
		{
			VirtualTableException.throwColumnNotFound(this,rootsColumnName);
		}

		final int idCol = getColumnIndex(idColumnName);
		if(idCol == -1)
		{
			VirtualTableException.throwColumnNotFound(this,idColumnName);
		}

		final int ownerCol = getColumnIndex(ownerColumnName);
		if(ownerCol == -1)
		{
			VirtualTableException.throwColumnNotFound(this,ownerColumnName);
		}

		final int captionCol = getColumnIndex(captionColumnName);
		if(captionCol == -1)
		{
			VirtualTableException.throwColumnNotFound(this,captionColumnName);
		}

		final IntList rootRows = new IntList();
		Object o;
		for(int i = 0; i < getRowCount(); i++)
		{
			o = getValueAt(i,rootsCol);
			if(equals(o,rootsID,null))
			{
				rootRows.add(i);
				if(!multipleRoots)
				{
					break;
				}
			}
		}

		if(rootRows.size() == 0)
		{
			return null;
		}

		final int dataCol = dataColumnName == null ? -1 : getColumnIndex(dataColumnName);

		XdevTreeNode root;
		if(multipleRoots)
		{
			root = new XdevTreeNode("root");
			for(int i = 0, c = rootRows.size(); i < c; i++)
			{
				final int rootRow = rootRows.get(i);
				final XdevTreeNode node = new XdevTreeNode(
						dataCol >= 0 ? getValueAt(rootRow,dataCol) : "",
						getFormattedValueAt(rootRow,captionCol));
				root.add(node);
				createTree(node,getValueAt(rootRow,idCol),idCol,ownerCol,captionCol,dataCol);
			}
		}
		else
		{
			final int rootRow = rootRows.get(0);
			root = new XdevTreeNode(dataCol >= 0 ? getValueAt(rootRow,dataCol) : "",
					getFormattedValueAt(rootRow,captionCol));
			createTree(root,getValueAt(rootRow,idCol),idCol,ownerCol,captionCol,dataCol);
		}

		return root;
	}


	private void createTree(final XdevTreeNode owner, final Object id, final int idCol,
			final int ownerCol, final int captionCol, final int dataCol)
	{
		Object o;
		for(int row = 0; row < getRowCount(); row++)
		{
			o = getValueAt(row,ownerCol);
			if(equals(o,id,null))
			{
				final Object data = dataCol >= 0 ? getValueAt(row,dataCol) : getRow(row);
				final XdevTreeNode node = new XdevTreeNode(data,
						getFormattedValueAt(row,captionCol));
				owner.add(node);
				createTree(node,getValueAt(row,idCol),idCol,ownerCol,captionCol,dataCol);
			}
		}
	}


	/**
	 * Creates and set the table model for a specified JTable.
	 * <p>
	 * This is an alias for {@link #setModelFor(JTable)}.
	 * </p>
	 *
	 * @param table
	 *            the {@link JTable} to set the model for.
	 */
	public void fillTable(final JTable table)
	{
		setModelFor(table);
	}


	/**
	 * Creates and set the table model for a specified JTable.
	 * <p>
	 * This is an alias for {@link #setModelFor(JTable)}.
	 * </p>
	 *
	 * @param table
	 *            the {@link JTable} to set the model for.
	 */
	public void setModelFor(final JTable table)
	{
		table.setModel(createTableModel());
	}


	/**
	 * Creates and returns a {@link TableModel} for this {@link VirtualTable}.
	 * Only Visible Columns are included.
	 *
	 * @return the {@link TableModel}
	 */
	public TableModel createTableModel()
	{
		return createTableModel(getVisibleColumnIndices());
	}


	/**
	 * Creates and returns a {@link TableModel} for this {@link VirtualTable}.
	 *
	 * @param columnNames
	 *            a array of columnNames to be included in the
	 *            {@link TableModel}.
	 *
	 * @return a {@link TableModel}
	 */
	public TableModel createTableModel(final String... columnNames)
	{
		final IntList list = new IntList();

		for(final String name : columnNames)
		{
			final int columnIndex = getColumnIndex(name);
			if(columnIndex >= 0)
			{
				list.add(columnIndex);
			}
		}

		return createTableModel(list.toArray());
	}


	/**
	 * Creates and returns a {@link TableModel} for this {@link VirtualTable}.
	 *
	 * @param columnNames
	 *            a {@link List} of columnNames to be included in the
	 *            {@link TableModel}.
	 *
	 * @return a {@link TableModel}
	 */
	public TableModel createTableModel(final List<String> columnNames)
	{
		final IntList list = new IntList();

		for(final String name : columnNames)
		{
			final int columnIndex = getColumnIndex(name);
			if(columnIndex >= 0)
			{
				list.add(columnIndex);
			}
		}

		return createTableModel(list.toArray());
	}


	/**
	 * Creates and returns a {@link TableModel} for this {@link VirtualTable}.
	 *
	 * @param columnIndices
	 *            the column indexed to be included in the {@link TableModel}.
	 * @return a {@link TableModel}
	 */
	public TableModel createTableModel(final int... columnIndices)
	{
		return new VirtualTableModel(this,columnIndices);
	}


	/**
	 * @deprecated Typo, use {@link #createTableModel(int...)} instead
	 */
	@Deprecated
	public TableModel createTabelModel(final int... columnIndices)
	{
		return createTableModel(columnIndices);
	}



	/**
	 * Helper method interface to select columns.
	 *
	 * @see VirtualTable#getColumns(ColumnSelector)
	 * @see VirtualTable#getColumnIndices(ColumnSelector)
	 */
	public static interface ColumnSelector
	{
		/**
		 * @return <code>true</code> if the column should be selected,
		 *         <code>false</code> otherwise
		 */
		public boolean select(VirtualTableColumn<?> column);
	}


	/**
	 * Returns all {@link VirtualTableColumn}s of this VirtualTable according to
	 * <code>selector</code>.
	 *
	 * @param selector
	 *            the {@link ColumnSelector}
	 * @return an array of {@link VirtualTableColumn}s according to
	 *         <code>selector</code>
	 */
	public VirtualTableColumn<?>[] getColumns(final ColumnSelector selector)
	{
		final List<VirtualTableColumn<?>> list = new ArrayList();

		for(final VirtualTableColumn<?> column : this.columns)
		{
			if(selector.select(column))
			{
				list.add(column);
			}
		}

		return list.toArray(new VirtualTableColumn<?>[list.size()]);
	}


	/**
	 * Returns all colum indices of this VirtualTable's columns according to
	 * <code>selector</code>.
	 *
	 * @param selector
	 *            the {@link ColumnSelector}
	 * @return the column's indices according to <code>selector</code>
	 */
	public int[] getColumnIndices(final ColumnSelector selector)
	{
		final IntList list = new IntList();

		for(int i = 0; i < this.columns.length; i++)
		{
			if(selector.select(this.columns[i]))
			{
				list.add(i);
			}
		}

		return list.toArray();
	}


	/**
	 * Returns an array with all indexes of the visible columns.
	 *
	 * @return an int array with indexes of visible columns
	 * @see VirtualTableColumn#isVisible()
	 */
	public int[] getVisibleColumnIndices()
	{
		return getColumnIndices(new ColumnSelector()
		{
			@Override
			public boolean select(final VirtualTableColumn<?> column)
			{
				return column.isVisible();
			}
		});
	}


	/**
	 * Returns an array with all indexes of the persistent columns.
	 *
	 * @return an int array with indexes of persistent columns
	 * @see VirtualTableColumn#isPersistent()
	 */
	public int[] getPersistentColumnIndices()
	{
		return getColumnIndices(new ColumnSelector()
		{
			@Override
			public boolean select(final VirtualTableColumn<?> column)
			{
				return column.isPersistent();
			}
		});
	}


	/**
	 * Returns an array with all indexes of the non persistent columns.
	 *
	 * @return an int array with indexes of non persistent columns
	 * @see VirtualTableColumn#isPersistent()
	 */
	public int[] getNonPersistentColumnIndices()
	{
		return getColumnIndices(new ColumnSelector()
		{
			@Override
			public boolean select(final VirtualTableColumn<?> column)
			{
				return !column.isPersistent();
			}
		});
	}


	/**
	 * Returns an array with all indexes of the visible and non persistent
	 * columns.
	 *
	 * @return an int array with indexes of visible and non persistent columns
	 * @see VirtualTableColumn#isPersistent()
	 * @see VirtualTableColumn#isVisible()
	 */
	public int[] getNonPersistentVisibleColumnIndices()
	{
		return getColumnIndices(new ColumnSelector()
		{
			@Override
			public boolean select(final VirtualTableColumn<?> column)
			{
				return !column.isPersistent() && column.isVisible();
			}
		});
	}


	/**
	 * Checks if this VirtualTable has a non persistent column.
	 *
	 * @return <code>true</code> if this VirtualTable has a non persistent
	 *         column, <code>false</code> otherwise
	 * @see VirtualTableColumn#isPersistent()
	 */

	public boolean hasNonPersistentColumns()
	{
		for(final VirtualTableColumn column : this.columns)
		{
			if(!column.isPersistent())
			{
				return true;
			}
		}

		return false;
	}


	/**
	 * Checks if this VirtualTable has a linked column.
	 *
	 * @return <code>true</code> if this VirtualTable has a linked column,
	 *         <code>false</code> otherwise
	 * @see VirtualTableColumn#getTableColumnLink()
	 *
	 * @since 3.2
	 */

	public boolean hasLinkedColumns()
	{
		for(final VirtualTableColumn column : this.columns)
		{
			if(column.getTableColumnLink() != null)
			{
				return true;
			}
		}

		return false;
	}


	/**
	 * Override of Objects toString method.
	 *
	 * @return the name of the {@link VirtualTable}, or "VirtualTable" if name
	 *         is empty
	 */
	@Override
	public String toString()
	{
		return this.name != null && this.name.length() > 0 ? this.name : "VirtualTable";
	}


	/**
	 * Returns a {@link String} representation for this {@link VirtualTable}.
	 * The first line of the array contains the captions of the columns
	 *
	 * @return a two dimensional array of Strings with the captions and data for
	 *         this VirtualTable.
	 */
	public String[][] toFormattedStrings()
	{
		final String[][] s = new String[getRowCount() + 1][];
		s[0] = getColumnCaptions();
		return this.data.putFormattedStrings(s,1);
	}


	/**
	 * Returns a {@link TableMetaData} representation of this
	 * {@link VirtualTable}.
	 */
	public TableMetaData toTableMetaData()
	{
		final TableInfo tableInfo = new TableInfo(TableType.TABLE,this.dbSchema,this.dbAlias);

		final List<ColumnMetaData> columnMeta = new ArrayList();
		for(final VirtualTableColumn column : this.columns)
		{
			if(column.isPersistent())
			{
				columnMeta.add(column.toColumnMetaData(this));
			}
		}

		final int c = this.indices.size();
		final Index[] indexMeta = new Index[c];
		for(int i = 0; i < c; i++)
		{
			indexMeta[i] = this.indices.get(i)._originalIndex.clone();
		}

		return new TableMetaData(tableInfo,
				columnMeta.toArray(new ColumnMetaData[columnMeta.size()]),indexMeta);
	}


	/**
	 * Returns a {@link XdevList} for all rows containing further XdevLists for
	 * all values of the rows.
	 * <p>
	 * This is an alias for {@link #toLists()}
	 * </p>
	 *
	 * @return a {@link XdevList} of {@link XdevList}s containing the data of
	 *         the Virtual Table
	 */
	public XdevList getValues()
	{
		return toLists();
	}


	/**
	 * Returns a {@link XdevList} containing all rows as further XdevLists
	 * containing all values of the rows.
	 *
	 * @return a {@link XdevList} of {@link XdevList}s containing the data of
	 *         the Virtual Table
	 */
	public XdevList toLists()
	{
		return toLists(-1);
	}


	/**
	 * Returns a {@link XdevList} containing all rows as further XdevLists
	 * containing all values of the rows.
	 *
	 * @param exceptCol
	 *            a column index to be excluded from the lists of values
	 * @return a {@link XdevList} of {@link XdevList}s containing the data of
	 *         the Virtual Table
	 */
	public XdevList toLists(final int exceptCol)
	{
		final XdevList all = new XdevList();
		for(int i = 0; i < this.data.size(); i++)
		{
			all.addElement(getRow(i,exceptCol));
		}
		return all;
	}


	/**
	 * Returns the {@link VirtualTableRow} for the specified row index.
	 *
	 * @param index
	 *            the row index
	 *
	 * @return a {@link VirtualTableRow}
	 *
	 * @throws IndexOutOfBoundsException
	 *             if index is not within range of row indices
	 */
	public VirtualTableRow getRow(final int index) throws IndexOutOfBoundsException
	{
		return this.data.get(index);
	}


	/**
	 * Returns all rows of this VirtualTable. If this VirtualTable has no data
	 * an empty array is returned.
	 *
	 * @return all rows of this VirtualTable.
	 */
	public VirtualTableRow[] getRows()
	{
		return this.data.getAll();
	}


	/**
	 * Returns a subset of rows of this VirtualTable.
	 *
	 * @param startIndex
	 *            the start index, inclusive
	 * @param endIndex
	 *            the end index, exclusive
	 * @return a subset of rows.
	 *
	 * @throws IndexOutOfBoundsException
	 *             if index is not within range of row indices
	 */
	public VirtualTableRow[] getRows(final int startIndex, final int endIndex)
			throws IndexOutOfBoundsException
	{
		return this.data.get(startIndex,endIndex);
	}


	/**
	 * Returns the last row of this {@link VirtualTable}.
	 *
	 * @return the last {@link VirtualTableRow}
	 */
	public VirtualTableRow getLastRow()
	{
		return this.data.get(this.data.size() - 1);
	}


	/**
	 * Returns the {@link VirtualTableRow} for the specified primary key values.
	 *
	 * @param pkValues
	 *            {@link KeyValues} instance with the primary key values of the
	 *            row
	 *
	 * @return a {@link VirtualTableRow}, or <code>null</code> if no row is
	 *         found
	 */
	public VirtualTableRow getRow(final KeyValues pkValues)
	{
		for(int i = 0; i < this.data.size(); i++)
		{
			final VirtualTableRow row = this.data.get(i);
			if(pkValues.equals(row))
			{
				return row;
			}
		}

		return null;
	}


	/**
	 * Returns a {@link XdevList} representing the row at the current cursor
	 * position.
	 * <p>
	 * This is an alias for {@link #getRowAsList(int)}.
	 * </p>
	 *
	 * @return a {@link XdevList}
	 */
	public XdevList getRowAsList()
	{
		return getRowAsList(this.cursorpos);
	}


	/**
	 * Returns a {@link XdevList} representing a specified row.
	 * <p>
	 * This is an alias for {@link #getRow(int, int)}.
	 * </p>
	 *
	 * @param row
	 *            the index of the row to be returned as a XdevList
	 * @return a {@link XdevList}
	 */
	public XdevList getRowAsList(final int row)
	{
		return getRow(row,-1);
	}


	/**
	 * Returns a {@link XdevList} representing a specified row.
	 * <p>
	 * This is an alias for {@link #getRowData(int, int)}.
	 * </p>
	 *
	 * @param row
	 *            the index of the row to be returned as a XdevList
	 * @param exceptCol
	 *            the index of a column to be excluded from the XdevList
	 * @return a {@link XdevList}
	 */
	private XdevList getRow(final int row, final int exceptCol)
	{
		return getRowData(row,exceptCol);
	}


	/**
	 * Returns a {@link XdevList} representing a specified row.
	 *
	 * @param row
	 *            the index of the row to be returned as a XdevList
	 * @param exceptCol
	 *            the index of a column to be excluded from the XdevList
	 * @return a {@link XdevList}
	 */
	public XdevList getRowData(final int row, final int exceptCol)
	{
		final int columnCount = getColumnCount();
		final XdevList rowData = new XdevList(columnCount);
		for(int col = 0; col < columnCount; col++)
		{
			if(col != exceptCol)
			{
				rowData.addElement(getValueAt(row,col));
			}
		}

		return rowData;
	}


	/**
	 * Returns a {@link XdevList} with all values for a specified column index.
	 *
	 * @param col
	 *            the index of the column to be returned as a list
	 * @return a {@link XdevList} with all values of the specified column.
	 */
	public XdevList getColumnData(final int col)
	{
		final int rowCount = getRowCount();
		final XdevList colData = new XdevList(rowCount);
		for(int row = 0; row < rowCount; row++)
		{
			colData.add(getValueAt(row,col));
		}

		return colData;
	}


	/**
	 * Creates and returns a {@link Map} representation of a specified row. The
	 * keys of the map are the column names, the entries are the values of the
	 * row.
	 *
	 * @return a {@link Map} for the specified row
	 */
	public Map<String, Object> getRowAsMap()
	{
		return getRowAsMap(this.cursorpos);
	}


	/**
	 * Creates and returns a {@link Map} representation of a specified row. The
	 * keys of the map are the column names, the entries are the values of the
	 * <code>row</code>.
	 *
	 * @param row
	 *            the index of the row to return as a map
	 * @return a {@link Map} for the specified <code>row</code>
	 */
	public Map<String, Object> getRowAsMap(final int row)
	{
		final Map<String, Object> rowData = new HashMap<String, Object>(this.columns.length);
		fillInMap(row,rowData);
		return rowData;
	}


	/**
	 * Fills the provided {@link Map} with data from the given <code>row</code>.
	 *
	 * @param row
	 *            row index to get the data from
	 * @param ht
	 *            {@link Map} to fill
	 */
	public void fillInMap(final int row, final Map<String, Object> ht)
	{
		for(int col = 0; col < this.columns.length; col++)
		{
			ht.put(this.columns[col].getName(),getValueAt(row,col));
		}
	}


	/**
	 * Adds all row data (formatted) of this {@link VirtualTable} to a specified
	 * {@link ItemList}.
	 * <p>
	 * This is an alias method for
	 * {@link ItemList#setModel(VirtualTable, String, String)}
	 * </p>
	 *
	 * @param list
	 *            {@link ItemList}
	 *
	 * @param itemCol
	 *            columnname to fill <code>item</code> from or string with
	 *            variables like <code>"{%SURNAME} {%NAME} - {%AGE}"</code>
	 *
	 * @param dataCol
	 *            columnname to fill <code>data</code> from
	 *
	 * @see ItemList#setModel(VirtualTable, String, String)
	 */
	public void fillInItemList(final ItemList list, final String itemCol, final String dataCol)
	{
		list.setModel(this,itemCol,dataCol);
	}


	/**
	 * Returns a {@link List} of {@link String} representations of column values
	 * for a specified column index.
	 *
	 * @param col
	 *            the column index
	 * @return the column values as a List of Strings
	 */
	public List<String> getColumnAsString(final int col)
	{
		final int c = getRowCount();
		final List<String> list = new ArrayList(c);
		for(int i = 0; i < c; i++)
		{
			list.add(getFormattedValueAt(i,col));
		}

		return list;
	}


	/**
	 * Sorts the data of a {@link VirtualTable} for a specified column name. The
	 * sort order depends on the {@link Comparator} of the selected
	 * {@link VirtualTableColumn}.
	 *
	 * @param columnName
	 *            the name of the column to sort
	 * @param ascending
	 *            if set to <code>true</code>, sort order will be ascending,
	 *            else descending
	 */
	public void sortByCol(final String columnName, final boolean ascending)
	{
		final int col = getColumnIndex(columnName);
		if(col != -1)
		{
			sortByCol(col,ascending);
		}
	}


	/**
	 * Sorts the data of a {@link VirtualTable} for a specified column name. The
	 * sort order depends on the {@link Comparator} of the selected
	 * {@link VirtualTableColumn}.
	 *
	 * @param col
	 *            the index of the column to sort
	 * @param ascending
	 *            if set to <code>true</code>, sort order will be ascending,
	 *            else descending
	 */
	public void sortByCol(final int col, final boolean ascending)
	{
		this.data.sortByCol(col,ascending);
	}


	/**
	 * Sorts the data of a {@link VirtualTable} The sort order depends on the
	 * specified {@link Comparator}.
	 *
	 * @param comparator
	 *            a {@link Comparator} of Type {@link VirtualTableRow} for
	 *            specifying a custom sort order
	 */
	public void sort(final Comparator<VirtualTableRow> comparator)
	{
		this.data.sortByCol(comparator);
	}


	/**
	 * Creates a new, empty {@link VirtualTableRow} and adds it at the end of
	 * this {@link VirtualTable}
	 */
	public VirtualTableRow addRow()
	{
		final VirtualTableRow row = createRowForInsert(null);

		this.data.add(row,true);

		fireRowInserted(row,row.index);

		return row;
	}


	/**
	 * Creates a new {@link VirtualTableRow} and adds it to this
	 * {@link VirtualTable}.
	 * <p>
	 * <b>Note:</b> The order of the values has to match the order of the
	 * columns in the {@link VirtualTable}.
	 * </p>
	 *
	 *
	 * @param synchronizeDB
	 *            if set to <code>true</code>, the new row will be inserted in
	 *            the database automatically.
	 *
	 * @param values
	 *            a number of values to be inserted as a new row
	 *
	 * @throws VirtualTableException
	 *             thrown if a column of the map was not found in the
	 *             {@link VirtualTable}
	 *
	 * @throws DBException
	 *             thrown if synchronization with database failed.
	 */
	public VirtualTableRow addRow(final boolean synchronizeDB, final Object... values)
			throws VirtualTableException, DBException
	{
		final Map ht = new HashMap(this.columns.length);
		for(int ci = 0, li = 0; ci < this.columns.length && li < values.length; ci++)
		{
			if(!this.columns[ci].isAutoIncrement())
			{
				ht.put(this.columns[ci].getName(),values[li++]);
			}
		}

		return addRow(ht,synchronizeDB);
	}


	/**
	 * Creates a new {@link VirtualTableRow} and adds it to this
	 * {@link VirtualTable}.
	 * <p>
	 * <b>Note:</b> The order of the entries in the <code>list</code> has to
	 * match the order of the columns in the {@link VirtualTable}.
	 * </p>
	 *
	 * @param list
	 *            a {@link List} of values to be inserted as a new row
	 * @param synchronizeDB
	 *            if set to <code>true</code>, the new row will be inserted in
	 *            the database automatically.
	 * @throws VirtualTableException
	 *             thrown if a column of the map was not found in the
	 *             {@link VirtualTable}
	 * @throws DBException
	 *             thrown if synchronization with database failed.
	 */
	public VirtualTableRow addRow(final List list, final boolean synchronizeDB)
			throws VirtualTableException, DBException
	{
		final Map ht = new HashMap(this.columns.length);
		for(int ci = 0, li = 0; ci < this.columns.length && li < list.size(); ci++)
		{
			if(!this.columns[ci].isAutoIncrement())
			{
				ht.put(this.columns[ci].getName(),list.get(li++));
			}
		}

		return addRow(ht,synchronizeDB);
	}


	/**
	 * Creates a new {@link VirtualTableRow} and adds it to this
	 * {@link VirtualTable}.
	 *
	 * @param form
	 *            a {@link Formular}, which values are to be added as a new row.
	 *
	 * @param synchronizeDB
	 *            if set to <code>true</code>, the new row will be inserted in
	 *            the database automatically.
	 *
	 * @throws VirtualTableException
	 *             thrown if a column of the map was not found in the
	 *             {@link VirtualTable}
	 *
	 * @throws DBException
	 *             thrown if synchronization with database failed.
	 */
	public VirtualTableRow addRow(final Formular form, final boolean synchronizeDB)
			throws VirtualTableException, DBException
	{
		return addRow(form.getData(true),synchronizeDB);
	}


	/**
	 * Creates a new {@link VirtualTableRow} and adds it to this
	 * {@link VirtualTable}.
	 * <p>
	 * This is an alias for <code>addRow(map,synchronizeDB,false)</code>.
	 * </p>
	 *
	 * @param map
	 *            a {@link Map} of {@link String} keys and {@link Object} as
	 *            values, containing the column names and values for the new
	 *            row.
	 * @param synchronizeDB
	 *            if set to <code>true</code>, the new row will be inserted in
	 *            the database automatically.
	 * @throws VirtualTableException
	 *             thrown if a column of the map was not found in the
	 *             {@link VirtualTable}
	 * @throws DBException
	 *             thrown if synchronization with database failed.
	 */
	public VirtualTableRow addRow(final Map<String, Object> map, final boolean synchronizeDB)
			throws VirtualTableException, DBException
	{
		return addRow(map,synchronizeDB,false);
	}


	/**
	 * Creates a new {@link VirtualTableRow} and adds it to this
	 * {@link VirtualTable}.
	 * <p>
	 * This is an alias for
	 * <code>addRow(null,map,synchronizeDB,ignoreWarnings)</code>.
	 * </p>
	 *
	 * @param map
	 *            a {@link Map} of {@link String} keys and {@link Object} as
	 *            values, containing the column names and values for the new
	 *            row.
	 * @param synchronizeDB
	 *            if set to <code>true</code>, the new row will be inserted in
	 *            the database automatically.
	 * @param ignoreWarnings
	 *            if set to <code>true</code>, warnings because of unmapped
	 *            columns get ignored.
	 * @throws VirtualTableException
	 *             thrown if a column of the map was not found in the
	 *             {@link VirtualTable} and ignoreWarning is set to
	 *             <code>false</code>
	 * @throws DBException
	 *             thrown if synchronization with database failed.
	 */
	public VirtualTableRow addRow(final Map<String, Object> map, final boolean synchronizeDB,
			final boolean ignoreWarnings) throws VirtualTableException, DBException
	{
		return addRow(null,map,synchronizeDB,ignoreWarnings);
	}


	/**
	 * Adds the {@link VirtualTableRow} row to this {@link VirtualTable}.
	 * <p>
	 * This is an alias for <code>addRow(row,synchronizeDB,false)</code>.
	 * </p>
	 *
	 * @param row
	 *            the row to add
	 * @param synchronizeDB
	 *            if set to <code>true</code>, the new row will be inserted in
	 *            the database automatically.
	 * @throws DBException
	 *             thrown if synchronization with database failed.
	 */
	public VirtualTableRow addRow(final VirtualTableRow row, final boolean synchronizeDB)
			throws VirtualTableException, DBException
	{
		return addRow(row,synchronizeDB,false);
	}


	/**
	 * Adds the {@link VirtualTableRow} row to this {@link VirtualTable}.
	 *
	 * @param row
	 *            the row to add
	 * @param synchronizeDB
	 *            if set to <code>true</code>, the new row will be inserted in
	 *            the database automatically.
	 * @param ignoreWarnings
	 *            if set to <code>true</code>, warnings because of unmapped
	 *            columns get ignored.
	 * @throws VirtualTableException
	 *             if <code>row</code> doesn't belong to this
	 *             {@link VirtualTable}.
	 * @throws DBException
	 *             thrown if synchronization with database failed.
	 */
	public VirtualTableRow addRow(final VirtualTableRow row, final boolean synchronizeDB,
			final boolean ignoreWarnings) throws VirtualTableException, DBException
	{
		return addRow(row,null,synchronizeDB,ignoreWarnings);
	}


	/**
	 * Adds a new {@link VirtualTableRow} to this {@link VirtualTable}.
	 *
	 * @param row
	 *            the {@link VirtualTableRow} to add. If it is <code>null</code>
	 *            , a new VirtualTableRow will be created.
	 * @param map
	 *            a {@link Map} of {@link String} keys and {@link Object} as
	 *            values, containing the column names and values for the new
	 *            row.
	 * @param synchronizeDB
	 *            if set to <code>true</code>, the new row will be inserted in
	 *            the database automatically.
	 * @param ignoreWarnings
	 *            if set to <code>true</code>, warnings because of unmapped
	 *            columns get ignored.
	 * @throws VirtualTableException
	 *             thrown if a column of the map was not found in the
	 *             {@link VirtualTable} and ignoreWarning is set to
	 *             <code>false</code>
	 * @throws VirtualTableException
	 *             if <code>row</code> doesn't belong to this
	 *             {@link VirtualTable}.
	 * @throws DBException
	 *             thrown if synchronization with database failed.
	 */
	private VirtualTableRow addRow(final VirtualTableRow row, final Map<String, Object> map,
			final boolean synchronizeDB, final boolean ignoreWarnings)
			throws VirtualTableException, DBException
	{
		return addRow(row,map,synchronizeDB,ignoreWarnings,null);
	}


	private VirtualTableRow addRow(VirtualTableRow row, Map<String, Object> map,
			final boolean synchronizeDB, final boolean ignoreWarnings,
			final DBConnection connection) throws VirtualTableException, DBException
	{
		INSERT insert = null;
		List values = null;
		if(synchronizeDB)
		{
			insert = new INSERT().INTO(this);
			values = new ArrayList();
		}

		if(row != null)
		{
			if(row.getVirtualTable() != this)
			{
				if(!row.getVirtualTable().getName().equals(getName()))
				{
					throw new VirtualTableException(this,
							"The row does not belong to this VirtualTable");
				}

				map = row.toMap();
				row = createRow();
			}
		}
		else
		{
			final List<VirtualTableColumn<?>> used = new ArrayList();
			for(int i = 0; i < this.columns.length; i++)
			{
				final Object key = getKey(this.columns[i].getName(),map);
				if(key != null)
				{
					used.add(this.columns[i]);
				}
			}
			row = createRowForInsert(used);
		}

		if(map != null)
		{
			for(int i = 0; i < this.columns.length; i++)
			{
				final Object key = getKey(this.columns[i].getName(),map);
				if(key != null)
				{
					final VirtualTableColumn column = this.columns[i];
					Object value = map.remove(key);
					if(value == null)
					{
						if(column.isAutoIncrement())
						{
							value = getNextAutoVal(column);
						}
						else
						{
							value = column.getDefaultValue();
						}
					}

					value = checkValue(i,value);

					if(synchronizeDB && !column.isAutoIncrement() && column.isPersistent())
					{
						insert.assign(column,"?");
						values.add(value);
					}

					row.set(i,value,false,false);
				}
			}

			if(map.size() > 0 && !ignoreWarnings)
			{
				final StringBuffer sb = new StringBuffer("Column(s) not found: ");
				sb.append(StringUtils.concat(", ",map.keySet()));
				throw new VirtualTableException(sb.toString());
			}
		}
		else if(synchronizeDB)
		{
			for(int i = 0; i < this.columns.length; i++)
			{
				final VirtualTableColumn column = this.columns[i];
				if(synchronizeDB && !column.isAutoIncrement() && column.isPersistent())
				{
					insert.assign(column,"?");
					values.add(row.get(i));
				}
			}
		}

		boolean markAsAdded = true;

		try
		{
			if(synchronizeDB && values.size() > 0)
			{
				final Object[] params = values.toArray(new Object[values.size()]);
				final WriteResult result = writeDB(connection,insert,getAutoValueColumns(),params);
				try
				{
					if(result.hasGeneratedKeys())
					{
						row.updateKeys(result.getGeneratedKeys());
					}
				}
				finally
				{
					result.close();
				}

				markAsAdded = false;
				row.rowState = RowState.UNCHANGED;
			}
		}
		finally
		{
			this.data.add(row,markAsAdded);
			fireRowInserted(row,row.index);
		}

		return row;
	}


	/**
	 * Removes a specified row from this {@link VirtualTable}.
	 * <p>
	 * The corresponding row in the database table gets deleted too.
	 * </p>
	 * <p>
	 * This is a synonym for: <code>removeRow(row,true);</code>
	 * </p>
	 *
	 * @param row
	 *            the index of the row to remove
	 * @throws VirtualTableException
	 *             thrown if no primary key is specified for this
	 *             {@link VirtualTable}.
	 * @throws DBException
	 *             thrown if deletion of row in database failed.
	 */
	public void deleteRow(final int row) throws VirtualTableException, DBException
	{
		removeRow(row,true);
	}


	/**
	 * Removes a specified row from this {@link VirtualTable}.
	 *
	 * @param row
	 *            the index of the row to remove
	 * @param synchronizeDB
	 *            if set to <code>true</code>, the corresponding row in the
	 *            database table gets deleted too.
	 * @throws VirtualTableException
	 *             thrown if no primary key is specified for this
	 *             {@link VirtualTable}.
	 * @throws DBException
	 *             thrown if deletion of row in database failed.
	 */
	public void removeRow(final int row, final boolean synchronizeDB)
			throws VirtualTableException, DBException
	{
		final VirtualTableColumn[] pk = getPrimaryKeyColumns();
		if(synchronizeDB && pk.length == 0)
		{
			throw new VirtualTableException(this,"No primary key specified");
		}

		final KeyValues pkValue = new KeyValues(this.data.get(row));

		this.data.remove(row);

		if(synchronizeDB)
		{
			deleteDB(pkValue);
		}
		else
		{
			this.removedRowKeys.add(pkValue);
		}
	}


	/**
	 * Removes rows from the {@link VirtualTable} as specified by a
	 * {@link KeyValues} object.
	 * <p>
	 * The corresponding rows in the database table get deleted too.
	 * </p>
	 * <p>
	 * This is a synonym for: <code>removeRows(keyValues,true);</code>
	 * </p>
	 *
	 * @param keyValues
	 *            the {@link KeyValues} object specifying the condition for the
	 *            removal of rows.
	 *
	 * @throws VirtualTableException
	 *
	 * @throws DBException
	 *             thrown if deletion of row in database failed.
	 */
	public void deleteRows(final KeyValues keyValues) throws VirtualTableException, DBException
	{
		removeRows(keyValues,true);
	}


	/**
	 * Removes rows from the {@link VirtualTable} as specified by a
	 * {@link KeyValues} object.
	 *
	 * @param keyValues
	 *            the {@link KeyValues} object specifying the condition for the
	 *            removal of rows.
	 *
	 * @param synchronizeDB
	 *            if set to <code>true</code>, the corresponding rows in the
	 *            database table get deleted too.
	 *
	 * @throws VirtualTableException
	 *
	 * @throws DBException
	 *             thrown if deletion of row in database failed.
	 */
	public void removeRows(final KeyValues keyValues, final boolean synchronizeDB)
			throws VirtualTableException, DBException
	{
		checkKeyValues(keyValues);

		for(int r = getRowCount() - 1; r >= 0; r--)
		{
			if(keyValues.equals(this.data.get(r)))
			{
				this.data.remove(r);
			}
		}

		if(synchronizeDB)
		{
			deleteDB(keyValues);
		}
		else
		{
			this.removedRowKeys.add(keyValues);
		}
	}


	private void deleteDB(final KeyValues keyValues) throws DBException, VirtualTableException
	{
		if(keyValues.isEmpty())
		{
			throw new VirtualTableException(this,
					"Cannot synchronize with database: No primary key defined");
		}

		final WHERE where = new WHERE();
		final List values = new ArrayList();

		keyValues.appendCondition(where,values);

		final DELETE delete = new DELETE().FROM(this).WHERE(where);
		final Object[] params = values.toArray(new Object[values.size()]);
		writeDB(null,delete,false,params);
	}


	/**
	 * Returns the primary key values of the removed rows since the last
	 * synchronization.<br>
	 * If no row was deleted an empty array is returned.
	 * <p>
	 * This values are used by the {@link #synchronizeChangedRows()} methods to
	 * remove the data from the underlying datasource.
	 *
	 * @return the primary key values of the removed rows since the last sync.
	 *
	 * @see #synchronizeChangedRows()
	 * @see RowState
	 *
	 * @since 3.1
	 */
	public KeyValues[] getRemovedRowsKeyValues()
	{
		return this.removedRowKeys.toArray(new KeyValues[this.removedRowKeys.size()]);
	}


	/**
	 * Updates rows from the {@link VirtualTable} as specified by a
	 * {@link Formular} .
	 *
	 * @param form
	 *            {@link Formular}, which values are to be used to update the
	 *            rows.
	 * @param keyValues
	 *            the {@link KeyValues} object specifying the condition for the
	 *            rows to update.
	 * @param synchronizeDB
	 *            if set to <code>true</code>, the corresponding rows in the
	 *            database table get updated too.
	 * @return the number of updated rows
	 * @throws VirtualTableException
	 * @throws DBException
	 *             thrown if the update of rows in database failed.
	 */
	public int updateRows(final Formular form, final KeyValues keyValues,
			final boolean synchronizeDB) throws VirtualTableException, DBException
	{
		return updateRows(form.getData(true),keyValues,synchronizeDB);
	}


	/**
	 * Updates rows from the {@link VirtualTable} as specified by a
	 * {@link KeyValues} object.
	 * <p>
	 * <b>Note:</b> The order of the items in the list has to match the order of
	 * the columns in the {@link VirtualTable}.
	 * </p>
	 *
	 * @param list
	 *            a {@link Collection} of values containing the new values for
	 *            the updated columns
	 *
	 * @param keyValues
	 *            the {@link KeyValues} object specifying the condition for the
	 *            rows to update.
	 *
	 * @param synchronizeDB
	 *            if set to <code>true</code>, the corresponding rows in the
	 *            database table get updated too.
	 *
	 * @return the number of updated rows
	 *
	 * @throws VirtualTableException
	 *
	 * @throws DBException
	 *             thrown if the update of rows in database failed.
	 */
	public int updateRows(final Collection<?> list, final KeyValues keyValues,
			final boolean synchronizeDB) throws VirtualTableException, DBException
	{
		final Map map = new HashMap();
		final Object[] values = list.toArray();
		final int max = Math.min(values.length,this.columns.length);
		for(int i = 0; i < max; i++)
		{
			map.put(this.columns[i].getName(),values[i]);
		}

		return updateRows(map,keyValues,synchronizeDB);
	}


	/**
	 * Updates rows from the {@link VirtualTable} as specified by a
	 * {@link KeyValues} object.
	 *
	 * @param map
	 *            a {@link Map} of {@link String} keys and {@link Object}
	 *            values, containing the column names and values for the updated
	 *            row.
	 * @param keyValues
	 *            the {@link KeyValues} object specifying the condition for the
	 *            rows to update.
	 * @param synchronizeDB
	 *            if set to <code>true</code>, the corresponding rows in the
	 *            database table get updated too.
	 * @return the number of updated rows
	 * @throws VirtualTableException
	 * @throws DBException
	 *             thrown if the update of rows in database failed.
	 */
	public int updateRows(final Map<String, Object> map, final KeyValues keyValues,
			final boolean synchronizeDB) throws VirtualTableException, DBException
	{
		return updateRows(map,keyValues,synchronizeDB,null);
	}


	private int updateRows(final Map<String, Object> map, final KeyValues keyValues,
			final boolean synchronizeDB, final DBConnection connection)
			throws VirtualTableException, DBException
	{
		checkKeyValues(keyValues);

		final IntList updatedRows = new IntList();

		final int max = getRowCount();
		for(int row = 0; row < max; row++)
		{
			if(keyValues.equals(this.data.get(row)))
			{
				updatedRows.add(row);

				for(int c = 0; c < this.columns.length; c++)
				{
					if(!this.columns[c].isAutoIncrement())
					{
						final String key = getKey(this.columns[c].getName(),map);
						if(key != null)
						{
							setValueAt(map.get(key),row,c);
						}
					}
				}
			}
		}

		final int updateCount = updatedRows.size();
		if(synchronizeDB && updateCount > 0)
		{
			final UPDATE update = new UPDATE(this);
			final List values = new ArrayList();

			boolean write = false;

			for(int c = 0; c < this.columns.length; c++)
			{
				final VirtualTableColumn column = this.columns[c];
				if(column.isPersistent() && !column.isAutoIncrement())
				{
					final Object key = getKey(column.getName(),map);
					if(key != null)
					{
						write = true;
						update.SET(column.toSqlField(),"?");
						values.add(map.get(key));
					}
				}
			}

			if(write)
			{
				final WHERE where = new WHERE();
				keyValues.appendCondition(where,values);
				update.WHERE(where);
				final Object[] params = values.toArray(new Object[values.size()]);
				writeDB(connection,update,false,params);

				for(int i = 0; i < updateCount; i++)
				{
					this.data.get(updatedRows.get(i)).rowState = RowState.UNCHANGED;
				}
			}
		}

		return updateCount;
	}


	/**
	 * Updates a row from the {@link VirtualTable} as specified by a row index.
	 *
	 * @param map
	 *            a {@link Map} of {@link String} keys and {@link Object}
	 *            values, containing the column names and values for the updated
	 *            row.
	 * @param row
	 *            the index of the row to update
	 * @param synchronizeDB
	 *            if set to <code>true</code>, the corresponding row in the
	 *            database table gets updated too.
	 *
	 * @throws VirtualTableException
	 *
	 * @throws DBException
	 *             thrown if deletion of row in database failed.
	 */
	public void updateRow(final Map<String, Object> map, final int row, final boolean synchronizeDB)
			throws VirtualTableException, DBException
	{
		updateRow(map,row,synchronizeDB,null);
	}


	private void updateRow(final Map<String, Object> map, final int row,
			final boolean synchronizeDB, final DBConnection connection)
			throws VirtualTableException, DBException
	{
		if(row < 0 || row >= this.data.size)
		{
			throw new IndexOutOfBoundsException(
					"Row index (" + row + ") out of range in '" + this.name + "'");
		}

		final VirtualTableRow vtRow = getRow(row);
		if(vtRow.getRowState() == RowState.ADDED && synchronizeDB)
		{
			// row is in vt, but not in db -> INSERT

			final INSERT insert = new INSERT().INTO(this);
			final List values = new ArrayList();

			for(int c = 0; c < this.columns.length; c++)
			{
				final VirtualTableColumn cd = this.columns[c];
				if(cd.isPersistent() && !cd.isAutoIncrement())
				{
					final Object key = getKey(cd.getName(),map);
					if(key != null)
					{
						setValueAt(map.get(key),row,c,false,false);
					}

					if(synchronizeDB)
					{
						insert.assign(cd,"?");
						values.add(vtRow.get(c));
					}
				}
			}

			final Object[] params = values.toArray(new Object[values.size()]);
			final WriteResult result = writeDB(connection,insert,getAutoValueColumns(),params);
			try
			{
				if(result.hasGeneratedKeys())
				{
					vtRow.updateKeys(result.getGeneratedKeys());
				}
			}
			finally
			{
				result.close();
			}

			vtRow.rowState = RowState.UNCHANGED;
		}
		else if(map != null && map.size() > 0)
		{
			UPDATE update = null;
			List values = null;
			if(synchronizeDB)
			{
				update = new UPDATE(this);
				values = new ArrayList();
			}

			boolean write = false;

			for(int c = 0; c < this.columns.length; c++)
			{
				final VirtualTableColumn cd = this.columns[c];
				if(cd.isPersistent() && !cd.isAutoIncrement())
				{
					final Object key = getKey(cd.getName(),map);
					if(key != null)
					{
						final Object val = setValueAt(map.get(key),row,c,false,false);
						write = true;

						if(synchronizeDB)
						{
							update.SET(cd.toSqlField(),"?");
							values.add(val);
						}
					}
				}
			}

			if(write && synchronizeDB)
			{
				final VirtualTableColumn[] pk = getPrimaryKeyColumns();
				if(pk.length == 0)
				{
					throw new VirtualTableException(this,
							"Cannot synchronize with database: No primary key defined");
				}

				final WHERE where = new WHERE();
				for(int i = 0; i < pk.length; i++)
				{
					where.and(pk[i].eq("?"));
					values.add(getValueAt(row,getColumnIndex(pk[i])));
				}
				update.WHERE(where);
				final Object[] params = values.toArray(new Object[values.size()]);
				writeDB(connection,update,false,params);

				this.data.get(row).rowState = RowState.UNCHANGED;
			}
		}
		fireRowUpdated(this.data.get(row),row);
	}


	private void checkKeyValues(final KeyValues values) throws VirtualTableException
	{
		if(!equals(values.getVirtualTable()))
		{
			throw new IllegalArgumentException("KeyValues does not refer to this VirtualTable");
		}

		if(values.isEmpty())
		{
			throw new IllegalArgumentException("KeyValues are empty");
		}
	}


	/**
	 * Synchronizes all added, changed and removed rows with the current data
	 * source.
	 * <p>
	 * This action is embedded in a transaction, meaning a rollback is performed
	 * if an error occurs.
	 *
	 * @throws VirtualTableException
	 *             If a error in this VirtualTable occurs
	 * @throws DBException
	 *             If an error occurs while writing the database
	 *
	 * @see #synchronizeChangedRows(DBConnection)
	 * @see RowState
	 * @see #getRemovedRowsKeyValues()
	 */
	public void synchronizeChangedRows() throws VirtualTableException, DBException
	{
		synchronizeChangedRows(null);
	}


	public void synchronizeChangedLockedRows(final List<Integer> updateRows)
			throws VirtualTableException, DBException
	{
		synchronizeChangedRows(null,this.columns,true,true,true,updateRows);
	}


	/**
	 * Synchronizes electively added, changed and removed rows with the current
	 * data source.
	 * <p>
	 * This action is embedded in a transaction, meaning a rollback is performed
	 * if an error occurs.
	 *
	 * @param doInserts
	 *            <code>true</code> if added rows should be synchronized
	 * @param doUpdates
	 *            <code>true</code> if changed rows should be synchronized
	 * @param doDeletes
	 *            <code>true</code> if removed rows should be synchronized
	 *
	 * @throws VirtualTableException
	 *             If a error in this VirtualTable occurs
	 * @throws DBException
	 *             If an error occurs while writing the database
	 * @throws IllegalArgumentException
	 *             If none of <code>doInserts</code>, <code>doUpdates</code> or
	 *             <code>doDeletes</code> is <code>true</code>
	 *
	 * @see #synchronizeChangedRows(DBConnection, boolean, boolean, boolean)
	 * @see RowState
	 * @see #getRemovedRowsKeyValues()
	 *
	 * @since 3.1
	 */
	public void synchronizeChangedRows(final boolean doInserts, final boolean doUpdates,
			final boolean doDeletes)
			throws VirtualTableException, DBException, IllegalArgumentException
	{
		synchronizeChangedRows(null,this.columns,doInserts,doUpdates,doDeletes,null);
	}


	/**
	 * Synchronizes all added, changed and removed rows via
	 * <code>connection</code>.
	 * <p>
	 * If you want to embed this action in a transaction automatically, use
	 * {@link #synchronizeChangedRows()}.
	 *
	 * @param connection
	 *            A connection to the underlying data source.
	 * @throws VirtualTableException
	 *             If a error in this VirtualTable occurs
	 * @throws DBException
	 *             If an error occurs while writing the database
	 *
	 * @see #synchronizeChangedRows()
	 * @see RowState
	 * @see #getRemovedRowsKeyValues()
	 */
	public void synchronizeChangedRows(final DBConnection<?> connection)
			throws VirtualTableException, DBException
	{
		synchronizeChangedRows(connection,this.columns,true,true,true,null);
	}


	/**
	 * Synchronizes electively added, changed and removed rows via
	 * <code>connection</code>.
	 * <p>
	 * If you want to embed this action in a transaction automatically, use
	 * {@link #synchronizeChangedRows()}.
	 *
	 * @param connection
	 *            A connection to the underlying data source.
	 * @param doInserts
	 *            <code>true</code> if added rows should be synchronized
	 * @param doUpdates
	 *            <code>true</code> if changed rows should be synchronized
	 * @param doDeletes
	 *            <code>true</code> if removed rows should be synchronized
	 * @throws VirtualTableException
	 *             If a error in this VirtualTable occurs
	 * @throws DBException
	 *             If an error occurs while writing the database
	 * @throws IllegalArgumentException
	 *             If none of <code>doInserts</code>, <code>doUpdates</code> or
	 *             <code>doDeletes</code> is <code>true</code>
	 *
	 * @see #synchronizeChangedRows()
	 * @see RowState
	 * @see #getRemovedRowsKeyValues()
	 *
	 * @since 3.1
	 */
	public void synchronizeChangedRows(final DBConnection<?> connection, final boolean doInserts,
			final boolean doUpdates, final boolean doDeletes)
			throws VirtualTableException, DBException, IllegalArgumentException
	{
		synchronizeChangedRows(connection,this.columns,doInserts,doUpdates,doDeletes,null);
	}


	private void synchronizeChangedRows(final DBConnection<?> connection,
			VirtualTableColumn[] columns, final boolean doInserts, final boolean doUpdates,
			final boolean doDeletes, final List<Integer> updateRows)
			throws VirtualTableException, DBException, IllegalArgumentException
	{
		if(!(doInserts || doUpdates || doDeletes))
		{
			throw new IllegalArgumentException(
					"At least one of doInserts, doUpdates or doDeletes must be true");
		}

		final VirtualTableColumn[] pk = getPrimaryKeyColumns();
		if(pk.length == 0)
		{
			throw new VirtualTableException(this,"Cannot synchronize: No primary key defined");
		}
		final int[] pkIndices = getColumnIndices(pk);

		// remove non persistant columns
		final List<VirtualTableColumn> list = new ArrayList();
		for(final VirtualTableColumn column : columns)
		{
			if(column.isPersistent())
			{
				list.add(column);
			}
		}
		columns = new VirtualTableColumn[list.size()];
		list.toArray(columns);

		final int[] colIndices = getColumnIndices(columns);

		final List<WriteRequest> updateStatements = new ArrayList();
		final List<WriteRequest> insertStatements = new ArrayList();
		final List<WriteRequest> deleteStatements = new ArrayList();
		final IntList updateIndices = new IntList();
		final IntList insertIndices = new IntList();

		if(doUpdates || doInserts)
		{
			for(int i = 0; i < this.data.size(); i++)
			{
				final VirtualTableRow r = this.data.get(i);

				if(updateRows == null || updateRows.contains(i))
				{
					
					if(r.rowState == RowState.UPDATED && doUpdates)
					{
						final UPDATE update = new UPDATE(this);
						final List values = new ArrayList();

						for(int c = 0; c < columns.length; c++)
						{
							final VirtualTableColumn cd = columns[c];
							// Don't update auto incremented primary key columns
							if(!(Arrays.binarySearch(pkIndices,c) >= 0 && cd.isAutoIncrement()))
							{
								update.SET(cd.toSqlField(),"?");
								values.add(r.get(colIndices[c]));
							}
						}

						final WHERE where = new WHERE();
						for(int pi = 0; pi < pk.length; pi++)
						{
							where.and(pk[pi].eq("?"));
							values.add(r.get(pkIndices[pi]));
						}
						update.WHERE(where);

						updateStatements.add(new WriteRequest(update,false,values.toArray()));
						updateIndices.add(i);
					}
					else if(r.rowState == RowState.ADDED && doInserts)
					{
						final INSERT insert = new INSERT().INTO(this);
						final List values = new ArrayList();

						for(int c = 0; c < columns.length; c++)
						{
							final VirtualTableColumn cd = columns[c];

							if(!cd.isAutoIncrement())
							{
								insert.assign(cd,"?");
								values.add(r.get(colIndices[c]));
							}
						}

						insertStatements.add(
								new WriteRequest(insert,getAutoValueColumns(),values.toArray()));
						insertIndices.add(i);
					}
				}
			}
		}

		if(doDeletes)
		{
			for(final KeyValues keyValues : this.removedRowKeys)
			{
				final DELETE delete = new DELETE().FROM(this);
				final List values = new ArrayList();
				final WHERE where = new WHERE();
				keyValues.appendCondition(where,values,this);
				delete.WHERE(where);
				deleteStatements.add(new WriteRequest(delete,false,values.toArray()));
			}
		}

		class Writer
		{
			void write(final DBConnection connection) throws DBException
			{
				int statementCount = updateStatements.size();
				if(statementCount > 0)
				{
					final WriteRequest[] updates = updateStatements
							.toArray(new WriteRequest[statementCount]);
					writeDB(connection,updates);

					for(int i = 0; i < statementCount; i++)
					{
						VirtualTable.this.data
								.get(updateIndices.get(i)).rowState = RowState.UNCHANGED;
					}
				}

				statementCount = insertStatements.size();
				if(statementCount > 0)
				{
					final WriteRequest[] inserts = insertStatements
							.toArray(new WriteRequest[statementCount]);

					int insertIndex = 0;
					for(final WriteResult result : writeDB(connection,inserts))
					{
						if(result.hasGeneratedKeys())
						{
							final Result rs = result.getGeneratedKeys();
							final int columnCount = rs.getColumnCount();
							try
							{
								while(rs.next())
								{
									final int row = insertIndices.get(insertIndex++);
									for(int c = 0; c < columnCount; c++)
									{
										int col = getColumnIndex(rs.getMetadata(c).getName());
										if(col == -1 && columnCount == 1)
										{
											col = getAutoValue(c);
										}
										if(col != -1)
										{
											setValueAt(rs.getObject(c),row,col,false,false,false);
										}
									}
								}
							}
							finally
							{
								result.close();
							}
						}
					}

					for(int i = 0; i < statementCount; i++)
					{
						VirtualTable.this.data
								.get(insertIndices.get(i)).rowState = RowState.UNCHANGED;
					}
				}

				statementCount = deleteStatements.size();
				if(statementCount > 0)
				{
					final WriteRequest[] deletes = deleteStatements
							.toArray(new WriteRequest[statementCount]);
					writeDB(connection,deletes);

					VirtualTable.this.removedRowKeys.clear();
				}
			}
		}
		final Writer writer = new Writer();

		if(connection != null)
		{
			writer.write(connection);
		}
		else
		{
			new Transaction(getDataSource())
			{
				@Override
				protected void write(final DBConnection connection) throws DBException
				{
					writer.write(connection);
				}
			}.execute();
		}
	}


	private WriteResult writeDB(DBConnection connection, final WritingQuery query,
			final String[] columnNames, final Object... params) throws DBException
	{
		boolean close = false;

		if(connection == null)
		{
			connection = getDataSource().openConnection();
			close = true;
		}

		try
		{
			return connection.write(query,columnNames,params);
		}
		finally
		{
			if(close)
			{
				connection.close();
			}
		}
	}


	private WriteResult writeDB(DBConnection connection, final WritingQuery query,
			final boolean returnGeneratedKeys, final Object... params) throws DBException
	{
		boolean close = false;

		if(connection == null)
		{
			connection = getDataSource().openConnection();
			close = true;
		}

		try
		{
			return connection.write(query,returnGeneratedKeys,params);
		}
		finally
		{
			if(close)
			{
				connection.close();
			}
		}
	}


	private WriteResult[] writeDB(final DBConnection connection, final WriteRequest... requests)
			throws DBException
	{
		final WriteResult[] results = new WriteResult[requests.length];

		for(int i = 0; i < requests.length; i++)
		{
			results[i] = requests[i].execute(connection);
		}

		return results;
	}


	/**
	 * Removes a {@link VirtualTableColumn} from this {@link VirtualTable}.
	 *
	 * @param columnName
	 *            the name of the column to be removed.
	 *
	 * @throws VirtualTableException
	 *             if no column with this name was found.
	 */
	public void removeColumn(final String columnName) throws VirtualTableException
	{
		final int col = getColumnIndex(columnName);
		if(col < 0)
		{
			throw new VirtualTableException(this,"Column '" + columnName + "' not found");
		}

		removeColumn(col);
	}


	/**
	 * Removes a {@link VirtualTableColumn} from this {@link VirtualTable}.
	 *
	 * @param col
	 *            the index of the column to be removed.
	 *
	 * @throws ArrayIndexOutOfBoundsException
	 *             if <code>col &lt; -1</code> or
	 *             <code>col > getColumnCount-1</code>
	 */
	public void removeColumn(final int col) throws ArrayIndexOutOfBoundsException
	{
		final VirtualTableColumn vtCol = getColumnAt(col);
		vtCol.setVirtualTable(null);

		this.columnNameToIndex.clear();
		this.columnSimpleNameToIndex.clear();
		this.autoValueColumns = null;

		this.columns = ArrayUtils.remove(VirtualTableColumn.class,this.columns,col);
		// maxVal = ArrayUtils.remove(maxVal,col);
		this.columnFlags = ArrayUtils.remove(this.columnFlags,col);

		this.columnToIndex.remove(col);
		for(final VirtualTableIndex index : this.indices)
		{
			index.removeCol(vtCol);
		}

		this.data.removeColumn(col);

		fireStructureChanged();
	}


	private String getKey(final String key, final Map<String, Object> map)
	{
		if(map != null)
		{
			for(final String retKey : map.keySet())
			{
				String s = retKey;
				final int i = s.lastIndexOf('.');
				if(i > 0)
				{
					s = s.substring(i + 1);
				}

				if(s.equalsIgnoreCase(key))
				{
					return retKey;
				}
			}
		}

		return null;
	}


	/**
	 * Returns the index of the nth column in this {@link VirtualTable} that is
	 * auto incremented.
	 * <p>
	 * Defaults to the value of the first auto incremented column, if none war
	 * found for <code>nr</code>.
	 * </p>
	 *
	 * @param nr
	 *            the nr of the auto incremented column, which index should be
	 *            returned
	 * @return a column index, or -1 if no auto incremented column was found.
	 */
	public int getAutoValue(final int nr)
	{
		int found = 0;
		for(int i = 0; i < this.columns.length; i++)
		{
			if(this.columns[i].isAutoIncrement())
			{
				if(found == nr)
				{
					return i;
				}
				else
				{
					found++;
				}
			}
		}

		return getFirstAutoValue();
	}


	/**
	 * Returns the index of the first column in this {@link VirtualTable} that
	 * is auto incremented.
	 *
	 * @return a column index, or -1 if no auto incremented column was found.
	 */
	public int getFirstAutoValue()
	{
		for(int i = 0; i < this.columns.length; i++)
		{
			if(this.columns[i].isAutoIncrement())
			{
				return i;
			}
		}

		return -1;
	}


	/**
	 * Returns an array of column names for all auto incremented columns.
	 *
	 * @return array of column names
	 */
	public String[] getAutoValueColumns()
	{
		if(this.autoValueColumns == null)
		{
			final List<String> list = new ArrayList();
			for(int i = 0; i < this.columns.length; i++)
			{
				if(this.columns[i].isAutoIncrement())
				{
					list.add(this.columns[i].getName());
				}
			}

			this.autoValueColumns = list.toArray(new String[list.size()]);
		}

		return this.autoValueColumns;
	}


	/**
	 * Checks and returns a value.
	 * <p>
	 * Checks for:
	 * <ul>
	 * <li><code>null</code> value for NotNull columns</li>
	 * <li>correct type according to the specified column</li>
	 * </ul>
	 * </p>
	 * <p>
	 * {@link Validator}s of the {@link VirtualTableColumn} at
	 * <code>columnIndex</code> are invoked too.
	 * </p>
	 *
	 * @param columnIndex
	 *            the index of the column for the value
	 *
	 * @param value
	 *            the value to be checked
	 *
	 * @return the checked value
	 *
	 * @throws VirtualTableException
	 *             if check failed or the <code>value</code> is null but the
	 *             column isn't nullable
	 *
	 * @see VirtualTableColumn#addValidator(Validator)
	 */
	public Object checkValue(final int columnIndex, Object value) throws VirtualTableException
	{
		value = checkValue0(columnIndex,value);

		/*
		 * Added in 3.1, user defined validation
		 */
		this.columns[columnIndex].validate(value);

		return value;
	}


	private Object checkValue0(final int columnIndex, final Object value)
			throws VirtualTableException
	{
		final VirtualTableColumn vtColumn = this.columns[columnIndex];

		if(value == null)
		{
			if(vtColumn.isNullable())
			{
				return null;
			}
			else
			{
				/*
				 * (22.02.2010 TM)NOTE: extended exception message
				 * "null not allowed" with more details after request for more
				 * detailed exception messages.
				 */
				throw new VirtualTableException(this,"null not allowed: column " + columnIndex
						+ " (" + vtColumn.getName() + ") is not nullable but value is null.");
			}
		}
		else if(value.toString().length() == 0 && vtColumn.isNullable()
				&& !vtColumn.getType().isString())
		{
			return null;
		}

		return createValue(value,columnIndex);
	}


	private Object createValue(final Object value, final int col) throws VirtualTableException
	{
		final VirtualTableColumn cd = this.columns[col];

		switch(cd.getType())
		{
			case TINYINT:
				return createByte(col,value);
			
			case SMALLINT:
				return createShort(col,value);
			
			case INTEGER:
				return createInt(col,value);
			
			case BIGINT:
				return createLong(col,value);
			
			case REAL:
				return createFloat(col,value);
			
			case FLOAT:
			case DOUBLE:
			case NUMERIC:
			case DECIMAL:
				return createDouble(col,value);
			
			case CHAR:
			case VARCHAR:
			case LONGVARCHAR:
				return createString(col,value);
			
			case CLOB:
			{
				final XdevClob clob = createClob(col,value);
				try
				{
					clob.readFully();
				}
				catch(final DBException e)
				{
					throw new VirtualTableException(this,e);
				}
				return clob;
			}
			
			case BINARY:
			case VARBINARY:
			case LONGVARBINARY:
				return createBytes(col,value);
			
			case BLOB:
			{
				final XdevBlob blob = createBlob(col,value);
				try
				{
					blob.readFully();
				}
				catch(final DBException e)
				{
					throw new VirtualTableException(this,e);
				}
				return blob;
			}
			
			case DATE:
			case TIME:
			case TIMESTAMP:
				return createDate(col,value);
			
			case BOOLEAN:
				return createBoolean(col,value);
		}

		throw new VirtualTableException(this,"Unkown type");
	}


	private Byte createByte(final int col, final Object value) throws VirtualTableException
	{
		try
		{
			return ConversionUtils.toByte(value);
		}
		catch(final ObjectConversionException oce)
		{
			try
			{
				final Object o = this.columns[col].getTextFormat().getFormat()
						.parseObject(value.toString());
				if(o instanceof Byte)
				{
					return (Byte)o;
				}
				else if(o instanceof Number)
				{
					return ((Number)o).byteValue();
				}
			}
			catch(final ParseException pe)
			{
			}
		}

		throw new VirtualTableException(this,
				"Not a byte: '" + value + "' -> " + this.name + "." + this.columns[col].getName());
	}


	private Short createShort(final int col, final Object value) throws VirtualTableException
	{
		try
		{
			return ConversionUtils.toShort(value);
		}
		catch(final ObjectConversionException oce)
		{
			try
			{
				final Object o = this.columns[col].getTextFormat().getFormat()
						.parseObject(value.toString());
				if(o instanceof Short)
				{
					return (Short)o;
				}
				else if(o instanceof Number)
				{
					return ((Number)o).shortValue();
				}
			}
			catch(final ParseException pe)
			{
			}
		}

		throw new VirtualTableException(this,
				"Not a short: '" + value + "' -> " + this.name + "." + this.columns[col].getName());
	}


	private Integer createInt(final int col, final Object value) throws VirtualTableException
	{
		try
		{
			return ConversionUtils.toInteger(value);
		}
		catch(final ObjectConversionException oce)
		{
			try
			{
				final Object o = this.columns[col].getTextFormat().getFormat()
						.parseObject(value.toString());
				if(o instanceof Integer)
				{
					return (Integer)o;
				}
				else if(o instanceof Number)
				{
					return ((Number)o).intValue();
				}
			}
			catch(final ParseException pe)
			{
			}
		}

		throw new VirtualTableException(this,
				"Not an int: '" + value + "' -> " + this.name + "." + this.columns[col].getName());
	}


	private Long createLong(final int col, final Object value) throws VirtualTableException
	{
		try
		{
			return ConversionUtils.toLong(value);
		}
		catch(final ObjectConversionException oce)
		{
			try
			{
				final Object o = this.columns[col].getTextFormat().getFormat()
						.parseObject(value.toString());
				if(o instanceof Long)
				{
					return (Long)o;
				}
				else if(o instanceof Number)
				{
					return ((Number)o).longValue();
				}
			}
			catch(final ParseException pe)
			{
			}
		}

		throw new VirtualTableException(this,
				"Not a long: '" + value + "' -> " + this.name + "." + this.columns[col].getName());
	}


	private Float createFloat(final int col, final Object value) throws VirtualTableException
	{
		try
		{
			return ConversionUtils.toFloat(value);
		}
		catch(final ObjectConversionException oce)
		{
			try
			{
				final Object o = this.columns[col].getTextFormat().getFormat()
						.parseObject(value.toString());
				if(o instanceof Float)
				{
					return (Float)o;
				}
				else if(o instanceof Number)
				{
					return ((Number)o).floatValue();
				}
			}
			catch(final ParseException pe)
			{
			}
		}

		throw new VirtualTableException(this,
				"Not a float: '" + value + "' -> " + this.name + "." + this.columns[col].getName());
	}


	private Double createDouble(final int col, final Object value) throws VirtualTableException
	{
		try
		{
			return ConversionUtils.toDouble(value);
		}
		catch(final ObjectConversionException oce)
		{
			try
			{
				final Object o = this.columns[col].getTextFormat().getFormat()
						.parseObject(value.toString());
				if(o instanceof Double)
				{
					return (Double)o;
				}
				else if(o instanceof Number)
				{
					return ((Number)o).doubleValue();
				}
			}
			catch(final ParseException pe)
			{
			}
		}

		throw new VirtualTableException(this,"Not a double: '" + value + "' -> " + this.name + "."
				+ this.columns[col].getName());
	}


	private String createString(final int col, final Object value) throws VirtualTableException
	{
		// optimization
		if(value == null)
		{
			return null;
		}

		try
		{
			final String str = ConversionUtils.toString(value);
			final int length = this.columns[col].getLength();
			int strLength;
			if(length > 0 && (strLength = str.length()) > length)
			{
				if(this.columns[col].isAutoTruncate())
				{
					return str.substring(0,length);
				}
				else
				{
					throw new VirtualTableException(this,
							"String length out of bounds, " + strLength + " > " + length + ", in "
									+ getName() + "." + this.columns[col].getName());
				}
			}
			else
			{
				return str;
			}
		}
		catch(final ObjectConversionException e)
		{
			throw new VirtualTableException(this,"Not a string: '" + value + "' -> " + this.name
					+ "." + this.columns[col].getName());
		}
	}


	private XdevClob createClob(final int col, final Object value) throws VirtualTableException
	{
		try
		{
			return ConversionUtils.toXdevClob(value);
		}
		catch(final ObjectConversionException e)
		{
			throw new VirtualTableException(this,"Not a CLOB: '" + value + "' -> " + this.name + "."
					+ this.columns[col].getName());
		}
	}


	private byte[] createBytes(final int col, final Object value) throws VirtualTableException
	{
		try
		{
			return ConversionUtils.toBytes(value);
		}
		catch(final ObjectConversionException e)
		{
			throw new VirtualTableException(this,"No bytes: '" + value + "' -> " + this.name + "."
					+ this.columns[col].getName());
		}
	}


	private XdevBlob createBlob(final int col, final Object value) throws VirtualTableException
	{
		try
		{
			return ConversionUtils.toXdevBlob(value);
		}
		catch(final ObjectConversionException e)
		{
			throw new VirtualTableException(this,"Not a BLOB: '" + value + "' -> " + this.name + "."
					+ this.columns[col].getName());
		}
	}


	/**
	 * Creates and returns a {@link Date} object, that fits the type of the
	 * specified column.
	 *
	 * @param col
	 *            the {@link VirtualTableColumn} to create a date for
	 *
	 * @param value
	 *            the Object to create the new {@link Date} from
	 *
	 * @return a {@link Date} object
	 *
	 * @throws VirtualTableException
	 *             thrown if the creation of the {@link Date} failed.
	 */
	public Date createDate(final VirtualTableColumn col, final Object value)
			throws VirtualTableException
	{
		return createDate(getColumnIndex(col),value);
	}


	/**
	 * Creates and returns a {@link Date} object, that fits the type of the
	 * specified column.
	 *
	 * @param col
	 *            the index of the {@link VirtualTableColumn} to create a date
	 *            for
	 *
	 * @param value
	 *            the Object to create the new {@link Date} from
	 *
	 * @return a {@link Date} object
	 *
	 * @throws VirtualTableException
	 *             thrown if the creation of the {@link Date} failed.
	 */
	private Date createDate(final int col, final Object value) throws VirtualTableException
	{
		Date date = null;
		try
		{
			date = ConversionUtils.toDate(value);
		}
		catch(final ObjectConversionException oce)
		{
			try
			{
				date = this.columns[col].getTextFormat().parseDate(value.toString());
			}
			catch(final ParseException pe)
			{
			}
		}

		if(date != null)
		{
			switch(this.columns[col].getType())
			{
				case DATE:
					return new java.sql.Date(date.getTime());
				
				case TIME:
					return new java.sql.Time(date.getTime());
				
				case TIMESTAMP:
					return new java.sql.Timestamp(date.getTime());
			}
		}

		throw new VirtualTableException(this,
				"Not a date: '" + value + "' -> " + this.name + "." + this.columns[col].getName());
	}


	private Boolean createBoolean(final int col, final Object value)
	{
		try
		{
			return ConversionUtils.toBoolean(value);
		}
		catch(final ObjectConversionException e)
		{
			throw new VirtualTableException(this,"Not a boolean: '" + value + "' -> " + this.name
					+ "." + this.columns[col].getName());
		}
	}



	private class VirtualTableData implements Iterable<VirtualTableRow>, Serializable
	{
		private static final long	serialVersionUID	= 5587743513073942290L;

		VirtualTableRow[]			data;
		int							size;


		VirtualTableData(final int initialCapacity)
		{
			this.data = new VirtualTableRow[initialCapacity];
			this.size = 0;
		}


		synchronized void add(final VirtualTableRow row, final boolean markAsAdded)
		{
			tryInsertInIndex(row);

			ensureCapacity(this.size + 1);
			row.index = this.size;
			this.data[this.size++] = row;

			if(markAsAdded)
			{
				row.rowState = RowState.ADDED;
			}
		}


		synchronized void insert(final VirtualTableRow row, final int index,
				final boolean markAsAdded)
		{
			tryInsertInIndex(row);

			ensureCapacity(this.size + 1);
			System.arraycopy(this.data,index,this.data,index + 1,this.size - index);
			this.data[index] = row;
			this.size++;
			for(int i = index; i < this.size; i++)
			{
				this.data[i].index = i;
			}

			if(markAsAdded)
			{
				row.rowState = RowState.ADDED;
			}
		}


		void tryInsertInIndex(final VirtualTableRow row)
		{
			for(final VirtualTableIndex vtIndex : VirtualTable.this.indices)
			{
				if(!vtIndex.put(row) && vtIndex.isUnique)
				{
					reportUniqueIndexDoubleValues(row,vtIndex);
				}
			}
		}


		void ensureCapacity(final int minCapacity)
		{
			final int oldCapacity = this.data.length;
			if(minCapacity > oldCapacity)
			{
				final Object oldData[] = this.data;
				int newCapacity = (oldCapacity * 3) / 2 + 1;
				if(newCapacity < minCapacity)
				{
					newCapacity = minCapacity;
				}
				this.data = new VirtualTableRow[newCapacity];
				System.arraycopy(oldData,0,this.data,0,this.size);
			}
		}


		synchronized VirtualTableRow get(final int i)
		{
			return this.data[i];
		}


		synchronized VirtualTableRow[] getAll()
		{
			final VirtualTableRow[] array = new VirtualTableRow[this.size];
			System.arraycopy(this.data,0,array,0,this.size);
			return array;
		}


		synchronized VirtualTableRow[] get(final int startIndex, final int endIndex)
		{
			final VirtualTableRow[] array = new VirtualTableRow[endIndex - startIndex];
			System.arraycopy(this.data,startIndex,array,0,endIndex - startIndex);
			return array;
		}


		synchronized int size()
		{
			return this.size;
		}


		synchronized VirtualTableRow remove(final int index)
		{
			final VirtualTableRow row = this.data[index];
			for(final VirtualTableIndex vtIndex : VirtualTable.this.indices)
			{
				vtIndex.remove(row);
			}

			final int numMoved = this.size - index - 1;
			if(numMoved > 0)
			{
				System.arraycopy(this.data,index + 1,this.data,index,numMoved);
			}
			this.data[--this.size] = null;

			for(int i = index; i < this.size; i++)
			{
				this.data[i].index = i;
			}
			row.index = -1;

			fireRowDeleted(row,index);

			return row;
		}


		synchronized void removeColumn(final int col)
		{
			for(int i = 0; i < this.size; i++)
			{
				this.data[i].remove(col);
			}

			for(final VirtualTableIndex vtIndex : VirtualTable.this.indices)
			{
				vtIndex.clear();
				for(int i = 0; i < this.size; i++)
				{
					vtIndex.put(this.data[i]);
				}
			}
		}


		synchronized void clear()
		{
			for(int i = 0; i < this.size; i++)
			{
				this.data[i].index = -1;
				this.data[i] = null;
			}
			this.size = 0;
		}


		/**
		 * Returns an iterator over all rows of this VirtualTable in their
		 * natural order.
		 * <p>
		 * Note: The returned iterator doesn't support {@link Iterator#remove()}
		 * .
		 * </p>
		 *
		 *
		 * @return An interator over the rows
		 */

		@Override
		public Iterator<VirtualTableRow> iterator()
		{
			return ArrayUtils.getIterator(this.data,0,this.size);
		}


		synchronized void sortByCol(final int columnIndex, final boolean ascending)
		{
			final Comparator columnComparator = VirtualTable.this.columns[columnIndex]
					.getComparator();
			final Comparator comparator = new Comparator<VirtualTableRow>()
			{
				@Override
				public int compare(final VirtualTableRow row1, final VirtualTableRow row2)
				{
					int value = columnComparator.compare(row1.data[columnIndex],
							row2.data[columnIndex]);
					if(!ascending)
					{
						value *= -1;
					}
					return value;
				}
			};
			sortByCol(comparator);
		}


		synchronized void sortByCol(final Comparator<VirtualTableRow> comparator)
		{
			Arrays.sort(this.data,0,this.size,comparator);
			for(int i = 0; i < this.size; i++)
			{
				this.data[i].index = i;
			}
			fireDataChanged();
		}


		@Override
		public String toString()
		{
			return Arrays.toString(this.data);
		}


		public String[][] putFormattedStrings(final String[][] s, int arrayOffset)
		{
			for(int row = 0; row < this.size && arrayOffset < s.length; row++)
			{
				s[arrayOffset++] = this.data[row].toFormattedStrings();
			}
			return s;
		}
	}



	/**
	 * State of the {@link VirtualTableRow} in comparison to the underlying
	 * {@link DataSource}.
	 * <p>
	 * The {@link VirtualTable#synchronizeChangedRows()} methods use this flag
	 * to decide which row has been added or modified.
	 *
	 * @see VirtualTableRow#getRowState()
	 * @see VirtualTableRow#setRowState(RowState)
	 *
	 * @see VirtualTable#synchronizeChangedRows()
	 * @see VirtualTable#getRemovedRowsKeyValues()
	 */
	public static enum RowState
	{
		/**
		 * The row has the same values as in the underlying datasource
		 */
		UNCHANGED,

		/**
		 * One or more values of the row has been changed
		 */
		UPDATED,

		/**
		 * The row is new and doesn't exist in the underlying datasource
		 */
		ADDED
	}



	/**
	 * Represents a single row within a {@link VirtualTable}.
	 *
	 * @author XDEV Software Corp.
	 */
	public class VirtualTableRow implements Serializable
	{
		private static final long	serialVersionUID	= 136292046887180787L;

		private Object[]			data;
		private RowState			rowState			= RowState.UNCHANGED;
		private int					index				= -1;
		String						primaryKeyHash;


		/**
		 * used for cloning
		 */
		private VirtualTableRow(final VirtualTableRow original)
		{
			this(original.data.length);
			System.arraycopy(original.data,0,this.data,0,this.data.length);
		}


		private VirtualTableRow(final int initialCapacity)
		{
			this.data = new Object[initialCapacity];
		}


		/**
		 * Returns a {@link PessimisticLock} related to this row, with the given
		 * locktime as lock duration.
		 * <p>
		 * This should be used to create a {@link PessimisticLock} instance with
		 * the given lockTime.
		 * </p>
		 * <p>
		 * To actually lock this {@link VirtualTableRow} invoke
		 * {@link PessimisticLock#getLock()}
		 * </p>
		 *
		 * @return a {@link PessimisticLock} instance for this particular row.
		 * @param lockTime
		 *            the locks duration.
		 * @throws LockingException
		 * @since 4.0
		 */
		public PessimisticLock getPessimisticLock(final long lockTime) throws LockingException
		{
			return LockFactory.getPessimisticLock(this,lockTime);
		}


		/**
		 * Returns a {@link PessimisticLock} related to this row. If the lock
		 * does not already exist, a new {@link PessimisticLock} instance with
		 * its default timeout will be created in consequence.
		 * <p>
		 * To actually lock this {@link VirtualTableRow} invoke
		 * {@link PessimisticLock#getLock()}
		 * </p>
		 *
		 * @return a {@link PessimisticLock} instance for this particular row.
		 * @throws LockingException
		 * @since 4.0
		 */
		public PessimisticLock getPessimisticLock() throws LockingException
		{
			return LockFactory.getPessimisticLock(this);
		}


		/**
		 * Returns the surrounding {@link VirtualTable} of this
		 * {@link VirtualTableRow}.
		 *
		 * @return a {@link VirtualTable}
		 */
		public VirtualTable getVirtualTable()
		{
			return VirtualTable.this;
		}


		/**
		 * Sets a value for this {@link VirtualTableRow} at the specified column
		 * index.
		 *
		 * @param columnName
		 *            the name of the column
		 * @param value
		 *            the new value of the column
		 *
		 * @return the new value
		 *
		 * @throws VirtualTableException
		 *             if the value is not appropriate for the specified column
		 * @throws IllegalArgumentException
		 *             if the column <code>columnName</code> does not exist in
		 *             this {@link VirtualTable}
		 */
		public synchronized Object set(final String columnName, final Object value)
				throws VirtualTableException, IllegalArgumentException
		{
			final int col = getColumnIndex(columnName);
			if(col == -1)
			{
				throw new IllegalArgumentException("column '" + columnName + "' not found");
			}

			return set(col,value);
		}


		/**
		 * Sets a value for this {@link VirtualTableRow} at the specified column
		 * index.
		 *
		 * @param index
		 *            the index of the column to set
		 * @param value
		 *            the new value of the column
		 *
		 * @return the new value
		 *
		 * @throws VirtualTableException
		 *             if the value is not appropriate for the specified column
		 */
		public synchronized Object set(final int index, final Object value)
				throws VirtualTableException
		{
			final int rowIndex = this.index;
			if(rowIndex >= 0)
			{
				return VirtualTable.this.setValueAt(value,rowIndex,index);
			}
			else
			{
				final Object old = this.data[index];
				if(VirtualTable.equals(value,old,getColumnAt(index).getType()))
				{
					return old;
				}

				return this.data[index] = optConvertValue(checkValue(index,value));
			}
		}


		private synchronized void set(final int columnIndex, Object value,
				final boolean changeToUpdated, final boolean updateIndex)
				throws VirtualTableException
		{
			value = optConvertValue(value);

			if(VirtualTable.equals(this.data[columnIndex],value,getColumnAt(columnIndex).getType()))
			{
				return;
			}

			/*
			 * only update index if this has been added to VirtualTableData
			 */
			if(updateIndex && this.index != -1)
			{
				for(final VirtualTableIndex vtIndex : VirtualTable.this.indices)
				{
					if(vtIndex.hasCol(columnIndex))
					{
						vtIndex.remove(this);
						final String newHash = vtIndex.computeHash(this,columnIndex,value);
						if(!vtIndex.put(newHash) && vtIndex.isUnique)
						{
							reportUniqueIndexDoubleValues(this,vtIndex);
						}
						if(vtIndex.type == IndexType.PRIMARY_KEY)
						{
							this.primaryKeyHash = newHash;
						}
					}
				}
			}

			this.data[columnIndex] = value;

			// if(columns[index].isAutoIncrement() && value != null && value
			// instanceof Number)
			// {
			// long l = ((Number)value).longValue();
			// if(l > maxVal[index])
			// {
			// maxVal[index] = l;
			// }
			// }

			if(changeToUpdated && this.rowState == RowState.UNCHANGED)
			{
				this.rowState = RowState.UPDATED;
			}
		}


		private Object optConvertValue(Object value)
		{
			if(value instanceof byte[])
			{
				value = new XdevBlob((byte[])value);
			}
			else if(value instanceof char[])
			{
				value = new XdevClob((char[])value);
			}

			if(value != null && value instanceof String && Settings.trimData())
			{
				value = value.toString().trim();
			}

			return value;
		}


		/**
		 * Returns the value at the specified column index.
		 *
		 * @param i
		 *            the index of the column
		 * @return a value
		 */
		public synchronized Object get(final int i)
		{
			return this.data[i];
		}


		/**
		 * Returns the value at the specified column.
		 *
		 * @param name
		 *            the name of the column
		 * @return a value
		 * @since 3.2
		 */
		public synchronized Object get(final String name)
		{
			return this.data[getColumnIndex(name)];
		}


		/**
		 * Returns the value at the specified column.
		 *
		 * @param column
		 *            {@link VirtualTableColumn}
		 * @return the value of the cell at the specified position.
		 */
		public synchronized <T> T get(final VirtualTableColumn<T> column)
		{
			return (T)this.data[getColumnIndex(column)];
		}


		/**
		 * Returns a formatted representation for the value at <code>col</code>.
		 * The TextFormat of the VirtualTableColumn at index <code>col</code> is
		 * used to format the value.
		 *
		 *
		 * @param col
		 *            the index of the column (0-based)
		 *
		 * @return a formatted {@link String} representation of the specified
		 *         value.
		 */
		public synchronized String getFormattedValue(final int col)
		{
			return formatValue(this.data[col],col);
		}


		/**
		 * Returns a formatted string, with this row used as
		 * {@link ParameterProvider}.
		 *
		 * @param str
		 *            the {@link String} to identify the value's in the
		 *            {@link ParameterProvider}
		 *
		 * @return a formatted string
		 *
		 * @see StringUtils#format(String, ParameterProvider)
		 * @see VirtualTable#formatValue(Object, int)
		 */
		public synchronized String format(final String str)
		{
			return StringUtils.format(str,new ParameterProvider()
			{
				@Override
				public String getValue(final String key)
				{
					final int index = getColumnIndex(key);
					if(index == -1)
					{
						return "";
					}
					return formatValue(VirtualTableRow.this.data[index],index);
				}
			});
		}


		/**
		 * Removes a column from this {@link VirtualTableColumn}.
		 * <p>
		 * <b>Note:</b> The value at this position is lost.
		 * </p>
		 *
		 * @param i
		 *            the index of the column to be removed
		 */
		private synchronized void remove(final int i)
		{
			this.data = ArrayUtils.remove(Object.class,this.data,i);
		}


		/**
		 * Returns the number of columns of this {@link VirtualTableRow}.
		 *
		 * @return the number of columns
		 */
		public int size()
		{
			return this.data.length;
		}


		/**
		 * Checks if this {@link VirtualTableRow} exists in the
		 * {@link VirtualTable}.
		 *
		 * @return <code>false</code> if this {@link VirtualTableRow} exists in
		 *         the {@link VirtualTable}, <code>true</code> otherwise
		 */
		public boolean isNew()
		{
			return this.index == -1;
		}


		/**
		 * Saves a {@link VirtualTableRow}.
		 * <p>
		 * If the {@link VirtualTableRow} is already part of a
		 * {@link VirtualTable}, an update is performed. Otherwise an insert
		 * will be done.
		 * </p>
		 *
		 * @param synchronizeDB
		 *            if set to <code>true</code>, changes will be propagated to
		 *            the underlying data source.
		 * @throws VirtualTableException
		 * @throws DBException
		 *             thrown if database access failed.
		 * @since 3.1
		 */
		public void save(final boolean synchronizeDB) throws VirtualTableException, DBException
		{
			save(synchronizeDB,null);
		}


		/**
		 * Saves a {@link VirtualTableRow}.
		 * <p>
		 * If the {@link VirtualTableRow} is already part of a
		 * {@link VirtualTable}, an update is performed. Otherwise an insert
		 * will be done.
		 * </p>
		 *
		 * @param synchronizeDB
		 *            if set to <code>true</code>, changes will be propagated to
		 *            the underlying data source.
		 * @param connection
		 *            An already open connection, e.g. in a transaction, or
		 *            <code>null</code>
		 * @throws VirtualTableException
		 * @throws DBException
		 *             thrown if database access failed.
		 * @since 3.1
		 */
		public void save(final boolean synchronizeDB, final DBConnection connection)
				throws VirtualTableException, DBException
		{
			if(isNew())
			{
				insert(null,synchronizeDB,connection);
			}
			else if(synchronizeDB)
			{
				if(this.rowState == RowState.ADDED)
				{
					// row is in vt, but not in db -> INSERT

					final INSERT insert = new INSERT().INTO(VirtualTable.this);
					final List values = new ArrayList();

					for(int c = 0; c < VirtualTable.this.columns.length; c++)
					{
						final VirtualTableColumn cd = VirtualTable.this.columns[c];
						if(cd.isPersistent() && !cd.isAutoIncrement())
						{
							insert.assign(cd,"?");
							values.add(get(c));
						}
					}

					final Object[] params = values.toArray(new Object[values.size()]);
					final WriteResult result = writeDB(connection,insert,getAutoValueColumns(),
							params);
					try
					{
						if(result.hasGeneratedKeys())
						{
							updateKeys(result.getGeneratedKeys());
						}
					}
					finally
					{
						result.close();
					}

					this.rowState = RowState.UNCHANGED;
				}
				else
				{
					final UPDATE update = new UPDATE(VirtualTable.this);
					final List values = new ArrayList();

					for(int c = 0; c < VirtualTable.this.columns.length; c++)
					{
						final VirtualTableColumn cd = VirtualTable.this.columns[c];
						if(cd.isPersistent() && !cd.isAutoIncrement())
						{
							update.SET(cd.toSqlField(),"?");
							values.add(get(c));
						}
					}

					final VirtualTableColumn[] pk = getPrimaryKeyColumns();
					if(pk.length == 0)
					{
						throw new VirtualTableException(VirtualTable.this,
								"Cannot synchronize with database: No primary key defined");
					}

					final WHERE where = new WHERE();
					for(int i = 0; i < pk.length; i++)
					{
						where.and(pk[i].eq("?"));
						values.add(get(getColumnIndex(pk[i])));
					}
					update.WHERE(where);
					final Object[] params = values.toArray(new Object[values.size()]);
					writeDB(connection,update,false,params);

					this.rowState = RowState.UNCHANGED;
				}
			}
		}


		/**
		 * Saves a {@link VirtualTableRow}.
		 * <p>
		 * If the {@link VirtualTableRow} is already part of a
		 * {@link VirtualTable}, an update is performed. Otherwise an insert
		 * will be done.
		 * </p>
		 *
		 * @param data
		 *            a {@link Map} of {@link String} keys and {@link Object} as
		 *            values, containing the column names and values for the
		 *            row.
		 * @param synchronizeDB
		 *            if set to <code>true</code>, changes will be propagated to
		 *            the underlying data source.
		 * @throws VirtualTableException
		 * @throws DBException
		 *             thrown if database access failed.
		 */
		public void save(final Map<String, Object> data, final boolean synchronizeDB)
				throws VirtualTableException, DBException
		{
			save(data,synchronizeDB,null);
		}


		/**
		 * Saves a {@link VirtualTableRow}.
		 * <p>
		 * If the {@link VirtualTableRow} is already part of a
		 * {@link VirtualTable}, an update is performed. Otherwise an insert
		 * will be done.
		 * </p>
		 *
		 * @param data
		 *            a {@link Map} of {@link String} keys and {@link Object} as
		 *            values, containing the column names and values for the
		 *            row.
		 * @param synchronizeDB
		 *            if set to <code>true</code>, changes will be propagated to
		 *            the underlying data source.
		 * @param connection
		 *            An already open connection, e.g. in a transaction, or
		 *            <code>null</code>
		 * @throws VirtualTableException
		 * @throws DBException
		 *             thrown if database access failed.
		 */
		public void save(final Map<String, Object> data, final boolean synchronizeDB,
				final DBConnection connection) throws VirtualTableException, DBException
		{
			if(isNew())
			{
				insert(data,synchronizeDB,connection);
			}
			else
			{
				update(data,synchronizeDB,connection);
			}
		}


		/**
		 * Updates a {@link VirtualTableRow}.
		 *
		 * @param data
		 *            a {@link Map} of {@link String} keys and {@link Object} as
		 *            values, containing the column names and values for the
		 *            row.
		 * @param synchronizeDB
		 *            if set to <code>true</code>, changes will be propagated to
		 *            database.
		 * @throws VirtualTableException
		 * @throws DBException
		 *             thrown if database access failed.
		 */
		public void update(final Map<String, Object> data, final boolean synchronizeDB)
				throws VirtualTableException, DBException
		{
			update(data,synchronizeDB,null);
		}


		/**
		 * Updates a {@link VirtualTableRow}.
		 *
		 * @param data
		 *            a {@link Map} of {@link String} keys and {@link Object} as
		 *            values, containing the column names and values for the
		 *            row.
		 * @param synchronizeDB
		 *            if set to <code>true</code>, changes will be propagated to
		 *            database.
		 * @param connection
		 *            An already open connection, e.g. in a transaction, or
		 *            <code>null</code>
		 * @throws VirtualTableException
		 * @throws DBException
		 *             thrown if database access failed.
		 */
		public void update(final Map<String, Object> data, final boolean synchronizeDB,
				final DBConnection connection) throws VirtualTableException, DBException
		{
			updateRow(data,this.index,synchronizeDB,connection);
		}


		/**
		 * Inserts a {@link VirtualTableRow} into its {@link VirtualTable}.
		 *
		 * @param data
		 *            a {@link Map} of {@link String} keys and {@link Object} as
		 *            values, containing the column names and values for the
		 *            row.
		 * @param synchronizeDB
		 *            if set to <code>true</code>, changes will be propagated to
		 *            database.
		 * @throws VirtualTableException
		 * @throws DBException
		 *             thrown if database access failed.
		 */
		public void insert(final Map<String, Object> data, final boolean synchronizeDB)
				throws VirtualTableException, DBException
		{
			insert(data,synchronizeDB,null);
		}


		/**
		 * Inserts a {@link VirtualTableRow} into its {@link VirtualTable}.
		 *
		 * @param data
		 *            a {@link Map} of {@link String} keys and {@link Object} as
		 *            values, containing the column names and values for the
		 *            row.
		 * @param synchronizeDB
		 *            if set to <code>true</code>, changes will be propagated to
		 *            database.
		 * @param connection
		 *            An already open connection, e.g. in a transaction, or
		 *            <code>null</code>
		 * @throws VirtualTableException
		 * @throws DBException
		 *             thrown if database access failed.
		 */
		public void insert(final Map<String, Object> data, final boolean synchronizeDB,
				final DBConnection connection) throws VirtualTableException, DBException
		{
			addRow(this,data,synchronizeDB,false,connection);
		}


		/**
		 * Deletes a {@link VirtualTableRow} from its {@link VirtualTable}.
		 *
		 * @param synchronizeDB
		 *            if set to <code>true</code>, changes will be propagated to
		 *            database.
		 * @throws VirtualTableException
		 * @throws DBException
		 *             thrown if database access failed.
		 */
		public void delete(final boolean synchronizeDB) throws VirtualTableException, DBException
		{
			removeRow(this.index,synchronizeDB);
		}


		/**
		 * Reloads the contents of this {@link VirtualTableRow} from the
		 * database.
		 * <p>
		 * This is an alias for {@link #reload(DBDataSource)}.
		 * </p>
		 *
		 * @throws VirtualTableException
		 * @throws DBException
		 *             thrown if database access failed.
		 */
		public void reload() throws VirtualTableException, DBException
		{
			reload(getDataSource());
		}


		/**
		 * Reloads the contents of this {@link VirtualTableRow} from the
		 * database.
		 *
		 * @param dataSource
		 *            the {@link DBDataSource} to be used to do the reloads
		 * @throws VirtualTableException
		 * @throws DBException
		 *             thrown if database access failed.
		 */
		public void reload(final DBDataSource dataSource) throws VirtualTableException, DBException
		{
			reloadImpl(dataSource,true);
		}


		/**
		 * Reloads the contents of this {@link VirtualTableRow} from the
		 * database with the default query.
		 * <p>
		 * This is an alias for {@link #reloadDefault(DBDataSource)}.
		 * </p>
		 *
		 * @param dataSource
		 *            the {@link DBDataSource} to be used to do the reloads
		 * @throws VirtualTableException
		 * @throws DBException
		 *             thrown if database access failed.
		 * @since 5.0
		 */
		public void reloadDefault() throws VirtualTableException, DBException
		{
			reloadDefault(getDataSource());
		}


		/**
		 * Reloads the contents of this {@link VirtualTableRow} from the
		 * database with the default query.
		 *
		 * @throws VirtualTableException
		 * @throws DBException
		 *             thrown if database access failed.
		 * @since 5.0
		 */
		public void reloadDefault(final DBDataSource dataSource)
				throws VirtualTableException, DBException
		{
			reloadImpl(dataSource,false);
		}


		private void reloadImpl(final DBDataSource dataSource, final boolean useLastQuery)
				throws VirtualTableException, DBException
		{
			final KeyValues keyValues = new KeyValues(this);

			SELECT select;
			List values;
			if(useLastQuery && VirtualTable.this.lastQuery != null)
			{
				select = VirtualTable.this.lastQuery.getSelect().clone();
				values = new XdevList(VirtualTable.this.lastQuery.getParameters());
			}
			else
			{
				select = getSelect();
				values = new XdevList();
			}

			WHERE where = select.getWhere();
			if(where == null)
			{
				where = new WHERE();
				select.WHERE(where);
			}

			keyValues.appendCondition(where,values);

			final DBConnection connection = dataSource.openConnection();
			try
			{
				final Result result = connection.query(select,values.toArray());
				try
				{
					if(!result.next())
					{
						throw new VirtualTableRowDeletedInDbException(this);
					}

					for(final VirtualTableIndex vtIndex : VirtualTable.this.indices)
					{
						vtIndex.remove(this);
					}

					final int[] colIndices = getColumnIndices(result,false);
					for(int i = 0; i < colIndices.length; i++)
					{
						if(colIndices[i] != -1)
						{
							final Object val = result.getObject(i);
							set(colIndices[i],checkValue(colIndices[i],val),false,false);
						}
					}

					for(final VirtualTableIndex vtIndex : VirtualTable.this.indices)
					{
						vtIndex.put(this);
					}
				}
				finally
				{
					result.close();
				}
			}
			finally
			{
				connection.close();
			}
		}


		/**
		 * Custom toString Implementation for {@link VirtualTableRow}.
		 * <p>
		 * The String contains all elements of the VirtualTableRow in a comma
		 * separated list
		 * </p>
		 *
		 * @return a {@link String} representation of this
		 *         {@link VirtualTableRow}.
		 */
		@Override
		public String toString()
		{
			return Arrays.toString(this.data);
		}


		/**
		 * Returns an array of formatted {@link String} representations for this
		 * {@link VirtualTableRow}.
		 *
		 * @return an array of Strings
		 */
		public String[] toFormattedStrings()
		{
			final int len = this.data.length;
			final String[] s = new String[len];
			for(int col = 0; col < len; col++)
			{
				s[col] = formatValue(this.data[col],col);
			}
			return s;
		}


		/**
		 * Returns all values of this row as an {@link Map}. The keys of the map
		 * are the column names, the entries are the values.
		 *
		 * @return all values of this row as an {@link Map}.
		 */
		public Map<String, Object> toMap()
		{
			final Map<String, Object> map = new HashMap<String, Object>(
					VirtualTable.this.columns.length);
			for(int col = 0; col < VirtualTable.this.columns.length; col++)
			{
				map.put(VirtualTable.this.columns[col].getName(),this.data[col]);
			}
			return map;
		}


		/**
		 * Returns all values of this row as a {@link XdevList}.
		 *
		 * @return all values of this row as a {@link XdevList}.
		 */
		public XdevList toList()
		{
			final int c = VirtualTable.this.columns.length;
			final XdevList list = new XdevList(c);
			for(int i = 0; i < c; i++)
			{
				list.add(this.data[i]);
			}
			return list;
		}


		/**
		 * Returns all values of this row as an array.
		 *
		 * @return all values of this row as an array.
		 */
		public Object[] getValues()
		{
			final Object[] values = new Object[VirtualTable.this.columns.length];
			System.arraycopy(this.data,0,values,0,VirtualTable.this.columns.length);
			return values;
		}


		/**
		 * {@inheritDoc}
		 */
		@Override
		public boolean equals(final Object obj)
		{
			if(obj == this)
			{
				return true;
			}

			if(obj instanceof VirtualTableRow)
			{
				/*
				 * TODO: Check if comparing primary keys is sufficient
				 */
				final VirtualTableRow other = (VirtualTableRow)obj;
				return getVirtualTable().equals(other.getVirtualTable()) && equalsValues(other);
			}

			return false;
		}


		/**
		 * Checks if the values of this row are the same as the values of an
		 * <code>other</code> row.
		 *
		 * @param other
		 *            The row to compare
		 * @return <code>true</code> if the values of both rows are equal,
		 *         <code>false</code> otherwise
		 * @see VirtualTable#equals(Object, Object)
		 */
		public boolean equalsValues(final VirtualTableRow other)
		{
			final Object[] data1 = this.data;
			final Object[] data2 = other.data;

			final int length = data1.length;
			if(length != data2.length)
			{
				return false;
			}

			for(int i = 0; i < length; i++)
			{
				if(!VirtualTable.equals(data1[i],data2[i],getColumnAt(i).getType()))
				{
					return false;
				}
			}

			return true;
		}


		/**
		 * Updates the auto value columns with values from
		 * <code>generatedKeys</code>.
		 * <p>
		 * This method gets called automatically after a row is inserted into
		 * the database.
		 *
		 * @param generatedKeys
		 *            the generated key values
		 * @throws DBException
		 *             if an database error occurs
		 */
		public void updateKeys(final Result generatedKeys) throws DBException
		{
			final int columnCount = generatedKeys.getColumnCount();
			try
			{
				if(generatedKeys.next())
				{
					for(int c = 0; c < columnCount; c++)
					{
						int col = getColumnIndex(generatedKeys.getMetadata(c).getName());
						if(col == -1 && columnCount == 1)
						{
							col = getAutoValue(c);
						}
						if(col != -1)
						{
							Object generatedKey = generatedKeys.getObject(c);
							generatedKey = checkValue(col,generatedKey);
							set(col,generatedKey,false,false);
						}
					}
				}
			}
			finally
			{
				generatedKeys.close();
			}
		}


		/**
		 * Returns the state of this row
		 *
		 * @return the state of this row
		 *
		 * @since 3.1
		 */
		public RowState getRowState()
		{
			return this.rowState;
		}


		/**
		 * Updates the state of this row.
		 *
		 * @param rowState
		 *            the new state
		 *
		 * @since 3.1
		 */
		public void setRowState(final RowState rowState)
		{
			this.rowState = rowState;
		}
	}



	/**
	 * Hash values are only computed and stored if the index is unique.
	 */
	private class VirtualTableIndex implements Serializable
	{
		private static final long				serialVersionUID	= 7320439821022328063L;

		final Index								_originalIndex;

		final String							name;
		final IndexType							type;
		VirtualTableColumn[]					columns;
		int[]									columnIndices;
		final boolean							isUnique;
		HashSet<String>							set;
		transient Object						hashLock;
		transient LinkedHashMap<String, Object>	hashMap;


		VirtualTableIndex(final Index index)
		{
			this._originalIndex = index;

			this.name = index.getName();
			this.type = index.getType();
			final String[] columnNames = index.getColumns();
			final List<VirtualTableColumn> columns = new ArrayList(columnNames.length);
			for(int i = 0; i < columnNames.length; i++)
			{
				final VirtualTableColumn col = getColumn(columnNames[i]);
				if(col != null)
				{
					columns.add(col);
				}
			}

			boolean onlyAutoValues = true;
			final int cc = columns.size();
			this.columns = new VirtualTableColumn[cc];
			this.columnIndices = new int[cc];
			for(int i = 0; i < cc; i++)
			{
				this.columns[i] = columns.get(i);
				this.columnIndices[i] = getColumnIndex(this.columns[i]);
				if(!this.columns[i].isAutoIncrement())
				{
					onlyAutoValues = false;
				}
			}

			boolean unique = false;
			if(index.isUnique())
			{
				this.set = new HashSet();
				if(!onlyAutoValues)
				{
					unique = true;
				}
			}
			this.isUnique = unique;
		}


		void removeCol(final VirtualTableColumn column)
		{
			final int index = ArrayUtils.indexOf(this.columns,column);
			if(index >= 0)
			{
				this.columns = ArrayUtils.remove(VirtualTableColumn.class,this.columns,index);
				this.columnIndices = ArrayUtils.remove(this.columnIndices,index);
			}
			else
			// update column indices
			{
				for(int i = 0; i < this.columns.length; i++)
				{
					this.columnIndices[i] = getColumnIndex(this.columns[i]);
				}
			}
		}


		boolean hasCol(final int index)
		{
			final int[] columnIndices = this.columnIndices;
			for(int i = 0; i < columnIndices.length; i++)
			{
				if(columnIndices[i] == index)
				{
					return true;
				}
			}

			return false;
		}


		String computeHash(final VirtualTableRow row)
		{
			synchronized(getHashLock())
			{
				if(this.hashMap == null)
				{
					this.hashMap = new LinkedHashMap<>();
				}
				else
				{
					this.hashMap.clear();
				}

				for(int i = 0, c = this.columns.length; i < c; i++)
				{
					this.hashMap.put(this.columns[i].getName(),row.data[this.columnIndices[i]]);
				}

				final String hash = HashComputer.computeHash(this.hashMap,
						getAllowMultipleNullsInUniqueIndices());

				if(this.type == IndexType.PRIMARY_KEY)
				{
					row.primaryKeyHash = hash;
				}

				return hash;
			}
		}


		String computeHash(final VirtualTableRow row, final int replacementIndex,
				final Object replacement)
		{
			synchronized(getHashLock())
			{
				if(this.hashMap == null)
				{
					this.hashMap = new LinkedHashMap<>();
				}
				else
				{
					this.hashMap.clear();
				}

				for(int i = 0, c = this.columns.length; i < c; i++)
				{
					final int columnIndex = this.columnIndices[i];
					final Object value = columnIndex == replacementIndex ? replacement
							: row.data[columnIndex];
					this.hashMap.put(this.columns[i].getName(),value);
				}

				return HashComputer.computeHash(this.hashMap,
						getAllowMultipleNullsInUniqueIndices());
			}
		}


		private Object getHashLock()
		{
			if(this.hashLock == null)
			{
				this.hashLock = new Object();
			}
			return this.hashLock;
		}


		/**
		 * @return <code>true</code> if row is new, <code>false</code> otherwise
		 */
		boolean put(final VirtualTableRow row)
		{
			if(this.set != null)
			{
				final String hash = computeHash(row);
				if(hash == HashComputer.EMPTY_HASH)
				{
					return true;
				}

				return this.set.add(hash);
			}

			return true;
		}


		/**
		 *
		 * @param hash
		 * @return <code>true</code> if hash is new, <code>false</code>
		 *         otherwise
		 */

		boolean put(final String hash)
		{
			if(this.set != null)
			{
				if(hash == HashComputer.EMPTY_HASH)
				{
					return true;
				}

				return this.set.add(hash);
			}

			return true;
		}


		void remove(final VirtualTableRow row)
		{
			if(this.set != null)
			{
				this.set.remove(computeHash(row));
			}
		}


		synchronized void clear()
		{
			if(this.set != null)
			{
				this.set.clear();
			}
		}
	}


	/**
	 * @return if multiple <code>null</code> values are allowed in unique
	 *         indices
	 * @since 4.1
	 */
	public boolean getAllowMultipleNullsInUniqueIndices()
	{
		return this.allowMultipleNullsInUniqueIndices;
	}


	/**
	 * Sets if multiple <code>null</code> values are allowed in unique indices.
	 * <br>
	 * The default is <code>false</code>.
	 *
	 * @param allowMultipleNullsInUniqueIndices
	 *            <code>true</code> if multipe nulls are allowed in unique
	 *            indices
	 * @since 4.1
	 */
	public void setAllowMultipleNullsInUniqueIndices(
			final boolean allowMultipleNullsInUniqueIndices)
	{
		this.allowMultipleNullsInUniqueIndices = allowMultipleNullsInUniqueIndices;
	}


	/**
	 * Returns if double values in unique indices are checked.
	 *
	 * @return <code>true</code> if double values in unique indices are checked,
	 *         <code>false</code> otherwise.
	 */
	public boolean getCheckUniqueIndexDoubleValues()
	{
		return this.checkUniqueIndexDoubleValues;
	}


	/**
	 * Sets if double values in unique indices should be checked, meaning an
	 * exception is thrown on an attempt to insert a duplicate value.
	 *
	 * @param checkUniqueIndexDoubleValues
	 */
	public void setCheckUniqueIndexDoubleValues(final boolean checkUniqueIndexDoubleValues)
	{
		this.checkUniqueIndexDoubleValues = checkUniqueIndexDoubleValues;
	}


	private void reportUniqueIndexDoubleValues(final VirtualTableRow row,
			final VirtualTableIndex index) throws UniqueIndexDuplicateValuesException
	{
		if(this.checkUniqueIndexDoubleValues)
		{
			final StringBuilder sb = new StringBuilder();
			sb.append(getClass().getName());
			sb.append(": Duplicate values in unique index ");
			sb.append(index.name);
			for(final int columnIndex : index.columnIndices)
			{
				sb.append(", ");
				sb.append(this.columns[columnIndex].getName());
				sb.append(" = ");
				sb.append(row.getFormattedValue(columnIndex));
			}

			throw new UniqueIndexDuplicateValuesException(this,sb.toString());
		}
	}


	/**
	 * Returns if missing columns in fill actions are allowed.
	 *
	 * @return <code>true</code> if missing columns in a fill action are
	 *         allowed, <code>false</code> otherwise.
	 * @since 3.1
	 */
	public boolean getAllowMissingFillColumns()
	{
		return this.allowMissingFillColumns;
	}


	/**
	 * Sets if missing columns in fill actions are allowed, if not an exception
	 * is thrown.
	 *
	 * @param allowMissingFillColumns
	 *            <code>true</code> if missing fill columns in fill action
	 *            should be allowed, <code>false</code> otherwise
	 * @since 3.1
	 */
	public void setAllowMissingFillColumns(final boolean allowMissingFillColumns)
	{
		this.allowMissingFillColumns = allowMissingFillColumns;
	}


	/**
	 * Returns if superfluous columns in fill actions are allowed.
	 *
	 * @return <code>true</code> if superfluous columns in a fill action are
	 *         allowed, <code>false</code> otherwise.
	 * @since 3.1
	 */
	public boolean getAllowSuperfluousFillColumns()
	{
		return this.allowSuperfluousFillColumns;
	}


	/**
	 * Sets if superfluous columns in fill actions are allowed, if not an
	 * exception is thrown.
	 *
	 * @param allowSuperfluousFillColumns
	 *            <code>true</code> if superfluous fill columns in fill action
	 *            should be allowed, <code>false</code> otherwise
	 * @since 3.1
	 */
	public void setAllowSuperfluousFillColumns(final boolean allowSuperfluousFillColumns)
	{
		this.allowSuperfluousFillColumns = allowSuperfluousFillColumns;
	}


	private int[] getColumnIndices(final Result result,
			final boolean checkMissingOrSuperfluousColumns) throws VirtualTableException
	{
		final int cc = result.getColumnCount();
		final int[] colIndices = new int[cc];
		for(int col = 0; col < cc; col++)
		{
			String colName = result.getMetadata(col).getCaption();
			colIndices[col] = getColumnIndex(colName);
			if(colIndices[col] == -1)
			{
				colName = result.getMetadata(col).getName();
				if(colName != null && colName.length() > 0)
				{
					colIndices[col] = getColumnIndex(colName);
				}
			}
		}

		if(checkMissingOrSuperfluousColumns)
		{
			if(!this.allowMissingFillColumns)
			{
				final List<String> missingColumns = getColumnNames();

				for(int col = 0; col < cc; col++)
				{
					if(colIndices[col] != -1)
					{
						missingColumns.remove(this.columns[colIndices[col]].getName());
					}
				}

				if(missingColumns.size() > 0)
				{
					throw new VirtualTableException(this,"Missing columns in result: "
							+ StringUtils.concat(", ",missingColumns));
				}
			}

			if(!this.allowSuperfluousFillColumns)
			{
				final List<String> superfluousColumns = new ArrayList();

				for(int col = 0; col < cc; col++)
				{
					if(colIndices[col] == -1)
					{
						String colName = result.getMetadata(col).getCaption();
						if(colName == null || colName.length() == 0)
						{
							colName = result.getMetadata(col).getName();
						}
						superfluousColumns.add(colName);
					}
				}

				if(superfluousColumns.size() > 0)
				{
					throw new VirtualTableException(this,"Superfluous columns in result: "
							+ StringUtils.concat(", ",superfluousColumns));
				}
			}
		}

		return colIndices;
	}


	/**
	 * Returns a {@link Table} for this {@link VirtualTable}.
	 *
	 * @return a {@link Table}
	 * @deprecated the VirtualTable is as SQL Table object itself since 3.2
	 */
	@Deprecated
	public Table toSqlTable()
	{
		return this;
	}


	/**
	 * Shortcut to create a new select with optional columns.
	 *
	 * <pre>
	 * new SELECT().FROM(VT).columns(...)
	 * ->
	 * VT.SELECT(...)
	 * </pre>
	 *
	 * @param columns
	 * @return a new {@link SELECT} object
	 * @since 3.2
	 */
	public SELECT SELECT(final Object... columns)
	{
		return new SELECT().FROM(this).columns(columns);
	}


	/**
	 * Stores the {@link QueryInfo} of the last executed query.
	 *
	 * @param lastQuery
	 *            a {@link QueryInfo}
	 */
	public void setLastQuery(final QueryInfo lastQuery)
	{
		this.lastQuery = lastQuery;
	}


	/**
	 * Returns the last executed query.
	 *
	 * @return a {@link QueryInfo}
	 */
	public QueryInfo getLastQuery()
	{
		if(this.lastQuery != null)
		{
			return this.lastQuery.clone();
		}

		return null;
	}


	/**
	 * Creates the default {@link SELECT} for this {@link VirtualTable}.
	 *
	 * <pre>
	 * SELECT [all columns...] FROM[tableName]
	 * </pre>
	 *
	 * @return the created {@link SELECT}
	 */
	public SELECT getDefaultSelect()
	{
		return getDefaultSelect(this.columns);
	}


	public SELECT getDefaultSelect(final VirtualTableColumn[] columns)
	{
		final SELECT select = new SELECT().FROM(this);

		for(final VirtualTableColumn col : columns)
		{
			if(col.isPersistent())
			{
				select.columns(col);
			}
		}

		return select;
	}


	/**
	 * Creates the extended {@link SELECT} for this {@link VirtualTable} with
	 * the joined, non persistent columns.
	 * <p>
	 * Alias for <code>getExtendedSelect(JoinType.LEFT_JOIN)</code>.
	 * </p>
	 *
	 * @return the created {@link SELECT}
	 *
	 * @throws VirtualTableException
	 *
	 * @see #getExtendedSelect(JoinType)
	 */
	public SELECT getExtendedSelect() throws VirtualTableException
	{
		return getExtendedSelect(JoinType.LEFT_JOIN);
	}


	public SELECT getExtendedSelect(final VirtualTableColumn[] columns) throws VirtualTableException
	{
		return getExtendedSelect(JoinType.LEFT_JOIN,columns);
	}


	/**
	 * Creates the extended {@link SELECT} for this {@link VirtualTable} with
	 * the joined, non persistent columns.
	 *
	 * @param joinType
	 *            the way linked tables should be joined
	 *
	 * @return the created {@link SELECT}
	 */
	public SELECT getExtendedSelect(final JoinType joinType) throws VirtualTableException
	{
		return getExtendedSelect(joinType,this.columns);
	}


	public SELECT getExtendedSelect(final JoinType joinType, final VirtualTableColumn[] columns)
			throws VirtualTableException
	{
		final SELECT select = new SELECT().FROM(this);
		SqlGenerationContext context = null;

		for(final VirtualTableColumn column : columns)
		{
			final TableColumnLink link = column.getTableColumnLink();
			if(link != null)
			{
				if(context == null)
				{
					context = new SqlGenerationContext();
				}
				link.addColumn(select,this,column,joinType,context);
			}
			else if(column.isPersistent())
			{
				select.columns(column);
			}
		}

		return select;
	}


	/**
	 * Sets a user-defined {@link SELECT} statement which should be used to fill
	 * this Virtual Table.
	 *
	 * @param select
	 *            the user defined select which should be used
	 *
	 * @see #getSelect()
	 *
	 * @since 4.0
	 */
	public void setSelect(final SELECT select)
	{
		this.userDefinedSelect = select;
	}


	/**
	 * Returns the default {@link SELECT} statement which is used to query the
	 * underlying datasource to fill this VT.
	 * <p>
	 * If a user-defined statement is set via {@link #setSelect(SELECT)}, this
	 * one is used.
	 * </p>
	 * <p>
	 * Otherwise, depending if this {@link VirtualTable} has non persistent
	 * columns, {@link #getExtendedSelect()} respectively
	 * {@link #getDefaultSelect()} is returned.
	 * </p>
	 *
	 * @return The best fitting select to query data for this VirtualTable
	 * @see #hasNonPersistentColumns()
	 * @see #setSelect(SELECT)
	 */
	public SELECT getSelect()
	{
		return getSelect(this.columns);
	}


	public SELECT getSelect(final VirtualTableColumn[] columns)
	{
		if(this.userDefinedSelect != null)
		{
			return this.userDefinedSelect;
		}

		return hasNonPersistentColumns() ? getExtendedSelect(columns) : getDefaultSelect(columns);
	}


	/**
	 * Reloads the contents of this {@link VirtualTable} applying the search
	 * conditions of the last executed query.
	 *
	 * @throws VirtualTableException
	 * @throws DBException
	 *             thrown if database access fails
	 */
	public void reload() throws VirtualTableException, DBException
	{
		if(this.lastQuery != null)
		{
			queryAndFill(getDataSource(),this.lastQueryIndices,this.lastQuery.getSelect(),
					this.lastQuery.getParameters());
		}
	}


	/**
	 * Executes the default select for this {@link VirtualTable}.
	 * <p>
	 * This is an alias for
	 * {@link #queryAndFill(DBDataSource, SELECT, Object...)}.
	 * </p>
	 *
	 * @throws VirtualTableException
	 * @throws DBException
	 *             thrown if database access failed.
	 */
	public void queryAndFill() throws VirtualTableException, DBException
	{
		queryAndFill(getDataSource(),getSelect());
	}


	/**
	 * Executes the default select for this {@link VirtualTable}.
	 * <p>
	 * This is an alias for
	 * {@link #queryAndFill(DBDataSource, SELECT, Object...)}.
	 * </p>
	 *
	 * @param dataSource
	 *            the {@link DBDataSource} used for querying the database
	 * @throws VirtualTableException
	 * @throws DBException
	 *             thrown if database access failed.
	 */
	public void queryAndFill(final DBDataSource dataSource)
			throws VirtualTableException, DBException
	{
		queryAndFill(dataSource,getSelect());
	}


	/**
	 * Executes the default select for this {@link VirtualTable}.
	 * <p>
	 * This is an alias for
	 * {@link #queryAndFill(DBDataSource, SELECT, Object...)}.
	 * </p>
	 *
	 * @param select
	 *            the SELECT to be executed for querying the database
	 * @param params
	 *            dynamic parameters, that can be used optionally
	 * @throws VirtualTableException
	 * @throws DBException
	 *             thrown if database access failed.
	 */
	public void queryAndFill(final SELECT select, final Object... params)
			throws VirtualTableException, DBException
	{
		queryAndFill(getDataSource(),select,params);
	}


	/**
	 * Executes the default select for this {@link VirtualTable}.
	 *
	 * @param dataSource
	 *            the {@link DBDataSource} used for querying the database
	 * @param select
	 *            the SELECT to be executed for querying the database
	 * @param params
	 *            dynamic parameters, that can be used optionally
	 * @throws VirtualTableException
	 * @throws DBException
	 *             thrown if database access failed.
	 */
	public void queryAndFill(final DBDataSource dataSource, final SELECT select,
			final Object... params) throws VirtualTableException, DBException
	{
		queryAndFill(dataSource,null,select,params);
	}


	private void queryAndFill(final DBDataSource dataSource, final int[] columnIndices,
			final SELECT select, final Object... params) throws VirtualTableException, DBException
	{
		final DBConnection connection = dataSource.openConnection();
		try
		{
			final Result result = connection.query(select,params);
			try
			{
				addData(result,columnIndices,VirtualTableFillMethod.OVERWRITE);
			}
			finally
			{
				result.close();
			}
		}
		finally
		{
			connection.close();
		}
	}


	/**
	 * Executes a query and appends its results at the end of this
	 * {@link VirtualTable}.
	 * <p>
	 * This is an alias for {@link #queryAndAppend(DBDataSource, KeyValues)}.
	 * </p>
	 *
	 * @param pkValue
	 *            the {@link KeyValues} for specifying the filter condition
	 *
	 * @return <code>true</code> if values were appended, <code>false</code>
	 *         otherwise.
	 *
	 * @throws VirtualTableException
	 *
	 * @throws DBException
	 *             thrown if database access failed.
	 */
	public boolean queryAndAppend(final KeyValues pkValue) throws VirtualTableException, DBException
	{
		return queryAndAppend(getDataSource(),pkValue);
	}


	/**
	 * Executes a query and appends its results at the end of this
	 * {@link VirtualTable}.
	 *
	 * @param dataSource
	 *            the {@link DBDataSource} used for querying the database
	 *
	 * @param pkValue
	 *            the {@link KeyValues} for specifying the filter condition
	 *
	 * @return <code>true</code> if values were appended, <code>false</code>
	 *         otherwise.
	 *
	 * @throws VirtualTableException
	 *
	 * @throws DBException
	 *             thrown if database access failed.
	 */
	public boolean queryAndAppend(final DBDataSource dataSource, final KeyValues pkValue)
			throws VirtualTableException, DBException
	{
		SELECT select;
		List values;
		if(this.lastQuery != null)
		{
			select = this.lastQuery.getSelect().clone();
			values = new XdevList(this.lastQuery.getParameters());
		}
		else
		{
			select = getSelect();
			values = new XdevList();
		}
		WHERE where = select.getWhere();
		if(where == null)
		{
			where = new WHERE();
			select.WHERE(where);
		}
		pkValue.appendCondition(where,values,this);

		boolean appended = false;

		final DBConnection connection = dataSource.openConnection();
		try
		{
			final Result result = connection.query(select,values.toArray());
			try
			{
				if(result.next())
				{
					final VirtualTableRow row = new VirtualTableRow(this.columns.length);
					final int[] colIndices = getColumnIndices(result,false);
					for(int i = 0; i < colIndices.length; i++)
					{
						if(colIndices[i] != -1)
						{
							final Object val = result.getObject(i);
							row.set(colIndices[i],checkValue(colIndices[i],val),false,false);
						}
					}
					this.data.add(row,false);

					appended = true;
				}
			}
			finally
			{
				result.close();
			}
		}
		finally
		{
			connection.close();
		}

		if(appended)
		{
			fireDataChanged();
		}

		return appended;
	}


	/**
	 * Compares two values with each other.
	 *
	 * @param value1
	 *            first value to be compared
	 * @param value2
	 *            second value to be compared
	 * @return <code>true</code>, if both values are equal
	 */
	public static boolean equals(final Object value1, final Object value2)
	{
		return equals(value1,value2,null);
	}


	/**
	 * Compares two values with each other.
	 *
	 * @param value1
	 *            first value to be compared
	 * @param value2
	 *            second value to be compared
	 * @param type
	 *            which should be considered for comparison, can be
	 *            <code>null</code>
	 * @return <code>true</code>, if both values are equal
	 * @since 4.1.2
	 */
	public static boolean equals(final Object value1, final Object value2, final DataType type)
	{
		if(value1 == value2)
		{
			return true;
		}

		if(value1 == null || value2 == null)
		{
			return false;
		}

		if(value1.equals(value2))
		{
			return true;
		}

		if(value1 instanceof ByteHolder || value1 instanceof CharHolder
				|| value2 instanceof ByteHolder || value2 instanceof CharHolder)
		{
			// don't perform further checks on byte or char holders
			return false;
		}

		if(value1 instanceof Number && value2 instanceof Number)
		{
			return ((Number)value1).doubleValue() == ((Number)value2).doubleValue();
		}
		else if(value1.toString().equals(value2.toString()))
		{
			return true;
		}

		if(type != null)
		{
			if(type.isNumeric() || type == DataType.BOOLEAN)
			{
				try
				{
					final Object o1 = ConversionUtils.convert(value1,type.getJavaClass());
					final Object o2 = ConversionUtils.convert(value2,type.getJavaClass());
					if(o1.equals(o2))
					{
						return true;
					}
				}
				catch(final ObjectConversionException e)
				{
				}
			}
		}

		return false;
	}
}
