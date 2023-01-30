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
package xdev.db.jdbc;


import java.sql.Connection;

import xdev.db.AbstractDBDataSource;
import xdev.db.ConnectionInformation;
import xdev.db.ConnectionPool;
import xdev.db.DBException;

import com.xdev.jadoth.sqlengine.dbms.DbmsAdaptor;
import com.xdev.jadoth.sqlengine.exceptions.SQLEngineCouldNotConnectToDBException;
import com.xdev.jadoth.sqlengine.interfaces.ConnectionProvider;
import com.xdev.jadoth.sqlengine.internal.DatabaseGateway;


public abstract class JDBCDataSource<T extends JDBCDataSource<T, A>, A extends DbmsAdaptor<A>>
		extends AbstractDBDataSource<T>
{
	private final A				adaptor;
	private ConnectionProvider	connectionProvider;
	private DatabaseGateway<A>	gateway;
	

	public JDBCDataSource(A adaptor)
	{
		this.adaptor = adaptor;
	}
	

	@Override
	public abstract JDBCConnection openConnectionImpl() throws DBException;
	

	public A getDbmsAdaptor()
	{
		return this.adaptor;
	}
	

	@Override
	public void closeAllOpenConnections() throws DBException
	{
		if(this.connectionProvider != null)
		{
			this.connectionProvider.close();
			this.connectionProvider = null;
		}
	}
	

	@Override
	public void clearCaches()
	{
		this.connectionProvider = null;
		this.gateway = null;
	}
	

	public DatabaseGateway<A> getGateway()
	{
		if(this.gateway == null)
		{
			this.gateway = this.createGateway();
		}
		
		return this.gateway;
	}
	

	protected DatabaseGateway<A> createGateway()
	{
		return new DatabaseGateway(this.getConnectionProvider());
	}
	

	public ConnectionProvider<A> getConnectionProvider()
	{
		if(this.connectionProvider == null)
		{
			this.connectionProvider = this.createConnectionProvider();
		}
		
		return this.connectionProvider;
	}
	

	protected ConnectionProvider createConnectionProvider()
	{
		return new ConnectionPool(this.getConnectionPoolSize(),this.keepConnectionsAlive(),this.adaptor,
				this.getConnectionInformation(), this.connConfig == null ? -1 : this.connConfig.getMaxStatementsPerConnection());
	}
	

	protected abstract ConnectionInformation getConnectionInformation();
	

	final Connection connectImpl() throws DBException
	{
		try
		{
			return this.getConnectionProvider().getConnection();
		}
		catch(final SQLEngineCouldNotConnectToDBException e)
		{
			Throwable cause = e.getCause();
			if(cause == null)
			{
				cause = e;
			}
			throw new DBException(this,cause.getMessage(),cause);
		} 
	}
}
