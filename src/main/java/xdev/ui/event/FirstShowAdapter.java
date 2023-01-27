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
package xdev.ui.event;


/*
 * Copyright (C) 2007-2013 by XDEV Software, All Rights Reserved.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License version 3.0 as published by the Free Software Foundation.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */

import java.awt.Component;
import java.awt.event.HierarchyEvent;
import java.awt.event.HierarchyListener;

import javax.swing.SwingUtilities;


/**
 * 
 * @author XDEV Software
 * @since 4.0
 */
public class FirstShowAdapter implements HierarchyListener
{
	public static void register(Component component, Runnable runnable)
	{
		component.addHierarchyListener(new FirstShowAdapter(component,false,runnable));
	}
	
	
	public static void invokeLater(Component component, Runnable runnable)
	{
		component.addHierarchyListener(new FirstShowAdapter(component,true,runnable));
	}
	
	private Component	component;
	private boolean		invokelater;
	private Runnable	runnable;
	
	
	private FirstShowAdapter(Component component, boolean invokelater, Runnable runnable)
	{
		this.component = component;
		this.invokelater = invokelater;
		this.runnable = runnable;
	}
	
	
	@Override
	public void hierarchyChanged(HierarchyEvent e)
	{
		if(e.getID() == HierarchyEvent.HIERARCHY_CHANGED && component.isShowing())
		{
			try
			{
				component.removeHierarchyListener(this);
				if(invokelater)
				{
					SwingUtilities.invokeLater(runnable);
				}
				else
				{
					runnable.run();
				}
			}
			finally
			{
				component = null;
				runnable = null;
			}
		}
	}
}
