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
package xdev.util;


import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.lang.reflect.Array;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Hashtable;
import java.util.Map;

import javax.swing.Icon;

import xdev.db.DBException;
import xdev.io.ByteHolder;
import xdev.io.CharHolder;
import xdev.io.IOUtils;
import xdev.io.XdevFile;
import xdev.lang.LibraryMember;
import xdev.lang.NotNull;
import xdev.ui.GraphicUtils;
import xdev.ui.XdevImage;
import xdev.vt.XdevBlob;
import xdev.vt.XdevClob;


/**
 * A utility class which converts Object's into specific types.
 * <p>
 * This is done via registered {@link ObjectConverter}'s. See
 * {@link #setObjectConverter(Class, ObjectConverter)},
 * {@link #getObjectConverter(Class)}.
 * <p>
 * The {@link #convert(Object, Class)} method forwards the request to the
 * registered {@link ObjectConverter} and returns the converted value, If the
 * conversion fails a {@link ObjectConversionException} is thrown.
 * <p>
 * Alternatively you can use {@link #convert(Object, Class, Object)} wich uses a
 * fallback instead of the {@link ObjectConversionException}.
 * <p>
 * Default converters are installed for the following types:
 * <ul>
 * <li>byte resp. Byte</li>
 * <li>short resp. Short</li>
 * <li>int resp. Integer</li>
 * <li>long resp. Long</li>
 * <li>float resp. Float</li>
 * <li>double resp. Double</li>
 * <li>boolean resp. Boolean</li>
 * <li>char resp. Character</li>
 * <li>String</li>
 * <li>Date</li>
 * <li>XdevDate</li>
 * <li>XdevBlob</li>
 * <li>byte[]</li>
 * <li>XdevClob</li>
 * <li>char[]</li>
 * <li>XdevList</li>
 * <li>XdevHashtable</li>
 * <li>XdevFile</li>
 * <li>XdevImage</li>
 * </ul>
 * There are convenience methods for the preinstalled converters too, like:<br>
 * {@link #toint(Object)} or {@link #toInteger(Object)}.
 * 
 * @author XDEV Software
 * @since 3.1
 */
@LibraryMember
public final class ConversionUtils
{
	private ConversionUtils()
	{
	}
	
	private static Map<Class, ObjectConverter>	objectConverter	= new Hashtable();
	static
	{
		setObjectConverter(Byte.class,new ByteConverter());
		setObjectConverter(Short.class,new ShortConverter());
		setObjectConverter(Integer.class,new IntegerConverter());
		setObjectConverter(Long.class,new LongConverter());
		setObjectConverter(Float.class,new FloatConverter());
		setObjectConverter(Double.class,new DoubleConverter());
		setObjectConverter(Boolean.class,new BooleanConverter());
		setObjectConverter(Character.class,new CharacterConverter());
		setObjectConverter(String.class,new StringConverter());
		setObjectConverter(Date.class,new DateConverter());
		setObjectConverter(XdevDate.class,new XdevDateConverter());
		setObjectConverter(XdevBlob.class,new XdevBlobConverter());
		setObjectConverter(byte[].class,new BytesConverter());
		setObjectConverter(XdevClob.class,new XdevClobConverter());
		setObjectConverter(char[].class,new CharsConverter());
		setObjectConverter(XdevList.class,new XdevListConverter());
		setObjectConverter(XdevHashtable.class,new XdevHashtableConverter());
		setObjectConverter(XdevFile.class,new XdevFileConverter());
		setObjectConverter(XdevImage.class,new XdevImageConverter());
	}
	
	
	/**
	 * Registeres an ObjectConverter for the type T.
	 * 
	 * @param <T>
	 * @param type
	 *            the registered type
	 * @param converter
	 *            the new converter
	 */
	public static <T> void setObjectConverter(Class<T> type, ObjectConverter<T> converter)
	{
		objectConverter.put(type,converter);
	}
	
	
	/**
	 * Returns the registered ObjectConverter for <code>type</code>, or
	 * <code>null</code> if none is registered.
	 * 
	 * @param <T>
	 * @param type
	 *            the conversion type
	 * @return the registered ObjectConverter or <code>null</code>
	 */
	public static <T> ObjectConverter<T> getObjectConverter(Class<T> type)
	{
		return objectConverter.get(type);
	}
	
	
	/**
	 * Converts <code>value</code> to <code>type</code> via the registered
	 * {@link ObjectConverter}.
	 * <p>
	 * If no converter is registered for <code>type</code> or the conversion
	 * fails an {@link ObjectConversionException} is thrown.
	 * 
	 * @param <T>
	 * @param value
	 *            the value to convert
	 * @param type
	 *            the target type
	 * @return the converted value
	 * @throws ObjectConversionException
	 *             if no converter is registered for <code>type</code> or the
	 *             conversion fails
	 */
	public static <T> T convert(Object value, Class<T> type) throws ObjectConversionException
	{
		ObjectConverter<T> converter = getObjectConverter(type);
		if(converter == null)
		{
			throw new ObjectConversionException("No converter found for type " + type.getName());
		}
		
		return converter.convert(value);
	}
	
	
	/**
	 * Converts <code>value</code> to <code>type</code> via the registered
	 * {@link ObjectConverter}.
	 * <p>
	 * If no converter is registered for <code>type</code> or the conversion
	 * fails <code>fallback</code> is returned.
	 * 
	 * @param <T>
	 * @param value
	 *            the value to convert
	 * @param type
	 *            the target type
	 * @param fallback
	 *            the fallback value
	 * @return the converted value or <code>fallback</code>
	 */
	public static <T> T convert(Object value, Class<T> type, T fallback)
	{
		try
		{
			return convert(value,type);
		}
		catch(ObjectConversionException e)
		{
			return fallback;
		}
	}
	
	
	// *******************************************************************************
	// Convenience Methods
	// *******************************************************************************
	
