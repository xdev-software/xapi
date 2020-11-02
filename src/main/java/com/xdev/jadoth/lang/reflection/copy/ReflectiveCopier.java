
package com.xdev.jadoth.lang.reflection.copy;

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

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.xdev.jadoth.Jadoth;
import com.xdev.jadoth.lang.reflection.JaReflect;
import com.xdev.jadoth.lang.wrapperexceptions.IllegalAccessRuntimeException;




/**
 * The Class ReflectiveCopier.
 *
 * @author Thomas Muenz
 */
public class ReflectiveCopier {

	///////////////////////////////////////////////////////////////////////////
	// static fields //
	//////////////////

	/** The Constant cachedInstanceFields. */
	private static final HashMap<Class<?>, Field[]> cachedInstanceFields = new HashMap<Class<?>, Field[]>();



	///////////////////////////////////////////////////////////////////////////
	// static methods //
	///////////////////

	/**
	 * Gets the all copyable fields.
	 *
	 * @param c the c
	 * @return the all copyable fields
	 */
	public static final ArrayList<Field> getAllCopyableFields(final Class<?> c) {
		return JaReflect.getAllFields(c, Modifier.FINAL | Modifier.STATIC);
	}

	/**
	 * Gets the cached instance fields.
	 *
	 * @param c the c
	 * @return the cached instance fields
	 */
	private static Field[] getCachedInstanceFields(final Class<?> c){
		Field[] instanceFields = cachedInstanceFields.get(c);
		if(instanceFields == null) {
			instanceFields = Jadoth.toArray(getAllCopyableFields(c), Field.class);
			cachedInstanceFields.put(c, instanceFields);
		}
		return instanceFields;
	}

	/**
	 * Field untyped copy.
	 *
	 * @param source the source
	 * @param target the target
	 * @param f the f
	 * @param copyHandler the copy handler
	 * @return the object
	 */
	public static final Object fieldUntypedCopy(
			final Object source, final Object target, final Field f, final CopyHandler copyHandler
	){
		if(copyHandler != null){
			copyHandler.copy(f, source, target);
		}
		else {
			//default (shallow) copy if no handler is given
			JaReflect.setFieldValue(f, target, JaReflect.getFieldValue(f, source));
		}
		return target;
	}

	/**
	 * Field copy.
	 *
	 * @param <O> the generic type
	 * @param <S> the generic type
	 * @param <T> the generic type
	 * @param source the source
	 * @param target the target
	 * @param f the f
	 * @param copyHandler the copy handler
	 * @return the t
	 */
	@SuppressWarnings("unchecked")
	public static final <O extends Object, S extends O, T extends O> T fieldCopy(
			final S source, final T target, final Field f, final CopyHandler copyHandler
	){
		return (T)fieldUntypedCopy(source, target, f, copyHandler);
	}

	/**
	 * Untyped copy.
	 *
	 * @param source the source
	 * @param target the target
	 * @param commonClass the common class
	 * @return the object
	 * @throws IllegalAccessRuntimeException the illegal access runtime exception
	 */
	public static final Object untypedCopy(final Object source, final Object target, final Class<?> commonClass)
		throws IllegalAccessRuntimeException
	{
		final Field[] instanceFields = getCachedInstanceFields(commonClass);
		for(final Field f : instanceFields) {
			JaReflect.setFieldValue(f, target, JaReflect.getFieldValue(f, source));
		}
		return target;
	}

	/**
	 * Untyped copy.
	 *
	 * @param source the source
	 * @param target the target
	 * @param commonClass the common class
	 * @param fieldsToExclude the fields to exclude
	 * @return the object
	 * @throws IllegalAccessRuntimeException the illegal access runtime exception
	 */
	public static final Object untypedCopy(final Object source, final Object target, final Class<?> commonClass, final Set<Field> fieldsToExclude)
		throws IllegalAccessRuntimeException
	{
		if(fieldsToExclude == null) {
			return untypedCopy(source, target, commonClass);
		}

		final Field[] instanceFields = getCachedInstanceFields(commonClass);
		for(final Field f : instanceFields) {
			if(fieldsToExclude.contains(f)){
				continue;
			}
			JaReflect.setFieldValue(f, target, JaReflect.getFieldValue(f, source));
		}
		return target;
	}

