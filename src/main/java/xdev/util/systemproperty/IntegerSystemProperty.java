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
 * This class provides implementation of the {@link SystemProperty} interface
 * for an {@link Integer} type.
 * 
 * 
 * @author XDEV Software (RHHF)
 * @since 3.2
 * 
 */
public class IntegerSystemProperty extends AbstractSystemProperty<Integer>
{
	/**
	 * Initializes the instance with a system property name.
	 * 
	 * @param name
	 *            name of the system property that is wrapped by this instance.
	 *            Must not be <code>null</code> nor empty.
	 */
	public IntegerSystemProperty(final @NotNull String name)
	{
		super(name);
		
	}
	

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Integer getValue()
	{
		return Integer.getInteger(this.getName());
	}
	

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Integer getValue(final Integer defaultValue)
	{
		return Integer.getInteger(getName(),defaultValue);
	}
	
}
