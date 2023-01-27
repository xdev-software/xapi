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


import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

import xdev.db.DataType;
import xdev.db.Index;
import xdev.db.Index.IndexType;
import xdev.db.jdbc.JDBCDataSource;
import xdev.lang.StaticInstanceSupport;
import xdev.ui.text.TextFormat;
import xdev.vt.VirtualTable;
import xdev.vt.VirtualTableColumn;


/**
 * Virtual Table for pessimistic lock.
 * 
 * @author XDEV Software (RHHF)
 * @since 4.0
 * 
 */
public class PessimisticLockTable extends VirtualTable implements StaticInstanceSupport
{
	/**
	 * id.
	 */
	public final static VirtualTableColumn<Integer>	id;
	/**
	 * table name.
	 */
	public final static VirtualTableColumn<String>	tableName;
	/**
	 * table row identifier.
	 */
	public final static VirtualTableColumn<String>	tableRowIdentifier;
	/**
	 * valid until date.
	 */
	public final static VirtualTableColumn<Date>	validUntil;
	/**
	 * user string.
	 */
	public final static VirtualTableColumn<String>	userString;
	/**
	 * user Id.
	 */
	public final static VirtualTableColumn<Long>	userID;
	/**
	 * time on server.
	 */
	public final static VirtualTableColumn<Date>	serverTime;
	
	static
	{
		id = new VirtualTableColumn<Integer>(PessimisticLockTableConstants.ID);
		id.setType(DataType.INTEGER);
		id.setAutoIncrement(true);
		id.setDefaultValue(null);
		id.setPreferredWidth(100);
		id.setTextFormat(TextFormat.getPlainInstance());
		
		tableName = new VirtualTableColumn<String>(PessimisticLockTableConstants.TABLE_NAME);
		tableName.setType(DataType.VARCHAR,50);
		tableName.setDefaultValue("");
		tableName.setPreferredWidth(100);
		tableName.setTextFormat(TextFormat.getPlainInstance());
		
		tableRowIdentifier = new VirtualTableColumn<String>(
				PessimisticLockTableConstants.TABLE_ROW_IDENTIFIER);
		tableRowIdentifier.setType(DataType.VARCHAR,100);
		tableRowIdentifier.setDefaultValue(null);
		tableRowIdentifier.setPreferredWidth(100);
		tableRowIdentifier.setTextFormat(TextFormat.getPlainInstance());
		
		validUntil = new VirtualTableColumn<Date>(PessimisticLockTableConstants.VALID_UNTIL);
		validUntil.setType(DataType.TIMESTAMP);
		validUntil.setDefaultValue(null);
		validUntil.setPreferredWidth(100);
		validUntil.setTextFormat(TextFormat.getDateInstance(Locale.getDefault(),null,
				TextFormat.USE_DATE_AND_TIME,DateFormat.MEDIUM,DateFormat.MEDIUM));
		
		userString = new VirtualTableColumn<String>(PessimisticLockTableConstants.USER_STRING);
		userString.setType(DataType.VARCHAR,255);
		userString.setDefaultValue("");
		userString.setPreferredWidth(100);
		userString.setTextFormat(TextFormat.getPlainInstance());
		
		userID = new VirtualTableColumn<Long>(PessimisticLockTableConstants.USER_ID);
		userID.setType(DataType.BIGINT);
		userID.setDefaultValue(null);
		userID.setPreferredWidth(100);
		userID.setTextFormat(TextFormat.getNumberInstance(Locale.getDefault(),null,0,0,true,false));
		
		serverTime = new VirtualTableColumn<Date>(PessimisticLockTableConstants.SERVER_TIME);
		serverTime.setType(DataType.TIMESTAMP);
		serverTime.setDefaultValue(null);
		serverTime.setPreferredWidth(100);
		serverTime.setTextFormat(TextFormat.getDateInstance(Locale.getDefault(),null,
				TextFormat.USE_DATE_AND_TIME,DateFormat.MEDIUM,DateFormat.MEDIUM));
		
		serverTime.setPersistent(false);
	}
	
	private static JDBCDataSource					dataSource;
	
	
	public PessimisticLockTable(JDBCDataSource dataSource)
	{
		super(PessimisticLockTable.class.getName(),null,
				PessimisticLockTableConstants.PESSIMISTIC_LOCK_TABLE_NAME,id,tableName,
				tableRowIdentifier,validUntil,userString,userID,serverTime);
		
		// XXX maybe exception here!!!
		setDataSource(dataSource);
		PessimisticLockTable.dataSource = dataSource;
		
		setPrimaryColumn(id);
		
		addIndex(new Index("PK",IndexType.PRIMARY_KEY,PessimisticLockTableConstants.ID));
	}
	
	private final static PessimisticLockTable	VT	= new PessimisticLockTable(dataSource);
	
	
	/**
	 * Get {@link PessimisticLockTable} instance.
	 * 
	 * @return PessimisticLockTable
	 */
	public static PessimisticLockTable getInstance()
	{
		return VT;
	}
	
}
