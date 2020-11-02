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


/**
 * 
 * 
 * 
 * @author XDEV Software
 * 
 */
//TODO javadoc (class description)
public abstract class Transaction
{	
	private final DBDataSource<?>	dataSource;
	private Savepoint				savepoint;
	

	/**
	 * Constructs a new <code>Transaction</code> with the current
	 * {@link DBDataSource}.
	 * 
	 * 
	 * @throws DBException
	 *             if the no DBDataSource is available
	 * 
	 * @see DBUtils#getCurrentDataSource()
	 */
	public Transaction() throws DBException
	{
		this(DBUtils.getCurrentDataSource());
	}
	

	/**
	 * Constructs a new <code>Transaction</code> with the specified
	 * <code>dataSource</code>.
	 * 
	 * 
	 * @param dataSource
	 *            the dataSource which provides the connections
	 */
	public Transaction(DBDataSource<?> dataSource)
	{
		this.dataSource = dataSource;
	}
	

	/**
	 * Returns the {@link DBDataSource} of this {@link Transaction}.
	 * 
	 * @return the current {@link DBDataSource}
	 */
	public DBDataSource getDataSource()
	{
		return dataSource;
	}
	

	/**
	 * Starts the transaction and executes the statements. If a error is occurs
	 * in the {@link #write(DBConnection)} method a rollback is done.
	 * 
	 * @throws DBException
	 *             if a database access error occurs
	 */
	public final void execute() throws DBException
	{
		DBConnection<?> connection = getConnection();
		try
		{
			connection.beginTransaction();
			write(connection);
			connection.commit();
		}
		catch(DBException dbe)
		{
			rollback(connection);
			throw dbe;
		}
		finally
		{
			connection.close();
		}
	}


	protected DBConnection<?> getConnection() throws DBException
	{
		return dataSource.openConnection();
	}
	

	protected abstract void write(DBConnection<?> connection) throws DBException;
	

	protected void setSavepoint(DBConnection<?> connection) throws DBException
	{
		savepoint = connection.setSavepoint();
	}
	

	protected void rollback(DBConnection<?> connection) throws DBException
	{
		if(savepoint != null)
		{
			connection.rollback(savepoint);
		}
		else
		{
			connection.rollback();
		}
	}
}