	/**
	 * Converts <code>value</code> to a byte primitive.
	 * <p>
	 * If value is <code>null</code> or the conversion fails an
	 * {@link ObjectConversionException} is thrown.
	 * 
	 * @param value
	 *            the value to convert
	 * @return a byte primitive
	 * @throws ObjectConversionException
	 *             If value is <code>null</code> or the conversion fails
	 */
	public static byte tobyte(@NotNull Object value) throws ObjectConversionException
	{
		if(value == null)
		{
			throw new ObjectConversionException(new NullPointerException());
		}
		
		return toByte(value).byteValue();
	}
	
	
	/**
	 * Converts <code>value</code> to a byte primitive.
	 * <p>
	 * If value is <code>null</code> or the conversion fails
	 * <code>fallback</code> is returned.
	 * 
	 * @param value
	 *            the value to convert
	 * @param fallback
	 *            the fallback value
	 * @return a byte primitive or <code>fallback</code>
	 */
	public static byte tobyte(Object value, byte fallback)
	{
		try
		{
			return tobyte(value);
		}
		catch(ObjectConversionException e)
		{
			return fallback;
		}
	}
	
	
	/**
	 * Converts <code>value</code> to a {@link Byte}.
	 * <p>
	 * If the conversion fails an {@link ObjectConversionException} is thrown.
	 * 
	 * @param value
	 *            the value to convert
	 * @return a Byte, or <code>null</code> if <code>value</code> is
	 *         <code>null</code>
	 * @throws ObjectConversionException
	 *             If the conversion fails
	 */
	public static Byte toByte(Object value) throws ObjectConversionException
	{
		return convert(value,Byte.class);
	}
	
	
	/**
	 * Converts <code>value</code> to a {@link Byte}.
	 * <p>
	 * If the conversion fails <code>fallback</code> is returned.
	 * 
	 * @param value
	 *            the value to convert
	 * @param fallback
	 *            the fallback value
	 * @return a Byte, or <code>fallback</code> if the conversion fails
	 */
	public static Byte toByte(Object value, Byte fallback)
	{
		try
		{
			return toByte(value);
		}
		catch(ObjectConversionException e)
		{
			return fallback;
		}
	}
	
	
	/**
	 * Converts <code>value</code> to a short primitive.
	 * <p>
	 * If value is <code>null</code> or the conversion fails an
	 * {@link ObjectConversionException} is thrown.
	 * 
	 * @param value
	 *            the value to convert
	 * @return a short primitive
	 * @throws ObjectConversionException
	 *             If value is <code>null</code> or the conversion fails
	 */
	public static short toshort(@NotNull Object value) throws ObjectConversionException
	{
		if(value == null)
		{
			throw new ObjectConversionException(new NullPointerException());
		}
		
		return toShort(value).shortValue();
	}
	
	
	/**
	 * Converts <code>value</code> to a short primitive.
	 * <p>
	 * If value is <code>null</code> or the conversion fails
	 * <code>fallback</code> is returned.
	 * 
	 * @param value
	 *            the value to convert
	 * @param fallback
	 *            the fallback value
	 * @return a short primitive or <code>fallback</code>
	 */
	public static short toshort(Object value, short fallback)
	{
		try
		{
			return toshort(value);
		}
		catch(ObjectConversionException e)
		{
			return fallback;
		}
	}
	
	
	/**
	 * Converts <code>value</code> to a {@link Short}.
	 * <p>
	 * If the conversion fails an {@link ObjectConversionException} is thrown.
	 * 
	 * @param value
	 *            the value to convert
	 * @return a Short, or <code>null</code> if <code>value</code> is
	 *         <code>null</code>
	 * @throws ObjectConversionException
	 *             If the conversion fails
	 */
	public static Short toShort(Object value) throws ObjectConversionException
	{
		return convert(value,Short.class);
	}
	
	
	/**
	 * Converts <code>value</code> to a {@link Short}.
	 * <p>
	 * If the conversion fails <code>fallback</code> is returned.
	 * 
	 * @param value
	 *            the value to convert
	 * @param fallback
	 *            the fallback value
	 * @return a Short, or <code>fallback</code> if the conversion fails
	 */
	public static Short toShort(Object value, Short fallback)
	{
		try
		{
			return toShort(value);
		}
		catch(ObjectConversionException e)
		{
			return fallback;
		}
	}
	
	
	/**
	 * Converts <code>value</code> to an int primitive.
	 * <p>
	 * If value is <code>null</code> or the conversion fails an
	 * {@link ObjectConversionException} is thrown.
	 * 
	 * @param value
	 *            the value to convert
	 * @return an int primitive
	 * @throws ObjectConversionException
	 *             If value is <code>null</code> or the conversion fails
	 */
	public static int toint(@NotNull Object value) throws ObjectConversionException
	{
		if(value == null)
		{
			throw new ObjectConversionException(new NullPointerException());
		}
		
		return toInteger(value).intValue();
	}
	
	
	/**
	 * Converts <code>value</code> to an int primitive.
	 * <p>
	 * If value is <code>null</code> or the conversion fails
	 * <code>fallback</code> is returned.
	 * 
	 * @param value
	 *            the value to convert
	 * @param fallback
	 *            the fallback value
	 * @return an int primitive or <code>fallback</code>
	 */
	public static int toint(Object value, int fallback)
	{
		try
		{
			return toint(value);
		}
		catch(ObjectConversionException e)
		{
			return fallback;
		}
	}
	
	
	/**
	 * Converts <code>value</code> to an {@link Integer}.
	 * <p>
	 * If the conversion fails an {@link ObjectConversionException} is thrown.
	 * 
	 * @param value
	 *            the value to convert
	 * @return a Integer, or <code>null</code> if <code>value</code> is
	 *         <code>null</code>
	 * @throws ObjectConversionException
	 *             If the conversion fails
	 */
	public static Integer toInteger(Object value) throws ObjectConversionException
	{
		return convert(value,Integer.class);
	}
	
	
	/**
	 * Converts <code>value</code> to an {@link Integer}.
	 * <p>
	 * If the conversion fails <code>fallback</code> is returned.
	 * 
	 * @param value
	 *            the value to convert
	 * @param fallback
	 *            the fallback value
	 * @return an Integer, or <code>fallback</code> if the conversion fails
	 */
	public static Integer toInteger(Object value, Integer fallback)
	{
		try
		{
			return toInteger(value);
		}
		catch(ObjectConversionException e)
		{
			return fallback;
		}
	}
	
	
	/**
	 * Converts <code>value</code> to a long primitive.
	 * <p>
	 * If value is <code>null</code> or the conversion fails an
	 * {@link ObjectConversionException} is thrown.
	 * 
	 * @param value
	 *            the value to convert
	 * @return a long primitive
	 * @throws ObjectConversionException
	 *             If value is <code>null</code> or the conversion fails
	 */
	public static long tolong(@NotNull Object value) throws ObjectConversionException
	{
		if(value == null)
		{
			throw new ObjectConversionException(new NullPointerException());
		}
		
		return toLong(value).longValue();
	}
	
	
	/**
	 * Converts <code>value</code> to a long primitive.
	 * <p>
	 * If value is <code>null</code> or the conversion fails
	 * <code>fallback</code> is returned.
	 * 
	 * @param value
	 *            the value to convert
	 * @param fallback
	 *            the fallback value
	 * @return a long primitive or <code>fallback</code>
	 */
	public static long tolong(Object value, long fallback)
	{
		try
		{
			return tolong(value);
		}
		catch(ObjectConversionException e)
		{
			return fallback;
		}
	}
	
	
	/**
	 * Converts <code>value</code> to a {@link Long}.
	 * <p>
	 * If the conversion fails an {@link ObjectConversionException} is thrown.
	 * 
	 * @param value
	 *            the value to convert
	 * @return a Long, or <code>null</code> if <code>value</code> is
	 *         <code>null</code>
	 * @throws ObjectConversionException
	 *             If the conversion fails
	 */
	public static Long toLong(Object value) throws ObjectConversionException
	{
		return convert(value,Long.class);
	}
	
	
	/**
	 * Converts <code>value</code> to a {@link Long}.
	 * <p>
	 * If the conversion fails <code>fallback</code> is returned.
	 * 
	 * @param value
	 *            the value to convert
	 * @param fallback
	 *            the fallback value
	 * @return a Long, or <code>fallback</code> if the conversion fails
	 */
	public static Long toLong(Object value, Long fallback)
	{
		try
		{
			return toLong(value);
		}
		catch(ObjectConversionException e)
		{
			return fallback;
		}
	}
	
	
	/**
	 * Converts <code>value</code> to a float primitive.
	 * <p>
	 * If value is <code>null</code> or the conversion fails an
	 * {@link ObjectConversionException} is thrown.
	 * 
	 * @param value
	 *            the value to convert
	 * @return a float primitive
	 * @throws ObjectConversionException
	 *             If value is <code>null</code> or the conversion fails
	 */
	public static float tofloat(@NotNull Object value) throws ObjectConversionException
	{
		if(value == null)
		{
			throw new ObjectConversionException(new NullPointerException());
		}
		
		return toFloat(value).floatValue();
	}
	
	
	/**
	 * Converts <code>value</code> to a float primitive.
	 * <p>
	 * If value is <code>null</code> or the conversion fails
	 * <code>fallback</code> is returned.
	 * 
	 * @param value
	 *            the value to convert
	 * @param fallback
	 *            the fallback value
	 * @return a float primitive or <code>fallback</code>
	 */
	public static float tofloat(Object value, float fallback)
	{
		try
		{
			return tofloat(value);
		}
		catch(ObjectConversionException e)
		{
			return fallback;
		}
	}
	
	
	/**
	 * Converts <code>value</code> to a {@link Float}.
	 * <p>
	 * If the conversion fails an {@link ObjectConversionException} is thrown.
	 * 
	 * @param value
	 *            the value to convert
	 * @return a Float, or <code>null</code> if <code>value</code> is
	 *         <code>null</code>
	 * @throws ObjectConversionException
	 *             If the conversion fails
	 */
	public static Float toFloat(Object value) throws ObjectConversionException
	{
		return convert(value,Float.class);
	}
	
	
	/**
	 * Converts <code>value</code> to a {@link Float}.
	 * <p>
	 * If the conversion fails <code>fallback</code> is returned.
	 * 
	 * @param value
	 *            the value to convert
	 * @param fallback
	 *            the fallback value
	 * @return a Float, or <code>fallback</code> if the conversion fails
	 */
	public static Float toFloat(Object value, Float fallback)
	{
		try
		{
			return toFloat(value);
		}
		catch(ObjectConversionException e)
		{
			return fallback;
		}
	}
	
	
	/**
	 * Converts <code>value</code> to a double primitive.
	 * <p>
	 * If value is <code>null</code> or the conversion fails an
	 * {@link ObjectConversionException} is thrown.
	 * 
	 * @param value
	 *            the value to convert
	 * @return a double primitive
	 * @throws ObjectConversionException
	 *             If value is <code>null</code> or the conversion fails
	 */
	public static double todouble(@NotNull Object value) throws ObjectConversionException
	{
		if(value == null)
		{
			throw new ObjectConversionException(new NullPointerException());
		}
		
		return toDouble(value).doubleValue();
	}
	
	
	/**
	 * Converts <code>value</code> to a double primitive.
	 * <p>
	 * If value is <code>null</code> or the conversion fails
	 * <code>fallback</code> is returned.
	 * 
	 * @param value
	 *            the value to convert
	 * @param fallback
	 *            the fallback value
	 * @return a double primitive or <code>fallback</code>
	 */
	public static double todouble(Object value, double fallback)
	{
		try
		{
			return todouble(value);
		}
		catch(ObjectConversionException e)
		{
			return fallback;
		}
	}
	
	
	/**
	 * Converts <code>value</code> to a {@link Double}.
	 * <p>
	 * If the conversion fails an {@link ObjectConversionException} is thrown.
	 * 
	 * @param value
	 *            the value to convert
	 * @return a Double, or <code>null</code> if <code>value</code> is
	 *         <code>null</code>
	 * @throws ObjectConversionException
	 *             If the conversion fails
	 */
	public static Double toDouble(Object value) throws ObjectConversionException
	{
		return convert(value,Double.class);
	}
	
	
	/**
	 * Converts <code>value</code> to a {@link Double}.
	 * <p>
	 * If the conversion fails <code>fallback</code> is returned.
	 * 
	 * @param value
	 *            the value to convert
	 * @param fallback
	 *            the fallback value
	 * @return a Double, or <code>fallback</code> if the conversion fails
	 */
	public static Double toDouble(Object value, Double fallback)
	{
		try
		{
			return toDouble(value);
		}
		catch(ObjectConversionException e)
		{
			return fallback;
		}
	}
	
	
	/**
	 * Converts <code>value</code> to a boolean primitive.
	 * <p>
	 * If value is <code>null</code> or the conversion fails an
	 * {@link ObjectConversionException} is thrown.
	 * 
	 * @param value
	 *            the value to convert
	 * @return a boolean primitive
	 * @throws ObjectConversionException
	 *             If value is <code>null</code> or the conversion fails
	 */
	public static boolean toboolean(@NotNull Object value) throws ObjectConversionException
	{
		if(value == null)
		{
			throw new ObjectConversionException(new NullPointerException());
		}
		
		return toBoolean(value).booleanValue();
	}
	
	
	/**
	 * Converts <code>value</code> to a boolean primitive.
	 * <p>
	 * If value is <code>null</code> or the conversion fails
	 * <code>fallback</code> is returned.
	 * 
	 * @param value
	 *            the value to convert
	 * @param fallback
	 *            the fallback value
	 * @return a boolean primitive or <code>fallback</code>
	 */
	public static boolean toboolean(Object value, boolean fallback)
	{
		try
		{
			return toboolean(value);
		}
		catch(ObjectConversionException e)
		{
			return fallback;
		}
	}
	
	
	/**
	 * Converts <code>value</code> to a {@link Boolean}.
	 * <p>
	 * If the conversion fails an {@link ObjectConversionException} is thrown.
	 * 
	 * @param value
	 *            the value to convert
	 * @return a Boolean, or <code>null</code> if <code>value</code> is
	 *         <code>null</code>
	 * @throws ObjectConversionException
	 *             If the conversion fails
	 */
	public static Boolean toBoolean(Object value) throws ObjectConversionException
	{
		return convert(value,Boolean.class);
	}
	
	
	/**
	 * Converts <code>value</code> to a {@link Boolean}.
	 * <p>
	 * If the conversion fails <code>fallback</code> is returned.
	 * 
	 * @param value
	 *            the value to convert
	 * @param fallback
	 *            the fallback value
	 * @return a Boolean, or <code>fallback</code> if the conversion fails
	 */
	public static Boolean toBoolean(Object value, Boolean fallback)
	{
		try
		{
			return toBoolean(value);
		}
		catch(ObjectConversionException e)
		{
			return fallback;
		}
	}
	
	
	/**
	 * Converts <code>value</code> to a char primitive.
	 * <p>
	 * If value is <code>null</code> or the conversion fails an
	 * {@link ObjectConversionException} is thrown.
	 * 
	 * @param value
	 *            the value to convert
	 * @return a char primitive
	 * @throws ObjectConversionException
	 *             If value is <code>null</code> or the conversion fails
	 */
	public static char tochar(@NotNull Object value) throws ObjectConversionException
	{
		if(value == null)
		{
			throw new ObjectConversionException(new NullPointerException());
		}
		
		return toCharacter(value).charValue();
	}
	
	
	/**
	 * Converts <code>value</code> to a char primitive.
	 * <p>
	 * If value is <code>null</code> or the conversion fails
	 * <code>fallback</code> is returned.
	 * 
	 * @param value
	 *            the value to convert
	 * @param fallback
	 *            the fallback value
	 * @return a char primitive or <code>fallback</code>
	 */
	public static char tochar(Object value, char fallback)
	{
		try
		{
			return tochar(value);
		}
		catch(ObjectConversionException e)
		{
			return fallback;
		}
	}
	
	
	/**
	 * Converts <code>value</code> to a {@link Character}.
	 * <p>
	 * If the conversion fails an {@link ObjectConversionException} is thrown.
	 * 
	 * @param value
	 *            the value to convert
	 * @return a Character, or <code>null</code> if <code>value</code> is
	 *         <code>null</code>
	 * @throws ObjectConversionException
	 *             If the conversion fails
	 */
	public static Character toCharacter(Object value) throws ObjectConversionException
	{
		return convert(value,Character.class);
	}
	
	
	/**
	 * Converts <code>value</code> to a {@link Character}.
	 * <p>
	 * If the conversion fails <code>fallback</code> is returned.
	 * 
	 * @param value
	 *            the value to convert
	 * @param fallback
	 *            the fallback value
	 * @return a Character, or <code>fallback</code> if the conversion fails
	 */
	public static Character toCharacter(Object value, Character fallback)
	{
		try
		{
			return toCharacter(value);
		}
		catch(ObjectConversionException e)
		{
			return fallback;
		}
	}
	
	
	/**
	 * Converts <code>value</code> to a {@link String}.
	 * <p>
	 * If the conversion fails an {@link ObjectConversionException} is thrown.
	 * 
	 * @param value
	 *            the value to convert
	 * @return a String, or <code>null</code> if <code>value</code> is
	 *         <code>null</code>
	 * @throws ObjectConversionException
	 *             If the conversion fails
	 */
	public static String toString(Object value) throws ObjectConversionException
	{
		return convert(value,String.class);
	}
	
	
	/**
	 * Converts <code>value</code> to a {@link String}.
	 * <p>
	 * If the conversion fails <code>fallback</code> is returned.
	 * 
	 * @param value
	 *            the value to convert
	 * @param fallback
	 *            the fallback value
	 * @return a String, or <code>fallback</code> if the conversion fails
	 */
	public static String toString(Object value, String fallback)
	{
		try
		{
			return toString(value);
		}
		catch(ObjectConversionException e)
		{
			return fallback;
		}
	}
	
	
	/**
	 * Converts <code>value</code> to a {@link Date}.
	 * <p>
	 * If the conversion fails an {@link ObjectConversionException} is thrown.
	 * 
	 * @param value
	 *            the value to convert
	 * @return a Date, or <code>null</code> if <code>value</code> is
	 *         <code>null</code>
	 * @throws ObjectConversionException
	 *             If the conversion fails
	 */
	public static Date toDate(Object value) throws ObjectConversionException
	{
		return convert(value,Date.class);
	}
	
	
	/**
	 * Converts <code>value</code> to a {@link Date}.
	 * <p>
	 * If the conversion fails <code>fallback</code> is returned.
	 * 
	 * @param value
	 *            the value to convert
	 * @param fallback
	 *            the fallback value
	 * @return a Date, or <code>fallback</code> if the conversion fails
	 */
	public static Date toDate(Object value, Date fallback)
	{
		try
		{
			return toDate(value);
		}
		catch(ObjectConversionException e)
		{
			return fallback;
		}
	}
	
	
	/**
	 * Converts <code>value</code> to a {@link XdevDate}.
	 * <p>
	 * If the conversion fails an {@link ObjectConversionException} is thrown.
	 * 
	 * @param value
	 *            the value to convert
	 * @return a XdevDate, or <code>null</code> if <code>value</code> is
	 *         <code>null</code>
	 * @throws ObjectConversionException
	 *             If the conversion fails
	 */
	public static XdevDate toXdevDate(Object value) throws ObjectConversionException
	{
		return convert(value,XdevDate.class);
	}
	
	
	/**
	 * Converts <code>value</code> to a {@link XdevDate}.
	 * <p>
	 * If the conversion fails <code>fallback</code> is returned.
	 * 
	 * @param value
	 *            the value to convert
	 * @param fallback
	 *            the fallback value
	 * @return a XdevDate, or <code>fallback</code> if the conversion fails
	 */
	public static XdevDate toXdevDate(Object value, XdevDate fallback)
	{
		try
		{
			return toXdevDate(value);
		}
		catch(ObjectConversionException e)
		{
			return fallback;
		}
	}
	
	
	/**
	 * Converts <code>value</code> to a {@link XdevBlob}.
	 * <p>
	 * If the conversion fails an {@link ObjectConversionException} is thrown.
	 * 
	 * @param value
	 *            the value to convert
	 * @return a XdevBlob, or <code>null</code> if <code>value</code> is
	 *         <code>null</code>
	 * @throws ObjectConversionException
	 *             If the conversion fails
	 */
	public static XdevBlob toXdevBlob(Object value) throws ObjectConversionException
	{
		return convert(value,XdevBlob.class);
	}
	
	
	/**
	 * Converts <code>value</code> to a {@link XdevBlob}.
	 * <p>
	 * If the conversion fails <code>fallback</code> is returned.
	 * 
	 * @param value
	 *            the value to convert
	 * @param fallback
	 *            the fallback value
	 * @return a XdevBlob, or <code>fallback</code> if the conversion fails
	 */
	public static XdevBlob toXdevBlob(Object value, XdevBlob fallback)
	{
		try
		{
			return toXdevBlob(value);
		}
		catch(ObjectConversionException e)
		{
			return fallback;
		}
	}
	
	
	/**
	 * Converts <code>value</code> to a byte[] array.
	 * <p>
	 * If the conversion fails an {@link ObjectConversionException} is thrown.
	 * 
	 * @param value
	 *            the value to convert
	 * @return a byte[] array, or <code>null</code> if <code>value</code> is
	 *         <code>null</code>
	 * @throws ObjectConversionException
	 *             If the conversion fails
	 */
	public static byte[] toBytes(Object value) throws ObjectConversionException
	{
		return convert(value,byte[].class);
	}
	
	
	/**
	 * Converts <code>value</code> to a byte[] array.
	 * <p>
	 * If the conversion fails <code>fallback</code> is returned.
	 * 
	 * @param value
	 *            the value to convert
	 * @param fallback
	 *            the fallback value
	 * @return a byte[] array, or <code>fallback</code> if the conversion fails
	 */
	public static byte[] toBytes(Object value, byte[] fallback)
	{
		try
		{
			return toBytes(value);
		}
		catch(ObjectConversionException e)
		{
			return fallback;
		}
	}
	
	
	/**
	 * Converts <code>value</code> to a {@link XdevClob}.
	 * <p>
	 * If the conversion fails an {@link ObjectConversionException} is thrown.
	 * 
	 * @param value
	 *            the value to convert
	 * @return a XdevClob, or <code>null</code> if <code>value</code> is
	 *         <code>null</code>
	 * @throws ObjectConversionException
	 *             If the conversion fails
	 */
	public static XdevClob toXdevClob(Object value) throws ObjectConversionException
	{
		return convert(value,XdevClob.class);
	}
	
	
	/**
	 * Converts <code>value</code> to a {@link XdevClob}.
	 * <p>
	 * If the conversion fails <code>fallback</code> is returned.
	 * 
	 * @param value
	 *            the value to convert
	 * @param fallback
	 *            the fallback value
	 * @return a XdevClob, or <code>fallback</code> if the conversion fails
	 */
	public static XdevClob toXdevClob(Object value, XdevClob fallback)
	{
		try
		{
			return toXdevClob(value);
		}
		catch(ObjectConversionException e)
		{
			return fallback;
		}
	}
	
	
	/**
	 * Converts <code>value</code> to a char[] array.
	 * <p>
	 * If the conversion fails an {@link ObjectConversionException} is thrown.
	 * 
	 * @param value
	 *            the value to convert
	 * @return a char[] array, or <code>null</code> if <code>value</code> is
	 *         <code>null</code>
	 * @throws ObjectConversionException
	 *             If the conversion fails
	 */
	public static char[] toChars(Object value) throws ObjectConversionException
	{
		return convert(value,char[].class);
	}
	
	
	/**
	 * Converts <code>value</code> to a char[] array.
	 * <p>
	 * If the conversion fails <code>fallback</code> is returned.
	 * 
	 * @param value
	 *            the value to convert
	 * @param fallback
	 *            the fallback value
	 * @return a char[] array, or <code>fallback</code> if the conversion fails
	 */
	public static char[] toChars(Object value, char[] fallback)
	{
		try
		{
			return toChars(value);
		}
		catch(ObjectConversionException e)
		{
			return fallback;
		}
	}
	
	
	/**
	 * Converts <code>value</code> to a {@link XdevList}.
	 * <p>
	 * If the conversion fails an {@link ObjectConversionException} is thrown.
	 * 
	 * @param value
	 *            the value to convert
	 * @return a XdevList, or <code>null</code> if <code>value</code> is
	 *         <code>null</code>
	 * @throws ObjectConversionException
	 *             If the conversion fails
	 */
	public static XdevList toXdevList(Object value) throws ObjectConversionException
	{
		return convert(value,XdevList.class);
	}
	
	
	/**
	 * Converts <code>value</code> to a {@link XdevList}.
	 * <p>
	 * If the conversion fails <code>fallback</code> is returned.
	 * 
	 * @param value
	 *            the value to convert
	 * @param fallback
	 *            the fallback value
	 * @return a XdevList, or <code>fallback</code> if the conversion fails
	 */
	public static XdevList toXdevList(Object value, XdevList fallback)
	{
		try
		{
			return toXdevList(value);
		}
		catch(ObjectConversionException e)
		{
			return fallback;
		}
	}
	
	
	/**
	 * Converts <code>value</code> to a {@link XdevHashtable}.
	 * <p>
	 * If the conversion fails an {@link ObjectConversionException} is thrown.
	 * 
	 * @param value
	 *            the value to convert
	 * @return a XdevHashtable, or <code>null</code> if <code>value</code> is
	 *         <code>null</code>
	 * @throws ObjectConversionException
	 *             If the conversion fails
	 */
	public static XdevHashtable toXdevHashtable(Object value) throws ObjectConversionException
	{
		return convert(value,XdevHashtable.class);
	}
	
	
	/**
	 * Converts <code>value</code> to a {@link XdevHashtable}.
	 * <p>
	 * If the conversion fails <code>fallback</code> is returned.
	 * 
	 * @param value
	 *            the value to convert
	 * @param fallback
	 *            the fallback value
	 * @return a XdevHashtable, or <code>fallback</code> if the conversion fails
	 */
	public static XdevHashtable toXdevHashtable(Object value, XdevHashtable fallback)
	{
		try
		{
			return toXdevHashtable(value);
		}
		catch(ObjectConversionException e)
		{
			return fallback;
		}
	}
	
	
	/**
	 * Converts <code>value</code> to a {@link XdevFile}.
	 * <p>
	 * If the conversion fails an {@link ObjectConversionException} is thrown.
	 * 
	 * @param value
	 *            the value to convert
	 * @return a XdevFile, or <code>null</code> if <code>value</code> is
	 *         <code>null</code>
	 * @throws ObjectConversionException
	 *             If the conversion fails
	 */
	public static XdevFile toXdevFile(Object value) throws ObjectConversionException
	{
		return convert(value,XdevFile.class);
	}
	
	
	/**
	 * Converts <code>value</code> to a {@link XdevFile}.
	 * <p>
	 * If the conversion fails <code>fallback</code> is returned.
	 * 
	 * @param value
	 *            the value to convert
	 * @param fallback
	 *            the fallback value
	 * @return a XdevFile, or <code>fallback</code> if the conversion fails
	 */
	public static XdevFile toXdevFile(Object value, XdevFile fallback)
	{
		try
		{
			return toXdevFile(value);
		}
		catch(ObjectConversionException e)
		{
			return fallback;
		}
	}
	
	
	/**
	 * Converts <code>value</code> to a {@link XdevImage}.
	 * <p>
	 * If the conversion fails an {@link ObjectConversionException} is thrown.
	 * 
	 * @param value
	 *            the value to convert
	 * @return a XdevImage, or <code>null</code> if <code>value</code> is
	 *         <code>null</code>
	 * @throws ObjectConversionException
	 *             If the conversion fails
	 */
	public static XdevImage toXdevImage(Object value) throws ObjectConversionException
	{
		return convert(value,XdevImage.class);
	}
	
	
	/**
	 * Converts <code>value</code> to a {@link XdevImage}.
	 * <p>
	 * If the conversion fails <code>fallback</code> is returned.
	 * 
	 * @param value
	 *            the value to convert
	 * @param fallback
	 *            the fallback value
	 * @return a XdevImage, or <code>fallback</code> if the conversion fails
	 */
	public static XdevImage toXdevImage(Object value, XdevImage fallback)
	{
		try
		{
			return toXdevImage(value);
		}
		catch(ObjectConversionException e)
		{
			return fallback;
		}
	}
	
