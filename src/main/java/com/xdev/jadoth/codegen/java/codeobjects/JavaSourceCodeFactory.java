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

import static com.xdev.jadoth.codegen.java.codeobjects.JavaModifier.DEFAULT;
import static com.xdev.jadoth.codegen.java.codeobjects.JavaModifier.FINAL;
import static com.xdev.jadoth.codegen.java.codeobjects.JavaModifier.PRIVATE;
import static com.xdev.jadoth.codegen.java.codeobjects.JavaModifier.PROTECTED;
import static com.xdev.jadoth.codegen.java.codeobjects.JavaModifier.PUBLIC;
import static com.xdev.jadoth.codegen.java.codeobjects.JavaModifier.STATIC;

import java.lang.reflect.Member;
import java.lang.reflect.Type;
import java.util.HashMap;



/**
 * @author Thomas Muenz
 *
 */
public interface JavaSourceCodeFactory
{	
	public static final JavaPrimitiveType boolean$ = new JavaPrimitiveType.Implementation(boolean.class);
	public static final JavaPrimitiveType byte$ = new JavaPrimitiveType.Implementation(byte.class);
	public static final JavaPrimitiveType short$ = new JavaPrimitiveType.Implementation(short.class);
	public static final JavaPrimitiveType int$ = new JavaPrimitiveType.Implementation(int.class);
	public static final JavaPrimitiveType long$ = new JavaPrimitiveType.Implementation(long.class);
	public static final JavaPrimitiveType float$ = new JavaPrimitiveType.Implementation(float.class);
	public static final JavaPrimitiveType double$ = new JavaPrimitiveType.Implementation(double.class);
	public static final JavaPrimitiveType char$ = new JavaPrimitiveType.Implementation(char.class);
	
	public static abstract class Code
	{
		private static HashMap<Class<?>, JavaClassDescription> wrappedClasses = 
			new HashMap<Class<?>, JavaClassDescription>();
		
		public static final JavaClassDescription wrap(final Class<?> existingClass)
		{
			JavaClassDescription jcd = wrappedClasses.get(existingClass);
			if(jcd == null){
				jcd = new JavaClassDescription.Implementation(existingClass);
				wrappedClasses.put(existingClass, jcd);
			}
			return jcd;
		}
		
		
		
		public static final JavaInitializer init(final CharSequence... code)
		{
			return new JavaInitializer.Implementation().code(code);
		}
		public static final JavaInitializer clinit(final CharSequence... code)
		{
			return new JavaInitializer.Implementation().code(code);
		}
		
		public static final JavaFieldDefinition Field(
			final int modifiers, final JavaTypeDescription type, final String name
		)
		{
			return Field(modifiers, type, name, null);
		}
		public static final JavaFieldDefinition Field(
			final int modifiers, final JavaTypeDescription type, final String name, final JavaExpression initialValue
		)
		{
			return new JavaFieldDefinition.Implemenatation(modifiers, type, name, initialValue);
		}
		
		public static final JavaConstructorDefinition Constructor(
			final JavaParameter... parameters
		)
		{
			return Constructor(0, parameters);
		}
		
		public static final JavaConstructorDefinition Constructor(
			final int modifiers,
			final JavaParameter... parameters
		)
		{
			return null;
		}
		
		public static final JavaMethodDefinition Method(
			final int modifiers, 
			final JavaTypeDescription returnType, 
			final String name,
			final JavaBlock code
		)
		{
			return Method(modifiers, returnType, name, null, null, code);
		}
		public static final JavaMethodDefinition Method(
			final int modifiers, 
			final JavaTypeDescription returnType, 
			final String name, 
			final JavaParameter[] parameters,
			final JavaBlock code
		)
		{
			return Method(modifiers, returnType, name, parameters, null, code);
		}
		public static final JavaMethodDefinition Method(
			final int modifiers, 
			final JavaTypeDescription returnType, 
			final String name, 
			final JavaParameter[] parameters,
			final JavaThrowableType[] throwableDeclarations,
			final JavaBlock code
		)
		{
			return null;
		}
		
