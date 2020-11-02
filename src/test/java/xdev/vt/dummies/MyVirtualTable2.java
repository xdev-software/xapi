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

@Ignore
public class MyVirtualTable2 extends xdev.vt.VirtualTable implements xdev.lang.StaticInstanceSupport
{
	public final static xdev.vt.VirtualTableColumn<Integer>			id;
	public final static xdev.vt.VirtualTableColumn<String>			Name;

	
	static
	{
		id = new xdev.vt.VirtualTableColumn("id");
		id.setType(xdev.db.DataType.INTEGER);
		id.setNullable(false);
		id.setPreferredWidth(0);
		id.setTextFormat(xdev.ui.text.TextFormat.getPlainInstance());
		id.setVisible(false);
		id.setDefaultValue(0);
		
		Name = new xdev.vt.VirtualTableColumn("Name");
		Name.setType(xdev.db.DataType.VARCHAR,50);
		Name.setNullable(true);
		Name.setDefaultValue("hallo");
		Name.setPreferredWidth(100);
		Name.setTextFormat(xdev.ui.text.TextFormat.getPlainInstance());
	}
	

	public MyVirtualTable2()
	{
		super(MyVirtualTable2.class.getName(),"myVirtualTable2",id,Name);
		
		setPrimaryColumn(id);
		
		//addIndex(new Index("SYS_PK_10048",IndexType.PRIMARY_KEY,"id"));
		
	}
	
	public final static MyVirtualTable2	VT	= new MyVirtualTable2();
	

	public static MyVirtualTable2 getInstance()
	{
		return VT;
	}
}
