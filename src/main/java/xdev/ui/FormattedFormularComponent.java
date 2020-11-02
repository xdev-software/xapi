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

import xdev.ui.text.TextFormat;


/**
 * Extended {@link FormularComponent} which uses a {@link TextFormat} to format
 * the displayed value.
 * 
 * @author XDEV Software
 * 
 * @param <C>
 *            type of the implementing {@link JComponent}
 * 
 * @see TextFormat
 */
public interface FormattedFormularComponent<C extends JComponent> extends FormularComponent<C>
{
	/**
	 * Returns the associated {@link TextFormat} of this component.
	 * 
	 * @return the associated {@link TextFormat} of this component
	 */
	public TextFormat getTextFormat();


	/**
	 * Sets the new {@link TextFormat} which is used to format the displayed
	 * value of this component.
	 * 
	 * @param textFormat
	 *            the new {@link TextFormat}
	 */
	public void setTextFormat(TextFormat textFormat);
}
