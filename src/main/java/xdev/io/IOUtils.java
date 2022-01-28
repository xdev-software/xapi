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


import java.awt.Image;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.CharArrayWriter;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.math.BigInteger;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipFile;

import javax.swing.filechooser.FileSystemView;

import xdev.Application;
import xdev.lang.LibraryMember;
import xdev.lang.NotNull;
import xdev.net.NetUtils;
import xdev.ui.DesktopUtils;
import xdev.ui.XdevImage;


/**
 * The class <code>IOUtils</code> provides utility methods for IO related tasks.
 * 
 * @since 2.0
 * @author XDEV Software
 * 
 */
@LibraryMember
public final class IOUtils
{
	private IOUtils()
	{
	}
	
	
	
	/**
	 * Growing byte array
	 */
	private static class ByteBuffer
	{
		private byte[]	data;
		private int		offset;
		
		
		public ByteBuffer(int initialCapacity)
		{
			data = new byte[initialCapacity];
			offset = 0;
		}
		
		
		public void add(byte[] b, int length)
		{
			if(offset + length >= data.length)
			{
				byte[] newData = new byte[(data.length + length) * 2];
				System.arraycopy(data,0,newData,0,offset);
				data = newData;
			}
			System.arraycopy(b,0,data,offset,length);
			offset += length;
		}
		
		
		public byte[] get()
		{
			byte[] b = new byte[offset];
			System.arraycopy(data,0,b,0,offset);
			return b;
		}
	}
	
	/**
	 * the operating system specific line separator.
	 */
	public static final String		LINE_SEPARATOR		= System.getProperty("line.separator");
	
	/**
	 * an empty byte array.
	 */
	public final static byte[]		EMPTY_BYTE_ARRAY	= new byte[0];
	
	/**
	 * one megabyte.
	 */
	public final static long		ONE_MEGABYTE		= 1024 * 1024;
	
	/**
	 * the Default buffer size.
	 */
	public final static int			DEFAULT_BUFFER_SIZE	= 1024 * 4;
	
	private static boolean			isWindows;
	private static boolean			isWindows9x;
	private static boolean			isMac;
	private static boolean			isLinux;
	private static boolean			isSolaris;
	
	private static FileSystemView	fileSystemView;
	private static NumberFormat		numberFormat;
	
	static
	{
		isWindows = isWindows9x = isMac = isLinux = isSolaris = false;
		String system = System.getProperty("os.name").toLowerCase();
		if(system.indexOf("windows") >= 0)
		{
			isWindows = true;
			if(system.indexOf("95") >= 0 || system.indexOf("98") >= 0)
			{
				isWindows9x = true;
			}
		}
		else if(system.indexOf("macintosh") >= 0 || system.indexOf("mac os") >= 0)
		{
			isMac = true;
		}
		else if(system.indexOf("solaris") >= 0 || system.indexOf("sun os") >= 0
				|| system.indexOf("sunos") >= 0)
		{
			isSolaris = true;
		}
		else
		{
			isLinux = true;
		}
		
		
		
		numberFormat = NumberFormat.getNumberInstance();
		numberFormat.setMaximumFractionDigits(2);
		numberFormat.setMinimumFractionDigits(0);
	}
	
	
	/**
	 * @return true, if operating system is Windows
	 */
	public static boolean isWindows()
	{
		return isWindows;
	}
	
	
	/**
	 * @return true, if operating system is Windows9x
	 */
	public static boolean isWindows9x()
	{
		return isWindows9x;
	}
	
	
	/**
	 * @return true, if operating system is Mac OS
	 */
	public static boolean isMac()
	{
		return isMac;
	}
	
	
	/**
	 * @return true, if operating system is Linux
	 */
	public static boolean isLinux()
	{
		return isLinux;
	}
	
	
	/**
	 * @return true, if operating system is Solaris
	 */
	public static boolean isSolaris()
	{
		return isSolaris;
	}
	
