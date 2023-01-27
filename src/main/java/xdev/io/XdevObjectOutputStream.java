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


import java.io.*;

import xdev.*;


public class XdevObjectOutputStream extends DataOutputStream implements XdevObjectIOConstants
{
	public XdevObjectOutputStream(OutputStream out)
	{
		super(out);
	}
	

	public void writeObject(Object o) throws Exception
	{
		if(o == null)
		{
			writeByte(NULL);
		}
		else if(o instanceof Byte)
		{
			writeByte(BYTE);
			writeByte(((Byte)o).byteValue());
		}
		else if(o instanceof Short)
		{
			writeByte(SHORT);
			writeShort(((Short)o).shortValue());
		}
		else if(o instanceof Integer)
		{
			writeByte(INTEGER);
			writeInt(((Integer)o).intValue());
		}
		else if(o instanceof Long)
		{
			writeByte(LONG);
			writeLong(((Long)o).longValue());
		}
		else if(o instanceof Float)
		{
			writeByte(FLOAT);
			writeFloat(((Float)o).floatValue());
		}
		else if(o instanceof Double)
		{
			writeByte(DOUBLE);
			writeDouble(((Double)o).doubleValue());
		}
		else if(o instanceof Boolean)
		{
			writeByte(BOOLEAN);
			writeBoolean(((Boolean)o).booleanValue());
		}
		else if(o instanceof Character)
		{
			writeByte(CHAR);
			writeChar(((Character)o).charValue());
		}
		else if(o instanceof String)
		{
			writeByte(STRING);
			writeUTF(o.toString());
		}
		else if(o instanceof java.sql.Date)
		{
			writeByte(SQL_DATE);
			writeLong(((java.sql.Date)o).getTime());
		}
		else if(o instanceof java.sql.Time)
		{
			writeByte(SQL_TIME);
			writeLong(((java.sql.Time)o).getTime());
		}
		else if(o instanceof java.sql.Timestamp)
		{
			writeByte(SQL_TIMESTAMP);
			java.sql.Timestamp ts = (java.sql.Timestamp)o;
			writeLong(ts.getTime());
			writeInt(ts.getNanos());
		}
		else if(o instanceof java.util.Date)
		{
			writeByte(UTIL_DATE);
			writeLong(((java.util.Date)o).getTime());
		}
		else if(o instanceof Version)
		{
			writeByte(VERSION);
			writeUTF(((Version)o).toString());
		}
		else if(o instanceof java.sql.Blob)
		{
			writeByte(BLOB);
			java.sql.Blob blob = (java.sql.Blob)o;
			writeLong(blob.length());
			InputStream in = blob.getBinaryStream();
			byte[] b = new byte[1024];
			int read = in.read(b);
			while(read >= 0)
			{
				write(b,0,read);
				read = in.read(b);
			}
			in.close();
		}
		else if(o instanceof byte[])
		{
			writeByte(BLOB);
			byte[] b = (byte[])o;
			writeLong(b.length);
			write(b);
		}
		else if(o instanceof ByteHolder)
		{
			writeByte(BLOB);
			byte[] b = ((ByteHolder)o).toByteArray();
			writeLong(b.length);
			write(b);
		}
		else if(o instanceof java.sql.Clob)
		{
			writeByte(CLOB);
			java.sql.Clob clob = (java.sql.Clob)o;
			writeLong(clob.length());
			Reader in = clob.getCharacterStream();
			int read = in.read();
			while(read >= 0)
			{
				writeChar(read);
				read = in.read();
			}
			in.close();
		}
		else if(o instanceof char[])
		{
			writeByte(CLOB);
			char[] c = (char[])o;
			writeLong(c.length);
			for(int i = 0; i < c.length; i++)
			{
				writeChar(c[i]);
			}
		}
		else if(o instanceof CharHolder)
		{
			writeByte(CLOB);
			char[] c = ((CharHolder)o).toCharArray();
			writeLong(c.length);
			for(int i = 0; i < c.length; i++)
			{
				writeChar(c[i]);
			}
		}
		else
		{
			ByteArrayOutputStream bout = new ByteArrayOutputStream();
			ObjectOutputStream oout = new ObjectOutputStream(bout);
			oout.writeObject(o);
			oout.close();
			
			writeByte(OTHER);
			writeInt(bout.size());
			bout.writeTo(this);
		}
	}
}
