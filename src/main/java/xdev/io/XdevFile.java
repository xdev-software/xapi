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
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.URI;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.swing.Icon;
import javax.swing.filechooser.FileSystemView;

import xdev.ui.GraphicUtils;
import xdev.ui.XdevImage;
import xdev.util.XdevDate;
import xdev.util.XdevList;


/**
 * The standard abstract representation of file and directory pathnames in XDEV.
 * 
 * @see File
 * 
 * @author XDEV Software
 * 
 * @since 2.0
 */
public class XdevFile extends File
{
	/**
	 * Converts an array of java.io.Files to an array of XdevFiles;
	 * 
	 * @param files
	 *            the array to convert
	 * @return an array of XdevFiles
	 * 
	 * @since 4.0
	 */
	public static XdevFile[] toXdevFile(File... files)
	{
		int c = files.length;
		XdevFile[] xf = new XdevFile[c];
		for(int i = 0; i < c; i++)
		{
			xf[i] = new XdevFile(files[i]);
		}
		return xf;
	}
	
	/**
	 * 
	 */
	private static final long	serialVersionUID	= 3493429683834583598L;
	
	
	/**
	 * Creates a new {@link XdevFile} instance by converting the given pathname
	 * string into an abstract pathname. If the given string is the empty
	 * string, then the result is the empty abstract pathname.
	 * 
	 * @param path
	 *            a file with a absolute path
	 * @throws NullPointerException
	 *             If the <code>path</code> argument is <code>null</code>
	 */
	public XdevFile(File path) throws NullPointerException
	{
		super(path.getAbsolutePath());
	}
	
	
	/**
	 * Creates a new {@link XdevFile} instance from a parent abstract pathname
	 * and a child pathname string.
	 * 
	 * <p>
	 * If <code>parent</code> is <code>null</code> then the new {@link XdevFile}
	 * instance is created as if by invoking the single-argument
	 * <code>File</code> constructor on the given <code>child</code> pathname
	 * string.
	 * 
	 * <p>
	 * Otherwise the <code>parent</code> abstract pathname is taken to denote a
	 * directory, and the <code>child</code> pathname string is taken to denote
	 * either a directory or a file. If the <code>child</code> pathname string
	 * is absolute then it is converted into a relative pathname in a
	 * system-dependent way. If <code>parent</code> is the empty abstract
	 * pathname then the new {@link XdevFile} instance is created by converting
	 * <code>child</code> into an abstract pathname and resolving the result
	 * against a system-dependent default directory. Otherwise each pathname
	 * string is converted into an abstract pathname and the child abstract
	 * pathname is resolved against the parent.
	 * 
	 * @param parent
	 *            The parent abstract pathname
	 * @param child
	 *            The child pathname string
	 * @throws NullPointerException
	 *             If <code>child</code> is <code>null</code>
	 */
	public XdevFile(File parent, String child) throws NullPointerException
	{
		super(parent,child);
	}
	
	
	/**
	 * Creates a new {@link XdevFile} instance from a parent pathname string and
	 * a child pathname string.
	 * 
	 * <p>
	 * If <code>parent</code> is <code>null</code> then the new {@link XdevFile}
	 * instance is created as if by invoking the single-argument
	 * <code>File</code> constructor on the given <code>child</code> pathname
	 * string.
	 * 
	 * <p>
	 * Otherwise the <code>parent</code> pathname string is taken to denote a
	 * directory, and the <code>child</code> pathname string is taken to denote
	 * either a directory or a file. If the <code>child</code> pathname string
	 * is absolute then it is converted into a relative pathname in a
	 * system-dependent way. If <code>parent</code> is the empty string then the
	 * new {@link XdevFile} instance is created by converting <code>child</code>
	 * into an abstract pathname and resolving the result against a
	 * system-dependent default directory. Otherwise each pathname string is
	 * converted into an abstract pathname and the child abstract pathname is
	 * resolved against the parent.
	 * 
	 * @param parent
	 *            The parent pathname string
	 * @param child
	 *            The child pathname string
	 * @throws NullPointerException
	 *             If <code>child</code> is <code>null</code>
	 */
	public XdevFile(String parent, String child) throws NullPointerException
	{
		super(parent,child);
	}
	
	
	/**
	 * Creates a new {@link XdevFile} instance by converting the given pathname
	 * string into an abstract pathname. If the given string is the empty
	 * string, then the result is the empty abstract pathname.
	 * 
	 * @param pathname
	 *            A pathname string
	 * @throws NullPointerException
	 *             If the <code>pathname</code> argument is <code>null</code>
	 */
	public XdevFile(String pathname) throws NullPointerException
	{
		super(pathname);
	}
	
	
	/**
	 * Creates a new {@link XdevFile} instance by converting the given
	 * <tt>file:</tt> URI into an abstract pathname.
	 * 
	 * <p>
	 * The exact form of a <tt>file:</tt> URI is system-dependent, hence the
	 * transformation performed by this constructor is also system-dependent.
	 * 
	 * <p>
	 * For a given abstract pathname <i>f</i> it is guaranteed that
	 * 
	 * <blockquote><tt>
	 * new File(</tt><i>&nbsp;f</i> <tt>.{@link #toURI() toURI}()).equals(</tt>
	 * <i>&nbsp;f</i> <tt>.{@link #getAbsoluteFile() getAbsoluteFile}())
	 * </tt></blockquote>
	 * 
	 * so long as the original abstract pathname, the URI, and the new abstract
	 * pathname are all created in (possibly different invocations of) the same
	 * Java virtual machine. This relationship typically does not hold, however,
	 * when a <tt>file:</tt> URI that is created in a virtual machine on one
	 * operating system is converted into an abstract pathname in a virtual
	 * machine on a different operating system.
	 * 
	 * @param uri
	 *            An absolute, hierarchical URI with a scheme equal to
	 *            <tt>"file"</tt>, a non-empty path component, and undefined
	 *            authority, query, and fragment components
	 * 
	 * @throws NullPointerException
	 *             If <tt>uri</tt> is <tt>null</tt>
	 * 
	 * @throws IllegalArgumentException
	 *             If the preconditions on the parameter do not hold
	 * 
	 * @see #toURI()
	 * @see java.net.URI
	 */
	public XdevFile(URI uri) throws NullPointerException, IllegalArgumentException
	{
		super(uri);
	}
	
	
	/**
	 * Return a {@link XdevDate} with the time of the last modification of this
	 * file.
	 * 
	 * @return a {@link XdevDate} with the time of the last modification of this
	 *         file.
	 * 
	 * 
	 * @throws SecurityException
	 *             If a security manager exists and its
	 *             {@link java.lang.SecurityManager#checkRead(java.lang.String)}
	 *             method denies read access to the file
	 */
	public XdevDate getLastModified() throws SecurityException
	{
		return new XdevDate(lastModified());
	}
	
	
	/**
	 * Sets the last-modified time of the file.
	 * 
	 * @param date
	 *            the {@link XdevDate} is set as the last modification of this
	 *            file.
	 * 
	 * @return <code>true</code> if and only if the operation succeeded;
	 *         <code>false</code> otherwise
	 * 
	 * @throws NullPointerException
	 *             If <tt>date</tt> is <tt>null</tt>
	 */
	public boolean setLastModified(XdevDate date) throws NullPointerException
	{
		return setLastModified(date.getTimeInMillis());
	}
	
	
	/**
	 * This method is a alias for {@link #length()}.
	 * 
	 * @return The length, in bytes, of the file denoted by this abstract
	 *         pathname, or <code>0L</code> if the file does not exist. Some
	 *         operating systems may return <code>0L</code> for pathnames
	 *         denoting system-dependent entities such as devices or pipes.
	 * 
	 * @throws SecurityException
	 *             If a security manager exists and its
	 *             {@link java.lang.SecurityManager#checkRead(java.lang.String)}
	 *             method denies read access to the file
	 */
	public long getSize() throws SecurityException
	{
		return length();
	}
	
	
	/**
	 * Return the {@link XdevFile} of the parent directory.
	 * 
	 * @return a {@link XdevFile} of the parent directory
	 * 
	 * @throws NullPointerException
	 *             If the {@link XdevFile} does not have a parent directory
	 * 
	 * @see #getChildren()
	 */
	public XdevFile getParentXdevFile() throws NullPointerException
	{
		return new XdevFile(getParentFile());
	}
	
	
	/**
	 * Tests if this file is a parent or a parent's parent directory of
	 * <code>child</code>.
	 * 
	 * 
	 * @param child
	 *            the may-be-child
	 * @return true if this file is a parent or a parent's parent directory of
	 *         <code>child</code>, <code>false</code> otherwise
	 */
	public boolean isParentOf(XdevFile child)
	{
		File parent = child;
		while(parent != null)
		{
			if(equals(parent))
			{
				return true;
			}
			
			parent = parent.getParentFile();
		}
		
		return false;
	}
	
	
	/**
	 * Tests if <code>parent</code> is a parent or a parent's parent directory
	 * of this file.
	 * 
	 * 
	 * @param parent
	 *            the may-be-parent
	 * @return true if <code>parent</code> is a parent or a parent's parent
	 *         directory of this file, <code>false</code> otherwise
	 */
	public boolean isChildOf(XdevFile parent)
	{
		return parent.isParentOf(this);
	}
	
	
	/**
	 * Returns a sorted {@link XdevList} of child files of this folder.<br>
	 * The directory's parent directory are not included in the result.
	 * 
	 * 
	 * @return a {@link XdevList} of child files in that folder.
	 * 
	 * @throws SecurityException
	 *             If a security manager exists and its
	 *             {@link java.lang.SecurityManager#checkRead(java.lang.String)}
	 *             method denies read access to the directory
	 * 
	 * @see #getParentXdevFile()
	 */
	public XdevList<XdevFile> getChildren() throws SecurityException
	{
		XdevList<XdevFile> children = new XdevList<XdevFile>();
		
		File[] f = listFiles();
		if(f != null)
		{
			List<File> dirs = new ArrayList<File>();
			List<File> files = new ArrayList<File>();
			for(int i = 0; i < f.length; i++)
			{
				if(f[i].isFile())
				{
					files.add(f[i]);
				}
				else
				{
					dirs.add(f[i]);
				}
			}
			
			sort(dirs);
			for(int i = 0; i < dirs.size(); i++)
			{
				children.add(new XdevFile((File)dirs.get(i)));
			}
			dirs.clear();
			
			sort(files);
			for(int i = 0; i < files.size(); i++)
			{
				children.add(new XdevFile((File)files.get(i)));
			}
			files.clear();
		}
		
		return children;
	}
	
