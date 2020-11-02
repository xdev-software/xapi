
package com.xdev.jadoth.codegen.java;

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

import static com.xdev.jadoth.codegen.Punctuation._;
import static java.lang.reflect.Modifier.isFinal;
import static java.lang.reflect.Modifier.isPublic;
import static java.lang.reflect.Modifier.isStatic;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;

import com.xdev.jadoth.codegen.CodeGenException;
import com.xdev.jadoth.lang.reflection.JaReflect;



/**
 * The Class Java.
 * 
 * @author Thomas Muenz
 */
public abstract class Java
{
			
	/**
	 * The Class Punctuation.
	 */
	public static abstract class Punctuation implements com.xdev.jadoth.codegen.Punctuation
	{
		
		/** The Constant genericsStart. */
		public static final char genericsStart = '<';
		
		/** The Constant genericsEnd. */
		public static final char genericsEnd = '>';
		
		//comparators
		/** The Constant eq. */
		public static final String eq = is+is;
		
		/** The Constant ne. */
		public static final String ne = ne1;

		/** The Constant _is_. */
		public static final String _is_ = _+is+_;
		
		/** The Constant _eq_. */
		public static final String _eq_ = _+eq+_;		
		
		//Generic stuff shortcuts
		/** The Constant scolN. */
		public static final String scolN = scol+NEW_LINE;
	}

	

	

	/**
	 * The Class Lang.
	 */
	public static abstract class Lang
	{
		/** The Constant $if. */
		public static final String $if = "if";
		
		/** The Constant $else. */
		public static final String $else = "else";
		
		/** The Constant blockStart. */
		public static final String blockStart = "{";
		
		/** The Constant blockEnd. */
		public static final String blockEnd = "}";
		
		//Java keywords
		/** The Constant $package. */
		public static final String $package 	= "package";
		
		/** The Constant $import. */
		public static final String $import 		= "import";

		/** The Constant $return. */
		public static final String $return 		= "return";
		
		/** The Constant $void. */
		public static final String $void 		= "void";
		
		/** The Constant $null. */
		public static final String $null 		= "null";
		
		/** The Constant $true. */
		public static final String $true 		= "true";
		
		/** The Constant $false. */
		public static final String $false 		= "false";

		/** The Constant $public. */
		public static final String $public 		= "public";
		
		/** The Constant $protected. */
		public static final String $protected	= "protected";
		
		/** The Constant $private. */
		public static final String $private 	= "private";

		/** The Constant $static. */
		public static final String $static 		= "static";
		
		/** The Constant $final. */
		public static final String $final 		= "final";

		/** The Constant $transient. */
		public static final String $transient 	= "transient";
		
		/** The Constant $native. */
		public static final String $native 		= "native";
		
		/** The Constant $volatile. */
		public static final String $volatile 	= "volatile";
		
		/** The Constant $strictfp. */
		public static final String $strictfp 	= "strictfp";

		/** The Constant $class. */
		public static final String $class 		= "class";
		
		/** The Constant $interface. */
		public static final String $interface 	= "interface";
		
		/** The Constant $abstract. */
		public static final String $abstract 	= "abstract";
		
		/** The Constant $extends. */
		public static final String $extends 	= "extends";
		
		/** The Constant $implements. */
		public static final String $implements 	= "implements";
		
		/** The Constant $instanceof. */
		public static final String $instanceof 	= "instanceof";
		
		/** The Constant $enum. */
		public static final String $enum 		= "enum";

		/** The Constant $new. */
		public static final String $new 		= "new";
		
		/** The Constant $this. */
		public static final String $this		= "this";
		
		/** The Constant $super. */
		public static final String $super		= "super";

		/** The Constant $boolean. */
		public static final String $boolean 	= "boolean";
		
		/** The Constant $byte. */
		public static final String $byte 		= "byte";
		
		/** The Constant $short. */
		public static final String $short 		= "short";
		
		/** The Constant $int. */
		public static final String $int 		= "int";
		
		/** The Constant $long. */
		public static final String $long 		= "long";
		
		/** The Constant $float. */
		public static final String $float 		= "float";
		
		/** The Constant $double. */
		public static final String $double 		= "double";
		
		/** The Constant $char. */
		public static final String $char 		= "char";

		/** The Constant $try. */
		public static final String $try 		= "try";
		
