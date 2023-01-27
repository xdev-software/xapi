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


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Vector;

import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.KeyStroke;

import xdev.lang.NotNull;
import xdev.ui.event.FirstShowAdapter;


/**
 * Helper class to connect a specific action to a shortcut.
 * <p>
 * Usage:
 * 
 * <pre>
 * Component target = ...;
 * KeyStroke shortcut = ...;
 * 
 * XdevKeyAction keyAction = new XdevKeyAction();
 * keyAction.setTarget(target);
 * keyAction.setShortcut(shortcut);
 * keyAction.addActionListener(...);
 * </pre>
 * 
 * or just
 * 
 * <pre>
 * new XdevKeyAction(cpn,shortcut).addActionListener(...);
 * </pre>
 * 
 * @author XDEV Software
 * @since 3.1
 */
public class XdevKeyAction
{
	/**
	 * Decides when the key action takes place.
	 * 
	 * @since 4.0
	 */
	public static enum Condition
	{
		/**
		 * Means that the command should be invoked when the component has the
		 * focus.
		 */
		WHEN_FOCUSED(JComponent.WHEN_FOCUSED),
		
		/**
		 * Means that the command should be invoked when the receiving component
		 * is an ancestor of the focused component or is itself the focused
		 * component.
		 */
		WHEN_ANCESTOR_OF_FOCUSED_COMPONENT(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT),
		
		/**
		 * Means that the command should be invoked when the receiving component
		 * is in the window that has the focus or is itself the focused
		 * component.
		 */
		WHEN_IN_FOCUSED_WINDOW(JComponent.WHEN_IN_FOCUSED_WINDOW);
		
		private int	value;
		
		
		private Condition(int value)
		{
			this.value = value;
		}
	}
	
	private List<ActionListener>	actionListeners	= new Vector();
	
	/**
	 * @since 4.0
	 */
	private Condition				condition		= Condition.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT;
	private JComponent				target;
	private KeyStroke				shortcut;
	
	
	/**
	 * Creates a new empty key action.
	 */
	public XdevKeyAction()
	{
	}
	
	
	/**
	 * Creates a new focus helper with a <code>shortcut</code> for the component
	 * <code>target</code>.
	 * 
	 * @param target
	 *            the component to focus
	 * @param shortcut
	 *            the keystroke to trigger the action
	 */
	public XdevKeyAction(JComponent target, KeyStroke shortcut)
	{
		setTarget(target);
		setShortcut(shortcut);
	}
	
	
	/**
	 * Sets the new condition when the action should take place.
	 * 
	 * @param condition
	 *            the new condition
	 * 
	 * @see Condition
	 * @since 4.0
	 */
	public void setCondition(@NotNull Condition condition)
	{
		this.condition = condition;
	}
	
	
	/**
	 * 
	 * @return the condition when the action takes place
	 * 
	 * @see Condition
	 * @since 4.0
	 */
	public Condition getCondition()
	{
		return condition;
	}
	
	
	/**
	 * Sets the target component for this key action.
	 * 
	 * @param target
	 *            the component to register the shortcut at
	 */
	public void setTarget(JComponent target)
	{
		this.target = target;
		
		if(target != null)
		{
			FirstShowAdapter.register(target,new Runnable()
			{
				@Override
				public void run()
				{
					registerValues();
				}
			});
		}
	}
	
	
	/**
	 * Returns the target of this key action.
	 * 
	 * @return the target of this key action
	 */
	public JComponent getTarget()
	{
		return target;
	}
	
	
	/**
	 * Sets the shortcut for this key action.
	 * 
	 * @param shortcut
	 *            the {@link KeyStroke} which triggers this action
	 */
	public void setShortcut(KeyStroke shortcut)
	{
		this.shortcut = shortcut;
	}
	
	
	/**
	 * Return the shortcut associated with this key action.
	 * 
	 * @return the shortcut associated with this key action
	 */
	public KeyStroke getShortcut()
	{
		return shortcut;
	}
	
	
	private void registerValues()
	{
		if(target != null && shortcut != null)
		{
			String key = getClass().getName() + " " + shortcut.toString();
			target.getActionMap().put(key,new AbstractAction()
			{
				@Override
				public void actionPerformed(ActionEvent e)
				{
					fireActionPerformed(e);
				}
			});
			
			target.getInputMap(condition.value).put(shortcut,key);
		}
	}
	
	
	/**
	 * Adds an <code>ActionListener</code> to the key action.
	 * 
	 * @param l
	 *            the <code>ActionListener</code> to be added
	 */
	public void addActionListener(ActionListener l)
	{
		actionListeners.add(l);
	}
	
	
	/**
	 * Removes an <code>ActionListener</code> from the key action.
	 * 
	 * @param l
	 *            the <code>ActionListener</code> to be removed
	 */
	public void removeActionListener(ActionListener l)
	{
		actionListeners.remove(l);
	}
	
	
	/**
	 * Returns an array of all the <code>ActionListener</code>s added to this
	 * key action with {@link #addActionListener(ActionListener)}
	 * 
	 * @return all of the <code>ActionListener</code>s added or an empty array
	 *         if no listeners have been added
	 */
	public ActionListener[] getActionListeners()
	{
		return actionListeners.toArray(new ActionListener[actionListeners.size()]);
	}
	
	
	protected void fireActionPerformed(ActionEvent e)
	{
		for(ActionListener actionListener : actionListeners)
		{
			actionListener.actionPerformed(e);
		}
	}
}
