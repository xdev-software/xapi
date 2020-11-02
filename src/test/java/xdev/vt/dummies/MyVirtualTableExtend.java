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


import java.util.Locale;

import org.junit.Ignore;

import xdev.db.DataType;
import xdev.lang.StaticInstanceSupport;
import xdev.ui.text.TextFormat;
import xdev.vt.VirtualTable;
import xdev.vt.VirtualTableColumn;
import xdev.vt.XdevBlob;
import xdev.vt.XdevClob;

@Ignore
public class MyVirtualTableExtend
extends VirtualTable implements StaticInstanceSupport 
{
	
	
	public final static VirtualTableColumn<Integer> id;
	public final static VirtualTableColumn<XdevBlob> blob;
	public final static VirtualTableColumn<XdevClob> clob;
	public final static VirtualTableColumn<Byte> tint;
	public final static VirtualTableColumn<Short> sint;
	public final static VirtualTableColumn<Long> bint;
	public final static VirtualTableColumn<Float> real;
	public final static VirtualTableColumn<byte[]> bin;
	public final static xdev.vt.VirtualTableColumn<String> Name;
	
	
	
	static
	{
		
				
		id = new VirtualTableColumn<Integer>("id");
		id.setType(DataType.INTEGER);
		id.setNullable(true);
		id.setDefaultValue(0);
		id.setAutoIncrement(true);
		id.setPreferredWidth(100);
		id.setTextFormat(TextFormat.getPlainInstance());
		
		blob = new VirtualTableColumn<XdevBlob>("blob");
		blob.setType(DataType.BLOB,50);
		blob.setNullable(true);
		blob.setDefaultValue("");
		blob.setPreferredWidth(100);
		blob.setTextFormat(TextFormat.getPlainInstance());
		
		clob = new VirtualTableColumn<XdevClob>("clob");
		clob.setType(DataType.CLOB,50);
		clob.setNullable(true);
		clob.setDefaultValue("");
		clob.setPreferredWidth(100);
		clob.setTextFormat(TextFormat.getPlainInstance());
		
		tint = new VirtualTableColumn<Byte>("tint");
		tint.setType(DataType.TINYINT);
		tint.setNullable(false);
		tint.setDefaultValue(0);
		tint.setPreferredWidth(100);
		tint.setTextFormat(TextFormat.getNumberInstance(Locale.getDefault(),null,2,2,true,false));
		
		sint = new VirtualTableColumn<Short>("sint");
		sint.setType(DataType.SMALLINT);
		sint.setNullable(false);
		sint.setDefaultValue(0);
		sint.setPreferredWidth(100);
		sint.setTextFormat(TextFormat.getNumberInstance(Locale.getDefault(),null,2,2,true,false));
		
		bint = new VirtualTableColumn<Long>("bint");
		bint.setType(DataType.BIGINT);
		bint.setNullable(false);
		bint.setDefaultValue(0);
		bint.setPreferredWidth(100);
		bint.setTextFormat(TextFormat.getNumberInstance(Locale.getDefault(),null,2,2,true,false));
		
		real = new VirtualTableColumn<Float>("real");
		real.setType(DataType.REAL);
		real.setNullable(false);
		real.setDefaultValue(0.0);
		real.setPreferredWidth(100);
		real.setTextFormat(TextFormat.getNumberInstance(Locale.getDefault(),null,2,2,true,false));
		
		bin = new VirtualTableColumn<byte[]>("bin");
		bin.setType(DataType.BINARY,50);
		bin.setNullable(true);
		bin.setDefaultValue("");
		bin.setPreferredWidth(100);
		bin.setTextFormat(TextFormat.getPlainInstance());
		
		Name = new xdev.vt.VirtualTableColumn<String>("Name");
		Name.setType(xdev.db.DataType.VARCHAR,50);
		Name.setNullable(true);
		Name.setDefaultValue("hallo");
		Name.setPreferredWidth(100);
		Name.setTextFormat(xdev.ui.text.TextFormat.getPlainInstance());
	}
	
	
	public MyVirtualTableExtend()
	{
		super(MyVirtualTableExtend.class.getName(),"MyVirtualTableExtend",id,blob,clob,tint,sint,bint,real,bin,Name);
	
		//setPrimaryColumn(id);
	
	}
	
	
	public final static MyVirtualTableExtend VT = new MyVirtualTableExtend();
	
	
	public static MyVirtualTableExtend getInstance()
	{
		return VT;
	}
	

}
