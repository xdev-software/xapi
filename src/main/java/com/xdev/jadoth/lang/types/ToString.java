
package com.xdev.jadoth.lang.types;

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
 * Although every Object has String toString() automatically, the meaning of this interface is to "type"
 * a class to have a toString() method that returns a reasonable value for the runtime context.
 * <p>
 * For example: An object of type SELECT will return the proper SQL query it represents instead of simple debug info   
 * 
 * @author Thomas Muenz
 *
 */
public interface ToString
{
	
	/**
	 * To string.
	 * 
	 * @return the string
	 */
	String toString();
}
