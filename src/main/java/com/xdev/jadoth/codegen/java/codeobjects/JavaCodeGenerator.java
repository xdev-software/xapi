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

import com.xdev.jadoth.codegen.CodeGenException;

/**
 * @author Thomas Muenz
 *
 */
public interface JavaCodeGenerator
{
	public String generateCode(JavaCompilationUnit compilationUnit);
	
	public void assembleJavaCompilationUnitMember (StringBuilder sb, JavaCompilationUnitMember member);
	
	public void assembleJavaPackageDefinition     (StringBuilder sb, JavaPackageDefinition member);
	public void assembleJavaImportDefinition      (StringBuilder sb, JavaImportDefinition member);
	public void assembleJavaImportStaticDefinition(StringBuilder sb, JavaImportStaticDefinition member);
	
	public void assembleJavaComment         (StringBuilder sb, JavaComment comment);
	public void assembleJavaCommentLine     (StringBuilder sb, JavaCommentLine commentLine);
	public void assembleJavaCommentLineGroup(StringBuilder sb, JavaCommentLineGroup commentLineGroup);
	public void assembleJavaCommentBlock    (StringBuilder sb, JavaCommentBlock commentBlock);
	public void assembleJavaDocBlock        (StringBuilder sb, JavaDocBlock javaDocBlock);
	
	public void assembleJavaTypeDefinition     (StringBuilder sb, JavaTypeDefinition type);
	public void assembleJavaClassDefinition    (StringBuilder sb, JavaClassDefinition javaClass);
	public void assembleJavaInterfaceDefinition(StringBuilder sb, JavaInterfaceDefinition javaInterface);
		
	public void assembleJavaTypeMemberDefinition (StringBuilder sb, JavaTypeMemberDefinition member);
	public void assembleJavaClassMemberDefinition(StringBuilder sb, JavaClassMemberDefinition member);
	public void assembleJavaFieldDefinition      (StringBuilder sb, JavaFieldDefinition field);
	public void assembleJavaMethodDefinition     (StringBuilder sb, JavaMethodDefinition method);
	public void assembleJavaConstructorDefinition(StringBuilder sb, JavaConstructorDefinition constructor);
	public void assembleJavaInitializer          (StringBuilder sb, JavaInitializer initializer);
	public void assembleJavaStaticInitializer    (StringBuilder sb, JavaStaticInitializer staticInitializer);
	
	
	
	
	public class Implementation implements JavaCodeGenerator
	{
		private static final char _ = ' ';
		private static final char sc = ';';
		private static final char n = '\n';
		private static final char d = '.';
		private static final char sl = '/';
		private static final char star = '*';
		
		private static final char indentation = '\t';
		
		
		
		private static final StringBuilder indent(final StringBuilder sb, int indentationLevel)
		{
			while(indentationLevel --> 0){
				sb.append(indentation);
			}
			return sb;
		}
		
		private static final StringBuilder indentCommentBlock(final StringBuilder sb, int indentationLevel)
		{
			while(indentationLevel --> 0){
				sb.append(indentation);
			}			
			return sb.append(_).append(star).append(_);
		}
		
		
		/**
		 * @param compilationUnit
		 * @return
		 * @see com.xdev.jadoth.codegen.java.codeobjects.JavaCodeGenerator#generateCode(com.xdev.jadoth.codegen.java.codeobjects.JavaCompilationUnit)
		 */
		@Override
		public String generateCode(final JavaCompilationUnit compilationUnit)
		{
			final StringBuilder sb = new StringBuilder(100*1024);
			
			final JavaPackageDefinition javaPackageDefinition = compilationUnit.getPackageDefinition();
			if(javaPackageDefinition != null){
				this.assembleJavaPackageDefinition(sb, javaPackageDefinition);
				sb.append(n).append(n);
			}
			
			boolean hasImports = false;
			for(final JavaImportDefinition javaImportDefinition : compilationUnit.iterateImports()) {
				hasImports = true;
				this.assembleJavaImportDefinition(sb, javaImportDefinition);
			}
			if(hasImports){
				sb.append(n);
			}
			
			boolean hasStaticImports = false;
			for(final JavaImportStaticDefinition javaImportStaticDefinition : compilationUnit.iterateStaticImports()) {
				hasStaticImports = true;
				this.assembleJavaImportStaticDefinition(sb, javaImportStaticDefinition);
			}
			if(hasStaticImports){
				sb.append(n);
			}
			sb.append(n);
			for(final JavaCompilationUnitMember member : compilationUnit.iterateBodyMembers()) {
				this.assembleJavaCompilationUnitMember(sb, member);
			}
			return sb.toString();
		}
		
		/**
		 * 
		 * @param sb
		 * @param member
		 * @throws CodeGenException
		 */
		public void assembleJavaCompilationUnitMember(
			final StringBuilder sb, final JavaCompilationUnitMember member
		)
			throws CodeGenException
		{
			final JavaDocBlock memberJavaDoc = member.getJavaDoc();
			if(memberJavaDoc != null){
				this.assembleJavaDocBlock(sb, memberJavaDoc);
			}
			final JavaCommentBlock memberComment = member.getAssociatedComment();
			if(memberComment != null){
				this.assembleJavaCommentBlock(sb, memberComment);
			}
			member.assemble(sb, this);			
		}
		
