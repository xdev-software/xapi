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
/**
 * 
 */

package xdev.vt;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.swing.table.JTableHeader;
import javax.swing.table.TableModel;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mockito;

import xdev.db.DBException;
import xdev.db.DataType;
import xdev.db.Index;
import xdev.db.Index.IndexType;
import xdev.db.QueryInfo;
import xdev.db.sql.SELECT;
import xdev.io.XdevObjectInputStream;
import xdev.io.XdevObjectOutputStream;
import xdev.ui.ItemList;
import xdev.ui.XdevFormular;
import xdev.ui.XdevTable;
import xdev.ui.text.TextFormat;
import xdev.util.XdevList;
import xdev.vt.VirtualTable.ColumnSelector;
import xdev.vt.VirtualTable.VirtualTableRow;
import xdev.vt.dummies.MyVirtualTableExtend;
import xdev.vt.dummies.PersonVT;
import xdev.vt.dummies.PersonVT2;


/**
 * @author XDEV Software (FHAE)
 * 
 */
@Ignore
public class VirtualTableTest
{
	private PersonVT vt;
	
	private final Object[][] data = {
		{
			"Mustermann",
			2500,
			75.05,
			new java.sql.Date(629716210000L),
			new java.sql.Time(17755000),
			new java.sql.Timestamp(629716210000L),
			false},
		{
			"Testmann",
			1200,
			55.89,
			new java.sql.Date(803203810000L),
			new java.sql.Time(36671000),
			new java.sql.Timestamp(803203810000L),
			true},
		{
			"Klinger",
			4000,
			80.33,
			new java.sql.Date(1104534100000L),
			new java.sql.Time(76942000),
			new java.sql.Timestamp(1104534100000L),
			false}};
	
	private final ColumnSelector selector = column -> column.getType() == DataType.INTEGER;
	/**
	 * Default {@link QueryInfo} for testing. QueryInfo as <code>String</code>:
	 * 
	 * 
	 */
	private QueryInfo defaultQueryInfo;
	
	/**
	 * Default select for this <code>vt</code>. Include all persistent columns.
	 */
	private SELECT defaultSelect;
	
	@Before
	public void init() throws VirtualTableException, DBException
	{
		this.vt = new PersonVT();
		// Fill the table
		for(int count = 0; count < this.data.length; count++)
		{
			this.vt.addRow(false, this.data[count]);
		}
		
		final SELECT select = new SELECT();
		select.FROM(this.vt);
		this.defaultQueryInfo = new QueryInfo(select);
		
		this.defaultSelect = new SELECT().FROM(this.vt);
		
		for(final VirtualTableColumn<?> col : this.vt.columns)
		{
			if(col.isPersistent())
			{
				this.defaultSelect.columns(col);
			}
		}
	}
	
	/**
	 * Include {@link Writer} and {@link Reader} for CSV export and import.
	 * 
	 * 
	 * @author XDEV Software (FHAE)
	 * 
	 */
	class CSVHelper
	{
		final File file = new File("wrktest\\CSVHelperFile.txt");
		Writer writer = null;
		Reader reader = null;
		
		public CSVHelper() throws IOException
		{
			this.refreshHelper();
		}
		
		public Writer getWriter()
		{
			
			return this.writer;
			
		}
		
		public Reader getReader() throws FileNotFoundException
		{
			return this.reader;
		}
		
		/**
		 * Initialize the {@link Writer} and {@link Reader} with a new instance.
		 * 
		 * @throws IOException
		 */
		public void refreshHelper() throws IOException
		{
			this.writer = new BufferedWriter(new FileWriter(this.file));
			this.reader = new BufferedReader(new FileReader(this.file));
			this.file.deleteOnExit();
		}
	}
	
	// /////////////////////////////////////////////////////////////////////////
	// constructors //
	// ///////////////////
	
	/**
	 * Test method for {@link xdev.vt.VirtualTable#VirtualTable(xdev.db.Result)}
	 * .
	 */
	@Test
	public void testVirtualTableResult()
	{
		// test run by dbjunit
	}
	
	/**
	 * Test method for
	 * {@link xdev.vt.VirtualTable#VirtualTable(xdev.db.Result, boolean)}.
	 */
	@Test
	public void testVirtualTableResultBoolean()
	{
		// test run by dbjunit
	}
	
	/**
	 * Test method for
	 * {@link xdev.vt.VirtualTable#VirtualTable(java.lang.String, java.lang.String, xdev.vt.VirtualTableColumn[])}
	 * .
	 */
	@Test
	public void testVirtualTableStringStringVirtualTableColumnArray()
	{
		// test run by dbjunit
	}
	
	/**
	 * Test method for
	 * {@link xdev.vt.VirtualTable#VirtualTable(java.lang.String, java.lang.String, xdev.vt.VirtualTableColumn[], xdev.vt.VirtualTableColumn, java.lang.Object[][])}
	 * .
	 */
	@Test
	public void testVirtualTableStringStringVirtualTableColumnArrayVirtualTableColumnObjectArrayArray()
	{
		// test run by dbjunit
	}
	
	// /////////////////////////////////////////////////////////////////////////
	// declared tests //
	// ///////////////////
	
	/**
	 * Test method for {@link xdev.vt.VirtualTable#clone()}.
	 */
	@Test
	public void testClone()
	{
		final VirtualTable cloneVT = this.vt.clone();
		
		for(int i = 0; i < this.vt.getColumnCount(); i++)
		{
			assertEquals(this.vt.getColumnAt(i), cloneVT.getColumnAt(i));
		}
	}
	
	/**
	 * Test method for {@link xdev.vt.VirtualTable#clone(boolean)}.
	 */
	@Test
	public void testCloneBoolean()
	{
		final VirtualTable cloneVT = this.vt.clone(true);
		
		// Verify the columns
		for(int i = 0; i < this.vt.getColumnCount(); i++)
		{
			assertEquals(this.vt.getColumnAt(i), cloneVT.getColumnAt(i));
		}
		
		// Verify the data
		for(int row = 0; row < this.vt.getRowCount(); row++)
		{
			for(int col = 0; col < this.vt.getColumnCount(); col++)
			{
				assertEquals(this.vt.getValueAt(row, col), cloneVT.getValueAt(row, col));
			}
		}
	}
	
	/**
	 * Test method for {@link xdev.vt.VirtualTable#copyHeader()}.
	 */
	@Test
	public void testCopyHeader()
	{
		final VirtualTable cloneVT = this.vt.copyHeader();
		// Verify the columns
		for(int i = 0; i < this.vt.getColumnCount(); i++)
		{
			assertEquals(this.vt.getColumnAt(i), cloneVT.getColumnAt(i));
		}
	}
	
	/**
	 * Test method for {@link xdev.vt.VirtualTable#addIndex(xdev.db.Index)} and
	 * {@link xdev.vt.VirtualTable#isUnique(xdev.vt.VirtualTableColumn)}.
	 */
	@Test
	public void testAddIndex()
	{
		final VirtualTable vt = PersonVT2.VT;
		
		assertFalse(vt.isUnique(PersonVT2.id));
		
		final VirtualTableColumn<?> vtCol1 = vt.getColumnAt(0);
		final VirtualTableColumn<?> vtCol2 = vt.getColumnAt(1);
		
		final Index index = new Index(
			"SYS_PK_10048",
			IndexType.PRIMARY_KEY,
			vtCol1.getName(),
			vtCol2.getName());
		vt.addIndex(index);
		
		final VirtualTableColumn<?>[] primaryKey = vt.getPrimaryKeyColumns();
		assertEquals(primaryKey[0], vtCol1);
		assertEquals(primaryKey[1], vtCol2);
		
		assertTrue(vt.isUnique(vt.getColumnAt(0)));
	}
	
	/**
	 * Test for Method {@link VirtualTable#addIndex(Index)}. The
	 * {@link NullPointerException} is tested
	 */
	@Test(expected = NullPointerException.class)
	public void addIndextNPException()
	{
		final Index in = new Index("test", IndexType.NORMAL, "nonexistent");
		this.vt.addIndex(in);
	}
	
	/**
	 * private class to test the {@link VirtualTable}.
	 * 
	 * @author XDEV Software (FHAE)
	 * 
	 */
	class MyVTListener implements VirtualTableListener
	{
		/**
		 * The current action as <code>String</code>.
		 */
		String action = null;
		/**
		 * Action delete as <code>String</code>.
		 */
		final String actionDelete = "Deleted";
		/**
		 * Action insert as <code>String</code>.
		 */
		final String actionInsert = "Inserted";
		/**
		 * Action update as <code>String</code>.
		 */
		final String actionUpdate = "Update";
		/**
		 * Action data changed as <code>String</code>.
		 */
		final String actionDataChanged = "DataChanged";
		/**
		 * Action structure changed as <code>String</code>.
		 */
		final String actionStructureChanged = "TableStructureChanged";
		
		@Override
		public void virtualTableRowDeleted(final VirtualTableEvent event)
		{
			this.action = this.actionDelete;
			
		}
		
		@Override
		public void virtualTableRowInserted(final VirtualTableEvent event)
		{
			this.action = this.actionInsert;
			
		}
		
		@Override
		public void virtualTableRowUpdated(final VirtualTableEvent event)
		{
			this.action = this.actionUpdate;
		}
		
		@Override
		public void virtualTableDataChanged(final VirtualTableEvent event)
		{
			this.action = this.actionDataChanged;
			
		}
		
		@Override
		public void virtualTableStructureChanged(final VirtualTableEvent event)
		{
			this.action = this.actionStructureChanged;
			
		}
	}
	
	/**
	 * Test method for
	 * {@link xdev.vt.VirtualTable#addVirtualTableListener(xdev.vt.VirtualTableListener)}
	 * .
	 * 
	 * @throws DBException
	 * @throws VirtualTableException
	 */
	@Test
	public void testAddVirtualTableListener() throws VirtualTableException, DBException
	{
		final MyVTListener l = new MyVTListener();
		
		this.vt.addVirtualTableListener(l);
		
		// Delete
		this.vt.removeRow(0, false);
		assertEquals(l.actionDelete, l.action);
		
		// Insert
		this.vt.addRow();
		assertEquals(l.actionInsert, l.action);
		
		// Update
		final HashMap<String, Object> map = new HashMap<>();
		map.put("Name", "UPDATE");
		this.vt.updateRow(map, 0, false);
		assertEquals(l.actionUpdate, l.action);
		
		// DataChanged
		this.vt.clearData();
		assertEquals(l.actionDataChanged, l.action);
		
		// StructureChanged
		this.vt.removeColumn(0);
		assertEquals(l.actionStructureChanged, l.action);
	}
	
	/**
	 * Test method for
	 * {@link xdev.vt.VirtualTable#removeVirtualTableListener(xdev.vt.VirtualTableListener)}
	 * .
	 */
	@Test
	public void testRemoveVirtualTableListener()
	{
		final MyVTListener l = new MyVTListener();
		this.vt.addVirtualTableListener(l);
		this.vt.removeVirtualTableListener(l);
		// Insert
		this.vt.addRow();
		assertEquals(null, l.action);
		
	}
	
	/**
	 * Test method for
	 * {@link xdev.vt.VirtualTable#fireRowDeleted(xdev.vt.VirtualTable.VirtualTableRow, int)}
	 * .
	 * 
	 * @throws DBException
	 * @throws VirtualTableException
	 */
	@Test
	public void testFireRowDeleted() throws VirtualTableException, DBException
	{
		final MyVTListener l = new MyVTListener();
		this.vt.addVirtualTableListener(l);
		// Delete
		this.vt.removeRow(0, false);
		assertEquals(l.actionDelete, l.action);
	}
	
