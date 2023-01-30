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


import java.awt.Image;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.UIManager;


/**
 * Severity indicators used by validators.
 * 
 * @author XDEV Software
 * @since 3.1
 */
public enum Severity
{
	/**
	 * Lowest severity, information only
	 */
	INFORMATION,
	
	/**
	 * Middle severity, indication a warning
	 */
	WARNING,
	
	/**
	 * Most severe, indication an error
	 */
	ERROR;
	
	private final Icon	icon;
	private final Icon	indicatorIcon;
	
	
	private Severity()
	{
		icon = UIManager.getIcon("OptionPane." + name().toLowerCase() + "Icon");
		indicatorIcon = new ImageIcon(GraphicUtils.createImageFromIcon(icon).getScaledInstance(8,8,
				Image.SCALE_SMOOTH));
	}
	
	
	/**
	 * @return the associated icon of this severity
	 * 
	 * @since 4.0
	 */
	public Icon getIcon()
	{
		return icon;
	}
	
	
	/**
	 * @return a smaller version of the associated icon of this severity
	 * 
	 * @since 4.0
	 */
	public Icon getIndicatorIcon()
	{
		return indicatorIcon;
	}
}
