
package com.xdev.jadoth.sqlengine.internal;

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
 * The Class SqlOrderItemNullSortable.
 */
public class SqlOrderItemNullSortable extends SqlOrderItem
{
	///////////////////////////////////////////////////////////////////////////
	// constructors     //
	/////////////////////

	/**
	 * 
	 */
	private static final long serialVersionUID = 8863275024611010316L;

	/**
	 * Instantiates a new sql order item null sortable.
	 * 
	 * @param item the item
	 * @param desc the desc
	 */
	public SqlOrderItemNullSortable(final Object item, final Boolean desc) {
		super(item, desc, null);
	}



	///////////////////////////////////////////////////////////////////////////
	// declared methods //
	/////////////////////

	/**
	 * _ null s_ first.
	 * 
	 * @return the sql order item
	 */
	public SqlOrderItem _NULLS_FIRST() {
		this.setNullsFirst(true);
		return this;
	}

	/**
	 * _ null s_ last.
	 * 
	 * @return the sql order item
	 */
	public SqlOrderItem _NULLS_LAST() {
		this.setNullsFirst(false);
		return this;
	}

}
