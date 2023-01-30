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
package xdev.ui.dnd;


import java.awt.datatransfer.*;
import java.io.*;


class LocalTransferable implements Transferable
{
	public final static DataFlavor	DATA_FLAVOR	= new DataFlavor(
														DataFlavor.javaJVMLocalObjectMimeType,
														"LocalTransferable");
	
	public Object					data;
	

	public LocalTransferable(Object data)
	{
		this.data = data;
	}
	

	public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException
	{
		return data;
	}
	

	public boolean isDataFlavorSupported(DataFlavor flavor)
	{
		return true;
	}
	

	public DataFlavor[] getTransferDataFlavors()
	{
		return new DataFlavor[]{DATA_FLAVOR};
	}
}