		/** The Constant $catch. */
		public static final String $catch 		= "catch";
		
		/** The Constant $finally. */
		public static final String $finally 	= "finally";

		/** The Constant $throw. */
		public static final String $throw		= "throw";
		
		/** The Constant $throws. */
		public static final String $throws		= "throws";

//		public static final String $if 			= "if";
//		public static final String $else 		= "else";
		/** The Constant $for. */
		public static final String $for 		= "for";
		
		/** The Constant $while. */
		public static final String $while 		= "while";
		
		/** The Constant $do. */
		public static final String $do	 		= "do";
		
		/** The Constant $break. */
		public static final String $break 		= "break";
		
		/** The Constant $continue. */
		public static final String $continue 	= "continue";
		
		/** The Constant $switch. */
		public static final String $switch		= "switch";
		
		/** The Constant $case. */
		public static final String $case		= "case";
		
		/** The Constant $default. */
		public static final String $default		= "default";

		/** The Constant $synchronized. */
		public static final String $synchronized = "synchronized";

		/** The Constant $assert. */
		public static final String $assert 		= "assert";





		//Java keyword shortcuts
		/** The Constant import_. */
		public static final String import_ 		= $import+_;
		
		/** The Constant package_. */
		public static final String package_ 	= $package+_;
		
		/** The Constant return_. */
		public static final String return_ 		= $return+_;

		/** The Constant public_. */
		public static final String public_		= $public+_;
		
		/** The Constant protected_. */
		public static final String protected_	= $protected+_;
		
		/** The Constant private_. */
		public static final String private_		= $private+_;

		/** The Constant static_. */
		public static final String static_		= $static+_;
		
		/** The Constant final_. */
		public static final String final_		= $final+_;

		/** The Constant transient_. */
		public static final String transient_ 	= $transient+_;
		
		/** The Constant native_. */
		public static final String native_		= $native+_;
		
		/** The Constant volatile_. */
		public static final String volatile_ 	= $volatile+_;
		
		/** The Constant strictfp_. */
		public static final String strictfp_ 	= $strictfp+_;

		/** The Constant void_. */
		public static final String void_		= $void+_;

		/** The Constant class_. */
		public static final String class_		= $class+_;
		
		/** The Constant interface_. */
		public static final String interface_ 	= $interface+_;
		
		/** The Constant abstract_. */
		public static final String abstract_ 	= $abstract+_;
		
		/** The Constant extends_. */
		public static final String extends_		= $extends+_;
		
		/** The Constant implements_. */
		public static final String implements_ 	= $implements+_;
		
		/** The Constant instanceof_. */
		public static final String instanceof_ 	= $instanceof+_;
		
		/** The Constant enum_. */
		public static final String enum_		= $enum+_;

		/** The Constant new_. */
		public static final String new_			= $new+_;

		/** The Constant boolean_. */
		public static final String boolean_		= $boolean+_;
		
		/** The Constant byte_. */
		public static final String byte_		= $byte+_;
		
		/** The Constant short_. */
		public static final String short_		= $short+_;
		
		/** The Constant int_. */
		public static final String int_			= $int+_;
		
		/** The Constant long_. */
		public static final String long_		= $long+_;
		
		/** The Constant float_. */
		public static final String float_		= $float+_;
		
		/** The Constant double_. */
		public static final String double_		= $double+_;
		
		/** The Constant char_. */
		public static final String char_		= $char+_;

		/** The Constant throw_. */
		public static final String throw_		= $throw+_;
		
		/** The Constant throws_. */
		public static final String throws_		= $throws+_;
		
		
		/** The Constant keywords. */
		private static final String[] keywords = collectJavaKeywords();
		
		/**
		 * Collect java keywords.
		 * 
		 * @return the string[]
		 */
		private static final String[] collectJavaKeywords() 
		{
			final ArrayList<String> javaKeywords = new ArrayList<String>(30);
			final int excludedModifiers = ~(Modifier.PUBLIC | Modifier.STATIC | Modifier.FINAL);
			
			try {
				for(final Field f : JaReflect.getAllFields(Lang.class, excludedModifiers)) {
					final int modifiers = f.getModifiers();
					if(f.getName().charAt(0) == '$' && isStatic(modifiers) && isPublic(modifiers) && isFinal(modifiers)){
						javaKeywords.add(f.get(null).toString());
					}
				}
			}
			catch(final IllegalArgumentException  e) {
				throw new CodeGenException(e);
			}
			catch(final IllegalAccessException  e) {
				throw new CodeGenException(e);
			}
			
			return javaKeywords.toArray(new String[javaKeywords.size()]);			
		}
		
	}
	
