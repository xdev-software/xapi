package xdev.ui.event;

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


import javax.swing.event.MenuDragMouseEvent;
import javax.swing.event.MenuDragMouseListener;


/**
 * Abstract adapter class for the {@link MenuDragMouseListener} interface.
 * 
 * @author XDEV Software
 * @since 3.1
 */
public abstract class MenuDragMouseAdapter implements MenuDragMouseListener
{
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void menuDragMouseEntered(MenuDragMouseEvent e)
	{
	}
	

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void menuDragMouseExited(MenuDragMouseEvent e)
	{
	}
	

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void menuDragMouseDragged(MenuDragMouseEvent e)
	{
	}
	

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void menuDragMouseReleased(MenuDragMouseEvent e)
	{
	}
}