	/**
	 * Test method for
	 * {@link xdev.vt.VirtualTable#fireRowInserted(xdev.vt.VirtualTable.VirtualTableRow, int)}
	 * .
	 */
	@Test
	public void testFireRowInserted()
	{
		final MyVTListener l = new MyVTListener();
		this.vt.addVirtualTableListener(l);
		// Insert
		this.vt.addRow();
		assertEquals(l.actionInsert, l.action);
	}
	
	/**
	 * Test method for
	 * {@link xdev.vt.VirtualTable#fireRowUpdated(xdev.vt.VirtualTable.VirtualTableRow, int)}
	 * .
	 * 
	 * @throws DBException
	 * @throws VirtualTableException
	 */
	@Test
	public void testFireRowUpdated() throws VirtualTableException, DBException
	{
		final MyVTListener l = new MyVTListener();
		this.vt.addVirtualTableListener(l);
		// Update
		final HashMap<String, Object> map = new HashMap<>();
		map.put("Name", "UPDATE");
		this.vt.updateRow(map, 0, false);
		assertEquals(l.actionUpdate, l.action);
	}
	
	/**
	 * Test method for {@link xdev.vt.VirtualTable#fireDataChanged()}.
	 */
	@Test
	public void testFireDataChanged()
	{
		final MyVTListener l = new MyVTListener();
		this.vt.addVirtualTableListener(l);
		// DataChanged
		this.vt.clearData();
		assertEquals(l.actionDataChanged, l.action);
	}
	
	/**
	 * Test method for {@link xdev.vt.VirtualTable#fireStructureChanged()}.
	 */
	@Test
	public void testFireStructureChanged()
	{
		final MyVTListener l = new MyVTListener();
		this.vt.addVirtualTableListener(l);
		// StructureChanged
		this.vt.removeColumn(0);
		assertEquals(l.actionStructureChanged, l.action);
	}
	
	/**
	 * Test method for {@link xdev.vt.VirtualTable#getName()}.
	 */
	@Test
	public void testGetName()
	{
		
		assertEquals(PersonVT.defaultVTName, this.vt.getName());
	}
	
	/**
	 * Test method for {@link xdev.vt.VirtualTable#getDatabaseAlias()}.
	 */
	@Test
	public void testGetDatabaseAlias()
	{
		assertEquals(PersonVT.defaultDBAlias, this.vt.getDatabaseAlias());
	}
	
	/**
	 * Test method for {@link xdev.vt.VirtualTable#getDataSource()}.
	 */
	@Test
	public void testGetDataSource()
	{
		// test run by dbjunit
	}
	
	/**
	 * Test method for {@link xdev.vt.VirtualTable#getPrimaryColumn()}.
	 */
	@Test
	public void testGetPrimaryColumn()
	{
		this.vt.setPrimaryColumn(this.vt.getColumnAt(0));
		
		assertEquals(this.vt.getColumnAt(0), this.vt.getPrimaryColumn());
	}
	
	/**
	 * Test method for
	 * {@link xdev.vt.VirtualTable#setPrimaryColumn(xdev.vt.VirtualTableColumn)}
	 * .
	 */
	@Test
	public void testSetPrimaryColumn()
	{
		this.vt.setPrimaryColumn(this.vt.getColumnAt(2));
		
		assertEquals(this.vt.getColumnAt(2), this.vt.getPrimaryColumn());
	}
	
	/**
	 * Test method for {@link xdev.vt.VirtualTable#getColumnCount()}.
	 */
	@Test
	public void testGetColumnCount()
	{
		assertEquals(this.vt.getColumnCount(), PersonVT.defaultColumnCount);
	}
	
	/**
	 * Test method for {@link xdev.vt.VirtualTable#getColumn(java.lang.String)}.
	 */
	@Test
	public void testGetColumn()
	{
		assertEquals(PersonVT.id, this.vt.getColumn(PersonVT.id.getName()));
		assertEquals(PersonVT.Name, this.vt.getColumn(PersonVT.Name.getName()));
		assertEquals(PersonVT.Gehalt, this.vt.getColumn(PersonVT.Gehalt.getName()));
		assertEquals(PersonVT.Gewicht, this.vt.getColumn(PersonVT.Gewicht.getName()));
		assertEquals(PersonVT.Geburtsdatum, this.vt.getColumn(PersonVT.Geburtsdatum.getName()));
		assertEquals(PersonVT.Geburtszeit, this.vt.getColumn(PersonVT.Geburtszeit.getName()));
		assertEquals(PersonVT.GeburtsTimeStamp, this.vt.getColumn(PersonVT.GeburtsTimeStamp.getName()));
		assertEquals(PersonVT.Krank, this.vt.getColumn(PersonVT.Krank.getName()));
		assertNull(this.vt.getColumn("Hans"));
	}
	
	/**
	 * Test method for
	 * {@link xdev.vt.VirtualTable#getColumnIndex(java.lang.String)}.
	 */
	@Test
	public void testGetColumnIndexString()
	{
		final int index1 = 0;
		final int index2 = 1;
		final int index3 = 2;
		
		assertEquals(index1, this.vt.getColumnIndex(this.vt.getColumnAt(index1)));
		assertEquals(index2, this.vt.getColumnIndex(this.vt.getColumnAt(index2)));
		assertEquals(index3, this.vt.getColumnIndex(this.vt.getColumnAt(index3)));
		assertEquals(-1, this.vt.getColumnIndex("HANS"));
	}
	
	/**
	 * Test method for {@link xdev.vt.VirtualTable#getColumnNames(int)}.
	 */
	@Test
	public void testGetColumnNames()
	{
		List<String> columnNames = this.vt.getColumnNames();
		assertEquals(PersonVT.defaultColumnCount, columnNames.size());
		columnNames = this.vt.getColumnNames(0);
		assertEquals(PersonVT.defaultColumnCount - 1, columnNames.size());
	}
	
	/**
	 * Test method for {@link xdev.vt.VirtualTable#getColumnName(int)}.
	 */
	@Test
	public void testGetColumnName()
	{
		final int index1 = 0;
		final int index2 = 1;
		final int index3 = 2;
		
		assertEquals(PersonVT.id.getName(), this.vt.getColumnName(index1));
		assertEquals(PersonVT.Name.getName(), this.vt.getColumnName(index2));
		assertEquals(PersonVT.Gehalt.getName(), this.vt.getColumnName(index3));
	}
	
	/**
	 * Test method for {@link xdev.vt.VirtualTable#getColumnCaption(int)}.
	 */
	@Test
	public void testGetColumnCaption()
	{
		final String captionOld = this.vt.getColumnCaption(0);
		final String captionString = "Hans";
		this.vt.getColumnAt(0).setCaption(captionString);
		assertEquals(captionString, this.vt.getColumnCaption(0));
		this.vt.getColumnAt(0).setCaption(captionOld);
	}
	
	/**
	 * Test method for {@link xdev.vt.VirtualTable#getColumnCaptions()}.
	 */
	@Test
	public void testGetColumnCaptions()
	{
		final String[] captions = this.vt.getColumnCaptions();
		assertNotNull(captions);
		assertEquals(PersonVT.defaultColumnCount, captions.length);
	}
	
	/**
	 * Test method for
	 * {@link xdev.vt.VirtualTable#getColumnIndex(xdev.vt.VirtualTableColumn)}.
	 */
	@Test
	public void testGetColumnIndexVirtualTableColumn()
	{
		final int index = 5;
		assertEquals(index, this.vt.getColumnIndex(this.vt.getColumnAt(index)));
		assertEquals(-1, this.vt.getColumnIndex(new VirtualTableColumn<String>("HANS")));
	}
	
	/**
	 * Test method for
	 * {@link xdev.vt.VirtualTable#getColumnIndices(xdev.vt.VirtualTableColumn[])}
	 * .
	 */
	@Test
	public void testGetColumnIndicesVirtualTableColumnArray()
	{
		final int[] indices = this.vt.getColumnIndices(PersonVT.id, PersonVT.Krank, PersonVT.Gehalt);
		assertNotNull(indices);
		assertEquals(3, indices.length);
		assertEquals(this.vt.getColumnIndex(PersonVT.id), indices[0]);
		assertEquals(this.vt.getColumnIndex(PersonVT.Krank), indices[1]);
		assertEquals(this.vt.getColumnIndex(PersonVT.Gehalt), indices[2]);
	}
	
	/**
	 * Test method for {@link xdev.vt.VirtualTable#getColumnAt(int)}.
	 */
	@Test
	public void testGetColumnAt()
	{
		final int indexFirst = 0;
		final int indexLast = 7;
		
		assertEquals(PersonVT.id, this.vt.getColumnAt(indexFirst));
		assertEquals(PersonVT.Krank, this.vt.getColumnAt(indexLast));
	}
	
	/**
	 * Test method for {@link xdev.vt.VirtualTable#getColumnsAt(int[])}.
	 */
	@Test
	public void testGetColumnsAt()
	{
		final int[] indices = {0, 7};
		
		final VirtualTableColumn<?>[] columns = this.vt.getColumnsAt(indices);
		
		assertEquals(indices.length, columns.length);
		assertEquals(PersonVT.id, columns[0]);
		assertEquals(PersonVT.Krank, columns[1]);
	}
	
	/**
	 * Test method for {@link xdev.vt.VirtualTable#getColumnsAt(int[])}. Tests
	 * the {@link IndexOutOfBoundsException}.
	 */
	@Test(expected = IndexOutOfBoundsException.class)
	public void testGetColumnsAtUpperIOOBException()
	{
		final int[] indices = {0, 8};
		this.vt.getColumnsAt(indices);
	}
	
	/**
	 * Test method for {@link xdev.vt.VirtualTable#getColumnsAt(int[])}. Tests
	 * the {@link IndexOutOfBoundsException}.
	 */
	@Test(expected = IndexOutOfBoundsException.class)
	public void testGetColumnsAtLowerIOOBException()
	{
		final int[] indices = {-1, 7};
		this.vt.getColumnsAt(indices);
	}
	
	/**
	 * Test method for {@link xdev.vt.VirtualTable#iterator()}.
	 */
	@Test
	public void testIterator()
	{
		int count = 0;
		final Iterator<VirtualTableColumn> it = this.vt.iterator();
		
		assertNotNull(it);
		
		while(it.hasNext())
		{
			it.next();
			count++;
		}
		
		assertEquals(PersonVT.defaultColumnCount, count);
	}
	
	/**
	 * Test method for {@link xdev.vt.VirtualTable#columns()}.
	 */
	@Test
	public void testColumns()
	{
		final Iterable<VirtualTableColumn> itColumns = this.vt.columns();
		
		int index = 0;
		for(final VirtualTableColumn<?> col : itColumns)
		{
			Assert.assertEquals(this.vt.getColumnAt(index++), col);
		}
	}
	
	/**
	 * Test method for {@link xdev.vt.VirtualTable#rows()}.
	 */
	@Test
	public void testRows()
	{
		final Iterable<VirtualTableRow> row = this.vt.rows();
		
		int index = 0;
		for(final VirtualTableRow r : row)
		{
			Assert.assertEquals(this.vt.getRow(index++), r);
		}
	}
	
	/**
	 * Test method for {@link xdev.vt.VirtualTable#getRowCount()}.
	 */
	@Test
	public void testGetRowCount()
	{
		assertEquals(this.data.length, this.vt.getRowCount());
	}
	
