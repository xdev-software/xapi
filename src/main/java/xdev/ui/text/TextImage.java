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


class TextImage implements Sign
{
	protected SignBounds	rect;
	protected Link			link;
	protected Image			img;
	protected String		str;
	

	public TextImage(Image img, String str)
	{
		this.img = img;
		rect = new SignBounds(0,0,img.getWidth(null),img.getHeight(null));
	}
	

	public boolean isTab()
	{
		return false;
	}
	

	public boolean isBreak()
	{
		return false;
	}
	

	public boolean canBreak()
	{
		return true;
	}
	

	public boolean isSpace()
	{
		return false;
	}
	

	public int getDrawY()
	{
		return 0;
	}
	

	public void setDrawY(int i)
	{
	}
	

	public void paint(Graphics g)
	{
		g.drawImage(img,rect.x,rect.y,null);
	}
	

	public int getDescent()
	{
		return 0;
	}
	

	public Link getLink()
	{
		return link;
	}
	

	public void setLink(Link l)
	{
		link = l;
	}
	

	public int getLeading()
	{
		return 0;
	}
	

	public SignBounds getBounds()
	{
		return rect;
	}
	

	@Override
	public String toString()
	{
		return str;
	}
	

	public boolean equalsStyleAndRow(Sign s)
	{
		return false;
	}
	

	public boolean equalsRow(Sign s)
	{
		return false;
	}
	

	public void addYdiff(int diff)
	{
		rect.y += diff;
	}
}
