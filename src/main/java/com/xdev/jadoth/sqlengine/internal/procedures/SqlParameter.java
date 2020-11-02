/**
 * 
 */
package com.xdev.jadoth.sqlengine.internal.procedures;

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
 * @author Thomas Muenz
 *
 */
public class SqlParameter
{
	private PROCEDURE<?> owner;
	private String name;
	
	public void setOwner(final PROCEDURE<?> owner)
	{
		this.owner = owner;
	}
	

	public PROCEDURE<?> getOwner()
	{
		return this.owner;
	}


	public String getName()
	{
		return this.name;
	}

	public void setName(final String name)
	{
		this.name = name;
	}
	
	
}
