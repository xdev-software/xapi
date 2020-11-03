
package xdev.io;

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

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import xdev.util.XdevDate;
import xdev.util.XdevList;


/**
 * Tests for the Class {@link XdevFile}.
 * 
 * @author XDEV Software (FHAE)
 */
public class XdevFileTest
{
	public FileHelper fth = new FileHelper();
	public final static double DOUBLE_DELTA = 0;
	
	@Before
	public void init()
	{
		this.fth = new FileHelper();
	}
	
	@After
	public void clean()
	{
		this.fth.deleteAll();
	}
	
	/**
	 * Test for method {@link XdevFile#createDirectory()}.
	 */
	@Test
	public void testCreateDirectory()
	{
		Assert.assertTrue(this.fth.getDirectory().createDirectory());
		Assert.assertFalse(this.fth.getDirectory().createDirectory());
	}
	
	/**
	 * Test for method {@link XdevFile#createFile()}.
	 */
	@Test
	public void testCreateFile()
	{
		final XdevFile file = this.fth.getFiles().get(0);
		// the directory doesnt exist, now the file can not created
		Assert.assertFalse(file.createFile());
		
		this.fth.getDirectory().createDirectory();
		Assert.assertTrue(file.createFile());
	}
	
	
	/**
	 * Test for method {@link XdevFile#getChildren()}.
	 */
	@Test
	public void testGetChildren()
	{
		this.fth.init();
		
		final XdevList<XdevFile> childrenDir = this.fth.getDirectory().getChildren();
		final XdevList<XdevFile> childrenFile = this.fth.getFirstFile().getChildren();
		
		Assert.assertEquals(this.fth.getFiles().size(), childrenDir.size());
		Assert.assertEquals(0, childrenFile.size());
	}
	
	/**
	 * Test for method {@link XdevFile#getLastModified()}.
	 */
	@Test
	public void testGetLastModified()
	{
		this.fth.init();
		final XdevDate lastModif = XdevDate.now();
		this.fth.getFirstFile().setLastModified(lastModif);
		
		Assert.assertTrue(lastModif.equals(this.fth.getFirstFile().getLastModified()));
	}
	
	/**
	 * Test for method {@link XdevFile#getParentXdevFile()}.
	 */
	@Test
	public void testGetParentXdevFile()
	{
		this.fth.init();
		final XdevFile parent = this.fth.getFirstFile().getParentXdevFile();
		Assert.assertNotNull(parent);
	}
	
	/**
	 * Test for method {@link XdevFile#getSize()}.
	 * 
	 * @throws IOException
	 *             .
	 * @throws SecurityException
	 *             .
	 */
	@Test
	public void testGetSize() throws SecurityException, IOException
	{
		this.fth.init();
		final XdevFile file = this.fth.getFirstFile();
		
		Assert.assertEquals(0.00, file.getSize(), DOUBLE_DELTA);
		
		this.fth.writeContentIntoFiles();
		
		Assert.assertEquals(this.fth.getFILE_CONTENT_TXT().length(), file.getSize(), DOUBLE_DELTA);
	}
	
	/**
	 * Test for method {@link XdevFile#getSystemDisplayName()}.
	 * 
	 */
	@Test
	public void testGetSystemDisplayName()
	{
		this.fth.init();
		Assert.assertEquals(this.fth.getFirstFile().getSystemDisplayName(), this.fth.getFirstFile().getName());
	}
	
	/**
	 * Test for method {@link XdevFile#getSystemIcon()}.
	 */
	@Test
	public void testGetSystemIcon()
	{
		Assert.assertNull(this.fth.getFirstFile().getSystemIcon());
		this.fth.init();
		Assert.assertNotNull(this.fth.getFirstFile().getSystemIcon());
	}
	
	/**
	 * Test for method {@link XdevFile#getSystemImage()}.
	 */
	@Test
	public void testGetSystemImage()
	{
		Assert.assertNull(this.fth.getFirstFile().getSystemImage());
		this.fth.init();
		Assert.assertNotNull(this.fth.getFirstFile().getSystemImage());
	}
	
	/**
	 * Test for method {@link XdevFile#getSystemImage()}.
	 */
	@Test
	@Ignore
	public void testGetSystemTypeDescription()
	{
		
		Assert.assertNull(this.fth.getFirstFile().getSystemTypeDescription());
		this.fth.init();
		Assert.assertEquals(this.fth.getFirstFile().getSystemTypeDescription(), this.fth.getFILE_SYS_TYPE());
	}
	
	/**
	 * Test for method {@link XdevFile#isParentOf(XdevFile)}.
	 */
	@Test
	public void testIsParentOf()
	{
		final XdevFile directory = this.fth.getDirectory();
		final XdevFile file = this.fth.getFirstFile();
		
		Assert.assertEquals(true, directory.isParentOf(file));
		Assert.assertEquals(false, file.isParentOf(directory));
		Assert.assertEquals(false, directory.isParentOf(null));
	}
	
