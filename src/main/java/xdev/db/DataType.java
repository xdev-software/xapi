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


import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import xdev.vt.XdevBlob;
import xdev.vt.XdevClob;

import com.xdev.jadoth.sqlengine.SQL.DATATYPE;


/**
 * <P>
 * The class that defines the constants that are used to identify SQL types.
 * 
 * 
 * @author XDEV Software
 * 
 */
public enum DataType
{
	/**
	 * 8-bit integer value between 0 and 255, signed or unsigned.
	 */
	TINYINT(Byte.class, DATATYPE.TINYINT),
	
	/**
	 * 16-bit signed integer value between -32768 and 32767.
	 */
	SMALLINT(Short.class, DATATYPE.SMALLINT),
	
	/**
	 * 32-bit signed integer value between -2147483648 and 2147483647.
	 */
	INTEGER(Integer.class, DATATYPE.INT),
	
	/**
	 * 64-bit signed integer value between -9223372036854775808 and
	 * 9223372036854775807.
	 */
	BIGINT(Long.class, DATATYPE.BIGINT),
	
	/**
	 * Single precision floating point number that supports 7 digits of
	 * mantissa.
	 */
	REAL(Float.class, DATATYPE.REAL),
	
	/**
	 * Double precision floating point number that supports 15 digits of
	 * mantissa.
	 */
	DOUBLE(Double.class, DATATYPE.DOUBLE),
	
	/**
	 * Double precision floating point number that supports 15 digits of
	 * mantissa, basically the same type as {@link #DOUBLE}, which should be
	 * preferred instead of FLOAT.
	 */
	FLOAT(Double.class, DATATYPE.FLOAT),
	
	/**
	 * Severe fixed-precision decimal number which takes precision and scale
	 * parameters.
	 */
	NUMERIC(Double.class, DATATYPE.NUMERIC),
	
	/**
	 * Lax fixed-precision decimal number which takes precision and scale
	 * parameters.
	 */
	DECIMAL(Double.class, DATATYPE.DECIMAL),
	
	/**
	 * 1-bit value, which is either 1 or 0, true of false.
	 */
	BOOLEAN(Boolean.class, DATATYPE.BOOLEAN),
	
	/**
	 * Fixed length character string.
	 */
	CHAR(String.class, DATATYPE.CHAR),
	
	/**
	 * Small variable length character string with length param.
	 */
	VARCHAR(String.class, DATATYPE.VARCHAR),
	
	/**
	 * Big variable length character string with length param.
	 */
	LONGVARCHAR(String.class, DATATYPE.LONGVARCHAR),
	
	/**
	 * Big variable length character string.
	 */
	CLOB(XdevClob.class, DATATYPE.CLOB),
	
	/**
	 * Fixed length binary value.
	 */
	BINARY(byte[].class, DATATYPE.BINARY),
	
	/**
	 * Small variable length binary value with length param.
	 */
	VARBINARY(byte[].class, DATATYPE.VARBINARY),
	
	/**
	 * Big variable length binary value with length param.
	 */
	LONGVARBINARY(byte[].class, DATATYPE.LONGVARBINARY),
	
	/**
	 * Big variable length binary value.
	 */
	BLOB(XdevBlob.class, DATATYPE.BLOB),
	
	/**
	 * Day, month and year.
	 */
	DATE(java.util.Date.class, DATATYPE.DATE),
	
	/**
	 * Hours, minutes and seconds.
	 */
	TIME(java.util.Date.class, DATATYPE.TIME),
	
	/**
	 * Object and Others
	 */
	OBJECT(Object.class, DATATYPE.OBJECT),
	
	/**
	 * {@link #DATE} + {@link #TIME} + Nanoseconds.
	 */
	TIMESTAMP(java.util.Date.class, DATATYPE.TIMESTAMP);
	
