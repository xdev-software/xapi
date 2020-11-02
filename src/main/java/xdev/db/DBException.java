package xdev.db;

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


import java.io.IOException;
import java.sql.SQLException;

import xdev.util.DataSourceException;


/**
 * 
 * Exception thrown by methods of {@link DBDataSource}, in most cases the cause
 * is a {@link SQLException} or an {@link IOException}.
 * 
 * @author XDEV Software
 * 
 */
public class DBException extends DataSourceException
{
	private static final long	serialVersionUID	= -7508616275827344324L;
	

	/**
	 * Constructs a new <code>DBException</code> with the specified
	 * <code>dataSource</code>.
	 * 
	 * 
	 * @param dataSource
	 *            the data source where the error occurs
	 */
	public DBException(DBDataSource dataSource)
	{
		super(dataSource);
	}
	

	/**
	 * Constructs a new <code>DBException</code> with the specified
	 * <code>dataSource</code>.
	 * 
	 * 
	 * @param dataSource
	 *            the data source where the error occurs
	 * 
	 * @param message
	 *            the detail message. The detail message is saved for later
	 *            retrieval by the {@link #getMessage()} method.
	 * 
	 */
	public DBException(DBDataSource dataSource, String message)
	{
		super(dataSource,message);
	}
	

	/**
	 * Constructs a new <code>DBException</code> with the specified
	 * <code>dataSource</code>.
	 * 
	 * 
	 * @param dataSource
	 *            the data source where the error occurs
	 * 
	 * @param cause
	 *            the cause (which is saved for later retrieval by the
	 *            {@link #getCause()} method). (A <tt>null</tt> value is
	 *            permitted, and indicates that the cause is nonexistent or
	 *            unknown.)
	 */
	public DBException(DBDataSource dataSource, Throwable cause)
	{
		super(dataSource,cause);
	}
	

	/**
	 * Constructs a new <code>DBException</code> with the specified
	 * <code>dataSource</code>.
	 * 
	 * 
	 * @param dataSource
	 *            the data source where the error occurs
	 * 
	 * @param message
	 *            the detail message. The detail message is saved for later
	 *            retrieval by the {@link #getMessage()} method.
	 * 
	 * @param cause
	 *            the cause (which is saved for later retrieval by the
	 *            {@link #getCause()} method). (A <tt>null</tt> value is
	 *            permitted, and indicates that the cause is nonexistent or
	 *            unknown.)
	 */
	public DBException(DBDataSource dataSource, String message, Throwable cause)
	{
		super(dataSource,message,cause);
	}
	

	/**
	 * Return the data source where the error occurs.
	 * 
	 * @return the data source where the error occurs
	 */
	public DBDataSource<?> getDataSource()
	{
		return (DBDataSource)super.getDataSource();
	}
}
