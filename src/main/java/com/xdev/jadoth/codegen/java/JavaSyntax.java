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

import static com.xdev.jadoth.Jadoth.appendArray;
import static com.xdev.jadoth.Jadoth.appendArraySeperated;
import static com.xdev.jadoth.codegen.Punctuation._;
import static com.xdev.jadoth.codegen.Punctuation.apo;
import static com.xdev.jadoth.codegen.Punctuation.comma_;
import static com.xdev.jadoth.codegen.Punctuation.dot;
import static com.xdev.jadoth.codegen.Punctuation.par;
import static com.xdev.jadoth.codegen.Punctuation.rap;
import static com.xdev.jadoth.codegen.java.Java.Lang.$class;
import static com.xdev.jadoth.codegen.java.Java.Lang.$extends;
import static com.xdev.jadoth.codegen.java.Java.Lang.$implements;
import static com.xdev.jadoth.codegen.java.Java.Lang.$new;
import static com.xdev.jadoth.codegen.java.Java.Punctuation.genericsEnd;
import static com.xdev.jadoth.codegen.java.Java.Punctuation.genericsStart;

import java.util.HashMap;
import java.util.HashSet;

import com.xdev.jadoth.codegen.AbstractSyntax;
import com.xdev.jadoth.lang.reflection.JaReflect;



/**
 * The Class JavaSyntax.
 * 
 * @author Thomas Muenz
 */
public class JavaSyntax extends AbstractSyntax 
{	
	
	/** The Constant directBoxable. */
	static final HashSet<Class<?>> directBoxable = new HashSet<Class<?>>();
	
	/** The Constant boxable. */
	static final HashSet<Class<?>> boxable = new HashSet<Class<?>>();
	
	/** The Constant singleton. */
	private static final JavaSyntax singleton = new JavaSyntax();
	
	static {
		directBoxable.add(boolean.class);
		directBoxable.add(byte.class);
		directBoxable.add(short.class);
		directBoxable.add(int.class);
		directBoxable.add(long.class);
		directBoxable.add(float.class);
		directBoxable.add(double.class);
		
		directBoxable.add(Boolean.class);
		directBoxable.add(Byte.class);
		directBoxable.add(Short.class);
		directBoxable.add(Integer.class);
		directBoxable.add(Long.class);
		directBoxable.add(Float.class);
		directBoxable.add(Double.class);
		
		boxable.addAll(directBoxable);
		boxable.add(char.class);
		boxable.add(Character.class);
		boxable.add(String.class);		
	}
	
	
	/**
	 * Quote.
	 * 
	 * @param s the s
	 * @return the string
	 */
	public static String quote(String s){
		return singleton.$quote(s);
	}
	
	/**
	 * Instantiate.
	 * 
	 * @param type the type
	 * @param value the value
	 * @return the string
	 */
	public static String instantiate(Class<?> type, String value){
		if(value == null) return null;
		
		if(directBoxable.contains(type)){
			return value;
		}
		if(type == Character.class || type == Character.TYPE){			
			return new StringBuilder(3).append(apo).append(value).append(apo).toString();
		}
		else if(type == String.class){
			return singleton.$quote(value);
		}
		else {
			return new StringBuilder(64) 
			.append($new).append(_).append(type.getSimpleName()).append(par).append(value).append(rap)
			.toString();
		}		
	}
	

	
	///////////////////////////////////////////////////////////////////////////
	// constructors //
	/////////////////
	/**
	 * Instantiates a new java syntax.
	 */
	public JavaSyntax(){
		super();
	}
	
	

	///////////////////////////////////////////////////////////////////////////
	// declared methods //
	/////////////////////
	/**
	 * _class.
	 * 
	 * @param sb the sb
	 * @param className the class name
	 * @param genericBounds the generic bounds
	 * @param superClass the super class
	 * @param superClassParams the super class params
	 * @param superClassEnclosingClassParams the super class enclosing class params
	 * @param interfaces the interfaces
	 * @return the string builder
	 */
	public StringBuilder _class(
			StringBuilder sb, 
			final String className, 
			final String genericBounds, 
			final Class<?> superClass, 
			final String superClassParams,
			final String superClassEnclosingClassParams,
			final String... interfaces
	){
		if(sb == null){
			sb = new StringBuilder(128);
		}
		sb.append($class).append(_).append(className);
		if(genericBounds != null && genericBounds.length() > 0){
			sb.append(_).append(genericsStart).append(genericBounds).append(genericsEnd);
		}
		if(superClass != null){			
			HashMap<Class<?>, String> params = null;
			if(superClass.getEnclosingClass() != null){
				params = new HashMap<Class<?>, String>(1);
				params.put(superClass.getEnclosingClass(), superClassEnclosingClassParams);
			}
			
			sb.append(_).append($extends).append(_);
			sb.append(JaReflect.getFullClassName(superClass, params));
		}
		if(superClassParams != null && superClassParams.length() > 0){
			sb.append(genericsStart).append(superClassParams).append(genericsEnd);
		}
		if(interfaces != null && interfaces.length > 0){
			sb.append(_).append($implements).append(_);
			appendArray(sb, comma_, interfaces);
		}
		
		return sb;
	}
	
	/**
	 * $class.
	 * 
	 * @param className the class name
	 * @param genericBounds the generic bounds
	 * @param superClass the super class
	 * @param superClassParameterisation the super class parameterisation
	 * @param superClassEnclosingClassParams the super class enclosing class params
	 * @param implementedInterfaces the implemented interfaces
	 * @return the string
	 */
	public String $class(
		final String className, 
		final String genericBounds, 
		final Class<?> superClass, 
		final String superClassParameterisation,
		final String superClassEnclosingClassParams,
		final String... implementedInterfaces
	){
		return _class(
			null, 
			className, 
			genericBounds, 
			superClass, 
			superClassParameterisation, 
			superClassEnclosingClassParams, 
			implementedInterfaces
		).toString();
	}
	
	
	/**
	 * Call method.
	 * 
	 * @param methodName the method name
	 * @return the string
	 */
	public static String callMethod(final String methodName){
		return callMethod(null, methodName, (Object[])null);
	}
	
	/**
	 * Call method.
	 * 
	 * @param owner the owner
	 * @param methodName the method name
	 * @return the string
	 */
	public static String callMethod(final String owner, final String methodName){
		return callMethod(owner, methodName, (Object[])null);
	}		
	
	/**
	 * Call method.
	 * 
	 * @param owner the owner
	 * @param methodName the method name
	 * @param paramterStrings the paramter strings
	 * @return the string
	 */
	public static String callMethod(final String owner, final String methodName, final Object... paramterStrings){
		final StringBuilder sb = new StringBuilder(512);
		if(owner != null){
			sb.append(owner).append(dot);
		}
		sb.append(methodName).append(par);
		appendArraySeperated(sb, ", ", paramterStrings);
		sb.append(rap);
		return sb.toString();
	}
	
	
	
}
