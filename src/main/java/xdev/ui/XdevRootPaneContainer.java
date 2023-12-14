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
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Window;
import java.awt.event.WindowListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.RootPaneContainer;
import javax.swing.ScrollPaneConstants;


/**
 * A root pane container is basically a container which is able to contain and
 * manage a {@link XdevWindow}, see {@link #setXdevWindow(XdevWindow)}.
 * 
 * @see XdevApplicationContainer
 * @see RootPaneContainer
 * 
 * @author XDEV Software
 * 
 */
public interface XdevRootPaneContainer extends RootPaneContainer
{
	/**
	 * Container is in the "normal" state. This symbolic constant names a
	 * container state with all state bits cleared.
	 * 
	 * @see #setExtendedState(int)
	 */
	int	NORMAL			= JFrame.NORMAL;
	
	/**
	 * This state bit indicates that container is iconified.
	 * 
	 * @see #setExtendedState(int)
	 */
	int	ICONIFIED		= JFrame.ICONIFIED;
	
	/**
	 * This state bit indicates that container is maximized in the horizontal
	 * direction.
	 * 
	 * @see #setExtendedState(int)
	 */
	int	MAXIMIZED_HORIZ	= JFrame.MAXIMIZED_HORIZ;
	
	/**
	 * This state bit indicates that container is maximized in the vertical
	 * direction.
	 * 
	 * @see #setExtendedState(int)
	 */
	int	MAXIMIZED_VERT	= JFrame.MAXIMIZED_VERT;
	
