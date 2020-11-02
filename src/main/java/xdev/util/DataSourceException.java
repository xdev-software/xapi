package xdev.util;

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


public class DataSourceException extends Exception
{
	private static final long		serialVersionUID	= -5217989105348061093L;

	private transient DataSource	dataSource;


	public DataSourceException(DataSource dataSource)
	{
		this.dataSource = dataSource;
	}


	public DataSourceException(DataSource dataSource, String message)
	{
		super(message);
		this.dataSource = dataSource;
	}


	public DataSourceException(DataSource dataSource, Throwable cause)
	{
		super(cause);
		this.dataSource = dataSource;
	}


	public DataSourceException(DataSource dataSource, String message, Throwable cause)
	{
		super(message,cause);
		this.dataSource = dataSource;
	}


	public DataSource<?> getDataSource()
	{
		return dataSource;
	}
}
