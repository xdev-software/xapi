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
package xdev.db.locking;


/**
 * Provides information about a specific locks construction state.
 * 
 * @author XDEV Software jwill
 * @see PessimisticLock#getLock()
 */
public interface LockConstructionState
{
	/**
	 * Indicates whether the concerned lock was created the first time.
	 * 
	 * @return the indicator whether the concerned lock was created the first
	 *         time or not.
	 */
	boolean isInitial();
	
	
	/**
	 * Sets the indicator whether the concerned lock was created the first time.
	 * 
	 * @param constructionState
	 *            the indicator whether the concerned lock was created the first
	 *            time.
	 */
	void setInitial(boolean constructionState);
	
	
	/**
	 * Indicates whether the concerned lock was renewed.
	 * 
	 * @return the indicator whether the concerned lock was renewed or not.
	 */
	boolean isRegenerated();
	
	
	/**
	 * Sets the indicator whether the concerned lock was renewed.
	 * 
	 * @param constructionState
	 *            the indicator whether the concerned lock was renewed or not.
	 */
	void setRegenerated(boolean constructionState);
}
