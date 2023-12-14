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
 * 
 * Utility class to get system properties in casted object typ. Uses the
 * implementations of {@link SystemProperty}
 * 
 * @author XDEV Software (MP)
 * @since 4.0
 * 
 */
public class XdevSystemPropertyUtils
{
	
	/**
	 * Determinates the value of the system property.
	 * 
	 * @see {@link StringSystemProperty}
	 * 
	 * @param key
	 *            for system property
	 * @return string value of the property. <code>null</code> if the specified
	 *         system property was not found or the value could not be parsed.
	 */
	public static String getStringValue(String key)
	{
		StringSystemProperty stringSystemProperty = new StringSystemProperty(key);
		return stringSystemProperty.getValue();
	}
	
	
	/**
	 * Determinates the value of the system property.
	 * 
	 * @see {@link StringSystemProperty}
	 * 
	 * @param key
	 *            for system property
	 * @param defaultValue
	 * @return string value of the property. <code>defaultValue</code> if the
	 *         specified system property was not found or the value could not be
	 *         parsed.
	 */
	public static String getStringValue(String key, String defaultValue)
	{
		StringSystemProperty stringSystemProperty = new StringSystemProperty(key);
		return stringSystemProperty.getValue(defaultValue);
	}
	
	
	/**
	 * Determinates the value of the system property.
	 * 
	 * @see {@link BooleanSystemProperty}
	 * 
	 * @param key
	 *            for system property
	 * @return boolean value of the property. <code>null</code> if the specified
	 *         system property was not found or the value could not be parsed.
	 */
	public static Boolean getBooleanValue(String key)
	{
		BooleanSystemProperty booleanSystemProperty = new BooleanSystemProperty(key);
		return booleanSystemProperty.getValue();
	}
	
	
	/**
	 * Determinates the value of the system property.
	 * 
	 * @see {@link BooleanSystemProperty}
	 * 
	 * @param key
	 *            for system property
	 * @param defaultValue
	 * @return boolean value of the property. <code>defaultValue</code> if the
	 *         specified system property was not found or the value could not be
	 *         parsed.
	 */
	public static Boolean getBooleanValue(String key, Boolean defaultValue)
	{
		BooleanSystemProperty booleanSystemProperty = new BooleanSystemProperty(key);
		return booleanSystemProperty.getValue(defaultValue);
	}
	
	
	/**
	 * Determinates the value of the system property.
	 * 
	 * @see {@link IntegerSystemProperty}
	 * 
	 * @param key
	 *            for system property
	 * @return integer value of the property. <code>null</code> if the specified
	 *         system property was not found or the value could not be parsed.
	 */
	public static Integer getIntegerValue(String key)
	{
		IntegerSystemProperty integerSystemProperty = new IntegerSystemProperty(key);
		return integerSystemProperty.getValue();
	}
	
	
	/**
	 * Determinates the value of the system property.
	 * 
	 * @see {@link IntegerSystemProperty}
	 * 
	 * @param key
	 *            for system property
	 * @param defaultValue
	 * @return integer value of the property. <code>defaultValue</code> if the
	 *         specified system property was not found or the value could not be
	 *         parsed.
	 */
	public static Integer getIntegerValue(String key, int defaultValue)
	{
		IntegerSystemProperty integerSystemProperty = new IntegerSystemProperty(key);
		return integerSystemProperty.getValue(defaultValue);
	}
	
	
	/**
	 * Determinates the value of the system property.
	 * 
	 * @see {@link LongSystemProperty}
	 * 
	 * @param key
	 *            for system property
	 * @return long value of the property. <code>null</code> if the specified
	 *         system property was not found or the value could not be parsed.
	 */
	public static long getLongValue(String key)
	{
		LongSystemProperty longSystemProperty = new LongSystemProperty(key);
		return longSystemProperty.getValue();
	}
	
	
	/**
	 * Determinates the value of the system property.
	 * 
	 * @see {@link LongSystemProperty}
	 * 
	 * @param key
	 *            for system property
	 * @param defaultValue
	 * @return long value of the property. <code>defaultValue</code> if the
	 *         specified system property was not found or the value could not be
	 *         parsed.
	 */
	public static long getLongValue(String key, long defaultValue)
	{
		LongSystemProperty longSystemProperty = new LongSystemProperty(key);
		return longSystemProperty.getValue(defaultValue);
	}
	
}
