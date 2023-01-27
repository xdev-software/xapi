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

import xdev.db.DBException;
import xdev.db.DataType;
import xdev.db.Index;
import xdev.db.Index.IndexType;
import xdev.lang.StaticInstanceSupport;
import xdev.ui.text.TextFormat;
import xdev.vt.VirtualTable;
import xdev.vt.VirtualTableColumn;

@Ignore
public class TableTree
extends VirtualTable implements StaticInstanceSupport 
{

	private static final long serialVersionUID = 1L;

	private static String[] abc = {"A","B","C","D","E","F","G","H","I","J","K","L","M",
	  "N","O","P","Q","R","S","T","U","V","W","X","Y","Z"};
	
	private static int anzEbenenMin = 1;
	
	
	private static int anzElemMin = 2;
	
	private static int ebenmerk = anzEbenenMin;
	
	private static int autoinc = 1;
	
	
	public void VtFillrek()
	{
		
		for(int i=0; i<anzElemMin; i++)
		{
			String ausgabe = abc[i] + " root"+i;
			
			Object[] root = {autoinc,"",ausgabe};
			autoinc++;
			
			try
			{
				this.addRow(false, root );
			}
			catch(DBException ex)
			{
				ex.printStackTrace();
			}
			
			int rootid = (Integer) this.getValueAt(this.getRowCount()-1,"id");
			erzeugeEbenen(rootid,anzEbenenMin);
			ebenmerk = anzEbenenMin;
			
		} 
		
		
	}
		public void erzeugeEbenen(int rootid,int anzEbene)
		{
				int rootidm = rootid-1;
				int z = rootidm;
				String output;
				String roots = (String)this.getValueAt(z,"Bezeichnung");
				
				if(anzEbene > 0)
				{	
					for(int i = 0; i<anzElemMin;i++)
					{	
						ebenmerk = anzEbenenMin;
						ebenmerk -= anzEbene;
						
						output = abc[i] + " Child von " + roots + " Ebene "+ebenmerk;
						
						Object[] element = {autoinc,rootid,output};	
						autoinc++;
						
						try
						{
							this.addRow(false, element );
						}
						catch(DBException ex)
						{
							ex.printStackTrace();
						}
							
						//ruft sich jedes mal selbst auf doch wenn anzebene>0 gleich return und added dann das naechste element der naechst hoeheren ebene->usw.	
									int rootidE = (Integer) this.getValueAt(this.getRowCount()-1,"id");
									
									//System.out.println(anzEbene+abc[i]);
									
									erzeugeEbenen(rootidE,anzEbene-1);
				
					   //nach der rekursion ist der wert (anzEbene) wieder der standart wert!!! deswegen koennen beliebig viele elemente abgehandelt werden.		
						
					}
					}
					else 
					return;
				}
	
	
	public final static VirtualTableColumn<Integer> id;
	public final static VirtualTableColumn<Integer> childId;
	public final static VirtualTableColumn<String> Bezeichnung;
	
	static
	{
//		id = new VirtualTableColumn<Integer>("id");
//		id.setType(DataType.INTEGER);
//		id.setAutoIncrement(true);
//		//id.setDefaultValue(null);
//		id.setPreferredWidth(100);
//		id.setTextFormat(TextFormat.getNumberInstance(Locale.getDefault(),null,2,2,true,false));
		id = new VirtualTableColumn<Integer>("id");
		id.setType(DataType.INTEGER);
		id.setAutoIncrement(false);
		id.setDefaultValue(null);
		id.setPreferredWidth(100);
		id.setTextFormat(TextFormat.getPlainInstance());
		
		childId = new VirtualTableColumn<Integer>("childId");
		childId.setType(DataType.INTEGER);
		childId.setDefaultValue(0);
		childId.setPreferredWidth(100);
		childId.setTextFormat(TextFormat.getPlainInstance());
		
		Bezeichnung = new VirtualTableColumn<String>("Bezeichnung");
		Bezeichnung.setType(DataType.VARCHAR,900);
		Bezeichnung.setDefaultValue("");
		Bezeichnung.setPreferredWidth(100);
		Bezeichnung.setTextFormat(TextFormat.getPlainInstance());
	}
	
	public TableTree()
	{
		super(TableTree.class.getName(),"TableTree",id,childId,Bezeichnung);
	
		setPrimaryColumn(id);
	
		addIndex(new Index("PK",IndexType.PRIMARY_KEY,"id"));
	}
	
	public final static TableTree VT = new TableTree();
	
	public static TableTree getInstance()
	{
		return VT;
	}
	
	public final static void main(String args[])
	{
		TableTree vt = new TableTree();
		
		vt.VtFillrek();
		//vt.ErzeugeTreeT(1,3);
		
		System.out.println();
	}
}
