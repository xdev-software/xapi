package xdev.ui.locking;

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


import xdev.db.locking.LockConstructionState;
import xdev.db.locking.LockFactory;
import xdev.db.locking.PessimisticLock;
import xdev.db.locking.PessimisticLockStrategy;
import xdev.db.locking.RowAlreadyLockedException;
import xdev.db.locking.VirtualTableLockingUtils;
import xdev.ui.WindowCountDown;
import xdev.vt.VirtualTable.VirtualTableRow;


/**
 * On-record-load lock strategy implementation of
 * {@link PessimisticLockStrategy}.
 * <p>
 * {@link PessimisticLock#getLock()} is called instantly on record selection.
 * 
 * @author XDEV Software jwill
 * @since 4.0
 */
public class OnLoadPessimisticLockStrategy implements PessimisticLockStrategy// <VirtualTableRow>
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
				this.initLockRenewListener(recordLock,lockable,recordLock.getLock());
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
	
	
	private void initLockRenewListener(PessimisticLock lock, Lockable lockable,
			LockConstructionState state)
	{
		if(state.isInitial())
		{
			// init renew lock mechanism if lock was successfully created.
			lock.addLockTimeoutListener(new RenewLockTimeoutListener(lockable,lock));
		}
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
