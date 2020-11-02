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
 * An extended {@link FormularComponent} which can edit and display boolean
 * values.
 * 
 * @author XDEV Software
 * 
 * @param <C>
 *            type of the implementing {@link JComponent}
 * 
 * @since 3.1
 */
public interface BooleanFormularComponent<C extends JComponent> extends FormularComponent<C>
{
	/**
	 * Returns the value of this {@link BooleanFormularComponent}.
	 * 
	 * @return the value of this component
	 */
	public boolean getBoolean();
	

	/**
	 * Sets the new value for this component.
	 * 
	 * @param value
	 *            <code>true</code> or <code>false</code>
	 */
	public void setBoolean(boolean value);
}
