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


import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JTree;
import javax.swing.UIManager;
import javax.swing.plaf.TreeUI;
import javax.swing.plaf.metal.MetalTreeUI;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeCellRenderer;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import xdev.ui.persistence.Persistable;
import xdev.ui.tree.DefaultXdevTreeManager;
import xdev.ui.tree.TreeUtils;
import xdev.ui.tree.XdevTreeManager;
import xdev.ui.tree.XdevTreeModel;
import xdev.ui.tree.XdevTreeNode;
import xdev.ui.tree.XdevTreeRenderer;
import xdev.util.ObjectUtils;
import xdev.util.XdevList;
import xdev.util.logging.LoggerFactory;
import xdev.util.logging.XdevLogger;
import xdev.vt.VirtualTable;
import xdev.vt.VirtualTable.VirtualTableRow;
import xdev.vt.VirtualTableException;


/**
 * The standard tree in XDEV. Based on {@link JTree}.
 * 
 * @see JTree
 * 
 * @author XDEV Software
 * 
 * @since 2.0
 */
@BeanSettings(useXdevCustomizer = true)
public class XdevTree extends JTree implements XdevFocusCycleComponent, Persistable
{
	
	/**
	 * Logger instance for this class.
	 */
	private static final XdevLogger	log							= LoggerFactory
																		.getLogger(XdevTree.class);
	
	public final static String		HOLE_LINE_TREE_PROPERTY		= "holdLineTree";
	public final static String		PAINT_TREE_LINES_PROPERTY	= "paintTreeLines";
	public final static String		OPEN_FOLDER_ICON_PROPERTY	= "openFolderIcon";
	public final static String		CLOSED_FOLDER_ICON_PROPERTY	= "closedFolderIcon";
	public final static String		LEAF_ICON_PROPERTY			= "leafIcon";
	
	private boolean					holeLineTree				= true;
	private boolean					paintTreeLines				= true;
	private Icon					openFolderIcon				= null;
	private Icon					closedFolderIcon			= null;
	private Icon					leafIcon					= null;
	/**
	 * tabIndex is used to store the index for {@link XdevFocusCycleComponent}
	 * functionality.
	 */
	private int						tabIndex					= -1;
	
