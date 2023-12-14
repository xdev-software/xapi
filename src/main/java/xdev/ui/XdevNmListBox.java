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


import java.util.ArrayList;
import java.util.List;

import xdev.db.DBConnection;
import xdev.db.DBException;
import xdev.db.sql.Condition;
import xdev.ui.ManyToMany.State;
import xdev.util.logging.LoggerFactory;
import xdev.util.logging.XdevLogger;
import xdev.vt.KeyValues;
import xdev.vt.VirtualTable;
import xdev.vt.VirtualTable.VirtualTableRow;
import xdev.vt.VirtualTableException;


public class XdevNmListBox extends XdevListBox implements ManyToManyComponent
{
	
	/**
	 * Logger instance for this class.
	 */
	private static final XdevLogger	log	= LoggerFactory.getLogger(XdevNmListBox.class);
	
	private State					state;
	private VirtualTable			savedState;
	
	
	public XdevNmListBox()
	{
		super(true);
	}
	
	
	@Override
	public void setModel(VirtualTable vt, String itemCol, String dataCol, boolean queryData)
	{
		/*
		 * Dont't use unique indices in this VT because most of the values are
		 * not used, default values will be added and
		 * UniqueIndexDoubleValuesException are thrown
		 */
		vt = vt.clone(!queryData);
		vt.setCheckUniqueIndexDoubleValues(false);
		
		super.setModel(vt,itemCol,dataCol,false);
		
		/*
		 * Don't query all the data of the nm-table, use the refresh method
		 * instead to get a reasonable result.
		 */
		if(queryData)
		{
			Formular form = FormularSupport.getFormularOf(this);
			if(form != null)
			{
				VirtualTable masterVT = form.getVirtualTable();
				if(masterVT != null)
				{
					refresh(masterVT.createRow());
				}
			}
		}
	}
	
	
	@Override
	public void refresh(VirtualTableRow masterRecord)
	{
		try
		{
			state = new State(masterRecord,getVirtualTable());
			
			List<Object> values = new ArrayList<Object>();
			Condition condition = state.getForeignKeyValues().getCondition(values);
			updateModel(condition,values.toArray());
			VirtualTable vt = getVirtualTable(); // re-get vt after updateModel
			savedState = vt.clone(true);
			int[] indices = state.fillUpNMTable(vt,savedState);
			getItemList().syncWithVT();
			
			this.setSelectedModelIndices(indices);
		}
		catch(Exception e)
		{
			log.error(e);
		}
	}
	
	
	@Override
	public void save(boolean synchronizeDB, DBConnection connection) throws DBException,
			VirtualTableException
	{
		VirtualTable vt = getVirtualTable();
		KeyValues foreignKeyValues = state.getForeignKeyValues();
		MasterDetail.updateForeignKeys(vt,foreignKeyValues);
		
		List<VirtualTableRow> added = new ArrayList<VirtualTableRow>();
		List<VirtualTableRow> changed = new ArrayList<VirtualTableRow>();
		List<VirtualTableRow> deleted = new ArrayList<VirtualTableRow>();
		
		for(int i : this.getSelectedModelIndices())
		{
			added.add(vt.getRow(i));
		}
		
		// after quick filtering and restoring default list state, there should
		// be the pre-selected item state.
		
		ManyToMany.getChanges(savedState,added,changed,deleted);
		
		if(synchronizeDB)
		{
			ManyToMany.synchronize(vt,added,changed,deleted,connection);
		}
		
		savedState.clear();
		for(int i : getSelectedModelIndices())
		{
			// re-get rows with generated keys
			savedState.addRow(vt.getRow(i),false);
		}
		
		state = new State(state.masterRecord,vt);
	}
}
