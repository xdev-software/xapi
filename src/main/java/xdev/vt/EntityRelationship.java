package xdev.vt;

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

import xdev.db.sql.Condition;
import xdev.lang.Copyable;
import xdev.util.ArrayUtils;
import xdev.util.MathUtils;
import xdev.util.StringUtils;


/**
 * This class describes the relationship between two {@link Entity} objects.
 * 
 * @author XDEV Software Corp.
 * @see Entity
 * 
 */
public class EntityRelationship implements Copyable<EntityRelationship>, Serializable
{
	private static final long	serialVersionUID	= -8619356395478812452L;
	
	private final Entity		firstEntity;
	private final Entity		secondEntity;
	private final boolean		loop;
	private final int			hash;
	

	/**
	 * Creates a new relationship containing the given tables, columns and
	 * cardinalities.
	 * <p>
	 * Note that the column count of both entities must match, otherwise a
	 * {@link IllegalArgumentException} is thrown.
	 * 
	 * @param tableName1
	 * @param columnName1
	 * @param cardinality1
	 * @param tableName2
	 * @param columnName2
	 * @param cardinality2
	 * @throws IllegalArgumentException
	 *             if the column count of both entities doesn't match
	 */
	public EntityRelationship(String tableName1, String columnName1, Cardinality cardinality1,
			String tableName2, String columnName2, Cardinality cardinality2)
			throws IllegalArgumentException
	{
		this(new Entity(tableName1,columnName1,cardinality1),new Entity(tableName2,columnName2,
				cardinality2));
	}
	

	/**
	 * Creates a new relationship containing the given tables, columns and
	 * cardinalities.
	 * <p>
	 * Note that the column count of both entities must match, otherwise a
	 * {@link IllegalArgumentException} is thrown.
	 * 
	 * @param tableName1
	 * @param columnNames1
	 * @param cardinality1
	 * @param tableName2
	 * @param columnNames2
	 * @param cardinality2
	 * @throws IllegalArgumentException
	 *             if the column count of both entities doesn't match
	 * 
	 * @since 3.2
	 */
	public EntityRelationship(String tableName1, String[] columnNames1, Cardinality cardinality1,
			String tableName2, String[] columnNames2, Cardinality cardinality2)
			throws IllegalArgumentException
	{
		this(new Entity(tableName1,columnNames1,cardinality1),new Entity(tableName2,columnNames2,
				cardinality2));
	}
	

	/**
	 * Initializes a new instance of {@link EntityRelationship}.
	 * <p>
	 * Note that the column count of both entities must match, otherwise a
	 * {@link IllegalArgumentException} is thrown.
	 * 
	 * @param firstEntity
	 *            the first entity of the relationship.
	 * @param secondEntity
	 *            the second entity of the relationship.
	 * @throws IllegalArgumentException
	 *             if the column count of both entities doesn't match
	 */
	public EntityRelationship(Entity firstEntity, Entity secondEntity)
			throws IllegalArgumentException
	{
		if(firstEntity.columnNames.length != secondEntity.columnNames.length)
		{
			throw new IllegalArgumentException("entities must have same number of columns");
		}
		
		this.firstEntity = firstEntity;
		this.secondEntity = secondEntity;
		
		this.loop = firstEntity.getTableName().equals(secondEntity.getTableName());
		
		int hash1 = firstEntity.hashCode();
		int hash2 = secondEntity.hashCode();
		if(hash1 > hash2)
		{
			int tmp = hash1;
			hash1 = hash2;
			hash2 = tmp;
		}
		this.hash = MathUtils.computeHash(hash1,hash2);
	}
	

	/**
	 * Returns the first entity of this relationship.
	 * 
	 * @return a {@link Entity}
	 */
	public Entity getFirstEntity()
	{
		return firstEntity;
	}
	

	/**
	 * Returns the second entity of this relationship.
	 * 
	 * @return a {@link Entity}
	 */
	public Entity getSecondEntity()
	{
		return secondEntity;
	}
	

