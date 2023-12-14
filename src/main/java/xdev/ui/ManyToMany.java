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


import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import xdev.db.DBConnection;
import xdev.db.DBException;
import xdev.db.DBUtils;
import xdev.db.WriteRequest;
import xdev.db.WriteResult;
import xdev.db.sql.DELETE;
import xdev.db.sql.INSERT;
import xdev.db.sql.SELECT;
import xdev.db.sql.UPDATE;
import xdev.db.sql.WHERE;
import xdev.util.IntList;
import xdev.vt.EntityRelationship;
import xdev.vt.EntityRelationship.Entity;
import xdev.vt.EntityRelationships;
import xdev.vt.KeyValues;
import xdev.vt.TableColumnLink;
import xdev.vt.VirtualTable;
import xdev.vt.VirtualTable.ColumnSelector;
import xdev.vt.VirtualTable.VirtualTableRow;
import xdev.vt.VirtualTableColumn;
import xdev.vt.VirtualTableException;
import xdev.vt.VirtualTables;


/**
 * Utility class with methods to manipulate many to many relations.
 * 
 * 
 * @author XDEV Software (TF, FH)
 * @since 3.0
 */
public final class ManyToMany
{
	private ManyToMany()
	{
	}
	
	
	
	static class State
	{
		/*
		 * original
		 */
		final VirtualTableRow	masterRecord;
		final VirtualTable		nTable;
		final VirtualTable		nmTable;
		/*
		 * clone
		 */
		final VirtualTable		mTable;
		
