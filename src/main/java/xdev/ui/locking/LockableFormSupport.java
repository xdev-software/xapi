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


import java.awt.Container;

import xdev.db.DBException;
import xdev.db.locking.LockFactory;
import xdev.ui.EnabledState;
import xdev.ui.Formular;
import xdev.ui.FormularSupport;
import xdev.ui.XdevFormular;
import xdev.vt.VirtualTable;
import xdev.vt.VirtualTable.VirtualTableRow;
import xdev.vt.VirtualTableException;


/**
 * Support functionality provider for lockable forms.
 * 
 * @author XDEV Software jwill
 * 
 * @param <T>
 *            a concrete lockable implementation for example
 *            {@link XdevFormular}
 * @since 4.0
 * @see Lockable
 */
public class LockableFormSupport<T extends Container & Formular & Lockable> extends FormularSupport
{
	private final T			form;
	private EnabledState	formEnabledState;
	
	private LockSupport		lockSupport	= new PessimisticLockSupport();	;
	
	
	/**
	 * Returns this forms enabled state.
	 * 
	 * @return the form enabled state.
	 */
	public EnabledState getFormEnabledState()
	{
		return formEnabledState;
	}
	
	
	/**
	 * Enables the current {@link XdevFormular}.
	 */
	public void restoreFormEnabledState()
	{
		formEnabledState.restoreState();
		formEnabledState = new EnabledState(this.form);
	}
	
	
	/**
	 * Initializes this lock support provider with the given concerned lockable.
	 * 
	 * @param form
	 *            the lockable to provide functionality for.
	 */
	public LockableFormSupport(T form)
	{
		super(form);
		this.form = form;
	}
	
	
	private void initLocking(VirtualTableRow virtualTableRow)
	{
		// release lock from "last" concerned row
		this.lockSupport.releaseLock(this.getVirtualTableRow(),this.form);
		
		this.lockSupport.initiateLocking(virtualTableRow,this.form);
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setModel(int row, VirtualTable vt)
	{
		VirtualTableRow virtualTableRow = vt.getRow(row);
		this.initLocking(virtualTableRow);
		super.setModel(row,vt);
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setModel(VirtualTable vt)
	{
		VirtualTableRow virtualTableRow = vt.createRow();
		this.initLocking(virtualTableRow);
		super.setModel(vt);
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setModel(VirtualTableRow virtualTableRow)
	{
		this.initLocking(virtualTableRow);
		super.setModel(virtualTableRow);
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void delete(boolean synchronizeDB) throws VirtualTableException, DBException
	{
		if(this.lockSupport.isNotLocked(this.form.getVirtualTableRow(),this.form))
		{
			super.delete(synchronizeDB);
		}
		else
		{
			this.form.getLockNotificationStrategy().lockInUseNotification(
					LockFactory.getPessimisticLock(this.form.getVirtualTableRow(),
							this.form.getLockingTime()),this.form);
		}
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void insert(boolean synchronizeDB) throws VirtualTableException, DBException
	{
		super.insert(synchronizeDB);
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void update(boolean synchronizeDB) throws VirtualTableException, DBException
	{
		if(this.lockSupport.isNotLocked(this.form.getVirtualTableRow(),this.form))
		{
			super.update(synchronizeDB);
		}
		else
		{
			this.form.getLockNotificationStrategy().lockInUseNotification(
					LockFactory.getPessimisticLock(this.form.getVirtualTableRow(),
							this.form.getLockingTime()),this.form);
		}
		
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void save(boolean synchronizeDB) throws VirtualTableException, DBException
	{
		// locking is only concerned about already persistent records.
		if(!this.form.getVirtualTableRow().isNew())
		{
			if(this.lockSupport.isNotLocked(this.form.getVirtualTableRow(),this.form))
			{
				super.save(synchronizeDB);
				LockFactory.getPessimisticLock(this.form.getVirtualTableRow(),
						this.form.getLockingTime()).removeLock();
			}
			else
			{
				this.form.getLockNotificationStrategy().lockInUseNotification(
						LockFactory.getPessimisticLock(this.form.getVirtualTableRow(),
								this.form.getLockingTime()),this.form);
			}
		}
		else
		{
			super.save(synchronizeDB);
		}
	}
	
}