	/**
	 * Returns the {@link Entity} on the other side of the relationship.
	 * 
	 * @param entity
	 *            the {@link Entity}, which related {@link Entity} should be
	 *            returned
	 * @return the other {@link Entity} or <code>null</code> if none found.
	 */
	public Entity getOther(Entity entity)
	{
		if(firstEntity == entity)
		{
			return secondEntity;
		}
		else if(secondEntity == entity)
		{
			return firstEntity;
		}
		
		return null;
	}
	

	/**
	 * Returns the {@link Entity} for a specified tableName.
	 * 
	 * @param tableName
	 *            to get the {@link Entity} for.
	 * @return an {@link Entity}, or <code>null</code> if none found.
	 */
	public Entity getReferrer(String tableName)
	{
		if(firstEntity.refersTo(tableName))
		{
			return firstEntity;
		}
		
		if(secondEntity.refersTo(tableName))
		{
			return secondEntity;
		}
		
		return null;
	}
	

	/**
	 * Returns the {@link Entity} for a specified tableName and columnName.
	 * 
	 * @param tableName
	 *            to get the {@link Entity} for.
	 * @param columnName
	 *            to get the {@link Entity} for
	 * @return an {@link Entity}, or <code>null</code> if none found.
	 * @see #getReferrer(String, String[])
	 */
	public Entity getReferrer(String tableName, String columnName)
	{
		return getReferrer(tableName,new String[]{columnName});
	}
	

	/**
	 * Returns the {@link Entity} for a specified tableName and columnNames.
	 * 
	 * @param tableName
	 *            to get the {@link Entity} for.
	 * @param columnNames
	 *            to get the {@link Entity} for
	 * @return an {@link Entity}, or <code>null</code> if none found.
	 * @since 3.2
	 */
	public Entity getReferrer(String tableName, String[] columnNames)
	{
		if(firstEntity.refersTo(tableName,columnNames))
		{
			return firstEntity;
		}
		
		if(secondEntity.refersTo(tableName,columnNames))
		{
			return secondEntity;
		}
		
		return null;
	}
	

	/**
	 * Checks if this relation is a loop, meaning it starts and ends at the same
	 * table.
	 * 
	 * @return <code>true</code> if the start end the end of this relation
	 *         points to the same table, <code>false</code> otherwise
	 */
	public boolean isLoop()
	{
		return loop;
	}
	

	/**
	 * Returns, if this {@link EntityRelationship} refers to the combination of
	 * the specified tablename and columnname.
	 * 
	 * @param tableName
	 *            the name of the table to check.
	 * @param columnName
	 *            the name of the column to check.
	 * @return true, if this {@link EntityRelationship} refers to the specified
	 *         parameters.
	 * @deprecated since multiple columns are supported, use
	 *             {@link #refersTo(String, String[])}
	 */
	@Deprecated
	public boolean refersTo(String tableName, String columnName)
	{
		return firstEntity.refersTo(tableName,columnName)
				|| secondEntity.refersTo(tableName,columnName);
	}
	

	/**
	 * Returns, if this {@link EntityRelationship} refers to the combination of
	 * the specified tablename and columnname.
	 * 
	 * @param tableName
	 *            the name of the table to check.
	 * @param columnNames
	 *            the names of the column to check.
	 * @return true, if this {@link EntityRelationship} refers to the specified
	 *         parameters.
	 * @since 3.2
	 */
	public boolean refersTo(String tableName, String[] columnNames)
	{
		return firstEntity.refersTo(tableName,columnNames)
				|| secondEntity.refersTo(tableName,columnNames);
	}
	

	/**
	 * Returns, if this {@link EntityRelationship} refers to the specified
	 * virtual table name.
	 * 
	 * @param tableName
	 *            the name of the virtual table to check.
	 * @return true, if this {@link EntityRelationship} refers to the specified
	 *         table name.
	 */
	public boolean refersTo(String tableName)
	{
		return firstEntity.refersTo(tableName) || secondEntity.refersTo(tableName);
	}
	

