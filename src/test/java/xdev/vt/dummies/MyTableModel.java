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

import java.awt.Point;
import java.util.Hashtable;

import javax.swing.table.AbstractTableModel;

import org.junit.Ignore;

@Ignore
public class MyTableModel extends AbstractTableModel 
{

	  private Hashtable lookup;

	  private final int rows;

	  private final int columns;

	  private final String headers[];
  
	  
	  public MyTableModel(int rows, String columnHeaders[]) {
	    if ((rows < 0) || (columnHeaders == null)) {
	      throw new IllegalArgumentException(
	          "Invalid row count/columnHeaders");
	    }
	    this.rows = rows;
	    this.columns = columnHeaders.length;
	    headers = columnHeaders;
	    lookup = new Hashtable();
	  }

	  public int getColumnCount() {
	    return columns;
	  }

	  public int getRowCount() {
	    return rows;
	  }

	  public String getColumnName(int column) {
	    return headers[column];
	  }

	  public Object getValueAt(int row, int column) {
	    return lookup.get(new Point(row, column));
	  }

	  public void setValueAt(Object value, int row, int column) {
	    if ((rows < 0) || (columns < 0)) {
	      throw new IllegalArgumentException("Invalid row/column setting");
	    }
	    if ((row < rows) && (column < columns)) {
	      lookup.put(new Point(row, column), value);
	    }
	  }
	}
