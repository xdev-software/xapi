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
package com.xdev.jadoth.codegen.java.codeobjects;

import java.util.ArrayList;

import com.xdev.jadoth.lang.functional.Operation;


/**
 * @author Thomas Muenz
 *
 */
public interface JavaPrimitiveType extends JavaTypeDescription
{
	
	
	public Class<?> getPrimitiveClass();
	
	
	public class Implementation extends JavaCompilationObject.Implementation implements JavaPrimitiveType
	{
		///////////////////////////////////////////////////////////////////////////
		// instance fields  //
		/////////////////////
		
		private final Class<?> primitiveClass;
		
		
		
		Implementation(final Class<?> primitiveClass)
		{
			super(null);
			this.primitiveClass = primitiveClass;
		}

		@Override
		public Class<?> getPrimitiveClass()
		{
			return primitiveClass;
		}

		@Override
		public String getClassSignature()
		{
			return null;
		}


		/**
		 * @return
		 */
		@Override
		public Iterable<JavaTypeMemberDescription> iterateMembers()
		{
			return null;
		}


		/**
		 * @return
		 */
		@Override
		public Iterable<JavaInterfaceDescription> iterateSuperInterfaces()
		{
			return null;
		}

		/**
		 * @return
		 */
		@Override
		public Iterable<JavaGenericParameter> iterateGenericParameters()
		{
			return null;
		}

		/**
		 * @return
		 */
		@Override
		public int getModifiers()
		{
			return 0;
		}

		/**
		 * @return
		 */
		@Override
		public String getName()
		{
			return null;
		}

		/**
		 * @param memberProcessor
		 */
		@Override
		public void processMembers(final Operation<JavaTypeMemberDescription> memberProcessor)
		{
			
		}

		/**
		 * @return
		 */
		@Override
		public Iterable<JavaFieldDescription> iterateFields()
		{
			return new ArrayList<JavaFieldDescription>(0);
		}

		/**
		 * @return
		 */
		@Override
		public Iterable<JavaMethodDescription> iterateMethods()
		{
			return new ArrayList<JavaMethodDescription>(0);
		}

		/**
		 * @return
		 */
		@Override
		public Iterable<JavaClassDescription> iterateNestedClasses()
		{
			return new ArrayList<JavaClassDescription>(0);
		}

		/**
		 * @return
		 */
		@Override
		public Iterable<JavaInterfaceDescription> iterateNestedInterfaces()
		{
			return new ArrayList<JavaInterfaceDescription>(0);
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
		 * @see com.xdev.jadoth.codegen.java.codeobjects.JavaClassMemberDescription#getOwnerClass()
		 */
		@Override
		public JavaClassDefinition getOwnerClass()
		{
			return null;
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
		 * @see com.xdev.jadoth.codegen.java.codeobjects.JavaTypeDescription#getCanonicalName()
		 */
		@Override
		public String getCanonicalName()
		{
			return null;
		}

		/**
		 * @return
		 * @see com.xdev.jadoth.codegen.java.codeobjects.JavaTypeDescription#getSimpleName()
		 */
		@Override
		public String getSimpleName()
		{
			return null;
		}

		/**
		 * @return
		 */
		@Override
		public int getNestingLevel()
		{
			return 0;
		}
		
		/**
		 * @return
		 * @see com.xdev.jadoth.codegen.java.codeobjects.JavaKeywordOwner#getKeyword()
		 */
		@Override
		public String getKeyword()
		{
			return null;
		}
		
	}

}
