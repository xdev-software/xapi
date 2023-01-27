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

import com.xdev.jadoth.collections.Collecting;
import com.xdev.jadoth.collections.GrowList;

/**
 * @author Thomas Muenz
 *
 */
public interface CqlProjector<E, P, C extends Collecting<E>>
{
	/**
	 * Takes a {@link Collecting} instance that contains all entries selected by a CQL query and applies an arbitrary
	 * projection to all entries, returning whatever type the projector defines as the projection type
	 * (which is the collection containing the data itself in the simple case).
	 * <p>
	 * In order to know the exact type of the {@link Collecting} instance to work with, the projector has to provide
	 * the collecting instance itself.
	 *   
	 * @param selectedData
	 * @return the data selected by the SQL query, projected to the final result type.
	 */
	public P project(C selectedData);
	
	/**
	 * Provides the {@link Collecting} instance that will be used by the SQL query to collect all selected entries
	 * and that will be passed back to the projector for projection.
	 * 
	 * @return the {@link Collecting} instance to be used to collect all selected entries.
	 */
	public C provideCollector();
	
	
	
	/**
	 * Default implementation of {@link CqlQueryHelper} which does no projection at all but returns the list containing
	 * the selected entries directly.
	 * 
	 * @author Thomas Muenz
	 *
	 * @param <E>
	 */
	public class Implementation<E> implements CqlProjector<E, GrowList<E>, GrowList<E>>
	{
		///////////////////////////////////////////////////////////////////////////
		// instance fields  //
		/////////////////////
		
		private int initialCapacity = 0;
		
		
		
		///////////////////////////////////////////////////////////////////////////
		// constructors     //
		/////////////////////
		
		public Implementation(final int initialCapacity)
		{
			super();
			this.initialCapacity = initialCapacity;
		}

		
		
		///////////////////////////////////////////////////////////////////////////
		// getters          //
		/////////////////////
		
		public int getInitialCapacity()
		{
			return initialCapacity;
		}

		
		
		///////////////////////////////////////////////////////////////////////////
		// setters          //
		/////////////////////
		
		public void setInitialCapacity(final int initialCapacity)
		{
			this.initialCapacity = initialCapacity;
		}

		
		
		///////////////////////////////////////////////////////////////////////////
		// override methods //
		/////////////////////
		
		/**
		 * @return
		 * @see net.jadoth.cql.CqlQueryHelper#provideCollector()
		 */
		@Override
		public GrowList<E> provideCollector()
		{
			return new GrowList<E>(this.initialCapacity);
		}

		/**
		 * @param data
		 * @return
		 * @see com.xdev.jadoth.cql.CqlProjector#project(com.xdev.jadoth.collections.XGettingCollection)
		 */
		@Override
		public GrowList<E> project(final GrowList<E> data)
		{
			// as provider methods provides a collection of this type, casting to this type again is safe.
			return data;
		}
		
	}
}
