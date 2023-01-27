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


import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.table.TableColumn;
import javax.swing.text.TableView.TableRow;

import xdev.vt.VirtualTable;
import xdev.vt.VirtualTableColumn;


/**
 * 
 * 
 * 
 * @author XDEV Software
 * 
 */
public interface RendererDelegate
{
	/**
	 * Returns the customized component regardint the type of value.
	 * 
	 * @param table
	 *            the associated table
	 * @param value
	 *            the value which is rendered.
	 * @param row
	 *            the {@link TableRow}s index to be rendered.
	 * @param column
	 *            the {@link TableColumn}s index to be rendered.
	 * @param vt
	 *            the {@link VirtualTable} which is associated with this table.
	 * @param vtColumn
	 *            the {@link VirtualTableColumn} which is rendered.
	 *            <p>
	 *            Can be <code><b>null</b></code> if a modified column model is
	 *            rendered.
	 *            </p>
	 * 
	 * @param editable
	 *            the identifier which determines the editable state of the
	 *            rendered component.
	 * @return
	 */
	public JComponent getRendererComponent(JTable table, Object value, int row, int column,
			VirtualTable vt, VirtualTableColumn vtColumn, boolean editable);
}
