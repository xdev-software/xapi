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


import java.io.Serializable;
import java.util.Arrays;

import xdev.lang.Copyable;
import xdev.util.MathUtils;


/**
 * This class represents a <code>index</code>.
 * <p>
 * It has properties like <code>name</code>, <code>type</code> and
 * <code>columns</code>.
 * </p>
 * 
 * @see DBMetaData
 * 
 * @author XDEV Software
 * 
 */
public class Index implements Copyable<Index>, Serializable
{
	private static final long	serialVersionUID	= -4491136367846546654L;
	
	
	
	/**
	 * A enumeration generally used for typify the {@link Index}.
	 * 
	 */
	public static enum IndexType
	{
		NORMAL,
		
		UNIQUE,
		
		PRIMARY_KEY
	}
	
	private String		name;
	private IndexType	type;
	private String[]	columns;
	
	
	/**
	 * Construct a new {@link Index} and initialize this with the
	 * <code>name</code>, <code>type</code> and <code>columns</code>.
	 * 
	 * @param name
	 *            the name of this {@link Index}
	 * 
	 * @param type
	 *            to typify this {@link Index}
	 * 
	 * @param columns
	 *            the columns on which the {@link Index} is
	 * 
	 * @see IndexType
	 */
	public Index(String name, IndexType type, String... columns)
	{
		this.name = name;
		this.type = type;
		this.columns = columns;
	}
	
	
	/**
	 * Sets the name for this {@link Index}.
	 * 
	 * @param name
	 *            the new name
	 */
	public void setName(String name)
	{
		this.name = name;
	}
	
	
	/**
	 * Returns the name of this {@link Index}.
	 * 
	 * @return the name of this {@link Index}
	 */
	public String getName()
	{
		return name;
	}
	
	
	/**
	 * Sets the {@link IndexType} for this {@link Index}.
	 * 
	 * @param type
	 *            the type for this {@link Index}. One of the following
	 *            constants defined in <code>IndexType</code>:
	 *            <ul>
	 *            <li><code>NORMAL</code>
	 *            <li><code>UNIQUE</code>
	 *            <li><code>PRIMARY_KEY</code>
	 *            </ul>
	 */
	public void setType(IndexType type)
	{
		this.type = type;
	}
	
	
	/**
	 * Returns the type of this {@link Index}.
	 * 
	 * @return the {@link IndexType} of this {@link Index}
	 */
	public IndexType getType()
	{
		return type;
	}
	
	
	/**
	 * Sets the columns for this {@link Index}.
	 * 
	 * @param columns
	 *            an array of strings with the columns
	 */
	public void setColumns(String[] columns)
	{
		this.columns = columns;
	}
	
	
	/**
	 * Returns the type of this {@link Index}.
	 * 
	 * @return a array of strings including the columns.
	 */
	public String[] getColumns()
	{
		return columns;
	}
	
	
	/**
	 * Returns <tt>true</tt> if this {@link Index} contains the specified
	 * column.
	 * 
	 * @param name
	 *            the column name
	 * 
	 * @return <tt>true</tt> if this {@link Index} contains the specified
	 *         column, otherwise <tt>false</tt>
	 */
	public boolean containsColumn(String name)
	{
		for(String column : columns)
		{
			if(column.equals(name))
			{
				return true;
			}
		}
		
		return false;
	}
	
	
	/**
	 * Returns <tt>true</tt> if this {@link Index} is a unique index.
	 * 
	 * 
	 * @return <tt>true</tt> if this {@link Index} is unique, otherwise
	 *         <tt>false</tt>
	 */
	public boolean isUnique()
	{
		switch(type)
		{
			case UNIQUE:
			case PRIMARY_KEY:
				return true;
		}
		
		return false;
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString()
	{
		return type + " " + name + " " + Arrays.toString(columns);
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Object obj)
	{
		if(obj == this)
		{
			return true;
		}
		
		if(obj instanceof Index)
		{
			Index index = (Index)obj;
			if(index.type == type && (type == IndexType.PRIMARY_KEY || index.name.equals(name))
					&& columns.length == index.columns.length)
			{
				String[] c1 = Arrays.copyOf(columns,columns.length);
				Arrays.sort(c1);
				String[] c2 = Arrays.copyOf(index.columns,index.columns.length);
				Arrays.sort(c2);
				
				for(int i = 0; i < c1.length; i++)
				{
					if(!c1[i].equalsIgnoreCase(c2[i]))
					{
						return false;
					}
				}
				
				return true;
			}
		}
		
		return false;
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode()
	{
		return MathUtils.computeHashDeep(name,type,columns);
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Index clone()
	{
		String[] columns = new String[this.columns.length];
		System.arraycopy(this.columns,0,columns,0,this.columns.length);
		return new Index(name,type,columns);
	}
}