	/**
	 * Untyped copy.
	 *
	 * @param source the source
	 * @param target the target
	 * @param commonClass the common class
	 * @param fieldsToExclude the fields to exclude
	 * @param targetFieldCopyHandlers the target field copy handlers
	 * @param targetAnnotationHandlers the target annotation handlers
	 * @param targetClassCopyHandlers the target class copy handlers
	 * @param genericCopyHandler the generic copy handler
	 * @return the object
	 * @throws IllegalAccessRuntimeException the illegal access runtime exception
	 */
	public static final Object untypedCopy(
			final Object source, final Object target, final Class<?> commonClass,
			Set<Field> fieldsToExclude,
			Map<Field, CopyHandler> targetFieldCopyHandlers,
			Map<Class<? extends Annotation>, CopyHandler> targetAnnotationHandlers,
			Map<Class<?>, CopyHandler> targetClassCopyHandlers,
			final CopyHandler genericCopyHandler
	)
		throws IllegalAccessRuntimeException
	{

		if(fieldsToExclude == null) {
			fieldsToExclude = new HashSet<Field>(0);
		}

		final Field[] instanceFields = getCachedInstanceFields(commonClass);

		// simple case without copyhandlers
		if(targetClassCopyHandlers == null && targetFieldCopyHandlers == null && targetAnnotationHandlers == null){
			for(final Field f : instanceFields) {
				if(fieldsToExclude.contains(f)) {
					continue;
				}
				if(genericCopyHandler != null){
					genericCopyHandler.copy(f, source, target);
				}
				else {
					JaReflect.setFieldValue(f, target, JaReflect.getFieldValue(f, source));
				}
			}
			return target;
		}



		// special treatment with copyhandlers

		if(targetClassCopyHandlers == null) {
			targetClassCopyHandlers = new HashMap<Class<?>, CopyHandler>(0);
		}
		if(targetAnnotationHandlers == null) {
			targetAnnotationHandlers = new HashMap<Class<? extends Annotation>, CopyHandler>(0);
		}
		if(targetFieldCopyHandlers == null) {
			targetFieldCopyHandlers = new HashMap<Field, CopyHandler>(0);
		}
		for(final Field f : instanceFields) {
			if(fieldsToExclude.contains(f)) {
				continue;
			}

			//1.) fieldCopyHandler has highest priority
			CopyHandler handler = targetFieldCopyHandlers.get(f);

			//2.) if no fieldCopyHandler then look for annotationCopyHandler
			if(handler == null){
				final Annotation[] annotations = f.getAnnotations();
				for (final Annotation a : annotations) {
					handler = targetAnnotationHandlers.get(a.annotationType());
					if(handler != null){
						break;
					}
				}
			}
			//3.) if neither fieldCopyHandler nor annotationCopyHandler then look for classCopyHandler
			if(handler == null){
				handler = targetClassCopyHandlers.get(f.getType());
			}
			//4.) finally, try using genericCopyHandler
			if(handler == null){
				handler = genericCopyHandler;
			}

			fieldCopy(source, target, f, handler);
		}

		return target;
	}

	/**
	 * This method is a convenience version for <code>copy(source, target, commonClass, null)</code> which means
	 * that no fields are excluded from copiing.
	 * <p>
	 * See {@link #execute(Object, Object, Class, Set)} for further details.
	 *
	 * @param <O> the common type of <code>source</code> and <code>target</code>. Can be the same class or a common super class.
	 * @param <S> the type of the source object, which must be of Type <code>O</code>
	 * @param <T> the type of the source object, which must be of Type <code>O</code>
	 * @param source the source object from which the values are read.
	 * @param target the target object to which the values from <code>source</code> are written
	 * @param commonClass the Class that determines the level, at field values are copied from <code>source</code> and <code>target</code>.
	 * Can be the same class or a common super class.
	 * @return
	 * @throws IllegalAccessRuntimeException the illegal access runtime exception
	 * @see {@link #execute(Object, Object, Class, Set)}
	 */
	@SuppressWarnings("unchecked")
	public static final <O extends Object, S extends O, T extends O> T copy(final S source, final T target, final Class<O> commonClass)
		throws IllegalAccessRuntimeException
	{
		return (T)untypedCopy(source, target, commonClass);
	}

