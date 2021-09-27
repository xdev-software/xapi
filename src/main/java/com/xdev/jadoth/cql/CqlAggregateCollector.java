/**
 * 
 */
package com.xdev.jadoth.cql;

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

import static com.xdev.jadoth.Jadoth.keyValue;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import com.xdev.jadoth.collections.Collecting;
import com.xdev.jadoth.collections.GrowList;
import com.xdev.jadoth.collections.Sortable;
import com.xdev.jadoth.collections.XList;
import com.xdev.jadoth.lang.Equalator;
import com.xdev.jadoth.util.KeyValue;


/**
 * CQL aggregate that aggregates all entries in key -> list groups according to a given distinction equalator with
 * key being the first entry that matches a new distinction group and the list containing all entries that belong to 
 * the same distinction group, including the key entry.
 * <p>
 * The behaviour of this aggregate resembles the COLLECT(...) aggregate in SQL with a GROUP BY (...) that is defined 
 * by the given equalator.
 * 
 * @author Thomas Muenz
 *
 */
public class CqlAggregateCollector<E> 
implements 
CqlAggregator<E,  XList<KeyValue<E, XList<E>>>, Collecting<E>>, Collecting<E>, Sortable<E>
{
	///////////////////////////////////////////////////////////////////////////
	// instance fields  //
	/////////////////////
	
	/* (30.09.2010)TODO replace List+Map combination by ListMap when available. 
	 * Adjust aggregate() and project() accordingly.
	 *  
	 */
	private final GrowList<E> keys;
	private final Map<E, XList<E>> aggregates;
	
	
	
	///////////////////////////////////////////////////////////////////////////
	// constructors     //
	/////////////////////
	
	public CqlAggregateCollector()
	{
		this.keys = new GrowList<E>();
		this.aggregates = new HashMap<E, XList<E>>();
	}
	
	public CqlAggregateCollector(final int initialCapacity)
	{
		this.keys = new GrowList<E>(initialCapacity);
		this.aggregates = new HashMap<E, XList<E>>(initialCapacity);
	}
	
	

	///////////////////////////////////////////////////////////////////////////
	// override methods //
	/////////////////////
	
	@Override
	public CqlAggregateCollector<E> reset()
	{
		this.aggregates.clear();	
		this.keys.clear();
		return this;
	}
	
	@Override
	public Collecting<E> provideCollector()
	{
		return this;
	}
	
	public boolean add(final E s)
	{
		// does nothing as aggregate() handles keys as well
		return false;
	}

	@Override
	public Sortable<E> sort(final Comparator<E> comparator)
	{
		this.keys.sort(comparator);
		return this;
	}

	@Override
	public XList<KeyValue<E, XList<E>>> project(final Collecting<E> data)
	{
		final GrowList<E> keys = this.keys;
		final GrowList<KeyValue<E, XList<E>>> result = 
			new GrowList<KeyValue<E, XList<E>>>(keys.size())
		;				
		for(final E k : keys){
			result.add(keyValue(k, this.aggregates.get(k)));
		}
		return result;
	}
	

	/**
	 * @param element
	 * @param equalator
	 * @return
	 * @see com.xdev.jadoth.cql.CqlAggregator#aggregate(java.lang.Object, com.xdev.jadoth.lang.Equalator)
	 */
	@Override
	public boolean aggregate(final E element, final Equalator<E> equalator)
	{	
		XList<E> aggValues;
		
		final E key = this.keys.search(element, equalator);		
		if(key == null){
			// new key element, add new aggregation group
			aggValues = new GrowList<E>();
			aggValues.add(element);
			this.keys.add(element);	
			this.aggregates.put(element, aggValues);
			return true; //return true to signal new grouping element (only for contract, has no effect for this class)
		}
		
		aggValues = this.aggregates.get(key);
		aggValues.add(element);		
		return false; //return false to signal already existing grouping element
	}

}
