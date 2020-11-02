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
import xdev.db.locking.LockingException;
import xdev.db.locking.PessimisticLock;
import xdev.db.locking.RowAlreadyLockedException;


/**
 * No notification {@link LockNotificationStrategy}.
 * 
 * @author XDEV Software jwill
 * @since 4.0
 */
public class NoNotificationLockInUseNotificationStrategy implements LockNotificationStrategy
{
	/**
	 * Only for integration purpose.
	 */
	public NoNotificationLockInUseNotificationStrategy()
	{
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void lockInUseNotification(RowAlreadyLockedException inUseException,
			PessimisticLock lock, Lockable lockable) throws LockingException
	{
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void lockInUseNotification(LockingException cause, PessimisticLock lock,
			Lockable lockable) throws LockingException
	{
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void lockInUseNotification(PessimisticLock lock, Lockable lockable)
			throws LockingException
	{
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void lockInUseNotificationFinished(PessimisticLock lock, Lockable lockable)
	{
	}
}
