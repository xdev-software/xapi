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
import java.beans.Beans;


/**
 * @deprecated Old table layout component, use {@link GridBagLayout} instead
 * 
 * 
 * @author XDEV Software
 * 
 */
@Deprecated
public class XdevDesignLayoutTable extends XdevContainer
{
	public XdevDesignLayoutTable()
	{
		super(null);
		
		setLayout(new DesignLayout());
	}
	
	
	@Override
	public void setLayout(LayoutManager mgr)
	{
		if(mgr instanceof DesignLayout)
		{
			super.setLayout(mgr);
		}
	}
	
	
	@Override
	protected void addImpl(Component comp, Object constraints, int index)
	{
		if(!(comp instanceof XdevLayoutCell) && !Beans.isDesignTime())
		{
			throw new IllegalArgumentException("Not a XdevLayoutCell");
		}
		
		super.addImpl(comp,constraints,index);
	}
	
	
	/*
	 * Opaque is always false to avoid artifacts
	 */
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isOpaque()
	{
		return false;
	}
	
	private boolean				firstLayout		= true;
	private Dimension			lastLayoutSize	= new Dimension();
	private XdevLayoutCell[]	cells			= null;
	private LayoutInfo			colInfo, rowInfo;
	
	
	
	private class LayoutInfo
	{
		int			count, index;
		double		fixedSum, weightSum, weightSumInverted, initialSum;
		
		InfoPart[]	part;
		
		
		LayoutInfo(int count)
		{
			this.count = count;
			part = new InfoPart[count];
			index = 0;
			fixedSum = 0d;
			weightSum = 0d;
			initialSum = 0d;
		}
		
		
		void addInfo(InfoPart infoPart)
		{
			part[index++] = infoPart;
			
			if(infoPart.fixed)
			{
				fixedSum += infoPart.initialSize;
			}
			else
			{
				weightSum += infoPart.weight;
				initialSum += infoPart.initialSize;
			}
		}
		
		
		void initInverted()
		{
			weightSumInverted = 0d;
			
			for(int i = 0; i < count; i++)
			{
				if(!part[i].fixed)
				{
					part[i].weightInverted = 1d / (part[i].weight / weightSum);
					weightSumInverted += part[i].weightInverted;
				}
			}
		}
		
		
		void layout(int size)
		{
			int position = 0;
			double space = size - fixedSum;
			
			for(index = 0; index < count; index++)
			{
				part[index].position = position;
				
				if(part[index].fixed)
				{
					part[index].size = (int)part[index].initialSize;
				}
				else
				{
					double diff = space - initialSum;
					double factor = diff >= 0 ? part[index].weight / weightSum
							: part[index].weightInverted / weightSumInverted;
					part[index].size = (int)Math.round(Math.max(part[index].minimumSize,
							part[index].initialSize + diff * factor));
				}
				
				position += part[index].size;
			}
		}
	}
	
	
	
	private class InfoPart
	{
		boolean	fixed;
		double	initialSize, minimumSize, weight, weightInverted;
		int		position, size;
		
		
		InfoPart(int initialSize)
		{
			fixed = true;
			this.initialSize = initialSize;
		}
		
		
		InfoPart(int initialSize, double weight, int minimumSize)
		{
			fixed = false;
			this.initialSize = initialSize;
			this.weight = weight;
			this.minimumSize = minimumSize;
		}
	}
	
	
	private void initLayout()
	{
		colInfo = new LayoutInfo(colCount());
		
		for(int col = 0; col < colInfo.count; col++)
		{
			int weight = 0;
			for(int i = 0; i < cells.length; i++)
			{
				if(cells[i].getCol() == col && cells[i].getColSpan() == 1)
				{
					weight = Math.max(weight,cells[i].getWeightX());
				}
			}
			
			XdevLayoutCell cell = getFirstNonStrechedColCell(col);
			if(cell == null)
			{
				for(int row = 0; row < rowCount() && cell == null; row++)
				{
					cell = getCell(row,col);
				}
			}
			
			if(cell != null)
			{
				if(weight == 0)
				{
					colInfo.addInfo(new InfoPart(cell.cellRect.width));
				}
				else
				{
					colInfo.addInfo(new InfoPart(cell.cellRect.width,weight,
							cell.getMinimumSize().width));
				}
			}
			else
			{
				colInfo.addInfo(new InfoPart(1,0.0,1));
			}
		}
		
		colInfo.initInverted();
		
		rowInfo = new LayoutInfo(rowCount());
		
		for(int row = 0; row < rowInfo.count; row++)
		{
			int weight = 0;
			for(int i = 0; i < cells.length; i++)
			{
				if(cells[i].getRow() == row && cells[i].getRowSpan() == 1)
				{
					weight = Math.max(weight,cells[i].getWeightY());
				}
			}
			
			XdevLayoutCell cell = getFirstNonStrechedRowCell(row);
			if(cell == null)
			{
				for(int col = 0; col < colCount() && cell == null; col++)
				{
					cell = getCell(row,col);
				}
			}
			
			if(weight == 0)
			{
				rowInfo.addInfo(new InfoPart(cell.cellRect.height));
			}
			else
			{
				rowInfo.addInfo(new InfoPart(cell.cellRect.height,weight,
						cell.getMinimumSize().height));
			}
		}
		
		rowInfo.initInverted();
	}
	
	
	private int rowCount()
	{
		int c = 0;
		for(int i = 0; i < cells.length; i++)
		{
			int cdc = cells[i].getRow() + cells[i].getRowSpan();
			c = Math.max(c,cdc);
		}
		return c;
	}
	
	
	private int colCount()
	{
		int c = 0;
		for(int i = 0; i < cells.length; i++)
		{
			int cdc = cells[i].getCol() + cells[i].getColSpan();
			c = Math.max(c,cdc);
		}
		return c;
	}
	
	
	private XdevLayoutCell getCell(int row, int col)
	{
		for(int i = 0; i < cells.length; i++)
		{
			if(cells[i].getRow() == row && cells[i].getCol() == col)
			{
				return cells[i];
			}
		}
		
		return null;
	}
	
	
	private XdevLayoutCell getFirstNonStrechedColCell(int col)
	{
		for(int i = 0; i < cells.length; i++)
		{
			if(cells[i].getCol() == col && cells[i].getColSpan() == 1)
			{
				return cells[i];
			}
		}
		
		return null;
	}
	
	
	private XdevLayoutCell getFirstNonStrechedRowCell(int row)
	{
		for(int i = 0; i < cells.length; i++)
		{
			if(cells[i].getRow() == row && cells[i].getRowSpan() == 1)
			{
				return cells[i];
			}
		}
		
		return null;
	}
	
	
	