	// *******************************************************************************
	// Default Implementations
	// *******************************************************************************
	
	private final static NumberFormat	integerFormat	= NumberFormat.getIntegerInstance();
	private final static NumberFormat	decimalFormat	= NumberFormat.getNumberInstance();
	
	
	/**
	 * Workaround to avoid {@link NumberFormat}'s parsing issues.
	 * 
	 * @param input
	 *            the string to parse
	 * @param formats
	 *            given formats
	 * @return parsed number or <code>null</code> if parse failed
	 * 
	 * @see http://www.ibm.com/developerworks/library/j-numberformat/
	 * @see https://www.xdevissues.com/browse/XDEVAPI-215
	 */
	private static Number parseNumber(String input, boolean decimal)
	{
		char separator = '\0';
		loop: for(int i = 0, c = input.length(); i < c; i++)
		{
			char ch = input.charAt(i);
			switch(ch)
			{
				case '0':
				case '1':
				case '2':
				case '3':
				case '4':
				case '5':
				case '6':
				case '7':
				case '8':
				case '9':
				case '-':
				break;
				
				case '.':
				case ',':
					if(separator == '\0')
					{
						separator = ch;
					}
					else
					{
						separator = '\0';
						break loop;
					}
					break;
					
				default:
					separator = '\0';
					break loop;
			}
		}
		
		NumberFormat format;
		
		if(separator != '\0' && decimal)
		{
			// one separator char
			DecimalFormatSymbols symbols = new DecimalFormatSymbols();
			symbols.setDecimalSeparator(separator);
			DecimalFormat decimalFormat = new DecimalFormat("#.#;-#.#",symbols);
			decimalFormat.setGroupingUsed(false);
			format = decimalFormat;
		}
		else if(decimal)
		{
			format = decimalFormat;
		}
		else
		{
			format = integerFormat;
		}
		
		ParsePosition pos = new ParsePosition(0);
		Number result = format.parse(input,pos);
		if(result != null && input.length() == pos.getIndex())
		{
			return result;
		}
		return null;
	}
	
	
	