	/**
	 * Copies the values of all copiable instance fields (non static, non final) from source object <code>source</code>
	 * to target object <code>target</code>.
	 * <p>
	 * The parameter <code>commonClass</code> ensures the type compatibility of <code>source</code> and <code>target</code>.
	 * It also determines the class level, on which the copiing shall be done.
	 * <p>
	 * Examples:<br>
	 * 1.)<br>
	 * Both <code>sourceObj</code> and <code>targetObj</code> are objects of Class MyClassA extends MyCommonClass.
	 * <p>
	 * The call <code>copy(sourceObj, targetObj, MyClassA.class)</code> enables the compiler to check if both objects are really
	 * of the same Type and instructs the copy method to copy all (non static, non final) fields from Classes MyClassA and
	 * MyCommonClass as well.
	 * <p>
	 * The call <code>copy(sourceObj, targetObj, MyCommonClass.class)</code> causes the method only to copy fields of Class
	 * MyCommonClass.
	 * <p>
	 * 2.)<br>
	 * <code>sourceObj</code> is of Class MyClassA extends MyCommonClass.<br>
	 * <code>targetObj</code> is of Class MyClassB extends MyCommonClass.<br>
	 * <p>
	 * The call <code>copy(sourceObj, targetObj, MyCommonClass.class)</code> would be valid.<br>
	 * The call <code>copy(sourceObj, targetObj, MyClassA.class)</code> would cause a compiler error<br>
	 *
	 * <p>
	 * Notes:<ul>
	 * <li><code>copy</code> does not create any new instances
	 * <li>Copiable fields are cached after the first call to improve performance.
	 * <li>The <code>source</code> object will not be modified in any kind.
	 * <li>All field values of <code>target</code> that are not overwritten by values from <code>source</code> keep their values.
	 * <li>The Java <code>IllegalAccessException</code> that can occur by reflection access should be prevented by internal mechanisms.
	 * In case it might still occur, it is nested into a RuntimeException of type IllegalAccessRuntimeException
	 * </ul>
	 *
	 * @param <O> the common type of <code>source</code> and <code>target</code>. Can be the same class or a common super class.
	 * @param <S> the type of the source object, which must be of Type <code>O</code>
	 * @param <T> the type of the source object, which must be of Type <code>O</code>
	 * @param source the source object from which the values are read.
	 * @param target the target object to which the values from <code>source</code> are written
	 * @param commonClass the Class that determines the level, at field values are copied from <code>source</code> and <code>target</code>.
	 * Can be the same class or a common super class.
	 * @param fieldsToExclude fields that shall explicitly excluded from copiing.
	 * @return
	 * @throws IllegalAccessRuntimeException the illegal access runtime exception
	 * @see {@link #execute(Object, Object, Class)}
	 */
	@SuppressWarnings("unchecked")
	public static final <O extends Object, S extends O, T extends O> T copy(final S source, final T target, final Class<O> commonClass, final Set<Field> fieldsToExclude)
		throws IllegalAccessRuntimeException
	{
		if(fieldsToExclude == null) {
			return (T)untypedCopy(source, target, commonClass);
		}
		return (T)untypedCopy(source, target, commonClass, fieldsToExclude);
	}

