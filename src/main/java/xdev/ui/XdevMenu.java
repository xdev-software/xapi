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
import java.awt.event.MouseEvent;

import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.JMenu;
import javax.swing.JPopupMenu;
import javax.swing.event.MenuEvent;

import xdev.ui.event.MenuAdapter;


/**
 * The standard menu in XDEV. Based on {@link JMenu}.
 * <p>
 * It can also be used as a popup menu, see<br>
 * {@link #show(MouseEvent)} and {@link #show(Component, int, int)}.
 * 
 * 
 * @author XDEV Software
 * 
 * @since 2.0
 */
@BeanSettings(acceptChildren = true)
public class XdevMenu extends JMenu
{
	private static final long	serialVersionUID	= 1031336250131294263L;
	
	private JPopupMenu			popup;
	
	
	/**
	 * Constructs a new {@link XdevMenu} with no text.
	 */
	public XdevMenu()
	{
		super();
	}
	
	
	/**
	 * Constructs a new {@link XdevMenu} whose properties are taken from the
	 * <code>Action</code> supplied.
	 * 
	 * @param a
	 *            an {@link Action}
	 * 
	 */
	public XdevMenu(Action a)
	{
		super(a);
	}
	
	
	/**
	 * Constructs a new {@link XdevMenu} with the supplied string as its text
	 * and specified as a tear-off menu or not.
	 * 
	 * @param text
	 *            the text for the menu label
	 * @param b
	 *            can the menu be torn off (not yet implemented)
	 */
	public XdevMenu(String text, boolean b)
	{
		super(text,b);
	}
	
	
	/**
	 * Constructs a new {@link XdevMenu} with the supplied string as its text.
	 * 
	 * @param text
	 *            the text for the menu label
	 */
	public XdevMenu(String text)
	{
		super(text);
	}
	
	
	/**
	 * Shows this menu as a popup menu relative to the <code>event</code>'s
	 * source.
	 * 
	 * @see #show(Component, int, int)
	 */
	public void show(MouseEvent event)
	{
		show(event.getComponent(),event.getX(),event.getY());
	}
	
	
	/**
	 * Displays this menu as a popup menu at the position x,y in the coordinate
	 * space of the component invoker.
	 * 
	 * @param invoker
	 *            the component in whose space the popup menu is to appear
	 * @param x
	 *            the x coordinate in invoker's coordinate space at which the
	 *            popup menu is to be displayed
	 * 
	 * @param y
	 *            the y coordinate in invoker's coordinate space at which the
	 *            popup menu is to be displayed
	 */
	public void show(Component invoker, int x, int y)
	{
		if(popup == null)
		{
			popup = new JPopupMenu();
			
			addMenuListener(new MenuAdapter()
			{
				@Override
				public void menuSelected(MenuEvent e)
				{
					if(popup.getComponentCount() > 0)
					{
						moveComponents(popup,popup.getComponents(),XdevMenu.this);
					}
				}
			});
		}
		
		if(getMenuComponentCount() > 0)
		{
			moveComponents(this,getMenuComponents(),popup);
		}
		popup.show(invoker,x,y);
	}
	
	
	void moveComponentsTo(JComponent target)
	{
		if(popup != null && popup.getComponentCount() > 0)
		{
			moveComponents(popup,popup.getComponents(),target);
		}
		else
		{
			moveComponents(this,getMenuComponents(),target);
		}
	}
	
	
	static void moveComponents(JComponent source, Component[] components, JComponent target)
	{
		source.removeAll();
		target.removeAll();
		if(components != null)
		{
			for(Component component : components)
			{
				target.add(component);
			}
		}
	}
	
	
	/**
	 * {@inheritDoc}
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
}
