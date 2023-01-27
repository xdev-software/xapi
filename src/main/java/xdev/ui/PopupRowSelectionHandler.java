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


import java.awt.Component;
import java.awt.PopupMenu;


/**
 * Tables or Lists which have a customized row-selection algorithm have to
 * implement this interface.
 * <p>
 * {@link PopupRowSelectionHandler#handlePopupRowSelection(int, int)} is
 * invoked when a {@link PopupMenu} is invoked on the implementing
 * {@link Component}.
 * </p>
 * 
 * @author XDEV Software JWill
 * @since 3.2
 */
public interface PopupRowSelectionHandler
{
	/**
	 * Handles the row selection of and {@link PopupRowSelectionHandler}.
	 * 
	 * @param x
	 *            the x-coordinate where the popup action is invoked.
	 * @param y
	 *            the y-coordinate where the popup action is invoked.
	 */
	public void handlePopupRowSelection(int x, int y);	
}
