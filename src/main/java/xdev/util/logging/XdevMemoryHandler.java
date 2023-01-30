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
package xdev.util.logging;


import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

import xdev.util.systemproperty.IntegerSystemProperty;


/*
 * It is not possible to configure the handler via standard property file, 
 * because the required methods of LogManager (getIntProperty) are package private and can not 
 * be delegated at the moment.
 */

/**
 * 
 * Handler that buffers requests in a circular buffer in memory.
 * 
 * Normally this Handler simply stores incoming LogRecords into its memory
 * buffer and discards earlier records. This buffering is very cheap and avoids
 * formatting costs.
 * 
 * <P>
 * The {@link XdevMemoryHandler} supports following properties, which must be
 * set in the <code>System</code> properties. To change the properties in
 * Runtime in a type-safe manner you may use the {@link IntegerSystemProperty}
 * reference hold by {@link #SYSTEM_PROPERTY_STORAGE_SIZE}.
 * 
 * <P>
 * <TABLE BORDER>
 * <TR>
 * <TH>Name</TH>
 * <TH>Type</TH>
 * <TH>Description</TH>
 * </TR>
 * 
 * 
 * <TR>
 * <TD>xdev.util.logging.xdevmemoryhandler.storageSize</TD>
 * <TD>int</TD>
 * <TD>
 * The <code>xdev.util.logging.xdevmemoryhandler.storageSize</code> System
 * property can be used to specify the storage size for the
 * {@link XdevMemoryHandler}. The {@link XdevMemoryHandler} holds on to a
 * maximum of log records specified by this property. If the maximum is reached
 * earlier log records will be discarded.</TD>
 * </TR>
 * 
 * 
 * </TABLE>
 * 
 * 
 * 
 * 
 * 
 * @author XDEV Software (FH)
 * @author XDEV Software (RHHF)
 * 
 * @see IntegerSystemProperty
 * @since 3.2
 * 
 */
public class XdevMemoryHandler extends Handler
{
	/**
	 * specifies the storage size for the {@link XdevMemoryHandler}.
	 */
	private final int							maxSize;
	
	/**
	 * Store for {@link LogRecord}s.
	 */
	private final LinkedList<LogRecord>			store;
	
	/**
	 * The <code>xdev.util.logging.xdevhandler.storageSize</code> System
	 * property can be used to specify the storage size for the
	 * {@link XdevMemoryHandler}. The {@link XdevMemoryHandler} holds on to a
	 * maximum of log records specified by this property. If the maximum is
	 * reached earlier log records will be discarded.
	 */
	public final static IntegerSystemProperty	SYSTEM_PROPERTY_STORAGE_SIZE	= new IntegerSystemProperty(
																						"xdev.util.logging.xdevmemoryhandler.storageSize");
	
	/**
	 * Default value for {@link #maxSize}.
	 */
	private final static int					DEFAULT_STORAGE_SIZE			= 100;
	

	/**
	 * Creates a new {@link XdevMemoryHandler} instance.
	 */
	public XdevMemoryHandler()
	{
		
		this.maxSize = SYSTEM_PROPERTY_STORAGE_SIZE.getValue(DEFAULT_STORAGE_SIZE);
		
		store = new LinkedList<LogRecord>();
		
	}
	

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void publish(LogRecord record)
	{
		synchronized(store)
		{
			store.add(record);
			
			while(store.size() > maxSize)
			{
				store.removeFirst();
			}
		}
		
	}
	

	/**
	 * This class does not support the concept of flush. You may use methods
	 * calls to get the log records.
	 * 
	 * @see #getRecords()
	 */
	@Override
	public void flush()
	{
		/*
		 * This class does not support the concept of flush. You may use methods
		 * calls to get the log records.
		 */
	}
	

	/**
	 * Returns a {@link List} (defensive copy) of the {@link LogRecord}s held by
	 * this {@link XdevMemoryHandler} instance.
	 * 
	 * @return a {@link List} (defensive copy) of the {@link LogRecord}s held by
	 *         this {@link XdevMemoryHandler} instance.
	 */
	public List<LogRecord> getRecords()
	{
		synchronized(store)
		{
			return new ArrayList<LogRecord>(store);
		}
	}
	

	/**
	 * Returns the count of the records currently stored by this
	 * {@link XdevMemoryHandler}.
	 * 
	 * @return the count of the records currently stored by this
	 *         {@link XdevMemoryHandler}.
	 */
	public int getRecordCount()
	{
		return store.size();
	}
	

	/**
	 * Returns the reference to the log record specified by <code>index</code>.
	 * 
	 * @param index
	 *            of the log record to return.
	 * @return the reference to the log record specified by <code>index</code>.
	 */
	public LogRecord getRecord(int index)
	{
		return store.get(index);
	}
	

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void close() throws SecurityException
	{
		this.store.clear();
	}
}
