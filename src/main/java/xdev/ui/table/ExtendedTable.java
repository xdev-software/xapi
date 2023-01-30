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
import java.awt.Point;
import java.io.IOException;
import java.io.Writer;

import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JViewport;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;

import xdev.db.sql.SELECT;
import xdev.ui.MasterDetailComponent;
import xdev.ui.XdevTable;
import xdev.vt.VirtualTable;
import xdev.vt.VirtualTable.VirtualTableRow;


/**
 * A {@link ExtendedTable} interface provides a useful extension to the
 * subclasses of {@link JTable}.
 *
 *
 * @author XDEV Software
 *
 * @since 3.0
 */
public interface ExtendedTable<C extends JComponent> extends MasterDetailComponent<C>
{
	/**
	 * Property constant for "editable".
	 */
	public final static String EDITABLE_PROPERTY = "editable";
	
	
	/**
	 * Determines if the {@link ExtendedTable} is editable. Returns
	 * <code>true</code> if the {@link ExtendedTable} is editable or
	 * <code>false</code> if not.
	 *
	 * @return <code>true</code> if the {@link ExtendedTable} is editable or
	 *         <code>false</code> if not.
	 */
	public boolean isEditable();
	
	
	/**
	 * Sets the specified boolean to indicate whether or not this
	 * {@link ExtendedTable} should be editable. A PropertyChange event
	 * ("EDITABLE_PROPERTY") is fired when the state is changed.
	 *
	 * @param editable
	 *            the boolean to be set
	 */
	public void setEditable(boolean editable);
	
	
	/**
	 * Creates a new {@code TableModel} based on a {@link VirtualTable} and
	 * other specified parameters and sets it for this {@code Table}.
	 * <p>
	 * The {@code TableModel} contains all columns and all rows of the specified
	 * {@code vt}
	 * </p>
	 *
	 * @param vt
	 *            the {@link VirtualTable} containing the data for the
	 *            {@code TableModel}
	 */
	public void setModel(VirtualTable vt);
	
	
	/**
	 * Creates a new {@code TableModel} based on a {@link VirtualTable} and
	 * other specified parameters and sets it for this {@code Table}.
	 *
	 * @param vt
	 *            the {@link VirtualTable} containing the data for the
	 *            {@code TableModel}
	 * @param columns
	 *            a comma separated list of valid column names for the param
	 *            {@code vt}, may be "*" for all columns
	 * @param queryData
	 *            if {@code true}, the best fitting select for this {@code vt}
	 *            is used
	 */
	public void setModel(VirtualTable vt, String columns, boolean queryData);
	
	
	/**
	 * Creates a new {@code TableModel} based on a {@link VirtualTable} and
	 * other specified parameters and sets it for this {@code Table}.
	 *
	 * @param vt
	 *            the {@link VirtualTable} containing the data for the
	 *            {@code TableModel}
	 * @param columns
	 *            a comma separated list of valid column names for the param
	 *            {@code vt}, may be "*" for all columns
	 * @param queryData
	 *            if {@code true}, the best fitting select for this {@code vt}
	 *            is used
	 * @param selectiveQuery
	 *            if {@code true}, only the display <code>columns</code> are
	 *            queried
	 * @since 5.0
	 */
	public void setModel(final VirtualTable vt, final String columns, final boolean queryData,
			final boolean selectiveQuery);
	
	
	/**
	 * Creates a new {@code TableModel} based on a {@link VirtualTable} and
	 * other specified parameters and sets it for this {@code Table}.
	 *
	 * @param vt
	 *            the {@link VirtualTable} containing the data for the
	 *            {@code TableModel}
	 * @param columns
	 *            a comma separated list of valid column names for the param
	 *            {@code vt}, may be "*" for all columns
	 * @param select
	 *            a custom {@link SELECT} for filtering or ordering the results
	 *            of the {@code vt}, may be null for the default select
	 * @param params
	 *            a set of parameters related to the param {@code select}, may
	 *            be null
	 */
	public void setModel(VirtualTable vt, String columns, SELECT select, Object... params);
	
	
	/**
	 * Sets the {@code visibleRows} property, which has different meanings
	 * depending on the layout orientation: For a {@code VERTICAL} layout
	 * orientation, this sets the preferred number of rows to display without
	 * requiring scrolling; for other orientations, it affects the wrapping of
	 * cells.
	 *
	 * @param visibleRows
	 *            an integer specifying the preferred number of rows to display
	 *            without requiring scrolling
	 *
	 *
	 * @see #getVisibleRowCount
	 * @see JComponent#getVisibleRect
	 * @see JViewport
	 *
	 */
	public void setVisibleRowCount(int visibleRows);
	
	
	/**
	 * Returns the value of the {@code visibleRows} property. See the
	 * documentation for {@link #setVisibleRowCount} for details on how to
	 * interpret this value.
	 *
	 * @return the value of the {@code visibleRows} property.
	 *
	 * @see #setVisibleRowCount
	 */
	public int getVisibleRowCount();
	
	
	/**
	 * Sets the background {@link Color} of even rows.
	 *
	 * @param evenBackground
	 *            The background of even rows as type {@link Color}.
	 */
	public void setEvenBackground(Color evenBackground);
	
	
	/**
	 * Returns the background {@link Color} of even rows.
	 *
	 * @return The background of even rows as type {@link Color}.
	 */
	public Color getEvenBackground();
	
	
	/**
	 * Sets the background {@link Color} of odd rows.
	 *
	 * @param oddBackground
	 *            The background of odd rows as type {@link Color}.
	 */
	public void setOddBackground(Color oddBackground);
	
	
	/**
	 * Returns the background {@link Color} of odd rows.
	 *
	 * @return The background of odd rows as type {@link Color}.
	 */
	public Color getOddBackground();
	
	
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
	public void exportCSV(Writer writer) throws IOException;
	
	
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
	public void exportCSV(Writer writer, char delimiter) throws IOException;
	
	
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
	public void exportCSV(Writer writer, char delimiter, boolean withColumnNames)
			throws IOException;
	
	
	/**
	 * Clears any existing data before creating the new
	 * {@link DefaultTableModel}.
	 */
	public void clear();
	
	
	/**
	 * Clears all previous set renderers and registers <code>renderer</code> as
	 * default renderer for <code>Object.class</code>, which makes it the
	 * default renderer as long as no other renderer is set for a specific
	 * column class.
	 *
	 * @param renderer
	 *            the new default renderer
	 * @see #setDefaultEditor(TableCellEditor)
	 * @see TableColumn#setCellRenderer(TableCellRenderer)
	 */
	public void setDefaultRenderer(TableCellRenderer renderer);
	
	
	/**
	 * Clears all previous set editors and registers <code>editor</code> as
	 * default editor for <code>Object.class</code>, which makes it the default
	 * editor as long as no other editor is set for a specific column class.
	 *
	 * @param editor
	 *            the new default editor
	 * @see #setDefaultRenderer(TableCellRenderer)
	 * @see TableColumn#setCellEditor(TableCellEditor)
	 */
	public void setDefaultEditor(TableCellEditor editor);
	
	
	/**
	 * Return <code>true</code> if something is selected, otherwise
	 * <code>false</code>.
	 *
	 * @return <code>true</code> if something is selected, otherwise
	 *         <code>false</code>
	 */
	public boolean isSomethingSelected();
	
	
	/**
	 * Returns the index of the {@link TableModel} for the first selected row or
	 * -1 if no row is selected.
	 *
	 * @return the index of the corresponding row in the {@link TableModel}
	 *
	 *
	 * @throws IndexOutOfBoundsException
	 *             if sorting is enabled and passed an index outside the range
	 *             of the {@link JTable} as determined by the method
	 *             {@link JTable#getRowCount()}
	 *
	 * @see JTable#convertRowIndexToModel(int)
	 * @see JTable#getSelectedRow()
	 */
	public int getSelectedModelRow() throws IndexOutOfBoundsException;
	
	
	/**
	 * Returns the indices of all selected rows in the {@link TableModel}.
	 *
	 * @return a list of integers containing the indices of all selected rows,
	 *         or an empty list if no row is selected
	 *
	 * @throws IndexOutOfBoundsException
	 *             if sorting is enabled and passed an index outside the range
	 *             of the {@link JTable} as determined by the method
	 *             {@link JTable#getRowCount()}
	 *
	 * @see JTable#convertRowIndexToModel(int)
	 * @see JTable#getSelectedRows()
	 */
	public int[] getSelectedModelRows() throws IndexOutOfBoundsException;
	
	
	/**
	 * Returns the selected {@link VirtualTableRow} of this component, or
	 * <code>null</code> if nothing is selected.
	 *
	 * @return the selected {@link VirtualTableRow} of this component
	 */
	@Override
	public VirtualTableRow getSelectedVirtualTableRow();
	
	
	/**
	 * Selects the row in the table if present.
	 *
	 * @param row
	 *            the row to select
	 * @since 3.2
	 */
	public void setSelectedVirtualTableRow(VirtualTableRow row);
	
	
	/**
	 * Returns the selected {@link VirtualTableRow}s of this component, or an
	 * empty array if nothing is selected.
	 *
	 * @return the selected {@link VirtualTableRow}s of this component
	 */
	public VirtualTableRow[] getSelectedVirtualTableRows();
	
	
	/**
	 * Selects the rows in the table if present.
	 *
	 * @param rows
	 *            the rows to select
	 * @since 3.2
	 */
	public void setSelectedVirtualTableRows(VirtualTableRow[] rows);
	
	
	/**
	 * Selects the row <code>row</code> in the view. If the <code>row</code> <
	 * 0, all columns and rows are deselected.
	 *
	 * @param row
	 *            the index of the row in terms of the model
	 *
	 * @throws IndexOutOfBoundsException
	 *             if sorting is enabled and passed an index outside the range
	 *             of the {@link JTable} as determined by the method
	 *             {@link JTable#getRowCount()}
	 *
	 * @throws IllegalArgumentException
	 *             if <code>row</code> lie outside [0,
	 *             {@link JTable#getRowCount()}-1]
	 *
	 * @see JTable#convertRowIndexToView(int)
	 * @see JTable#addRowSelectionInterval(int, int)
	 * @see JTable#clearSelection()
	 */
	public void setSelectedModelRow(int row)
			throws IndexOutOfBoundsException, IllegalArgumentException;
	
	
	/**
	 * Selects all rows with the indices specified in the list. If
	 * <code>rows</code> are <code>null</code> all columns and rows are
	 * deselected.
	 *
	 * @param rows
	 *            an list of integers containing the row indices
	 *
	 * @throws IndexOutOfBoundsException
	 *             if sorting is enabled and passed an index outside the range
	 *             of the {@link JTable} as determined by the method
	 *             {@link JTable#getRowCount()}
	 *
	 * @throws IllegalArgumentException
	 *             if <code>row</code> lie outside [0,
	 *             {@link JTable#getRowCount()}-1]
	 *
	 * @see JTable#convertRowIndexToView(int)
	 * @see JTable#addRowSelectionInterval(int, int)
	 * @see JTable#clearSelection()
	 * @see #setSelectedModelRows(int[])
	 */
	public void setSelectedModelRows(int[] rows)
			throws IndexOutOfBoundsException, IllegalArgumentException;
	
	
	/**
	 * Changes the selection to be between <code>start</code> and
	 * <code>end</code> inclusive. <code>start</code> doesn't have to be less
	 * than or equal to <code>end</code>.
	 * <p>
	 * In {@code SINGLE_SELECTION} selection mode, only the second index is
	 * used.
	 * <p>
	 * If this represents a change to the current selection, then each
	 * {@code ListSelectionListener} is notified of the change.
	 *
	 * @param start
	 *            one end of the interval.
	 * @param end
	 *            other end of the interval
	 *
	 */
	public void setSelectedModelRows(int start, int end);
	
	
	/**
	 * Changes the selection to be the set of indices specified by the given
	 * array. Indices greater than or equal to the model size are ignored. This
	 * is a convenience method that clears the selection and then uses
	 * {@code addSelectionInterval} on the selection model to add the indices.
	 * Refer to the documentation of the selection model class being used for
	 * details on how values less than {@code 0} are handled.
	 *
	 * @param indices
	 *            an array of the indices of the rows to select,
	 *            {@code non-null}
	 *
	 */
	public void setSelectedRows(int[] indices);
	
	
	/**
	 *
	 * Returns the view-index of the row that x- and y-coordinate lies in.
	 *
	 * @param x
	 *            the <i>x</i> coordinate of the row
	 * @param y
	 *            the <i>y</i> coordinate of the row
	 *
	 * @return the index of the corresponding row
	 *
	 * @throws IndexOutOfBoundsException
	 *             if sorting is enabled and passed an index outside the range
	 *             of the {@link JTable} as determined by the method
	 *             {@link JTable#getRowCount()}
	 *
	 * @see #getRowAtPoint(Point)
	 */
	public int getRowAtPoint(int x, int y) throws IndexOutOfBoundsException;
	
	
	/**
	 *
	 * Returns the view-index of the row that {@link Point} lies in, or -1 if
	 * the result is not in the range [0, {@link JTable#getRowCount()}-1].
	 *
	 * @param location
	 *            the {@link Point} of the row
	 *
	 * @return the index of the corresponding row
	 *
	 * @throws IndexOutOfBoundsException
	 *             if sorting is enabled and passed an index outside the range
	 *             of the {@link JTable} as determined by the method
	 *             {@link JTable#getRowCount()}
	 *
	 * @see #getRowAtPoint(int, int)
	 */
	public int getRowAtPoint(Point location) throws IndexOutOfBoundsException;
	
	
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
	public int getColumnAtPoint(int x, int y);
	
	
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
	public int getColumnAtPoint(Point location);
	
	
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
	public void setColumnWidth(int index, int width);
	
	
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
	public void setColumnTitle(int index, String title);
	
	
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
	public void ensureCellIsVisible(int rowIndex, int columnIndex);
	
	
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
	public void ensureRowIsVisible(int rowIndex);
	
	
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
	public void ensureColumnIsVisible(int columnIndex);
	
	// /**
	// * Adding rowNumberTable to display TableRowHeader.
	// *
	// * @param rowNumberTable
	// */
	// public void addRowNumberTable(RowNumberTable rowNumberTable);
	//
	//
	// /**
	// * Get {@link TableSupport} instance.
	// *
	// * @return TableSupport
	// */
	// public TableSupport getTableSupport();
	//
	//
	// /**
	// * Get {@link RowNumberTable} instance.
	// *
	// * @return RowNumberTable
	// */
	// public RowNumberTable getRowNumberTable();
	//
}