	private Class<?>	javaClass;
	private DATATYPE	sqlType;
	
	
	/**
	 * Instantiates a new DataType.
	 * 
	 * @param javaClass
	 *            the java class
	 * 
	 * @param sqlType
	 *            the sql typ
	 * 
	 */
	private DataType(Class<?> javaClass, DATATYPE sqlType)
	{
		this.javaClass = javaClass;
		this.sqlType = sqlType;
	}
	
	
	/**
	 * Gets the java class for this {@link DataType}.
	 * 
	 * @return the java class
	 */
	public Class<?> getJavaClass()
	{
		return javaClass;
	}
	
	
	/**
	 * Gets the sql type for this {@link DataType}.
	 * 
	 * @return the {@link DATATYPE}
	 */
	public DATATYPE getSqlType()
	{
		return sqlType;
	}
	
	
	/**
	 * Check if <code>this</code> is an integer {@link DataType}.
	 * 
	 * <br>
	 * Integer {@link DataType} are:
	 * <ul>
	 * <li>{@link #TINYINT}
	 * <li>{@link #SMALLINT}
	 * <li>{@link #INTEGER}
	 * <li>{@link #BIGINT}
	 * </ul>
	 * 
	 * @return <code>true</code> if this is an integer {@link DataType},
	 *         otherwise <code>false</code>
	 */
	public boolean isInt()
	{
		switch(this)
		{
			case TINYINT:
			case SMALLINT:
			case INTEGER:
			case BIGINT:
				return true;
		}
		return false;
	}
	
	
	/**
	 * Check if <code>this</code> is an decimal {@link DataType}.
	 * 
	 * <br>
	 * Decimal {@link DataType} are:
	 * <ul>
	 * <li>{@link #REAL}
	 * <li>{@link #FLOAT}
	 * <li>{@link #DOUBLE}
	 * <li>{@link #DECIMAL}
	 * <li>{@link #NUMERIC}
	 * </ul>
	 * 
	 * @return <code>true</code> if this is an decimal {@link DataType},
	 *         otherwise <code>false</code>
	 */
	public boolean isDecimal()
	{
		switch(this)
		{
			case REAL:
			case FLOAT:
			case DOUBLE:
			case DECIMAL:
			case NUMERIC:
				return true;
		}
		return false;
	}
	
	
	/**
	 * Check if <code>this</code> is numeric {@link DataType}.
	 * 
	 * <br>
	 * Numeric {@link DataType} are:
	 * <ul>
	 * <li>{@link #REAL}
	 * <li>{@link #FLOAT}
	 * <li>{@link #DOUBLE}
	 * <li>{@link #DECIMAL}
	 * <li>{@link #NUMERIC} *
	 * <li>{@link #TINYINT}
	 * <li>{@link #SMALLINT}
	 * <li>{@link #INTEGER}
	 * <li>{@link #BIGINT}
	 * </ul>
	 * 
	 * @return <code>true</code> if this is a numeric value {@link DataType},
	 *         otherwise <code>false</code>
	 * @since 4.0
	 */
	public boolean isNumeric()
	{
		switch(this)
		{
			case TINYINT:
			case SMALLINT:
			case INTEGER:
			case BIGINT:
			case REAL:
			case FLOAT:
			case DOUBLE:
			case DECIMAL:
			case NUMERIC:
				return true;
		}
		return false;
	}
	
	
	/**
	 * Check if <code>this</code> is an string {@link DataType}.
	 * 
	 * <br>
	 * String {@link DataType} are:
	 * <ul>
	 * <li>{@link #CHAR}
	 * <li>{@link #VARCHAR}
	 * <li>{@link #LONGVARCHAR}
	 * <li>{@link #CLOB}
	 * </ul>
	 * 
	 * @return <code>true</code> if this is an string {@link DataType},
	 *         otherwise <code>false</code>
	 */
	public boolean isString()
	{
		switch(this)
		{
			case CHAR:
			case VARCHAR:
			case LONGVARCHAR:
			case CLOB:
				return true;
		}
		return false;
	}
	
	
	/**
	 * Check if <code>this</code> is an blob {@link DataType}.
	 * 
	 * <br>
	 * Blob {@link DataType} are:
	 * <ul>
	 * <li>{@link #BINARY}
	 * <li>{@link #VARBINARY}
	 * <li>{@link #LONGVARBINARY}
	 * <li>{@link #BLOB}
	 * </ul>
	 * 
	 * @return <code>true</code> if this is an blob {@link DataType}, otherwise
	 *         <code>false</code>
	 */
	public boolean isBlob()
	{
		switch(this)
		{
			case BINARY:
			case VARBINARY:
			case LONGVARBINARY:
			case BLOB:
				return true;
		}
		return false;
	}
	
	
	/**
	 * Check if <code>this</code> is an date {@link DataType}.
	 * 
	 * <br>
	 * Date {@link DataType} are:
	 * <ul>
	 * <li>{@link #DATE}
	 * <li>{@link #TIME}
	 * <li>{@link #TIMESTAMP}
	 * </ul>
	 * 
	 * @return <code>true</code> if this is an date {@link DataType}, otherwise
	 *         <code>false</code>
	 */
	public boolean isDate()
	{
		switch(this)
		{
			case DATE:
			case TIME:
			case TIMESTAMP:
				return true;
		}
		return false;
	}
	
	
	/**
	 * Verify if this {@link DataType} has a length.
	 * 
	 * @return <code>true</code> if the {@link DataType} has a length, otherwise
	 *         <code>false</code>
	 * 
	 * @see #hasScale()
	 */
	public boolean hasLength()
	{
		if(hasScale())
		{
			return true;
		}
		
		switch(this)
		{
			case CHAR:
			case VARCHAR:
			case LONGVARCHAR:
			case BINARY:
			case VARBINARY:
			case LONGVARBINARY:
			{
				return true;
			}
		}
		
		return false;
	}
	
	
	/**
	 * Verify if this {@link DataType} has a scale.
	 * 
	 * @return <code>true</code> if the {@link DataType} has a scale, otherwise
	 *         <code>false</code>
	 * 
	 * @see #hasLength()
	 */
	public boolean hasScale()
	{
		switch(this)
		{
			case DECIMAL:
			case NUMERIC:
				return true;
		}
		return false;
	}
	
	
	/**
	 * Returns a {@link List} containing {@link DataType}s which are
	 * <code>String</code> typed.
	 * 
	 * 
	 * @return string typed {@link DataType}s.
	 * 
	 * @since 4.0
	 */
	public static List<DataType> getStringTypes()
	{
		List<DataType> typeList = new ArrayList<DataType>();
		
		typeList.add(DataType.CHAR);
		typeList.add(DataType.VARCHAR);
		typeList.add(DataType.LONGVARCHAR);
		typeList.add(DataType.CLOB);
		
		return typeList;
	}
	
	
	/**
	 * Returns a {@link List} containing {@link DataType}s which are
	 * <code>Number</code> typed.
	 * 
	 * 
	 * @return number typed {@link DataType}s.
	 * 
	 * @since 4.0
	 */
	public static List<DataType> getNumericTypes()
	{
		List<DataType> typeList = new ArrayList<DataType>();
		
		typeList.add(DataType.TINYINT);
		typeList.add(DataType.SMALLINT);
		typeList.add(DataType.INTEGER);
		typeList.add(DataType.BIGINT);
		typeList.add(DataType.REAL);
		typeList.add(DataType.FLOAT);
		typeList.add(DataType.DOUBLE);
		typeList.add(DataType.DECIMAL);
		typeList.add(DataType.NUMERIC);
		
		return typeList;
	}
	
	
	/**
	 * Returns a {@link List} containing {@link DataType}s which are
	 * <code>Binary</code> typed.
	 * 
	 * 
	 * @return binary typed {@link DataType}s.
	 * 
	 * @since 4.0
	 */
	public static List<DataType> getBlobTypes()
	{
		List<DataType> typeList = new ArrayList<DataType>();
		
		typeList.add(DataType.BINARY);
		typeList.add(DataType.VARBINARY);
		typeList.add(DataType.LONGVARBINARY);
		typeList.add(DataType.BLOB);
		
		return typeList;
	}
	
	
	/**
	 * Returns a {@link List} containing {@link DataType}s which are
	 * <code>Date</code> typed.
	 * 
	 * 
	 * @return date typed {@link DataType}s.
	 * 
	 * @since 4.0
	 */
	public static List<DataType> getDateTypes()
	{
		List<DataType> typeList = new ArrayList<DataType>();
		
		typeList.add(DataType.DATE);
		typeList.add(DataType.TIME);
		typeList.add(DataType.TIMESTAMP);
		typeList.add(DataType.BLOB);
		
		return typeList;
	}
	
	
	/**
	 * Returns a {@link List} containing {@link DataType}s which are
	 * <code>Decimal</code> typed.
	 * 
	 * 
	 * @return decimal typed {@link DataType}s.
	 * 
	 * @since 4.0
	 */
	public static List<DataType> getDecimalTypes()
	{
		List<DataType> typeList = new ArrayList<DataType>();
		
		typeList.add(DataType.REAL);
		typeList.add(DataType.FLOAT);
		typeList.add(DataType.DOUBLE);
		typeList.add(DataType.DECIMAL);
		typeList.add(DataType.NUMERIC);
		
		return typeList;
	}
	
	
	/**
	 * Returns a {@link List} containing {@link DataType}s which are
	 * <code>Integer</code> typed.
	 * 
	 * 
	 * @return integer typed {@link DataType}s.
	 * 
	 * @since 4.0
	 */
	public static List<DataType> getIntTypes()
	{
		List<DataType> typeList = new ArrayList<DataType>();
		
		typeList.add(DataType.TINYINT);
		typeList.add(DataType.SMALLINT);
		typeList.add(DataType.INTEGER);
		typeList.add(DataType.BIGINT);
		
		return typeList;
	}
	
	
	/**
	 * Returns the matching {@link DataType} for the <code>sqlType</code>.
	 * 
	 * @param sqlType
	 *            the sql type
	 * 
	 * @return the matching {@link DataType}
	 */
	public static DataType get(int sqlType)
	{
		switch(sqlType)
		{
			case Types.TINYINT:
				return TINYINT;
			case Types.SMALLINT:
				return SMALLINT;
			case Types.INTEGER:
				return INTEGER;
			case Types.BIGINT:
				return BIGINT;
				
			case Types.FLOAT:
				return FLOAT;
			case Types.REAL:
				return REAL;
			case Types.DOUBLE:
				return DOUBLE;
			case Types.NUMERIC:
				return NUMERIC;
			case Types.DECIMAL:
				return DECIMAL;
				
			case Types.BIT:
			case Types.BOOLEAN:
				return BOOLEAN;
				
			case Types.CHAR:
			case Types.NCHAR:
				return CHAR;
			case Types.VARCHAR:
			case Types.NVARCHAR:
				return VARCHAR;
			case Types.LONGVARCHAR:
			case Types.LONGNVARCHAR:
				return LONGVARCHAR;
			case Types.CLOB:
			case Types.NCLOB:
				return CLOB;
				
			case Types.BINARY:
				return BINARY;
			case Types.VARBINARY:
				return VARBINARY;
			case Types.LONGVARBINARY:
				return LONGVARBINARY;
			case Types.BLOB:
				return BLOB;
				
			case Types.DATE:
				return DATE;
				
			case Types.TIME:
				return TIME;
				
			case Types.TIMESTAMP:
				return TIMESTAMP;
				
			default:
				return VARCHAR;
		}
	}
}
