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


/**
 * Interface to add a user defined validation to a component
 * 
 * @author XDEV Software
 * @since 3.1
 */
public interface Validator<T>
{
	/**
	 * Validates a component's state.
	 * <p>
	 * If the component is not valid a {@link ValidationException} is thrown,
	 * otherwise the method returns normally.
	 * 
	 * @param component
	 *            the component to be validated
	 * @throws ValidationException
	 *             if the component's state is not valid
	 */
	public void validate(T component) throws ValidationException;
}
