package xdev.ui;

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


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * Marks a field as a bean property used by the XDEV GUI designer.
 * <p>
 * All fields with appropriate getters and setters are bean properties per
 * default, except the bean is annotated with {@link NoBeanProperties}.
 * <p>
 * Usually this annotation is used to specify constraints for the property, like
 * the value range with {@link #intMax()} and {@link #intMax()}.
 * 
 * @author XDEV Software
 * @since 3.0
 * 
 * @see NoBeanProperty
 * @see AllBeanProperties
 * @see NoBeanProperties
 * @see BeanSettings
 */
@Target({ElementType.FIELD,ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface BeanProperty
{
	/**
	 * The category name of the property
	 * 
	 * @see DefaultBeanCategories
	 * @since 3.2
	 */
	String category() default "";
	

	/**
	 * the name of the owner property on which this property depends on
	 * 
	 * @since 3.2
	 */
	String owner() default "";
	

	/**
	 * the supported supertype if the property's type is {@link Class}
	 */
	Class classType() default Object.class;
	

	/**
	 * minimum value, if the type is byte
	 */
	byte byteMin() default Byte.MIN_VALUE;
	

	/**
	 * maximum value, if the type is byte
	 */
	byte byteMax() default Byte.MAX_VALUE;
	

	/**
	 * minimum value, if the type is short
	 */
	short shortMin() default Short.MIN_VALUE;
	

	/**
	 * maximum value, if the type is short
	 */
	short shortMax() default Short.MAX_VALUE;
	

	/**
	 * minimum value, if the type is int
	 */
	int intMin() default Integer.MIN_VALUE;
	

	/**
	 * maximum value, if the type is int
	 */
	int intMax() default Integer.MAX_VALUE;
	

	/**
	 * minimum value, if the type is long
	 */
	long longMin() default Long.MIN_VALUE;
	

	/**
	 * maximum value, if the type is max
	 */
	long longMax() default Long.MAX_VALUE;
	

	/**
	 * minimum value, if the type is float
	 */
	float floatMin() default Float.MIN_VALUE;
	

	/**
	 * maximum value, if the type is float
	 */
	float floatMax() default Float.MAX_VALUE;
	

	/**
	 * minimum value, if the type is double
	 */
	double doubleMin() default Double.MIN_VALUE;
	

	/**
	 * maximum value, if the type is double
	 */
	double doubleMax() default Double.MAX_VALUE;
}
