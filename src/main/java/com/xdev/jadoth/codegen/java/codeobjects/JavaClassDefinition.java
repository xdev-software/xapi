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
import static com.xdev.jadoth.codegen.java.codeobjects.JavaModifier.VISIBILITY;

import java.lang.reflect.Type;
import java.util.LinkedHashSet;

import com.xdev.jadoth.codegen.java.Java;




/**
 * Represents a Java class.
 * 
 * @author Thomas Muenz
 *
 */
public interface JavaClassDefinition extends JavaClassDescription, JavaTypeDefinition
{
	public static final int VALID_MODIFIERS = VISIBILITY|STATIC|FINAL|ABSTRACT;
	
	public JavaClassDescription getSuperClassDescription();
	
	public boolean addInterface(JavaInterfaceDescription javaInterface);
	
	
//	public JavaClassDefinition implementS(JavaInterfaceDescription... superInterfaces);
//	public JavaClassDefinition implementS(Class<?>... superInterfaces);
	
	public JavaClassDefinition implementS(Type... superInterfaces);
	
	public JavaClassDefinition extendS(JavaClassDescription superClass);
	public JavaClassDefinition extendS(Class<?> superClass);
	
	
	public boolean add(JavaClassMemberDefinition member);
	
	public boolean add(JavaClassMemberDefinition... members);	

