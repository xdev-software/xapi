package xdev.util.dummies;

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

import org.junit.Ignore;

import xdev.util.Comparer;


/**
 * @author jwill.
 * Comparerclass for method {@link ArrayUtils#contains(Object[], Object, Comparer).
 */
@Ignore
public class Person implements Comparer<Person> 
{
	public Person()
	{
		
	}
	
	private String vorname;
	private String name;
	

	@Override
	public boolean equals(Person obj1, Person obj2) 
	{
		if(obj1.vorname == obj2.vorname && obj1.name == obj2.name)
			return true;
		
		return false;
	}
	
	public Person(String vorname, String name)
	{
		this.vorname = vorname;
		this.name = name;
	}

}
