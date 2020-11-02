package xdev.ui;

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


/**
 * 
 * 
 * Interface XdevFocusCycleComponent enables components to participate in
 * XdevFocusCycle. All components that participate in the XdevFocusCycle must
 * implement this interface.
 * 
 * @see XdevFocusCycleRoot
 * @see XdevFocusCycleController
 * @see XdevFocusTraversalPolicy
 * 
 * 
 * 
 * @author XDEV Software
 * 
 */
public interface XdevFocusCycleComponent
{
	/**
	 * The property name used by the implementing beans to fire property change
	 * events.
	 */
	public final static String	TAB_INDEX_PROPERTY	= "tabIndex";


	/**
	 * Returns the tabindex assigned to this component. The tabindex defines the
	 * position of the component in the focus cycle.
	 * 
	 * @return tabindex of this component
	 */
	public int getTabIndex();


	/**
	 * Sets the tabindex of this component. The tabindex defines the position of
	 * the component in the focus cycle.
	 * <p>
	 * To exclude this component from the focus cycle set tabindex to -1.
	 * 
	 * @param tabIndex
	 *            to be set
	 */
	public void setTabIndex(int tabIndex);
}
