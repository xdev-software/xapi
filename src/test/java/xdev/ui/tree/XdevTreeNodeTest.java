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
	private XdevTreeNode createTestNodes(){
		XdevTreeNode root = new XdevTreeNode("root");
		root.setCaption("root");
		
		XdevTreeNode level1 = new XdevTreeNode("level1");
		level1.setCaption("level1");
		
		root.add(level1);
		
		
		return root;
	}
	
	
	@Test
	public void getSubTree(){
		
		this.createTestNodes().getSubTree();
	}
}