	/**
	 * Int-Identifier for a Windows Operating System.
	 */
	public final static int	WINDOWS	= 0;
	/**
	 * Int-Identifier for a Mac Operating System.
	 */
	public final static int	MAC		= 1;
	/**
	 * Int Identifier for a Unix Operating System.
	 */
	public final static int	UNIX	= 2;
	
	
	/**
	 * Returns the type of operating system this application is running on.
	 * 
	 * @return Int-Identifier for the operating system
	 * @see IOUtils#WINDOWS
	 * @see IOUtils#MAC
	 * @see IOUtils#UNIX
	 */
	public final static int getOS()
	{
		String os = System.getProperty("os.name").toUpperCase();
		
		if(os.indexOf("WINDOWS") >= 0)
		{
			return WINDOWS;
		}
		else if(os.indexOf("MACINTOSH") >= 0)
		{
			return MAC;
		}
		else
		{
			return UNIX;
		}
	}
	
	
	/**
	 * Reads from a {@link InputStream} and returns the results as a byte array.
	 * 
	 * @param in
	 *            {@link InputStream} to read from
	 * 
	 * @return byte array of read data
	 * 
	 * @throws IOException
	 *             if an I/O error occurs
	 * 
	 * @see #readData(InputStream, int)
	 */
	public static byte[] readData(InputStream in) throws IOException
	{
		try
		{
			ByteBuffer bb = new ByteBuffer(1024);
			byte[] b = new byte[1024];
			int read = in.read(b,0,1024);
			bb.add(b,read);
			while(read >= 0)
			{
				read = in.read(b,0,1024);
				if(read >= 0)
				{
					bb.add(b,read);
				}
			}
			
			return bb.get();
		}
		catch(IOException e)
		{
			throw new IOException(e);
		}
	}
	
	
	/**
	 * Reads from a {@link InputStream} and returns the results as a byte array.
	 * 
	 * @param in
	 *            stream to read from
	 * @param len
	 *            the number of bytes read from the stream at once
	 * 
	 * @return byte array of read data
	 * 
	 * @throws IOException
	 *             if an I/O error occurs
	 */
	public static byte[] readData(InputStream in, int len) throws IOException
	{
		try
		{
			ByteBuffer bb = new ByteBuffer(len);
			byte[] b = new byte[len];
			int read = in.read(b,0,len);
			bb.add(b,read);
			while(read < len)
			{
				int i = in.read(b,0,len - read);
				if(i >= 0)
				{
					bb.add(b,i);
					read += i;
				}
			}
			
			return bb.get();
		}
		catch(IOException e)
		{
			throw new IOException(e);
		}
	}
	
	
	/**
	 * Reads the contents of the file <code>f</code> into a byte array.
	 * 
	 * @param f
	 *            The file to be read
	 * @return The file's content as a byte array.
	 * @throws IOException
	 *             if an I/O error occurs
	 * 
	 * @since 3.1
	 */
	public static byte[] readData(File f) throws IOException
	{
		FileInputStream in = new FileInputStream(f);
		try
		{
			return readData(in,(int)f.length());
		}
		finally
		{
			closeSilent(in);
		}
	}
	
	
	/**
	 * Reads from a specified {@link InputStream} and stores results in a
	 * string.<br>
	 * The stream gets closed afterwards. <br>
	 * This method is a alias for
	 * <code>IOUtils.readString(InputStream is, boolean close)</code>
	 * 
	 * @param is
	 *            a implementation of the interface {@link InputStream}
	 * 
	 * @return a string containing the read characters.
	 * 
	 * @throws IOException
	 *             if an I/O error occurs
	 * 
	 * @see #readString(InputStream, boolean)
	 */
	public static String readString(InputStream is) throws IOException
	{
		return readString(is,true);
	}
	
	
	/**
	 * Reads from a specified {@link InputStream} and stores results in a
	 * string.<br>
	 * This method is a alias for
	 * <code>IOUtils.readString(Reader r, boolean close)</code>
	 * 
	 * @param is
	 *            a implementation of the interface {@link InputStream}
	 * @param close
	 *            if true stream gets closed afterwards
	 * 
	 * @return a string containing the read characters.
	 * 
	 * @throws IOException
	 *             if an I/O error occurs
	 * 
	 * @see #readString(Reader, boolean)
	 */
	public static String readString(InputStream is, boolean close) throws IOException
	{
		return readString(new InputStreamReader(is),close);
	}
	
	
	/**
	 * Reads from a specified {@link InputStream} and stores results in a
	 * string.<br>
	 * The stream gets closed afterwards.<br>
	 * This method is a alias for
	 * <code>IOUtils.readString(Reader r, boolean close)</code>
	 * 
	 * @param is
	 *            a implementation of the interface {@link InputStream}
	 * @param charsetName
	 *            a name of a character set to use
	 * 
	 * @return a string containing the read characters.
	 * 
	 * @throws IOException
	 *             if an I/O error occurs
	 * 
	 * @see #readString(Reader, boolean)
	 */
	public static String readString(InputStream is, String charsetName) throws IOException
	{
		return readString(is,charsetName,true);
	}
	
	
	/**
	 * Reads from a specified {@link InputStream} and stores results in a
	 * string.<br>
	 * This method is a alias for
	 * <code>IOUtils.readString(Reader r, boolean close)</code>
	 * 
	 * @param is
	 *            a implementation of the interface {@link InputStream}
	 * @param charsetName
	 *            a name of a character set to use
	 * @param close
	 *            if true, reader gets closed afterwards.
	 * @return a string containing the read characters.
	 * @throws IOException
	 *             if an I/O error occurs
	 * @see #readString(InputStream, boolean)
	 */
	public static String readString(InputStream is, String charsetName, boolean close)
			throws IOException
	{
		return readString(new InputStreamReader(is,charsetName),close);
	}
	
	
	/**
	 * Reads from a specified {@link File} and stores results in a string. The
	 * reader gets closed afterwards.<br>
	 * This method is a alias for <code>IOUtils.readString(Reader r)</code>
	 * 
	 * @param f
	 *            a {@link File} to read from
	 * 
	 * @return a string containing the read characters.
	 * 
	 * @throws IOException
	 *             if an I/O error occurs
	 * 
	 * @see #readString(Reader)
	 */
	public static String readString(File f) throws IOException
	{
		return readString(new FileReader(f));
	}
	
	
	/**
	 * Reads from a specified reader and stores results in a string. The reader
	 * gets closed afterwards.<br>
	 * This method is a alias for
	 * <code>IOUtils.readString(Reader r, boolean close)</code>
	 * 
	 * @param in
	 *            a implementation of the interface {@link Reader}
	 * 
	 * @return a string containing the read characters.
	 * 
	 * @throws IOException
	 *             if an I/O error occurs
	 * 
	 * @see #readString(Reader, boolean)
	 */
	public static String readString(Reader in) throws IOException
	{
		return readString(in,true);
	}
	
	
	/**
	 * Reads from a specified reader and stores results in a string.
	 * <p>
	 * 
	 * <pre>
	 * FileReader	reader	= new FileReader(&quot;customer.txt&quot;);
	 * 														String	result	= IOUtils.readString(
	 * 																				reader,false);
	 * </pre>
	 * 
	 * Creates a new FileReader <code>reader</code>, then the file is read and
	 * its contents are stored in <code>result</code>.
	 * </p>
	 * 
	 * @param in
	 *            a implementation of the interface {@link Reader}
	 * @param close
	 *            if true, reader gets closed afterwards.
	 * @return a string containing the read characters.
	 * @throws IOException
	 *             if an I/O error occurs
	 */
	public static String readString(Reader in, boolean close) throws IOException
	{
		CharArrayWriter out = new CharArrayWriter(DEFAULT_BUFFER_SIZE);
		copy(in,out,close);
		return out.toString();
	}
	
	
	/**
	 * Reads from a specified {@link InputStream} and stores results in a
	 * character array. Stream is closed afterwards. <br>
	 * This method is a alias for
	 * <code>IOUtils.readChars(InputStream is, boolean close)</code>
	 * 
	 * @param is
	 *            a implementation of the interface {@link InputStream}
	 * @return a char[] containing the read characters.
	 * @throws IOException
	 *             if an I/O error occurs
	 */
	public static char[] readChars(InputStream is) throws IOException
	{
		return readChars(is,true);
	}
	
	
	/**
	 * Reads from a specified {@link InputStream} and stores results in a
	 * character array. <br>
	 * This method is a alias for
	 * <code>IOUtils.readChars(Reader r, boolean close)</code>
	 * 
	 * @param is
	 *            a implementation of the interface {@link InputStream}
	 * @param close
	 *            if true, reader gets closed afterwards.
	 * @return a char[] containing the read characters.
	 * @throws IOException
	 *             if an I/O error occurs
	 */
	public static char[] readChars(InputStream is, boolean close) throws IOException
	{
		return readChars(new InputStreamReader(is),close);
	}
	
	
	/**
	 * Reads from a specified {@link InputStream} and stores results in a
	 * character array. <br>
	 * The stream gets closed afterwards. <br>
	 * This method is a alias for
	 * <code>IOUtils.readChars(is,charsetName,true)</code>
	 * 
	 * @param is
	 *            a implementation of the interface {@link InputStream}
	 * @param charsetName
	 *            the name of the character set to use
	 * @return a char[] containing the read characters.
	 * @throws IOException
	 *             if an I/O error occurs
	 */
	public static char[] readChars(InputStream is, String charsetName) throws IOException
	{
		return readChars(is,charsetName,true);
	}
	
	
	/**
	 * Reads from a specified {@link InputStream} and stores results in a
	 * character array. <br>
	 * This method is a alias for
	 * <code>IOUtils.readChars(Reader in, boolean close)</code>
	 * 
	 * @param is
	 *            a implementation of the interface {@link InputStream}
	 * @param charsetName
	 *            the name of the character set to use
	 * @param close
	 *            if true, reader gets closed afterwards.
	 * @return a char[] containing the read characters.
	 * @throws IOException
	 *             if an I/O error occurs
	 */
	public static char[] readChars(InputStream is, String charsetName, boolean close)
			throws IOException
	{
		return readChars(new InputStreamReader(is,charsetName),close);
	}
	
	
	/**
	 * Reads from a specified {@link InputStream} and stores results in a
	 * character array. The reader gets closed afterwards. <br>
	 * This method is a alias for <code>IOUtils.readChars(Reader r)</code>.
	 * 
	 * @param f
	 *            a {@link File} to read from
	 * @return a char[] containing the read characters.
	 * @throws IOException
	 *             if an I/O error occurs
	 */
	public static char[] readChars(File f) throws IOException
	{
		return readChars(new FileReader(f));
	}
	
	
	/**
	 * Reads from a specified {@link InputStream} and stores results in a
	 * character array. The reader gets closed afterwards. <br>
	 * This method is a alias for
	 * <code>IOUtils.readChars(Reader r, boolean close)</code>
	 * 
	 * @param in
	 *            a implementation of the interface {@link Reader}
	 * @return a char[] containing the read characters.
	 * @throws IOException
	 *             if an I/O error occurs
	 */
	public static char[] readChars(Reader in) throws IOException
	{
		return readChars(in,true);
	}
	
	
	/**
	 * Reads from a specified reader and stores results in a character array.
	 * <p>
	 * 
	 * <pre>
	 * FileReader	reader	= new FileReader(&quot;customer.txt&quot;);
	 * 														char[]	result	= IOUtils.readChars(reader,
	 * 																				false);
	 * </pre>
	 * 
	 * Creates a new FileReader <code>reader</code> and
	 * </p>
	 * 
	 * @param in
	 *            a implementation of the interface {@link Reader}
	 * @param close
	 *            if true, reader gets closed afterwards.
	 * @return a char[] containing the read characters.
	 * @throws IOException
	 *             if an I/O error occurs
	 */
	public static char[] readChars(Reader in, boolean close) throws IOException
	{
		CharArrayWriter out = new CharArrayWriter(DEFAULT_BUFFER_SIZE);
		copy(in,out,close);
		return out.toCharArray();
	}
	
	
	/**
	 * Copies in to out and closes both writers.
	 * <p>
	 * This method is a alias for <code>IOUtils.copy(in,out,true);</code>
	 * </p>
	 * 
	 * @param in
	 *            the {@link Reader} to read from
	 * @param out
	 *            the {@link Writer} to write to
	 * @return the number of bytes written
	 * @throws IOException
	 *             if an I/O error occurs
	 */
	public static int copy(Reader in, Writer out) throws IOException
	{
		return copy(in,out,true);
	}
	
	
	/**
	 * Copies in to out and closes both reader and writer if <code>close</code>
	 * is <code>true</code>.
	 * 
	 * @param in
	 *            the {@link Reader} to read from
	 * @param out
	 *            the {@link Writer} to write to
	 * @param close
	 *            if true reader in and writer out get closed
	 * @return the number of bytes written
	 * @throws IOException
	 *             if an I/O error occurs
	 */
	public static int copy(Reader in, Writer out, boolean close) throws IOException
	{
		try
		{
			char[] buffer = new char[DEFAULT_BUFFER_SIZE];
			int count = 0;
			int n = 0;
			while(-1 != (n = in.read(buffer)))
			{
				out.write(buffer,0,n);
				count += n;
			}
			return count;
		}
		finally
		{
			if(close)
			{
				closeSilent(in);
				closeSilent(out);
			}
		}
	}
	
	
	/**
	 * Copies in to out and closes both streams.
	 * <p>
	 * This method is a alias for <code>IOUtils.copy(in,out,true);</code>
	 * </p>
	 * 
	 * @param in
	 *            the {@link InputStream} to read from
	 * @param out
	 *            the {@link OutputStream} to write to
	 * @return the number of bytes written
	 * @throws IOException
	 *             if an I/O error occurs
	 * @since 3.1
	 */
	public static int copy(InputStream in, OutputStream out) throws IOException
	{
		return copy(in,out,true);
	}
	
	
	/**
	 * Copies in to out and closes both streams if <code>close</code> is
	 * <code>true</code>.
	 * 
	 * @param in
	 *            the {@link InputStream} to read from
	 * @param out
	 *            the {@link OutputStream} to write to
	 * @param close
	 *            if true streams will be closed
	 * @return the number of bytes written
	 * @throws IOException
	 *             if an I/O error occurs
	 * @since 3.1
	 */
	public static int copy(InputStream in, OutputStream out, boolean close) throws IOException
	{
		try
		{
			byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
			int count = 0;
			int n = 0;
			while(-1 != (n = in.read(buffer)))
			{
				out.write(buffer,0,n);
				count += n;
			}
			return count;
		}
		finally
		{
			if(close)
			{
				closeSilent(in);
				closeSilent(out);
			}
		}
	}
	
	
	/**
	 * Copies <code>src</code> to <code>dest</code>.<br>
	 * <br>
	 * If <code>src</code> is a directory:
	 * <ul>
	 * <li><code>dest</code> has to be a directory or an <code>IOExeption</code>
	 * is thrown</li>
	 * <li>a new directory with <code>src</code>'s name is created in
	 * <code>dest</code> and <code>src</code>'s content is copied to the new
	 * directory</li>
	 * </ul>
	 * else if <code>src</code> is a file:
	 * <ul>
	 * <li>if <code>dest</code> is a directory, <code>src</code> is copied to
	 * <code>dest</code> to a file with the same name
	 * <li>else if <code>dest</code> is a file, <code>dest</code> is overwritten
	 * with <code>src</code>'s content</li>
	 * </ul>
	 * 
	 * @param src
	 *            the source file or directory
	 * @param dest
	 *            the destination file or directory
	 * @return the destination file
	 * @throws IOException
	 *             if an I/O error occurs
	 * 
	 * @since 3.1
	 */
	public static File copy(File src, File dest) throws IOException
	{
		if(src.isDirectory())
		{
			ensureDir(dest);
			
			dest = new File(dest,src.getName());
			ensureDir(dest);
			
			File[] children = src.listFiles();
			if(children != null)
			{
				for(File child : children)
				{
					copy(child,dest);
				}
			}
		}
		else
		{
			if(dest.isDirectory())
			{
				dest = new File(dest,src.getName());
			}
			
			FileInputStream in = null;
			FileOutputStream out = null;
			
			try
			{
				in = new FileInputStream(src);
				out = new FileOutputStream(dest);
				
				FileChannel inChannel = in.getChannel();
				FileChannel outChannel = out.getChannel();
				
				inChannel.transferTo(0,inChannel.size(),outChannel);
			}
			finally
			{
				closeSilent(in);
				closeSilent(out);
			}
			
			dest.setLastModified(src.lastModified());
		}
		
		return dest;
	}
	
	
	/**
	 * Ensures that <code>dir</code> is a existing directory, it will be created
	 * if it doesn't exist.
	 * 
	 * @param dir
	 * @throws IOException
	 *             if <code>dir</code> exists and is not a directoy or if the
	 *             directory could not be created
	 * 
	 * @since 3.1
	 */
	public static void ensureDir(File dir) throws IOException
	{
		if(!dir.exists())
		{
			if(!dir.mkdirs())
			{
				throw new IOException("directory \"" + dir.getAbsolutePath()
						+ "\" could not be created");
			}
		}
		else if(!dir.isDirectory())
		{
			throw new IOException("\"" + dir.getAbsolutePath() + "\" is not a directory");
		}
	}
	
	
	/**
	 * Creates a hash value for a {@link String} object using the specified
	 * algorithm.
	 * 
	 * @param str
	 *            the {@link String} to create the hash value for.
	 * @param algorithm
	 *            the name of the hash algorithm to use. See Appendix A in the
	 *            <a href=
	 *            "../../../technotes/guides/security/crypto/CryptoSpec.html#AppA"
	 *            > Java Cryptography Architecture API Specification &amp;
	 *            Reference </a> for information about standard algorithm names.
	 * @return a 32 charaters long hexadecimal {@link String}.
	 * @throws NoSuchAlgorithmException
	 *             if an invalid algorithm was specified
	 */
	public static String createHash(@NotNull String str, String algorithm)
			throws NoSuchAlgorithmException, NullPointerException
	{
		try
		{
			return createHash(str.getBytes("UTF8"),algorithm);
		}
		catch(UnsupportedEncodingException e)
		{
			return createHash(str.getBytes(),algorithm);
		}
	}
	
	
	/**
	 * Creates a hash value for an array of <code>bytes</code> using the
	 * specified algorithm.
	 * 
	 * @param data
	 *            the byte array to create the hash value for.
	 * @param algorithm
	 *            the name of the hash algorithm to use. See Appendix A in the
	 *            <a href=
	 *            "../../../technotes/guides/security/crypto/CryptoSpec.html#AppA"
	 *            > Java Cryptography Architecture API Specification &amp;
	 *            Reference </a> for information about standard algorithm names.
	 * @return a 32 charaters long hexadecimal {@link String}.
	 * @throws NoSuchAlgorithmException
	 *             if an invalid algorithm was specified
	 */
	public static String createHash(byte[] data, String algorithm) throws NoSuchAlgorithmException
	{
		return toHashString(MessageDigest.getInstance(algorithm).digest(data),algorithm);
	}
	
	
	/**
	 * Creates a hash value an {@link InputStream} using the specified
	 * algorithm.
	 * 
	 * @param in
	 *            the {@link InputStream} to build the hash value for.
	 * @param algorithm
	 *            the name of the hash algorithm to use. See Appendix A in the
	 *            <a href=
	 *            "../../../technotes/guides/security/crypto/CryptoSpec.html#AppA"
	 *            > Java Cryptography Architecture API Specification &amp;
	 *            Reference </a> for information about standard algorithm names.
	 * @return a 32 charaters long hexadecimal {@link String}.
	 * @throws NoSuchAlgorithmException
	 *             if an invalid algorithm was specified
	 */
	public static String createHash(InputStream in, String algorithm)
			throws NoSuchAlgorithmException, IOException
	{
		MessageDigest md = MessageDigest.getInstance(algorithm);
		
		byte[] buffer = new byte[1024];
		int read = in.read(buffer);
		while(read != -1)
		{
			md.update(buffer,0,read);
			read = in.read(buffer);
		}
		
		return toHashString(md.digest(),algorithm);
	}
	
	
	/**
	 * Converts the digest into a proper hash string.
	 * 
	 * @param digest
	 *            the fingerprint
	 * @param algorithm
	 *            e.g. SHA, MD5, ...
	 * @return The hash string for the specified digest.
	 */
	public static String toHashString(byte[] digest, String algorithm)
	{
		if(algorithm.equalsIgnoreCase("MD5"))
		{
			return String.format("%1$032x",new BigInteger(1,digest));
		}
		else if(algorithm.equalsIgnoreCase("SHA") || algorithm.startsWith("SHA-"))
		{
			StringBuffer sb = new StringBuffer();
			for(int i = 0, c = digest.length; i < c; i++)
			{
				sb.append(Integer.toString((digest[i] & 0xff) + 0x100,16).substring(1));
			}
			return sb.toString();
		}
		else
		{
			return new BigInteger(1,digest).toString(16);
		}
	}
	
	
	/**
	 * Returns all root files on this system.
	 * <p>
	 * 
	 * <pre>
	 * XdevFile[]	roots	= IOUtils.getSystemRoots();
	 * </pre>
	 * 
	 * </p>
	 * <p>
	 * This is a synonym for {@link XdevFile#getSystemRoots()}
	 * 
	 * @return all root files on this system. The values are dependent of the
	 *         operating system.
	 */
	public static XdevFile[] getSystemRoots()
	{
		return XdevFile.getSystemRoots();
	}
	
	
	/**
	 * Returns a string from the system clipboard.
	 * 
	 * <pre>
	 * String	string	= getClipboardString();
	 * </pre>
	 * 
	 * @return the {@link String} from the system clipboard or NULL if no
	 *         {@link String} is present
	 * 
	 * @see #getClipboardFileList()
	 * @see #getClipboardImage()
	 * @see #putClipboardString(String)
	 */
	public static String getClipboardString()
	{
		try
		{
			Object o = getSysClipboardContent(DataFlavor.stringFlavor);
			if(o != null)
			{
				return o.toString();
			}
		}
		catch(Exception e)
		{
		}
		
		return null;
	}
	
	
	/**
	 * Returns a image from the system clipboard.
	 * 
	 * <pre>
	 * XdevImage	img	= getClipboardImage();
	 * </pre>
	 * 
	 * @return the {@link XdevImage} from the system clipboard or NULL if no
	 *         image is present
	 * @see #getClipboardFileList()
	 * @see #getClipboardString()
	 * @see #putClipboardImage(Image)
	 */
	public static XdevImage getClipboardImage()
	{
		try
		{
			Object o = getSysClipboardContent(DataFlavor.imageFlavor);
			if(o != null)
			{
				if(o instanceof XdevImage)
				{
					return (XdevImage)o;
				}
				else if(o instanceof Image)
				{
					return new XdevImage((Image)o);
				}
			}
		}
		catch(Exception e)
		{
		}
		
		return null;
	}
	
	
	/**
	 * Returns the file list from the system clipboard
	 * 
	 * <pre>
	 * XdevFile[]	files	= getClipboardFileList();
	 * </pre>
	 * 
	 * @see #getClipboardFileList()
	 * @see #getClipboardString()
	 * @see #putClipboardFileList(java.util.List)
	 */
	public static XdevFile[] getClipboardFileList()
	{
		try
		{
			Object o = getSysClipboardContent(DataFlavor.javaFileListFlavor);
			if(o != null && o instanceof java.util.List)
			{
				List files = (List)o;
				int c = files.size();
				List<XdevFile> list = new ArrayList(c);
				for(int i = 0; i < c; i++)
				{
					list.add(new XdevFile((File)files.get(i)));
				}
				return list.toArray(new XdevFile[list.size()]);
			}
		}
		catch(Exception e)
		{
		}
		
		return null;
	}
	
	
	private static Object getSysClipboardContent(DataFlavor flavor) throws Exception
	{
		Clipboard cp = Toolkit.getDefaultToolkit().getSystemClipboard();
		return cp.getContents(Application.getContainer()).getTransferData(flavor);
	}
	
	
	/**
	 * Store a string in the system clipboard.
	 * <p>
	 * 
	 * <pre>
	 * boolean	success	= IOUtils.putClipboardString(&quot;store me&quot;);
	 * </pre>
	 * 
	 * A string is stored in the clipboard.
	 * </p>
	 * 
	 * @param str
	 *            a string to be stored in the clipboard
	 * 
	 * @return true if, string str was successfully stored in the clipboard
	 * 
	 * @see #getClipboardFileList()
	 * @see #putClipboardFileList(List)
	 * @see #putClipboardString(String)
	 */
	public static boolean putClipboardString(String str)
	{
		return putSystemClipboardContent(str,DataFlavor.stringFlavor);
	}
	
	
	/**
	 * Store a {@link Image} in the system clipboard.
	 * <p>
	 * 
	 * <pre>
	 * ArrayList	fileList	= createList();
	 * 										Image	image	= createImage();
	 * 																			boolean	success	= IOUtils
	 * 																									.putClipboardImage(image);
	 * </pre>
	 * 
	 * A image is created and stored in the clipboard.
	 * </p>
	 * 
	 * @param image
	 *            a {@link Image} to be stored in the clipboard
	 * 
	 * @return true if, image was successfully stored in the clipboard
	 * 
	 * @see #getClipboardFileList()
	 * @see #putClipboardFileList(List)
	 * @see #putClipboardString(String)
	 */
	public static boolean putClipboardImage(Image image)
	{
		return putSystemClipboardContent(image,DataFlavor.imageFlavor);
	}
	
	
	/**
	 * Store a list of files in the system clipboard.
	 * <p>
	 * <b>Hint!</b><br>
	 * It won't be verified, if the files-objects reference existing files.<br>
	 * 
	 * <pre>
	 * Image img = createImage();
	 * boolean success = IOUtils.putClipboardImage(img)
	 * </pre>
	 * 
	 * Stores the image <code>img</code> in the system clipboard.
	 * </p>
	 * 
	 * @param files
	 *            a @link {@link List} of files with containing filepath entries
	 * @return true if, list of files was successfully stored in the clipboard
	 * @see #getClipboardFileList()
	 * @see #putClipboardImage(Image)
	 * @see #putClipboardString(String)
	 */
	public static boolean putClipboardFileList(List<? extends File> files)
	{
		boolean success = false;
		if(files.size() > 0)
		{
			success = putSystemClipboardContent(files,DataFlavor.javaFileListFlavor);
		}
		
		return success;
	}
	
	
	private static boolean putSystemClipboardContent(final Object data, final DataFlavor flavor)
	{
		try
		{
			Clipboard cp = Toolkit.getDefaultToolkit().getSystemClipboard();
			final DataFlavor[] flavors = new DataFlavor[]{flavor};
			cp.setContents(new Transferable()
			{
				public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException,
						IOException
				{
					return data;
				}
				
				
				public DataFlavor[] getTransferDataFlavors()
				{
					return flavors;
				}
				
				
				public boolean isDataFlavorSupported(DataFlavor df)
				{
					return flavor.equals(df);
				}
			},new ClipboardOwner()
			{
				public void lostOwnership(Clipboard clipboard, Transferable contents)
				{
				}
			});
			
			return true;
		}
		catch(Exception e)
		{
			return false;
		}
	}
	
	
	/**
	 * Extracts the name of a file from a path.
	 * <p>
	 * 
	 * <pre>
	 * String	path	= &quot;c:\\one\two\\filename.txt&quot;;
	 * 												String	filename	= IOUtils.getFileName(path);
	 * </pre>
	 * 
	 * Variable filename contains "filename.txt" after the call.
	 * </p>
	 * 
	 * @param path
	 *            the path to extract the filename from
	 * 
	 * @return the file name
	 * 
	 * @throws NullPointerException
	 *             if path is null
	 */
	public static String getFileName(@NotNull String path) throws NullPointerException
	{
		if(path.length() == 1)
		{
			return path;
		}
		
		int i = path.lastIndexOf('/');
		if(i >= 0)
		{
			if(i == path.length() - 1)
			{
				path = path.substring(0,i - 1);
				i = path.lastIndexOf('/');
			}
			
			return path.substring(i + 1);
		}
		
		i = path.lastIndexOf('\\');
		if(i >= 0)
		{
			if(i == path.length() - 1)
			{
				path = path.substring(0,i - 1);
				i = path.lastIndexOf('\\');
			}
			
			return path.substring(i + 1);
		}
		
		return path;
	}
	
	
	/**
	 * Returns the prefix of a filename (filename without extension) of a
	 * specified file.
	 * <p>
	 * This method is a alias for <code>getPrefix(file.getName())</code>
	 * </p>
	 * 
	 * @param file
	 *            the file to get the prefix of the name
	 * 
	 * @return the prefix of the name
	 * 
	 * @throws NullPointerException
	 *             if file is null
	 */
	public static String getPrefix(@NotNull File file) throws NullPointerException
	{
		return getPrefix(file.getName());
	}
	
	
	/**
	 * Returns the prefix of a <code>filename</code> (filename without
	 * extension). <blockquote>
	 * 
	 * <pre>
	 * String	filename	= &quot;filename.txt&quot;;
	 * 										String	prefix	= IOUtils.getPrefix(filename);
	 * </pre>
	 * 
	 * </blockquote>
	 * <p>
	 * prefix contains "filename" after the call.
	 * </p>
	 * 
	 * @param fileName
	 *            name of the file as String
	 * 
	 * @return the prefix of the name
	 * 
	 * @throws NullPointerException
	 *             if filename is null
	 */
	public static String getPrefix(String fileName) throws NullPointerException
	{
		int i = fileName.lastIndexOf('.');
		if(i >= 0)
		{
			return fileName.substring(0,i);
		}
		
		return fileName;
	}
	
	
	/**
	 * Returns the suffix of a filename (Extension without filename) of a
	 * specified file.
	 * <p>
	 * This method is an alias <code>getSuffix(String fileName)</code>.
	 * </p>
	 * 
	 * @param file
	 *            the file to get the suffix of the name
	 * @return the suffix of the name
	 * @throws NullPointerException
	 *             if file is null
	 */
	public static String getSuffix(@NotNull File file) throws NullPointerException
	{
		return getSuffix(file.getName());
	}
	
	
	/**
	 * Returns the suffix of a <code>filename</code> (Extension without
	 * filename).
	 * <p>
	 * 
	 * <pre>
	 * String	filename	= &quot;filename.txt&quot;;
	 * 										String	suffix	= IOUtils.getSuffix(filename);
	 * </pre>
	 * 
	 * suffix contains ".txt" after the call.
	 * </p>
	 * 
	 * @param fileName
	 *            name of the file as String
	 * @return the prefix of the name
	 * @throws NullPointerException
	 *             if filename is null
	 */
	public static String getSuffix(@NotNull String fileName) throws NullPointerException
	{
		int i = fileName.lastIndexOf('.');
		if(i >= 0)
		{
			return fileName.substring(i);
		}
		
		return fileName;
	}
	
	
	/**
	 * The human readable file size for the file <code>f</code>.
	 * 
	 * @return The human readable file size for the file <code>f</code>.<br>
	 *         If <code>f</code> is a directory an empty String is returned.
	 * 
	 * @param f
	 *            the file to retrieve the filesize from
	 * 
	 * @see #getFileSize(long)
	 */
	public static String getFileSize(File f)
	{
		if(!f.isDirectory() && !getFileSystemView().isDrive(f))
		{
			return getFileSize(f.length());
		}
		
		return "";
	}
	
	
	/**
	 * Parses a filesize into a human readable form.
	 * 
	 * @param n
	 *            The file size in bytes
	 * @return The human readable file size for <code>n</code> bytes<br>
	 *         E.g: n=1024 -> 1 KB
	 */
	