	private class DesignLayout implements LayoutManager2
	{
		public void layoutContainer(Container parent)
		{
			synchronized(getTreeLock())
			{
				Dimension tableSize = getSize();
				if(!lastLayoutSize.equals(tableSize))
				{
					int count = getComponentCount();
					if(cells == null || cells.length != count)
					{
						cells = new XdevLayoutCell[count];
						for(int i = 0; i < count; i++)
						{
							cells[i] = (XdevLayoutCell)getComponent(i);
						}
					}
					
					lastLayoutSize.setSize(tableSize);
					
					if(firstLayout)
					{
						initLayout();
					}
					
					colInfo.layout(tableSize.width);
					rowInfo.layout(tableSize.height);
					
					for(int col = 0; col < colInfo.count; col++)
					{
						for(int row = 0; row < rowInfo.count; row++)
						{
							XdevLayoutCell cell = getCell(row,col);
							
							if(cell != null)
							{
								cell.cellRect.x = colInfo.part[cell.getCol()].position;
								cell.cellRect.y = rowInfo.part[cell.getRow()].position;
								
								cell.cellRect.width = 0;
								for(int i = 0; i < cell.getColSpan(); i++)
								{
									cell.cellRect.width += colInfo.part[cell.getCol() + i].size;
								}
								
								cell.cellRect.height = 0;
								for(int i = 0; i < cell.getRowSpan(); i++)
								{
									cell.cellRect.height += rowInfo.part[cell.getRow() + i].size;
								}
							}
						}
					}
					
					for(int i = 0; i < cells.length; i++)
					{
						Rectangle original = cells[i].getBounds();
						
						Rectangle r = new Rectangle(cells[i].cellRect);
						Insets ci = cells[i].getPadding();
						r.x += ci.left;
						r.y += ci.top;
						r.width -= ci.left + ci.right;
						r.height -= ci.top + ci.bottom;
						r.width = Math.max(1,r.width);
						r.height = Math.max(1,r.height);
						if(firstLayout || !original.equals(r))
						{
							cells[i].setBounds(r);
						}
					}
					
					firstLayout = false;
				}
			}
		}
		
		
		public void addLayoutComponent(String name, Component comp)
		{
		}
		
		
		public void addLayoutComponent(Component comp, Object constraints)
		{
		}
		
		
		public void removeLayoutComponent(Component comp)
		{
		}
		
		
		public Dimension preferredLayoutSize(Container parent)
		{
			return new Dimension(0,0);
		}
		
		
		public Dimension minimumLayoutSize(Container parent)
		{
			return new Dimension(0,0);
		}
		
		
		public Dimension maximumLayoutSize(Container target)
		{
			return null;
		}
		
		
		public void invalidateLayout(Container target)
		{
		}
		
		
		public float getLayoutAlignmentX(Container target)
		{
			return 0.5f;
		}
		
		
		public float getLayoutAlignmentY(Container target)
		{
			return 0.5f;
		}
	}
}
