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


import xdev.db.locking.HybridVirtualTableLock;
import xdev.db.locking.LockNotificationStrategy;
import xdev.db.locking.LockTimeMonitor;
import xdev.db.locking.PessimisticLockStrategy;


//maybe swap interface against LockingManager
/**
 * Property container for lock properties.
 * 
 * 
 * @author XDEV Software jwill
 * @since 4.0
 */
public class LockPropertyInfo implements Lockable
{
	private boolean						autoLock				= false;
	private long						lockingTime				= HybridVirtualTableLock.DEFAULT_TIMEOUT;
	private long						notificationThreshold	= HybridVirtualTableLock.DEFAULT_NOTIFICATION_THRESHOLD;
	private LockTimeMonitor[]			countdownMonitors		= new LockTimeMonitor[0];
	
	// default strategies
	private PessimisticLockStrategy		lockStrategy			= new OnLoadPessimisticLockStrategy();
	private LockNotificationStrategy	notificationStrategy	= new DialogLockInUseNotificationStrategy();
	private LockAlreadyInUseIndicator	lockInUseNotifier		= new UIUtilsLockAlreadyInUseIndicator();
	private AbstractLockRenewWindow		lockRenovator			= new RenewLockWindow();
	
	
	public LockPropertyInfo()
	{
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setAutoLock(boolean b)
	{
		this.autoLock = b;
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isAutoLock()
	{
		return this.autoLock;
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setLockingTime(long lockingTime)
	{
		this.lockingTime = lockingTime;
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public long getLockingTime()
	{
		return this.lockingTime;
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setNotificationThreshold(long notificationThreshold)
	{
		this.notificationThreshold = notificationThreshold;
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public long getNotificationThreshold()
	{
		return this.notificationThreshold;
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setRenewLockWindow(AbstractLockRenewWindow renovator)
	{
		this.lockRenovator = renovator;
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public AbstractLockRenewWindow getRenewLockWindow()
	{
		return this.lockRenovator;
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setLockInUseNotifier(LockAlreadyInUseIndicator lockInUseNotifier)
	{
		this.lockInUseNotifier = lockInUseNotifier;
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public LockAlreadyInUseIndicator getLockInUseNotifier()
	{
		return this.lockInUseNotifier;
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setCountdownMonitors(LockTimeMonitor[] countdownMonitors)
	{
		this.countdownMonitors = countdownMonitors;
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public LockTimeMonitor[] getCountdownMonitors()
	{
		return this.countdownMonitors;
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setLockStrategy(PessimisticLockStrategy lockStrategy)
	{
		this.lockStrategy = lockStrategy;
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public PessimisticLockStrategy getLockStrategy()
	{
		return this.lockStrategy;
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setLockNotificationStrategy(LockNotificationStrategy notificationStrategy)
	{
		this.notificationStrategy = notificationStrategy;
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public LockNotificationStrategy getLockNotificationStrategy()
	{
		return this.notificationStrategy;
	}
}
