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
import java.awt.Dimension;

import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JInternalFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.WindowConstants;
import javax.swing.plaf.DesktopIconUI;

import xdev.ui.persistence.Persistable;


/**
 * The standard InternalFrame in XDEV. Based on {@link JInternalFrame}.
 * 
 * @see XdevInternalFrame
 * @see JInternalFrame
 * 
 * @author XDEV Software
 * 
 * @since 2.0
 */
@BeanSettings(acceptChildren = true, useXdevCustomizer = true)
public class XdevInternalFrame extends JInternalFrame implements DistinctChild, Persistable
{
	/**
	 * 
	 */
	private static final long	serialVersionUID	= -2733653851513845056L;
	
	/**
	 * Should the gui state be persisted? Defaults to {@code true}.
	 */
	private boolean				persistenceEnabled	= true;
	
	
	/**
	 * Creates a {@link XdevInternalFrame} with default values.
	 * 
	 */
	public XdevInternalFrame()
	{
		this("",null,true,true,true,true);
	}
	
	
	/**
	 * Creates a {@link XdevInternalFrame} with the specified title, icon,
	 * resizability, closability, maximizability, and iconifiability. All
	 * <code>XdevInternalFrame</code> constructors use this one.
	 * 
	 * @param title
	 *            the <code>String</code> to display in the title bar
	 * 
	 * @param icon
	 *            the <code>Icon</code> to display in the title bar
	 * 
	 * @param resizable
	 *            if <code>true</code>, the internal frame can be resized
	 * 
	 * @param closable
	 *            if <code>true</code>, the internal frame can be closed
	 * 
	 * @param maximizable
	 *            if <code>true</code>, the internal frame can be maximized
	 * 
	 * @param iconifiable
	 *            if <code>true</code>, the internal frame can be iconified
	 */
	public XdevInternalFrame(String title, Icon icon, boolean resizable, boolean closable,
			boolean maximizable, boolean iconifiable)
	{
		super(title,resizable,closable,maximizable,iconifiable);
		this.setLayout(null);
		this.setFrameIcon(icon);
		this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		this.setFocusTraversalPolicy(new XdevFocusTraversalPolicy(this));
		this.setVisible(true);
		this.setDesktopIcon(new XdevDesktopIcon(this));
		this.updateUI();
	}
	
	
	@Override
	public void setName(final String name)
	{
		super.setName(name);
		this.desktopIcon.setName(name);
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString()
	{
		final String toString = UIUtils.toString(this);
		if(toString != null)
		{
			return toString;
		}
		
		return super.toString();
	}
	
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see #savePersistentState()
	 */
	@Override
	public void loadPersistentState(String persistentState)
	{
		final String[] persistentValues = persistentState.split(Persistable.VALUE_SEPARATOR);
		int index = 0;
		try
		{
			final int width = Integer.parseInt(persistentValues[index++]);
			final int height = Integer.parseInt(persistentValues[index++]);
			final int x = Integer.parseInt(persistentValues[index++]);
			final int y = Integer.parseInt(persistentValues[index++]);
			this.setBounds(x,y,width,height);
			this.setIcon(Boolean.parseBoolean(persistentValues[index + 1]));
			final int zOrder = Integer.parseInt(persistentValues[index++]);
			if(zOrder >= 0)
			{
				this.getDesktopPane().setComponentZOrder(this,zOrder);
			}
		}
		catch(final Exception e)
		{
			// do nothing here, if persistent value can't be retrieved...
		}
	}
	
	
	/**
	 * {@inheritDoc}
	 * <p>
	 * Persisted properties:
	 * <ul>
	 * <li>width</li>
	 * <li>height</li>
	 * <li>position</li>
	 * <li>layer</li>
	 * </ul>
	 * </p>
	 */
	@Override
	public String savePersistentState()
	{
		final StringBuilder persistentState = new StringBuilder();
		persistentState.append(Integer.toString(this.getWidth()));
		persistentState.append(Persistable.VALUE_SEPARATOR);
		persistentState.append(Integer.toString(this.getHeight()));
		persistentState.append(Persistable.VALUE_SEPARATOR);
		persistentState.append(Integer.toString(this.getX()));
		persistentState.append(Persistable.VALUE_SEPARATOR);
		persistentState.append(Integer.toString(this.getY()));
		persistentState.append(Persistable.VALUE_SEPARATOR);
		final int zOrder = this.getDesktopPane().getComponentZOrder(this);
		persistentState.append(Integer.toString(zOrder));
		persistentState.append(Persistable.VALUE_SEPARATOR);
		persistentState.append(Boolean.toString(this.isIcon()));
		return persistentState.toString();
	}
	
	
	/**
	 * Uses the name of the component as a persistent id.
	 * <p>
	 * If no name is specified the name of the class will be used. This will
	 * work only for one persistent instance of the class!
	 * </p>
	 * {@inheritDoc}
	 */
	@Override
	public String getPersistentId()
	{
		return this.getName() != null ? this.getName() : this.getClass().getSimpleName();
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isPersistenceEnabled()
	{
		return this.persistenceEnabled;
	}
	
	
	/**
	 * Sets the persistenceEnabled flag.
	 * 
	 * @param persistenceEnabled
	 *            the state for this instance
	 */
	public void setPersistenceEnabled(boolean persistenceEnabled)
	{
		this.persistenceEnabled = persistenceEnabled;
	}
	
	
	
	/**
	 * XdevDesktopIcon. Needed to persist the minimized state of the
	 * XdevInternalFrame
	 * 
	 * @author XDEV Software (HL)
	 */
	static public class XdevDesktopIcon extends JInternalFrame.JDesktopIcon implements Persistable
	{
		final XdevInternalFrame	internalFrame;
		
		
		/**
		 * Creates an icon for an internal frame.
		 * 
		 * @param f
		 *            the <code>JInternalFrame</code> for which the icon is
		 *            created
		 */
		public XdevDesktopIcon(XdevInternalFrame f)
		{
			super(f);
			this.setVisible(true);
			this.internalFrame = f;
			super.updateUI();
		}
		
		
		@Override
		public void loadPersistentState(String persistentState)
		{
			this.internalFrame.loadPersistentState(persistentState);
		}
		
		
		@Override
		public String savePersistentState()
		{
			return this.internalFrame.savePersistentState();
		}
		
		
		@Override
		public String getPersistentId()
		{
			return this.internalFrame.getPersistentId();
		}
		
		
		@Override
		public boolean isPersistenceEnabled()
		{
			return this.internalFrame.isPersistenceEnabled();
		}
		
		
		/**
		 * This method must be overriden, otherwise the minimized InternalFrame
		 * is not shown in the desktopPane.
		 * 
		 * Notification from the <code>UIManager</code> that the look and feel
		 * has changed. Replaces the current UI object with the latest version
		 * from the <code>UIManager</code>.
		 * 
		 * @see JComponent#updateUI
		 */
		@Override
		public void updateUI()
		{
			this.setUI((DesktopIconUI)UIManager.getUI(this));
			this.invalidate();
			
			final Dimension r = this.getPreferredSize();
			this.setSize(r.width,r.height);
			
			if(this.internalFrame != null && this.internalFrame.getUI() != null)
			{ // don't do this if UI not created yet
				SwingUtilities.updateComponentTreeUI(this.internalFrame);
			}
		}
		
		
		/**
		 * This method must be overriden, otherwise the minimized InternalFrame
		 * is not shown in the desktopPane.
		 * 
		 * <p>
		 * Warning:http://stackoverflow.com/questions/7569548/suppress-the-
		 * method-does-not-override-the-inherited-method-since-it-is-private
		 * </p>
		 */
		void updateUIWhenHidden()
		{
			/* Update this UI and any associated internal frame */
			this.setUI((DesktopIconUI)UIManager.getUI(this));
			
			final Dimension r = this.getPreferredSize();
			this.setSize(r.width,r.height);
			
			this.invalidate();
			final Component[] children = this.getComponents();
			if(children != null)
			{
				for(int i = 0; i < children.length; i++)
				{
					SwingUtilities.updateComponentTreeUI(children[i]);
				}
			}
		}
	}
}
