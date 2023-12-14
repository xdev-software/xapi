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
import javax.swing.JCheckBoxMenuItem;


public class XdevCheckBoxMenuItem extends JCheckBoxMenuItem
{
	public XdevCheckBoxMenuItem()
	{
		super();
	}


	public XdevCheckBoxMenuItem(Action a)
	{
		super(a);
	}


	public XdevCheckBoxMenuItem(Icon icon)
	{
		super(icon);
	}


	public XdevCheckBoxMenuItem(String text, boolean selected)
	{
		super(text,selected);
	}


	public XdevCheckBoxMenuItem(String text, Icon icon, boolean selected)
	{
		super(text,icon,selected);
	}


	public XdevCheckBoxMenuItem(String text, Icon icon)
	{
		super(text,icon);
	}


	public XdevCheckBoxMenuItem(String text)
	{
		super(text);
	}


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
