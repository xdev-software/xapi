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
package xdev;


import java.util.Map;


/**
 * An extension of the XDEV Application Framework.
 * <p>
 * It is instantiated before the {@link Application} is started.
 * 
 * @see Application#initApplication()
 * @see Application#getExtension(String)
 * @see Application#getExtensions()
 * 
 * @author XDEV Software
 * @since 3.0
 */
public interface Extension
{
	/**
	 * Called in the {@link Application}s init routine.
	 * 
	 * @throws ExtensionInitializationException
	 *             if an error occurs or dependencies of the extension are not
	 *             fulfilled
	 */
	public void init(Map<String, String> args) throws ExtensionInitializationException;
	

	/**
	 * Returns the name of this extension.
	 * 
	 * @return the name of this extension
	 */
	public String getName();
	

	/**
	 * Returns the version of this extension.
	 * 
	 * @return the version of this extension
	 */
	public Version getVersion();
}
