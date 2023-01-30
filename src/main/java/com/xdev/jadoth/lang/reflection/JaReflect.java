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
package com.xdev.jadoth.lang.reflection;

import static com.xdev.jadoth.lang.types.JaTypes.isBoolean;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import com.xdev.jadoth.Jadoth;
import com.xdev.jadoth.lang.functional.Predicate;
import com.xdev.jadoth.lang.functional.controlflow.TOperation;
import com.xdev.jadoth.lang.functional.controlflow.TPredicate;
import com.xdev.jadoth.lang.reflection.annotations.Label;
import com.xdev.jadoth.lang.signalthrows.ThrowBreak;
import com.xdev.jadoth.lang.signalthrows.ThrowContinue;
import com.xdev.jadoth.lang.signalthrows.ThrowReturn;
import com.xdev.jadoth.lang.wrapperexceptions.ClassNotFoundRuntimeException;
import com.xdev.jadoth.lang.wrapperexceptions.IllegalAccessRuntimeException;
import com.xdev.jadoth.lang.wrapperexceptions.InstantiationRuntimeException;
import com.xdev.jadoth.lang.wrapperexceptions.InvalidClassRuntimeException;
import com.xdev.jadoth.lang.wrapperexceptions.InvocationTargetRuntimeException;
import com.xdev.jadoth.lang.wrapperexceptions.NoSuchFieldRuntimeException;
import com.xdev.jadoth.lang.wrapperexceptions.NoSuchMethodRuntimeException;

/**
 * Provides additional generic util methods for working with java reflection.
 *
 * @author Thomas Muenz
 *
 */
public abstract class JaReflect
{
	private JaReflect(){}
	
	
	/** The Constant CODE_CONVENTION_GETTER_PREFIX. */
	public static final String CODE_CONVENTION_GETTER_PREFIX = "get";
	
	/** The Constant CODE_CONVENTION_ISGETTER_PREFIX. */
	public static final String CODE_CONVENTION_ISGETTER_PREFIX = "is";
	
	/** The Constant CODE_CONVENTION_SETTER_PREFIX. */
	public static final String CODE_CONVENTION_SETTER_PREFIX = "set";
	

	/** The Constant primitiveClasses. */
	private static final HashMap<String, Class<?>> primitiveClasses = initPrimitiveClasses();


	  /////////////////////////////////////////////////////////////////////////
	 // Type Tools    //
	////////////////////


	// Interface Tools //

	
	/**
  	 * Checks if is interface of type.
  	 *
  	 * @param interfaceClass the interface class
  	 * @param implementedSuperInterface the implemented super interface
  	 * @return true, if is interface of type
  	 */
  	public static final boolean isInterfaceOfType(final Class<?> interfaceClass, final Class<?> implementedSuperInterface) {
		if(interfaceClass == implementedSuperInterface) return true;

		final Class<?>[] interfaces	= interfaceClass.getInterfaces();
		boolean isInterfaceType = false;
		for (final Class<?> i : interfaces) {
			isInterfaceType |= isInterfaceOfType(i, implementedSuperInterface);
		}		
		return isInterfaceType;
	}

