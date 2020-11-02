package xdev.db.locking;

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


import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import xdev.db.DBConnection;
import xdev.db.DBException;
import xdev.db.DBUtils;
import xdev.db.Transaction;
import xdev.db.jdbc.JDBCDataSource;
import xdev.db.sql.DELETE;
import xdev.db.sql.INSERT;
import xdev.db.sql.SELECT;
import xdev.db.sql.UPDATE;
import xdev.vt.VirtualTable;
import xdev.vt.VirtualTable.VirtualTableRow;

import com.xdev.jadoth.sqlengine.SQL;


/**
 * Implementation of {@link PessimisticLock} with the enhancement of the
 * optimistic locking approach.
 * <p>
 * Optimistic locking allows multiple concurrent users access to the database
 * whilst the system keeps a copy of the initial-read made by each user.
 * <p>
 * When a user wants to update a record, the application determines whether
 * another user has changed the record since it was last read. The application
 * does this by comparing the initial-read held in memory to the database record
 * to verify any changes made to the record.
 * <p>
 * Any discrepancies between the initial-read and the database record violates
 * concurrency rules and hence causes the system to disregard any update
 * request.
 * 
 * @author XDEV Software (RHHF)
 * @author XDEV Software jwill
 * @since 4.0
 */
public class HybridVirtualTableLock implements PessimisticLock
{
	/**
	 * Default time to hold a specific lock.
	 */
	public static final long						DEFAULT_TIMEOUT					= 60000;
	
	/**
	 * Default threshold before lock duration is going to end, used to notify
	 * concerned observers.
	 */
	public static final long						DEFAULT_NOTIFICATION_THRESHOLD	= 0;
	
	private final String							userString;
	private final long								userId;
	private final String							rowIdentifier;
	private final String							tableName;
	private Date									lastValidUntil;
	
	private long									timeout							= DEFAULT_TIMEOUT;
	
	private final PessimisticLockTable				lockVt;
	
	protected Map<LockTimeoutListener, TimerTask>	lockTimeoutListenerMap			= null;
	
	protected List<LockTimeoutChangeListener>		lockListenerList				= new ArrayList<LockTimeoutChangeListener>();
	
	private final JDBCDataSource					dataSource;
	private Timer									timer;
	
	private final Map<LockTimeoutListener, Timer>	listenerTimerMap				= new HashMap<LockTimeoutListener, Timer>();
	private LockConstructionState					constructionState;
	
