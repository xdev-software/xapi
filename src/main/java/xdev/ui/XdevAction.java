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


import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.KeyStroke;


public abstract class XdevAction extends AbstractAction
{
	public final static String	TOGGLE_ACTION	= "ToggleAction";


	protected void setToggleAction(boolean b)
	{
		putValue(TOGGLE_ACTION,b);
	}


	public boolean isToggleAction()
	{
		return Boolean.TRUE.equals(getValue(TOGGLE_ACTION));
	}


	public void setName(String name)
	{
		putValue(NAME,name);
	}


	public String getName()
	{
		return (String)getValue(NAME);
	}


	public void setMnemonic(char mnemonic)
	{
		putValue(MNEMONIC_KEY,(int)mnemonic);
	}


	public void setShortcut(KeyStroke keyStroke)
	{
		putValue(ACCELERATOR_KEY,keyStroke);
	}


	public KeyStroke getShortcut()
	{
		return (KeyStroke)getValue(ACCELERATOR_KEY);
	}


	public void setIcon(Icon icon)
	{
		putValue(SMALL_ICON,icon);
	}


	public Icon getIcon()
	{
		return (Icon)getValue(SMALL_ICON);
	}


	public void setToolTipText(String ttt)
	{
		ttt = ttt.trim();
		if(ttt.length() > 0)
		{
			putValue(SHORT_DESCRIPTION,ttt);
		}
	}


	public String getToolTipText()
	{
		String toolTip = (String)getValue(Action.SHORT_DESCRIPTION);
		if(toolTip == null || toolTip.trim().length() == 0)
		{
			toolTip = (String)getValue(Action.NAME);
		}

		if(toolTip != null)
		{
			toolTip = toolTip.trim();

			if(toolTip.length() > 0)
			{
				KeyStroke keyStroke = getShortcut();
				if(keyStroke != null)
				{
					StringBuilder sb = new StringBuilder();
					sb.append(toolTip);
					sb.append(" (");
					int modifiers = keyStroke.getModifiers();
					if(modifiers > 0)
					{
						sb.append(KeyEvent.getKeyModifiersText(modifiers));
						sb.append("+");
					}
					int keyCode = keyStroke.getKeyCode();
					if(keyCode != 0)
					{
						sb.append(KeyEvent.getKeyText(keyCode));
					}
					else
					{
						sb.append(keyStroke.getKeyChar());
					}
					sb.append(")");
					toolTip = sb.toString();
				}

				return toolTip;
			}
		}

		return null;
	}


	public boolean isSelected()
	{
		return Boolean.TRUE.equals(getValue(SELECTED_KEY));
	}


	public void setSelected(boolean b)
	{
		putValue(SELECTED_KEY,b);
	}


	public void toggleSelected()
	{
		setSelected(!isSelected());
	}


	public void toggleEnabled()
	{
		setEnabled(!isEnabled());
	}
}
