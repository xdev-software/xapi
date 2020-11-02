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


import java.awt.*;
import java.awt.event.*;
import java.text.*;

import javax.swing.*;
import javax.swing.table.*;
import javax.swing.text.*;

import xdev.vt.*;


public class NumberEditor extends JFormattedTextField implements EditorDelegate, ActionListener
{
	private TableCellEditor	editor;


	public NumberEditor()
	{
		setBorder(XdevTableEditor.DEFAULT_BORDER);
		addActionListener(this);
	}


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

		setFormatterFactory(new DefaultFormatterFactory(new NumberFormatter((NumberFormat)vtColumn
				.getTextFormat().getFormat())));
		try
		{
			setValue(value);
		}
		catch(Exception e)
		{
			try
			{
				setValue(0);
			}
			catch(Exception e2)
			{
				setValue(vtColumn.getDefaultValue());
			}
		}

		setHorizontalAlignment(vtColumn.getHorizontalAlignment());

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
		try
		{
			commitEdit();
		}
		catch(ParseException e)
		{
		}

		return getValue();
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
