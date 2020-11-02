
package xdev.vt;

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


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import javax.swing.JTable;

import org.junit.Ignore;

import xdev.lang.NotNull;
import xdev.vt.VirtualTable.VirtualTableRow;


/**
 * A set of assertion methods useful for writing tests. Only failed assertions
 * are recorded. These methods can be used directly:
 * <code>AssertVirtualTable.assertEqualsVirtualTableData(...)</code>, however,
 * they read better if they are referenced through static import:<br/>
 * 
 * <pre>
 * import static xdev.vt.AssertVirtualTable.*;
 *    ...
 *    assertEqualsVirtualTableData(...);
 * </pre>
 * 
 * @see AssertionError
 * @see Assert
 * 
 * @author XDEV Software (FHAE)
 * 
 */
@Ignore
public class AssertVirtualTable
{
	
	/**
	 * Asserts that two {@link VirtualTable}s data are equal. If they are not,
	 * an {@link AssertionError} is thrown. If <code>expected</code> and
	 * <code>actual</code> are <code>null</code>, they are considered equal.
	 * 
	 * @param expected
	 *            expected value
	 * @param actual
	 *            actual value
	 */
	public static void assertEqualsVirtualTableData(@NotNull VirtualTable expected,
			@NotNull VirtualTable actual)
	{
		assertNotNull(expected);
		assertNotNull(actual);
		assertEquals(expected.getRowCount(),actual.getRowCount());
		
		int index = 0;
		for(VirtualTableRow r : expected.rows())
		{
			assertTrue("expected<" + r + "> but was <" + actual.getRow(index) + ">",
					r.equalsValues(actual.getRow(index)));
			index++;
		}
	}
	

	/**
	 * Verifys the column count and the row count, additionaly the values of the
	 * <code>vt</code> and <code>table</code> are checked. The first
	 * {@link VirtualTableColumn} is invisible.
	 * 
	 * @param vt
	 * @param table
	 */
	public static void assertEqualsVirtualTableAndJTable(@NotNull VirtualTable vt,
			@NotNull JTable table)
	{
		int tableRC = table.getRowCount();
		int vtRC = vt.getRowCount();
		int tableCC = table.getColumnCount();
		int vtCC = vt.getColumnCount();
		
		assertEquals(vtRC,tableRC);
		// -1 of the vtCC because the ID column is in the table not available
		assertEquals(vtCC - 1,tableCC);
		
		for(int currentRC = 0; currentRC < vtRC; currentRC++)
		{
			// -1 of the vtCC because the ID column is in the table not
			// available
			for(int currentCC = 0; currentCC < vtCC - 1; currentCC++)
			{
				Object vtValue = vt.getValueAt(currentRC,currentCC + 1);
				Object tableValue = table.getValueAt(currentRC,currentCC);
				assertEquals(vtValue,tableValue);
			}
		}
	}
}
