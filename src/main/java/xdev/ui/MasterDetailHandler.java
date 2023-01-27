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


import xdev.vt.VirtualTable;
import xdev.vt.VirtualTable.VirtualTableRow;


/**
 * Handler to manage the corporation between {@link MasterDetailComponent}s or a
 * {@link Formular} and a {@link MasterDetailComponent}.
 * 
 * @see MasterDetail#setHandler(MasterDetailHandler)
 * @see MasterDetailComponent
 * @author XDEV Software
 */
public interface MasterDetailHandler
{
	/**
	 * Client property used to turn off auto-synchronization with the datasource<br>
	 * <code>formular.putClientProperty(OFFLINE,Boolean.TRUE);</code>
	 */
	public final static Object	OFFLINE	= "MasterDetail.offline";
	
	
	/**
	 * Connects two {@link MasterDetailComponent}s.
	 * <p>
	 * The detail is filled with the appropriate records depending on the
	 * master's selection.
	 * <p>
	 * The maching master is selected if the detail's selection changes.
	 * 
	 * @param master
	 *            the component which operates as master
	 * @param detail
	 *            the component which operates as detail view
	 * @throws MasterDetailException
	 *             if necessary properties like the linked {@link VirtualTable}
	 *             are not set or insufficient entity relation information is
	 *             available
	 */
	public void connect(MasterDetailComponent master, MasterDetailComponent detail)
			throws MasterDetailException;
	
	
	/**
	 * Connects a {@link MasterDetailComponent} with a {@link Formular}, meaning the
	 * formular (<code>detail</code>) is filled with the current selected record
	 * ( {@link VirtualTableRow}) of the <code>master</code>.
	 * 
	 * @param master
	 *            the component which operates as master
	 * @param detail
	 *            the {@link Formular} which operates as detail view
	 * @throws MasterDetailException
	 *             if necessary properties like the linked {@link VirtualTable}
	 *             are not set or insufficient entity relation information is
	 *             available
	 */
	public void connect(final MasterDetailComponent master, final Formular detail)
			throws MasterDetailException;
}
