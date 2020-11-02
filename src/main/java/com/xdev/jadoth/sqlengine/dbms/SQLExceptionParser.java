
package com.xdev.jadoth.sqlengine.dbms;

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


import java.sql.SQLException;

import com.xdev.jadoth.sqlengine.exceptions.SQLEngineException;



/**
 * The Interface SQLExceptionParser.
 * 
 * @author Thomas Muenz
 */
public interface SQLExceptionParser 
{
	
	/**
	 * Parses the sql exception.
	 * 
	 * @param e the e
	 * @return the sQL engine exception
	 */
	public SQLEngineException parseSQLException(SQLException e);
	
	
	/**
	 * The Class Body.
	 */
	public static class Body implements SQLExceptionParser
	{
		
		/**
		 * @param e
		 * @return
		 * @see com.xdev.jadoth.sqlengine.dbms.SQLExceptionParser#parseSQLException(java.sql.SQLException)
		 */
		@Override
		public SQLEngineException parseSQLException(SQLException e) {
			return new SQLEngineException(e);
		}		
	}
}