	private static class ByteConverter implements ObjectConverter<Byte>
	{
		@Override
		public Byte convert(Object value) throws ObjectConversionException
		{
			if(value == null)
			{
				return null;
			}
			
			if(value instanceof Byte)
			{
				return (Byte)value;
			}
			
			if(value instanceof Number)
			{
				return ((Number)value).byteValue();
			}
			
			Number number = parseNumber(value.toString(),false);
			if(number != null)
			{
				long longVal = number.longValue();
				if(longVal < Byte.MIN_VALUE || longVal > Byte.MAX_VALUE)
				{
					throw new ObjectConversionException("Value out of range: " + longVal);
				}
				return number.byteValue();
			}
			
			throw new ObjectConversionException(value,Byte.class);
		}
	}
	
	
	
	private static class ShortConverter implements ObjectConverter<Short>
	{
		@Override
		public Short convert(Object value) throws ObjectConversionException
		{
			if(value == null)
			{
				return null;
			}
			
			if(value instanceof Short)
			{
				return (Short)value;
			}
			
			if(value instanceof Number)
			{
				return ((Number)value).shortValue();
			}
			
			Number number = parseNumber(value.toString(),false);
			if(number != null)
			{
				long longVal = number.longValue();
				if(longVal < Short.MIN_VALUE || longVal > Short.MAX_VALUE)
				{
					throw new ObjectConversionException("Value out of range: " + longVal);
				}
				return number.shortValue();
			}
			
			throw new ObjectConversionException(value,Short.class);
		}
	}
	
	
	