		/**
		 * 
		 * @param sb
		 * @param member
		 * @see com.xdev.jadoth.codegen.java.codeobjects.JavaCodeGenerator#assembleJavaPackageDefinition(java.lang.StringBuilder, com.xdev.jadoth.codegen.java.codeobjects.JavaPackageDefinition)
		 */
		public void assembleJavaPackageDefinition(final StringBuilder sb, final JavaPackageDefinition member)
		{
			sb
			.append(member.getKeyword()).append(_)
			.append(member.getPackage())
			.append(sc).append(n)
			;
		}
		
		/**
		 * 
		 * @param sb
		 * @param member
		 * @see com.xdev.jadoth.codegen.java.codeobjects.JavaCodeGenerator#assembleJavaImportDefinition(java.lang.StringBuilder, com.xdev.jadoth.codegen.java.codeobjects.JavaImportDefinition)
		 */
		public void assembleJavaImportDefinition(final StringBuilder sb, final JavaImportDefinition member)
		{
			sb
			.append(member.getName()).append(_)
			.append(member.getImportedType().getCanonicalName())
			.append(sc).append(n)
			;
		}
		
		/**
		 * 
		 * @param sb
		 * @param type
		 * @see com.xdev.jadoth.codegen.java.codeobjects.JavaCodeGenerator#assembleJavaTypeDefinition(java.lang.StringBuilder, com.xdev.jadoth.codegen.java.codeobjects.JavaTypeDefinition)
		 */
		public void assembleJavaTypeDefinition(final StringBuilder sb, final JavaTypeDefinition type)
		{
			type.assemble(sb, this);	
		}
		
		/**
		 * 
		 * @param sb
		 * @param comment
		 * @see com.xdev.jadoth.codegen.java.codeobjects.JavaCodeGenerator#assembleJavaComment(java.lang.StringBuilder, com.xdev.jadoth.codegen.java.codeobjects.JavaComment)
		 */
		public void assembleJavaComment(final StringBuilder sb, final JavaComment comment)
		{
			comment.assemble(sb, this);	
		}

		/**
		 * @param sb
		 * @param javaClass
		 * @see com.xdev.jadoth.codegen.java.codeobjects.JavaCodeGenerator#assembleJavaClassDefinition(java.lang.StringBuilder, com.xdev.jadoth.codegen.java.codeobjects.JavaClassDefinition)
		 */
		@Override
		public void assembleJavaClassDefinition(final StringBuilder sb, final JavaClassDefinition javaClass)
		{
			sb
			.append(javaClass.getKeyword()).append(_).append(javaClass.getName());
			// (11.07.2010)FIXME: assembleJavaClassDefinition
		}

		/**
		 * @param sb
		 * @param javaInterface
		 * @see com.xdev.jadoth.codegen.java.codeobjects.JavaCodeGenerator#assembleJavaInterfaceDefinition(java.lang.StringBuilder, com.xdev.jadoth.codegen.java.codeobjects.JavaInterfaceDefinition)
		 */
		@Override
		public void assembleJavaInterfaceDefinition(final StringBuilder sb, final JavaInterfaceDefinition javaInterface)
		{
			
		}

		/**
		 * @param sb
		 * @param member
		 * @see com.xdev.jadoth.codegen.java.codeobjects.JavaCodeGenerator#assembleJavaImportStaticDefinition(java.lang.StringBuilder, com.xdev.jadoth.codegen.java.codeobjects.JavaImportStaticDefinition)
		 */
		@Override
		public void assembleJavaImportStaticDefinition(
			final StringBuilder sb, final JavaImportStaticDefinition staticImport
		)
		{
			final JavaTypeMemberDescription member = staticImport.getStaticImportedMember();
			sb
			.append(staticImport.getName()).append(_)
			.append(member.getOwnerType().getCanonicalName()).append(d).append(member.getName())
			.append(sc).append(n)
			;
			
		}

		/**
		 * @param sb
		 * @param field
		 * @see com.xdev.jadoth.codegen.java.codeobjects.JavaCodeGenerator#assembleJavaClassMemberDefinition(java.lang.StringBuilder, com.xdev.jadoth.codegen.java.codeobjects.JavaClassMemberDefinition)
		 */
		@Override
		public void assembleJavaClassMemberDefinition(final StringBuilder sb, final JavaClassMemberDefinition member)
		{
			member.assemble(sb, this);			
		}