	public static String getFileSize(long n)
	{
		try
		{
			double d = 0;
			if(n < 1024)
			{
				return numberFormat.format(n).concat(" Bytes");
			}
			else
			{
				d = n / 1024d;
				if(d < 1024d)
				{
					return numberFormat.format(d).concat(" KB");
				}
				else
				{
					d /= 1024d;
					if(d < 1024d)
					{
						return numberFormat.format(d).concat(" MB");
					}
					else
					{
						d /= 1024d;
						return numberFormat.format(d).concat(" GB");
					}
				}
			}
		}
		catch(Exception e)
		{
			return "" + n;
		}
	}
	
	
	/**
	 * Closes <code>c</code> silently, throws no exception whether
	 * <code>c</code> is <code>null</code> nor if an error occurs.
	 * 
	 * @param c
	 *            The {@link Closeable} to be closed
	 */
	public static void closeSilent(Closeable c)
	{
		if(c != null)
		{
			try
			{
				c.close();
			}
			catch(IOException e)
			{
			}
		}
	}
	
	
	/**
	 * Closes <code>s</code> silently, throws no exception whether
	 * <code>s</code> is <code>null</code> nor if an error occurs.
	 * 
	 * @param s
	 *            The {@link Socket} to be closed
	 */
	
	public static void closeSilent(ServerSocket s)
	{
		if(s != null)
		{
			try
			{
				s.close();
			}
			catch(IOException e)
			{
			}
		}
	}
	
	
	/**
	 * Closes <code>s</code> silently, throws no exception whether
	 * <code>s</code> is <code>null</code> nor if an error occurs.
	 * 
	 * @param s
	 *            The {@link Socket} to be closed
	 */
	
