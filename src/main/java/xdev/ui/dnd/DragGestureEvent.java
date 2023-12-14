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

import java.awt.dnd.*;

import xdev.util.*;


public class DragGestureEvent extends java.awt.dnd.DragGestureEvent
{
	public DragGestureEvent(java.awt.dnd.DragGestureEvent source)
	{
		super(source.getSourceAsDragGestureRecognizer(),source.getDragAction(),source.getDragOrigin(),
				new XdevList(source.toArray()));
	}
	
	
	public void startDrag(Object data)
	{
		super.startDrag(DragSource.DefaultMoveDrop,new LocalTransferable(data));
	}
}
