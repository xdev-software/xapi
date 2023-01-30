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
package xdev.ui.listbox;


import javax.swing.JList;

import xdev.ui.XdevComboBox;


/**
 * Specifies the contract between a {@link XdevComboBox} and a Quickfilter.<br>
 * This interface guarantees that the modified data is handled properly
 * independent of the filtered model
 * 
 * @author XDEV Software (JW)
 * @since 3.1
 */
public interface ListBoxRowConverter
{
	/**
	 * This method returns the data representation of a given view-model
	 * (quickfiltered-model) row.# E.g. save the filtered data independent of
	 * the view-model index
	 * 
	 * @param comboBox
	 * @param row
	 * @return the representation of the given row in the datamodel
	 */
	public int viewToModel(JList list, int row);
	
	
	/**
	 * This method returns the view representation of a given data-model
	 * (quickfiltered-model) row.# E.g. set a selected data-index independent of the
	 * view index
	 * 
	 * @param comboBox
	 * @param row
	 * @return the representation of the given row in the datamodel
	 */
	public int modelToView(JList list, int row);
	
}
