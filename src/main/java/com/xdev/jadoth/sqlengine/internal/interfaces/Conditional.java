
package com.xdev.jadoth.sqlengine.internal.interfaces;

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
 * The Interface Conditional.
 * 
 * @param <C> the generic type
 */
public interface Conditional<C extends Conditional<C>>
{	
	
	/**
	 * AND.
	 * 
	 * @param condition the condition
	 * @return the c
	 */
	public C AND(Object condition);
	
	/**
	 * OR.
	 * 
	 * @param condition the condition
	 * @return the c
	 */
	public C OR(Object condition);
}