	private final static Comparator<File>	FileComparator;
	static
	{
		FileComparator = new Comparator<File>()
		{
			final boolean	caseSensitive	= IOUtils.isWindows();
			
			
			@Override
			public int compare(File f1, File f2)
			{
				boolean dir1 = f1.isDirectory();
				if(dir1 != f2.isDirectory())
				{
					return dir1 ? 1 : -1;
				}
				
				if(caseSensitive)
				{
					return f1.getName().compareTo(f2.getName());
				}
				else
				{
					return f1.getName().compareToIgnoreCase(f2.getName());
				}
			}
		};
	}
	
	
	/**
	 * Sorts a list of files in the underlying operating system's manner.
	 * 
	 * @param files
	 *            the files to sort
	 * 
	 * @since 3.1
	 */
	public static void sort(List<? extends File> files)
	{
		Collections.sort(files,FileComparator);
	}
	
	
	/**
	 * Atomically creates a new, empty file named by <code>this</code> abstract
	 * pathname if and only if a file with this name does not yet exist. The
	 * check for the existence of the file and the creation of the file if it
	 * does not exist are a single operation that is atomic with respect to all
	 * other filesystem activities that might affect the file.
	 * 
	 * @return <code>true</code> if the named file does not exist and was
	 *         successfully created; <code>false</code> if the named file
	 *         already exists or any problem occurred.
	 * 
	 * 
	 * @throws SecurityException
	 *             If a security manager exists and its
	 *             {@link java.lang.SecurityManager#checkWrite(java.lang.String)}
	 *             method denies write access to the file
	 * 
	 * @see #createNewFile()
	 */
	public boolean createFile() throws SecurityException
	{
		try
		{
			return createNewFile();
		}
		catch(IOException e)
		{
			return false;
		}
	}
	
	
	/**
	 * This method is a alias for {@link #mkdirs()}.
	 * 
	 * @return <code>true</code> if and only if the directory was created, along
	 *         with all necessary parent directories; <code>false</code>
	 *         otherwise
	 * 
	 * @throws SecurityException
	 *             If a security manager exists and its
	 *             {@link java.lang.SecurityManager#checkRead(java.lang.String)}
	 *             method does not permit verification of the existence of the
	 *             named directory and all necessary parent directories; or if
	 *             the
	 *             {@link java.lang.SecurityManager#checkWrite(java.lang.String)}
	 *             method does not permit the named directory and all necessary
	 *             parent directories to be created
	 */
	public boolean createDirectory() throws SecurityException
	{
		return mkdirs();
	}
	
	
	/**
	 * Name of a file, directory, or folder as it would be displayed in a system
	 * file browser. If the name of this {@link XdevFile} is not available, the
	 * name of this {@link XdevFile} is returned.
	 * 
	 * @return a {@link String} the file name as it would be displayed by a
	 *         native file chooser; <code>null</code> if name is not available
	 */
	public String getSystemDisplayName()
	{
		try
		{
			return getFileSystemView().getSystemDisplayName(this);
		}
		catch(Exception e)
		{
			return getName();
		}
	}
	
	
	/**
	 * Returns this file's size in a human readable form.
	 * 
	 * @return The human readable file size e.g: 1 KB
	 * 
	 * @since 3.1
	 */
	public String getSystemDisplaySize()
	{
		return IOUtils.getFileSize(this);
	}
	
	
	/**
	 * Icon for this {@link XdevFile} as it would be displayed in a system file
	 * browser. If the icon for this {@link XdevFile} is not available,
	 * <code>null</code> is returned.
	 * 
	 * @return an {@link Icon} as it would be displayed by a native file
	 *         chooser; <code>null</code> if icon is not available
	 */
	public Icon getSystemIcon()
	{
		try
		{
			return getFileSystemView().getSystemIcon(this);
		}
		catch(Exception e)
		{
			return null;
		}
	}
	
	
	/**
	 * Creates a new {@link XdevImage} with the system icon for this
	 * {@link XdevFile}.
	 * 
	 * @return a new {@link XdevImage} with the system icon for this
	 *         {@link XdevFile}; <code>null</code> if icon is not available
	 */
	public XdevImage getSystemImage()
	{
		Icon icon = getSystemIcon();
		if(icon != null)
		{
			return new XdevImage(GraphicUtils.createImageFromIcon(icon));
		}
		
		return null;
	}
	
	
	/**
	 * Type description for a file, directory, or folder as it would be
	 * displayed in a system file browser. Example from Windows: the "Desktop"
	 * folder is described as "Desktop".
	 * 
	 * @return the file type description as it would be displayed by a native
	 *         file chooser or <code>null</code> if no native information is
	 *         available.
	 */
	public String getSystemTypeDescription()
	{
		try
		{
			return getFileSystemView().getSystemTypeDescription(this);
		}
		catch(Exception e)
		{
			return null;
		}
	}
	
	
	/**
	 * Creates a new {@link Reader}, to read from this {@link XdevFile}.
	 * 
	 * @return a new {@link Reader}
	 * 
	 * @throws IOException
	 *             If the file does not exist, is a directory rather than a
	 *             regular file, or for some other reason cannot be opened for
	 *             reading.
	 */
	public Reader openReader() throws IOException
	{
		return new FileReader(this);
	}
	
	
	/**
	 * Creates a new {@link Reader}, to read from this {@link XdevFile}.
	 * 
	 * @param charsetName
	 *            The name of a supported {@link Charset}
	 * 
	 * @return a new {@link Reader}
	 * 
	 * @throws IOException
	 *             If the file does not exist, is a directory rather than a
	 *             regular file, or for some other reason cannot be opened for
	 *             reading.
	 * @throws UnsupportedEncodingException
	 *             If the named charset is not supported.
	 * 
	 * @since 3.1
	 */
	public Reader openReader(String charsetName) throws IOException, UnsupportedEncodingException
	{
		return new InputStreamReader(openInputStream(),charsetName);
	}
	
	
	/**
	 * Creates a new {@link Reader}, to read from this {@link XdevFile}, using
	 * the given <code>charset</code>.
	 * 
	 * @param charset
	 *            A {@link Charset}
	 * 
	 * @return a new {@link Reader}
	 * 
	 * @throws IOException
	 *             if the file does not exist, is a directory rather than a
	 *             regular file, or for some other reason cannot be opened for
	 *             reading.
	 * 
	 * @since 3.1
	 */
	public Reader openReader(Charset charset) throws IOException
	{
		return new InputStreamReader(openInputStream(),charset);
	}
	
	
	/**
	 * This method is a alias for <code>openWriter(false)</code>.
	 * 
	 * @return a new {@link Writer}
	 * 
	 * @throws IOException
	 *             if the file exists but is a directory rather than a regular
	 *             file, does not exist but cannot be created, or cannot be
	 *             opened for any other reason
	 * 
	 * @see #openWriter(boolean)
	 */
	public Writer openWriter() throws IOException
	{
		return openWriter(false);
	}
	
	
	/**
	 * Constructs a {@link Writer} object to write on this {@link XdevFile}. If
	 * <code>append</code> is <code>true</code>, then bytes will be written to
	 * the end of the file rather than the beginning.
	 * 
	 * @param append
	 *            if <code>true</code>, then bytes will be written to the end of
	 *            the file rather than the beginning
	 * 
	 * @return a {@link Writer} object to write on this {@link XdevFile}
	 * 
	 * @throws IOException
	 *             if the file exists but is a directory rather than a regular
	 *             file, does not exist but cannot be created, or cannot be
	 *             opened for any other reason
	 */
	public Writer openWriter(boolean append) throws IOException
	{
		return new FileWriter(this,append);
	}
	
	
	/**
	 * Constructs a {@link Writer} object to write on this {@link XdevFile}
	 * using the given charset.
	 * 
	 * @param charsetName
	 *            The name of a supported {@link Charset}
	 * 
	 * @return a {@link Writer} object to write on this {@link XdevFile}
	 * 
	 * @throws IOException
	 *             if the file exists but is a directory rather than a regular
	 *             file, does not exist but cannot be created, or cannot be
	 *             opened for any other reason
	 * @throws UnsupportedEncodingException
	 *             If the named charset is not supported.
	 * 
	 * @since 3.1
	 */
	public Writer openWriter(String charsetName) throws IOException, UnsupportedEncodingException
	{
		return new OutputStreamWriter(openOutputStream(),charsetName);
	}
	
	
	/**
	 * Constructs a {@link Writer} object to write on this {@link XdevFile}
	 * using the given charset. If <code>append</code> is <code>true</code>,
	 * then bytes will be written to the end of the file rather than the
	 * beginning.
	 * 
	 * @param append
	 *            if <code>true</code>, then bytes will be written to the end of
	 *            the file rather than the beginning
	 * @param charsetName
	 *            The name of a supported {@link Charset}
	 * 
	 * @return a {@link Writer} object to write on this {@link XdevFile}
	 * 
	 * @throws IOException
	 *             if the file exists but is a directory rather than a regular
	 *             file, does not exist but cannot be created, or cannot be
	 *             opened for any other reason
	 * @throws UnsupportedEncodingException
	 *             If the named charset is not supported.
	 * 
	 * @since 3.1
	 */
	public Writer openWriter(boolean append, String charsetName) throws IOException,
			UnsupportedEncodingException
	{
		return new OutputStreamWriter(openOutputStream(append),charsetName);
	}
	
	
	/**
	 * Constructs a {@link Writer} object to write on this {@link XdevFile}
	 * using the given charset.
	 * 
	 * @param charset
	 *            A {@link Charset}
	 * 
	 * @return a {@link Writer} object to write on this {@link XdevFile}
	 * 
	 * @throws IOException
	 *             if the file exists but is a directory rather than a regular
	 *             file, does not exist but cannot be created, or cannot be
	 *             opened for any other reason
	 * 
	 * @since 3.1
	 */
	public Writer openWriter(Charset charset) throws IOException
	{
		return new OutputStreamWriter(openOutputStream(),charset);
	}
	
	
	/**
	 * Constructs a {@link Writer} object to write on this {@link XdevFile}
	 * using the given charset. If <code>append</code> is <code>true</code>,
	 * then bytes will be written to the end of the file rather than the
	 * beginning.
	 * 
	 * @param append
	 *            if <code>true</code>, then bytes will be written to the end of
	 *            the file rather than the beginning
	 * @param charset
	 *            A {@link Charset}
	 * 
	 * @return a {@link Writer} object to write on this {@link XdevFile}
	 * 
	 * @throws IOException
	 *             if the file exists but is a directory rather than a regular
	 *             file, does not exist but cannot be created, or cannot be
	 *             opened for any other reason
	 * 
	 * @since 3.1
	 */
	public Writer openWriter(boolean append, Charset charset) throws IOException
	{
		return new OutputStreamWriter(openOutputStream(append),charset);
	}
	
	
	/**
	 * Creates a {@link InputStream} by opening a connection to this
	 * {@link XdevFile}. A new {@link FileDescriptor} object is created to
	 * represent this file connection.
	 * <p>
	 * First, if there is a security manager, its
	 * {@link SecurityManager#checkRead(String)} method is called with the path
	 * represented by the this {@link XdevFile} argument as its argument.
	 * <p>
	 * If the named file does not exist, is a directory rather than a regular
	 * file, or for some other reason cannot be opened for reading then a
	 * {@link FileNotFoundException} is thrown.
	 * 
	 * @return a new {@link InputStream}
	 * 
	 * @exception FileNotFoundException
	 *                if the file does not exist, is a directory rather than a
	 *                regular file, or for some other reason cannot be opened
	 *                for reading.
	 * @exception SecurityException
	 *                if a security manager exists and its
	 *                {@link SecurityManager#checkRead(String)} method denies
	 *                read access to the file.
	 * 
	 * @see InputStream
	 * @see FileInputStream
	 */
	public InputStream openInputStream() throws FileNotFoundException, SecurityException
	{
		return new FileInputStream(this);
	}
	
	
	/**
	 * This method is a alias for <code>openOutputStream(false)</code>.
	 * 
	 * @return a new {@link OutputStream} to write into this {@link XdevFile}
	 * 
	 * @exception FileNotFoundException
	 *                if the file exists but is a directory rather than a
	 *                regular file, does not exist but cannot be created, or
	 *                cannot be opened for any other reason
	 * 
	 * @exception SecurityException
	 *                if a security manager exists and its
	 *                {@link SecurityManager#checkWrite(String)} method denies
	 *                write access to the file.
	 * 
	 * @see #openOutputStream(boolean)
	 */
	public OutputStream openOutputStream() throws FileNotFoundException, SecurityException
	{
		return openOutputStream(false);
	}
	
	
	/**
	 * Creates {@link OutputStream} to write to the file represented by the
	 * specified {@link XdevFile} object. If the second argument is
	 * <code>true</code>, then bytes will be written to the end of the file
	 * rather than the beginning. A new {@link FileDescriptor} object is created
	 * to represent this file connection.
	 * <p>
	 * First, if there is a security manager, its
	 * {@link SecurityManager#checkWrite(String)} method is called with the path
	 * represented by the {@link XdevFile} argument as its argument.
	 * <p>
	 * If the file exists but is a directory rather than a regular file, does
	 * not exist but cannot be created, or cannot be opened for any other reason
	 * then a {@link FileNotFoundException} is thrown.
	 * 
	 * @param append
	 *            if <code>true</code>, then bytes will be written to the end of
	 *            the file rather than the beginning
	 * 
	 * @return a new {@link OutputStream} to write into the {@link XdevFile}
	 * 
	 * @throws FileNotFoundException
	 *             if the file exists but is a directory rather than a regular
	 *             file, does not exist but cannot be created, or cannot be
	 *             opened for any other reason
	 * 
	 * @throws SecurityException
	 *             if a security manager exists and its
	 *             {@link SecurityManager#checkWrite(String)} method denies
	 *             write access to the file.
	 * 
	 * @see FileOutputStream
	 */
	public OutputStream openOutputStream(boolean append) throws FileNotFoundException,
			SecurityException
	{
		return new FileOutputStream(this,append);
	}
	
	
	/**
	 * Reads the contents of this file into a byte array.
	 * 
	 * @return The file's content as a byte array.
	 * @throws IOException
	 *             if an I/O error occurs
	 * 
	 * @since 3.1
	 */
	public byte[] readData() throws IOException
	{
		return IOUtils.readData(this);
	}
	
	
	/**
	 * Reads from this file and stores results in a string.
	 * 
	 * 
	 * @return a string containing the read characters.
	 * 
	 * @since 3.1
	 */
	public String readString() throws IOException
	{
		return IOUtils.readString(this);
	}
	
	
	/**
	 * Reads from this file and stores results in a string.<br>
	 * 
	 * @param charsetName
	 *            a name of a character set to use
	 * 
	 * @return a string containing the read characters.
	 * 
	 * @throws IOException
	 *             if an I/O error occurs
	 * 
	 * @since 3.1
	 */
	public String readString(String charsetName) throws IOException
	{
		return IOUtils.readString(openInputStream(),charsetName);
	}
	
	
	/**
	 * Copies this file to <code>dest</code>.<br>
	 * <br>
	 * If this file is a directory:
	 * <ul>
	 * <li><code>dest</code> has to be a directory or an <code>IOExeption</code>
	 * is thrown</li>
	 * <li>a new directory with <code>src</code>'s name is created in
	 * <code>dest</code> and this file's content is copied to the new directory</li>
	 * </ul>
	 * else if this is a file:
	 * <ul>
	 * <li>if <code>dest</code> is a directory, this file is copied to
	 * <code>dest</code> to a file with the same name
	 * <li>else if <code>dest</code> is a file, <code>dest</code> is overwritten
	 * with this file's content</li>
	 * </ul>
	 * 
	 * @param dest
	 *            the destination file or directory
	 * @return the destination file
	 * @throws IOException
	 *             if an I/O error occurs
	 * 
	 * @since 3.1
	 */
	public XdevFile copyTo(File dest) throws IOException
	{
		return new XdevFile(IOUtils.copy(this,dest));
	}
	
	
	/**
	 * Creates a hash value of this file using the specified algorithm.
	 * 
	 * @param algorithm
	 *            the name of the hash algorithm to use. See Appendix A in the
	 *            <a href=
	 *            "../../../technotes/guides/security/crypto/CryptoSpec.html#AppA"
	 *            > Java Cryptography Architecture API Specification &amp;
	 *            Reference </a> for information about standard algorithm names.
	 * @return a 32 charaters long hexadecimal {@link String}.
	 */
	public String createHash(String algorithm) throws IOException
	{
		try
		{
			FileInputStream fin = new FileInputStream(this);
			String hash = IOUtils.createHash(fin,algorithm);
			fin.close();
			
			return hash;
		}
		catch(Exception e)
		{
			throw new IOException(e.getMessage());
		}
	}
	
	private static FileSystemView	fsv;
	
	
	/**
	 * Returns the gateway to the file system.
	 * 
	 * @return the gateway to the file system.
	 */
	public static FileSystemView getFileSystemView()
	{
		if(fsv == null)
		{
			fsv = FileSystemView.getFileSystemView();
		}
		
		return fsv;
	}
	
	
	/**
	 * Returns all root files on this system.
	 * <p>
	 * 
	 * <pre>
	 * XdevFile[]	roots	= XdevFile.getSystemRoots();
	 * </pre>
	 * 
	 * </p>
	 * 
	 * @return all root files on this system. The values are dependent of the
	 *         operating system.
	 * 
	 * @since 3.1
	 */
	public static XdevFile[] getSystemRoots()
	{
		List<XdevFile> list = new ArrayList();
		
		File[] roots = getFileSystemView().getRoots();
		if(roots != null)
		{
			for(int i = 0; i < roots.length; i++)
			{
				list.add(new XdevFile(roots[i]));
			}
		}
		
		return list.toArray(new XdevFile[list.size()]);
	}
}
