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


import java.util.ArrayList;
import java.util.List;


//TODO javadoc (class description)
public class PrefetchedResult extends Result
{
	private final Result			result;
	private final int				columnCount;
	private final List<Object[]>	data;
	private int						index	= -1;
	
	
	/**
	 * Constructs a {@link PrefetchedResult} that is initialized with
	 * <code>result</code>. <br>
	 * 
	 * @param result
	 *            a {@link Result} that include the data for this
	 *            {@link PrefetchedResult}
	 * 
	 */
	public PrefetchedResult(Result result) throws DBException
	{
		this.result = result;
		
		columnCount = result.getColumnCount();
		data = new ArrayList();
		
		while(result.next())
		{
			Object[] row = new Object[columnCount];
			for(int col = 0; col < columnCount; col++)
			{
				row[col] = result.getObject(col);
			}
			data.add(row);
		}
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getColumnCount()
	{
		return columnCount;
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public ColumnMetaData getMetadata(int col)
	{
		return result.getMetadata(col);
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean next() throws DBException
	{
		return ++index < data.size();
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object getObject(int col) throws DBException
	{
		return data.get(index)[col];
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public int skip(int count) throws DBException
	{
		index += count;
		return count;
	}
	
	
	/**
	 * @since 3.2
	 */
	public void rewind()
	{
		index = -1;
	}
	
	
	/**
	 * @return the number of rows in this result
	 * 
	 * @since 4.0
	 */
	public int getRowCount()
	{
		return this.data.size();
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void close() throws DBException
	{
		result.close();
		data.clear();
	}
}
