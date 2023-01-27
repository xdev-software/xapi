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
package xdev.ui.paging;


/**
 * @author XDEV Software (HL)
 * @since 4.0
 */
public interface LazyLoadable
{
	/**
	 * Enabled or disables the lazy loading feature.
	 * <p>
	 * If lazy loading is enabled only the displayed rows are loaded.
	 * 
	 * @since 4.0
	 */
	public void setLazyLoadingEnabled(boolean b);
	
	
	/**
	 * @return if the lazy loading feature is enabled
	 * 
	 * @since 4.0
	 */
	public boolean isLazyLoadingEnabled();
	
}
