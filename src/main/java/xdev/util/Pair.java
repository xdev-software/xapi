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

//TODO class description
public class Pair<A, B>
{
	private final A		first;
	private final B		second;
	private final int	hash;
	

	/**
	 * Create a new {@link Pair} Object.
	 * 
	 * @param first
	 *            the first parameter
	 * 
	 * @param second
	 *            the second parameter
	 */
	public Pair(A first, B second)
	{
		this.first = first;
		this.second = second;
		this.hash = MathUtils.computeHash(first,second);
	}
	

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode()
	{
		return hash;
	}
	

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Object other)
	{
		if(other instanceof Pair)
		{
			Pair otherPair = (Pair)other;
			return ObjectUtils.equals(first,otherPair.first)
					&& ObjectUtils.equals(second,otherPair.second);
		}
		
		return false;
	}
	

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString()
	{
		return "(" + first + ", " + second + ")";
	}
	

	/**
	 * Returns the first parameter of this {@link Pair}.
	 * 
	 * @return the first parameter
	 */
	public A getFirst()
	{
		return first;
	}
	

	/**
	 * Returns the second parameter of this {@link Pair}.
	 * 
	 * @return the second parameter
	 */
	public B getSecond()
	{
		return second;
	}
}
