package xdev.ui.tree;

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


import javax.swing.SwingUtilities;
import javax.swing.tree.DefaultTreeModel;

import xdev.lang.Copyable;


public class XdevTreeModel extends DefaultTreeModel implements Copyable<XdevTreeModel>
{
	public XdevTreeModel(XdevTreeNode root)
	{
		super(root);
		root.setModel(this);
	}
	
	
	@Override
	public XdevTreeModel clone()
	{
		XdevTreeNode root = (XdevTreeNode)this.root;
		return new XdevTreeModel(root.cloneTree());
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void fireTreeNodesChanged(final Object source, final Object[] path,
			final int[] childIndices, final Object[] children)
	{
		runInEDT(new Runnable()
		{
			@Override
			public void run()
			{
				super_fireTreeNodesChanged(source,path,childIndices,children);
			}
		});
	}
	
	
	private void super_fireTreeNodesChanged(Object source, Object[] path, int[] childIndices,
			Object[] children)
	{
		try
		{
			super.fireTreeNodesChanged(source,path,childIndices,children);
		}
		catch(ArrayIndexOutOfBoundsException aioobe)
		{
			// VariableHeightLayoutCache not up to date (JDK issue)
		}
		catch(Exception e)
		{
			// swallow
		}
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void fireTreeNodesInserted(final Object source, final Object[] path,
			final int[] childIndices, final Object[] children)
	{
		runInEDT(new Runnable()
		{
			@Override
			public void run()
			{
				super_fireTreeNodesInserted(source,path,childIndices,children);
			}
		});
	}
	
	
	private void super_fireTreeNodesInserted(Object source, Object[] path, int[] childIndices,
			Object[] children)
	{
		try
		{
			super.fireTreeNodesInserted(source,path,childIndices,children);
		}
		catch(Exception e)
		{
			// swallow
		}
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void fireTreeNodesRemoved(final Object source, final Object[] path,
			final int[] childIndices, final Object[] children)
	{
		runInEDT(new Runnable()
		{
			@Override
			public void run()
			{
				super_fireTreeNodesRemoved(source,path,childIndices,children);
			}
		});
	}
	
	
	private void super_fireTreeNodesRemoved(Object source, Object[] path, int[] childIndices,
			Object[] children)
	{
		try
		{
			super.fireTreeNodesRemoved(source,path,childIndices,children);
		}
		catch(Exception e)
		{
			// swallow
		}
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void fireTreeStructureChanged(final Object source, final Object[] path,
			final int[] childIndices, final Object[] children)
	{
		runInEDT(new Runnable()
		{
			@Override
			public void run()
			{
				super_fireTreeStructureChanged(source,path,childIndices,children);
			}
		});
	}
	
	
	private void super_fireTreeStructureChanged(Object source, Object[] path, int[] childIndices,
			Object[] children)
	{
		try
		{
			super.fireTreeStructureChanged(source,path,childIndices,children);
		}
		catch(Exception e)
		{
			// swallow
		}
	}
	
	
	private void runInEDT(Runnable runnable)
	{
		if(SwingUtilities.isEventDispatchThread())
		{
			runnable.run();
		}
		else
		{
			SwingUtilities.invokeLater(runnable);
		}
	}
}
