/**
 *
 */
package com.xdev.jadoth.collections;

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

import com.xdev.jadoth.lang.functional.Operation;
import com.xdev.jadoth.lang.functional.controlflow.TOperation;


/**
 * @author Thomas Muenz
 *
 */
public interface XCollection<E>
extends
XSettingCollection<E>,
XAddingCollection<E>,
XRemovingCollection<E>
{
	public interface Factory<E>
	extends XSettingCollection.Factory<E>, XAddingCollection.Factory<E>, XRemovingCollection.Factory<E>
	{
		@Override
		public XCollection<E> createInstance();
	}



	public XCollection<E> execute( Operation<E> operation);
	public XCollection<E> execute(TOperation<E> operation);

	public XCollection<E> process( Operation<E> operation);
	public XCollection<E> process(TOperation<E> operation);

}
