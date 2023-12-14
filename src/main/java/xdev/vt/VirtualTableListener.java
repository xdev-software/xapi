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


import java.util.EventListener;


/**
 * This interface specifies all events from a {@link VirtualTable} an external
 * listener can register on.
 * 
 * @author XDEV Software Corp.
 * @see VirtualTableAdapter
 */
public interface VirtualTableListener extends EventListener
{
	/**
	 * This method gets called, when a row gets deleted in the
	 * {@link VirtualTable}.
	 * 
	 * @param event
	 *            an {@link VirtualTableEvent} with detailed information
	 */
	public void virtualTableRowDeleted(VirtualTableEvent event);
	

	/**
	 * This method gets called, when a row gets inserted in the
	 * {@link VirtualTable}.
	 * 
	 * @param event
	 *            an {@link VirtualTableEvent} with detailed information
	 */
	public void virtualTableRowInserted(VirtualTableEvent event);
	

	/**
	 * This method gets called, when a row gets updated in the
	 * {@link VirtualTable}.
	 * 
	 * @param event
	 *            an {@link VirtualTableEvent} with detailed information
	 */
	public void virtualTableRowUpdated(VirtualTableEvent event);
	

	/**
	 * This method gets called, when a change of the {@link VirtualTable}s data
	 * happens.
	 * <p>
	 * Examples are: Clearing the data or doing a bulk addition of data.
	 * </p>
	 * 
	 * @param event
	 *            an {@link VirtualTableEvent} with detailed information
	 */
	public void virtualTableDataChanged(VirtualTableEvent event);
	

	/**
	 * This method gets called when the {@link VirtualTable}'s structure was
	 * changed, e.g. a column was added or removed.
	 * 
	 * @param event
	 *            an {@link VirtualTableEvent} with detailed information
	 */
	public void virtualTableStructureChanged(VirtualTableEvent event);
}
