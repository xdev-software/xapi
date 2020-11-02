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


import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics2D;
import java.awt.HeadlessException;
import java.awt.LayoutManager;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.beans.Beans;
import java.util.Locale;

import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JRootPane;
import javax.swing.JWindow;
import javax.swing.ScrollPaneConstants;

import xdev.Application;
import xdev.db.locking.LockTimeMonitor;
import xdev.lang.XDEV;
import xdev.ui.locking.LockCountMonitorDownUtils;
import xdev.util.Countdown;
import xdev.util.res.ResourceUtils;


/**
 * The XdevWindow is a root container which can be added into a
 * {@link XdevRootPaneContainer}.
 * <p>
 * Don't confuse XdevWindow with {@link JWindow}, see {@link XdevScreen}.
 *
 *
 * @author XDEV Software
 *
 * @see XdevComponent
 * @see XdevRootPaneContainer
 * @see XDEV#OpenWindow(xdev.lang.cmd.OpenWindow)
 *
 * @since 3.0
 */
@BeanSettings(acceptChildren = true, useXdevCustomizer = true)
public class XdevWindow extends XdevComponent
		implements LockTimeMonitor /*
									 * implements JobHandler
									 */
{
	private String					title						= "";
	private Icon					icon						= null;

	private boolean					resizable					= true;
	private int						verticalScrollBarPolicy		= ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER;
	private int						horizontalScrollBarPolicy	= ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER;
	private int						extendedState				= XdevRootPaneContainer.NORMAL;

	private JMenuBar				menuBar						= null;
	private JButton					defaultButton				= null;

	private XdevRootPaneContainer	owner;

	private Object					parameter					= null;
	private Object					responseValue				= null;
	private boolean					responseState				= false;

	private Component				lockPanel;

	// @since 4.0
	// private ThreadPoolExecutor threadPoolExecutor;
	// private boolean threadPoolCancelled = false;

	private int						verticalUnitIncrement		= 16;
	private int						horizontalUnitIncrement		= 16;

	private int						verticalBlockIncrement		= 10;
	private int						horizontalBlockIncrement	= 10;


	/**
	 * Create a new {@link XdevWindow} with no layout manager.
	 *
	 * <p>
	 * Alias for <code>XdevWindow(null)</code>
	 * </p>
	 *
	 * @see #XdevWindow(LayoutManager)
	 *
	 */
	public XdevWindow()
	{
		this(null);
	}


	/**
	 * Create a new {@link XdevWindow} with the specified layout manager.
	 *
	 * @param layout
	 *            the LayoutManager to use
	 */
	public XdevWindow(final LayoutManager layout)
	{
		super(layout);
	}


	/**
	 * Adds the specified window listener to receive window events of this
	 * {@link XdevWindow}.
	 *
	 *
	 * @param listener
	 *            the window listener
	 * @see #removeWindowListener(WindowListener)
	 * @see #getWindowListeners()
	 */
	public void addWindowListener(final WindowListener listener)
	{
		this.listenerList.add(WindowListener.class,listener);

		if(this.owner != null)
		{
			this.owner.addWindowListener(listener);
		}
	}


	/**
	 * Removes the specified window listener so that it no longer receives
	 * window events from this {@link XdevWindow}.
	 *
	 * @param listener
	 *            the window listener to remove
	 * @see #addWindowListener(WindowListener)
	 * @see #getWindowListeners()
	 */
	public void removeWindowListener(final WindowListener listener)
	{
		this.listenerList.remove(WindowListener.class,listener);

		if(this.owner != null)
		{
			this.owner.removeWindowListener(listener);
		}
	}


	/**
	 * Returns an array of all the window listeners registered on this
	 * {@link XdevWindow}.
	 *
	 * @return all of this window's <code>WindowListener</code>s or an empty
	 *         array if no window listeners are currently registered
	 *
	 * @see #addWindowListener(WindowListener)
	 * @see #removeWindowListener(WindowListener)
	 */
	public WindowListener[] getWindowListeners()
	{
		return this.listenerList.getListeners(WindowListener.class);
	}


	/**
	 * Invokes {@link WindowListener#windowOpened(WindowEvent)} of all
	 * registered {@link WindowListener}s.
	 *
	 * @param event
	 *            the window event
	 * @see #getWindowListeners()
	 */
	public void fireWindowOpened(final WindowEvent event)
	{
		for(final WindowListener l : this.listenerList.getListeners(WindowListener.class))
		{
			l.windowOpened(event);
		}
	}


	/**
	 * Sets the owner of this window.
	 *
	 *
	 * @param owner
	 *            the <code>XdevRootPaneContainer</code> to set as owner
	 *
	 * @see #getOwner()
	 */
	public void setOwner(final XdevRootPaneContainer owner)
	{
		// Fix for XDEV-3009
		if(this.owner != null)
		{
			for(final WindowListener l : this.listenerList.getListeners(WindowListener.class))
			{
				this.owner.removeWindowListener(l);
			}
		}

		this.owner = owner;

		if(owner != null)
		{
			for(final WindowListener l : this.listenerList.getListeners(WindowListener.class))
			{
				owner.addWindowListener(l);
			}

			// refresh lock panel
			setEnabled(isEnabled());
		}
		// else
		// {
		// threadPoolCancelled = true;
		// if(threadPoolExecutor != null)
		// {
		// threadPoolExecutor.shutdown();
		// threadPoolExecutor = null;
		// }
		// }
	}


	/**
	 * Gets the container of this {@link XdevWindow}.
	 *
	 * @return the owner of this {@link XdevWindow} as
	 *         <code>XdevRootPaneContainer</code>. The owner may be
	 *         <code>null</code>.
	 *
	 * @see #setOwner(XdevRootPaneContainer)
	 */
	public XdevRootPaneContainer getOwner()
	{
		return this.owner;
	}


	/**
	 * Sets the title of the window.
	 *
	 * @param title
	 *            the title displayed in the dialog's or frame's border; a null
	 *            value results in an empty title
	 * @see #getTitle()
	 */
	public void setTitle(final String title)
	{
		this.title = title;

		if(this.owner != null)
		{
			this.owner.setTitle(title);
		}
	}


	/**
	 * Gets the title of the window. The title is displayed in the dialog's or
	 * frame's border.
	 *
	 * @return the title of this window. The title may be <code>null</code>.
	 *
	 * @see #setTitle(String)
	 */
	public String getTitle()
	{
		return this.title;
	}


	/**
	 * Sets the <code>menubar</code> for this window.
	 *
	 * @param menuBar
	 *            the <code>menubar</code> being placed in the window
	 *
	 * @see #getJMenuBar()
	 */
	public void setJMenuBar(final JMenuBar menuBar)
	{
		this.menuBar = menuBar;
	}


	/**
	 * Returns the <code>menubar</code> set on this window.
	 *
	 * @return the {@link JMenuBar} of this {@link XdevWindow}
	 *
	 * @see #setJMenuBar(JMenuBar)
	 */
	public JMenuBar getJMenuBar()
	{
		return this.menuBar;
	}


	/**
	 * Sets the <code>defaultButton</code> property, which determines the
	 * current default button for this window. The default button is the button
	 * which will be activated when a UI-defined activation event (typically the
	 * <b>Enter</b> key) occurs in the root pane regardless of whether or not
	 * the button has keyboard focus (unless there is another component within
	 * the window which consumes the activation event, such as a
	 * <code>JTextPane</code>). For default activation to work, the button must
	 * be an enabled descendent of the window when activation occurs. To remove
	 * a default button from this window, set this property to <code>null</code>
	 * .
	 *
	 * @see JButton#isDefaultButton()
	 * @param defaultButton
	 *            the {@link JButton} which is to be the default button
	 */
	public void setDefaultButton(final JButton defaultButton)
	{
		this.defaultButton = defaultButton;

		if(this.owner != null)
		{
			this.owner.getRootPane().setDefaultButton(defaultButton);
		}
	}


	/**
	 * Returns the value of the <code>defaultButton</code> property.
	 *
	 * @return the {@link JButton} which is currently the default button
	 * @see #setDefaultButton(JButton)
	 */
	public JButton getDefaultButton()
	{
		return this.defaultButton;
	}


	/**
	 * Sets the <code>parameter</code> of the window.
	 *
	 * @param o
	 *            the <code>Object</code> to set as parameter
	 *
	 * @see #getParameter()
	 */
	public void setParameter(final Object o)
	{
		this.parameter = o;
	}


	/**
	 * Gets the <code>parameter</code> of the window.
	 *
	 * @return the parameter of this window. The parameter may be
	 *         <code>null</code>.
	 *
	 * @see #setParameter(Object)
	 */
	public Object getParameter()
	{
		return this.parameter;
	}


	/**
	 * Sets the <code>response value</code> of the window.
	 *
	 * @param responseValue
	 *            the <code>Object</code> to set as response value
	 *
	 * @see #getResponseValue()
	 */
	public void setResponseValue(final Object responseValue)
	{
		this.responseValue = responseValue;
	}


	/**
	 * Gets the <code>response value</code> of the window.
	 *
	 * @return the response value of this window. The response value may be
	 *         <code>null</code>.
	 *
	 * @see #setResponseValue(Object)
	 */
	public Object getResponseValue()
	{
		return this.responseValue;
	}


	/**
	 * Sets the <code>response state</code> of the window.
	 *
	 * @param responseState
	 *            the response state value
	 *
	 * @see #isResponseOK()
	 */
	public void setResponseState(final boolean responseState)
	{
		this.responseState = responseState;
	}


	/**
	 * Returns the value of the <code>responseState</code>. The default value is
	 * <code>false</code>
	 *
	 * @return the value of the <code>responseState</code>
	 *
	 * @see #setResponseState(boolean)
	 */
	public boolean isResponseOK()
	{
		return this.responseState;
	}


	/**
	 * @deprecated use {@link #setIcon(Icon)} instead
	 */
	// @Deprecated
	// public void setIconPath(String iconPath)
	// {
	// setIcon(UIUtils.loadIcon(iconPath));
	// }

	/**
	 * Defines the icon this window's parent (dialog,frame,...) will display. If
	 * the value of icon is null, nothing is displayed.
	 *
	 * @param icon
	 *            the icon to display
	 */
	public void setIcon(final Icon icon)
	{
		this.icon = icon;
	}


	/**
	 * Returns this window's icon.
	 *
	 * @return an icon or <code>null</code>
	 */
	public Icon getIcon()
	{
		return this.icon;
	}


	/**
	 * Sets whether this {@link XdevWindow} is resizable by the user.
	 *
	 * @param resizable
	 *            <code>true</code> if the user can resize this
	 *            {@link XdevWindow} ; <code>false</code> otherwise.
	 *
	 * @see #isResizable()
	 */
	public void setResizable(final boolean resizable)
	{
		this.resizable = resizable;

		if(this.owner != null)
		{
			this.owner.setResizable(resizable);
		}
	}


	/**
	 * Returns <code>true</code> if this {@link XdevWindow} is resizeable by the
	 * user.
	 *
	 * @return <code>true</code> if the user can resize this {@link XdevWindow}
	 *         ; <code>false</code> otherwise.
	 *
	 * @see #setResizable(boolean)
	 */
	public boolean isResizable()
	{
		return this.resizable;
	}


	/**
	 * Determines when the vertical scrollbar appears in the scrollpane. Legal
	 * values are:
	 * <ul>
	 * <li><code>ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED</code>
	 * <li><code>ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER</code>
	 * <li><code>ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS</code>
	 * </ul>
	 *
	 * @param verticalScrollBarPolicy
	 *            one of the three values listed above
	 *
	 * @throws IllegalArgumentException
	 *             if <code>policy</code> is not one of the legal values shown
	 *             above
	 *
	 * @see #getVerticalScrollBarPolicy
	 */
	public void setVerticalScrollBarPolicy(final int verticalScrollBarPolicy)
			throws IllegalArgumentException
	{
		this.verticalScrollBarPolicy = verticalScrollBarPolicy;
	}


	/**
	 * Returns the vertical scroll bar policy value.
	 *
	 * @return the vertical scroll bar policy value
	 *
	 * @see #setVerticalScrollBarPolicy
	 */
	public int getVerticalScrollBarPolicy()
	{
		return this.verticalScrollBarPolicy;
	}


	/**
	 * Set the vertical unit increment
	 *
	 * @param verticalUnitIncrement
	 *            the vertical unit increment
	 * @throws IllegalArgumentException
	 *             if <code>policy</code> is not positive or 0.
	 * @see #getVerticalUnitIncrement()
	 */
	@BeanProperty(category = DefaultBeanCategories.DESIGN, owner = "verticalScrollBarPolicy", intMin = 0, intMax = Integer.MAX_VALUE)
	public void setVerticalUnitIncrement(final int verticalUnitIncrement)
			throws IllegalArgumentException
	{
		if(verticalUnitIncrement <= 0)
		{
			throw new IllegalArgumentException(
					"Vertical unit increment value have to be between 0 and Integer.MAX_VALUE");
		}

		this.verticalUnitIncrement = verticalUnitIncrement;
	}


	/**
	 * Returns the vertical unit increment
	 *
	 * @return the vertical unit increment
	 * @see #setVerticalUnitIncrement(int)
	 */
	public int getVerticalUnitIncrement()
	{
		return this.verticalUnitIncrement;
	}


	/**
	 * Determines when the horizontal scrollbar appears in the scrollpane. The
	 * options are:
	 * <ul>
	 * <li><code>ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED</code>
	 * <li><code>ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER</code>
	 * <li><code>ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS</code>
	 * </ul>
	 *
	 * @param horizontalScrollBarPolicy
	 *            one of the three values listed above
	 *
	 * @throws IllegalArgumentException
	 *             if <code>policy</code> is not one of the legal values shown
	 *             above
	 *
	 * @see #getHorizontalScrollBarPolicy
	 */
	public void setHorizontalScrollBarPolicy(final int horizontalScrollBarPolicy)
			throws IllegalArgumentException
	{
		this.horizontalScrollBarPolicy = horizontalScrollBarPolicy;
	}


	/**
	 * Returns the horizontal scroll bar policy value.
	 *
	 * @return the horizontal scroll bar policy value
	 *
	 * @see #setHorizontalScrollBarPolicy
	 */
	public int getHorizontalScrollBarPolicy()
	{
		return this.horizontalScrollBarPolicy;
	}


	/**
	 * Set the horizontal unit increment
	 *
	 * @param horizontalUnitIncrement
	 *            the horizontal unit increment
	 * @throws IllegalArgumentException
	 *             if <code>policy</code> is not possitive or 0.
	 * @see #getHorizontalUnitIncrement()
	 */
	@BeanProperty(category = DefaultBeanCategories.DESIGN, owner = "horizontalScrollBarPolicy", intMin = 0, intMax = Integer.MAX_VALUE)
	public void setHorizontalUnitIncrement(final int horizontalUnitIncrement)
	{
		this.horizontalUnitIncrement = horizontalUnitIncrement;
	}


	/**
	 * Returns the horizontal unit increment
	 *
	 * @return the horizontal unit increment
	 * @see #setHorizontalUnitIncrement(int)
	 */
	public int getHorizontalUnitIncrement()
	{
		return this.horizontalUnitIncrement;
	}


	/**
	 * Returns the vertical block increment
	 *
	 * @return the vertical block increment
	 * @see #VerticalBlockIncrement(int)
	 */
	public int getVerticalBlockIncrement()
	{
		return this.verticalBlockIncrement;
	}


	/**
	 * Set the vertical block increment
	 *
	 * @param verticalBlockIncrement
	 *            the vertical block increment
	 * @throws IllegalArgumentException
	 *             if <code>policy</code> is not positive or 0.
	 * @see #getVerticalBlockIncrement()
	 */
	@BeanProperty(category = DefaultBeanCategories.DESIGN, owner = "verticalScrollBarPolicy", intMin = 0, intMax = Integer.MAX_VALUE)
	public void setVerticalBlockIncrement(final int verticalBlockIncrement)
	{
		this.verticalBlockIncrement = verticalBlockIncrement;
	}


	/**
	 * Returns the horizontal block increment
	 *
	 * @return the horizontal block increment
	 * @see #setHorizontalBlockIncrement(int)
	 */
	public int getHorizontalBlockIncrement()
	{
		return this.horizontalBlockIncrement;
	}


	/**
	 * Set the horizontal block increment
	 *
	 * @param horizontalBlockIncrement
	 *            the horizontal block increment
	 * @throws IllegalArgumentException
	 *             if <code>policy</code> is not possitive or 0.
	 * @see #getHorizontalBlockIncrement()
	 */
	@BeanProperty(category = DefaultBeanCategories.DESIGN, owner = "horizontalScrollBarPolicy", intMin = 0, intMax = Integer.MAX_VALUE)
	public void setHorizontalBlockIncrement(final int horizontalBlockIncrement)
	{
		this.horizontalBlockIncrement = horizontalBlockIncrement;
	}


	/**
	 * Sets the state of this window. The state is represented as a bitwise
	 * mask.
	 *
	 * @param extendedState
	 *            a bitwise mask of frame state constants
	 * @see #getExtendedState
	 * @see XdevRootPaneContainer#setExtendedState(int)
	 * @since 3.1
	 */
	public void setExtendedState(final int extendedState)
	{
		this.extendedState = extendedState;

		if(this.owner != null)
		{
			this.owner.setExtendedState(extendedState);
		}
	}


	/**
	 * Gets the state of this window. The state is represented as a bitwise
	 * mask.
	 *
	 * @return a bitwise mask of frame state constants
	 * @see #setExtendedState(int)
	 * @since 3.1
	 */
	public int getExtendedState()
	{
		return this.extendedState;
	}


	/**
	 * This method indicates that the window is fully maximized (that is both
	 * horizontally and vertically).
	 *
	 * <p>
	 * Note that if the state is not supported on a given platform, nothing will
	 * happen.
	 * </p>
	 *
	 * @see JFrame#setExtendedState(int)
	 * @see Frame#MAXIMIZED_BOTH
	 */
	public void maximize()
	{
		if(this.owner instanceof JFrame)
		{
			((JFrame)this.owner).setExtendedState(Frame.MAXIMIZED_BOTH);
		}
	}


	public void pack()
	{
		if(this.owner != null)
		{
			this.owner.pack();
		}
	}


	/**
	 * Removes all the components from this window. This method also notifies
	 * the layout manager to remove the components from this content pane's
	 * layout via the <code>removeLayoutComponent</code> method.
	 *
	 * @see #removeAll()
	 * @see Application#gc()
	 */
	public void cleanUp()
	{
		removeAll();
		Application.gc();
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void paintImage(final Graphics2D g)
	{
		final Dimension d = getSize();
		paintBackground(g,0,0,d.width,d.height);
		drawTexture(g,0,0,d.width,d.height);
	}


	/**
	 * Returns the localized version of the given {@link String}
	 * <code>str</code>.
	 *
	 * <P>
	 * This method is an alias for
	 * <code>ResourceUtils#optLocalizeString(str, this)</code>.
	 * <p>
	 *
	 * @param str
	 *            to localize
	 *
	 * @return the localized String for the given <code>str</code>.
	 *
	 * @see Locale
	 * @see ResourceUtils#optLocalizeString(String, Object)
	 * @see ResourceUtils#optLocalizeChar(String, Object)
	 */
	public String localizeString(final String str)
	{
		return ResourceUtils.optLocalizeString(str,this);
	}


	/**
	 * Returns the localized version of the given {@link String}
	 * <code>str</code> as char if the result is a 1-length String, '\0'
	 * otherwise.
	 *
	 * <P>
	 * This method is an alias for
	 * <code>ResourceUtils#optLocalizeChar(str,this);</code>.
	 * <p>
	 *
	 * @param str
	 *            to localize
	 *
	 * @return the localized char for the given <code>str</code> or '\0'.
	 *
	 * @see Locale
	 * @see ResourceUtils#optLocalizeString(String, Object)
	 * @see ResourceUtils#optLocalizeChar(String, Object)
	 */
	public char localizeChar(final String str)
	{
		return ResourceUtils.optLocalizeChar(str,this);
	}


	/**
	 * Open the window in the current window.
	 *
	 *
	 * @param window
	 *            the {@link XdevWindow} to open
	 *
	 */
	public void openInCurrentWindow(final XdevWindow window)
	{
		final XdevRootPaneContainer container = UIUtils
				.getParentOfClass(XdevRootPaneContainer.class,this);
		if(container != null)
		{
			container.setXdevWindow(window);
			container.pack();
			container.setLocationRelativeTo(null);
		}
	}


	/**
	 * Open the <code>window</code> in a new {@link XdevDialog}. <br>
	 * This {@link XdevWindow} is used as <code>owner</code>.
	 *
	 * <P>
	 * This method is an alias for <code>openInDialog(window,modal,this);</code>
	 * .
	 * <p>
	 *
	 *
	 * @param window
	 *            the {@link XdevWindow} to open
	 *
	 * @param modal
	 *            specifies whether dialog blocks user input to other top-level
	 *            windows when shown.
	 *
	 *
	 * @return the new {@link JDialog}
	 */
	public JDialog openInDialog(final XdevWindow window, final boolean modal)
	{
		return openInDialog(window,modal,this);
	}


	/**
	 * Open the <code>window</code> in a new {@link XdevDialog}.
	 *
	 *
	 * @param window
	 *            the {@link XdevWindow} to open
	 *
	 * @param modal
	 *            specifies whether dialog blocks user input to other top-level
	 *            windows when shown.
	 *
	 * @param owner
	 *            the <code>Component</code> from which the dialog is displayed
	 *            or <code>null</code> if this dialog has no owner
	 *
	 * @return the new {@link JDialog}
	 */
	public JDialog openInDialog(final XdevWindow window, final boolean modal, final Component owner)
	{
		final XdevDialog dialog = new XdevDialog(owner,modal,window);
		dialog.pack();
		dialog.setLocationRelativeTo(null);
		dialog.setVisible(true);
		return dialog;
	}


	/**
	 * Open the <code>window</code> in a new {@link XdevScreen}. <br>
	 * This {@link XdevWindow} is used as <code>owner</code>.
	 *
	 * <P>
	 * This method is an alias for <code>openInWindow(window,modal,this);</code>
	 * .
	 * <p>
	 *
	 * @param window
	 *            the content of this window
	 *
	 * @param modal
	 *            specifies whether window blocks user input to other top-level
	 *            windows when shown.
	 *
	 *
	 * @throws HeadlessException
	 *             if <code>GraphicsEnvironment.isHeadless()</code> returns
	 *             true.
	 *
	 * @return the new {@link JWindow}
	 */
	public JWindow openInScreen(final XdevWindow window, final boolean modal)
			throws HeadlessException
	{
		return openInScreen(window,modal,this);
	}


	/**
	 * Open the <code>window</code> in a new {@link XdevScreen}.
	 *
	 * @param window
	 *            the content of this window
	 *
	 * @param modal
	 *            specifies whether window blocks user input to other top-level
	 *            windows when shown.
	 *
	 * @param owner
	 *            component to get Window ancestor of
	 *
	 * @throws HeadlessException
	 *             if <code>GraphicsEnvironment.isHeadless()</code> returns
	 *             true.
	 *
	 * @return the new {@link JWindow}
	 */
	public JWindow openInScreen(final XdevWindow window, final boolean modal, final Component owner)
			throws HeadlessException
	{
		final XdevScreen screen = new XdevScreen(owner,modal,window);
		screen.pack();
		screen.setLocationRelativeTo(null);
		screen.setVisible(true);
		return screen;
	}


	/**
	 * Open the <code>window</code> in a new {@link XdevFrame}.
	 *
	 * @param window
	 *            the {@link XdevWindow} to open
	 *
	 * @return the new {@link JFrame}
	 */
	public JFrame openInFrame(final XdevWindow window)
	{
		final XdevFrame frame = new XdevFrame(window);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		return frame;
	}


	/**
	 * Hides this {@link XdevWindow}.
	 */
	public void close()
	{
		if(this.owner != null)
		{
			this.owner.close();
		}
	}


	/**
	 * Gets the lock panel of the window.
	 *
	 * @return the lock panel as <code>Component</code>
	 */
	public Component getLockPanel()
	{
		if(this.lockPanel == null)
		{
			this.lockPanel = createLockPanel();
		}

		return this.lockPanel;
	}


	protected Component createLockPanel()
	{
		return new LockPanel();
	}


	/**
	 *
	 * @param lockPanel
	 * @since 3.1
	 */
	public void setLockPanel(final Component lockPanel)
	{
		this.lockPanel = lockPanel;
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setEnabled(final boolean enabled)
	{
		super.setEnabled(enabled);

		if(!Beans.isDesignTime())
		{
			final JRootPane rootPane = this.owner != null ? this.owner.getRootPane()
					: getRootPane();
			if(rootPane != null)
			{
				if(enabled)
				{
					rootPane.getGlassPane().setVisible(false);
					rootPane.getGlassPane().setEnabled(true);
				}
				else
				{
					rootPane.setGlassPane(getLockPanel());
					rootPane.getGlassPane().setEnabled(false);
					rootPane.getGlassPane().setVisible(true);
				}
			}
		}
	}


	/**
	 * {@inheritDoc}
	 */
	@BeanProperty(category = DefaultBeanCategories.MISC)
	@Override
	public void setForeground(final Color fg)
	{
		super.setForeground(fg);
	}


	/**
	 * {@inheritDoc}
	 */
	@BeanProperty(category = DefaultBeanCategories.MISC)
	@Override
	public void setFont(final Font font)
	{
		super.setFont(font);
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public void updateContext(final Countdown countdown, final long remainingTime)
	{
		this.setTitle(
				LockCountMonitorDownUtils.getUpdatedText(countdown,remainingTime,this.getTitle()));
	}

	// /**
	// * {@inheritDoc}
	// *
	// * @since 4.0
	// */
	// @Override
	// public void enqueue(Job job)
	// {
	// if(threadPoolCancelled)
	// {
	// return;
	// }
	//
	// if(threadPoolExecutor == null)
	// {
	// threadPoolExecutor = new ThreadPoolExecutor(0,1,0L,TimeUnit.MILLISECONDS,
	// new LinkedBlockingQueue<Runnable>());
	// }
	//
	// threadPoolExecutor.submit(job);
	// }
}
