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
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import javax.swing.SwingConstants;

import xdev.db.ColumnMetaData;
import xdev.db.DataType;
import xdev.db.sql.Column;
import xdev.db.sql.Field;
import xdev.db.sql.Table;
import xdev.lang.Copyable;
import xdev.ui.text.TextFormat;
import xdev.util.Settings;


/**
 * This class represents a column within a {@link VirtualTable}.
 * <p>
 * On the one hand it is an abstraction of a column in a database table.
 * Therefore it has properties like name, data type, length, nullability, and an
 * autoIncrement value.
 * </p>
 * <p>
 * On the other hand it is used for displaying data on the screen and therefore
 * has properties like visibility, editability, preferredWidth, alignment and
 * caption.
 * </p>
 * 
 * @see Column
 * 
 * @author XDEV Software Corp.
 * 
 * @param <T>
 *            the type of this {@link VirtualTableColumn}
 */
public class VirtualTableColumn<T> extends Column implements Serializable,
		Copyable<VirtualTableColumn<T>>, SwingConstants
{
	/**
	 * Helper method which returns the names of the given columns.
	 * 
	 * @param columns
	 *            the columns to get the names for
	 * @return the names as a string array
	 * @since 3.2
	 */
	public static String[] getNamesOf(VirtualTableColumn... columns)
	{
		int c = columns.length;
		String[] names = new String[c];
		for(int i = 0; i < c; i++)
		{
			names[i] = columns[i].getName();
		}
		return names;
	}
	
	final static VirtualTableColumn[]				NO_COLUMN			= new VirtualTableColumn[0];
	
	private static final long						serialVersionUID	= -7935649244234165972L;
	
	private VirtualTable							virtualTable;
	
	private String									name				= null;
	
	private DataType								type				= null;
	
	/**
	 * Number of characters
	 */
	private int										length				= 50;
	
	/**
	 * Number of digits right of the decimal point
	 */
	private int										scale				= 0;
	
	private Object									defaultValue		= null;
	
	private boolean									nullable			= false;
	private boolean									autoIncrement		= false;
	
	private boolean									visible				= true;
	private boolean									editable			= true;
	private String									caption				= null;
	private int										preferredWidth		= 100;
	
	/**
	 * @since 4.0
	 */
	private int										minWidth			= 0;
	/**
	 * @since 4.0
	 */
	private int										maxWidth			= 0;
	
	private int										horizontalAlignment	= LEFT;
	private TextFormat								textFormat			= null;
	
	private boolean									persistent			= true;
	private TableColumnLink							tableColumnLink		= null;
	
	private DataFlavor								dataFlavor			= DataFlavor.UNDEFINED;
	
	/**
	 * @since 3.1
	 */
	private boolean									autoTruncate		= true;
	/**
	 * @since 3.1
	 */
	private transient List<Validator<? super T>>	validators;
	
	private transient Comparator					comparator;
	
	
	/**
	 * Initializes new instance of a {@link VirtualTableColumn}.
	 * 
	 * @param name
	 *            the column name
	 */
	public VirtualTableColumn(String name)
	{
		super(name);
		
		this.name = name;
		
		type = DataType.VARCHAR;
		length = 50;
		
		defaultValue = "";
		nullable = true;
		
		caption = "";
		
		initFormat();
	}
	
	
	/**
	 * Initializes a new instance of a {@link VirtualTableColumn}.
	 * <p>
	 * The properties of this {@link VirtualTableColumn} are set according to a
	 * specified database column.
	 * </p>
	 * 
	 * @param meta
	 *            a {@link ColumnMetaData} object describing a database column.
	 * @see ColumnMetaData
	 */
	public VirtualTableColumn(ColumnMetaData meta)
	{
		super("");
		
		if(Settings.swapColumnNameAndCaption())
		{
			caption = meta.getName();
			name = meta.getCaption();
			if(name == null || name.length() == 0)
			{
				name = caption;
			}
		}
		else
		{
			name = meta.getName();
			caption = meta.getCaption();
		}
		
		_updateDelegate(name);
		
		type = meta.getType();
		length = meta.getLength();
		scale = meta.getScale();
		nullable = meta.isNullable();
		autoIncrement = meta.isAutoIncrement();
		
		initFormat();
	}
	
	
	private void initFormat()
	{
		textFormat = new TextFormat(this);
		
		if(isAutoIncrement())
		{
			editable = false;
			visible = false;
			
			textFormat.setType(TextFormat.NUMBER);
			textFormat.setMinFractionDigits(0);
			textFormat.setMaxFractionDigits(0);
		}
		else
		{
			visible = true;
			editable = true;
			
			if(type.isInt())
			{
				textFormat.setType(TextFormat.NUMBER);
				textFormat.setMinFractionDigits(0);
				textFormat.setMaxFractionDigits(0);
			}
			else if(type.isDecimal())
			{
				textFormat.setType(TextFormat.NUMBER);
			}
			else if(type.isDate())
			{
				textFormat.setType(TextFormat.DATETIME);
				
				switch(type)
				{
					case DATE:
						textFormat.setDateUse(TextFormat.USE_DATE_ONLY);
					break;
					case TIME:
						textFormat.setDateUse(TextFormat.USE_TIME_ONLY);
					break;
					case TIMESTAMP:
						textFormat.setDateUse(TextFormat.USE_DATE_AND_TIME);
					break;
				}
				
				textFormat.setDateStyle(DateFormat.SHORT);
				textFormat.setTimeStyle(DateFormat.SHORT);
			}
		}
		
		checkPreferredWidth();
	}
	
	
	/**
	 * Sets the preferred width of a column according to the type of this
	 * {@link VirtualTableColumn}.
	 */
	public void checkPreferredWidth()
	{
		if(preferredWidth == -1)
		{
			switch(type)
			{
				case BOOLEAN:
				case TINYINT:
				case SMALLINT:
				case INTEGER:
				case BIGINT:
				case REAL:
				case FLOAT:
				case DOUBLE:
				case DECIMAL:
				case NUMERIC:
					preferredWidth = 50;
				break;
				
				case DATE:
				case TIME:
				case TIMESTAMP:
					preferredWidth = 100;
				break;
				
				case CHAR:
				case VARCHAR:
				case LONGVARCHAR:
				case CLOB:
					if(length <= 0)
					{
						preferredWidth = 250;
					}
					else
					{
						preferredWidth = Math.min(250,5 * length);
					}
				break;
			}
		}
	}
	
	
	/**
	 * Creates and returns a copy of this {@link VirtualTableColumn}.
	 * 
	 * @return a copied {@link VirtualTableColumn}
	 */
	/**
	 * {@inheritDoc}
	 */
	@Override
	public VirtualTableColumn<T> clone()
	{
		return new VirtualTableColumn(this);
	}
	
	
	private VirtualTableColumn(VirtualTableColumn col)
	{
		super(col.name);
		
		name = col.name;
		type = col.type;
		length = col.length;
		scale = col.scale;
		defaultValue = col.defaultValue;
		nullable = col.nullable;
		autoIncrement = col.autoIncrement;
		visible = col.visible;
		editable = col.editable;
		preferredWidth = col.preferredWidth;
		minWidth = col.minWidth;
		maxWidth = col.maxWidth;
		caption = col.caption;
		horizontalAlignment = col.horizontalAlignment;
		textFormat = col.textFormat != null ? col.textFormat.clone() : null;
		persistent = col.persistent;
		tableColumnLink = col.tableColumnLink != null ? col.tableColumnLink.clone() : null;
		dataFlavor = col.dataFlavor;
		autoTruncate = col.autoTruncate;
	}
	
	
	/**
	 * Set the {@link VirtualTable} of this {@link VirtualTableColumn}.
	 * 
	 * @param virtualTable
	 *            the {@link VirtualTable} to set.
	 */
	void setVirtualTable(VirtualTable virtualTable)
	{
		this.virtualTable = virtualTable;
		
		_updateDelegate();
	}
	
	
	private void _updateDelegate()
	{
		String name = this.name;
		
		Table table;
		if(!persistent && tableColumnLink != null)
		{
			table = VirtualTables.getVirtualTable(tableColumnLink.getLinkedTable());
			name = tableColumnLink.getLinkedColumn();
		}
		else
		{
			table = virtualTable;
		}
		
		if(table != null)
		{
			_updateDelegate(table,name);
		}
		else
		{
			_updateDelegate(name);
		}
	}
	
	
	/**
	 * Returns the {@link VirtualTable} of this {@link VirtualTableColumn}.
	 * 
	 * @return a {@link VirtualTable}
	 */
	public VirtualTable getVirtualTable()
	{
		return virtualTable;
	}
	
	
	/**
	 * Sets the name of this {@link VirtualTableColumn}.
	 * 
	 * @param name
	 *            a column name
	 */
	public void setName(String name)
	{
		this.name = name;
	}
	
	
	/**
	 * Returns the name of this {@link VirtualTableColumn}.
	 * 
	 * @return the column name
	 */
	public String getName()
	{
		return name;
	}
	
	
	/**
	 * Returns the full qualified name of this {@link VirtualTableColumn}.
	 * 
	 * @return the full qualified column name - vtPath.vtName.vtColumnName.
	 * 
	 * @since 4.0
	 */
	public String getFullQualifiedName()
	{
		return this.getVirtualTable().getName() + "." + this.getName();
	}
	
	
	/**
	 * Sets the data type of this {@link VirtualTableColumn}.
	 * 
	 * @param type
	 *            the {@link DataType} to set
	 */
	public void setType(DataType type)
	{
		this.type = type;
		this.textFormat = null;
	}
	
	
	/**
	 * Sets the data type and length of this {@link VirtualTableColumn}.
	 * 
	 * @param type
	 *            the {@link DataType} to set
	 * @param length
	 *            the length of the column
	 */
	public void setType(DataType type, int length)
	{
		setType(type);
		setLength(length);
	}
	
	
	/**
	 * Sets the data type, length and scope of this {@link VirtualTableColumn}.
	 * 
	 * @param type
	 *            the {@link DataType} to set
	 * @param length
	 *            the length of the column
	 * @param scale
	 *            the number of digits to the right of the decimal point
	 */
	public void setType(DataType type, int length, int scale)
	{
		setType(type);
		setLength(length);
		setScale(scale);
	}
	
	
	/**
	 * Returns the data type of this {@link VirtualTableColumn}.
	 * 
	 * @return the {@link DataType}
	 */
	public DataType getType()
	{
		return type;
	}
	
	
	/**
	 * Returns the the length of this {@link VirtualTableColumn}.
	 * 
	 * @return the length (number of characters)
	 */
	public int getLength()
	{
		return length;
	}
	
	
	/**
	 * Sets the length of this {@link VirtualTableColumn}.
	 * 
	 * @param length
	 *            the number of characters
	 */
	public void setLength(int length)
	{
		this.length = length;
	}
	
	
	/**
	 * Returns the the scale of this {@link VirtualTableColumn}.
	 * 
	 * @return scale the number of digits right of the decimal point (defaults
	 *         to 0).
	 */
	public int getScale()
	{
		return scale;
	}
	
	
	/**
	 * Sets the scale of this {@link VirtualTableColumn}.
	 * 
	 * @param scale
	 *            the number of digits right of the decimal point (defaults to
	 *            0).
	 */
	public void setScale(int scale)
	{
		this.scale = scale;
	}
	
	
	/**
	 * Get the default value of this {@link VirtualTableColumn}.
	 * 
	 * @return the default value
	 */
	public Object getDefaultValue()
	{
		return defaultValue;
	}
	
	
	/**
	 * Sets the default value of this {@link VirtualTableColumn}.
	 * 
	 * @param defaultValue
	 *            to set
	 */
	public void setDefaultValue(Object defaultValue)
	{
		this.defaultValue = defaultValue;
	}
	
	
	/**
	 * Resets this {@link VirtualTableColumn} to its standard default value.
	 */
	public void resetDefaultValue()
	{
		defaultValue = getStandardDefaultValue();
	}
	
	
	/**
	 * Returns the standard default value for a {@link VirtualTableColumn}.
	 * <p>
	 * Uses:
	 * <ul>
	 * <li><code>null</code> for nullable columns</li>
	 * <li><code>0</code> for integer columns</li>
	 * <li><code>0.0</code> for decimal columns</li>
	 * <li><code>""</code> for other columns</li>
	 * </ul>
	 * </p>
	 * 
	 * @return the standard default value
	 */
	public Object getStandardDefaultValue()
	{
		if(nullable)
		{
			return null;
		}
		else if(type.isInt())
		{
			return 0;
		}
		else if(type.isDecimal())
		{
			return 0.0;
		}
		else
		{
			return "";
		}
	}
	
	
	/**
	 * Returns, if this {@link VirtualTableColumn} is nullable.
	 * 
	 * @return true, if {@link VirtualTableColumn} is nullable.
	 */
	public boolean isNullable()
	{
		return nullable;
	}
	
	
	/**
	 * Sets the nullability for this {@link VirtualTableColumn}.
	 * 
	 * @param nullable
	 *            if set to <code>true</code>, column is nullable
	 */
	public void setNullable(boolean nullable)
	{
		this.nullable = nullable;
	}
	
	
	/**
	 * Returns, if this {@link VirtualTableColumn} is incremented automatically.
	 * <p>
	 * The values of auto incremented columns are set automatically, when new
	 * rows are added.
	 * </p>
	 * 
	 * @return true, if {@link VirtualTableColumn} is incremented automatically.
	 */
	public boolean isAutoIncrement()
	{
		return autoIncrement;
	}
	
	
	/**
	 * Sets the autoIncrement value for this {@link VirtualTableColumn}.
	 * 
	 * @param autoIncrement
	 *            if set to <code>true</code>, column is incremented
	 *            automatically.
	 */
	public void setAutoIncrement(boolean autoIncrement)
	{
		this.autoIncrement = autoIncrement;
	}
	
	
	/**
	 * Returns, if this {@link VirtualTableColumn} is visible.
	 * 
	 * @return true, if this column is visible.
	 */
	public boolean isVisible()
	{
		return visible;
	}
	
	
	/**
	 * Set the visibility of this {@link VirtualTableColumn}.
	 * 
	 * @param visible
	 *            if set to true, column is visible.
	 */
	public void setVisible(boolean visible)
	{
		this.visible = visible;
	}
	
	
	/**
	 * Returns, if the values of this column are editable.
	 * 
	 * @return true, if the values of this column are editable
	 */
	public boolean isEditable()
	{
		return editable;
	}
	
	
	/**
	 * Sets the editability of this {@link VirtualTableColumn}s column values.
	 * 
	 * @param editable
	 *            if set to true, the values of the column are editable.
	 */
	public void setEditable(boolean editable)
	{
		this.editable = editable;
	}
	
	
	/**
	 * Returns the caption of this {@link VirtualTableColumn}.
	 * 
	 * @return the caption
	 */
	public String getCaption()
	{
		return caption;
	}
	
	
	/**
	 * Sets the caption of this {@link VirtualTableColumn}.
	 * 
	 * @param caption
	 *            a caption to be set.
	 */
	public void setCaption(String caption)
	{
		this.caption = caption;
	}
	
	
	/**
	 * Returns the preferred width of this {@link VirtualTableColumn}.
	 * 
	 * @return the preferred width
	 */
	public int getPreferredWidth()
	{
		return preferredWidth;
	}
	
	
	/**
	 * Sets the preferred width of this {@link VirtualTableColumn}.
	 * 
	 * @param preferredWidth
	 *            to be set
	 * @see #checkPreferredWidth()
	 */
	public void setPreferredWidth(int preferredWidth)
	{
		this.preferredWidth = preferredWidth;
	}
	
	
	/**
	 * Returns the minimum width of this {@link VirtualTableColumn}.
	 * 
	 * @return the minimum width
	 * 
	 * @since 4.0
	 */
	public int getMinWidth()
	{
		return minWidth;
	}
	
	
	/**
	 * Sets the minimum width of this {@link VirtualTableColumn}.
	 * 
	 * @param minWidth
	 *            to be set
	 * 
	 * @since 4.0
	 */
	public void setMinWidth(int minWidth)
	{
		this.minWidth = minWidth;
	}
	
	
	/**
	 * Returns the maximum width of this {@link VirtualTableColumn}.
	 * 
	 * @return the maximum width
	 * 
	 * @since 4.0
	 */
	public int getMaxWidth()
	{
		return maxWidth;
	}
	
	
	/**
	 * Sets the maximum width of this {@link VirtualTableColumn}.
	 * 
	 * @param maxWidth
	 *            to be set
	 * 
	 * @since 4.0
	 */
	public void setMaxWidth(int maxWidth)
	{
		this.maxWidth = maxWidth;
	}
	
	
	/**
	 * Returns the horizontal alignment of this {@link VirtualTableColumn}
	 * <p>
	 * The valid values are defined in class {@link SwingConstants}. Defaults to
	 * <code>SwingConstants.LEFT</code>.
	 * </p>
	 * 
	 * @return the horizontal alignment as {@link SwingConstants} value
	 */
	public int getHorizontalAlignment()
	{
		return horizontalAlignment;
	}
	
	
	/**
	 * Sets the horizontal alignment of this {@link VirtualTableColumn}
	 * <p>
	 * The valid values are defined in class {@link SwingConstants}. Defaults to
	 * <code>SwingConstants.LEFT</code>.
	 * </p>
	 * 
	 * @param horizontalAlignment
	 *            the horizontal alignment as {@link SwingConstants} value
	 */
	public void setHorizontalAlignment(int horizontalAlignment)
	{
		this.horizontalAlignment = horizontalAlignment;
	}
	
	
	/**
	 * Returns the {@link TextFormat} of this {@link VirtualTableColumn}.
	 * <p>
	 * If no {@link TextFormat} is present, a new one will be initialized.
	 * </p>
	 * 
	 * @return the {@link TextFormat}
	 */
	public TextFormat getTextFormat()
	{
		if(textFormat == null)
		{
			initFormat();
		}
		
		return textFormat;
	}
	
	
	/**
	 * Sets the {@link TextFormat} of this {@link VirtualTableColumn}.
	 * 
	 * @param textFormat
	 *            a {@link TextFormat} to be set.
	 */
	public void setTextFormat(TextFormat textFormat)
	{
		this.textFormat = textFormat;
	}
	
	
	/**
	 * Returns, if this {@link VirtualTableColumn} is persistent. Persistence
	 * means, that the columns data gets propagated to the database.
	 * 
	 * @return true, if this column is persistent.
	 * @see #getTableColumnLink()
	 */
	public boolean isPersistent()
	{
		return persistent;
	}
	
	
	/**
	 * Sets the persistence of this {@link VirtualTableColumn}. Persistence
	 * means, that the columns data gets propagated to the database.
	 * 
	 * @param persistent
	 *            if set to true, data gets persisted to the database.
	 */
	public void setPersistent(boolean persistent)
	{
		this.persistent = persistent;
		
		if(virtualTable != null)
		{
			_updateDelegate();
		}
	}
	
	
	/**
	 * Returns this {@link VirtualTableColumn}s column link, may be
	 * <code>null</code>.
	 * 
	 * @return the {@link TableColumnLink} of this column
	 * @see #isPersistent()
	 */
	public TableColumnLink getTableColumnLink()
	{
		return tableColumnLink;
	}
	
	
	/**
	 * Links this {@link VirtualTableColumn} with another
	 * {@link VirtualTableColumn} through the application's
	 * {@link EntityRelationshipModel}.
	 * 
	 * @param tableColumnLink
	 *            the link to the other {@link VirtualTableColumn}
	 */
	public void setTableColumnLink(TableColumnLink tableColumnLink)
	{
		this.tableColumnLink = tableColumnLink;
		
		if(virtualTable != null)
		{
			_updateDelegate();
		}
	}
	
	
	/**
	 * Returns the foreign key column corresponding to the
	 * {@link TableColumnLink} of this non-persistent column or
	 * <code>null</code> if no corresponding column is found.
	 * <p>
	 * If this column has no link an {@link IllegalStateException} is thrown.
	 * 
	 * @return the corresponding foreign key column or <code>null</code>
	 * 
	 * @throws IllegalStateException
	 *             if this column has no {@link TableColumnLink}
	 */
	public VirtualTableColumn getCorrespondingForeignKeyColumn() throws IllegalStateException
	{
		final TableColumnLink link = this.tableColumnLink;
		if(link == null)
		{
			throw new IllegalStateException("column has no link");
		}
		
		VirtualTable vt = getVirtualTable();
		String master = vt.getName();
		for(VirtualTableColumn column : vt)
		{
			EntityRelationship relation = EntityRelationships.getModel().getRelationship(master,
					new String[]{column.getName()},link.getLinkedTable());
			if(relation != null)
			{
				return column;
			}
		}
		
		return null;
	}
	
	
	/**
	 * Returns the data flavor of this column.
	 * 
	 * @return the data flavor of this column
	 */
	public DataFlavor getDataFlavor()
	{
		return dataFlavor;
	}
	
	
	/**
	 * Sets the data flavor of this column.
	 * 
	 * @param dataFlavor
	 *            the new data flavor
	 */
	public void setDataFlavor(DataFlavor dataFlavor)
	{
		this.dataFlavor = dataFlavor;
	}
	
	
	/**
	 * Returns <code>true</code> if character values in this columns are
	 * automatically truncated, <code>false</code> otherwiese.
	 * 
	 * @return <code>true</code> if character values in this columns are
	 *         automatically truncated, <code>false</code> otherwiese.
	 * @see #setAutoTruncate(boolean)
	 * @since 3.1
	 */
	public boolean isAutoTruncate()
	{
		return autoTruncate;
	}
	
	
	/**
	 * Sets if character values will be automatically truncated.
	 * <p>
	 * This setting becomes effective if a character value with a length longer
	 * then the length of this column is inserted in the VirtualTable.<br>
	 * If <code>autoTruncate</code> is <code>true</code> the value will be
	 * truncated, otherwise a {@link VirtualTableException} is thrown.
	 * 
	 * @param autoTruncate
	 *            <code>true</code> if character values should be truncated
	 *            automatically, <code>false</code> otherwise
	 * @since 3.1
	 */
	public void setAutoTruncate(boolean autoTruncate)
	{
		this.autoTruncate = autoTruncate;
	}
	
	
	/**
	 * Convenience method for
	 * <code>getVirtualTable().getValueAt(int,String)</code>.
	 * 
	 * @param row
	 *            the index of the row (0-based)
	 * @return the value of the cell at the specified position.
	 * @see #getVirtualTable()
	 * @see VirtualTable#getValueAt(int,String)
	 */
	public Object getValueAt(int row)
	{
		return virtualTable.getValueAt(row,name);
	}
	
	
	/**
	 * Convenience method for
	 * <code>getVirtualTable().getFormattedValueAt(int,String)</code>.
	 * 
	 * @param row
	 *            the index of the row (0-based)
	 * @return a formatted {@link String} representation of the specified value.
	 * @see #getVirtualTable()
	 * @see VirtualTable#getFormattedValueAt(int, String)
	 */
	public String getFormattedValueAt(int row)
	{
		return virtualTable.getFormattedValueAt(row,name);
	}
	
	
	/**
	 * Convenience method for <code>getVirtualTable().getRowCount()</code>.
	 * 
	 * @return the number of rows
	 * @see #getVirtualTable()
	 * @see VirtualTable#getRowCount()
	 */
	public int getRowCount()
	{
		return virtualTable.getRowCount();
	}
	
	
	/**
	 * Returns the hashcode of for this {@link VirtualTableColumn} using the
	 * column name.
	 * 
	 * @return the hashcode of for this {@link VirtualTableColumn} using the
	 *         column name
	 */
	@Override
	public int hashCode()
	{
		return name.hashCode();
	}
	
	
	/**
	 * Returns, if this {@link VirtualTableColumn} equals another one.
	 * <p>
	 * Two columns art considered equal, if they are both of type
	 * {@link VirtualTableColumn} and have equal column names.
	 * </p>
	 * 
	 * @param obj
	 *            the other {@link VirtualTableColumn}
	 * @return true, if both columns are equals.
	 */
	@Override
	public boolean equals(Object obj)
	{
		if(this == obj)
		{
			return true;
		}
		if(obj == null)
		{
			return false;
		}
		if(getClass() != obj.getClass())
		{
			return false;
		}
		VirtualTableColumn other = (VirtualTableColumn)obj;
		return other.name.equals(name);
	}
	
	
	/**
	 * Returns the name of the {@link VirtualTableColumn} as a {@link String}.
	 * 
	 * @return the name of the {@link VirtualTableColumn}
	 */
	@Override
	public String toString()
	{
		return name;
	}
	
	
	/**
	 * Returns a {@link Column} object for use in a SQL-Engine query for this
	 * {@link VirtualTableColumn}.
	 * 
	 * @return a {@link Column} object.
	 * @deprecated The VirtualTableColumn is a SQLColumn itself since 3.2
	 * @see #toSqlColumn(Table)
	 * @see #toSqlField()
	 */
	@Deprecated
	public Column toSqlColumn()
	{
		return this;
	}
	
	
	public Column toSqlColumn(Table table)
	{
		return new Column(table,name);
	}
	
	
	public Field toSqlField()
	{
		return new Field(name,type,length,length,scale,!nullable,virtualTable.isUnique(this),
				defaultValue);
	}
	
	
	/**
	 * Returns a {@link ColumnMetaData} representation of this
	 * {@link VirtualTableColumn}.
	 * 
	 * @param vt
	 *            The containing table
	 */
	public ColumnMetaData toColumnMetaData(VirtualTable vt)
	{
		return new ColumnMetaData(vt.getDatabaseAlias(),name,caption,type,length,scale,
				defaultValue,nullable,autoIncrement);
	}
	
	
	/**
	 * Adds a user defined validator for this column.
	 * 
	 * @param validator
	 *            the validator to add
	 * @since 3.1
	 */
	public void addValidator(Validator<? super T> validator)
	{
		if(this.validators == null)
		{
			this.validators = new ArrayList();
		}
		
		this.validators.add(validator);
	}
	
	
	/**
	 * Removes a user defined validator for this column.
	 * 
	 * @param validator
	 *            the validator to remove
	 * @since 3.1
	 */
	public void removeValidator(Validator<? super T> validator)
	{
		if(this.validators != null)
		{
			this.validators.remove(validator);
		}
	}
	
	
	/**
	 * Gets all validators of this column, if no validator is set a empty array
	 * is returnd.
	 * 
	 * @return the validators of this column
	 * @since 3.1
	 */
	public Validator<? super T>[] getValidators()
	{
		if(validators == null)
		{
			return new Validator[0];
		}
		
		return validators.toArray(new Validator[validators.size()]);
	}
	
	
	/**
	 * Called from VirtualTable before insertion of a value
	 * 
	 * @since 3.1
	 */
	void validate(Object value) throws VirtualTableException
	{
		if(validators != null)
		{
			for(Validator validator : validators)
			{
				validator.validate(value,this);
			}
		}
	}
	
	
	/**
	 * Returns the current {@link Comparator} for this
	 * {@link VirtualTableColumn}.
	 * <p>
	 * If no comparator exists, a new default instance will be created.
	 * </p>
	 * 
	 * @return a {@link Comparator}
	 */
	public Comparator getComparator()
	{
		if(comparator == null)
		{
			comparator = createDefaultComparator();
		}
		
		return comparator;
	}
	
	
	/**
	 * Sets a custom {@link Comparator} for this {@link VirtualTableColumn}.
	 * 
	 * @param comparator
	 *            a {@link Comparator} to be set.
	 */
	public void setComparator(Comparator comparator)
	{
		this.comparator = comparator;
	}
	
	
	protected Comparator createDefaultComparator()
	{
		Comparator c;
		
		switch(type)
		{
			case BINARY:
			case VARBINARY:
			case BLOB:
				c = new ByteArrayComparator();
				break;
				
			default:
				c = new DefaultComparator();
		}
		
		return new NullAwareComparator(c);
	}
	
	
	
	public static class NullAwareComparator implements Comparator
	{
		protected Comparator	actualComparator;
		
		
		public NullAwareComparator(Comparator actualComparator)
		{
			this.actualComparator = actualComparator;
		}
		
		
		public int compare(Object o1, Object o2)
		{
			if(o1 == null)
			{
				return o2 == null ? 0 : -1;
			}
			if(o2 == null)
			{
				return o1 == null ? 0 : 1;
			}
			
			return actualComparator.compare(o1,o2);
		}
	}
	
	
	
	public static class DefaultComparator implements Comparator
	{
		public int compare(Object o1, Object o2)
		{
			return ((Comparable)o1).compareTo(o2);
		}
	}
	
	
	
	public static class ByteArrayComparator implements Comparator<byte[]>
	{
		public int compare(byte[] o1, byte[] o2)
		{
			long length1 = o1.length;
			long length2 = o2.length;
			return length1 < length2 ? -1 : length1 == length2 ? 0 : 1;
		}
	}
}
