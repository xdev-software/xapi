package xdev.util.logging;

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


import java.util.logging.Logger;

import xdev.lang.NotNull;


/**
 * 
 * The {@link LoggerFactory} is a utility class producing {@link XdevLoggerImpl}s.
 * 
 * @author XDEV Software (RHHF)
 * @since 3.2
 * 
 */
public class LoggerFactory
{
	
	/**
	 * 
	 * 
	 * Returns a logger named corresponding to the class(name) passed as
	 * parameter.
	 * 
	 * @param clazz
	 *            the returned logger will be named after clazz
	 * @return logger named corresponding to the class(name) passed as
	 *         parameter.
	 */
	public static XdevLogger getLogger(final @NotNull Class<?> clazz)
	{
		return new XdevLoggerImpl(Logger.getLogger(clazz.getName()));
		
	}
}
