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

import xdev.ui.event.FirstShowAdapter;


/**
 * 
 * Little helper class to focus a specific {@link Component} after a window will
 * be opened.
 * <p>
 * Usage:
 * 
 * <pre>
 * Component componentToFocus = ...;
 * 
 * XdevFocus focus = new XdevFocus();
 * focus.setTarget(componentToFocus);
 * </pre>
 * 
 * or just
 * 
 * <pre>
 * new XdevFocus(componentToFocus);
 * </pre>
 * 
 * @author XDEV Software
 * @since 3.1
 */
public class XdevFocus
{
	private Component	target;
	
	
	/**
	 * Creates a new focus helper. Don't forget to set the target:
	 * {@link #setTarget(Component)}
	 */
	public XdevFocus()
	{
	}
	
	
	/**
	 * Creates a new focus helper, and calls {@link #setTarget(Component)} for
	 * <code>target</code>.
	 * 
	 * @param target
	 *            the component to focus
	 */
	public XdevFocus(Component target)
	{
		setTarget(target);
	}
	
	
	/**
	 * Sets the target component of this focus helper.
	 * 
	 * @param target
	 *            the component to focus
	 */
	public void setTarget(final Component target)
	{
		this.target = target;
		
		if(target != null)
		{
			FirstShowAdapter.invokeLater(target,new Runnable()
			{
				@Override
				public void run()
				{
					target.requestFocus();
				}
			});
		}
	}
	
	
	/**
	 * Returns the target component which will be focused.
	 * 
	 * @return the target component
	 */
	public Component getTarget()
	{
		return target;
	}
}
