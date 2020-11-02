package xdev.ui.text;

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


public interface Sign
{
	public SignBounds getBounds();
	

	public boolean isTab();
	

	public boolean isBreak();
	

	public boolean isSpace();
	

	public int getDescent();
	

	public int getLeading();
	

	public int getDrawY();
	

	public void setDrawY(int i);
	

	public void setLink(Link l);
	

	public Link getLink();
	

	public boolean canBreak();
	

	public boolean equalsStyleAndRow(Sign s);
	

	public boolean equalsRow(Sign s);
	

	public void addYdiff(int diff);
}
