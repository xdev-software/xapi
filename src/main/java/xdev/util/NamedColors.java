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


import java.awt.Color;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import xdev.util.logging.LoggerFactory;
import xdev.util.logging.XdevLogger;


/**
 * Container class for {@link NamedColor}s.
 * <p>
 * Provides a range of sample color collections.
 * </p>
 * 
 * @author XDEV Software
 * @since 4.0
 */
public class NamedColors
{
	private static final XdevLogger LOG = LoggerFactory.getLogger(NamedColors.class);
	
	private final static Map<Integer, NamedColor>	namedColors;
	private final static Map<Integer, NamedColor>	ralColors;
	private final static Color[]					commonColors;
	
	static
	{
		namedColors = new LinkedHashMap();
		
		try(BufferedReader in = new BufferedReader(new InputStreamReader(getJarResource(NamedColors.class,"palette.colors"))))
		{
			String line = in.readLine();
			while(line != null)
			{
				String line2 = in.readLine();
				
				try
				{
					StringTokenizer st = new StringTokenizer(line2,",");
					NamedColor nc = new NamedColor(line,Integer.parseInt(st.nextToken()),
							Integer.parseInt(st.nextToken()),Integer.parseInt(st.nextToken()));
					namedColors.put(nc.getRGB(),nc);
				}
				catch(Exception e)
				{
					LOG.error(e);
				}
				
				line = in.readLine();
			}
		}
		catch(Exception e)
		{
			LOG.error(e);
		}
	}
	
	static
	{
		ralColors = new LinkedHashMap();
		
		try(BufferedReader in = new BufferedReader(new InputStreamReader(getJarResource(NamedColors.class,"ral.colors"))))
		{
			String line = in.readLine();
			while(line != null)
			{
				String line2 = in.readLine();
				
				try
				{
					StringTokenizer st = new StringTokenizer(line2,",");
					NamedColor nc = new NamedColor(line,Integer.parseInt(st.nextToken()),
							Integer.parseInt(st.nextToken()),Integer.parseInt(st.nextToken()));
					ralColors.put(nc.getRGB(),nc);
				}
				catch(Exception e)
				{
					LOG.error(e);
				}
				
				line = in.readLine();
			}
		}
		catch(Exception e)
		{
			LOG.error(e);
		}
	}
	
	static
	{
		List<Color> list = new ArrayList();
		
		try(BufferedReader in = new BufferedReader(new InputStreamReader(getJarResource(NamedColors.class,"common.colors"))))
		{
			String line;
			while((line = in.readLine()) != null)
			{
				line = line.trim();
				if(line.length() > 0)
				{
					try
					{
						StringTokenizer st = new StringTokenizer(line,",");
						list.add(new Color(Integer.parseInt(st.nextToken()),Integer.parseInt(st
								.nextToken()),Integer.parseInt(st.nextToken())));
					}
					catch(Exception e)
					{
						LOG.error(e);
					}
				}
			}
		}
		catch(Exception e)
		{
			LOG.error(e);
		}
		
		commonColors = list.toArray(new Color[list.size()]);
	}
	
	
	/**
	 * Returns the palette color collection.
	 * 
	 * @return the palette color collection.
	 */
	public static NamedColor[] getPaletteColors()
	{
		return namedColors.values().toArray(new NamedColor[namedColors.size()]);
	}
	
	
	/**
	 * Returns the RAL color collection.
	 * 
	 * @return the RAL color collection.
	 */
	public static NamedColor[] getRalColors()
	{
		return ralColors.values().toArray(new NamedColor[ralColors.size()]);
	}
	
	
	/**
	 * Returns the common color collection.
	 * 
	 * @return the common color collection.
	 */
	public static Color[] getCommonColors()
	{
		return ArrayUtils.copyOf(commonColors);
	}
	
	
	private static InputStream getJarResource(Class packageClass, String file) throws IOException
	{
		return getJarResource(packageClass,file,false);
	}
	
	
	private static InputStream getJarResource(Class packageClass, String file,
			boolean searchSuperClassPackages) throws IOException
	{
		StringBuilder sb = new StringBuilder("/");
		sb.append(packageClass.getPackage().getName().replace('.','/'));
		sb.append("/");
		sb.append(file);
		URL url = packageClass.getResource(sb.toString());
		
		if(url == null && searchSuperClassPackages)
		{
			packageClass = packageClass.getSuperclass();
			if(packageClass != null)
			{
				return getJarResource(packageClass,file,true);
			}
		}
		
		if(url != null)
		{
			return url.openStream();
		}
		
		return null;
	}
}
