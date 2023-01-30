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


interface ServletServiceConstants
{
	byte[]	HANDSHAKE							= new byte[]{0x11,0x1B,0x07,0x03};
	
	String	SESSIONID_PARAM						= "sid";
	String	KEY_PARAM							= "key";
	String	KEY_FROM_CERT						= "$CERT";
	
	String	SECRET_KEY_ALGORITHM				= "DESede";
	String	KEY_EXCHANGE_ALGORITHM				= "DH";
	String	MAC_ALGORITHM						= "DESede/ECB/PKCS5Padding";
	
	byte	REQUEST_TEST_CONNECTION				= 1;
	
	byte	REQUEST_METADATA					= 10;
	byte	REQUEST_METADATA_TABLE_INFOS		= 11;
	byte	REQUEST_METADATA_TABLE_METADATA		= 12;
	byte	REQUEST_METADATA_STORED_PROCEDURES	= 13;
	byte	REQUEST_METADATA_ERS				= 14;
	byte	REQUEST_METADATA_SYNC				= 15;
	
	byte	REQUEST_QUERY_SQL					= 20;
	byte	REQUEST_GET_QUERY_ROW_COUNT			= 21;
	byte	REQUEST_WRITE_SQL_RGK				= 22;
	byte	REQUEST_WRITE_SQL_COLS				= 23;
	byte	REQUEST_WRITE_SQL					= 24;
	byte	REQUEST_STORED_PROCEDURE			= 25;
	byte	REQUEST_BEGIN_TRANSACTION			= 26;
	byte	REQUEST_SET_SAVEPOINT				= 27;
	byte	REQUEST_SET_SAVEPOINT_NAME			= 28;
	byte	REQUEST_RELEASE_SAVEPOINT			= 29;
	byte	REQUEST_COMMIT						= 30;
	byte	REQUEST_ROLLBACK					= 31;
	byte	REQUEST_ROLLBACK_SAVEPOINT			= 32;
	
	byte	REQUEST_PAGING_START				= 40;
	byte	REQUEST_PAGING_RESULT				= 41;
	byte	REQUEST_PAGING_CLOSE				= 42;
	
	byte	RESPONSE_TYPE_0						= 0;
	byte	RESPONSE_TYPE_1						= 1;
	
	byte	RESPONSE_OK							= 0;
	byte	RESPONSE_EXCEPTION					= 1;
	
	byte	RESPONSE_STORED_PROCEDURE_VOID		= 0;
	byte	RESPONSE_STORED_PROCEDURE_RESULT	= 1;
	byte	RESPONSE_STORED_PROCEDURE_OBJECT	= 2;
}
