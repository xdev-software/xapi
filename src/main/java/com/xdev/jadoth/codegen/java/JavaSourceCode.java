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
package com.xdev.jadoth.codegen.java;

import static com.xdev.jadoth.Jadoth.appendArraySeperated;
import static com.xdev.jadoth.codegen.Punctuation._;
import static com.xdev.jadoth.codegen.Punctuation.at;
import static com.xdev.jadoth.codegen.Punctuation.comma_;
import static com.xdev.jadoth.codegen.Punctuation.dot;
import static com.xdev.jadoth.codegen.Punctuation.par;
import static com.xdev.jadoth.codegen.Punctuation.rap;
import static com.xdev.jadoth.codegen.Punctuation.scol;
import static com.xdev.jadoth.codegen.Punctuation.star;
import static com.xdev.jadoth.codegen.java.Java.Lang.$super;
import static com.xdev.jadoth.codegen.java.Java.Lang.$this;
import static com.xdev.jadoth.codegen.java.Java.Lang.$throws;
import static com.xdev.jadoth.codegen.java.Java.Lang.import_;
import static com.xdev.jadoth.codegen.java.Java.Lang.package_;
import static com.xdev.jadoth.codegen.java.Java.Lang.static_;
import static com.xdev.jadoth.codegen.java.Java.Punctuation._is_;
import static com.xdev.jadoth.codegen.java.Java.Punctuation.scolN;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;

import javax.annotation.Generated;

import com.xdev.jadoth.codegen.AbstractSourceCode;
import com.xdev.jadoth.codegen.CodeGenException;




/**
 * The Class JavaSourceCode.
 */
public class JavaSourceCode extends AbstractSourceCode<JavaSourceCode>
{
	

	
	/**
	 * Checks if is unnecessary import class.
	 * 
	 * @param c the c
	 * @return true, if is unnecessary import class
	 */
	private static final boolean isUnnecessaryImportClass(final Class<?> c) {
		return c == null
			|| c.isPrimitive()
			|| c == String.class
			|| c == Boolean.class
			|| c == Byte.class
			|| c == Integer.class
			|| c == Long.class
			|| c == Float.class
			|| c == Double.class
			|| c == Character.class;
	}

	/**
	 * Return_.
	 * 
	 * @param codeElement the code element
	 * @return the string
	 */
	public static String return_(final Object codeElement) {
		return new StringBuilder(256).append(Java.Lang.return_).append(codeElement).append(scol).toString();
	}

	/**
	 * Adds the annotation.
	 * 
	 * @param sb the sb
	 * @param annotationClass the annotation class
	 * @param content the content
	 * @return the string builder
	 */
	public static final StringBuilder addAnnotation(StringBuilder sb, final Class<?> annotationClass, final String content) {
		if(!annotationClass.isAnnotation()) {
			throw new CodeGenException("No Annotation class: "+annotationClass);
		}
		if(sb == null) {
			sb = new StringBuilder(64);
		}
		sb.append(at).append(annotationClass.getSimpleName());
		if(content != null) {
			sb.append(par).append(content).append(rap);
		}
		return sb;
	}
	
	/**
	 * Annotation.
	 * 
	 * @param annotationClass the annotation class
	 * @param content the content
	 * @return the string
	 */
	public static final String annotation(final Class<?> annotationClass, final String content) {
		return addAnnotation(null, annotationClass, content).toString();
	}
	
	/**
	 * Annotation.
	 * 
	 * @param annotationClass the annotation class
	 * @return the string
	 */
	public static final String annotation(final Class<?> annotationClass) {
		return addAnnotation(null, annotationClass, null).toString();
	}

	/**
	 * Import class.
	 * 
	 * @param c the c
	 * @return the string
	 */
	public static final String importClass(final Class<?> c) {
		return importClass(new StringBuilder(1024), c).toString();
	}
	
	/**
	 * Import class.
	 * 
	 * @param sb the sb
	 * @param c the c
	 * @return the string builder
	 */
	public static final StringBuilder importClass(final StringBuilder sb, final Class<?> c) {
		if(isUnnecessaryImportClass(c)) return sb;
		return sb.append(import_).append(c.getName()).append(scolN);
	}
	
	/**
	 * Import class.
	 * 
	 * @param sb the sb
	 * @param className the class name
	 * @return the string builder
	 */
	public static final StringBuilder importClass(final StringBuilder sb, final String className) {
		if(className == null) return sb;
		return sb.append(import_).append(className).append(scolN);
	}
	
