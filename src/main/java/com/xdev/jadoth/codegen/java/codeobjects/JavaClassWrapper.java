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
/**
 * 
 */
package com.xdev.jadoth.codegen.java.codeobjects;

import com.xdev.jadoth.lang.wrapperexceptions.InvalidClassRuntimeException;

/**
 * @author Thomas Muenz
 *
 */
public interface JavaClassWrapper extends JavaTypeWrapper, JavaClassDescription
{

	
	public class Implementation extends JavaTypeWrapper.Implementation implements JavaClassWrapper
	{
		private static final Class<?> validateWrappedType(final Class<?> wrappedType)
		{
			if(wrappedType.isInterface() || wrappedType.isAnnotation() || wrappedType.isEnum()){
				throw new InvalidClassRuntimeException(wrappedType.getName());
			}
			return wrappedType;
		}
		
		/**
		 * @param wrappedType
		 */
		public Implementation(final Class<?> wrappedType)
		{
			super(validateWrappedType(wrappedType));
		}

		/**
		 * @return
		 * @see com.xdev.jadoth.codegen.java.codeobjects.JavaClassDescription#iterateClassMembers()
		 */
		@Override
		public Iterable<? extends JavaClassMemberDescription> iterateClassMembers()
		{
			return null;
		}

		/**
		 * @return
		 * @see com.xdev.jadoth.codegen.java.codeobjects.JavaClassDescription#iterateConstructors()
		 */
		@Override
		public Iterable<? extends JavaConstructorDescription> iterateConstructors()
		{
			return null;
		}

		/**
		 * @return
		 * @see com.xdev.jadoth.codegen.java.codeobjects.JavaClassDescription#iterateInitializers()
		 */
		@Override
		public Iterable<? extends JavaInitializer> iterateInitializers()
		{
			return null;
		}
		
	}
}
