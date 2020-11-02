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

/**
 * @author Thomas Muenz
 *
 */
public interface JavaModifierableCompilationObjectDescription extends JavaCompilationObject
{	
	public int getModifiers();
	
	
	public abstract class Implementation extends JavaCompilationObject.Implementation 
	implements JavaModifierableCompilationObjectDescription
	{
		///////////////////////////////////////////////////////////////////////////
		// instance fields  //
		/////////////////////
		
		private int modifiers = 0;
		
		
		
		///////////////////////////////////////////////////////////////////////////
		// constructors     //
		/////////////////////
		
		/**
		 * @param name
		 */
		Implementation(final int modifier, final String name)
		{
			super(name);
			this.modifiers = modifier;			
		}

		
		protected JavaModifierableCompilationObjectDescription setModifiers(final int modifiers)
		{
			this.modifiers = modifiers;
			return this;
		}


		/**
		 * @return
		 * @see com.xdev.jadoth.codegen.java.codeobjects.JavaModifierableCompilationObjectDescription#getModifiers()
		 */
		@Override
		public int getModifiers()
		{
			return this.modifiers;
		}
		
		
		
		protected abstract boolean isValidModifier(final int modifier);

		
	}
	
}
