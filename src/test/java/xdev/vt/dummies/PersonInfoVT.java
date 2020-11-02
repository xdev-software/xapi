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

import xdev.db.DataType;
import xdev.lang.StaticInstanceSupport;
import xdev.ui.text.TextFormat;
import xdev.vt.VirtualTable;
import xdev.vt.VirtualTableColumn;


@Ignore
public class PersonInfoVT extends VirtualTable implements StaticInstanceSupport
{
	
	public final static VirtualTableColumn<Integer>	id;
	public final static VirtualTableColumn<String>	Notiz;
	
	static
	{
		id = new VirtualTableColumn<Integer>("id");
		id.setType(DataType.INTEGER);
		id.setDefaultValue(null);
		id.setPreferredWidth(100);
		id.setTextFormat(TextFormat.getPlainInstance());
		
		Notiz = new VirtualTableColumn<String>("Notiz");
		Notiz.setType(DataType.VARCHAR,100);
		Notiz.setDefaultValue(null);
		Notiz.setPreferredWidth(100);
		Notiz.setTextFormat(TextFormat.getPlainInstance());
	}
	

	public PersonInfoVT()
	{
		super(PersonInfoVT.class.getName(),"PersonInfoVT",id,Notiz);
		
		setPrimaryColumn(id);
		
	}
	
	public final static PersonInfoVT	VT	= new PersonInfoVT();
	

	public static PersonInfoVT getInstance()
	{
		return VT;
	}
	
}
