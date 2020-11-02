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


import javax.swing.JComponent;


/**
 * An extended {@link FormularComponent} which can edit and display images.
 * 
 * @author XDEV Software
 * 
 * @param <C>
 *            type of the implementing {@link JComponent}
 * 
 * @since 3.1
 */
public interface ImageFormularComponent<C extends JComponent> extends FormularComponent<C>
{
	/**
	 * Returns the image of this {@link ImageFormularComponent}.
	 * 
	 * @return a new {@link XdevImage} including the image of this
	 *         {@link ImageFormularComponent}; if the image is <code>null</code>
	 *         , <code>null</code> is returned
	 */
	public XdevImage getImage();
	

	/**
	 * Sets a {@link XdevImage} <code>image</code> to be displayed by this
	 * {@link ImageFormularComponent}.
	 * 
	 * @param image
	 *            {@link XdevImage} to be displayed
	 */
	public void setImage(XdevImage image);
}
