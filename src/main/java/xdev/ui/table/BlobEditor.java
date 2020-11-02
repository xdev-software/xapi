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
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.JToolBar;
import javax.swing.table.TableCellEditor;

import xdev.io.ByteHolder;
import xdev.io.IOUtils;
import xdev.ui.GBC;
import xdev.ui.GraphicUtils;
import xdev.ui.ImageFileFilter;
import xdev.ui.XdevFileChooser;
import xdev.ui.XdevFileChooserFactory;
import xdev.util.logging.LoggerFactory;
import xdev.util.logging.XdevLogger;
import xdev.vt.DataFlavor;
import xdev.vt.VirtualTable;
import xdev.vt.VirtualTableColumn;
import xdev.vt.XdevBlob;


public class BlobEditor extends BlobRenderer implements EditorDelegate
{
	/**
	 * Logger instance for this class.
	 */
	private static final XdevLogger	log			= LoggerFactory.getLogger(BlobEditor.class);
	
	private JButton			cmdOpen;
	private JButton			cmdSave;

	private TableCellEditor	editor;
	private Object			value;
	private DataFlavor		flavor;
	private JTable			table;
	private int				editingRow;


	public BlobEditor()
	{
		cmdOpen = new JButton(GraphicUtils.loadResIcon("open.png"));
		cmdOpen.setBorder(BorderFactory.createEmptyBorder());
		cmdOpen.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent event)
			{
				File file = getFileChooser().showOpenDialog();
				if(file != null)
				{
					FileInputStream in = null;
					try
					{
						in = new FileInputStream(file);
						value = new XdevBlob(IOUtils.readData(in));
						editor.stopCellEditing();
					}
					catch(Exception e)
					{
						log.error(e);
					}
					finally
					{
						IOUtils.closeSilent(in);
					}
				}
			}
		});

		cmdSave = new JButton(GraphicUtils.loadResIcon("save.png"));
		cmdSave.setBorder(BorderFactory.createEmptyBorder());
		cmdSave.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent event)
			{
				File file = getFileChooser().showSaveDialog();
				if(file != null)
				{
					FileOutputStream out = null;
					try
					{
						out = new FileOutputStream(file);

						byte[] bytes = null;
						if(value instanceof ByteHolder)
						{
							bytes = ((ByteHolder)value).toByteArray();
						}
						else if(value instanceof byte[])
						{
							bytes = (byte[])value;
						}

						out.write(bytes);
					}
					catch(Exception e)
					{
						log.error(e);
					}
					finally
					{
						IOUtils.closeSilent(out);
					}
				}
			}
		});

		JToolBar toolbar = new JToolBar();
		toolbar.setFloatable(false);
		toolbar.setRollover(true);
		toolbar.setBorder(BorderFactory.createEmptyBorder());
		toolbar.add(cmdOpen);
		toolbar.add(cmdSave);

		setLayout(new GridBagLayout());
		add(toolbar,new GBC(1,1,1,1,0.0,0.0,GBC.CENTER,GBC.NONE,0));
	}


	protected XdevFileChooser getFileChooser()
	{
		String key = getClass().getName();

		switch(flavor)
		{
			case IMAGE:
				return XdevFileChooserFactory.getFileChooser(key,ImageFileFilter.getInstance());
		}

		return XdevFileChooserFactory.getFileChooser(key,true);
	}


	@Override
	public Component getEditorComponent(TableCellEditor editor, JTable table, Object value,
			int row, int column, VirtualTable vt, VirtualTableColumn vtColumn)
	{
		this.editor = editor;
		this.value = value;
		this.flavor = vtColumn != null ? vtColumn.getDataFlavor() : DataFlavor.UNDEFINED;
		this.table = table;
		this.editingRow = row;

		int length = 0;
		if(value instanceof XdevBlob)
		{
			length = ((XdevBlob)value).length();
		}
		else if(value instanceof byte[])
		{
			length = ((byte[])value).length;
		}

		cmdSave.setEnabled(length > 0);

		super.getRendererComponent(table,value,row,column,vt,vtColumn,true);

		return this;
	}


	@Override
	public Component getFocusComponent()
	{
		return this;
	}


	@Override
	public Object getEditorValue()
	{
		return value;
	}


	@Override
	public void editingCanceled()
	{
	}


	@Override
	public void editingStopped()
	{
		// refresh table row height
		table.setRowHeight(editingRow,table.getRowHeight());
	}
}
