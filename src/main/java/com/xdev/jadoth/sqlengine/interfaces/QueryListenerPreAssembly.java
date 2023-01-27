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
package com.xdev.jadoth.sqlengine.interfaces;

import java.util.ArrayList;
import java.util.Collection;

/**
 * @author Thomas Muenz
 *
 */
public interface QueryListenerPreAssembly extends QueryActionListener
{
	public void invoke(QueryEventPreAssembly event);
	
	
	public class List extends ArrayList<QueryListenerPreAssembly> implements QueryListenerPreAssembly
	{		
		///////////////////////////////////////////////////////////////////////////
		// constants        //
		/////////////////////		

		/**
		 * 
		 */
		private static final long serialVersionUID = -1964483456059531437L;



		///////////////////////////////////////////////////////////////////////////
		// static methods //
		///////////////////

		public static List create(final QueryListenerPreAssembly... listeners)
		{
			if(listeners == null) {
				return new List();
			}
			
			final List list = new List(listeners.length);
			for(final QueryListenerPreAssembly e : listeners) {
				list.add(e);
			}
			return list;
		}
		
		public static List create(final Iterable<? extends QueryListenerPreAssembly> listeners)
		{
			final List list = new List();
			if(listeners == null) {
				return list;
			}			
			
			for(final QueryListenerPreAssembly e : listeners) {
				list.add(e);
			}
			return list;
		}
		
		
		
		///////////////////////////////////////////////////////////////////////////
		// constructors //
		/////////////////

		/**
		 * 
		 */
		public List()
		{
			super();
		}		
		/**
		 * @param c
		 */
		public List(Collection<? extends QueryListenerPreAssembly> c)
		{
			super(c);
		}		
		/**
		 * @param initialCapacity
		 */
		public List(int initialCapacity)
		{
			super(initialCapacity);
		}

		

		///////////////////////////////////////////////////////////////////////////
		// override methods //
		/////////////////////

		/**
		 * 
		 * @param dbc
		 * @param statement
		 * @param query
		 * @param assembleParameters
		 */
		@Override
		public void invoke(final QueryEventPreAssembly assemblyEvent)
		{
			for(int i = 0, size = this.size(); i < size; i++){
				final QueryListenerPreAssembly element = this.get(i);
				if(element == null) continue;
				element.invoke(assemblyEvent);
			}
		}
		
		
		
		///////////////////////////////////////////////////////////////////////////
		// declared methods //
		/////////////////////
		
		public void addMultiple(final QueryListenerPreAssembly... listeners)
		{
			for(QueryListenerPreAssembly queryPreAssemblyListener : listeners) {
				this.add(queryPreAssemblyListener);
			}
		}
		
	}	
}
