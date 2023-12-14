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
package com.xdev.jadoth.lang.functional.aggregates;

import com.xdev.jadoth.collections.XGettingCollection;


/**
 * @author Thomas Muenz
 *
 */
public final class AvgInteger implements Aggregate<Integer, Integer>
{
	private int sum = 0;
	private int count = 0;

	public AvgInteger()
	{
		super();
	}

	public AvgInteger(final XGettingCollection<Integer> c)
	{
		super();
		c.aggregate(this);
	}

	@Override
	public void execute(final Integer n)
	{
		if(n != null){
			this.sum += n;
		}
		this.count++;
	}

	public Integer yield()
	{
		return this.sum/this.count;
	}

	/**
	 * @return
	 * @see com.xdev.jadoth.lang.functional.aggregates.Aggregate#reset()
	 */
	@Override
	public Aggregate<Integer, Integer> reset()
	{
		this.sum = 0;
		return this;
	}
}
