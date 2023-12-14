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


import javax.swing.text.JTextComponent;


/**
 * Enum to control the cursor after setText() was called.
 * 
 * @author XDEV Software
 * @since 4.0
 */
public enum TextChangedBehavior
{
	/**
	 * Lets the system handle the behavior.
	 * <p>
	 * This is the default behavior.
	 */
	SYSTEM_DEFAULT,
	
	/**
	 * Scrolls to the first letter and places the cursor before the text.
	 */
	CURSOR_TO_BEGIN
	{
		@Override
		public void textChanged(JTextComponent txt)
		{
			txt.setCaretPosition(0);
		}
	},
	
	/**
	 * Scrolls to the last letter and places the cursor after the text.
	 */
	CURSOR_TO_END
	{
		@Override
		public void textChanged(JTextComponent txt)
		{
			txt.setCaretPosition(txt.getDocument().getLength());
		}
	};
	
	final static String	PROPERTY_NAME	= "textChangedBehavior";
	
	
	/**
	 * Performs the appropriate action.
	 * 
	 * @param txt
	 *            the text component
	 */
	public void textChanged(JTextComponent txt)
	{
	}
}
