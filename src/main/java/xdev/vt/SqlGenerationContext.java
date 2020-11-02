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


import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import xdev.db.sql.Table;


class SqlGenerationContext
{
	private Set<EntityRelationship>			joinedRelations	= new HashSet();
	private Map<EntityRelationship, Table>	relationTables	= new HashMap();
	private Map<VirtualTable, Integer>		aliasCounter	= new HashMap();
	
	
	boolean join(EntityRelationship relation)
	{
		if(joinedRelations.contains(relation))
		{
			return false;
		}
		
		joinedRelations.add(relation);
		return true;
	}
	
	
	Table getTableExpression(EntityRelationship relation, VirtualTable joinedTable)
	{
		Table table = relationTables.get(relation);
		if(table == null)
		{
			table = getTableExpression(joinedTable);
			relationTables.put(relation,table);
		}
		return table;
	}
	
	
	private Table getTableExpression(VirtualTable vt)
	{
		Integer nr = aliasCounter.get(vt);
		if(nr == null)
		{
			aliasCounter.put(vt,1);
			return vt;
		}
		
		nr++;
		aliasCounter.put(vt,nr);
		return vt.AS(vt.getTableName() + nr);
	}
}
