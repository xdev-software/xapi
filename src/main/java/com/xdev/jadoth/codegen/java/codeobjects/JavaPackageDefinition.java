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

import com.xdev.jadoth.codegen.java.Java;

/**
 * @author Thomas Muenz
 *
 */
public interface JavaPackageDefinition extends JavaCompilationUnitMember, JavaKeywordOwner
{
	public String getPackage();
	public JavaPackageDefinition setPackage(String packageString);
	
	
	public class Implementation extends JavaCompilationObject.Implementation implements JavaPackageDefinition
	{
		private String packageString;

		Implementation(final String packageString)
		{
			super(null);
			this.packageString = packageString;
		}

		public String getPackage()
		{
			return packageString;
		}

		public JavaPackageDefinition setPackage(final String packageString)
		{
			this.packageString = packageString;
			return this;
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
			compilationUnit.setPackageDefinition(this);
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
			codeGenerator.assembleJavaPackageDefinition(sb, this);			
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
			return Java.Lang.$package;
		}
		
		
	}
}
