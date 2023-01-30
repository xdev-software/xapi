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


import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.util.Locale;

import org.junit.Ignore;

import xdev.ui.text.TextFormat;


@Ignore
public class PersonVT2 extends xdev.vt.VirtualTable implements xdev.lang.StaticInstanceSupport
{
	
	public static xdev.vt.VirtualTableColumn<Integer>				id;
	public static xdev.vt.VirtualTableColumn<String>				Name;
	public static xdev.vt.VirtualTableColumn<Integer>				Gehalt;
	public static xdev.vt.VirtualTableColumn<Double>				Gewicht;
	public static xdev.vt.VirtualTableColumn<java.sql.Date>			Geburtsdatum;
	public static xdev.vt.VirtualTableColumn<java.sql.Time>			Geburtszeit;
	public static xdev.vt.VirtualTableColumn<java.sql.Timestamp>	GeburtsTimeStamp;
	public static xdev.vt.VirtualTableColumn<Boolean>				Krank;
	
	public final static String										defaultDBAlias		= "myDBAlias";
	public final static String										defaultVTName		= PersonVT2.class
																								.getName();
	
	public final static int											defaultColumnCount	= 8;
	
	static
	{
		id = new xdev.vt.VirtualTableColumn<Integer>("id");
		id.setType(xdev.db.DataType.INTEGER);
		id.setNullable(false);
		id.setAutoIncrement(true);
		id.setDefaultValue(0);
		id.setPreferredWidth(50);
		id.setTextFormat(xdev.ui.text.TextFormat.getPlainInstance());
		id.setVisible(false);
		
		Name = new xdev.vt.VirtualTableColumn<String>("Name");
		Name.setType(xdev.db.DataType.VARCHAR,50);
		Name.setNullable(true);
		Name.setDefaultValue("");
		Name.setPreferredWidth(0);
		Name.setTextFormat(xdev.ui.text.TextFormat.getPlainInstance());
		
		Gehalt = new xdev.vt.VirtualTableColumn<Integer>("Gehalt");
		Gehalt.setType(xdev.db.DataType.INTEGER);
		Gehalt.setNullable(true);
		Gehalt.setDefaultValue(null);
		// Gehalt.setPreferredWidth(100);
		Gehalt.setTextFormat(TextFormat.getCurrencyInstance(Locale.getDefault(),null,2,2,true,false));
		
		Gewicht = new xdev.vt.VirtualTableColumn<Double>("Gewicht");
		Gewicht.setType(xdev.db.DataType.DOUBLE);
		Gewicht.setNullable(true);
		Gewicht.setDefaultValue(null);
		// Gewicht.setPreferredWidth(100);
		Gewicht.setTextFormat(TextFormat.getNumberInstance(Locale.getDefault(),null,2,2,true,false));
		
		Geburtsdatum = new xdev.vt.VirtualTableColumn<Date>("Geburtsdatum");
		Geburtsdatum.setType(xdev.db.DataType.DATE);
		Geburtsdatum.setNullable(true);
		Geburtsdatum.setDefaultValue(null);
		// Geburtsdatum.setPreferredWidth(100);
		Geburtsdatum.setTextFormat(TextFormat.getDateInstance(Locale.getDefault(),null,
				TextFormat.USE_DATE_ONLY,DateFormat.MEDIUM,-1));
		
		Geburtszeit = new xdev.vt.VirtualTableColumn<Time>("Geburtszeit");
		Geburtszeit.setType(xdev.db.DataType.TIME);
		Geburtszeit.setNullable(true);
		Geburtszeit.setDefaultValue(null);
		// Geburtszeit.setPreferredWidth(100);
		Geburtszeit.setTextFormat(TextFormat.getDateInstance(Locale.getDefault(),null,
				TextFormat.USE_TIME_ONLY,-1,DateFormat.MEDIUM));
		
		GeburtsTimeStamp = new xdev.vt.VirtualTableColumn<Timestamp>("GeburtsTimeStamp");
		GeburtsTimeStamp.setType(xdev.db.DataType.TIMESTAMP);
		GeburtsTimeStamp.setNullable(true);
		GeburtsTimeStamp.setDefaultValue(null);
		// GeburtsTimeStamp.setPreferredWidth(100);
		GeburtsTimeStamp.setTextFormat(TextFormat.getDateInstance(Locale.getDefault(),null,
				TextFormat.USE_DATE_AND_TIME,DateFormat.SHORT,DateFormat.SHORT));
		
		Krank = new xdev.vt.VirtualTableColumn<Boolean>("Krank");
		Krank.setType(xdev.db.DataType.BOOLEAN);
		Krank.setNullable(true);
		Krank.setDefaultValue("");
		// Krank.setPreferredWidth(100);
		Krank.setTextFormat(xdev.ui.text.TextFormat.getPlainInstance());
	}
	

	public PersonVT2()
	{
		super(defaultVTName,defaultDBAlias,id,Name,Gehalt,Gewicht,Geburtsdatum,Geburtszeit,
				GeburtsTimeStamp,Krank);
		setPrimaryColumn(id);
	}
	
	public final static PersonVT2	VT	= new PersonVT2();
	

	public static PersonVT2 getInstance()
	{
		return VT;
	}
	
}
