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

import javax.swing.Icon;
import javax.swing.JTabbedPane;

import xdev.lang.NotNull;
import xdev.ui.persistence.Persistable;


/**
 * The standard tabbed pane in XDEV. Based on {@link JTabbedPane}.
 * 
 * @see JTabbedPane
 * 
 * @author XDEV Software
 * 
 * @since 2.0
 */

@BeanSettings(acceptChildren = true, useXdevCustomizer = true)
@XdevTabbedPaneSettings(tabType = XdevTab.class)
public class XdevTabbedPane extends JTabbedPane implements Persistable
{
	/**
	 * serialVersionUID.
	 */
	private static final long	serialVersionUID	= 1601949782765071454L;
	
	/**
	 * Should the gui state be persisted? Defaults to {@code true}.
	 */
	private boolean				persistenceEnabled	= true;
	

	/**
	 * Creates an empty <code>TabbedPane</code> with a default tab placement of
	 * <code>JTabbedPane.TOP</code>.
	 */
	public XdevTabbedPane()
	{
		super();
	}
	

	/**
	 * Creates an empty <code>TabbedPane</code> with the specified tab placement
	 * of either: <code>JTabbedPane.TOP</code>, <code>JTabbedPane.BOTTOM</code>,
	 * <code>JTabbedPane.LEFT</code>, or <code>JTabbedPane.RIGHT</code>.
	 * 
	 * @param tabPlacement
	 *            the placement for the tabs relative to the content
	 * 
	 */
	public XdevTabbedPane(int tabPlacement)
	{
		super(tabPlacement);
	}
	

	/**
	 * Creates an empty <code>TabbedPane</code> with the specified tab placement
	 * and tab layout policy. Tab placement may be either:
	 * <code>JTabbedPane.TOP</code>, <code>JTabbedPane.BOTTOM</code>,
	 * <code>JTabbedPane.LEFT</code>, or <code>JTabbedPane.RIGHT</code>. Tab
	 * layout policy may be either: <code>JTabbedPane.WRAP_TAB_LAYOUT</code> or
	 * <code>JTabbedPane.SCROLL_TAB_LAYOUT</code>.
	 * 
	 * @param tabPlacement
	 *            the placement for the tabs relative to the content
	 * @param tabLayoutPolicy
	 *            the policy for laying out tabs when all tabs will not fit on
	 *            one run
	 * @throws IllegalArgumentException
	 *             if tab placement or tab layout policy are not one of the
	 *             above supported values
	 * 
	 * 
	 */
	public XdevTabbedPane(int tabPlacement, int tabLayoutPolicy) throws IllegalArgumentException
	{
		super(tabPlacement,tabLayoutPolicy);
	}
	

	/**
	 * Adds a {@link XdevTab} to the this {@link XdevTabbedPane}.
	 * 
	 * @param tab
	 *            the {@link XdevTab} to add
	 * 
	 * @throws NullPointerException
	 *             if <code>tab</code> is <code>null</code>
	 * 
	 */
	public void addTab(@NotNull XdevTab tab) throws NullPointerException
	{
		addTab(tab.getTitle(),tab.getIcon(),tab,tab.getToolTipText());
	}
	

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void insertTab(String title, Icon icon, Component component, String tip, int index)
	{
		super.insertTab(title,icon,component,tip,index);
		setEnabledAt(index,component.isEnabled());
		
		if(component instanceof XdevTab)
		{
			((XdevTab)component).setIndex(index);
		}
	}
	

	/**
	 * Returns the component at <code>index</code>.
	 * 
	 * @param index
	 *            the index of the item being queried
	 * @return the <code>Component</code> at <code>index</code>
	 * @throws IndexOutOfBoundsException
	 *             if index is out of range (index < 0 || index >= tab count)
	 * 
	 */
	public Component getTabAt(int index) throws IndexOutOfBoundsException
	{
		return getComponentAt(index);
	}
	

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString()
	{
		String toString = UIUtils.toString(this);
		if(toString != null)
		{
			return toString;
		}
		
		return super.toString();
	}
	

