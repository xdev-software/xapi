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
package xdev.util;


/**
 * Object converter to convert an object into a specific type.
 * 
 * @param <T>
 *            the parser's type
 * 
 * @author XDEV Software
 * @since 3.1
 */
public interface ObjectConverter<T>
{
	/**
	 * Returns the converted object of type T, or <code>null</code> if
	 * <code>value</code> is <code>null</code>.
	 * <p>
	 * If <code>value</code> cannot be converted to type T an
	 * {@link ObjectConversionException} is thrown.
	 * 
	 * @param value
	 *            the value to convert
	 * @return the converted object of type T, may be a new instance of type T,
	 *         or <code>value</code> itself if it is an instance of T
	 * @throws ObjectConversionException
	 *             if the parsing failed
	 */
	public T convert(Object value) throws ObjectConversionException;
}
