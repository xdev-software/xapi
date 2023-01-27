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

package xdev.util.codec;

import java.io.IOException;
import java.util.Arrays;

import org.junit.Assert;
import org.junit.Test;

import xdev.io.IOUtils;

public class Base64Test
{
	@Test
	public void encode_decode() throws IOException
	{
		final byte[] bytes = ("i am a string to encode\r\n12345@bla\r !'§%\"§%$\\\n\n\n"
			+ IOUtils.readString(this.getClass().getResourceAsStream("/blindtext.txt"))).getBytes();
		final String base64 = Base64.encodeBytes(bytes);
		final byte[] decoded = Base64.decode(base64);
		Assert.assertTrue(Arrays.equals(bytes, decoded));
		
	}
}
