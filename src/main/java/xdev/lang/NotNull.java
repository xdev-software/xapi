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


import java.lang.annotation.*;


/**
 * A program element annotated &#64;NotNull should always have a value.
 * <ul>
 * <li>
 * Field: Should be initialized within the class constructors or initializers
 * and not become a <code>null</code> pointer during runtime.</li>
 * <li>
 * Method: Should never return <code>null</code></li>
 * <li>
 * Parameter: Should not be <code>null</code>, otherwise most probable a
 * {@link NullPointerException} is thrown.</li>
 * </ul>
 * 
 * @author XDEV Software Corp.
 * 
 */

@Target({ElementType.FIELD,ElementType.PARAMETER,ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface NotNull
{
}
