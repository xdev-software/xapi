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
import java.util.*;


class RowBox
{
	private Vector		words;
	private Rectangle	rect;
	private int			maxDescent	= 0, maxLeading = 0, c = 0;


	public RowBox(Rectangle rect)
	{
		words = new Vector();
		this.rect = rect;
	}


	public void addWord(Word w)
	{
		words.addElement(w);
		maxDescent = Math.max(maxDescent,w.maxDescent);
		maxLeading = Math.max(maxLeading,w.maxLeading);
		c++;
	}


	public Sign layout(int align, int newHeight, int row)
	{
		Sign s = null;

		try
		{
			rect.height = newHeight;

			Word w;
			int x, allwidth;

			if(align == XdevDocument.TRAILING)
			{
				w = (Word)words.elementAt(words.size() - 1);
				if(w.sign[w.sign.length - 1].isBreak())
				{
					align = XdevDocument.LEFT;
				}
			}

			switch(align)
			{
				case XdevDocument.TRAILING:
				case XdevDocument.LEFT:
					x = rect.x;
					for(int i = 0; i < c; i++)
					{
						w = (Word)words.elementAt(i);
						s = w.layout(x,rect,row,maxDescent,maxLeading,0);
						x += w.width;
					}
				break;
				case XdevDocument.RIGHT:
					x = rect.x + rect.width - 1;
					for(int i = c - 1; i >= 0; i--)
					{
						w = (Word)words.elementAt(i);
						x -= w.width;
						s = w.layout(x,rect,row,maxDescent,maxLeading,0);
					}
				break;
				case XdevDocument.CENTER:
					allwidth = 0;
					for(int i = 0; i < c; i++)
					{
						w = (Word)words.elementAt(i);
						allwidth += w.width;
					}
					x = rect.x + (rect.width - allwidth) / 2;
					for(int i = 0; i < c; i++)
					{
						w = (Word)words.elementAt(i);
						s = w.layout(x,rect,row,maxDescent,maxLeading,0);
						x += w.width;
					}
				break;
			}
		}
		catch(Exception e)
		{
		}
		finally
		{
			words.removeAllElements();
			rect = null;
		}

		return s;
	}
}
