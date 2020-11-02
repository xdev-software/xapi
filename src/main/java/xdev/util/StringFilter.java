package xdev.util;

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
 * 
 * The {@link StringFilter} defines a pattern to filter a given {@link String};
 * 
 * @author XDEV Software (RHHF)
 * 
 * @since 3.0
 */
public interface StringFilter {

	/**
	 * Returns the filtered <code>input</code> {@link String}.
	 * 
	 * @param input
	 *            {@link String} to be filtered.
	 * @return the filtered <code>input</code> {@link String}.
	 */
	public String filter(String input);

	/**
	 * 
	 * A "null pattern" implementation of {@link StringFilter}.
	 * 
	 * @author XDEV Software
	 * 
	 * @since 3.0
	 * 
	 */
	public static class NullStringFilter implements StringFilter {

		/**
		 * "The" {@link NullStringFilter} instance to use for comparison.
		 */
		public static final NullStringFilter NULL_STRING_FILTER = new NullStringFilter();

		/**
		 * Does nothing. Just returns the provided input {@link String}.
		 */
		@Override
		public String filter(String input) {
			return input;
		}

	}
}
