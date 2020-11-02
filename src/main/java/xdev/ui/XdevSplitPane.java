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

import javax.swing.JSplitPane;
import javax.swing.UIManager;

import xdev.ui.persistence.Persistable;


/**
 * The standard split pane in XDEV. Based on {@link JSplitPane}.
 * 
 * @see JSplitPane
 * 
 * @author XDEV Software
 * 
 * @since 2.0
 */
@BeanSettings(acceptChildren = true, useXdevCustomizer = true)
public class XdevSplitPane extends JSplitPane implements Persistable
{
	/**
	 * 
	 */
	private static final long	serialVersionUID	= 491725401915117145L;
	
	/**
	 * Should the gui state be persisted? Defaults to {@code true}.
	 */
	private boolean				persistenceEnabled	= true;
	

	/**
	 * Creates a new {@link XdevSplitPane} with the default orientation
	 * {@link JSplitPane#HORIZONTAL_SPLIT}.
	 */
	public XdevSplitPane()
	{
		this(HORIZONTAL_SPLIT);
	}
	

	/**
	 * Creates a new {@link XdevSplitPane} with the specified orientation.
	 * 
	 * @param orientation
	 *            <code>JSplitPane.HORIZONTAL_SPLIT</code> or
	 *            <code>JSplitPane.VERTICAL_SPLIT</code>
	 * 
	 * @throws IllegalArgumentException
	 *             if <code>orientation</code> is not one of HORIZONTAL_SPLIT or
	 *             VERTICAL_SPLIT
	 */
	public XdevSplitPane(int orientation) throws IllegalArgumentException
	{
		this(orientation,UIManager.getBoolean("SplitPane.continuousLayout"));
	}
	

	/**
	 * Creates a new {@link XdevSplitPane} with the specified orientation and
	 * redrawing style.
	 * 
	 * @param newOrientation
	 *            <code>JSplitPane.HORIZONTAL_SPLIT</code> or
	 *            <code>JSplitPane.VERTICAL_SPLIT</code>
	 * 
	 * @param newContinuousLayout
	 *            a boolean, <code>true</code> for the components to redraw
	 *            continuously as the divider changes position,
	 *            <code>false</code> to wait until the divider position stops
	 *            changing to redraw
	 * 
	 * @throws IllegalArgumentException
	 *             if <code>orientation</code> is not one of HORIZONTAL_SPLIT or
	 *             VERTICAL_SPLIT
	 */
	public XdevSplitPane(int newOrientation, boolean newContinuousLayout)
			throws IllegalArgumentException
	{
		this(newOrientation,newContinuousLayout,null,null);
	}
	

	/**
	 * Creates a new {@link XdevSplitPane} with the specified orientation and
	 * components.
	 * 
	 * @param newOrientation
	 *            <code>JSplitPane.HORIZONTAL_SPLIT</code> or
	 *            <code>JSplitPane.VERTICAL_SPLIT</code>
	 * 
	 * @param newLeftComponent
	 *            the <code>Component</code> that will appear on the left of a
	 *            horizontally-split pane, or at the top of a vertically-split
	 *            pane
	 * 
	 * @param newRightComponent
	 *            the <code>Component</code> that will appear on the right of a
	 *            horizontally-split pane, or at the bottom of a
	 *            vertically-split pane
	 * 
	 * @throws IllegalArgumentException
	 *             if <code>orientation</code> is not one of HORIZONTAL_SPLIT or
	 *             VERTICAL_SPLIT
	 */
	public XdevSplitPane(int newOrientation, Component newLeftComponent, Component newRightComponent)
			throws IllegalArgumentException
	{
		this(newOrientation,UIManager.getBoolean("SplitPane.continuousLayout"),newLeftComponent,
				newRightComponent);
	}
	

	/**
	 * Creates a new {@link XdevSplitPane} with the specified orientation and
	 * redrawing style, and with the specified components.
	 * 
	 * @param newOrientation
	 *            <code>JSplitPane.HORIZONTAL_SPLIT</code> or
	 *            <code>JSplitPane.VERTICAL_SPLIT</code>
	 * 
	 * @param newContinuousLayout
	 *            a boolean, <code>true</code> for the components to redraw
	 *            continuously as the divider changes position,
	 *            <code>false</code> to wait until the divider position stops
	 *            changing to redraw
	 * 
	 * @param newLeftComponent
	 *            the <code>Component</code> that will appear on the left of a
	 *            horizontally-split pane, or at the top of a vertically-split
	 *            pane
	 * 
	 * @param newRightComponent
	 *            the <code>Component</code> that will appear on the right of a
	 *            horizontally-split pane, or at the bottom of a
	 *            vertically-split pane
	 * 
	 * @throws IllegalArgumentException
	 *             if <code>orientation</code> is not one of HORIZONTAL_SPLIT or
	 *             VERTICAL_SPLIT
	 */
	public XdevSplitPane(int newOrientation, boolean newContinuousLayout,
			Component newLeftComponent, Component newRightComponent)
			throws IllegalArgumentException
	{
		super(newOrientation,newContinuousLayout,newLeftComponent,newRightComponent);
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
	 * {@inheritDoc}
	 * 
	 * @see #savePersistentState()
	 */
	@Override
	public void loadPersistentState(String persistentState)
	{
		this.setDividerLocation(Integer.parseInt(persistentState));
	}
	

	/**
	 * {@inheritDoc}
	 * <p>
	 * Persisted properties:
	 * <ul>
	 * <li>divider location</li>
	 * </ul>
	 * </p>
	 */
	@Override
	public String savePersistentState()
	{
		return Integer.toString(this.getDividerLocation());
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
