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


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.Arrays;

import javax.sql.rowset.serial.SerialBlob;
import javax.sql.rowset.serial.SerialException;

import xdev.db.DBException;
import xdev.io.ByteHolder;
import xdev.io.IOUtils;


/**
 * The mapping in XDEV for the SQL BLOB type. A XdevBlob stores a Binary Large
 * Object as a column value in a row of a {@link VirtualTable}.
 * <p>
 * The XdevBlob holds either an reference to a {@link Blob} object or plain
 * bytes.
 * 
 * @author XDEV Software Corp.
 * @see Blob
 * 
 */
public class XdevBlob implements ByteHolder, Serializable, Comparable<ByteHolder>
{
	private static final long	serialVersionUID	= 7975847043132313545L;
	
	private byte[]				bytes;
	private Blob				blob;
	

	/**
	 * Initializes a new instance of {@link XdevBlob}.
	 */
	public XdevBlob()
	{
		this(new byte[0]);
	}
	

	/**
	 * Initializes a new instance of {@link XdevBlob}.
	 * 
	 * @param bytes
	 *            a byte array initially to be set
	 */
	public XdevBlob(byte[] bytes)
	{
		super();
		
		this.blob = null;
		this.bytes = bytes;
	}
	

	/**
	 * Initializes a new instance of {@link XdevBlob}.
	 * 
	 * @param blob
	 *            a {@link Blob} initially to be set.
	 * @throws DBException
	 *             if the specified blob could not be set.
	 */
	public XdevBlob(Blob blob) throws DBException
	{
		super();
		
		this.bytes = null;
		this.blob = blob;
	}
	

	void readFully() throws DBException
	{
		if(bytes == null && blob != null)
		{
			int length = length();
			if(length >= 0)
			{
				bytes = new byte[length];
				InputStream in = getBinaryStream();
				try
				{
					int read;
					int offset = 0;
					while(offset < length && (read = in.read(bytes,offset,length - offset)) != -1)
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
					IOUtils.closeSilent(in);
				}
			}
			else
			{
				bytes = new byte[0];
			}
			
			blob = null;
		}
	}
	

	/**
	 * Returns the number of bytes of this {@link XdevBlob}.
	 * 
	 * @return the number of bytes or -1 if the length couldn't be retrieved
	 */
	public int length()
	{
		if(blob != null)
		{
			try
			{
				return (int)blob.length();
			}
			catch(SQLException e)
			{
				return -1;
			}
		}
		else
		{
			return bytes.length;
		}
	}
	

	/**
	 * Truncates the BLOB value that this {@link XdevBlob} object represents to
	 * be len bytes in length.
	 * 
	 * @param length
	 *            the length, in bytes, to which the BLOB value that this Blob
	 *            object represents should be truncated
	 * @throws DBException
	 *             if truncate was not successful.
	 */
	public void truncate(long length) throws DBException
	{
		if(blob != null)
		{
			try
			{
				blob.truncate(length);
			}
			catch(SQLException e)
			{
				throw new DBException(null,e);
			}
		}
		else
		{
			if(length < bytes.length && length >= 0)
			{
				byte[] bytes = new byte[(int)length];
				System.arraycopy(this.bytes,0,bytes,0,bytes.length);
				this.bytes = bytes;
			}
		}
	}
	

	/**
	 * Retrieves the BLOB value designated by this Blob instance as a stream.
	 * 
	 * @return an {@link InputStream} containing the BLOB data
	 * @throws DBException
	 *             if there is an error accessing the BLOB value
	 */
	public InputStream getBinaryStream() throws DBException
	{
		if(blob != null)
		{
			try
			{
				return blob.getBinaryStream();
			}
			catch(SQLException e)
			{
				throw new DBException(null,e);
			}
		}
		else
		{
			return new ByteArrayInputStream(bytes);
		}
	}
	

