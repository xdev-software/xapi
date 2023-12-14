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
package xdev.util.systemproperty;


/**
 * XdevSystemPropertyKeys provides the XDEV specific system property keys
 * 
 * @author XDEV Software (MP)
 * @since 4.0
 * 
 */
public class XdevSystemPropertyKeys
{
	// The XDEV system property identifier
	private static final String	XDEV_PROPERTY_PATTERN						= "XDEV_";
	
	// The XDEV DB system property indentifier
	private static final String	XDEV_DB_PROPERTY_PATTERN					= XDEV_PROPERTY_PATTERN
																					.concat("DB_");
	
	/**
	 * System property to enable/disable debug mode to use further infos for
	 * debugging and finding connection leaks for example. default value
	 * XDEV_DB_CONNECTIONDEBUGMODE=false
	 */
	public static final String	CONNECTION_DEBUG_MODE						= XDEV_DB_PROPERTY_PATTERN
																					.concat("CONNECTIONDEBUGMODE");
	
	/**
	 * System property to set stackTraceTimeOutDelay in ms. If a connection is
	 * not closed during this period the logger prints out the stack trace.
	 * default value: XDEV_DB_STACKTRACETIMEOUTDELAY=30000
	 * 
	 * @see<br> enable {@link CONNECTION_DEBUG_MODE} otherwise the timer will no
	 *          be activated</br>
	 */
	public static final String	STACKTRACE_TIMEOUT_DELAY					= XDEV_DB_PROPERTY_PATTERN
																					.concat("STACKTRACETIMEOUTDELAY");
	
	/**
	 * System property to set a timeout in ms for the connection. default value:
	 * XDEV_DB_CONNECTIONTIMEOUT=10000
	 */
	public static final String	CONNECTION_TIMEOUT							= XDEV_DB_PROPERTY_PATTERN
																					.concat("CONNECTIONTIMEOUT");
	
	/**
	 * System property to change location of database property file.
	 * XDEV_DB_DBPROPERTYFILE=C:/xdev_workspace/db_location
	 */
	public static final String	DATABASE_CONNECTION_PROPERTYFILE_LOCATION	= XDEV_DB_PROPERTY_PATTERN
																					.concat("DBPROPERTYFILE");
	
}
