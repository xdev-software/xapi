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
package xdev.lang.cmd;


import java.awt.Component;
import java.awt.Point;

import javax.swing.FocusManager;

import xdev.Application;
import xdev.ui.UIUtils;
import xdev.ui.XdevDialog;
import xdev.ui.XdevFrame;
import xdev.ui.XdevRootPaneContainer;
import xdev.ui.XdevScreen;
import xdev.ui.XdevWindow;
import xdev.ui.XdevWindowContainer;


public abstract class OpenWindow extends XdevCommandObject
{
	public static enum ContainerType
	{
		CURRENT_CONTAINER,
		
		FRAME,
		
		DIALOG,
		
		SCREEN,
		
		WINDOW_CONTAINER
	}
	
	private XdevWindow		window			= null;
	private ContainerType	containerType	= null;
	private boolean			modal			= false;
	private Component		owner			= null;
	private Point			location		= null;
	
	
	public OpenWindow(Object... args)
	{
		super(args);
	}
	
	
	public void setXdevWindow(XdevWindow window)
	{
		this.window = window;
	}
	
	
	public void setContainerType(ContainerType containerType)
	{
		this.containerType = containerType;
	}
	
	
	public void setModal(boolean modal)
	{
		this.modal = modal;
	}
	
	
	public void setOwner(Component owner)
	{
		this.owner = owner;
	}
	
	
	/**
	 * @param x
	 * @param y
	 * 
	 * @since 3.1
	 */
	public void setLocation(int x, int y)
	{
		this.location = new Point(x,y);
	}
	
	
	@Override
	public void execute()
	{
		if(window == null)
		{
			CommandException.throwMissingParameter("contentPane");
		}
		
		if(containerType == null)
		{
			CommandException.throwMissingParameter("containerType");
		}
		
		final Component relativeOwner = owner;
		if(owner == null)
		{
			FocusManager fm = FocusManager.getCurrentManager();
			owner = fm.getFocusOwner();
			if(owner == null)
			{
				owner = UIUtils.getActiveWindow();
			}
		}
		
		switch(containerType)
		{
			case CURRENT_CONTAINER:
			{
				XdevRootPaneContainer container = UIUtils.getParentOfClass(
						XdevRootPaneContainer.class,owner);
				if(container == null)
				{
					container = Application.getContainer();
				}
				
				container.setXdevWindow(window);
				container.pack();
				setLocation(container,relativeOwner);
			}
			break;
			
			case FRAME:
			{
				XdevFrame frame = new XdevFrame(window);
				frame.pack();
				setLocation(frame,relativeOwner);
				frame.setVisible(true);
			}
			break;
			
			case DIALOG:
			{
				XdevDialog dialog = new XdevDialog(owner,modal,window);
				dialog.pack();
				setLocation(dialog,relativeOwner);
				dialog.setVisible(true);
			}
			break;
			
			case SCREEN:
			{
				XdevScreen screen = new XdevScreen(owner,modal,window);
				screen.pack();
				setLocation(screen,relativeOwner);
				screen.setVisible(true);
			}
			break;
			
			case WINDOW_CONTAINER:
			{
				XdevWindowContainer windowContainer = (XdevWindowContainer)owner;
				windowContainer.setXdevWindow(window);
			}
			break;
		}
	}
	
	
	private void setLocation(XdevRootPaneContainer container, Component relativeOwner)
	{
		boolean maximized = false;
		switch(container.getExtendedState())
		{
			case XdevRootPaneContainer.MAXIMIZED_VERT:
			case XdevRootPaneContainer.MAXIMIZED_HORIZ:
			case XdevRootPaneContainer.MAXIMIZED_BOTH:
				maximized = true;
		}
		
		if(location != null && !maximized)
		{
			container.setLocation(location.x,location.y);
		}
		else
		{
			container.setLocationRelativeTo(relativeOwner);
		}
	}
	
	
	public XdevWindow getWindow()
	{
		return window;
	}
}
