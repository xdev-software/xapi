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

import java.util.ArrayList;
import java.util.LinkedHashSet;

import com.xdev.jadoth.lang.functional.Operation;


/**
 * @author Thomas Muenz
 *
 */
public interface JavaTypeDefinition 
extends JavaTypeDescription, JavaCompilationUnitMember, JavaTypeMemberDefinition, JavaClassMemberDefinition 
{
	public static final int VALID_MODIFIERS = JavaModifier.VISIBILITY;
	
	@Override
	public Iterable<JavaInterfaceDefinition> iterateSuperInterfaces();	
	@Override
	public Iterable<JavaFieldDefinition> iterateFields();
	@Override
	public Iterable<JavaMethodDefinition> iterateMethods();
	@Override
	public Iterable<JavaClassDefinition> iterateNestedClasses();
	@Override
	public Iterable<JavaInterfaceDefinition> iterateNestedInterfaces();
	@Override
	public Iterable<JavaTypeMemberDefinition> iterateMembers();
	
	
	
	public JavaTypeDefinition add(JavaTypeMemberDefinition javaMember);
	public JavaTypeDefinition add(JavaFieldDefinition javaField);
	public JavaTypeDefinition add(JavaMethodDefinition javaMethod);
	
	public JavaTypeDefinition add(JavaTypeDefinition javaClass);
	public JavaTypeDefinition add(JavaInterfaceDefinition javaClass);
	public JavaTypeDefinition add(JavaClassDefinition javaClass);
	
	public JavaTypeDefinition add(JavaTypeMemberDefinition... javaMembers);
	public JavaTypeDefinition add(JavaFieldDefinition... javaFields);
	public JavaTypeDefinition add(JavaMethodDefinition... javaMethods);
	
	public JavaTypeDefinition add(JavaTypeDefinition... javaClasses);
	public JavaTypeDefinition add(JavaInterfaceDefinition... javaClasses);
	public JavaTypeDefinition add(JavaClassDefinition... javaClasses);
	
	public JavaTypeDefinition addSuperInterface(JavaInterfaceDefinition javaInterface);
		
	
		
	
	
	public abstract class Implementation extends JavaModifierableCompilationObjectDefinition.Implementation
	implements JavaTypeDefinition
	{		
		///////////////////////////////////////////////////////////////////////////
		// instance fields  //
		/////////////////////
		
		private final LinkedHashSet<JavaInterfaceDefinition> superInterfaces = new LinkedHashSet<JavaInterfaceDefinition>();
		
		//member collections
//		private final LinkedHashSet<JavaFieldDefinition> fields = new LinkedHashSet<JavaFieldDefinition>();
//		private final LinkedHashSet<JavaMethodDefinition> methods = new LinkedHashSet<JavaMethodDefinition>();		
//		private final LinkedHashSet<JavaInterfaceDefinition> nestedInterfaces = new LinkedHashSet<JavaInterfaceDefinition>();
//		private final LinkedHashSet<JavaClassDefinition> nestedClasses = new LinkedHashSet<JavaClassDefinition>();
		
		//addiotnal indexing member collections
		private final LinkedHashSet<JavaTypeMemberDefinition> members = new LinkedHashSet<JavaTypeMemberDefinition>();		
//		private final LinkedHashSet<JavaTypeDefinition> nestedCompilationUnits = 
//			new LinkedHashSet<JavaTypeDefinition>();
		
		private final LinkedHashSet<JavaCompilationUnitMember> elements = 
			new LinkedHashSet<JavaCompilationUnitMember>();
				
		

		/**
		 * @param modifiers
		 * @param name
		 */
		Implementation(final int modifiers, final String name)
		{
			super(modifiers, name);
		}

		/**
		 * @param modifier
		 * @return
		 * @see com.xdev.jadoth.codegen.java.codeobjects.JavaModifierableCompilationObjectDefinition.Implementation#isValidModifier(int)
		 */
		@Override
		protected boolean isValidModifier(final int modifier)
		{
			return (modifier & ~getValidModifiers()) == 0;
		}
		
		
		protected int getValidModifiers()
		{
			return VALID_MODIFIERS;
		}


		/**
		 * @return
		 * @see com.xdev.jadoth.codegen.java.codeobjects.JavaCompilationUnit#iterateAllMembers()
		 */
		@Override
		public Iterable<JavaTypeMemberDefinition> iterateMembers()
		{
			return this.members;
		}

//		/**
//		 * @return
//		 * @see net.jadoth.codegen.java.codeobjects.JavaCompilationUnit#iterateFields()
//		 */
//		@Override
//		public Iterable<JavaFieldDefinition> iterateFields()
//		{
//			return this.fields;
//		}

//		/**
//		 * @return
//		 * @see net.jadoth.codegen.java.codeobjects.JavaCompilationUnit#iterateMethods()
//		 */
//		@Override
//		public Iterable<JavaMethodDefinition> iterateMethods()
//		{
//			return this.methods;
//		}

//		/**
//		 * @return
//		 * @see net.jadoth.codegen.java.codeobjects.JavaCompilationUnit#iterateNestedTypes()
//		 */
//		@Override
//		public Iterable<JavaTypeDefinition> iterateNestedTypes()
//		{
//			return this.nestedCompilationUnits;
//		}

		/**
		 * @return
		 * @see com.xdev.jadoth.codegen.java.codeobjects.JavaCompilationUnit#iterateSuperInterfaces()
		 */
		@Override
		public Iterable<JavaInterfaceDefinition> iterateSuperInterfaces()
		{
			return this.superInterfaces;
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
		 * @param javaMember
		 * @return
		 * @see com.xdev.jadoth.codegen.java.codeobjects.JavaCompilationUnit#add(com.xdev.jadoth.codegen.java.codeobjects.JavaTypeMemberDefinition)
		 */
		@Override
		public JavaTypeDefinition.Implementation add(final JavaTypeMemberDefinition javaMember)
		{
			javaMember.registerAtOwner(this);
			return this;
		}

		/**
		 * @param javaField
		 * @return
		 * @see com.xdev.jadoth.codegen.java.codeobjects.JavaCompilationUnit#add(com.xdev.jadoth.codegen.java.codeobjects.JavaFieldDefinition)
		 */
		@Override
		public JavaTypeDefinition.Implementation add(final JavaFieldDefinition javaField)
		{
			javaField.setOwner(this);			
			this.members.add(javaField);
			return this;
		}

		/**
		 * @param javaMethod
		 * @return
		 * @see com.xdev.jadoth.codegen.java.codeobjects.JavaCompilationUnit#add(com.xdev.jadoth.codegen.java.codeobjects.JavaMethodDefinition)
		 */
		@Override
		public JavaTypeDefinition.Implementation add(final JavaMethodDefinition javaMethod)
		{
			javaMethod.setOwner(this);
			this.members.add(javaMethod);
			return this;
		}

		/**
		 * @param javaNestedClass
		 * @return
		 * @see com.xdev.jadoth.codegen.java.codeobjects.JavaCompilationUnit#addNestedClass(net.jadoth.codegen.java.codeobjects.JavaNestedClass)
		 */
		@Override
		public JavaTypeDefinition.Implementation add(final JavaClassDefinition javaNestedClass)
		{
			javaNestedClass.setOwner(this);
			this.members.add(javaNestedClass);
			return this;
		}

		/**
		 * @param javaNestedCompilationUnit
		 * @return
		 * @see com.xdev.jadoth.codegen.java.codeobjects.JavaCompilationUnit#addNestedCompilationUnit(net.jadoth.codegen.java.codeobjects.JavaNestedCompilationUnit)
		 */
		@Override
		public JavaTypeDefinition.Implementation add(final JavaTypeDefinition javaNestedCompilationUnit)
		{
			javaNestedCompilationUnit.registerAtOwner(this);
			return this;
		}

		/**
		 * @param javaNestedInterface
		 * @return
		 * @see com.xdev.jadoth.codegen.java.codeobjects.JavaCompilationUnit#addNestedInterface(net.jadoth.codegen.java.codeobjects.JavaNestedInterface)
		 */
		@Override
		public JavaTypeDefinition.Implementation add(final JavaInterfaceDefinition javaNestedInterface)
		{
			javaNestedInterface.setOwner(this);
			this.members.add(javaNestedInterface);
			return this;
		}

		/**
		 * @return
		 * @see com.xdev.jadoth.codegen.java.codeobjects.JavaCompilationUnit#addSuperInterface(com.xdev.jadoth.codegen.java.codeobjects.JavaInterfaceDefinition)
		 */
		@Override
		public JavaTypeDefinition.Implementation addSuperInterface(final JavaInterfaceDefinition javaInterface)
		{
			this.superInterfaces.add(javaInterface);
			return this;
		}
		
		
		/**
		 * @param memberProcessor
		 */
		@Override
		public void processMembers(final Operation<JavaTypeMemberDescription> memberProcessor)
		{
			for(final JavaTypeMemberDefinition member : this.members) {
				memberProcessor.execute(member);
			}
			
		}
		
		
		/**
		 * @return
		 */
		@Override
		public Iterable<JavaFieldDefinition> iterateFields()
		{
			final ArrayList<JavaFieldDefinition> fields = new ArrayList<JavaFieldDefinition>(this.members.size());
			for(final JavaTypeMemberDescription member : this.members) {
				if(member instanceof JavaFieldDefinition){
					fields.add((JavaFieldDefinition)member);
				}
			}
			return fields;
		}

		/**
		 * @return
		 */
		@Override
		public Iterable<JavaMethodDefinition> iterateMethods()
		{
			final ArrayList<JavaMethodDefinition> methods = new ArrayList<JavaMethodDefinition>(this.members.size());
			for(final JavaTypeMemberDescription member : this.members) {
				if(member instanceof JavaMethodDefinition){
					methods.add((JavaMethodDefinition)member);
				}
			}
			return methods;
		}
		
		/**
		 * @return
		 */
		@Override
		public Iterable<JavaClassDefinition> iterateNestedClasses()
		{
			final ArrayList<JavaClassDefinition> classes = new ArrayList<JavaClassDefinition>(this.members.size());
			for(final JavaTypeMemberDescription member : this.members) {
				if(member instanceof JavaClassDefinition){
					classes.add((JavaClassDefinition)member);
				}
			}
			return classes;
		}
		
		/**
		 * @return
		 */
		@Override
		public Iterable<JavaInterfaceDefinition> iterateNestedInterfaces()
		{
			final ArrayList<JavaInterfaceDefinition> interfaces = new ArrayList<JavaInterfaceDefinition>(this.members.size());
			for(final JavaTypeMemberDescription member : this.members) {
				if(member instanceof JavaInterfaceDefinition){
					interfaces.add((JavaInterfaceDefinition)member);
				}
			}
			return interfaces;
		}
		
	}
}