	/**
	 * Returns an InputStream object that contains a partial Blob value,
	 * starting with the byte specified by pos, which is length bytes in length.
	 * 
	 * @param position
	 *            the offset to the first byte of the partial value to be
	 *            retrieved
	 * @param length
	 *            the length in bytes of the partial value to be retrieved
	 * 
	 * @return {@link InputStream} through which the partial Blob value can be
	 *         read.
	 * 
	 * @throws DBException
	 *             if there is an error accessing the BLOB value
	 */
	public InputStream getBinaryStream(int position, int length) throws DBException
	{
		if(blob != null)
		{
			try
			{
				return blob.getBinaryStream(position + 1,length);
			}
			catch(SQLException e)
			{
				throw new DBException(null,e);
			}
		}
		else
		{
			return new ByteArrayInputStream(bytes,position,length);
		}
	}
	

	/**
	 * Retrieves a stream that can be used to write to the BLOB value that this
	 * Blob object represents. The stream begins at position pos. The bytes
	 * written to the stream will overwrite the existing bytes in the Blob
	 * object starting at the position pos. If the end of the Blob value is
	 * reached while writing to the stream, then the length of the Blob value
	 * will be increased to accomodate the extra bytes.
	 * 
	 * @param position
	 *            the position in the BLOB value at which to start writing
	 * @return a {@link OutputStream} object to which data can be written
	 * @throws DBException
	 *             if there is an error accessing the BLOB value
	 */
	public OutputStream setBinaryStream(final int position) throws DBException
	{
		if(blob != null)
		{
			try
			{
				return blob.setBinaryStream(position + 1);
			}
			catch(SQLException e)
			{
				throw new DBException(null,e);
			}
		}
		else
		{
			return new ByteArrayOutputStream()
			{
				@Override
				public void close() throws IOException
				{
					byte[] result = new byte[position + count];
					System.arraycopy(bytes,0,result,0,position);
					System.arraycopy(buf,0,result,position,count);
					XdevBlob.this.bytes = result;
				}
			};
		}
	}
	

	/**
	 * Returns the byte value at the specified index position.
	 * 
	 * @param index
	 *            the index to retrieve the byte from.
	 * @return a byte value
	 * @throws DBException
	 *             if byte could not be read.
	 */
	public int getByteAt(int index) throws DBException
	{
		if(blob != null)
		{
			try
			{
				return blob.getBytes(index + 1,1)[0];
			}
			catch(SQLException e)
			{
				throw new DBException(null,e);
			}
		}
		else
		{
			return bytes[index];
		}
	}
	

	/**
	 * Retrieves all or part of the BLOB value that this Blob object represents,
	 * as an array of bytes. This byte array contains up to length consecutive
	 * bytes starting at position pos.
	 * 
	 * @param position
	 *            the ordinal position of the first byte in the BLOB value to be
	 *            extracted
	 * @param length
	 *            the number of consecutive bytes to be copied; the value for
	 *            length must be 0 or greater
	 * @return a byte array containing up to length consecutive bytes from the
	 *         BLOB value designated by this Blob object, starting with the byte
	 *         at position pos
	 * @throws DBException
	 *             if there is an error accessing the BLOB value
	 */
	public byte[] getBytes(int position, int length) throws DBException
	{
		if(blob != null)
		{
			try
			{
				return blob.getBytes(position + 1,length);
			}
			catch(SQLException e)
			{
				throw new DBException(null,e);
			}
		}
		else
		{
			return Arrays.copyOfRange(bytes,position,position + length);
		}
	}
	

	/**
	 * Writes the given array of bytes to the BLOB value that this Blob object
	 * represents, starting at position pos, and returns the number of bytes
	 * written. The array of bytes will overwrite the existing bytes in the Blob
	 * object starting at the position pos. If the end of the Blob value is
	 * reached while writing the array of bytes, then the length of the Blob
	 * value will be increased to accomodate the extra bytes.
	 * 
	 * @param position
	 *            the position in the BLOB object at which to start writing
	 * @param bytes
	 *            the array of bytes to be written to the BLOB value that this
	 *            Blob object represents
	 * 
	 * @return the number of bytes written
	 * 
	 * @throws DBException
	 *             if there is an error accessing the BLOB value
	 */
	public int setBytes(int position, byte[] bytes) throws DBException
	{
		if(blob != null)
		{
			try
			{
				return blob.setBytes(position + 1,bytes);
			}
			catch(SQLException e)
			{
				throw new DBException(null,e);
			}
		}
		else
		{
			System.arraycopy(bytes,0,this.bytes,position,bytes.length);
			return bytes.length;
		}
	}
	

