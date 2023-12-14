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
package xdev.util.res;


import java.io.*;
import java.util.*;

import xdev.io.*;


public class DefaultResourceSearchStrategy implements ResourceSearchStrategy
{
	private Map<Locale, ResourceBundle>	localizedProjectBundles;
	private ResourceBundle				defaultProjectBundle;
	
	
	public DefaultResourceSearchStrategy()
	{
		localizedProjectBundles = new HashMap();
		defaultProjectBundle = loadProjectResourceBundle(null);
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	public String lookupResourceString(String key, Locale locale, Object requestor)
			throws MissingResourceException
	{
		if(locale == null)
		{
			locale = Locale.getDefault();
		}
		
		Class clazz = null;
		if(requestor != null)
		{
			if(requestor instanceof Class)
			{
				clazz = (Class)requestor;
			}
			else
			{
				clazz = requestor.getClass();
			}
		}
		
		if(clazz != null)
		{
			String name = clazz.getName();
			boolean first = true;
			
			while(true)
			{
				try
				{
					String baseName;
					if(first)
					{
						first = false;
						baseName = name;
					}
					else
					{
						baseName = name.concat(".package");
					}
					
					return ResourceBundle.getBundle(baseName,locale,clazz.getClassLoader())
							.getString(key);
				}
				catch(MissingResourceException mre)
				{
				}
				catch(NullPointerException npe)
				{
				}
				
				int lastDot = name.lastIndexOf('.');
				if(lastDot > 0)
				{
					name = name.substring(0,lastDot);
				}
				else
				{
					break;
				}
			}
		}
		
		ResourceBundle localizedProjectBundle = null;
		if(localizedProjectBundles.containsKey(locale))
		{
			localizedProjectBundle = localizedProjectBundles.get(locale);
		}
		else
		{
			localizedProjectBundle = loadProjectResourceBundle(locale);
			localizedProjectBundles.put(locale,localizedProjectBundle);
		}
		
		if(localizedProjectBundle != null)
		{
			try
			{
				return localizedProjectBundle.getString(key);
			}
			catch(MissingResourceException e)
			{
			}
		}
		
		if(defaultProjectBundle != null)
		{
			try
			{
				return defaultProjectBundle.getString(key);
			}
			catch(MissingResourceException e)
			{
			}
		}
		
		String className = clazz != null ? clazz.getName() : DefaultResourceSearchStrategy.class
				.getName();
		throw new MissingResourceException("No resource found for key '" + key + "', requestor = "
				+ className + ", locale = " + locale.getLanguage(),className,key);
	}
	
	
	public static ResourceBundle loadProjectResourceBundle(Locale locale)
	{
		try
		{
			return ResourceBundle.getBundle("res.project");
		}
		catch(MissingResourceException mre)
		{
			String localeSuffix = locale != null ? "_" + locale.getLanguage() : "";
			
			InputStream in = null;
			try
			{
				in = ResourceUtils.getResource("res/project" + localeSuffix + ".properties");
				if(in != null)
				{
					return new PropertyResourceBundle(in);
				}
			}
			catch(IOException e)
			{
			}
			finally
			{
				IOUtils.closeSilent(in);
			}
			
			return null;
		}
	}
}