	/**
	 * Test method for {@link xdev.vt.VirtualTable#getValueAt(java.lang.String)}
	 * .
	 */
	@Test
	public void testGetValueAtString()
	{
		this.vt.setCursorPos(0);
		final String name = (String)this.vt.getValueAt(PersonVT.Name.getName());
		assertEquals(this.data[0][0], name);
	}
	
	/**
	 * Test method for {@link xdev.vt.VirtualTable#getValueAt(int)}.
	 */
	@Test
	public void testGetValueAtInt()
	{
		this.vt.setCursorPos(0);
		final String name = (String)this.vt.getValueAt(this.vt.getColumnIndex(PersonVT.Name));
		assertEquals(this.data[0][0], name);
	}
	
	/**
	 * Test method for {@link xdev.vt.VirtualTable#getValueAt(int, int)}.
	 */
	@Test
	public void testGetValueAtIntInt()
	{
		final Object name = this.vt.getValueAt(0, this.vt.getColumnIndex(PersonVT.Name));
		assertEquals(this.data[0][0], name);
	}
	
	/**
	 * Test method for
	 * {@link xdev.vt.VirtualTable#getValueAt(int, java.lang.String)}.
	 */
	@Test
	public void testGetValueAtIntString()
	{
		final Object name = this.vt.getValueAt(0, PersonVT.Name.getName());
		assertEquals(this.data[0][0], name);
	}
	
	/**
	 * Test method for
	 * {@link xdev.vt.VirtualTable#getValueAt(int, xdev.vt.VirtualTableColumn)}.
	 */
	@Test
	public void testGetValueAtIntVirtualTableColumnOfT()
	{
		final String name = this.vt.getValueAt(0, PersonVT.Name);
		assertEquals(this.data[0][0], name);
	}
	
	/**
	 * Test method for
	 * {@link xdev.vt.VirtualTable#getValueAt(int, xdev.vt.VirtualTableColumn)}.
	 * Test the {@link IllegalArgumentException} of this method.
	 * 
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testGetValueAtIntVirtualTableColumnOfTIAException()
	{
		this.vt.getValueAt(0, new VirtualTableColumn<String>("Hans"));
	}
	
	/**
	 * Test method for
	 * {@link xdev.vt.VirtualTable#getFormattedValueAt(int, int)}.
	 */
	@Test
	public void testGetFormattedValueAtIntInt()
	{
		final TextFormat formatter = new TextFormat(
			TextFormat.NUMBER,
			Locale.getDefault(),
			null,
			2,
			2,
			true,
			false,
			null);
		
		final String vtValue = this.vt.getFormattedValueAt(0, this.vt.getColumnIndex(PersonVT.Gewicht));
		
		assertEquals(formatter.format(this.data[0][this.vt.getColumnIndex(PersonVT.Gewicht) - 1]), vtValue);
	}
	
	/**
	 * Test method for
	 * {@link xdev.vt.VirtualTable#format(int, java.lang.String)}.
	 */
	@Test
	public void testFormat()
	{
		// TODO (FHAE): write test for method VirtualTable#format()
	}
	
	/**
	 * Test method for
	 * {@link xdev.vt.VirtualTable#getFormattedValueAt(int, java.lang.String)}.
	 */
	@Test
	public void testGetFormattedValueAtIntString()
	{
		final TextFormat formatter = new TextFormat(
			TextFormat.NUMBER,
			Locale.getDefault(),
			null,
			2,
			2,
			true,
			false,
			null);
		
		final String vtValue = this.vt.getFormattedValueAt(0, PersonVT.Gewicht.getName());
		
		assertEquals(formatter.format(this.data[0][this.vt.getColumnIndex(PersonVT.Gewicht) - 1]), vtValue);
	}
	
	/**
	 * Test method for
	 * {@link xdev.vt.VirtualTable#getFormattedValueAt(int, xdev.vt.VirtualTableColumn)}
	 * .
	 */
	@Test
	public void testGetFormattedValueAtIntVirtualTableColumn()
	{
		final TextFormat formatter = new TextFormat(
			TextFormat.NUMBER,
			Locale.getDefault(),
			null,
			2,
			2,
			true,
			false,
			null);
		
		final String vtValue = this.vt.getFormattedValueAt(0, PersonVT.Gewicht);
		
		assertEquals(formatter.format(this.data[0][this.vt.getColumnIndex(PersonVT.Gewicht) - 1]), vtValue);
	}
	
	/**
	 * Test method for
	 * {@link xdev.vt.VirtualTable#formatValue(java.lang.Object, int)}.
	 */
	@Test
	public void testFormatValue()
	{
		final TextFormat formatter = new TextFormat(
			TextFormat.NUMBER,
			Locale.getDefault(),
			null,
			2,
			2,
			true,
			false,
			null);
		final Object toFormat = this.data[0][this.vt.getColumnIndex(PersonVT.Gewicht) - 1];
		
		final String vtValue = this.vt.formatValue(toFormat, this.vt.getColumnIndex(PersonVT.Gewicht));
		
		assertEquals(formatter.format(toFormat), vtValue);
	}
	
	/**
	 * Test method for
	 * {@link xdev.vt.VirtualTable#setValueAt(java.lang.Object, int, java.lang.String)}
	 * .
	 */
	@Test
	public void testSetValueAtObjectIntString()
	{
		final String name = "Hans";
		final Object newName = this.vt.setValueAt(name, 0, PersonVT.Name.getName());
		assertEquals(name, newName);
		assertEquals(name, this.vt.getValueAt(0, PersonVT.Name.getName()));
	}
	
	/**
	 * Test method for
	 * {@link xdev.vt.VirtualTable#setValueAt(java.lang.Object, int, java.lang.String)}
	 * .
	 */
	@Test(expected = VirtualTableException.class)
	public void testSetValueAtObjectIntStringVTException()
	{
		PersonVT.Name.setNullable(false);
		this.vt.setValueAt(null, 0, PersonVT.Name.getName());
	}
	
	/**
	 * Test method for
	 * {@link xdev.vt.VirtualTable#setValueAt(java.lang.Object, java.lang.String)}
	 * .
	 */
	@Test
	public void testSetValueAtObjectString()
	{
		final String name = "Hans";
		final Object newName = this.vt.setValueAt(name, 0, PersonVT.Name.getName());
		assertEquals(name, newName);
		assertEquals(name, this.vt.getValueAt(0, PersonVT.Name.getName()));
	}
	
	/**
	 * Test method for
	 * {@link xdev.vt.VirtualTable#setValueAt(java.lang.Object, java.lang.String)}
	 * .
	 */
	@Test(expected = VirtualTableException.class)
	public void testSetValueAtObjectStringVTException()
	{
		PersonVT.Name.setNullable(false);
		this.vt.setCursorPos(0);
		this.vt.setValueAt(null, PersonVT.Name.getName());
	}
	
	/**
	 * Test method for
	 * {@link xdev.vt.VirtualTable#setValueAt(java.lang.Object, int)}.
	 */
	@Test
	public void testSetValueAtObjectInt()
	{
		final String name = "Hans";
		this.vt.setCursorPos(0);
		final Object newName = this.vt.setValueAt(name, this.vt.getColumnIndex(PersonVT.Name));
		assertEquals(name, newName);
		assertEquals(name, this.vt.getValueAt(0, PersonVT.Name.getName()));
	}
	
	/**
	 * Test method for
	 * {@link xdev.vt.VirtualTable#setValueAt(java.lang.Object, int)}.
	 */
	@Test(expected = VirtualTableException.class)
	public void testSetValueAtObjectIntVTException()
	{
		PersonVT.Name.setNullable(false);
		this.vt.setCursorPos(0);
		this.vt.setValueAt(null, this.vt.getColumnIndex(PersonVT.Name));
	}
	
	/**
	 * Test method for
	 * {@link xdev.vt.VirtualTable#setValueAt(java.lang.Object, int, int)}.
	 */
	@Test
	public void testSetValueAtObjectIntInt()
	{
		final String name = "Hans";
		final Object newName = this.vt.setValueAt(name, 0, this.vt.getColumnIndex(PersonVT.Name));
		assertEquals(name, newName);
		assertEquals(name, this.vt.getValueAt(0, PersonVT.Name.getName()));
	}
	
	/**
	 * Test method for
	 * {@link xdev.vt.VirtualTable#setValueAt(java.lang.Object, int, int)}.
	 */
	@Test(expected = VirtualTableException.class)
	public void testSetValueAtObjectIntIntVTException()
	{
		PersonVT.Name.setNullable(false);
		this.vt.setCursorPos(0);
		
		this.vt.setValueAt(null, 0, this.vt.getColumnIndex(PersonVT.Name));
	}
	
	/**
	 * Test method for
	 * {@link xdev.vt.VirtualTable#setValueAt(java.lang.Object, int, int, boolean)}
	 * .
	 */
	@Test
	public void testSetValueAtObjectIntIntBoolean()
	{
		final String name = "Hans";
		final Object newName = this.vt.setValueAt(name, 0, this.vt.getColumnIndex(PersonVT.Name), false);
		assertEquals(name, newName);
		assertEquals(name, this.vt.getValueAt(0, PersonVT.Name.getName()));
	}
	
	/**
	 * Test method for
	 * {@link xdev.vt.VirtualTable#setValueAt(java.lang.Object, int, int, boolean)}
	 * .
	 */
	@Test(expected = VirtualTableException.class)
	public void testSetValueAtObjectIntIntBooleanVTException()
	{
		PersonVT.Name.setNullable(false);
		this.vt.setValueAt(null, 0, this.vt.getColumnIndex(PersonVT.Name), false);
	}
	
	/**
	 * Test method for
	 * {@link xdev.vt.VirtualTable#setValueAt(java.lang.Object, int, int, boolean, boolean)}
	 * .
	 */
	
	@Test
	public void testSetValueAtObjectIntIntBooleanBoolean()
	{
		// test run by dbjunit
	}
	
	/**
	 * Test method for
	 * {@link xdev.vt.VirtualTable#setValueAt(java.lang.Object, int, int, boolean, boolean, boolean)}
	 * .
	 */
	@Test
	public void testSetValueAtObjectIntIntBooleanBooleanBoolean()
	{
		// test run by dbjunit
	}
	
	/**
	 * Test method for {@link xdev.vt.VirtualTable#clear()}.
	 */
	@Test
	public void testClear()
	{
		this.vt.clear();
		assertEquals(0, this.vt.getRowCount());
	}
	
	/**
	 * Test method for {@link xdev.vt.VirtualTable#clearData()}.
	 */
	@Test
	public void testClearData()
	{
		this.vt.clearData();
		assertEquals(0, this.vt.getRowCount());
	}
	
	/**
	 * Test method for
	 * {@link xdev.vt.VirtualTable#getPreferredColWidth(int, javax.swing.table.JTableHeader, int)}
	 * .
	 */
	@Test
	public void testGetPreferredColWidth()
	{
		final int col = 3;
		final JTableHeader header = new JTableHeader();
		final int tableCol = 3;
		final int preferredWidth = 30;
		
		// Default value is 100
		assertEquals(100, this.vt.getPreferredColWidth(col, header, tableCol));
		// Customer preferred width
		PersonVT.Gewicht.setPreferredWidth(preferredWidth);
		assertEquals(preferredWidth, this.vt.getPreferredColWidth(col, header, tableCol));
	}
	
	/**
	 * Test method for {@link xdev.vt.VirtualTable#createRow()}.
	 */
	@Test
	public void testCreateRow()
	{
		final VirtualTableRow row = this.vt.createRow();
		assertNotNull(row);
	}
	
