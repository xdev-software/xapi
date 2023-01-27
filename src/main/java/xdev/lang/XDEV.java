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
package xdev.lang;


import xdev.db.DBDataSource;
import xdev.db.DBException;
import xdev.lang.cmd.FillTree;
import xdev.lang.cmd.OpenWindow;
import xdev.lang.cmd.Query;
import xdev.ui.XdevWindow;
import xdev.ui.XdevTree;
import xdev.vt.VirtualTable;
import xdev.vt.VirtualTableException;


/**
 * The XDEV class contains the static XDEV methods which can be parameterized by
 * a wizard in the IDE.
 * 
 * 
 * @author XDEV Software
 */

@LibraryMember
public final class XDEV
{
	/**
	 * No instanciation.
	 */
	private XDEV()
	{
	}
	

	/**
	 * Queries a {@link DBDataSource} with a query and reads the data into a
	 * {@link VirtualTable}.
	 * 
	 * @param query
	 *            The {@link Query} data object
	 * @return the target VirtualTable
	 */
	public static VirtualTable Query(Query query) throws DBException, VirtualTableException
	{
		query.prepare();
		query.execute();
		return query.getVirtualTable();
	}
	

	/**
	 * Opens a {@link XdevWindow} in a specific container.
	 * 
	 * @param openWindow
	 *            The {@link OpenWindow} data object
	 * @return the opened window
	 */
	public static XdevWindow OpenWindow(OpenWindow openWindow)
	{
		openWindow.prepare();
		openWindow.execute();
		return openWindow.getWindow();
	}
	

	/**
	 * Creates a data model for a {@link XdevTree}.
	 */
	public static void FillTree(FillTree fillTree) throws DBException, VirtualTableException
	{
		fillTree.prepare();
		fillTree.execute();
	}
}
