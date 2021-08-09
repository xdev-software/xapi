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


import java.util.Enumeration;
import java.util.List;

import javax.swing.Icon;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreeNode;

import xdev.lang.Copyable;
import xdev.ui.XdevTree;
import xdev.util.XdevList;


/**
 * The standard tree node in XDEV. Based on {@link DefaultMutableTreeNode}.
 * 
 * <p>
 * The {@link XdevTree} provides methods to modify <code>tree</code>s from a
 * <code>node</code>s perspective.
 * </p>
 * 
 * @see DefaultMutableTreeNode
 * 
 * @author XDEV Software
 * 
 * @since 2.0
 */
public class XdevTreeNode extends DefaultMutableTreeNode implements Copyable<XdevTreeNode>
{
	/**
	 * JavaDoc omitted for private field.
	 */
	private DefaultTreeModel	model;
	/**
	 * JavaDoc omitted for private field.
	 */
	private String				caption;
	/**
	 * JavaDoc omitted for private field.
	 */
	private Icon				icon;


	/**
	 * Creates a tree node with no parent, no children, but which allows
	 * children, and initializes it with the specified user object.
	 * 
	 * @param userObject
	 *            an Object provided by the user that constitutes the node's
	 *            data
	 */
	public XdevTreeNode(Object userObject)
	{
		this(userObject,String.valueOf(userObject));
	}


	/**
	 * Creates a tree node with no parent, no children, but which allows
	 * children, and initializes it with the specified user object.
	 * 
	 * @param userObject
	 *            an Object provided by the user that constitutes the node's
	 *            data
	 * @param caption
	 *            the caption of this {@link XdevTreeNode}
	 */
	public XdevTreeNode(Object userObject, String caption)
	{
		this(userObject,caption,null);
	}


	/**
	 * Creates a tree node with no parent, no children, but which allows
	 * children, and initializes it with the specified user object.
	 * 
	 * @param userObject
	 *            an Object provided by the user that constitutes the node's
	 *            data
	 * @param caption
	 *            the caption of this {@link XdevTreeNode}
	 * @param icon
	 *            {@link Icon} of this {@link XdevTreeNode}
	 */
	public XdevTreeNode(Object userObject, String caption, Icon icon)
	{
		super(userObject);

		this.caption = caption;
		this.icon = icon;
	}


	/**
	 * Removes <code>nodes</code> from their parent and makes them a child of
	 * this node by adding it to the end of this node's child array.
	 * 
	 * @see #insert
	 * @param nodes
	 *            nodes to add as a children of this node
	 * @throws IllegalArgumentException
	 *             if one element of <code>nodes</code> is null
	 * @return this {@link XdevTreeNode} after the nodes have been added
	 */
	public XdevTreeNode add(XdevTreeNode... nodes) throws IllegalArgumentException
	{
		for(XdevTreeNode node : nodes)
		{
			add(node);
		}

		return this;
	}


