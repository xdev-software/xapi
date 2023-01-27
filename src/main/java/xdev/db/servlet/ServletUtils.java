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
package xdev.db.servlet;


import java.math.BigInteger;
import java.security.CodeSource;
import java.security.MessageDigest;
import java.security.ProtectionDomain;
import java.security.cert.Certificate;


class ServletUtils
{
	private ServletUtils()
	{
	}
	

	static String computeKey(Class clazz)
	{
		try
		{
			ProtectionDomain domain = clazz.getProtectionDomain();
			if(domain == null)
			{
				return null;
			}
			
			CodeSource source = domain.getCodeSource();
			if(source == null)
			{
				return null;
			}
			
			Certificate[] certs = source.getCertificates();
			if(certs == null || certs.length == 0)
			{
				return null;
			}
			
			byte[] key = MessageDigest.getInstance("SHA-1").digest(certs[0].getEncoded());
			return new BigInteger(1,key).toString(Character.MAX_RADIX);
		}
		catch(Throwable t)
		{
			return null;
		}
	}
	

	static String hashKey(String key)
	{
		try
		{
			return new BigInteger(1,MessageDigest.getInstance("SHA-1").digest(key.getBytes("UTF8")))
					.toString(Character.MAX_RADIX);
		}
		catch(Exception e)
		{
			// NoSuchAlgorithmException, UnsupportedEncodingException
			// shouldn't happen
			return key;
		}
	}
}
