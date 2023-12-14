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
import xdev.db.locking.PessimisticLock;
import xdev.db.locking.PessimisticLockStrategy;
import xdev.db.locking.RowAlreadyLockedException;
import xdev.db.locking.VirtualTableLockingUtils;
import xdev.ui.WindowCountDown;
import xdev.vt.VirtualTable.VirtualTableRow;


/**
 * On-record-load lock strategy implementation of
 * {@link PessimisticLockStrategy} without any lock renew notifications.
 * <p>
 * {@link PessimisticLock#getLock()} is called instantly on record selection.
 * 
 * @author XDEV Software jwill
 * @since 4.0
 */
public class NoRenewOnLoadPessimisticLockStrategy implements PessimisticLockStrategy// <VirtualTableRow>
{
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void triggerLocking(VirtualTableRow record, Lockable lockable)
			throws RowAlreadyLockedException
	{
		// only lock persistent data
		if(!record.isNew())
		{
			PessimisticLock recordLock = LockFactory.getPessimisticLock(record,
					lockable.getLockingTime());
			// catch LockingExceptions here to limit the layer of user
			// interaction
			// with exception handling -> RAD API
			try
			{
				// default strategy - record lock
				recordLock.getLock();
				triggerLockTimeMonitoring(record,lockable);
				// release inhibition on successful lock request
				lockable.getLockNotificationStrategy().lockInUseNotificationFinished(recordLock,
						lockable);
			}
			catch(RowAlreadyLockedException e1)
			{
				triggerLockTimeMonitoring(record,lockable);
				lockable.getLockNotificationStrategy().lockInUseNotification(e1,
						LockFactory.getPessimisticLock(record,lockable.getLockingTime()),lockable);
			}
		}
	}
	
	
	protected void triggerLockTimeMonitoring(final VirtualTableRow record, final Lockable lockable)
	{
		final PessimisticLock recordLock = LockFactory.getPessimisticLock(record,
				lockable.getLockingTime());
		new WindowCountDown(recordLock,lockable.getCountdownMonitors());
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void releaseLocking(VirtualTableRow record, Lockable lockable)
	{
		PessimisticLock lock = LockFactory.getPessimisticLock(record,lockable.getLockingTime());
		if(VirtualTableLockingUtils.isUserHoldsLock(lock))
		{
			lock.removeLock();
		}
	}
}
