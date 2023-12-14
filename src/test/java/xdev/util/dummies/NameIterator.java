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
package xdev.util.dummies;


import static org.junit.Assert.fail;

import java.util.Iterator;
import java.util.NoSuchElementException;

import org.junit.Ignore;


/**
 * This iterator include this data as string:
 * 
 * <ul>
 * <li>Hans</li>
 * <li>Fritz</li>
 * <li>Egon</li>
 * <li>Berta</li>
 * <li>Sebi</li>
 * </ul>
 * <br>
 * <br>
 * 
 * 
 * @author XDEV Software (FHAE)
 * 
 */
@Ignore
public class NameIterator implements Iterator<String>
{
	
	final String[]	values		= {"Hans","Fritz","Egon","Berta","Sebi"};
	int				cursorPos	= 0;
	

	@Override
	public void remove()
	{
		fail("Not yet implemented");
	}
	

	@Override
	public String next()
	{
		if(this.cursorPos < this.values.length)
		{
			return this.values[this.cursorPos++];
		}
		throw new NoSuchElementException();
	}
	

	@Override
	public boolean hasNext()
	{
		return this.cursorPos < this.values.length;
	}
	
}
