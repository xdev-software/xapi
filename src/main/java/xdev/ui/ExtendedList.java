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
package xdev.ui;


import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JList;

import xdev.util.StringUtils;
import xdev.vt.VirtualTable;


/**
 * A {@link ExtendedList} interface provides a useful extension to the
 * subclasses of {@link JList} or {@link JComboBox}.
 *
 *
 * @author XDEV Software
 * @since 4.0
 */
public interface ExtendedList<C extends JComponent> extends MasterDetailComponent<C>
{
	/**
	 * Updates the underlying model with data from the {@link VirtualTable} vt.
	 * <p>
	 * This is a shortcut for <code>setModel(vt,itemCol,dataCol,false)</code>
	 *
	 * @param vt
	 *            the {@link VirtualTable} containing the data for the model
	 * @param itemCol
	 *            columnname to fill <code>item</code> from or string with
	 *            variables like <code>"{%SURNAME} {%NAME} - {%AGE}"</code>
	 * @param dataCol
	 *            column name to fill <code>data</code> from, or multiple
	 *            columns names, comma-separated
	 *
	 * @see ItemList#setModel(VirtualTable, String, String)
	 * @see StringUtils#format(String, xdev.util.StringUtils.ParameterProvider)
	 */
	public void setModel(VirtualTable vt, String itemCol, String dataCol);


	/**
	 * Updates the underlying model with data from the {@link VirtualTable} vt.
	 *
	 * @param vt
	 *            the {@link VirtualTable} containing the data for the model
	 * @param itemCol
	 *            columnname to fill <code>item</code> from or string with
	 *            variables like <code>"{%SURNAME} {%NAME} - {%AGE}"</code>
	 * @param dataCol
	 *            column name to fill <code>data</code> from, or multiple
	 *            columns names, comma-separated
	 * @param queryData
	 *            if {@code true}, the best fitting select for this {@code vt}
	 *            is used
	 *
	 * @see ItemList#setModel(VirtualTable, String, String, boolean)
	 * @see StringUtils#format(String, xdev.util.StringUtils.ParameterProvider)
	 */
	public void setModel(final VirtualTable vt, final String itemCol, final String dataCol,
			final boolean queryData);


	/**
	 * Updates the underlying model with data from the {@link VirtualTable} vt.
	 *
	 * @param vt
	 *            the {@link VirtualTable} containing the data for the model
	 * @param itemCol
	 *            columnname to fill <code>item</code> from or string with
	 *            variables like <code>"{%SURNAME} {%NAME} - {%AGE}"</code>
	 * @param dataCol
	 *            column name to fill <code>data</code> from, or multiple
	 *            columns names, comma-separated
	 * @param queryData
	 *            if {@code true}, the best fitting select for this {@code vt}
	 *            is used
	 * @param selectiveQuery
	 *            if {@code true}, only the used <code>columns</code> are
	 *            queried
	 *
	 * @see ItemList#setModel(VirtualTable, String, String, boolean, boolean)
	 * @see StringUtils#format(String, xdev.util.StringUtils.ParameterProvider)
	 *
	 * @since 5.0
	 */
	public void setModel(final VirtualTable vt, final String itemCol, final String dataCol,
			final boolean queryData, final boolean selectiveQuery);


	/**
	 * Returns the {@link ItemList}, that include the <code>item</code> and
	 * <code>data</code>.
	 *
	 * @return the {@link ItemList} of this {@link ExtendedList}
	 */
	public ItemList getItemList();


	/**
	 * Sets the {@link ItemList} that represents the contents or "value" of the
	 * {@link ExtendedList} and then clears the list's selection.
	 *
	 * @param itemList
	 *            {@link ItemList} include the <code>item</code> and
	 *            <code>data</code>
	 */
	public void setItemList(ItemList itemList);
}
