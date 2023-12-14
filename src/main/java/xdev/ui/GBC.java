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


import java.awt.Component;
import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JPanel;


public class GBC extends GridBagConstraints
{
	public GBC()
	{
	}
	

	public GBC(int gridx, int gridy, int gridwidth, int gridheight, double weightx, double weighty,
			int anchor, int fill, int insets)
	{
		this(gridx,gridy,gridwidth,gridheight,weightx,weighty,anchor,fill,new Insets(insets,insets,
				insets,insets));
	}
	

	public GBC(int gridx, int gridy, int gridwidth, int gridheight, double weightx, double weighty,
			int anchor, int fill, int top, int left, int bottom, int right)
	{
		this(gridx,gridy,gridwidth,gridheight,weightx,weighty,anchor,fill,new Insets(top,left,
				bottom,right));
	}
	

	public GBC(int gridx, int gridy, int gridwidth, int gridheight, double weightx, double weighty,
			int anchor, int fill, Insets insets)
	{
		this(gridx,gridy,gridwidth,gridheight,weightx,weighty,anchor,fill,insets,0,0);
	}
	

	public GBC(int gridx, int gridy, int gridwidth, int gridheight, double weightx, double weighty,
			int anchor, int fill, Insets insets, int ipadx, int ipady)
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
	
	public final static GBC	constraints	= new GBC();
	

	public static GBC set(int gridx, int gridy, int gridwidth, int gridheight, double weightx,
			double weighty, int anchor, int fill, int insets)
	{
		return set(gridx,gridy,gridwidth,gridheight,weightx,weighty,anchor,fill,new Insets(insets,
				insets,insets,insets));
	}
	

	public static GBC set(int gridx, int gridy, int gridwidth, int gridheight, double weightx,
			double weighty, int anchor, int fill, int top, int left, int bottom, int right)
	{
		return set(gridx,gridy,gridwidth,gridheight,weightx,weighty,anchor,fill,new Insets(top,
				left,bottom,right));
	}
	

	public static GBC set(int gridx, int gridy, int gridwidth, int gridheight, double weightx,
			double weighty, int anchor, int fill, Insets insets)
	{
		return set(gridx,gridy,gridwidth,gridheight,weightx,weighty,anchor,fill,insets,0,0);
	}
	

	public static GBC set(int gridx, int gridy, int gridwidth, int gridheight, double weightx,
			double weighty, int anchor, int fill, Insets insets, int ipadx, int ipady)
	{
		constraints.gridx = gridx;
		constraints.gridy = gridy;
		constraints.gridwidth = gridwidth;
		constraints.gridheight = gridheight;
		constraints.weightx = weightx;
		constraints.weighty = weighty;
		constraints.anchor = anchor;
		constraints.fill = fill;
		constraints.insets = insets;
		constraints.ipadx = ipadx;
		constraints.ipady = ipady;
		return constraints;
	}
	

	public static void addSpacer(Container parent)
	{
		addSpacer(parent,true,true);
	}
	

	public static void addSpacer(Container parent, boolean vertical, boolean horizontal)
	{
		GridBagLayout gbl = (GridBagLayout)parent.getLayout();
		
		int x = 1;
		int y = 1;
		
		for(Component child : parent.getComponents())
		{
			GridBagConstraints constraints = gbl.getConstraints(child);
			if(constraints != null)
			{
				x = Math.max(x,constraints.gridx + constraints.gridwidth);
				y = Math.max(y,constraints.gridy + constraints.gridheight);
				
				if(horizontal && constraints.weightx > 0)
				{
					horizontal = false;
				}
				if(vertical && constraints.weighty > 0)
				{
					vertical = false;
				}
			}
		}
		
		if(horizontal)
		{
			set(x,1,1,Math.max(1,y - 1),1.0,0.0,CENTER,BOTH,0,0,0,0);
			parent.add(createSpacer(),constraints);
		}
		
		if(vertical)
		{
			set(1,y,Math.max(1,x - 1),1,0.0,1.0,CENTER,BOTH,0,0,0,0);
			parent.add(createSpacer(),constraints);
		}
	}
	

	public static Component createSpacer()
	{
		JPanel pnl = new JPanel(null);
		pnl.setOpaque(false);
		return pnl;
	}
}
