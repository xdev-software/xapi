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
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.HeadlessException;
import java.awt.Window;

import javax.swing.JDialog;
import javax.swing.SwingUtilities;


/**
 * The {@link XdevDialog} is a top-level window with a title and a border in
 * XDEV. Based on {@link JDialog}.
 * 
 * 
 * @see JDialog
 * @see XdevRootPaneContainer
 * 
 * @author XDEV Software
 * 
 * @since 2.0
 */
public class XdevDialog extends JDialog implements XdevRootPaneContainer
{
	private Component	owner;
	private XdevWindow	window;
	
	
	/**
	 * Creates a new {@link XdevDialog} with the specified title, owner
	 * <code>Window</code> and modality.
	 * <p>
	 * This constructor sets the component's locale property to the value
	 * returned by <code>JComponent.getDefaultLocale</code>.
	 * 
	 * @param owner
	 *            Component to get Window ancestor of
	 * 
	 * @param modal
	 *            specifies whether dialog blocks user input to other top-level
	 *            windows when shown. If <code>true</code>, the modality type
	 *            property is set to <code>APPLICATION_MODAL</code> otherwise
	 *            the dialog is <code>MODELESS</code>
	 * 
	 * @param window
	 *            the {@link XdevWindow} for this {@link XdevDialog}
	 * 
	 * @throws java.awt.HeadlessException
	 *             when <code>GraphicsEnvironment.isHeadless()</code> returns
	 *             <code>true</code>
	 * 
	 * @see JDialog
	 */
	public XdevDialog(Component owner, boolean modal, XdevWindow window)
			throws java.awt.HeadlessException
	{
		this(getWindowFor(owner),modal,window);
	}
	
	
	static Window getWindowFor(Component owner)
	{
		if(owner instanceof Window)
		{
			return (Window)owner;
		}
		
		if(owner != null)
		{
			Window w = SwingUtilities.getWindowAncestor(owner);
			if(w != null)
			{
				return w;
			}
		}
		
		return UIUtils.getActiveWindow();
	}
	
	
	/**
	 * Creates a new {@link XdevDialog} with the specified title, owner
	 * <code>Window</code> and modality.
	 * <p>
	 * This constructor sets the component's locale property to the value
	 * returned by <code>JComponent.getDefaultLocale</code>.
	 * 
	 * @param owner
	 *            the <code>Window</code> from which the dialog is displayed or
	 *            <code>null</code> if this dialog has no owner
	 * 
	 * @param modal
	 *            specifies whether dialog blocks user input to other top-level
	 *            windows when shown. If <code>true</code>, the modality type
	 *            property is set to <code>APPLICATION_MODAL</code> otherwise
	 *            the dialog is <code>MODELESS</code>
	 * 
	 * @param window
	 *            the {@link XdevWindow} for this {@link XdevDialog}
	 * 
	 * @throws java.awt.HeadlessException
	 *             when <code>GraphicsEnvironment.isHeadless()</code> returns
	 *             <code>true</code>
	 * 
	 * @see JDialog
	 */
	public XdevDialog(Window owner, boolean modal, XdevWindow window)
			throws java.awt.HeadlessException
	{
		super(owner,window.getTitle(),modal ? ModalityType.APPLICATION_MODAL
				: ModalityType.MODELESS);
		
		init(owner);
		
		setXdevWindow(window);
	}
	
	
	/**
	 * Creates a new {@link XdevDialog} with the specified title, owner
	 * <code>Frame</code> and modality. If <code>owner</code> is
	 * <code>null</code>, a shared, hidden frame will be set as the owner of
	 * this dialog.
	 * <p>
	 * This constructor sets the component's locale property to the value
	 * returned by <code>JComponent.getDefaultLocale</code>.
	 * <p>
	 * NOTE: Any popup components (<code>JComboBox</code>,
	 * <code>JPopupMenu</code>, <code>JMenuBar</code>) created within a modal
	 * dialog will be forced to be lightweight.
	 * <p>
	 * NOTE: This constructor does not allow you to create an unowned
	 * <code>XdevDialog</code>.
	 * 
	 * @param owner
	 *            the <code>Frame</code> from which the dialog is displayed
	 * 
	 * @param title
	 *            the <code>String</code> to display in the dialog's title bar
	 * 
	 * @param modal
	 *            specifies whether dialog blocks user input to other top-level
	 *            windows when shown. If <code>true</code>, the modality type
	 *            property is set to <code>DEFAULT_MODALITY_TYPE</code>
	 *            otherwise the dialog is modeless
	 * 
	 * @throws HeadlessException
	 *             if <code>GraphicsEnvironment.isHeadless()</code> returns
	 *             <code>true</code>.
	 * 
	 * @see JDialog
	 */
	public XdevDialog(Frame owner, String title, boolean modal) throws HeadlessException
	{
		super(owner,title,modal);
		
		init(owner);
	}
	
	
	/**
	 * Creates a new {@link XdevDialog} with the specified title, modality and
	 * the specified owner <code>Dialog</code>.
	 * 
	 * @param owner
	 *            the owner <code>Dialog</code> from which the dialog is
	 *            displayed or <code>null</code> if this dialog has no owner
	 * 
	 * @param title
	 *            the <code>String</code> to display in the dialog's title bar
	 * 
	 * @param modal
	 *            specifies whether dialog blocks user input to other top-level
	 *            windows when shown. If <code>true</code>, the modality type
	 *            property is set to <code>DEFAULT_MODALITY_TYPE</code>,
	 *            otherwise the dialog is modeless
	 * 
	 * @throws HeadlessException
	 *             if <code>GraphicsEnvironment.isHeadless()</code> returns
	 *             <code>true</code>.
	 * @see JDialog
	 */
	public XdevDialog(Dialog owner, String title, boolean modal) throws HeadlessException
	{
		super(owner,title,modal);
		
		init(owner);
	}
	
	
	private void init(Component owner)
	{
		this.owner = owner;
		
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		
		setFocusTraversalPolicy(new XdevFocusTraversalPolicy(this));
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public final void setXdevWindow(XdevWindow window)
	{
		cleanup();
		
		this.window = window;
		window.setOwner(this);
		
		setTitle(window.getTitle());
		setBounds(window.getX(),window.getY(),window.getWidth(),window.getHeight());
		setResizable(window.isResizable());
		setIconImages(Util.createImageList(window));
		setJMenuBar(window.getJMenuBar());
		getRootPane().setDefaultButton(window.getDefaultButton());
		
		setContentPane(Util.createContainer(this,window));
		
		if(window.isMinimumSizeSet())
		{
			setMinimumSize(window.getMinimumSize());
		}
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
	public void setVisible(boolean b)
	{
		// if(b)
		// {
		// XdevRootPaneContainer.Util.containers.put(id,this);
		// }
		
		super.setVisible(b);
		
		if(!b)
		{
			SwingUtilities.invokeLater(new Runnable()
			{
				public void run()
				{
					cleanup();
					// XdevRootPaneContainer.Util.containers.remove(id);
				}
			});
			
			if(owner instanceof Window)
			{
				((Window)owner).toFront();
			}
		}
	}
	
	
	private void cleanup()
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
