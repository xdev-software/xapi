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


class Word
{
	Vector	signs;
	float	width		= 0;
	int		height		= 0;
	int		maxDescent	= 0;
	int		maxLeading	= 0;
	int		row;
	Sign[]	sign;
	String	string		= "";
	int		signCount;


	Word()
	{
		signs = new Vector();
	}


	void computeSize()
	{
		width = height = maxDescent = maxLeading = 0;
		signCount = signs.size();
		sign = new Sign[signCount];
		for(int i = 0; i < signCount; i++)
		{
			sign[i] = (Sign)signs.elementAt(i);
			width += sign[i].getBounds().width;
			height = Math.max(height,sign[i].getBounds().height);
			maxDescent = Math.max(maxDescent,sign[i].getDescent());
			maxLeading = Math.max(maxLeading,sign[i].getLeading());
		}
	}


	Sign layout(int x, Rectangle r, int row, int descent, int leading, int gap)
	{
		this.row = row;
		int baseline = r.y + r.height;
		SignBounds b;
		StringBuffer sb = new StringBuffer();
		for(int i = 0; i < signCount; i++)
		{
			b = sign[i].getBounds();
			b.y = baseline - b.height;
			sign[i].setDrawY(baseline - descent - leading);
			b.x = x;
			x += b.width;
			sb.append(sign[i]).toString();
			sign[i] = null;
		}

		string = sb.toString();

		Sign s;
		if(signs.size() > 0)
		{
			s = (Sign)signs.elementAt(signs.size() - 1);
			signs.removeAllElements();
		}
		else
		{
			s = sign[0];
		}

		return s;
	}


	boolean isSpace()
	{
		for(int i = 0; i < signCount; i++)
		{
			if(!sign[i].isSpace())
			{
				return false;
			}
		}

		return true;
	}


	boolean isTab()
	{
		return signCount == 1 && sign[0].isTab();
	}


	boolean isBreak()
	{
		return signCount == 1 && sign[0].isBreak();
	}


	@Override
	public String toString()
	{
		return string;
	}
}
