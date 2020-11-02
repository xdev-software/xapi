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


import java.beans.Beans;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * Bean annotation to customize the behaviour of the customizer in the XDEV GUI
 * builder.
 * 
 * @author XDEV Software
 * @since 3.0
 * 
 * @see BeanProperty
 * @see NoBeanProperty
 * @see AllBeanProperties
 * @see NoBeanProperties
 */

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface BeanSettings
{
	/**
	 * @return <code>true</code> if this container accepts child components,
	 *         <code>false</code> otherwise (default)
	 */
	boolean acceptChildren() default false;
	
	
	/**
	 * @return <code>true</code> if the customizer should instantiate the
	 *         component automatically, <code>false</code> otherwise
	 * @deprecated auto preview is always on, use {@link Beans#isDesignTime()}
	 *             to control design time program flow
	 */
	@Deprecated
	boolean autoPreview() default true;
	
	
	/**
	 * @return <code>true</code> if the customizer for the superclass in the
	 *         package xdev.ui should be used, <code>false</code> otherwise
	 */
	boolean useXdevCustomizer() default true;
	
	
	/**
	 * @return the preferred width of the customizer if {@link #autoPreview()}
	 *         is <code>false</code>, default is 100
	 */
	int preferredWidth() default 100;
	
	
	/**
	 * @return the preferred height of the customizer if {@link #autoPreview()}
	 *         is <code>false</code>, default is 100
	 */
	int preferredHeight() default 100;
}
