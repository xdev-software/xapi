package xdev.util.auth;

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


import java.security.MessageDigest;
import java.security.Security;
import java.util.Arrays;
import java.util.Set;

import xdev.io.IOUtils;
import xdev.util.logging.LoggerFactory;
import xdev.util.logging.XdevLogger;


/**
 * 
 * This class holds an immutable plain text password and / or a fingerprint of
 * it.
 * <p>
 * To match a string (plain text) or a byte array (fingerprint) with this
 * password use the <code>matches</code> methods.
 * <p>
 * To retrieve the fingerprint use {@link #getHash()}.<br>
 * The fingerprint is computed with a specified algorithm, e.g. SHA = default,
 * MD5, ...
 * 
 * @see #matches(byte[])
 * @see #matches(String)
 * @see #getHash()
 * 
 * @author XDEV Software
 * @since 3.0
 */

public class Password
{
	
	/**
	 * Logger instance for this class.
	 */
	private static final XdevLogger	log					= LoggerFactory.getLogger(Password.class);
	
	/**
	 * The default algorithm for the fingerprint calculation: SHA.
	 */
	public final static String		DEFAULT_ALGORITHM	= "SHA";
	
	
	/**
	 * Returns an array of Strings containing all supported
	 * {@link MessageDigest} algorithms used by the {@link Password} class.
	 * 
	 * @return all supported {@link MessageDigest} algorithms
	 */
	public static String[] getAlgorithms()
	{
		Set<String> set = Security.getAlgorithms("MessageDigest");
		String[] algorithms = set.toArray(new String[set.size()]);
		Arrays.sort(algorithms);
		return algorithms;
	}
	
	private final String	plainText;
	private final String	algorithm;
	private final byte[]	hash;
	
	
	/**
	 * Creates a plain text password, the fingerprint is computed with the
	 * default algorithm.
	 * 
	 * @see #DEFAULT_ALGORITHM
	 * 
	 * @param plainText
	 *            The password
	 */
	
	public Password(String plainText)
	{
		this(plainText,DEFAULT_ALGORITHM);
	}
	
	
	/**
	 * Creates a plain text password, the fingerprint is computed with the
	 * specified algorithm.
	 * 
	 * @param plainText
	 *            The password
	 * @param algorithm
	 *            The algorithm to compute the fingerprint
	 * @see #getAlgorithms()
	 */
	
	public Password(String plainText, String algorithm)
	{
		this.plainText = plainText;
		this.algorithm = algorithm;
		this.hash = computeHash(plainText);
	}
	
	
	/**
	 * Creates a fingerprint password, the plaintext will never be available.
	 * 
	 * @param algorithm
	 *            The algorithm which the fingerprint was calculated with
	 * @param hash
	 *            The password's fingerprint
	 * @see #getAlgorithms()
	 */
	
	public Password(String algorithm, byte[] hash)
	{
		this(null,algorithm,hash);
	}
	
	
	/**
	 * Creates a password with the plain text, its fingerprint and the algorithm
	 * which the fingerprint was calculated with.
	 * <p>
	 * The constructor doesn't double check if the hash is valid according to
	 * the algorithm.
	 * 
	 * @param plainText
	 *            The password
	 * @param algorithm
	 *            The password's fingerprint
	 * @param hash
	 *            The algorithm which the fingerprint was calculated with
	 * @see #getAlgorithms()
	 */
	
	public Password(String plainText, String algorithm, byte[] hash)
	{
		this.plainText = plainText;
		this.algorithm = algorithm;
		this.hash = hash;
	}
	
	
	/**
	 * @param plainText
	 *            The password
	 * @return The fingerprint of the password
	 * @see MessageDigest
	 */
	
	protected byte[] computeHash(String plainText)
	{
		try
		{
			return MessageDigest.getInstance(algorithm).digest(plainText.getBytes("UTF8"));
		}
		catch(Exception e)
		{
			log.error(e);
		}
		
		return null;
	}
	
	
	/**
	 * Returns the algorithm which this password's fingerprint was calculated
	 * with.
	 * 
	 * @return The fingerprint algorithm
	 * @see #DEFAULT_ALGORITHM
	 */
	
	public String getAlgorithm()
	{
		return algorithm;
	}
	
	
	/**
	 * @return This password's fingerprint
	 */
	
	public byte[] getHash()
	{
		return hash;
	}
	
	
	/**
	 * Returns this password's fingerpring as a proper hash string.
	 * 
	 * @return This password's fingerpring as string.
	 */
	public String getHashString()
	{
		return IOUtils.toHashString(hash,algorithm);
	}
	
	
	/**
	 * @return This password as plain text or <code>null</code> if only the
	 *         fingerprint is avaliable.
	 */
	
	public String getPlainText()
	{
		return plainText;
	}
	
	
	/**
	 * Compares this password's plain text with <code>plainText</code> if
	 * available.<br>
	 * Otherwise the fingerprint of <code>plainText</code> is computed and
	 * compared with this password's fingerprint.
	 * 
	 * @param plainText
	 *            The password to match
	 * @return <code>true</code> if this password matches the parameter,
	 *         <code>false</code> otherwise
	 * 
	 * @see #matches(byte[])
	 */
	
	public boolean matches(String plainText)
	{
		if(this.plainText == null)
		{
			return matches(computeHash(plainText));
		}
		
		return this.plainText.equals(plainText);
	}
	
	
	/**
	 * Compares this password's fingerprint with <code>fingerprint</code>.
	 * 
	 * @param hash
	 *            A fingerprint
	 * @return <code>true</code> if this password's fingerprint matches the
	 *         parameter, <code>false</code> otherwise
	 */
	
	public boolean matches(byte[] hash)
	{
		return Arrays.equals(this.hash,hash);
	}
	
	
	@Override
	public boolean equals(Object obj)
	{
		if(obj == this)
		{
			return true;
		}
		
		if(obj instanceof Password)
		{
			Password pw = (Password)obj;
			return Arrays.equals(hash,pw.hash);
		}
		
		return false;
	}
	
	int	_hashCode	= 0;
	
	
	@Override
	public int hashCode()
	{
		if(_hashCode == 0)
		{
			_hashCode = Arrays.hashCode(hash);
		}
		
		return _hashCode;
	}
}
