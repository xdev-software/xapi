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
package xdev.vt;


import xdev.vt.VirtualTable.VirtualTableRow;


/**
 * Thrown while performing a reload, if a row was deleted in the underlying
 * database.
 * 
 * @see #getVirtualTableRow()
 * 
 * @author XDEV Software Corp.
 */
public class VirtualTableRowDeletedInDbException extends VirtualTableException
{
	private VirtualTableRow	virtualTableRow;
	

	/**
	 * Initializes a new {@link VirtualTableRowDeletedInDbException}.
	 */
	public VirtualTableRowDeletedInDbException()
	{
		super();
	}
	

	/**
	 * Initializes a new {@link VirtualTableRowDeletedInDbException}.
	 * 
	 * @param virtualTableRow
	 *            the exception's source/cause
	 * @since 3.1
	 */
	public VirtualTableRowDeletedInDbException(VirtualTableRow virtualTableRow)
	{
		super(virtualTableRow.getVirtualTable());
		
		this.virtualTableRow = virtualTableRow;
	}
	

	/**
	 * Returns the source resp cause of this exception.
	 * 
	 * @return the {@link VirtualTableRow} associated with this exception, may
	 *         be <code>null</code>.
	 * @since 3.1
	 */
	public VirtualTableRow getVirtualTableRow()
	{
		return virtualTableRow;
	}
}