		public static final JavaClassDefinition Class(
			final int modifiers, final String name
		)
		{
			return Class(modifiers, name, (JavaClassDescription)null);
		}
		public static final JavaClassDefinition Class(
			final int modifiers, final String name, final JavaClassDescription superClass
		)
		{
			return Class(modifiers, name, superClass);
		}	
		public static final JavaClassDefinition Class(
			final int modifiers, final String name, final JavaInterfaceDefinition... interfaces
		)
		{
			return Class(modifiers, name, null, interfaces);
		}
		public static final JavaClassDefinition Class(
			final int modifiers, final String name, final JavaClassDescription superClass, final JavaInterfaceDefinition... interfaces
		)
		{
			return null;
		}
		
		public static final JavaPrimitiveType primitiveType(final Class<?> primitiveType)
		{
			return null;
		}
		public static final JavaTypeDescription existingType(final Class<?> existingType)
		{
			return null;
		}
		public static final JavaObjectType definedType(final JavaCompilationUnit definedType)
		{
			return null;
		}
		
		public static final JavaExpression exp(final int intExpression)
		{
			return null;
		}	
		
		public static final JavaCompilationUnit CompilationUnit(final JavaCompilationUnitMember... members)
		{
			return new JavaCompilationUnit.Implementation().add(members);
		}
		
		public static final JavaPackageDefinition pacKage(final String packageString)
		{
			return new JavaPackageDefinition.Implementation(packageString);
		}
		
		public static final JavaImportStaticDefinition import_static(final Member member)
		{
			return new JavaImportStaticDefinition.Implementation(member);
		}
		
		public static final JavaImportDefinition imporT(final Type type)
		{
			return new JavaImportDefinition.Implementation(type);
		}
		
		public static final JavaCommentLine CommentLine(final String commentLine)
		{
			return new JavaCommentLine.Implementation(commentLine);
		}
		
		public static final JavaCommentLineGroup CommentLineGroup(final String... commentLine)
		{
			return new JavaCommentLineGroup.Implementation(commentLine);
		}
		
		public static final JavaCommentBlock CommentBlock(final String commentBlock)
		{
			return new JavaCommentBlock.Implementation(commentBlock);
		}
		
		public static final JavaDocBlock JavaDoc(final String javaDocString)
		{
			return new JavaDocBlock.Implementation(javaDocString);
		}
		
		
		private Code(){}
	}
	
	
	
	public static final FactoryAny pubLic =    new FactoryAny(PUBLIC);
	public static final FactoryAny protecTed = new FactoryAny(PROTECTED);
	public static final FactoryAny defauLt =   new FactoryAny(DEFAULT);
	public static final FactoryAny prIvate =   new FactoryAny(PRIVATE);
	
	public static final FactoryClassFieldMethod public_static =    new FactoryClassFieldMethod(PUBLIC|STATIC);
	public static final FactoryClassFieldMethod protected_static = new FactoryClassFieldMethod(PROTECTED|STATIC);
	public static final FactoryClassFieldMethod default_static =   new FactoryClassFieldMethod(DEFAULT|STATIC);
	public static final FactoryClassFieldMethod private_static =   new FactoryClassFieldMethod(PRIVATE|STATIC);
	
	public static final FactoryClassFieldMethod public_static_final =    new FactoryClassFieldMethod(PUBLIC|STATIC|FINAL);
	public static final FactoryClassFieldMethod protected_static_final = new FactoryClassFieldMethod(PROTECTED|STATIC|FINAL);
	public static final FactoryClassFieldMethod default_static_final =   new FactoryClassFieldMethod(DEFAULT|STATIC|FINAL);
	public static final FactoryClassFieldMethod private_static_final =   new FactoryClassFieldMethod(PRIVATE|STATIC|FINAL);
			
