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

import java.lang.reflect.Member;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;

import com.xdev.jadoth.lang.functional.iterables.ChainedIterables;
import com.xdev.jadoth.lang.functional.iterables.DummyIterable;




/**
 * @author Thomas Muenz
 *
 */
public interface JavaCompilationUnit extends Iterable<JavaCompilationUnitMember>
{
	public JavaCompilationUnit add(JavaCompilationUnitMember member);	
	public JavaCompilationUnit add(JavaImportDefinition importDefinition);
	public JavaCompilationUnit add(JavaImportStaticDefinition staticImportDefinition);
	public JavaCompilationUnit add(JavaTypeDefinition type);	
	
	public JavaCompilationUnit add(JavaCompilationUnitMember... members);	
	public JavaCompilationUnit add(JavaImportDefinition... imports);
	public JavaCompilationUnit add(JavaImportStaticDefinition... imports);
	public JavaCompilationUnit add(JavaTypeDefinition... types);
	
	/**
	 * 
	 * @param types
	 * @return
	 * @throws ClassCastException if any element of <code>types</code> is neither a 
	 * {@link Class} nor a {@link JavaTypeDescription}
	 */
	public JavaCompilationUnit imporT(Type... types) throws ClassCastException;
	
	public JavaCompilationUnit import_static(Member... staticMember);
	
	public JavaCompilationUnit setPackage(String packageString);
	public JavaCompilationUnit setPackageDefinition(JavaPackageDefinition packageString);
	public String getPackage();
	
	
	public JavaPackageDefinition getPackageDefinition();
	
	public Iterable<JavaTypeDefinition> iterateTypes();
	
	public Iterable<JavaImportDefinition> iterateImports();
	
	public Iterable<JavaImportStaticDefinition> iterateStaticImports();
	
	public Iterable<JavaCompilationUnitMember> iterateAllMembers();
	
	public Iterable<JavaCompilationUnitMember> iterateBodyMembers();
	
	public String generateSourceCode(JavaCodeGenerator generator);
	
	
	public class Implementation implements JavaCompilationUnit
	{		
		private LinkedHashSet<JavaImportDefinition> imports = 
			new LinkedHashSet<JavaImportDefinition>();
		
		private LinkedHashSet<JavaImportStaticDefinition> staticImports = 
			new LinkedHashSet<JavaImportStaticDefinition>();
		
		private LinkedHashSet<JavaCompilationUnitMember> bodyMembers = 
			new LinkedHashSet<JavaCompilationUnitMember>();

		private DummyIterable<JavaPackageDefinition> packageDefinition = new DummyIterable<JavaPackageDefinition>();
		
		@SuppressWarnings("unchecked")
		private ChainedIterables<JavaCompilationUnitMember> members = new ChainedIterables<JavaCompilationUnitMember>(
			cast(packageDefinition), 
			cast(staticImports), 
			cast(imports), 
			cast(bodyMembers)
		);
		
		@SuppressWarnings("unchecked")
		private static <T extends JavaCompilationUnitMember> 
		Iterable<JavaCompilationUnitMember> cast(final Iterable<T> elements)
		{
			return (Iterable<JavaCompilationUnitMember>)elements;
		}
		
		
		/**
		 * @param members
		 * @return
		 * @see com.xdev.jadoth.codegen.java.codeobjects.JavaCompilationUnit#add(com.xdev.jadoth.codegen.java.codeobjects.JavaCompilationUnitMember[])
		 */
		@Override
		public Implementation add(final JavaCompilationUnitMember... members)
		{
			for(final JavaCompilationUnitMember javaCompilationUnitMember : members) {
//				this.add(javaCompilationUnitMember);
				javaCompilationUnitMember.registerAtOwner(this);
			}
			return this;
		}

		/**
		 * @return
		 * @see com.xdev.jadoth.codegen.java.codeobjects.JavaCompilationUnit#getPackage()
		 */
		@Override
		public String getPackage()
		{
			final JavaPackageDefinition p = this.packageDefinition.get();
			return p == null ?null :p.getPackage();
		}