	public static void closeSilent(Socket s)
	{
		if(s != null)
		{
			try
			{
				s.close();
			}
			catch(IOException e)
			{
			}
		}
	}
	
	
	/**
	 * Closes <code>zf</code> silently, throws no exception whether
	 * <code>s</code> is <code>null</code> nor if an error occurs.
	 * 
	 * @param zf
	 *            The {@link ZipFile} to be closed
	 */
	
	public static void closeSilent(ZipFile zf)
	{
		if(zf != null)
		{
			try
			{
				zf.close();
			}
			catch(IOException e)
			{
			}
		}
	}
	
	
	/**
	 * Searches for a resource in the application's classpath and the
	 * filesystem.
	 * <p>
	 * If the resource is found an {@link InputStream} is returned to read from
	 * the resource.<br>
	 * If no resource is found an {@link FileNotFoundException} is thrown.
	 * 
	 * @param relativePath
	 *            The relative path of the resource, e.g. 'res/pics/splash.png'
	 * @return An {@link InputStream} to read from the resource
	 * @throws IOException
	 *             if an IO-error occurs
	 * @throws FileNotFoundException
	 *             if the resource cannot be found
	 * @deprecated typo, use {@link #findResource(String)}
	 */
	@Deprecated
	public static InputStream findRessource(String relativePath) throws IOException,
			FileNotFoundException
	{
		return findResource(relativePath);
	}
	
	
	/**
	 * Searches for a resource in the application's classpath and the
	 * filesystem.
	 * <p>
	 * If the resource is found an {@link InputStream} is returned to read from
	 * the resource.<br>
	 * If no resource is found an {@link FileNotFoundException} is thrown.
	 * 
	 * @param relativePath
	 *            The relative path of the resource, e.g. 'res/pics/splash.png'
	 * @return An {@link InputStream} to read from the resource
	 * @throws IOException
	 *             if an IO-error occurs
	 * @throws FileNotFoundException
	 *             if the resource cannot be found
	 * @since 3.1
	 */
	public static InputStream findResource(String relativePath) throws IOException,
			FileNotFoundException
	{
		relativePath = relativePath.replace('\\','/');
		
		ClassLoader classLoader = IOUtils.class.getClassLoader();
		InputStream in = classLoader.getResourceAsStream(relativePath);
		if(in == null)
		{
			if(relativePath.startsWith("/"))
			{
				in = classLoader.getResourceAsStream(relativePath.substring(1));
			}
			else
			{
				in = classLoader.getResourceAsStream("/" + relativePath);
			}
		}
		if(in != null)
		{
			return in;
		}
		
		File f = new File(relativePath).getAbsoluteFile();
		if(f.exists())
		{
			return new FileInputStream(f);
		}
		
		String projectHome = System.getProperty("project.home",null);
		if(projectHome != null)
		{
			StringBuilder path = new StringBuilder();
			if(projectHome.endsWith("/"))
			{
				path.append(projectHome,0,projectHome.length() - 1);
			}
			else
			{
				path.append(projectHome);
			}
			if(!relativePath.startsWith("/"))
			{
				path.append("/");
			}
			path.append(relativePath);
			
			f = new File(path.toString()).getAbsoluteFile();
			if(f.exists())
			{
				return new FileInputStream(f);
			}
		}
		
		throw new FileNotFoundException(relativePath);
	}
	
	
	// ===============================================================================
	// -------------------------------------------------------------------------------
	// Deprecated and moved stuff
	// -------------------------------------------------------------------------------
	// ===============================================================================
	
