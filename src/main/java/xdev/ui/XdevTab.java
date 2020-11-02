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
import java.awt.Container;
import java.awt.Font;
import java.awt.LayoutManager;

import javax.swing.Icon;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import xdev.lang.Nullable;


/**
 * A component that represents a tab of a {@link JTabbedPane} displaying a given
 * title and/or icon. A tab is represented by an index corresponding to the
 * position it was added in, where the first tab has an index equal to 0 and the
 * last tab has an index equal to the tab count minus 1.
 * <p>
 * The <code>TabbedPane</code> uses a <code>SingleSelectionModel</code> to
 * represent the set of tab indices and the currently selected index. If the tab
 * count is greater than 0, then there will always be a selected index, which by
 * default will be initialized to the first tab. If the tab count is 0, then the
 * selected index will be -1.
 * <p>
 * 
 * @author XDEV Software
 * 
 * @since 2.0
 * 
 */

@BeanSettings(acceptChildren = true, useXdevCustomizer = true)
public class XdevTab extends JPanel implements DistinctChild, XdevFocusCycleController
{
	private int		index	= -1;
	private String	title	= "";
	private Icon	icon	= null;
	

	/**
	 * Creates a new {@link XdevTab}.
	 * 
	 */
	public XdevTab()
	{
		super(null);
	}
	

	/**
	 * Creates a new {@link XdevTab} with the specified layout manager and
	 * index.
	 * 
	 * @param layout
	 *            the LayoutManager to use
	 */
	public XdevTab(LayoutManager layout)
	{
		super(layout);
	}
	

	/**
	 * Creates a new {@link XdevTab} with the specified index.
	 * 
	 * 
	 * @param index
	 *            the index of the tab in the {@link JTabbedPane}
	 * 
	 * @deprecated index is automatically set by {@link JTabbedPane}
	 */
	@Deprecated
	public XdevTab(int index)
	{
		this(index,null);
	}
	

	/**
	 * Creates a new {@link XdevTab} with the specified layout manager and
	 * index.
	 * 
	 * 
	 * @param index
	 *            the index of the tab in the {@link JTabbedPane}
	 * @param layout
	 *            the LayoutManager to use
	 * 
	 * @deprecated index is automatically set by {@link JTabbedPane}
	 */
	@Deprecated
	public XdevTab(int index, LayoutManager layout)
	{
		super(layout);
		
		setIndex(index);
	}
	

	/**
	 * Sets the index of this tab to <code>index</code>.
	 * <p>
	 * <strong> This method is not intended to be called by the user. The index
	 * is set automatically by the {@link JTabbedPane}!</strong>
	 * </p>
	 * 
	 * @param index
	 *            the tab index where this tab is being placed in the
	 *            {@link JTabbedPane}
	 */
	public void setIndex(int index)
	{
		this.index = index;
	}
	

	/**
	 * Returns the index of this tab in the {@link JTabbedPane}.
	 * 
	 * @return the index of this tab in the {@link JTabbedPane}
	 */
	public int getIndex()
	{
		return index;
	}
	

	/**
	 * Sets the title to <code>title</code> which can be <code>null</code>.
	 * 
	 * 
	 * @param title
	 *            the title for this tab to be displayed.
	 * 
	 * @see #getTitle()
	 */
	public void setTitle(@Nullable String title)
	{
		this.title = title;
		
		Container parent = getParent();
		if(parent instanceof JTabbedPane)
		{
			((JTabbedPane)parent).setTitleAt(index,title);
		}
	}
	

	/**
	 * Returns the tab title.
	 * 
	 * @return the title of this tab.
	 * 
	 * @see #getTitle()
	 */
	public String getText()
	{
		return getTitle();
	}
	

	/**
	 * Returns the tab title.
	 * 
	 * @return the title of this tab.
	 * 
	 * @see #setTitle(String)
	 */
	public String getTitle()
	{
		Container parent = getParent();
		if(parent instanceof JTabbedPane)
		{
			return ((JTabbedPane)parent).getTitleAt(index);
		}
		
		return title;
	}
	

