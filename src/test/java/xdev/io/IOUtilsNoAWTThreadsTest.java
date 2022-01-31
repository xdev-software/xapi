
package xdev.io;

/*-
 * #%L
 * XDEV Application Framework
 * %%
 * Copyright (C) 2003 - 2022 XDEV Software
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

import java.util.Set;
import java.util.stream.Collectors;

import org.junit.Assert;
import org.junit.Test;


/**
 * Tests for the Class {@link IOUtils}.
 * 
 * @author XDEV Software (CF)
 */
public class IOUtilsNoAWTThreadsTest
{
	@Test
	public void noUIThread()
	{
		final int awtThreadsBefore = this.runnigAWTThreads().size();
		
		IOUtils.getOS();
		
		final int awtThreadsAfter = this.runnigAWTThreads().size();
		
		Assert.assertEquals(0, awtThreadsBefore);
		Assert.assertEquals(0, awtThreadsAfter);
	}
	
	private Set<Thread> runnigAWTThreads()
	{
		// @formatter:off
		return Thread.getAllStackTraces()
			.keySet()
			.stream()
			.filter(t -> t.getName().startsWith("AWT"))
			.collect(Collectors.toSet());
		// @formatter:on
	}
	
}