	/**
	 * Checks if is java keyword.
	 * 
	 * @param string the string
	 * @return true, if is java keyword
	 */
	public static final boolean isJavaKeyword(final String string) 
	{
		if(string == null) return false;
		
		for(final String keyword : Java.Lang.keywords) {
			if(keyword.equals(string)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Generated the SerialVersionUID value for <code>serializableClass</code>.
	 * 
	 * @param serializableClass any class tha implements {@link Serializable}
	 * @return the SerialVersionUID generated by the JVM implementation
	 * @throws ClassCastException if <code>serializableClass</code> does not implement {@link Serializable}
	 */
	public static final long generateSerialVersionUID(final Class<?> serializableClass)
		throws ClassCastException
	{
		final java.io.ObjectStreamClass osc = java.io.ObjectStreamClass.lookup(serializableClass);
		if(osc == null){
			throw new ClassCastException("Not serializable: "+serializableClass);
		}
		return osc.getSerialVersionUID();
	}

	
	/**
	 * The Enum Visibility.
	 */
	public enum Visibility {
		
		/** The public_. */
		public_(Lang.$public), 
		
		/** The protected_. */
		protected_(Lang.$protected), 
		
		/** The private_. */
		private_(Lang.$private),
		
		/** The default_. */
		default_(null);		
		
		/** The modifier. */
		private String modifier;
		
		/**
		 * Instantiates a new visibility.
		 * 
		 * @param modifier the modifier
		 */
		private Visibility(final String modifier){
			this.modifier = modifier;
		}
		
		/**
		 * @return
		 * @see java.lang.Enum#toString()
		 */
		@Override
		public String toString() {
			return this.modifier;
		}		
		
	}
		

	
	
	


	///////////////////////////////////////////////////////////////////////////
	// constructors     //
	/////////////////////
	/**
	 * Instantiates a new java.
	 */
	protected Java() {
		super();
	}


	
	/**
	 * The Class Array.
	 */
	public static class Array{
		
		/** The comp type. */
		public final Class<?> compType;
		
		/** The dim. */
		public final int dim;
		
		/**
		 * Instantiates a new array.
		 * 
		 * @param compType the comp type
		 * @param dim the dim
		 */
		public Array(final Class<?> compType, final int dim) {
			super();
			this.compType = compType;
			this.dim = dim;
		}
		
		/**
		 * Gets the declaration.
		 * 
		 * @return the declaration
		 */
		public String getDeclaration(){
			final StringBuilder sb = new StringBuilder(128).append(this.compType.getSimpleName());
			int d = this.dim;
			while(d-->0){
				sb.append("[]");
			}
			return sb.toString();
		}
		
		/**
		 * Instantiate.
		 * 
		 * @param initializer the initializer
		 * @param dims the dims
		 * @return the string
		 */
		private String instantiate(final String initializer, final int... dims){
			final StringBuilder sb = new StringBuilder(128)
			.append(Lang.$new)
			.append(_)
			.append(this.compType.getSimpleName());
			if(dims == null){
				int d = this.dim;
				while(d-->0){
					sb.append("[]");
				}
				sb.append("{").append(initializer).append("}");
			}
			else {
				final int maxDimsIdx = dims.length-1;
				for(int i = 0; i < this.dim; i++) {
					sb.append("[");
					if(i <= maxDimsIdx){
						sb.append(dims[i]);
					}
					else {
						sb.append(0);
					}
					sb.append("]");
				}
			}			
			return sb.toString();
		}
		
		/**
		 * Instantiate.
		 * 
		 * @param dims the dims
		 * @return the string
		 */
		public String instantiate(final int... dims){
			return this.instantiate(null, dims);
		}
		
		/**
		 * Instantiate.
		 * 
		 * @param initializer the initializer
		 * @return the string
		 */
		public String instantiate(final String initializer){
			return this.instantiate(initializer, null);
		}
		
	}
	

}
