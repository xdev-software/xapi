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


import java.awt.Container;
import java.text.SimpleDateFormat;

import xdev.db.locking.LockFactory;
import xdev.db.locking.LockNotificationStrategy;
import xdev.db.locking.LockingException;
import xdev.db.locking.PessimisticLock;
import xdev.db.locking.RowAlreadyLockedException;
import xdev.ui.EnabledState;


/**
 * Information dialog and enabled state {@link LockNotificationStrategy}.
 * 
 * @author XDEV Software jwill
 * @since 4.0
 */
public class HybridLockInUseNotificationStrategy implements LockNotificationStrategy
{
	private Container	lockDisplay;
	
	
	/**
	 * Initializes this {@link LockNotificationStrategy}.
	 * 
	 * @param lockDisplay
	 *            the lock display to manipulate the enabled state for
	 *            notification purpose.
	 */
	public HybridLockInUseNotificationStrategy(Container lockDisplay)
	{
		this.lockDisplay = lockDisplay;
	}
	
	
	/**
	 * Only for integration purpose!!!
	 */
	public HybridLockInUseNotificationStrategy()
	{
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void lockInUseNotification(RowAlreadyLockedException inUseException,
			PessimisticLock lock, Lockable lockable) throws LockingException
	{
		// hybrid (1) - enabled state
		final EnabledState state = LockFactory.getLockUIState(lock,this.lockDisplay);
		state.disableAllComponents();
		
		// hybrid (2) - notification dialog
		SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
		lockable.getLockInUseNotifier()
				.setTitle(UIResourceBundle.getString("LockingBlocked.Title"));
		lockable.getLockInUseNotifier().showText(
				UIResourceBundle.getString("LockingBlocked.Text",inUseException
						.getBlockingLockInfo().getUserString(),df.format(inUseException
						.getBlockingLockInfo().getValidUntil())));
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void lockInUseNotification(LockingException cause, PessimisticLock lock,
			Lockable lockable) throws LockingException
	{
		// hybrid (1) - enabled state
		final EnabledState state = LockFactory.getLockUIState(lock,this.lockDisplay);
		state.disableAllComponents();
		
		// hybrid (2) - notification dialog
		lockable.getLockInUseNotifier()
				.setTitle(UIResourceBundle.getString("LockingBlocked.Title"));
		lockable.getLockInUseNotifier().showText(cause.getLocalizedMessage());
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void lockInUseNotification(PessimisticLock lock, Lockable lockable)
			throws LockingException
	{
		// hybrid (1) - enabled state
		final EnabledState state = LockFactory.getLockUIState(lock,this.lockDisplay);
		state.disableAllComponents();
		
		// hybrid (2) - notification dialog
		lockable.getLockInUseNotifier()
				.setTitle(UIResourceBundle.getString("LockingBlocked.Title"));
		lockable.getLockInUseNotifier().setTitle(UIResourceBundle.getString("LockingBlocked.TextPlain"));
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void lockInUseNotificationFinished(PessimisticLock lock, Lockable lockable)
	{
		// hybrid (1) - enabled state
		final EnabledState state = LockFactory.getLockUIState(lock,this.lockDisplay);
		state.restoreState();
	}
}