		/**
		 * @param types
		 * @return
		 * @see com.xdev.jadoth.codegen.java.codeobjects.JavaCompilationUnit#imporT(java.lang.reflect.Type[])
		 */
		@Override
		public JavaCompilationUnit imporT(final Type... types)
		{
			for(final Type type : types) {
				this.add(new JavaImportDefinition.Implementation(JavaTypeDescription.Implementation.wrap(type)));				
			}
			return this;
		}
		

		/**
		 * @param staticMember
		 * @return
		 * @see com.xdev.jadoth.codegen.java.codeobjects.JavaCompilationUnit#import_static(com.xdev.jadoth.codegen.java.codeobjects.JavaTypeMemberDescription[])
		 */
		@Override
		public JavaCompilationUnit import_static(final Member... staticMembers)
		{
			JavaImportStaticDefinition.Implementation.addMembers(staticImports, staticMembers);
			return this;
		}

		/**
		 * @return
		 * @see com.xdev.jadoth.codegen.java.codeobjects.JavaCompilationUnit#iterateTypes()
		 */
		@Override
		public Iterable<JavaTypeDefinition> iterateTypes()
		{
			final ArrayList<JavaTypeDefinition> types = new ArrayList<JavaTypeDefinition>();
			for(final JavaCompilationUnitMember member : this.bodyMembers) {
				if(member instanceof JavaTypeDefinition){
					types.add((JavaTypeDefinition)member);
				}
			}
			return types;
		}

		/**
		 * @param packageString
		 * @return
		 * @see com.xdev.jadoth.codegen.java.codeobjects.JavaCompilationUnit#setPackage(java.lang.String)
		 */
		@Override
		public JavaCompilationUnit setPackage(final String packageString)
		{
			final JavaPackageDefinition p = new JavaPackageDefinition.Implementation(packageString);
			p.setOwner(this);
			this.packageDefinition.set(p);
			return this;
		}

		/**
		 * @param member
		 * @return
		 * @see com.xdev.jadoth.codegen.java.codeobjects.JavaCompilationUnit#add(com.xdev.jadoth.codegen.java.codeobjects.JavaCompilationUnitMember)
		 */
		@Override
		public JavaCompilationUnit add(final JavaCompilationUnitMember member)
		{
//			if(member instanceof JavaImportDefinition){
//				return this.add((JavaImportDefinition)member);
//			}	
//			if(member instanceof JavaImportStaticDefinition){
//				return this.add((JavaImportStaticDefinition)member);
//			}
//			member.setOwner(this);
//			this.otherMembers.add(member);
			member.registerAtOwner(this);
			return this;
		}

		/**
		 * @param importDefinition
		 * @return
		 * @see com.xdev.jadoth.codegen.java.codeobjects.JavaCompilationUnit#add(com.xdev.jadoth.codegen.java.codeobjects.JavaImportDefinition)
		 */
		@Override
		public JavaCompilationUnit add(final JavaImportDefinition importDefinition)
		{
			importDefinition.setOwner(this);
			this.imports.add(importDefinition);
			return this;
		}

		/**
		 * @param staticImportDefinition
		 * @return
		 * @see com.xdev.jadoth.codegen.java.codeobjects.JavaCompilationUnit#add(com.xdev.jadoth.codegen.java.codeobjects.JavaImportStaticDefinition)
		 */
		@Override
		public JavaCompilationUnit add(final JavaImportStaticDefinition staticImportDefinition)
		{
			staticImportDefinition.setOwner(this);
			this.staticImports.add(staticImportDefinition);
			return this;
		}

		/**
		 * @param imports
		 * @return
		 * @see com.xdev.jadoth.codegen.java.codeobjects.JavaCompilationUnit#add(com.xdev.jadoth.codegen.java.codeobjects.JavaImportDefinition[])
		 */
		@Override
		public JavaCompilationUnit add(final JavaImportDefinition... imports)
		{
			for(final JavaImportDefinition javaImportDefinition : imports) {
				this.add(javaImportDefinition);
			}
			return this;
		}

