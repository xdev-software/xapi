package xdev.vt;

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


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import xdev.util.logging.LoggerFactory;
import xdev.util.logging.XdevLogger;


/**
 * 
 * Global registry of all {@link VirtualTable}s.
 * 
 * @author XDEV Software Corp.
 * 
 */
public final class VirtualTables
{
	/**
	 * Logger instance for this class.
	 */
	private static final XdevLogger	log	= LoggerFactory.getLogger(VirtualTables.class);
	
	
	private VirtualTables()
	{
	}
	
	public final static String	REGISTRY_PATH;
	
	private static Set<String>	virtualTableClassNames;
	
	static
	{
		REGISTRY_PATH = VirtualTables.class.getName().replace('.','/');
		
		virtualTableClassNames = new LinkedHashSet();
		
		try
		{
			ClassLoader loader = VirtualTables.class.getClassLoader();
			for(Enumeration<URL> urls = loader.getResources(REGISTRY_PATH); urls.hasMoreElements();)
			{
				URL url = urls.nextElement();
				try
				{
					load(url);
				}
				catch(Exception e)
				{
					log.error(e);
				}
			}
		}
		catch(Exception e)
		{
			log.error(e);
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
			NodeList children = doc.getElementsByTagName("vt");
			for(int i = 0, c = children.getLength(); i < c; i++)
			{
				Element child = (Element)children.item(i);
				String className = child.getAttribute("class");
				virtualTableClassNames.add(className);
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
						virtualTableClassNames.add(line);
					}
				}
			}
		}
	}
	
	
	/**
	 * Returns a {@link VirtualTable} as specified by its full qualified table
	 * name.
	 * 
	 * @param name
	 *            the full qualified table name of the virtual table to return
	 * @return a {@link VirtualTable}, or <code>null</code>, if none found.
	 */
	public static VirtualTable getVirtualTable(String name)
	{
		try
		{
			Class clazz = Class.forName(name);
			Field field = clazz.getField("VT");
			if(field != null)
			{
				return (VirtualTable)field.get(null);
			}
			
			Method method = clazz.getMethod("getInstance");
			if(method != null)
			{
				return (VirtualTable)method.invoke(null);
			}
			
			return (VirtualTable)clazz.newInstance();
		}
		catch(Exception e)
		{
			return null;
		}
	}
	
	
	/**
	 * Returns an <code>array</code> of all {@link VirtualTable} objects of this
	 * {@link VirtualTables} registry.
	 * 
	 * @return an <code>array</code> of {@link VirtualTable} objects.
	 */
	public static VirtualTable[] getVirtualTables()
	{
		List<VirtualTable> list = new ArrayList();
		for(String name : virtualTableClassNames)
		{
			VirtualTable vt = getVirtualTable(name);
			if(vt != null)
			{
				list.add(vt);
			}
		}
		return list.toArray(new VirtualTable[list.size()]);
	}
	
	
	/**
	 * @deprecated discontinued since 3.2, no need to register a vt to get it
	 *             via {@link #getVirtualTable(String)}
	 */
	@Deprecated
	public static void registerVirtualTable(VirtualTable vt)
	{
	}
	
	
	/**
	 * @deprecated returns always <code>null</code>, discontinued since 3.2
	 *             because of erroneous behavior, multiple virtual tables can
	 *             have the same database alias
	 */
	@Deprecated
	public static VirtualTable getVirtualTableByDatabaseAlias(String name)
	{
		return null;
	}
}
