/**
 * 
 */
package com.xdev.jadoth.codegen.java.codeobjects;

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

import java.lang.reflect.Modifier;

/**
 * @author Thomas Muenz
 *
 */
public class JavaModifier
{
	
	public static final int DEFAULT = 1<<30;
	public static final int PRIVATE = Modifier.PRIVATE;
	public static final int PROTECTED = Modifier.PROTECTED;
	public static final int PUBLIC = Modifier.PUBLIC;
	
	public static final int STATIC = Modifier.STATIC;
	
	public static final int FINAL = Modifier.FINAL;
	
	public static final int ABSTRACT = Modifier.ABSTRACT;
	
	public static final int TRANSIENT = Modifier.TRANSIENT;
	
	public static final int VOLATILE = Modifier.VOLATILE;
	
	public static final int SYNCHRONIZED = Modifier.SYNCHRONIZED;
	
	public static final int NATIVE = Modifier.NATIVE;
	
	

	
	public static final int VISIBILITY = DEFAULT|PRIVATE|PROTECTED|PUBLIC;

	
	
	private final int modifiers;






	JavaModifier(final int modifiers)
	{
		super();
		this.modifiers = modifiers;
	}
	
	
	
	
	public int getModifiers()
	{
		return this.modifiers;
	}
	
}
