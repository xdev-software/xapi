package xdev.ui;

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


/**
 * @author XDEV Software (MP)
 * @since 
 *
 */
import java.awt.Component;
import java.awt.Font;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JViewport;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;


/*
 *	Use a JTable as a renderer for row numbers of a given main table.
 *  This table must be added to the row header of the scrollpane that
 *  contains the main table.
 */
public class RowNumberTable extends JTable implements ChangeListener, PropertyChangeListener
{
	private static int		COLUMN_WIDTH	= 20;
	
	private final JTable	main;
	protected int			clickedRow		= -1;
	
	
	public RowNumberTable(JTable table)
	{
		main = table;
		main.addPropertyChangeListener(this);
		
		setFocusable(false);
		setAutoCreateColumnsFromModel(false);
		setModel(main.getModel());
		setSelectionModel(main.getSelectionModel());
		
		TableColumn column = new TableColumn();
		column.setHeaderValue(" ");
		addColumn(column);
		column.setCellRenderer(new RowNumberRenderer());
		
		getColumnModel().getColumn(0).setPreferredWidth(COLUMN_WIDTH);
		setPreferredScrollableViewportSize(getPreferredSize());
		
		getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
	}
	
	
	/**
	 * @return the clickedRow
	 */
	public int getClickedRow()
	{
		return clickedRow;
	}
	
	
	/**
	 * @param clickedRow
	 *            the clickedRow to set
	 */
	public void setClickedRow(int clickedRow)
	{
		this.clickedRow = clickedRow;
	}
	
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.JTable#setSelectionModel(javax.swing.ListSelectionModel)
	 */
	@Override
	public void setSelectionModel(ListSelectionModel paramListSelectionModel)
	{
		// TODO not implement yet to handle multiple selection
		super.setSelectionModel(paramListSelectionModel);
	}
	
	
	@Override
	public void addNotify()
	{
		super.addNotify();
		
		Component c = getParent();
		
		// Keep scrolling of the row table in sync with the main table.
		
		if(c instanceof JViewport)
		{
			JViewport viewport = (JViewport)c;
			viewport.addChangeListener(this);
		}
	}
	
	
	/*
	 * Delegate method to main table
	 */
	@Override
	public int getRowCount()
	{
		return main.getRowCount();
	}
	
	
	@Override
	public int getRowHeight(int row)
	{
		return main.getRowHeight(row);
	}
	
	
	/*
	 * This table does not use any data from the main TableModel, so just return
	 * a value based on the row parameter.
	 */
	@Override
	public Object getValueAt(int row, int column)
	{
		return Integer.toString(row + 1);
	}
	
	
	/*
	 * Don't edit data in the main TableModel by mistake
	 */
	@Override
	public boolean isCellEditable(int row, int column)
	{
		return false;
	}
	
	
	//
	// Implement the ChangeListener
	//
	@Override
	public void stateChanged(ChangeEvent e)
	{
		// Keep the scrolling of the row table in sync with main table
		
		JViewport viewport = (JViewport)e.getSource();
		JScrollPane scrollPane = (JScrollPane)viewport.getParent();
		scrollPane.getVerticalScrollBar().setValue(viewport.getViewPosition().y);
	}
	
	
	//
	// Implement the PropertyChangeListener
	//
	@Override
	public void propertyChange(PropertyChangeEvent e)
	{
		// Keep the row table in sync with the main table
		if("selectionModel".equals(e.getPropertyName()))
		{
			setSelectionModel(main.getSelectionModel());
		}
		
		if("model".equals(e.getPropertyName()))
		{
			setModel(main.getModel());
		}
	}
	
	
	
	/*
	 * Borrow the renderer from JDK1.4.2 table header
	 */
	private static class RowNumberRenderer extends DefaultTableCellRenderer
	{
		
		ImageIcon	lockedIcon		= GraphicUtils.loadResIcon("locked.png");
		
		ImageIcon	unlockedIcon	= GraphicUtils.loadResIcon("unlocked.png");
		
		
		public RowNumberRenderer()
		{
			setHorizontalAlignment(JLabel.CENTER);
		}
		
		
		@Override
		public Component getTableCellRendererComponent(JTable table, Object value,
				boolean isSelected, boolean hasFocus, int row, int column)
		{
			if(table != null)
			{
				JTableHeader header = table.getTableHeader();
				
				if(header != null)
				{
					setForeground(header.getForeground());
					setBackground(header.getBackground());
					setFont(header.getFont());
				}
			}
			
			int clickedRow = ((RowNumberTable)table).getClickedRow();
			if(row == clickedRow && row != -1)
			{
				setFont(getFont().deriveFont(Font.BOLD));
				setIcon(lockedIcon);
			}
			else
			{
				setIcon(unlockedIcon);
				
			}
			
			// setText((value == null) ? "" : value.toString());
			setBorder(UIManager.getBorder("TableHeader.cellBorder"));
			return this;
		}
	}
}
