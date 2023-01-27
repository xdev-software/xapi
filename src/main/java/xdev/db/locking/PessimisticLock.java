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


import java.util.List;
import java.util.Set;


/**
 * A user who reads a record, with the intention of updating it, places an
 * exclusive lock on the record to prevent other users from manipulating it.
 * This means no one else can manipulate that record until the user releases the
 * lock.
 * <p>
 * Provides functionality to create, renew, remove and observe a records lock.
 * </p>
 * Locks will be stored in a {@link PessimisticLockTable} which must be used to
 * lock records and has to be created before starting the locking process.
 * 
 * 
 * @author XDEV Software (RHHF)
 * @author XDEV Software jwill
 * 
 * @since 4.0
 * @see PessimisticLockTable
 */
public interface PessimisticLock
{
	/**
	 * Notifies about the lock construction state either regenerated or
	 * initially created.
	 * 
	 * @return the {@link LockConstructionState}.
	 * @throws LockingException
	 *             which provides exception information about failures within
	 *             the locking process.
	 * @throws RowAlreadyLockedException
	 *             which notifies about requesting a lock for a conflicted
	 *             record.
	 */
	public LockConstructionState getLock() throws LockingException, RowAlreadyLockedException;
	
	
	/**
	 * Removes the lock from its related record.
	 * 
	 * @throws LockingException
	 *             which provides exception information about failures within
	 *             the locking process.
	 */
	public void removeLock() throws LockingException;
	
	
	/**
	 * Regenerates an existing lock for a specific time.
	 * 
	 * @param timeout
	 *            the regeneration time.
	 * @throws LockingException
	 *             which provides exception information about failures within
	 *             the locking process.
	 * @throws RowAlreadyLockedException
	 *             which notifies about requesting a lock for a conflicted
	 *             record.
	 */
	public void renewLock(final long timeout) throws LockingException, RowAlreadyLockedException;
	
	
	/**
	 * Returns a property container which provides all relevant data about a
	 * specific lock.
	 * 
	 * @return returns all relevant data about a specific lock instance.
	 */
	public PessimisticLockInfo getLockInfo();
	
	
	/**
	 * Indicates whether this lock is still valid.
	 * 
	 * @return the existence state of the lock.
	 * @throws LockingException
	 *             which provides exception information about failures within
	 *             the locking process.
	 */
	public boolean isUserValid() throws LockingException;
	
	
	/**
	 * Returns the initial lock duration.
	 * 
	 * @return the initial lock duration.
	 */
	public long getTimeout();
	
	
	/**
	 * Returns the remaining lock duration.
	 * 
	 * @return the remaining lock duration.
	 * @throws LockingException
	 *             which provides exception information about failures within
	 *             the locking process.
	 */
	public long getRemainingTime() throws LockingException;
	
	
	/**
	 * Observes for the locks timeout and notification threshold.
	 * 
	 * @param listener
	 *            the lock duration observer.
	 * @throws LockingException
	 *             which provides exception information about failures within
	 *             the locking process.
	 */
	public void addLockTimeoutListener(final LockTimeoutListener listener) throws LockingException;
	
	
	/**
	 * Stops the observation of the locks timeout and notification threshold.
	 * 
	 * @param listener
	 *            the lock duration observer to remove.
	 */
	public void removeLockTimeoutListener(final LockTimeoutListener listener);
	
	
	/**
	 * Returns all lock time out observers.
	 * 
	 * @return the lock timeout observers.
	 */
	public Set<LockTimeoutListener> getLockTimeoutListener();
	
	
	/**
	 * Observes for a property change on the lock timeout property.
	 * 
	 * @param listener
	 *            the lock timeout property observer.
	 */
	public void addLockTimeoutPropertyChangeListener(final LockTimeoutChangeListener listener);
	
	
	/**
	 * Stops observing the lock timeout property.
	 * 
	 * @param listener
	 *            the observer to remove.
	 */
	public void removeLockTimeoutPropertyChangeListener(final LockTimeoutChangeListener listener);
	
	
	/**
	 * Returns all lock timeout property change observers.
	 * 
	 * @return the lock timeout property change observers.
	 */
	public List<LockTimeoutChangeListener> getLockListener();
}