		/**
		 * @param imports
		 * @return
		 * @see com.xdev.jadoth.codegen.java.codeobjects.JavaCompilationUnit#add(com.xdev.jadoth.codegen.java.codeobjects.JavaImportStaticDefinition[])
		 */
		@Override
		public JavaCompilationUnit add(final JavaImportStaticDefinition... imports)
		{
			for(final JavaImportStaticDefinition javaImportDefinition : imports) {
				this.add(javaImportDefinition);
			}
			return this;
		}

		
		/**
		 * @return
		 * @see com.xdev.jadoth.codegen.java.codeobjects.JavaCompilationUnit#iterateAllMembers()
		 */
		@Override
		public Iterable<JavaCompilationUnitMember> iterateAllMembers()
		{
			return this.members;
		}


		/**
		 * @return
		 * @see com.xdev.jadoth.codegen.java.codeobjects.JavaCompilationUnit#getPackageDefinition()
		 */
		@Override
		public JavaPackageDefinition getPackageDefinition()
		{
			return this.packageDefinition.get();
		}


		/**
		 * @param packageString
		 * @return
		 * @see com.xdev.jadoth.codegen.java.codeobjects.JavaCompilationUnit#setPackageDefinition(com.xdev.jadoth.codegen.java.codeobjects.JavaPackageDefinition)
		 */
		@Override
		public JavaCompilationUnit setPackageDefinition(final JavaPackageDefinition packageDefinition)
		{
			packageDefinition.setOwner(this);
			this.packageDefinition.set(packageDefinition);
			return this;
		}


		/**
		 * @return
		 * @see java.lang.Iterable#iterator()
		 */
		@Override
		public Iterator<JavaCompilationUnitMember> iterator()
		{
			return this.members.iterator();
		}


		/**
		 * @param generator
		 * @return
		 * @see com.xdev.jadoth.codegen.java.codeobjects.JavaCompilationUnit#generateSourceCode(com.xdev.jadoth.codegen.java.codeobjects.JavaCodeGenerator)
		 */
		@Override
		public String generateSourceCode(final JavaCodeGenerator generator)
		{
			return generator.generateCode(this);
		}


		/**
		 * 
		 * @param type
		 * @return
		 * @see com.xdev.jadoth.codegen.java.codeobjects.JavaCompilationUnit#add(com.xdev.jadoth.codegen.java.codeobjects.JavaTypeDefinition)
		 */
		@Override
		public JavaCompilationUnit add(final JavaTypeDefinition type)
		{
			type.setOwner(this);
			this.bodyMembers.add(type);
			return this;
		}


		/**
		 * 
		 * @param types
		 * @return
		 * @see com.xdev.jadoth.codegen.java.codeobjects.JavaCompilationUnit#add(com.xdev.jadoth.codegen.java.codeobjects.JavaTypeDefinition[])
		 */
		@Override
		public JavaCompilationUnit add(final JavaTypeDefinition... types)
		{
			for(final JavaTypeDefinition javaTypeDefinition : types) {
				this.add(javaTypeDefinition);
			}
			return this;
		}


		/**
		 * @return
		 * @see com.xdev.jadoth.codegen.java.codeobjects.JavaCompilationUnit#iterateImports()
		 */
		@Override
		public Iterable<JavaImportDefinition> iterateImports()
		{
			return this.imports;
		}


		/**
		 * @return
		 * @see com.xdev.jadoth.codegen.java.codeobjects.JavaCompilationUnit#iterateStaticImports()
		 */
		@Override
		public Iterable<JavaImportStaticDefinition> iterateStaticImports()
		{
			return this.staticImports;
		}


		/**
		 * @return
		 * @see com.xdev.jadoth.codegen.java.codeobjects.JavaCompilationUnit#iterateBodyMembers()
		 */
		@Override
		public Iterable<JavaCompilationUnitMember> iterateBodyMembers()
		{
			return this.bodyMembers;
		}
		
		
	}
	
	

}
