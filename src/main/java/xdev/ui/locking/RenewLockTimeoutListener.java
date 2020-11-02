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


import xdev.db.locking.LockTimeoutEvent;
import xdev.db.locking.LockTimeoutListener;
import xdev.db.locking.PessimisticLock;
import xdev.lang.XDEV;
import xdev.lang.cmd.OpenWindow;


/**
 * Observes a particular lock regarding its notification threshold.
 * <p>
 * If the notification threshold is hit a prompt which enables to regenerate the
 * concerned lock will be shown.
 * <p>
 * 
 * @author XDEV Software jwill
 * @since 4.0
 * @see Lockable#getRenewLockWindow()
 * @see Lockable#getNotificationThreshold()
 */
public class RenewLockTimeoutListener implements LockTimeoutListener
{
	private final Lockable			lockable;
	private final PessimisticLock	lock;
	
	
	/**
	 * Initializes a lock renew observer.
	 * 
	 * @param lockable
	 *            to respond to {@link Lockable#getNotificationThreshold()}
	 * @param renewableLock
	 *            the {@link PessimisticLock} to regenerate.
	 */
	public RenewLockTimeoutListener(final Lockable lockable, final PessimisticLock renewableLock)
	{
		this.lockable = lockable;
		this.lock = renewableLock;
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void timeoutImminent(LockTimeoutEvent e)
	{
		XDEV.OpenWindow(new OpenWindow()
		{
			@Override
			public void init()
			{
				AbstractLockRenewWindow wndRenew = lockable.getRenewLockWindow();
				wndRenew.setLock(lock);
				
				setXdevWindow(wndRenew);
				setContainerType(ContainerType.DIALOG);
				setModal(true);
			}
		});
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public long getNotificationThreshold()
	{
		return lockable.getNotificationThreshold();
	}
}
