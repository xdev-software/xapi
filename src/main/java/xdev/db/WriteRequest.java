package xdev.db;

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


import xdev.db.sql.WritingQuery;


public class WriteRequest
{
	private WritingQuery	query;
	private String			sql;
	private String[]		columnNames;
	private boolean			returnGeneratedKeys;
	private Object[]		params;
	

	public WriteRequest(WritingQuery query, boolean returnGeneratedKeys, Object... params)
	{
		this.query = query;
		this.returnGeneratedKeys = returnGeneratedKeys;
		this.params = params;
	}
	

	public WriteRequest(String sql, boolean returnGeneratedKeys, Object... params)
	{
		this.sql = sql;
		this.returnGeneratedKeys = returnGeneratedKeys;
		this.params = params;
	}
	

	public WriteRequest(WritingQuery query, String[] columnNames, Object... params)
	{
		this.query = query;
		this.columnNames = columnNames;
		this.params = params;
	}
	

	public WriteRequest(String sql, String[] columnNames, Object... params)
	{
		this.sql = sql;
		this.columnNames = columnNames;
		this.params = params;
	}
	

	public WriteResult execute(DBConnection conn) throws DBException
	{
		if(query != null)
		{
			if(columnNames != null)
			{
				return conn.write(query,columnNames,params);
			}
			else
			{
				return conn.write(query,returnGeneratedKeys,params);
			}
		}
		else
		{
			if(columnNames != null)
			{
				return conn.write(sql,columnNames,params);
			}
			else
			{
				return conn.write(sql,returnGeneratedKeys,params);
			}
		}
	}
}
