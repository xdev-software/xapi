package xdev.ui.dnd;

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


import java.awt.Component;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;

import xdev.util.logging.LoggerFactory;
import xdev.util.logging.XdevLogger;


public class DropAdapter
{
	
	/**
	 * Logger instance for this class.
	 */
	private static final XdevLogger	log			= LoggerFactory.getLogger(DropAdapter.class);
	
	public DropAdapter(Component cpn)
	{
		new DropTarget(cpn,new DropTargetListener()
		{
			public void drop(DropTargetDropEvent dtde)
			{
				try
				{
					dtde.acceptDrop(DnDConstants.ACTION_COPY);
					Object data = getTransferData(dtde.getTransferable());
					dtde.dropComplete(true);
					DropAdapter.this.drop(new DropEvent(dtde.getSource(),data,dtde.getLocation()));
				}
				catch(Exception e)
				{
					log.error(e);
				}
			}
			

			public void dragEnter(DropTargetDragEvent dtde)
			{
			}
			

			public void dragOver(DropTargetDragEvent dtde)
			{
			}
			

			public void dragExit(DropTargetEvent dte)
			{
			}
			

			public void dropActionChanged(DropTargetDragEvent dtde)
			{
			}
			

			Object getTransferData(Transferable t)
			{
				try
				{
					return t.getTransferData(LocalTransferable.DATA_FLAVOR);
				}
				catch(Exception e)
				{
					try
					{
						if(t.isDataFlavorSupported(DataFlavor.javaFileListFlavor))
						{
							return t.getTransferData(DataFlavor.javaFileListFlavor);
						}
						else if(t.isDataFlavorSupported(DataFlavor.imageFlavor))
						{
							return t.getTransferData(DataFlavor.imageFlavor);
						}
						else if(t.isDataFlavorSupported(DataFlavor.stringFlavor))
						{
							return t.getTransferData(DataFlavor.stringFlavor);
						}
						
						return "";
					}
					catch(Exception e2)
					{
						log.error(e2);
						return "";
					}
				}
			}
		});
	}
	

	public void drop(DropEvent event)
	{
	}
}
