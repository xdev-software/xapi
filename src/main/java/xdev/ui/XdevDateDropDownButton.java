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


import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.GrayFilter;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JComponent;


public class XdevDateDropDownButton extends JComponent
{
	private final static Image	calendarImage;
	private final static Image	disabledCalendarImage;
	public final static Icon	calendarIcon;
	static
	{
		calendarImage = GraphicUtils.loadResIcon("calendar.png").getImage();
		disabledCalendarImage = GrayFilter.createDisabledImage(calendarImage);
		calendarIcon = new ImageIcon(calendarImage);
	}
	
	
	public static void paintCalendarDropDownButton(Component c, Graphics g)
	{
		Image img = c.isEnabled() ? calendarImage : disabledCalendarImage;
		g.drawImage(img,0,0,c.getWidth(),c.getHeight(),c);
	}
	
	private XdevDateTextField	txt;
	
	
	public XdevDateDropDownButton(final XdevDateTextField txt)
	{
		super();
		
		this.txt = txt;
		
		addMouseListener(new MouseAdapter()
		{
			public void mousePressed(MouseEvent e)
			{
				if(txt.isEnabled() && txt.isEditable())
				{
					txt.setDatePopupVisible(!txt.isDatePopupVisible());
				}
			}
		});
	}
	
	
	@Override
	public Dimension getPreferredSize()
	{
		int s = txt.getPreferredSize().height;
		return new Dimension(s,s);
	}
	
	
	@Override
	public void paint(Graphics g)
	{
		paintCalendarDropDownButton(this,g);
	}
}
