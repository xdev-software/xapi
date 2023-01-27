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


import xdev.db.sql.Condition;
import xdev.vt.VirtualTable;


/**
 * Identifies objects as a holder or user of a {@link VirtualTable}.
 * 
 * @author XDEV Software
 */
public interface VirtualTableOwner
{
	/**
	 * Sets the {@link VirtualTable} containing the valid values for this
	 * component.
	 * 
	 * @param virtualTable
	 *            {@link VirtualTable} containing the valid values for this
	 *            component.
	 * 
	 * @since 4.0
	 */
	public void setVirtualTable(VirtualTable vt);
	
	
	/**
	 * Returns the associated {@link VirtualTable} of this object.
	 * 
	 * @return The associated {@link VirtualTable} of this object.
	 */
	public VirtualTable getVirtualTable();
	
	
	/**
	 * Reloads the data form the underlying data source.
	 * <p>
	 * The last executed query is extended with <code>condition</code>.
	 * 
	 * @param condition
	 *            The additional filter for the query
	 * @param params
	 *            param objects used in <code>condition</code>
	 */
	public void updateModel(Condition condition, Object... params);
}
