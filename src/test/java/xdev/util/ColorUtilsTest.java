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
package xdev.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.awt.Color;

import org.junit.Ignore;
import org.junit.Test;


/**
 * Tests for the Class {@link ColorUtils}.
 * 
 * @author XDEV Software (FHAE)
 *
 */
public class ColorUtilsTest
{
	
	/**
	 * {@link Color} with RGB values of 0,0,0 .
	 */
	private final Color COLOR_1 = new Color(0, 0, 0);
	/**
	 * {@link Color} with RGB values of 255,255,255 .
	 */
	private final Color COLOR_2 = new Color(255, 255, 255);
	/**
	 * {@link Color} with RGB values of 127,127,127 .
	 */
	private final Color COLOR_3 = new Color(127, 127, 127);
	
	/**
	 * Test method for {@link xdev.util.ColorUtils#deriveColor(java.awt.Color, int)}.
	 */
	@Test
	public void testDeriveColor_defaultBehavior()
	{
		final int alpha = 30;
		final Color actual = ColorUtils.deriveColor(this.COLOR_1, alpha);
		assertEquals(actual.getAlpha(), alpha);
	}
	
	/**
	 * Test method for {@link xdev.util.ColorUtils#deriveColor(java.awt.Color, int)}.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testDeriveColor_AlphaGreaterIllegalArgumentException()
	{
		ColorUtils.deriveColor(this.COLOR_1, 300);
	}
	
	/**
	 * Test method for {@link xdev.util.ColorUtils#deriveColor(java.awt.Color, int)}.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testDeriveColor_AlphaNegativeIllegalArgumentException()
	{
		ColorUtils.deriveColor(this.COLOR_1, -100);
	}
	
	/**
	 * Test method for {@link xdev.util.ColorUtils#getMiddle(java.awt.Color, java.awt.Color)}.
	 */
	@Test
	public void testGetMiddle()
	{
		final Color actual = ColorUtils.getMiddle(this.COLOR_1, this.COLOR_2);
		assertEquals(this.COLOR_3, actual);
	}
	
	/**
	 * Test method for {@link xdev.util.ColorUtils#getDifference(java.awt.Color, java.awt.Color, float)}.
	 */
	@Test
	@Ignore
	public void testGetDifference()
	{
		fail("Not yet implemented - No JavaDoc"); // TODO
		
	}
	
}
