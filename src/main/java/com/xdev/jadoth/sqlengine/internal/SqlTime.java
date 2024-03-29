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
package com.xdev.jadoth.sqlengine.internal;

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

}
