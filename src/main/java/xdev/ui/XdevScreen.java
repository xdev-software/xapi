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
import java.awt.HeadlessException;
import java.awt.Window;

import javax.swing.JWindow;
import javax.swing.SwingUtilities;


/**
 * The {@link XdevScreen} is a top-level window with no borders and no menubar
 * in XDEV. Based on {@link JWindow}.
 * 
 * 
 * @see JWindow
 * @see XdevRootPaneContainer
 * 
 * @author XDEV Software
 * 
 * @since 2.0
 */
public class XdevScreen extends JWindow implements XdevRootPaneContainer
{
	protected Window		owner;
	protected XdevWindow	window;
	protected boolean		modal;
	
	
	/**
	 * Creates a window with the specified owner window. This window will not be
	 * focusable unless its owner is showing on the screen. If
	 * <code>owner</code> is <code>null</code>, the shared owner will be used
	 * and this window will not be focusable.
	 * <p>
	 * This constructor sets the component's locale property to the value
	 * returned by <code>JComponent.getDefaultLocale</code>.
	 * 
	 * @param owner
	 *            component to get Window ancestor of
	 * 
	 * @param modal
	 *            specifies whether window blocks user input to other top-level
	 *            windows when shown.
	 * 
	 * @param window
	 *            the content of this screen
	 * 
	 * @throws HeadlessException
	 *             if <code>GraphicsEnvironment.isHeadless()</code> returns
	 *             true.
	 * 
	 */
	public XdevScreen(Component owner, boolean modal, XdevWindow window) throws HeadlessException
	{
		this(XdevDialog.getWindowFor(owner),modal,window);
	}
	
	
	/**
	 * Creates a window with the specified owner window. This window will not be
	 * focusable unless its owner is showing on the screen. If
	 * <code>owner</code> is <code>null</code>, the shared owner will be used
	 * and this window will not be focusable.
	 * <p>
	 * This constructor sets the component's locale property to the value
	 * returned by <code>JComponent.getDefaultLocale</code>.
	 * 
	 * @param owner
	 *            the window from which the window is displayed
	 * 
	 * @param modal
	 *            specifies whether window blocks user input to other top-level
	 *            windows when shown.
	 * 
	 * @param window
	 *            the content of this screen
	 * 
	 * @throws HeadlessException
	 *             if <code>GraphicsEnvironment.isHeadless()</code> returns
	 *             true.
	 * 
	 */
	public XdevScreen(Window owner, boolean modal, XdevWindow window) throws HeadlessException
	{
		super(owner);
		
		init(owner,modal);
		
		setXdevWindow(window);
	}
	
	
	/**
	 * Creates a window with the specified owner window. This window will not be
	 * focusable unless its owner is showing on the screen. If
	 * <code>owner</code> is <code>null</code>, the shared owner will be used
	 * and this window will not be focusable.
	 * <p>
	 * This constructor sets the component's locale property to the value
	 * returned by <code>JComponent.getDefaultLocale</code>.
	 * 
	 * @param owner
	 *            component to get Window ancestor of
	 * 
	 * @param modal
	 *            specifies whether window blocks user input to other top-level
	 *            windows when shown.
	 * 
	 * @throws HeadlessException
	 *             if <code>GraphicsEnvironment.isHeadless()</code> returns
	 *             true.
	 * 
	 */
	public XdevScreen(Component owner, boolean modal) throws HeadlessException
	{
		this(owner != null ? SwingUtilities.getWindowAncestor(owner) : null,modal);
	}
	
	
	/**
	 * Creates a window with the specified owner window. This window will not be
	 * focusable unless its owner is showing on the screen. If
	 * <code>owner</code> is <code>null</code>, the shared owner will be used
	 * and this window will not be focusable.
	 * <p>
	 * This constructor sets the component's locale property to the value
	 * returned by <code>JComponent.getDefaultLocale</code>.
	 * 
	 * @param owner
	 *            the window from which the window is displayed
	 * 
	 * @param modal
	 *            specifies whether window blocks user input to other top-level
	 *            windows when shown.
	 * 
	 * @throws HeadlessException
	 *             if <code>GraphicsEnvironment.isHeadless()</code> returns
	 *             true.
	 * 
	 */
	public XdevScreen(Window owner, boolean modal) throws HeadlessException
	{
		super(owner);
		
		init(owner,modal);
	}
	
	
	private void init(Window owner, boolean modal)
	{
		this.owner = owner;
		this.modal = modal;
		
		setFocusTraversalPolicy(new XdevFocusTraversalPolicy(this));
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setXdevWindow(XdevWindow window)
	{
		cleanup();
		
		this.window = window;
		window.setOwner(this);
		
		setTitle(window.getTitle());
		setBounds(window.getX(),window.getY(),window.getWidth(),window.getHeight());
		getRootPane().setDefaultButton(window.getDefaultButton());
		
		setContentPane(Util.createContainer(this,window));
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public XdevWindow getXdevWindow()
	{
		return window;
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Window getWindow()
	{
		return this;
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setTitle(String s)
	{
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setResizable(boolean b)
	{
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setVisible(boolean b)
	{
		if(b)
		{
			if(modal)
			{
				owner.setEnabled(false);
			}
		}
		
		super.setVisible(b);
		
		if(!b)
		{
			SwingUtilities.invokeLater(new Runnable()
			{
				public void run()
				{
					cleanup();
				}
			});
			
			if(modal)
			{
				owner.setEnabled(true);
			}
			
			owner.toFront();
		}
	}
	
	
	protected void cleanup()
	{
		if(window != null)
		{
			window.setOwner(null);
			window = null;
		}
	}
	
	
	/**
	 * Implemented with no action for {@link XdevApplicationContainer}.
	 */
	@Override
	public void setExtendedState(int extendedState)
	{
		// no op
	}
	
	
	/**
	 * Implemented with no action for {@link XdevApplicationContainer}.
	 */
	@Override
	public int getExtendedState()
	{
		return NORMAL;
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void close()
	{
		setVisible(false);
		dispose();
	}
}
