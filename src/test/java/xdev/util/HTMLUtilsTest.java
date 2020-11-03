/**
 * 
 */

package xdev.util;

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

import java.awt.Font;

import org.junit.Ignore;
import org.junit.Test;


/**
 * Tests for the Class {@link HTMLUtils}.
 * 
 * @author XDEV Software (FHAE)
 * 
 */
public class HTMLUtilsTest
{
	public final String FONT_STRING = "font-family:'Times New Roman'; font-style:italic; font-size:12pt;";
	public final String FONT_NAME = "Times New Roman";
	public final int FONT_SIZE = 12;
	public final int FONT_STYLE = Font.ITALIC;
	public final Font FONT = new Font(this.FONT_NAME, this.FONT_STYLE, this.FONT_SIZE);
	
	public final String JAVA_STRING = "Test´s build <> & ";
	public final int JAVA_STRING_LENGTH = 18;
	public final String HTML_STRING = "Test&acute;s build &lt;&gt; &amp; ";
	public final int HTML_STRING_LENGTH = 34;
	
	/**
	 * Test method for {@link xdev.util.HTMLUtils#toHTML(java.lang.String)}.
	 */
	@Test
	public void testToHTML_defaultBehavior()
	{
		final String actual = HTMLUtils.toHTML(this.JAVA_STRING);
		
		assertEquals(this.HTML_STRING_LENGTH, actual.length());
		assertEquals(this.HTML_STRING, actual);
	}
	
	/**
	 * Test method for {@link xdev.util.HTMLUtils#toStyle(java.awt.Font)}.
	 */
	@Test
	@Ignore("Depends on locale")
	public void testToStyleFont_defaultBehavior()
	{
		assertEquals(this.FONT_STRING, HTMLUtils.toStyle(this.FONT));
	}
	
	/**
	 * Test method for
	 * {@link xdev.util.HTMLUtils#toStyle(java.lang.String, int, int)}.
	 */
	@Test
	public void testToStyleStringIntInt_defaultBehavior()
	{
		assertEquals(this.FONT_STRING, HTMLUtils.toStyle(this.FONT_NAME, this.FONT_STYLE, this.FONT_SIZE));
	}
	
	/**
	 * Test method for {@link xdev.util.HTMLUtils#htmlToText(java.lang.String)}.
	 */
	@Test
	public void testHtmlToText_defaultBehavior()
	{
		final String actual = HTMLUtils.htmlToText(this.HTML_STRING);
		
		assertEquals(this.JAVA_STRING_LENGTH - 1, actual.length());
	}
	
}
