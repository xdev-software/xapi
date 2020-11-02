package xdev.lang;

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
 * An annotation for SQL query methods.
 * <p>
 * Can be used as marker annotation
 * 
 * <pre>
 * &#64;SQLQuery public void myQuery()...
 * </pre>
 * 
 * or with a <code>group</code> parameter
 * 
 * <pre>
 * &#64;SQLQuery(group="myqueries") public void myQuery()...
 * </pre>
 * 
 * @author XDEV Software
 * @since 3.0
 */

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface SQLQuery
{
	/**
	 * @return optional group of the query, default is none ("")
	 */
	String group() default "";
}
