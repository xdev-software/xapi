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


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import xdev.util.logging.LoggerFactory;
import xdev.util.logging.XdevLogger;


public final class DataSources
{
	
	/**
	 * Logger instance for this class.
	 */
	private static final XdevLogger	LOG	= LoggerFactory.getLogger(DataSources.class);
	
	
	private DataSources()
	{
	}
	
	public final static String				REGISTRY_PATH;
	private static Map<String, DataSource>	dataSources;
	
	static
	{
		REGISTRY_PATH = DataSources.class.getName().replace('.','/');
		
		dataSources = new Hashtable();
		
		try
		{
			ClassLoader loader = DataSources.class.getClassLoader();
			for(Enumeration<URL> urls = loader.getResources(REGISTRY_PATH); urls.hasMoreElements();)
			{
				URL url = urls.nextElement();
				try
				{
					load(url);
				}
				catch(Exception e)
				{
					LOG.error(e);
				}
			}
		}
		catch(Exception e)
		{
			LOG.error(e);
		}
	}
	
	
	private static void load(URL url) throws ParserConfigurationException, IOException
	{
		boolean oldFormat = false;
		
		try
		{
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document doc = builder.parse(url.openStream());
			doc.getDocumentElement().normalize();
			NodeList children = doc.getElementsByTagName("datasource");
			for(int i = 0, c = children.getLength(); i < c; i++)
			{
				Element child = (Element)children.item(i);
				String className = child.getAttribute("class");
				try
				{
					Class clazz = Class.forName(className);
					DataSource dataSource = (DataSource)clazz.getMethod("getInstance").invoke(null);
					registerDataSource(dataSource);
				}
				catch(Exception e)
				{
					LOG.error(e);
				}
			}
		}
		catch(SAXException e)
		{
			oldFormat = true;
		}
		
		if(oldFormat)
		{
			try(BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream(),StandardCharsets.UTF_8)))
			{
				String line;
				while((line = in.readLine()) != null)
				{
					line = line.trim();
					if(line.length() > 0)
					{
						try
						{
							Class clazz = Class.forName(line);
							DataSource dataSource = (DataSource)clazz.getMethod("getInstance").invoke(
									null);
							registerDataSource(dataSource);
						}
						catch(Exception e)
						{
							LOG.error(e);
						}
					}
				}
			}
		}
	}
	
	
	public static void registerDataSource(DataSource dataSource)
	{
		dataSources.put(dataSource.getName(),dataSource);
	}
	
	
	public static DataSource getDataSource(String name)
	{
		return dataSources.get(name);
	}
	
	
	public static DataSource[] getDataSources()
	{
		Collection<DataSource> collection = dataSources.values();
		return collection.toArray(new DataSource[collection.size()]);
	}
	
	
	public static <T extends DataSource> T[] getDataSources(Class<T> type)
	{
		List<T> list = new ArrayList();
		for(DataSource dataSource : dataSources.values())
		{
			if(type.isInstance(dataSource))
			{
				list.add((T)dataSource);
			}
		}
		T[] e = (T[])Array.newInstance(type,list.size());
		return list.toArray(e);
	}
}