	/**
	 * Test for method {@link XdevFile#isChildOf(XdevFile)}.
	 */
	@Test
	public void testIsChildOf()
	{
		final XdevFile directory = this.fth.getDirectory();
		final XdevFile file = this.fth.getFirstFile();
		
		Assert.assertEquals(true, file.isChildOf(directory));
		Assert.assertEquals(false, directory.isChildOf(file));
	}
	
	/**
	 * Test for method {@link XdevFile#setLastModified(XdevDate)}.
	 */
	@Test
	public void testSetLastModified()
	{
		final XdevFile file = this.fth.getFirstFile();
		final XdevDate now = XdevDate.now();
		
		Assert.assertEquals(false, file.setLastModified(now));
		this.fth.init();
		Assert.assertEquals(true, file.setLastModified(now));
	}
	
	/**
	 * Test for method {@link XdevFile#setLastModified(XdevDate)}.
	 */
	@Test(expected = NullPointerException.class)
	public void testSetLastModified_Exception()
	{
		this.fth.getFirstFile().setLastModified(null);
	}
	
	/**
	 * Test for method {@link XdevFile#openInputStream()}.
	 * 
	 * @throws SecurityException
	 *             .
	 * @throws IOException
	 *             .
	 */
	@Test
	public void testOpenInputStream() throws SecurityException, IOException
	{
		this.fth.init();
		final XdevFile file = this.fth.getFirstFile();
		
		try(final InputStream stream = file.openInputStream())
		{
			Assert.assertNotNull(stream);
		}
	}
	
	/**
	 * Test for method {@link XdevFile#openOutputStream()}.
	 * 
	 * @throws SecurityException
	 * @throws IOException
	 * 
	 */
	@Test
	public void testOpenOutputStream() throws SecurityException, IOException
	{
		this.fth.init();
		final XdevFile file = this.fth.getFirstFile();
		
		try(final OutputStream output = file.openOutputStream())
		{
			Assert.assertNotNull(output);
			output.write(this.fth.getFILE_CONTENT_TXT().getBytes());
			Assert.assertEquals(this.fth.getFILE_CONTENT_TXT().length(), file.getSize());
		}
		
	}
	
	/**
	 * Test for method {@link XdevFile#openOutputStream(boolean)}.
	 * 
	 * @throws SecurityException
	 * @throws IOException
	 * 
	 */
	@Test
	public void testOpenOutputStream_Boolean() throws SecurityException, IOException
	{
		this.fth.init();
		this.fth.writeContentIntoFiles();
		
		final XdevFile file = this.fth.getFirstFile();
		final String context = this.fth.getFILE_CONTENT_TXT();
		
		// Write the context at the end of the file, now the file include the context 2 times
		try(final OutputStream output = file.openOutputStream(true))
		{
			output.write(context.getBytes());
			Assert.assertEquals(context.length() * 2, file.getSize());
		}
		
		try(final OutputStream output_no_append = file.openOutputStream(false))
		{
			output_no_append.write(context.getBytes());
			Assert.assertEquals(context.length(), file.getSize());
		}
	}
	
	/**
	 * Test for method {@link XdevFile#openReader()}.
	 * 
	 * @throws IOException
	 * 
	 */
	@Test
	public void testOpenReader() throws IOException
	{
		this.fth.init();
		
		try(final Reader reader = this.fth.getFirstFile().openReader())
		{
			Assert.assertNotNull(reader);
		}
	}
	
	/**
	 * Test for method {@link XdevFile#openWriter()}.
	 * 
	 * @throws IOException
	 * 
	 */
	@Test
	public void testOpenWriter() throws IOException
	{
		this.fth.init();
		
		try(final Writer writer = this.fth.getFirstFile().openWriter())
		{
			Assert.assertNotNull(writer);
		}
	}
	
	/**
	 * Test for method {@link XdevFile#openWriter()}.
	 * 
	 * @throws IOException
	 * 
	 */
	@Test
	public void testOpenWriter_Boolean() throws IOException
	{
		this.fth.init();
		this.fth.writeContentIntoFiles();
		
		final XdevFile file = this.fth.getFirstFile();
		final String context = this.fth.getFILE_CONTENT_TXT();
		
		try(final Writer writer_append = file.openWriter(true))
		{
			writer_append.write(context);
		}
		Assert.assertEquals(context.length() * 2, file.getSize());
		
		try(final Writer writer_No_append = file.openWriter(false))
		{
			Assert.assertNotNull(writer_No_append);
			writer_No_append.write(context);
		}
		Assert.assertEquals(context.length(), file.getSize());
	}
	
	/**
	 * Test for method {@link XdevFile#isParentOf(XdevFile)} and
	 * {@link XdevFile#isChildOf(XdevFile)}.
	 */
	@Test
	public void testHierarchy()
	{
		final XdevFile home = new XdevFile(System.getProperty("java.home"));
		final XdevFile lib = new XdevFile(home, "lib");
		Assert.assertTrue(home.isParentOf(lib));
		Assert.assertTrue(lib.isChildOf(home));
	}
}