	private static class IntegerConverter implements ObjectConverter<Integer>
	{
		@Override
		public Integer convert(Object value) throws ObjectConversionException
		{
			if(value == null)
			{
				return null;
			}
			
			if(value instanceof Integer)
			{
				return (Integer)value;
			}
			
			if(value instanceof Number)
			{
				return ((Number)value).intValue();
			}
			
			Number number = parseNumber(value.toString(),false);
			if(number != null)
			{
				long longVal = number.longValue();
				if(longVal < Integer.MIN_VALUE || longVal > Integer.MAX_VALUE)
				{
					throw new ObjectConversionException("Value out of range: " + longVal);
				}
				return number.intValue();
			}
			
			throw new ObjectConversionException(value,Integer.class);
		}
	}
	
	
	
	private static class LongConverter implements ObjectConverter<Long>
	{
		@Override
		public Long convert(Object value) throws ObjectConversionException
		{
			if(value == null)
			{
				return null;
			}
			
			if(value instanceof Long)
			{
				return (Long)value;
			}
			
			if(value instanceof Number)
			{
				return ((Number)value).longValue();
			}
			
			if(value instanceof Date)
			{
				return ((Date)value).getTime();
			}
			
			Number number = parseNumber(value.toString(),false);
			if(number != null)
			{
				return number.longValue();
			}
			
			throw new ObjectConversionException(value,Long.class);
		}
	}
	
	
	
