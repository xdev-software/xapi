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
import java.awt.Component;
import java.awt.ComponentOrientation;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.LayoutManager2;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.Serializable;

import javax.swing.JToolBar;
import javax.swing.SizeRequirements;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.plaf.ToolBarUI;
import javax.swing.plaf.UIResource;


/**
 * The standard toolbar in XDEV. Based on {@link JToolBar}.
 * 
 * @see JToolBar
 * 
 * @author XDEV Software
 * 
 * @since 2.0
 */
@BeanSettings(acceptChildren = true, useXdevCustomizer = true)
public class XdevToolBar extends JToolBar
{
	/**
	 * 
	 */
	private static final long	serialVersionUID	= 611151672227584331L;
	

	/**
	 * Creates a new {@link XdevToolBar}. The orientation of this
	 * {@link XdevToolBar} is initialized with {@link SwingConstants#HORIZONTAL}
	 * .
	 * 
	 * @see #XdevToolBar(int)
	 */
	public XdevToolBar()
	{
		this(HORIZONTAL);
	}
	

	/**
	 * Creates a new {@link XdevToolBar} with the specified
	 * <code>orientation</code>. The <code>orientation</code> must be either
	 * <code>HORIZONTAL</code> or <code>VERTICAL</code>.
	 * 
	 * @param orientation
	 *            the orientation desired
	 * 
	 * @see #setRollover(boolean)
	 * @see #setFloatable(boolean)
	 * @see #setLayout(LayoutManager)
	 */
	public XdevToolBar(int orientation)
	{
		super(orientation);
		
		setRollover(true);
		setFloatable(false);
		
		setLayout(new XdevToolBarLayout(orientation));
	}
	

	/**
	 * {@inheritDoc}
	 * 
	 */
	@Override
	public void setOrientation(int orientation)
	{
		super.setOrientation(orientation);
		
		setLayout(new XdevToolBarLayout(orientation));
		
		revalidate();
	}
	

	/**
	 * {@inheritDoc}
	 * 
	 */
	@Override
	public void updateUI()
	{
		setUI((ToolBarUI)UIManager.getUI(this));
		if(getLayout() == null)
		{
			setLayout(new XdevToolBarLayout(getOrientation()));
		}
		invalidate();
	}
	

	/**
	 * {@inheritDoc}
	 * 
	 */
	@Override
	public Dimension getPreferredSize()
	{
		Dimension d = super.getPreferredSize();
		if(getComponentCount() == 0) // no content
		{
			d.setSize(20,20);
		}
		return d;
	}
	

	/**
	 * {@inheritDoc}
	 * 
	 */
	@Override
	public String toString()
	{
		String toString = UIUtils.toString(this);
		if(toString != null)
		{
			return toString;
		}
		
		return super.toString();
	}
	

	/**
	 * {@inheritDoc}
	 */
	@BeanProperty(category = DefaultBeanCategories.MISC)
	@Override
	public void setForeground(Color fg)
	{
		super.setForeground(fg);
	}
	

	/**
	 * {@inheritDoc}
	 */
	@BeanProperty(category = DefaultBeanCategories.MISC)
	@Override
	public void setFont(Font font)
	{
		super.setFont(font);
	}
	
	static final int	X_AXIS		= 0;
	static final int	Y_AXIS		= 1;
	static final int	LINE_AXIS	= 2;
	static final int	PAGE_AXIS	= 3;
	