	/**
	 * Writes all or part of the given byte array to the BLOB value that this
	 * Blob object represents and returns the number of bytes written. Writing
	 * starts at position pos in the BLOB value; len bytes from the given byte
	 * array are written. The array of bytes will overwrite the existing bytes
	 * in the Blob object starting at the position pos. If the end of the Blob
	 * value is reached while writing the array of bytes, then the length of the
	 * Blob value will be increased to accomodate the extra bytes.
	 * 
	 * @param position
	 *            the position in the BLOB object at which to start writing
	 * @param bytes
	 *            the array of bytes to be written to this BLOB object
	 * @param offset
	 *            the offset into the array bytes at which to start reading the
	 *            bytes to be set
	 * @param length
	 *            the number of bytes to be written to the BLOB value from the
	 *            array of bytes bytes
	 * @return the number of bytes written
	 * @throws DBException
	 *             if there is an error accessing the BLOB value
	 */
	public int setBytes(int position, byte[] bytes, int offset, int length) throws DBException
	{
		if(blob != null)
		{
			try
			{
				return blob.setBytes(position + 1,bytes,offset,length);
			}
			catch(SQLException e)
			{
				throw new DBException(null,e);
			}
		}
		else
		{
			System.arraycopy(bytes,offset,this.bytes,position,length);
			return length;
		}
	}
	

	/**
	 * Returns the {@link String} represenation for this {@link XdevBlob}
	 * containing the length of the BLOB.
	 * 
	 * @return a String represenation for this {@link XdevBlob}.
	 */
	@Override
	public String toString()
	{
		return "BLOB, length = " + length();
	}
	

	/**
	 * Returns a copied <code>byte array</code> of this {@link XdevBlob}.
	 * 
	 * @return a <code>byte array</code>
	 */
	public byte[] toByteArray()
	{
		if(blob != null)
		{
			try
			{
				return blob.getBytes(1,(int)blob.length());
			}
			catch(Exception e)
			{
				return new byte[0];
			}
		}
		else
		{
			byte[] array = new byte[bytes.length];
			System.arraycopy(bytes,0,array,0,bytes.length);
			return array;
		}
	}
	

	/**
	 * Compares this {@link XdevBlob} with another {@link ByteHolder}.
	 * <p>
	 * The length of the {@link ByteHolder} objects is used for this comparison.
	 * </p>
	 * 
	 * @param other
	 *            the other {@link ByteHolder}.
	 * @return <ul>
	 *         <li>0 - if the length of both objects is equal</li>
	 *         <li>-1 - if the length of this {@link ByteHolder} is greater</li>
	 *         <li>1 - if the length of this {@link ByteHolder} is smaller</li>
	 *         </ul>
	 */
	public int compareTo(ByteHolder other)
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
		
		if(obj instanceof ByteHolder)
		{
			ByteHolder other = (ByteHolder)obj;
			
			if(length() == other.length())
			{
				return Arrays.equals(toByteArray(),other.toByteArray());
			}
		}
		
		return false;
	}
	

	/**
	 * Returns a copied {@link Blob} for this {@link XdevBlob}.
	 * 
	 * @return a {@link Blob}
	 * @throws DBException
	 *             if {@link Blob} could not be created.
	 */
	public Blob toJDBCBlob() throws DBException
	{
		if(blob != null)
		{
			return blob;
		}
		
		try
		{
			return new SerialBlob(bytes);
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
		return Arrays.hashCode(toByteArray());
	}
	

	private void writeObject(ObjectOutputStream out) throws IOException
	{
		if(blob != null)
		{
			try
			{
				out.writeInt((int)blob.length());
				
				byte[] buffer = new byte[1024];
				InputStream in = blob.getBinaryStream();
				try
				{
					int read;
					while((read = in.read(buffer)) != -1)
					{
						out.write(buffer,0,read);
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
			out.writeInt(bytes.length);
			out.write(bytes);
		}
	}
	

	private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException
	{
		int len = in.readInt();
		this.bytes = new byte[len];
		in.readFully(this.bytes);
	}
}
