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
package com.xdev.jadoth.codegen.java.codeobjects;

/**
 * @author Thomas Muenz
 *
 */
public interface JavaTypeWrapper extends JavaTypeDescription
{
	public Class<?> getWrappedType();
	
	
	
	public class Implementation extends JavaTypeDescription.Implementation implements JavaTypeWrapper
	{
		private final Class<?> wrappedType;

		public Implementation(final Class<?> wrappedType)
		{
			super(wrappedType.getModifiers(), wrappedType.getSimpleName());
			this.wrappedType = wrappedType;
		}

		/**
		 * @return
		 * @see com.xdev.jadoth.codegen.java.codeobjects.JavaTypeWrapper#getWrappedType()
		 */
		@Override
		public Class<?> getWrappedType()
		{
			return this.wrappedType;
		}

		/**
		 * @return
		 * @see com.xdev.jadoth.codegen.java.codeobjects.JavaKeywordOwner#getKeyword()
		 */
		@Override
		public String getKeyword()
		{
			return null;
		}

		@Override
		public String getCanonicalName()
		{
			return this.wrappedType.getCanonicalName();
		}
		
		
		
		
		
		
	}
}
