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
import java.lang.annotation.Target;


/**
 * 
 * A class annotated with LibraryMember appears in the library view in the IDE
 * if it is in the current project's classpath.
 * <p>
 * Typically utility classes are using this annotation because only the public
 * static methods are visible in the library view.
 * 
 * @author XDEV Software
 * 
 */

@Target({ElementType.TYPE})
public @interface LibraryMember
{
	String description() default "";
}
