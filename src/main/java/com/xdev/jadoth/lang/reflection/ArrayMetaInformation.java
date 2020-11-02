
package com.xdev.jadoth.lang.reflection;

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

import com.xdev.jadoth.lang.exceptions.NotAnArrayException;


/**
 * The Class ArrayMetaInformation.
 * 
 * @author Thomas Muenz
 */
public class ArrayMetaInformation 
{
	///////////////////////////////////////////////////////////////////////////
	// static methods   //
	/////////////////////
	
	/**
	 * Determine dimensions.
	 * 
	 * @param arrayClass the array class
	 * @return the int
	 * @throws NotAnArrayException the no array runtime exception
	 */
	public static final int determineDimensions(final Class<?> arrayClass) throws NotAnArrayException 
	{
		if(!arrayClass.isArray()) {
			throw new NotAnArrayException(arrayClass);
		}		
		int dim = 1;
		Class<?> componentType = arrayClass.getComponentType();
		while(componentType.isArray()) {
			dim++;
			componentType = componentType.getComponentType();
		}
		return dim;
	}
	
	/**
	 * Determine component type.
	 * 
	 * @param arrayClass the array class
	 * @return the class
	 * @throws NotAnArrayException the no array runtime exception
	 */
	public static final Class<?> determineComponentType(final Class<?> arrayClass) throws NotAnArrayException 
	{
		if(!arrayClass.isArray()) {
			throw new NotAnArrayException(arrayClass);
		}
		Class<?> componentType = arrayClass.getComponentType();
		while(componentType.isArray()) {
			componentType = componentType.getComponentType();
		}
		return componentType;
	}
	
	
	
	///////////////////////////////////////////////////////////////////////////
	// instance fields  //
	/////////////////////
	
	/** The component type. */
	public final Class<?> componentType;
	
	/** The dimensions. */
	public final int dimensions;
	
	
	
	///////////////////////////////////////////////////////////////////////////
	// constructors     //
	/////////////////////
	
	/**
	 * Instantiates a new array meta information.
	 * 
	 * @param arrayClass the array class
	 * @throws NotAnArrayException the no array runtime exception
	 */
	public ArrayMetaInformation(final Class<?> arrayClass) throws NotAnArrayException 
	{
		if(!arrayClass.isArray()) {
			throw new NotAnArrayException(arrayClass);
		}		
		int dim = 1;
		Class<?> componentType = arrayClass.getComponentType();
		while(componentType.isArray()) {
			dim++;
			componentType = componentType.getComponentType();
		}
		this.dimensions = dim;
		this.componentType = componentType;
	}

}
