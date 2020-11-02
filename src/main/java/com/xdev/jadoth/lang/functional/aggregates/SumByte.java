/**
 * 
 */
package com.xdev.jadoth.lang.functional.aggregates;

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

import com.xdev.jadoth.collections.XGettingCollection;

/**
 * @author Thomas Muenz
 *
 */
public final class SumByte implements Aggregate<Byte, Integer>
{
	private int sum = 0;	
	
	public SumByte(final XGettingCollection<Byte> c)
	{
		super();
		c.execute(this);
	}

	@Override
	public void execute(final Byte n)
	{
		if(n != null) this.sum += n;		
	}
	
	public Integer yield()
	{
		return this.sum;
	}
	
	/**
	 * @return
	 * @see com.xdev.jadoth.lang.functional.aggregates.Aggregate#reset()
	 */
	@Override
	public Aggregate<Byte, Integer> reset()
	{
		this.sum = 0;
		return this;
	}
}
