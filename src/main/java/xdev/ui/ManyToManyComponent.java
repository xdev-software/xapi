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


import xdev.db.DBConnection;
import xdev.db.DBException;
import xdev.vt.VirtualTable;
import xdev.vt.VirtualTable.VirtualTableRow;
import xdev.vt.VirtualTableException;


/**
 * Component which manages many to many relations of a specific record.
 * <p>
 * Many-to-many components are used inside a {@link Formular}.
 * </p>
 * 
 * @author XDEV Software
 * @see Formular
 */
public interface ManyToManyComponent
{
	/**
	 * Returns the associated {@link VirtualTable} of this component.
	 * 
	 * @return the associated {@link VirtualTable} of this component
	 */
	public VirtualTable getVirtualTable();
	
	
	/**
	 * Called by the {@link Formular} when the master record changes.
	 * 
	 * @param masterRecord
	 *            the new record to diplay the detail records for
	 */
	public void refresh(VirtualTableRow masterRecord);
	
	
	/**
	 * Deletes removed, inserts added and updates changed records of the
	 * associated detail table.
	 * 
	 * @param synchronizeDB
	 *            if set to <code>true</code>, changes will be propagated to the
	 *            underlying data source.
	 * @param connection
	 *            the connection used by the transaction
	 * @throws DBException
	 *             if a database error occurs
	 * 
	 * @throws VirtualTableException
	 * 
	 * @see Formular#save
	 */
	public void save(boolean synchronizeDB, DBConnection connection) throws DBException,
			VirtualTableException;
	
	
	/**
	 * Checks if the component's state since the last call of
	 * {@link #refresh(VirtualTableRow)} has changed.
	 * 
	 * @return <code>true</code> if the component's state has changed,
	 *         <code>false</code> otherwise
	 * 
	 * @since 3.1
	 */
	public boolean hasStateChanged();
	
	
	/**
	 * Saves the state of the component internally.
	 * <p>
	 * A saved state can be restored using {@link #restoreState()}.
	 * </p>
	 * 
	 * @since 3.2
	 */
	public void saveState();
	
	
	/**
	 * Restores the internally saved state of the component.
	 * <p>
	 * The state of the component can be saved using {@link #saveState()}.
	 * </p>
	 * 
	 * @since 3.2
	 */
	public void restoreState();
}
