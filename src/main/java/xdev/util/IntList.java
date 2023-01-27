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
package xdev.util;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;

import xdev.util.logging.LoggerFactory;
import xdev.util.logging.XdevLogger;


public class IntList
{
	
	/**
	 * Logger instance for this class.
	 */
	private static final XdevLogger	log		= LoggerFactory.getLogger(IntList.class);
	
	private int[]					data;
	private int						size;
	private int						hash	= 0;
	
	
	public IntList()
	{
		this(10);
	}
	
	
	public IntList(int initialCapacity)
	{
		data = new int[initialCapacity];
		size = 0;
		hash = 0;
	}
	
	
	public IntList(int... values)
	{
		if(values != null && values.length > 0)
		{
			this.data = values;
			this.size = values.length;
			rehash();
		}
		else
		{
			data = new int[10];
			size = 0;
			hash = 0;
		}
	}
	
	
	/**
	 * Converts the String to numbers. e.g. "1,2,3,4" or with ranges
	 * "1-3,5,9-11,13"
	 * 
	 * @param str
	 */
	
	public IntList(String str)
	{
		this(10);
		
		StringTokenizer st = new StringTokenizer(str,",");
		while(st.hasMoreTokens())
		{
			String token = st.nextToken();
			int minus = token.indexOf('-',1);
			if(minus > 0)
			{
				int start = Integer.parseInt(token.substring(0,minus));
				int stop = Integer.parseInt(token.substring(minus + 1));
				for(int i = start; i <= stop; i++)
				{
					add(i);
				}
			}
			else
			{
				add(Integer.parseInt(token));
			}
		}
	}
	
	
	public int get(int index)
	{
		return data[index];
	}
	
	
	public synchronized void add(int value)
	{
		ensureCapacity(size + 1);
		data[size++] = value;
		
		rehash();
	}
	
	
	public synchronized void addAll(IntList list)
	{
		ensureCapacity(size + list.size);
		try
		{
			System.arraycopy(list.data,0,data,size,list.size);
		}
		catch(Exception e)
		{
			log.error(e);
		}
		size += list.size;
		
		rehash();
	}
	
	
	public int removeIndex(int index)
	{
		int oldValue = data[index];
		
		int numMoved = size - index - 1;
		if(numMoved > 0)
		{
			System.arraycopy(data,index + 1,data,index,numMoved);
		}
		size--;
		
		rehash();
		
		return oldValue;
	}
	
	
	public int removeValue(int value)
	{
		int count = 0;
		
		int index;
		while((index = indexOf(value)) >= 0)
		{
			removeIndex(index);
			count++;
		}
		
		rehash();
		
		return count;
	}
	
	
	public boolean contains(int value)
	{
		return indexOf(value) != -1;
	}
	
	
	public int indexOf(int value)
	{
		int[] data = this.data;
		int size = this.size;
		for(int i = 0; i < size; i++)
		{
			if(data[i] == value)
			{
				return i;
			}
		}
		return -1;
	}
	
	
	public int size()
	{
		return size;
	}
	
	
	public synchronized void clear()
	{
		for(int i = 0; i < size; i++)
		{
			data[i] = -1;
		}
		size = 0;
		
		rehash();
	}
	
	
	private void ensureCapacity(int minCapacity)
	{
		int oldCapacity = data.length;
		if(minCapacity > oldCapacity)
		{
			int oldData[] = data;
			int newCapacity = (oldCapacity * 3) / 2 + 1;
			if(newCapacity < minCapacity)
			{
				newCapacity = minCapacity;
			}
			data = new int[newCapacity];
			System.arraycopy(oldData,0,data,0,size);
		}
	}
	
	
	private void rehash()
	{
		hash = 0;
	}
	
	
	@Override
	public int hashCode()
	{
		if(hash == 0)
		{
			hash = Arrays.hashCode(data);
		}
		
		return hash;
	}
	
	
	@Override
	public boolean equals(Object obj)
	{
		if(obj instanceof IntList)
		{
			return ((IntList)obj).hashCode() == hashCode();
		}
		
		return super.equals(obj);
	}
	
	
	public int[] toArray()
	{
		return Arrays.copyOf(data,size);
	}
	
	
	public void sort()
	{
		if(size > 0)
		{
			Arrays.sort(data,0,size);
		}
	}
	
	
	public List<Range> getRanges()
	{
		List<Range> ranges = new ArrayList();
		
		Range range = null;
		for(int i = 0; i < size; i++)
		{
			if(range == null)
			{
				range = new Range(data[i]);
			}
			else if(range.stopIndex + 1 == data[i])
			{
				range.stopIndex++;
			}
			else
			{
				ranges.add(range);
				range = new Range(data[i]);
			}
		}
		if(range != null)
		{
			ranges.add(range);
		}
		
		return ranges;
	}
	
	
	
	public class Range
	{
		public int	startIndex;
		public int	stopIndex;
		
		
		public Range(int index)
		{
			this.startIndex = this.stopIndex = index;
		}
		
		
		@Override
		public String toString()
		{
			if(startIndex == stopIndex)
			{
				return String.valueOf(startIndex);
			}
			else
			{
				StringBuilder sb = new StringBuilder();
				sb.append(startIndex);
				sb.append("-");
				sb.append(stopIndex);
				return sb.toString();
			}
		}
	}
}