	/**
	 * Returns a copy of this {@link EntityRelationship}.
	 * 
	 * @return a copied {@link EntityRelationship}.
	 */
	public String getConnectedTable(String tableName)
	{
		Entity entity = getReferrer(tableName);
		if(entity != null)
		{
			return getOther(entity).getTableName();
		}
		
		return null;
	}
	

	/**
	 * @since 3.2
	 */
	public int getColumnCount()
	{
		return firstEntity.getColumnCount();
	}
	

	@Override
	public EntityRelationship clone()
	{
		return new EntityRelationship(firstEntity.clone(),secondEntity.clone());
	}
	

	/**
	 * Checks this {@link EntityRelationship} with another
	 * {@link EntityRelationship} for equality.
	 * <p>
	 * Two {@link EntityRelationship} objects are considered equal, if its
	 * entities are equal.
	 * </p>
	 * 
	 * @param obj
	 *            the other Entity
	 * @return true if this Entity equals the other one.
	 */
	@Override
	public boolean equals(Object obj)
	{
		if(obj == this)
		{
			return true;
		}
		
		if(obj instanceof EntityRelationship)
		{
			return hash == ((EntityRelationship)obj).hash;
		}
		
		return false;
	}
	

	/**
	 * Determines if this relationship refers to the same tables and columns as
	 * the <code>other</code> relationship.
	 * 
	 * @param other
	 *            the relationship to check
	 * @return <code>true</code> if this relationship refers to the same tables
	 *         and columns as the <code>other</code> relationship,
	 *         <code>false</code> otherwise.
	 */
	public boolean equalsTablesAndColumns(EntityRelationship other)
	{
		Entity otherFirst = other.firstEntity;
		Entity otherSecond = other.secondEntity;
		return (firstEntity.equals(otherFirst) && secondEntity.equals(otherSecond))
				|| (firstEntity.equals(otherSecond) && secondEntity.equals(otherFirst));
	}
	

	/**
	 * Returns a hashcode built from the hashcodes of the related entities.
	 * 
	 * @return a hashcode
	 */
	@Override
	public int hashCode()
	{
		return hash;
	}
	

	/**
	 * Returns a {@link String} representation of this
	 * {@link EntityRelationship} containing both entities.
	 * 
	 * @return the relationship as {@link String}.
	 */
	@Override
	public String toString()
	{
		return new StringBuilder().append(firstEntity.tableName).append("(")
				.append(StringUtils.concat(",",(Object[])firstEntity.columnNames)).append(") [ ")
				.append(firstEntity.cardinality.toString()).append(" - ")
				.append(secondEntity.cardinality.toString()).append(" ] ")
				.append(secondEntity.tableName).append("(")
				.append(StringUtils.concat(",",(Object[])secondEntity.columnNames)).append(")")
				.toString();
	}
	

	/**
	 * Creates a {@link Condition} that can be used for joining the entities of
	 * this {@link EntityRelationship} within SQL queries.
	 * 
	 * @return a {@link Condition}.
	 */
	public Condition getCondition()
	{
		Condition condition = null;
		
		int cc = firstEntity.getColumnCount();
		for(int i = 0; i < cc; i++)
		{
			VirtualTableColumn col1 = firstEntity.getVirtualTableColumn(i);
			VirtualTableColumn col2 = secondEntity.getVirtualTableColumn(i);
			if(col1 == null || col2 == null)
			{
				return null;
			}
			
			Condition part = col1.eq(col2);
			if(condition == null)
			{
				condition = part;
			}
			else
			{
				condition = condition.AND(part);
			}
		}
		
		return condition;
	}
	


	/**
	 * This class provides an abstraction for an entity within a
	 * {@link EntityRelationship}.
	 * 
	 * @author XDEV Software Corp.
	 * @see EntityRelationship
	 */
	public static class Entity implements Copyable<Entity>, Serializable
	{
		private static final long		serialVersionUID	= -4394075785300688603L;
		
		private final String			tableName;
		private final String[]			columnNames;
		private final int				hash;
		private Cardinality				cardinality;
		private VirtualTable			virtualTable;
		private VirtualTableColumn[]	virtualTableColumns;
		

