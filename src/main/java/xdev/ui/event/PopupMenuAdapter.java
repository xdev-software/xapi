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


import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;


/**
 * Abstract adapter class for the {@link PopupMenuListener} interface.
 * 
 * @author XDEV Software
 * @since 3.1
 */
public abstract class PopupMenuAdapter implements PopupMenuListener
{
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void popupMenuWillBecomeVisible(PopupMenuEvent e)
	{
	}
	

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void popupMenuWillBecomeInvisible(PopupMenuEvent e)
	{
	}
	

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void popupMenuCanceled(PopupMenuEvent e)
	{
	}
}
