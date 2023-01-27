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
package xdev.vt;


import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import xdev.db.sql.Condition;
import xdev.db.sql.WHERE;
import xdev.vt.VirtualTable.VirtualTableRow;


/**
 * A container for the values of a {@link VirtualTableRow} which may belong to a
 * key of the according {@link VirtualTable}.
 * <p>
 * This class is not intended to be subclassed.<br>
 * All values are unmodifiable.
 * </p>
 * 
 * @author XDEV Software
 * @since 3.0
 */

public class KeyValues implements Serializable
{
	/**
	 * 
	 */
	private static final long					serialVersionUID	= 1626794227482872347L;
	
	private final VirtualTable					virtualTable;
	private final LinkedHashMap<String, Object>	values;
	private boolean								isPrimaryKey		= false;
	private String								hash;
	
	
	/**
	 * Stores all values of the primary key columns of <code>record</code>.
	 * 
	 * @param record
	 *            The record of a {@link VirtualTable}
	 */
	public KeyValues(VirtualTableRow record)
	{
		if(record == null)
		{
			throw new IllegalArgumentException("record cannot be null");
		}
		
		this.virtualTable = record.getVirtualTable();
		VirtualTableColumn[] pkColumns = this.virtualTable.getPrimaryKeyColumns();
		if(pkColumns.length == 0)
		{
			throw new IllegalArgumentException("no primary key defined in '"
					+ this.virtualTable.getName() + "'");
		}
		
		values = new LinkedHashMap();
		for(VirtualTableColumn col : pkColumns)
		{
			values.put(col.getName(),record.get(col));
		}
		
		this.isPrimaryKey = true;
	}
	
	
	/**
	 * Stores all values of the given columns of <code>record</code>.
	 * 
	 * @param record
	 *            The record of a {@link VirtualTable}
	 * @param columnNames
	 *            the column names to get the values for
	 */
	public KeyValues(VirtualTableRow record, String[] columnNames)
	{
		if(record == null)
		{
			throw new IllegalArgumentException("record cannot be null");
		}
		
		this.virtualTable = record.getVirtualTable();
		
		values = new LinkedHashMap();
		for(String columnName : columnNames)
		{
			values.put(columnName,record.get(columnName));
		}
	}
	
	
	/**
	 * Stores all values of the given columns of <code>record</code>.
	 * 
	 * @param record
	 *            The record of a {@link VirtualTable}
	 * @param columns
	 *            the column names to get the values for
	 * @since 3.2
	 */
	public KeyValues(VirtualTableRow record, VirtualTableColumn[] columns)
	{
		if(record == null)
		{
			throw new IllegalArgumentException("record cannot be null");
		}
		
		this.virtualTable = record.getVirtualTable();
		
		values = new LinkedHashMap();
		for(VirtualTableColumn column : columns)
		{
			values.put(column.getName(),record.get(column));
		}
	}
	
	
	/**
	 * Stores a single key value pair and the {@link VirtualTable}.
	 * 
	 * @param virtualTable
	 *            The used {@link VirtualTable}
	 * @param singleKeyName
	 *            The column name of the single key column
	 * @param singleKeyValue
	 *            The value of the single key
	 */
	public KeyValues(VirtualTable virtualTable, String singleKeyName, Object singleKeyValue)
	{
		if(virtualTable == null)
		{
			throw new IllegalArgumentException("virtualTable cannot be null");
		}
		
		if(singleKeyName == null)
		{
			throw new IllegalArgumentException("singleKeyName cannot be null");
		}
		
		this.virtualTable = virtualTable;
		
		values = new LinkedHashMap();
		values.put(singleKeyName,singleKeyValue);
	}
	
	
	/**
	 * Stores all <code>values</code> as the key.
	 * <p>
	 * <strong> It is highly recommended to use a {@link LinkedHashMap}, because
	 * the order of the key-value-pairs is important. </strong>
	 * </p>
	 * 
	 * @param virtualTable
	 *            The used {@link VirtualTable}
	 * @param values
	 *            The key value pairs
	 */
	public KeyValues(VirtualTable virtualTable, Map<String, Object> values)
	{
		if(virtualTable == null)
		{
			throw new IllegalArgumentException("virtualTable cannot be null");
		}
		
		if(values == null)
		{
			throw new IllegalArgumentException("values cannot be null");
		}
		
		this.virtualTable = virtualTable;
		this.values = new LinkedHashMap(values);
	}
	
	
	/**
	 * Returns the {@link VirtualTable} to which this {@link KeyValues} refers.
	 * 
	 * @return The {@link VirtualTable} of this {@link KeyValues}
	 */
	public VirtualTable getVirtualTable()
	{
		return virtualTable;
	}
	
	
	/**
	 * Returns the column names of the primary key or an empty array if no
	 * values are set.
	 * 
	 * @return An array of the primary key's column names.
	 * @see #isEmpty()
	 */
	public String[] getColumnNames()
	{
		return values.keySet().toArray(new String[values.size()]);
	}
	
	
	/**
	 * Returns the column names of the primary key.
	 * 
	 * @return An {@link Iterable} over primary key's column names.
	 */
	public Iterable<String> columnNames()
	{
		return values.keySet();
	}
	
	
	/**
	 * Returns the primary key value mapped to <code>columnName</code>. If no
	 * column is found <code>null</code> is returned.
	 * 
	 * @param columnName
	 *            The column name of the primary key column
	 * @return The primary key value mapped to <code>columnName</code> or
	 *         <code>null</code>.
	 */
	public Object getValue(String columnName)
	{
		return values.get(columnName);
	}
	
	
	/**
	 * Returns the primary key values mapped to <code>columnNames</code>.
	 * 
	 * @param columnNames
	 *            The column names to get the values for
	 * @return The key value mapped to <code>columnNames</code>.
	 * @since 3.2
	 */
	public Object[] getValues(String[] columnNames)
	{
		int c = columnNames.length;
		Object[] values = new Object[c];
		for(int i = 0; i < c; i++)
		{
			values[i] = getValue(columnNames[i]);
		}
		return values;
	}
	
	
	/**
	 * Returns <code>true</code> if no key-value mappings are present,
	 * <code>false</code> otherwise.
	 * 
	 * @return <code>true</code> if no key-value mappings are present
	 */
	public boolean isEmpty()
	{
		return values.isEmpty();
	}
	
	
	/**
	 * Returns the number of key-value pairs in this {@link KeyValues}.
	 * 
	 * @return The number of key-value pairs in this {@link KeyValues}
	 */
	public int getColumnCount()
	{
		return values.size();
	}
	
	
	/**
	 * Compares all values of this {@link KeyValues} to the according values in
	 * <code>record</code>.
	 * <p>
	 * Returns <code>true</code>, if and only if all values of the key columns
	 * are equal.
	 * 
	 * 
	 * @param record
	 *            The {@link VirtualTableRow} to check
	 * @return <code>true</code> if <code>record</code> matches this
	 *         {@link KeyValues}, <code>false</code> otherwise
	 */
	public boolean equals(VirtualTableRow record)
	{
		if(isPrimaryKey)
		{
			return hash().equals(record.primaryKeyHash);
		}
		
		for(String name : values.keySet())
		{
			Object thisVal = values.get(name);
			int column = virtualTable.getColumnIndex(name);
			Object otherVal = record.get(column);
			if(!VirtualTable.equals(thisVal,otherVal,virtualTable.getColumnAt(column).getType()))
			{
				return false;
			}
		}
		
		return true;
	}
	
	
	/**
	 * Appends the key-value pairs to the {@link WHERE} condition of query and
	 * the value-list.
	 */
	public void appendCondition(WHERE where, List values)
	{
		appendCondition(where,values,virtualTable);
	}
	
	
	public void appendCondition(WHERE where, List values, VirtualTable virtualTable)
	{
		boolean first = true;
		
		for(String name : this.values.keySet())
		{
			VirtualTableColumn col = virtualTable.getColumn(name);
			if(col != null)
			{
				if(first)
				{
					first = false;
					
					if(!where.isEmpty())
					{
						where.encloseWithPars();
					}
				}
				
				where.and(col.eq("?"));
				values.add(this.values.get(name));
			}
		}
	}
	
	
	public Condition getCondition(List values)
	{
		return getCondition(values,virtualTable);
	}
	
	
	public Condition getCondition(List values, VirtualTable virtualTable)
	{
		Condition condition = null;
		
		for(String name : this.values.keySet())
		{
			VirtualTableColumn col = virtualTable.getColumn(name);
			if(col != null)
			{
				Condition comparison = col.eq("?");
				if(condition == null)
				{
					condition = comparison;
				}
				else
				{
					condition = condition.AND(comparison);
				}
				
				values.add(this.values.get(name));
			}
		}
		
		return condition;
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
		
		if(obj instanceof KeyValues)
		{
			KeyValues other = (KeyValues)obj;
			return virtualTable.equals(other.virtualTable) && values.equals(other.values);
		}
		
		return false;
	}
	
	
	private String hash()
	{
		if(hash == null)
		{
			hash = HashComputer.computeHash(values,
					virtualTable.getAllowMultipleNullsInUniqueIndices());
		}
		
		return hash;
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode()
	{
		return hash().hashCode();
	}
}