	/**
	 * Copy.
	 *
	 * @param <O> the generic type
	 * @param <S> the generic type
	 * @param <T> the generic type
	 * @param source the source
	 * @param target the target
	 * @param commonClass the common class
	 * @param fieldsToExclude the fields to exclude
	 * @param targetFieldCopyHandlers the target field copy handlers
	 * @param targetAnnotationHandlers the target annotation handlers
	 * @param targetClassCopyHandlers the target class copy handlers
	 * @param genericCopyHandler the generic copy handler
	 * @return the t
	 * @throws IllegalAccessRuntimeException the illegal access runtime exception
	 */
	@SuppressWarnings("unchecked")
	public static final <O extends Object, S extends O, T extends O> T copy(final S source, final T target, final Class<O> commonClass,
			final Set<Field> fieldsToExclude,
			final Map<Field, CopyHandler> targetFieldCopyHandlers,
			final Map<Class<? extends Annotation>, CopyHandler> targetAnnotationHandlers,
			final Map<Class<?>, CopyHandler> targetClassCopyHandlers,
			final CopyHandler genericCopyHandler
	)
		throws IllegalAccessRuntimeException
	{
		return (T)untypedCopy(source, target, commonClass, fieldsToExclude, targetFieldCopyHandlers, targetAnnotationHandlers, targetClassCopyHandlers, genericCopyHandler);
	}



	///////////////////////////////////////////////////////////////////////////
	// instance fields //
	////////////////////

	/** The location class annotation class handlers. */
	private HashMap<Class<?>, HashMap<Class<? extends Annotation>, CopyHandler>> locationClassAnnotationClassHandlers =
		new HashMap<Class<?>, HashMap<Class<? extends Annotation>, CopyHandler>>();

	/** The location class copy target class handlers. */
	private HashMap<Class<?>, HashMap<Class<?>, CopyHandler>> locationClassCopyTargetClassHandlers =
		new HashMap<Class<?>, HashMap<Class<?>, CopyHandler>>();

	/** The location class copy target field handlers. */
	private HashMap<Class<?>, HashMap<Field, CopyHandler>> locationClassCopyTargetFieldHandlers =
		new HashMap<Class<?>, HashMap<Field, CopyHandler>>();
	/*
	 * only one GenericCopyHandler per locationClass makes sense
	 */
	/** The location class generic handlers. */
	private HashMap<Class<?>, CopyHandler> locationClassGenericHandlers =
		new HashMap<Class<?>, CopyHandler>();

	/** The general annotation handlers. */
	private HashMap<Class<? extends Annotation>, CopyHandler> generalAnnotationHandlers =
		new HashMap<Class<? extends Annotation>, CopyHandler>();

	/** The general copy target class handlers. */
	private HashMap<Class<?>, CopyHandler> generalCopyTargetClassHandlers =
		new HashMap<Class<?>, CopyHandler>();

	/** The general copy target field handlers. */
	private HashMap<Field, CopyHandler> generalCopyTargetFieldHandlers =
		new HashMap<Field, CopyHandler>();

	/** The generic handler. */
	private CopyHandler genericHandler = null;



	///////////////////////////////////////////////////////////////////////////
	// constructors //
	/////////////////
	/**
	 * Trivial default constructor.
	 */
	public ReflectiveCopier() {
		super();
	}



	///////////////////////////////////////////////////////////////////////////
	// declared methods //
	/////////////////////

	/**
	 * Sets the generic copy handler.
	 *
	 * @param handler the handler
	 * @return the reflective copier
	 */
	public ReflectiveCopier setGenericCopyHandler(final CopyHandler handler){
		this.genericHandler = handler;
		return this;
	}

	/**
	 * Adds the copy handler by location class.
	 *
	 * @param handler the handler
	 * @param locationClass the location class
	 * @return the reflective copier
	 */
	public ReflectiveCopier addCopyHandlerByLocationClass(final CopyHandler handler, final Class<?> locationClass){
		return this.addCopyHandler(handler, locationClass, null, null, null);
	}

	/**
	 * Adds the copy handler by copy class.
	 *
	 * @param handler the handler
	 * @param copyTargetClass the copy target class
	 * @return the reflective copier
	 */
	public ReflectiveCopier addCopyHandlerByCopyClass(final CopyHandler handler, final Class<?> copyTargetClass){
		return this.addCopyHandler(handler, null, copyTargetClass, null, null);
	}

