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
package xdev.db.servlet;


import javax.crypto.SecretKey;


class SessionInfo
{
	private final ServletDBDataSource	dataSource;
	private final String				url;
	private final String				sessionID;
	private final String				authKey;
	private final SecretKey				secretKey;
	

	SessionInfo(ServletDBDataSource dataSource, String url, String sessionID, String authKey,
			SecretKey secretKey)
	{
		this.dataSource = dataSource;
		this.url = url;
		this.sessionID = sessionID;
		this.authKey = authKey;
		this.secretKey = secretKey;
	}
	

	ServletDBDataSource getDataSource()
	{
		return dataSource;
	}
	

	String getURL()
	{
		return url;
	}
	

	String getSessionID()
	{
		return sessionID;
	}
	

	String getAuthKey()
	{
		return authKey;
	}
	

	SecretKey getSecretKey()
	{
		return secretKey;
	}
}
