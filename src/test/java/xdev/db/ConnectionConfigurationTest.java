/*
 * Copyright (C) 2007-2013 by XDEV Software, All Rights Reserved.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License version 3.0 as published by the Free Software Foundation.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */

package xdev.db;

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