	private static final String						TIMER_NAME_PREFIX				= "db-lock-timer: ";
	
	
	public HybridVirtualTableLock(JDBCDataSource dataSource, VirtualTableRow row)
	{
		this(row,new UserNameIdentifier(ClientSession.getUserName()),ClientSession.getUserName(),
				new DefaultVirtualTableRowIdentifier(row),dataSource);
	}
	
	
	public HybridVirtualTableLock(JDBCDataSource dataSource, VirtualTableRow row, long timeout)
	{
		this(row,new UserNameIdentifier(ClientSession.getUserName()),ClientSession.getUserName(),
				new DefaultVirtualTableRowIdentifier(row),timeout,dataSource);
	}
	
	
	public HybridVirtualTableLock(final VirtualTableRow row, final UserIdentifier userIdentifier,
			final String userString, final RowIdentifier rowIdentifier, JDBCDataSource dataSource)
	{
		this(row,userIdentifier,userString,rowIdentifier,DEFAULT_TIMEOUT,dataSource);
		
	}
	
	
	public HybridVirtualTableLock(final VirtualTableRow row, final UserIdentifier userIdentifier,
			final String userString, final RowIdentifier rowIdentifier, final long timeout,
			JDBCDataSource dataSource)
	{
		this.rowIdentifier = rowIdentifier.getRowIdentifierString();
		this.tableName = row.getVirtualTable().getDatabaseAlias();
		this.userId = userIdentifier.getUserId();
		this.userString = userString;
		this.timeout = timeout;
		this.lockVt = new PessimisticLockTable(dataSource);
		this.dataSource = dataSource;
		
		lockTimeoutListenerMap = new HashMap<LockTimeoutListener, TimerTask>();
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void renewLock(final long additionalTime) throws LockingException,
			RowAlreadyLockedException
	{
		Transaction lockTransaction = null;
		try
		{
			lockTransaction = new Transaction()
			{
				@Override
				protected void write(DBConnection<?> connection) throws DBException
				{
					if(isEditable())
					{
						updateLockDuration(additionalTime);
						updateTimeoutListenerNotificationThreshold();
						notifyTimeOutListeners(getRemainingTime(),lastValidUntil.getTime(),false);
					}
				}
			};
			lockTransaction.execute();
		}
		catch(DBException e)
		{
			throw new LockingException("Could not obtain lock for table: " + this.tableName
					+ " row: " + this.rowIdentifier,e);
		}
	}
	
	
	private void updateTimeoutListenerNotificationThreshold() throws LockingException
	{
		LockTimeoutListener[] lockTimeoutListeners = lockTimeoutListenerMap.keySet().toArray(
				new LockTimeoutListener[lockTimeoutListenerMap.size()]);
		for(LockTimeoutListener listener : lockTimeoutListeners)
		{
			TimerTask task = createTask(listener);
			this.timerNotificationThresholdSchedule(listener,task);
		}
	}
	
	
	private void timerNotificationThresholdSchedule(LockTimeoutListener listener, TimerTask task)
			throws LockingException
	{
		if(this.timer != null)
		{
			long remainingTime = getRemainingTime();
			
			if(remainingTime > listener.getNotificationThreshold())
			{
				this.timer.schedule(task,remainingTime - listener.getNotificationThreshold());
			}
			else
			{
				// XXX exception type?
				throw new LockingException(
						"Notification threshold should be lower than locks remaining time!");
			}
		}
	}
	
	
	private PessimisticLockInfo getBlockingLockInfo(VirtualTableRow record)
	{
		final Date serverTime = record.get(PessimisticLockTable.serverTime);
		
		final Date validUntilServerTime = record.get(PessimisticLockTable.validUntil);
		
		final long millisToTimeout = validUntilServerTime.getTime() - serverTime.getTime();
		
		final Date validUntil = new Date(System.currentTimeMillis() + millisToTimeout);
		final PessimisticLockInfo blockingLock = new DefaultPessimisticLockInfo(this.tableName,
				validUntil,record);
		
		return blockingLock;
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public LockConstructionState getLock() throws LockingException, RowAlreadyLockedException
	{
		this.clearRemainingTimeOutDependencies();
		this.timer = new Timer(this.getTimerName());
		Transaction lockTransaction = null;
		try
		{
			lockTransaction = new Transaction()
			{
				@Override
				protected void write(DBConnection<?> connection) throws DBException
				{
					constructionState = manageLocking();
				}
			};
			lockTransaction.execute();
		}
		catch(DBException e)
		{
			throw new LockingException("Could not obtain lock for table: " + this.tableName
					+ " row: " + this.rowIdentifier,e);
		}
		return constructionState;
	}
	
	
	private VirtualTableRow getFirstBlockingLock() throws LockingException
	{
		Date serverTime = null;
		
		SELECT select = new SELECT();
		select.columns(SQL.STAR);
		select.FROM(this.lockVt);
		select.WHERE(PessimisticLockTable.tableName.eq("?"))
				.AND(PessimisticLockTable.tableRowIdentifier.eq("?"))
				.AND(PessimisticLockTable.validUntil.gte("?"))
				.AND(PessimisticLockTable.userID.ne("?"));
		
		try
		{
			serverTime = VirtualTableLockingUtils.getServerTime(this.dataSource);
			Object[] param = new Object[4];
			param[0] = this.tableName;
			param[1] = this.rowIdentifier;
			param[2] = serverTime;
			param[3] = this.userId;
			this.lockVt.clearData();
			DBUtils.query(this.lockVt,select,param);
		}
		catch(DBException e)
		{
			throw new LockingException("Could not receive lock for table: " + this.tableName
					+ " row: " + this.rowIdentifier,e);
		}
		
		if(this.lockVt.getRowCount() > 0)
		{
			VirtualTableRow blockRow = this.lockVt.getRow(0);
			blockRow.set(PessimisticLockTable.serverTime.getName(),serverTime);
			this.lockVt.clearData();
			return blockRow;
		}
		return null;
	}
	
	
	private VirtualTableRow getPotentialExistingLockRow() throws LockingException
	{
		SELECT select = new SELECT();
		select.columns(SQL.STAR);
		select.FROM(this.lockVt);
		select.WHERE(PessimisticLockTable.tableName.eq("?"))
				.AND(PessimisticLockTable.tableRowIdentifier.eq("?"))
				.AND(PessimisticLockTable.userID.eq("?"));
		
		try
		{
			Object[] param = new Object[3];
			param[0] = this.tableName;
			param[1] = this.rowIdentifier;
			param[2] = this.userId;
			this.lockVt.clearData();
			DBUtils.query(this.lockVt,select,param);
		}
		catch(DBException e)
		{
			throw new LockingException("Could get lock for table: " + this.tableName + " row: "
					+ this.rowIdentifier,e);
		}
		
		if(this.lockVt.getRowCount() > 0)
		{
			VirtualTableRow existingRow = this.lockVt.getRow(0);
			this.lockVt.clearData();
			return existingRow;
		}
		return null;
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void removeLock() throws LockingException
	{
		Transaction lockTransaction = null;
		try
		{
			lockTransaction = new Transaction()
			{
				@Override
				protected void write(DBConnection<?> connection) throws DBException
				{
					if(isUserValid())
					{
						removeLockEntry();
					}
				}
			};
			lockTransaction.execute();
		}
		catch(DBException e)
		{
			throw new LockingException("Could not obtain lock for table: " + this.tableName
					+ " row: " + this.rowIdentifier,e);
		}
	}
	
	
	protected void removeLockEntry() throws LockingException
	{
		DELETE delete = new DELETE();
		delete.FROM(this.lockVt);
		delete.WHERE(PessimisticLockTable.tableName.eq("?"))
				.AND(PessimisticLockTable.tableRowIdentifier.eq("?"))
				.AND(PessimisticLockTable.userID.eq("?"));
		
		Object[] paramsDelete = new Object[3];
		paramsDelete[0] = this.tableName;
		paramsDelete[1] = this.rowIdentifier;
		paramsDelete[2] = this.userId;
		
		try
		{
			this.clearRemainingTimeOutDependencies();
			DBUtils.write(delete,paramsDelete);
		}
		catch(DBException e)
		{
			throw new LockingException("Could not remove lock for table: " + this.tableName
					+ " row: " + this.rowIdentifier,e);
		}
	}
	
	
	/**
	 * Returns the time for how long the lock is still valid.
	 * 
	 * @return server valid until time
	 * @throws LockingException
	 *             indicator for connection problems.
	 */
	public Date getValidUntilTime() throws LockingException
	{
		this.lockVt.clearData();
		SELECT select = new SELECT();
		select.columns(PessimisticLockTable.validUntil);
		select.FROM(this.lockVt);
		select.WHERE(PessimisticLockTable.tableName.eq("?"))
				.AND(PessimisticLockTable.tableRowIdentifier.eq("?"))
				.AND(PessimisticLockTable.userID.eq("?"));
		
		Object[] param = new Object[3];
		param[0] = this.tableName;
		param[1] = this.rowIdentifier;
		param[2] = this.userId;
		
		try
		{
			VirtualTable result = DBUtils.query(this.lockVt,select,param);
			if(result.getRowCount() > 0)
			{
				VirtualTableRow activeRow = result.getRow(0);
				Date validUntil = (Date)activeRow.get(PessimisticLockTable.validUntil.getName());
				return validUntil;
			}
		}
		catch(DBException e)
		{
			throw new LockingException("Lock for row " + this.rowIdentifier
					+ "is no longer valid for user " + this.userString);
		}
		
		return null;
	}
	
	
	private void clearRemainingTimeOutDependencies()
	{
		if(this.timer != null)
		{
			this.timer.cancel();
			this.notifyTimeOutListeners(0,this.getTimeout(),true);
		}
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public long getRemainingTime() throws LockingException
	{
		Date now;
		try
		{
			now = VirtualTableLockingUtils.getServerTime(this.dataSource);
		}
		catch(DBException e)
		{
			// XXX exception type? - like LockingDBException("msg",datasource
			// ...);
			throw new LockingException("Could not receive server time",e);
		}
		
		this.lastValidUntil = this.getValidUntilTime();
		if(lastValidUntil != null)
		{
			if(lastValidUntil.getTime() >= now.getTime())
			{
				return lastValidUntil.getTime() - now.getTime();
			}
		}
		return 0;
	}
	
	
	// optimistic locking
	protected boolean isEditable() throws RowAlreadyLockedException
	{
		final VirtualTableRow row = getFirstBlockingLock();
		if(row != null)
		{
			// not editable / false
			HybridVirtualTableLock.this.notifyTimeOutListeners(0,
					HybridVirtualTableLock.this.getTimeout(),true);
			
			final PessimisticLockInfo acquiringLockInfo = getLockInfo();
			throw new RowAlreadyLockedException(new RowAlreadyLockedException(
					"Could not acquire lock " + this.toString(),acquiringLockInfo,
					getBlockingLockInfo(row)));
		}
		return true;
	}
	
	
	protected LockConstructionState manageLocking() throws RowAlreadyLockedException,
			LockingException
	{
		LockConstructionState optimisticLockConstructionCaseState = new OptimisticLockConstructionState();
		
		if(this.isEditable())
		{
			VirtualTableRow potentialExistingRow = this.getPotentialExistingLockRow();
			if(potentialExistingRow != null)
			{
				Date serverTime = null;
				try
				{
					serverTime = VirtualTableLockingUtils.getServerTime(this.dataSource);
				}
				catch(DBException e)
				{
					throw new LockingException("Could not receive lock for table: "
							+ this.tableName + " row: " + this.rowIdentifier,e);
				}
				if(serverTime.getTime() > potentialExistingRow.get(PessimisticLockTable.validUntil)
						.getTime())
				{
					this.renewLock(this.getTimeout());
					optimisticLockConstructionCaseState.setRegenerated(true);
					return optimisticLockConstructionCaseState;
				}
			}
			else
			{
				this.createLockEntry();
				optimisticLockConstructionCaseState.setInitial(true);
				return optimisticLockConstructionCaseState;
			}
		}
		return optimisticLockConstructionCaseState;
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isUserValid() throws LockingException
	{
		if(this.getFirstBlockingLock() != null)
		{
			return false;
		}
		return true;
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void addLockTimeoutListener(final LockTimeoutListener listener) throws LockingException
	{
		if(this.timer != null)
		{
			final TimerTask task = createTask(listener);
			this.timerNotificationThresholdSchedule(listener,task);
		}
	}
	
	
	private TimerTask createTask(final LockTimeoutListener listener)
	{
		final TimerTask task = new TimerTask()
		{
			@Override
			public void run()
			{
				listener.timeoutImminent(new LockTimeoutEvent(HybridVirtualTableLock.this));
				HybridVirtualTableLock.this.listenerTimerMap.remove(listener);
			}
		};
		this.lockTimeoutListenerMap.put(listener,task);
		return task;
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void removeLockTimeoutListener(final LockTimeoutListener listener)
	{
		final TimerTask task = this.lockTimeoutListenerMap.get(listener);
		if(task != null)
		{
			task.cancel();
			this.lockTimeoutListenerMap.remove(listener);
		}
		
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Set<LockTimeoutListener> getLockTimeoutListener()
	{
		return new HashSet<LockTimeoutListener>(this.lockTimeoutListenerMap.keySet());
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public long getTimeout()
	{
		return this.timeout;
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public PessimisticLockInfo getLockInfo()
	{
		return new DefaultPessimisticLockInfo(this.tableName,new Date(this.getRemainingTime()
				+ new Date().getTime()),this.userString,this.userId,this.rowIdentifier);
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString()
	{
		return this.tableName + "[" + this.rowIdentifier + "]";
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void addLockTimeoutPropertyChangeListener(LockTimeoutChangeListener listener)
	{
		this.lockListenerList.add(listener);
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<LockTimeoutChangeListener> getLockListener()
	{
		return new ArrayList<LockTimeoutChangeListener>(this.lockListenerList);
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void removeLockTimeoutPropertyChangeListener(LockTimeoutChangeListener listener)
	{
		this.lockListenerList.remove(listener);
	}
	
	
	/**
	 * 
	 * @param timeOut
	 */
	protected void setTimeOut(final long timeOut)
	{
		// final long oldValue = this.timeout;
		this.timeout = timeOut;
		// this.notifyTimeOutListeners(timeOut,oldValue,false);
	}
	
	
	protected void notifyTimeOutListeners(final long newValue, final long oldValue, boolean canceled)
	{
		final LockTimeOutChangeEvent changeEvent = new LockTimeOutChangeEvent(this,oldValue,
				newValue,canceled);
		
		for(LockTimeoutChangeListener listener : this.lockListenerList)
		{
			listener.lockTimeOutChange(changeEvent);
		}
	}
	
	
	protected void setTimeOut(final long timeOut, String timerName)
	{
		final long oldValue = this.timeout;
		this.timeout = timeOut;
		final LockTimeOutChangeEvent changeEvent = new LockTimeOutChangeEvent(this,oldValue,timeOut);
		
		for(LockTimeoutChangeListener listener : this.lockListenerList)
		{
			listener.lockTimeOutChange(changeEvent);
		}
		
	}
	
	
	private String getTimerName()
	{
		return TIMER_NAME_PREFIX + this.toString();
	}
	
	
	protected void updateLockDuration(long additionalTime) throws LockingException
	{
		Date updatedDate = new Date(this.calculateUpdatedRemainingTime(additionalTime));
		UPDATE update = new UPDATE(this.lockVt);
		update.SET(PessimisticLockTable.validUntil,"?");
		update.WHERE(PessimisticLockTable.tableName.eq("?"))
				.AND(PessimisticLockTable.tableRowIdentifier.eq("?"))
				.AND(PessimisticLockTable.userID.eq("?"));
		
		Object[] paramsInsert = new Object[4];
		paramsInsert[0] = updatedDate;
		paramsInsert[1] = this.tableName;
		paramsInsert[2] = this.rowIdentifier;
		paramsInsert[3] = this.userId;
		
		try
		{
			DBUtils.write(update,true,paramsInsert);
		}
		catch(DBException e)
		{
			throw new LockingException("Could not update time for lock corresponding to table: "
					+ this.tableName + " row: " + this.rowIdentifier,e);
		}
	}
	
	
	private long calculateUpdatedRemainingTime(long additionalTime) throws LockingException
	{
		long now;
		try
		{
			now = VirtualTableLockingUtils.getServerTime(this.dataSource).getTime();
		}
		catch(DBException e)
		{
			throw new LockingException(
					"Could not calculate remaining lock time for lock corresponding to: "
							+ this.tableName + " row: " + this.rowIdentifier,e);
		}
		long remainingTime = this.getRemainingTime();
		
		return now + remainingTime + additionalTime;
	}
	
	
	protected void createLockEntry() throws LockingException
	{
		try
		{
			Date serverTime = VirtualTableLockingUtils.getServerTime(this.dataSource);
			Date validUntilServerTime = new Date(serverTime.getTime() + this.getTimeout());
			
			INSERT insert = new INSERT();
			insert.INTO(this.lockVt);
			insert.columns(PessimisticLockTable.tableName,PessimisticLockTable.tableRowIdentifier,
					PessimisticLockTable.validUntil,PessimisticLockTable.userString,
					PessimisticLockTable.userID);
			insert.VALUES("?","?","?","?","?");
			
			Object[] paramsInsert = new Object[5];
			paramsInsert[0] = this.tableName;
			paramsInsert[1] = this.rowIdentifier;
			paramsInsert[2] = validUntilServerTime;
			paramsInsert[3] = this.userString;
			paramsInsert[4] = this.userId;
			
			DBUtils.write(insert,true,paramsInsert);
			
		}
		catch(DBException e)
		{
			throw new LockingException("Could not obtain lock for table: " + this.tableName
					+ " row: " + this.rowIdentifier,e);
		}
	}
}
