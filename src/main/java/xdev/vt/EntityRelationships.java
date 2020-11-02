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
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;

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
 * Global registry of all {@link EntityRelationship}s.
 * 
 * @author XDEV Software Corp.
 * 
 */

public final class EntityRelationships
{
	
	/**
	 * Logger instance for this class.
	 */
	private static final XdevLogger	log	= LoggerFactory.getLogger(EntityRelationships.class);
	
	
	private EntityRelationships()
	{
	}
	
	public final static String				REGISTRY_PATH;
	private static EntityRelationshipModel	model;
	
	static
	{
		REGISTRY_PATH = EntityRelationships.class.getName().replace('.','/');
		
		model = new EntityRelationshipModel();
		
		try
		{
			ClassLoader loader = EntityRelationships.class.getClassLoader();
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
			NodeList children = doc.getElementsByTagName("erm");
			for(int i = 0, c = children.getLength(); i < c; i++)
			{
				Element child = (Element)children.item(i);
				String className = child.getAttribute("class");
				try
				{
					Class clazz = Class.forName(className);
					EntityRelationshipModel erm = (EntityRelationshipModel)clazz.getMethod(
							"getInstance").invoke(null);
					registerEntityRelationshipModel(erm);
				}
				catch(Exception e)
				{
					log.error(e);
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
							EntityRelationshipModel erm = (EntityRelationshipModel)clazz.getMethod(
									"getInstance").invoke(null);
							registerEntityRelationshipModel(erm);
						}
						catch(Exception e)
						{
							log.error(e);
						}
					}
				}
			}
		}
	}
	
	
	/**
	 * Adds all {@link EntityRelationship} objects from the specified
	 * {@link EntityRelationshipModel} to this {@link EntityRelationships}
	 * model.
	 * 
	 * @param erm
	 *            the {@link EntityRelationshipModel}, which
	 *            {@link EntityRelationship} objects are to add.
	 */
	public static void registerEntityRelationshipModel(EntityRelationshipModel erm)
	{
		model.addAll(erm);
	}
	
	
	/**
	 * Returns the underlying {@link EntityRelationshipModel} of this
	 * {@link EntityRelationships} registry.
	 */
	public static EntityRelationshipModel getModel()
	{
		return model;
	}
}
