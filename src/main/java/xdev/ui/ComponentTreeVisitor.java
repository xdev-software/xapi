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
package xdev.ui;


import java.awt.Component;


/**
 * 
 * @author XDEV Software Corp.
 * 
 * @param <T>
 *            The return type
 * @param <C>
 *            The component type to visit
 */

public interface ComponentTreeVisitor<T extends Object, C extends Component>
{
	/**
	 * This method is invoked for every visitable component in the component
	 * tree hierarchy which fits the type C.
	 * 
	 * 
	 * @param cpn
	 *            The current visited component
	 * 
	 * @return a value != <code>null</code> means the visit should be ended,
	 *         <code>null</code> means the visit continues
	 */

	public T visit(C cpn);
}
