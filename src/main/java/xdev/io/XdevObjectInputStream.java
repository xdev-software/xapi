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


import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectStreamClass;
import java.lang.reflect.Modifier;
import java.lang.reflect.Proxy;

import xdev.Version;


public class XdevObjectInputStream extends DataInputStream implements XdevObjectIOConstants
{
	// ClassLoader context for de-serialization
	private final Object	context;


	public XdevObjectInputStream(InputStream in)
	{
		this(in,null);
	}


	public XdevObjectInputStream(InputStream in, Object context)
	{
		super(in);

		this.context = context;
	}


	public Object readObject() throws Exception
	{
		Object o = null;

		byte flag = readByte();

		switch(flag)
		{
			case OTHER:
			{
				int len = readInt();
				byte[] b = new byte[len];
				int read = in.read(b);
				while(read < b.length)
				{
					read += read(b,read,b.length - read);
				}

				ObjectInputStream oin = new ObjectInputStream(new ByteArrayInputStream(b))
				{
					@Override
					protected Class<?> resolveClass(ObjectStreamClass desc) throws IOException,
							ClassNotFoundException
					{
						if(context != null)
						{
							ClassLoader cl = context.getClass().getClassLoader();
							if(cl != null)
							{
								Class c = Class.forName(desc.getName(),false,cl);
								if(c != null)
								{
									return c;
								}
							}
						}

						return super.resolveClass(desc);
					}


					@Override
					protected Class<?> resolveProxyClass(String[] interfaces) throws IOException,
							ClassNotFoundException
					{
						ClassLoader classLoader = null;
						if(context != null)
						{
							classLoader = context.getClass().getClassLoader();
						}

						if(classLoader == null)
						{
							return super.resolveProxyClass(interfaces);
						}

						ClassLoader nonPublicLoader = null;
						boolean hasNonPublicInterface = false;

						Class[] classObjs = new Class[interfaces.length];
						for(int i = 0; i < interfaces.length; i++)
						{
							Class cl = Class.forName(interfaces[i],false,classLoader);
							if((cl.getModifiers() & Modifier.PUBLIC) == 0)
							{
								if(hasNonPublicInterface)
								{
									if(nonPublicLoader != cl.getClassLoader())
									{
										throw new IllegalAccessError(
												"conflicting non-public interface class loaders");
									}
								}
								else
								{
									nonPublicLoader = cl.getClassLoader();
									hasNonPublicInterface = true;
								}
							}
							classObjs[i] = cl;
						}
						try
						{
							return Proxy.getProxyClass(hasNonPublicInterface ? nonPublicLoader
									: classLoader,classObjs);
						}
						catch(IllegalArgumentException e)
						{
							throw new ClassNotFoundException(null,e);
						}
					}
				};

				o = oin.readObject();
				oin.close();
			}
			break;

			case NULL:
			{
				o = null;
			}
			break;

			case BYTE:
			{
				o = readByte();
			}
			break;

			case SHORT:
			{
				o = readShort();
			}
			break;

			case INTEGER:
			{
				o = readInt();
			}
			break;

			case LONG:
			{
				o = readLong();
			}
			break;

			case FLOAT:
			{
				o = readFloat();
			}
			break;

			case DOUBLE:
			{
				o = readDouble();
			}
			break;

			case BOOLEAN:
			{
				o = readBoolean();
			}
			break;

			case CHAR:
			{
				o = readChar();
			}
			break;

			case STRING:
			{
				o = readUTF();
			}
			break;

			case SQL_DATE:
			{
				o = new java.sql.Date(readLong());
			}
			break;

			case SQL_TIME:
			{
				o = new java.sql.Time(readLong());
			}
			break;

			case SQL_TIMESTAMP:
			{
				java.sql.Timestamp ts = new java.sql.Timestamp(readLong());
				ts.setNanos(readInt());
				o = ts;
			}
			break;

			case UTIL_DATE:
			{
				o = new java.util.Date(readLong());
			}
			break;

			case VERSION:
			{
				o = new Version(readUTF());
			}
			break;

			case BLOB:
			{
				int len = (int)readLong();
				byte[] b = new byte[len];
				int read = read(b);
				while(read < len)
				{
					read += read(b,read,len - read);
				}
				o = b;
			}
			break;

			case CLOB:
			{
				int len = (int)readLong();
				char[] c = new char[len];
				for(int i = 0; i < len; i++)
				{
					c[i] = readChar();
				}
				o = c;
			}
			break;
		}

		return o;
	}
}
