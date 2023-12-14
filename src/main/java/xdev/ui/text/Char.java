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
package xdev.ui.text;


import java.awt.*;


class Char implements Sign
{
	protected char			c;
	public String			s;
	protected Color			color;
	protected Font			font;
	protected boolean		underline, switchVals;
	protected SignBounds	rect;
	protected int			ascent, descent, leading, underlineY, underlineHeight, drawY;
	protected Link			link;
	

	public Char(char c, Font font, int ascent, int descent, int leading, Color color,
			boolean underline, int underlineY, int underlineHeight)
	{
		this.c = c;
		s = new String(new char[]{c});
		this.ascent = ascent;
		this.descent = descent;
		this.leading = leading;
		this.underline = underline;
		this.underlineY = underlineY;
		this.underlineHeight = underlineHeight;
		this.font = font;
		this.color = color;
		rect = new SignBounds();
	}
	

	public boolean isTab()
	{
		return c == '\t';
	}
	

	public boolean isBreak()
	{
		return c == '\n';
	}
	

	public boolean isSpace()
	{
		return c == ' ';
	}
	

	public int getLeading()
	{
		return leading;
	}
	

	public int getDescent()
	{
		return descent;
	}
	

	public int getDrawY()
	{
		return drawY;
	}
	

	public void setDrawY(int i)
	{
		drawY = i;
	}
	

	public SignBounds getBounds()
	{
		return rect;
	}
	

	public void setLink(Link l)
	{
		link = l;
	}
	

	public Link getLink()
	{
		return link;
	}
	

	public boolean canBreak()
	{
		return c == '-' || Character.isWhitespace(c);
	}
	

	@Override
	public String toString()
	{
		return s;
	}
	

	public void addBounds(SignBounds r)
	{
		int x1 = Math.min(rect.x,r.x);
		float x2 = Math.max(rect.x + rect.width,r.x + r.width);
		int y1 = Math.min(rect.y,r.y);
		int y2 = Math.max(rect.y + rect.height,r.y + r.height);
		r.x = x1;
		r.y = y1;
		r.width = x2 - x1;
		r.height = y2 - y1;
	}
	

	public boolean equalsStyleAndRow(Sign _s)
	{
		if(_s instanceof Char)
		{
			Char s = (Char)_s;
			try
			{
				return s.font.equals(font) && s.color.equals(color) && s.underline == underline
						&& s.drawY == drawY;
			}
			catch(Exception e)
			{
				return false;
			}
		}
		
		return false;
	}
	

	public boolean equalsRow(Sign _s)
	{
		if(_s instanceof Char)
		{
			Char s = (Char)_s;
			try
			{
				return s.drawY == drawY;
			}
			catch(Exception e)
			{
				return false;
			}
		}
		
		return false;
	}
	

	public void addYdiff(int diff)
	{
		drawY += diff;
		underlineY += diff;
		rect.y += diff;
	}
}