	public static final FactoryClassMethod public_abstract =    new FactoryClassMethod(PUBLIC|STATIC);
	public static final FactoryClassMethod protected_abstract = new FactoryClassMethod(PUBLIC|STATIC);
	public static final FactoryClassMethod default_abstract =   new FactoryClassMethod(PUBLIC|STATIC);
	public static final FactoryClassMethod private_abstract =   new FactoryClassMethod(PUBLIC|STATIC);
			
	public static final FactoryClassFieldMethod public_final =    new FactoryClassFieldMethod(0);
	public static final FactoryClassFieldMethod protected_final = new FactoryClassFieldMethod(0);
	public static final FactoryClassFieldMethod default_final =   new FactoryClassFieldMethod(0);
	public static final FactoryClassFieldMethod private_final =   new FactoryClassFieldMethod(0);
	
	
	
	public static final FactoryField public_static_transient =    new FactoryField();
	public static final FactoryField protected_static_transient  = new FactoryField();
	public static final FactoryField default_static_transient  =   new FactoryField();
	public static final FactoryField private_static_transient  =   new FactoryField();
	
	public static final FactoryField public_final_transient =    new FactoryField();
	public static final FactoryField protected_final_transient = new FactoryField();
	public static final FactoryField default_final_transient =   new FactoryField();
	public static final FactoryField private_final_transient =   new FactoryField();
			
	public static final FactoryField public_transient =    new FactoryField();
	public static final FactoryField protected_transient = new FactoryField();
	public static final FactoryField default_transient =   new FactoryField();
	public static final FactoryField private_transient =   new FactoryField();
	
	
	
	
	
	
	public static final FactoryMethod public_final_synchronized =    new FactoryMethod(0);
	public static final FactoryMethod protected_final_synchronized = new FactoryMethod(0);
	public static final FactoryMethod default_final_synchronized =   new FactoryMethod(0);
	public static final FactoryMethod private_final_synchronized =   new FactoryMethod(0);
	
	public static final FactoryMethod public_static_synchronized =    new FactoryMethod(0);
	public static final FactoryMethod protected_static_synchronized = new FactoryMethod(0);
	public static final FactoryMethod default_static_synchronized =   new FactoryMethod(0);
	public static final FactoryMethod private_static_synchronized =   new FactoryMethod(0);
	
	public static final FactoryMethod public_synchronized =    new FactoryMethod(0);
	public static final FactoryMethod protected_synchronized = new FactoryMethod(0);
	public static final FactoryMethod default_synchronized =   new FactoryMethod(0);
	public static final FactoryMethod private_synchronized =   new FactoryMethod(0);
	
