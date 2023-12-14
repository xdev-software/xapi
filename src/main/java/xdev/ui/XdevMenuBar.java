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
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.border.Border;


/**
 * The standard menubar in XDEV. Based on {@link JMenuBar}.
 * 
 * @see JMenuBar
 * 
 * @author XDEV Software
 * 
 * @since 2.0
 */
@BeanSettings(acceptChildren = true)
public class XdevMenuBar extends JMenuBar
{
	/**
	 * Default height used when this MenuBar has no content.
	 */
	public final static int	DEFAULT_HEIGHT	= 20;
	
	private JMenu			helpMenu;
	
	
	/**
	 * Creates a new menu bar.
	 */
	public XdevMenuBar()
	{
		super();
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
			d.height = DEFAULT_HEIGHT;
		}
		return d;
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
	
	
	/**
	 * Workaround implementation for {@link JMenuBar#setHelpMenu(JMenu)}. It is
	 * public API but throws only an error "not yet implemented".
	 * 
	 * @see <a
	 *      href="http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=4087846">http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=4087846</a>
	 * 
	 * @since 3.1
	 */
	@Override
	public void setHelpMenu(JMenu menu)
	{
		if(helpMenu != null)
		{
			remove(helpMenu);
		}
		helpMenu = menu;
		super.add(helpMenu);
	}
	
	
	/**
	 * Workaround implementation for {@link JMenuBar#getHelpMenu()}. It is
	 * public API but throws only an error "not yet implemented".
	 * 
	 * @see <a
	 *      href="http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=4087846">http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=4087846</a>
	 * 
	 * @since 3.1
	 */
	@Override
	public JMenu getHelpMenu()
	{
		return helpMenu;
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public JMenu add(JMenu menu)
	{
		if(helpMenu != null)
		{
			return (JMenu)add(menu,getComponentCount() - 1);
		}
		else
		{
			return super.add(menu);
		}
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void remove(int index)
	{
		if(getComponent(index) == helpMenu)
		{
			helpMenu = null;
		}
		
		super.remove(index);
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void removeAll()
	{
		super.removeAll();
		
		helpMenu = null;
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
	
	
	/**
	 * {@inheritDoc}
	 */
	@BeanProperty(category = DefaultBeanCategories.MISC)
	@Override
	public void setBorder(Border border)
	{
		super.setBorder(border);
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@BeanProperty(category = DefaultBeanCategories.MISC)
	@Override
	public void setFocusable(boolean focusable)
	{
		super.setFocusable(focusable);
	}
}
