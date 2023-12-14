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


import java.util.Date;

import xdev.vt.VirtualTable.VirtualTableRow;


/**
 * Implementation of {@link PessimisticLockInfo}.
 * 
 * @author XDEV Software (RHHF)
 * @since 4.0
 * 
 */
public class DefaultPessimisticLockInfo implements PessimisticLockInfo
{
	
	private final String	tableName;
	
	private final Date		validUntil;
	
	private final String	userString;
	private final long		userId;
	private final String	rowIdentifier;
	
	
	public DefaultPessimisticLockInfo(String tableName, Date validUntil, String userString,
			long userId, String rowIdentifier)
	{
		this.tableName = tableName;
		this.validUntil = validUntil;
		this.userString = userString;
		this.userId = userId;
		this.rowIdentifier = rowIdentifier;
	}
	
	
	public DefaultPessimisticLockInfo(final String tableName, final Date validUntil,
			final VirtualTableRow row)
	{
		this.tableName = tableName;
		this.validUntil = validUntil;
		this.userString = row.get(PessimisticLockTable.userString);
		this.userId = row.get(PessimisticLockTable.userID);
		this.rowIdentifier = row.get(PessimisticLockTable.tableRowIdentifier);
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getRowIdentifier()
	{
		return this.rowIdentifier;
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getTableName()
	{
		
		return this.tableName;
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public long getUserId()
	{
		return this.userId;
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getUserString()
	{
		return this.userString;
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Date getValidUntil()
	{
		return this.validUntil;
	}
}
