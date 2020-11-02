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


import java.net.URL;
import java.net.URLConnection;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PublicKey;
import java.security.spec.KeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.KeyAgreement;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;

import xdev.db.AbstractDBDataSource;
import xdev.db.DBException;
import xdev.db.DBMetaData;
import xdev.db.jdbc.JDBCDataSource;
import xdev.io.XdevObjectInputStream;
import xdev.io.XdevObjectOutputStream;


public class ServletDBDataSource extends AbstractDBDataSource<ServletDBDataSource> implements
		ServletServiceConstants
{
	final JDBCDataSource	original;
	private SessionInfo		sessionInfo	= null;
	
	
	public ServletDBDataSource(JDBCDataSource original)
	{
		this.original = original;
	}
	
	
	@Override
	public void setAuthKey(String authKey)
	{
		super.setAuthKey(authKey);
		
		resetSessionInfo();
	}
	
	
	@Override
	public ServletDBConnection openConnectionImpl() throws DBException
	{
		try
		{
			if(sessionInfo == null)
			{
				refreshSessionInfo();
			}
			
			return new ServletDBConnection(this,sessionInfo);
		}
		catch(Exception e)
		{
			throw new DBException(this,e);
		}
	}
	
	
	void resetSessionInfo()
	{
		sessionInfo = null;
	}
	
	
	SessionInfo refreshSessionInfo() throws Exception
	{
		return sessionInfo = createSessionInfo(getServerURL());
	}
	
	
	protected SessionInfo createSessionInfo(String url) throws Exception
	{
		KeyPairGenerator kpg = KeyPairGenerator.getInstance(KEY_EXCHANGE_ALGORITHM);
		kpg.initialize(512);
		KeyPair kp = kpg.generateKeyPair();
		
		byte[] clientPublicKey = kp.getPublic().getEncoded();
		
		URLConnection con = new URL(url).openConnection();
		con.setDoOutput(true);
		
		XdevObjectOutputStream out = new XdevObjectOutputStream(con.getOutputStream());
		out.write(HANDSHAKE);
		
		out.writeInt(clientPublicKey.length);
		for(int i = 0; i < clientPublicKey.length; i++)
		{
			out.writeByte(clientPublicKey[i]);
		}
		
		out.close();
		
		XdevObjectInputStream in = new XdevObjectInputStream(con.getInputStream());
		
		String sessionID = in.readUTF();
		
		int c = in.readInt();
		byte[] serverPublicKeyEncoded = new byte[c];
		for(int i = 0; i < c; i++)
		{
			serverPublicKeyEncoded[i] = in.readByte();
		}
		
		in.close();
		
		KeyFactory kf = KeyFactory.getInstance(KEY_EXCHANGE_ALGORITHM);
		X509EncodedKeySpec x509Spec = new X509EncodedKeySpec(serverPublicKeyEncoded);
		PublicKey serverPublicKey = kf.generatePublic(x509Spec);
		
		KeyAgreement ka = KeyAgreement.getInstance(KEY_EXCHANGE_ALGORITHM);
		ka.init(kp.getPrivate());
		ka.doPhase(serverPublicKey,true);
		
		byte secret[] = ka.generateSecret();
		SecretKeyFactory skf = SecretKeyFactory.getInstance(SECRET_KEY_ALGORITHM);
		
		KeySpec keySpec = new DESedeKeySpec(secret);
		SecretKey secretKey = skf.generateSecret(keySpec);
		
		String authKey = ServletUtils.hashKey(getAuthKey());
		return new SessionInfo(this,url,sessionID,authKey,secretKey);
	}
	
	
	@Override
	public void closeAllOpenConnections() throws DBException
	{
	}
	
	
	@Override
	public void clearCaches()
	{
		sessionInfo = null;
	}
	
	
	@Override
	public Parameter[] getDefaultParameters()
	{
		return new Parameter[]{SERVER_URL.clone(),AUTH_KEY.clone()};
	}
	
	
	@Override
	public DBMetaData getMetaData() throws DBException
	{
		ServletDBConnection connection = (ServletDBConnection)openConnection();
		try
		{
			DBMetaData meta = connection.getMetaData();
			return new ServletDBMetaData(this,meta);
		}
		finally
		{
			connection.close();
		}
	}
	
	
	@Override
	public boolean canExport()
	{
		return original.canExport();
	}
}
