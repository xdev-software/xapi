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
package xdev.ui.table;


import javax.swing.JTable;

import xdev.vt.VirtualTable;


/**
 * Specifies the contract between a Table which modifies its columns and a
 * {@link VirtualTable} used for TableModel purpose.
 * <p>
 * This interface guarantees that the modified data is handled properly
 * independent of the filtered model
 * </p>
 * 
 * <p>
 * For Example if the Table modifies its column count -
 * {@link TableColumnConverter#viewToModel(JTable, int)} can be used to identify
 * non model columns.
 * </p>
 * 
 * @author XDEV Software (JW)
 * @since 3.2
 */
public interface TableColumnConverter
{
	/**
	 * JIDEs return value if no column could be found in the given model.
	 */
	public static final Integer	NOT_IN_ORIGINAL_MODEL	= -1;
	
	
	/**
	 * This method returns the data representation of a given view-model column.
	 * E.g. save the filtered data independent of the view-model index
	 * 
	 * @param table
	 * @param column
	 * @return the representation of the given column in the datamodel
	 */
	public int viewToModel(JTable table, int column);
	
	
	/**
	 * This method returns the view representation of a given data-model column.
	 * E.g. set dataindex to view-model index
	 * 
	 * @param table
	 * @param column
	 * @return the representation of the given column in the datamodel
	 */
	public int modelToView(JTable table, int column);
}