	private static class FloatConverter implements ObjectConverter<Float>
	{
		@Override
		public Float convert(Object value) throws ObjectConversionException
		{
			if(value == null)
			{
				return null;
			}
			
			if(value instanceof Float)
			{
				return (Float)value;
			}
			
			if(value instanceof Number)
			{
				return ((Number)value).floatValue();
			}
			
			String string = value.toString();
			Number number = parseNumber(string,true);
			if(number != null)
			{
				float floatVal = number.floatValue();
				if(floatVal == Float.NaN || floatVal == Float.POSITIVE_INFINITY
						|| floatVal == Float.NEGATIVE_INFINITY)
				{
					throw new ObjectConversionException("Value out of range: " + string);
				}
				return floatVal;
			}
			
			throw new ObjectConversionException(value,Float.class);
		}
	}
	
	
	
	private static class DoubleConverter implements ObjectConverter<Double>
	{
		@Override
		public Double convert(Object value) throws ObjectConversionException
		{
			if(value == null)
			{
				return null;
			}
			
			if(value instanceof Double)
			{
				return (Double)value;
			}
			
			if(value instanceof Number)
			{
				return ((Number)value).doubleValue();
			}
			
			if(value instanceof Date)
			{
				return (double)((Date)value).getTime();
			}
			
			String string = value.toString();
			Number number = parseNumber(string,true);
			if(number != null)
			{
				double doubleVal = number.doubleValue();
				if(doubleVal == Double.NaN || doubleVal == Double.POSITIVE_INFINITY
						|| doubleVal == Double.NEGATIVE_INFINITY)
				{
					throw new ObjectConversionException("Value out of range: " + string);
				}
				return doubleVal;
			}
			
			throw new ObjectConversionException(value,Double.class);
		}
	}
	
	
	
