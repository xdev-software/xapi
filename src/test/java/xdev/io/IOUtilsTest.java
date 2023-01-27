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

package xdev.io;

import java.awt.Image;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import xdev.junit.IORuntimeException;
import xdev.test.UITest;
import xdev.ui.XdevImage;


/**
 * Tests for the Class {@link IOUtils}.
 * 
 * @author XDEV Software (FHAE)
 */
public class IOUtilsTest
{
	/**
	 * The {@link FileHelper} is used to handle the file objects.
	 */
	public FileHelper fth = null;
	private static boolean closed = false;
	
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
	 * Test for method {@link IOUtils#copy(java.io.Reader, java.io.Writer)}.
	 * 
	 * @throws IOException
	 * @throws
	 */
	@Test
	public void testCopy_Reader_Writer() throws IOException
	{
		this.fth.init();
		this.fth.writeContentIntoFiles();
		
		final ArrayList<XdevFile> files = this.fth.getFiles();
		final XdevFile readFile = files.get(0);
		final Reader in = readFile.openReader();
		
		final XdevFile writeFile = new XdevFile(this.fth.getDirectoryPath() + "copyTest.txt");
		writeFile.createFile();
		final Writer out = writeFile.openWriter();
		Assert.assertEquals(readFile.getSize(), IOUtils.copy(in, out));
		
		writeFile.delete();
	}
	
	/**
	 * Test for method
	 * {@link IOUtils#copy(java.io.Reader, java.io.Writer, boolean)}.
	 * 
	 * @throws IOException
	 */
	@Test
	public void testCopy_Reader_Writer_Boolean() throws IOException
	{
		this.fth.init();
		this.fth.writeContentIntoFiles();
		
		final ArrayList<XdevFile> files = this.fth.getFiles();
		final XdevFile readFile = files.get(0);
		final Reader in = readFile.openReader();
		
		final XdevFile writeFile = new XdevFile(this.fth.getDirectoryPath() + "copyTest.txt");
		writeFile.createFile();
		final Writer out = writeFile.openWriter();
		Assert.assertEquals(readFile.getSize(), IOUtils.copy(in, out, true));
		
		writeFile.delete();
	}
	
	/**
	 * Test for method {@link IOUtils#copy(java.io.InputStream, java.io.OutputStream)}.
	 * 
	 * @throws IOException
	 * @throws
	 */
	@Test
	public void testCopy_InputStream_OutputStream() throws IOException
	{
		this.fth.init();
		this.fth.writeContentIntoFiles();
		
		final ArrayList<XdevFile> files = this.fth.getFiles();
		final XdevFile readFile = files.get(0);
		final InputStream in = readFile.openInputStream();
		
		final XdevFile writeFile = new XdevFile(this.fth.getDirectoryPath() + "copyTest.txt");
		writeFile.createFile();
		final OutputStream out = writeFile.openOutputStream();
		Assert.assertEquals(readFile.getSize(), IOUtils.copy(in, out));
		
		writeFile.delete();
	}
	
	/**
	 * Test for method {@link IOUtils#createURLString(String)}.
	 * 
	 */
	@Test
	public void testCreateURLString()
	{
		final String input = "hello hello";
		@SuppressWarnings("deprecation")
		final String urlString = IOUtils.createURLString(input);
		Assert.assertEquals("hello+hello", urlString);
	}
	
	/**
	 * Test for method {@link IOUtils#getClipboardImage()}.<br>
	 * Test for method {@link IOUtils#putClipboardImage(Image)}.
	 */
	@Ignore
	@Test
	public void testClipboardImage()
	{
		this.fth.init();
		final XdevFile file = this.fth.getFirstFile();
		// Check valid image
		final XdevImage systemXdevImage = file.getSystemImage();
		IOUtils.putClipboardImage(systemXdevImage.getImage());
		
		Assert.assertSame(systemXdevImage.getImage(), IOUtils.getClipboardImage().getImage());
		// Check null return value
		IOUtils.putClipboardImage(null);
		Assert.assertNull(IOUtils.getClipboardImage());
	}
	
	/**
	 * Test for method {@link IOUtils#getClipboardString()}.<br>
	 * Test for method {@link IOUtils#putClipboardString(String)}.
	 */
	@Test
	@Category(UITest.class)
	public void testClipboardString()
	{
		final String expected = "Hans";
		IOUtils.putClipboardString(expected);
		Assert.assertEquals(expected, IOUtils.getClipboardString());
		
		IOUtils.putClipboardString(null);
		Assert.assertNull(IOUtils.getClipboardString());
	}
	
	/**
	 * Test for method {@link IOUtils#getFileName(String)}.
	 * 
	 */
	@Test
	public void testGetFilename()
	{
		final XdevFile file = this.fth.getFirstFile();
		final String filename = IOUtils.getFileName(file.getAbsolutePath());
		Assert.assertEquals(file.getName(), filename);
	}
	
	/**
	 * Test for method {@link IOUtils#getFileName(String)}.
	 * 
	 */
	@Test(expected = NullPointerException.class)
	public void testGetFilename_Exception()
	{
		IOUtils.getFileName(null);
	}
	
	/**
	 * Test for method {@link IOUtils#getFileSize(java.io.File)}.
	 * 
	 * @throws IOException
	 * @throws SecurityException
	 * 
	 */
	@Test
	public void testGetFileSize_File() throws SecurityException, IOException
	{
		this.fth.init();
		
		// Check the empty string when a directory is given
		Assert.assertEquals("", IOUtils.getFileSize(this.fth.getDirectory()));
		// Check the return value
		this.fth.writeContentIntoFiles();
		final String size = IOUtils.getFileSize(this.fth.getFirstFile());
		Assert.assertEquals(this.fth.getFILE_CONTENT_HUMAN_READABLE_SIZE(), size);
		
	}
	
	/**
	 * Test for method {@link IOUtils#getFileSize(long)}.
	 * 
	 * @throws IOException
	 * @throws SecurityException
	 * 
	 */
	@Test
	public void testGetFileSize_Long() throws SecurityException, IOException
	{
		this.fth.init();
		this.fth.writeContentIntoFiles();
		
		final String size = IOUtils.getFileSize(this.fth.getFirstFile().getSize());
		Assert.assertEquals(this.fth.getFILE_CONTENT_HUMAN_READABLE_SIZE(), size);
		
	}
	
	/**
	 * Test for method {@link IOUtils#getPrefix(java.io.File)}.
	 * 
	 */
	@Test
	public void testGetPrefix_File()
	{
		this.fth.init();
		
		final String prefix = IOUtils.getPrefix(this.fth.getFirstFile());
		Assert.assertEquals(this.fth.getFILE_NAMES_WITHOUT_EXTENSION()[0], prefix);
	}
	
	/**
	 * Test for method {@link IOUtils#getPrefix(java.io.File)}.
	 * 
	 */
	@Test(expected = NullPointerException.class)
	public void testGetPrefix_File_Exception()
	{
		IOUtils.getPrefix((File)null);
	}
	
	/**
	 * Test for method {@link IOUtils#getPrefix(String)}.
	 * 
	 */
	@Test
	public void testGetPrefix_String()
	{
		this.fth.init();
		
		final String prefix = IOUtils.getPrefix(this.fth.getFirstFile().getName());
		Assert.assertEquals(this.fth.getFILE_NAMES_WITHOUT_EXTENSION()[0], prefix);
	}
	
	/**
	 * Test for method {@link IOUtils#getPrefix(java.io.File)}.
	 * 
	 */
	@Test(expected = NullPointerException.class)
	public void testGetPrefix_String_Exception()
	{
		IOUtils.getPrefix((String)null);
	}
	
	/**
	 * Test for method {@link IOUtils#getSuffix(java.io.File)}.
	 * 
	 */
	@Test
	public void testGetSuffix_File()
	{
		this.fth.init();
		
		final String suffix = IOUtils.getSuffix(this.fth.getFirstFile());
		Assert.assertEquals(this.fth.getFILE_SUFFIX(), suffix);
	}
	
	/**
	 * Test for method {@link IOUtils#getSuffix(java.io.File)}.
	 * 
	 */
	@Test(expected = NullPointerException.class)
	public void testGetSuffix_File_Exception()
	{
		IOUtils.getSuffix((File)null);
	}
	
	/**
	 * Test for method {@link IOUtils#getSuffix(String)}.
	 * 
	 */
	@Test
	public void testGetSuffix_String()
	{
		this.fth.init();
		
		final String suffix = IOUtils.getSuffix(this.fth.getFirstFile().getName());
		Assert.assertEquals(this.fth.getFILE_SUFFIX(), suffix);
	}
	
	/**
	 * Test for method {@link IOUtils#readChars(File)}.
	 * 
	 */
	@Test
	public void testReadChars_File() throws IOException
	{
		this.fth.init();
		this.fth.writeContentIntoFiles();
		final XdevFile file = this.fth.getFirstFile();
		final char[] content = IOUtils.readChars(file);
		
		org.junit.Assert.assertArrayEquals(this.fth.getFILE_CONTENT_TXT().toCharArray(), content);
	}
	
	/**
	 * Test for method {@link IOUtils#readChars(File)}.
	 * 
	 */
	@Test(expected = IORuntimeException.class)
	public void testReadChars_File_Exception()
	{
		try
		{
			IOUtils.readChars(this.fth.getFirstFile());
		}
		catch(final IOException e)
		{
			throw new IORuntimeException(e);
		}
	}
	
	/**
	 * Test for method {@link IOUtils#readChars(java.io.InputStream)}.
	 * 
	 */
	@Test
	public void testReadChars_InputStream() throws IOException
	{
		this.fth.init();
		this.fth.writeContentIntoFiles();
		final XdevFile file = this.fth.getFirstFile();
		final char[] content = IOUtils.readChars(file.openInputStream());
		org.junit.Assert.assertArrayEquals(this.fth.getFILE_CONTENT_TXT().toCharArray(), content);
	}
	
	/**
	 * Test for method {@link IOUtils#readChars(java.io.InputStream)}.
	 * 
	 */
	@Test(expected = IORuntimeException.class)
	public void testReadChars_InputStream_Exception()
	{
		try
		{
			final InputStream is = this.fth.getFirstFile().openInputStream();
			IOUtils.readChars(is);
		}
		catch(final IOException e)
		{
			throw new IORuntimeException(e);
		}
	}
	
	/**
	 * Test for method {@link IOUtils#readChars(InputStream, String)}.
	 * 
	 */
	@Test
	public void testReadChars_InputStream_String() throws IOException
	{
		this.fth.init();
		this.fth.writeContentIntoFiles();
		final XdevFile file = this.fth.getFirstFile();
		final char[] content = IOUtils.readChars(file.openInputStream(), "ISO-8859-1");
		org.junit.Assert.assertArrayEquals(this.fth.getFILE_CONTENT_TXT().toCharArray(), content);
	}
	
	/**
	 * Test for method {@link IOUtils#readChars(InputStream, String)}.
	 * 
	 * @throws IOException
	 * 
	 * 
	 */
	@Test(expected = IORuntimeException.class)
	public void testReadChars_InputStream_String_Exception() throws IOException
	{
		this.fth.init();
		this.fth.writeContentIntoFiles();
		final XdevFile file = this.fth.getFirstFile();
		
		try
		{
			try(InputStream is = file.openInputStream())
			{
				final char[] content = IOUtils.readChars(is, "fail");
				org.junit.Assert.assertArrayEquals(this.fth.getFILE_CONTENT_TXT().toCharArray(), content);
			}
		}
		catch(final IOException e)
		{
			throw new IORuntimeException(e);
		}
	}
	
	/**
	 * Test for method {@link IOUtils#readChars(Reader, String)}.
	 * 
	 */
	@Test
	public void testReadChars_Reader() throws IOException
	{
		this.fth.init();
		this.fth.writeContentIntoFiles();
		final XdevFile file = this.fth.getFirstFile();
		final Reader in = new InputStreamReader(file.openInputStream());
		final char[] content = IOUtils.readChars(in);
		org.junit.Assert.assertArrayEquals(this.fth.getFILE_CONTENT_TXT().toCharArray(), content);
	}
	
	/**
	 * Test for method {@link IOUtils#readChars(Reader)}.
	 * 
	 * @throws IOException
	 * 
	 */
	@Test(expected = IORuntimeException.class)
	public void testReadChars_Reader_Exception() throws IOException
	{
		Reader in = null;
		char[] content;
		
		this.fth.init();
		this.fth.writeContentIntoFiles();
		final XdevFile file = this.fth.getFirstFile();
		in = new InputStreamReader(file.openInputStream());
		in.close();
		
		try
		{
			content = IOUtils.readChars(in, false);
			org.junit.Assert.assertArrayEquals(this.fth.getFILE_CONTENT_TXT().toCharArray(), content);
		}
		catch(final IOException e)
		{
			throw new IORuntimeException(e);
		}
	}
	
	/**
	 * Test for method {@link IOUtils#readChars(Reader, String,Boolean)}.
	 * 
	 * @throws IOException
	 * 
	 */
	@Test
	public void testReadChars_Reader_Boolean() throws IOException
	{
		Reader in = null;
		char[] content;
		
		this.fth.init();
		this.fth.writeContentIntoFiles();
		final XdevFile file = this.fth.getFirstFile();
		in = new InputStreamReader(file.openInputStream());
		
		content = IOUtils.readChars(in, false);
		org.junit.Assert.assertArrayEquals(this.fth.getFILE_CONTENT_TXT().toCharArray(), content);
		// Stream is not closed
		in.read();
		in.close();
	}
	
	/**
	 * Test for method {@link IOUtils#readChars(Reader, String,Boolean)}.
	 * 
	 * @throws IOException
	 * 
	 */
	@Test(expected = IORuntimeException.class)
	public void testReadChars_Reader_Boolean_Execption() throws IOException
	{
		Reader in = null;
		
		this.fth.init();
		this.fth.writeContentIntoFiles();
		final XdevFile file = this.fth.getFirstFile();
		in = new InputStreamReader(file.openInputStream());
		IOUtils.readChars(in, true);
		
		try
		{
			in.read();
		}
		catch(final IOException e)
		{
			throw new IORuntimeException(e);
		}
	}
	
	/**
	 * Test for method {@link IOUtils#readData(InputStream)}.
	 * 
	 * @throws IOException
	 * 
	 */
	@Test
	public void testReadDataInput() throws IOException
	{
		this.fth.init();
		this.fth.writeContentIntoFiles();
		final XdevFile file = this.fth.getFirstFile();
		try(final InputStream in = file.openInputStream())
		{
			final byte[] content = IOUtils.readData(in);
			org.junit.Assert.assertArrayEquals(this.fth.getFILE_CONTENT_TXT().getBytes(), content);
		}
	}
	
	/**
	 * Test for method {@link IOUtils#readData(InputStream)}.
	 * 
	 * @throws IOException
	 * 
	 */
	@Test(expected = IORuntimeException.class)
	public void testReadDataInput_Exception() throws IOException
	{
		this.fth.init();
		this.fth.writeContentIntoFiles();
		final XdevFile file = this.fth.getFirstFile();
		final InputStream in = file.openInputStream();
		in.close();
		try
		{
			IOUtils.readData(in);
		}
		catch(final IOException e)
		{
			throw new IORuntimeException(e);
		}
	}
	
	/**
	 * Test for method {@link IOUtils#readData(InputStream, int)}.
	 * 
	 * @throws IOException
	 * 
	 */
	@Test
	public void testReadDataInput_Int() throws IOException
	{
		final int size = 6;
		this.fth.init();
		this.fth.writeContentIntoFiles();
		final XdevFile file = this.fth.getFirstFile();
		try(final InputStream in = file.openInputStream())
		{
			final byte[] content = IOUtils.readData(in, size);
			org.junit.Assert.assertArrayEquals(
				this.fth.getFILE_CONTENT_TXT().substring(0, size).getBytes(),
				content);
		}
	}
	
	/**
	 * Test for method {@link IOUtils#readData(InputStream, int)}.
	 * 
	 * @throws IOException
	 * 
	 */
	@Test(expected = IORuntimeException.class)
	public void testReadDataInput_Int_Exception() throws IOException
	{
		this.fth.init();
		this.fth.writeContentIntoFiles();
		final XdevFile file = this.fth.getFirstFile();
		final InputStream in = file.openInputStream();
		in.close();
		
		try
		{
			IOUtils.readData(in, 5);
		}
		catch(final IOException e)
		{
			throw new IORuntimeException(e);
		}
	}
	
	/**
	 * Test for method {@link IOUtils#readString(File)}.
	 * 
	 * @throws IOException
	 * 
	 */
	@Test
	public void testReadString_File() throws IOException
	{
		this.fth.init();
		this.fth.writeContentIntoFiles();
		final XdevFile file = this.fth.getFirstFile();
		
		final String content = IOUtils.readString(file);
		Assert.assertEquals(this.fth.getFILE_CONTENT_TXT(), content);
	}
	
	/**
	 * Test for method {@link IOUtils#readString(File)}.
	 * 
	 * @throws IOException
	 * 
	 */
	@Test(expected = IORuntimeException.class)
	public void testReadString_File_Exception()
	{
		final XdevFile file = this.fth.getFirstFile();
		try
		{
			IOUtils.readString(file);
		}
		catch(final IOException e)
		{
			throw new IORuntimeException(e);
		}
		
	}
	
	/**
	 * Test for method {@link IOUtils#readString(InputStream)}.
	 * 
	 * @throws IOException
	 * 
	 */
	@Test
	public void testReadString_InputStream() throws IOException
	{
		this.fth.init();
		this.fth.writeContentIntoFiles();
		try(final InputStream in = this.fth.getFirstFile().openInputStream())
		{
			final String content = IOUtils.readString(in);
			Assert.assertEquals(this.fth.getFILE_CONTENT_TXT(), content);
		}
	}
	
	/**
	 * Test for method {@link IOUtils#readString(InputStream)}.
	 * 
	 * @throws IOException
	 * 
	 */
	@Test(expected = IORuntimeException.class)
	public void testReadString_InputStream_Exception() throws IOException
	{
		this.fth.init();
		this.fth.writeContentIntoFiles();
		final InputStream in = this.fth.getFirstFile().openInputStream();
		in.close();
		
		try
		{
			IOUtils.readString(in);
		}
		catch(final IOException e)
		{
			throw new IORuntimeException(e);
		}
	}
	
	/**
	 * Test for method {@link IOUtils#readString(InputStream, String)}.
	 * 
	 * @throws IOException
	 * 
	 */
	@Test
	public void testReadString_InputStream_String() throws IOException
	{
		this.fth.init();
		this.fth.writeContentIntoFiles();
		try(final InputStream in = this.fth.getFirstFile().openInputStream())
		{
			final String content = IOUtils.readString(in, "ISO-8859-1");
			Assert.assertEquals(this.fth.getFILE_CONTENT_TXT(), content);
		}
	}
	
	/**
	 * Test for method {@link IOUtils#readString(InputStream, String)}.
	 * 
	 * @throws IOException
	 * 
	 */
	@Test(expected = IORuntimeException.class)
	public void testReadString_InputStream_String_Exception() throws IOException
	{
		this.fth.init();
		this.fth.writeContentIntoFiles();
		final InputStream in = this.fth.getFirstFile().openInputStream();
		in.close();
		
		try
		{
			IOUtils.readString(in, "ISO-8859-1");
		}
		catch(final IOException e)
		{
			throw new IORuntimeException(e);
		}
	}
	
	/**
	 * Test for method {@link IOUtils#readString(Reader)}.
	 * 
	 * @throws IOException
	 * 
	 */
	@Test
	public void testReadString_Reader() throws IOException
	{
		this.fth.init();
		this.fth.writeContentIntoFiles();
		final Reader in = new InputStreamReader(this.fth.getFirstFile().openInputStream());
		
		final String content = IOUtils.readString(in);
		Assert.assertEquals(this.fth.getFILE_CONTENT_TXT(), content);
	}
	
	/**
	 * Test for method {@link IOUtils#readString(Reader)}.
	 * 
	 * @throws IOException
	 * 
	 */
	@Test(expected = IORuntimeException.class)
	public void testReadString_Reader_Exception() throws IOException
	{
		this.fth.init();
		this.fth.writeContentIntoFiles();
		final Reader in = new InputStreamReader(this.fth.getFirstFile().openInputStream());
		in.close();
		
		try
		{
			IOUtils.readString(in);
		}
		catch(final IOException e)
		{
			throw new IORuntimeException(e);
		}
	}
	
	/**
	 * Test for method {@link IOUtils#readString(Reader, Boolean)}.
	 * 
	 * @throws IOException
	 * 
	 */
	@Test
	public void testReadString_Reader_Boolean() throws IOException
	{
		this.fth.init();
		this.fth.writeContentIntoFiles();
		try(final Reader in = new InputStreamReader(this.fth.getFirstFile().openInputStream()))
		{
			final String content = IOUtils.readString(in, false);
			Assert.assertEquals(this.fth.getFILE_CONTENT_TXT(), content);
			in.ready();
		}
	}
	
	/**
	 * Test for method {@link IOUtils#readString(Reader, Boolean)}.
	 * 
	 * @throws IOException
	 * 
	 */
	@Test(expected = IORuntimeException.class)
	public void testReadString_Reader_Boolean_Exception() throws IOException
	{
		this.fth.init();
		this.fth.writeContentIntoFiles();
		final Reader in = new InputStreamReader(this.fth.getFirstFile().openInputStream());
		IOUtils.readString(in, true);
		
		try
		{
			in.ready();
		}
		catch(final IOException e)
		{
			throw new IORuntimeException(e);
		}
	}
	
	/**
	 * Test for method {@link IOUtils#renewSessionID()}.
	 * 
	 */
	@SuppressWarnings("deprecation")
	@Test
	public void testRenewSessionID()
	{
		final String sID_old = IOUtils.getSessionID();
		IOUtils.renewSessionID();
		final String sID_new = IOUtils.getSessionID();
		Assert.assertNotSame(sID_new, sID_old);
	}
	
	/**
	 * Test for method {@link IOUtils#readData(File)}.
	 * 
	 * @throws SecurityException
	 * @throws IOException
	 */
	@Test
	public void testReadData() throws SecurityException, IOException
	{
		this.fth.init();
		this.fth.writeContentIntoFiles();
		final byte[] ret = IOUtils.readData(this.fth.getFirstFile());
		
		final byte[] bytes = this.fth.getFILE_CONTENT_TXT().getBytes();
		
		for(int i = 0; i < bytes.length; i++)
		{
			Assert.assertEquals(ret[i], bytes[i]);
		}
		
	}
	
	/**
	 * Test for method {@link IOUtils#copy(InputStream, OutputStream)}.
	 * 
	 * @throws SecurityException
	 * @throws IOException
	 */
	@Test
	public void testcopy() throws SecurityException, IOException
	{
		final String valid = "test";
		
		try(final InputStream bais = new ByteArrayInputStream(valid.getBytes()))
		{
			final int ret = IOUtils.copy(bais, new OutputStream()
			{
				@Override
				public void write(final int b) throws IOException
				{
				}
			});
			
			Assert.assertEquals(valid.getBytes().length, ret);
		}
		
	}
	
	/**
	 * Test for method {@link IOUtils#copy(InputStream, OutputStream, boolean)}.
	 * 
	 * @throws SecurityException
	 * @throws IOException
	 */
	@Test
	public void testcopy2() throws SecurityException, IOException
	{
		final String valid = "test";
		final InputStream bais = new ByteArrayInputStream(valid.getBytes())
		{
			@Override
			public void close() throws IOException
			{
				super.close();
				closed = true;
			}
		};
		
		final int ret = IOUtils.copy(bais, new OutputStream()
		{
			@Override
			public void write(final int b) throws IOException
			{
			}
		}, true);
		
		Assert.assertEquals(valid.getBytes().length, ret);
		Assert.assertEquals(true, closed);
	}
	
	/**
	 * Test for method {@link IOUtils#copy(File, File)}.
	 * 
	 * @throws SecurityException
	 * @throws IOException
	 */
	@Test
	public void testcopy3() throws SecurityException, IOException
	{
		this.fth.init();
		this.fth.writeContentIntoFiles();
		
		File valid = new File(this.fth.getDirectoryPath());
		valid = IOUtils.copy(this.fth.getFirstFile(), valid);
		
		Assert.assertEquals(IOUtils.readString(this.fth.getFirstFile()), IOUtils.readString(valid));
	}
	
	/**
	 * Test for method {@link IOUtils#ensureDir(File)}.
	 * 
	 * @throws SecurityException
	 * @throws IOException
	 */
	@Test(expected = IOException.class)
	public void testensureDir() throws SecurityException, IOException
	{
		this.fth.init();
		this.fth.writeContentIntoFiles();
		
		final File valid = this.fth.getFirstFile();
		IOUtils.ensureDir(valid);
		valid.delete();
	}
	
}
