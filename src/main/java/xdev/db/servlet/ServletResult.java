package xdev.db.servlet;

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

import xdev.db.ColumnMetaData;
import xdev.db.Result;
import xdev.io.XdevObjectInputStream;
import xdev.io.XdevObjectOutputStream;


public class ServletResult extends Result
{
	public static void write(Result result, XdevObjectOutputStream out) throws Exception
	{
		int colCount = result.getColumnCount();
		out.writeInt(colCount);

		for(int i = 0; i < colCount; i++)
		{
			out.writeObject(result.getMetadata(i));
		}

		while(result.next())
		{
			out.writeBoolean(true);
			for(int col = 0; col < colCount; col++)
			{
				out.writeObject(result.getObject(col));
			}
		}
		out.writeBoolean(false);
	}

	private int					colCount;
	private ColumnMetaData[]	metadata;

	private Object[][]			data;
	private int					currentRow		= -1;
	private int					traversedRows	= 0;


	public ServletResult(XdevObjectInputStream in) throws Exception
	{
		colCount = in.readInt();

		metadata = new ColumnMetaData[colCount];
		for(int i = 0; i < colCount; i++)
		{
			metadata[i] = (ColumnMetaData)in.readObject();
		}

		List<Object[]> list = new ArrayList();
		while(in.readBoolean())
		{
			Object[] rowData = new Object[colCount];
			for(int col = 0; col < colCount; col++)
			{
				rowData[col] = in.readObject();
			}
			list.add(rowData);
		}
		this.data = new Object[list.size()][];
		list.toArray(this.data);
	}


	@Override
	public synchronized boolean next()
	{
		Integer maxRowCount = getMaxRowCount();
		if(maxRowCount != null && traversedRows >= maxRowCount)
		{
			return false;
		}

		if(++currentRow < data.length)
		{
			traversedRows++;
			return true;
		}
		
		return false;
	}


	@Override
	public synchronized int skip(int count)
	{
		currentRow += count;
		return count;
	}


	@Override
	public Object getObject(int col)
	{
		return data[currentRow][col];
	}


	@Override
	public int getColumnCount()
	{
		return colCount;
	}


	@Override
	public ColumnMetaData getMetadata(int col)
	{
		return metadata[col];
	}


	@Override
	public void close()
	{
		metadata = null;
		data = null;
	}
}
