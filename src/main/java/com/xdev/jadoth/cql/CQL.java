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
package com.xdev.jadoth.cql;

import java.util.Comparator;

import com.xdev.jadoth.Jadoth;
import com.xdev.jadoth.collections.Collecting;
import com.xdev.jadoth.collections.GrowList;
import com.xdev.jadoth.collections.Sortable;
import com.xdev.jadoth.collections.XGettingCollection;
import com.xdev.jadoth.lang.ComparatorSequence;
import com.xdev.jadoth.lang.Equalator;
import com.xdev.jadoth.lang.EqualatorSequence;
import com.xdev.jadoth.lang.functional.Condition;
import com.xdev.jadoth.lang.functional.Predicate;


/**
 * Collection Query Language
 * @author Thomas Muenz
 *
 */
public class CQL
{	
	
	public static final <E> CqlProjector.Implementation<E> defaultProjection(
		final int initialCapactity, 
		final Class<E> type
	)
	{
		return new CqlProjector.Implementation<E>(initialCapactity);
	}
	
	public static final <E> GrowList<E> selectFrom(
		final XGettingCollection<E> data, 
		final Predicate<E> predicate
	)
	{
		return data.copyTo(new GrowList<E>(), predicate);
	}
		
	
	public static final <E> GrowList<E> selectFrom(
		final XGettingCollection<E> data, 
		final Predicate<E> predicate, 
		final int skip,
		final int limit
	)
	{
		return data.copyTo(new GrowList<E>(), predicate, skip, limit);
	}
	
	
	
	@SuppressWarnings("unchecked")
	public static final <E, P, C extends Collecting<E>> P select(
		final CqlProjector<E, P, C> projection,
		final XGettingCollection<E> dataSource, 
		final Predicate<E> condition,
		final Comparator<E> sorting,
		final int skip,
		final int limit
	)
	{
		// nifty & naughty way to optimize storage behaviour without additional overloaded methods
		final C storage = projection.provideCollector();
		
		// only apply sorting if result is actually sortable
		final C result = dataSource.copyTo(storage, condition, skip, limit);
		if(result instanceof Sortable<?>){
			((Sortable<E>)result).sort(sorting);
		}
		
		// apply projection and return projection result
		return projection.project(result);
	}
	
	/**
	 * 
	 * @param <E>
	 * @param data
	 * @param condition
	 * @param distinction
	 * @param sorting
	 * @param limit
	 * @return
	 */
	public static final <E, P, C extends Collecting<E>> P select(
		final CqlProjector<E, P, C> projection,
		final XGettingCollection<E> dataSource, 
		final Predicate<E> condition,
		final Equalator<E> distinction,
		final Comparator<E> sorting, 
		final int skip,
		final int limit
	)
	{				
		return select(
			projection, 
			dataSource, 
			extendConditionForAggregating(projection, condition, distinction), 
			sorting, 
			skip,
			limit
		);
	}
	
	private static <E> Predicate<E> extendConditionForAggregating(
		final CqlProjector<E, ?, ?> projection, 
		final Predicate<E> condition,
		final Equalator<E> distinction
	)
	{
		final Predicate<E> aggregationExtendedCondition;		
		if(projection instanceof CqlAggregator<?, ?, ?>){
			final CqlAggregator<E, ?, ?> aggregator = (CqlAggregator<E, ?, ?>)projection;
			aggregationExtendedCondition = new Predicate<E>() {
				@Override
				public boolean apply(final E e)
				{
					return condition.apply(e) && aggregator.aggregate(e, distinction);
				}				
			};
		}
		else {
			final GrowList<E> keys = new GrowList<E>();
			aggregationExtendedCondition = new Predicate<E>() {
				@Override
				public boolean apply(final E t)
				{
					return condition.apply(t) && keys.contains(t, distinction);
				}				
			};
		}
		return aggregationExtendedCondition;
	}
	
	
	
	public static <T> XGettingCollection<T> from(final T... data)
	{
		return Jadoth.list(data);
	}
	
	/**
	 * Dummy method for SQL-style syntax.
	 * @param <T>
	 * @param dataSource
	 * @return
	 */
	public static <T> XGettingCollection<T> from(final XGettingCollection<T> dataSource)
	{
		return dataSource;
	}
	
	/**
	 * Dummy method for SQL-style syntax.
	 * @param limit
	 * @return
	 */
	public static int fetchFirst(final int limit)
	{
		return limit;
	}
	
	/**
	 * Dummy method for SQL-style syntax.
	 * @param skip
	 * @return
	 */
	public static int skip(final int skip)
	{
		return skip;
	}
	
	
	/**
	 * sql-like alias for {@link #condition(Predicate)}
	 * @param <T>
	 * @param predicate
	 * @return
	 */
	public static final <T> Condition<T> where(final Predicate<T> predicate)
	{
		return new Condition<T>(){
			@Override public boolean apply(final T t)
			{
				return predicate.apply(t);
			}
		};
	}
	
	public static final <T>  Condition<T> or(final  Predicate<T> predicate1, final  Predicate<T> predicate2)
	{
		return new Condition<T>(){
			@Override public boolean apply(final T t)
			{
				return predicate1.apply(t) || predicate2.apply(t);
			}
		};
	}
	
	public static final <T> Condition<T> and(final Predicate<T> predicate1, final Predicate<T> predicate2)
	{
		return new Condition<T>(){
			@Override public boolean apply(final T t)
			{
				return predicate1.apply(t) && predicate2.apply(t);
			}
		};
	}
	
	
	
	/**
	 * sql-like alias for {@link #condition(Predicate)}
	 * @param <T>
	 * @param predicate
	 * @return
	 */
	public static final <T> Comparator<T> orderBy(final Comparator<T>... sortings)
	{
		return new ComparatorSequence<T>(sortings);
	}
	
	/**
	 * SQL-style syntax dummy method for using the passed comparator itself.  
	 * @param <T>
	 * @param comparator the comparator to use as itself [:-)].
	 * @return the passed comparator itself. 
	 */
	public static final <T> Comparator<T> asc(final Comparator<T> comparator)
	{
		return comparator;
	}
	
	/**
	 * SQL-style syntax dummy method for inverting the passed comparator.<br>
	 * See {@link #invert(Comparator)}.
	 * @param <T>
	 * @param comparator the comparator whose order shall be inverted.
	 * @return a {@link Comparator} instance that inverts the passed comparator's results.
	 */
	public static final <T> Comparator<T> desc(final Comparator<T> comparator)
	{
		return new Comparator<T>() {
			@Override
			public int compare(final T o1, final T o2)
			{
				return comparator.compare(o1, o2) * (-1);
			}			
		};
	}
	
	/**
	 * Inverts the result of the passed comparator by multiplying it with (-1), effectively inverting the order 
	 * it defines.
	 *  
	 * @param <T>
	 * @param comparator the comparator whose order shall be inverted.
	 * @return a {@link Comparator} instance that inverts the passed comparator's results.
	 */
	public static final <T> Comparator<T> invert(final Comparator<T> comparator)
	{
		return new Comparator<T>() {
			@Override
			public int compare(final T o1, final T o2)
			{
				return comparator.compare(o1, o2) * (-1);
			}			
		};
	}
	
	public static final <T> Equalator<T> groupBy(final Equalator<T>... distinction)
	{
		return new EqualatorSequence<T>(distinction);
	}
	
}
