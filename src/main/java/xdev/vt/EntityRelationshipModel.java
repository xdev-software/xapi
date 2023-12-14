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
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import xdev.vt.EntityRelationship.Entity;


/**
 * This class is a container for {@link EntityRelationship} objects.
 * <p>
 * It can be used to represent a complete entity relationship model from a
 * database.
 * </p>
 * 
 * @author XDEV Software Corp.
 */
public class EntityRelationshipModel extends HashSet<EntityRelationship> implements Cloneable,
		Serializable
{
	private static final long	serialVersionUID	= -6620723489144663123L;
	
	
	/**
	 * Initializes a new instance of a {@link EntityRelationshipModel}.
	 */
	public EntityRelationshipModel()
	{
		super();
	}
	
	
	/**
	 * Creates a new {@link EntityRelationship} and adds it to this
	 * {@link EntityRelationshipModel}.
	 * 
	 * @param tableName1
	 *            the table name of the first {@link Entity} that is part of the
	 *            new relationship.
	 * @param columnName1
	 *            the join column name of the first entity.
	 * @param cardinality1
	 *            the cardinality of the first entity.
	 * @param tableName2
	 *            the table name of the second {@link Entity} that is part of
	 *            the new relationship.
	 * @param columnName2
	 *            the join column name of the second entity.
	 * @param cardinality2
	 *            the cardinality of the second entity.
	 */
	protected void add(String tableName1, String columnName1, Cardinality cardinality1,
			String tableName2, String columnName2, Cardinality cardinality2)
	{
		add(new EntityRelationship(new Entity(tableName1,columnName1,cardinality1),new Entity(
				tableName2,columnName2,cardinality2)));
	}
	
	
	/**
	 * Creates a new {@link EntityRelationship} and adds it to this
	 * {@link EntityRelationshipModel}.
	 * 
	 * @param tableName1
	 *            the table name of the first {@link Entity} that is part of the
	 *            new relationship.
	 * @param columnNames1
	 *            the join column name of the first entity.
	 * @param cardinality1
	 *            the cardinality of the first entity.
	 * @param tableName2
	 *            the table name of the second {@link Entity} that is part of
	 *            the new relationship.
	 * @param columnNames2
	 *            the join column name of the second entity.
	 * @param cardinality2
	 *            the cardinality of the second entity.
	 * @since 3.2
	 */
	protected void add(String tableName1, String[] columnNames1, Cardinality cardinality1,
			String tableName2, String[] columnNames2, Cardinality cardinality2)
	{
		add(new EntityRelationship(new Entity(tableName1,columnNames1,cardinality1),new Entity(
				tableName2,columnNames2,cardinality2)));
	}
	
	
	/**
	 * Returns all relations of the entity <code>table</code>.
	 * 
	 * @param table
	 *            the table name of the entity.
	 * @return all relations of the entity <code>table</code>
	 */
	public EntityRelationship[] getRelations(String table)
	{
		List<EntityRelationship> list = new ArrayList();
		
		for(EntityRelationship relation : this)
		{
			if(relation.refersTo(table))
			{
				list.add(relation);
			}
		}
		
		return list.toArray(new EntityRelationship[list.size()]);
	}
	
	
	/**
	 * Returns a {@link EntityRelationship} from this
	 * {@link EntityRelationshipModel} as specified by table and column name of
	 * an referred {@link Entity}.
	 * 
	 * @param table
	 *            the table name of the entity.
	 * @param column
	 *            the column name of the entity.
	 * @return an {@link EntityRelationship}, or <code>null</code>, if none was
	 *         found.
	 * @deprecated since multiple columns are supported, use
	 *             {@link #getRelationship(String, String[])}
	 */
	@Deprecated
	public EntityRelationship getRelationship(String table, String column)
	{
		return getRelationship(table,new String[]{column});
	}
	
	
	/**
	 * Returns a {@link EntityRelationship} from this
	 * {@link EntityRelationshipModel} as specified by table and column names of
	 * an referred {@link Entity}.
	 * 
	 * @param table
	 *            the table name of the entity.
	 * @param columnss
	 *            the column names of the entity.
	 * @return an {@link EntityRelationship}, or <code>null</code>, if none was
	 *         found.
	 * @since 3.2
	 */
	public EntityRelationship getRelationship(String table, String[] columns)
	{
		for(EntityRelationship relation : this)
		{
			if(relation.refersTo(table,columns))
			{
				return relation;
			}
		}
		
		return null;
	}
	
	
	/**
	 * Returns a {@link EntityRelationship} from this
	 * {@link EntityRelationshipModel} as specified by a master table and a
	 * detail table.
	 * 
	 * @param masterTable
	 *            name of the master table
	 * @param masterKey
	 *            key column of the master table
	 * @param detailTable
	 *            name of the detail table
	 * @return an {@link EntityRelationship}, or <code>null</code>, if none was
	 *         found.
	 * @deprecated since multiple columns are supported use
	 *             {@link #getRelationship(String, String[], String)}
	 */
	@Deprecated
	public EntityRelationship getRelationship(String masterTable, String masterKey,
			String detailTable)
	{
		return getRelationship(masterTable,new String[]{masterKey},detailTable);
	}
	
	
	/**
	 * Returns a {@link EntityRelationship} from this
	 * {@link EntityRelationshipModel} as specified by a master table and a
	 * detail table.
	 * 
	 * @param masterTable
	 *            name of the master table
	 * @param primaryKeyColumns
	 *            key column names of the master table
	 * @param detailTable
	 *            name of the detail table
	 * @return an {@link EntityRelationship}, or <code>null</code>, if none was
	 *         found.
	 * @since 3.2
	 */
	public EntityRelationship getRelationship(String masterTable, String[] primaryKeyColumns,
			String detailTable)
	{
		for(EntityRelationship relation : this)
		{
			if(relation.isLoop())
			{
				continue;
			}
			
			if(relation.refersTo(masterTable,primaryKeyColumns) && relation.refersTo(detailTable))
			{
				return relation;
			}
		}
		
		return null;
	}
	
	
	/**
	 * Returns a {@link EntityRelationship} from this
	 * {@link EntityRelationshipModel} as specified by a master table and a
	 * detail table.
	 * 
	 * @param masterTable
	 *            name of the master table
	 * @param primaryKeyColumns
	 *            key column names of the master table
	 * @param detailTable
	 *            name of the detail table
	 * @param foreignKeyColumns
	 *            key column names of the detail table
	 * @return an {@link EntityRelationship}, or <code>null</code>, if none was
	 *         found.
	 * @since 4.0
	 */
	public EntityRelationship getRelationship(String masterTable, String[] primaryKeyColumns,
			String detailTable, String[] foreignKeyColumns)
	{
		for(EntityRelationship relation : this)
		{
			if(relation.isLoop())
			{
				continue;
			}
			
			if(relation.refersTo(masterTable,primaryKeyColumns)
					&& relation.refersTo(detailTable,foreignKeyColumns))
			{
				return relation;
			}
		}
		
		return null;
	}
	
	
	/**
	 * Returns a shallow copy of this {@link EntityRelationshipModel}.
	 * <p>
	 * Shallow means, that the {@link EntityRelationship} objects get not
	 * copied, but referenced from the original {@link EntityRelationshipModel}.
	 * </p>
	 * 
	 * @return a shallow copy of this {@link EntityRelationshipModel}.
	 */
	@Override
	public EntityRelationshipModel clone()
	{
		return (EntityRelationshipModel)super.clone();
	}
}
