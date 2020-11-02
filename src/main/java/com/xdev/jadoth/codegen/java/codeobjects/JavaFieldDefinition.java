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

import static com.xdev.jadoth.codegen.java.codeobjects.JavaModifier.FINAL;
import static com.xdev.jadoth.codegen.java.codeobjects.JavaModifier.STATIC;
import static com.xdev.jadoth.codegen.java.codeobjects.JavaModifier.TRANSIENT;
import static com.xdev.jadoth.codegen.java.codeobjects.JavaModifier.VISIBILITY;
import static com.xdev.jadoth.codegen.java.codeobjects.JavaModifier.VOLATILE;



/**
 * @author Thomas Muenz
 *
 */
public interface JavaFieldDefinition 
extends JavaFieldDescription, JavaAccessibleObjectDefinition, JavaTypeMemberDefinition
{
	public static final int VALID_MODIFIERS = VISIBILITY|STATIC|FINAL|TRANSIENT|VOLATILE;
	
	public JavaFieldDefinition set(JavaExpression initialExpression);
	
	
	public class Implemenatation extends JavaModifierableCompilationObjectDefinition.Implementation
	implements JavaFieldDefinition
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
		 * @see com.xdev.jadoth.codegen.java.codeobjects.JavaFieldDefinition#getInitialExpression()
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
		
		@Override
		public void setOwner(final JavaTypeDefinition owner)
		{
			this.owner = owner;
			this.ownerIsClass = owner instanceof JavaClassDefinition;	
		}
	
		/**
		 * @param compilationUnit
		 * @return
		 * @see com.xdev.jadoth.codegen.java.codeobjects.JavaTypeMemberDefinition#setOwner(com.xdev.jadoth.codegen.java.codeobjects.JavaCompilationUnit)
		 */
		@Override
		public void registerAtOwner(final JavaTypeDefinition owner)
		{	
			owner.add(this);
		}
		/**
		 * @param initialExpression
		 * @return
		 * @see com.xdev.jadoth.codegen.java.codeobjects.JavaFieldDefinition#setInitialExpression(com.xdev.jadoth.codegen.java.codeobjects.JavaExpression)
		 */
		@Override
		public JavaFieldDefinition set(final JavaExpression initialExpression)
		{
			this.initialExpression = initialExpression;
			return this;
		}
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
		 * @param javaClass
		 * @return
		 * @see com.xdev.jadoth.codegen.java.codeobjects.JavaClassMemberDefinition#setOwner(com.xdev.jadoth.codegen.java.codeobjects.JavaClassDefinition)
		 */
		@Override
		public boolean setOwner(final JavaClassDefinition javaClass)
		{
			return false;
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
		 * @param sb
		 * @param codeGenerator
		 * @see com.xdev.jadoth.codegen.java.codeobjects.JavaCodeAssembable#assemble(java.lang.StringBuilder, com.xdev.jadoth.codegen.java.codeobjects.JavaCodeGenerator)
		 */
		@Override
		public void assemble(final StringBuilder sb, final JavaCodeGenerator codeGenerator)
		{
			codeGenerator.assembleJavaFieldDefinition(sb, this);			
		}



		/**
		 * @return
		 * @see com.xdev.jadoth.codegen.java.codeobjects.JavaTypeMemberDefinition#getNestingLevel()
		 */
		@Override
		public int getNestingLevel()
		{
			return 0;
		}
			
		
	}
	
}
