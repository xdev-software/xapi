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
 * Settings enumeration for the behavior of a text component after it gained the
 * focus.
 * 
 * @author XDEV Software
 * @since 3.1
 */
public enum FocusGainedBehavior
{
	/**
	 * Lets the system handle the behavior.
	 * <p>
	 * This is the default behavior.
	 */
	SYSTEM_DEFAULT,

	/**
	 * The cursor is placed at the begin of the text comopnent.
	 */
	CURSOR_TO_BEGIN
	{
		@Override
		public void focusGained(JTextComponent txt)
		{
			txt.setCaretPosition(0);
		}
	},
	
	/**
	 * The cursor is placed at the end of the text comopnent.
	 */
	CURSOR_TO_END
	{
		@Override
		public void focusGained(JTextComponent txt)
		{
			txt.setCaretPosition(txt.getDocument().getLength());
		}
	},
	
	/**
	 * Selects all the text in the text component.
	 */
	SELECT_ALL
	{
		@Override
		public void focusGained(JTextComponent txt)
		{
			txt.selectAll();
		}
	};
	
	final static String	PROPERTY_NAME	= "focusGainedBehavior";
	

	/**
	 * Performs the appropriate action.
	 * 
	 * @param txt
	 *            the text component
	 */
	public void focusGained(JTextComponent txt)
	{
	}
}
