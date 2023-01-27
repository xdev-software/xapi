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
package xdev.vt.dummies;

import javax.swing.table.AbstractTableModel;

import org.junit.Ignore;

import xdev.vt.VirtualTable;

@Ignore
public class MyTableModel2 extends AbstractTableModel 
{

	  private VirtualTable lookup;

	  private int rows= 0;

	  private int columns=0;


	  public MyTableModel2(int rows,VirtualTable lookup) 
	  {
		columns = lookup.getColumnCount();
	    rows = lookup.getRowCount();
	    this.lookup = lookup;
	  }

	  public int getColumnCount() {
	    return columns;
	  }

	  public int getRowCount() {
	    return rows;
	  }

	  public String getColumnName(int column) {
	    return lookup.getColumnName(column);
	  }

	  public Object getValueAt(int row, int column) {
	    return lookup.getValueAt(row, column);
	  }

	  public void setValueAt(Object value, int row, int column) {
	    if ((rows < 0) || (columns < 0)) {
	      throw new IllegalArgumentException("Invalid row/column setting");
	    }
	    if ((row < rows) && (column < columns)) {
	      lookup.setValueAt(value, row, column);
	    }
	  }
	}
