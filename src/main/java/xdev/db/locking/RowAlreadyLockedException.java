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


/**
 * RowAlreadyLockedException for situation when user want to get a lock but a
 * other user already has the lock.
 * 
 * @author XDEV Software (RHHF)
 * @since 4.0
 * 
 */
public class RowAlreadyLockedException extends LockingException
{
	private final PessimisticLockInfo	acquiringLockInfo;
	private final PessimisticLockInfo	blockingLockInfo;
	
	
	public RowAlreadyLockedException(final String message,
			final PessimisticLockInfo acquiringLockInfo, final PessimisticLockInfo blockingLockInfo)
	{
		super(message);
		this.acquiringLockInfo = acquiringLockInfo;
		this.blockingLockInfo = blockingLockInfo;
	}
	
	
	public RowAlreadyLockedException(Throwable cause, final PessimisticLockInfo acquiringLockInfo,
			final PessimisticLockInfo blockingLockInfo)
	{
		super(cause);
		this.acquiringLockInfo = acquiringLockInfo;
		this.blockingLockInfo = blockingLockInfo;
	}
	
	
	public RowAlreadyLockedException(String message, Throwable cause,
			final PessimisticLockInfo acquiringLockInfo, final PessimisticLockInfo blockingLockInfo)
	{
		super(message,cause);
		this.acquiringLockInfo = acquiringLockInfo;
		this.blockingLockInfo = blockingLockInfo;
	}
	
	
	public RowAlreadyLockedException(RowAlreadyLockedException cause)
	{
		super(cause.getMessage(),cause);
		this.acquiringLockInfo = cause.getAcquiringLockInfo();
		this.blockingLockInfo = cause.getBlockingLockInfo();
	}
	
	
	/**
	 * Get AcquiringLockInfo.
	 * 
	 * @return acquiringLockInfo
	 */
	public PessimisticLockInfo getAcquiringLockInfo()
	{
		return this.acquiringLockInfo;
	}
	
	
	/**
	 * Get BlockingLockInfo.
	 * 
	 * @return blockingLockInfo
	 */
	public PessimisticLockInfo getBlockingLockInfo()
	{
		return this.blockingLockInfo;
	}
	
}