		EntityRelationship		nToNMrelation;
		
		
		State(VirtualTableRow masterRecord, VirtualTable nmTable) throws MasterDetailException,
				VirtualTableException
		{
			this.masterRecord = masterRecord;
			this.nTable = masterRecord.getVirtualTable();
			this.nmTable = nmTable;
			
			Map<String, EntityRelationship> map = new Hashtable();
			
			for(EntityRelationship relation : EntityRelationships.getModel().getRelations(
					nmTable.getName()))
			{
				if(relation.isLoop())
				{
					continue;
				}
				
				Entity entity = relation.getOther(relation.getReferrer(nmTable.getName()));
				if(!entity.refersTo(nTable.getName()))
				{
					map.put(entity.getTableName(),relation);
					nToNMrelation = relation;
				}
				
				if(map.size() > 1)
				{
					break;
				}
			}
			
			if(map.size() != 1)
			{
				throw new MasterDetailException(nTable,nmTable,
						"No unambiguous detail table found for '" + nTable.getName() + "' -> '"
								+ nmTable.getName() + "' <- ?");
			}
			
			String mTableName = map.keySet().iterator().next();
			VirtualTable mTable = VirtualTables.getVirtualTable(mTableName);
			if(mTable == null)
			{
				VirtualTableException.throwVirtualTableNotFound(mTableName);
			}
			
			this.mTable = mTable.clone(false);
		}
		
		
		KeyValues getForeignKeyValues()
		{
			return MasterDetail.getForeignKeyValues(masterRecord,nmTable);
		}
		
		
		int[] fillUpNMTable(VirtualTable nmTable, VirtualTable nmClone) throws DBException,
				VirtualTableException
		{
			IntList indices = new IntList();
			
			nmTable.clear();
			
			Map<VirtualTableColumn<?>, VirtualTableColumn<?>> columnMap = new Hashtable();
			VirtualTableColumn[] nmRelationColumns = null;
			VirtualTableColumn[] mRelationColumns = null;
			
			for(VirtualTableColumn nmColumn : nmClone)
			{
				VirtualTableColumn mColumn = null;
				
				if(!nmColumn.isPersistent())
				{
					TableColumnLink link = nmColumn.getTableColumnLink();
					if(link != null && mTable.getName().equals(link.getLinkedTable()))
					{
						mColumn = mTable.getColumn(link.getLinkedColumn());
					}
				}
				
				if(mColumn != null)
				{
					columnMap.put(mColumn,nmColumn);
				}
			}
			
			Entity nmEntity = nToNMrelation.getReferrer(nmClone.getName());
			if(nmEntity != null)
			{
				Entity mEntity = nToNMrelation.getOther(nmEntity);
				
				nmRelationColumns = nmClone.getColumns(nmEntity.getColumnNames());
				mRelationColumns = mTable.getColumns(mEntity.getColumnNames());
				
				for(int i = 0; i < mRelationColumns.length; i++)
				{
					columnMap.put(mRelationColumns[i],nmRelationColumns[i]);
				}
			}
			
			mTable.queryAndFill(mTable.getDefaultSelect());
			
			int cc;
			if(mRelationColumns != null && (cc = mRelationColumns.length) > 0)
			{
				int nmc = nmClone.getRowCount();
				
				int rc = mTable.getRowCount();
				Object[] mValues = new Object[cc];
				for(int r = 0; r < rc; r++)
				{
					for(int c = 0; c < cc; c++)
					{
						mValues[c] = mRelationColumns[c].getValueAt(r);
					}
					for(int nmr = 0; nmr < nmc; nmr++)
					{
						boolean equal = true;
						for(int c = 0; c < cc; c++)
						{
							Object nmValue = nmRelationColumns[c].getValueAt(nmr);
							if(!VirtualTable.equals(mValues[c],nmValue,
									nmRelationColumns[c].getType()))
							{
								equal = false;
								break;
							}
						}
						if(equal)
						{
							indices.add(r);
						}
					}
					
					Map<String, Object> map = new HashMap();
					for(VirtualTableColumn mColumn : columnMap.keySet())
					{
						VirtualTableColumn nmColumn = columnMap.get(mColumn);
						map.put(nmColumn.getName(),mColumn.getValueAt(r));
					}
					nmTable.addRow(map,false);
				}
			}
			
			return indices.toArray();
		}
	}
	
	
	static void getChanges(VirtualTable oldState, List<VirtualTableRow> newRows,
			List<VirtualTableRow> changed, List<VirtualTableRow> deleted)
	{
		int[] columnIndices = oldState.getColumnIndices(new ColumnSelector()
		{
			@Override
			public boolean select(VirtualTableColumn column)
			{
				return column.isPersistent() && !column.isAutoIncrement();
			}
		});
		
		int oc = oldState.getRowCount();
		for(int oi = 0; oi < oc; oi++)
		{
			VirtualTableRow oldRow = oldState.getRow(oi);
			Map<String, Object> values = new HashMap();
			for(int col : columnIndices)
			{
				values.put(oldState.getColumnName(col),oldRow.get(col));
			}
			KeyValues kv = new KeyValues(oldState,values);
			
			VirtualTableRow newRow = null;
			int nc = newRows.size();
			for(int ni = 0; ni < nc; ni++)
			{
				VirtualTableRow row = newRows.get(ni);
				if(kv.equals(row))
				{
					newRow = row;
					newRows.remove(ni);
					break;
				}
			}
			
			if(newRow == null)
			{
				deleted.add(oldRow);
			}
			else if(!equals(oldRow,newRow,columnIndices))
			{
				changed.add(newRow);
			}
		}
	}
	
	
	private static boolean equals(VirtualTableRow row1, VirtualTableRow row2, int[] indices)
	{
		for(int index : indices)
		{
			if(!VirtualTable.equals(row1.get(index),row2.get(index),row1.getVirtualTable()
					.getColumnAt(index).getType()))
			{
				return false;
			}
		}
		
		return true;
	}
	
	
	static void synchronize(VirtualTable table, List<VirtualTableRow> added,
			List<VirtualTableRow> changed, List<VirtualTableRow> deleted, DBConnection connection)
			throws DBException
	{
		VirtualTableColumn[] pk = table.getPrimaryKeyColumns();
		if(pk.length == 0)
		{
			throw new VirtualTableException(table,"Cannot synchronize: No primary key defined");
		}
		
		int[] pkIndices = table.getColumnIndices(pk);
		
		int[] colIndices = table.getPersistentColumnIndices();
		VirtualTableColumn[] columns = table.getColumnsAt(colIndices);
		
		Map<WriteRequest, VirtualTableRow> statements = new LinkedHashMap();
		
		if(added != null)
		{
			for(VirtualTableRow row : added)
			{
				INSERT insert = new INSERT().INTO(table);
				List values = new ArrayList();
				
				for(int c = 0; c < columns.length; c++)
				{
					VirtualTableColumn column = columns[c];
					
					if(!column.isAutoIncrement())
					{
						insert.assign(column,"?");
						values.add(row.get(colIndices[c]));
					}
				}
				
				statements.put(new WriteRequest(insert,true,values.toArray()),row);
			}
		}
		
		if(changed != null)
		{
			for(VirtualTableRow row : changed)
			{
				UPDATE update = new UPDATE(table);
				List values = new ArrayList();
				
				for(int c = 0; c < columns.length; c++)
				{
					VirtualTableColumn cd = columns[c];
					update.SET(cd.toSqlField(),"?");
					values.add(row.get(colIndices[c]));
				}
				
				WHERE where = new WHERE();
				for(int pi = 0; pi < pk.length; pi++)
				{
					where.and(pk[pi].eq("?"));
					values.add(row.get(pkIndices[pi]));
				}
				update.WHERE(where);
				
				statements.put(new WriteRequest(update,false,values.toArray()),null);
			}
		}
		
		if(deleted != null)
		{
			for(VirtualTableRow row : deleted)
			{
				DELETE delete = new DELETE().FROM(table);
				List values = new ArrayList();
				
				WHERE where = new WHERE();
				for(int pi = 0; pi < pk.length; pi++)
				{
					where.and(pk[pi].eq("?"));
					values.add(row.get(pkIndices[pi]));
				}
				delete.WHERE(where);
				
				statements.put(new WriteRequest(delete,false,values.toArray()),null);
			}
		}
		
		boolean closeConnection = false;
		if(connection == null)
		{
			connection = DBUtils.getCurrentDataSource().openConnection();
			closeConnection = true;
		}
		
		try
		{
			for(WriteRequest statement : statements.keySet())
			{
				WriteResult result = statement.execute(connection);
				try
				{
					VirtualTableRow row = statements.get(statement);
					if(row != null && result.hasGeneratedKeys())
					{
						row.updateKeys(result.getGeneratedKeys());
					}
				}
				finally
				{
					result.close();
				}
			}
		}
		finally
		{
			if(closeConnection)
			{
				connection.close();
			}
		}
	}
	
	
	/**
	 * Adds a connection of specified detail row to a master row.
	 * <p>
	 * If the detail row that is already connected to the master, this row is
	 * ignored.
	 * </p>
	 * <p>
	 * This method writes changes back to database immediately.
	 * </p>
	 * 
	 * @param masterDetailVT
	 *            the intermediary {@link VirtualTable} connecting master and
	 *            details.
	 * @param masterRow
	 *            a {@link VirtualTableRow} to which details are to be added
	 * @param detailRow
	 *            a {@link VirtualTableRow} to be added.
	 * @throws DBException
	 *             if database operation fails.
	 */
	public static void addDetail(VirtualTable masterDetailVT, VirtualTableRow masterRow,
			VirtualTableRow detailRow) throws DBException
	{
		addDetails(masterDetailVT,masterRow,new VirtualTableRow[]{detailRow});
	}
	
	
	/**
	 * Adds a connection of specified detail row to a master row.
	 * <p>
	 * If the detail row that is already connected to the master, this row is
	 * ignored.
	 * </p>
	 * <p>
	 * This method writes changes back to database immediately.
	 * </p>
	 * 
	 * @param masterDetailVT
	 *            the intermediary {@link VirtualTable} connecting master and
	 *            details.
	 * @param masterRow
	 *            a {@link VirtualTableRow} to which details are to be added
	 * @param detailRow
	 *            a {@link VirtualTableRow} to be added.
	 * @param connection
	 *            a {@link DBConnection} instance for accessing the database
	 * @throws DBException
	 *             if database operation fails.
	 */
	public static void addDetail(VirtualTable masterDetailVT, VirtualTableRow masterRow,
			VirtualTableRow detailRow, DBConnection<?> connection) throws DBException
	{
		addDetails(masterDetailVT,masterRow,new VirtualTableRow[]{detailRow},connection);
	}
	
	
	/**
	 * Adds a connection of specified detail rows within a VirtualTable to a
	 * master row.
	 * <p>
	 * If the details contain rows that are already connected to the master,
	 * those rows are ignored.
	 * </p>
	 * <p>
	 * This method writes changes back to database immediately.
	 * </p>
	 * 
	 * @param masterDetailVT
	 *            the intermediary {@link VirtualTable} connecting master and
	 *            details.
	 * @param masterVT
	 *            a {@link VirtualTable} containing a {@link VirtualTableRow} to
	 *            which details are to be added
	 * @param detailVT
	 *            a {@link VirtualTable} containing {@link VirtualTableRow}s
	 *            that are to be added to the master row.
	 * @throws DBException
	 *             if database operation fails.
	 */
	public static void addDetails(VirtualTable masterDetailVT, VirtualTable masterVT,
			VirtualTable detailVT) throws DBException
	{
		if(masterVT.getRowCount() != 1)
		{
			throw new IllegalStateException(
					"The masterVT has to contain exactly one record, but contains "
							+ masterVT.getRowCount());
		}
		VirtualTableRow masterRow = masterVT.getRow(0);
		VirtualTableRow[] rows = rowsFromVT(detailVT);
		addDetails(masterDetailVT,masterRow,rows);
	}
	
	
	/**
	 * Adds a connection of specified detail rows within a VirtualTable to a
	 * master row.
	 * <p>
	 * If the details contain rows that are already connected to the master,
	 * those rows are ignored.
	 * </p>
	 * <p>
	 * This method writes changes back to database immediately.
	 * </p>
	 * 
	 * @param masterDetailVT
	 *            the intermediary {@link VirtualTable} connecting master and
	 *            details.
	 * @param masterVT
	 *            a {@link VirtualTable} containing a {@link VirtualTableRow} to
	 *            which details are to be added
	 * @param detailVT
	 *            a {@link VirtualTable} containing {@link VirtualTableRow}s
	 *            that are to be added to the master row.
	 * @param connection
	 *            a {@link DBConnection} instance for accessing the database
	 * @throws DBException
	 *             if database operation fails.
	 */
	public static void addDetails(VirtualTable masterDetailVT, VirtualTable masterVT,
			VirtualTable detailVT, DBConnection<?> connection) throws DBException
	{
		if(masterVT.getRowCount() != 1)
		{
			throw new IllegalStateException(
					"The masterVT has to contain exactly one record, but contains "
							+ masterVT.getRowCount());
		}
		VirtualTableRow masterRow = masterVT.getRow(0);
		VirtualTableRow[] rows = rowsFromVT(detailVT);
		addDetails(masterDetailVT,masterRow,rows,connection);
	}
	
	
	/**
	 * Convenience method for
	 * {@link #addDetails(VirtualTable, VirtualTableRow, VirtualTableRow[])}.
	 * <p>
	 * The masterVT has to contain exactly one row.
	 * </p>
	 * <p>
	 * This method writes changes back to database immediately.
	 * </p>
	 * 
	 * @param masterDetailVT
	 *            the intermediary {@link VirtualTable} connecting master and
	 *            details.
	 * @param masterRow
	 *            a {@link VirtualTableRow} to which details are to be added
	 * @param detailVT
	 *            a {@link VirtualTable} containing {@link VirtualTableRow}s
	 *            that are to be added to the master row.
	 * @throws DBException
	 *             if database operation fails.
	 */
	public static void addDetails(VirtualTable masterDetailVT, VirtualTableRow masterRow,
			VirtualTable detailVT) throws DBException
	{
		VirtualTableRow[] rows = rowsFromVT(detailVT);
		addDetails(masterDetailVT,masterRow,rows);
	}
	
	
	/**
	 * Convenience method for
	 * {@link #addDetails(VirtualTable, VirtualTableRow, VirtualTableRow[], DBConnection)}
	 * . .
	 * <p>
	 * The masterVT has to contain exactly one row.
	 * </p>
	 * <p>
	 * This method writes changes back to database immediately.
	 * </p>
	 * 
	 * @param masterDetailVT
	 *            the intermediary {@link VirtualTable} connecting master and
	 *            details.
	 * @param masterRow
	 *            a {@link VirtualTableRow} to which details are to be added
	 * @param detailVT
	 *            a {@link VirtualTable} containing {@link VirtualTableRow}s
	 *            that are to be added to the master row.
	 * @param connection
	 *            a {@link DBConnection} instance for accessing the database
	 * @throws DBException
	 *             if database operation fails.
	 */
	public static void addDetails(VirtualTable masterDetailVT, VirtualTableRow masterRow,
			VirtualTable detailVT, DBConnection<?> connection) throws DBException
	{
		VirtualTableRow[] rows = rowsFromVT(detailVT);
		addDetails(masterDetailVT,masterRow,rows,connection);
	}
	
	
	/**
	 * Adds a connection of a specified list of detail rows to a master row.
	 * <p>
	 * If the details contain rows that are already connected to the master,
	 * those rows are ignored.
	 * </p>
	 * <p>
	 * This method writes changes back to database immediately.
	 * </p>
	 * 
	 * @param masterDetailVT
	 *            the intermediary {@link VirtualTable} connecting master and
	 *            details.
	 * @param masterRow
	 *            a {@link VirtualTableRow} to which details are to be added
	 * @param detailRows
	 *            a list of {@link VirtualTableRow}s that are to be added to the
	 *            master row.
	 * @throws DBException
	 *             if database operation fails.
	 */
	public static void addDetails(VirtualTable masterDetailVT, VirtualTableRow masterRow,
			VirtualTableRow[] detailRows) throws DBException
	{
		DBConnection<?> connection = masterDetailVT.getDataSource().openConnection();
		addDetails(masterDetailVT,masterRow,detailRows,connection);
	}
	
	
	/**
	 * Adds a connection of a specified list of detail rows to a master row.
	 * <p>
	 * If the details contain rows that are already connected to the master,
	 * those rows are ignored.
	 * </p>
	 * <p>
	 * This method writes changes back to database immediately.
	 * </p>
	 * 
	 * @param masterDetailVT
	 *            the intermediary {@link VirtualTable} connecting master and
	 *            details.
	 * @param masterRow
	 *            a {@link VirtualTableRow} to which details are to be added
	 * @param detailRows
	 *            an array of {@link VirtualTableRow}s that are to be added to
	 *            the master row.
	 * @param connection
	 *            a {@link DBConnection} instance for accessing the database
	 * @throws DBException
	 *             if database operation fails.
	 */
	public static void addDetails(VirtualTable masterDetailVT, VirtualTableRow masterRow,
			VirtualTableRow[] detailRows, DBConnection<?> connection) throws DBException
	{
		KeyValues masterKeyValues = MasterDetail.getForeignKeyValues(masterRow,masterDetailVT);
		
		List<VirtualTableRow> rowsToAdd = new ArrayList<VirtualTable.VirtualTableRow>();
		for(VirtualTableRow detailRow : detailRows)
		{
			if(detailRow != null)
			{
				KeyValues detailKeyValues = MasterDetail.getForeignKeyValues(detailRow,
						masterDetailVT);
				
				boolean alreadyExists = false;
				
				VirtualTable masterRowsFromDB = getMasterRowsFromDB(masterDetailVT,masterKeyValues);
				
				for(int i = 0; i < masterRowsFromDB.getRowCount(); i++)
				{
					VirtualTableRow existingRow = masterRowsFromDB.getRow(i);
					if(rowHasKeyValues(masterKeyValues,existingRow)
							&& rowHasKeyValues(detailKeyValues,existingRow))
					{
						alreadyExists = true;
						break;
					}
				}
				
				if(!alreadyExists)
				{
					VirtualTableRow nmRow = masterDetailVT.createRow();
					setKeyValues(masterKeyValues,nmRow);
					setKeyValues(detailKeyValues,nmRow);
					rowsToAdd.add(nmRow);
				}
			}
		}
		
		ManyToMany.synchronize(masterDetailVT,rowsToAdd,Collections.EMPTY_LIST,
				Collections.EMPTY_LIST,connection);
	}
	
	
	/**
	 * Removes all connections of a specified master row related to the
	 * masterDetailVT.
	 * <p>
	 * This method writes changes back to database immediately.
	 * </p>
	 * 
	 * @param masterDetailVT
	 *            the intermediary {@link VirtualTable} connecting master and
	 *            details.
	 * @param masterRow
	 *            a {@link VirtualTableRow} on which all details are to be
	 *            removed
	 * @throws DBException
	 *             if database operation fails.
	 */
	public static void removeAllDetails(VirtualTable masterDetailVT, VirtualTableRow masterRow)
			throws DBException
	{
		removeDetails(masterDetailVT,masterRow,null,true);
	}
	
	
	/**
	 * Removes all connections of a specified master row related to the
	 * masterDetailVT.
	 * <p>
	 * This method writes changes back to database immediately.
	 * </p>
	 * 
	 * @param masterDetailVT
	 *            the intermediary {@link VirtualTable} connecting master and
	 *            details.
	 * @param masterRow
	 *            a {@link VirtualTableRow} on which all details are to be
	 *            removed
	 * @param connection
	 *            a {@link DBConnection} instance for accessing the database
	 * @throws DBException
	 *             if database operation fails.
	 */
	public static void removeAllDetails(VirtualTable masterDetailVT, VirtualTableRow masterRow,
			DBConnection<?> connection) throws DBException
	{
		removeDetails(masterDetailVT,masterRow,null,true,connection);
	}
	
	
	/**
	 * Removes all connections of a specified master row related to the
	 * masterDetailVT.
	 * 
	 * @param masterDetailVT
	 *            the intermediary {@link VirtualTable} connecting master and
	 *            details.
	 * @param masterVT
	 *            a {@link VirtualTable} with a {@link VirtualTableRow} on which
	 *            all details are to be removed
	 * @throws DBException
	 *             if database operation fails.
	 */
	public static void removeAllDetails(VirtualTable masterDetailVT, VirtualTable masterVT)
			throws DBException
	{
		if(masterVT.getRowCount() != 1)
		{
			throw new IllegalStateException(
					"The masterVT has to contain exactly one record, but contains "
							+ masterVT.getRowCount());
		}
		VirtualTableRow masterRow = masterVT.getRow(0);
		removeDetails(masterDetailVT,masterRow,null,true);
	}
	
	
	/**
	 * Removes all connections of a specified master row related to the
	 * masterDetailVT.
	 * 
	 * @param masterDetailVT
	 *            the intermediary {@link VirtualTable} connecting master and
	 *            details.
	 * @param masterVT
	 *            a {@link VirtualTable} with a {@link VirtualTableRow} on which
	 *            all details are to be removed
	 * @param connection
	 *            a {@link DBConnection} instance for accessing the database
	 * @throws DBException
	 *             if database operation fails.
	 */
	public static void removeAllDetails(VirtualTable masterDetailVT, VirtualTable masterVT,
			DBConnection<?> connection) throws DBException
	{
		if(masterVT.getRowCount() != 1)
		{
			throw new IllegalStateException(
					"The masterVT has to contain exactly one record, but contains "
							+ masterVT.getRowCount());
		}
		VirtualTableRow masterRow = masterVT.getRow(0);
		removeDetails(masterDetailVT,masterRow,null,true,connection);
	}
	
	
	/**
	 * Removes the connection of a specified detail row to its master row.
	 * <p>
	 * If the details contain rows that are not connected to the master, this
	 * method will silently ignore those rows.
	 * </p>
	 * <p>
	 * This method writes changes back to database immediately.
	 * </p>
	 * 
	 * @param masterDetailVT
	 *            the intermediary {@link VirtualTable} connecting master and
	 *            details.
	 * @param masterRow
	 *            a {@link VirtualTableRow} on which details are to be removed
	 * @param detailRow
	 *            a {@link VirtualTableRow} on which details are to be removed
	 * @throws DBException
	 *             if database operation fails.
	 */
	public static void removeDetail(VirtualTable masterDetailVT, VirtualTableRow masterRow,
			VirtualTableRow detailRow) throws DBException
	{
		removeDetails(masterDetailVT,masterRow,new VirtualTableRow[]{detailRow});
	}
	
	
	/**
	 * Removes the connection of a specified detail row to its master row.
	 * <p>
	 * If the details contain rows that are not connected to the master, this
	 * method will silently ignore those rows.
	 * </p>
	 * <p>
	 * This method writes changes back to database immediately.
	 * </p>
	 * 
	 * @param masterDetailVT
	 *            the intermediary {@link VirtualTable} connecting master and
	 *            details.
	 * @param masterRow
	 *            a {@link VirtualTableRow} on which details are to be removed
	 * @param detailRow
	 *            a {@link VirtualTableRow} on which details are to be removed
	 * @param connection
	 *            a {@link DBConnection} instance for accessing the database
	 * @throws DBException
	 *             if database operation fails.
	 */
	public static void removeDetail(VirtualTable masterDetailVT, VirtualTableRow masterRow,
			VirtualTableRow detailRow, DBConnection<?> connection) throws DBException
	{
		removeDetails(masterDetailVT,masterRow,new VirtualTableRow[]{detailRow},connection);
	}
	
	
	/**
	 * Removes the connection of specified detail rows within a VirtualTable to
	 * its master row.
	 * <p>
	 * If the details contain rows that are not connected to the master, this
	 * method will silently ignore those rows.
	 * </p>
	 * <p>
	 * This method writes changes back to database immediately.
	 * </p>
	 * 
	 * @param masterDetailVT
	 *            the intermediary {@link VirtualTable} connecting master and
	 *            details.
	 * @param masterRow
	 *            a {@link VirtualTableRow} on which details are to be removed
	 * @param detailVT
	 *            a {@link VirtualTable} containing the {@link VirtualTableRow}s
	 *            to be removed.
	 * @throws DBException
	 *             if database operation fails.
	 */
	public static void removeDetails(VirtualTable masterDetailVT, VirtualTableRow masterRow,
			VirtualTable detailVT) throws DBException
	{
		VirtualTableRow[] rows = rowsFromVT(detailVT);
		removeDetails(masterDetailVT,masterRow,rows);
	}
	
	
	/**
	 * Gets an array of all rows from a {@link VirtualTable}
	 * 
	 * @param vt
	 *            a {@link VirtualTable}
	 * @return a array containing {@link VirtualTableRow}s
	 */
	private static VirtualTableRow[] rowsFromVT(VirtualTable vt)
	{
		int rowCount = vt.getRowCount();
		VirtualTableRow[] rows = new VirtualTableRow[rowCount];
		for(int i = 0; i < rowCount; i++)
		{
			rows[i] = vt.getRow(i);
		}
		return rows;
	}
	
	
	/**
	 * Removes the connection of specified detail rows within a VirtualTable to
	 * its master row.
	 * <p>
	 * If the details contain rows that are not connected to the master, this
	 * method will silently ignore those rows.
	 * </p>
	 * <p>
	 * This method writes changes back to database immediately.
	 * </p>
	 * 
	 * @param masterDetailVT
	 *            the intermediary {@link VirtualTable} connecting master and
	 *            details.
	 * @param masterRow
	 *            a {@link VirtualTableRow} on which details are to be removed
	 * @param detailVT
	 *            a {@link VirtualTable} containing the {@link VirtualTableRow}s
	 *            to be removed.
	 * @param connection
	 *            a {@link DBConnection} instance for accessing the database
	 * @throws DBException
	 *             if database operation fails.
	 */
	public static void removeDetails(VirtualTable masterDetailVT, VirtualTableRow masterRow,
			VirtualTable detailVT, DBConnection<?> connection) throws DBException
	{
		VirtualTableRow[] rows = rowsFromVT(detailVT);
		removeDetails(masterDetailVT,masterRow,rows,connection);
	}
	
	
	/**
	 * Convenience method for
	 * {@link #removeDetails(VirtualTable, VirtualTableRow, VirtualTable)}.
	 * <p>
	 * The masterVT has to contain exactly one row.
	 * </p>
	 * <p>
	 * This method writes changes back to database immediately.
	 * </p>
	 * 
	 * @param masterDetailVT
	 *            the intermediary {@link VirtualTable} connecting master and
	 *            details.
	 * @param masterVT
	 *            a {@link VirtualTable} containing the {@link VirtualTableRow}
	 *            on which details are to be removed
	 * @param detailVT
	 *            a {@link VirtualTable} containing the {@link VirtualTableRow}s
	 *            to be removed.
	 * @throws DBException
	 *             if database operation fails.
	 */
	public static void removeDetails(VirtualTable masterDetailVT, VirtualTable masterVT,
			VirtualTable detailVT) throws DBException
	{
		if(masterVT.getRowCount() != 1)
		{
			throw new IllegalStateException(
					"The masterVT has to contain exactly one record, but contains "
							+ masterVT.getRowCount());
		}
		VirtualTableRow masterRow = masterVT.getRow(0);
		
		VirtualTableRow[] rows = rowsFromVT(detailVT);
		removeDetails(masterDetailVT,masterRow,rows);
	}
	
	
	/**
	 * Convenience method for
	 * {@link #removeDetails(VirtualTable, VirtualTableRow, VirtualTableRow[], DBConnection)}
	 * .
	 * <p>
	 * The masterVT has to contain exactly one row.
	 * </p>
	 * <p>
	 * This method writes changes back to database immediately.
	 * </p>
	 * 
	 * @param masterDetailVT
	 *            the intermediary {@link VirtualTable} connecting master and
	 *            details.
	 * @param masterVT
	 *            a {@link VirtualTable} containing the {@link VirtualTableRow}
	 *            on which details are to be removed
	 * @param detailVT
	 *            a {@link VirtualTable} containing the {@link VirtualTableRow}s
	 *            to be removed.
	 * @param connection
	 *            a {@link DBConnection} instance for accessing the database
	 * @throws DBException
	 *             if database operation fails.
	 */
	public static void removeDetails(VirtualTable masterDetailVT, VirtualTable masterVT,
			VirtualTable detailVT, DBConnection<?> connection) throws DBException
	{
		if(masterVT.getRowCount() != 1)
		{
			throw new IllegalStateException(
					"The masterVT has to contain exactly one record, but contains "
							+ masterVT.getRowCount());
		}
		VirtualTableRow masterRow = masterVT.getRow(0);
		
		VirtualTableRow[] rows = rowsFromVT(detailVT);
		removeDetails(masterDetailVT,masterRow,rows,connection);
	}
	
	
	/**
	 * Removes the connection of a specified list of detail rows to its master
	 * row.
	 * <p>
	 * If the details contain rows that are not connected to the master, this
	 * method will silently ignore those rows.
	 * </p>
	 * <p>
	 * This method writes changes back to database immediately.
	 * </p>
	 * 
	 * @param masterDetailVT
	 *            the intermediary {@link VirtualTable} connecting master and
	 *            details.
	 * @param masterRow
	 *            a {@link VirtualTableRow} on which details are to be removed
	 * @param detailRows
	 *            a list of {@link VirtualTableRow}s that are to be removed from
	 *            the master row.
	 * @throws DBException
	 *             if database operation fails.
	 */
	public static void removeDetails(VirtualTable masterDetailVT, VirtualTableRow masterRow,
			VirtualTableRow[] detailRows) throws DBException
	{
		removeDetails(masterDetailVT,masterRow,detailRows,false);
	}
	
	
	/**
	 * Removes the connection of a specified list of detail rows to its master
	 * row.
	 * <p>
	 * If the details contain rows that are not connected to the master, this
	 * method will silently ignore those rows.
	 * </p>
	 * <p>
	 * This method writes changes back to database immediately.
	 * </p>
	 * 
	 * @param masterDetailVT
	 *            the intermediary {@link VirtualTable} connecting master and
	 *            details.
	 * @param masterRow
	 *            a {@link VirtualTableRow} on which details are to be removed
	 * @param detailRows
	 *            a list of {@link VirtualTableRow}s that are to be removed from
	 *            the master row.
	 * @param connection
	 *            a {@link DBConnection} instance for accessing the database
	 * @throws DBException
	 *             if database operation fails.
	 */
	public static void removeDetails(VirtualTable masterDetailVT, VirtualTableRow masterRow,
			VirtualTableRow[] detailRows, DBConnection<?> connection) throws DBException
	{
		removeDetails(masterDetailVT,masterRow,detailRows,false,connection);
	}
	
	
	/**
	 * Removes the connection of a specified list of detail rows to its master
	 * row.
	 * <p>
	 * If the details contain rows that are not connected to the master, this
	 * method will silently ignore those rows.
	 * </p>
	 * <p>
	 * This method writes changes back to database immediately.
	 * </p>
	 * 
	 * @param masterDetailVT
	 *            the intermediary {@link VirtualTable} connecting master and
	 *            details.
	 * @param masterRow
	 *            a {@link VirtualTableRow} on which details are to be removed
	 * @param detailRows
	 *            a list of {@link VirtualTableRow}s that are to be removed from
	 *            the master row.
	 * @param deleteAllDetails
	 *            if set to {@code true} all details rows of the master get
	 *            removed
	 * @throws DBException
	 *             if database operation fails.
	 */
	private static void removeDetails(VirtualTable masterDetailVT, VirtualTableRow masterRow,
			VirtualTableRow[] detailRows, boolean deleteAllDetails) throws DBException
	{
		DBConnection<?> connection = masterDetailVT.getDataSource().openConnection();
		removeDetails(masterDetailVT,masterRow,detailRows,deleteAllDetails,connection);
	}
	
	
	/**
	 * Removes the connection of a specified list of detail rows to its master
	 * row.
	 * <p>
	 * If the details contain rows that are not connected to the master, this
	 * method will silently ignore those rows.
	 * </p>
	 * <p>
	 * This method writes changes back to database immediately.
	 * </p>
	 * 
	 * @param masterDetailVT
	 *            the intermediary {@link VirtualTable} connecting master and
	 *            details.
	 * @param masterRow
	 *            a {@link VirtualTableRow} on which details are to be removed
	 * @param detailRows
	 *            a list of {@link VirtualTableRow}s that are to be removed from
	 *            the master row.
	 * @param deleteAllDetails
	 *            if set to {@code true} all details rows of the master get
	 *            removed
	 * @param connection
	 *            a {@link DBConnection} instance for accessing the database
	 * @throws DBException
	 *             if database operation fails.
	 */
	private static void removeDetails(VirtualTable masterDetailVT, VirtualTableRow masterRow,
			VirtualTableRow[] detailRows, boolean deleteAllDetails, DBConnection<?> connection)
			throws DBException
	{
		KeyValues masterKeyValues = MasterDetail.getForeignKeyValues(masterRow,masterDetailVT);
		
		VirtualTable masterRowsFromDB = getMasterRowsFromDB(masterDetailVT,masterKeyValues);
		
		List<VirtualTableRow> rowsToDelete = new ArrayList<VirtualTable.VirtualTableRow>();
		for(int i = 0; i < masterRowsFromDB.getRowCount(); i++)
		{
			VirtualTableRow row = masterRowsFromDB.getRow(i);
			
			boolean rowToBeDeleted = false;
			if(rowHasKeyValues(masterKeyValues,row))
			{
				if(!deleteAllDetails)
				{
					for(VirtualTableRow detailRow : detailRows)
					{
						if(detailRow != null)
						{
							KeyValues detailKeyValues = MasterDetail.getForeignKeyValues(detailRow,
									masterRowsFromDB);
							if(rowHasKeyValues(detailKeyValues,row))
							{
								rowToBeDeleted = true;
								break;
							}
						}
					}
				}
				else
				{
					rowToBeDeleted = true;
				}
			}
			if(rowToBeDeleted)
			{
				rowsToDelete.add(row);
			}
		}
		ManyToMany.synchronize(masterDetailVT,Collections.EMPTY_LIST,Collections.EMPTY_LIST,
				rowsToDelete,connection);
	}
	
	
	/**
	 * Executes a query on the masterdetail table for querying the current rows
	 * related to a specified master row.
	 * 
	 * @param masterDetailVT
	 *            the masterdetail vt (not changed by this method call)
	 * @param masterKeyValues
	 *            an instance of {@link KeyValues} for uniquely identifying the
	 *            master row.
	 * @return a instance of {@link VirtualTable} containing the rows related to
	 *         the master row.
	 * @throws DBException
	 *             if query fails.
	 */
	private static VirtualTable getMasterRowsFromDB(VirtualTable masterDetailVT,
			KeyValues masterKeyValues) throws DBException
	{
		VirtualTable masterDetailVTFromDB = masterDetailVT.clone();
		SELECT select = masterDetailVTFromDB.getDefaultSelect();
		WHERE where = new WHERE();
		List values = new ArrayList();
		masterKeyValues.appendCondition(where,values);
		Object[] params = values.toArray(new Object[values.size()]);
		select.WHERE(where);
		masterDetailVTFromDB.queryAndFill(select,params);
		return masterDetailVTFromDB;
	}
	
	
	/**
	 * Checks if a {@link VirtualTableRow} matches the specified
	 * {@link KeyValues}.
	 * 
	 * @param keyValues
	 *            the {@link KeyValues} to match against.
	 * @param nmRow
	 *            a {@link VirtualTableRow} to be checked.
	 * @return {@code true} if row matches keyValues
	 */
	private static boolean rowHasKeyValues(KeyValues keyValues, VirtualTableRow nmRow)
	{
		boolean haveSame = true;
		for(String columnName : keyValues.getColumnNames())
		{
			Object detailValue = keyValues.getValue(columnName);
			VirtualTable nmTable = nmRow.getVirtualTable();
			Object nmColumnValue = nmRow.get(nmTable.getColumn(columnName));
			if(!detailValue.equals(nmColumnValue))
			{
				haveSame = false;
				break;
			}
		}
		return haveSame;
	}
	
	
	/**
	 * Sets the specified {@link KeyValues} in a {@link VirtualTableRow}.
	 * 
	 * @param keyValues
	 *            the {@link KeyValues} to be set
	 * @param nmRow
	 *            the target {@link VirtualTableRow} to set the
	 *            {@link KeyValues} on.
	 */
	private static void setKeyValues(KeyValues keyValues, VirtualTableRow nmRow)
	{
		for(String columnName : keyValues.getColumnNames())
		{
			Object value = keyValues.getValue(columnName);
			nmRow.set(columnName,value);
		}
	}
}
