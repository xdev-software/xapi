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


import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JButton;


/**
 * The standard button in XDEV. Based on {@link JButton}.
 * 
 * @see JButton
 * 
 * @author XDEV Software
 * 
 * @since 2.0
 */

@BeanSettings(useXdevCustomizer = true)
public class XdevButton extends JButton implements XdevFocusCycleComponent
{
	/**
	 * tabIndex is used to store the index for {@link XdevFocusCycleComponent}
	 * functionality.
	 */
	private int	tabIndex	= -1;
	

	/**
	 * Creates a button with no set text or icon.
	 */
	public XdevButton()
	{
		super();
	}
	

	/**
	 * Creates a button where properties are taken from the <code>Action</code>
	 * supplied.
	 * 
	 * @param a
	 *            the <code>Action</code> used to specify the new button
	 * 
	 * 
	 */
	public XdevButton(Action a)
	{
		super(a);
	}
	

	/**
	 * Creates a button with an icon.
	 * 
	 * @param icon
	 *            the Icon image to display on the button
	 * 
	 */
	public XdevButton(Icon icon)
	{
		super(icon);
	}
	

	/**
	 * Creates a button with initial text and an icon.
	 * 
	 * @param text
	 *            the text of the button
	 * @param icon
	 *            the Icon image to display on the button
	 */
	public XdevButton(String text, Icon icon)
	{
		super(text,icon);
	}
	

	/**
	 * Creates a button with text.
	 * 
	 * @param text
	 *            the text of the button
	 */
	public XdevButton(String text)
	{
		super(text);
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
	 * {@inheritDoc}
	 */
	@Override
	public int getTabIndex()
	{
		return tabIndex;
	}
	

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setTabIndex(int tabIndex)
	{
		if(this.tabIndex != tabIndex)
		{
			int oldValue = this.tabIndex;
			this.tabIndex = tabIndex;
			firePropertyChange(TAB_INDEX_PROPERTY,oldValue,tabIndex);
		}
	}
}
