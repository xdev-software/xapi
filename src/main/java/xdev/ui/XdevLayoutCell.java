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


import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.LayoutManager2;
import java.awt.Rectangle;


/**
 * @deprecated Old layout component cell, use {@link GridBagLayout} instead
 * 
 * 
 * @author XDEV Software
 * 
 */
@Deprecated
@BeanSettings(acceptChildren = true, useXdevCustomizer = true)
public class XdevLayoutCell extends XdevTextPane
{
	public final static int	FILL_NONE		= GBC.NONE;
	public final static int	FILL_HORIZONTAL	= GBC.HORIZONTAL;
	public final static int	FILL_VERTICAL	= GBC.VERTICAL;
	public final static int	FILL_BOTH		= GBC.BOTH;
	
	private Insets			insets			= new Insets(3,3,3,3);
	private Insets			padding			= new Insets(0,0,0,0);
	private int				anchor			= GBC.CENTER;
	private int				fill			= FILL_HORIZONTAL;
	private int				row				= 0;
	private int				col				= 0;
	private int				rowSpan			= 1;
	private int				colSpan			= 1;
	private int				weightX			= 0;
	private int				weightY			= 0;
	Rectangle				cellRect;
	
	
	public XdevLayoutCell()
	{
		super();
		
		insets = new Insets(0,0,0,0);
		padding = new Insets(0,0,0,0);
		cellRect = new Rectangle();
		
		setLayout(new CellLayout());
	}
	
	
	public XdevLayoutCell(Rectangle cellRect, int row, int col, int rowSpan, int colSpan,
			int weightX, int weightY)
	{
		this();
		
		this.cellRect = cellRect;
		this.row = row;
		this.col = col;
		this.rowSpan = rowSpan;
		this.colSpan = colSpan;
		this.weightX = weightX;
		this.weightY = weightY;
	}
	
	
	@Override
	public void setLayout(LayoutManager mgr)
	{
		if(mgr instanceof CellLayout)
		{
			super.setLayout(mgr);
		}
	}
	
	
	public void setInsets(Insets insets)
	{
		this.insets = insets;
	}
	
	
	@Override
	public Insets getInsets()
	{
		return insets;
	}
	
	
	public void setPadding(Insets padding)
	{
		this.padding = padding;
	}
	
	
	public Insets getPadding()
	{
		return padding;
	}
	
	
	public void setAnchor(int anchor)
	{
		this.anchor = anchor;
	}
	
	
	public int getAnchor()
	{
		return anchor;
	}
	
	
	public void setFill(int fill)
	{
		this.fill = fill;
	}
	
	
	public int getFill()
	{
		return fill;
	}
	
	
	public int getRow()
	{
		return row;
	}
	
	
	public int getCol()
	{
		return col;
	}
	
	
	public int getRowSpan()
	{
		return rowSpan;
	}
	
	
	public int getColSpan()
	{
		return colSpan;
	}
	
	
	public int getWeightX()
	{
		return weightX;
	}
	
	
	public void setWeightX(int weightX)
	{
		this.weightX = weightX;
	}
	
	
	public int getWeightY()
	{
		return weightY;
	}
	
	
	public void setWeightY(int weightY)
	{
		this.weightY = weightY;
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Dimension getPreferredSize()
	{
		Dimension d = getChildPreferredSize();
		
		if(isPreferredSizeSet())
		{
			Dimension ps = super.getPreferredSize();
			if(ps.width == 0)
			{
				ps.width = d.width;
			}
			if(ps.height == 0)
			{
				ps.height = d.height;
			}
			
			d = ps;
		}
		
		d.width += insets.left + insets.right;
		d.height += insets.top + insets.bottom;
		
		d.width += padding.left + padding.right;
		d.height += padding.top + padding.bottom;
		
		return d;
	}
	
	private final static Dimension	minPrefSize	= new Dimension(10,10);
	
	
	public Dimension getChildPreferredSize()
	{
		Component[] children = getComponents();
		if(children.length == 1)
		{
			return children[0].getPreferredSize();
		}
		else
		{
			return minPrefSize.getSize();
		}
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Dimension getMinimumSize()
	{
		return minPrefSize.getSize();
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Dimension getMaximumSize()
	{
		return getPreferredSize();
	}
	
	
	
	private class CellLayout implements LayoutManager2
	{
		/**
		 * {@inheritDoc}
		 */
		@Override
		public void layoutContainer(Container parent)
		{
			synchronized(getTreeLock())
			{
				Dimension size = getSize();
				Rectangle place = new Rectangle(insets.left,insets.top,size.width - insets.left
						- insets.right,size.height - insets.top - insets.bottom);
				
				int c = getComponentCount();
				for(int i = 0; i < c; i++)
				{
					Component child = getComponent(i);
					
					Rectangle r = child.getBounds();
					Dimension ps = child.getPreferredSize();
					if(ps.width <= 1)
					{
						ps.width = r.width;
					}
					if(ps.height <= 1)
					{
						ps.height = r.height;
					}
					
					switch(fill)
					{
						case FILL_NONE:
							r.setSize(ps);
						break;
						
						case FILL_HORIZONTAL:
							r.width = Math.max(1,place.width);
							r.height = Math.min(place.height,ps.height);
						break;
						
						case FILL_VERTICAL:
							r.width = Math.min(place.width,ps.width);
							r.height = Math.max(1,place.height);
						break;
						
						case FILL_BOTH:
							r.width = Math.max(1,place.width);
							r.height = Math.max(1,place.height);
						break;
					}
					
					switch(anchor)
					{
						case GBC.CENTER:
							r.setLocation(place.x + (place.width - r.width) / 2,place.y
									+ (place.height - r.height) / 2);
						break;
						
						case GBC.NORTHEAST:
							r.setLocation(place.x + place.width - r.width,place.y);
						break;
						
						case GBC.NORTH:
							r.setLocation(place.x + (place.width - r.width) / 2,place.y);
						break;
						
						case GBC.NORTHWEST:
							r.setLocation(place.x,place.y);
						break;
						
						case GBC.EAST:
							r.setLocation(place.x + place.width - r.width,place.y
									+ (place.height - r.height) / 2);
						break;
						
						case GBC.SOUTHEAST:
							r.setLocation(place.x + place.width - r.width,place.y + place.height
									- r.height);
						break;
						
						case GBC.SOUTH:
							r.setLocation(place.x + (place.width - r.width) / 2,place.y
									+ place.height - r.height);
						break;
						
						case GBC.SOUTHWEST:
							r.setLocation(place.x,place.y + place.height - r.height);
						break;
						
						case GBC.WEST:
							r.setLocation(place.x,place.y + (place.height - r.height) / 2);
						break;
					}
					
					child.setBounds(r);
				}
			}
		}
		
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		public void addLayoutComponent(String name, Component comp)
		{
		}
		
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		public void addLayoutComponent(Component comp, Object constraints)
		{
		}
		
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		public void removeLayoutComponent(Component comp)
		{
		}
		
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		public Dimension preferredLayoutSize(Container parent)
		{
			return getPreferredSize();
		}
		
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		public Dimension minimumLayoutSize(Container parent)
		{
			int iw = insets.left + insets.right, ih = insets.top + insets.bottom;
			Dimension d = new Dimension(iw,ih);
			
			int c = getComponentCount();
			for(int i = 0; i < c; i++)
			{
				Component child = getComponent(i);
				Dimension ms = child.getMinimumSize();
				if(ms != null)
				{
					d.width = Math.max(d.width,iw + ms.width);
					d.height = Math.max(d.height,ih + ms.height);
				}
			}
			
			return d;
		}
		
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		public Dimension maximumLayoutSize(Container target)
		{
			return null;
		}
		
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		public void invalidateLayout(Container target)
		{
		}
		
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		public float getLayoutAlignmentX(Container target)
		{
			return 0.5f;
		}
		
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		public float getLayoutAlignmentY(Container target)
		{
			return 0.5f;
		}
	}
	
	// @Override
	// public String toString()
	// {
	// return super.toString() + "[col=" + col + ", row=" + row + ", colSpan=" +
	// colSpan
	// + ", rowSpan=" + rowSpan + "]";
	// }
}
