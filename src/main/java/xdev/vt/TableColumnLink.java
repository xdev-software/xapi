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

import xdev.db.JoinType;
import xdev.db.sql.Condition;
import xdev.db.sql.SELECT;
import xdev.db.sql.Table;
import xdev.lang.Copyable;
import xdev.util.MathUtils;
import xdev.util.ObjectUtils;
import xdev.vt.EntityRelationship.Entity;


/**
 * A link from a {@link VirtualTable}'s column to another {@link VirtualTable}'s
 * column through the {@link EntityRelationshipModel} of the application.
 * <p>
 * The {@link VirtualTableColumn} which holds this link is the source column and
 * the information stored within this object represents the target (linked)
 * column.
 * 
 * @author XDEV Software
 * @see VirtualTableColumn
 * @see VirtualTableColumn#setTableColumnLink(TableColumnLink)
 * @see VirtualTableColumn#getTableColumnLink()
 * @see EntityRelationships#getModel()
 */
public class TableColumnLink implements Serializable, Copyable<TableColumnLink>
{
	private static final long			serialVersionUID	= 6629654855079135425L;
	
	private final String				linkedTable;
	private final String				linkedColumn;
	private final EntityRelationship[]	path;
	private final int					hash;
	
	
	/**
	 * Creates a new link.
	 * 
	 * @param linkedTable
	 *            the full qualified name of the linked table
	 * @param linkedColumn
	 *            the name of the linked column
	 * @param path
	 *            the path through the {@link EntityRelationshipModel}
	 */
	public TableColumnLink(String linkedTable, String linkedColumn, EntityRelationship... path)
	{
		this.linkedTable = linkedTable;
		this.linkedColumn = linkedColumn;
		this.path = path;
		this.hash = MathUtils.computeHashDeep(linkedTable,linkedColumn,path);
	}
	
	
	/**
	 * Returns the linked table name of the target table.
	 * 
	 * @return the linked table name
	 * 
	 * @see VirtualTables#getVirtualTable(String)
	 */
	public String getLinkedTable()
	{
		return linkedTable;
	}
	
	
	/**
	 * Returns the linked column name of the target column.
	 * 
	 * @return the linked column name
	 * 
	 * @see VirtualTable#getColumn(String)
	 */
	public String getLinkedColumn()
	{
		return linkedColumn;
	}
	
	
	/**
	 * Returns the path between the linked columns.
	 * 
	 * @return the path between the linked columns
	 */
	public EntityRelationship[] getPath()
	{
		return path;
	}
	
	
	/**
	 * Returns the linked key column of the linked table.
	 * 
	 * @return the linked key column of the linked table
	 */
	// public String getLinkedKeyColumn()
	// {
	// EntityRelationship last = path[path.length - 1];
	// return last.getReferrer(linkedTable).getColumnName();
	// }
	
	/**
	 * @since 3.2
	 */
	public String[] getLinkedKeyColumns()
	{
		EntityRelationship last = path[path.length - 1];
		return last.getReferrer(linkedTable).getColumnNames();
	}
	
	
	void addColumn(SELECT select, VirtualTable masterVT, VirtualTableColumn linker,
			JoinType joinType, SqlGenerationContext context) throws VirtualTableException
	{
		VirtualTable linkedTable = VirtualTables.getVirtualTable(this.linkedTable);
		if(linkedTable == null)
		{
			VirtualTableException.throwVirtualTableNotFound(this.linkedTable);
		}
		
		VirtualTableColumn linkedCol = linkedTable.getColumn(this.linkedColumn);
		if(linkedCol == null)
		{
			VirtualTableException.throwColumnNotFound(linkedTable,this.linkedColumn);
		}
		
		EntityRelationship last = path[path.length - 1];
		Table table = context.getTableExpression(last,linkedTable);
		select.columns(linkedCol.toSqlColumn(table).AS(linker.getName()));
		
		for(EntityRelationship relation : path)
		{
			Entity masterEntity = relation.getReferrer(masterVT.getName());
			Entity joinedEntity = relation.getOther(masterEntity);
			
			int columnCount = masterEntity.getColumnCount();
			VirtualTableColumn[] masterCol = new VirtualTableColumn[columnCount];
			for(int i = 0; i < columnCount; i++)
			{
				String columnName = masterEntity.getColumnName(i);
				masterCol[i] = masterVT.getColumn(columnName);
				if(masterCol[i] == null)
				{
					VirtualTableException.throwColumnNotFound(masterVT,columnName);
				}
			}
			
			VirtualTable joinedVT = VirtualTables.getVirtualTable(joinedEntity.getTableName());
			if(joinedVT == null)
			{
				VirtualTableException.throwVirtualTableNotFound(joinedEntity.getTableName());
			}
			
			VirtualTableColumn[] joinedCol = new VirtualTableColumn[columnCount];
			for(int i = 0; i < columnCount; i++)
			{
				String columnName = joinedEntity.getColumnName(i);
				joinedCol[i] = joinedVT.getColumn(columnName);
				if(joinedCol[i] == null)
				{
					VirtualTableException.throwColumnNotFound(joinedVT,columnName);
				}
			}
			
			if(context.join(relation))
			{
				table = context.getTableExpression(relation,joinedVT);
				
				Condition joinCondition = null;
				for(int i = 0; i < columnCount; i++)
				{
					Condition part = joinedCol[i].toSqlColumn(table).eq(masterCol[i]);
					if(joinCondition == null)
					{
						joinCondition = part;
					}
					else
					{
						joinCondition = joinCondition.AND(part);
					}
				}
				
				switch(joinType)
				{
					case INNER_JOIN:
						select.INNER_JOIN(table,joinCondition);
					break;
					case OUTER_JOIN:
						select.OUTER_JOIN(table,joinCondition);
					break;
					case LEFT_JOIN:
						select.LEFT_JOIN(table,joinCondition);
					break;
					case RIGHT_JOIN:
						select.RIGHT_JOIN(table,joinCondition);
					break;
				}
			}
			
			masterVT = joinedVT;
		}
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public TableColumnLink clone()
	{
		return new TableColumnLink(linkedTable,linkedColumn,ObjectUtils.clone(path));
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
		
		if(obj instanceof TableColumnLink)
		{
			return hash == ((TableColumnLink)obj).hash;
		}
		
		return false;
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode()
	{
		return hash;
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString()
	{
		return new StringBuilder("Link to ").append(linkedTable).append(".").append(linkedColumn)
				.toString();
	}
}
