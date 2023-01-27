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
package xdev.util.res;


import java.util.*;


/**
 * A ResourceSearchStrategy is the way how resources are searched through the
 * class hierarchy or the file system.
 * 
 * @author XDEV Software
 * 
 * @since 2.0
 * 
 */

public interface ResourceSearchStrategy
{
	/**
	 * Searches for a resource entry according to <code>key</code>.<br>
	 * The <code>requestor</code> may be crucial how the strategy is looking for
	 * the ressource.<br>
	 * <br>
	 * If <code>requestor</code> is <code>null</code> only the default resource
	 * bundle is searched through.
	 * 
	 * @param key
	 *            the key of the resource's value pair
	 * @param locale
	 *            to lookup the String for
	 * @param requestor
	 *            the origin of the call to this method or <code>null</code>
	 * @return the value mapped to <code>key</code>
	 * @throws MissingResourceException
	 *             if no resource bundle can be found - depending on this search
	 *             strategy - or if the key can not be found in one of the
	 *             resource files
	 * @throws NullPointerException
	 *             if <code>key</code> is <code>null</code>
	 */
	
	public String lookupResourceString(String key, Locale locale, Object requestor)
			throws MissingResourceException, NullPointerException;
}
