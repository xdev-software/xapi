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
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.table.TableCellEditor;

import xdev.ui.DatePopup;
import xdev.ui.DatePopupCustomizer;
import xdev.ui.DatePopupOwner;
import xdev.ui.GBC;
import xdev.ui.XdevDateDropDownButton;
import xdev.ui.text.TextFormat;
import xdev.vt.VirtualTable;
import xdev.vt.VirtualTableColumn;


public class DateEditor extends JPanel implements EditorDelegate, DatePopupOwner, ActionListener
{
	private JTextField		txt;
	private JLabel			dropDown;
	private DatePopup		datePopup	= null;
	private TableCellEditor	editor;
	private TextFormat		format;
	
	
	public DateEditor()
	{
		super(new GridBagLayout());
		
		txt = new JTextField(15);
		txt.setBorder(BorderFactory.createEmptyBorder());
		txt.addActionListener(this);
		
		setBackground(txt.getBackground());
		
		dropDown = new JLabel(XdevDateDropDownButton.calendarIcon);
		dropDown.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mousePressed(MouseEvent e)
			{
				setDatePopupVisible(!isDatePopupVisible());
			}
		});
		
		add(txt,new GBC(1,1,1,1,1.0,0.0,GBC.CENTER,GBC.HORIZONTAL,0));
		add(dropDown,new GBC(2,1,1,1,0.0,0.0,GBC.CENTER,GBC.NONE,0));
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
		return txt;
	}
	
	
	@Override
	public Component getEditorComponent(TableCellEditor editor, JTable table, Object value,
			int row, int column, VirtualTable vt, VirtualTableColumn vtColumn)
	{
		this.editor = editor;
		
		format = vtColumn.getTextFormat();
		txt.setText(value instanceof Date ? format.format(value) : "");
		txt.setHorizontalAlignment(vtColumn.getHorizontalAlignment());
		
		SwingUtilities.invokeLater(new Runnable()
		{
			public void run()
			{
				txt.selectAll();
			}
		});
		
		return this;
	}
	
	
	@Override
	public Object getEditorValue()
	{
		String s = txt.getText().trim();
		if(s.length() == 0)
		{
			return null;
		}
		
		try
		{
			return format.parseDate(s);
		}
		catch(Exception e)
		{
			return null;
		}
	}
	
	
	@Override
	public void editingCanceled()
	{
		setDatePopupVisible(false);
	}
	
	
	@Override
	public void editingStopped()
	{
		setDatePopupVisible(false);
	}
	
	
	@Override
	public DatePopupCustomizer getDatePopupCustomizer()
	{
		return null;
	}
	
	
	@Override
	public TextFormat getTextFormat()
	{
		return format;
	}
	
	
	@Override
	public Component getComponentForDatePopup()
	{
		return dropDown;
	}
	
	
	@Override
	public String getText()
	{
		return txt.getText();
	}
	
	
	@Override
	public void setText(String text)
	{
		txt.setText(text);
		
		if(editor != null)
		{
			editor.stopCellEditing();
		}
	}
	
	
	public void setDatePopupVisible(boolean b)
	{
		if(b)
		{
			datePopup = DatePopup.getInstance(this);
			datePopup.showPopup(this);
		}
		else
		{
			hideDatePopup();
		}
	}
	
	
	public boolean isDatePopupVisible()
	{
		return datePopup != null && datePopup.isVisible();
	}
	
	
	@Override
	public void hideDatePopup()
	{
		if(datePopup != null && datePopup.isVisible())
		{
			datePopup.hidePopup();
			datePopup = null;
		}
	}
}
