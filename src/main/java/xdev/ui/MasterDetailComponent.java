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


import java.util.Map;

import javax.swing.JComponent;

import xdev.vt.VirtualTable;
import xdev.vt.VirtualTable.VirtualTableRow;


/**
 * Extended {@link FormularComponent} which is used to display the detail
 * records for a specific master record, or to display master records and
 * control another MasterDetailComponent which displays the details.
 * <p>
 * A MasterDetailComponent can be connected to a {@link Formular}:
 * 
 * <pre>
 * {@link MasterDetail#connect(MasterDetailComponent, Formular)}
 * </pre>
 * 
 * or to another MasterDetailComponent:
 * 
 * <pre>
 * {@link MasterDetail#connect(MasterDetailComponent, MasterDetailComponent)}
 * </pre>
 * 
 * 
 * 
 * @author XDEV Software
 * 
 * @param <C>
 *            The implementor's type
 * 
 * @see MasterDetail
 * @see MasterDetailHandler
 */
public interface MasterDetailComponent<C extends JComponent> extends FormularComponent<C>,
		VirtualTableOwner
{
	/**
	 * Returns the selected {@link VirtualTableRow} of this component, or
	 * <code>null</code> if nothing is selected.
	 * 
	 * @return the selected {@link VirtualTableRow} of this component
	 */
	public VirtualTableRow getSelectedVirtualTableRow();
	
	
	/**
	 * Reloads the data from the underlying data source with the last executed
	 * resp. default query.
	 */
	public void refresh();
	
	
	/**
	 * Clears the underlying data model, mostly a {@link VirtualTable}, and the
	 * view.
	 */
	public void clearModel();
	
	
	/**
	 * Sets the value of this component (master), taking the value(s) of the
	 * record.
	 * <p>
	 * Called by the {@link MasterDetailHandler} if the client's value has
	 * changed.
	 * 
	 * @param vt
	 *            the underlying virtual table
	 * @param record
	 *            the data &lt;column,value&gt;
	 * @since 3.2
	 */
	public void setMasterValue(VirtualTable vt, Map<String, Object> record);
	
	
	/**
	 * Sets the handler which controls the corporation of two
	 * {@link MasterDetailComponent}s.
	 * 
	 * @param detailHandler
	 *            the new {@link DetailHandler}
	 */
	public void setDetailHandler(DetailHandler detailHandler);
	
	
	
	/**
	 * Handles the detail view of a {@link MasterDetailComponent} which is used
	 * as detail of another {@link MasterDetailComponent} which is the master.
	 */
	public static interface DetailHandler
	{
		/**
		 * Adjusts the detail view according to the selected master value.
		 * 
		 * @param value
		 *            the selected primary key value of the master
		 */
		public void checkDetailView(Map<String, Object> value);
	}
}
