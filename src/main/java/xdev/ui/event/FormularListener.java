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
package xdev.ui.event;


import java.util.EventListener;

import xdev.ui.Formular;
import xdev.ui.FormularComponent;


/**
 * An event listener to receive events from a {@link Formular}.
 * 
 * @see Formular
 * 
 * @author XDEV Software
 * 
 */
public interface FormularListener extends EventListener
{
	/**
	 * Invoked every time
	 * {@link Formular#setModel(xdev.vt.VirtualTable.VirtualTableRow)} or equivalent
	 * setModel-methods are performed.
	 * 
	 * @param event
	 *            the formular event
	 */
	public void formularModelChanged(FormularEvent event);
	
	
	/**
	 * Invoked when a value of an underlying {@link FormularComponent} has been
	 * changed.
	 * 
	 * @param event
	 *            the formular event
	 * 
	 * @see FormularComponent#addValueChangeListener(xdev.ui.FormularComponent.ValueChangeListener)
	 * @see FormularEvent#getFormularComponent()
	 * @see FormularEvent#getFormularComponentEventObject()
	 * 
	 * @since 3.1
	 */
	public void formularComponentValueChanged(FormularEvent event);
	
	
	/**
	 * Invoked every time {@link Formular#save()} or other writing methods performed
	 * successfully.
	 * 
	 * @param event
	 *            the formular event
	 */
	public void formularSavePerformed(FormularEvent event);
}
