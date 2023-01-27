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


import java.util.EventObject;

import xdev.vt.VirtualTable.VirtualTableRow;


/**
 * This class stores information on events occuring on {@link VirtualTable}s. It
 * is used to carry information from the source of the event to notified
 * listeners.
 * 
 * @author XDEV Software Corp.
 * @see VirtualTable
 * @see VirtualTableListener
 */
public class VirtualTableEvent extends EventObject
{
	/**
	 * An enumeration of types of a {@link VirtualTableEvent}.
	 * 
	 * @author XDEV Software Corp.
	 */
	public static enum Type
	{
		/**
		 * A row in the {@link VirtualTable} has been updated.
		 */
		UPDATED,

		/**
		 * A row in the {@link VirtualTable} has been removed.
		 */
		REMOVED,

		/**
		 * A row in the {@link VirtualTable} has been inserted.
		 */
		INSERTED,

		/**
		 * The data in the {@link VirtualTable} has changed.
		 */
		CHANGED
	}
	
	private Type			type;
	private VirtualTableRow	row;
	private int				rowIndex;
	private int				columnIndex;
	

	/**
	 * Initializes a new {@link VirtualTableEvent}.
	 * 
	 * @param vt
	 *            the {@link VirtualTable} the event is created for.
	 */
	public VirtualTableEvent(VirtualTable vt)
	{
		this(vt,Type.CHANGED,null,-1);
	}
	

	/**
	 * Initializes a new {@link VirtualTableEvent}.
	 * 
	 * @param vt
	 *            the {@link VirtualTable} the event is created for.
	 * @param type
	 *            the {@link Type} of the event
	 * @param row
	 *            the {@link VirtualTableRow} that caused the event
	 * @param rowIndex
	 *            the index of the row that caused the event
	 */
	public VirtualTableEvent(VirtualTable vt, Type type, VirtualTableRow row, int rowIndex)
	{
		this(vt,type,row,rowIndex,-1);
	}
	

	/**
	 * Initializes a new {@link VirtualTableEvent}.
	 * 
	 * @param vt
	 *            the {@link VirtualTable} the event is created for.
	 * @param type
	 *            the {@link Type} of the event
	 * @param row
	 *            the {@link VirtualTableRow} that caused the event
	 * @param rowIndex
	 *            the index of the row that caused the event
	 * @param columnIndex
	 *            the index of the column that caused the event
	 * @since 3.2
	 */
	public VirtualTableEvent(VirtualTable vt, Type type, VirtualTableRow row, int rowIndex,
			int columnIndex)
	{
		super(vt);
		
		this.type = type;
		this.row = row;
		this.rowIndex = rowIndex;
		this.columnIndex = columnIndex;
	}
	

	/**
	 * Returns the {@link VirtualTable} that caused the event.
	 * 
	 * @return a {@link VirtualTable}
	 */
	@Override
	public VirtualTable getSource()
	{
		return (VirtualTable)super.getSource();
	}
	

	/**
	 * Returns the {@link Type} of the event.
	 * 
	 * @return a {@link Type}
	 */
	public Type getType()
	{
		return type;
	}
	

	/**
	 * Returns the {@link VirtualTableRow} that caused the event.
	 * 
	 * @return a {@link VirtualTableRow}.
	 */
	public VirtualTableRow getRow()
	{
		return row;
	}
	

	/**
	 * Returns the index of the row that caused the event.
	 * 
	 * @return a row index
	 */
	public int getRowIndex()
	{
		return rowIndex;
	}
	

	/**
	 * Returns the index of the column that caused the event.
	 * 
	 * @return a column index
	 * @since 3.2
	 */
	public int getColumnIndex()
	{
		return columnIndex;
	}
}
