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
package xdev.ui.laf;


import java.util.HashSet;
import java.util.Set;


public final class LookAndFeelManager
{
	private LookAndFeelManager()
	{
	}
	
	private static LookAndFeel					lookAndFeel;
	private static Set<LookAndFeelExtension>	extensions;
	
	
	public static void setLookAndFeel(LookAndFeel lookAndFeel) throws LookAndFeelException
	{
		LookAndFeelManager.lookAndFeel = lookAndFeel;
		
		lookAndFeel.setLookAndFeel();
		
		if(extensions != null)
		{
			for(LookAndFeelExtension extension : extensions)
			{
				extension.installLookAndFeelExtension(lookAndFeel);
			}
		}
	}
	
	
	/**
	 * @since 3.2
	 */
	public static LookAndFeel getLookAndFeel()
	{
		return lookAndFeel;
	}
	
	
	public static void registerExtension(LookAndFeelExtension extension)
	{
		if(extensions == null)
		{
			extensions = new HashSet();
		}
		
		extensions.add(extension);
	}
	
	
	/**
	 * @since 3.2
	 */
	public static LookAndFeelExtension[] getExtensions()
	{
		return extensions.toArray(new LookAndFeelExtension[extensions.size()]);
	}
}
