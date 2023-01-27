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
package xdev.ui;


import java.text.MessageFormat;
import java.util.ResourceBundle;


class UIResourceBundle
{
	private static ResourceBundle	resourceBundle;


	private static ResourceBundle getBundle()
	{
		if(resourceBundle == null)
		{
			resourceBundle = ResourceBundle.getBundle("xdev.ui.ui");
		}

		return resourceBundle;
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
