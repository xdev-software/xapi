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


import xdev.lang.NotNull;


/**
 * This class provides a skeletal implementation of the {@link SystemProperty}
 * interface to minimize the effort required to implement this interface.
 * 
 * 
 * @author XDEV Software (RHHF)
 * @since 3.2
 * 
 * @param <T>
 *            type of the {@link SystemProperty} that is wrapped.
 */
public abstract class AbstractSystemProperty<T extends Object> implements SystemProperty<T>
{
	/**
	 * Constant for null String representation.
	 */
	protected static final String	NULL_STRING	= "null";
	
	/**
	 * Name of the system property that is wrapped by this instance.
	 */
	protected final String			name;
	

	/**
	 * Initializes the instance with a system property name.
	 * 
	 * @param name
	 *            name of the system property that is wrapped by this instance.
	 *            Must not be <code>null</code> nor empty.
	 */
	protected AbstractSystemProperty(final @NotNull String name)
	{
		if(name == null)
		{
			throw new IllegalArgumentException("name must not be null");
		}
		if(name.isEmpty())
		{
			throw new IllegalArgumentException("name must not be empty");
		}
		this.name = name;
	}
	

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getName()
	{
		return name;
	}
	

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String setValue(T value)
	{
		return System.setProperty(getName(),String.valueOf(value));
		
	}
	

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void clear()
	{
		System.getProperties().remove(getName());
	}
	
}
