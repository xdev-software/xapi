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


import xdev.vt.VirtualTable;
import xdev.vt.VirtualTable.VirtualTableRow;
import xdev.vt.VirtualTableColumn;


/**
 * {@link DefaultVirtualTableRowIdentifier} provides an unique identifier for
 * each {@link VirtualTableRow} based on the primary key info of the
 * {@link VirtualTable}.
 * 
 * 
 * @author XDEV Software (FHAE)
 * @author XDEV Software (RHHF)
 * @since 4.0
 */
public class DefaultVirtualTableRowIdentifier implements RowIdentifier
{
	
	private String			DELIMITER			= "-";
	private String			rowIdentifierString	= null;
	private VirtualTableRow	internalRow			= null;
	
	
	/**
	 * Instantiates a new row identifier.
	 * 
	 * @param the
	 *            {@link VirtualTableRow}
	 */
	public DefaultVirtualTableRowIdentifier(VirtualTableRow row)
	{
		this.internalRow = row;
	}
	
	
	private String buildIdentifier(VirtualTableRow row)
	{
		final StringBuilder sb = new StringBuilder();
		final VirtualTableColumn<?>[] pks = this.internalRow.getVirtualTable()
				.getPrimaryKeyColumns();
		
		for(int i = 0; i < pks.length; i++)
		{
			if(i != 0)
			{
				sb.append(DELIMITER);
			}
			Object pkValue = row.get(row.getVirtualTable().getColumnIndex(pks[i]));
			
			sb.append(pkValue);
		}
		
		return sb.toString();
	}
	
	
	/**
	 * Returns the identifier of the <code>VirtualTableRow</code> as
	 * <code>String</code>.
	 * 
	 * @return the unique identifier of the row.
	 */
	public String getRowIdentifierString()
	{
		if(this.rowIdentifierString == null)
		{
			this.rowIdentifierString = buildIdentifier(internalRow);
		}
		return this.rowIdentifierString;
	}
	
}
