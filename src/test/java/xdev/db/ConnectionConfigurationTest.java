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
package xdev.db;

import static org.junit.Assert.fail;

import java.io.IOException;

import org.junit.Test;


public class ConnectionConfigurationTest
{
	
	@Test
	public void testConstructor1()
	{
		ConnectionConfiguration conConfig = null;
		try
		{
			conConfig = new ConnectionConfiguration(this.getClass().getSimpleName());
			conConfig.storeProperty("host", "JUnit");
			
			if(!conConfig.getHost().equals("JUnit"))
			{
				fail("Cannot read host parameter");
			}
		}
		catch(final IOException e)
		{
			fail("Cannot store property" + e.getStackTrace());
		}
		finally
		{
			if(conConfig != null) 
			{
				conConfig.deletePropertyFile();
			}
		}
		
	}
	
	@Test
	public void testConstructor2()
	{
		ConnectionConfiguration conConfig = null;
		try
		{
			conConfig = new ConnectionConfiguration(this.getClass());
			conConfig.storeProperty("host", "JUnit");
			
			if(!conConfig.getHost().equals("JUnit"))
			{
				fail("Cannot read host parameter");
			}
		}
		catch(final IOException e)
		{
			fail("Cannot store property" + e.getStackTrace());
		}
		finally
		{
			if(conConfig != null) 
			{
				conConfig.deletePropertyFile();
			}
		}
		
	}
	
}
