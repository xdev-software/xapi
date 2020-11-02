package xdev.ui.text;

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


import java.awt.*;


class SignBounds
{
	public int		x, y, height;
	public float	width;
	

	public SignBounds()
	{
		this(0,0,0,0);
	}
	

	public SignBounds(int x, int y, float width, int height)
	{
		setBounds(x,y,width,height);
	}
	

	public void setBounds(SignBounds sb)
	{
		setBounds(sb.x,sb.y,sb.width,sb.height);
	}
	

	public void setBounds(int x, int y, float width, int height)
	{
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}
	

	public Rectangle getBounds()
	{
		return new Rectangle(x,y,Math.round(width),height);
	}
}
