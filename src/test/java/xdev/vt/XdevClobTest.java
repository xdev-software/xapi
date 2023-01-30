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


import javax.sql.rowset.serial.SerialClob;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


public class XdevClobTest
{

	private static final String	TEST_STRING	= "0123456789";

	private XdevClob			clob;


	@Before
	public void doBeforeEveryTest()
	{
		this.clob = new XdevClob(TEST_STRING.toCharArray());
	}


	@Test
	public void testSubString() throws Exception
	{
		XdevClob clobWithArray = new XdevClob(TEST_STRING.toCharArray());
		XdevClob clobWithClob = new XdevClob(new SerialClob(TEST_STRING.toCharArray()));

		// fails, because java.sql.Clob starts with index 1
		Assert.assertEquals(clobWithArray.getSubString(1,1),clobWithClob.getSubString(1,1));
	}


	@Test
	public void testCompareAndEquals() throws Exception
	{
		XdevClob clob2 = new XdevClob(TEST_STRING.toCharArray());

		boolean equalByComparison = this.clob.compareTo(clob2) == 0;
		boolean equalByEquals = this.clob.equals(clob2);

		// fails, because equals is not overwritten
		Assert.assertEquals(equalByComparison,equalByEquals);
	}

}
