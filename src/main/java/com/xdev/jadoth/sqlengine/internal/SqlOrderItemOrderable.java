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
package com.xdev.jadoth.sqlengine.internal;


/**
 * The Class SqlOrderItemOrderable.
 */
public class SqlOrderItemOrderable extends SqlOrderItemNullSortable
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4585399800962794709L;

	/**
	 * Instantiates a new sql order item orderable.
	 * 
	 * @param item the item
	 */
	public SqlOrderItemOrderable(final Object item) {
		super(item, null);
	}


	///////////////////////////////////////////////////////////////////////////
	// declared methods //
	/////////////////////

	/**
	 * _ asc.
	 * 
	 * @return the sql order item null sortable
	 */
	public SqlOrderItemNullSortable _ASC() {
		this.setDesc(false);
		return this;
	}

	/**
	 * _ desc.
	 * 
	 * @return the sql order item null sortable
	 */
	public SqlOrderItemNullSortable _DESC() {
		this.setDesc(true);
		return this;
	}

}