	public static final FactoryMethod public_static_final_synchronized =    new FactoryMethod(0);
	public static final FactoryMethod protected_static_final_synchronized = new FactoryMethod(0);
	public static final FactoryMethod default_static_final_synchronized =   new FactoryMethod(0);
	public static final FactoryMethod private_static_final_synchronized =   new FactoryMethod(0);
//	
//	
//	
//	
//	
//	
//	public static final FactoryMethod public_static_final_synchronized_native =    new FactoryMethod(0);
//	public static final FactoryMethod protected_static_final_synchronized_native = new FactoryMethod(0);
//	public static final FactoryMethod default_static_final_synchronized_native =   new FactoryMethod(0);
//	public static final FactoryMethod private_static_final_synchronized_native =   new FactoryMethod(0);
//	
//	public static final FactoryMethod public_final_synchronized_native =    new FactoryMethod(0);
//	public static final FactoryMethod protected_final_synchronized_native = new FactoryMethod(0);
//	public static final FactoryMethod default_final_synchronized_native =   new FactoryMethod(0);
//	public static final FactoryMethod private_final_synchronized_native =   new FactoryMethod(0);
//	
//	public static final FactoryMethod public_static_synchronized_native =    new FactoryMethod(0);
//	public static final FactoryMethod protected_static_synchronized_native = new FactoryMethod(0);
//	public static final FactoryMethod default_static_synchronized_native =   new FactoryMethod(0);
//	public static final FactoryMethod private_static_synchronized_native =   new FactoryMethod(0);
//	
//	public static final FactoryMethod public_synchronized_native =    new FactoryMethod(0);
//	public static final FactoryMethod protected_synchronized_native = new FactoryMethod(0);
//	public static final FactoryMethod default_synchronized_native =   new FactoryMethod(0);
//	public static final FactoryMethod private_synchronized_native =   new FactoryMethod(0);
//	
//	
//	
//	
//	public static final FactoryMethod public_static_native =    new FactoryMethod(0);
//	public static final FactoryMethod protected_static_native = new FactoryMethod(0);
//	public static final FactoryMethod default_static_native =   new FactoryMethod(0);
//	public static final FactoryMethod private_static_native =   new FactoryMethod(0);
//	
//	public static final FactoryMethod public_native =    new FactoryMethod(0);
//	public static final FactoryMethod protected_native = new FactoryMethod(0);
//	public static final FactoryMethod default_native =   new FactoryMethod(0);
//	public static final FactoryMethod private_native =   new FactoryMethod(0);
//		
//	public static final FactoryMethod public_final_native =    new FactoryMethod(0);
//	public static final FactoryMethod protected_final_native = new FactoryMethod(0);
//	public static final FactoryMethod default_final_native =   new FactoryMethod(0);
//	public static final FactoryMethod private_final_native =   new FactoryMethod(0);
//	
//	public static final FactoryMethod public_static_final_native =    new FactoryMethod(0);
//	public static final FactoryMethod protected_static_final_native = new FactoryMethod(0);
//	public static final FactoryMethod default_static_final_native =   new FactoryMethod(0);
//	public static final FactoryMethod private_static_final_native =   new FactoryMethod(0);
	

	
		
	
	
	

	public static class FactoryAny
	{
		private final int modifiers; 

		private FactoryAny(final int modifiers)
		{
			super();
			this.modifiers = modifiers;
		}
		
		/**
		 * @param name
		 * @return
		 * @see com.xdev.jadoth.codegen.java.codeobjects.JavaSourceCodeFactory.InterfaceFactory#Interface(java.lang.String)
		 */
		public JavaInterfaceDefinition Interface(final String name)
		{
			return null;
		}

		/**
		 * @param name
		 * @return
		 * @see com.xdev.jadoth.codegen.java.codeobjects.JavaSourceCodeFactory.ClassFactory#Class(java.lang.String)
		 */
		public JavaClassDefinition Class(final String name)
		{
			return new JavaClassDefinition.Implementation(modifiers, name);
		}

		/**
		 * @param type
		 * @param name
		 * @return
		 * @see com.xdev.jadoth.codegen.java.codeobjects.JavaSourceCodeFactory.FieldFactory#Field(com.xdev.jadoth.codegen.java.codeobjects.JavaTypeDescription, java.lang.String)
		 */
		public JavaFieldDefinition Field(final JavaTypeDescription type, final String name)
		{
			return null;
		}

		/**
		 * @param type
		 * @param name
		 * @return
		 * @see com.xdev.jadoth.codegen.java.codeobjects.JavaSourceCodeFactory.MethodFactory#Method(com.xdev.jadoth.codegen.java.codeobjects.JavaTypeDescription, java.lang.String)
		 */
		public JavaMethodDefinition Method(final JavaTypeDescription type, final String name)
		{
			return null;
		}
		
		/**
		 * @param type
		 * @param name
		 * @return
		 * @see com.xdev.jadoth.codegen.java.codeobjects.JavaSourceCodeFactory.MethodFactory#Method(com.xdev.jadoth.codegen.java.codeobjects.JavaTypeDescription, java.lang.String)
		 */
		public JavaConstructorDefinition Constructor(final JavaParameter... parameters)
		{
			return null;
		}
		
	}
	
