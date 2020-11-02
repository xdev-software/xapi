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


import java.awt.Component;
import java.awt.event.FocusListener;
import java.util.HashMap;
import java.util.Map;

import xdev.db.locking.LockFactory;
import xdev.db.locking.PessimisticLock;
import xdev.db.locking.PessimisticLockStrategy;
import xdev.db.locking.RowAlreadyLockedException;
import xdev.db.locking.VirtualTableLockingUtils;
import xdev.vt.VirtualTable.VirtualTableRow;


/**
 * On-Focus implementation of {@link PessimisticLockStrategy} without any lock
 * renew notifications.
 * <p>
 * {@link PessimisticLock#getLock()} is called whenever the focus owner is going
 * to be a {@link Lockable} component.
 * 
 * @author XDEV Software jwill
 * @since 4.0
 */
public class NoRenewOnComponentFocusPessimisticLockStrategy implements PessimisticLockStrategy
{
	private Component							cpn;
	private Map<VirtualTableRow, FocusListener>	rowSpecificFocusListenerRegistry	= new HashMap<VirtualTableRow, FocusListener>();
	
	
	public NoRenewOnComponentFocusPessimisticLockStrategy(Component cpn)
	{
		this.cpn = cpn;
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void triggerLocking(final VirtualTableRow record, final Lockable lockable)
			throws RowAlreadyLockedException
	{
		
		FocusListener rowSpecificFocusListener = new NoRenewOnComponentFocusLockStrategyFocusListener(
				record,lockable,this.cpn);
		this.cpn.addFocusListener(rowSpecificFocusListener);
		this.rowSpecificFocusListenerRegistry.put(record,rowSpecificFocusListener);
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void releaseLocking(VirtualTableRow record, Lockable lockable)
	{
		this.cpn.removeFocusListener(this.rowSpecificFocusListenerRegistry.get(record));
		this.rowSpecificFocusListenerRegistry.remove(record);
		
		PessimisticLock lock = LockFactory.getPessimisticLock(record,lockable.getLockingTime());
		if(VirtualTableLockingUtils.isUserHoldsLock(lock))
		{
			lock.removeLock();
		}
	}
	
	
	/**
	 * Only for integration purpose!!!
	 */
	public NoRenewOnComponentFocusPessimisticLockStrategy()
	{
	}
}
