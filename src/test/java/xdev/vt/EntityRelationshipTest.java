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
package xdev.vt;

import org.junit.Assert;


import org.junit.Test;


public class EntityRelationshipTest
{
	@Test
	public void equality()
	{
		EntityRelationship er1 = new EntityRelationship("customer","address_id",Cardinality.MANY,
				"address","id",Cardinality.ONE);
		EntityRelationship er2 = new EntityRelationship("address","id",Cardinality.ONE,"customer",
				"address_id",Cardinality.MANY);
		Assert.assertEquals(er1,er2);
		Assert.assertEquals(er1.hashCode(),er2.hashCode());
	}
}
