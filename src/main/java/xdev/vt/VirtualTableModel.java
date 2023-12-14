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
package xdev.vt;


import java.util.Vector;

import javax.swing.SwingUtilities;
import javax.swing.table.AbstractTableModel;

import xdev.db.DataType;
import xdev.ui.XdevTable;
import xdev.util.ArrayUtils;
import xdev.util.logging.LoggerFactory;
import xdev.util.logging.XdevLogger;
import xdev.vt.VirtualTable.VirtualTableRow;


/**
 * This class acts as a model implementation for {@link XdevTable}s using a
 * {@link VirtualTable} as the underlying data model.
 * <p>
 * Calls for retrieving or modifying data are delegated to the
 * {@link VirtualTable}.
 * </p>
 * <p>
 * The number and order of columns of this Model and the {@link VirtualTable}
 * may differ. <code>modelColumnIndices</code> is used to map the columns of
 * this Model to the columns of the {@link VirtualTable}.
 * 
 * @author XDEV Software Corp.
 */
public class VirtualTableModel extends AbstractTableModel implements VirtualTableListener,
		VirtualTableWrapper
{
	/**
	 * Logger instance for this class.
	 */
	private static final XdevLogger	log	= LoggerFactory.getLogger(VirtualTableModel.class);
	
	private VirtualTable			vt;
	private int[]					columnIndices;
	
	
	/**
	 * Initializes a new {@link VirtualTableModel}.
	 * 
	 * @param vt
	 *            the {@link VirtualTable} to use
	 * @param columnIndices
	 *            an <code>int</code> array of column indices of the model. The
	 *            values of the array contain the indexes of the
	 *            {@link VirtualTable}.
	 */
	public VirtualTableModel(VirtualTable vt, int[] columnIndices)
	{
		this.vt = vt;
		this.columnIndices = columnIndices;
		
		vt.addVirtualTableListener(this);
	}
	
	
	/**
	 * Returns the wrapped {@link VirtualTable}.
	 * 
	 * @return a {@link VirtualTable}
	 */
	public VirtualTable getVT()
	{
		return vt;
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public int[] getModelColumnIndices()
	{
		return columnIndices;
	}
	
	
	/**
	 * Returns a value from the {@link VirtualTable} at the specified position.
	 * 
	 * @param row
	 *            the row index
	 * @param col
	 *            the model column index.
	 * @return a value
	 */
	@Override
	public Object getValueAt(int row, int col)
	{
		return vt.getValueAt(row,columnIndices[col]);
	}
	
	
	/**
	 * Returns the number of columns of the model.
	 * 
	 * @return the number of columns
	 */
	@Override
	public int getColumnCount()
	{
		return columnIndices.length;
	}
	
	
	/**
	 * Returns the number of rows in the {@link VirtualTable}.
	 * 
	 * @return the number of rows.
	 */
	@Override
	public int getRowCount()
	{
		return vt.getRowCount();
	}
	
	
	/**
	 * Returns the column name using the caption of the underlying
	 * {@link VirtualTableColumn}.
	 * 
	 * @param col
	 *            the model column index.
	 * @return the column name of the column
	 */
	@Override
	public String getColumnName(int col)
	{
		return vt.getColumnCaption(columnIndices[col]);
	}
	
	
	/**
	 * Returns the class of the underlying {@link VirtualTableColumn}.
	 * 
	 * @param col
	 *            the model column index.
	 * @return the {@link DataType} of the column
	 */
	@Override
	public Class getColumnClass(int col)
	{
		return getClassForType(vt.columns[columnIndices[col]].getType());
	}
	
	
	/**
	 * Helper method for mapping {@link DataType}s to Java classes.
	 * 
	 * @param type
	 *            a {@link DataType} to get a class for.
	 * @return a {@link Class} for the <code>type</code>.
	 */
	public static Class getClassForType(DataType type)
	{
		return type.getJavaClass();
		
		// if(type.isInt())
		// {
		// return Integer.class;
		// }
		// else if(type.isDecimal())
		// {
		// return Double.class;
		// }
		// else if(type.isDate())
		// {
		// return java.util.Date.class;
		// }
		// else if(type == DataType.BOOLEAN)
		// {
		// return Boolean.class;
		// }
		// else if(type.isBlob())
		// {
		// return XdevBlob.class;
		// }
		// else if(type == DataType.CLOB)
		// {
		// return XdevClob.class;
		// }
		//
		// return String.class;
	}
	
	
	/**
	 * Sets a value in the underlying {@link VirtualTable} at the specified
	 * position.
	 * 
	 * @param newValue
	 *            the new value to be set.
	 * @param row
	 *            the index of the row.
	 * @param col
	 *            the model column index.
	 */
	@Override
	public void setValueAt(Object newValue, int row, int col)
	{
		try
		{
			Object oldValue = getValueAt(row,col);
			if(!VirtualTable.equals(oldValue,newValue))
			{
				vt.setValueAt(newValue,row,columnIndices[col]);
			}
		}
		catch(Exception e)
		{
			log.error(e);
		}
	}
	
	
	/**
	 * Returns, if a cell is editables.
	 * 
	 * @param row
	 *            the index of the row.
	 * @param col
	 *            the model column index.
	 * @return if <code>true</code>, the cell can be edited.
	 */
	@Override
	public boolean isCellEditable(int row, int col)
	{
		return vt.getColumnAt(columnIndices[col]).isEditable();
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public VirtualTable getVirtualTable()
	{
		return vt;
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public int viewToModelColumn(int col)
	{
		return columnIndices[col];
	}
	
	
	private int modelToViewColumn(int col)
	{
		return ArrayUtils.indexOf(columnIndices,col);
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public VirtualTableRow getVirtualTableRow(int modelRow)
	{
		return vt.getRow(modelRow);
	}
	
	
	/**
	 * Notifies listeners that all cell values in the {@link VirtualTable} may
	 * have changed.
	 * 
	 * @param event
	 *            a {@link VirtualTableEvent} with detailed information.
	 */
	@Override
	public void virtualTableDataChanged(VirtualTableEvent event)
	{
		enqueue(new Runnable()
		{
			@Override
			public void run()
			{
				fireTableDataChanged();
			}
		});
		// UIUtils.enqueue(false,new Runnable()
		// {
		// @Override
		// public void run()
		// {
		// fireTableDataChanged();
		// }
		// });
	}
	
	
	/**
	 * Notifies all listeners that a row in the {@link VirtualTable} has been
	 * deleted.
	 * 
	 * @param event
	 *            a {@link VirtualTableEvent} with detailed information.
	 */
	@Override
	public void virtualTableRowDeleted(VirtualTableEvent event)
	{
		final int row = event.getRowIndex();
		
		enqueue(new Runnable()
		{
			@Override
			public void run()
			{
				fireTableRowsDeleted(row,row);
			}
		});
		// UIUtils.enqueue(false,new Runnable()
		// {
		// @Override
		// public void run()
		// {
		// fireTableRowsDeleted(row,row);
		// }
		// });
	}
	
	
	/**
	 * Notifies all listeners that a row in the {@link VirtualTable} has been
	 * inserted.
	 * 
	 * @param event
	 *            a {@link VirtualTableEvent} with detailed information.
	 */
	@Override
	public void virtualTableRowInserted(VirtualTableEvent event)
	{
		final int row = event.getRowIndex();
		
		enqueue(new Runnable()
		{
			@Override
			public void run()
			{
				fireTableRowsInserted(row,row);
			}
		});
		// UIUtils.enqueue(false,new Runnable()
		// {
		// @Override
		// public void run()
		// {
		// fireTableRowsInserted(row,row);
		// }
		// });
	}
	
	
	/**
	 * Notifies all listeners that a row in the {@link VirtualTable} has been
	 * updated.
	 * 
	 * @param event
	 *            a {@link VirtualTableEvent} with detailed information.
	 */
	@Override
	public void virtualTableRowUpdated(VirtualTableEvent event)
	{
		final int row = event.getRowIndex();
		int c = event.getColumnIndex();
		final int col = c != -1 ? modelToViewColumn(c) : -1;
		
		enqueue(new Runnable()
		{
			@Override
			public void run()
			{
				if(col != -1)
				{
					fireTableCellUpdated(row,col);
				}
				else
				{
					fireTableRowsUpdated(row,row);
				}
			}
		});
		// UIUtils.enqueue(false,new Runnable()
		// {
		// @Override
		// public void run()
		// {
		// if(col != -1)
		// {
		// fireTableCellUpdated(row,col);
		// }
		// else
		// {
		// fireTableRowsUpdated(row,row);
		// }
		// }
		// });
	}
	
	
	/**
	 * Notifies all listeners that the structure of the {@link VirtualTable} has
	 * been changed and updates the model column indices.
	 * 
	 * @param event
	 *            a {@link VirtualTableEvent} with detailed information.
	 */
	@Override
	public void virtualTableStructureChanged(VirtualTableEvent event)
	{
		Vector v = new Vector(columnIndices.length);
		for(int i = 0; i < columnIndices.length; i++)
		{
			v.add(new Integer(columnIndices[i]));
		}
		
		for(int i = v.size() - 1; i >= 0; i--)
		{
			if(((Integer)v.get(i)).intValue() >= vt.getColumnCount())
			{
				v.remove(i);
			}
		}
		
		columnIndices = new int[v.size()];
		for(int i = 0; i < v.size(); i++)
		{
			columnIndices[i] = ((Integer)v.get(i)).intValue();
		}
		
		enqueue(new Runnable()
		{
			@Override
			public void run()
			{
				fireTableStructureChanged();
			}
		});
		// UIUtils.enqueue(false,new Runnable()
		// {
		// @Override
		// public void run()
		// {
		// fireTableStructureChanged();
		// }
		// });
	}
	
	
	private void enqueue(Runnable r)
	{
		if(SwingUtilities.isEventDispatchThread())
		{
			r.run();
		}
		else
		{
			SwingUtilities.invokeLater(r);
		}
	}
}
