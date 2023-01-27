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
package xdev.ui.tree;


import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.RoundRectangle2D;
import java.io.File;

import javax.swing.Icon;
import javax.swing.UIManager;
import javax.swing.filechooser.FileSystemView;
import javax.swing.tree.TreePath;

import xdev.ui.XdevTree;
import xdev.util.ColorUtils;


public class DefaultXdevTreeManager implements XdevTreeManager
{
	private static Icon	defaultLeafIcon;
	private static Icon	defaultOpenFolderIcon;
	private static Icon	defaultClosedFolderIcon;
	
	private XdevTree	tree;
	
	
	public DefaultXdevTreeManager(final XdevTree tree)
	{
		super();
		
		this.tree = tree;
		
		if(defaultLeafIcon == null)
		{
			boolean useDefault = true;
			
			try
			{
				File f = new File("./").getAbsoluteFile();
				if(f.exists() && f.isDirectory())
				{
					Icon icon = FileSystemView.getFileSystemView().getSystemIcon(f);
					defaultOpenFolderIcon = icon;
					defaultClosedFolderIcon = icon;
					defaultLeafIcon = new DefaultLeafIcon(icon.getIconWidth(),icon.getIconHeight());
					
					useDefault = false;
				}
			}
			catch(Throwable t)
			{
				// ignore
			}
			
			if(useDefault)
			{
				defaultOpenFolderIcon = UIManager.getIcon("Tree.openIcon");
				defaultClosedFolderIcon = UIManager.getIcon("Tree.closedIcon");
				defaultLeafIcon = UIManager.getIcon("Tree.leafIcon");
			}
		}
	}
	
	
	@Override
	public Icon getIcon(XdevTreeNode node)
	{
		Icon icon = node.getIcon();
		if(icon != null)
		{
			return icon;
		}
		
		if(node.isLeaf())
		{
			icon = tree.getLeafIcon();
			if(icon != null)
			{
				return icon;
			}
			
			return defaultLeafIcon;
		}
		else if(tree.isExpanded(new TreePath(node.getPath())))
		{
			icon = tree.getOpenFolderIcon();
			if(icon != null)
			{
				return icon;
			}
			
			return defaultOpenFolderIcon;
		}
		else
		{
			icon = tree.getClosedFolderIcon();
			if(icon != null)
			{
				return icon;
			}
			
			return defaultClosedFolderIcon;
		}
	}
	
	
	@Override
	public String getCaption(XdevTreeNode node)
	{
		return node.getCaption();
	}
	
	
	
	protected static class DefaultLeafIcon implements Icon
	{
		final int	size;
		final int	arc;
		final int	x;
		final int	y;
		final int	width;
		final int	height;
		Color		draw;
		Color		fill;
		
		
		public DefaultLeafIcon(int width, int height)
		{
			this.size = 8;
			this.arc = 4;
			this.x = (width - size) / 2;
			this.y = (height - size) / 2;
			this.width = width;
			this.height = height;
		}
		
		
		public int getIconWidth()
		{
			return width;
		}
		
		
		public int getIconHeight()
		{
			return height;
		}
		
		
		public void paintIcon(Component c, Graphics g0, int x, int y)
		{
			Graphics2D g = (Graphics2D)g0;
			g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
			
			if(draw == null)
			{
				draw = UIManager.getColor("Tree.line");
				if(draw == null)
				{
					draw = UIManager.getColor("Tree.hash");
					if(draw == null)
					{
						draw = new Color(150,150,170);
					}
				}
				
				Color background = c.getBackground();
				if(background == null)
				{
					background = UIManager.getColor("Tree.background");
					if(background == null)
					{
						background = Color.white;
					}
				}
				
				fill = ColorUtils.getDifference(draw,background,0.75f);
			}
			
			Stroke stroke = g.getStroke();
			g.setStroke(new BasicStroke(0.5f));
			Shape shape = new RoundRectangle2D.Double(x + this.x,y + this.y,size,size,arc,arc);
			g.setPaint(fill);
			g.fill(shape);
			g.setPaint(draw);
			g.draw(shape);
			g.setStroke(stroke);
		}
	}
}
