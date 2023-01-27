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
package xdev.ui;


import java.awt.Color;
import java.awt.Dimension;
import java.awt.image.ColorModel;
import java.awt.image.RGBImageFilter;


public class AlphaFilter extends RGBImageFilter
{
	public final static int	COMPLETE				= 0;
	public final static int	COLOR_DEPENDENT			= 1;
	public final static int	COLOR_DEPENDENT_REVERSE	= 2;
	public final static int	TO_RIGHT				= 3;
	public final static int	TO_LEFT					= 4;
	public final static int	TO_TOP					= 5;
	public final static int	TO_BOTTOM				= 6;
	
	private XdevComponent	cpn;
	private int				filter;
	private double			perPixel;
	private boolean			dofilter				= false;
	

	public AlphaFilter(XdevComponent cpn)
	{
		super();
		this.cpn = cpn;
		canFilterIndexColorModel = false;
	}
	

	public void update()
	{
		perPixel = 0.0;
		Dimension d = cpn.getSizeForFilter();
		if(cpn.alphaCondition == TO_RIGHT)
		{
			perPixel = (double)(255 - cpn.alpha) / (double)d.width;
		}
		else if(cpn.alphaCondition == TO_LEFT)
		{
			perPixel = (double)(255 - cpn.alpha) / (double)d.width;
		}
		else if(cpn.alphaCondition == TO_TOP)
		{
			perPixel = (double)(255 - cpn.alpha) / (double)d.height;
		}
		else if(cpn.alphaCondition == TO_BOTTOM)
		{
			perPixel = (double)(255 - cpn.alpha) / (double)d.height;
		}
	}
	

	@Override
	public void filterRGBPixels(int x, int y, int w, int h, int pixels[], int off, int scansize)
	{
		setFilter(cpn.alpha);
		if(cpn.alphaCondition == COMPLETE)
		{
			int index = off;
			for(int cy = 0; cy < h; cy++)
			{
				for(int cx = 0; cx < w; cx++)
				{
					pixels[index] &= filter;
					index++;
				}
				index += scansize - w;
			}
			consumer.setPixels(x,y,w,h,ColorModel.getRGBdefault(),pixels,off,scansize);
		}
		else
		{
			super.filterRGBPixels(x,y,w,h,pixels,off,scansize);
		}
	}
	

	@Override
	public int filterRGB(int x, int y, int rgb)
	{
		dofilter = false;
		if(perPixel > 0.0)
		{
			dofilter = true;
			if(cpn.alphaCondition == TO_RIGHT)
			{
				setFilter(255 - (int)Math.round(x * perPixel));
			}
			else if(cpn.alphaCondition == TO_LEFT)
			{
				setFilter(cpn.alpha + (int)Math.round(x * perPixel));
			}
			else if(cpn.alphaCondition == TO_TOP)
			{
				setFilter(cpn.alpha + (int)Math.round(y * perPixel));
			}
			else if(cpn.alphaCondition == TO_BOTTOM)
			{
				setFilter(255 - (int)Math.round(y * perPixel));
			}
		}
		else
		{
			Color c = new Color((rgb >> 16) & 0xff,(rgb >> 8) & 0xff,(rgb >> 0) & 0xff);
			if(cpn.alphaCondition == COLOR_DEPENDENT)
			{
				dofilter = compareWithAlphaColors(c);
			}
			else if(cpn.alphaCondition == COLOR_DEPENDENT_REVERSE)
			{
				dofilter = !compareWithAlphaColors(c);
			}
		}
		return dofilter ? (rgb & filter) : rgb;
	}
	

	private void setFilter(int alpha)
	{
		filter = (int)Long.parseLong(Integer.toHexString(alpha) + "FFFFFF",16);
	}
	

	private boolean compareWithAlphaColors(Color c)
	{
		Color[] alphaColors = cpn.getAlphaColors();
		if(alphaColors != null && alphaColors.length > 0)
		{
			for(int i = 0; i < alphaColors.length; i++)
			{
				Color ac = cpn.alphaColors[i];
				if(Math.abs(ac.getRed() - c.getRed()) <= cpn.alphaTolerance
						&& Math.abs(ac.getGreen() - c.getGreen()) <= cpn.alphaTolerance
						&& Math.abs(ac.getBlue() - c.getBlue()) <= cpn.alphaTolerance)
				{
					return true;
				}
			}
		}
		return false;
	}
}
