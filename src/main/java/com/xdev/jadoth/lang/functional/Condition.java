/**
 *
 */
package com.xdev.jadoth.lang.functional;

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
public abstract class Condition<T> implements Predicate<T>
{

	public final Condition<T> and(final Predicate<T> andPredicate)
	{
		return new Condition<T>(){
			@Override public boolean apply(final T t)
			{
				return Condition.this.apply(t) && andPredicate.apply(t);
			}
		};
	}

	public final Condition<T> or(final Predicate<T> orPredicate)
	{
		return new Condition<T>(){
			@Override public boolean apply(final T t)
			{
				return Condition.this.apply(t) || orPredicate.apply(t);
			}
		};
	}

}
