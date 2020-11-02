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

import xdev.lang.Copyable;
import xdev.lang.Nullable;
import xdev.util.MathUtils;


/**
 * This class includes information about the columns of the database table.
 * <p>
 * The class has properties like table, name, caption, type, typeName, length,
 * scale, defaultValue, nullable and autoIncrement value.
 * </p>
 * 
 * @author XDEV Software
 * 
 */
public class ColumnMetaData implements Comparable<ColumnMetaData>, Copyable<ColumnMetaData>,
		Serializable
{
	private static final long	serialVersionUID	= 7510358852298733757L;
	
	private String				table;
	private String				name;
	private String				caption;
	
	private DataType			type;
	private String				typeName;
	private int					length;
	private int					scale;
	
	private Object				defaultValue;
	
	private boolean				nullable;
	private boolean				autoIncrement;
	

	/**
	 * Initializes a new instance of a {@link ColumnMetaData}.
	 * <p>
	 * The properties of this {@link ColumnMetaData} are set according to the
	 * specified parameters.
	 * </p>
	 * 
	 * @param table
	 *            the name of the table
	 * 
	 * @param name
	 *            the column name
	 * 
	 * @param caption
	 *            the caption of the column
	 * 
	 * @param type
	 *            the {@link DataType} of the column
	 * 
	 * @param length
	 *            the length (number of characters)
	 * 
	 * @param scale
	 *            the number of digits right of the decimal point
	 * 
	 * @param defaultValue
	 *            the default value of the column
	 * 
	 * @param nullable
	 *            <code>true</code> if the column can be <code>null</code>,
	 *            otherwise <code>false</code>
	 * 
	 * @param autoIncrement
	 *            <code>true</code> if the column has an auto value, otherwise
	 *            <code>false</code>
	 * 
	 */
	public ColumnMetaData(String table, String name, String caption, DataType type, int length,
			int scale, Object defaultValue, boolean nullable, boolean autoIncrement)
	{
		this.table = table;
		this.name = name;
		this.caption = caption;
		this.type = type;
		this.length = length;
		this.scale = scale;
		this.defaultValue = defaultValue;
		this.nullable = nullable;
		this.autoIncrement = autoIncrement;
	}
	

	/**
	 * Returns the table name of this {@link ColumnMetaData}.
	 * 
	 * @return the name of the table.
	 */
	public String getTable()
	{
		return table;
	}
	

	/**
	 * Sets the table name.
	 * 
	 * @param table
	 *            the name of the table
	 */
	public void setTable(String table)
	{
		this.table = table;
	}
	

	/**
	 * Returns the column name of this {@link ColumnMetaData}.
	 * 
	 * @return the name of the column.
	 */
	public String getName()
	{
		return name;
	}
	

	/**
	 * Sets the column name of this {@link ColumnMetaData}.
	 * 
	 * @param name
	 *            the name of the column
	 */
	public void setName(String name)
	{
		this.name = name;
	}
	

	/**
	 * Returns the column caption of this {@link ColumnMetaData}.
	 * 
	 * @return the caption of the column.
	 */
	public String getCaption()
	{
		return caption;
	}
	

	/**
	 * Sets the column caption of this {@link ColumnMetaData}.
	 * 
	 * @param caption
	 *            the column caption
	 */
	public void setCaption(String caption)
	{
		this.caption = caption;
	}
	

	/**
	 * Returns the columns {@link DataType} of this {@link ColumnMetaData}.
	 * 
	 * @return the columns {@link DataType}
	 */
	public DataType getType()
	{
		return type;
	}
	

	/**
	 * Sets the data type of this {@link ColumnMetaData}.
	 * 
	 * @param type
	 *            the {@link DataType} to set
	 */
	public void setType(DataType type)
	{
		this.type = type;
	}
	

	/**
	 * Returns the type name of this {@link ColumnMetaData}.
	 * 
	 * @return the type name or <code>null</code> if not available
	 */
	@Nullable
	public String getTypeName()
	{
		return typeName;
	}
	

	/**
	 * Sets the name of the type.
	 * 
	 * @param typeName
	 *            the name of the type
	 */
	public void setTypeName(String typeName)
	{
		this.typeName = typeName;
	}
	

	/**
	 * Returns the column length of this {@link ColumnMetaData}.
	 * 
	 * @return the length of the column.
	 */
	public int getLength()
	{
		return length;
	}
	

	/**
	 * Sets the length of the column.
	 * 
	 * @param length
	 *            the column length
	 */
	public void setLength(int length)
	{
		this.length = length;
	}
	

	/**
	 * Returns the scale of the column.
	 * 
	 * @return scale the number of digits right of the decimal point (defaults
	 *         to 0).
	 */
	public int getScale()
	{
		return scale;
	}
	

	/**
	 * Sets the scale of the column.
	 * 
	 * @param scale
	 *            the scale to set
	 */
	public void setScale(int scale)
	{
		this.scale = scale;
	}
	

	/**
	 * Returns the default value of the column.
	 * 
	 * @return the default value
	 */
	public Object getDefaultValue()
	{
		return defaultValue;
	}
	

	/**
	 * Sets the default value.
	 * 
	 * @param defaultValue
	 *            the default value
	 */
	public void setDefaultValue(Object defaultValue)
	{
		this.defaultValue = defaultValue;
	}
	

	/**
	 * Returns <code>true</code> if the column allowed the value
	 * <code>null</code>.
	 * 
	 * @return <code>true</code> if <code>null</code> is allowed, otherwise
	 *         <code>false</code>
	 */
	public boolean isNullable()
	{
		return nullable;
	}
	

	/**
	 * Sets the nullable flag for this {@link ColumnMetaData}.
	 * 
	 * @param nullable
	 *            <code>true</code> if the column allowed the value
	 *            <code>null</code>, otherwise <code>false</code>
	 */
	public void setNullable(boolean nullable)
	{
		this.nullable = nullable;
	}
	

	/**
	 * Returns <code>true</code> if the column allowed auto increment values.
	 * 
	 * @return <code>true</code> if auto increment values are allowed, otherwise
	 *         <code>false</code>
	 */
	public boolean isAutoIncrement()
	{
		return autoIncrement;
	}
	

	/**
	 * Sets the auto increment flag for this {@link ColumnMetaData}.
	 * 
	 * @param autoIncrement
	 *            <code>true</code> if the column has an auto value, otherwise
	 *            <code>false</code>
	 */
	public void setAutoIncrement(boolean autoIncrement)
	{
		this.autoIncrement = autoIncrement;
	}
	

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString()
	{
		return name;
	}
	

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int compareTo(ColumnMetaData other)
	{
		return name.compareTo(other.name);
	}
	

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode()
	{
		return MathUtils.computeHash(table,name,caption,type,length,scale,defaultValue,nullable,
				autoIncrement);
	}
	

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Object obj)
	{
		if(this == obj)
		{
			return true;
		}
		if(obj == null)
		{
			return false;
		}
		if(!(obj instanceof ColumnMetaData))
		{
			return false;
		}
		ColumnMetaData other = (ColumnMetaData)obj;
		if(autoIncrement != other.autoIncrement)
		{
			return false;
		}
		if(caption == null)
		{
			if(other.caption != null)
			{
				return false;
			}
		}
		else if(!caption.equals(other.caption))
		{
			return false;
		}
		if(!areDefaultValuesEqual(defaultValue,other.defaultValue))
		{
			return false;
		}
		if(length != other.length)
		{
			return false;
		}
		if(name == null)
		{
			if(other.name != null)
			{
				return false;
			}
		}
		else if(!name.equals(other.name))
		{
			return false;
		}
		if(nullable != other.nullable)
		{
			return false;
		}
		if(scale != other.scale)
		{
			return false;
		}
		if(table == null)
		{
			if(other.table != null)
			{
				return false;
			}
		}
		else if(!table.equals(other.table))
		{
			return false;
		}
		if(type == null)
		{
			if(other.type != null)
			{
				return false;
			}
		}
		else if(!type.equals(other.type))
		{
			return false;
		}
		return true;
	}
	

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ColumnMetaData clone()
	{
		return new ColumnMetaData(table,name,caption,type,length,scale,defaultValue,nullable,
				autoIncrement);
	}
	

	public static boolean areDefaultValuesEqual(Object def1, Object def2)
	{
		if(def1 == def2)
		{
			return true;
		}
		
		if(def1 == null || def2 == null)
		{
			return false;
		}
		
		if(def1.equals(def2))
		{
			return true;
		}
		
		if(def1 instanceof Number && def2 instanceof Number)
		{
			return ((Number)def1).doubleValue() == ((Number)def2).doubleValue();
		}
		
		return false;
	}
}
