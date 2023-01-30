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


import java.awt.Container;
import java.util.HashMap;
import java.util.Map;

import xdev.db.DBException;
import xdev.db.DBUtils;
import xdev.db.jdbc.JDBCDataSource;
import xdev.ui.EnabledState;
import xdev.vt.VirtualTable.VirtualTableRow;


/**
 * Factory class which maps {@link PessimisticLock} instances to their concerned
 * records (VirtualTableRows).
 * 
 * @author XDEV Software jwill
 * @since 4.0
 */
public final class LockFactory
{
	private LockFactory()
	{
	}
	
	private static Map<PessimisticLock, EnabledState>		lockUIState	= new HashMap<PessimisticLock, EnabledState>();
	
	private static Map<VirtualTableRow, PessimisticLock>	lockMap		= new HashMap<VirtualTableRow, PessimisticLock>();
	
	
	/**
	 * Returns a {@link PessimisticLock} instance mapped to the given record or
	 * creates a lock instance if not already existing.
	 * 
	 * @param row
	 *            the concerned record.
	 * @param lockTime
	 *            the initial lock duration which is considered during lock
	 *            creation.
	 * @return returns the {@link PessimisticLock} instance mapped to the given
	 *         record.
	 * @throws LockingException
	 */
	public static PessimisticLock getPessimisticLock(VirtualTableRow row, long lockTime)
			throws LockingException
	{
		if(lockMap.containsKey(row))
		{
			PessimisticLock pessimisticLockRow = lockMap.get(row);
			return pessimisticLockRow;
		}
		
		PessimisticLock hybridVirtualTabelLock = null;
		try
		{
			hybridVirtualTabelLock = new HybridVirtualTableLock(
					(JDBCDataSource)DBUtils.getCurrentDataSource(),row,lockTime);
		}
		catch(DBException e)
		{
			throw new LockingException("Could not access lock datasource",e);
		}
		lockMap.put(row,hybridVirtualTabelLock);
		return hybridVirtualTabelLock;
	}
	
	
	/**
	 * Returns a {@link PessimisticLock} instance mapped to the given record or
	 * creates a lock instance if not already existing.
	 * 
	 * @param row
	 *            the concerned record.
	 * @return returns the {@link PessimisticLock} instance mapped to the given
	 *         record.
	 * @throws LockingException
	 */
	public static PessimisticLock getPessimisticLock(VirtualTableRow row) throws LockingException
	{
		if(lockMap.containsKey(row))
		{
			PessimisticLock pessimisticLockRow = lockMap.get(row);
			return pessimisticLockRow;
		}
		
		PessimisticLock hybridVirtualTabelLock = null;
		try
		{
			hybridVirtualTabelLock = new HybridVirtualTableLock(
					(JDBCDataSource)DBUtils.getCurrentDataSource(),row);
		}
		catch(DBException e)
		{
			throw new LockingException("Could not access lock datasource",e);
		}
		lockMap.put(row,hybridVirtualTabelLock);
		return hybridVirtualTabelLock;
	}
	
	
	/**
	 * Returns the current enabled state for the given record editor.
	 * 
	 * @param lock
	 *            the lock instance.
	 * @param uiRecordEditor
	 *            the lock record editor.
	 * @return returns the mapped enabled state for the given lock instance, if
	 *         no state already exists a new one will be mapped.
	 */
	public static EnabledState getLockUIState(PessimisticLock lock, Container uiRecordEditor)
	{
		if(lockUIState.containsKey(lock))
		{
			EnabledState state = lockUIState.get(lock);
			return state;
		}
		
		EnabledState state = new EnabledState(uiRecordEditor);
		lockUIState.put(lock,state);
		
		return state;
	}
}
