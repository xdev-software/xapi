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


import java.io.CharArrayReader;
import java.io.CharArrayWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Reader;
import java.io.Serializable;
import java.io.Writer;
import java.sql.Clob;
import java.sql.SQLException;
import java.util.Arrays;

import javax.sql.rowset.serial.SerialException;

import xdev.db.DBException;
import xdev.io.CharHolder;
import xdev.io.IOUtils;
import xdev.util.logging.LoggerFactory;
import xdev.util.logging.XdevLogger;


/**
 * The mapping in XDEV for the SQL CLOB type. A XdevClob stores a Character
 * Large Object as a column value in a row of a {@link VirtualTable}.
 * <p>
 * The XdevClob holds either an reference to a {@link Clob} object or plain
 * chars.
 * 
 * @author XDEV Software Corp.
 * @see Clob
 * 
 */
public class XdevClob implements CharHolder, Serializable, Comparable<CharHolder>
{
	/**
	 * Logger instance for this class.
	 */
	private static final XdevLogger	log					= LoggerFactory.getLogger(XdevClob.class);
	
	private static final long		serialVersionUID	= 7975847043132313545L;
	
	private char[]					chars;
	private Clob					clob;
	

	/**
	 * Initializes a new instance of {@link XdevClob}.
	 */
	public XdevClob()
	{
		this(new char[0]);
	}
	

	/**
	 * Initializes a new instance of {@link XdevBlob}.
	 * 
	 * @param chars
	 *            a character array initially to be set
	 */
	public XdevClob(char[] chars)
	{
		super();
		
		this.clob = null;
		this.chars = chars;
	}
	

	/**
	 * Initializes a new instance of {@link XdevClob}.
	 * 
	 * @param clob
	 *            a {@link Clob} initially to be set.
	 * @throws DBException
	 *             if the specified clob could not be set.
	 */
	public XdevClob(Clob clob) throws DBException
	{
		super();
		
		this.chars = null;
		this.clob = clob;
	}
	

	void readFully() throws DBException
	{
		if(chars == null && clob != null)
		{
			int length = length();
			if(length >= 0)
			{
				chars = new char[length];
				Reader reader = getCharacterStream();
				try
				{
					int read;
					int offset = 0;
					while(offset < length
							&& (read = reader.read(chars,offset,length - offset)) != -1)
					{
						offset += read;
					}
				}
				catch(IOException ioe)
				{
					throw new DBException(null,ioe);
				}
				finally
				{
					IOUtils.closeSilent(reader);
				}
			}
			else
			{
				chars = new char[0];
			}
			
			clob = null;
		}
	}
	

	/**
	 * Returns the number of characters of this {@link XdevBlob}.
	 * 
	 * @return the number of characters or -1 if the length couldn't be
	 *         retrieved
	 */
	public int length()
	{
		if(clob != null)
		{
			try
			{
				return (int)clob.length();
			}
			catch(SQLException e)
			{
				return -1;
			}
		}
		else
		{
			return chars.length;
		}
	}
	

	/**
	 * Truncates the CLOB value that this {@link XdevClob} object represents to
	 * be len characters in length.
	 * 
	 * @param length
	 *            the length, in characters, to which the CLOB value that this
	 *            Blob object represents should be truncated
	 * @throws DBException
	 *             if truncate was not successful.
	 */
	public void truncate(long length) throws DBException
	{
		if(clob != null)
		{
			try
			{
				clob.truncate(length);
			}
			catch(SQLException e)
			{
				throw new DBException(null,e);
			}
		}
		else
		{
			if(length < chars.length && length >= 0)
			{
				char[] chars = new char[(int)length];
				System.arraycopy(this.chars,0,chars,0,chars.length);
				this.chars = chars;
			}
		}
	}
	

	/**
	 * Retrieves the CLOB value designated by this Clob object as a
	 * {@link Reader} object (or as a stream of characters).
	 * 
	 * @return a {@link Reader} object containing the CLOB data
	 * @throws DBException
	 *             if there is an error accessing the CLOB value
	 */
	public Reader getCharacterStream() throws DBException
	{
		if(clob != null)
		{
			try
			{
				return clob.getCharacterStream();
			}
			catch(SQLException e)
			{
				throw new DBException(null,e);
			}
		}
		else
		{
			return new CharArrayReader(chars);
		}
	}
	

