package xdev.util;

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
 * An exception which occures if an {@link ObjectConverter} failes to convert an
 * object in the target type.
 * 
 * @author XDEV Software
 * @since 3.1
 */
public class ObjectConversionException extends Exception
{
	/**
	 * Creates a new object conversion exception.
	 */
	public ObjectConversionException()
	{
	}
	

	/**
	 * Creates a new object conversion exception.
	 * 
	 * @param sourceValue
	 *            the source value
	 * @param targetType
	 *            the target type
	 */
	public ObjectConversionException(Object sourceValue, Class<?> targetType)
	{
		this(String.valueOf(sourceValue) + " cannot be converted to " + targetType.getName());
	}
	

	/**
	 * Creates a new object conversion exception.
	 * 
	 * @param message
	 *            the error message
	 * @param cause
	 *            the error cause
	 */
	public ObjectConversionException(String message, Throwable cause)
	{
		super(message,cause);
	}
	

	/**
	 * Creates a new object conversion exception.
	 * 
	 * @param message
	 *            the error message
	 */
	public ObjectConversionException(String message)
	{
		super(message);
	}
	

	/**
	 * Creates a new object parse exception.
	 * 
	 * @param cause
	 *            the error cause
	 */
	public ObjectConversionException(Throwable cause)
	{
		super(cause);
	}
}
