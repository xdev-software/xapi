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


import java.awt.*;
import javax.swing.*;
import javax.swing.tree.*;


public class XdevTreeRenderer extends DefaultTreeCellRenderer
{
	private final XdevTreeManager	manager;


	public XdevTreeRenderer(XdevTreeManager manager)
	{
		this.manager = manager;
	}


	@Override
	public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel,
			boolean expanded, boolean leaf, int row, boolean hasFocus)
	{
		super.getTreeCellRendererComponent(tree,value,sel,expanded,leaf,row,hasFocus);

		if(value instanceof XdevTreeNode)
		{
			XdevTreeNode node = (XdevTreeNode)value;

			setText(manager.getCaption(node));
			setIcon(manager.getIcon(node));
		}

		return this;
	}
}