	/**
	 * Opens the url in the currently running browser if the Application is an
	 * Applet, otherwise the system's default browser will be used.
	 * 
	 * @deprecated use {@link Application#showDocument(URL,String)} or
	 *             {@link DesktopUtils#browse(String)}
	 */
	public static void showURL(String url, String target) throws IOException
	{
		if(target == null)
		{
			target = "";
		}
		
		if(Application.isApplet())
		{
			showDocument(url,target);
		}
		else
		{
			launchBrowser(url);
		}
	}
	
	
	/**
	 * Opens the url in the currently running browser if the Application is an
	 * Applet, otherwise the system's default browser will be used.
	 * 
	 * @deprecated use {@link Application#showDocument(URL)} or
	 *             {@link DesktopUtils#browse(String)}
	 */
	@Deprecated
	public static void launchBrowser(String url) throws IOException
	{
		if(Application.isApplet())
		{
			showDocument(url,"_blank");
		}
		else
		{
			DesktopUtils.browse_noError(url);
		}
	}
	
	
	/**
	 * @deprecated use {@link Application#showDocument(URL, String)}
	 */
	@Deprecated
	public static void showDocument(String url, String target) throws IOException
	{
		try
		{
			url = createURLString(url);
			
			URL u = null;
			if(url.startsWith("http://"))
			{
				u = new URL(url);
			}
			else
			{
				u = new URL(Application.getContainer().getCodeBase(),url);
			}
			
			if(target != null && target.length() > 0)
			{
				Application.getContainer().showDocument(u,target);
			}
			else
			{
				Application.getContainer().showDocument(u);
			}
		}
		catch(IOException ioe)
		{
			throw ioe;
		}
		catch(Exception e)
		{
			throw new IOException(e.getMessage());
		}
	}
	
	
	/**
	 * Creates a url encoded representation of the specified string.
	 * <p>
	 * 
	 * <pre>
	 * String	input	= &quot;hello hello&quot;;
	 * 									String	urlString	= IOUtils.createURLString(input);
	 * </pre>
	 * 
	 * the input string "hello hello" get url encoded, which results in
	 * "hello+hello"
	 * </p>
	 * 
	 * @param s
	 *            the string to encode
	 * @return the encoded string
	 * @deprecated Use {@link NetUtils#encodeURLString(String)}
	 */
	@Deprecated
	public static String createURLString(String s)
	{
		return NetUtils.encodeURLString(s);
	}
	
	
	/**
	 * @deprecated use {@link NetUtils#getSessionID()}
	 */
	@Deprecated
	public static String getSessionID()
	{
		return NetUtils.getSessionID();
	}
	
	
	/**
	 * @deprecated
	 */
	@Deprecated
	public static void renewSessionID()
	{
		NetUtils.renewSessionID();
	}
	
	
	/**
	 * @deprecated use {@link NetUtils#setCookie(String, String)}
	 */
	@Deprecated
	public static boolean setCookie(String name, String value) throws IOException
	{
		return false;
	}
	
	
	/**
	 * @deprecated use {@link NetUtils#getCookie(String)}
	 */
	@Deprecated
	public static String getCookie(String name) throws IOException
	{
		return null;
	}
	
	
	/**
	 * @deprecated use {@link NetUtils#sendMail(String, String, String, String)}
	 */
	@Deprecated
	public static boolean mail(String receiver, String subject, String message) throws IOException
	{
		return NetUtils.sendMail(receiver,subject,message);
	}
	
	
	/**
	 * @deprecated use {@link NetUtils#sendMail(String, String, String, String)}
	 */
	@Deprecated
	public static boolean mail(String receiver, String subject, String message,
			String additionalHeaders) throws IOException
	{
		return NetUtils.sendMail(receiver,subject,message,additionalHeaders);
	}
	
	
	/**
	 * @deprecated use {@link NetUtils#uploadFile(String, String, long, String)}
	 */
	public static void uploadFile(String destFolder, String destFileName, long maxSize,
			String dialogTitle) throws IOException
	{
		NetUtils.uploadFile(destFolder,destFileName,maxSize,dialogTitle);
	}
	
	/**
	 * Lazy init of fileSystemView to prevent spawning an UI Thread prematurely
	 * @return {@link FileSystemView}
	 */
	private static FileSystemView getFileSystemView() {
		if (fileSystemView == null) {
			fileSystemView = FileSystemView.getFileSystemView();
		}
		
		return fileSystemView;
	}
	
}