	/**
	 * Adds the copy handler by annotation.
	 *
	 * @param handler the handler
	 * @param copyTargetAnnotation the copy target annotation
	 * @return the reflective copier
	 */
	public ReflectiveCopier addCopyHandlerByAnnotation(final CopyHandler handler, final Class<? extends Annotation> copyTargetAnnotation){
		return this.addCopyHandler(handler, null, null, copyTargetAnnotation, null);
	}

	/**
	 * Adds the copy handler by copy field.
	 *
	 * @param handler the handler
	 * @param copyTargetField the copy target field
	 * @return the reflective copier
	 */
	public ReflectiveCopier addCopyHandlerByCopyField(final CopyHandler handler, final Field copyTargetField){
		return this.addCopyHandler(handler, null, null, null, copyTargetField);
	}

	/**
	 * Registers <code>handler</code> to be used by this DeepCopier.
	 *
	 * @param handler the handler
	 * @param locationClass the location class
	 * @param copyTargetClass the copy target class
	 * @param copyTargetAnnotation the copy target annotation
	 * @param copyTargetField the copy target field
	 * @return the reflective copier
	 * @return
	 */
	public ReflectiveCopier addCopyHandler(
			final CopyHandler handler,
			final Class<?> locationClass,
			final Class<?> copyTargetClass,
			final Class<? extends Annotation> copyTargetAnnotation,
			final Field copyTargetField
	){
		if(locationClass != null){
			// register locationClass-specific handlers

			if(copyTargetField != null) {
				HashMap<Field, CopyHandler> targetFieldHandlers = this.locationClassCopyTargetFieldHandlers.get(locationClass);
				if(targetFieldHandlers == null){
					targetFieldHandlers = new HashMap<Field, CopyHandler>();
					this.locationClassCopyTargetFieldHandlers.put(locationClass, targetFieldHandlers);
				}
				targetFieldHandlers.put(copyTargetField, handler);
			}
			if(copyTargetAnnotation != null) {
				HashMap<Class<? extends Annotation>, CopyHandler> targetAnnotationHandlers = this.locationClassAnnotationClassHandlers.get(locationClass);
				if(targetAnnotationHandlers == null){
					targetAnnotationHandlers = new HashMap<Class<? extends Annotation>, CopyHandler>();
					this.locationClassAnnotationClassHandlers.put(locationClass, targetAnnotationHandlers);
				}
				targetAnnotationHandlers.put(copyTargetAnnotation, handler);
			}
			else if(copyTargetClass != null){
				HashMap<Class<?>, CopyHandler> targetClassHandlers = this.locationClassCopyTargetClassHandlers.get(locationClass);
				if(targetClassHandlers == null){
					targetClassHandlers = new HashMap<Class<?>, CopyHandler>();
					this.locationClassCopyTargetClassHandlers.put(locationClass, targetClassHandlers);
				}
				targetClassHandlers.put(copyTargetClass, handler);
			}
			else {
				this.locationClassGenericHandlers.put(locationClass, handler);
			}
		}
		else if(copyTargetField != null){
			this.generalCopyTargetFieldHandlers.put(copyTargetField, handler);
		}
		else if(copyTargetAnnotation != null){
			this.generalAnnotationHandlers.put(copyTargetAnnotation, handler);
		}
		else if(copyTargetClass != null){
			this.generalCopyTargetClassHandlers.put(copyTargetClass, handler);
		}
		else {
			this.genericHandler = handler;
		}

		return this;
	}

	/**
	 * Gets the all copy target class handlers for.
	 *
	 * @param locationClass the location class
	 * @return the all copy target class handlers for
	 */
	private HashMap<Class<?>, CopyHandler> getAllCopyTargetClassHandlersFor(final Class<?> locationClass)
	{
		final HashMap<Class<?> , CopyHandler> locationClassMap;
		locationClassMap = this.locationClassCopyTargetClassHandlers.get(locationClass);

		if(locationClassMap == null){
			return this.generalCopyTargetClassHandlers;
		}

		final HashMap<Class<?>, CopyHandler> returnMap = new HashMap<Class<?>, CopyHandler>();
		returnMap.putAll(this.generalCopyTargetClassHandlers);
		returnMap.putAll(locationClassMap);
		return returnMap;
	}

