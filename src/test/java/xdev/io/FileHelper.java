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

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.ArrayList;

import org.junit.Ignore;


@Ignore
public class FileHelper
{
	public static final String FILE_SEPARATOR = System.getProperty("file.separator");
	public static final String DIRECTORY_NAME = "xdevTestDirectory";
	
	private ArrayList<XdevFile> testFiles = new ArrayList<>();
	private XdevFile testDirectory = null;
	private XdevFile root = null;
	
	// Root
	private final String rootPath;
	
	// Directory
	private final String directoryPath;
	
	// Files
	public static final String FILE_NAMES[] = {
		"xdevTestClass1.txt",
		"xdevTestClass2.txt",
		"xdevTestClass3.txt",
		"xdevTestClass4.txt"};
	public static final String FILE_NAMES_WITHOUT_EXTENSION[] = {
		"xdevTestClass1",
		"xdevTestClass2",
		"xdevTestClass3",
		"xdevTestClass4"};
	
	public static final String FILE_SUFFIX = ".txt";
	
	public static final String FILE_SYS_TYPE = "Textdokument";
	public static final String FILE_CONTENT_TXT = "Dieser Text wird in der Datei gespeichert!";
	
	public static final String FILE_CONTENT_HUMAN_READABLE_SIZE = "42 Bytes";
	
	/**
	 * Create a {@link FileHelper} with the following structure:
	 * 
	 * <ul>
	 * <li>wrktest/xdevTestDirectory/</li>
	 * <li>wrktest/xdevTestDirectory/xdevTestClass1.txt</li>
	 * <li>wrktest/xdevTestDirectory/xdevTestClass2.txt</li>
	 * <li>wrktest/xdevTestDirectory/xdevTestClass3.txt</li>
	 * <li>wrktest/xdevTestDirectory/xdevTestClass4.txt</li>
	 * </ul>
	 * 
	 * The Files does not exist. To create the files call the method
	 * {@link #init()}.
	 * 
	 */
	public FileHelper()
	{
		try
		{
			File tempFolder = Files.createTempDirectory("wrktest").toFile();
			tempFolder.deleteOnExit();
			
			this.rootPath = tempFolder.getAbsolutePath();
		}
		catch(IOException e)
		{
			throw new RuntimeException(e);
		}
		
		this.directoryPath = new File(
			this.rootPath
				+ FILE_SEPARATOR
				+ DIRECTORY_NAME).getAbsolutePath();
		
		this.root = new XdevFile(this.rootPath);
		this.root.deleteOnExit();
		
		this.testDirectory = new XdevFile(this.directoryPath);
		this.testDirectory.deleteOnExit();
		
		for(final String path : FILE_NAMES)
		{
			final XdevFile file = new XdevFile(this.testDirectory + FILE_SEPARATOR + path);
			file.deleteOnExit();
			this.testFiles.add(file);
		}
		
	}
	
	/**
	 * Create the directory and all files.
	 */
	public void init()
	{
		this.testDirectory.createDirectory();
		
		for(final XdevFile file : this.testFiles)
		{
			file.createFile();
		}
	}
	
	/**
	 * Put the {@link #FILE_CONTENT_TXT} into all test fiels and close the
	 * stream.
	 * 
	 * @throws SecurityException
	 * @throws IOException
	 */
	public void writeContentIntoFiles() throws SecurityException, IOException
	{
		for(final XdevFile file : this.testFiles)
		{
			try(final OutputStream stream = file.openOutputStream())
			{
				stream.write(FILE_CONTENT_TXT.getBytes());
			}
		}
	}
	
	public void deleteAll()
	{
		for(final XdevFile file : this.testFiles)
		{
			file.delete();
		}
		
		this.testDirectory.delete();
		
		this.root.delete();
		
		new File(this.rootPath).delete();
	}
	
	public String getDirectoryPath()
	{
		return this.directoryPath;
	}
	
	public ArrayList<XdevFile> getFiles()
	{
		return this.testFiles;
	}
	
	public XdevFile getFirstFile()
	{
		return this.testFiles.get(0);
	}
	
	public XdevFile getRoot()
	{
		return this.root;
	}
	
	public XdevFile getDirectory()
	{
		return this.testDirectory;
	}
	
	public String getFILE_CONTENT_TXT()
	{
		return FILE_CONTENT_TXT;
	}
	
	public String getFILE_SYS_TYPE()
	{
		return FILE_SYS_TYPE;
	}
	
	public String getFILE_CONTENT_HUMAN_READABLE_SIZE()
	{
		return FILE_CONTENT_HUMAN_READABLE_SIZE;
	}
	
	public String[] getFILE_NAMES_WITHOUT_EXTENSION()
	{
		return FILE_NAMES_WITHOUT_EXTENSION;
	}
	
	public String[] getFILE_NAMES()
	{
		return FILE_NAMES;
	}
	
	public String getFILE_SUFFIX()
	{
		return FILE_SUFFIX;
	}
}
