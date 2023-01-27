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
import java.awt.Image;
import java.awt.Window;

import javax.swing.Icon;
import javax.swing.JFrame;
import javax.swing.WindowConstants;

import xdev.Application;
import xdev.ui.laf.LookAndFeel;
import xdev.ui.laf.LookAndFeelManager;


/**
 * The {@link XdevFrame} is a top-level window with a title and a border in
 * XDEV. Based on {@link JFrame}.
 * 
 * 
 * @see JFrame
 * @see XdevRootPaneContainer
 * 
 * @author XDEV Software
 * 
 * @since 2.0
 */
public class XdevFrame extends JFrame implements XdevRootPaneContainer
{
	protected XdevWindow	window;
	
	
	/**
	 * Creates a new, initially invisible Frame.
	 * 
	 * <p>
	 * Alias for <code>XdevFrame(String title, Icon icon, String id)</code>
	 * </p>
	 * 
	 * @param window
	 *            the {@link XdevWindow} for this {@link XdevFrame}
	 * 
	 */
	public XdevFrame(XdevWindow window)
	{
		this(window.getTitle(),window.getIcon());
		
		setXdevWindow(window);
	}
	
	
	/**
	 * Creates a new, initially invisible Frame with the specified
	 * <code>title</code>, <code>icon</code> and <code>id</code>.
	 * 
	 * <p>
	 * Alias for <code>XdevFrame(String title, Image icon, String id)</code>
	 * </p>
	 * 
	 * @param title
	 *            the title for the frame
	 * 
	 * @param icon
	 *            the icon to be displayed as the icon for this frame
	 */
	public XdevFrame(String title, Icon icon)
	{
		this(title,icon == null ? null : GraphicUtils.createImageFromIcon(icon));
	}
	
	
	/**
	 * Creates a new, initially invisible Frame with the specified
	 * <code>title</code>, <code>icon</code> and <code>id</code>.
	 * 
	 * <p>
	 * This constructor sets the default close operation to
	 * {@link WindowConstants#DO_NOTHING_ON_CLOSE}.
	 * </p>
	 * 
	 * @param title
	 *            the title for the frame
	 * 
	 * @param icon
	 *            the image to be displayed as the icon for this frame
	 * 
	 */
	public XdevFrame(String title, Image icon)
	{
		super(title);
		
		if(icon == null)
		{
			setIconImage(Application.getIconImage());
		}
		else
		{
			setIconImage(icon);
		}
		
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		
		setFocusTraversalPolicy(new XdevFocusTraversalPolicy(this));
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public final void setXdevWindow(XdevWindow window)
	{
		cleanup();
		
		this.window = window;
		window.setOwner(this);
		
		setExtendedState(window.getExtendedState());
		if(!isMaximized())
		{
			setBounds(window.getX(),window.getY(),window.getWidth(),window.getHeight());
		}
		
		setResizable(window.isResizable());
		setTitle(window.getTitle());
		setIconImages(Util.createImageList(window));
		setJMenuBar(window.getJMenuBar());
		getRootPane().setDefaultButton(window.getDefaultButton());
		
		setContentPane(Util.createContainer(this,window));
		
		if(window.isMinimumSizeSet())
		{
			try
			{
				setMinimumSize(window.getMinimumSize());
			}
			catch(Throwable t)
			{
			}
		}
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public XdevWindow getXdevWindow()
	{
		return window;
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Window getWindow()
	{
		return this;
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setVisible(boolean b)
	{
		super.setVisible(b);
		
		if(!b)
		{
			cleanup();
		}
	}
	
	
	private void cleanup()
	{
		if(window != null)
		{
			window.setOwner(null);
			window = null;
		}
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void pack()
	{
		if(isMaximized())
		{
			invalidate();
			validate();
			if(window != null)
			{
				setSize(window.getPreferredSize());
			}
		}
		else
		{
			super.pack();
		}
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setLocationRelativeTo(Component c)
	{
		if(!isMaximized())
		{
			super.setLocationRelativeTo(c);
		}
	}
	
	
	/**
	 * Returns <code>true</code> if this frame is maximized.
	 * 
	 * @return <code>true</code> if this frame is maximized, otherwise
	 *         <code>false</code>
	 */
	public boolean isMaximized()
	{
		switch(getExtendedState())
		{
			case XdevRootPaneContainer.MAXIMIZED_BOTH:
			case XdevRootPaneContainer.MAXIMIZED_HORIZ:
			case XdevRootPaneContainer.MAXIMIZED_VERT:
				return true;
				
			default:
				return false;
		}
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setExtendedState(int state)
	{
		LookAndFeel laf = LookAndFeelManager.getLookAndFeel();
		if(laf != null)
		{
			laf.extendedStateWillChange(this,state);
		}
		
		super.setExtendedState(state);
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void close()
	{
		setVisible(false);
		dispose();
	}
}
