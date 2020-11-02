package xdev.ui;

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
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.border.Border;
import javax.swing.plaf.UIResource;


public class XScrollPane extends JScrollPane
{
	public XScrollPane(Component view)
	{
		this(view,VERTICAL_SCROLLBAR_AS_NEEDED,HORIZONTAL_SCROLLBAR_AS_NEEDED);
	}


	public XScrollPane(Component view, int vsbPolicy, int hsbPolicy)
	{
		super(view,vsbPolicy,hsbPolicy);

		if(view != null)
		{
			setVisible(view.isVisible());
			view.addComponentListener(new ComponentAdapter()
			{
				@Override
				public void componentShown(ComponentEvent e)
				{
					setVisible(true);
				}


				@Override
				public void componentHidden(ComponentEvent e)
				{
					setVisible(false);
				}
			});

			if(view instanceof JComponent)
			{
				final JComponent jc = (JComponent)view;

				Border border = jc.getBorder();
				if(border != null && !(border instanceof UIResource))
				{
					setBorder(border);
					jc.setBorder(null);
				}

				jc.addPropertyChangeListener("border",new PropertyChangeListener()
				{
					boolean	flag	= true;


					public void propertyChange(PropertyChangeEvent evt)
					{
						if(flag)
						{
							flag = false;

							setBorder((Border)evt.getNewValue());
							jc.setBorder(null);

							flag = true;
						}
					}
				});

				setOpaque(jc.isOpaque());
				getViewport().setOpaque(jc.isOpaque());
				jc.addPropertyChangeListener("opaque",new PropertyChangeListener()
				{
					public void propertyChange(PropertyChangeEvent evt)
					{
						setOpaque(jc.isOpaque());
						getViewport().setOpaque(jc.isOpaque());
					}
				});
			}
		}
	}
}
