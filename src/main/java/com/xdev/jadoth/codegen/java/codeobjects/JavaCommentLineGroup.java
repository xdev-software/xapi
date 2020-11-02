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

import java.util.Iterator;

import com.xdev.jadoth.Jadoth;


/**
 * @author Thomas Muenz
 *
 */
public interface JavaCommentLineGroup extends JavaComment, Iterable<JavaCommentLine>
{
	public class Implementation implements JavaCommentLineGroup
	{		
		///////////////////////////////////////////////////////////////////////////
		// instance fields  //
		/////////////////////
		
		private final String[] commentStrings;
		
		

		///////////////////////////////////////////////////////////////////////////
		// constructors     //
		/////////////////////
		
		public Implementation(final String... commentStrings)
		{
			super();
			this.commentStrings = commentStrings;
		}

		
		
		///////////////////////////////////////////////////////////////////////////
		// override methods //
		/////////////////////
		
		/**
		 * @return
		 * @see com.xdev.jadoth.codegen.java.codeobjects.JavaComment#getCommentString()
		 */
		@Override
		public String getCommentString()
		{
			return Jadoth.concat('\n', (Object[])this.commentStrings);
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
		 * @return
		 * @see com.xdev.jadoth.util.strings.Named#getName()
		 */
		@Override
		public String getName()
		{
			return null;
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
		 * @param javaClass
		 * @return
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
		 * @see com.xdev.jadoth.codegen.java.codeobjects.JavaModifierableCompilationObjectDescription#getModifiers()
		 */
		@Override
		public int getModifiers()
		{
			return 0;
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
		 * @see java.lang.Iterable#iterator()
		 */
		@Override
		public Iterator<JavaCommentLine> iterator()
		{
			return null;
		}
		
		/**
		 * @param name
		 * @return
		 * @see com.xdev.jadoth.util.strings.Nameable#setName(java.lang.String)
		 */
		@Override
		public Implementation setName(final String name)
		{
			return null;
		}
		
	}
}