	/**
	 * Import static.
	 * 
	 * @param sb the sb
	 * @param importItem the import item
	 * @return the string builder
	 */
	public static final StringBuilder importStatic(final StringBuilder sb, final String importItem) {
		if(importItem == null) return sb;
		return sb.append(import_).append(static_).append(importItem).append(scolN);
	}

	///////////////////////////////////////////////////////////////////////////
	// instance fields  //
	/////////////////////
	/** The imports. */
	private final HashSet<String> imports = new HashSet<String>();
	
	/** The imports static. */
	private final HashSet<String> importsStatic = new HashSet<String>();
	
	/** The java syntax. */
	protected final JavaSyntax javaSyntax;


	///////////////////////////////////////////////////////////////////////////
	// constructors     //
	/////////////////////
	/**
	 * Instantiates a new java source code.
	 */
	public JavaSourceCode() {
		super(new JavaSyntax());
		this.javaSyntax = (JavaSyntax)this.syntax;
	}


	///////////////////////////////////////////////////////////////////////////
	// getters          //
	/////////////////////
	/**
	 * Gets the imports.
	 * 
	 * @return the imports
	 */
	public HashSet<String> getImports() {
		return this.imports;
	}
	
	/**
	 * Gets the syntax.
	 * 
	 * @return the syntax
	 */
	@Override
	public JavaSyntax getSyntax() {
		return (JavaSyntax)this.syntax;
	}




	/**
	 * Adds the generation annotation.
	 * 
	 * @param tabLevel the tab level
	 * @param generatorClasses the generator classes
	 * @param comments the comments
	 * @return the java source code
	 */
	public JavaSourceCode addGenerationAnnotation(final int tabLevel, final Class<?>[] generatorClasses, final String comments) {
		final StringBuilder sb = new StringBuilder(1024);
		sb.append('@').append(Generated.class.getSimpleName()).append('(')
		.append("date=\"").append(new Date(System.currentTimeMillis()).toString()).append("\", ")
		.append("value={").append('"').append(this.getClass().getName()).append('"');
		for (final Class<?> gc : generatorClasses) {
			sb.append(", ").append('"').append(gc.getName()).append('"');
		}
		sb.append("}");
		if(comments != null) {
			sb.append(", comments=").append('"').append(comments).append('"');
		}
		sb.append(')');
		this.addTabbedLine(tabLevel, sb.toString());
		return this;
	}


	/**
	 * Import_static_.
	 * 
	 * @param importItem the import item
	 * @return the java source code
	 */
	public JavaSourceCode import_static_(final String importItem) {
		if(importItem == null) return this;
		if(!this.importsStatic.contains(importItem)) {
			this.imports.add(importItem);
			importStatic(this.code, importItem);
		}
		return this;
	}
	
	/**
	 * Import_static_all of.
	 * 
	 * @param c the c
	 * @return the java source code
	 */
	public JavaSourceCode import_static_allOf(final Class<?> c) {
		return this.import_static_itemOf(c, star);
	}
	
	/**
	 * Import_static_item of.
	 * 
	 * @param c the c
	 * @param staticItem the static item
	 * @return the java source code
	 */
	public JavaSourceCode import_static_itemOf(final Class<?> c, final String staticItem) {
		if(c == null) return this;
		return this.import_static_(c.getCanonicalName()+dot+staticItem);
	}
	
	/**
	 * Import_.
	 * 
	 * @param className the class name
	 * @return the java source code
	 */
	public JavaSourceCode import_(final String className) {
		if(className == null) return this;
		if(!this.imports.contains(className)) {
			this.imports.add(className);
			importClass(this.code, className);
		}
		return this;
	}
	
	/**
	 * Import_.
	 * 
	 * @param c the c
	 * @return the java source code
	 */
	public JavaSourceCode import_(final Class<?> c) {
		if(isUnnecessaryImportClass(c)) return this;
		return this.import_(c.getCanonicalName());
	}

	/**
	 * Package_.
	 * 
	 * @param packageString the package string
	 * @return the java source code
	 * @throws CodeGenException the code gen exception
	 */
	public JavaSourceCode package_(final String packageString) throws CodeGenException {
		if(this.code.length() == 0) {
			this.code.append(package_).append(packageString).append(scolN);
		}
		else {
			throw new CodeGenException("package must be the first declaration");
		}
		return this;
	}

