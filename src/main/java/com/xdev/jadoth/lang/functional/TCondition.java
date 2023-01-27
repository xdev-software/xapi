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
/**
 *
 */
package com.xdev.jadoth.lang.functional;

import com.xdev.jadoth.lang.functional.controlflow.TPredicate;
import com.xdev.jadoth.lang.signalthrows.ThrowBreak;
import com.xdev.jadoth.lang.signalthrows.ThrowContinue;
import com.xdev.jadoth.lang.signalthrows.ThrowReturn;



/**
 * @author Thomas Muenz
 *
 */
public abstract class TCondition<T> implements TPredicate<T>
{

	public final TCondition<T> and(final TPredicate<T> andPredicate)
	{
		return new TCondition<T>(){
			@Override public boolean apply(final T t) throws ThrowBreak, ThrowContinue, ThrowReturn
			{
				return TCondition.this.apply(t) && andPredicate.apply(t);
			}
		};
	}

	public final TCondition<T> or(final TPredicate<T> orPredicate)
	{
		return new TCondition<T>(){
			@Override public boolean apply(final T t) throws ThrowBreak, ThrowContinue, ThrowReturn
			{
				return TCondition.this.apply(t) || orPredicate.apply(t);
			}
		};
	}

}
