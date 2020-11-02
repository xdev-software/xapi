
package xdev.vt.dummies;

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