		/**
		 * Initializes a new {@link Entity}.
		 * 
		 * @param tableName
		 *            the table name of the entity.
		 * @param columnName
		 *            the column name used for the relationship to the other
		 *            entity.
		 * @param cardinality
		 *            the {@link Cardinality} of the entity.
		 */
		public Entity(String tableName, String columnName, Cardinality cardinality)
		{
			this(tableName,new String[]{columnName},cardinality);
		}
		

		/**
		 * Initializes a new {@link Entity}.
		 * 
		 * @param tableName
		 *            the table name of the entity.
		 * @param columnNames
		 *            the column names used for the relationship to the other
		 *            entity.
		 * @param cardinality
		 *            the {@link Cardinality} of the entity.
		 * @since 3.2
		 */
		public Entity(String tableName, String[] columnNames, Cardinality cardinality)
		{
			this.tableName = tableName;
			this.columnNames = columnNames;
			this.hash = MathUtils.computeHashDeep(tableName,columnNames);
			this.cardinality = cardinality;
		}
		

		/**
		 * Returns the table name.
		 * 
		 * @return the table name
		 */
		public String getTableName()
		{
			return tableName;
		}
		

		/**
		 * Returns the first column name (name of the join column of the
		 * relationship).
		 * 
		 * @return the first column name
		 * @see #getColumnCount()
		 * @see #getColumnNames()
		 */
		public String getColumnName()
		{
			return columnNames[0];
		}
		

		/**
		 * Returns the number of columns of this entity.
		 * 
		 * @return the number of columns of this entity
		 * @since 3.2
		 */
		public int getColumnCount()
		{
			return columnNames.length;
		}
		

		/**
		 * Returns the column name at the specified <code>index</code>.
		 * 
		 * @since 3.2
		 * @see #getColumnCount()
		 * @see #getColumnNames()
		 */
		public String getColumnName(int index)
		{
			return columnNames[index];
		}
		

		/**
		 * Returns all column names of this entity
		 * 
		 * @since 3.2
		 * 
		 * @see #getColumnCount()
		 * @see #getColumnName(int)
		 */
		public String[] getColumnNames()
		{
			return columnNames;
		}
		

		/**
		 * Determines if this {@link Entity} refers to the specified table name.
		 * 
		 * @param tableName
		 *            name of the table to be checked.
		 * @return true, if this {@link Entity} refers to the specified
		 *         <code>tableName</code>, <code>false</code> otherwise.
		 */
		public boolean refersTo(String tableName)
		{
			return this.tableName.equals(tableName);
		}
		

		/**
		 * Determines if this {@link Entity} refers to the specified table and
		 * column name.
		 * 
		 * @param tableName
		 *            name of the table to be checked.
		 * @param columnName
		 *            name of the column to be checked.
		 * @return true, if this {@link Entity} refers to the specified
		 *         <code>tableName</code> and <code>columnName</code>,
		 *         <code>false</code> otherwise.
		 * @deprecated since multiple columns are supported, use
		 *             {@link #refersTo(String, String[])}
		 */
		@Deprecated
		public boolean refersTo(String tableName, String columnName)
		{
			return refersTo(tableName,new String[]{columnName});
		}
		

		/**
		 * Determines if this {@link Entity} refers to the specified table and
		 * column name.
		 * 
		 * @param tableName
		 *            name of the table to be checked.
		 * @param columnNames
		 *            names of the column to be checked.
		 * @return true, if this {@link Entity} refers to the specified
		 *         <code>tableName</code> and <code>columnName</code>,
		 *         <code>false</code> otherwise.
		 * @since 3.2
		 */
		public boolean refersTo(String tableName, String[] columnNames)
		{
			return this.tableName.equals(tableName)
					&& ArrayUtils.equals(this.columnNames,columnNames);
		}
		

