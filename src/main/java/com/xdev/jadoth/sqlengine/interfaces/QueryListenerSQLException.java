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
package com.xdev.jadoth.sqlengine.interfaces;

import java.util.ArrayList;
import java.util.Collection;

/**
 * @author Thomas Muenz
 *
 */
public interface QueryListenerSQLException extends QueryActionListener
{
	public void invoke(final QueryEventSQLException event);
	
	
	public class List extends ArrayList<QueryListenerSQLException> implements QueryListenerSQLException
	{
		///////////////////////////////////////////////////////////////////////////
		// constants        //
		/////////////////////
		
		/**
		 * 
		 */
		private static final long serialVersionUID = 743693406170614939L;
		
		
		
		///////////////////////////////////////////////////////////////////////////
		// static methods //
		///////////////////
		
		public static List create(final QueryListenerSQLException... listeners)
		{
			if(listeners == null) {
				return new List();
			}
			
			final List list = new List(listeners.length);
			for(final QueryListenerSQLException e : listeners) {
				list.add(e);
			}
			return list;
		}
		
		public static List create(final Iterable<? extends QueryListenerSQLException> listeners)
		{
			final List list = new List();
			if(listeners == null) {
				return list;
			}			
			
			for(final QueryListenerSQLException e : listeners) {
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
		public List(Collection<? extends QueryListenerSQLException> c)
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


		@Override
		public void invoke(final QueryEventSQLException sqlExceptionEvent)
		{
			for(int i = 0, size = this.size(); i < size; i++){
				final QueryListenerSQLException element = this.get(i);
				if(element == null) continue;
				element.invoke(sqlExceptionEvent);
			}			
		}

		
		
		///////////////////////////////////////////////////////////////////////////
		// declared methods //
		/////////////////////
		
		public void addMultiple(final QueryListenerSQLException... listeners)
		{
			for(QueryListenerSQLException queryPreAssemblyListener : listeners) {
				this.add(queryPreAssemblyListener);
			}
		}
		
		
	}
	
}
