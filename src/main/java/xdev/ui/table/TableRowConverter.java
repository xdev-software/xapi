package xdev.ui.table;

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


import javax.swing.JTable;

import xdev.ui.XdevTable;


/**
 * Specifies the contract between a {@link XdevTable} and a Quickfilter.
 * <p>
 * This interface guarantees that the modified data is handled properly
 * independent of the filtered model
 * 
 * @author XDEV Software (JW)
 * @since 3.1
 */
public interface TableRowConverter
{
	
	/**
	 * This method returns the data representation of a given view-model
	 * (quickfiltered-model) row.# E.g. save the filtered data independent of
	 * the view-model index
	 * 
	 * @param table
	 * @param row
	 * @return the representation of the given row in the datamodel
	 */
	public int viewToModel(JTable table, int row);
	

	/**
	 * This method returns the view representation of a given data-model
	 * (quickfiltered-model) row.# E.g. set dataindex to view-model index
	 * 
	 * @param table
	 * @param row
	 * @return the representation of the given row in the datamodel
	 */
	public int modelToView(JTable table, int row);
}
