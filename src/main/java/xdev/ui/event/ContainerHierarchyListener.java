package xdev.ui.event;

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
import java.awt.Container;
import java.awt.event.ContainerEvent;
import java.awt.event.ContainerListener;


/**
 * Extended {@link ContainerListener} which observes the complete hierarchy of a
 * {@link Container}.
 * 
 * @author XDEV Software
 * 
 * @since 3.1
 */
public abstract class ContainerHierarchyListener implements ContainerListener
{
	/**
	 * {@inheritDoc}
	 * 
	 * @see #componentAddedInHierarchy(ContainerEvent)
	 */
	@Override
	public final void componentAdded(ContainerEvent e)
	{
		Component child = e.getChild();
		if(child instanceof Container)
		{
			((Container)child).addContainerListener(this);
		}
		
		componentAddedInHierarchy(e);
	}
	

	/**
	 * {@inheritDoc}
	 * 
	 * @see #componentRemovedInHierarchy(ContainerEvent)
	 */
	@Override
	public final void componentRemoved(ContainerEvent e)
	{
		Component child = e.getChild();
		if(child instanceof Container)
		{
			((Container)child).removeContainerListener(this);
		}
		
		componentRemovedInHierarchy(e);
	}
	

	/**
	 * Invoked when a component has been added to any container in the
	 * hierarchy.
	 */
	public abstract void componentAddedInHierarchy(ContainerEvent e);
	

	/**
	 * Invoked when a component has been removed from any container in the
	 * hierarchy.
	 */
	public abstract void componentRemovedInHierarchy(ContainerEvent e);
}
