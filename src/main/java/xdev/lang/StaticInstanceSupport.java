package xdev.lang;

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


/**
 * This is a marker interface for classes which offer a static instance of
 * itself.
 * <p>
 * Note: The class may be a singleton or offer public constructors for
 * additional instances. This behaviour is not defined by this interface.
 * </p>
 * <p>
 * Every class which implements <code>StaticInstanceSupport</code> must provide
 * a static <code>getInstance()</code> method.<br>
 * Example:
 * 
 * <pre>
 * public class GlobalVirtualTable extends VirtualTable implements StaticInstanceSupport
 * {
 * 	private static GlobalVirtualTable instance = null;
 * 
 * 	public static GlobalVirtualTable getInstance(){
 * 		if(instance == null){
 * 			instance = new GlobalVirtualTable();
 * 		}
 * 
 * 		return instance;
 * 	}
 * 	
 * 	...
 * }
 * </pre>
 * 
 * </p>
 * 
 * 
 * @author XDEV Software Corp.
 */

public interface StaticInstanceSupport
{
}
