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


import java.awt.Component;


/**
 * The XdevFocusCycleController class is used for focus control operations and
 * decisions.
 * 
 * @author XDEV Software
 * 
 */
public interface XdevFocusCycleController
{
	/**
	 * Returns the {@link Component} before this {@link Component} in the
	 * XdevFoucsCycle.
	 * 
	 * @return the {@link Component} before this {@link Component} in the
	 *         XdevFoucsCycle.
	 */
	public Component getFocusComponentBefore();
	

	/**
	 * Returns the {@link Component} after this {@link Component} in the
	 * XdevFoucsCycle.
	 * 
	 * @return the {@link Component} after this {@link Component} in the
	 *         XdevFoucsCycle.
	 */
	public Component getFocusComponentAfter();
}
