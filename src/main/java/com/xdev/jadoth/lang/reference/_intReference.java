/**
 * 
 */
package com.xdev.jadoth.lang.reference;

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
 * Reference wrapper class for an int value.
 * 
 * @author Thomas Muenz
 *
 */
public final class _intReference
{
	/**
	 * The int value this reference points to
	 */
	public int value;

	
	/**
	 * Instantiates a new IntReference object.
	 * 
	 * @param value the initial value for this reference
	 */
	public _intReference(final int value)
	{
		super();
		this.value = value;
	}
	
	/**
	 * @return the String represenation of this reference's int value (<code>return Integer.toString(this.value)</code>)
	 */
	@Override
	public String toString()
	{
		return Integer.toString(this.value);
	}
	
}
