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
package xdev.util.systemproperty;


/**
 * This class provides implementation of the {@link SystemProperty} interface
 * for an {@link String} type.
 * 
 * 
 * @author XDEV Software (RHHF)
 * @since 3.2
 * 
 */
public class StringSystemProperty extends AbstractSystemProperty<String>
{
	/**
	 * Initializes the instance with a system property name.
	 * 
	 * @param name
	 *            name of the system property that is wrapped by this instance.
	 *            Must not be <code>null</code> nor empty.
	 */
	public StringSystemProperty(String name)
	{
		super(name);
	}
	

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getValue()
	{
		return System.getProperty(getName());
		
	}
	

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getValue(String defaultValue)
	{
		final String value = this.getValue();
		if(value == null)
		{
			return defaultValue;
		}
		else
		{
			return value;
		}
	}
	
}