	/**
	 * Test method for
	 * {@link xdev.vt.VirtualTable#addData(xdev.db.Result, xdev.vt.VirtualTableColumn[])}
	 * .
	 */
	@Test
	public void testAddDataResultVirtualTableColumnArray()
	{
		// test run by dbjunit
	}
	
	/**
	 * Test method for
	 * {@link xdev.vt.VirtualTable#addData(xdev.db.Result, xdev.vt.VirtualTableColumn[], xdev.vt.VirtualTableFillMethod)}
	 * .
	 */
	@Test
	public void testAddDataResultVirtualTableColumnArrayVirtualTableFillMethod()
	{
		// test run by dbjunit
	}
	
	/**
	 * Test method for {@link xdev.vt.VirtualTable#addData(xdev.db.Result)}.
	 */
	@Test
	public void testAddDataResult()
	{
		// test run by dbjunit
	}
	
	/**
	 * Test method for
	 * {@link xdev.vt.VirtualTable#addData(xdev.db.Result, xdev.vt.VirtualTableFillMethod)}
	 * .
	 */
	@Test
	public void testAddDataResultVirtualTableFillMethod()
	{
		// test run by dbjunit
	}
	
	/**
	 * Test method for
	 * {@link xdev.vt.VirtualTable#addData(xdev.db.Result, int[])}.
	 */
	@Test
	public void testAddDataResultIntArray()
	{
		// test run by dbjunit
	}
	
	/**
	 * Test method for
	 * {@link xdev.vt.VirtualTable#addData(xdev.db.Result, int[], xdev.vt.VirtualTableFillMethod)}
	 * .
	 */
	@Test
	public void testAddDataResultIntArrayVirtualTableFillMethod()
	{
		// test run by dbjunit
	}
	
	/**
	 * Test method for {@link xdev.vt.VirtualTable#getCaptions()}.
	 */
	@Test
	@SuppressWarnings("unchecked")
	public void testGetCaptions()
	{
		final XdevList<String> captions = this.vt.getCaptions();
		assertNotNull(captions);
		assertEquals(PersonVT.defaultColumnCount, captions.size());
		assertEquals(this.vt.getColumnAt(0).getCaption(), captions.get(0));
	}
	
	/**
	 * Test method for
	 * {@link xdev.vt.VirtualTable#fillFormular(xdev.ui.XdevFormular, int)}.
	 */
	@Test
	public void testFillFormular()
	{
		// test run by squish
	}
	
	/**
	 * Test method for {@link xdev.vt.VirtualTable#saveLocal()}.
	 */
	@Test
	public void testSaveLocal()
	{
		// test run by squish
	}
	
	/**
	 * Test method for
	 * {@link xdev.vt.VirtualTable#write(xdev.io.XdevObjectOutputStream)}.
	 * 
	 * @throws IOException
	 *             if any io error occurs
	 * 			
	 */
	@Test
	public void testWrite() throws IOException
	{
		final VirtualTable cloneVT = this.vt.clone();
		
		// Write the data to the bytearray
		final ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
		final XdevObjectOutputStream stream = new XdevObjectOutputStream(byteStream);
		this.vt.write(stream);
		
		assertEquals(0, cloneVT.getRowCount());
		
		cloneVT.read(new XdevObjectInputStream(new ByteArrayInputStream(byteStream.toByteArray())));
		
		AssertVirtualTable.assertEqualsVirtualTableData(this.vt, cloneVT);
	}
	
	/**
	 * Test method for {@link xdev.vt.VirtualTable#loadLocal()}.
	 */
	@Test
	public void testLoadLocal()
	{
		// test run by squish
	}
	
	/**
	 * Test method for {@link xdev.vt.VirtualTable#load(int)}.
	 */
	@Test
	public void testLoadInt()
	{
		// this method is deprecated
	}
	
	/**
	 * Test method for
	 * {@link xdev.vt.VirtualTable#read(xdev.io.XdevObjectInputStream)}.
	 * 
	 * @throws IOException
	 *             if any io error occurs
	 */
	@Test
	public void testRead() throws IOException
	{
		final VirtualTable cloneVT = this.vt.clone();
		
		// Write the data to the bytearray
		final ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
		final XdevObjectOutputStream stream = new XdevObjectOutputStream(byteStream);
		this.vt.write(stream);
		
		assertEquals(0, cloneVT.getRowCount());
		
		cloneVT.read(new XdevObjectInputStream(new ByteArrayInputStream(byteStream.toByteArray())));
		
		AssertVirtualTable.assertEqualsVirtualTableData(this.vt, cloneVT);
	}
	
	/**
	 * Test method for {@link xdev.vt.VirtualTable#importCSV(java.io.Reader)}.
	 */
	@Test
	public void testImportCSVReader()
	{
		// TODO (FHAE): write test method VirtualTable#importCSV(java.io.Reader)
	}
	
	/**
	 * Test method for
	 * {@link xdev.vt.VirtualTable#importCSV(java.io.Reader, char)}.
	 */
	@Test
	public void testImportCSVReaderChar()
	{
		// TODO (FHAE): write test method VirtualTable#importCSV(java.io.Reader,
		// char)
	}
	
	/**
	 * Test method for
	 * {@link xdev.vt.VirtualTable#importCSV(java.io.Reader, char, java.lang.String)}
	 * .
	 */
	@Test
	public void testImportCSVReaderCharString()
	{
		// TODO (FHAE): write test method VirtualTable#importCSV(java.io.Reader,
		// char, java.lang.String)
	}
	
	/**
	 * Test method for
	 * {@link xdev.vt.VirtualTable#importCSV(java.io.Reader, char, java.lang.String, boolean)}
	 * .
	 */
	@Test
	public void testImportCSVReaderCharStringBoolean()
	{
		// TODO (FHAE): write test method VirtualTable#importCSV(java.io.Reader,
		// char, java.lang.String, boolean)
	}
	
	/**
	 * Test method for
	 * {@link xdev.vt.VirtualTable#importCSV(java.io.Reader, char, java.lang.String, boolean, int)}
	 * .
	 * 
	 * @throws IOException
	 */
	@Test
	public void testImportCSVReaderCharStringBooleanInt() throws IOException
	{
		// TODO (FHAE) write test for CSV import
		
		// CSVHelper csvhelper = new CSVHelper();
		// Writer writer = csvhelper.getWriter();
		// Reader reader = csvhelper.getReader();
		// VirtualTable cloneVT = vt.clone();
		// String nullValue = "";
		// char delimiter = '#';
		//
		// // TEST1
		// // export csv with headercaption etc.
		// vt.exportCSV(writer,0,nullValue,delimiter,Arrays.asList(vt.getColumnCaptions()),true);
		// writer.close();
		// // Import csv
		// cloneVT.importCSV(reader,delimiter,nullValue,true,0);
		// reader.close();
		// // check all data in table
		// AssertVirtualTable.assertEqualsVirtualTableData(vt,cloneVT);
		//
		// // TEST2
		// csvhelper.refreshHelper();
		// vt.exportCSV(writer,0,nullValue,delimiter,Arrays.asList(vt.getColumnCaptions()),false);
		// writer.close();
		// // Import csv
		// cloneVT.importCSV(reader,delimiter,nullValue,false,0);
		// reader.close();
		// // check all data in table
		// AssertVirtualTable.assertEqualsVirtualTableData(vt,cloneVT);
	}
	
	/**
	 * Test method for {@link xdev.vt.VirtualTable#exportCSV(java.io.Writer)}.
	 */
	@Test
	public void testExportCSVWriter()
	{
		// TODO (FHAE): write test method exportCSV(java.io.Writer)
	}
	
	/**
	 * Test method for
	 * {@link xdev.vt.VirtualTable#exportCSV(java.io.Writer, int)}.
	 */
	@Test
	public void testExportCSVWriterInt()
	{
		// TODO (FHAE): write test method exportCSV(java.io.Writer, int)
	}
	
	/**
	 * Test method for
	 * {@link xdev.vt.VirtualTable#exportCSV(java.io.Writer, int, java.lang.String)}
	 * .
	 */
	@Test
	public void testExportCSVWriterIntString()
	{
		// TODO (FHAE): write test method exportCSV(java.io.Writer, int,
		// java.lang.String)
	}
	
	/**
	 * Test method for
	 * {@link xdev.vt.VirtualTable#exportCSV(java.io.Writer, int, java.lang.String, char)}
	 * .
	 */
	@Test
	public void testExportCSVWriterIntStringChar()
	{
		// TODO (FHAE): write test method exportCSV(java.io.Writer, int,
		// java.lang.String, char)
	}
	
	/**
	 * Test method for
	 * {@link xdev.vt.VirtualTable#exportCSV(java.io.Writer, int, java.lang.String, char, java.util.List)}
	 * .
	 */
	@Test
	public void testExportCSVWriterIntStringCharListOfString()
	{
		// TODO (FHAE): write test method exportCSV(java.io.Writer, int,
		// java.lang.String, char, java.util.List)
	}
	
	/**
	 * Test method for
	 * {@link xdev.vt.VirtualTable#exportCSV(java.io.Writer, int, java.lang.String, char, java.util.List, boolean)}
	 * .
	 * 
	 * @throws IOException
	 *             if any io error occurs
	 */
	@Test
	public void testExportCSVWriterIntStringCharListOfStringBoolean() throws IOException
	{
		// TODO TODO (FHAE) write test for CSV export
	}
	
	/**
	 * Test method for {@link xdev.vt.VirtualTable#getCursorPos()}.
	 */
	@Test
	public void testGetCursorPos()
	{
		final int cursorpos = 2;
		assertEquals(-1, this.vt.getCursorPos());
		this.vt.setCursorPos(cursorpos);
		assertEquals(cursorpos, this.vt.getCursorPos());
	}
	
	/**
	 * Test method for {@link xdev.vt.VirtualTable#setCursorPos(int)}.
	 */
	@Test
	public void testSetCursorPos()
	{
		final int cursorpos = 2;
		this.vt.setCursorPos(cursorpos);
		assertEquals(cursorpos, this.vt.getCursorPos());
	}
	
	/**
	 * Test method for {@link xdev.vt.VirtualTable#resetCursor()}.
	 */
	@Test
	public void testResetCursor()
	{
		final int cursorpos = 2;
		assertEquals(-1, this.vt.getCursorPos());
		this.vt.setCursorPos(cursorpos);
		assertEquals(cursorpos, this.vt.getCursorPos());
		this.vt.resetCursor();
		assertEquals(-1, this.vt.getCursorPos());
	}
	
	/**
	 * Test method for {@link xdev.vt.VirtualTable#next()}.
	 */
	@Test
	public void testNext()
	{
		int index = 0;
		while(this.vt.next())
		{
			index++;
		}
		assertEquals(this.vt.getRowCount(), index);
	}
	
	/**
	 * Test method for
	 * {@link xdev.vt.VirtualTable#getRowIndex(java.lang.String, java.lang.Object)}
	 * .
	 */
	@Test
	public void testGetRowIndexStringObject()
	{
		final int rowIndex = 2;
		int actualIndex = this.vt.getRowIndex(PersonVT.Name.getName(), this.data[rowIndex][0]);
		assertEquals(rowIndex, actualIndex);
		actualIndex = this.vt.getRowIndex(PersonVT.Name.getName(), this.data[rowIndex][2]);
		assertEquals(-1, actualIndex);
	}
	
