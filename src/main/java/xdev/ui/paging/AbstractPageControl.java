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


import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.HierarchyEvent;
import java.awt.event.HierarchyListener;
import java.util.List;
import java.util.Vector;

import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.JViewport;
import javax.swing.Timer;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import xdev.db.DBException;
import xdev.db.DBPager;
import xdev.db.QueryInfo;
import xdev.db.Result;
import xdev.db.sql.SELECT;
import xdev.util.logging.LoggerFactory;
import xdev.util.logging.XdevLogger;
import xdev.vt.VirtualTable;
import xdev.vt.VirtualTableException;
import xdev.vt.VirtualTableFillMethod;


/**
 * @author XDEV Software (HL)
 * @since 4.0
 */
public abstract class AbstractPageControl<C extends JComponent> implements PageControl
{
	public static final XdevLogger	log				= LoggerFactory
															.getLogger(AbstractPageControl.class);
	
	protected final C				component;
	// protected final P pageable;
	protected DBPager				pager;
	protected List<ChangeListener>	changeListeners	= new Vector();
	protected QueryInfo				lastQuery;
	protected int					rowsPerPage		= DYNAMIC_ROW_COUNT;
	
	
	public AbstractPageControl(C c/* , P p */)
	{
		this.component = c;
		// this.pageable = p;
		
		component.addHierarchyListener(new HierarchyListener()
		{
			@Override
			public void hierarchyChanged(HierarchyEvent e)
			{
				if(e.getID() == HierarchyEvent.HIERARCHY_CHANGED && component.isShowing())
				{
					addListener();
				}
			}
		});
	}
	
	
	private void addListener()
	{
		Container resizeAnchor = component, parent;
		if((parent = component.getParent()) instanceof JViewport
				&& (parent = parent.getParent()) instanceof JScrollPane)
		{ // use scrollpane as resize anchor
			resizeAnchor = parent;
		}
		
		final Timer timer = new Timer(250,new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent event)
			{
				try
				{
					if(pager != null)
					{
						if(pager.isClosed())
						{
							refresh();
						}
						
						rowsPerPageChanged();
					}
				}
				catch(DBException e)
				{
					handle(e);
				}
			}
		});
		timer.setRepeats(false);
		timer.restart();
		
		resizeAnchor.addComponentListener(new ComponentAdapter()
		{
			@Override
			public void componentResized(ComponentEvent e)
			{
				timer.restart();
			}
		});
		
		component.addHierarchyListener(new HierarchyListener()
		{
			@Override
			public void hierarchyChanged(HierarchyEvent e)
			{
				if(e.getID() == HierarchyEvent.HIERARCHY_CHANGED && !component.isShowing())
				{
					component.removeHierarchyListener(this);
					
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
	}
	
	
	@Override
	public void addChangeListener(ChangeListener l)
	{
		changeListeners.add(l);
	}
	
	
	public void refresh()
	{
		if(lastQuery != null)
		{
			try
			{
				changeModel(lastQuery.getSelect(),lastQuery.getParameters(),getCurrentPageIndex());
			}
			catch(Exception e)
			{
				handle(e);
			}
		}
	}
	
	
	public void changeModel(SELECT select, Object[] params, int showPageIndex) throws DBException
	{
		if(pager != null)
		{
			pager.close();
		}
		
		lastQuery = new QueryInfo(select,params);
		pager = getVirtualTable().getDataSource().openConnection()
				.getPager(getActualRowCount(),select,params);
		Result result = showPageIndex == 0 ? pager.firstPage() : pager.gotoPage(Math.min(
				showPageIndex,getMaxPageIndex()));
		if(result != null)
		{
			dataReceived(result);
		}
		else
		{
			getVirtualTable().clear();
		}
	}
	
	
	private void dataReceived(Result data) throws VirtualTableException, DBException
	{
		updateModel(data);
		
		ChangeEvent event = new ChangeEvent(this);
		for(ChangeListener l : changeListeners)
		{
			l.stateChanged(event);
		}
	}
	
	
	protected void updateModel(Result data) throws VirtualTableException, DBException
	{
		VirtualTable vt = getVirtualTable();
		QueryInfo lastQuery = vt.getLastQuery();
		vt.addData(data,VirtualTableFillMethod.OVERWRITE);
		vt.setLastQuery(lastQuery); // restore query before paging
	}
	
	
	@Override
	public boolean hasNextPage()
	{
		return pager != null && pager.hasNextPage();
	}
	
	
	@Override
	public boolean hasPreviousPage()
	{
		return pager != null && pager.hasPreviousPage();
	}
	
	
	@Override
	public void nextPage()
	{
		try
		{
			Result result = pager.nextPage();
			if(result != null)
			{
				dataReceived(result);
			}
		}
		catch(Exception e)
		{
			handle(e);
		}
	}
	
	
	@Override
	public void previousPage()
	{
		try
		{
			Result result = pager.previousPage();
			if(result != null)
			{
				dataReceived(result);
			}
		}
		catch(Exception e)
		{
			handle(e);
		}
	}
	
	
	@Override
	public void firstPage()
	{
		try
		{
			Result result = pager.firstPage();
			if(result != null)
			{
				dataReceived(result);
			}
		}
		catch(Exception e)
		{
			handle(e);
		}
	}
	
	
	@Override
	public void lastPage()
	{
		try
		{
			Result result = pager.lastPage();
			if(result != null)
			{
				dataReceived(result);
			}
		}
		catch(Exception e)
		{
			handle(e);
		}
	}
	
	
	@Override
	public void gotoPage(int page)
	{
		try
		{
			Result result = pager.gotoPage(page);
			if(result != null)
			{
				dataReceived(result);
			}
		}
		catch(Exception e)
		{
			handle(e);
		}
	}
	
	
	@Override
	public int getCurrentPageIndex()
	{
		return pager != null ? pager.getCurrentPageIndex() : 0;
	}
	
	
	@Override
	public int getMaxPageIndex()
	{
		return pager != null ? pager.getMaxPageIndex() : 0;
	}
	
	
	@Override
	public void setRowsPerPage(int rowsPerPage)
	{
		if(this.rowsPerPage != rowsPerPage || pager != null
				&& pager.getRowsPerPage() != getActualRowCount())
		{
			this.rowsPerPage = rowsPerPage;
			
			rowsPerPageChanged();
		}
	}
	
	
	protected void rowsPerPageChanged()
	{
		if(pager != null)
		{
			try
			{
				Result result = pager.setRowsPerPage(getActualRowCount());
				if(result != null)
				{
					dataReceived(result);
				}
			}
			catch(Exception e)
			{
				handle(e);
			}
		}
	}
	
	
	@Override
	public int getRowsPerPage()
	{
		return rowsPerPage;
	}
	
	
	protected int getActualRowCount()
	{
		return rowsPerPage == DYNAMIC_ROW_COUNT ? getVisibleRowCount() : rowsPerPage;
	}
	
	
	public abstract int getVisibleRowCount();
	
	
	protected void handle(Throwable e)
	{
		log.error(e);
	}
}
