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

import static com.xdev.jadoth.codegen.java.codeobjects.JavaModifier.ABSTRACT;
import static com.xdev.jadoth.codegen.java.codeobjects.JavaModifier.FINAL;
import static com.xdev.jadoth.codegen.java.codeobjects.JavaModifier.STATIC;
import static com.xdev.jadoth.codegen.java.codeobjects.JavaModifier.SYNCHRONIZED;
import static com.xdev.jadoth.codegen.java.codeobjects.JavaModifier.VISIBILITY;

/**
 * @author Thomas Muenz
 *
 */
public interface JavaMethodDefinition 
extends JavaMethodDescription, JavaCallableObjectDefinition, JavaTypeMemberDefinition
{
	public static final int VALID_MODIFIERS = VISIBILITY|STATIC|FINAL|ABSTRACT|SYNCHRONIZED;
	
	
	
	public class Implementation extends JavaModifierableCompilationObjectDefinition.Implementation
	implements JavaMethodDefinition
	{
		///////////////////////////////////////////////////////////////////////////
		// instance fields  //
		/////////////////////
		
		private JavaTypeDescription type;
		
		
		///////////////////////////////////////////////////////////////////////////
		// constructors     //
		/////////////////////
		
		Implementation(
			final int modifiers, 
			final JavaTypeDescription type, 
			final String name
		)
		{
			super(modifiers, name);
			this.type = type;
		}
		

		/**
		 * @return
		 * @see com.xdev.jadoth.codegen.java.codeobjects.JavaCallableObjectDescription#iterateDeclaredThrowables()
		 */
		@Override
		public Iterable<JavaThrowableType> iterateDeclaredThrowables()
		{
			return null;
		}

		/**
		 * @return
		 * @see com.xdev.jadoth.codegen.java.codeobjects.JavaCallableObjectDescription#iterateParameters()
		 */
		@Override
		public Iterable<JavaParameter> iterateParameters()
		{
			return null;
		}

		/**
		 * @return
		 * @see com.xdev.jadoth.codegen.java.codeobjects.JavaClassMemberDescription#getOwnerClass()
		 */
		@Override
		public JavaClassDefinition getOwnerClass()
		{
			return null;
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
		 * @see com.xdev.jadoth.codegen.java.codeobjects.JavaTypedCodeObject#getType()
		 */
		@Override
		public JavaTypeDescription getType()
		{
			return null;
		}

		/**
		 * @param modifier
		 * @return
		 * @see com.xdev.jadoth.codegen.java.codeobjects.JavaModifierableCompilationObjectDefinition#addModifier(int)
		 */
		@Override
		public JavaModifierableCompilationObjectDefinition addModifier(final int modifier)
		{
			return null;
		}

		/**
		 * @param modifier
		 * @return
		 * @see com.xdev.jadoth.codegen.java.codeobjects.JavaModifierableCompilationObjectDefinition#removeModifier(int)
		 */
		@Override
		public JavaModifierableCompilationObjectDefinition removeModifier(final int modifier)
		{
			return null;
		}

		/**
		 * @param modifier
		 * @return
		 * @see com.xdev.jadoth.codegen.java.codeobjects.JavaModifierableCompilationObjectDefinition#setModifiers(int)
		 */
		@Override
		public JavaModifierableCompilationObjectDefinition setModifiers(final int modifier)
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
		 * @return
		 * @see com.xdev.jadoth.codegen.java.codeobjects.JavaGenericParametrizableObject#iterateGenericParameters()
		 */
		@Override
		public Iterable<JavaGenericParameter> iterateGenericParameters()
		{
			return null;
		}

		/**
		 * @return
		 * @see com.xdev.jadoth.codegen.java.codeobjects.JavaTypeMemberDescription#getNestingLevel()
		 */
		@Override
		public int getNestingLevel()
		{
			return 0;
		}

		/**
		 * @return
		 * @see com.xdev.jadoth.codegen.java.codeobjects.JavaTypeMemberDescription#getOwnerType()
		 */
		@Override
		public JavaTypeDescription getOwnerType()
		{
			return null;
		}

		/**
		 * @return
		 * @see java.lang.reflect.Member#getDeclaringClass()
		 */
		@Override
		public Class<?> getDeclaringClass()
		{
			return null;
		}

		/**
		 * @return
		 * @see java.lang.reflect.Member#isSynthetic()
		 */
		@Override
		public boolean isSynthetic()
		{
			return false;
		}

		/**
		 * @param throwables
		 * @return
		 * @see com.xdev.jadoth.codegen.java.codeobjects.JavaCallableObjectDefinition#throwS(com.xdev.jadoth.codegen.java.codeobjects.JavaThrowableType[])
		 */
		@Override
		public JavaCallableObjectDefinition throwS(final JavaThrowableType... throwables)
		{
			return null;
		}

		/**
		 * @param throwables
		 * @return
		 * @see net.jadoth.codegen.java.codeobjects.JavaCallableObjectDefinition#throwS(java.lang.Class<? extends java.lang.Throwable>[])
		 */
		@Override
		public JavaCallableObjectDefinition throwS(final Class<? extends Throwable>... throwables)
		{
			return null;
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
		 * @param sb
		 * @param codeGenerator
		 * @see com.xdev.jadoth.codegen.java.codeobjects.JavaCodeAssembable#assemble(java.lang.StringBuilder, com.xdev.jadoth.codegen.java.codeobjects.JavaCodeGenerator)
		 */
		@Override
		public void assemble(final StringBuilder sb, final JavaCodeGenerator codeGenerator)
		{
			
		}

		/**
		 * @param code
		 * @return
		 * @see com.xdev.jadoth.codegen.java.codeobjects.JavaCodeHolder#code(java.lang.CharSequence[])
		 */
		@Override
		public JavaCodeHolder code(final CharSequence... code)
		{
			return null;
		}

		/**
		 * @return
		 * @see com.xdev.jadoth.codegen.java.codeobjects.JavaCodeHolder#getCodeBlock()
		 */
		@Override
		public JavaBlock getCodeBlock()
		{
			return null;
		}

		/**
		 * @param javaClass
		 * @see com.xdev.jadoth.codegen.java.codeobjects.JavaTypeMemberDefinition#registerAtOwner(com.xdev.jadoth.codegen.java.codeobjects.JavaTypeDefinition)
		 */
		@Override
		public void registerAtOwner(final JavaTypeDefinition javaClass)
		{
			
		}

		/**
		 * @param javaClass
		 * @see com.xdev.jadoth.codegen.java.codeobjects.JavaTypeMemberDefinition#setOwner(com.xdev.jadoth.codegen.java.codeobjects.JavaTypeDefinition)
		 */
		@Override
		public void setOwner(final JavaTypeDefinition javaClass)
		{
			
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
		
	}
}