	/**
	 * This state bit mask indicates that container is fully maximized (that is
	 * both horizontally and vertically). It is just a convenience alias for
	 * <code>MAXIMIZED_VERT&nbsp;|&nbsp;MAXIMIZED_HORIZ</code>.
	 * 
	 * @see #setExtendedState(int)
	 */
	int	MAXIMIZED_BOTH	= JFrame.MAXIMIZED_BOTH;
	
	
	/**
	 * Returns the {@link Window} of this {@link XdevRootPaneContainer}.
	 * 
	 * 
	 * @return the {@link Window} of this {@link XdevRootPaneContainer}
	 */
	public Window getWindow();
	
	
	/**
	 * Sets the content of this {@link XdevRootPaneContainer}.
	 * 
	 * @param window
	 *            the {@link XdevWindow} for this {@link XdevRootPaneContainer}
	 */
	public void setXdevWindow(XdevWindow window);
	
	
	/**
	 * Returns the internal {@link XdevWindow} of this
	 * {@link XdevRootPaneContainer}.
	 * 
	 * 
	 * @return the internal {@link XdevWindow}
	 */
	public XdevWindow getXdevWindow();
	
	
	/**
	 * Adds the specified window listener to receive window events.
	 * 
	 * 
	 * @param listener
	 *            the window listener
	 * 
	 */
	public void addWindowListener(WindowListener listener);
	
	
	/**
	 * Removes the specified window listener to receive window events.
	 * 
	 * 
	 * @param listener
	 *            the window listener
	 * 
	 * @since 4.0
	 */
	public void removeWindowListener(WindowListener listener);
	
	
	/**
	 * Makes the {@link XdevRootPaneContainer} visible or invisible.
	 * 
	 * @param b
	 *            <code>true</code> to make the container visible;
	 *            <code>false</code> to make it invisible
	 */
	public void setVisible(boolean b);
	
	
	/**
	 * Causes this Window to be sized to fit the preferred size and layouts of
	 * its subcomponents. If the window and/or its owner are not yet
	 * displayable, both are made displayable before calculating the preferred
	 * size. The Window will be validated after the preferredSize is calculated.
	 * <p>
	 * If this container isn't a Window this method has no effect.
	 * 
	 * @see Component#isDisplayable()
	 */
	public void pack();
	
	
	/**
	 * Sets the location of the {@link XdevRootPaneContainer} relative to the
	 * specified component.
	 * 
	 * @param c
	 *            the component in relation to which the
	 *            {@link XdevRootPaneContainer} location is determined
	 */
	public void setLocationRelativeTo(Component c);
	
	
	/**
	 * Returns the location of the {@link XdevRootPaneContainer}.
	 * 
	 * 
	 * @return the location as {@link Point}
	 */
	public Point getLocation();
	
	
	/**
	 * Returns the size of the {@link XdevRootPaneContainer}.
	 * 
	 * @return the size as {@link Dimension}
	 */
	public Dimension getSize();
	
	
	/**
	 * Returns the size of the {@link XdevRootPaneContainer}.
	 * 
	 * @return the size as {@link Dimension}
	 */
	public Rectangle getBounds();
	
	
	/**
	 * Moves this {@link XdevRootPaneContainer} to a new location.
	 * 
	 * @param x
	 *            the <i>x</i>-coordinate of the new location
	 * @param y
	 *            the <i>y</i>-coordinate of the new location
	 * 
	 * @see #getLocation
	 * @see #setBounds
	 */
	public void setLocation(int x, int y);
	
	
	/**
	 * Moves and resizes this {@link XdevRootPaneContainer}. The new location of
	 * the top-left corner is specified by <code>x</code> and <code>y</code>,
	 * and the new size is specified by <code>w</code> and <code>h</code>.
	 * 
	 * @param x
	 *            the new <i>x</i>-coordinate of this
	 *            {@link XdevRootPaneContainer}
	 * @param y
	 *            the new <i>y</i>-coordinate of this
	 *            {@link XdevRootPaneContainer}
	 * @param w
	 *            the new <code>width</code> of this
	 *            {@link XdevRootPaneContainer}
	 * @param h
	 *            the new <code>height</code> of this
	 *            {@link XdevRootPaneContainer}
	 * 
	 * @see #getBounds
	 * @see #setLocation(int, int)
	 */
	public void setBounds(int x, int y, int w, int h);
	
	
	/**
	 * If this Window is visible, brings this Window to the front and may make
	 * it the focused Window.
	 * <p>
	 * If this container isn't a Window this method has no effect.
	 * <p>
	 * Places this Window at the top of the stacking order and shows it in front
	 * of any other Windows in this VM. No action will take place if this Window
	 * is not visible. Some platforms do not allow Windows which own other
	 * Windows to appear on top of those owned Windows. Some platforms may not
	 * permit this VM to place its Windows above windows of native applications,
	 * or Windows of other VMs. This permission may depend on whether a Window
	 * in this VM is already focused. Every attempt will be made to move this
	 * Window as high as possible in the stacking order; however, developers
	 * should not assume that this method will move this Window above all other
	 * windows in every situation.
	 * <p>
	 * Because of variations in native windowing systems, no guarantees about
	 * changes to the focused and active Windows can be made. Developers must
	 * never assume that this Window is the focused or active Window until this
	 * Window receives a WINDOW_GAINED_FOCUS or WINDOW_ACTIVATED event. On
	 * platforms where the top-most window is the focused window, this method
	 * will <b>probably</b> focus this Window, if it is not already focused. On
	 * platforms where the stacking order does not typically affect the focused
	 * window, this method will <b>probably</b> leave the focused and active
	 * Windows unchanged.
	 * <p>
	 * If this method causes this Window to be focused, and this Window is a
	 * Frame or a Dialog, it will also become activated. If this Window is
	 * focused, but it is not a Frame or a Dialog, then the first Frame or
	 * Dialog that is an owner of this Window will be activated.
	 * <p>
	 * If this window is blocked by modal dialog, then the blocking dialog is
	 * brought to the front and remains above the blocked window.
	 * 
	 * @see #toBack()
	 */
	public void toFront();
	
	
	/**
	 * If this Window is visible, sends this Window to the back and may cause it
	 * to lose focus or activation if it is the focused or active Window.
	 * <p>
	 * If this container isn't a Window this method has no effect.
	 * <p>
	 * Places this Window at the bottom of the stacking order and shows it
	 * behind any other Windows in this VM. No action will take place is this
	 * Window is not visible. Some platforms do not allow Windows which are
	 * owned by other Windows to appear below their owners. Every attempt will
	 * be made to move this Window as low as possible in the stacking order;
	 * however, developers should not assume that this method will move this
	 * Window below all other windows in every situation.
	 * <p>
	 * Because of variations in native windowing systems, no guarantees about
	 * changes to the focused and active Windows can be made. Developers must
	 * never assume that this Window is no longer the focused or active Window
	 * until this Window receives a WINDOW_LOST_FOCUS or WINDOW_DEACTIVATED
	 * event. On platforms where the top-most window is the focused window, this
	 * method will <b>probably</b> cause this Window to lose focus. In that
	 * case, the next highest, focusable Window in this VM will receive focus.
	 * On platforms where the stacking order does not typically affect the
	 * focused window, this method will <b>probably</b> leave the focused and
	 * active Windows unchanged.
	 * 
	 * @see #toFront()
	 */
	public void toBack();
	
	
	/**
	 * Sets the title of this {@link XdevRootPaneContainer}.
	 * 
	 * @param s
	 *            the title to be displayed
	 */
	public void setTitle(String s);
	
	
	/**
	 * Sets whether this {@link XdevRootPaneContainer} is resizable by the user.
	 * 
	 * @param b
	 *            <code>true</code> if the user can resize this
	 *            {@link XdevRootPaneContainer}; <code>false</code> otherwise.
	 * 
	 */
	public void setResizable(boolean b);
	
	
	/**
	 * <p>
	 * Sets the state of this container. The state is represented as a bitwise
	 * mask.
	 * <p>
	 * If this container doesn't support an extended state this method has no
	 * effect.
	 * <ul>
	 * <li>{@link #NORMAL}<br>
	 * Indicates that no state bits are set.
	 * <li>{@link #ICONIFIED}
	 * <li>{@link #MAXIMIZED_HORIZ}
	 * <li>{@link #MAXIMIZED_VERT}
	 * <li>{@link #MAXIMIZED_BOTH} <br>
	 * Concatenates <code>MAXIMIZED_HORIZ</code> and <code>MAXIMIZED_VERT</code>.
	 * </ul>
	 * <p>
	 * Note that if the state is not supported on a given platform, nothing will
	 * happen. The application may determine if a specific state is available
	 * via the <code>java.awt.Toolkit#isFrameStateSupported(int state)</code>
	 * method.
	 * 
	 * @param extendedState
	 *            a bitwise mask of frame state constants
	 * @see #getExtendedState
	 * @see java.awt.Toolkit#isFrameStateSupported(int)
	 * @since 3.1
	 */
	public void setExtendedState(int extendedState);
	
	
	/**
	 * Gets the state of this container. The state is represented as a bitwise
	 * mask.
	 * 
	 * @return a bitwise mask of frame state constants
	 * @see #setExtendedState(int)
	 * @since 3.1
	 */
	public int getExtendedState();
	
	
	/**
	 * Closes this container and releases all used resources.
	 * 
	 * @since 4.0
	 */
	public void close();
	
	
	
