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


import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.UIManager;


public class OKCancelPanel extends JPanel
{
	public JButton	cmdOK, cmdCancel;
	

	public OKCancelPanel(ActionListener al)
	{
		super(new BorderLayout());
		
		cmdOK = new JButton(UIManager.getString("OptionPane.okButtonText"));
		cmdOK.addActionListener(al);
		
		cmdCancel = new JButton(UIManager.getString("OptionPane.cancelButtonText"));
		cmdCancel.addActionListener(al);
		
		JPanel pnlCmd = new JPanel(new GridLayout(1,2,10,0));
		pnlCmd.add(cmdOK);
		pnlCmd.add(cmdCancel);
		
		add(new JPanel(),BorderLayout.CENTER);
		add(pnlCmd,BorderLayout.EAST);
	}
}
