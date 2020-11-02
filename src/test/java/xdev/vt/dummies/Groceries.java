
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


import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

import org.junit.Ignore;

import xdev.db.DBException;
import xdev.db.DataType;
import xdev.lang.StaticInstanceSupport;
import xdev.ui.text.TextFormat;
import xdev.vt.VirtualTable;
import xdev.vt.VirtualTableColumn;


@Ignore
@SuppressWarnings("deprecation")
public class Groceries extends VirtualTable implements StaticInstanceSupport // аз{GENERATED-CODE-LINE:VT_SUPERCLASS}
{
	
	/**
	 * The testdata wich is filled in the VT
	 * <p>
	 * <b>Note:</b>The indexcount of each array has to be the same
	 * </p>
	 */
	
	private static Object[]							kind				= {10,12,16,18,3,20};
	private static Object[]							manufacturer		= {"Wal Mart",
			"Supermarket","Apple Store","Mircosoft Store","Sausage Store","Joghurt Store"};
	private static Object[]							quality				= {"bio","rotten",
			"gourmet","normal","cheap","special item"					};
	private static Object[]							expense				= {16.3,15.99,14.99,5.99,
			29.99,19.99													};
	private static Object[]							durability			= {new Date(90,5,11),
			new Date(50,5,3),new Date(40,3,10),new Date(50,2,4),new Date(90,2,6),new Date(80,3,4)};
	private static Object[]							weight				= {0.2,0.3,0.4,0.5,1.0,1.2};
	private static Object[]							description			= {"Beef","Sausages",
			"Chilli","Snacks","Joghurt","Pudding"						};
	private static Object[]							manufacturerland	= {"Germany","Italy",
			"Spain","Ukrain","Hawaii","Poland"							};
	private static Object[]							importland			= {"Czech Republic",
			"Austria","Australia","England","Netherlands","USA"			};
	private static Object[]							color				= {"red","yellow","grey",
			"white","brown","orange"									};
	/**
	 * The autogenerated code wich contains the creation of the VT columns
	 */
	
	// Generated code, do not edit!аз{GENERATED-CODE-BLOCK-START:VIRTUAL_TABLE}
	public final static VirtualTableColumn<Integer>	id;
	public final static VirtualTableColumn<String>	Kind;
	public final static VirtualTableColumn<String>	Manufacturer;
	public final static VirtualTableColumn<String>	Quality;
	public final static VirtualTableColumn<Date>	Durability;
	public final static VirtualTableColumn<Double>	Expense;
	public final static VirtualTableColumn<String>	Desciption;
	public final static VirtualTableColumn<Double>	Weight;
	public final static VirtualTableColumn<String>	ManufacturerCountry;
	public final static VirtualTableColumn<String>	ImportLand;
	public final static VirtualTableColumn<String>	Color;
	
