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


import java.util.List;
import java.util.Vector;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import xdev.db.DBException;
import xdev.db.DBPager;
import xdev.db.QueryInfo;
import xdev.db.Result;
import xdev.db.sql.SELECT;
import xdev.ui.Formular;
import xdev.util.logging.LoggerFactory;
import xdev.util.logging.XdevLogger;
import xdev.vt.VirtualTable;
import xdev.vt.VirtualTableException;
import xdev.vt.VirtualTableFillMethod;


/**
 * @author XDEV Software
 */
// Implementation copied from AbstractPageControl
public class VirtualFormularPageControl implements PageControl
{
	public static final XdevLogger	log				= LoggerFactory
															.getLogger(AbstractPageControl.class);
	
	protected final Formular		form;
	private VirtualTable			vt;
	protected DBPager				pager;
	protected List<ChangeListener>	changeListeners	= new Vector();
	protected QueryInfo				lastQuery;
	protected int					rowsPerPage		= DYNAMIC_ROW_COUNT;
	
	
	public VirtualFormularPageControl(Formular form)
	{
		this.form = form;
		this.rowsPerPage = 1;
	}
	
	
	@Override
	public void addChangeListener(ChangeListener l)
	{
		changeListeners.add(l);
	}
	
	
	@Override
	public void refresh()
	{
		if(lastQuery != null)
		{
			try
			{
				changeModel(this.vt,lastQuery.getSelect(),lastQuery.getParameters(),
						getCurrentPageIndex());
			}
			catch(Exception e)
			{
				handle(e);
			}
		}
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public VirtualTable getVirtualTable()
	{
		return this.vt;
	}
	
	
	public void changeModel(VirtualTable vt, SELECT select, Object[] params, int showPageIndex)
			throws DBException
	{
		this.vt = vt;
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
		this.form.setModel(this.vt.getRow(0));
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
		// if(this.rowsPerPage != rowsPerPage || pager != null
		// && pager.getRowsPerPage() != getActualRowCount())
		// {
		// this.rowsPerPage = rowsPerPage;
		//
		// rowsPerPageChanged();
		// }
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isSingleRowPager()
	{
		return true;
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
	
	
	public int getVisibleRowCount()
	{
		return this.rowsPerPage;
	}
	
	
	protected void handle(Throwable e)
	{
		// cut exception!?
		log.error(e);
	}
	
}
