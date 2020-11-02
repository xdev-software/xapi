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
public interface JavaModifierableCompilationObjectDefinition extends JavaModifierableCompilationObjectDescription
{
	public JavaModifierableCompilationObjectDefinition setModifiers(int modifier);
	public JavaModifierableCompilationObjectDefinition addModifier(int modifier);
	public JavaModifierableCompilationObjectDefinition removeModifier(int modifier);
	
	
	
	public abstract class Implementation extends JavaModifierableCompilationObjectDescription.Implementation 
	implements JavaModifierableCompilationObjectDefinition
	{
		///////////////////////////////////////////////////////////////////////////
		// constructors     //
		/////////////////////
		
		/**
		 * @param name
		 */
		Implementation(final int modifiers, final String name)
		{
			super(modifiers, name);
		}

		
		
		///////////////////////////////////////////////////////////////////////////
		// getters          //
		/////////////////////
		

		
		///////////////////////////////////////////////////////////////////////////
		// setters          //
		/////////////////////
		
		@Override
		public JavaModifierableCompilationObjectDefinition setModifiers(final int modifiers)
		{
			super.setModifiers(modifiers);
			return this;
		}



		///////////////////////////////////////////////////////////////////////////
		// override methods //
		/////////////////////
		
		/**
		 * @param modifier
		 * @return
		 * @see com.xdev.jadoth.codegen.java.codeobjects.JavaModifierableCompilationObjectDefinition#addModifier(int)
		 */
		@Override
		public JavaModifierableCompilationObjectDefinition addModifier(final int modifier)
		{				
			super.setModifiers(this.getModifiers() | modifier);
			return this;
		}
		
		/**
		 * @param modifier
		 * @return
		 * @see com.xdev.jadoth.codegen.java.codeobjects.JavaModifierableCompilationObjectDefinition#removeModifier(int)
		 */
		@Override
		public JavaModifierableCompilationObjectDefinition removeModifier(final int modifier)
		{
			super.setModifiers(this.getModifiers() & ~modifier);
			return this;
		}	
		
		
		@Override
		protected abstract boolean isValidModifier(final int modifier);
		
	}
	
}
