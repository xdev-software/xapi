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


import java.util.ArrayList;

import org.junit.Ignore;


/**
 * This {@link ArrayList} include {@link Person}s:
 * 
 * <ul>
 * <li>Felix, Mustermann</li>
 * <li>Egon, Schmidt</li>
 * <li>Sebastian, Hartl</li>
 * <li>Thomas, Schmid</li>
 * </ul>
 * <br>
 * <br>
 * 
 * 
 * @author XDEV Software (FHAE)
 * 
 */
@Ignore
public class PersonList extends ArrayList<Person>
{
	{
		add(new Person("Felix","Mustermann"));
		add(new Person("Egon","Schmidt"));
		add(new Person("Sebastian","Hartl"));
		add(new Person("Thomas","Schmid"));
	}
}
