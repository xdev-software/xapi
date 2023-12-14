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
package xdev.db.locking;


import java.util.UUID;


/**
 * Helper-Class to set and get a username.
 * 
 * @author XDEV Software (RHHF)
 * @since 4.0
 */
public class ClientSession
{
	private static String	userName	= null;
	
	
	/**
	 * Returns username. If no username was set a {@link UUID#randomUUID()} will
	 * returned.
	 * 
	 * @return username
	 */
	public static String getUserName()
	{
		
		if(userName == null)
		{
			userName = UUID.randomUUID().toString();
		}
		
		return userName;
	}
	
	
	/**
	 * Set a username to identifie a client during a session.
	 * 
	 * @param userName
	 *            (String)
	 */
	public static void setUserName(final String userName)
	{
		ClientSession.userName = userName;
	}
}