	private static class BooleanConverter implements ObjectConverter<Boolean>
	{
		@Override
		public Boolean convert(Object value) throws ObjectConversionException
		{
			return toBoolean(value,true);
		}
	}
	
	
	/**
	 * Used by number converters with tryNumberConversion=false to avoid stack
	 * overflow
	 */
	private static Boolean toBoolean(Object value, boolean tryNumberConversion)
			throws ObjectConversionException
	{
		if(value == null)
		{
			return null;
		}
		
		if(value instanceof Boolean)
		{
			return (Boolean)value;
		}
		else
		{
			String s = value.toString();
			
			if("TRUE".equalsIgnoreCase(s))
			{
				return Boolean.TRUE;
			}
			if("FALSE".equalsIgnoreCase(s))
			{
				return Boolean.FALSE;
			}
			
			if(tryNumberConversion)
			{
				try
				{
					long number = tolong(value);
					return number != 0;
				}
				catch(ObjectConversionException e)
				{
				}
			}
		}
		
		throw new ObjectConversionException(value,Boolean.class);
	}
	
	
	
	private static class CharacterConverter implements ObjectConverter<Character>
	{
		@Override
		public Character convert(Object value) throws ObjectConversionException
		{
			if(value == null)
			{
				return null;
			}
			
			if(value instanceof Character)
			{
				return (Character)value;
			}
			
			if(value instanceof String)
			{
				String str = (String)value;
				if(str.length() == 1)
				{
					return str.charAt(0);
				}
			}
			else
			{
				try
				{
					int number = ConversionUtils.toint(value);
					return (char)number;
				}
				catch(ObjectConversionException e)
				{
				}
			}
			
			throw new ObjectConversionException(value,Character.class);
		}
	}
	
	
	
	private static class StringConverter implements ObjectConverter<String>
	{
		@Override
		public String convert(Object value) throws ObjectConversionException
		{
			if(value == null)
			{
				return null;
			}
			
			if(value.getClass() == String.class)
			{
				return (String)value;
			}
			if(value instanceof char[])
			{
				return new String((char[])value);
			}
			if(value instanceof byte[])
			{
				return new String((byte[])value);
			}
			if(value instanceof Reader)
			{
				try
				{
					return IOUtils.readString((Reader)value,true);
				}
				catch(IOException e)
				{
					throw new ObjectConversionException(e);
				}
			}
			if(value instanceof InputStream)
			{
				try
				{
					return IOUtils.readString((InputStream)value,true);
				}
				catch(IOException e)
				{
					throw new ObjectConversionException(e);
				}
			}
			if(value instanceof CharHolder)
			{
				try
				{
					return new String(((CharHolder)value).toCharArray());
				}
				catch(Exception e)
				{
					throw new ObjectConversionException(e);
				}
			}
			if(value instanceof ByteHolder)
			{
				try
				{
					return new String(((ByteHolder)value).toByteArray());
				}
				catch(Exception e)
				{
					throw new ObjectConversionException(e);
				}
			}
			if(value instanceof Clob)
			{
				try
				{
					return IOUtils.readString(((Clob)value).getCharacterStream(),true);
				}
				catch(Exception e)
				{
					throw new ObjectConversionException(e);
				}
			}
			if(value instanceof Blob)
			{
				try
				{
					return IOUtils.readString(((Blob)value).getBinaryStream(),true);
				}
				catch(Exception e)
				{
					throw new ObjectConversionException(e);
				}
			}
			
			return value.toString();
		}
	}
	
	
	
	private static class DateConverter implements ObjectConverter<Date>
	{
		final SimpleDateFormat	SIMPLE_DATETIME_FORMAT;
		final SimpleDateFormat	SIMPLE_DATE_FORMAT;
		final SimpleDateFormat	SIMPLE_TIME_FORMAT;
		final int[]				DATE_STYLE_CHECK_ORDER;
		{
			SIMPLE_DATETIME_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			SIMPLE_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
			SIMPLE_TIME_FORMAT = new SimpleDateFormat("HH:mm:ss");
			DATE_STYLE_CHECK_ORDER = new int[]{DateFormat.MEDIUM,DateFormat.SHORT,DateFormat.LONG,
					DateFormat.FULL};
		}
		
		
		@Override
		public Date convert(Object value) throws ObjectConversionException
		{
			if(value == null)
			{
				return null;
			}
			
			if(value instanceof Date)
			{
				return (Date)value;
			}
			if(value instanceof Calendar)
			{
				return ((Calendar)value).getTime();
			}
			if(value instanceof Number)
			{
				return new Date(((Number)value).longValue());
			}
			
			String s = value.toString();
			
			try
			{
				return SIMPLE_DATETIME_FORMAT.parse(s);
			}
			catch(ParseException e)
			{
			}
			
			try
			{
				return SIMPLE_DATE_FORMAT.parse(s);
			}
			catch(ParseException e)
			{
			}
			
			try
			{
				return SIMPLE_TIME_FORMAT.parse(s);
			}
			catch(ParseException e)
			{
			}
			
			for(int date : DATE_STYLE_CHECK_ORDER)
			{
				for(int time : DATE_STYLE_CHECK_ORDER)
				{
					try
					{
						return DateFormat.getDateTimeInstance(date,time).parse(s);
					}
					catch(ParseException e)
					{
					}
				}
			}
			
			throw new ObjectConversionException(value,XdevDate.class);
		}
	}
	
	
	
	private static class XdevDateConverter implements ObjectConverter<XdevDate>
	{
		@Override
		public XdevDate convert(Object value) throws ObjectConversionException
		{
			if(value == null)
			{
				return null;
			}
			
			if(value instanceof XdevDate)
			{
				return (XdevDate)value;
			}
			if(value instanceof Date)
			{
				return new XdevDate((Date)value);
			}
			if(value instanceof Calendar)
			{
				return new XdevDate((Calendar)value);
			}
			if(value instanceof Number)
			{
				return new XdevDate(new Date(((Number)value).longValue()));
			}
			
			try
			{
				Date date = ConversionUtils.toDate(value);
				return new XdevDate(date);
			}
			catch(ObjectConversionException e)
			{
			}
			
			throw new ObjectConversionException(value,XdevDate.class);
		}
	}
	
	
	
