package xdev.util.systemproperty;

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


import xdev.lang.Nullable;


/**
 * 
 * {@link SystemProperty} is a thin wrapper around the java system properties
 * facility. It provides type-safe access to a system property and is aware of
 * the name of that property.
 * 
 * 
 * @author XDEV Software (RHHF)
 * @since 3.2
 * 
 * @param <T>
 *            type of the {@link SystemProperty} that is wrapped.
 */
public interface SystemProperty<T extends Object>
{
	
	/**
	 * Determines the value of the system property specified by
	 * {@link #getName()}.
	 * 
	 * 
	 * 
	 * @return the value of the property. <code>null</code> if the specified
	 *         system property was not found or the value could not be parsed.
	 */
	public @Nullable
	T getValue();
	

	/**
	 * Determines the value of the system property specified by
	 * {@link #getName()}.
	 * 
	 * @param defaultValue
	 *            will be returned if the specified system property was not
	 *            found.
	 * 
	 * @return the value of the property. <code>defaultValue</code> if the
	 *         specified system property was not found or the value could not be
	 *         parsed.
	 */
	public @Nullable
	T getValue(final @Nullable T defaultValue);
	

	/**
	 * Returns the name of the system property that is wrapped by this
	 * {@link SystemProperty} instance.
	 * 
	 * @return the name of the system property that is wrapped by this
	 *         {@link SystemProperty} instance.
	 */
	public @Nullable
	String getName();
	

	/**
	 * Sets the system property specified by {@link #getName()}.
	 * <p>
	 * First, if a security manager exists, its
	 * <code>SecurityManager.checkPermission</code> method is called with a
	 * <code>PropertyPermission(key, "write")</code> permission. This may result
	 * in a SecurityException being thrown. If no exception is thrown, the
	 * specified property is set to the given value.
	 * <p>
	 * 
	 * 
	 * @param value
	 *            the value of the system property.
	 * @return the previous value of the system property, or <code>null</code>
	 *         if it did not have one.
	 * 
	 * @exception SecurityException
	 *                if a security manager exists and its
	 *                <code>checkPermission</code> method doesn't allow setting
	 *                of the specified property.
	 * @exception NullPointerException
	 *                if <code>{@link #getName()}</code> or <code>value</code>
	 *                is <code>null</code>.
	 * @exception IllegalArgumentException
	 *                if <code>{@link #getName()}</code> is empty.
	 */
	public @Nullable
	String setValue(final @Nullable T value) throws SecurityException, NullPointerException,
			IllegalArgumentException;
	

	/**
	 * Removes the system property from the system property set.
	 */
	public void clear();
	
}
