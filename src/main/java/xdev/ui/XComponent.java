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


import java.awt.LayoutManager;

import javax.swing.JPanel;


/**
 * The {@link XComponent} is a basic GUI component. Based on {@link JPanel}.
 * 
 * @see JPanel
 * 
 * @author XDEV Software
 * 
 * @since 2.0
 */
public class XComponent extends JPanel
{
	/**
	 * Create a new {@link XComponent} with no layout manager.
	 * 
	 * <p>
	 * Alias for <code>XComponent(null)</code>
	 * </p>
	 * 
	 * @see #XComponent(LayoutManager)
	 * 
	 */
	public XComponent()
	{
		this(null);
	}
	

	/**
	 * Create a new {@link XComponent} with the specified layout manager.
	 * 
	 * @param layout
	 *            the LayoutManager to use
	 */
	public XComponent(LayoutManager layout)
	{
		super(layout);
		setOpaque(false);
	}
}
