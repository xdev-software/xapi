package xdev.vt;

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


/**
 * This is a convenience class for using the interface
 * {@link VirtualTableListener}.
 * <p>
 * It provides empty implementations for all of {@link VirtualTableListener}s
 * methods. It allows subclasses of it to implement only those methods, that are
 * relevant to it.
 * </p>
 * 
 * @author XDEV Software Corp.
 */
public abstract class VirtualTableAdapter implements VirtualTableListener
{
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void virtualTableDataChanged(VirtualTableEvent event)
	{
	}
	

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void virtualTableRowDeleted(VirtualTableEvent event)
	{
	}
	

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void virtualTableRowInserted(VirtualTableEvent event)
	{
	}
	

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void virtualTableRowUpdated(VirtualTableEvent event)
	{
	}
	

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void virtualTableStructureChanged(VirtualTableEvent event)
	{
	}
}