	/**
	 * 
	 * Use {@link #addTab(XdevTab)} to add a {@link XdevTab} to the
	 * {@link XdevTabbedPane}. If use one of the &quot;add&quot; methods the
	 * {@link XdevTab} will not be displayed as tab.
	 * 
	 * <p>
	 * This method is only overridden to place a hint related to the use of the
	 * {@link #addTab(XdevTab)} method.
	 * </p>
	 * 
	 * {@inheritDoc}
	 */
	@Override
	public Component add(Component comp)
	{
		return super.add(comp);
	}
	

	/**
	 * 
	 * Use {@link #addTab(XdevTab)} to add a {@link XdevTab} to the
	 * {@link XdevTabbedPane}. If use one of the &quot;add&quot; methods the
	 * {@link XdevTab} will not be displayed as tab.
	 * 
	 * <p>
	 * This method is only overridden to place a hint related to the use of the
	 * {@link #addTab(XdevTab)} method.
	 * </p>
	 * 
	 * {@inheritDoc}
	 */
	@Override
	public Component add(Component comp, int index)
	{
		return super.add(comp,index);
	}
	

	/**
	 * 
	 * Use {@link #addTab(XdevTab)} to add a {@link XdevTab} to the
	 * {@link XdevTabbedPane}. If use one of the &quot;add&quot; methods the
	 * {@link XdevTab} will not be displayed as tab.
	 * 
	 * <p>
	 * This method is only overridden to place a hint related to the use of the
	 * {@link #addTab(XdevTab)} method.
	 * </p>
	 * 
	 * {@inheritDoc}
	 */
	@Override
	public void add(Component comp, Object constraints)
	{
		super.add(comp,constraints);
	}
	

	/**
	 * 
	 * Use {@link #addTab(XdevTab)} to add a {@link XdevTab} to the
	 * {@link XdevTabbedPane}. If use one of the &quot;add&quot; methods the
	 * {@link XdevTab} will not be displayed as tab.
	 * 
	 * <p>
	 * This method is only overridden to place a hint related to the use of the
	 * {@link #addTab(XdevTab)} method.
	 * </p>
	 * 
	 * {@inheritDoc}
	 */
	@Override
	public void add(Component comp, Object constraints, int index)
	{
		super.add(comp,constraints,index);
	}
	

	/**
	 * 
	 * Use {@link #addTab(XdevTab)} to add a {@link XdevTab} to the
	 * {@link XdevTabbedPane}. If use one of the &quot;add&quot; methods the
	 * {@link XdevTab} will not be displayed as tab.
	 * 
	 * <p>
	 * This method is only overridden to place a hint related to the use of the
	 * {@link #addTab(XdevTab)} method.
	 * </p>
	 * 
	 * {@inheritDoc}
	 */
	@Override
	public Component add(String name, Component comp)
	{
		return super.add(name,comp);
	}
	

	/**
	 * {@inheritDoc}
	 * 
	 * @see #savePersistentState()
	 */
	@Override
	public void loadPersistentState(String persistentState)
	{
		this.setSelectedIndex(Integer.parseInt(persistentState));
	}
	

	/**
	 * {@inheritDoc}
	 * <p>
	 * Persisted properties:
	 * <ul>
	 * <li>selected index</li>
	 * </ul>
	 * </p>
	 */
	@Override
	public String savePersistentState()
	{
		return Integer.toString(this.getSelectedIndex());
	}
	

	/**
	 * Uses the name of the component as a persistent id.
	 * <p>
	 * If no name is specified the name of the class will be used. This will
	 * work only for one persistent instance of the class!
	 * </p> {@inheritDoc}
	 */
	@Override
	public String getPersistentId()
	{
		return (this.getName() != null) ? this.getName() : this.getClass().getSimpleName();
	}
	

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isPersistenceEnabled()
	{
		return persistenceEnabled;
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
}