	/**
	 * Should the gui state be persisted? Defaults to {@code true}.
	 */
	private boolean					persistenceEnabled			= true;
	
	
	/**
	 * Constructs a {@link XdevTree} that is initialized with
	 * {@link DefaultTreeModel} and a {@link DefaultXdevTreeManager}.
	 * 
	 * 
	 * @see #XdevTree(TreeModel)
	 */
	public XdevTree()
	{
		this(new DefaultTreeModel(null));
	}
	
	
	/**
	 * Constructs a {@link XdevTree} that is initialized with
	 * {@link XdevTreeModel} and a {@link DefaultXdevTreeManager}.
	 * 
	 * @param root
	 *            a {@link XdevTreeNode} object that is the root of the tree
	 * 
	 * @see #XdevTree(TreeModel)
	 * @see XdevTreeNode
	 */
	public XdevTree(XdevTreeNode root)
	{
		this(new XdevTreeModel(root));
	}
	
	
	/**
	 * Constructs a {@link XdevTree} that is initialized with <code>model</code>
	 * as the data model and a {@link DefaultXdevTreeManager}.
	 * 
	 * @param model
	 *            the <code>TreeModel</code> to use as the data model
	 * 
	 * @see JTree#JTree(TreeModel)
	 * @see #setDefaultTreeManager()
	 * @see DefaultXdevTreeManager
	 */
	public XdevTree(TreeModel model)
	{
		super(model);
		
		setDefaultTreeManager();
	}
	
	
	// /**
	// * Sets the <code>TreeModel</code> that will provide the data.
	// *
	// * @param newModel
	// * the <code>TreeModel</code> that is to provide the data
	// * @beaninfo bound: true description: The TreeModel that will provide the
	// * data.
	// */
	public void setRoot(TreeNode root)
	{
		if(root instanceof XdevTreeNode)
		{
			setModel(new XdevTreeModel((XdevTreeNode)root));
		}
		else
		{
			setModel(new DefaultTreeModel(root));
		}
	}
	
	
	/**
	 * Returns the root of the tree.
	 * 
	 * @return the root of the tree or <code>null</code> if this
	 *         {@link XdevTree} has no nodes or the root is not a instance of
	 *         {@link XdevTreeNode}
	 */
	public XdevTreeNode getRoot()
	{
		Object root = getModel().getRoot();
		if(root != null && root instanceof XdevTreeNode)
		{
			return (XdevTreeNode)root;
		}
		
		return null;
	}
	
	
	/**
	 * Returns the {@link XdevTreeManager} of this {@link XdevTree}.
	 * 
	 * @return the {@link XdevTreeManager} of this {@link XdevTree}. If no valid
	 *         tree manager is available <code>null</code> is returned
	 */
	public XdevTreeManager getTreeManager()
	{
		TreeCellRenderer renderer = getCellRenderer();
		if(renderer instanceof XdevTreeManager)
		{
			return (XdevTreeManager)renderer;
		}
		
		return null;
	}
	
	
	/**
	 * Sets the tree manager for this {@link XdevTree}. <br>
	 * If the given <code>manager</code> is null the method
	 * {@link #setDefaultTreeManager()} is called.
	 * 
	 * @param manager
	 *            the new {@link XdevTreeManager}
	 */
	public void setTreeManager(XdevTreeManager manager)
	{
		if(manager == null)
		{
			setDefaultTreeManager();
		}
		else
		{
			setCellRenderer(new XdevTreeRenderer(manager));
		}
	}
	
	
	/**
	 * Sets a {@link DefaultXdevTreeManager} as tree manager for this
	 * {@link XdevTree}.
	 * 
	 * @see #setCellRenderer(TreeCellRenderer)
	 * @see XdevTreeRenderer
	 * @see DefaultXdevTreeManager
	 */
	public void setDefaultTreeManager()
	{
		setCellRenderer(new XdevTreeRenderer(new DefaultXdevTreeManager(this)));
	}
	
	
	/**
	 * Creates a tree model for this tree containing data of the VirtualTable
	 * <code>vt</code> by creating {@link XdevTreeNode}s out of
	 * {@link VirtualTableRow}s.
	 * <p>
	 * This is a synonym for:
	 * 
	 * <pre>
	 * setModel(vt,rootsColumnName,rootsID,idColumnName,ownerColumnName,captionColumnName,dataColumnName,
	 * 		false);
	 * </pre>
	 * <p>
	 * For a detailed description see
	 * {@link VirtualTable#createTree(String, Object, String, String, String, String, boolean)}
	 * 
	 * @param vt
	 *            the data source
	 * @param rootsColumnName
	 *            the column name for the root condition
	 * @param rootsID
	 *            the value for the root condition
	 * @param idColumnName
	 *            the id column name
	 * @param ownerColumnName
	 *            the referenced owner column name
	 * @param captionColumnName
	 *            the column name for the caption of the nodes
	 * @param dataColumnName
	 *            the data column name, or <code>null</code> if the
	 *            {@link VirtualTableRow}s should be used as data object
	 */
	public void setModel(VirtualTable vt, String rootsColumnName, Object rootsID,
			String idColumnName, String ownerColumnName, String captionColumnName,
			String dataColumnName)
	{
		setModel(vt,rootsColumnName,rootsID,idColumnName,ownerColumnName,captionColumnName,
				dataColumnName,false);
	}
	
	
	/**
	 * Creates a tree model for this tree containing data of the VirtualTable
	 * <code>vt</code> by creating {@link XdevTreeNode}s out of
	 * {@link VirtualTableRow}s.
	 * <p>
	 * For a detailed description see
	 * {@link VirtualTable#createTree(String, Object, String, String, String, String, boolean)}
	 * 
	 * @param vt
	 *            the data source
	 * @param rootsColumnName
	 *            the column name for the root condition
	 * @param rootsID
	 *            the value for the root condition
	 * @param idColumnName
	 *            the id column name
	 * @param ownerColumnName
	 *            the referenced owner column name
	 * @param captionColumnName
	 *            the column name for the caption of the nodes
	 * @param dataColumnName
	 *            the data column name, or <code>null</code> if the
	 *            {@link VirtualTableRow}s should be used as data object
	 * @param multipleRoots
	 *            <code>true</code> for a dummy root with multiple sub-roots, or
	 *            <code>false</code> for a single root
	 * 
	 * @since 3.1
	 */
	public void setModel(final VirtualTable vt, String rootsColumnName, Object rootsID,
			String idColumnName, String ownerColumnName, String captionColumnName,
			String dataColumnName, boolean multipleRoots)
	{
		try
		{
			vt.setModelFor(XdevTree.this,rootsColumnName,rootsID,idColumnName,ownerColumnName,
					captionColumnName,dataColumnName,multipleRoots);
		}
		catch(VirtualTableException e)
		{
			log.error(e);
		}
	}
	
	
	/**
	 * Sets the selection model, which must be one of SINGLE_TREE_SELECTION,
	 * CONTIGUOUS_TREE_SELECTION or DISCONTIGUOUS_TREE_SELECTION in
	 * {@link TreeSelectionModel}.
	 * <p>
	 * 
	 * This may change the selection if the current selection is not valid for
	 * the new mode. For example, if three TreePaths are selected when the mode
	 * is changed to <code>SINGLE_TREE_SELECTION</code>, only one TreePath will
	 * remain selected. It is up to the particular implementation to decide what
	 * TreePath remains selected.
	 * 
	 * @param mode
	 *            the new selection mode
	 * 
	 * @see TreeSelectionModel#setSelectionMode(int)
	 */
	public void setSelectionMode(int mode)
	{
		getSelectionModel().setSelectionMode(mode);
	}
	
	
	/**
	 * Returns the current selection mode. <br>
	 * Possible values in {@link TreeSelectionModel}:
	 * <ul>
	 * <li><code>SINGLE_TREE_SELECTION</code></li>
	 * <li><code>CONTIGUOUS_TREE_SELECTION</code></li>
	 * <li><code>DISCONTIGUOUS_TREE_SELECTION</code></li>
	 * </ul>
	 * 
	 * @return the selection mode as <code>int</code>
	 * 
	 * @see TreeSelectionModel
	 */
	public int getSelectionMode()
	{
		return getSelectionModel().getSelectionMode();
	}
	
	
	/**
	 * @return the objects of the selected path as a list, or <code>null</code>
	 *         if nothing is selected
	 */
	public List<?> getSelectionPathAsList()
	{
		TreePath tp = getSelectionPath();
		if(tp != null)
		{
			return new XdevList(tp.getPath());
		}
		
		return null;
	}
	
	
	/**
	 * Selects the node identified by the given <code>path</code>. If the given
	 * <code>path</code> is <code>null</code> the selection is canceled.
	 * 
	 * @param path
	 *            the {@link Collection} with the paths specifying the
	 *            selection
	 * 
	 * @see #clearSelection()
	 */
	public void setSelectionPath(Collection<?> path)
	{
		if(path == null || path.size() == 0)
		{
			clearSelection();
		}
		else
		{
			setSelectionPath(new TreePath(path.toArray()));
		}
	}
	
	
	/**
	 * @return the objects of the selected paths as lists, or <code>null</code>
	 *         if nothing is selected
	 */
	public List<List<?>> getSelectionPathsAsList()
	{
		if(getSelectionCount() > 0)
		{
			List<List<?>> list = new XdevList();
			
			TreePath[] tp = getSelectionPaths();
			for(int i = 0; i < tp.length; i++)
			{
				list.add(new XdevList(tp[i].getPath()));
			}
			
			return list;
		}
		
		return null;
	}
	
	
	/**
	 * Sets the selection by creating {@link TreePath}s out of <code>path</code>
	 * . <br>
	 * Each element of path should be either a {@link TreePath} or a
	 * {@link Collection}.
	 * <p>
	 * If <code>path</code> is <code>null</code> or empty the selection is
	 * cleared.
	 * 
	 * @param path
	 *            the path elements
	 * @throws IllegalArgumentException
	 *             if an element of path isn't either a {@link TreePath} nor a
	 *             {@link List}
	 */
	public void setSelectionPaths(Collection<?> path) throws IllegalArgumentException
	{
		if(path == null || path.size() == 0)
		{
			clearSelection();
		}
		else
		{
			List<TreePath> list = new ArrayList();
			for(Object o : path)
			{
				if(o instanceof TreePath)
				{
					list.add((TreePath)o);
				}
				else if(o instanceof Collection)
				{
					list.add(new TreePath(((Collection)o).toArray()));
				}
				else
				{
					throw new IllegalArgumentException();
				}
			}
			
			setSelectionPaths(list.toArray(new TreePath[list.size()]));
		}
	}
	
	
	/**
	 * Returns the first selected {@link XdevTreeNode}.
	 * 
	 * @return the first selected {@link XdevTreeNode}. If the selected node is
	 *         not a {@link XdevTreeNode} or no node is selected
	 *         <code>null</code> is returned.
	 * 
	 * @see TreePath
	 */
	public XdevTreeNode getSelectedNode()
	{
		TreePath tp = getSelectionPath();
		if(tp != null)
		{
			Object o = tp.getLastPathComponent();
			if(o instanceof XdevTreeNode)
			{
				return (XdevTreeNode)o;
			}
		}
		
		return null;
	}
	
	
	/**
	 * Selects the node identified by the given <code>node</code>. If the given
	 * <code>node</code> is <code>null</code> the selection is canceled.
	 * 
	 * @param node
	 *            the {@link XdevTreeNode} specifying the selection
	 * 
	 * @see #clearSelection()
	 */
	public void setSelectedNode(XdevTreeNode node)
	{
		if(node == null)
		{
			clearSelection();
		}
		else
		{
			setSelectionPath(new TreePath(node.getPath()));
		}
	}
	
	
	/**
	 * Returns a {@link List} of all selected nodes.
	 * 
	 */
	public List<DefaultMutableTreeNode> getSelectedNodes()
	{
		if(getSelectionCount() > 0)
		{
			List<DefaultMutableTreeNode> list = new ArrayList();
			
			TreePath[] tp = getSelectionPaths();
			for(int i = 0; i < tp.length; i++)
			{
				Object o = tp[i].getLastPathComponent();
				if(o instanceof DefaultMutableTreeNode)
				{
					list.add((DefaultMutableTreeNode)o);
				}
				else
				{
					return null;
				}
			}
			
			return list;
		}
		
		return null;
	}
	
	
	/**
	 * Selects all nodes identified by the given {@link Collection}. If the
	 * given {@link Collection} is <code>null</code> the selection is canceled.
	 * 
	 * @param nodes
	 *            the {@link Collection} specifying the selection
	 * 
	 * @see #clearSelection()
	 */
	public void setSelectedNodes(Collection<? extends DefaultMutableTreeNode> nodes)
	{
		if(nodes == null || nodes.size() == 0)
		{
			clearSelection();
		}
		else
		{
			List<TreePath> list = new ArrayList();
			for(DefaultMutableTreeNode node : nodes)
			{
				list.add(new TreePath(node.getPath()));
			}
			
			setSelectionPaths(list.toArray(new TreePath[list.size()]));
		}
	}
	
	
	/**
	 * Ensures that the node identified by the specified path is expanded and
	 * viewable. If the last item in the path is a leaf, this will have no
	 * effect.
	 * 
	 * @param path
	 *            the path to expand
	 */
	public void expandPath(List<?> path)
	{
		expandPath(new TreePath(path.toArray()));
	}
	
	
	/**
	 * Ensures that the <code>node</code> is expanded and viewable.
	 * 
	 * @param node
	 *            the {@link XdevTreeNode} to expand
	 * 
	 * @see #expandPath(TreePath)
	 */
	public void expandNode(XdevTreeNode node)
	{
		expandPath(new TreePath(node.getPath()));
	}
	
	
	/**
	 * Expand all nodes of this {@link XdevTree}.
	 * 
	 * 
	 * @see #expandAll(DefaultMutableTreeNode)
	 */
	public void expandAll()
	{
		expandAll((TreeNode)getModel().getRoot());
	}
	
	
	/**
	 * Expand all nodes which the <code>DefaultMutableTreeNode</code> contains.
	 * 
	 * @param node
	 *            the <code>DefaultMutableTreeNode</code> identifying the nodes
	 * 
	 * @see #expandPath(TreePath)
	 */
	public void expandAll(TreeNode node)
	{
		expandPath(TreeUtils.getTreePath(node));
		for(int i = 0; i < node.getChildCount(); i++)
		{
			expandAll(node.getChildAt(i));
		}
	}
	
	
	/**
	 * Ensures that the node identified by the specified path is collapsed and
	 * viewable.
	 * 
	 * @param path
	 *            the path to collapse
	 */
	public void collapsePath(Collection<?> path)
	{
		collapsePath(new TreePath(path.toArray()));
	}
	
	
	/**
	 * Ensures that the <code>node</code> is collapsed and viewable.
	 * 
	 * @param node
	 *            the {@link XdevTreeNode} to collapse
	 */
	public void collapseNode(XdevTreeNode node)
	{
		collapsePath(new TreePath(node.getPath()));
	}
	
	
	/**
	 * Ensures that all nodes in this {@link XdevTree} are collapsed.
	 * 
	 * @see #collapseRow(int)
	 */
	public void collapseAll()
	{
		for(int i = getRowCount() - 1; i >= 0; i--)
		{
			collapseRow(i);
		}
	}
	
	
	/**
	 * Returns the {@link XdevTreeNode} at the specified location.
	 * 
	 * @param x
	 *            an integer giving the number of pixels horizontally from the
	 *            left edge of the display area, minus any left margin
	 * @param y
	 *            an integer giving the number of pixels vertically from the top
	 *            of the display area, minus any top margin
	 * 
	 * @return the <code>XdevTreeNode</code> for the node at that location. If
	 *         no valid node is available <code>null</code> is returned.
	 */
	public XdevTreeNode getNodeAt(int x, int y)
	{
		TreePath tp = getPathForLocation(x,y);
		if(tp != null)
		{
			Object o = tp.getLastPathComponent();
			if(o instanceof XdevTreeNode)
			{
				return (XdevTreeNode)o;
			}
		}
		
		return null;
	}
	
	
	/**
	 * Sets the hole line tree property.
	 * 
	 * @param holeLineTree
	 *            <code>true</code> if a tree line should always cover the whole
	 *            width of the tree, <code>false</code> otherwise
	 */
	public void setHoleLineTree(boolean holeLineTree)
	{
		if(this.holeLineTree != holeLineTree)
		{
			boolean oldValue = this.holeLineTree;
			this.holeLineTree = holeLineTree;
			firePropertyChange(HOLE_LINE_TREE_PROPERTY,oldValue,holeLineTree);
			repaint();
		}
	}
	
	
	/**
	 * Gets the <code>holeLineTree</code> property.
	 * 
	 * @return the value of the <code>holeLineTree</code> property
	 * 
	 * @see #setHoleLineTree(boolean)
	 */
	public boolean getHoleLineTree()
	{
		return holeLineTree;
	}
	
	
	/**
	 * Sets the <code>paintTreeLines</code> property. If <code>true</code> the
	 * tree line is painted. The default value for the
	 * <code>paintTreeLines</code> property is <code>true</code>.
	 * 
	 * @param paintTreeLines
	 *            if <code>true</code> the tree line is painted
	 * 
	 * @see #getPaintTreeLines()
	 */
	public void setPaintTreeLines(boolean paintTreeLines)
	{
		if(this.paintTreeLines != paintTreeLines)
		{
			boolean oldValue = this.paintTreeLines;
			this.paintTreeLines = paintTreeLines;
			firePropertyChange(PAINT_TREE_LINES_PROPERTY,oldValue,paintTreeLines);
			repaint();
		}
	}
	
	
	/**
	 * Gets the <code>paintTreeLines</code> property.
	 * 
	 * @return the value of the <code>paintTreeLines</code> property
	 * 
	 * @see #setPaintTreeLines(boolean)
	 */
	public boolean getPaintTreeLines()
	{
		return paintTreeLines;
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void updateUI()
	{
		setUI(new XdevTreeUI());
		invalidate();
	}
	
	
	/**
	 * Returns the {@link Icon} of the open folder nodes.
	 * 
	 * @return the {@link Icon} of the open folder nodes
	 */
	public Icon getOpenFolderIcon()
	{
		return openFolderIcon;
	}
	
	
	/**
	 * Sets the {@link Icon} which render the open folder nodes.
	 * 
	 * @param openFolderIcon
	 *            the new open folder icon
	 */
	public void setOpenFolderIcon(Icon openFolderIcon)
	{
		if(!ObjectUtils.equals(this.openFolderIcon,openFolderIcon))
		{
			Icon oldValue = this.openFolderIcon;
			this.openFolderIcon = openFolderIcon;
			firePropertyChange(OPEN_FOLDER_ICON_PROPERTY,oldValue,openFolderIcon);
			repaint();
		}
	}
	
	
	/**
	 * Returns the {@link Icon} of the closed folder nodes.
	 * 
	 * @return the {@link Icon} of the closed folder nodes
	 */
	public Icon getClosedFolderIcon()
	{
		return closedFolderIcon;
	}
	
	
	/**
	 * Sets the {@link Icon} which render the closed folder nodes.
	 * 
	 * @param closedFolderIcon
	 *            the new closed folder icon
	 */
	public void setClosedFolderIcon(Icon closedFolderIcon)
	{
		if(!ObjectUtils.equals(this.closedFolderIcon,closedFolderIcon))
		{
			Icon oldValue = this.closedFolderIcon;
			this.closedFolderIcon = closedFolderIcon;
			firePropertyChange(CLOSED_FOLDER_ICON_PROPERTY,oldValue,closedFolderIcon);
			repaint();
		}
	}
	
	
	/**
	 * Returns the {@link Icon} of the leaf nodes.
	 * 
	 * @return the {@link Icon} of the leaf nodes
	 */
	public Icon getLeafIcon()
	{
		return leafIcon;
	}
	
	
	/**
	 * Sets the {@link Icon} which render the leaf nodes.
	 * 
	 * @param leafIcon
	 *            the new leaf icon
	 */
	public void setLeafIcon(Icon leafIcon)
	{
		if(!ObjectUtils.equals(this.leafIcon,leafIcon))
		{
			Icon oldValue = this.leafIcon;
			this.leafIcon = leafIcon;
			firePropertyChange(LEAF_ICON_PROPERTY,oldValue,leafIcon);
			repaint();
		}
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Dimension getPreferredScrollableViewportSize()
	{
		int width = 250;
		
		if(treeModel != null)
		{
			Object o = treeModel.getRoot();
			if(o != null && o instanceof TreeNode)
			{
				TreeNode tn = (TreeNode)o;
				if(tn.getChildCount() > 0)
				{
					width = getPreferredSize().width;
				}
			}
		}
		
		int visRows = getVisibleRowCount();
		int height = -1;
		
		if(isFixedRowHeight())
		{
			height = visRows * getRowHeight();
		}
		else
		{
			TreeUI ui = getUI();
			if(ui != null && visRows > 0)
			{
				int rc = ui.getRowCount(this);
				if(rc >= visRows)
				{
					Rectangle bounds = getRowBounds(visRows - 1);
					if(bounds != null)
					{
						height = bounds.y + bounds.height;
					}
				}
				else if(rc > 0)
				{
					Rectangle bounds = getRowBounds(0);
					if(bounds != null)
					{
						height = bounds.height * visRows;
					}
				}
			}
			if(height == -1)
			{
				height = 16 * visRows;
			}
		}
		
		return new Dimension(width,height);
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
		this.collapseAll();
		String[] persistentValues = persistentState.split(Persistable.VALUE_SEPARATOR);
		for(int i = 0; i < persistentValues.length; i++)
		{
			if(persistentValues[i] != null && !persistentValues[i].trim().equals(""))
			{
				int row = Integer.parseInt(persistentValues[i]);
				this.expandRow(row);
			}
		}
	}
	
	
	/**
	 * {@inheritDoc}
	 * <p>
	 * Persisted properties:
	 * <ul>
	 * <li>Expanded tree nodes</li>
	 * </ul>
	 * </p>
	 */
	@Override
	public String savePersistentState()
	{
		XdevTreeNode root = this.getRoot();
		TreePath rowPath = new TreePath(root);
		
		StringBuilder persistentState = new StringBuilder();
		int rowCount = this.getRowCount();
		for(int i = 0; i < rowCount; i++)
		{
			TreePath path = this.getPathForRow(i);
			if(i == 0 || isDescendant(path,rowPath))
			{
				if(this.isExpanded(path))
				{
					if(persistentState.length() > 0)
					{
						persistentState.append(Persistable.VALUE_SEPARATOR);
					}
					persistentState.append(String.valueOf(i));
				}
			}
			else
				break;
		}
		return persistentState.toString();
	}
	
	
	/**
	 * Checks if {@code path1} is a descendant of {@code path2}.
	 * 
	 * @param path1
	 * @param path2
	 * @return {@code true} if {@code path1} is a descendant of {@code path2}.
	 */
	private static boolean isDescendant(TreePath path1, TreePath path2)
	{
		int count1 = path1.getPathCount();
		int count2 = path2.getPathCount();
		if(count1 <= count2)
			return false;
		while(count1 != count2)
		{
			path1 = path1.getParentPath();
			count1--;
		}
		return path1.equals(path2);
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
	
	
	/**
	 * {@inheritDoc}
	 */
	@BeanProperty(category = DefaultBeanCategories.MISC)
	@Override
	public void setForeground(Color fg)
	{
		super.setForeground(fg);
	}
	
	
	
	protected class XdevTreeUI extends MetalTreeUI
	{
		/**
		 * {@inheritDoc}
		 */
		@Override
		public void installUI(JComponent c)
		{
			super.installUI(c);
			
			int rowHeight = treeState.getRowHeight();
			if(rowHeight > 0)
			{
				treeState.setRowHeight(rowHeight + 2);
			}
		}
		
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		public Rectangle getPathBounds(JTree tree, TreePath path)
		{
			if(holeLineTree)
			{
				if(tree != null && treeState != null)
				{
					Insets i = tree.getInsets();
					Rectangle bounds = treeState.getBounds(path,null);
					
					if(bounds != null && i != null)
					{
						bounds.width = tree.getWidth() - bounds.x;
						bounds.x += i.left;
						bounds.width -= i.left;
						bounds.y += i.top;
					}
					
					return bounds;
				}
				return null;
			}
			else
			{
				return super.getPathBounds(tree,path);
			}
		}
		
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		public void paint(Graphics g0, JComponent c)
		{
			Graphics2D g = (Graphics2D)g0;
			
			if(paintTreeLines)
			{
				g.setColor(UIManager.getColor("control"));
				Rectangle r = g.getClipBounds();
				int beginRow = getRowForPath(tree,getClosestPathForLocation(tree,0,r.y));
				int endRow = getRowForPath(tree,
						getClosestPathForLocation(tree,0,r.y + r.height - 1));
				
				if(beginRow <= -1 || endRow <= -1)
				{
					return;
				}
				int w = c.getWidth();
				
				for(int i = beginRow; i <= endRow; ++i)
				{
					TreePath path = getPathForRow(tree,i);
					if(path != null)
					{
						r = getPathBounds(tree,getPathForRow(tree,i));
						if(r != null)
						{
							g.drawLine(0,r.y,w,r.y);
							if(i == endRow)
							{
								g.drawLine(0,r.y + r.height,w,r.y + r.height);
							}
						}
					}
				}
			}
			
			super.paint(g,c);
		}
		
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		protected void paintRow(Graphics g, Rectangle clipBounds, Insets insets, Rectangle r,
				TreePath path, int row, boolean isExpanded, boolean hasBeenExpanded, boolean isLeaf)
		{
			if(editingComponent != null && editingRow == row)
			{
				return;
			}
			
			if(holeLineTree)
			{
				r.width = tree.getWidth() - r.x - insets.left;
			}
			
			Component component = currentCellRenderer
					.getTreeCellRendererComponent(tree,path.getLastPathComponent(),
							tree.isRowSelected(row),isExpanded,isLeaf,row,false);
			
			if(holeLineTree)
			{
				r.width = tree.getWidth() - r.x;
			}
			
			rendererPane.paintComponent(g,component,tree,r.x,r.y,r.width,r.height,true);
			
			if(paintTreeLines)
			{
				g.setColor(UIManager.getColor("control"));
				g.drawLine(r.x,r.y,r.x + r.width,r.y);
			}
		}
		
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		protected void paintVerticalLine(Graphics g, JComponent c, int x, int top, int bottom)
		{
			g.setColor(UIManager.getColor("JTree.lineColor"));
			
			if(top % 2 != 0)
			{
				top--;
			}
			
			for(int y = top; y <= bottom; y += 2)
			{
				g.drawLine(x,y,x,y);
			}
		}
		
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		protected void paintHorizontalLine(Graphics g, JComponent c, int y, int left, int right)
		{
			g.setColor(UIManager.getColor("JTree.lineColor"));
			
			if(left % 2 != 0)
			{
				left++;
			}
			
			for(int x = left; x <= right; x += 2)
			{
				g.drawLine(x,y,x,y);
			}
		}
	}
}
