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
import xdev.util.DateFormatException;
import xdev.util.XdevDate;


/**
 * An extended {@link FormularComponent} which can edit and display dates.
 * 
 * @author XDEV Software
 * 
 * @param <C>
 *            type of the implementing {@link JComponent}
 * 
 * @since 3.1
 */
public interface DateFormularComponent<C extends JComponent> extends FormularComponent<C>
{
	/**
	 * Returns the value of this {@link DateFormularComponent} as
	 * {@link XdevDate}.
	 * 
	 * @return the value of this {@link DateFormularComponent} as
	 *         {@link XdevDate}.
	 * 
	 * @throws DateFormatException
	 *             if the value of this {@link DateFormularComponent} can not be
	 *             converted.
	 * 
	 * @see #getDate(XdevDate)
	 * @see TextFormat#parseDate(String)
	 */
	public XdevDate getDate() throws DateFormatException;
	

	/**
	 * Returns the value of this {@link DateFormularComponent} as
	 * {@link XdevDate}. If the internal value can not be converted into a
	 * {@link XdevDate}, the <code>defaultValue</code> is returned.
	 * 
	 * @param defaultValue
	 *            the default {@link XdevDate}
	 * 
	 * @return the value of this {@link DateFormularComponent}. If the internal
	 *         value can not be converted into a {@link XdevDate}, the
	 *         <code>defaultValue</code> is returned.
	 * 
	 * @see #getDate()
	 */
	public XdevDate getDate(XdevDate defaultValue);
	

	/**
	 * Sets the date of this {@link DateFormularComponent}.
	 * 
	 * @param date
	 *            the new date
	 */
	public void setDate(XdevDate date);
}
