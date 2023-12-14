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


import java.awt.*;
import java.awt.dnd.*;


public class DragAdapter
{
	public DragAdapter(Component cpn)
	{
		new DragSource().createDefaultDragGestureRecognizer(cpn,DnDConstants.ACTION_COPY,
				new DragGestureListener()
				{
					public void dragGestureRecognized(java.awt.dnd.DragGestureEvent dge)
					{
						DragAdapter.this
								.dragGestureRecognized(new xdev.ui.dnd.DragGestureEvent(dge));
					}
				});
	}
	

	public void dragGestureRecognized(xdev.ui.dnd.DragGestureEvent event)
	{
	}
}
