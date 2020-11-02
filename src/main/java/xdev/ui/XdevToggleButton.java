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


import javax.swing.Action;
import javax.swing.ButtonGroup;
import javax.swing.ButtonModel;
import javax.swing.DefaultButtonModel;
import javax.swing.Icon;
import javax.swing.JToggleButton;

import xdev.ui.persistence.Persistable;


/**
 * The standard toggle button in XDEV. Based on {@link JToggleButton}.
 * 
 * @see JToggleButton
 * @see XdevFocusCycleComponent
 * 
 * @author XDEV Software
 * 
 * @since 2.0
 */
@BeanSettings(useXdevCustomizer = true)
public class XdevToggleButton extends JToggleButton implements XdevFocusCycleComponent, Persistable
{
	/**
	 * 
	 */
	private static final long	serialVersionUID	= -4542179659861619749L;
	
	/**
	 * tabIndex is used to store the index for {@link XdevFocusCycleComponent}
	 * functionality.
	 */
	private int					tabIndex			= -1;
	
	/**
	 * Should the gui state be persisted? Defaults to {@code true}.
	 */
	private boolean				persistenceEnabled	= true;
	

	/**
	 * Creates an initially unselected {@link XdevToggleButton} without setting
	 * the text or image.
	 */
	public XdevToggleButton()
	{
		super();
	}
	

	/**
	 * Creates a {@link XdevToggleButton} where properties are taken from the
	 * Action supplied.
	 * 
	 * @param a
	 *            the <code>Action</code> used to specify the new
	 *            {@link XdevToggleButton}
	 * 
	 * @see #setAction(Action)
	 */
	public XdevToggleButton(Action a)
	{
		super(a);
	}
	

	/**
	 * Creates an initially unselected {@link XdevToggleButton} with the
	 * specified image but no text.
	 * 
	 * @param icon
	 *            the image that the {@link XdevToggleButton} should display
	 */
	public XdevToggleButton(Icon icon)
	{
		super(icon);
	}
	

	/**
	 * Creates a {@link XdevToggleButton} that has the specified text and image,
	 * and that is initially unselected.
	 * 
	 * @param text
	 *            the string displayed on the {@link XdevToggleButton}
	 * @param icon
	 *            the image that the {@link XdevToggleButton} should display
	 */
	public XdevToggleButton(String text, Icon icon)
	{
		super(text,icon);
	}
	

	/**
	 * Creates an unselected {@link XdevToggleButton} with the specified text.
	 * 
	 * @param text
	 *            the string displayed on the {@link XdevToggleButton}
	 */
	public XdevToggleButton(String text)
	{
		super(text);
	}
	

	/**
	 * Returns the group that the button belongs to.
	 * 
	 * @return the group that the button belongs to
	 */
	public ButtonGroup getButtonGroup()
	{
		ButtonModel model = getModel();
		if(model instanceof DefaultButtonModel)
		{
			return ((DefaultButtonModel)model).getGroup();
		}
		
		return null;
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
	 */
	@Override
	public int getTabIndex()
	{
		return tabIndex;
	}
	

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setTabIndex(int tabIndex)
	{
		if(this.tabIndex != tabIndex)
		{
			int oldValue = this.tabIndex;
			this.tabIndex = tabIndex;
			firePropertyChange(TAB_INDEX_PROPERTY,oldValue,tabIndex);
		}
	}
	

	/**
	 * {@inheritDoc}
	 * 
	 * @see #savePersistentState()
	 */
	@Override
	public void loadPersistentState(String persistentState)
	{
		this.setSelected(Boolean.valueOf(persistentState));
	}
	

	/**
	 * {@inheritDoc}
	 * <p>
	 * Persisted properties:
	 * <ul>
	 * <li>selection state</li>
	 * </ul>
	 * </p>
	 */
	@Override
	public String savePersistentState()
	{
		return Boolean.toString(this.isSelected());
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
