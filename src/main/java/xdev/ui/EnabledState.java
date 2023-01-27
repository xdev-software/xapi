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
import java.util.Map;
import java.util.WeakHashMap;


/**
 * The {@link EnabledState} class memorizes the state (enabled or disabled) of
 * components in a container. This information can be used to restore these
 * states.
 * 
 * <p>
 * This class is immutable.
 * </p>
 * 
 * @see UIUtils#setEnabled(Container, boolean)
 * 
 * @author XDEV Software (RHHF)
 * 
 * @since 3.0
 */
public class EnabledState
{
	// WeakHashMap to avoid memory leaks
	/**
	 * States of child components.
	 */
	private final Map<Component, Boolean>	states	= new WeakHashMap<Component, Boolean>();
	
	/**
	 * Root Component.
	 */
	private final Container					root;
	

	/**
	 * Creates a new {@link EnabledState} instance that has the current state
	 * (enabled or disabled) of all {@link Component}s of <code>root</code>.
	 * 
	 * @param root
	 *            {@link Container} whose current {@link Component}s states are
	 *            to be memorized.
	 */
	public EnabledState(Container root)
	{
		this.root = root;
		this.saveState();
	}
	

	/**
	 * Saves the state of all {@link Component} in {@link #root}.
	 */
	// protected to make class immutable
	protected void saveState()
	{
		UIUtils.lookupComponentTree(this.root,new SaveStateVisitor(this.root,this.states));
	}
	

	/**
	 * Restores all {@link Component}s in <code>root</code> to the state
	 * (enabled or disabled) they had when this instance was created.
	 */
	public void restoreState()
	{
		UIUtils.lookupComponentTree(this.root,new RestoreStateVisitor(this.root,this.states));
	}
	

	/**
	 * Disables all {@link Component}s in <code>root</code>. Does not change the
	 * memorized states.
	 */
	public void disableAllComponents()
	{
		UIUtils.lookupComponentTree(this.root,new DisableVisitor(this.root));
	}
	


	/**
	 * {@link SaveStateVisitor} is a implementation of
	 * {@link ComponentTreeVisitor} that memorizes the state (enabled or
	 * disabled) of all {@link Component}s it visits.
	 * 
	 * @author XDEV Software
	 * 
	 * @since 3.0
	 * 
	 * @see ComponentTreeVisitor
	 * 
	 */
	protected static class SaveStateVisitor implements ComponentTreeVisitor<Component, Component>
	{
		/**
		 * Root {@link Container}.
		 */
		private final Container					root;
		
		/**
		 * Stores the states of the {@link Component}s.
		 */
		private final Map<Component, Boolean>	states;
		

		/**
		 * Creates a new {@link SaveStateVisitor} instance.
		 * 
		 * @param root
		 *            {@link Container} itself will be ignored.
		 * @param states
		 *            {@link Map} to store the states in.
		 */
		public SaveStateVisitor(final Container root, final Map<Component, Boolean> states)
		{
			this.root = root;
			this.states = states;
		}
		

		/**
		 * {@inheritDoc}
		 */
		@Override
		public Component visit(final Component cpn)
		{
			if(cpn != this.root)
			{
				this.states.put(cpn,cpn.isEnabled());
			}
			return null;
		}
	}
	


	/**
	 * {@link RestoreStateVisitor} is a implementation of
	 * {@link ComponentTreeVisitor} that restores the memorized state (enabled
	 * or disabled) of all {@link Component}s it visits (and it has a state
	 * for). If it has no state for a {@link Component} then the
	 * {@link Component} will be ignored.
	 * 
	 * @author XDEV Software
	 * 
	 * @since 3.0
	 * 
	 * @see ComponentTreeVisitor
	 * 
	 */
	protected static class RestoreStateVisitor implements
			ComponentTreeVisitor<Component, Component>
	{
		/**
		 * Root {@link Container}.
		 */
		private final Container					root;
		
		/**
		 * Stores the states of the {@link Component}s.
		 */
		private final Map<Component, Boolean>	states;
		

		/**
		 * Creates a new {@link RestoreStateVisitor} instance.
		 * 
		 * @param root
		 *            {@link Container} itself will be ignored.
		 * @param states
		 *            {@link Map} to read the memorized states from.
		 */
		public RestoreStateVisitor(final Container root, final Map<Component, Boolean> states)
		{
			this.root = root;
			this.states = states;
		}
		

		/**
		 * {@inheritDoc}
		 */
		@Override
		public Component visit(Component cpn)
		{
			if(cpn != this.root)
			{
				Boolean state = this.states.get(cpn);
				
				if(state != null)
				{
					cpn.setEnabled(state);
				}
			}
			
			return null;
		}
	}
	


	/**
	 * {@link DisableVisitor} is a implementation of
	 * {@link ComponentTreeVisitor} that disables all {@link Component}s it
	 * visits.
	 * 
	 * 
	 * @author XDEV Software
	 * 
	 * @since 3.0
	 * 
	 * @see ComponentTreeVisitor
	 * 
	 */
	protected static class DisableVisitor implements ComponentTreeVisitor<Component, Component>
	{
		/**
		 * Root {@link Container}.
		 */
		private final Container	root;
		

		/**
		 * Creates a new {@link RestoreStateVisitor} instance.
		 * 
		 * @param root
		 *            {@link Container} itself will be ignored.
		 */
		public DisableVisitor(final Container root)
		{
			this.root = root;
		}
		

		/**
		 * {@inheritDoc}
		 */
		@Override
		public Component visit(Component cpn)
		{
			if(cpn != this.root)
			{
				cpn.setEnabled(false);
			}
			return null;
		}
	}
}
