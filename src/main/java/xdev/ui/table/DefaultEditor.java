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
package xdev.ui.table;


import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.table.TableCellEditor;

import xdev.vt.VirtualTable;
import xdev.vt.VirtualTableColumn;


public class DefaultEditor extends JTextField implements EditorDelegate, ActionListener
{
	private TableCellEditor	editor;


	public DefaultEditor()
	{
		setBorder(XdevTableEditor.DEFAULT_BORDER);
		addActionListener(this);
	}


	@Override
	public void actionPerformed(ActionEvent e)
	{
		if(editor != null)
		{
			editor.stopCellEditing();
		}
	}


	@Override
	public Component getFocusComponent()
	{
		return this;
	}


	@Override
	public Component getEditorComponent(TableCellEditor editor, JTable table, Object value,
			int row, int column, VirtualTable vt, VirtualTableColumn vtColumn)
	{
		this.editor = editor;

		setText(value == null ? "" : value.toString());
		setHorizontalAlignment(vtColumn != null ? vtColumn.getHorizontalAlignment()
				: SwingConstants.LEADING);

		SwingUtilities.invokeLater(new Runnable()
		{
			public void run()
			{
				selectAll();
			}
		});

		return this;
	}


	@Override
	public Object getEditorValue()
	{
		return getText();
	}
	
	
	@Override
	public void editingCanceled()
	{
	}


	@Override
	public void editingStopped()
	{
	}
}