	/**
	 * Test method for
	 * {@link xdev.vt.VirtualTable#getRowIndex(java.lang.String, java.lang.Object, int)}
	 * .
	 */
	@Test
	public void testGetRowIndexStringObjectInt()
	{
		final int rowIndex = 2;
		int startIndex = 0;
		int actualIndex = this.vt.getRowIndex(PersonVT.Name.getName(), this.data[rowIndex][0], startIndex);
		assertEquals(rowIndex, actualIndex);
		// Set start index
		startIndex = 3;
		actualIndex = this.vt.getRowIndex(PersonVT.Name.getName(), this.data[rowIndex][0], startIndex);
		assertEquals(-1, actualIndex);
	}
	
	/**
	 * Test method for {@link xdev.vt.VirtualTable#getPrimaryKeyColumns()}.
	 */
	@Test
	public void testGetPrimaryKeyColumns()
	{
		final VirtualTable vt = PersonVT2.VT;
		final Index index = new Index(
			"PKI1",
			IndexType.PRIMARY_KEY,
			PersonVT.id.getName(),
			PersonVT.Name.getName());
		vt.addIndex(index);
		
		final VirtualTableColumn<?>[] primaryKeyCols = vt.getPrimaryKeyColumns();
		assertEquals(primaryKeyCols[0], PersonVT.id);
		assertEquals(primaryKeyCols[1], PersonVT.Name);
	}
	
	/**
	 * Test method for
	 * {@link xdev.vt.VirtualTable#fillTree(xdev.ui.XdevTree, java.lang.String, java.lang.Object, java.lang.String, java.lang.String, java.lang.String, java.lang.String)}
	 * .
	 */
	@Test
	public void testFillTree()
	{
		// TODO (FHAE): write test method VirtualTable#fillTree()
	}
	
	/**
	 * Test method for
	 * {@link xdev.vt.VirtualTable#setModelFor(xdev.ui.XdevTree, java.lang.String, java.lang.Object, java.lang.String, java.lang.String, java.lang.String, java.lang.String)}
	 * .
	 */
	@Test
	public void testSetModelForXdevTreeStringObjectStringStringStringString()
	{
		// TODO (FHAE): write test method VirtualTable#setModelFor()
	}
	
	/**
	 * Test method for
	 * {@link xdev.vt.VirtualTable#createTree(java.lang.String, java.lang.Object, java.lang.String, java.lang.String, java.lang.String, java.lang.String)}
	 * .
	 */
	@Test
	public void testCreateTree()
	{
		// TODO (FHAE): write test method VirtualTable#createTree
	}
	
	/**
	 * Test method for
	 * {@link xdev.vt.VirtualTable#fillTable(javax.swing.JTable)}.
	 */
	@Test
	public void testFillTable()
	{
		
		final XdevTable table = new XdevTable();
		this.vt.fillTable(table);
		
		final int tableRC = table.getRowCount();
		final int vtRC = this.vt.getRowCount();
		final int tableCC = table.getColumnCount();
		final int vtCC = this.vt.getColumnCount();
		
		assertEquals(vtRC, tableRC);
		// -1 of the vtCC because the ID column is in the table not available
		assertEquals(vtCC - 1, tableCC);
		
		for(int currentRC = 0; currentRC < vtRC; currentRC++)
		{
			// -1 of the vtCC because the ID column is in the table not
			// available
			for(int currentCC = 0; currentCC < vtCC - 1; currentCC++)
			{
				final Object vtValue = this.vt.getValueAt(currentRC, currentCC + 1);
				final Object tableValue = table.getValueAt(currentRC, currentCC);
				assertEquals(vtValue, tableValue);
			}
		}
	}
	
	/**
	 * Test method for
	 * {@link xdev.vt.VirtualTable#setModelFor(javax.swing.JTable)}.
	 */
	@Test
	public void testSetModelForJTable()
	{
		
		final XdevTable table = new XdevTable();
		this.vt.setModelFor(table);
		
		AssertVirtualTable.assertEqualsVirtualTableAndJTable(this.vt, table);
		
	}
	
	/**
	 * Test method for {@link xdev.vt.VirtualTable#createTableModel()}.
	 */
	@Test
	public void testCreateTableModel()
	{
		final TableModel tm = this.vt.createTableModel();
		final XdevTable table = new XdevTable();
		table.setModel(tm);
		
		AssertVirtualTable.assertEqualsVirtualTableAndJTable(this.vt, table);
	}
	
	/**
	 * Test method for
	 * {@link xdev.vt.VirtualTable#createTableModel(java.lang.String[])}.
	 */
	@Test
	public void testCreateTableModelStringArray()
	{
		final TableModel tm = this.vt.createTableModel(PersonVT.Name.getName());
		final XdevTable table = new XdevTable();
		table.setModel(tm);
		
		assertEquals(1, table.getColumnCount());
		assertEquals(this.vt.getRowCount(), table.getRowCount());
		
		for(int currentRC = 0; currentRC < this.vt.getRowCount(); currentRC++)
		{
			final Object vtValue = this.vt.getValueAt(currentRC, 1);
			final Object tableValue = table.getValueAt(currentRC, 0);
			assertEquals(vtValue, tableValue);
		}
	}
	
	/**
	 * Test method for
	 * {@link xdev.vt.VirtualTable#createTableModel(java.util.List)}.
	 */
	@Test
	public void testCreateTableModelListOfString()
	{
		final List<String> list = new ArrayList<String>()
		{
			{
				this.add(PersonVT.Name.getName());
			}
		};
		
		final TableModel tm = this.vt.createTableModel(list);
		final XdevTable table = new XdevTable();
		table.setModel(tm);
		
		assertEquals(1, table.getColumnCount());
		assertEquals(this.vt.getRowCount(), table.getRowCount());
		
		for(int currentRC = 0; currentRC < this.vt.getRowCount(); currentRC++)
		{
			final Object vtValue = this.vt.getValueAt(currentRC, 1);
			final Object tableValue = table.getValueAt(currentRC, 0);
			assertEquals(vtValue, tableValue);
		}
	}
	
	/**
	 * Test method for {@link xdev.vt.VirtualTable#createTabelModel(int[])}.
	 */
	@Test
	public void testCreateTabelModel()
	{
		final TableModel tm = this.vt.createTableModel(1);
		final XdevTable table = new XdevTable();
		table.setModel(tm);
		
		assertEquals(1, table.getColumnCount());
		assertEquals(this.vt.getRowCount(), table.getRowCount());
		
		for(int currentRC = 0; currentRC < this.vt.getRowCount(); currentRC++)
		{
			final Object vtValue = this.vt.getValueAt(currentRC, 1);
			final Object tableValue = table.getValueAt(currentRC, 0);
			assertEquals(vtValue, tableValue);
		}
	}
	
	/**
	 * Test method for
	 * {@link xdev.vt.VirtualTable#getColumns(xdev.vt.VirtualTable.ColumnSelector)}
	 * .
	 */
	@Test
	public void testGetColumns()
	{
		// Only Integer columns are selected, 2 columns ID and gehalt
		final VirtualTableColumn<?>[] cols = this.vt.getColumns(this.selector);
		
		assertEquals(2, cols.length);
		assertEquals(PersonVT.id, cols[0]);
		assertEquals(PersonVT.Gehalt, cols[1]);
	}
	
	/**
	 * Test method for
	 * {@link xdev.vt.VirtualTable#getColumnIndices(xdev.vt.VirtualTable.ColumnSelector)}
	 * .
	 */
	@Test
	public void testGetColumnIndicesColumnSelector()
	{
		// Only Integer columns are selected, 2 columns ID and gehalt
		final int[] colIndices = this.vt.getColumnIndices(this.selector);
		
		assertEquals(2, colIndices.length);
		assertEquals(this.vt.getColumnIndex(PersonVT.id), colIndices[0]);
		assertEquals(this.vt.getColumnIndex(PersonVT.Gehalt), colIndices[1]);
	}
	
	/**
	 * Test method for {@link xdev.vt.VirtualTable#getVisibleColumnIndices()}.
	 */
	@Test
	public void testGetVisibleColumnIndices()
	{
		final int[] indices = this.vt.getVisibleColumnIndices();
		assertEquals(this.vt.getColumnCount() - 1, indices.length);
	}
	
	/**
	 * Test method for {@link xdev.vt.VirtualTable#getPersistentColumnIndices()}
	 * .
	 */
	@Test
	public void testGetPersistentColumnIndices()
	{
		final int[] cols = this.vt.getPersistentColumnIndices();
		assertEquals(8, cols.length);
	}
	
	/**
	 * Test method for
	 * {@link xdev.vt.VirtualTable#getNonPersistentColumnIndices()}.
	 */
	@Test
	public void testGetNonPersistentColumnIndices()
	{
		int[] cols = this.vt.getNonPersistentColumnIndices();
		assertEquals(0, cols.length);
		this.vt.getColumnAt(0).setPersistent(false);
		cols = this.vt.getNonPersistentColumnIndices();
		assertEquals(1, cols.length);
		this.vt.getColumnAt(0).setPersistent(true);
	}
	
	/**
	 * Test method for
	 * {@link xdev.vt.VirtualTable#getNonPersistentVisibleColumnIndices()}.
	 */
	@Test
	public void testGetNonPersistentVisibleColumnIndices()
	{
		this.vt.getColumnAt(1).setPersistent(false);
		final int[] cols = this.vt.getNonPersistentVisibleColumnIndices();
		// ID is invisible
		assertEquals(1, cols.length);
		
		this.vt.getColumnAt(1).setPersistent(true);
	}
	
	/**
	 * Test method for {@link xdev.vt.VirtualTable#hasNonPersistentColumns()}.
	 */
	@Test
	public void testHasNonPersistentColumns()
	{
		boolean hasNonPC = this.vt.hasNonPersistentColumns();
		assertEquals(false, hasNonPC);
		// Change persistens
		this.vt.getColumnAt(0).setPersistent(false);
		
		hasNonPC = this.vt.hasNonPersistentColumns();
		assertEquals(true, hasNonPC);
		
		this.vt.getColumnAt(0).setPersistent(true);
	}
	
	/**
	 * Test method for {@link xdev.vt.VirtualTable#toString()}.
	 */
	@Test
	public void testToString()
	{
		final String defaultName = "VirtualTable";
		final VirtualTable vtNew = new VirtualTable(null, null, PersonVT.id);
		assertEquals(defaultName, vtNew.toString());
		assertEquals(PersonVT.defaultVTName, this.vt.toString());
		
	}
	
	/**
	 * Test method for {@link xdev.vt.VirtualTable#toFormattedStrings()}.
	 */
	@Test
	public void testToFormattedStrings()
	{
		final String[][] formattedString = this.vt.toFormattedStrings();
		assertNotNull(formattedString);
		assertEquals(this.vt.getRowCount() + 1, formattedString.length);
		
		final int vtRC = this.vt.getRowCount() + 1;
		final int vtCC = this.vt.getColumnCount();
		
		for(int currentRC = 0; currentRC < vtRC; currentRC++)
		{
			// -1 of the vtCC because the ID column is in the table not
			// available
			for(int currentCC = 0; currentCC < vtCC; currentCC++)
			{
				if(currentRC <= 0)
				{
					// caption
					assertEquals(
						this.vt.getColumnAt(currentCC).getName(),
						formattedString[currentRC][currentCC]);
				}
				else
				{
					final Object vtValue = this.vt.getFormattedValueAt(currentRC - 1, currentCC);
					assertEquals(vtValue, formattedString[currentRC][currentCC]);
				}
			}
		}
	}
	
	/**
	 * Test method for {@link xdev.vt.VirtualTable#getValues()}.
	 */
	@Test
	public void testGetValues()
	{
		final List<?> list = this.vt.getValues();
		
		final int vtRC = this.vt.getRowCount();
		final int vtCC = this.vt.getColumnCount();
		
		for(int currentRC = 0; currentRC < vtRC; currentRC++)
		{
			for(int currentCC = 0; currentCC < vtCC; currentCC++)
			{
				assertEquals(
					this.vt.getValueAt(currentRC, currentCC),
					((XdevList<?>)list.get(currentRC)).get(currentCC));
			}
		}
	}
	