	private class XdevToolBarLayout implements LayoutManager2, Serializable,
			PropertyChangeListener, UIResource
	{
		XdevToolbarBoxLayout	boxLayout;
		

		XdevToolBarLayout(int orientation)
		{
			refresh(orientation);
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
		public Dimension preferredLayoutSize(Container target)
		{
			return boxLayout.preferredLayoutSize(target);
		}
		

		/**
		 * {@inheritDoc}
		 */
		@Override
		public Dimension minimumLayoutSize(Container target)
		{
			return boxLayout.minimumLayoutSize(target);
		}
		

		/**
		 * {@inheritDoc}
		 */
		@Override
		public Dimension maximumLayoutSize(Container target)
		{
			return boxLayout.maximumLayoutSize(target);
		}
		

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void layoutContainer(Container target)
		{
			boxLayout.layoutContainer(target);
		}
		

		/**
		 * {@inheritDoc}
		 */
		@Override
		public float getLayoutAlignmentX(Container target)
		{
			return boxLayout.getLayoutAlignmentX(target);
		}
		

		/**
		 * {@inheritDoc}
		 */
		@Override
		public float getLayoutAlignmentY(Container target)
		{
			return boxLayout.getLayoutAlignmentY(target);
		}
		

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void invalidateLayout(Container target)
		{
			boxLayout.invalidateLayout(target);
		}
		

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void propertyChange(PropertyChangeEvent e)
		{
			String name = e.getPropertyName();
			if(name.equals("orientation"))
			{
				refresh(((Integer)e.getNewValue()).intValue());
			}
		}
		

		private void refresh(int orientation)
		{
			if(orientation == SwingConstants.VERTICAL)
			{
				boxLayout = new XdevToolbarBoxLayout(XdevToolBar.this,PAGE_AXIS);
			}
			else
			{
				boxLayout = new XdevToolbarBoxLayout(XdevToolBar.this,LINE_AXIS);
			}
		}
		


		class XdevToolbarBoxLayout implements LayoutManager2
		{
			private int								axis;
			private Container						target;
			
			private transient SizeRequirements[]	xChildren;
			private transient SizeRequirements[]	yChildren;
			private transient SizeRequirements		xTotal;
			private transient SizeRequirements		yTotal;
			

			public XdevToolbarBoxLayout(Container target, int axis)
			{
				this.axis = axis;
				this.target = target;
			}
			

			/**
			 * {@inheritDoc}
			 */
			@Override
			public synchronized void invalidateLayout(Container target)
			{
				xChildren = null;
				yChildren = null;
				xTotal = null;
				yTotal = null;
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
			public void removeLayoutComponent(Component comp)
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
			public Dimension preferredLayoutSize(Container target)
			{
				Dimension size;
				synchronized(this)
				{
					checkRequests();
					size = new Dimension(xTotal.preferred,yTotal.preferred);
				}
				
				Insets insets = target.getInsets();
				size.width = (int)Math.min((long)size.width + (long)insets.left + insets.right,
						Integer.MAX_VALUE);
				size.height = (int)Math.min((long)size.height + (long)insets.top + insets.bottom,
						Integer.MAX_VALUE);
				return size;
			}
			

			/**
			 * {@inheritDoc}
			 */
			@Override
			public Dimension minimumLayoutSize(Container target)
			{
				Dimension size;
				synchronized(this)
				{
					checkRequests();
					size = new Dimension(xTotal.minimum,yTotal.minimum);
				}
				
				Insets insets = target.getInsets();
				size.width = (int)Math.min((long)size.width + (long)insets.left + insets.right,
						Integer.MAX_VALUE);
				size.height = (int)Math.min((long)size.height + (long)insets.top + insets.bottom,
						Integer.MAX_VALUE);
				return size;
			}
			

			/**
			 * {@inheritDoc}
			 */
			@Override
			public Dimension maximumLayoutSize(Container target)
			{
				Dimension size;
				synchronized(this)
				{
					checkRequests();
					size = new Dimension(xTotal.maximum,yTotal.maximum);
				}
				
				Insets insets = target.getInsets();
				size.width = (int)Math.min((long)size.width + (long)insets.left + insets.right,
						Integer.MAX_VALUE);
				size.height = (int)Math.min((long)size.height + (long)insets.top + insets.bottom,
						Integer.MAX_VALUE);
				return size;
			}
			

			/**
			 * {@inheritDoc}
			 */
			@Override
			public synchronized float getLayoutAlignmentX(Container target)
			{
				checkRequests();
				return xTotal.alignment;
			}
			

			/**
			 * {@inheritDoc}
			 */
			@Override
			public synchronized float getLayoutAlignmentY(Container target)
			{
				checkRequests();
				return yTotal.alignment;
			}
			

			/**
			 * {@inheritDoc}
			 */
			@Override
			public void layoutContainer(Container target)
			{
				int nChildren = target.getComponentCount();
				int[] xOffsets = new int[nChildren];
				int[] xSpans = new int[nChildren];
				int[] yOffsets = new int[nChildren];
				int[] ySpans = new int[nChildren];
				
				Dimension alloc = target.getSize();
				Insets in = target.getInsets();
				alloc.width -= in.left + in.right;
				alloc.height -= in.top + in.bottom;
				
				ComponentOrientation o = target.getComponentOrientation();
				int absoluteAxis = resolveAxis(axis,o);
				boolean ltr = (absoluteAxis != axis) ? o.isLeftToRight() : true;
				
				synchronized(this)
				{
					checkRequests();
					
					if(absoluteAxis == X_AXIS)
					{
						SizeRequirements.calculateTiledPositions(alloc.width,xTotal,xChildren,
								xOffsets,xSpans,ltr);
						SizeRequirements.calculateAlignedPositions(alloc.height,yTotal,yChildren,
								yOffsets,ySpans);
					}
					else
					{
						SizeRequirements.calculateAlignedPositions(alloc.width,xTotal,xChildren,
								xOffsets,xSpans,ltr);
						SizeRequirements.calculateTiledPositions(alloc.height,yTotal,yChildren,
								yOffsets,ySpans);
						
						int maxWidth = 0;
						for(int i = 0; i < xSpans.length; i++)
						{
							maxWidth = Math.max(maxWidth,xSpans[i]);
						}
						
						for(int i = 0; i < xSpans.length; i++)
						{
							xSpans[i] = maxWidth;
						}
					}
				}
				
				for(int i = 0; i < nChildren; i++)
				{
					Component c = target.getComponent(i);
					c.setBounds((int)Math.min((long)in.left + (long)xOffsets[i],Integer.MAX_VALUE),
							(int)Math.min((long)in.top + (long)yOffsets[i],Integer.MAX_VALUE),
							xSpans[i],ySpans[i]);
				}
			}
			

			void checkRequests()
			{
				if(xChildren == null || yChildren == null)
				{
					int n = target.getComponentCount();
					xChildren = new SizeRequirements[n];
					yChildren = new SizeRequirements[n];
					for(int i = 0; i < n; i++)
					{
						Component c = target.getComponent(i);
						if(!c.isVisible())
						{
							xChildren[i] = new SizeRequirements(0,0,0,c.getAlignmentX());
							yChildren[i] = new SizeRequirements(0,0,0,c.getAlignmentY());
							continue;
						}
						Dimension min = c.getMinimumSize();
						Dimension typ = c.getPreferredSize();
						Dimension max = c.getMaximumSize();
						xChildren[i] = new SizeRequirements(min.width,typ.width,max.width,
								c.getAlignmentX());
						yChildren[i] = new SizeRequirements(min.height,typ.height,max.height,
								c.getAlignmentY());
					}
					
					int absoluteAxis = resolveAxis(axis,target.getComponentOrientation());
					
					if(absoluteAxis == X_AXIS)
					{
						xTotal = SizeRequirements.getTiledSizeRequirements(xChildren);
						yTotal = SizeRequirements.getAlignedSizeRequirements(yChildren);
					}
					else
					{
						xTotal = SizeRequirements.getAlignedSizeRequirements(xChildren);
						yTotal = SizeRequirements.getTiledSizeRequirements(yChildren);
					}
				}
			}
			

			private int resolveAxis(int axis, ComponentOrientation o)
			{
				switch(axis)
				{
					case LINE_AXIS:
						return o.isHorizontal() ? X_AXIS : Y_AXIS;
					case PAGE_AXIS:
						return o.isHorizontal() ? Y_AXIS : X_AXIS;
					default:
						return axis;
				}
			}
		}
	}
}
