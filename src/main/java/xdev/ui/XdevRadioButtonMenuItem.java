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
import javax.swing.JRadioButtonMenuItem;


/**
 * The standard radio button menu item in XDEV. Based on
 * {@link JRadioButtonMenuItem}.
 * 
 * @see JRadioButtonMenuItem
 * 
 * @author XDEV Software
 * 
 * @since 2.0
 */
public class XdevRadioButtonMenuItem extends JRadioButtonMenuItem
{
	/**
	 * 
	 */
	private static final long	serialVersionUID	= 8190541413113117423L;
	

	/**
	 * Creates a {@link XdevRadioButtonMenuItem} with no set text or icon.
	 */
	public XdevRadioButtonMenuItem()
	{
		super();
	}
	

	/**
	 * Creates a {@link XdevRadioButtonMenuItem} whose properties are taken from
	 * the {@link Action} supplied.
	 * 
	 * @param a
	 *            the {@link Action} on which to base the radio button menu item
	 */
	public XdevRadioButtonMenuItem(Action a)
	{
		super(a);
	}
	

	/**
	 * Creates a {@link XdevRadioButtonMenuItem} with the specified image and
	 * selection state, but no text.
	 * 
	 * @param icon
	 *            the image that the button should display
	 * @param selected
	 *            if true, the button is initially selected; otherwise, the
	 *            button is initially unselected
	 */
	public XdevRadioButtonMenuItem(Icon icon, boolean selected)
	{
		super(icon,selected);
	}
	

	/**
	 * Creates a {@link XdevRadioButtonMenuItem} with an icon.
	 * 
	 * @param icon
	 *            the <code>Icon</code> to display on the
	 *            {@link XdevRadioButtonMenuItem}
	 */
	public XdevRadioButtonMenuItem(Icon icon)
	{
		super(icon);
	}
	

	/**
	 * Creates a {@link XdevRadioButtonMenuItem} with the specified text and
	 * selection state.
	 * 
	 * @param text
	 *            the text of the <code>XdevRadioButtonMenuItem</code>
	 * @param selected
	 *            the selected state of the <code>XdevRadioButtonMenuItem</code>
	 */
	public XdevRadioButtonMenuItem(String text, boolean selected)
	{
		super(text,selected);
	}
	

	/**
	 * Creates a {@link XdevRadioButtonMenuItem} that has the specified text,
	 * image, and selection state. All other constructors defer to this one.
	 * 
	 * @param text
	 *            the string displayed on the radio button
	 * @param icon
	 *            the image that the button should display
	 * @param selected
	 *            the selected state of the <code>XdevRadioButtonMenuItem</code>
	 */
	public XdevRadioButtonMenuItem(String text, Icon icon, boolean selected)
	{
		super(text,icon,selected);
	}
	

	/**
	 * Creates a XdevRadioButtonMenuItem with the specified text and
	 * <code>Icon</code>.
	 * 
	 * @param text
	 *            the text of the {@link XdevRadioButtonMenuItem}
	 * @param icon
	 *            the icon to display on the {@link XdevRadioButtonMenuItem}
	 */
	public XdevRadioButtonMenuItem(String text, Icon icon)
	{
		super(text,icon);
	}
	

	/**
	 * Creates a {@link XdevRadioButtonMenuItem} with text.
	 * 
	 * @param text
	 *            the text of the {@link XdevRadioButtonMenuItem}
	 */
	public XdevRadioButtonMenuItem(String text)
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
}
