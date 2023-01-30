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
package com.xdev.jadoth.identity;

/**
 * Objects of this type specify the data-defined identity of an object of type <code>I</code>.
 * The values that define the identified object are called identity values. They should, but don't have to, 
 * have "primitive" data types like primitives, primitive Wrappers and String. 
 * <p>
 * It is strongly recommended that any datatype used as an identity value is immutable. 
 * And of cource that an implementation of this type is immutable as well. 
 * <p>
 * A good examples for a concrete "Identity" is a Primary Key in a database table.<br>
 * Subtypes of this interface can be used to abstract (prescind) Primary Key values in application code.    
 * <p>
 * It is recommended that implementations of this type properly override equals(), hashCode() and toString() in close
 * relation to the identity values they handle.
 * 
 * @author Thomas Muenz
 *
 */
public interface Identity<I>
{

}
