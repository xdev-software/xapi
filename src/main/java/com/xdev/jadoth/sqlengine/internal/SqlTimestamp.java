
package com.xdev.jadoth.sqlengine.internal;

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


import static com.xdev.jadoth.sqlengine.SQL.LANG.NULL;
import static com.xdev.jadoth.sqlengine.SQL.Punctuation.apo;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.xdev.jadoth.sqlengine.SQL;
import com.xdev.jadoth.sqlengine.dbms.DbmsDMLAssembler;
import com.xdev.jadoth.sqlengine.exceptions.SQLUnparseableDateStringException;




/**
 * The Class SqlTimestamp.
 * 
 * @author Thomas Muenz
 */
public class SqlTimestamp extends SqlExpression
{
	///////////////////////////////////////////////////////////////////////////
	// constants        //
	/////////////////////

	private static final long serialVersionUID = -9026289741635623680L;

	/** The Constant date2TIMESTAMP. */
	public static final SimpleDateFormat date2TIMESTAMP = new SimpleDateFormat(SQL.FORMAT.TIMESTAMP);
	
	/** The Constant date2DATE. */
	public static final SimpleDateFormat date2DATE = new SimpleDateFormat(SQL.FORMAT.DATE);
	
	/** The Constant date2TIME. */
	public static final SimpleDateFormat date2TIME = new SimpleDateFormat(SQL.FORMAT.TIME);



	///////////////////////////////////////////////////////////////////////////
	// static methods   //
	/////////////////////

	/**
	 * Parses the long timestamp.
	 * 
	 * @param timestamp the timestamp
	 * @return the date
	 */
	protected static Date parseLongTimestamp(final long timestamp) {
		return new Date(timestamp);
	}

	/**
	 * To sql timestamp string.
	 * 
	 * @param d the d
	 * @return the string
	 */
	public static String toSQLTimestampString(final Date d) {
		return toSQLTimestampString(d, new StringBuilder(20)).toString();
	}

	/**
	 * To sql timestamp string.
	 * 
	 * @param d the d
	 * @param sb the sb
	 * @return the string builder
	 */
	public static StringBuilder toSQLTimestampString(final Date d, final StringBuilder sb) {
		return (d==null)?sb.append(NULL):sb.append(apo).append(date2TIMESTAMP.format(d)).append(apo);
	}

	/**
	 * Parses the date.
	 * 
	 * @param o the o
	 * @return the date
	 */
	protected static Date parseDate(final Object o) {
		if(o instanceof Date) {
			return (Date)o;
		}
		else if(o instanceof Long) {
			return parseLongTimestamp((Long)o);
		}
		
		// (18.04.2010)XXX: What about QuerPart instance and default assembly in its .toString()?
		String oString = o.toString();

		if(oString.charAt(oString.length() - 1) == '\'') {
			oString = oString.substring(0, oString.length() - 2);
		}
		if(oString.charAt(0) == '\'') {
			oString = oString.substring(1);
		}

		try {
			return date2TIMESTAMP.parse(oString);
		} catch (final ParseException e1) {
			try {
				return date2DATE.parse(oString);
			} catch (final ParseException e2) {
				try {
					return date2TIME.parse(oString);
				} catch (final ParseException e3) {
					throw new SQLUnparseableDateStringException(e3);
				}
			}
		}
	}

	

	///////////////////////////////////////////////////////////////////////////
	// constructors     //
	/////////////////////

	/**
	 * Instantiates a new sql timestamp.
	 * 
	 * @param date the date
	 */
	public SqlTimestamp(final Date date)
	{
		// (21.02.2010)TODO handle Timestamp ms
		super(date);
	}

	/**
	 * Instantiates a new sql timestamp.
	 * 
	 * @param o the o
	 */
	public SqlTimestamp(final Object o) {
		this(parseDate(o));
	}
	
	/**
	 * Instantiates a new sql timestamp.
	 * 
	 * @param timestamp the timestamp
	 */
	public SqlTimestamp(final long timestamp) {
		this(parseLongTimestamp(timestamp));
	}



	///////////////////////////////////////////////////////////////////////////
	// getters          //
	/////////////////////
	
	/**
	 * Gets the date.
	 * 
	 * @return the date
	 */
	public Date getDate() {
		return (Date)this.expression;
	}
	
	
	public DateFormat getDateFormat(final DbmsDMLAssembler<?> dbmsAdaptor)
	{
		return dbmsAdaptor.getDateFormatTIMESTAMP();
	}

	/**
	 * @param dmlAssembler
	 * @param sb
	 * @param indentLevel
	 * @param flags
	 * @return
	 */
	@Override
	protected StringBuilder assemble(final DbmsDMLAssembler<?> dmlAssembler, final StringBuilder sb, final int indentLevel, final int flags)
	{
		return dmlAssembler.assembleDateTimeExpression(this, sb);
	}

}
