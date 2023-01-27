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
package com.xdev.jadoth.util.strings;
/**
 * 
 */

/**
 * @author Thomas Muenz
 *
 */
public interface Nameable extends Named
{
	public Nameable setName(String name);
	
	
	
	public class Implementation implements Nameable
	{
		///////////////////////////////////////////////////////////////////////////
		// instance fields  //
		/////////////////////
		
		private String name;

		
		
		///////////////////////////////////////////////////////////////////////////
		// constructors     //
		/////////////////////
		
		Implementation(final String name)
		{
			super();
			this.name = name;
		}

		
		
		///////////////////////////////////////////////////////////////////////////
		// getters          //
		/////////////////////
		
		/**
		 * @return
		 * @see com.xdev.jadoth.util.strings.Named#getName()
		 */
		@Override
		public String getName()
		{
			return this.name;
		}

		
		
		///////////////////////////////////////////////////////////////////////////
		// setters          //
		/////////////////////
		
		public Implementation setName(final String name)
		{
			this.name = name;
			return this;
		}			
		
	}
	
}
