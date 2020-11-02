package xdev.db.servlet;

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


import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;

import xdev.io.XdevObjectInputStream;
import xdev.io.XdevObjectOutputStream;

@Deprecated
abstract class Exchange<Response> implements ServletServiceConstants
{
	private SessionInfo				info;
	private URLConnection			con;
	private XdevObjectInputStream	in;
	private XdevObjectOutputStream	out;
	
	
	Exchange(SessionInfo info) throws Exception
	{
		setSessionInfo(info);
	}
	
	
	void setSessionInfo(SessionInfo info) throws Exception
	{
		this.info = info;
		
		StringBuilder sb = new StringBuilder(info.getURL());
		sb.append("?");
		sb.append(SESSIONID_PARAM);
		sb.append("=");
		sb.append(info.getSessionID());
		
		con = new URL(sb.toString()).openConnection();
		con.setDoOutput(true);
		
		Cipher cipher = Cipher.getInstance(MAC_ALGORITHM);
		cipher.init(Cipher.ENCRYPT_MODE,info.getSecretKey());
		
		out = new XdevObjectOutputStream(new CipherOutputStream(con.getOutputStream(),cipher));
		out.write(HANDSHAKE);
		out.writeUTF(info.getAuthKey());
	}
	
	
	final Response exec() throws Exception
	{
		try
		{
			sendRequest(out);
			
			out.close();
			out = null;
			
			InputStream conin = con.getInputStream();
			byte responseType = (byte)conin.read();
			switch(responseType)
			{
				case RESPONSE_TYPE_0:
				{
					in = new XdevObjectInputStream(conin);
				}
				break;
				
				case RESPONSE_TYPE_1:
				{
					Cipher cipher = Cipher.getInstance(MAC_ALGORITHM);
					cipher.init(Cipher.DECRYPT_MODE,info.getSecretKey());
					
					in = new XdevObjectInputStream(new CipherInputStream(conin,cipher),
							info.getDataSource().original);
				}
				break;
			}
			
			byte response = in.readByte();
			
			switch(response)
			{
				case RESPONSE_EXCEPTION:
				{
					throw (Exception)in.readObject();
				}
			}
			
			return readResponse(in);
		}
		finally
		{
			try
			{
				if(out != null)
				{
					out.close();
				}
				
				if(in != null)
				{
					in.close();
				}
			}
			catch(Exception e)
			{
			}
		}
	}
	
	
	abstract void sendRequest(XdevObjectOutputStream out) throws Exception;
	
	
	abstract Response readResponse(XdevObjectInputStream in) throws Exception;
}