	/**
	 * Sets the icon <code>icon</code> which can be <code>null</code>.
	 * 
	 * 
	 * @param icon
	 *            the icon to be displayed in the tab. If <code>icon</code> is
	 *            null no icon will be displayed.
	 * 
	 * @see #getIcon()
	 */
	public void setIcon(@Nullable Icon icon)
	{
		this.icon = icon;
		
		Container parent = getParent();
		if(parent instanceof JTabbedPane)
		{
			((JTabbedPane)parent).setIconAt(index,icon);
		}
	}
	

	/**
	 * Returns the tab icon.
	 * 
	 * @return the icon at <code>index</code>
	 * 
	 * @see #setIcon(Icon)
	 */
	public Icon getIcon()
	{
		Container parent = getParent();
		if(parent instanceof JTabbedPane)
		{
			return ((JTabbedPane)parent).getIconAt(index);
		}
		
		return icon;
	}
	

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setToolTipText(@Nullable String text)
	{
		super.setToolTipText(text);
		
		Container parent = getParent();
		if(parent instanceof JTabbedPane)
		{
			((JTabbedPane)parent).setToolTipTextAt(index,text);
		}
	}
	

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setEnabled(boolean b)
	{
		super.setEnabled(b);
		
		Container parent = getParent();
		if(parent instanceof JTabbedPane)
		{
			((JTabbedPane)parent).setEnabledAt(index,b);
		}
	}
	

	/**
	 * 
	 * Checks if this tab is the current selected tab in the {@link JTabbedPane}
	 * parent.
	 * 
	 * @return <code>true</code> if this tab is the current selected tab in the
	 *         {@link JTabbedPane} parent, <code>false</code> otherwise
	 */
	@Override
	public boolean isSelected()
	{
		Container parent = getParent();
		if(parent instanceof JTabbedPane)
		{
			return ((JTabbedPane)parent).getSelectedComponent() == this;
		}
		return false;
	}
	

	/**
	 * Removes this tab from its parent.
	 * <p>
	 * To re-add the tab use {@link XdevTabbedPane#addTab(XdevTab)}.
	 * 
	 * @since 3.1
	 */
	public void close()
	{
		Container parent = getParent();
		if(parent instanceof JTabbedPane)
		{
			((JTabbedPane)parent).remove(this);
		}
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
	public Component getFocusComponentAfter()
	{
		Container parent = getParent();
		if(parent != null && parent instanceof JTabbedPane)
		{
			JTabbedPane tabPane = (JTabbedPane)parent;
			int index = tabPane.indexOfComponent(this);
			if(tabPane.getTabCount() > 0)
			{
				if(index < tabPane.getTabCount() - 1)
				{
					return tabPane.getTabComponentAt(index + 1);
				}
				else
				{
					return tabPane.getTabComponentAt(0);
				}
			}
		}
		
		return null;
	}
	

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Component getFocusComponentBefore()
	{
		Container parent = getParent();
		if(parent != null && parent instanceof JTabbedPane)
		{
			JTabbedPane tabPane = (JTabbedPane)parent;
			int index = tabPane.indexOfComponent(this);
			
			if(tabPane.getTabCount() > 0)
			{
				if(index > 0)
				{
					return tabPane.getTabComponentAt(index - 1);
				}
				else
				{
					return tabPane.getTabComponentAt(tabPane.getTabCount() - 1);
				}
			}
		}
		
		return null;
	}
	

	/**
	 * {@inheritDoc}
	 */
	@BeanProperty(category = DefaultBeanCategories.MISC)
	@Override
	public void setForeground(Color fg)
	{
		super.setForeground(fg);
	}
	

	/**
	 * {@inheritDoc}
	 */
	@BeanProperty(category = DefaultBeanCategories.MISC)
	@Override
	public void setFont(Font font)
	{
		super.setFont(font);
	}
}
