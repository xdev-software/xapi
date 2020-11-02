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


import java.awt.Color;

import xdev.lang.LibraryMember;


/**
 * 
 * <p>
 * The <code>ColorUtils</code> class provides utility methods for {@link Color}
 * handling.
 * </p>
 * 
 * @author XDEV Software
 * 
 * @since 2.0
 */
@LibraryMember
public final class ColorUtils
{
	private ColorUtils()
	{
	}
	
	
	/**
	 * Creates an sRGB color with the RGB values of the <code>original</code>
	 * and the specified <code>alpha</code> value.
	 * 
	 * 
	 * @param original
	 *            the original {@link Color}
	 * @param alpha
	 *            the alpha component (range 0 - 255)
	 * 
	 * @return the new created color
	 * 
	 * @throws IllegalArgumentException
	 *             if <code>alpha</code> are outside of the range 0 to 255,
	 *             inclusive
	 * 
	 * @see Color
	 */
	public static Color deriveColor(Color original, int alpha) throws IllegalArgumentException
	{
		return new Color(original.getRed(),original.getGreen(),original.getBlue(),alpha);
	}
	
	
	/**
	 * Creates a color from the average values of the given <code>c1</code> and
	 * <code>c2</code>.
	 * 
	 * @param c1
	 *            first color
	 * @param c2
	 *            second color
	 * 
	 * @return the {@link Color} with the average values
	 */
	public static Color getMiddle(Color c1, Color c2)
	{
		int r = c1.getRed();
		int g = c1.getGreen();
		int b = c1.getBlue();
		
		int rd = (c2.getRed() - r) / 2;
		int gd = (c2.getGreen() - g) / 2;
		int bd = (c2.getBlue() - b) / 2;
		
		return new Color(r + rd,g + gd,b + bd);
	}
	
	
	// TODO javadoc
	public static Color getDifference(Color c1, Color c2, float pos)
	{
		return new Color(getColorPos(c1.getRed(),c2.getRed(),pos),getColorPos(c1.getGreen(),
				c2.getGreen(),pos),getColorPos(c1.getBlue(),c2.getBlue(),pos));
	}
	
	
	private static int getColorPos(int i1, int i2, float pos)
	{
		int max = Math.max(i1,i2);
		int min = Math.min(i1,i2);
		
		return min + Math.round((max - min) * pos);
	}
	
	
	/**
	 * Calculates a {@link Color} from given <code>RGB</code>
	 * <code>String</code> values.
	 * 
	 * @param values
	 *            the <code>RGB</code> <code>String</code> values.
	 * @return the {@link Color} representation of the given values, or
	 *         <code>null</code> if no <code>Color</code> could be calculated.
	 * 
	 * @since 4.0
	 */
	public static Color getColorFromRGBString(String... values)
	{
		Integer[] rgbValues = new Integer[3];
		
		if(values.length >= 0)
		{
			// parse
			for(int i = 0; i < values.length; i++)
			{
				rgbValues[i] = Integer.parseInt(values[i]);
			}
			
			Color color = new Color(rgbValues[0],rgbValues[1],rgbValues[2]);
			return color;
		}
		throw new RuntimeException("Values could not be parsed");
	}
	
	
	/**
	 * Returns the given colors values as concatenated String
	 * 
	 * @param color
	 *            the color to convert.
	 * @return a string containing the given colors RGB values.
	 */
	public static String toString(Color color)
	{
		return toString(color,false);
	}
	
	
	/**
	 * Returns the given colors values as concatenated String
	 * 
	 * @param color
	 *            the color to convert.
	 * @param withAlpha
	 *            indicates whether alpha values should be considered or not.
	 * @return a string containing the given colors RGB values.
	 */
	public static String toString(Color color, boolean withAlpha)
	{
		StringBuilder sb = new StringBuilder();
		
		sb.append(color.getRed());
		sb.append(',');
		sb.append(color.getGreen());
		sb.append(',');
		sb.append(color.getBlue());
		
		if(withAlpha)
		{
			int alpha = color.getAlpha();
			if(alpha != 255)
			{
				sb.append(',');
				sb.append(alpha);
			}
		}
		
		return sb.toString();
	}
}