	/**
	 * Returns a Reader object that contains a partial Clob value, starting with
	 * the character specified by pos, which is length characters in length.
	 * 
	 * @param position
	 *            the offset to the first character of the partial value to be
	 *            retrieved
	 * @param length
	 *            the length in characters of the partial value to be retrieved
	 * @return a {@link Reader} through which the partial Clob value can be read
	 * @throws DBException
	 *             if there is an error accessing the CLOB value
	 */
	public Reader getCharacterStream(int position, int length) throws DBException
	{
		if(clob != null)
		{
			try
			{
				return clob.getCharacterStream(position + 1,length);
			}
			catch(SQLException e)
			{
				throw new DBException(null,e);
			}
		}
		else
		{
			return new CharArrayReader(chars,position,length);
		}
	}
	

	/**
	 * Retrieves a stream to be used to write a stream of Unicode characters to
	 * the CLOB value that this Clob object represents, at position pos.
	 * Characters written to the stream will overwrite the existing characters
	 * in the Clob object starting at the position pos. If the end of the Clob
	 * value is reached while writing characters to the stream, then the length
	 * of the Clob value will be increased to accomodate the extra characters.
	 * 
	 * @param position
	 *            the position at which to start writing to the CLOB value
	 * @return a {@link Writer} to which Unicode encoded characters can be
	 *         written
	 * @throws DBException
	 *             if there is an error accessing the CLOB value
	 */
	public Writer setCharacterStream(final int position) throws DBException
	{
		if(clob != null)
		{
			try
			{
				return clob.setCharacterStream(position + 1);
			}
			catch(SQLException e)
			{
				throw new DBException(null,e);
			}
		}
		else
		{
			return new CharArrayWriter()
			{
				@Override
				public void close()
				{
					byte[] result = new byte[position + count];
					System.arraycopy(chars,0,result,0,position);
					System.arraycopy(buf,0,result,position,count);
					XdevClob.this.chars = chars;
				}
			};
		}
	}
	

	/**
	 * Returns a part of this {@link XdevClob} as a {@link String}.
	 * 
	 * @param pos
	 *            the position of the first character to be returned.
	 * 
	 * @param length
	 *            the number of characters to be returned.
	 * 
	 * @return a {@link String} representation for a part of this
	 *         {@link XdevClob}.
	 * 
	 * @throws DBException
	 */
	public String getSubString(int pos, int length) throws DBException
	{
		if(clob != null)
		{
			try
			{
				return clob.getSubString(pos + 1,length);
			}
			catch(SQLException e)
			{
				throw new DBException(null,e);
			}
		}
		else
		{
			return new String(chars,pos,length);
		}
	}
	

	/**
	 * Writes the given Java String to the CLOB value that this Clob object
	 * designates at the position pos. The string will overwrite the existing
	 * characters in the Clob object starting at the position pos. If the end of
	 * the Clob value is reached while writing the given string, then the length
	 * of the Clob value will be increased to accomodate the extra characters.
	 * 
	 * @param position
	 *            the position at which to start writing to the CLOB value that
	 *            this Clob object represents
	 * @param str
	 *            the string to be written to the CLOB value that this Clob
	 *            designates
	 * @return the number of characters written
	 * @throws DBException
	 *             if there is an error accessing the CLOB value
	 */
	public int setString(int position, String str) throws DBException
	{
		if(clob != null)
		{
			try
			{
				return clob.setString(position + 1,str);
			}
			catch(SQLException e)
			{
				throw new DBException(null,e);
			}
		}
		else
		{
			str.getChars(0,str.length(),chars,position);
			return str.length();
		}
	}
	

	/**
	 * Writes len characters of str, starting at character offset, to the CLOB
	 * value that this Clob represents. The string will overwrite the existing
	 * characters in the Clob object starting at the position pos. If the end of
	 * the Clob value is reached while writing the given string, then the length
	 * of the Clob value will be increased to accomodate the extra characters.
	 * 
	 * @param position
	 *            the position at which to start writing to this CLOB object
	 * @param str
	 *            the string to be written to the CLOB value that this Clob
	 *            object represents
	 * @param offset
	 *            the offset into str to start reading the characters to be
	 *            written
	 * @param length
	 *            the number of characters to be written
	 * @return the number of characters written
	 * @throws DBException
	 *             if there is an error accessing the CLOB value
	 */
	public int setString(int position, String str, int offset, int length) throws DBException
	{
		if(clob != null)
		{
			try
			{
				return clob.setString(position + 1,str,offset,length);
			}
			catch(SQLException e)
			{
				throw new DBException(null,e);
			}
		}
		else
		{
			str.getChars(offset,length,chars,position);
			return length;
		}
	}
	
