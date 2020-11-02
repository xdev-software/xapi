package xdev.ui.locking;

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


import java.net.URL;
import java.text.MessageFormat;
import java.util.ResourceBundle;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import xdev.util.logging.LoggerFactory;
import xdev.util.logging.XdevLogger;


class UIResourceBundle
{
	private static final XdevLogger	LOG	= LoggerFactory.getLogger(UIResourceBundle.class);
	private static ResourceBundle	resourceBundle;
	
	
	private static ResourceBundle getBundle()
	{
		if(resourceBundle == null)
		{
			resourceBundle = ResourceBundle.getBundle("xdev.ui.locking.ui");
		}
		
		return resourceBundle;
	}
	
	
	public static ImageIcon loadResIcon(String name)
	{
		try
		{
			URL url = UIResourceBundle.class.getResource("xdev/ui/locking/icons/" + name);
			return new ImageIcon(ImageIO.read(url));
		}
		catch(Exception e)
		{
			LOG.error(e);
			return null;
		}
	}
	
	
	public static String getString(String key, Object... args)
	{
		String str = getBundle().getString(key);
		
		if(args != null && args.length > 0)
		{
			str = MessageFormat.format(str,args);
		}
		
		return str;
	}
}
