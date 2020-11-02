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


import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JMenuItem;


/**
 * The standard menu item in XDEV. Based on {@link JMenuItem}.
 * 
 * 
 * @author XDEV Software
 * 
 * @since 2.0
 */
public class XdevMenuItem extends JMenuItem
{
	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1155191658716015657L;
	

	/**
	 * Creates a {@link XdevMenuItem} with no set text or icon.
	 */
	public XdevMenuItem()
	{
		super();
	}
	

	/**
	 * Creates a {@link XdevMenuItem} whose properties are taken from the
	 * specified <code>Action</code>.
	 * 
	 * @param a
	 *            the action of the {@link XdevMenuItem}
	 */
	public XdevMenuItem(Action a)
	{
		super(a);
	}
	

	/**
	 * Creates a {@link XdevMenuItem} with the specified icon.
	 * 
	 * @param icon
	 *            the icon of the {@link XdevMenuItem}
	 */
	public XdevMenuItem(Icon icon)
	{
		super(icon);
	}
	

	/**
	 * Creates a {@link XdevMenuItem} with the specified text and icon.
	 * 
	 * @param text
	 *            the text of the {@link XdevMenuItem}
	 * @param icon
	 *            the icon of the {@link XdevMenuItem}
	 */
	public XdevMenuItem(String text, Icon icon)
	{
		super(text,icon);
	}
	

	/**
	 * Creates a {@link XdevMenuItem} with the specified text and keyboard
	 * mnemonic.
	 * 
	 * @param text
	 *            the text of the {@link XdevMenuItem}
	 * @param mnemonic
	 *            the keyboard mnemonic for the {@link XdevMenuItem}
	 */
	public XdevMenuItem(String text, int mnemonic)
	{
		super(text,mnemonic);
	}
	

	/**
	 * Creates a {@link XdevMenuItem} with the specified text.
	 * 
	 * @param text
	 *            the text of the {@link XdevMenuItem}
	 */
	public XdevMenuItem(String text)
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
