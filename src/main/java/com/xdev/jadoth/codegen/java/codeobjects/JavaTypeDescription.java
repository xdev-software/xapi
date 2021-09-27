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

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.LinkedHashSet;

import com.xdev.jadoth.lang.functional.Operation;


/**
 * @author Thomas Muenz
 *
 */
public interface JavaTypeDescription 
extends JavaModifierableCompilationObjectDescription, JavaGenericParametrizableObject, 
JavaTypeMemberDescription, JavaClassMemberDescription, JavaKeywordOwner, Type
{	
	public String getClassSignature();
	
	public Iterable<? extends JavaInterfaceDescription> iterateSuperInterfaces();
	
	public Iterable<? extends JavaTypeMemberDescription> iterateMembers();
	
	public Iterable<? extends JavaFieldDescription> iterateFields();
	public Iterable<? extends JavaMethodDescription> iterateMethods();
	
	public Iterable<? extends JavaClassDescription> iterateNestedClasses();	
	public Iterable<? extends JavaInterfaceDescription> iterateNestedInterfaces();
	
	public void processMembers(Operation<JavaTypeMemberDescription> memberProcessor);
	
//	public Iterable<? extends JavaMemberDescription> processMembers(Predicate<JavaMemberDescription> predicate);
	
	public String getSimpleName();	
	public String getName();
	public String getCanonicalName();
		
	
	
	public abstract class Implementation extends JavaModifierableCompilationObjectDescription.Implementation
	implements JavaTypeDescription
	{	
		///////////////////////////////////////////////////////////////////////////
		// static methods   //
		/////////////////////
		
		public static JavaTypeDescription wrap(final Type type)
		{
			if(type instanceof JavaTypeDescription){
				return (JavaTypeDescription)type;
			}
			
			final Class<?> c = (Class<?>)type;
			if(c.isInterface()){
				return new JavaInterfaceWrapper.Implementation(c);
			}
			return new JavaClassWrapper.Implementation(c);
		}
		
		///////////////////////////////////////////////////////////////////////////
		// instance fields  //
		/////////////////////
		
		private final LinkedHashSet<JavaTypeMemberDescription> members = new LinkedHashSet<JavaTypeMemberDescription>();

		/**
		 * @param modifier
		 * @param name
		 */
		Implementation(final int modifier, final String name)
		{
			super(modifier, name);
		}
				
		
		/**
		 * @param modifier
		 * @return
		 * @see com.xdev.jadoth.codegen.java.codeobjects.JavaModifierableCompilationObjectDescription.Implementation#isValidModifier(int)
		 */
		@Override
		protected boolean isValidModifier(final int modifier)
		{
			return false;
		}

//		/**
//		 * @return
//		 * @see net.jadoth.codegen.java.codeobjects.JavaCompilationUnitDescription#iterateFields()
//		 */
//		@Override
//		public Iterable<JavaFieldDescription> iterateFields()
//		{
//			return null;
//		}

		/**
		 * @return
		 * @see net.jadoth.codegen.java.codeobjects.JavaCompilationUnitDescription#iterateAllMembers()
		 */
		@Override
		public Iterable<JavaTypeMemberDescription> iterateMembers()
		{
			return null;
		}

//		/**
//		 * @return
//		 * @see net.jadoth.codegen.java.codeobjects.JavaCompilationUnitDescription#iterateMethods()
//		 */
//		@Override
//		public Iterable<JavaMethodDescription> iterateMethods()
//		{
//			return null;
//		}

//		/**
//		 * @return
//		 * @see net.jadoth.codegen.java.codeobjects.JavaCompilationUnitDescription#iterateNestedTypes()
//		 */
//		@Override
//		public Iterable<JavaTypeDescription> iterateNestedTypes()
//		{
//			return null;
//		}

		/**
		 * @return
		 * @see net.jadoth.codegen.java.codeobjects.JavaCompilationUnitDescription#iterateSuperInterfaces()
		 */
		@Override
		public Iterable<JavaInterfaceDescription> iterateSuperInterfaces()
		{
			return null;
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
		 * @see com.xdev.jadoth.codegen.java.codeobjects.JavaTypeDescription#getClassSignature()
		 */
		@Override
		public String getClassSignature()
		{
			return null;
		}

		/**
		 * @param memberProcessor
		 */
		@Override
		public void processMembers(final Operation<JavaTypeMemberDescription> memberProcessor)
		{
			for(final JavaTypeMemberDescription member : this.members) {
				memberProcessor.execute(member);
			}			
		}

		/**
		 * @return
		 */
		@Override
		public Iterable<JavaFieldDescription> iterateFields()
		{
			final ArrayList<JavaFieldDescription> fields = new ArrayList<JavaFieldDescription>(this.members.size());
			for(final JavaTypeMemberDescription member : this.members) {
				if(member instanceof JavaFieldDescription){
					fields.add((JavaFieldDescription)member);
				}
			}
			return fields;
		}

		/**
		 * @return
		 */
		@Override
		public Iterable<JavaMethodDescription> iterateMethods()
		{
			final ArrayList<JavaMethodDescription> methods = new ArrayList<JavaMethodDescription>(this.members.size());
			for(final JavaTypeMemberDescription member : this.members) {
				if(member instanceof JavaMethodDescription){
					methods.add((JavaMethodDescription)member);
				}
			}
			return methods;
		}

		/**
		 * @return
		 */
		@Override
		public Iterable<JavaClassDescription> iterateNestedClasses()
		{
			final ArrayList<JavaClassDescription> classes = new ArrayList<JavaClassDescription>(this.members.size());
			for(final JavaTypeMemberDescription member : this.members) {
				if(member instanceof JavaClassDescription){
					classes.add((JavaClassDescription)member);
				}
			}
			return classes;
		}

		/**
		 * @return
		 */
		@Override
		public Iterable<JavaInterfaceDescription> iterateNestedInterfaces()
		{
			final ArrayList<JavaInterfaceDescription> interfaces = new ArrayList<JavaInterfaceDescription>(this.members.size());
			for(final JavaTypeMemberDescription member : this.members) {
				if(member instanceof JavaInterfaceDescription){
					interfaces.add((JavaInterfaceDescription)member);
				}
			}
			return interfaces;
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


				
	}
	
}
