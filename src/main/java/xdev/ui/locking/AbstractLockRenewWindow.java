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


import xdev.db.locking.PessimisticLock;
import xdev.ui.XdevWindow;


/**
 * UI implementation of {@link LockRenovator} to provide concrete duration
 * values .
 * 
 * @author XDEV Software jwill
 * @since 4.0
 */
public abstract class AbstractLockRenewWindow extends XdevWindow implements LockRenovator
{
	/**
	 * Sets the lock to renovate.
	 * 
	 * @param lock
	 *            the lock to renovate.
	 */
	public abstract void setLock(PessimisticLock lock);
	
	
	/**
	 * Returns the lock to renovate.
	 * 
	 * @return the lock to renovate.
	 */
	public abstract PessimisticLock getLock();
}
