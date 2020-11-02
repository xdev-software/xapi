/**
 * 
 */
package com.xdev.jadoth.util.logging.jul;

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

import java.util.logging.Level;

/**
 * @author Thomas Muenz
 *
 */
public interface LoggingContext
{
	/**
	 * Gets the logging aspect.
	 * 
	 * @return the logging aspect
	 */
	public LoggingAspect getLoggingAspect();
	
	/**
	 * Gets the level.
	 * 
	 * @return the level
	 */
	public Level getLevel();
	
	/**
	 * Gets the source class.
	 * 
	 * @return the source class
	 */
	public Class<?> getSourceClass();
	
	/**
	 * Gets the source method name.
	 * 
	 * @return the source method name
	 */
	public String getSourceMethodName();
}