		/**
		 * Determines if this {@link Entity} refers to the same table and column
		 * as <code>entity</code>.
		 * 
		 * @param entity
		 *            the {@link Entity} to check
		 * @return true, if this {@link Entity} refers to the same table and
		 *         column as <code>entity</code>, <code>false</code> otherwise.
		 */
		public boolean refersTo(Entity entity)
		{
			return refersTo(entity.tableName,entity.columnNames);
		}
		

		/**
		 * Returns the fully qualified column name (of the first column).
		 * 
		 * @return the fully qualified column name
		 * @deprecated not particularly useful since multiple columns are
		 *             supported
		 */
		@Deprecated
		public String getFullQualifiedName()
		{
			return tableName + "." + columnNames[0];
		}
		

		/**
		 * Returns the {@link Cardinality} of the {@link Entity}.
		 * 
		 * @return a {@link Cardinality}
		 */
		public Cardinality getCardinality()
		{
			return cardinality;
		}
		

		/**
		 * Sets the {@link Cardinality} of the {@link Entity}.
		 * 
		 * @param cardinality
		 *            {@link Cardinality} to be set.
		 */
		public void setCardinality(Cardinality cardinality)
		{
			this.cardinality = cardinality;
		}
		

		/**
		 * Returns the referred {@link VirtualTable}.
		 * <p>
		 * This is a shortcut for
		 * 
		 * <pre>
		 * VirtualTables.getVirtualTable(entity.getTableName());
		 * </pre>
		 * 
		 * @return the referred {@link VirtualTable} of this entity, or
		 *         <code>null</code> if the virtual table cannot be found
		 * @since 3.2
		 * @see #getVirtualTableColumn()
		 */
		public VirtualTable getVirtualTable()
		{
			if(virtualTable == null)
			{
				virtualTable = VirtualTables.getVirtualTable(tableName);
			}
			
			return virtualTable;
		}
		

		/**
		 * Returns a referred {@link VirtualTableColumn}.
		 * 
		 * @param columnIndex
		 *            the index of the column to ask for
		 * @return a referred {@link VirtualTableColumn} of this entity, or
		 *         <code>null</code> if the column cannot be found
		 * @since 3.2
		 * @see #getVirtualTable()
		 */
		public VirtualTableColumn getVirtualTableColumn(int columnIndex)
		{
			if(virtualTableColumns == null)
			{
				virtualTableColumns = new VirtualTableColumn[columnNames.length];
			}
			
			VirtualTable vt;
			if(virtualTableColumns[columnIndex] == null && (vt = getVirtualTable()) != null)
			{
				virtualTableColumns[columnIndex] = vt.getColumn(columnNames[columnIndex]);
			}
			
			return virtualTableColumns[columnIndex];
		}
		

		/**
		 * Returns a copy of this {@link Entity}.
		 * 
		 * @return the copied {@link Entity}.
		 */
		@Override
		public Entity clone()
		{
			return new Entity(tableName,columnNames,cardinality);
		}
		

		/**
		 * Checks this {@link Entity} with another {@link Entity} for equality.
		 * <p>
		 * The table name and the column name have to be equal to be considered
		 * equal.
		 * </p>
		 * 
		 * @param obj
		 *            the other Entity
		 * @return true if this Entity equals the other one.
		 */
		@Override
		public boolean equals(Object obj)
		{
			if(obj == this)
			{
				return true;
			}
			
			if(obj != null && obj instanceof Entity)
			{
				return ((Entity)obj).hash == hash;
			}
			
			return false;
		}
		

		/**
		 * Returns the hashcode of this {@link Entity} built from the name of
		 * the table and the name of the column.
		 * 
		 * @return a hashcode
		 */
		@Override
		public int hashCode()
		{
			return hash;
		}
		

		/**
		 * Returns an {@link String} representation for this {@link Entity}
		 * consisting of tablename, columnname and cardinality.
		 * 
		 * @return the entity as a String
		 */
		@Override
		public String toString()
		{
			return new StringBuilder().append(tableName).append("(")
					.append(StringUtils.concat(",",(Object[])columnNames)).append(") [")
					.append(cardinality.toString()).append("]").toString();
		}
	}
}
