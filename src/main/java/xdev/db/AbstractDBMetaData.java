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

import xdev.util.ProgressMonitor;
import xdev.vt.EntityRelationshipModel;


/**
 * 
 * @author XDEV Software
 * 
 */
// TODO javadoc (class description)
public abstract class AbstractDBMetaData implements DBMetaData, Serializable
{
	private static final long	serialVersionUID	= -3760443801966056286L;
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString()
	{
		return getName() + " " + getVersion();
	}
	
	
	@Override
	public EntityRelationshipModel getEntityRelationshipModel(ProgressMonitor monitor)
			throws DBException
	{
		return getEntityRelationshipModel(monitor,getTableInfos(monitor,TableType.TABLES_AND_VIEWS));
	}
	
	
	/**
	 * Compare the given two columns {@code column1} and {@code column2} according to the following criteria:
	 *  <ul>
	 *  <li>column name</li>
	 *  <li>column type</li>
	 *  <li>nullable</li>
	 *  <li>auto increment</li>
	 *  <li>default value</li>
	 * </ul>
	 * <br>
	 * The default value is only considered if the column is not a auto increment column. 
	 * 
	 * @param table1
	 * @param column1
	 * @param table2
	 * @param column2
	 * @return
	 * @throws DBException
	 */
	protected boolean isColumnEqual(TableMetaData table1, ColumnMetaData column1,
			TableMetaData table2, ColumnMetaData column2) throws DBException
	{
		
		if(
				(isCaseSensitive() ? column1.getName().equals(column2.getName()) : column1.getName().equalsIgnoreCase(column2.getName()))
				&& equalsType(column1, column2)
				&& column1.isNullable() == column2.isNullable()
				&& column1.isAutoIncrement() == column2.isAutoIncrement()
				&& (column1.isAutoIncrement() != true ? 
				ColumnMetaData.areDefaultValuesEqual(column1.getDefaultValue(),	column2.getDefaultValue()) : true))
		{
			DataType type = column1.getType();
			if(type.hasLength())
			{
				if(column1.getLength() != column2.getLength())
				{
					return false;
				}
				
				if(type.hasScale())
				{
					if(column1.getScale() != column2.getScale())
					{
						return false;
					}
				}
			}
			
			return true;
		}
		
		return false;
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void dispose()
	{
		// do nothing
	}
}