		/**
		 * @param sb
		 * @param commentBlock
		 * @see com.xdev.jadoth.codegen.java.codeobjects.JavaCodeGenerator#assembleJavaCommentBlock(java.lang.StringBuilder, com.xdev.jadoth.codegen.java.codeobjects.JavaCommentBlock)
		 */
		@Override
		public void assembleJavaCommentBlock(final StringBuilder sb, final JavaCommentBlock commentBlock)
		{
			final int nestingLevel = commentBlock.getNestingLevel();
			
			indent(sb, nestingLevel).append(sl).append(star).append(n);
			indentCommentBlock(sb, nestingLevel);
			for(final char c : commentBlock.getCommentString().toCharArray()){
				sb.append(c);
				if(c == n){
					indentCommentBlock(sb, nestingLevel);
				}
			}
			indent(sb, nestingLevel).append(_).append(star).append(sl).append(n);			
		}

		/**
		 * @param sb
		 * @param commentLine
		 * @see com.xdev.jadoth.codegen.java.codeobjects.JavaCodeGenerator#assembleJavaCommentLine(java.lang.StringBuilder, com.xdev.jadoth.codegen.java.codeobjects.JavaCommentLine)
		 */
		@Override
		public void assembleJavaCommentLine(final StringBuilder sb, final JavaCommentLine commentLine)
		{
			sb.append(sl).append(sl).append(commentLine.getCommentString()).append(n);
		}

		/**
		 * @param sb
		 * @param commentLineGroup
		 * @see com.xdev.jadoth.codegen.java.codeobjects.JavaCodeGenerator#assembleJavaCommentLineGroup(java.lang.StringBuilder, com.xdev.jadoth.codegen.java.codeobjects.JavaCommentLineGroup)
		 */
		@Override
		public void assembleJavaCommentLineGroup(final StringBuilder sb, final JavaCommentLineGroup commentLineGroup)
		{
			for(final JavaCommentLine javaCommentLine : commentLineGroup) {
				this.assembleJavaCommentLine(sb, javaCommentLine);
			}			
		}

		/**
		 * @param sb
		 * @param constructor
		 * @see com.xdev.jadoth.codegen.java.codeobjects.JavaCodeGenerator#assembleJavaConstructorDefinition(java.lang.StringBuilder, com.xdev.jadoth.codegen.java.codeobjects.JavaConstructorDefinition)
		 */
		@Override
		public void assembleJavaConstructorDefinition(final StringBuilder sb, final JavaConstructorDefinition constructor)
		{
			
		}

		/**
		 * @param sb
		 * @param javaDocBlock
		 * @see com.xdev.jadoth.codegen.java.codeobjects.JavaCodeGenerator#assembleJavaDocBlock(java.lang.StringBuilder, com.xdev.jadoth.codegen.java.codeobjects.JavaDocBlock)
		 */
		@Override
		public void assembleJavaDocBlock(final StringBuilder sb, final JavaDocBlock javaDocBlock)
		{
			
		}

		/**
		 * @param sb
		 * @param field
		 * @see com.xdev.jadoth.codegen.java.codeobjects.JavaCodeGenerator#assembleJavaFieldDefinition(java.lang.StringBuilder, com.xdev.jadoth.codegen.java.codeobjects.JavaFieldDefinition)
		 */
		@Override
		public void assembleJavaFieldDefinition(final StringBuilder sb, final JavaFieldDefinition field)
		{
			
		}

		/**
		 * @param sb
		 * @param initializer
		 * @see com.xdev.jadoth.codegen.java.codeobjects.JavaCodeGenerator#assembleJavaInitializer(java.lang.StringBuilder, com.xdev.jadoth.codegen.java.codeobjects.JavaInitializer)
		 */
		@Override
		public void assembleJavaInitializer(final StringBuilder sb, final JavaInitializer initializer)
		{
			
		}

		/**
		 * @param sb
		 * @param method
		 * @see com.xdev.jadoth.codegen.java.codeobjects.JavaCodeGenerator#assembleJavaMethodDefinition(java.lang.StringBuilder, com.xdev.jadoth.codegen.java.codeobjects.JavaMethodDefinition)
		 */
		@Override
		public void assembleJavaMethodDefinition(final StringBuilder sb, final JavaMethodDefinition method)
		{
			
		}

		/**
		 * @param sb
		 * @param staticInitializer
		 * @see com.xdev.jadoth.codegen.java.codeobjects.JavaCodeGenerator#assembleJavaStaticInitializer(java.lang.StringBuilder, com.xdev.jadoth.codegen.java.codeobjects.JavaStaticInitializer)
		 */
		@Override
		public void assembleJavaStaticInitializer(final StringBuilder sb, final JavaStaticInitializer staticInitializer)
		{
			
		}

		/**
		 * @param sb
		 * @param member
		 * @see com.xdev.jadoth.codegen.java.codeobjects.JavaCodeGenerator#assembleJavaTypeMemberDefinition(java.lang.StringBuilder, com.xdev.jadoth.codegen.java.codeobjects.JavaTypeMemberDefinition)
		 */
		@Override
		public void assembleJavaTypeMemberDefinition(final StringBuilder sb, final JavaTypeMemberDefinition member)
		{
			member.assemble(sb, this);			
		}
		
		
	}
}
