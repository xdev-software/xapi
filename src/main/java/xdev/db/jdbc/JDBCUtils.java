package xdev.db.jdbc;

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


import java.sql.ResultSet;
import java.sql.Statement;

import xdev.lang.Nullable;


/**
 * The class {@link JDBCUtils} provides utility methods for JDBC related tasks.
 * 
 * @since 3.2
 * @author XDEV Software
 * 
 */
public class JDBCUtils
{
	/**
	 * Closes <code>rs</code> silently, throws no exception whether
	 * <code>rs</code> is <code>null</code> nor if an error occurs.
	 * 
	 * @param rs
	 *            {@link ResultSet} to be closed
	 */
	public static void closeSilent(final @Nullable ResultSet rs)
	{
		// 2014-08-20 (FHAE) - ensure there is no resource leak
		if(rs != null)
		{
			Statement statement = null;
			try
			{
				statement = rs.getStatement();
			}
			catch(final Throwable e)
			{
				// swallow
			}
			
			try
			{
				rs.close();
			}
			catch(final Throwable t)
			{
				// swallow
			}
			
			if(statement != null)
			{
				try
				{
					statement.close();
				}
				catch(final Throwable t)
				{
					// swallow
				}
			}
		}
		
	}
}
