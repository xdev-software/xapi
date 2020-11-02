package xdev.ui.table;

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


import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JTable;
import javax.swing.table.TableCellEditor;

import xdev.ui.FormularComponent;
import xdev.ui.XdevComboBox;
import xdev.util.StringUtils;
import xdev.vt.TableColumnLink;
import xdev.vt.VirtualTable;
import xdev.vt.VirtualTableColumn;
import xdev.vt.VirtualTableException;
import xdev.vt.VirtualTables;


public class DetailEditor implements EditorDelegate
{
	private TableCellEditor				editor;
	private int							row;
	private boolean						refreshing	= false;
	private Map<String, DetailComboBox>	cache;
	private DetailComboBox				combo;
	

	public DetailEditor()
	{
		cache = new HashMap();
	}
	

	@Override
	public Component getEditorComponent(TableCellEditor editor, JTable table, Object value,
			int row, int column, VirtualTable vt, VirtualTableColumn vtColumn)
	{
		this.editor = editor;
		this.row = row;
		
		try
		{
			refreshing = true;
			
			String key = vt.getName() + "." + vtColumn.getName();
			combo = cache.get(key);
			if(combo == null)
			{
				combo = new DetailComboBox(vt,vtColumn);
				cache.put(key,combo);
			}
			
			combo.setSelectedItem(value);
		}
		finally
		{
			refreshing = false;
		}
		
		return combo;
	}
	

	@Override
	public Component getFocusComponent()
	{
		return combo;
	}
	

	@Override
	public Object getEditorValue()
	{
		return combo.getSelectedEntry().getItem();
	}
	

	@Override
	public void editingCanceled()
	{
	}
	

	@Override
	public void editingStopped()
	{
		if(combo == null || combo.correspondingForeignKeyColumn == null)
		{
			return;
		}
		
		Object foreignKey = combo.getSelectedData();
		combo.vt.setValueAt(foreignKey,row,combo.correspondingForeignKeyColumn.getName());
	}
	


	protected class DetailComboBox extends XdevComboBox implements ActionListener
	{
		protected VirtualTable			vt;
		protected VirtualTableColumn	correspondingForeignKeyColumn;
		

		public DetailComboBox(VirtualTable vt, VirtualTableColumn column)
				throws VirtualTableException
		{
			super();
			
			this.vt = vt;
			
			putClientProperty("JComboBox.isTableCellEditor",Boolean.TRUE);
			setBorder(XdevTableEditor.DEFAULT_BORDER);
			addActionListener(this);
			
			TableColumnLink link = column.getTableColumnLink();
			VirtualTable linkedTable = VirtualTables.getVirtualTable(link.getLinkedTable());
			if(linkedTable == null)
			{
				VirtualTableException.throwVirtualTableNotFound(link.getLinkedTable());
			}
			
			VirtualTableColumn linkedColumn = linkedTable.getColumn(link.getLinkedColumn());
			if(linkedColumn == null)
			{
				VirtualTableException.throwColumnNotFound(linkedTable,link.getLinkedColumn());
			}
			
			correspondingForeignKeyColumn = column.getCorrespondingForeignKeyColumn();
			
			String[] ids = link.getLinkedKeyColumns();
			setModel(linkedTable,linkedColumn.getName(),
					StringUtils.concat(FormularComponent.DATA_FIELD_SEPARATOR,(Object[])ids),true);
		}
		

		@Override
		public void actionPerformed(ActionEvent e)
		{
			if(!refreshing && DetailEditor.this.editor != null)
			{
				DetailEditor.this.editor.stopCellEditing();
			}
		}
	}
}
