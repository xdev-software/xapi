package xdev.db.locking;

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


/**
 * Constants of XDEVPESSIMISTICLOCKTABLE.
 * 
 * @author XDEV Software (MP)
 * @since 4.0
 * 
 */
@SuppressWarnings("javadoc")
public class PessimisticLockTableConstants
{
	
	public static final String	PESSIMISTIC_LOCK_TABLE_NAME	= "XDEVPESSIMISTICLOCKTABLE";
	
	public static final String	ID							= "ID";
	public static final String	TABLE_NAME					= "TABLENAME";
	public static final String	TABLE_ROW_IDENTIFIER		= "TABLEROWIDENTIFIER";
	public static final String	VALID_UNTIL					= "VALIDUNTIL";
	public static final String	USER_STRING					= "USERSTRING";
	public static final String	USER_ID						= "USERID";
	public static final String	SERVER_TIME					= "SERVERTIME";
	
	public static final String	AUTO_INCREMENT				= "AUTO_INCREMENT";
	public static final String	NOT_NULL					= "NOT NULL";
	public static final String	INTEGER_TEXT				= "INTEGER";
	public static final String	VARCHAR_TEXT				= "VARCHAR(45)";
	public static final String	TIMESTAMP_TEXT				= "TIMESTAMP";
	
	public static final String	DEFAULT_INTEGER_EXTENDED	= INTEGER_TEXT + " " + NOT_NULL;
	public static final String	DEFAULT_VARCHAR_EXTENDED	= VARCHAR_TEXT + " " + NOT_NULL;
	public static final String	DEFAULT_TIMESTAMP_EXTENDED	= TIMESTAMP_TEXT + " " + NOT_NULL;
	
	public static final String	DATETIME_TEXT				= "DATETIME";
}
