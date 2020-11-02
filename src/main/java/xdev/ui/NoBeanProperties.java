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
 * Marker annotation for beans to mark all fields as no bean property.
 * <p>
 * All fields of a bean with appropriate getters and setters are bean properties
 * per default, except the bean is annotated with <code>NoBeanProperties</code>.
 * <p>
 * To use single fields of a NoBeanProperties-bean as properties they have to be
 * annotated with {@link BeanProperty}.
 * 
 * 
 * @author XDEV Software
 * @since 3.0
 * 
 * @see BeanProperty
 * @see NoBeanProperty
 * @see AllBeanProperties
 * @see BeanSettings
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface NoBeanProperties
{
}
