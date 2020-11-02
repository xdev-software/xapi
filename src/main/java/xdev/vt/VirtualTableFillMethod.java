package xdev.vt;

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
 * An enum specifying the possible modes for adding new data to a
 * {@link VirtualTable}.
 * 
 * @author XDEV Software Corp.
 * 
 */
public enum VirtualTableFillMethod
{
	/**
	 * Overwrite existing data with the new data.
	 */
	OVERWRITE,

	/**
	 * Append new data at the end of the existing data.
	 */
	APPEND,

	/**
	 * Prepend new data in front of the existing data.
	 */
	PREPEND
}