	/**
	 * Test method for {@link xdev.vt.VirtualTable#toLists()}.
	 */
	@Test
	public void testToLists()
	{
		final List<?> list = this.vt.toLists();
		
		final int vtRC = this.vt.getRowCount();
		final int vtCC = this.vt.getColumnCount();
		
		for(int currentRC = 0; currentRC < vtRC; currentRC++)
		{
			for(int currentCC = 0; currentCC < vtCC; currentCC++)
			{
				assertEquals(
					this.vt.getValueAt(currentRC, currentCC),
					((XdevList<?>)list.get(currentRC)).get(currentCC));
			}
		}
	}
	
	/**
	 * Test method for {@link xdev.vt.VirtualTable#toLists(int)}.
	 */
	@Test
	public void testToListsInt()
	{
		final List<?> list = this.vt.toLists(0);
		
		final int vtRC = this.vt.getRowCount();
		// the first column is excluded
		final int vtCC = this.vt.getColumnCount() - 1;
		
		for(int currentRC = 0; currentRC < vtRC; currentRC++)
		{
			for(int currentCC = 0; currentCC < vtCC; currentCC++)
			{
				// the first column is excluded, +1 by vt
				assertEquals(
					this.vt.getValueAt(currentRC, currentCC + 1),
					((XdevList<?>)list.get(currentRC)).get(currentCC));
			}
		}
	}
	
	/**
	 * Test method for {@link xdev.vt.VirtualTable#getRow(int)}.
	 */
	@Test
	public void testGetRowInt()
	{
		for(int rowCount = 0; rowCount < this.vt.getRowCount(); rowCount++)
		{
			final VirtualTableRow row = this.vt.getRow(rowCount);
			assertNotNull(row);
			
			for(int columnCount = 0; columnCount < this.vt.getColumnCount(); columnCount++)
			{
				assertEquals(this.vt.getValueAt(rowCount, columnCount), row.get(columnCount));
			}
		}
	}
	
	/**
	 * Test method for {@link xdev.vt.VirtualTable#getLastRow()}.
	 */
	@Test
	public void testGetLastRow()
	{
		final VirtualTableRow lastRow = this.vt.getLastRow();
		assertEquals(this.vt.getRow(this.vt.getRowCount() - 1), lastRow);
	}
	
	/**
	 * Test method for {@link xdev.vt.VirtualTable#getRow(xdev.vt.KeyValues)}.
	 */
	@Test
	public void testGetRowKeyValues()
	{
		final KeyValues pkValues = new KeyValues(this.vt, PersonVT.Name.getName(), "Mustermann");
		
		Assert.assertEquals(this.vt.getRow(0), this.vt.getRow(pkValues));
	}
	
	/**
	 * Test method for {@link xdev.vt.VirtualTable#getRowAsList()}.
	 */
	@Test
	public void testGetRowAsList()
	{
		List<?> rowData;
		final int RC = this.vt.getRowCount();
		for(int row = 0; row < RC; row++)
		{
			this.vt.setCursorPos(row);
			rowData = this.vt.getRowAsList();
			for(int col = 0; col < rowData.size(); col++)
			{
				assertEquals(this.vt.getValueAt(row, col), rowData.get(col));
			}
		}
	}
	
	/**
	 * Test method for {@link xdev.vt.VirtualTable#getRowAsList(int)}.
	 */
	@Test
	public void testGetRowAsListInt()
	{
		List<?> rowData;
		final int RC = this.vt.getRowCount();
		for(int row = 0; row < RC; row++)
		{
			rowData = this.vt.getRowAsList(row);
			for(int col = 0; col < rowData.size(); col++)
			{
				assertEquals(this.vt.getValueAt(row, col), rowData.get(col));
			}
		}
	}
	
	/**
	 * Test method for {@link xdev.vt.VirtualTable#getRowData(int, int)}.
	 */
	@Test
	public void testGetRowData()
	{
		final int expectcol = 2;
		final int row = 2;
		
		final XdevList<?> actual = this.vt.getRowData(row, expectcol);
		
		int index = 0;
		for(int col = 0; col < this.vt.getColumnCount(); col++)
		{
			if(col != expectcol)
			{
				assertEquals(this.vt.getValueAt(row, col), actual.get(index));
				index++;
			}
		}
	}
	
	/**
	 * Test method for {@link xdev.vt.VirtualTable#getColumnData(int)}.
	 */
	@Test
	public void testGetColumnData()
	{
		for(int col = 0; col < this.vt.getColumnCount(); col++)
		{
			final XdevList<?> actual = this.vt.getColumnData(col);
			
			for(int row = 0; row < this.vt.getRowCount(); row++)
			{
				assertEquals(this.vt.getValueAt(row, col), actual.get(row));
			}
		}
	}
	
	/**
	 * Test method for {@link xdev.vt.VirtualTable#getRowAsMap()}.
	 */
	@Test
	public void testGetRowAsMap()
	{
		this.vt.setCursorPos(1);
		final Map<String, Object> actual = this.vt.getRowAsMap();
		
		for(int col = 0; col < this.vt.getColumnCount(); col++)
		{
			assertEquals(this.vt.getValueAt(col), actual.get(this.vt.getColumnName(col)));
		}
	}
	
	/**
	 * Test method for {@link xdev.vt.VirtualTable#getRowAsMap(int)}.
	 */
	@Test
	public void testGetRowAsMapInt()
	{
		final int rowIndex = 2;
		final Map<String, Object> actual = this.vt.getRowAsMap(rowIndex);
		
		for(int col = 0; col < this.vt.getColumnCount(); col++)
		{
			assertEquals(this.vt.getValueAt(rowIndex, col), actual.get(this.vt.getColumnName(col)));
		}
	}
	
	/**
	 * Test method for
	 * {@link xdev.vt.VirtualTable#fillInMap(int, java.util.Map)}.
	 */
	@Test
	public void testFillInMap()
	{
		final int rowIndex = 2;
		final Map<String, Object> rowData = new HashMap<>(this.vt.getColumnCount());
		this.vt.fillInMap(rowIndex, rowData);
		
		for(int col = 0; col < this.vt.getColumnCount(); col++)
		{
			assertEquals(this.vt.getValueAt(rowIndex, col), rowData.get(this.vt.getColumnName(col)));
		}
	}
	
	/**
	 * Test method for
	 * {@link xdev.vt.VirtualTable#fillInItemList(xdev.ui.ItemList, java.lang.String, java.lang.String)}
	 * .
	 */
	@Test
	public void testFillInItemList()
	{
		final ItemList list = new ItemList();
		this.vt.fillInItemList(list, PersonVT.id.getName(), PersonVT.Name.getName());
		
		for(int rowIndex = 0; rowIndex < list.size(); rowIndex++)
		{
			assertEquals(this.vt.getFormattedValueAt(rowIndex, 0), list.get(rowIndex).getItem());
			assertEquals(this.vt.getFormattedValueAt(rowIndex, 1), list.get(rowIndex).getData());
		}
	}
	
	/**
	 * Test method for {@link xdev.vt.VirtualTable#getColumnAsString(int)}.
	 */
	@Test
	public void testGetColumnAsString()
	{
		final int colIndex = this.vt.getColumnIndex(PersonVT.Name);
		final List<String> list = this.vt.getColumnAsString(colIndex);
		final int rowCount = this.vt.getRowCount();
		for(int i = 0; i < rowCount; i++)
		{
			assertEquals(this.vt.getFormattedValueAt(i, colIndex), list.get(i));
		}
	}
	
	/**
	 * Test method for
	 * {@link xdev.vt.VirtualTable#sortByCol(java.lang.String, boolean)}.
	 */
	@Test
	public void testSortByColStringBoolean()
	{
		// ascending
		this.vt.sortByCol(PersonVT.Name.getName(), true);
		final int colIndex = this.vt.getColumnIndex(PersonVT.Name);
		
		// Klinger
		assertEquals(this.data[2][colIndex - 1], this.vt.getValueAt(0, colIndex));
		// Mustermann
		assertEquals(this.data[0][colIndex - 1], this.vt.getValueAt(1, colIndex));
		// Testmann
		assertEquals(this.data[1][colIndex - 1], this.vt.getValueAt(2, colIndex));
		
		// descending
		this.vt.sortByCol(PersonVT.Name.getName(), false);
		// Klinger
		assertEquals(this.data[2][colIndex - 1], this.vt.getValueAt(2, colIndex));
		// Mustermann
		assertEquals(this.data[0][colIndex - 1], this.vt.getValueAt(1, colIndex));
		// Testmann
		assertEquals(this.data[1][colIndex - 1], this.vt.getValueAt(0, colIndex));
	}
	
	/**
	 * Test method for {@link xdev.vt.VirtualTable#sortByCol(int, boolean)}.
	 */
	@Test
	public void testSortByColIntBoolean()
	{
		final int colIndex = this.vt.getColumnIndex(PersonVT.Name);
		// ascending
		this.vt.sortByCol(colIndex, true);
		// Klinger
		assertEquals(this.data[2][colIndex - 1], this.vt.getValueAt(0, colIndex));
		// Mustermann
		assertEquals(this.data[0][colIndex - 1], this.vt.getValueAt(1, colIndex));
		// Testmann
		assertEquals(this.data[1][colIndex - 1], this.vt.getValueAt(2, colIndex));
		
		// descending
		this.vt.sortByCol(colIndex, false);
		// Klinger
		assertEquals(this.data[2][colIndex - 1], this.vt.getValueAt(2, colIndex));
		// Mustermann
		assertEquals(this.data[0][colIndex - 1], this.vt.getValueAt(1, colIndex));
		// Testmann
		assertEquals(this.data[1][colIndex - 1], this.vt.getValueAt(0, colIndex));
	}
	
	class MyComparator implements Comparator<VirtualTableRow>
	{
		
		@Override
		public int compare(final VirtualTableRow r1, final VirtualTableRow r2)
		{
			final int gehalt1 = r1.get(PersonVT.Gehalt);
			final int gehalt2 = r2.get(PersonVT.Gehalt);
			
			int compare = 0;
			
			if(gehalt1 == gehalt2)
			{
				compare = 0;
			}
			else if(gehalt1 < gehalt2)
			{
				compare = -1;
			}
			else if(gehalt1 > gehalt2)
			{
				compare = 1;
			}
			
			return compare;
		}
		
	}
	
	/**
	 * Test method for {@link xdev.vt.VirtualTable#sort(java.util.Comparator)}.
	 */
	@Test
	public void testSort()
	{
		final int colIndex = this.vt.getColumnIndex(PersonVT.Gehalt);
		this.vt.sort(new MyComparator());
		
		// 2500
		assertEquals(this.data[0][colIndex - 1], this.vt.getValueAt(1, colIndex));
		// 1200
		assertEquals(this.data[1][colIndex - 1], this.vt.getValueAt(0, colIndex));
		// 4000
		assertEquals(this.data[2][colIndex - 1], this.vt.getValueAt(2, colIndex));
		
	}
	
	/**
	 * Test method for {@link xdev.vt.VirtualTable#addRow()}.
	 */
	@Test
	public void testAddRow()
	{
		final int rcBefore = this.vt.getRowCount();
		this.vt.addRow();
		final int rcAfter = this.vt.getRowCount();
		assertTrue(rcBefore < rcAfter);
	}
	
