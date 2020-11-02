package xdev.ui.paging;

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
 * @author XDEV Software (HL)
 * @since 4.0
 */
public interface Pageable
{
	/**
	 * Turns the paging behavior on or off.
	 * 
	 * @param b
	 *            <code>true</code> if the paging should be controlled via the
	 *            {@link #getPageControl()}
	 * @see #isPagingEnabled()
	 */
	public void setPagingEnabled(boolean b);
	
	
	/**
	 * @return <code>true</code> if the paging behavior is on,
	 *         <code>false</code> otherwise
	 * @see #setPagingEnabled(boolean)
	 */
	public boolean isPagingEnabled();
	
	
	/**
	 * 
	 * @return the controller object to control the paging of the pageable
	 *         component
	 */
	public PageControl getPageControl();
}
