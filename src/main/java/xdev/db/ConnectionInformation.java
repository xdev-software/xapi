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
package xdev.db;


import java.sql.Connection;

import com.xdev.jadoth.sqlengine.dbms.DbmsAdaptor;
import com.xdev.jadoth.sqlengine.dbms.DbmsConnectionInformation;



public abstract class ConnectionInformation<A extends DbmsAdaptor<A>> extends
		DbmsConnectionInformation.Implementation<A>
{
	public ConnectionInformation(String host, int port, String user, String password,
			String catalog, String urlExtension, A dbmsAdaptor)
	{
		super(host,port,user,password,catalog,urlExtension,dbmsAdaptor);
	}
	
	
	public boolean isConnectionValid(Connection connection)
	{
		try
		{
			return !connection.isClosed() && connection.isValid(1);
		}
		catch(Throwable e)
		{
		}
		
		return false;
	}
	
	
	/**
	 * @since 4.0
	 */
	protected String appendUrlExtension(String url)
	{
		String urlExtension = getUrlExtension();
		if(urlExtension == null || urlExtension.length() == 0)
		{
			return url;
		}
		
		StringBuilder sb = new StringBuilder();
		sb.append(url);
		if(urlExtension.startsWith("?"))
		{
			if(url.contains("?"))
			{
				sb.append(urlExtension.substring(1));
			}
			else
			{
				sb.append(urlExtension);
			}
		}
		else
		{
			if(!url.contains("?"))
			{
				sb.append("?");
			}
			sb.append(urlExtension);
		}
		return sb.toString();
	}
}
