package xdev;

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
 * Holds information of the XDEV Application Framework.
 * 
 * @author XDEV Software
 * @deprecated Will no longer be maintained and removed in the future
 */
@Deprecated
public final class API
{
	/**
	 * <p>
	 * <code>API</code> class can not be instantiated. The class provides
	 * information via static members.
	 * </p>
	 */
	private API()
	{
	}
	
	/**
	 * The version of the XDEV Application Framework.
	 * @deprecated Will no longer be maintained and removed in the future
	 */
	@Deprecated
	public final static Version	VERSION	= new Version(5,0,2,0);
}
