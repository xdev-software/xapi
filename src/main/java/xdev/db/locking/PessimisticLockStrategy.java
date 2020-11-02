package xdev.db.locking;

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


import xdev.ui.locking.Lockable;
import xdev.vt.VirtualTable.VirtualTableRow;


/**
 * Determines when a specific {@link PessimisticLock} instances
 * {@link PessimisticLock#getLock()} is being called.
 * 
 * @author XDEV Software jwill
 * @since 4.0
 */
public interface PessimisticLockStrategy// <RecordType>
{
	/**
	 * Calls {@link PessimisticLock#getLock()} at specific circumstances.
	 * 
	 * @param record
	 *            the "to lock" record.
	 * @param lockable
	 *            the affected {@link Lockable}
	 * @throws RowAlreadyLockedException
	 *             if the concerned row is already locked.
	 */
	void triggerLocking(VirtualTableRow record, Lockable lockable) throws RowAlreadyLockedException;
	
	
	/**
	 * Releases the concerned lock at specific circumstances.
	 * 
	 * @param record
	 *            the "to release" record.
	 * @param lockable
	 *            the affected {@link Lockable}
	 */
	void releaseLocking(VirtualTableRow record, Lockable lockable);
}
