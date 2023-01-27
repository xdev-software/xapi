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
package xdev.util;

/**
 * The super class of all {@link StringFilter} clients.
 * 
 * <p>
 * The {@link StringFilter} of a instance will be applied in the order they were added.
 * </p>
 * 
 * @author XDEV Software (RHHF)
 * 
 * @since 3.0
 */
public interface StringFilterClient {

	/**
	 * Adds an {@link StringFilter} to the instance.
	 * 
	 * <p>
	 * The {@link StringFilter} of a instance will be applied in the order they were added.
	 * </p>
	 * 
	 * @param stringFilter
	 *            the {@link StringFilter} to be added.
	 */
	public void addStringFilter(StringFilter stringFilter);

	/**
	 * Removes an {@link StringFilter} from the instance.
	 * 
	 * @param stringFilter
	 *            {@link StringFilter} to be removed.
	 * 
	 * @return if the provided {@link StringFilter} was present, the removed {@link StringFilter} ; otherwise the
	 *         {@link StringFilter.NullStringFilter#NULL_STRING_FILTER} instance. Never returns <code>null</code>.
	 */
	public StringFilter removeStringFilter(StringFilter stringFilter);

	/**
	 * Returns an array of all the {@link StringFilter}s added to this instance with
	 * {@link #addStringFilter(StringFilter)}.
	 * 
	 * @return all of the {@link StringFilter}s added or an empty array if no filters have been added
	 */
	public StringFilter[] getStringFilters();

}