	/**
	 * Returns this node's parent or null if this node has no parent.
	 * 
	 * @return this node's parent {@link XdevTreeNode}, or null if this node has
	 *         no parent
	 */
	public XdevTreeNode getXdevParent()
	{
		Object parent = getParent();
		if(parent instanceof XdevTreeNode)
		{
			return (XdevTreeNode)parent;
		}

		return null;
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public TreeNode getChildAt(int index)
	{
		return (XdevTreeNode)super.getChildAt(index);
	}


	/**
	 * Returns a {@link XdevList} containing the children of this
	 * {@link XdevTreeNode}.
	 * 
	 * <p>
	 * Only the children of this {@link XdevTreeNode} are returned. The children
	 * of the children are <em>not</em> returned.
	 * </p>
	 * 
	 * @return a {@link XdevList} containing the children of this
	 *         {@link XdevTreeNode}.
	 */
	public <T extends TreeNode> List<T> getChildren()
	{
		int c = getChildCount();
		XdevList<T> list = new XdevList<T>(c);
		for(int i = 0; i < c; i++)
		{
			list.add((T)getChildAt(i));
		}

		return list;
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setUserObject(Object o)
	{
		super.setUserObject(o);

		DefaultTreeModel model = getModel();
		if(model != null)
		{
			model.nodeChanged(this);
		}
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public void add(MutableTreeNode node)
	{
		super.add(node);

		DefaultTreeModel model = getModel();
		if(model != null)
		{
			model.nodesWereInserted(this,new int[]{getChildCount() - 1});
		}
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public void insert(MutableTreeNode node, int index)
	{
		node.removeFromParent();

		super.insert(node,index);

		DefaultTreeModel model = getModel();
		if(model != null)
		{
			model.nodesWereInserted(this,new int[]{index});
		}
	}


	/**
	 * Removes <code>node</code> from this node's child array, giving it a null
	 * parent.
	 * 
	 * @param node
	 *            a child of this node to remove
	 * @return the removed node
	 * @exception IllegalArgumentException
	 *                if <code>node</code> is null or is not a child of this
	 *                node
	 */
	public XdevTreeNode removeNode(XdevTreeNode node) throws IllegalArgumentException
	{
		remove(node);
		return node;
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public void remove(MutableTreeNode node)
	{
		if(isNodeChild(node))
		{
			int index = getIndex(node);

			super.remove(node);

			DefaultTreeModel model = getModel();
			if(model != null)
			{
				model.nodesWereRemoved(this,new int[]{index},new Object[]{node});
			}
		}
	}


	/**
	 * Removes the node at the specified index from this node's children and
	 * sets that node's parent to null.
	 * 
	 * @param index
	 *            the index in this node's child array of the child to remove * @return
	 *            the removed node
	 * @exception ArrayIndexOutOfBoundsException
	 *                if <code>childIndex</code> is out of bounds
	 */
	public XdevTreeNode removeNodeAt(int index) throws ArrayIndexOutOfBoundsException
	{
		TreeNode node = getChildAt(index);

		super.remove(index);

		DefaultTreeModel model = getModel();
		if(model != null)
		{
			model.nodesWereRemoved(this,new int[]{index},new Object[]{node});
		}

		return (XdevTreeNode)node;
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public void remove(int index)
	{
		TreeNode node = getChildAt(index);

		super.remove(index);

		DefaultTreeModel model = getModel();
		if(model != null)
		{
			model.nodesWereRemoved(this,new int[]{index},new Object[]{node});
		}
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public void removeAllChildren()
	{
		super.removeAllChildren();

		DefaultTreeModel model = getModel();
		if(model != null)
		{
			model.nodeStructureChanged(this);
		}
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public void removeFromParent()
	{
		DefaultTreeModel model = getModel();
		TreeNode parent = getParent();
		if(parent != null)
		{
			int index = parent.getIndex(this);

			super.removeFromParent();

			if(model != null)
			{
				model.nodesWereRemoved(parent,new int[]{index},new Object[]{this});
			}
		}
	}


	/**
	 * Returns the path from the root, to get to this node. The last element in
	 * the path is this node.
	 * 
	 * @return an {@link XdevList} of TreeNode objects giving the path, where
	 *         the first element in the path is the root and the last element is
	 *         this node.
	 */
	public <T extends TreeNode> List<T> getPathAsList()
	{
		return (List<T>)new XdevList<TreeNode>(getPath());
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString()
	{
		return caption;
	}


	/**
	 * Returns the caption of this {@link XdevTreeNode}.
	 * 
	 * @return the caption of this {@link XdevTreeNode}.
	 */
	public String getCaption()
	{
		return caption;
	}


	/**
	 * Sets the caption of this {@link XdevTreeNode}.
	 * 
	 * @param caption
	 *            the caption of this {@link XdevTreeNode}.
	 */
	public void setCaption(String caption)
	{
		this.caption = caption;

		DefaultTreeModel model = getModel();
		if(model != null)
		{
			model.nodeChanged(this);
		}
	}


	/**
	 * Returns the {@link Icon} of this {@link XdevTreeNode}.
	 * 
	 * @return the {@link Icon} of this {@link XdevTreeNode}.
	 */
	public Icon getIcon()
	{
		return icon;
	}


	/**
	 * Sets the {@link Icon} of this {@link XdevTreeNode}.
	 * 
	 * @param icon
	 *            the {@link Icon} of this {@link XdevTreeNode}.
	 */
	public void setIcon(Icon icon)
	{
		this.icon = icon;

		DefaultTreeModel model = getModel();
		if(model != null)
		{
			model.nodeChanged(this);
		}
	}


	/**
	 * Creates and returns an {@link XdevList} that traverses the subtree rooted
	 * at this node in preorder. The first node of the {@link XdevList} is this
	 * node.
	 * 
	 * 
	 * 
	 * @return an XdevList for traversing the tree in preorder
	 */
	public <T extends TreeNode> List<T> getSubTree()
	{
		XdevList<T> subTree = new XdevList<>();

		@SuppressWarnings("unchecked")
		// OK, because Enumeration comes untyped
		Enumeration<T> postOrder = (Enumeration<T>)preorderEnumeration();
		while(postOrder.hasMoreElements())
		{
			subTree.add(postOrder.nextElement());
		}

		return subTree;
	}


	/**
	 * Searches the tree below (including) this {@link XdevTreeNode} for the
	 * <strong>first</strong> node matching the given <code>userObject</code>
	 * and/or the given <code>caption</code>.
	 * 
	 * @param userObject
	 *            to search for. If <code>userObject</code> is <code>null</code>
	 *            , the searched for node must only match <code>caption</code>.
	 * @param caption
	 *            to search for. If <code>caption</code> is <code>null</code>,
	 *            the searched for node must only match <code>userObject</code>.
	 * @return the node that matches <code>userObject</code> and/or the given
	 *         <code>caption</code>.
	 */
	public XdevTreeNode searchNode(Object userObject, String caption)
	{
		XdevTreeNode found = null;
		Enumeration<?> e = breadthFirstEnumeration();
		while(e.hasMoreElements() && found == null)
		{
			XdevTreeNode node = (XdevTreeNode)e.nextElement();
			Object uo = node.getUserObject();

			if((caption == null ? true : node.caption.equals(caption))
					&& (userObject == null ? true : uo != null && uo.equals(userObject)))
			{
				found = node;
			}
		}
		return found;
	}


	/**
	 * Searches the tree below (including) this {@link XdevTreeNode} for the
	 * <strong>all</strong> node matching the given <code>userObject</code>
	 * and/or the given <code>caption</code>.
	 * 
	 * @param userObject
	 *            to search for. If <code>userObject</code> is <code>null</code>
	 *            , the searched for node must only match <code>caption</code>.
	 * @param caption
	 *            to search for. If <code>caption</code> is <code>null</code>,
	 *            the searched for node must only match <code>userObject</code>.
	 * @return the node that matches <code>userObject</code> and/or the given
	 *         <code>caption</code>.
	 */
	public <T extends TreeNode> List<T> searchNodes(Object userObject, String caption)
	{
		List<T> list = null;
		Enumeration<T> e = (Enumeration<T>)breadthFirstEnumeration();
		while(e.hasMoreElements())
		{
			XdevTreeNode node = (XdevTreeNode)e.nextElement();
			Object uo = node.getUserObject();

			if((caption == null ? true : node.caption.equals(caption))
					&& (userObject == null ? true : uo != null && uo.equals(userObject)))
			{
				if(list == null)
				{
					list = new XdevList();
				}

				list.add((T)node);
			}
		}
		return list;
	}


	/**
	 * Sets the {@link TreeModel} for this {@link XdevTreeNode}.
	 * 
	 * @param model
	 *            the {@link TreeModel} for this {@link XdevTreeNode}.
	 */
	public void setModel(DefaultTreeModel model)
	{
		this.model = model;
	}


	/**
	 * Returns the {@link TreeModel} for this {@link XdevTreeNode}.
	 * 
	 * @return the {@link TreeModel} for this {@link XdevTreeNode}.
	 */
	public DefaultTreeModel getModel()
	{
		XdevTreeNode node = this;
		while(node != null)
		{
			if(node.model != null)
			{
				return node.model;
			}
			if(node.parent instanceof XdevTreeNode)
			{
				node = (XdevTreeNode)node.parent;
			}
			else
			{
				break;
			}
		}

		return null;
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public XdevTreeNode clone()
	{
		return new XdevTreeNode(userObject,caption,icon);
	}


	/**
	 * Clones a {@link XdevTreeNode} and all its children (deep clone).
	 * 
	 * @return a deep clone of this {@link XdevTreeNode}.
	 */
	public XdevTreeNode cloneTree()
	{
		XdevTreeNode clone = clone();
		for(int i = 0; i < getChildCount(); i++)
		{
			XdevTreeNode child = (XdevTreeNode)getChildAt(i);
			clone.add(child.cloneTree());
		}
		return clone;
	}
}
