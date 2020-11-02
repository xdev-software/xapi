package xdev.vt;

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


/**
 * Interface to add a user defined validation to the {@link VirtualTable}.
 * 
 * @param <T>
 *            the column object type
 * 
 * @see VirtualTableColumn#addValidator(Validator)
 * 
 * @author XDEV Software
 * @since 3.1
 */
public interface Validator<T>
{
	/**
	 * Validates <code>value</code> before it is inserted into a
	 * {@link VirtualTable}.
	 * <p>
	 * If the value is not valid to be inserted a {@link VirtualTableException}
	 * is thrown, otherwise the method returns normally.
	 * 
	 * @param value
	 *            the value to be validated
	 * @param column
	 *            the column in which the value will be inserted
	 * @throws VirtualTableException
	 *             if the value is not valid to be inserted
	 */
	public void validate(T value, VirtualTableColumn<?> column) throws VirtualTableException;
}
