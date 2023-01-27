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


import xdev.db.locking.LockFactory;
import xdev.db.locking.LockingException;
import xdev.db.locking.PessimisticLock;
import xdev.db.locking.RowAlreadyLockedException;
import xdev.vt.VirtualTable.VirtualTableRow;


/**
 * The concrete pessimistic lock approach implementation of {@link LockSupport}.
 * 
 * @author XDEV Software jwill
 * @since 4.0
 */
public class PessimisticLockSupport implements LockSupport// <VirtualTableRow>
{
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isNotLocked(VirtualTableRow record, Lockable lockable) throws LockingException
	{
		PessimisticLock recordLock = LockFactory.getPessimisticLock(record,
				lockable.getLockingTime());
		return recordLock.isUserValid();
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void initiateLocking(VirtualTableRow record, Lockable lockable)
			throws RowAlreadyLockedException
	{
		lockable.getLockStrategy().triggerLocking(record,lockable);
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void releaseLock(VirtualTableRow row, Lockable lockable) throws LockingException
	{
		if(row != null) // check for initial model
		{
			lockable.getLockStrategy().releaseLocking(row,lockable);
			lockable.getLockNotificationStrategy().lockInUseNotificationFinished(
					LockFactory.getPessimisticLock(row,lockable.getLockingTime()),lockable);
		}
	}
}
