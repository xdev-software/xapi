package xdev.db.sql;

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


import com.xdev.jadoth.sqlengine.internal.SqlField;

import xdev.db.DataType;


public class Field extends Column
{
	private static final long	serialVersionUID	= 4062231236407684395L;
	
	
	public Field(String fieldname, DataType type, int typeLength, Integer precision, Integer scale,
			boolean notNull, boolean unique, Object defaultValue)
	
	{
		super(new SqlField(fieldname,type.getSqlType(),typeLength,precision,scale,notNull,unique,
				defaultValue));
	}
}
