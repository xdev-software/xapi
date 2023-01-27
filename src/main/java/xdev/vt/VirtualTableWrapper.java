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
 * Specifies the contract of a wrapper implementation for {@link VirtualTable}s.
 * 
 * @author XDEV Software Corp.
 */
public interface VirtualTableWrapper
{
	/**
	 * Returns the wrapped {@link VirtualTable}.
	 * 
	 * @return a {@link VirtualTable}
	 */
	public VirtualTable getVirtualTable();
	

	/**
	 * Return the index of the wrapped {@link VirtualTable} corresponding to
	 * Parameter <code>col</code>.
	 * 
	 * @param col
	 *            the model column index
	 * @return a column index in the {@link VirtualTable}
	 */
	public int viewToModelColumn(int col);
	

	/**
	 * Returns the array of column indexes of the model.
	 * 
	 * @return an <code>int</code> array of indexed
	 */
	public int[] getModelColumnIndices();
	

	/**
	 * Returns the wrapped {@link VirtualTableRow} behind the index
	 * <code>modelRow</code>.
	 * 
	 * @param modelRow
	 *            the index in the model
	 * @return the wrapped {@link VirtualTableRow}
	 */
	public VirtualTableRow getVirtualTableRow(int modelRow);
	

	/**
	 * Returns the number of rows in the model.
	 * 
	 * @return the number of rows in the model
	 * @since 3.2
	 */
	public int getRowCount();
}
