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


import java.awt.Color;
import java.awt.Graphics;
import java.awt.SystemColor;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import javax.swing.JPanel;


@BeanSettings(acceptChildren = true)
public class LockPanel extends JPanel
{
	public LockPanel()
	{
		super(null);
		setOpaque(false);
		
		addMouseListener(new MouseListener()
		{
			@Override
			public void mouseReleased(MouseEvent e)
			{
				consume(e);
			}
			
			
			@Override
			public void mousePressed(MouseEvent e)
			{
				consume(e);
			}
			
			
			@Override
			public void mouseExited(MouseEvent e)
			{
				consume(e);
			}
			
			
			@Override
			public void mouseEntered(MouseEvent e)
			{
				consume(e);
			}
			
			
			@Override
			public void mouseClicked(MouseEvent e)
			{
				consume(e);
			}
		});
		
		addMouseMotionListener(new MouseMotionListener()
		{
			@Override
			public void mouseMoved(MouseEvent e)
			{
				consume(e);
			}
			
			
			@Override
			public void mouseDragged(MouseEvent e)
			{
				consume(e);
			}
		});
		
		addMouseWheelListener(new MouseWheelListener()
		{
			@Override
			public void mouseWheelMoved(MouseWheelEvent e)
			{
				if(!isEnabled())
				{
					e.consume();
				}
			}
		});
	}
	
	
	private void consume(MouseEvent e)
	{
		if(!isEnabled())
		{
			e.consume();
		}
	}
	
	
	@Override
	public void setEnabled(boolean enabled)
	{
		super.setEnabled(enabled);
		setFocusable(!enabled);
	}
	
	
	@Override
	protected void paintComponent(Graphics g)
	{
		if(!isEnabled())
		{
			Color c = SystemColor.control;
			g.setColor(new Color(c.getRed(),c.getGreen(),c.getBlue(),150));
			g.fillRect(0,0,getWidth(),getHeight());
		}
	}
}
