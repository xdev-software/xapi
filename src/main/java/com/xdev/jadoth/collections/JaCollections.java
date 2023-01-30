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
package com.xdev.jadoth.collections;

import static com.xdev.jadoth.Jadoth.notNull;

import java.util.Collection;
import java.util.Comparator;

import com.xdev.jadoth.lang.functional.aggregates.AvgInteger;
import com.xdev.jadoth.lang.functional.aggregates.AvgIntegerNonNull;
import com.xdev.jadoth.lang.functional.aggregates.SumInteger;


/**
 * @author TM
 *
 */
public class JaCollections
{
	public static final boolean containsNull(final Collection<?> c) throws NullPointerException
	{
		notNull(c);
		try {
			return c.contains(null);
		}
		catch(final NullPointerException e) {
			//collection implementations not supporting null values may throw an exception according to contract
			return false;
		}
	}





	///////////////////////////////////////////////////////////////////////////
	// collection aggregates  //
	///////////////////////////


	public static final Comparator<Boolean> COMPARE_BOOLEANS = new Comparator<Boolean>(){
		@Override
		public int compare(final Boolean o1, final Boolean o2)
		{
			if(o1 == o2)   return  0;
			if(o1 == null) return -1;
			if(o2 == null) return  1;
			if(o1.booleanValue()) return o2.booleanValue() ?0 :1;
			return o2.booleanValue() ?-1 :0;
		}
	};
	public static final Comparator<Byte>    COMPARE_BYTES    = new Comparator<Byte>(){
		@Override
		public int compare(final Byte o1, final Byte o2)
		{
			if(o1 == o2)   return  0;
			if(o1 == null) return -1;
			if(o2 == null) return  1;
			if(o1.byteValue() < o2.byteValue()) return -1;
			if(o1.byteValue() > o2.byteValue()) return  1;
			return 0;
		}
	};
	public static final Comparator<Short>   COMPARE_SHORTS   = new Comparator<Short>(){
		@Override
		public int compare(final Short o1, final Short o2)
		{
			if(o1 == o2)   return  0;
			if(o1 == null) return -1;
			if(o2 == null) return  1;
			if(o1.shortValue() < o2.shortValue()) return -1;
			if(o1.shortValue() > o2.shortValue()) return  1;
			return 0;
		}
	};
	public static final Comparator<Integer> COMPARE_INTEGERS = new Comparator<Integer>(){
		@Override
		public int compare(final Integer o1, final Integer o2)
		{
			if(o1 == o2)   return  0;
			if(o1 == null) return -1;
			if(o2 == null) return  1;
			if(o1.intValue() < o2.intValue()) return -1;
			if(o1.intValue() > o2.intValue()) return  1;
			return 0;
		}
	};
	public static final Comparator<Long>    COMPARE_LONGS    = new Comparator<Long>(){
		@Override
		public int compare(final Long o1, final Long o2)
		{
			if(o1 == o2)   return  0;
			if(o1 == null) return -1;
			if(o2 == null) return  1;
			if(o1.longValue() < o2.longValue()) return -1;
			if(o1.longValue() > o2.longValue()) return  1;
			return 0;
		}
	};
	public static final Comparator<Float>   COMPARE_FLOATS   = new Comparator<Float>(){
		@Override
		public int compare(final Float o1, final Float o2)
		{
			if(o1 == o2)   return  0;
			if(o1 == null) return -1;
			if(o2 == null) return  1;
			if(o1.floatValue() < o2.floatValue()) return -1;
			if(o1.floatValue() > o2.floatValue()) return  1;
			return 0;
		}
	};
	public static final Comparator<Double>  COMPARE_DOUBLES  = new Comparator<Double>(){
		@Override
		public int compare(final Double o1, final Double o2)
		{
			if(o1 == o2)   return  0;
			if(o1 == null) return -1;
			if(o2 == null) return  1;
			if(o1.doubleValue() < o2.doubleValue()) return -1;
			if(o1.doubleValue() > o2.doubleValue()) return  1;
			return 0;
		}
	};

	public static final Integer count(final XGettingCollection<?> c)
	{
		return c.size(); //kind of stupid
	}

	public static final Integer sum(final XGettingCollection<Integer> ints)
	{
		return new SumInteger(ints).yield();
	}
	public static final Integer avg(final XGettingCollection<Integer> ints)
	{
		return new AvgInteger(ints).yield();
	}
	public static final Integer avg(final XGettingCollection<Integer> ints, final boolean includeNulls)
	{
		return (includeNulls ?new AvgInteger(ints) :new AvgIntegerNonNull(ints)).yield();
	}
	public static final Integer max(final XGettingCollection<Integer> ints)
	{
		return ints.max(COMPARE_INTEGERS);
	}
	public static final Integer min(final XGettingCollection<Integer> ints)
	{
		return ints.min(COMPARE_INTEGERS);
	}
}
