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
package xdev.ui.event;


import java.awt.event.*;

import javax.swing.*;


/**
 * Key action adapter to observe the escape key.
 * 
 * @author XDEV Software
 * 
 */
public abstract class EscapeActionAdapter
{
	public EscapeActionAdapter(JComponent c)
	{
		KeyStroke ksEnter = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE,0);
		c.getActionMap().put("ESC",new AbstractAction()
		{
			public void actionPerformed(ActionEvent e)
			{
				escapeActionPerformed(e);
			}
		});
		c.getInputMap(JLayeredPane.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(ksEnter,"ESC");
	}
	

	/**
	 * Invoked when the escape key has been pressed.
	 */
	public abstract void escapeActionPerformed(ActionEvent e);
}
