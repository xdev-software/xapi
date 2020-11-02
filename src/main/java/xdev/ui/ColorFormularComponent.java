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


import java.awt.Color;

import javax.swing.JComponent;


/**
 * An extended {@link FormularComponent} which can edit and display colors.
 * 
 * @author XDEV Software
 * 
 * @param <C>
 *            type of the implementing {@link JComponent}
 * 
 * @since 4.0
 */
public interface ColorFormularComponent<C extends JComponent> extends FormularComponent<C>
{
	/**
	 * Returns the color of this {@link ColorFormularComponent}.
	 * 
	 * @return a new {@link Color} including the color of this
	 *         {@link ColorFormularComponent}; if the color is <code>null</code>
	 *         , <code>null</code> is returned
	 */
	public Color getColor();
	
	
	/**
	 * Sets a {@link Color} <code>color</code> to be displayed by this
	 * {@link ColorFormularComponent}.
	 * 
	 * @param color
	 *            {@link Color} to be displayed
	 */
	public void setColor(Color color);
}
