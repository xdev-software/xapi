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


import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JComponent;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.Document;
import javax.swing.text.JTextComponent;

import xdev.ui.FormularComponent.ValueChangeListener;
import xdev.ui.event.TextChangeAdapter;


/**
 * 
 * @author XDEV Software
 * 
 * @since 3.1
 */
public class TextFormularComponentSupport<C extends JComponent & FormularComponent<C>> extends
		FormularComponentSupport<C>
{
	public TextFormularComponentSupport(C component)
	{
		super(component);
	}
	
	
	public void addValueChangeListener(final ValueChangeListener l, JTextComponent txtCpn)
	{
		final DocumentListener docListener = new TextChangeAdapter()
		{
			@Override
			public void textChanged(DocumentEvent e)
			{
				l.valueChanged(e);
			}
		};
		
		Document document = txtCpn.getDocument();
		if(document != null)
		{
			document.addDocumentListener(docListener);
		}
		txtCpn.addPropertyChangeListener("document",new PropertyChangeListener()
		{
			@Override
			public void propertyChange(PropertyChangeEvent evt)
			{
				Object o = evt.getNewValue();
				if(o instanceof Document)
				{
					((Document)o).addDocumentListener(docListener);
				}
			}
		});
	}
}
