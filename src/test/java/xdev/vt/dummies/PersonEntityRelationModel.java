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

package xdev.vt.dummies;


import org.junit.Ignore;

import xdev.lang.StaticInstanceSupport;
import xdev.vt.Cardinality;
import xdev.vt.EntityRelationshipModel;


@Ignore
public class PersonEntityRelationModel extends EntityRelationshipModel implements
		StaticInstanceSupport
{
	
	private static PersonEntityRelationModel	instance	= null;
	

	public static PersonEntityRelationModel getInstance()
	{
		if(instance == null)
		{
			instance = new PersonEntityRelationModel();
		}
		return instance;
	}
	
	{
		add(PersonInfoVT.class.getName(),PersonInfoVT.id.getName(),Cardinality.MANY,
				PersonVT.class.getName(),PersonVT.id.getName(),Cardinality.ONE);
	}
	
}