	public static class FactoryClassFieldMethod
	{
		private final int modifiers; 

		private FactoryClassFieldMethod(final int modifiers)
		{
			super();
			this.modifiers = modifiers;
		}
		
		public FactoryClassFieldMethod add(final int additionalModifiers)
		{
			return new FactoryClassFieldMethod(this.modifiers|additionalModifiers);
		}

		/**
		 * @param name
		 * @return
		 * @see com.xdev.jadoth.codegen.java.codeobjects.JavaSourceCodeFactory.ClassFactory#Class(java.lang.String)
		 */
		public JavaClassDefinition Class(final String name)
		{
			return null;
		}

		/**
		 * @param type
		 * @param name
		 * @return
		 * @see com.xdev.jadoth.codegen.java.codeobjects.JavaSourceCodeFactory.FieldFactory#Field(com.xdev.jadoth.codegen.java.codeobjects.JavaTypeDescription, java.lang.String)
		 */
		public JavaFieldDefinition Field(final JavaTypeDescription type, final String name)
		{
			return new JavaFieldDefinition.Implemenatation(modifiers, type, name, null);
		}

		/**
		 * @param type
		 * @param name
		 * @return
		 * @see com.xdev.jadoth.codegen.java.codeobjects.JavaSourceCodeFactory.MethodFactory#Method(com.xdev.jadoth.codegen.java.codeobjects.JavaTypeDescription, java.lang.String)
		 */
		public JavaMethodDefinition Method(
			final JavaTypeDescription type, final String name, final JavaParameter... parameters
		)
		{
			return null;
		}
		
	}
		
	public static class FactoryClassMethod
	{
		private final int modifiers; 

		private FactoryClassMethod(final int modifiers)
		{
			super();
			this.modifiers = modifiers;
		}
		
		/**
		 * @param name
		 * @return
		 * @see com.xdev.jadoth.codegen.java.codeobjects.JavaSourceCodeFactory.ClassFactory#Class(java.lang.String)
		 */
		public JavaClassDefinition Class(final String name)
		{
			return null;
		}

		/**
		 * @param type
		 * @param name
		 * @return
		 * @see com.xdev.jadoth.codegen.java.codeobjects.JavaSourceCodeFactory.MethodFactory#Method(com.xdev.jadoth.codegen.java.codeobjects.JavaTypeDescription, java.lang.String)
		 */
		public JavaMethodDefinition Method(final JavaTypeDescription type, final String name)
		{
			return new JavaMethodDefinition.Implementation(this.modifiers, type, name);
		}
		
	}
	
	public static class FactoryMethod
	{
		private final int modifiers; 

		private FactoryMethod(final int modifiers)
		{
			super();
			this.modifiers = modifiers;
		}
		
		public FactoryMethod modifiers(final int additionalModifiers)
		{
			return new FactoryMethod(this.modifiers|additionalModifiers);
		}

		/**
		 * @param type
		 * @param name
		 * @return
		 * @see com.xdev.jadoth.codegen.java.codeobjects.JavaSourceCodeFactory.MethodFactory#Method(com.xdev.jadoth.codegen.java.codeobjects.JavaTypeDescription, java.lang.String)
		 */
		public JavaMethodDefinition Method(final JavaTypeDescription type, final String name)
		{
			return null;
		}
		
	}
	
	public static class FactoryField
	{

		/**
		 * @param type
		 * @param name
		 * @return
		 * @see com.xdev.jadoth.codegen.java.codeobjects.JavaSourceCodeFactory.FieldFactory#Field(com.xdev.jadoth.codegen.java.codeobjects.JavaTypeDescription, java.lang.String)
		 */
		public JavaFieldDefinition Field(final JavaTypeDescription type, final String name)
		{
			return null;
		}
		
	}
	
	
	
	
	
}
