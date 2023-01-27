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
package xdev.db.locking;


import xdev.ui.locking.Lockable;


/**
 * Defines how the user should be informed about a specific lock state. For
 * example already locked records.
 * 
 * @author XDEV Software jwill
 * @since 4.0
 */
public interface LockNotificationStrategy
{
	/**
	 * Triggers the notification process for a specific lock state.
	 * 
	 * @param inUseException
	 *            the specific lock in use information.
	 * @param lock
	 *            the concerned lock
	 * @param lockable
	 *            the lockable which contains the notification monitor -
	 *            {@link Lockable#getLockInUseNotifier()}
	 * @throws LockingException
	 *             if the locking process fails.
	 */
	void lockInUseNotification(RowAlreadyLockedException inUseException, PessimisticLock lock,
			Lockable lockable) throws LockingException;
	
	
	/**
	 * Triggers the notification process for a specific lock state.
	 * 
	 * @param cause
	 *            the specific locking error information.
	 * @param lock
	 *            the concerned lock
	 * @param lockable
	 *            the lockable which contains the notification monitor -
	 *            {@link Lockable#getLockInUseNotifier()}
	 * @throws LockingException
	 *             if the locking process fails.
	 */
	void lockInUseNotification(LockingException cause, PessimisticLock lock, Lockable lockable)
			throws LockingException;
	
	
	/**
	 * Triggers the notification process for a specific lock state.
	 * 
	 * @param lock
	 *            the concerned lock
	 * @param lockable
	 *            the lockable which contains the notification monitor -
	 *            {@link Lockable#getLockInUseNotifier()}
	 * @throws LockingException
	 *             if the locking process fails.
	 */
	void lockInUseNotification(PessimisticLock lock, Lockable lockable) throws LockingException;
	
	
	/**
	 * Releases the notification process after a specific lock state.
	 * 
	 * @param lock
	 *            the concerned {@link PessimisticLock} instance.
	 * @param lockable
	 *            the lockable which contains the notification monitor -
	 *            {@link Lockable#getLockInUseNotifier()}
	 */
	void lockInUseNotificationFinished(PessimisticLock lock, Lockable lockable);
}
