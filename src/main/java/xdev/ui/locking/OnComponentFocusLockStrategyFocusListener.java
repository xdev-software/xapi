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


import java.awt.Component;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.FocusManager;
import javax.swing.SwingUtilities;

import xdev.db.locking.LockConstructionState;
import xdev.db.locking.LockFactory;
import xdev.db.locking.PessimisticLock;
import xdev.db.locking.RowAlreadyLockedException;
import xdev.db.locking.VirtualTableLockingUtils;
import xdev.ui.WindowCountDown;
import xdev.vt.VirtualTable.VirtualTableRow;


/**
 * Observer implementation for {@link OnComponentFocusPessimisticLockStrategy}.
 * 
 * @author XDEV Software jwill
 * @since 4.0
 */
public class OnComponentFocusLockStrategyFocusListener extends FocusAdapter
{
	private VirtualTableRow	row;
	private Lockable		lockable;
	private Component		lockableComponentRoot;
	private Component		latestRootComponent;
	
	
	public OnComponentFocusLockStrategyFocusListener(VirtualTableRow row, Lockable lockable,
			Component lockableComponent)
	{
		this.row = row;
		this.lockable = lockable;
		this.lockableComponentRoot = SwingUtilities.getRoot(lockableComponent);
		this.latestRootComponent = this.lockableComponentRoot;
		
		FocusManager.getCurrentManager().addPropertyChangeListener("focusOwner",
				new PropertyChangeListener()
				{
					@Override
					public void propertyChange(PropertyChangeEvent evt)
					{
						Component root = SwingUtilities.getRoot((Component)evt.getOldValue());
						if(root != null)
						{
							latestRootComponent = root;
						}
					}
				});
	}
	
	
	private void initLockRenewListener(PessimisticLock lock, LockConstructionState state)
	{
		if(state.isInitial())
		{
			// init renew lock mechanism if lock was successfully created.
			lock.addLockTimeoutListener(new RenewLockTimeoutListener(this.lockable,lock));
		}
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void focusGained(FocusEvent event)
	{
		// only lock persistent data
		if(!this.row.isNew())
		{
			if(this.latestRootComponent.equals(this.lockableComponentRoot)) // other
																			// window
			{
				VirtualTableRow existingLock = getStillActiveLockRow(this.row);
				if(existingLock == null)
				{
					// VirtualTableLockingUtils.renewLockRow(this.row);
					PessimisticLock recordLock = LockFactory.getPessimisticLock(this.row,
							this.lockable.getLockingTime());
					
					try
					{
						this.initLockRenewListener(recordLock,recordLock.getLock());
						this.triggerLockTimeMonitoring(this.row,this.lockable);
						// release inhibition on successful lock request
						this.lockable.getLockNotificationStrategy().lockInUseNotificationFinished(
								recordLock,this.lockable);
					}
					catch(RowAlreadyLockedException e1)
					{
						// catch LockingExceptions here to limit the layer of
						// user
						// interaction
						// with exception handling -> RAD API
						triggerLockTimeMonitoring(this.row,this.lockable);
						this.lockable.getLockNotificationStrategy().lockInUseNotification(
								e1,
								LockFactory.getPessimisticLock(this.row,
										this.lockable.getLockingTime()),this.lockable);
					}
				}
			}
		}
	}
	
	
	/**
	 * Avoid requesting locks if the lock is still valid.
	 */
	private VirtualTableRow getStillActiveLockRow(VirtualTableRow row)
	{
		PessimisticLock lock = LockFactory.getPessimisticLock(row,this.lockable.getLockingTime());
		
		return VirtualTableLockingUtils.getPotentialBlockingRow(lock);
	}
	
	
	protected void triggerLockTimeMonitoring(VirtualTableRow record, Lockable lockable)
	{
		final PessimisticLock recordLock = LockFactory.getPessimisticLock(record,
				lockable.getLockingTime());
		new WindowCountDown(recordLock,lockable.getCountdownMonitors());
	}
}