	public boolean addConstructor(JavaConstructorDescription javaConstructor);
	public boolean addInitializer(JavaInitializer javaInitializer);
	public boolean addStaticInitializer(JavaStaticInitializer javaStaticInitializer);
	
	
	
	
	public class Implementation extends JavaTypeDefinition.Implementation 
	implements JavaClassDefinition
	{
		///////////////////////////////////////////////////////////////////////////
		// instance fields  //
		/////////////////////
		
		private JavaClassDescription superClass = null;		
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


		/**
		 * @param javaConstructor
		 * @return
		 * @see com.xdev.jadoth.codegen.java.codeobjects.JavaClassDefinition#addConstructor(com.xdev.jadoth.codegen.java.codeobjects.JavaConstructorDescription)
		 */
		@Override
		public boolean addConstructor(final JavaConstructorDescription javaConstructor)
		{
			return false;
		}


		/**
		 * @param javaInitializer
		 * @return
		 * @see com.xdev.jadoth.codegen.java.codeobjects.JavaClassDefinition#addInitializer(com.xdev.jadoth.codegen.java.codeobjects.JavaInitializer)
		 */
		@Override
		public boolean addInitializer(final JavaInitializer javaInitializer)
		{
			return false;
		}


		/**
		 * @param javaInterface
		 * @return
		 * @see com.xdev.jadoth.codegen.java.codeobjects.JavaClassDefinition#addInterface(com.xdev.jadoth.codegen.java.codeobjects.JavaInterfaceDefinition)
		 */
		@Override
		public boolean addInterface(final JavaInterfaceDescription javaInterface)
		{
			return false;
		}



		/**
		 * @param javaStaticInitializer
		 * @return
		 * @see com.xdev.jadoth.codegen.java.codeobjects.JavaClassDefinition#addStaticInitializer(com.xdev.jadoth.codegen.java.codeobjects.JavaStaticInitializer)
		 */
		@Override
		public boolean addStaticInitializer(final JavaStaticInitializer javaStaticInitializer)
		{
			return javaStaticInitializer.setOwner(this);
		}


		@Override
		public Iterable<JavaClassMemberDefinition> iterateClassMembers()
		{
			return this.classMembers;
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
		 * @param javaMembers
		 * @return
		 */
		@Override
		public JavaClassDefinition.Implementation add(final JavaTypeMemberDefinition... javaMembers)
		{
			for(final JavaTypeMemberDefinition javaMemberDefinition : javaMembers) {
				javaMemberDefinition.registerAtOwner(this);
			}
			return this;
		}


		/**
		 * @param javaFields
		 * @return
		 */
		@Override
		public JavaClassDefinition.Implementation add(final JavaFieldDefinition... javaFields)
		{
			for(final JavaFieldDefinition javaField : javaFields) {
				javaField.registerAtOwner(this);
			}
			return this;
		}


		/**
		 * @param javaMethods
		 * @return
		 */
		@Override
		public JavaClassDefinition.Implementation add(final JavaMethodDefinition... javaMethods)
		{
			for(final JavaMethodDefinition javaMethod : javaMethods) {
				javaMethod.registerAtOwner(this);
			}
			return this;
		}


		/**
		 * @param javaNestedCompilationUnits
		 * @return
		 */
		@Override
		public JavaClassDefinition.Implementation add(final JavaTypeDefinition... javaNestedCompilationUnits)
		{
			for(final JavaTypeDefinition javaNestedCompilationUnit : javaNestedCompilationUnits) {
				javaNestedCompilationUnit.registerAtOwner(this);
			}
			return this;
		}


		/**
		 * @param javaNestedInterfaces
		 * @return
		 */
		@Override
		public JavaClassDefinition.Implementation add(final JavaInterfaceDefinition... javaNestedInterfaces)
		{
			for(final JavaInterfaceDefinition javaNestedInterface : javaNestedInterfaces) {
				javaNestedInterface.registerAtOwner(this);
			}
			return this;
		}





		/**
		 * @return
		 */
		@Override
		public JavaClassDescription getSuperClassDescription()
		{
			return this.superClass;
		}


		/**
		 * @param javaClass
		 * @return
		 */
		@Override
		public JavaClassDefinition.Implementation add(final JavaClassDefinition javaClass)
		{
			return this;
		}


		/**
		 * @param javaNestedClasss
		 * @return
		 */
		@Override
		public JavaClassDefinition.Implementation add(final JavaClassDefinition... javaNestedClasss)
		{
			for(final JavaClassDefinition javaNestedClass : javaNestedClasss) {
				javaNestedClass.registerAtOwner(this);
			}
			return this;
		}


		/**
		 * @return
		 */
		@Override
		public JavaTypeDescription getOwnerType()
		{
			return null;
		}


		/**
		 * @param compilationUnit
		 * @return
		 */
		@Override
		public void registerAtOwner(final JavaTypeDefinition compilationUnit)
		{
		}


		/**
		 * @param owner
		 */
		@Override
		public void setOwner(final JavaTypeDefinition owner)
		{
			
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
		 * @see com.xdev.jadoth.codegen.java.codeobjects.JavaCompilationUnitMember#getOwnerCompilationUnit()
		 */
		@Override
		public JavaCompilationUnit getOwnerCompilationUnit()
		{
			return null;
		}


		/**
		 * @param compilationUnit
		 * @return
		 * @see com.xdev.jadoth.codegen.java.codeobjects.JavaCompilationUnitMember#registerAtOwner(JavaCompilationUnit)
		 */
		@Override
		public JavaCompilationUnitMember registerAtOwner(final JavaCompilationUnit compilationUnit)
		{
			compilationUnit.add(this);
			return this;
		}


		/**
		 * @param owner
		 * @see com.xdev.jadoth.codegen.java.codeobjects.JavaCompilationUnitMember#setOwner(JavaCompilationUnit)
		 */
		@Override
		public void setOwner(final JavaCompilationUnit owner)
		{
			
		}


		/**
		 * @param member
		 * @return
		 * @see com.xdev.jadoth.codegen.java.codeobjects.JavaClassDefinition#add(com.xdev.jadoth.codegen.java.codeobjects.JavaClassMemberDefinition)
		 */
		@Override
		public boolean add(final JavaClassMemberDefinition member)
		{
			return false;
		}


		/**
		 * @param members
		 * @return
		 * @see com.xdev.jadoth.codegen.java.codeobjects.JavaClassDefinition#add(com.xdev.jadoth.codegen.java.codeobjects.JavaClassMemberDefinition[])
		 */
		@Override
		public boolean add(final JavaClassMemberDefinition... members)
		{
			return false;
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
		 * @param superClass
		 * @return
		 * @see com.xdev.jadoth.codegen.java.codeobjects.JavaClassDefinition#extendS(com.xdev.jadoth.codegen.java.codeobjects.JavaClassDescription)
		 */
		@Override
		public JavaClassDefinition extendS(final JavaClassDescription superClass)
		{
			return null;
		}


//		/**
//		 * @param superInterfaces
//		 * @return
//		 * @see net.jadoth.codegen.java.codeobjects.JavaClassDefinition#implementS(net.jadoth.codegen.java.codeobjects.JavaInterfaceDescription[])
//		 */
//		@Override
//		public JavaClassDefinition implementS(final JavaInterfaceDescription... superInterfaces)
//		{
//			return null;
//		}


		/**
		 * @param superClass
		 * @return
		 * @see com.xdev.jadoth.codegen.java.codeobjects.JavaClassDefinition#extendS(java.lang.Class)
		 */
		@Override
		public JavaClassDefinition extendS(final Class<?> superClass)
		{
			return null;
		}


		/**
		 * @param superInterfaces
		 * @return
		 * @see net.jadoth.codegen.java.codeobjects.JavaClassDefinition#implementS(java.lang.Class<?>[])
		 */
		@Override
		public JavaClassDefinition implementS(final Type... superInterfaces)
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
		 * @param codeGenerator
		 * @see com.xdev.jadoth.codegen.java.codeobjects.JavaCodeAssembable#assemble(StringBuilder, com.xdev.jadoth.codegen.java.codeobjects.JavaCodeGenerator)
		 */
		@Override
		public void assemble(final StringBuilder sb, final JavaCodeGenerator codeGenerator)
		{
			codeGenerator.assembleJavaClassDefinition(sb, this);			
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
		 * @see com.xdev.jadoth.codegen.java.codeobjects.JavaCompilationUnitMember#getAssociatedComment()
		 */
		@Override
		public JavaCommentBlock getAssociatedComment()
		{
			return null;
		}


		/**
		 * @return
		 * @see com.xdev.jadoth.codegen.java.codeobjects.JavaCompilationUnitMember#getJavaDoc()
		 */
		@Override
		public JavaDocBlock getJavaDoc()
		{
			return null;
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
