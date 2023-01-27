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


/**
 * Simplification of the {@link KeyListener} interface.
 * <p>
 * In this key listener adapter it is only necessary to handle one method:
 * {@link #keyActionPerformed(KeyEvent)}.
 * 
 * @author XDEV Software
 */
public abstract class KeyActionAdapter implements KeyListener
{
	private boolean	letterOrDigit	= false;
	private int		keyCode			= 0;
	

	/**
	 * {@inheritDoc}
	 */
	public void keyPressed(KeyEvent e)
	{
		if(Character.isLetterOrDigit(e.getKeyChar()))
		{
			keyCode = e.getKeyCode();
			letterOrDigit = true;
		}
		else
		{
			letterOrDigit = false;
			keyActionPerformed(e);
		}
	}
	

	/**
	 * {@inheritDoc}
	 */
	public void keyReleased(KeyEvent e)
	{
	}
	

	/**
	 * {@inheritDoc}
	 */
	public void keyTyped(KeyEvent e)
	{
		if(letterOrDigit)
		{
			e.setKeyCode(keyCode);
			keyActionPerformed(e);
		}
	}
	

	/**
	 * Invoked only once when a key has been pressed or typed.
	 * 
	 * @param e
	 *            the event object
	 */
	public abstract void keyActionPerformed(KeyEvent e);
}
