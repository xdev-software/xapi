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
package xdev.io.csv;

import java.io.IOException;
import java.io.Reader;
import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import xdev.io.XdevFile;

@Ignore
public class CSVReaderTest
{
	@Test
	public void readTest() throws IOException
	{
		try(final Reader reader = new XdevFile("test.csv").openReader();
			final CSVReader csvReader = new CSVReader(reader, ';'))
		{
			final List<String[]> list = csvReader.readAll();
			Assert.assertEquals(2, list.size());
			Assert.assertTrue(
				Arrays.equals(
					new String[]{"abc", "123", "a\"q", "\"\"a", "aq\""},
					list.get(0)));
			Assert.assertTrue(Arrays.equals(new String[]{"", "2", "", "4", ""}, list.get(1)));
		}
	}
}
