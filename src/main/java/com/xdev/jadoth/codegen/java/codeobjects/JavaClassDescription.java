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

import java.util.LinkedHashSet;

import com.xdev.jadoth.codegen.java.Java;




/**
 * Represents a Java class.
 * 
 * @author Thomas Muenz
 *
 */
public interface JavaClassDescription extends JavaTypeDescription
{
	public Iterable<? extends JavaClassMemberDescription> iterateClassMembers();
	
	public Iterable<? extends JavaConstructorDescription> iterateConstructors();
	
	public Iterable<? extends JavaInitializer> iterateInitializers();
	
	
			
	
	public class Implementation extends JavaTypeDescription.Implementation implements JavaClassDescription
	{
		///////////////////////////////////////////////////////////////////////////
		// instance fields  //
		/////////////////////	
		
		private Class<?> wrappedClass = null;
		private transient JavaClassDescription superClassDescription = null;
		
		private final LinkedHashSet<JavaClassMemberDefinition> classMembers = new LinkedHashSet<JavaClassMemberDefinition>();
		
		
		
		///////////////////////////////////////////////////////////////////////////
		// constructors //
		/////////////////
		
		/**
		 * @param modifiers
		 * @param name
		 */
		Implementation(final int modifiers, final String name)
		{
			super(modifiers, name);
		}
		
		Implementation(final Class<?> wrappedClass)
		{
			super(wrappedClass.getModifiers(), wrappedClass.getSimpleName());
		}
		
		
		
		///////////////////////////////////////////////////////////////////////////
		// getters          //
		/////////////////////
		
		/**
		 * @return the superClass
		 */
		public JavaClassDescription getSuperClassDescription()
		{
			if(superClassDescription == null && this.wrappedClass != null){
				final Class<?> wrapperClassSuperClass = this.wrappedClass.getSuperclass();
				if(wrapperClassSuperClass != null && wrapperClassSuperClass != Object.class){
					this.superClassDescription = new JavaClassDescription.Implementation(wrapperClassSuperClass);
				}
			}
			return this.superClassDescription;
		}
		
		
		
		///////////////////////////////////////////////////////////////////////////
		// setters          //
		/////////////////////
		
		/**
		 * @param javaClass
		 * @return
		 */
		protected void setSuperClassDescription(final JavaClassDescription javaClass)
		{
			this.superClassDescription = javaClass;
		}
		
		
		
		///////////////////////////////////////////////////////////////////////////
		// override methods //
		/////////////////////
		
		@Override
		public Iterable<JavaClassMemberDefinition> iterateClassMembers()
		{
			return this.classMembers;
		}

		/**
		 * @return
		 */
		@Override
		public Iterable<? extends JavaConstructorDescription> iterateConstructors()
		{
			return null;
		}

		/**
		 * @return
		 */
		@Override
		public Iterable<? extends JavaInitializer> iterateInitializers()
		{
			return null;
		}

		/**
		 * @return
		 * @see com.xdev.jadoth.codegen.java.codeobjects.JavaKeywordOwner#getKeyword()
		 */
		@Override
		public String getKeyword()
		{
			return Java.Lang.$class;
		}
		
	}
	
}
