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


import java.text.ParseException;
import java.util.Date;

import com.xdev.jadoth.sqlengine.SQL;

import xdev.db.DBException;
import xdev.db.DBUtils;
import xdev.db.Result;
import xdev.db.jdbc.JDBCConnection;
import xdev.db.jdbc.JDBCDataSource;
import xdev.db.sql.DELETE;
import xdev.db.sql.SELECT;
import xdev.vt.VirtualTable;
import xdev.vt.VirtualTable.VirtualTableRow;


/**
 * Provides utility functionality regarding lock processing.
 * 
 * @since 4.0
 */
public class VirtualTableLockingUtils
{
	/**
	 * Examines for the optimistic lock case that another user possibly already
	 * holds a lock for a specific row.
	 * 
	 * @param lock
	 *            the lock to compare the details from.
	 * @return a potential blocking lock.
	 * @throws LockingException
	 *             which provides exception information about failures within
	 *             the locking process.
	 */
	public static VirtualTableRow getPotentialBlockingRow(PessimisticLock lock)
			throws LockingException
	{
		PessimisticLockInfo lockInfo = null;
		try
		{
			JDBCDataSource dataSource = (JDBCDataSource)DBUtils.getCurrentDataSource();
			VirtualTable lockVT = new PessimisticLockTable(dataSource);
			lockInfo = lock.getLockInfo();
			Date serverNowTime = getServerTime(dataSource);
			
			SELECT select = new SELECT();
			select.columns(SQL.STAR);
			select.FROM(lockVT);
			select.WHERE(PessimisticLockTable.tableName.eq("?"))
					.AND(PessimisticLockTable.tableRowIdentifier.eq("?"))
					.AND(PessimisticLockTable.validUntil.gte("?"))
					.AND(PessimisticLockTable.userID.eq("?"));
			
			Object[] param = new Object[4];
			param[0] = lockInfo.getTableName();
			param[1] = lockInfo.getRowIdentifier();
			param[2] = serverNowTime;
			param[3] = lockInfo.getUserId();
			lockVT.clearData();
			DBUtils.query(lockVT,select,param);
			
			if(lockVT.getRowCount() > 0)
			{
				VirtualTableRow blockRow = lockVT.getRow(0);
				blockRow.set(PessimisticLockTable.serverTime.getName(),serverNowTime);
				lockVT.clearData();
				return blockRow;
			}
			
		}
		catch(DBException e)
		{
			throw new LockingException("Could not read locks for table: " + lockInfo.getTableName()
					+ " row: " + lockInfo.getRowIdentifier(),e);
		}
		return null;
	}
	
	
	/**
	 * Indicates whether the given lock does exist for the current user.
	 * 
	 * @param lock
	 *            questionable lock.
	 * @return the indicator whether the lock does exist for the current user.
	 * @throws LockingException
	 *             which provides exception information about failures within
	 *             the locking process.
	 */
	public static boolean isUserHoldsLock(PessimisticLock lock) throws LockingException
	{
		PessimisticLockInfo lockInfo = null;
		try
		{
			JDBCDataSource dataSource = (JDBCDataSource)DBUtils.getCurrentDataSource();
			VirtualTable lockVT = new PessimisticLockTable(dataSource);
			lockInfo = lock.getLockInfo();
			
			SELECT select = new SELECT();
			select.columns(SQL.STAR);
			select.FROM(lockVT);
			select.WHERE(PessimisticLockTable.tableName.eq("?"))
					.AND(PessimisticLockTable.tableRowIdentifier.eq("?"))
					.AND(PessimisticLockTable.userID.eq("?"));
			
			Object[] param = new Object[3];
			param[0] = lockInfo.getTableName();
			param[1] = lockInfo.getRowIdentifier();
			param[2] = lockInfo.getUserId();
			
			Result result = DBUtils.query(select,param);
			
			while(result.next())
			{
				return true;
			}
		}
		catch(DBException e)
		{
			throw new LockingException("Could not read locks for table: " + lockInfo.getTableName()
					+ " row: " + lockInfo.getRowIdentifier(),e);
		}
		return false;
	}
	
	
	/**
	 * Indicates whether the given lock does exist.
	 * 
	 * @param lock
	 *            questionable lock.
	 * @return the indicator whether the lock does exist.
	 * @throws LockingException
	 *             which provides exception information about failures within
	 *             the locking process.
	 */
	public static boolean isLockExists(PessimisticLock lock) throws LockingException
	{
		PessimisticLockInfo lockInfo = null;
		try
		{
			JDBCDataSource dataSource = (JDBCDataSource)DBUtils.getCurrentDataSource();
			VirtualTable lockVT = new PessimisticLockTable(dataSource);
			lockInfo = lock.getLockInfo();
			
			SELECT select = new SELECT();
			select.columns(SQL.STAR);
			select.FROM(lockVT);
			select.WHERE(PessimisticLockTable.tableName.eq("?")).AND(
					PessimisticLockTable.tableRowIdentifier.eq("?"));
			
			Object[] param = new Object[2];
			param[0] = lockInfo.getTableName();
			param[1] = lockInfo.getRowIdentifier();
			
			Result result = DBUtils.query(select,param);
			
			while(result.next())
			{
				return true;
			}
		}
		catch(DBException e)
		{
			throw new LockingException("Could not read locks for table: " + lockInfo.getTableName()
					+ " row: " + lockInfo.getRowIdentifier(),e);
		}
		return false;
	}
	
	
	/**
	 * Removes all locks of the current user.
	 * 
	 * @throws LockingException
	 *             which provides exception information about failures within
	 *             the deletion process.
	 */
	public static void deleteUserLocks() throws LockingException
	{
		// does not cancel any lock timers.
		try
		{
			JDBCDataSource dataSource = (JDBCDataSource)DBUtils.getCurrentDataSource();
			VirtualTable lockVT = new PessimisticLockTable(dataSource);
			
			DELETE delete = new DELETE();
			delete.FROM(lockVT);
			delete.WHERE(PessimisticLockTable.userString.eq("?"));
			
			Object[] paramsDelete = new Object[1];
			paramsDelete[0] = ClientSession.getUserName();
			
			DBUtils.write(delete,paramsDelete);
		}
		catch(DBException e)
		{
			throw new LockingException("Could not remove lock for user: "
					+ ClientSession.getUserName(),e);
		}
	}
	
	
	/**
	 * Returns the current server time.
	 * 
	 * @param dataSource
	 *            the server
	 * @return the server time.
	 * @throws DBException
	 *             if the connection process failed.
	 * 
	 * @see PessimisticLockTable
	 */
	public static Date getServerTime(JDBCDataSource dataSource) throws DBException
	{
		JDBCConnection openConnectionImpl = dataSource.openConnectionImpl();
		Date serverTime = null;
		try
		{
			serverTime = openConnectionImpl.getServerTime();
		}
		catch(ParseException e)
		{
			// Returns null 
		}
		finally
		{
			openConnectionImpl.close();
		}
		
		return serverTime;
	}
}
