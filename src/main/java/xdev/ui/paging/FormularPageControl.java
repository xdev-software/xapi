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


import javax.swing.JComponent;

import xdev.db.DBException;
import xdev.db.Result;
import xdev.db.sql.SELECT;
import xdev.ui.Formular;
import xdev.vt.VirtualTable;
import xdev.vt.VirtualTableException;


/**
 * 
 * @author XDEV Software
 * @since 4.0
 */
public class FormularPageControl<C extends JComponent & Formular> extends AbstractPageControl<C>
{
	private VirtualTable	vt;
	private final C			formular;
	
	
	public FormularPageControl(C c)
	{
		super(c);
		this.formular = c;
		
		rowsPerPage = 1;
	}
	
	
	@Override
	public int getVisibleRowCount()
	{
		return 1;
	}
	
	
	@Override
	public void setRowsPerPage(int rowsPerPage)
	{
	}
	
	
	@Override
	public boolean isSingleRowPager()
	{
		return true;
	}
	
	
	public void changeModel(VirtualTable vt, SELECT select, Object[] params, int showPageIndex)
			throws DBException
	{
		this.vt = vt;
		
		super.changeModel(select,params,showPageIndex);
	}
	
	
	@Override
	public VirtualTable getVirtualTable()
	{
		return this.vt;
	}
	
	
	@Override
	protected void updateModel(Result data) throws VirtualTableException, DBException
	{
		super.updateModel(data);
		
		this.formular.setModel(this.vt.getRow(0));
	}
}
