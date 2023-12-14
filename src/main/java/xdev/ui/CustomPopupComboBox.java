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
package xdev.ui;


import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Window;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.accessibility.Accessible;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import javax.swing.plaf.ComboBoxUI;

import xdev.io.IOUtils;


public class CustomPopupComboBox extends JComboBox implements FocusListener, PopupMenuListener,
		Runnable
{
	private final static int	SIZER_SIZE			= 3, MIN_SIZE = 50;
	private JPopupMenu			popup;
	private Dimension			preferredPopupSize	= null;
	

	public CustomPopupComboBox()
	{
		super();
		
		if(IOUtils.isMac())
		{
			DefaultComboBoxModel model = new DefaultComboBoxModel(new Object[]{""});
			setModel(model);
			setSelectedItem(null);
		}
		
		setLightWeightPopupEnabled(false);
		
		popup = getPopup();
		popup.removeAll();
		popup.setFocusable(true);
		
		popup.setLayout(new BorderLayout());
		
		JPanel pnlSouth = new JPanel(new BorderLayout());
		pnlSouth.add(new BorderSizerBottom(),BorderLayout.CENTER);
		pnlSouth.add(new BorderSizerBottomRight(),BorderLayout.EAST);
		
		popup.add(pnlSouth,BorderLayout.SOUTH);
		popup.add(new BorderSizerRight(),BorderLayout.EAST);
		
		popup.addPopupMenuListener(this);
		
		FocusListener[] fl = getFocusListeners();
		for(int i = 0; i < fl.length; i++)
		{
			removeFocusListener(fl[i]);
		}
		
		addFocusListener(this);
	}
	

	private JPopupMenu getPopup()
	{
		ComboBoxUI ui = getUI();
		int c = ui.getAccessibleChildrenCount(this);
		for(int i = 0; i < c; i++)
		{
			Accessible a = ui.getAccessibleChild(this,i);
			if(a instanceof JPopupMenu)
			{
				return (JPopupMenu)a;
			}
		}
		
		return null;
	}
	

	public void setPopupView(Component view)
	{
		view.setFocusable(false);
		popup.add(view,BorderLayout.CENTER);
	}
	

	public void focusGained(FocusEvent e)
	{
		repaint();
	}
	

	public void focusLost(FocusEvent e)
	{
		repaint();
	}
	

	public void popupMenuWillBecomeVisible(PopupMenuEvent e)
	{
		popup.setPopupSize(getPreferredPopupSize());
		SwingUtilities.invokeLater(this);
	}
	

	public void popupMenuWillBecomeInvisible(PopupMenuEvent e)
	{
	}
	

	public void popupMenuCanceled(PopupMenuEvent e)
	{
	}
	

	protected Dimension getPreferredPopupSize()
	{
		int width = 200, height = 150;
		if(preferredPopupSize != null)
		{
			width = preferredPopupSize.width;
			height = preferredPopupSize.height;
		}
		
		return new Dimension(Math.max(getWidth(),width),height);
	}
	

	public void setPreferredPopupSize(Dimension d)
	{
		preferredPopupSize = d;
	}
	

	public void run()
	{
		Window w = SwingUtilities.getWindowAncestor(popup);
		int i = 0;
		while(w == null && ++i < 10)
		{
			try
			{
				Thread.sleep(100);
			}
			catch(InterruptedException e)
			{
			}
			
			w = SwingUtilities.getWindowAncestor(popup);
		}
		
		if(w != null)
		{
			w.setFocusableWindowState(true);
		}
	}
	

	private Component getComponentToResize()
	{
		Component c = SwingUtilities.getWindowAncestor(popup);
		if(c.getParent() == null)
		{
			return getHeavyweightAncestor(popup);
		}
		
		return c;
	}
	

	private Component getHeavyweightAncestor(Component c)
	{
		for(Container p = c.getParent(); p != null; p = p.getParent())
		{
			if(!(p instanceof JComponent))
			{
				return p;
			}
		}
		return null;
	}
	

	abstract class BorderSizer extends JComponent implements MouseListener, MouseMotionListener
	{
		protected Point	firstPoint	= new Point();
		

		public BorderSizer()
		{
			super();
			addMouseListener(this);
			addMouseMotionListener(this);
		}
		

		public void mousePressed(MouseEvent e)
		{
			firstPoint.setLocation(e.getX(),e.getY());
		}
		

		public void mouseReleased(MouseEvent e)
		{
		}
		

		public void mouseEntered(MouseEvent e)
		{
		}
		

		public void mouseExited(MouseEvent e)
		{
		}
		

		public void mouseClicked(MouseEvent e)
		{
		}
		

		public void mouseMoved(MouseEvent e)
		{
		}
	}
	

	class BorderSizerBottom extends BorderSizer
	{
		public BorderSizerBottom()
		{
			super();
			setCursor(Cursor.getPredefinedCursor(Cursor.S_RESIZE_CURSOR));
		}
		

		public void mouseDragged(MouseEvent e)
		{
			Component c = getComponentToResize();
			c.setSize(c.getWidth(),Math.max(MIN_SIZE,c.getHeight() + e.getY() - firstPoint.y));
			c.validate();
			e.consume();
		}
		

		@Override
		public Dimension getPreferredSize()
		{
			Dimension d = super.getPreferredSize();
			d.height = SIZER_SIZE;
			return d;
		}
	}
	

	class BorderSizerRight extends BorderSizer
	{
		public BorderSizerRight()
		{
			super();
			setCursor(Cursor.getPredefinedCursor(Cursor.E_RESIZE_CURSOR));
		}
		

		public void mouseDragged(MouseEvent e)
		{
			Component c = getComponentToResize();
			c.setSize(Math.max(MIN_SIZE,c.getWidth() + e.getX() - firstPoint.x),c.getHeight());
			c.validate();
			e.consume();
		}
		

		@Override
		public Dimension getPreferredSize()
		{
			Dimension d = super.getPreferredSize();
			d.width = SIZER_SIZE;
			return d;
		}
	}
	

	class BorderSizerBottomRight extends BorderSizer
	{
		private Dimension	prefSize	= new Dimension(SIZER_SIZE,SIZER_SIZE);
		

		public BorderSizerBottomRight()
		{
			super();
			setCursor(Cursor.getPredefinedCursor(Cursor.SE_RESIZE_CURSOR));
		}
		

		public void mouseDragged(MouseEvent e)
		{
			Component c = getComponentToResize();
			c.setSize(Math.max(MIN_SIZE,c.getWidth() + e.getX() - firstPoint.x),Math.max(MIN_SIZE,c
					.getHeight()
					+ e.getY() - firstPoint.y));
			c.validate();
			e.consume();
		}
		

		@Override
		public Dimension getPreferredSize()
		{
			return prefSize;
		}
	}
}
