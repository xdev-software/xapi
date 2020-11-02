package xdev.util.res;

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


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Locale;
import java.util.MissingResourceException;

import xdev.Application;
import xdev.lang.LibraryMember;
import xdev.lang.NotNull;
import xdev.util.StringUtils;
import xdev.util.StringUtils.ParameterProvider;
import xdev.util.logging.LoggerFactory;
import xdev.util.logging.XdevLogger;


/**
 * <p>
 * The <code>ResourceUtils</code> class provides utility methods for resource
 * handling.
 * </p>
 * 
 * @since 2.0
 * 
 * @author XDEV Software
 */
@LibraryMember
public final class ResourceUtils
{
	/**
	 * Logger instance for this class.
	 */
	private static final XdevLogger	log	= LoggerFactory.getLogger(ResourceUtils.class);
	
	
	/**
	 * <p>
	 * <code>ResourceUtils</code> instances can not be instantiated. The class
	 * should be used as utility class:
	 * <code>ResourceUtils.getStrategy();</code>.
	 * </p>
	 */
	private ResourceUtils()
	{
	}

	/**
	 * JavaDoc omitted for private field.
	 */
	private static ResourceHandler			handler		= new DefaultResourceHandler();

	/**
	 * JavaDoc omitted for private field.
	 */
	private static ResourceSearchStrategy	strategy	= null;


	/**
	 * Sets the {@link ResourceHandler}.
	 * 
	 * @param resourceHandler
	 *            {@link ResourceHandler} to be set.
	 * @throws NullPointerException
	 *             if <code>resourceHandler</code> is <code>null</code>
	 */
	public static void setHandler(@NotNull ResourceHandler resourceHandler)
			throws NullPointerException
	{
		if(resourceHandler == null)
		{
			throw new NullPointerException();
		}

		handler = resourceHandler;
		strategy = null;
	}


	/**
	 * Returns the {@link ResourceSearchStrategy}.
	 * 
	 * @return the {@link ResourceSearchStrategy}.
	 */
	private static ResourceSearchStrategy getStrategy()
	{
		if(strategy == null)
		{
			strategy = handler.getSearchStrategy();
		}

		return strategy;
	}


	/**
	 * Returns the localized String for the given <code>key</code>.
	 * 
	 * <p>
	 * The current set {@link Locale} is used to lookup the requested String
	 * </p>
	 * 
	 * @param key
	 *            to get localized String for.
	 * @return the localized String for the given <code>key</code>.
	 * @throws MissingResourceException
	 *             if no String could be found in the current set {@link Locale}
	 *             for the given <code>key</code>.
	 * @see Locale
	 */
	public static String getResourceString(String key) throws MissingResourceException
	{
		return getResourceString(key,null,null);
	}


	/**
	 * Returns the localized String for the given <code>key</code>.
	 * 
	 * <p>
	 * The current set {@link Locale} is used to lookup the requested String
	 * </p>
	 * 
	 * @param key
	 *            to get localized String for.
	 * @param requestor
	 *            object that requests the resource {@link String}. The
	 *            <code>requestor</code> may be crucial how the strategy is
	 *            looking for the resource. If <code>requestor</code> is
	 *            <code>null</code> only the default resource bundle is searched
	 *            through.
	 * @return the localized String for the given <code>key</code>.
	 * @throws MissingResourceException
	 *             if no String could be found in the current set {@link Locale}
	 *             for the given <code>key</code>.
	 * @see Locale
	 */
	public static String getResourceString(String key, Object requestor)
			throws MissingResourceException
	{
		return getResourceString(key,null,requestor);
	}


	/**
	 * Returns the localized String for the given <code>key</code> and
	 * <code>locale</code>.
	 * 
	 * 
	 * @param key
	 *            to get localized String for.
	 * @param locale
	 *            to lookup the {@link String} for.
	 * 
	 * @return the localized String for the given <code>key</code>.
	 * @throws MissingResourceException
	 *             if no String could be found in the current set {@link Locale}
	 *             for the given <code>key</code>.
	 * 
	 * @see Locale
	 */
	public static String getResourceString(String key, Locale locale)
			throws MissingResourceException
	{
		return getResourceString(key,locale,null);
	}


	/**
	 * Returns the localized String for the given <code>key</code> and
	 * <code>locale</code>.
	 * 
	 * 
	 * @param key
	 *            to get localized String for.
	 * @param locale
	 *            to lookup the {@link String} for.
	 * @param requestor
	 *            object that requests the resource {@link String}. The
	 *            <code>requestor</code> may be crucial how the strategy is
	 *            looking for the resource. If <code>requestor</code> is
	 *            <code>null</code> only the default resource bundle is searched
	 *            through.
	 * @return the localized String for the given <code>key</code>.
	 * @throws MissingResourceException
	 *             if no String could be found in the current set {@link Locale}
	 *             for the given <code>key</code>.
	 * 
	 * @see Locale
	 */
	public static String getResourceString(String key, Locale locale, Object requestor)
			throws MissingResourceException
	{
		return getStrategy().lookupResourceString(key,locale,requestor);
	}


