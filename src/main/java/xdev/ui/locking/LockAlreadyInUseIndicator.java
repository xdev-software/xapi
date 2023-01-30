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
package xdev.ui.locking;


import xdev.db.locking.PessimisticLock;


/**
 * A indicator which holds information about a particular
 * {@link PessimisticLock} which is already in use.
 * 
 * @author XDEV Software jwill
 * @since 4.0
 */
public interface LockAlreadyInUseIndicator
{
	/**
	 * Sets this indicators title.
	 * 
	 * @param title
	 *            the title to set.
	 */
	void setTitle(String title);
	
	
	/**
	 * Returns this indicators title.
	 * 
	 * @return the indicator title.
	 */
	String getTitle();
	
	
	/**
	 * Notifies about a invalid lock request.
	 * 
	 * @param inUseText
	 *            the text to display which informs about a record which is
	 *            already in use.
	 */
	void showText(String inUseText);
	
	
	/**
	 * Returns the text which informs about a record which is already in use.
	 * 
	 * @return the text which informs about a record which is already in use.
	 */
	String getText();
}