	/**
	 * Class_.
	 * 
	 * @param className the class name
	 * @param genericBounds the generic bounds
	 * @param superClass the super class
	 * @param superClassParams the super class params
	 * @param superClassEnclosingClassParams the super class enclosing class params
	 * @param interfaces the interfaces
	 * @return the java source code
	 */
	public JavaSourceCode class_(
		final String className,
		final String genericBounds,
		final Class<?> superClass,
		final String superClassParams,
		final String superClassEnclosingClassParams,
		final String... interfaces
	)
	{
		this.javaSyntax._class(this.code, className, genericBounds, superClass, superClassParams, superClassEnclosingClassParams, interfaces);
		this.space();
		this.blockNewLineStart();
		return this;
	}

	
	public JavaSourceCode generateNeededSerialVersionUID(final Class<?> potentialSerializableClass)
	{
		if(!Serializable.class.isAssignableFrom(potentialSerializableClass)) return this;		
		this.indent();
		this.code.append("private static final long serialVersionUID = ")
			.append(Java.generateSerialVersionUID(potentialSerializableClass))
			.append('L')
		;
		return this.endLine();		
//		return this.generateNeededSerialVersionUID.call(potentialSerializableClass);
	}
	


	/**
	 * Class_.
	 * 
	 * @param className the class name
	 * @param superClass the super class
	 * @param superClassParams the super class params
	 * @return the java source code
	 */
	public JavaSourceCode class_(final String className, final Class<?> superClass,	final String superClassParams)
	{
		return this.class_(className, null, superClass, superClassParams, null, (String[])null);
	}

	/**
	 * Class_.
	 * 
	 * @param className the class name
	 * @param superClass the super class
	 * @return the java source code
	 */
	public JavaSourceCode class_(final String className,final Class<?> superClass)
	{
		return this.class_(className, null, superClass, null, null, (String[])null);
	}


	/**
	 * Constructor_.
	 * 
	 * @param className the class name
	 * @param params the params
	 * @return the java source code
	 */
	public JavaSourceCode constructor_(final String className, final String... params){
		this.code.append(className).append(par);
		appendArraySeperated(this.code, comma_, (Object[])params);	
		this.code.append(rap);
		return this;
	}
	
	/**
	 * Super_.
	 * 
	 * @param params the params
	 * @return the java source code
	 */
	public JavaSourceCode super_(final String... params){
		this.code.append($super).append(par);
		appendArraySeperated(this.code, comma_, (Object[])params);
		this.code.append(rap);
		return this;
	}
	
	/**
	 * This_.
	 * 
	 * @param params the params
	 * @return the java source code
	 */
	public JavaSourceCode this_(final String... params){
		this.code.append($this).append(par);
		appendArraySeperated(this.code, comma_, (Object[])params);
		this.code.append(rap);
		return this;
	}
	
	/**
	 * Throws_.
	 * 
	 * @param exceptions the exceptions
	 * @return the java source code
	 */
	public JavaSourceCode throws_(final Class<? extends Exception>... exceptions){
		if(exceptions != null && exceptions.length > 0){
			this.code.append($throws).append(_);
			for(int i = 0; i < exceptions.length; i++) {
				this.import_(exceptions[i]);
				this.code.append(i>0?comma_:"").append(exceptions[i].getSimpleName());
			}
		}
		return this;
	}


	/**
	 * Var decl_init.
	 * 
	 * @param type the type
	 * @param name the name
	 * @param initValue the init value
	 * @return the java source code
	 */
	public JavaSourceCode varDecl_init(final Class<?> type, final String name, final String initValue){
		this.code.append(type.getSimpleName()).append(_).append(name);
		if(initValue != null){
			this.code.append(_is_).append(JavaSyntax.instantiate(type, initValue));
		}
		return this;
	}
	
	/**
	 * Var decl_assign.
	 * 
	 * @param type the type
	 * @param name the name
	 * @param initValue the init value
	 * @return the java source code
	 */
	public JavaSourceCode varDecl_assign(final Class<?> type, final String name, final String initValue){
		this.code.append(type.getSimpleName()).append(_).append(name).append(_is_).append(initValue);
		return this;
	}
	
	/**
	 * Var decl_init.
	 * 
	 * @param type the type
	 * @param name the name
	 * @param initValue the init value
	 * @return the java source code
	 */
	public JavaSourceCode varDecl_init(final Java.Array type, final String name, final String initValue){
		this.code.append(type.getDeclaration()).append(_).append(name);
		if(initValue != null){
			this.code.append(_is_);
			this.code.append(type.instantiate(initValue));
		}
		return this;
	}

}
