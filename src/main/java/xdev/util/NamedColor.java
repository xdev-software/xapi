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


import java.awt.*;

import javax.swing.*;


/**
 * Wrapper class for iconified named color instances.
 * 
 * @author XDEV Software
 * @since 4.0
 */
public class NamedColor extends Color implements Icon
{
	private String	name;
	
	
	/**
	 * 
	 * @param name
	 *            the color alias
	 * @param c
	 *            the color
	 */
	public NamedColor(String name, Color c)
	{
		this(name,c.getRed(),c.getGreen(),c.getBlue());
	}
	
	
	/**
	 * 
	 * @param name
	 *            the color alias
	 * @param r
	 *            color red value
	 * @param g
	 *            color green value
	 * @param b
	 *            color blue value
	 */
	public NamedColor(String name, int r, int g, int b)
	{
		super(r,g,b);
		this.name = name;
	}
	
	
	/**
	 * Returns the color name
	 * 
	 * @return color name
	 */
	public String getName()
	{
		return name;
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString()
	{
		return name;
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	public int getIconHeight()
	{
		return 12;
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	public int getIconWidth()
	{
		return 12;
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	public void paintIcon(Component c, Graphics g, int x, int y)
	{
		g.setColor(this);
		g.fillRect(x,y,getIconWidth(),getIconHeight());
	}
}
