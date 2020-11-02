package xdev.ui.paging;

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


import java.awt.Cursor;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.HierarchyEvent;
import java.awt.event.HierarchyListener;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.swing.JTable;
import javax.swing.RowSorter;
import javax.swing.RowSorter.SortKey;
import javax.swing.SortOrder;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.event.RowSorterEvent;
import javax.swing.event.RowSorterEvent.Type;
import javax.swing.event.RowSorterListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import xdev.db.DBException;
import xdev.db.DBPager;
import xdev.db.QueryInfo;
import xdev.db.Result;
import xdev.db.sql.SELECT;
import xdev.ui.MasterDetailComponent;
import xdev.util.logging.LoggerFactory;
import xdev.util.logging.XdevLogger;
import xdev.vt.VirtualTable;
import xdev.vt.VirtualTable.VirtualTableRow;
import xdev.vt.VirtualTableColumn;
import xdev.vt.VirtualTableFillMethod;
import xdev.vt.VirtualTableModel;
import xdev.vt.VirtualTableWrapper;


/**
 * {@link VirtualTable}-based {@link TableModel} which uses a {@link DBPager} to
 * lazy load the viewed rows.
 * 
 * @author XDEV Software
 * 
 * @since 4.0
 */
public class LazyLoadingTableModel<T extends JTable & MasterDetailComponent<T>> extends
		AbstractTableModel implements VirtualTableWrapper
{
	
	public static final XdevLogger	log				= LoggerFactory
															.getLogger(LazyLoadingTableModel.class);
	
	private T						table;
	private VirtualTable			vt;
	private int[]					columnIndices;
	private SELECT					select;
	private Object[]				params;
	private DBPager					pager;
	
	private int						totalRowCount	= -1;
	private Timer					loadTimer;
	private ThreadPoolExecutor		executor;
	private SortKey					sortKey;
	private int						requestedFirst	= -1;
	
	private int						requestedLast	= -1;
	private int						loadedFirst		= -1;
	private int						loadedLast		= -1;
	
	
	public LazyLoadingTableModel(T t)
	{
		table = t;
		table.addHierarchyListener(new HierarchyListener()
		{
			@Override
			public void hierarchyChanged(HierarchyEvent e)
			{
				if(e.getID() == HierarchyEvent.HIERARCHY_CHANGED && !table.isShowing())
				{
					table.removeHierarchyListener(this);
					
					if(pager != null)
					{
						try
						{
							pager.close();
						}
						catch(DBException ex)
						{
							handle(ex);
						}
					}
				}
			}
		});
		
		loadTimer = new Timer(250,new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				synchronized(LazyLoadingTableModel.this)
				{
					final int first = requestedFirst;
					final int last = requestedLast;
					executor.execute(new Runnable()
					{
						@Override
						public void run()
						{
							try
							{
								loadRequestedData(first,last);
							}
							catch(Exception e)
							{
								handle(e);
							}
						}
					});
				}
			}
		});
		loadTimer.setRepeats(false);
		
		executor = new ThreadPoolExecutor(0,1,10L,TimeUnit.SECONDS,
				new LinkedBlockingQueue<Runnable>());
	}
	
	
	public void setData(VirtualTable vt, int[] columnIndices, SELECT select, Object[] params)
	{
		boolean needsRefresh = this.vt != null;
		
		this.vt = vt;
		this.columnIndices = columnIndices;
		this.select = select;
		this.params = params;
		totalRowCount = -1;
		
		if(needsRefresh)
		{
			refresh();
		}
	}
	
	
	public void clear()
	{
		totalRowCount = 0;
		if(vt != null)
		{
			vt.clear();
		}
		fireTableDataChanged();
	}
	
	
	public void refresh()
	{
		requestedFirst = requestedLast = loadedFirst = loadedLast = -1;
		
		if(sortKey != null)
		{
			int viewIndex = sortKey.getColumn();
			int sortindex = columnIndices[viewIndex];
			VirtualTableColumn<?> col = vt.getColumnAt(sortindex);
			select.clear_ORDER_BY();
			select.ORDER_BY(col,sortKey.getSortOrder() == SortOrder.DESCENDING);
		}
		
		try
		{
			if(pager != null)
			{
				pager.close();
			}
			pager = getVirtualTable().getDataSource().openConnection().getPager(10,select,params);
		}
		catch(DBException e)
		{
			handle(e);
		}
		
		SwingUtilities.invokeLater(new Runnable()
		{
			@Override
			public void run()
			{
				fireTableDataChanged();
			}
		});
	}
	
	
	public RowSorter createRowSorter(TableModel model)
	{
		TableRowSorter sorter = new TableRowSorter(model)
		{
			@Override
			public void sort()
			{
				fireRowSorterChanged(null);
			}
		};
		
		sorter.addRowSorterListener(new RowSorterListener()
		{
			int	call	= 1;
			
			
			@Override
			public void sorterChanged(RowSorterEvent e)
			{
				if(call % 2 == 0)
				{
					Type t = e.getType();
					if(t == Type.SORTED && call != 2)
					{
						return;
					}
					List<?> l = e.getSource().getSortKeys();
					if(l.isEmpty())
					{
						return;
					}
					sortKey = (SortKey)l.get(0);
					
					synchronized(LazyLoadingTableModel.this)
					{
						refresh();
					}
				}
				call++;
			}
		});
		
		return sorter;
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getRowCount()
	{
		if(totalRowCount == -1)
		{
			totalRowCount = 0;
			
			executor.execute(new Runnable()
			{
				@Override
				public void run()
				{
					fetchTotalRowCount();
				}
			});
		}
		
		return totalRowCount;
	}
	
	
	private void fetchTotalRowCount()
	{
		try
		{
			pager = getVirtualTable().getDataSource().openConnection().getPager(10,select,params);
			totalRowCount = pager.getTotalRows();
			
			SwingUtilities.invokeLater(new Runnable()
			{
				@Override
				public void run()
				{
					fireTableDataChanged();
				}
			});
		}
		catch(DBException e)
		{
			handle(e);
		}
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object getValueAt(int row, int col)
	{
		synchronized(this)
		{
			if(row < loadedFirst || row > loadedLast)
			{
				if(row < requestedFirst || row > requestedLast)
				{
					Rectangle vr = table.getVisibleRect();
					int requestedFirst = table.rowAtPoint(vr.getLocation());
					vr.translate(0,vr.height);
					int requestedLast = table.rowAtPoint(vr.getLocation());
					
					if(requestedLast == -1 || requestedLast >= totalRowCount)
					{
						// end of table
						requestedLast = totalRowCount - 1;
					}
					
					if(this.requestedFirst != requestedFirst || this.requestedLast != requestedLast)
					{
						this.requestedFirst = requestedFirst;
						this.requestedLast = requestedLast;
						
						loadTimer.restart();
					}
				}
				
				return " ";
			}
		}
		
		return vt.getValueAt(row - loadedFirst,columnIndices[col]);
	}
	
	
	private void loadRequestedData(int requestedFirst, int requestedLast) throws Exception
	{
		if(pager != null)
		{
			if(pager.isClosed())
			{
				refresh();
				return;
			}
			
			try
			{
				table.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
				
				pager.setRowsPerPage(requestedLast - requestedFirst + 1);
				Result result = pager.gotoRow(requestedFirst);
				QueryInfo lastQuery = vt.getLastQuery();
				vt.addData(result,VirtualTableFillMethod.OVERWRITE);
				vt.setLastQuery(lastQuery); // restore query before paging
			}
			finally
			{
				table.setCursor(Cursor.getDefaultCursor());
			}
		}
		
		synchronized(this)
		{
			loadedFirst = requestedFirst;
			loadedLast = requestedLast;
		}
		
		SwingUtilities.invokeLater(new Runnable()
		{
			@Override
			public void run()
			{
				table.saveState();
				
				fireTableDataChanged();
				
				table.restoreState();
			}
		});
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
	 * {@inheritDoc}
	 */
	@Override
	public int getColumnCount()
	{
		return columnIndices.length;
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getColumnName(int col)
	{
		return vt.getColumnCaption(columnIndices[col]);
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Class<?> getColumnClass(int col)
	{
		return VirtualTableModel.getClassForType(vt.getColumnAt(columnIndices[col]).getType());
	}
	
	
	/**
	 * {@inheritDoc}
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
	public void setValueAt(Object newValue, int row, int col)
	{
		try
		{
			row -= loadedFirst;
			
			Object oldValue = getValueAt(row,col);
			int columnIndex = columnIndices[col];
			if(!VirtualTable.equals(oldValue,newValue,vt.getColumnAt(columnIndex).getType()))
			{
				vt.setValueAt(newValue,row,columnIndex);
			}
		}
		catch(Exception e)
		{
			log.error(e);
		}
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
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public VirtualTableRow getVirtualTableRow(int modelRow)
	{
		return vt.getRow(modelRow - loadedFirst);
	}
	
	
	protected void handle(Throwable e)
	{
		log.error(e);
	}
	
	
	/**
	 * Returns the lazy loading default select.
	 * 
	 * @return the default {@link SELECT}
	 */
	public SELECT getSelect()
	{
		return select;
	}
	
	
	/**
	 * Returns the concerned pager.
	 * 
	 * @return the currently used {@link DBPager}.
	 */
	public DBPager getPager()
	{
		return pager;
	}
	
	
	/**
	 * Returns the currently first requested row.
	 * 
	 * @return the currently first requested row.
	 */
	public int getRequestedFirst()
	{
		return requestedFirst;
	}
	
	
	/**
	 * Returns the currently last requested row.
	 * 
	 * @return the currently last requested row.
	 */
	public int getRequestedLast()
	{
		return requestedLast;
	}
}