	/**
	 * Test method for
	 * {@link xdev.vt.VirtualTable#addRow(boolean, java.lang.Object[])}.
	 * 
	 * @throws DBException
	 *             .
	 * @throws VirtualTableException
	 *             .
	 */
	@Test
	public void testAddRowBooleanObjectArray() throws VirtualTableException, DBException
	{
		final int rcBefore = this.vt.getRowCount();
		
		this.vt.addRow(
			false,
			"Klinger",
			4000,
			80.33,
			new java.sql.Date(1104534100000L),
			new java.sql.Time(
				76942000),
			new java.sql.Timestamp(1104534100000L),
			false);
		
		final int rcAfter = this.vt.getRowCount();
		assertTrue(rcBefore < rcAfter);
		
	}
	
	/**
	 * Test method for
	 * {@link xdev.vt.VirtualTable#addRow(java.util.List, boolean)}.
	 * 
	 * @throws DBException
	 *             .
	 * 
	 * @throws VirtualTableException
	 *             .
	 */
	@Test
	public void testAddRowListBoolean() throws VirtualTableException, DBException
	{
		final int rcBefore = this.vt.getRowCount();
		
		final Object row = this.data[0];
		final List<?> list = Arrays.asList(row);
		this.vt.addRow(list, false);
		
		final int rcAfter = this.vt.getRowCount();
		assertTrue(rcBefore < rcAfter);
	}
	
	/**
	 * Test method for
	 * {@link xdev.vt.VirtualTable#addRow(xdev.ui.XdevFormular, boolean)}.
	 * 
	 * @throws DBException
	 * @throws VirtualTableException
	 */
	@Test
	public void testAddRowXdevFormularBoolean() throws VirtualTableException, DBException
	{
		final XdevFormular formular = Mockito.mock(XdevFormular.class);
		
		Mockito.when(formular.getData(Mockito.anyBoolean())).thenAnswer(
			invocation ->
			{
				final Map<String, Object> objMap = new HashMap<>();
				objMap.put(PersonVT.Name.getName(), "Hans");
				objMap.put(PersonVT.Gehalt.getName(), 6800);
				objMap.put(PersonVT.Gewicht.getName(), 100.88);
				objMap.put(PersonVT.Geburtsdatum.getName(), new java.sql.Date(485042400000L));
				objMap.put(PersonVT.Geburtszeit.getName(), new java.sql.Time(44365000L));
				objMap.put(
					PersonVT.GeburtsTimeStamp.getName(),
					new java.sql.Timestamp(
						611409300000L));
				objMap.put(PersonVT.Krank.getName(), true);
				
				return objMap;
			});
		
		this.vt.addRow(formular, false);
	}
	
	/**
	 * Test method for
	 * {@link xdev.vt.VirtualTable#addRow(xdev.ui.XdevFormular, boolean)}.
	 * 
	 * @throws DBException
	 * @throws VirtualTableException
	 */
	@Test(expected = VirtualTableException.class)
	public void testAddRowXdevFormularBooleanVTException() throws VirtualTableException,
		DBException
	{
		final XdevFormular formular = Mockito.mock(XdevFormular.class);
		
		Mockito.when(formular.getData(Mockito.anyBoolean())).thenAnswer(
			invocation -> new TestDataSources().getMap());
		
		this.vt.addRow(formular, false);
	}
	
	/**
	 * Test method for
	 * {@link xdev.vt.VirtualTable#addRow(java.util.Map, boolean)}.
	 * 
	 * @throws DBException
	 * @throws VirtualTableException
	 */
	@Test
	public void testAddRowMapOfStringObjectBoolean() throws VirtualTableException, DBException
	{
		final int rcBefore = this.vt.getRowCount();
		
		final Map<String, Object> map = this.vt.getRowAsMap(0);
		this.vt.addRow(map, false);
		
		final int rcAfter = this.vt.getRowCount();
		assertTrue(rcBefore < rcAfter);
	}
	
	/**
	 * Test method for
	 * {@link xdev.vt.VirtualTable#addRow(java.util.Map, boolean, boolean)}.
	 * 
	 * @throws DBException
	 * @throws VirtualTableException
	 */
	@Test
	public void testAddRowMapOfStringObjectBooleanBoolean() throws VirtualTableException,
		DBException
	{
		int rcBefore = this.vt.getRowCount();
		
		Map<String, Object> map = this.vt.getRowAsMap(0);
		this.vt.addRow(map, false, false);
		
		int rcAfter = this.vt.getRowCount();
		assertTrue(rcBefore < rcAfter);
		
		// ignore flag is true
		rcBefore = this.vt.getRowCount();
		
		map = this.vt.getRowAsMap(0);
		map.put("Hans", "hans");
		this.vt.addRow(map, false, true);
		
		rcAfter = this.vt.getRowCount();
		assertTrue(rcBefore < rcAfter);
	}
	
	/**
	 * Test method for
	 * {@link xdev.vt.VirtualTable#addRow(java.util.Map, boolean, boolean)}.
	 * 
	 * @throws DBException
	 * @throws VirtualTableException
	 */
	@Test(expected = VirtualTableException.class)
	public void testAddRowMapOfStringObjectBooleanBooleanVTException()
		throws VirtualTableException, DBException
	{
		final int rcBefore = this.vt.getRowCount();
		
		final Map<String, Object> map = this.vt.getRowAsMap(0);
		map.put("Hans", "hans");
		this.vt.addRow(map, false, false);
		
		final int rcAfter = this.vt.getRowCount();
		assertTrue(rcBefore < rcAfter);
		
	}
	
	/**
	 * Test method for {@link xdev.vt.VirtualTable#removeRow(int, boolean)}.
	 * 
	 * @throws DBException
	 * @throws VirtualTableException
	 */
	@Test
	public void testRemoveRow() throws VirtualTableException, DBException
	{
		final int rcBefore = this.vt.getRowCount();
		final int row = 0;
		final Object valueBefore = this.vt.getValueAt(0, 1);
		
		this.vt.removeRow(row, false);
		final Object valueAfter = this.vt.getValueAt(0, 1);
		
		final int rcAfter = this.vt.getRowCount();
		assertTrue(rcBefore > rcAfter);
		assertFalse(valueBefore.equals(valueAfter));
	}
	
	class TestDataSources
	{
		private Map<String, Object> mapWithID;
		private Map<String, Object> objMap;
		
		{
			final int id = -1;
			final String name = "Felix";
			final int gehalt = 2600;
			final double gewicht = 72.02;
			final Time geburtsdatum = new java.sql.Time(36671000);
			final Date geburtszeit = new java.sql.Date(629716210000L);
			final Timestamp geburtsTimeStamp = new java.sql.Timestamp(1104534100000L);
			final boolean krank = false;
			
			this.mapWithID = new HashMap<>();
			this.mapWithID.put(PersonVT.id.getName(), id);
			this.mapWithID.put(PersonVT.Name.getName(), name);
			this.mapWithID.put(PersonVT.Gehalt.getName(), gehalt);
			this.mapWithID.put(PersonVT.Gewicht.getName(), gewicht);
			this.mapWithID.put(PersonVT.Geburtsdatum.getName(), geburtsdatum);
			this.mapWithID.put(PersonVT.Geburtszeit.getName(), geburtszeit);
			this.mapWithID.put(PersonVT.GeburtsTimeStamp.getName(), geburtsTimeStamp);
			this.mapWithID.put(PersonVT.Krank.getName(), krank);
			
			this.objMap = new HashMap<>();
			this.objMap.put(PersonVT.Name.getName(), "Hans");
			this.objMap.put(PersonVT.Gehalt.getName(), 6800);
			this.objMap.put(PersonVT.Gewicht.getName(), 100.88);
			this.objMap.put(PersonVT.Geburtsdatum.getName(), new java.sql.Date(485042400000L));
			this.objMap.put(PersonVT.Geburtszeit.getName(), new java.sql.Time(44365000L));
			this.objMap.put(PersonVT.GeburtsTimeStamp.getName(), new java.sql.Timestamp(611409300000L));
			this.objMap.put("NotInVT", new java.sql.Timestamp(611409300000L));
			this.objMap.put(PersonVT.Krank.getName(), true);
		}
		
		/**
		 * Get a new instance of <code>map</code>. The <code>map</code> include
		 * a new data row of the {@link PersonVT}.
		 * 
		 * @return a new instance of <code>map</code>. The <code>map</code>
		 *         include a new data row of the {@link PersonVT}.
		 */
		Map<String, Object> getMapWithID()
		{
			return new HashMap<>(this.mapWithID);
		}
		
		/**
		 * Get a new instance of <code>map</code>. The <code>map</code> include
		 * a new data row of the {@link PersonVT}.
		 * 
		 * @return a new instance of <code>map</code>. The <code>map</code>
		 *         include a new data row of the {@link PersonVT}.
		 */
		Map<String, Object> getMap()
		{
			return new HashMap<>(this.objMap);
		}
	}
	
	/**
	 * Test method for
	 * {@link xdev.vt.VirtualTable#removeRows(xdev.vt.KeyValues, boolean)}.
	 * 
	 * @throws DBException
	 * @throws VirtualTableException
	 */
	@Test
	public void testRemoveRowsKeyValuesBoolean() throws VirtualTableException, DBException
	{
		final int beforeRC = this.vt.getRowCount();
		final KeyValues identifier = new KeyValues(this.vt, PersonVT.Name.getName(), "Mustermann");
		this.vt.removeRows(identifier, false);
		final int afterRC = this.vt.getRowCount();
		assertEquals(beforeRC - 1, afterRC);
	}
	
	/**
	 * Test method for
	 * {@link xdev.vt.VirtualTable#updateRows(xdev.ui.XdevFormular, xdev.vt.KeyValues, boolean)}
	 * .
	 * 
	 * @throws DBException
	 * @throws VirtualTableException
	 * 
	 */
	@Test
	public void testUpdateRowsXdevFormularKeyValuesBoolean() throws VirtualTableException,
		DBException
	{
		final XdevFormular formular = Mockito.mock(XdevFormular.class);
		
		final KeyValues identifier = new KeyValues(this.vt, PersonVT.Name.getName(), "Mustermann");
		
		Mockito.when(formular.getData(Mockito.anyBoolean())).thenAnswer(
			invocation -> new TestDataSources().getMap());
		
		final int updated = this.vt.updateRows(formular, identifier, false);
		assertEquals(1, updated);
	}
	
	/**
	 * Test method for
	 * {@link xdev.vt.VirtualTable#updateRows(java.util.List, xdev.vt.KeyValues, boolean)}
	 * .
	 * 
	 * @throws DBException
	 * @throws VirtualTableException
	 */
	@Test
	public void testUpdateRowsListKeyValuesBoolean() throws VirtualTableException, DBException
	{
		final List<Object> list = new ArrayList<>(new TestDataSources().getMap().values());
		final KeyValues identifier = new KeyValues(this.vt, PersonVT.Name.getName(), "Mustermann");
		final int updated = this.vt.updateRows(list, identifier, false);
		assertEquals(1, updated);
	}
	
	/**
	 * Test method for
	 * {@link xdev.vt.VirtualTable#updateRows(java.util.Map, xdev.vt.KeyValues, boolean)}
	 * .
	 * 
	 * @throws DBException
	 * @throws VirtualTableException
	 */
	@Test
	public void testUpdateRowsMapOfStringObjectKeyValuesBoolean() throws VirtualTableException,
		DBException
	{
		final KeyValues identifier = new KeyValues(this.vt, PersonVT.Name.getName(), "Mustermann");
		
		final int updated = this.vt.updateRows(new TestDataSources().getMap(), identifier, false);
		assertEquals(1, updated);
	}
	
