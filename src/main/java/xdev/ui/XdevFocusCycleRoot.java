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

/**
 * 
 * 
 * Interface XdevFocusCycleRoot enables components to own a offset.
 * 
 * 
 * @see XdevFocusCycleComponent
 * @see XdevFocusCycleController
 * @see XdevFocusTraversalPolicy
 * 
 * @author XDEV Software
 * 
 */
//TODO class description
public interface XdevFocusCycleRoot
{
	/**
	 * Returns the tabindex offset assigned to this component. 
	 * 
	 * @return tabindex offset of this root component
	 */
	public int getTabIndexOffset();
}
