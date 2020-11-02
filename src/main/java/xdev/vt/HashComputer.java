package xdev.vt;

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


import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.LinkedHashMap;

import xdev.util.logging.LoggerFactory;
import xdev.util.logging.XdevLogger;


final class HashComputer
{
	
	/**
	 * Logger instance for this class.
	 */
	private static final XdevLogger	log	= LoggerFactory.getLogger(HashComputer.class);
	
	
	private HashComputer()
	{
	}
	
	final static String				EMPTY_HASH	= "";
	
	private static MessageDigest	digest;
	static
	{
		try
		{
			digest = MessageDigest.getInstance("SHA-1");
		}
		catch(NoSuchAlgorithmException e)
		{
			log.error(e);
		}
	}
	
	
	static String computeHash(LinkedHashMap<String, Object> map, boolean ignoreNulls)
	{
		synchronized(digest)
		{
			digest.reset();
			boolean hasContent = false;
			
			for(String key : map.keySet())
			{
				Object value = map.get(key);
				if(value == null && ignoreNulls)
				{
					continue;
				}
				
				hasContent = true;
				
				int v = hashCode(key);
				digest.update((byte)((v >>> 24) & 0xFF));
				digest.update((byte)((v >>> 16) & 0xFF));
				digest.update((byte)((v >>> 8) & 0xFF));
				digest.update((byte)((v >>> 0) & 0xFF));
				v = hashCode(value);
				digest.update((byte)((v >>> 24) & 0xFF));
				digest.update((byte)((v >>> 16) & 0xFF));
				digest.update((byte)((v >>> 8) & 0xFF));
				digest.update((byte)((v >>> 0) & 0xFF));
			}
			
			if(hasContent)
			{
				return new BigInteger(1,digest.digest()).toString(Character.MAX_RADIX);
			}
			
			return EMPTY_HASH;
		}
	}
	
	
	private static int hashCode(Object obj)
	{
		return obj == null ? Integer.MIN_VALUE : obj.hashCode();
	}
}
