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
package xdev.lang.cmd;


import xdev.db.DBException;
import xdev.ui.XdevTree;
import xdev.ui.tree.XdevTreeModel;
import xdev.ui.tree.XdevTreeNode;
import xdev.vt.VirtualTable;
import xdev.vt.VirtualTableColumn;
import xdev.vt.VirtualTableException;
import xdev.vt.VirtualTable.VirtualTableRow;


public abstract class FillTree extends XdevCommandObject
{
	private static class Group
	{
		final VirtualTable			vt;
		final String				caption;
		final int					captionColumnIndex;
		boolean						queryData;
		final VirtualTableColumn	foreignKey;
		final VirtualTableColumn	parentKey;
		Group						child;
		

		Group(VirtualTable vt, String caption, boolean queryData, VirtualTableColumn foreignKey,
				VirtualTableColumn parentKey)
		{
			this.vt = vt;
			this.caption = caption != null ? caption : "";
			this.captionColumnIndex = vt.getColumnIndex(this.caption);
			this.queryData = queryData;
			this.foreignKey = foreignKey;
			this.parentKey = parentKey;
		}
		

		void query() throws DBException
		{
			if(queryData)
			{
				queryData = false;
				
				vt.queryAndFill();
			}
		}
	}
	
	private XdevTree	tree;
	private Group		rootGroup;
	

	public FillTree(Object... args)
	{
		super(args);
	}
	

	public void setTree(XdevTree tree)
	{
		this.tree = tree;
	}
	

	public void addGroup(VirtualTable vt, String caption, boolean queryData)
	{
		addGroup(vt,caption,queryData,null,null);
	}
	

	public void addGroup(VirtualTable vt, String caption, boolean queryData,
			VirtualTableColumn foreignKey, VirtualTableColumn parentKey)
	{
		if(vt == null)
		{
			throw new IllegalArgumentException("vt cannot be null");
		}
		
		Group parent = null;
		if(rootGroup == null)
		{
			if(foreignKey != null || parentKey != null)
			{
				throw new IllegalArgumentException("root group cannot have a foreign key");
			}
		}
		else
		{
			parent = rootGroup;
			while(parent.child != null)
			{
				parent = parent.child;
			}
			
			if(foreignKey == null || parentKey == null)
			{
				throw new IllegalArgumentException(
						"child group must specify the relation to the parent group");
			}
			else if(!foreignKey.getVirtualTable().equals(vt))
			{
				throw new IllegalArgumentException("foreignKey is not a column of vt");
			}
			else if(!parent.vt.equals(parentKey.getVirtualTable()))
			{
				throw new IllegalArgumentException(
						"parentKey doesn't match the parent's VirtualTable");
			}
		}
		
		Group group = new Group(vt,caption,queryData,foreignKey,parentKey);
		if(rootGroup == null)
		{
			rootGroup = group;
		}
		else
		{
			parent.child = group;
		}
	}
	

	@Override
	public void execute() throws DBException, VirtualTableException
	{
		if(tree == null)
		{
			CommandException.throwMissingParameter("tree");
		}
		
		if(rootGroup == null)
		{
			throw new CommandException("No groups defined");
		}
		
		XdevTreeNode root = new XdevTreeNode("root");
		fill(root,rootGroup,null,null);
		tree.setRootVisible(false);
		tree.setModel(new XdevTreeModel(root));
	}
	

	private void fill(XdevTreeNode parentNode, Group group, Group parentGroup, Object keyValue)
			throws DBException
	{
		group.query();
		
		for(VirtualTableRow row : group.vt.rows())
		{
			boolean use = false;
			if(parentGroup == null)
			{
				use = true;
			}
			else
			{
				Object value = row.get(group.foreignKey);
				use = VirtualTable.equals(value,keyValue);
			}
			
			if(!use)
			{
				continue;
			}
			
			String caption;
			if(group.captionColumnIndex >= 0)
			{
				caption = row.getFormattedValue(group.captionColumnIndex);
			}
			else
			{
				caption = row.format(group.caption);
			}
			
			XdevTreeNode node = new XdevTreeNode(row,caption);
			parentNode.add(node);
			
			if(group.child != null)
			{
				fill(node,group.child,group,row.get(group.child.parentKey));
			}
		}
	}
}