	/**
	 * Test method for
	 * {@link xdev.vt.VirtualTable#updateRow(java.util.Map, int, boolean)}.
	 * 
	 * @throws DBException
	 * @throws VirtualTableException
	 */
	@Test
	public void testUpdateRow() throws VirtualTableException, DBException
	{
		final TestDataSources tds = new TestDataSources();
		final Map<String, Object> data = tds.getMapWithID();
		final int rowIndex = 0;
		
		this.vt.updateRow(data, rowIndex, false);
		assertEquals(this.vt.getRowAsMap(rowIndex), data);
	}
	
	/**
	 * Test method for {@link xdev.vt.VirtualTable#synchronizeChangedRows()}.
	 */
	@Test
	public void testSynchronizeChangedRows()
	{
		// test run by dbjunit
	}
	
	/**
	 * Test method for
	 * {@link xdev.vt.VirtualTable#synchronizeChangedRows(xdev.db.DBConnection)}
	 * .
	 */
	@Test
	public void testSynchronizeChangedRowsDBConnectionOfQ()
	{
		// test run by dbjunit
	}
	
	/**
	 * Test method for
	 * {@link xdev.vt.VirtualTable#removeColumn(java.lang.String)}.
	 */
	@Test
	public void testRemoveColumnString()
	{
		final String colName = PersonVT.Name.getName();
		final int ccBefore = this.vt.getColumnCount();
		this.vt.removeColumn(colName);
		
		assertEquals(ccBefore - 1, this.vt.getColumnCount());
		assertNull(this.vt.getColumn(colName));
	}
	
	/**
	 * Test method for
	 * {@link xdev.vt.VirtualTable#removeColumn(java.lang.String)}.
	 */
	@Test(expected = VirtualTableException.class)
	public void testRemoveColumnStringVTException()
	{
		final String colName = "Hans";
		this.vt.removeColumn(colName);
	}
	
	/**
	 * Test method for {@link xdev.vt.VirtualTable#removeColumn(int)}.
	 */
	@Test
	public void testRemoveColumnInt()
	{
		final int colIndex = this.vt.getColumnIndex(PersonVT.Name.getName());
		final int ccBefore = this.vt.getColumnCount();
		this.vt.removeColumn(colIndex);
		assertEquals(ccBefore - 1, this.vt.getColumnCount());
	}
	
	/**
	 * Test method for {@link xdev.vt.VirtualTable#removeColumn(int)}.
	 */
	@Test(expected = ArrayIndexOutOfBoundsException.class)
	public void testRemoveColumnIntUpperAIOOBException()
	{
		final int colIndex = this.vt.getColumnCount();
		this.vt.removeColumn(colIndex);
	}
	
	/**
	 * Test method for {@link xdev.vt.VirtualTable#removeColumn(int)}.
	 */
	@Test(expected = ArrayIndexOutOfBoundsException.class)
	public void testRemoveColumnIntLowerAIOOBException()
	{
		final int colIndex = -1;
		this.vt.removeColumn(colIndex);
	}
	
	/**
	 * Test method for {@link xdev.vt.VirtualTable#getAutoValue(int)}.
	 */
	@Test
	public void testGetAutoValue()
	{
		// TODO (FHAE): write test method VirtualTable#getAutoValue(int)
	}
	
	/**
	 * Test method for {@link xdev.vt.VirtualTable#getFirstAutoValue()}.
	 */
	@Test
	public void testGetFirstAutoValue()
	{
		// TODO (FHAE): write test method VirtualTable#getFirstAutoValue()
	}
	
	/**
	 * Test method for {@link xdev.vt.VirtualTable#getAutoValueColumns()}.
	 */
	@Test
	public void testGetAutoValueColumns()
	{
		final String[] autoValueCol = this.vt.getAutoValueColumns();
		assertNotNull(autoValueCol);
		assertEquals(1, autoValueCol.length);
		assertEquals(PersonVT.id.getName(), autoValueCol[0]);
	}
	
	/**
	 * Test method for
	 * {@link xdev.vt.VirtualTable#checkValue(int, java.lang.Object)}.
	 */
	@Test
	public void testCheckValue()
	{
		final VirtualTable tvt = MyVirtualTableExtend.getInstance();
		
		final byte[] ba = {'a', 'b', 'c'};
		final char[] ca = {'a', 'b', 'c'};
		final XdevBlob bc = new XdevBlob(ba);
		final XdevClob xc = new XdevClob(ca);
		
		final byte b = 120;
		final short a = 12;
		
		final Object[] to = {new Integer(10), bc, xc, b, new Short(a), new Long(13), new Float(1), ba, "name"};
		
		for(int i = 1; i < to.length; i++)
		{
			Assert.assertEquals(to[i].toString(), tvt.checkValue(i, to[i]).toString());
		}
	}
	
	/**
	 * Test method for
	 * {@link xdev.vt.VirtualTable#checkValue(int, java.lang.Object)}. Check if
	 * the {@link VirtualTableException} is thrown.
	 */
	@Test(expected = VirtualTableException.class)
	public void testCheckValueNotNullableVTException()
	{
		this.vt.checkValue(0, null);
	}
	
	/**
	 * Test method for
	 * {@link xdev.vt.VirtualTable#createDate(xdev.vt.VirtualTableColumn, java.lang.Object)}
	 * .
	 */
	@Test
	public void testCreateDate()
	{
		// Create Date from date
		final Date expectedDate = new java.sql.Date(629716210000L);
		java.util.Date actual = this.vt.createDate(
			this.vt.getColumnAt(this.vt.getColumnIndex(PersonVT.Geburtsdatum)),
			expectedDate);
		assertEquals(expectedDate, actual);
		
		// Create Date from string
		final String tsString = "1995-06-15 10:10:10.0";
		final Timestamp stamp = new java.sql.Timestamp(803203810000L);
		actual = this.vt.createDate(
			this.vt.getColumnAt(this.vt.getColumnIndex(PersonVT.GeburtsTimeStamp)),
			tsString);
		
		assertEquals(stamp, actual);
		
	}
	
	/**
	 * Test method for
	 * {@link xdev.vt.VirtualTable#createDate(xdev.vt.VirtualTableColumn, java.lang.Object)}
	 * .
	 */
	@Test(expected = VirtualTableException.class)
	public void testCreateDateVTException()
	{
		final Date expectedDate = new java.sql.Date(629716210000L);
		this.vt.createDate(this.vt.getColumnAt(0), expectedDate);
	}
	
	/**
	 * Test method for
	 * {@link xdev.vt.VirtualTable#getCheckUniqueIndexDoubleValues()}.
	 */
	@Test
	public void testGetCheckUniqueIndexDoubleValues()
	{
		assertTrue(this.vt.getCheckUniqueIndexDoubleValues());
	}
	
	/**
	 * Test method for
	 * {@link xdev.vt.VirtualTable#setCheckUniqueIndexDoubleValues(boolean)}.
	 */
	@Test
	public void testSetCheckUniqueIndexDoubleValues()
	{
		this.vt.setCheckUniqueIndexDoubleValues(false);
		assertFalse(this.vt.getCheckUniqueIndexDoubleValues());
	}
	
	/**
	 * Test method for
	 * {@link xdev.vt.VirtualTable#setLastQuery(xdev.db.QueryInfo)}.
	 */
	@Test
	public void testSetLastQuery()
	{
		this.vt.setLastQuery(this.defaultQueryInfo);
		
		assertNotNull(this.vt.getLastQuery());
		assertEquals(this.defaultQueryInfo.getSelect().toString(), this.vt.getLastQuery().getSelect().toString());
		assertArrayEquals(this.vt.getLastQuery().getParameters(), new Object[0]);
	}
	
	/**
	 * Test method for {@link xdev.vt.VirtualTable#getLastQuery()}.
	 */
	@Test
	public void testGetLastQuery()
	{
		assertNull(this.vt.getLastQuery());
		
		this.vt.setLastQuery(this.defaultQueryInfo);
		
		assertNotNull(this.vt.getLastQuery());
		assertEquals(this.defaultQueryInfo.getSelect().toString(), this.vt.getLastQuery().getSelect().toString());
		assertArrayEquals(this.vt.getLastQuery().getParameters(), new Object[0]);
	}
	
	/**
	 * Test method for {@link xdev.vt.VirtualTable#getDefaultSelect()}.
	 */
	@Test
	public void testGetDefaultSelect()
	{
		assertEquals(this.defaultSelect.toString(), this.vt.getDefaultSelect().toString());
	}
	
	/**
	 * Test method for {@link xdev.vt.VirtualTable#getExtendedSelect()}.
	 */
	@Test
	public void testGetExtendedSelect()
	{
		// test run by dbjunit
		this.vt.getExtendedSelect();
	}
	
	/**
	 * Test method for {@link xdev.vt.VirtualTable#getSelect()}.
	 */
	@Test
	public void testGetSelect()
	{
		// test run by dbjunit
	}
	
	/**
	 * Test method for {@link xdev.vt.VirtualTable#reload()}.
	 */
	@Test
	public void testReload()
	{
		// test run by dbjunit
	}
	
	/**
	 * Test method for {@link xdev.vt.VirtualTable#queryAndFill()}.
	 */
	@Test
	public void testQueryAndFill()
	{
		// test run by dbjunit
	}
	
	/**
	 * Test method for
	 * {@link xdev.vt.VirtualTable#queryAndFill(xdev.db.DBDataSource)}.
	 */
	@Test
	public void testQueryAndFillDBDataSource()
	{
		// test run by dbjunit
	}
	
	/**
	 * Test method for
	 * {@link xdev.vt.VirtualTable#queryAndFill(net.jadoth.sqlengine.SELECT, java.lang.Object[])}
	 * .
	 */
	@Test
	public void testQueryAndFillSELECTObjectArray()
	{
		// test run by dbjunit
	}
	
	/**
	 * Test method for
	 * {@link xdev.vt.VirtualTable#queryAndFill(xdev.db.DBDataSource, net.jadoth.sqlengine.SELECT, java.lang.Object[])}
	 * .
	 */
	@Test
	public void testQueryAndFillDBDataSourceSELECTObjectArray()
	{
		// test run by dbjunit
	}
	
	/**
	 * Test method for
	 * {@link xdev.vt.VirtualTable#queryAndAppend(xdev.vt.KeyValues)}.
	 */
	@Test
	public void testQueryAndAppendKeyValues()
	{
		// test run by dbjunit
	}
	
	/**
	 * Test method for
	 * {@link xdev.vt.VirtualTable#queryAndAppend(xdev.db.DBDataSource, xdev.vt.KeyValues)}
	 * .
	 */
	@Test
	public void testQueryAndAppendDBDataSourceKeyValues()
	{
		// test run by dbjunit
	}
	
	/**
	 * Test method for
	 * {@link xdev.vt.VirtualTable#equals(java.lang.Object, java.lang.Object)}.
	 */
	@Test
	public void testEqualsObjectObject()
	{
		assertTrue(VirtualTable.equals(this.vt, this.vt));
		assertFalse(VirtualTable.equals(this.vt, null));
		assertFalse(VirtualTable.equals(null, this.vt));
		
		final double value = 12.30;
		
		final Number n1 = new Double(value);
		final Number n2 = new Double(value);
		
		assertTrue(VirtualTable.equals(n1, n2));
		
		final Number n3 = new Double(value);
		final Number n4 = new Double(value + 1);
		
		assertFalse(VirtualTable.equals(n3, n4));
		
	}
	
}
