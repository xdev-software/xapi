package xdev.ui;

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
 * Constant collection for the default bean property categories used in XDEV.
 * 
 * @author XDEV Software
 * @since 3.2
 */
public interface DefaultBeanCategories
{
	/**
	 * The main group
	 */
	String	OBJECT	= "object";
	
	/**
	 * Group for design related properties
	 */
	String	DESIGN	= "design";
	
	/**
	 * Group for data related properties
	 */
	String	DATA	= "data";
	
	/**
	 * The uncategorized group for expert properties or unused properties
	 */
	String	MISC	= "misc";
}
