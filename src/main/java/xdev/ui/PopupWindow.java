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


import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Insets;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.ContainerEvent;
import java.awt.event.ContainerListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JLayeredPane;
import javax.swing.JPopupMenu;
import javax.swing.JWindow;
import javax.swing.event.EventListenerList;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;


/**
 * You can add another JPopupMenu or JComboxBox in this popup.
 * <p>
 * Some code is taken from<br>
 * <a href="http://forum.java.sun.com/thread.jsp?forum=57&thread=230866">http://
 * forum.java.sun.com</a><br>
 * and {@link JPopupMenu}.
 * </p>
 *
 * @since 4.0
 */
class PopupWindow
{
	/**
	 * A list of event listeners for this component.
	 */
	protected EventListenerList	listenerList	= new EventListenerList();
	
	private JWindow				delegate;
	private Container			container;
	private List<Component>		grabbed			= new ArrayList<>();
	private List<Component>		excluded		= new ArrayList<>();
	private WindowListener		windowListener;
	private ComponentListener	componentListener;
	private ContainerListener	containerListener;
	private MouseListener		mouseListener;
	private Component			component;
	private KeyEventDispatcher	keyEventDispatcher;
	
	
	public PopupWindow(Container container)
	{
		this.container = container;
		
		createDelegate();
		createListeners();
	}
	
	
	private void createDelegate()
	{
		Window window = getWindow();
		if(window != null)
		{
			delegate = new JWindow(window);
		}
	}
	
	
	public void add(Component c)
	{
		component = c;
		component.addPropertyChangeListener("preferredSize",new PropertyChangeListener()
		{
			@Override
			public void propertyChange(PropertyChangeEvent evt)
			{
				if(delegate != null)
				{
					delegate.pack();
				}
			}
		});
		if(delegate != null)
		{
			delegate.getContentPane().add(component);
			delegate.pack();
			// workaround for a problem. JWindow somehow offset the height by 1
			// See
			// http://developer.java.sun.com/developer/bugParade/bugs/4511106.html
			// looks like call pack again solve the problem.
			delegate.pack();
			// mDelegate.setSize(mDelegate.getSize().width,
			// mDelegate.getSize().height + 1);
		}
	}
	
	
	public void show(Component invoker, int x, int y)
	{
		if(delegate == null || delegate.getParent() != getWindow())
		{
			createDelegate();
			if(delegate == null)
			{
				return;
			}
			add(component);
		}
		
		Point p = getPopupLocation(invoker,x,y);
		p = adjustPopupLocationToFitScreen(invoker,p);
		
		delegate.setLocation(p.x,p.y);
		delegate.setSize(component.getPreferredSize());
		firePopupMenuWillBecomeVisible();
		delegate.setVisible(true);
		grabContainers();
		
		keyEventDispatcher = new KeyEventDispatcher()
		{
			@Override
			public boolean dispatchKeyEvent(KeyEvent e)
			{
				if(e.getKeyCode() == KeyEvent.VK_ESCAPE)
				{
					hide();
					return true;
				}
				return false;
			}
		};
		KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(
				keyEventDispatcher);
	}
	
	
	private Point getPopupLocation(Component invoker, int x, int y)
	{
		if(invoker != null)
		{
			Point invokerOrigin = invoker.getLocationOnScreen();
			
			// To avoid integer overflow
			long lx, ly;
			lx = ((long)invokerOrigin.x) + ((long)x);
			ly = ((long)invokerOrigin.y) + ((long)y);
			if(lx > Integer.MAX_VALUE)
			{
				lx = Integer.MAX_VALUE;
			}
			if(lx < Integer.MIN_VALUE)
			{
				lx = Integer.MIN_VALUE;
			}
			if(ly > Integer.MAX_VALUE)
			{
				ly = Integer.MAX_VALUE;
			}
			if(ly < Integer.MIN_VALUE)
			{
				ly = Integer.MIN_VALUE;
			}
			
			return new Point((int)lx,(int)ly);
		}
		
		return new Point(x,y);
	}
	
	
	/*
	 * @see XDEVAPI-219
	 */
	private Point adjustPopupLocationToFitScreen(Component invoker, Point popupLocation)
	{
		if(GraphicsEnvironment.isHeadless())
		{
			return popupLocation;
		}
		
		// Get screen bounds
		Rectangle scrBounds;
		GraphicsConfiguration gc = getCurrentGraphicsConfiguration(invoker,popupLocation);
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		if(gc != null)
		{
			// If we have GraphicsConfiguration use it to get screen bounds
			scrBounds = gc.getBounds();
		}
		else
		{
			// If we don't have GraphicsConfiguration use primary screen
			scrBounds = new Rectangle(toolkit.getScreenSize());
		}
		
		// Calculate the screen size that popup should fit
		Dimension popupSize = component.getPreferredSize();
		long popupRightX = (long)popupLocation.x + (long)popupSize.width;
		long popupBottomY = (long)popupLocation.y + (long)popupSize.height;
		int scrWidth = scrBounds.width;
		int scrHeight = scrBounds.height;
		
		// Insets include the task bar. Take them into account.
		Insets scrInsets = toolkit.getScreenInsets(gc);
		scrBounds.x += scrInsets.left;
		scrBounds.y += scrInsets.top;
		scrWidth -= scrInsets.left + scrInsets.right;
		scrHeight -= scrInsets.top + scrInsets.bottom;
		
		int scrRightX = scrBounds.x + scrWidth;
		int scrBottomY = scrBounds.y + scrHeight;
		
		// Ensure that popup menu fits the screen
		if(popupRightX > scrRightX)
		{
			popupLocation.x = scrRightX - popupSize.width;
		}
		
		if(popupBottomY > scrBottomY)
		{
			popupLocation.y = invoker.getLocationOnScreen().y - popupSize.height;
			// popupLocation.y = scrBottomY - popupSize.height;
		}
		
		if(popupLocation.x < scrBounds.x)
		{
			popupLocation.x = scrBounds.x;
		}
		
		if(popupLocation.y < scrBounds.y)
		{
			popupLocation.y = scrBounds.y;
		}
		
		return popupLocation;
	}
	
	
	/**
	 * Tries to find GraphicsConfiguration that contains the mouse cursor
	 * position. Can return null.
	 */
	private GraphicsConfiguration getCurrentGraphicsConfiguration(Component invoker,
			Point popupLocation)
	{
		GraphicsConfiguration gc = null;
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsDevice[] gd = ge.getScreenDevices();
		for(int i = 0; i < gd.length; i++)
		{
			if(gd[i].getType() == GraphicsDevice.TYPE_RASTER_SCREEN)
			{
				GraphicsConfiguration dgc = gd[i].getDefaultConfiguration();
				if(dgc.getBounds().contains(popupLocation))
				{
					gc = dgc;
					break;
				}
			}
		}
		// If not found and we have invoker, ask invoker about his gc
		if(gc == null && invoker != null)
		{
			gc = invoker.getGraphicsConfiguration();
		}
		return gc;
	}
	
	
	public void hide()
	{
		firePopupMenuWillBecomeInvisible();
		
		if(delegate != null)
		{
			delegate.setVisible(false);
		}
		
		if(keyEventDispatcher != null)
		{
			KeyboardFocusManager.getCurrentKeyboardFocusManager().removeKeyEventDispatcher(
					keyEventDispatcher);
			keyEventDispatcher = null;
		}
		releaseContainers();
		disposeDelegate();
	}
	
	
	private void createListeners()
	{
		windowListener = new WindowAdapter()
		{
			@Override
			public void windowClosing(WindowEvent e)
			{
				hide();
			}
			
			
			@Override
			public void windowClosed(WindowEvent e)
			{
				hide();
			}
			
			
			@Override
			public void windowIconified(WindowEvent e)
			{
				hide();
			}
		};
		componentListener = new ComponentListener()
		{
			@Override
			public void componentResized(ComponentEvent e)
			{
				hide();
			}
			
			
			@Override
			public void componentMoved(ComponentEvent e)
			{
				hide();
			}
			
			
			@Override
			public void componentShown(ComponentEvent e)
			{
				hide();
			}
			
			
			@Override
			public void componentHidden(ComponentEvent e)
			{
				hide();
			}
		};
		containerListener = new ContainerListener()
		{
			@Override
			public void componentAdded(ContainerEvent e)
			{
				hide();
			}
			
			
			@Override
			public void componentRemoved(ContainerEvent e)
			{
				hide();
			}
		};
		mouseListener = new MouseAdapter()
		{
			@Override
			public void mouseReleased(MouseEvent e)
			{
				hide();
			}
		};
	}
	
	
	private void disposeDelegate()
	{
		if(delegate != null)
		{
			delegate.dispose();
			delegate = null;
		}
	}
	
	
	private Window getWindow()
	{
		Container c = container;
		if(c == null)
		{
			return null;
		}
		while(!(c instanceof Window) && c.getParent() != null)
		{
			c = c.getParent();
		}
		if(c instanceof Window)
		{
			return (Window)c;
		}
		return null;
	}
	
	
	private void grabContainers()
	{
		Container c = container;
		while(!(c instanceof Window) && c.getParent() != null)
		{
			c = c.getParent();
		}
		grabContainer(c);
	}
	
	
	private void grabContainer(Container c)
	{
		if(c instanceof Window)
		{
			((Window)c).addWindowListener(windowListener);
			c.addComponentListener(componentListener);
			grabbed.add(c);
		}
		
		synchronized(c.getTreeLock())
		{
			int ncomponents = c.getComponentCount();
			Component[] component = c.getComponents();
			for(int i = 0; i < ncomponents; i++)
			{
				Component comp = component[i];
				if(!comp.isVisible())
				{
					continue;
				}
				if(isExcludedComponent(comp))
				{
					continue;
				}
				comp.addMouseListener(mouseListener);
				grabbed.add(comp);
				if(comp instanceof Container)
				{
					Container cont = (Container)comp;
					if(cont instanceof JLayeredPane)
					{
						cont.addContainerListener(containerListener);
					}
					grabContainer(cont);
				}
			}
		}
	}
	
	
	void releaseContainers()
	{
		for(Component c : grabbed)
		{
			if(c instanceof Window)
			{
				((Window)c).removeWindowListener(windowListener);
				c.removeComponentListener(componentListener);
			}
			else
			{
				c.removeMouseListener(mouseListener);
			}
			
			if(c instanceof Container)
			{
				if(c instanceof JLayeredPane)
				{
					((Container)c).removeContainerListener(containerListener);
				}
			}
		}
		grabbed.clear();
	}
	
	
	/**
	 * Gets the visibility of this popup.
	 *
	 * @return true if popup is visible
	 */
	public boolean isVisible()
	{
		return delegate != null ? delegate.isVisible() : false;
	}
	
	
	/**
	 * Adds a <code>PopupMenu</code> listener which will listen to notification
	 * messages from the popup portion of the combo box.
	 * <p/>
	 * For all standard look and feels shipped with Java 2, the popup list
	 * portion of combo box is implemented as a <code>JPopupMenu</code>. A
	 * custom look and feel may not implement it this way and will therefore not
	 * receive the notification.
	 *
	 * @param l
	 *            the <code>PopupMenuListener</code> to add
	 * @since 1.4
	 */
	public void addPopupMenuListener(PopupMenuListener l)
	{
		listenerList.add(PopupMenuListener.class,l);
	}
	
	
	/**
	 * Removes a <code>PopupMenuListener</code>.
	 *
	 * @param l
	 *            the <code>PopupMenuListener</code> to remove
	 * @see #addPopupMenuListener
	 * @since 1.4
	 */
	public void removePopupMenuListener(PopupMenuListener l)
	{
		listenerList.remove(PopupMenuListener.class,l);
	}
	
	
	/**
	 * Returns an array of all the <code>PopupMenuListener</code>s added to this
	 * JComboBox with addPopupMenuListener().
	 *
	 * @return all of the <code>PopupMenuListener</code>s added or an empty
	 *         array if no listeners have been added
	 *
	 * @since 1.4
	 */
	public PopupMenuListener[] getPopupMenuListeners()
	{
		return listenerList.getListeners(PopupMenuListener.class);
	}
	
	
	/**
	 * Notifies <code>PopupMenuListener</code>s that the popup portion of the
	 * combo box will become visible.
	 * <p/>
	 * This method is public but should not be called by anything other than the
	 * UI delegate.
	 *
	 * @see #addPopupMenuListener
	 * @since 1.4
	 */
	public void firePopupMenuWillBecomeVisible()
	{
		Object[] listeners = listenerList.getListenerList();
		PopupMenuEvent e = null;
		for(int i = listeners.length - 2; i >= 0; i -= 2)
		{
			if(listeners[i] == PopupMenuListener.class)
			{
				if(e == null)
				{
					e = new PopupMenuEvent(this);
				}
				((PopupMenuListener)listeners[i + 1]).popupMenuWillBecomeVisible(e);
			}
		}
	}
	
	
	/**
	 * Notifies <code>PopupMenuListener</code>s that the popup portion of the
	 * combo box has become invisible.
	 * <p/>
	 * This method is public but should not be called by anything other than the
	 * UI delegate.
	 *
	 * @see #addPopupMenuListener
	 * @since 1.4
	 */
	public void firePopupMenuWillBecomeInvisible()
	{
		Object[] listeners = listenerList.getListenerList();
		PopupMenuEvent e = null;
		for(int i = listeners.length - 2; i >= 0; i -= 2)
		{
			if(listeners[i] == PopupMenuListener.class)
			{
				if(e == null)
				{
					e = new PopupMenuEvent(this);
				}
				((PopupMenuListener)listeners[i + 1]).popupMenuWillBecomeInvisible(e);
			}
		}
	}
	
	
	/**
	 * Notifies <code>PopupMenuListener</code>s that the popup portion of the
	 * combo box has been canceled.
	 * <p/>
	 * This method is public but should not be called by anything other than the
	 * UI delegate.
	 *
	 * @see #addPopupMenuListener
	 * @since 1.4
	 */
	public void firePopupMenuCanceled()
	{
		Object[] listeners = listenerList.getListenerList();
		PopupMenuEvent e = null;
		for(int i = listeners.length - 2; i >= 0; i -= 2)
		{
			if(listeners[i] == PopupMenuListener.class)
			{
				if(e == null)
				{
					e = new PopupMenuEvent(this);
				}
				((PopupMenuListener)listeners[i + 1]).popupMenuCanceled(e);
			}
		}
	}
	
	
	/**
	 * PopupWindow will add necessary listeners to some components so that mouse
	 * click etc can hide the popup window. However in certain case, you might
	 * not want this.
	 *
	 * @param comp
	 *            component which will not hide popup when it is clicked.
	 */
	public void addAsExcludedComponents(Component comp)
	{
		if(excluded.contains(comp))
		{
			return;
		}
		excluded.add(comp);
	}
	
	
	public void removeFromExcludedComponents(Component comp)
	{
		if(!excluded.contains(comp))
		{
			return;
		}
		excluded.remove(comp);
	}
	
	
	public boolean isExcludedComponent(Component comp)
	{
		return excluded.contains(comp);
	}
}