	/**
	 * Returns the localized version of the given {@link String}
	 * <code>str</code>. Only parts of the {@link String} fitting the pattern
	 * <code>{$myKey}</code> will be localized. <code>myKey</code> represents
	 * the key for a localized String in the chosen {@link Locale}.
	 * 
	 * <p>
	 * The current set {@link Locale} is used to lookup the requested String.
	 * </p>
	 * <p>
	 * <strong>Hint: </strong> The {@link MissingResourceException} is
	 * suppressed by the method. The stack trace of the
	 * {@link MissingResourceException} will be reported to the
	 * {@link Application}'s logger.
	 * </p>
	 * 
	 * @param str
	 *            to localize
	 * @param requestor
	 *            object that requests the resource {@link String}. The
	 *            <code>requestor</code> may be crucial how the strategy is
	 *            looking for the resource. If <code>requestor</code> is
	 *            <code>null</code> only the default resource bundle is searched
	 *            through.
	 * @return the localized String for the given <code>str</code>.
	 * 
	 * @see Locale
	 */
	public static String optLocalizeString(String str, Object requestor)
	{
		try
		{
			return localizeString(str,requestor);
		}
		catch(MissingResourceException e)
		{
			log.error(e);
			return str;
		}
	}


	/**
	 * Returns the localized version of the given {@link String}
	 * <code>str</code> as char if the result is a 1-length String, '\0'
	 * otherwise. Only parts of the {@link String} fitting the pattern
	 * <code>{$myKey}</code> will be localized. <code>myKey</code> represents
	 * the key for a localized String in the chosen {@link Locale}.
	 * 
	 * <p>
	 * The current set {@link Locale} is used to lookup the requested String.
	 * </p>
	 * <p>
	 * <strong>Hint: </strong> The {@link MissingResourceException} is
	 * suppressed by the method. The stack trace of the
	 * {@link MissingResourceException} will be reported to the
	 * {@link Application}'s logger.
	 * </p>
	 * 
	 * @param str
	 *            to localize
	 * @param requestor
	 *            object that requests the resource {@link String}. The
	 *            <code>requestor</code> may be crucial how the strategy is
	 *            looking for the resource. If <code>requestor</code> is
	 *            <code>null</code> only the default resource bundle is searched
	 *            through.
	 * @return the localized char for the given <code>str</code> or '\0'.
	 * 
	 * @see Locale
	 */
	public static char optLocalizeChar(String str, Object requestor)
	{
		int length = str.length();
		switch(length)
		{
			case 0:
				return '\0';
			case 1:
				return str.charAt(0);
			default:
				str = optLocalizeString(str,requestor);
				length = str.length();
				return length == 1 ? str.charAt(0) : '\0';
		}
	}


	/**
	 * Returns the localized version of the given {@link String}
	 * <code>str</code>. Only parts of the {@link String} fitting the pattern
	 * <code>{$myKey}</code> will be localized. <code>myKey</code> represents
	 * the key for a localized String in the chosen {@link Locale}.
	 * 
	 * <p>
	 * The current set {@link Locale} is used to lookup the requested String.
	 * </p>
	 * 
	 * 
	 * 
	 * @param str
	 *            to localize
	 * @param requestor
	 *            object that requests the resource {@link String}. The
	 *            <code>requestor</code> may be crucial how the strategy is
	 *            looking for the resource. If <code>requestor</code> is
	 *            <code>null</code> only the default resource bundle is searched
	 *            through.
	 * @return the localized String for the given <code>key</code>.
	 * 
	 * @throws MissingResourceException
	 *             if no String could be found in the current set {@link Locale}
	 *             for the given <code>key</code>.
	 * 
	 * @see Locale
	 */
	public static String localizeString(String str, Object requestor)
			throws MissingResourceException
	{
		return localizeString(str,null,requestor);
	}


	/**
	 * Returns the localized version of the given {@link String}
	 * <code>str</code>. Only parts of the {@link String} fitting the pattern
	 * <code>{$myKey}</code> will be localized. <code>myKey</code> represents
	 * the key for a localized String in the chosen {@link Locale}.
	 * 
	 * 
	 * @param str
	 *            to localize
	 * 
	 * @param locale
	 *            to lookup the {@link String} for.
	 * @param requestor
	 *            object that requests the resource {@link String}. The
	 *            <code>requestor</code> may be crucial how the strategy is
	 *            looking for the resource. If <code>requestor</code> is
	 *            <code>null</code> only the default resource bundle is searched
	 *            through.
	 * @return the localized String for the given <code>key</code>.
	 * 
	 * @throws MissingResourceException
	 *             if no String could be found in the current set {@link Locale}
	 *             for the given <code>key</code>.
	 * 
	 * @see Locale
	 */
	public static String localizeString(String str, final Locale locale, final Object requestor)
			throws MissingResourceException
	{
		return StringUtils.format(str,new ParameterProvider()
		{
			@Override
			public String getValue(String key)
			{
				return getResourceString(key,locale,requestor);
			}
		});
	}


	//TODO javadoc
	public static InputStream getResource(String path) throws IOException
	{
		InputStream in = ResourceUtils.class.getResourceAsStream(path);
		if(in != null)
		{
			return in;
		}

		try
		{
			File file = new File(path).getAbsoluteFile();
			if(file.exists())
			{
				return new FileInputStream(file);
			}
		}
		catch(Throwable t)
		{
			// ignore - no right to read
		}

		if(Application.isApplet())
		{
			String url = Application.getContainer().getCodeBase().toExternalForm();
			StringBuilder sb = new StringBuilder(url);
			if(!url.endsWith("/"))
			{
				sb.append("/");
			}
			if(path.startsWith("/"))
			{
				path = path.substring(1);
			}
			sb.append(path);
			url = sb.toString();

			try
			{
				return new URL(url).openStream();
			}
			catch(Exception e)
			{
				// ignore
			}
		}

		return null;
	}
}
