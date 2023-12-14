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
package xdev.ui.tree;


import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.EventObject;
import java.util.List;

import javax.swing.JTree;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import xdev.ui.XdevMenu;


public final class TreeUtils
{
	private TreeUtils()
	{
	}
	

	public static void registerPopup(final JTree tree, final XdevMenu popup)
	{
		registerPopupHandler(tree,new TreePopupHandler()
		{
			public void showPopup(EventObject trigger, Object treeNode, Point p)
			{
				if(treeNode != null)
				{
					tree.requestFocus();
					popup.show(tree,p.x,p.y);
				}
			}
		});
	}
	

	public static void registerPopupHandler(final JTree tree, final TreePopupHandler handler)
	{
		tree.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mousePressed(MouseEvent e)
			{
				mouseAction(e);
			}
			

			@Override
			public void mouseReleased(MouseEvent e)
			{
				mouseAction(e);
			}
			

			private void mouseAction(MouseEvent e)
			{
				if(e.isPopupTrigger())
				{
					Object node = null;
					
					TreePath path = tree.getPathForLocation(e.getX(),e.getY());
					if(path != null)
					{
						node = path.getLastPathComponent();
						if(!tree.isPathSelected(path))
						{
							tree.setSelectionPath(path);
						}
						tree.setLeadSelectionPath(path);
					}
					
					Point p = tree.getPopupLocation(e);
					if(p == null)
					{
						p = e.getPoint();
					}
					
					handler.showPopup(e,node,p);
				}
			}
		});
		
		tree.addKeyListener(new KeyAdapter()
		{
			@Override
			public void keyPressed(KeyEvent e)
			{
				if(e.getKeyCode() == KeyEvent.VK_CONTEXT_MENU)
				{
					TreePath path = tree.getLeadSelectionPath();
					if(path != null)
					{
						Rectangle r = tree.getPathBounds(path);
						handler.showPopup(e,path.getLastPathComponent(),new Point(r.x,r.y
								+ r.height));
					}
				}
			}
		});
	}
	

	/**
	 * @since 3.2
	 */
	public static TreePath getTreePath(TreeNode node)
	{
		List<TreeNode> list = new ArrayList();
		while(node != null)
		{
			list.add(0,node);
			node = node.getParent();
		}
		return new TreePath(list.toArray());
	}
}