	static
	{
		id = new VirtualTableColumn<Integer>("id");
		id.setType(DataType.INTEGER);
		id.setAutoIncrement(true);
		id.setDefaultValue(null);
		id.setPreferredWidth(100);
		id.setTextFormat(TextFormat.getPlainInstance());
		
		Kind = new VirtualTableColumn<String>("Kind");
		Kind.setType(DataType.VARCHAR,50);
		Kind.setDefaultValue("");
		Kind.setPreferredWidth(100);
		Kind.setTextFormat(TextFormat.getPlainInstance());
		
		Manufacturer = new VirtualTableColumn<String>("Manufacturer");
		Manufacturer.setType(DataType.VARCHAR,50);
		Manufacturer.setDefaultValue("");
		Manufacturer.setPreferredWidth(100);
		Manufacturer.setTextFormat(TextFormat.getPlainInstance());
		
		Quality = new VirtualTableColumn<String>("Quality");
		Quality.setType(DataType.VARCHAR,50);
		Quality.setDefaultValue(null);
		Quality.setPreferredWidth(100);
		Quality.setTextFormat(TextFormat.getPlainInstance());
		
		Durability = new VirtualTableColumn<Date>("Durability");
		Durability.setType(DataType.DATE);
		Durability.setDefaultValue(null);
		Durability.setPreferredWidth(100);
		Durability.setTextFormat(TextFormat.getDateInstance(Locale.getDefault(),null,
				TextFormat.USE_DATE_ONLY,DateFormat.MEDIUM,DateFormat.MEDIUM));
		
		Expense = new VirtualTableColumn<Double>("Expense");
		Expense.setType(DataType.DOUBLE);
		Expense.setDefaultValue(null);
		Expense.setPreferredWidth(100);
		Expense.setTextFormat(TextFormat.getNumberInstance(Locale.getDefault(),null,2,2,true,false));
		
		Desciption = new VirtualTableColumn<String>("Desciption");
		Desciption.setType(DataType.VARCHAR,50);
		Desciption.setDefaultValue("");
		Desciption.setPreferredWidth(100);
		Desciption.setTextFormat(TextFormat.getPlainInstance());
		
		Weight = new VirtualTableColumn<Double>("Weight");
		Weight.setType(DataType.DOUBLE);
		Weight.setDefaultValue(null);
		Weight.setPreferredWidth(100);
		Weight.setTextFormat(TextFormat.getNumberInstance(Locale.getDefault(),null,2,2,true,false));
		
		ManufacturerCountry = new VirtualTableColumn<String>("ManufacturerCountry");
		ManufacturerCountry.setType(DataType.VARCHAR,50);
		ManufacturerCountry.setDefaultValue("");
		ManufacturerCountry.setPreferredWidth(100);
		ManufacturerCountry.setTextFormat(TextFormat.getPlainInstance());
		
		ImportLand = new VirtualTableColumn<String>("ImportLand");
		ImportLand.setType(DataType.VARCHAR,50);
		ImportLand.setDefaultValue("");
		ImportLand.setPreferredWidth(100);
		ImportLand.setTextFormat(TextFormat.getPlainInstance());
		
		Color = new VirtualTableColumn<String>("Color");
		Color.setType(DataType.VARCHAR,50);
		Color.setDefaultValue("");
		Color.setPreferredWidth(100);
		Color.setTextFormat(TextFormat.getPlainInstance());
	}
	
	
	public Groceries()
	{
		super(Groceries.class.getName(),"Groceries",id,Kind,Manufacturer,Quality,Durability,
				Expense,Desciption,Weight,ManufacturerCountry,ImportLand,Color);
		
		setPrimaryColumn(id);
		
	}
	
	public final static Groceries	VT	= new Groceries();
	
	
	public static Groceries getInstance()
	{
		return VT;
	}
	
	
	// End generated codeаз{GENERATED-CODE-BLOCK-END:VIRTUAL_TABLE}
	
	/**
	 * Creates as much rows as the value of the given parameter, filled with
	 * example data
	 * 
	 * @param rowcount
	 * 
	 */
	public void VtFill(int rows)
	{
		
		// Calendar currenttime = Calendar.getInstance();
		// Date sqldate = new Date((currenttime.getTime()).getTime());
		int e = 0;
		int f = 0;
		int d = 0;
		// boolean wdh = false;
		
		for(int i = 0; i < rows; i++)
		{
			
			if(e < kind.length)
			{
				Object[] in = {kind[f],manufacturer[e],quality[d],durability[f],expense[f],
						description[f],weight[f],manufacturerland[f],importland[f],color[f]};
				
				try
				{
					this.addRow(false,in);
					
				}
				catch(DBException ex)
				{
					ex.printStackTrace();
				}
				
				if(f < kind.length - 1)
					f++;
				else
				{
					f = 0;
					if(d < kind.length - 1)
					{
						d++;
					}
					
					else
					{
						d = 0;
						e++;
					}
					
				}
			}
			
			else
			{
				e = 0;
				f = 0;
				d = 0;
				// wdh = true;
				
			}
			
		}
	}
	
	
	public final static void main(String args[])
	{
		final Groceries vt = new Groceries();
		
		// param = number of wished rows
		vt.VtFill(90);
		
	}
	
}
