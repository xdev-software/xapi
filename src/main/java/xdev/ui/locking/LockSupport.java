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
package xdev.ui.locking;


import xdev.db.locking.LockingException;
import xdev.db.locking.PessimisticLockStrategy;
import xdev.db.locking.RowAlreadyLockedException;
import xdev.vt.VirtualTable.VirtualTableRow;


/**
 * The basic lock functionality provider for {@link Lockable} implementations.
 * 
 * @author XDEV Software jwill
 * @since 4.0
 */
public interface LockSupport// <RecordType>
{
	/**
	 * Initiates the lock process considering the applied
	 * {@link PessimisticLockStrategy}.
	 * 
	 * @param row
	 *            the record which shall be locked.
	 * @param lockable
	 *            the lockable implementation.
	 * @throws RowAlreadyLockedException
	 *             exception information about conflicting lock requests.
	 * @throws LockingException
	 *             exception information about an error within the lock process.
	 * 
	 * @see Lockable#getLockStrategy()
	 */
	void initiateLocking(VirtualTableRow row, Lockable lockable) throws RowAlreadyLockedException,
			LockingException;
	
	
	/**
	 * Releases an existing lock for the concerned {@link VirtualTableRow}.
	 * 
	 * @param row
	 *            the record which shall be locked.
	 * @param lockable
	 *            the lockable implementation.
	 * @throws LockingException
	 *             exception information about an error within the lock process.
	 */
	void releaseLock(VirtualTableRow row, Lockable lockable) throws LockingException;
	
	
	/**
	 * Informs about the lock state of a particular record.
	 * 
	 * @param record
	 *            the record to get the lock state from.
	 * @param lockable
	 *            the lockable implementation.
	 * @return the indicator whether the concerned record is already locked.
	 * @throws LockingException
	 *             exception information about an error within the lock process.
	 */
	boolean isNotLocked(VirtualTableRow record, Lockable lockable) throws LockingException;
}
