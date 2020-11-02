/**
 * 
 */
package com.xdev.jadoth.lang;

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

import java.util.Comparator;

/**
 * Useful for implementing SQL-like "ORDER BY" for collections.
 * 
 * @author Thomas Muenz
 *
 */
public class ComparatorSequence<T> implements Comparator<T>
{
	final private Comparator<T>[] comparators;

	public ComparatorSequence(final Comparator<T>... comparators)
	{
		super();
		this.comparators = comparators;
	}

	@Override
	public int compare(final T o1, final T o2)
	{
		int c;
		for(final Comparator<T> cmp : this.comparators){
			if((c = cmp.compare(o1, o2)) != 0) return c;
		}
		return 0;
	}
	
	
}
