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

import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Collection;

import com.xdev.jadoth.codegen.java.Java;


/**
 * @author Thomas Muenz
 *
 */
public interface JavaImportStaticDefinition extends JavaCompilationUnitMember, JavaKeywordOwner
{
	public JavaTypeMemberDescription getStaticImportedMember();
	
	
	
	public class Implementation extends JavaCompilationObject.Implementation implements JavaImportStaticDefinition
	{
		private static final String import_static = Java.Lang.$import+' '+Java.Lang.$static;
		
		private static void validateMember(final Member member)
		{
			if(!(member instanceof JavaTypeMemberDescription 
				||   member instanceof Field || member instanceof Method)
			){
				throw new IllegalArgumentException("Cannot statically import "+member);					
			}
			
			if(!Modifier.isStatic(member.getModifiers())){
				throw new IllegalArgumentException("Cannot statically import non-static member: "+member);
			}
		}
		
		
		public static void addMembers(final Collection<JavaImportStaticDefinition> staticImports, final Member... staticMember)
		{
			//check-loop
			for(final Member member : staticMember) {
				JavaImportStaticDefinition.Implementation.validateMember(member);
			}
			
			//add-loop
			for(final Member member : staticMember) {
				staticImports.add(new JavaImportStaticDefinition.Implementation(member));
			}
		}
		
		
		///////////////////////////////////////////////////////////////////////////
		// constructors     //
		/////////////////////
		
		Implementation(final Member member)
		{
			super(null);
		}

		
		
		///////////////////////////////////////////////////////////////////////////
		// override methods //
		/////////////////////
		
		/**
		 * @return
		 * @see com.xdev.jadoth.codegen.java.codeobjects.JavaImportStaticDefinition#getStaticImportedMember()
		 */
		@Override
		public JavaTypeMemberDescription getStaticImportedMember()
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
		 * @see com.xdev.jadoth.codegen.java.codeobjects.JavaCompilationUnitMember#registerAtOwner(com.xdev.jadoth.codegen.java.codeobjects.JavaCompilationUnit)
		 */
		@Override
		public JavaCompilationUnitMember registerAtOwner(final JavaCompilationUnit compilationUnit)
		{
			return this;
		}

		/**
		 * @param owner
		 * @see com.xdev.jadoth.codegen.java.codeobjects.JavaCompilationUnitMember#setOwner(com.xdev.jadoth.codegen.java.codeobjects.JavaCompilationUnit)
		 */
		@Override
		public void setOwner(final JavaCompilationUnit owner)
		{
			
		}
		
		/**
		 * @param codeGenerator
		 * @see com.xdev.jadoth.codegen.java.codeobjects.JavaCodeAssembable#assemble(StringBuilder, com.xdev.jadoth.codegen.java.codeobjects.JavaCodeGenerator)
		 */
		@Override
		public void assemble(final StringBuilder sb, final JavaCodeGenerator codeGenerator)
		{
			codeGenerator.assembleJavaImportStaticDefinition(sb, this);			
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
		 * @see com.xdev.jadoth.codegen.java.codeobjects.JavaKeywordOwner#getKeyword()
		 */
		@Override
		public String getKeyword()
		{
			return Java.Lang.$import;
		}
		
	}
}