	/**
	 * Gets the all copy target field handlers for.
	 *
	 * @param locationClass the location class
	 * @return the all copy target field handlers for
	 */
	private HashMap<Field, CopyHandler> getAllCopyTargetFieldHandlersFor(final Class<?> locationClass)
	{
		final HashMap<Field , CopyHandler> locationClassMap;
		locationClassMap = this.locationClassCopyTargetFieldHandlers.get(locationClass);

		if(locationClassMap == null){
			return this.generalCopyTargetFieldHandlers;
		}

		final HashMap<Field , CopyHandler> returnMap = new HashMap<Field, CopyHandler>();
		returnMap.putAll(this.generalCopyTargetFieldHandlers);
		returnMap.putAll(locationClassMap);
		return returnMap;
	}

	/**
	 * Gets the all copy target annotation handlers for.
	 *
	 * @param locationClass the location class
	 * @return the all copy target annotation handlers for
	 */
	private HashMap<Class<? extends Annotation>, CopyHandler> getAllCopyTargetAnnotationHandlersFor(final Class<?> locationClass)
	{
		final HashMap<Class<? extends Annotation>, CopyHandler> locationClassMap;
		locationClassMap = this.locationClassAnnotationClassHandlers.get(locationClass);

		if(locationClassMap == null){
			return this.generalAnnotationHandlers;
		}

		final HashMap<Class<? extends Annotation>, CopyHandler> returnMap = new HashMap<Class<? extends Annotation>, CopyHandler>();
		returnMap.putAll(this.generalAnnotationHandlers);
		returnMap.putAll(locationClassMap);
		return returnMap;
	}

	/**
	 * Execute.
	 *
	 * @param <O> the generic type
	 * @param <S> the generic type
	 * @param <T> the generic type
	 * @param source the source
	 * @param target the target
	 * @param commonClass the common class
	 * @return the t
	 * @throws IllegalAccessRuntimeException the illegal access runtime exception
	 */
	public <O extends Object, S extends O, T extends O> T execute(final S source, final T target, final Class<O> commonClass)  throws IllegalAccessRuntimeException {
		return this.execute(source, target, commonClass, null);
	}

	/**
	 * Execute.
	 *
	 * @param <O> the generic type
	 * @param <S> the generic type
	 * @param <T> the generic type
	 * @param source the source
	 * @param target the target
	 * @param commonClass the common class
	 * @param fieldsToExclude the fields to exclude
	 * @return the t
	 * @throws IllegalAccessRuntimeException the illegal access runtime exception
	 */
	public <O extends Object, S extends O, T extends O> T execute(final S source, final T target, final Class<O> commonClass, final Set<Field> fieldsToExclude)  throws IllegalAccessRuntimeException {
		final Class<?> locationClass = source.getClass();
		HashMap<Class<?>, CopyHandler> copyTargetClassHandlers = this.getAllCopyTargetClassHandlersFor(locationClass);
		HashMap<Class<? extends Annotation>, CopyHandler> annotationHandlers = this.getAllCopyTargetAnnotationHandlersFor(locationClass);
		HashMap<Field, CopyHandler> copyTargetFieldHandlers = this.getAllCopyTargetFieldHandlersFor(locationClass);

		//all 3 null will cause the copy method to take a more performant code branch
		if(copyTargetClassHandlers.size() + copyTargetFieldHandlers.size() + annotationHandlers.size() == 0 ){
			copyTargetClassHandlers = null;
			copyTargetFieldHandlers = null;
			annotationHandlers = null;
		}

		CopyHandler genericHandler = this.locationClassGenericHandlers.get(locationClass);
		if(genericHandler == null){
			genericHandler = this.genericHandler;
		}

		return copy(source, target, commonClass, fieldsToExclude, copyTargetFieldHandlers, annotationHandlers, copyTargetClassHandlers, genericHandler);
	}

}
