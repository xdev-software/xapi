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


/**
 * @author Thomas Muenz
 *
 */
public interface JavaInitializer extends JavaCodeHolder, JavaClassMemberDefinition
{
	
	
	
	public class Implementation extends JavaCompilationObject.Implementation implements JavaInitializer
	{

		Implementation()
		{
			super(null);
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
		 * @see com.xdev.jadoth.util.strings.Named#getName()
		 */
		@Override
		public String getName()
		{
			return null;
		}


		/**
		 * @param code
		 * @return
		 * @see com.xdev.jadoth.codegen.java.codeobjects.JavaCodeHolder#code(java.lang.CharSequence[])
		 */
		@Override
		public JavaInitializer.Implementation code(final CharSequence... code)
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
			codeGenerator.assembleJavaInitializer(sb, this);			
		}


		
	}
}
