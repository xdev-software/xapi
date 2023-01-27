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

package com.xdev.jadoth.lang.reflection.copy;

import java.lang.reflect.Field;

import com.xdev.jadoth.lang.reflection.JaReflect;
import com.xdev.jadoth.lang.wrapperexceptions.IllegalAccessRuntimeException;




/**
 * The Interface CopyHandler.
 * 
 * @author Thomas Muenz
 */
public interface CopyHandler {

	/**
	 * Copies the content of Field <code>fieldToCopy</code> from object <code>source</code> to object <code>target</code>.
	 * <p>
	 * Implementation hint:<br>
	 * Use {@link ReflectionTools.setFieldValue(Field f, Object obj, Object value)} to set the value eventually
	 * 
	 * @param fieldToCopy the field to copy
	 * @param source the source
	 * @param target the target
	 */
	public void copy(Field fieldToCopy, Object source, Object target);



	/**
	 * Default implementation of CopyHandler.<br>
	 * Little meaningful because it does the exact same thing as ReflectionTools.copy() does.
	 *
	 * @author Thomas Muenz
	 */
	public static class DefaultImplementation implements CopyHandler {


		///////////////////////////////////////////////////////////////////////////
		// constructors //
		/////////////////
		/**
		 * Trivial default constructor.
		 */
		public DefaultImplementation() {
			super();
		}



		///////////////////////////////////////////////////////////////////////////
		// override methods //
		/////////////////////
		/**
		 * Default copy implementation. Little meaningful because it does the exact same thing as
		 * ReflectionTools.copy() does.
		 * 
		 * @param fieldToCopy the field to copy
		 * @param source the source
		 * @param target the target
		 * @throws IllegalAccessRuntimeException the illegal access runtime exception
		 */
		@Override
		public void copy(final Field fieldToCopy, final Object source, final Object target) throws IllegalAccessRuntimeException {
			JaReflect.setFieldValue(fieldToCopy, target, JaReflect.getFieldValue(fieldToCopy, source));
		}

	}

}
