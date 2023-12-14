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
package xdev.lang;


/**
 * The <code>Copyable</code> interface defines a type safe clone
 * method to create a field-for-field copy of instances of that class.
 * 
 * <p>
 * <strong>Hint:</strong> This interface extends the
 * <code>java.lang.Cloneable</code> interface.
 * </p>
 * 
 * @see Comparable
 * 
 * @author XDEV Software
 * @param <T>
 *            type of the clone
 * @since 2.0
 */
public interface Copyable<T> extends Cloneable
{
	/**
	 * Returns a field-for-field copy of this instance.
	 * 
	 * @return a field-for-field copy of this instance.
	 */
	public T clone();
}
