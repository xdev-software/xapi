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


import xdev.db.locking.LockNotificationStrategy;
import xdev.db.locking.LockTimeMonitor;
import xdev.db.locking.PessimisticLock;
import xdev.db.locking.PessimisticLockStrategy;


/**
 * A lock able data display which is able to trigger locking at specific
 * conditions through the applied {@link PessimisticLockStrategy}.
 * <p>
 * If the displayed record is locked, no other user is able to persist changes
 * done to it. Attempts to persist already locked records end in an aborting
 * notification.
 * </p>
 * <p>
 * The lock is also regenerative starting at a specific notification threshold
 * which in consequence ends in user controlled lock times.
 * </p>
 * 
 * @author XDEV Software jwill
 * @since 4.0
 * 
 * @see PessimisticLockStrategy
 * @see LockNotificationStrategy
 * @see LockTimeMonitor
 */
public interface Lockable
{
	public static final String	AUTO_LOCK	= "autoLock";
	
	
	/**
	 * Controls whether locking is activated or not.
	 * 
	 * @param autoLock
	 *            the auto lock indicator.
	 */
	public void setAutoLock(boolean autoLock);
	
	
	/**
	 * Indicates whether locking is activated or not.
	 * 
	 * @return the lock state.
	 */
	public boolean isAutoLock();
	
	
	/**
	 * Sets the initial lock duration.
	 * 
	 * @param lockingTime
	 *            the initial duration from start until notification threshold.
	 */
	public void setLockingTime(long lockingTime);
	
	
	/**
	 * Returns the initial duration from start until notification threshold.
	 * 
	 * @return locking time the initial lock duration.
	 */
	public long getLockingTime();
	
	
	/**
	 * Indicates the time from which the lock is able to be renewed.
	 * 
	 * @param notificationThreshold
	 *            the lock renew notification threshold.
	 */
	public void setNotificationThreshold(long notificationThreshold);
	
	
	/**
	 * Returns the time from which the lock is able to be renewed.
	 * 
	 * @return the time from which the lock is able to be renewed.
	 */
	public long getNotificationThreshold();
	
	
	/**
	 * Sets the component which is the interface between remaining lock duration
	 * and lock instance.
	 * 
	 * @param lockRenovator
	 *            interface the interface between remaining lock duration and
	 *            lock instance.
	 */
	public void setRenewLockWindow(AbstractLockRenewWindow lockRenovator);
	
	
	/**
	 * Returns the interface between remaining lock duration and lock instance.
	 * 
	 * @return renewDialog returns interface the interface between remaining
	 *         lock duration and lock instance.
	 */
	public AbstractLockRenewWindow getRenewLockWindow();
	
	
	/**
	 * Notifies about attempting to write a record which is already in
	 * use/locked.
	 * 
	 * @param lockAlreadyInUseIndicator
	 *            the lock already in use dialog.
	 */
	public void setLockInUseNotifier(LockAlreadyInUseIndicator lockAlreadyInUseIndicator);
	
	
	/**
	 * Returns the notifier which indicates whether a lock is already in use or
	 * not.
	 * 
	 * @return lockAlreadyInUseIndicator the lock in use notifier.
	 */
	public LockAlreadyInUseIndicator getLockInUseNotifier();
	
	
	/**
	 * Sets the {@link LockTimeMonitor}s which are capable of displaying the
	 * remaining time of a {@link PessimisticLock}.
	 * 
	 * @param countdownMonitors
	 *            the monitorCountdown to set
	 */
	public void setCountdownMonitors(LockTimeMonitor[] countdownMonitors);
	
	
	/**
	 * Returns the set {@link LockTimeMonitor}s which are capable of displaying
	 * the remaining time of a {@link PessimisticLock}.
	 * 
	 * @return the countdownMonitor the lock duration monitors.
	 */
	public LockTimeMonitor[] getCountdownMonitors();
	
	
	/**
	 * Sets the lock strategy which is capable of triggering a lock instance.
	 * 
	 * @param lockStrategy
	 *            the lock instance trigger.
	 */
	public void setLockStrategy(PessimisticLockStrategy lockStrategy);
	
	
	/**
	 * Returns the set lock strategy which is capable of triggering a lock
	 * instance.
	 * 
	 * @return either returns the default lock strategy
	 *         {@link OnLoadPessimisticLockStrategy} or the set strategy.
	 */
	public PessimisticLockStrategy getLockStrategy();
	
	
	/**
	 * Sets the {@link LockNotificationStrategy} which in charge of notifying if
	 * a specific lock state is active.
	 * 
	 * @param lockNotificationStrategy
	 *            the notification strategy {@link LockNotificationStrategy} to
	 *            set.
	 */
	public void setLockNotificationStrategy(LockNotificationStrategy lockNotificationStrategy);
	
	
	/**
	 * Returns the {@link LockNotificationStrategy} which in charge of notifying
	 * if a specific lock state is active.
	 * 
	 * @return either returns the default notification strategy
	 *         {@link DialogLockInUseNotificationStrategy} or the set
	 *         {@link LockNotificationStrategy}.
	 */
	public LockNotificationStrategy getLockNotificationStrategy();
}
