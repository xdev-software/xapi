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
import java.awt.Font;
import java.awt.Insets;

import xdev.ui.text.XdevDocument;


public interface XdevTextComponent extends TextComponent
{
	public Component getCpn();
	

	public void repaint();
	

	public XdevDocument getDocument();
	

	public Color getForeground();
	

	public Font getFont();
	

	public boolean getUnderline();
	

	public Dimension getOriginalSize();
	

	public Dimension getSize();
	

	public void setSize(Dimension d);
	

	public Dimension getPreferredSize();
	

	public void setPreferredSize(Dimension d);
	

	public boolean paintAsImage();
	

	public void setPaintText();
	

	public int getHorizontalAlign();
	

	public boolean isVertical();
	

	public int getVerticalAlign();
	

	public Insets getBorderInsets(boolean withTextBorder);
	

	public Insets getTextInsets();
	

	public void setTextInsets(Insets i);
	

	@Deprecated
	public int getTextColumnCount();
	

	@Deprecated
	public void setTextColumnCount(int i);
	

	@Deprecated
	public int getTextColumnGap();
	

	@Deprecated
	public void setTextColumnGap(int textColumnGap);
}