	public static class Util
	{
		/**
		 * @deprecated root pane container ids not in use anymore
		 */
		// @Deprecated public final static Map<String, XdevRootPaneContainer>
		// containers = new Hashtable();
		
		/**
		 * 
		 */
		public static Container createContainer(XdevRootPaneContainer rootPane,
				final XdevWindow window)
		{
			int v = window.getVerticalScrollBarPolicy();
			int h = window.getHorizontalScrollBarPolicy();
			if(v == ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER
					&& h == ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER)
			{
				return window;
			}
			
			JScrollPane scrl = new JScrollPane(window,v,h);
			scrl.getVerticalScrollBar().setUnitIncrement(window.getVerticalUnitIncrement());
			scrl.getHorizontalScrollBar().setUnitIncrement(window.getHorizontalUnitIncrement());
			scrl.getVerticalScrollBar().setBlockIncrement(window.getVerticalBlockIncrement());
			scrl.getHorizontalScrollBar().setBlockIncrement(window.getHorizontalBlockIncrement());
			
			scrl.setBorder(BorderFactory.createEmptyBorder());
			scrl.setViewportBorder(BorderFactory.createEmptyBorder());
			
			if(window.getLayout() != null)
			{
				scrl.setPreferredSize(window.getPreferredSize());
				window.setPreferredSize(null);
			}
			
			return scrl;
		}
		
		
		public static List<Image> createImageList(XdevWindow contentPane)
		{
			Icon icon = contentPane.getIcon();
			if(icon != null)
			{
				List<Image> list = new ArrayList();
				list.add(GraphicUtils.createImageFromIcon(icon));
				return list;
			}
			else
			{
				return getDefaultDialogImageList();
			}
		}
		
		
		public static List<Image> getDefaultDialogImageList()
		{
			List<Image> list = new ArrayList();
			for(String name : new String[]{"xdev_16x16.png","xdev_24x24.png","xdev_32x32.png",
					"xdev_48x48.png","xdev_64x64.png","xdev_96x96.png","xdev_128x128.png",
					"xdev_256x256.png"})
			{
				list.add(GraphicUtils.loadResIcon(name).getImage());
			}
			return list;
		}
	}
	
	
	
	// /**
	// * @since 4.0
	// */
	// static class JobHandler
	// {
	// private ThreadPoolExecutor threadPoolExecutor;
	//
	//
	// void enqueue(Job job)
	// {
	// if(threadPoolExecutor == null)
	// {
	// threadPoolExecutor = new ThreadPoolExecutor(0,1,0L,TimeUnit.MILLISECONDS,
	// new LinkedBlockingQueue<Runnable>());
	// }
	//
	// threadPoolExecutor.submit(job);
	// }
	//
	//
	// void cancel()
	// {
	// if(threadPoolExecutor != null)
	// {
	// threadPoolExecutor.shutdown();
	// threadPoolExecutor = null;
	// }
	// }
	// }
}
