
package com.xdev.jadoth.sqlengine.internal;

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

import java.text.DateFormat;

import com.xdev.jadoth.sqlengine.dbms.DbmsDMLAssembler;




/**
 * The Class SqlTime.
 * 
 * @author Thomas Muenz
 */
public class SqlTime extends SqlTimestamp {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7467042116245951979L;

	/**
	 * Instantiates a new sql time.
	 * 
	 * @param o the o
	 */
	public SqlTime(final Object o) {
		super(o);
	}
	
	
	
	@Override
	public DateFormat getDateFormat(final DbmsDMLAssembler<?> dbmsAdaptor)
	{
		return dbmsAdaptor.getDateFormatTIME();
	}

	// (18.04.2010)FIXME: Fix SqlTime
//	/**
//	 * Assemble.
//	 * 
//	 * @return the string
//	 */
//	public String assemble() {
//		return this.getSqlTIME();
//	}

}
