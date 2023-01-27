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
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.LayoutManager;

import javax.swing.JPanel;


/**
 * The {@link XdevContainer} is the basic GUI container component in XDEV. Based
 * on {@link JPanel}.
 * 
 * @see JPanel
 * 
 * @author XDEV Software
 * 
 * @since 2.0
 */
@BeanSettings(acceptChildren = true, useXdevCustomizer = true)
public class XdevContainer extends XdevComponent
{
	/**
	 * Create a new {@link XdevContainer} with no layout manager.
	 * 
	 * <p>
	 * Alias for <code>XdevContainer(null)</code>
	 * </p>
	 * 
	 * @see #XdevContainer(LayoutManager)
	 * 
	 */
	public XdevContainer()
	{
		super();
	}


	/**
	 * Create a new {@link XdevContainer} with the specified layout manager.
	 * 
	 * @param layout
	 *            the LayoutManager to use
	 */
	public XdevContainer(LayoutManager layout)
	{
		super(layout);
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void paintImage(Graphics2D g)
	{
		paintBackground(g,0,0,getWidth(),getHeight());
		drawTexture(g,0,0,getWidth(),getHeight());
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
}
