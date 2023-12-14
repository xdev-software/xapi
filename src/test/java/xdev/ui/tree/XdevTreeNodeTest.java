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

import java.util.List;

import javax.swing.tree.TreeNode;

import org.junit.Assert;

import org.junit.Test;


/**
 * 
 * Tests regarding {@link XdevTreeNode}.
 * 
 * @author RF
 *
 */
public final class XdevTreeNodeTest
{
	private XdevTreeNode createTestNodes()
	{
		final XdevTreeNode root = new XdevTreeNode("root");
		root.setCaption("root");
		
		final XdevTreeNode level1 = new XdevTreeNode("level1");
		level1.setCaption("level1");
		
		root.add(level1);
		
		return root;
	}
	
	@Test
	public void getSubTree()
	{
		
		final List<TreeNode> nodes = this.createTestNodes().getSubTree();
		
		final XdevTreeNode node1 = (XdevTreeNode)nodes.get(0);
		final XdevTreeNode node2 = (XdevTreeNode)nodes.get(1);
		
		Assert.assertEquals("root", node1.getCaption());
		Assert.assertEquals("level1", node2.getCaption());
	}
	
	@Test
	public void searchTreeNodes()
	{
		
		final XdevTreeNode root = this.createTestNodes();
		
		final List<TreeNode> nodes = root.searchNodes(null, "level1");
		Assert.assertEquals("level1", ((XdevTreeNode)nodes.get(0)).getCaption());
	}
	
}
