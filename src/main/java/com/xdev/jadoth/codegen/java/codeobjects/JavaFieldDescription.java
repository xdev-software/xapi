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
package com.xdev.jadoth.codegen.java.codeobjects;




/**
 * @author Thomas Muenz
 *
 */
public interface JavaFieldDescription extends JavaAccessibleObjectDescription, JavaTypeMemberDescription
{	
	public JavaExpression getInitialExpression();
	
	
	public class Implemenatation extends JavaModifierableCompilationObjectDescription.Implementation 
	implements JavaFieldDescription
	{		
		///////////////////////////////////////////////////////////////////////////
		// instance fields  //
		/////////////////////
		
		private JavaTypeDefinition owner = null;
		private boolean ownerIsClass = false;
		
		private JavaExpression initialExpression;
		private JavaTypeDescription type;

		
		
		///////////////////////////////////////////////////////////////////////////
		// constructors     //
		/////////////////////
		
		Implemenatation(
			final int modifiers, 
			final JavaTypeDescription type, 
			final String name, 
			final JavaExpression initialExpression
		)
		{
			super(modifiers, name);
			this.type = type;
			this.initialExpression = initialExpression;
		}

		
		
		///////////////////////////////////////////////////////////////////////////
		// getters          //
		/////////////////////
	
		/**
		 * @return
		 * @see com.xdev.jadoth.codegen.java.codeobjects.JavaFieldDescription#getInitialExpression()
		 */
		@Override
		public JavaExpression getInitialExpression()
		{
			return this.initialExpression;
		}
		/**
		 * @return
		 * @see com.xdev.jadoth.codegen.java.codeobjects.JavaClassMemberDefinition#getOwnerClass()
		 */
		@Override
		public JavaClassDefinition getOwnerClass()
		{
			return this.ownerIsClass ?(JavaClassDefinition)this.owner :null;
		}
		/**
		 * @return
		 * @see com.xdev.jadoth.codegen.java.codeobjects.JavaTypeMemberDefinition#getOwnerType()
		 */
		@Override
		public JavaTypeDefinition getOwnerType()
		{
			return this.owner;
		}
		/**
		 * @return the type
		 */
		public JavaTypeDescription getType()
		{
			return type;
		}	

		
		///////////////////////////////////////////////////////////////////////////
		// setters          //
		/////////////////////
		
		/**
		 * @param initialExpression
		 * @return
		 * @see net.jadoth.codegeneration.java.codeobjects.JavaFieldDescription#setInitialExpression(net.jadoth.codegeneration.java.codeobjects.JavaExpression)
		 */
		/**
		 * @param type the type to set
		 */
		public void setType(final JavaTypeDescription type)
		{
			this.type = type;
		}



		/**
		 * @param modifier
		 * @return
		 * @see com.xdev.jadoth.codegen.java.codeobjects.JavaModifierableCompilationObjectDefinition.Implementation#isValidModifier(int)
		 */
		@Override
		protected boolean isValidModifier(final int modifier)
		{
			return false;
		}



		/**
		 * @return
		 * @see com.xdev.jadoth.util.strings.Named#getName()
		 */
		@Override
		public String getName()
		{
			return null;
		}



		/**
		 * @return
		 * @see com.xdev.jadoth.codegen.java.codeobjects.JavaModifierableCompilationObjectDescription#getModifiers()
		 */
		@Override
		public int getModifiers()
		{
			return 0;
		}

		
		
		/**
		 * @return always <tt>null</tt>
		 * @see java.lang.reflect.Member#getDeclaringClass()
		 */
		@Override
		public Class<?> getDeclaringClass()
		{
			return null;
		}

		/**
		 * @return always <tt>false</tt>
		 * @see java.lang.reflect.Member#isSynthetic()
		 */
		@Override
		public boolean isSynthetic()
		{
			return false;
		}



		/**
		 * @return
		 */
		@Override
		public int getNestingLevel()
		{
			if(this.owner == null) return 0;			
			return this.owner.getNestingLevel()+1;
		}


			
		
	}
	
}