	/**
	 * Implements interface.
	 *
	 * @param c the c
	 * @param interfaceClass the interface class
	 * @return true, if successful
	 */
	public static final boolean implementsInterface(final Class<?> c, final Class<?> interfaceClass) {
		if(c == null || interfaceClass == null || !interfaceClass.isInterface()) {
			return false;
		}

		final Class<?>[] interfaces = JaReflect.getAllInterfaces(c);
		for (final Class<?> i : interfaces) {
			if(isInterfaceOfType(i, interfaceClass)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Gets the all interfaces.
	 *
	 * @param c the c
	 * @return the all interfaces
	 */
	public static final Class<?>[] getAllInterfaces(final Class<?> c) {
		if(c == Object.class) {
			return new Class<?>[0];
		}
		Class<?> currentClass = c;
		final ArrayList<Class<?>[]> classes = new ArrayList<Class<?>[]>(10);
		int interfaceCount = 0;

		Class<?>[] currentClassInterfaces;
		while(currentClass != null && currentClass != Object.class) {
			currentClassInterfaces = currentClass.getInterfaces();
			interfaceCount += currentClassInterfaces.length;
			classes.add(currentClassInterfaces);
			currentClass = currentClass.getSuperclass();
		}

		final Class<?>[] allInterfaces = new Class<?>[interfaceCount];
		int allInterfaceIndex = 0;
		for (int i = classes.size()-1, stop = 0; i>=stop; i--) {
			currentClassInterfaces = classes.get(i);
			for(int j = 0, len = currentClassInterfaces.length; j < len; j++) {
				allInterfaces[allInterfaceIndex++] = currentClassInterfaces[j];
			}
		}
		return allInterfaces;
	}


	/**
	 * Checks if is of class type.
	 *
	 * @param c the c
	 * @param superclass the superclass
	 * @return true, if is of class type
	 */
	public static final boolean isOfClassType(final Class<?> c, final Class<?> superclass) {
		if(c.isInterface()||superclass.isInterface()) return false;
		return c==superclass?true:isSubClassOf(c, superclass);
	}

	/**
	 * Checks if is sub class of.
	 *
	 * @param c the c
	 * @param superclass the superclass
	 * @return true, if is sub class of
	 */
	public static final boolean isSubClassOf(final Class<?> c, final Class<?> superclass) {
		if(c == null || superclass == null) {
			return false;
		}
		Class<?> currentType = c;
		while(currentType != null) {
			currentType = currentType.getSuperclass();
			if(currentType == superclass) {
				return true;
			}
		}
		return false;
	}



	
	
	public static final ArrayList<Field> getAllFields(final Class<?> c)
	{
		return getAllFields(c, null);
	}
	
	
	/**
	 * <b><u>BranchingThrows</u></b>: <br>
	 * - <code>ThrowBreak</code> and <code>ThrowContinue</code> will affect the 
	 * outer class loop, not the inner field loop, because after adding (or not adding) a field, the inner loop
	 * does not contain any further logic.<br>
	 * - A <code>ThrowReturn</code> causes the immediate end of the method, returning all fields that have been
	 * found so far.<br>
	 * - Hint objects are ignored in any case.<br>
	 * <p>
	 * 
	 * @param c the class of which all fields shall be retrieved
	 * @param fieldFilter the filter for excluding fields. May be null to increase performance
	 * @return all apropriate fields as controlled by <code>fieldFilter</code>.
	 */
	@SuppressWarnings("null") //noExclude constant ensures existance of fieldFilter.
	public static final ArrayList<Field> getAllFields(final Class<?> c, final TPredicate<Field> fieldFilter)
	{
		//applies to Object.class, Void.class, interfaces, primitives. See Class.getSuperclass() JavaDoc.
		if(c.isArray() || c.getSuperclass() == null) {
			return new ArrayList<Field>(0);
		}
		
		final boolean noExclude = fieldFilter == null; //increase performance if no exclusion is possible
		final ArrayList<Field[]> classFields = new ArrayList<Field[]>(20);
		int elementCount = 0;		

		Class<?> currentClass = c;
		Field[] currentClassFields;
		while(currentClass != Object.class) {
			currentClassFields = currentClass.getDeclaredFields();
			elementCount += currentClassFields.length;
			classFields.add(currentClassFields);
			currentClass = currentClass.getSuperclass();
		}

		final ArrayList<Field> allFields = new ArrayList<Field>(elementCount);
		
		
		// case A: no exclusion
		if(noExclude) {
			for(int i = classFields.size(); i --> 0;) {
				for(final Field f : classFields.get(i)) {
					allFields.add(f);
				}
			}
			return allFields;
		}
		
		// case B: with exclusion
		Field loopField;
		for (int i = classFields.size(); i --> 0;) {
			currentClassFields = classFields.get(i);	

			final int len = currentClassFields.length;
			int j = 0;			
			while(j < len){
				try {
					while(j < len){
						if(fieldFilter.apply(loopField = currentClassFields[j++])){
							allFields.add(loopField);
						}
					}
					break;
				}
				catch(final ThrowBreak e)    { break; }
				catch(final ThrowContinue e) { /*Nothing*/ }					
				catch(final ThrowReturn e)   { return allFields; }				
			}
		}
		return allFields;
	}
	
	
	public static final void processAllClassFields(final Class<?> c, final TOperation<Field> fieldProcessor)
	{
		//applies to Object.class, Void.class, interfaces, primitives. See Class.getSuperclass() JavaDoc.
		if(c.isArray() || c.getSuperclass() == null) return;
		
		Class<?> currentClass = c;
		classLoop: do {			
			int i = 0;
			final Field[] fields = currentClass.getDeclaredFields();
			final int length = fields.length; 
			while(i < length){
				try {
					while(i < length){
						fieldProcessor.execute(fields[i++]);
					}		
					break;
				}
				catch(final ThrowBreak b)    { break; }
				catch(final ThrowContinue tc){ continue classLoop; }
				catch(final ThrowReturn r)   { return; }
			}			
			currentClass = currentClass.getSuperclass();
		} 
		while(currentClass != Object.class);
	}
	
	
	/**
	 * List all fields.
	 *
	 * @param c the c
	 * @param excludedModifiers the excluded modifiers
	 * @return the list
	 */
	public static final ArrayList<Field> getAllFields(final Class<?> c, final int excludedModifiers) 
	{
		if(c == Object.class || c.isInterface()) {
			return new ArrayList<Field>();
		}
		Class<?> currentClass = c;
		//10 parent classes should normally be sufficient
		final ArrayList<Field[]> classes = new ArrayList<Field[]>();
		int elementCount = 0;
		final boolean noExclude = excludedModifiers == 0;

		Field[] currentClassFields;
		while(currentClass != null && currentClass != Object.class) {
			currentClassFields = currentClass.getDeclaredFields();
			elementCount += currentClassFields.length;
			classes.add(currentClassFields);
			currentClass = currentClass.getSuperclass();
		}

		final ArrayList<Field> allFields = new ArrayList<Field>(elementCount);
		Field loopField;
		for (int i = classes.size()-1, stop = 0; i >= stop; i--) {
			currentClassFields = classes.get(i);
			for(int j = 0, len = currentClassFields.length; j < len; j++) {
				if(noExclude) {
					allFields.add(currentClassFields[j]);
				}
				else {
					loopField = currentClassFields[j];
					if((loopField.getModifiers() & excludedModifiers) == 0) {
						allFields.add(loopField);
					}
				}
			}
		}
		return allFields;
	}

	/**
	 * Adds the all fields.
	 *
	 * @param <C> the generic type
	 * @param c the c
	 * @param excludedModifiers the excluded modifiers
	 * @param collection the collection
	 * @return the c
	 */
	public static <C extends Collection<Field>> C addAllFields(
		final Class<?> c,
		final int excludedModifiers,
		final C collection
	){
		collection.addAll(getAllFields(c, excludedModifiers));
		return collection;
	}


	/**
	 * Gets the declared field.
	 *
	 * @param c the c
	 * @param name the name
	 * @return the declared field
	 * @throws NoSuchFieldRuntimeException the no such field runtime exception
	 */
	public static final Field getDeclaredField(final Class<?> c, final String name)
		throws NoSuchFieldRuntimeException
	{
		try {
			return c.getDeclaredField(name);
		} catch (final NoSuchFieldException e) {
			throw new NoSuchFieldRuntimeException(e);
		}
	}

	/**
	 * Gets the field.
	 *
	 * @param c the c
	 * @param name the name
	 * @return the field
	 * @throws NoSuchFieldRuntimeException the no such field runtime exception
	 */
	public static final Field getField(final Class<?> c, final String name)
		throws NoSuchFieldRuntimeException
	{
		try {
			return c.getField(name);
		} catch (final NoSuchFieldException e) {
			throw new NoSuchFieldRuntimeException(e);
		}
	}

	/**
	 * Gets the any field.
	 *
	 * @param c the c
	 * @param name the name
	 * @return the any field
	 * @throws NoSuchFieldRuntimeException the no such field runtime exception
	 */
	public static final Field getAnyField(final Class<?> c, final String name)
		throws NoSuchFieldRuntimeException
	{
		final List<Field> allFields = getAllFields(c, 0);
		for (final Field f : allFields) {
			if(f.getName().equals(name)) {
				return f;
			}
		}
		throw new NoSuchFieldRuntimeException(name);
	}


	public static final boolean isFinal(final Field field)
	{
		return Modifier.isFinal(field.getModifiers());
	}
	public static final boolean isStatic(final Field field)
	{
		return Modifier.isStatic(field.getModifiers());
	}
	public static final boolean isTransient(final Field field)
	{
		return Modifier.isTransient(field.getModifiers());
	}
	
	public static final boolean isPrivate(final Field field)
	{
		return Modifier.isPrivate(field.getModifiers());
	}
	public static final boolean isProtected(final Field field)
	{
		return Modifier.isProtected(field.getModifiers());
	}
	public static final boolean isPublic(final Field field)
	{
		return Modifier.isPublic(field.getModifiers());
	}
	public static final boolean isDefaultVisible(final Field field)
	{
		final int modifiers = field.getModifiers();
		return !(Modifier.isPrivate(modifiers) || Modifier.isProtected(modifiers) || Modifier.isPublic(modifiers));
	}


	//JDK trickery stuff
	private static final Field ArrayList_elementData;
	private static final Field HashMap_loadFactor;	
	private static final Field HashSet_map;
	private static final Field String_value;
	private static final Field String_offset;
	private static final Field AbstractStringBuilder_value;
	
	
	static{
		//will most likely never throw an exception, since those classes won't ever change these fields in the future.
		//except for non-sun implementations ^^
		ArrayList_elementData = getDeclaredFieldOrNull(ArrayList.class, "elementData");
		HashMap_loadFactor = getDeclaredFieldOrNull(HashMap.class, "loadFactor");
		HashSet_map = getDeclaredFieldOrNull(HashSet.class, "map");
		
		//handle string class completely. If anything goes wrong, disable reflective access (fields are null)
		//see access~() methods
		final Field offset = getDeclaredFieldOrNull(String.class, "offset");
		String_value = offset == null ?null :getDeclaredFieldOrNull(String.class, "value");
		String_offset = String_value == null? null :offset;
		
		AbstractStringBuilder_value = getDeclaredFieldOrNull(classForName("java.lang.AbstractStringBuilder"), "value");
	}
	
	private static Field getDeclaredFieldOrNull(final Class<?> c, final String name)
	{
		try {
			return getDeclaredField(c, name);
		}
		catch(final Exception e) {
			return null;
		}
	}	
	
	
	/**
	 * Accesses the elementData field containint the array holding the elements of <code>arrayList</code>.
	 * <p>
	 * <u><b>Warning</b></u>: Use this method wisely!<br>
	 * In almost all situations, it is not necessary to "peek" inside the <code>ArrayList</code> object and "steal" the 
	 * array from it. The use of this method in such situations is bad programming style and can cause any sort of
	 * trouble. E.g. logic manipulating the array while the actual <code>ArrayList</code> object is still active.<br>
	 * <br>
	 * Handle with care!<br>
	 * 
	 * 
	 * 
	 * @param <T>
	 * @param arrayList
	 * @return the elementData member array used by <code>arrayList</code>
	 */
	public static final Object[] accessArray(final ArrayList<?> arrayList)
	{
		//can never throw an exception if field elementData has been successfully retrieved
		return (Object[])getFieldValue(ArrayList_elementData, arrayList);
	}
	
	public static final char[] accessCharArray(final String s)
	{
		//can never throw an exception if field elementData has been successfully retrieved
		//I'm curious if this "if" is skipped by HotSpot optimisation 
		return String_value == null ? s.toCharArray() :(char[])getFieldValue(String_value, s);
	}
	
	public static final int accessOffset(final String s)
	{
		//hard to tell what to do with implementations without "offset" field. Best guess is String.class has no offset
		return String_offset == null ?0 :getField_int(String_offset, s);
	}
	
	
	
	public static final char[] accessCharArray(final StringBuilder sb)
	{
		if(AbstractStringBuilder_value == null){
			final char[] chars = new char[sb.length()];
			sb.getChars(0, sb.length(), chars, 0);
			return chars;
		}
		return (char[])getFieldValue(AbstractStringBuilder_value, sb);
	}
	
	// stupid AbstractStringBuilder class is not visible. Time and time again not understandable ...  
	public static final char[] accessCharArray(final StringBuffer sb)
	{
		if(AbstractStringBuilder_value == null){
			final char[] chars = new char[sb.length()];
			sb.getChars(0, sb.length(), chars, 0);
			return chars;
		}
		return (char[])getFieldValue(AbstractStringBuilder_value, sb);
	}
	
	
	/**
	 * Thie methods sets the loadFactor of an existing HashMap.<br>
	 * It's hard to understand why HashMap provides no setter for it so it could be changed in a normal way.
	 * 
	 * @param hashMap
	 * @param loadFactor see {@link HashMap} for allowed values. May not be null.
	 * @throws IllegalArgumentException if <code>loadFactor</code> is illegal for {@link HashMap}
	 */
	public static final void setLoadFactor(final HashMap<?,?> hashMap, final Float loadFactor)
		throws IllegalArgumentException
	{
		final float loadFactorValue = loadFactor.floatValue(); //provoke NullPointer
		if (loadFactorValue <= 0 || Float.isNaN(loadFactorValue)){
            throw new IllegalArgumentException("Illegal load factor: " + loadFactor);
		}		
		setFieldValue(HashMap_loadFactor, hashMap, loadFactor);
	}
	
	
	
	public static final void setLoadFactor(final HashSet<?> hashSet, final Float loadFactor)
		throws IllegalArgumentException
	{
		//works for LinkedHashSet as well
		setLoadFactor((HashMap<?,?>)getFieldValue(HashSet_map, hashSet), loadFactor);
	}
	
	
	
	

	// Method Tools //
	
	/* (08.09.2009 TM)NOTE:
	 * The Method block is genereted out of the Field block
	 * by replacing "Field" with "Method".
	 * Except the single getXXXMethod() Methods.
	 *
	 * For all other methods: Do not edit twice! Delete and replace again instead!
	 *
	 */

	/**
	 * Gets the all methods.
	 *
	 * @param c the c
	 * @return the all methods
	 */
	public static final Method[] getAllMethods(final Class<?> c) {
		return getAllMethods(c, 0);
	}

	/**
	 * Gets the all methods.
	 *
	 * @param c the c
	 * @param excludedModifiers the excluded modifiers
	 * @return the all methods
	 */
	public static final Method[] getAllMethods(final Class<?> c, final int excludedModifiers) {
		final List<Method> allMethods = listAllMethods(c, excludedModifiers);
		return allMethods.toArray(new Method[allMethods.size()]);
	}

	/**
	 * List all methods.
	 *
	 * @param c the c
	 * @param excludedModifiers the excluded modifiers
	 * @return the list
	 */
	public static final List<Method> listAllMethods(final Class<?> c, final int excludedModifiers) {
		if(c == Object.class || c.isInterface()) {
			return new ArrayList<Method>();
		}
		Class<?> currentClass = c;
		//10 parent classes should normally be sufficient
		final ArrayList<Method[]> classes = new ArrayList<Method[]>(10);
		int elementCount = 0;
		final boolean noExclude = excludedModifiers == 0;

		Method[] currentClassMethods;
		while(currentClass != null && currentClass != Object.class) {
			currentClassMethods = currentClass.getDeclaredMethods();
			elementCount += currentClassMethods.length;
			classes.add(currentClassMethods);
			currentClass = currentClass.getSuperclass();
		}

		final ArrayList<Method> allMethods = new ArrayList<Method>(elementCount);
		for (int i = classes.size()-1, stop = 0; i>=stop; i--) {
			currentClassMethods = classes.get(i);
			for(int j = 0, len = currentClassMethods.length; j < len; j++) {
				if(noExclude) {
					allMethods.add(currentClassMethods[j]);
				}
				else {
					if((currentClassMethods[j].getModifiers() & excludedModifiers) == 0) {
						allMethods.add(currentClassMethods[j]);
					}
				}
			}
		}
		return allMethods;
	}

	/**
	 * Adds the all methods.
	 *
	 * @param <C> the generic type
	 * @param c the c
	 * @param excludedModifiers the excluded modifiers
	 * @param collection the collection
	 * @return the c
	 */
	public static <C extends Collection<Method>> C addAllMethods(
		final Class<?> c,
		final int excludedModifiers,
		final C collection
	){
		collection.addAll(listAllMethods(c, excludedModifiers));
		return collection;
	}


	/**
	 * Gets the declared method.
	 *
	 * @param c the c
	 * @param name the name
	 * @param parameterTypes the parameter types
	 * @return the declared method
	 * @throws NoSuchMethodRuntimeException the no such method runtime exception
	 */
	public static final Method getDeclaredMethod(final Class<?> c, final String name, final Class<?>... parameterTypes)
		throws NoSuchMethodRuntimeException
	{
		try {
			return c.getDeclaredMethod(name, parameterTypes);
		} catch (final NoSuchMethodException e) {
			throw new NoSuchMethodRuntimeException(e);
		}
	}
	
	
	/**
	 * Gets the first declared method found with this name, independent from its parameters.
	 *
	 * @param c the c
	 * @param name the name
	 * @return the declared method
	 * @throws NoSuchMethodRuntimeException the no such method runtime exception
	 */
	public static final Method getDeclaredMethodFirstNamed(final Class<?> c, final String name)
		throws NoSuchMethodRuntimeException
	{
		final Method foundMethod = Jadoth.search(c.getDeclaredMethods(), 
			new Predicate<Method>(){ @Override public boolean apply(final Method t){
				return t.getName().equals(name);
			}
		});
		
		if(foundMethod == null){
			throw new NoSuchMethodRuntimeException();
		}

		return foundMethod;
	}
	

	/**
	 * Gets the method.
	 *
	 * @param c the c
	 * @param name the name
	 * @param parameterTypes the parameter types
	 * @return the method
	 * @throws NoSuchMethodRuntimeException the no such method runtime exception
	 */
	public static final Method getMethod(final Class<?> c, final String name, final Class<?>... parameterTypes)
		throws NoSuchMethodRuntimeException
	{
		try {
			return c.getMethod(name, parameterTypes);
		} catch (final NoSuchMethodException e) {
			throw new NoSuchMethodRuntimeException(e);
		}
	}

	/**
	 * Gets the any method.
	 *
	 * @param c the c
	 * @param name the name
	 * @param parameterTypes the parameter types
	 * @return the any method
	 * @throws NoSuchMethodRuntimeException the no such method runtime exception
	 */
	public static final Method getAnyMethod(final Class<?> c, final String name, Class<?>... parameterTypes)
		throws NoSuchMethodRuntimeException
	{
		if(parameterTypes == null){
			parameterTypes = new Class<?>[0];
		}
		final List<Method> allMethods = listAllMethods(c, 0);
		for (final Method f : allMethods) {
			if(f.getName().equals(name) && f.getParameterTypes().equals(parameterTypes)) {
				return f;
			}
		}
		throw new NoSuchMethodRuntimeException(name);
	}




	/**
	 * Validate label value.
	 *
	 * @param label the label
	 * @param value the value
	 * @return true if ;
	 */
	public static final boolean validateLabelValue(final Label label, final String value){
		if(label == null) return false;
		if(value == null) return true;

		final String[] values = label.value();
		for(final String v : values) {
			if(v.equals(value)){
				return true;
			}
		}
		return false;
	}


	/**
	 * Gets the member collection by label.
	 *
	 * @param <E> the element type
	 * @param <C> the generic type
	 * @param label the label
	 * @param elements the elements
	 * @param collection the collection
	 * @return the member collection by label
	 */
	public static final <E extends AnnotatedElement, C extends Collection<E>> C getMemberCollectionByLabel(
		final String label,
		final E[] elements,
		final C collection
	)
	{
		if(collection == null || elements == null) return null;
		for (final E f : elements) {
			if(f.isAnnotationPresent(Label.class)) {
				for (final String s : f.getAnnotation(Label.class).value()) {
					if(s.equals(label)) {
						collection.add(f);
					}
				}
			}
		}
		return collection;
	}

	/**
	 * Gets the members by label.
	 *
	 * @param <E> the element type
	 * @param label the label
	 * @param elements the elements
	 * @return the members by label
	 */
	@SuppressWarnings("unchecked")
	public static final <E extends AnnotatedElement> E[] getMembersByLabel(final String label, final E[] elements)
	{
		if(elements == null) return null;
		final ArrayList<E> labeledElements = getMemberCollectionByLabel(label, elements, new ArrayList<E>());
		return labeledElements.toArray(
			(E[])Array.newInstance(elements.getClass().getComponentType(), labeledElements.size())
		);
	}

	/**
	 * Gets the member by label.
	 *
	 * @param <E> the element type
	 * @param label the label
	 * @param elements the elements
	 * @return the member by label
	 */
	public static final <E extends AnnotatedElement> E getMemberByLabel(final String label, final E[] elements)
	{
		if(elements == null) return null;
		for (final E f : elements) {
			if(f.isAnnotationPresent(Label.class)) {
				for (final String s : f.getAnnotation(Label.class).value()) {
					if(s.equals(label)) {
						return f;
					}
				}
			}
		}
		return null;
	}

	/**
	 * Gets the member collection with annotation.
	 *
	 * @param <E> the element type
	 * @param <C> the generic type
	 * @param annotation the annotation
	 * @param elements the elements
	 * @param collection the collection
	 * @return the member collection with annotation
	 */
	public static final <E extends AnnotatedElement, C extends Collection<E>> C getMemberCollectionWithAnnotation(
		final Class<? extends Annotation> annotation,
		final E[] elements,
		final C collection
	)
	{
		if(collection == null || elements == null) return null;
		for(final E f : elements) {
			if(f.isAnnotationPresent(annotation)) {
				collection.add(f);
			}
		}
		return collection;
	}

	/**
	 * Gets the members with annotation.
	 *
	 * @param <E> the element type
	 * @param annotation the annotation
	 * @param elements the elements
	 * @return the members with annotation
	 */
	@SuppressWarnings("unchecked")
	public static final <E extends AnnotatedElement> E[] getMembersWithAnnotation(
		final Class<? extends Annotation> annotation, final E[] elements
	)
	{
		if(elements == null) return null;
		final ArrayList<E> labeledElements = getMemberCollectionWithAnnotation(annotation, elements, new ArrayList<E>());
		return labeledElements.toArray((E[])Array.newInstance(elements.getClass().getComponentType(), labeledElements.size()));
	}

	/**
	 * Gets the member with annotation.
	 *
	 * @param <E> the element type
	 * @param annotation the annotation
	 * @param elements the elements
	 * @return the member with annotation
	 */
	public static final <E extends AnnotatedElement> E getMemberWithAnnotation(
		final Class<? extends Annotation> annotation, final E[] elements
	)
	{
		if(elements == null) return null;
		for(final E f : elements) {
			if(f.isAnnotationPresent(annotation)) {
				return f;
			}
		}
		return null;
	}



	/**
	 * Gets the field value.
	 *
	 * @param f the f
	 * @param obj the obj
	 * @return the field value
	 * @throws IllegalAccessRuntimeException the illegal access runtime exception
	 */
	public static Object getFieldValue(final Field f, final Object obj)
		throws IllegalAccessRuntimeException
	{
		synchronized(f) {
			if(f.isAccessible()) {
				try {
					return f.get(obj);
				} 
				catch (final IllegalAccessException e) {
					throw new IllegalAccessRuntimeException(e);
				}
			}
			f.setAccessible(true);
			try {
				return f.get(obj);
			} 
			catch (final IllegalAccessException e) {
				//Should never happen: accessible Object not accessible in synchronized block
				throw new IllegalAccessRuntimeException(e);
			}
			finally {
				f.setAccessible(false);
			}
		}
	}
	
	public static int getField_int(final Field f, final Object obj)
	throws IllegalAccessRuntimeException
{
	synchronized(f) {
		if(f.isAccessible()) {
			try {
				return f.getInt(obj);
			} 
			catch (final IllegalAccessException e) {
				throw new IllegalAccessRuntimeException(e);
			}
		}
		f.setAccessible(true);
		try {
			return f.getInt(obj);
		} 
		catch (final IllegalAccessException e) {
			//Should never happen: accessible Object not accessible in synchronized block
			throw new IllegalAccessRuntimeException(e);
		}
		finally {
			f.setAccessible(false);
		}
	}
}
	
	


	/**
	 * Sets the field value.
	 *
	 * @param f the f
	 * @param obj the obj
	 * @param value the value
	 * @throws IllegalAccessRuntimeException the illegal access runtime exception
	 */
	public static void setFieldValue(final Field f, final Object obj, final Object value)
		throws IllegalAccessRuntimeException
	{
		synchronized(f) {
			if(f.isAccessible()) {
				try {
					f.set(obj, value);
				} catch (final IllegalAccessException e) {
					throw new IllegalAccessRuntimeException(e);
				}
			}
			else {
				f.setAccessible(true);
				try {
					f.set(obj, value);
				} catch (final IllegalAccessException e) {
					throw new IllegalAccessRuntimeException(e);
				} finally {
					f.setAccessible(false);
				}
			}
		}
	}



	  /////////////////////////////////////////////////////////////////////////
	 // Array Tools    //
	////////////////////

	/**
  	 * Unbox.
  	 *
  	 * @param intArray the int array
  	 * @param nullReplacement the null replacement
  	 * @return the int[]
  	 */
  	public static final int[] unbox(final Integer[] intArray, final int nullReplacement) {
		if(intArray == null) return null;

		final int[] returnArray = new int[intArray.length];
		for (int i = 0; i < returnArray.length; i++) {
			final Integer objI = intArray[i];
			returnArray[i] = objI==null?nullReplacement:objI.intValue();
		}
		return returnArray;
	}

	/**
	 * Unbox.
	 *
	 * @param intArray the int array
	 * @return the int[]
	 */
	public static final int[] unbox(final Integer[] intArray) {
		return unbox(intArray, 0);
	}

	
	@SuppressWarnings("unchecked")
	public static final <T> T[] convertArray(final Object[] objects, final Class<T> type) throws ClassCastException
	{		
		final T[] converted = (T[])Array.newInstance(type, objects.length);		
		for(int i = 0; i < objects.length; i++) {
			converted[i] = (T)objects[i];
		}
		return converted;
	}


	  /////////////////////////////////////////////////////////////////////////
	 // Class Tools    //
	////////////////////


	public static final Class<?>[] getTypes(final Object... objects)
	{
		final Class<?>[] paramTypes = new Class<?>[objects.length];
		for(int i = 0; i < objects.length; i++) {
			paramTypes[i] = objects[i].getClass();
		}
		return paramTypes;
	}
	
	/**
  	 * Load class from file.
  	 *
  	 * @param directory the directory
  	 * @param fullQualifiedClassName the full qualified class name
  	 * @return the class
  	 * @throws ClassNotFoundException the class not found exception
  	 */
  	public static final Class<?> loadClassFromFile(final File directory, final String fullQualifiedClassName) throws ClassNotFoundException{
	    try {
	    	// Convert file to a URL
	        final URL[] urls = new URL[]{directory.toURI().toURL()};

	        // Create a new class loader with the directory
	        try(final URLClassLoader cl = new URLClassLoader(urls)) {
	        	return cl.loadClass(fullQualifiedClassName);
			} catch(final IOException e) {
				throw new RuntimeException(e);
			}
	    } catch (final MalformedURLException e) {
	    	//should never happen has URL is generated from File Object
	    	throw new RuntimeException(e);
	    }
	}

	/**
	 * Call default constructor.
	 *
	 * @param <O> the generic type
	 * @param c the c
	 * @param enclosingInstance the enclosing instance
	 * @return the o
	 * @return
	 */
	public static final <O> O callDefaultConstructor(final Class<O> c, final Object enclosingInstance)
	{
		if(isInnerNestedClass(c) || c.isAnonymousClass()) {
			final Class<?> enclosingClass = c.getEnclosingClass();
			if(enclosingInstance != null && enclosingInstance.getClass() != enclosingClass) {
				throw new InvalidClassRuntimeException(enclosingInstance.getClass().getName());
			}
			return callConstructor(c, new Class<?>[]{enclosingClass}, new Object[]{enclosingInstance});
		}
		return callConstructor(c, null, (Object[])null);
	}

	/**
	 * Call default constructor.
	 *
	 * @param <O> the generic type
	 * @param c the c
	 * @return the o
	 */
	public static final <O> O callDefaultConstructor(final Class<O> c){
		return callConstructor(c, null, (Object[])null);
	}
	
	public static final <O> O callConstructor(final Class<O> c, final Object... paramValues)
	{
		if(paramValues == null || paramValues.length == 0){
			return callConstructor(c, null, (Object[])null);
		}		
		return callConstructor(c, getTypes(paramValues), paramValues);		
	}
	
	/**
	 * Call constructor.
	 *
	 * @param <O> the generic type
	 * @param c the c
	 * @param paramTypes the param types
	 * @param paramValues the param values
	 * @return the o
	 */
	public static final <O> O callConstructor(final Class<O> c, final Class<?>[] paramTypes, Object... paramValues)
	{
		Constructor<O> cr;
		try {
			if(paramTypes == null) {
				cr = c.getDeclaredConstructor();
			}
			else {
				cr = c.getDeclaredConstructor(paramTypes);
				if(paramValues == null) {
					//expand null paramValues to array of nulls
					paramValues = new Object[paramTypes.length];
				}
			}
		} 
		catch (final NoSuchMethodException e) {
			throw new NoSuchMethodRuntimeException(e);
		}

		synchronized(cr) {
			if(cr.isAccessible()) {
				try {
					return cr.newInstance(paramValues);
				} 
				catch(final IllegalAccessException e) {
					throw new IllegalAccessRuntimeException(e);
				} 
				catch(final InvocationTargetException e) {
					throw new InvocationTargetRuntimeException(e);
				} 
				catch(final InstantiationException e) {
					throw new InstantiationRuntimeException(e);
				}
			}
			cr.setAccessible(true);
			try {
				return cr.newInstance(paramValues);
			}
			catch(final IllegalAccessException e) {
				throw new IllegalAccessRuntimeException(e);
			}
			catch(final InvocationTargetException e) {
				throw new InvocationTargetRuntimeException(e);
			}
			catch(final InstantiationException e) {
				throw new InstantiationRuntimeException(e);
			}
			finally {
				cr.setAccessible(false);
			}
		}
	}


	/**
	 * Returns the simple name of the class preceeded by all of its enclosing classes, connected with a "$".
	 *
	 * @param c the c
	 * @return the full enclosing class name
	 * @return
	 */
	public static final String getFullEnclosingClassName(final Class<?> c)
	{
		final String name = c.getName();
		final int lastDotIndex = name.lastIndexOf('.');
		if(lastDotIndex == -1) {
			return name;
		}
		return name.substring(lastDotIndex+1);
	}


	/**
	 * Checks if is static nested class.
	 *
	 * @param c the c
	 * @return true, if is static nested class
	 */
	public static final boolean isStaticNestedClass(final Class<?> c) {
		return c.isMemberClass() && !isInnerNestedClass(c);
	}

	/**
	 * Checks if is inner nested class.
	 *
	 * @param c the c
	 * @return true, if is inner nested class
	 */
	public static final boolean isInnerNestedClass(final Class<?> c)
	{
		if(!c.isMemberClass()) {
			return false;
		}
		final Class<?> enclosingClass = c.getEnclosingClass();

		final Field[] declFields = c.getDeclaredFields();

		// at least one field must be a reference to the enclosing class
		boolean enclClassRefPresent = false;
		for (final Field f : declFields) {
			if(f.getType() == enclosingClass) {
				enclClassRefPresent = true;
			}
		}
		if(!enclClassRefPresent) {
			return false;
		}

		// every constructor must have the type of the enclosing class as a first parameter
		final Constructor<?>[] cons = c.getConstructors();
		for (final Constructor<?> con : cons) {
			final Class<?>[] pTypes = con.getParameterTypes();
			if(pTypes.length == 0 || pTypes[0] != enclosingClass) {
				return false;
			}
		}
		//all tests passed: class is most likely a nested inner class (directly or self-tinkered static wise)
		return true;
	}




	/**
	 * Gets the full class name.
	 *
	 * @param c the c
	 * @return the full class name
	 */
	public static final String getFullClassName(final Class<?> c)
	{
		return getFullClassName(c, null);
	}

	/**
	 * Returns the full class name without packages of class <code>c</code>.
	 * <p>
	 * Examples:<br>
	 * full class name of String.class: "String"
	 * full class name of HashMap.Entry.class: "HashMap.Entry"
	 * <p>
	 * This is useful if the enclosing class has already been imported,
	 * so that the inner class does not have to be referenced full qualified.
	 * <p>
	 * If <code>enclosingClassParameters</code> is not null, then generic bounds parameter
	 * will be applied to enclosing classes. If <code>enclosingClassParameters</code> does not
	 * contain a bounds parameter string for a parametrized class, "?" will be used as a bounds parameter.
	 * <p>
	 * If <code>enclosingClassParameters</code> is null, no generics bounds parameter will be applied
	 *
	 * @param c the c
	 * @param enclosingClassParameters the enclosing class parameters
	 * @return the full class name
	 * @return
	 */
	public static final String getFullClassName(final Class<?> c, final Map<Class<?>, String> enclosingClassParameters)
	{
		Class<?> currentClass = c;
		final List<Class<?>> enclosingClasses = new ArrayList<Class<?>>();
		final StringBuilder sb = new StringBuilder(256);

		while((currentClass = currentClass.getEnclosingClass()) != null){
			enclosingClasses.add(currentClass);
		}

		int i = enclosingClasses.size();
		while(i-->0){
			currentClass = enclosingClasses.get(i);
			sb.append(currentClass.getSimpleName());
			if(enclosingClassParameters != null && currentClass.getTypeParameters().length > 0){
				final String param =  enclosingClassParameters.get(currentClass);
				sb.append('<').append(param==null?'?':param).append('>');
			}
			sb.append('.');
		}
		sb.append(c.getSimpleName());
		return sb.toString();
	}



	/**
	 * Inits the primitive classes.
	 *
	 * @return the hash map
	 */
	private static final HashMap<String, Class<?>> initPrimitiveClasses(){
		final HashMap<String, Class<?>> map = new HashMap<String, Class<?>>();
		map.put(boolean.class.getCanonicalName(), boolean.class);
		map.put(byte.class.getCanonicalName(), byte.class);
		map.put(short.class.getCanonicalName(), short.class);
		map.put(int.class.getCanonicalName(), int.class);
		map.put(long.class.getCanonicalName(), long.class);
		map.put(float.class.getCanonicalName(), float.class);
		map.put(double.class.getCanonicalName(), double.class);
		map.put(char.class.getCanonicalName(), char.class);
		return map;
	}

	/**
	 * Class for name.
	 *
	 * @param className the class name
	 * @return the class
	 * @throws ClassNotFoundRuntimeException the class not found runtime exception
	 */
	public static final Class<?> classForName(final String className)
		throws ClassNotFoundRuntimeException
	{
		final Class<?> c = primitiveClasses.get(className);
		if(c != null) {
			return c;
		}
		try {
			return Class.forName(className);
		} catch (final ClassNotFoundException e) {
			throw new ClassNotFoundRuntimeException(e);
		}
	}

	/**
	 * Class new instance.
	 *
	 * @param <I> the generic type
	 * @param constructor the constructor
	 * @param initargs the initargs
	 * @return the i
	 * @throws InvocationTargetRuntimeException the invocation target runtime exception
	 * @throws IllegalAccessRuntimeException the illegal access runtime exception
	 */
	public static final <I> I classNewInstance(final Constructor<I> constructor, final Object... initargs)
		throws InvocationTargetRuntimeException, IllegalAccessRuntimeException, InvocationTargetRuntimeException
	{
		try {
			return constructor.newInstance(initargs);
		}
		catch (final InvocationTargetException e) {
			throw new InvocationTargetRuntimeException(e);
		}
		catch (final IllegalAccessException e) {
			throw new IllegalAccessRuntimeException(e);
		}
		catch (final InstantiationException e) {
			throw new InstantiationRuntimeException(e);
		}
	}

	/**
	 * Class get constructor.
	 *
	 * @param <I> the generic type
	 * @param c the c
	 * @param parameterTypes the parameter types
	 * @return the constructor
	 * @throws NoSuchMethodRuntimeException the no such method runtime exception
	 */
	public static final <I> Constructor<I> classGetConstructor(final Class<I> c, final Class<?>... parameterTypes)
		throws NoSuchMethodRuntimeException
	{
		try {
			return c.getConstructor(parameterTypes);
		}
		catch (final NoSuchMethodException e) {
			throw new NoSuchMethodRuntimeException(e);
		}
	}

	/**
	 * Inner class get constructor.
	 *
	 * @param <I> the generic type
	 * @param c the c
	 * @param parameterTypes the parameter types
	 * @return the constructor
	 * @throws NoSuchMethodRuntimeException the no such method runtime exception
	 */
	public static final <I> Constructor<I> innerClassGetConstructor(final Class<I> c, final Class<?>... parameterTypes)
		throws NoSuchMethodRuntimeException
	{
		final Class<?> outerClass = c.getEnclosingClass();
		final Class<?>[] actualParamTypes = new Class<?>[parameterTypes==null?1:parameterTypes.length+1];
		actualParamTypes[0] = outerClass;
		if(parameterTypes!=null) {
			for (int i = 0; i < parameterTypes.length; i++) {
				actualParamTypes[i+1] = parameterTypes[i];
			}
		}
		return classGetConstructor(c, actualParamTypes);
	}

	/**
	 * Inner class new instance.
	 *
	 * @param <I> the generic type
	 * @param constructor the constructor
	 * @param enclosingInstance the enclosing instance
	 * @param initargs the initargs
	 * @return the i
	 * @throws InvocationTargetRuntimeException the invocation target runtime exception
	 * @throws IllegalAccessRuntimeException the illegal access runtime exception
	 */
	public static final <I> I innerClassNewInstance(final Constructor<I> constructor, final Object enclosingInstance, final Object... initargs)
		throws InvocationTargetRuntimeException, IllegalAccessRuntimeException, InvocationTargetRuntimeException
	{
		return classNewInstance(constructor, enclosingInstance, initargs);
	}

	/**
	 * Validate setter.
	 * 
	 * @param setter the setter
	 * @param type the type
	 * @param mustReturnVoid the must return void
	 * @return true, if successful
	 */
	public static boolean validateSetter(final Method setter, final Class<?> type, final boolean mustReturnVoid){
		final Class<?>[] paramTypes = setter.getParameterTypes();
		if(paramTypes.length != 1){
			return false;
		}
		if(paramTypes[0] != type){
			return false;
		}
		if(mustReturnVoid && setter.getReturnType() != Void.TYPE){
			return false;
		}
		return true;
	}

	/**
	 * Validate getter.
	 * 
	 * @param getter the getter
	 * @param type the type
	 * @return true, if successful
	 */
	public static boolean validateGetter(final Method getter, final Class<?> type){
		final Class<?>[] paramTypes = getter.getParameterTypes();
		if(paramTypes.length != 0){
			return false;
		}
		if(getter.getReturnType() != type){
			return false;
		}
		return true;
	}

	/**
	 * Derive setter name from field name.
	 * 
	 * @param fieldName the field name
	 * @return the string
	 */
	public static String deriveSetterNameFromFieldName(final String fieldName){
		return Jadoth.createMedialCapitalsString(CODE_CONVENTION_SETTER_PREFIX, fieldName);
	}

	/**
	 * Derive getter name from field name.
	 * 
	 * @param fieldName the field name
	 * @param usePrefix_is the use prefix_is
	 * @return the string
	 */
	public static String deriveGetterNameFromFieldName(final String fieldName, final boolean usePrefix_is){
		return Jadoth.createMedialCapitalsString(
			usePrefix_is?CODE_CONVENTION_ISGETTER_PREFIX:CODE_CONVENTION_GETTER_PREFIX, 
			fieldName
		);
	}

	/**
	 * Derive setter name from field.
	 * 
	 * @param field the field
	 * @return the string
	 */
	public static String deriveSetterNameFromField(final Field field){
		return deriveSetterNameFromFieldName(field.getName());
	}

	/**
	 * Derive getter name from field.
	 * 
	 * @param field the field
	 * @param usePrefix_is_forBoolean the use prefix_is_for boolean
	 * @return the string
	 */
	public static String deriveGetterNameFromField(final Field field, final boolean usePrefix_is_forBoolean){
		return deriveGetterNameFromFieldName(field.getName(), usePrefix_is_forBoolean && isBoolean(field.getType()));
	}

	/**
	 * Derive getter name from field.
	 * 
	 * @param field the field
	 * @return the string
	 */
	public static String deriveGetterNameFromField(final Field field){
		return deriveGetterNameFromField(field, true);
	}

}