	private static class XdevBlobConverter implements ObjectConverter<XdevBlob>
	{
		@Override
		public XdevBlob convert(Object value) throws ObjectConversionException
		{
			if(value == null)
			{
				return null;
			}
			
			if(value instanceof XdevBlob)
			{
				return (XdevBlob)value;
			}
			if(value instanceof Blob)
			{
				try
				{
					return new XdevBlob((Blob)value);
				}
				catch(DBException e)
				{
					throw new ObjectConversionException(e);
				}
			}
			if(value instanceof byte[])
			{
				return new XdevBlob((byte[])value);
			}
			if(value instanceof ByteHolder)
			{
				return new XdevBlob(((ByteHolder)value).toByteArray());
			}
			if(value instanceof InputStream)
			{
				try
				{
					InputStream in = (InputStream)value;
					try
					{
						return new XdevBlob(IOUtils.readData(in));
					}
					finally
					{
						IOUtils.closeSilent(in);
					}
				}
				catch(IOException e)
				{
					throw new ObjectConversionException(e);
				}
			}
			
			throw new ObjectConversionException(value,XdevBlob.class);
		}
	}
	
	
	
	private static class BytesConverter implements ObjectConverter<byte[]>
	{
		@Override
		public byte[] convert(Object value) throws ObjectConversionException
		{
			if(value == null)
			{
				return null;
			}
			
			if(value instanceof byte[])
			{
				return (byte[])value;
			}
			else if(value instanceof Blob)
			{
				try
				{
					Blob blob = (Blob)value;
					return blob.getBytes(1,(int)blob.length());
				}
				catch(SQLException e)
				{
					throw new ObjectConversionException(e);
				}
			}
			else if(value instanceof InputStream)
			{
				try
				{
					InputStream in = (InputStream)value;
					try
					{
						return IOUtils.readData(in);
					}
					finally
					{
						IOUtils.closeSilent(in);
					}
				}
				catch(IOException e)
				{
					throw new ObjectConversionException(e);
				}
			}
			else if(value instanceof ByteHolder)
			{
				return ((ByteHolder)value).toByteArray();
			}
			
			throw new ObjectConversionException(value,byte[].class);
		}
	}
	
	
	
	private static class XdevClobConverter implements ObjectConverter<XdevClob>
	{
		@Override
		public XdevClob convert(Object value) throws ObjectConversionException
		{
			if(value == null)
			{
				return null;
			}
			
			if(value instanceof XdevClob)
			{
				return (XdevClob)value;
			}
			if(value instanceof Clob)
			{
				try
				{
					return new XdevClob((Clob)value);
				}
				catch(DBException e)
				{
					throw new ObjectConversionException(e);
				}
			}
			if(value instanceof char[])
			{
				return new XdevClob((char[])value);
			}
			if(value instanceof CharHolder)
			{
				return new XdevClob(((CharHolder)value).toCharArray());
			}
			if(value instanceof String)
			{
				return new XdevClob(((String)value).toCharArray());
			}
			if(value instanceof Reader)
			{
				try
				{
					return new XdevClob(IOUtils.readChars((Reader)value,true));
				}
				catch(IOException e)
				{
					throw new ObjectConversionException(e);
				}
			}
			if(value instanceof InputStream)
			{
				try
				{
					return new XdevClob(IOUtils.readString((InputStream)value,true).toCharArray());
				}
				catch(IOException e)
				{
					throw new ObjectConversionException(e);
				}
			}
			
			throw new ObjectConversionException(value,XdevClob.class);
		}
	}
	
	
	
	private static class CharsConverter implements ObjectConverter<char[]>
	{
		@Override
		public char[] convert(Object value) throws ObjectConversionException
		{
			if(value == null)
			{
				return null;
			}
			
			if(value instanceof char[])
			{
				return (char[])value;
			}
			
			try
			{
				String string = ConversionUtils.toString(value);
				return string.toCharArray();
			}
			catch(ObjectConversionException e)
			{
			}
			
			throw new ObjectConversionException(value,char[].class);
		}
	}
	
	
	
	private static class XdevListConverter implements ObjectConverter<XdevList>
	{
		@Override
		public XdevList convert(Object value) throws ObjectConversionException
		{
			if(value == null)
			{
				return null;
			}
			
			if(value instanceof XdevList)
			{
				return (XdevList)value;
			}
			if(value instanceof Collection)
			{
				return new XdevList((Collection)value);
			}
			if(value.getClass().isArray())
			{
				return new XdevList((Object[])value);
			}
			
			throw new ObjectConversionException(value,XdevList.class);
		}
	}
	
	
	
	private static class XdevHashtableConverter implements ObjectConverter<XdevHashtable>
	{
		@Override
		public XdevHashtable convert(Object value) throws ObjectConversionException
		{
			if(value == null)
			{
				return null;
			}
			
			if(value instanceof XdevHashtable)
			{
				return (XdevHashtable)value;
			}
			if(value instanceof Map)
			{
				return new XdevHashtable((Map)value);
			}
			if(value.getClass().isArray() && Array.getLength(value) % 2 == 0)
			{
				return new XdevHashtable((Object[])value);
			}
			
			throw new ObjectConversionException(value,XdevHashtable.class);
		}
	}
	
	
	
	private static class XdevFileConverter implements ObjectConverter<XdevFile>
	{
		@Override
		public XdevFile convert(Object value) throws ObjectConversionException
		{
			if(value == null)
			{
				return null;
			}
			
			if(value instanceof XdevFile)
			{
				return (XdevFile)value;
			}
			if(value instanceof File)
			{
				return new XdevFile((File)value);
			}
			if(value instanceof URI)
			{
				try
				{
					return new XdevFile((URI)value);
				}
				catch(IllegalArgumentException e)
				{
				}
			}
			if(value instanceof URL)
			{
				try
				{
					return new XdevFile(((URL)value).toURI());
				}
				catch(URISyntaxException e)
				{
				}
				catch(IllegalArgumentException e)
				{
				}
			}
			
			throw new ObjectConversionException(value,XdevFile.class);
		}
	}
	
	
	
	private static class XdevImageConverter implements ObjectConverter<XdevImage>
	{
		@Override
		public XdevImage convert(Object value) throws ObjectConversionException
		{
			if(value == null)
			{
				return null;
			}
			
			if(value instanceof XdevImage)
			{
				return (XdevImage)value;
			}
			if(value instanceof Image)
			{
				return new XdevImage((Image)value);
			}
			else if(value instanceof Icon)
			{
				return new XdevImage(GraphicUtils.createImageFromIcon((Icon)value));
			}
			
			throw new ObjectConversionException(value,XdevImage.class);
		}
	}
}