	private String	toString	= null;
	

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString()
	{
		if(toString == null)
		{
			try
			{
				toString = getSubString(0,length());
			}
			catch(DBException e)
			{
				log.error(e);
				
				toString = "CLOB, length = " + length();
			}
		}
		
		return toString;
	}
	

	/**
	 * Returns a copied <code>char array</code> of this {@link XdevClob}.
	 * 
	 * @return a <code>char array</code>
	 */
	public char[] toCharArray()
	{
		if(clob != null)
		{
			try
			{
				return clob.getSubString(1,(int)clob.length()).toCharArray();
			}
			catch(Exception e)
			{
				return new char[0];
			}
		}
		else
		{
			char[] array = new char[chars.length];
			System.arraycopy(chars,0,array,0,chars.length);
			return array;
		}
	}
	

	/**
	 * Compares this {@link XdevClob} with another {@link CharHolder}.
	 * <p>
	 * The length of the {@link CharHolder} objects is used for this comparison.
	 * </p>
	 * 
	 * @param other
	 *            the other {@link CharHolder}.
	 *            <ul>
	 *            <li>0 - if the length of both objects is equal</li>
	 *            <li>-1 - if the length of this {@link CharHolder} is greater</li>
	 *            <li>1 - if the length of this {@link CharHolder} is smaller</li>
	 *            </ul>
	 */
	public int compareTo(CharHolder other)
	{
		long thisLength = length();
		long otherLength = other.length();
		return thisLength < otherLength ? -1 : thisLength == otherLength ? 0 : 1;
	}
	

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Object obj)
	{
		if(this == obj)
		{
			return true;
		}
		
		if(obj instanceof CharHolder)
		{
			CharHolder other = (CharHolder)obj;
			
			if(length() == other.length())
			{
				return Arrays.equals(toCharArray(),other.toCharArray());
			}
		}
		
		return false;
	}
	

	/**
	 * Returns a copied {@link Clob} for this {@link XdevClob}.
	 * 
	 * @return a {@link Clob}
	 * @throws DBException
	 *             if {@link Clob} could not be created.
	 */
	public Clob toJDBCClob() throws DBException
	{
		if(clob != null)
		{
			return clob;
		}
		
		try
		{
			return new ExtendedSerialClob(chars);
		}
		catch(SerialException e)
		{
			throw new DBException(null,e);
		}
		catch(SQLException e)
		{
			throw new DBException(null,e);
		}
	}
	

	/**
	 * Generates a hashcode for this {@link XdevBlob}.
	 * 
	 * @return a hashcode
	 */
	@Override
	public int hashCode()
	{
		return Arrays.hashCode(toCharArray());
	}
	

	private void writeObject(ObjectOutputStream out) throws IOException
	{
		if(clob != null)
		{
			try
			{
				out.writeInt((int)clob.length());
				
				char[] buffer = new char[1024];
				Reader in = clob.getCharacterStream();
				try
				{
					int read;
					while((read = in.read(buffer)) != -1)
					{
						for(int i = 0; i < read; i++)
						{
							out.writeChar(buffer[i]);
						}
					}
				}
				finally
				{
					in.close();
				}
			}
			catch(SQLException e)
			{
				throw new IOException(e);
			}
		}
		else
		{
			int len = chars.length;
			out.writeInt(len);
			for(int i = 0; i < len; i++)
			{
				out.writeChar(chars[i]);
			}
		}
	}
	

	private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException
	{
		int len = in.readInt();
		this.chars = new char[len];
		for(int i = 0; i < len; i++)
		{
			this.chars[i] = in.readChar();
		}
	}
	


	private static class ExtendedSerialClob implements Clob, Serializable, Cloneable
	{
		private static final long	serialVersionUID	= 6734977304211158086L;
		
		/**
		 * @serial
		 */
		private char				buf[];
		
		private Clob				clob;
		
		/**
		 * @serial
		 */
		private long				len;
		
		/**
		 * @serial
		 */
		private long				origLen;
		

		public ExtendedSerialClob(char ch[]) throws SerialException, SQLException
		{
			len = ch.length;
			buf = new char[(int)len];
			for(int i = 0; i < len; i++)
			{
				buf[i] = ch[i];
			}
			origLen = len;
		}
		

		public long length() throws SerialException
		{
			return len;
		}
		

		public Reader getCharacterStream() throws SerialException
		{
			return (java.io.Reader)new CharArrayReader(buf);
		}
		

		public InputStream getAsciiStream() throws SerialException, SQLException
		{
			if(this.clob != null)
			{
				return this.clob.getAsciiStream();
			}
			else
			{
				return new java.io.InputStream()
				{
					int	pos	= 0;
					

					@Override
					public int read() throws IOException
					{
						return pos < buf.length ? buf[pos++] : -1;
					}
				};
			}
		}
		

		public String getSubString(long pos, int length) throws SerialException
		{
			if(pos < 1 || pos > this.length())
			{
				throw new SerialException("Invalid position in BLOB object set");
			}
			
			if((pos - 1) + length > this.length())
			{
				throw new SerialException("Invalid position and substring length");
			}
			
			try
			{
				return new String(buf,(int)pos - 1,length);
				
			}
			catch(StringIndexOutOfBoundsException e)
			{
				throw new SerialException("StringIndexOutOfBoundsException: " + e.getMessage());
			}
			
		}
		

		public long position(String searchStr, long start) throws SerialException, SQLException
		{
			if(start < 1 || start > len)
			{
				return -1;
			}
			
			char pattern[] = searchStr.toCharArray();
			
			int pos = (int)start - 1;
			int i = 0;
			long patlen = pattern.length;
			
			while(pos < len)
			{
				if(pattern[i] == buf[pos])
				{
					if(i + 1 == patlen)
					{
						return (pos + 1) - (patlen - 1);
					}
					i++;
					pos++; // increment pos, and i
					
				}
				else if(pattern[i] != buf[pos])
				{
					pos++; // increment pos only
				}
			}
			return -1; // not found
		}
		

		public long position(Clob searchStr, long start) throws SerialException, SQLException
		{
			return position(searchStr.getSubString(1,(int)searchStr.length()),start);
		}
		

		public int setString(long pos, String str) throws SerialException
		{
			return(setString(pos,str,0,str.length()));
		}
		

		public int setString(long pos, String str, int offset, int length) throws SerialException
		{
			String temp = str.substring(offset);
			char cPattern[] = temp.toCharArray();
			
			if(offset < 0 || offset > str.length())
			{
				throw new SerialException("Invalid offset in byte array set");
			}
			
			if(pos < 1 || pos > this.length())
			{
				throw new SerialException("Invalid position in BLOB object set");
			}
			
			if((long)(length) > origLen)
			{
				throw new SerialException("Buffer is not sufficient to hold the value");
			}
			
			if((length + offset) > str.length())
			{
				// need check to ensure length + offset !> bytes.length
				throw new SerialException("Invalid OffSet. Cannot have combined offset "
						+ " and length that is greater that the Blob buffer");
			}
			
			int i = 0;
			pos--; // values in the array are at position one less
			while(i < length || (offset + i + 1) < (str.length() - offset))
			{
				this.buf[(int)pos + i] = cPattern[offset + i];
				i++;
			}
			return i;
		}
		

		public java.io.OutputStream setAsciiStream(long pos) throws SerialException, SQLException
		{
			if(this.clob.setAsciiStream(pos) != null)
			{
				return this.clob.setAsciiStream(pos);
			}
			else
			{
				throw new SerialException(
						"Unsupported operation. SerialClob cannot "
								+ "return a writable ascii stream\n unless instantiated with a Clob object "
								+ "that has a setAsciiStream() implementation");
			}
		}
		

		public java.io.Writer setCharacterStream(long pos) throws SerialException, SQLException
		{
			if(this.clob.setCharacterStream(pos) != null)
			{
				return this.clob.setCharacterStream(pos);
			}
			else
			{
				throw new SerialException(
						"Unsupported operation. SerialClob cannot "
								+ "return a writable character stream\n unless instantiated with a Clob object "
								+ "that has a setCharacterStream implementation");
			}
		}
		

		public void truncate(long length) throws SerialException
		{
			if(length > len)
			{
				throw new SerialException("Length more than what can be truncated");
			}
			else
			{
				len = length;
				// re-size the buffer
				
				if(len == 0)
				{
					buf = new char[]{};
				}
				else
				{
					buf = (this.getSubString(1,(int)len)).toCharArray();
				}
			}
		}
		

		public Reader getCharacterStream(long pos, long length) throws SQLException
		{
			return new CharArrayReader(buf);
		}
		

		public void free() throws SQLException
		{
			throw new java.lang.UnsupportedOperationException("Not supported");
		}
	}
}
