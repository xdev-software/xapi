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


import javax.swing.JComponent;


/**
 * An extended {@link FormularComponent} which can edit and display numbers.
 * 
 * @author XDEV Software
 * 
 * @param <C>
 *            type of the implementing {@link JComponent}
 * 
 * @since 3.1
 */
public interface NumberFormularComponent<C extends JComponent> extends FormularComponent<C>
{
	/**
	 * Returns the value of this {@link NumberFormularComponent} as
	 * {@link Number}.
	 * 
	 * @return the value of this {@link NumberFormularComponent} as
	 *         {@link Number}.
	 * 
	 * @throws NumberFormatException
	 *             if the value of this {@link NumberFormularComponent} can not
	 *             be converted.
	 * 
	 * @see #getNumber(Number)
	 */
	public Number getNumber() throws NumberFormatException;
	

	/**
	 * Returns the value of this {@link NumberFormularComponent} as
	 * {@link Number}. If the internal value can not be converted into a
	 * {@link Number}, the <code>defaultValue</code> is returned.
	 * 
	 * @param defaultValue
	 *            the default {@link Number}
	 * 
	 * @return the value of this {@link NumberFormularComponent}. If the
	 *         internal value can not be converted into a {@link Number}, the
	 *         <code>defaultValue</code> is returned.
	 * 
	 * @see #getNumber()
	 */
	public Number getNumber(Number defaultValue);
	

	/**
	 * Sets the number of this {@link NumberFormularComponent}.
	 * 
	 * @param number
	 *            the new number
	 */
	public void setNumber(Number number);
}
