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


/**
 * Abstract base class for delegating result types.
 * <p>
 * <i><b>This class is not intended to be subclassed by the user!</b></i>
 * 
 * @author XDEV Software
 * @since 3.1
 */
public abstract class DelegatingResult extends Result
{
	private final Result	delegate;
	

	public DelegatingResult(Result delegate)
	{
		this.delegate = delegate;
		
		setDataSource(delegate.getDataSource());
		setQueryInfo(delegate.getQueryInfo());
		setMaxRowCount(delegate.getMaxRowCount());
	}
	

	/**
	 * @return the result to which the calls are delegated to
	 */
	public Result getDelegate()
	{
		return delegate;
	}
	

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getColumnCount()
	{
		return delegate.getColumnCount();
	}
	

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ColumnMetaData getMetadata(int col)
	{
		return delegate.getMetadata(col);
	}
	

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean next() throws DBException
	{
		return delegate.next();
	}
	

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int skip(int count) throws DBException
	{
		return delegate.skip(count);
	}
	

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object getObject(int col) throws DBException
	{
		return delegate.getObject(col);
	}
	

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void close() throws DBException
	{
		delegate.close();
	}
}
