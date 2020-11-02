/**
 * 
 */
package com.xdev.jadoth.codegen.java.codeobjects;

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

import com.xdev.jadoth.util.strings.Nameable;

/**
 * @author Thomas Muenz
 *
 */
public interface JavaCompilationObject extends Nameable
{	
	
	public class Implementation implements JavaCompilationObject
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
		
		public JavaCompilationObject setName(final String name)
		{
			this.name = name;
			return this;
		}



			
		
	}
	
}
