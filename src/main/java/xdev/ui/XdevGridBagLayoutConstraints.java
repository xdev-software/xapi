package xdev.ui;

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


import java.awt.Insets;
import java.io.Serializable;

import xdev.lang.Copyable;


public class XdevGridBagLayoutConstraints implements Copyable<XdevGridBagLayoutConstraints>,
		Serializable
{
	public static final int		NONE				= 0;
	public static final int		BOTH				= 1;
	public static final int		HORIZONTAL			= 2;
	public static final int		VERTICAL			= 3;
	public static final int		CENTER				= 10;
	public static final int		NORTH				= 11;
	public static final int		NORTHEAST			= 12;
	public static final int		EAST				= 13;
	public static final int		SOUTHEAST			= 14;
	public static final int		SOUTH				= 15;
	public static final int		SOUTHWEST			= 16;
	public static final int		WEST				= 17;
	public static final int		NORTHWEST			= 18;
	public static final int		PAGE_START			= 19;
	public static final int		PAGE_END			= 20;
	public static final int		LINE_START			= 21;
	public static final int		LINE_END			= 22;
	public static final int		FIRST_LINE_START	= 23;
	public static final int		FIRST_LINE_END		= 24;
	public static final int		LAST_LINE_START		= 25;
	public static final int		LAST_LINE_END		= 26;
	public int					gridx;
	public int					gridy;
	public int					gridwidth;
	public int					gridheight;
	public double				weightx;
	public double				weighty;
	public int					anchor;
	public int					fill;
	public Insets				insets;
	public int					ipadx;
	public int					ipady;
	int							tempX;
	int							tempY;
	int							tempWidth;
	int							tempHeight;
	int							minWidth;
	int							minHeight;
	
	private static final long	serialVersionUID	= -1000070633030801713L;
	

	public XdevGridBagLayoutConstraints(int gridx, int gridy, int gridwidth, int gridheight,
			double weightx, double weighty, int anchor, int fill, Insets insets)
	{
		this(gridx,gridy,gridwidth,gridheight,weightx,weighty,anchor,fill,insets,0,0);
	}
	

	public XdevGridBagLayoutConstraints(int gridx, int gridy, int gridwidth, int gridheight,
			double weightx, double weighty, int anchor, int fill, Insets insets, int ipadx,
			int ipady)
	{
		this.gridx = gridx;
		this.gridy = gridy;
		this.gridwidth = gridwidth;
		this.gridheight = gridheight;
		this.weightx = weightx;
		this.weighty = weighty;
		this.anchor = anchor;
		this.fill = fill;
		this.insets = insets;
		this.ipadx = ipadx;
		this.ipady = ipady;
	}
	

	/**
	 * {@inheritDoc}
	 */
	@Override
	public XdevGridBagLayoutConstraints clone()
	{
		try
		{
			XdevGridBagLayoutConstraints c = (XdevGridBagLayoutConstraints)super.clone();
			c.insets = (Insets)insets.clone();
			return c;
		}
		catch(CloneNotSupportedException e)
		{
			throw new InternalError();
		}
	}
}
