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
package xdev.util.auth;


import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;


/**
 * Encrypted version of {@link Password} which is used to avoid plain text
 * passwords in generated source code.
 * 
 * @author XDEV Software
 * @since 3.0
 */

public class EncryptedPassword extends Password
{
	private final static Encrypter	encrypter	= new Encrypter();
	

	/**
	 * Constructs an encrypted password.
	 * 
	 * 
	 * @param encryptedText
	 *            The encrypted password
	 * @see #encrypt(String)
	 */
	
	public EncryptedPassword(String encryptedText)
	{
		super(encrypter.decrypt(encryptedText));
	}
	

	/**
	 * Encrypts the <code>passPhrase</code>.
	 * 
	 * @param passPhrase
	 *            The password to encrypt.
	 * @return The encrypted password.
	 * @see #EncryptedPassword(String)
	 */
	
	public static String encrypt(String passPhrase)
	{
		return encrypter.encrypt(passPhrase);
	}
	


	private final static class Encrypter
	{
		final String	STRING_ENCODING	= "UTF8";
		final char[]	PASS_PHRASE		= Encrypter.class.getName().toCharArray();
		final byte[]	SALT			= {(byte)0x28,(byte)0x3C,(byte)0x73,(byte)0x6,(byte)0x50,
												(byte)0x14,(byte)0x66,(byte)0x65};
		final int		ITERATION_COUNT	= 23;
		
		final Cipher	ecipher;
		final Cipher	dcipher;
		

		Encrypter()
		{
			try
			{
				KeySpec keySpec = new PBEKeySpec(PASS_PHRASE,SALT,ITERATION_COUNT);
				SecretKey key = SecretKeyFactory.getInstance("PBEWithMD5AndDES").generateSecret(
						keySpec);
				
				ecipher = Cipher.getInstance(key.getAlgorithm());
				dcipher = Cipher.getInstance(key.getAlgorithm());
				
				AlgorithmParameterSpec paramSpec = new PBEParameterSpec(SALT,ITERATION_COUNT);
				
				ecipher.init(Cipher.ENCRYPT_MODE,key,paramSpec);
				dcipher.init(Cipher.DECRYPT_MODE,key,paramSpec);
			}
			catch(InvalidAlgorithmParameterException e)
			{
				throw new IllegalArgumentException("invalid algorithm parameter",e);
			}
			catch(InvalidKeySpecException e)
			{
				throw new IllegalArgumentException("invalid key spec",e);
			}
			catch(NoSuchPaddingException e)
			{
				throw new IllegalArgumentException("no such padding",e);
			}
			catch(NoSuchAlgorithmException e)
			{
				throw new IllegalArgumentException("no such algorithm",e);
			}
			catch(InvalidKeyException e)
			{
				throw new IllegalArgumentException("invalid key",e);
			}
		}
		

		String encrypt(String str)
		{
			try
			{
				byte[] charValues = str.getBytes(STRING_ENCODING);
				byte[] enc = ecipher.doFinal(charValues);
				return Base64.getEncoder().encodeToString(enc);
			}
			catch(BadPaddingException e)
			{
			}
			catch(IllegalBlockSizeException e)
			{
			}
			catch(UnsupportedEncodingException e)
			{
			}
			return null;
		}
		

		String decrypt(String str)
		{
			try
			{
				byte[] dec = Base64.getDecoder().decode(str);
				byte[] charValues = dcipher.doFinal(dec);
				return new String(charValues,STRING_ENCODING);
			}
			catch(BadPaddingException e)
			{
			}
			catch(IllegalBlockSizeException e)
			{
			}
			catch(UnsupportedEncodingException e)
			{
			}
			catch(IOException e)
			{
			}
			
			return null;
		}
	}
}
