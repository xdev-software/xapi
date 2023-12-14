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
package xdev.ui;


import java.util.HashMap;
import java.util.Map;

import xdev.util.StringUtils;
import xdev.vt.Cardinality;
import xdev.vt.EntityRelationship;
import xdev.vt.EntityRelationship.Entity;
import xdev.vt.EntityRelationships;
import xdev.vt.KeyValues;
import xdev.vt.VirtualTable;
import xdev.vt.VirtualTable.VirtualTableRow;
import xdev.vt.VirtualTableColumn;
import xdev.vt.VirtualTableException;


/**
 * Registry for the current used {@link MasterDetailHandler}.
 * 
 * @author XDEV Software
 */
public final class MasterDetail
{
	private MasterDetail()
	{
	}
	
	private static MasterDetailHandler	handler;
	
	
	/**
	 * Sets the new MasterDetailHandler which is used to manage the corporation
	 * between {@link MasterDetailComponent}s or a {@link Formular} and a
	 * {@link MasterDetailComponent}.
	 * 
	 * @param handler
	 *            the new {@link MasterDetailHandler}
	 */
	public static void setHandler(MasterDetailHandler handler)
	{
		MasterDetail.handler = handler;
	}
	
	
	private static MasterDetailHandler getHandler()
	{
		if(handler == null)
		{
			handler = new DefaultMasterDetailHandler();
		}
		
		return handler;
	}
	
	
	/**
	 * Connects two {@link MasterDetailComponent}s.
	 * <p>
	 * The detail is filled with the appropriate records depending on the
	 * master's selection.
	 * <p>
	 * The maching master is selected if the detail's selection changes.
	 * 
	 * @param master
	 *            the component which operates as master
	 * @param detail
	 *            the component which operates as detail view
	 * @throws MasterDetailException
	 *             if necessary properties like the linked {@link VirtualTable}
	 *             are not set or insufficient entity relation information is
	 *             available
	 */
	public static void connect(MasterDetailComponent master, MasterDetailComponent detail)
			throws MasterDetailException
	{
		getHandler().connect(master,detail);
	}
	
	
	/**
	 * Connects a {@link MasterDetailComponent} with a {@link Formular}, meaning
	 * the formular (<code>detail</code>) is filled with the current selected
	 * record ( {@link VirtualTableRow}) of the <code>master</code>.
	 * 
	 * @param master
	 *            the component which operates as master
	 * @param detail
	 *            the {@link Formular} which operates as detail view
	 * @throws MasterDetailException
	 *             if necessary properties like the linked {@link VirtualTable}
	 *             are not set or insufficient entity relation information is
	 *             available
	 */
	public static void connect(final MasterDetailComponent master, final Formular detail)
	{
		getHandler().connect(master,detail);
	}
	
	
	/**
	 * @since 4.0
	 */
	public static VirtualTableRow getMasterRow(VirtualTableRow detailRecord, VirtualTable master)
			throws MasterDetailException, VirtualTableException
	{
		return master.getRow(getPrimaryKeyValues(detailRecord,master));
	}
	
	
	/**
	 * @since 4.0
	 */
	public static VirtualTableRow getMasterRow(VirtualTableRow detailRecord, VirtualTable master,
			EntityRelationship relation) throws MasterDetailException, VirtualTableException
	{
		return master.getRow(getPrimaryKeyValues(detailRecord,master,relation));
	}
	
	
	/**
	 * @since 4.0
	 */
	public static VirtualTableRow getMasterRow(VirtualTableRow detailRecord, VirtualTable master,
			VirtualTableColumn... foreignKey) throws MasterDetailException, VirtualTableException
	{
		EntityRelationship relation = getRelation(detailRecord,master,foreignKey);
		return master.getRow(getPrimaryKeyValues(detailRecord,master,relation));
	}
	
	
	/**
	 * @since 4.0
	 */
	public static EntityRelationship getRelation(VirtualTableRow detailRecord, VirtualTable master,
			VirtualTableColumn... foreignKey) throws MasterDetailException, VirtualTableException
	{
		VirtualTable detail = detailRecord.getVirtualTable();
		VirtualTableColumn[] pkColumns = master.getPrimaryKeyColumns();
		if(pkColumns.length == 0)
		{
			throw new MasterDetailException(master,detail,"no primary key defined in '"
					+ master.getName() + "'");
		}
		
		String[] pkColumnNames = VirtualTableColumn.getNamesOf(pkColumns);
		String[] fkColumnNames = VirtualTableColumn.getNamesOf(foreignKey);
		EntityRelationship relation = EntityRelationships.getModel().getRelationship(
				master.getName(),pkColumnNames,detail.getName(),fkColumnNames);
		if(relation == null)
		{
			throw new MasterDetailException(master,detail,"no relation found for "
					+ master.getName() + "(" + StringUtils.concat(",",(Object[])pkColumnNames)
					+ ")' - '" + detail.getName() + "("
					+ StringUtils.concat(",",(Object[])fkColumnNames) + ")'");
		}
		return relation;
	}
	
	
	/**
	 * @since 4.0
	 */
	public static KeyValues getPrimaryKeyValues(VirtualTableRow detailRecord, VirtualTable master)
			throws MasterDetailException, VirtualTableException
	{
		VirtualTable detail = detailRecord.getVirtualTable();
		VirtualTableColumn[] pkColumns = master.getPrimaryKeyColumns();
		if(pkColumns.length == 0)
		{
			throw new MasterDetailException(master,detail,"no primary key defined in '"
					+ master.getName() + "'");
		}
		
		String[] pkColumnNames = VirtualTableColumn.getNamesOf(pkColumns);
		EntityRelationship relation = EntityRelationships.getModel().getRelationship(
				master.getName(),pkColumnNames,detail.getName());
		if(relation == null)
		{
			throw new MasterDetailException(master,detail,"no foreign key for " + master.getName()
					+ "(" + StringUtils.concat(",",(Object[])pkColumnNames) + ") found in '"
					+ detail.getName() + "'");
		}
		
		return getPrimaryKeyValues(detailRecord,master,relation);
	}
	
	
	/**
	 * @since 4.0
	 */
	public static KeyValues getPrimaryKeyValues(VirtualTableRow detailRecord, VirtualTable master,
			VirtualTableColumn... foreignKey) throws MasterDetailException, VirtualTableException
	{
		EntityRelationship relation = getRelation(detailRecord,master,foreignKey);
		return getPrimaryKeyValues(detailRecord,master,relation);
	}
	
	
	/**
	 * @since 4.0
	 */
	public static KeyValues getPrimaryKeyValues(VirtualTableRow detailRecord, VirtualTable master,
			EntityRelationship relation) throws MasterDetailException, VirtualTableException
	{
		VirtualTable detail = detailRecord.getVirtualTable();
		Entity masterEntity = relation.getReferrer(master.getName());
		if(masterEntity.getCardinality() != Cardinality.ONE)
		{
			throw new MasterDetailException(master,detail,
					"invalid master detail relation: master's cardinality = MANY");
		}
		
		Map<String, Object> map = new HashMap();
		Entity detailEntity = relation.getOther(masterEntity);
		int columnCount = relation.getColumnCount();
		for(int i = 0; i < columnCount; i++)
		{
			String columnName = masterEntity.getColumnName(i);
			VirtualTableColumn masterColumn = master.getColumn(columnName);
			if(masterColumn == null)
			{
				VirtualTableException.throwColumnNotFound(master,columnName);
			}
			
			map.put(masterColumn.getName(),detailRecord.get(detailEntity.getColumnName(i)));
		}
		
		return new KeyValues(master,map);
	}
	
	
	/**
	 * @since 4.0
	 */
	public static KeyValues getForeignKeyValues(VirtualTableRow masterRecord, VirtualTable detail)
			throws MasterDetailException, VirtualTableException
	{
		VirtualTable master = masterRecord.getVirtualTable();
		VirtualTableColumn[] pkColumns = master.getPrimaryKeyColumns();
		int columnCount = pkColumns.length;
		if(columnCount == 0)
		{
			throw new MasterDetailException(master,detail,"no primary key defined in '"
					+ master.getName() + "'");
		}
		
		String[] pkColumnNames = VirtualTableColumn.getNamesOf(pkColumns);
		EntityRelationship relation = EntityRelationships.getModel().getRelationship(
				master.getName(),pkColumnNames,detail.getName());
		if(relation == null)
		{
			throw new MasterDetailException(master,detail,"no foreign key for " + master.getName()
					+ "(" + StringUtils.concat(",",(Object[])pkColumnNames) + ") found in '"
					+ detail.getName() + "'");
		}
		
		Entity masterEntity = relation.getReferrer(master.getName());
		if(masterEntity.getCardinality() != Cardinality.ONE)
		{
			throw new MasterDetailException(master,detail,
					"invalid master detail relation: master's cardinality = MANY");
		}
		
		Map<String, Object> map = new HashMap();
		
		Entity detailEntity = relation.getOther(masterEntity);
		for(int i = 0; i < columnCount; i++)
		{
			String columnName = detailEntity.getColumnName(i);
			VirtualTableColumn detailColumn = detail.getColumn(columnName);
			if(detailColumn == null)
			{
				VirtualTableException.throwColumnNotFound(detail,columnName);
			}
			
			map.put(detailColumn.getName(),masterRecord.get(master.getColumnIndex(pkColumns[i])));
		}
		
		return new KeyValues(detail,map);
	}
	
	
	static void updateForeignKeys(VirtualTable vt, KeyValues keyValues)
			throws VirtualTableException
	{
		String[] columns = keyValues.getColumnNames();
		int cc = columns.length;
		int[] indices = new int[cc];
		for(int c = 0; c < cc; c++)
		{
			indices[c] = vt.getColumnIndex(columns[c]);
			if(indices[c] == -1)
			{
				VirtualTableException.throwColumnNotFound(vt,columns[c]);
			}
		}
		
		int rc = vt.getRowCount();
		for(int r = 0; r < rc; r++)
		{
			VirtualTableRow row = vt.getRow(r);
			for(int c = 0; c < cc; c++)
			{
				row.set(indices[c],keyValues.getValue(columns[c]));
			}
		}
	}
}
