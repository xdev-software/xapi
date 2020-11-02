package xdev.ui.event;

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


import java.util.EventObject;

import xdev.ui.Formular;
import xdev.ui.Formular.WorkingState;
import xdev.ui.FormularComponent;


/**
 * Event object which contains detail information of events of a
 * {@link FormularListener}.
 * 
 * @author XDEV Software
 * 
 */
public class FormularEvent extends EventObject
{
	private FormularComponent	formularComponent;
	private Object				formularComponentEventObject;
	private WorkingState		formularWorkingState;
	
	
	/**
	 * Creates a new event object.
	 * <p>
	 * Used for
	 * <ul>
	 * <li> {@link FormularListener#formularModelChanged(FormularEvent)}</li>
	 * <li>
	 * {@link FormularListener#formularSavePerformed(FormularEvent)}</li>
	 * </ul>
	 * 
	 * @param source
	 *            the event's source formular
	 */
	public FormularEvent(Formular source)
	{
		super(source);
	}
	
	
	/**
	 * Creates a new event object.
	 * <p>
	 * Used for
	 * <ul>
	 * <li>{@link FormularListener#formularComponentValueChanged(FormularEvent)}
	 * </li>
	 * </ul>
	 * 
	 * @param source
	 *            the event's source formular
	 * @param formularComponent
	 *            the changed {@link FormularComponent}
	 * @param formularComponentEventObject
	 *            the change's origin
	 * @param formularWorkingState
	 *            the current {@link WorkingState} of the underlying
	 *            <code>source</code> formular
	 * 
	 * @since 3.1
	 */
	public FormularEvent(Formular source, FormularComponent formularComponent,
			Object formularComponentEventObject, WorkingState formularWorkingState)
	{
		super(source);
		
		this.formularComponent = formularComponent;
		this.formularComponentEventObject = formularComponentEventObject;
		this.formularWorkingState = formularWorkingState;
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Formular getSource()
	{
		return (Formular)super.getSource();
	}
	
	
	/**
	 * Returns the changed formular component, may be <code>null</code>.
	 * <p>
	 * Only used in
	 * {@link FormularListener#formularComponentValueChanged(FormularEvent)}
	 * 
	 * @return the changed formular component
	 * 
	 * @since 3.1
	 */
	public FormularComponent getFormularComponent()
	{
		return formularComponent;
	}
	
	
	/**
	 * Returns the {@link FormularComponent}-change's source event, may be
	 * <code>null</code>.
	 * <p>
	 * Only used in
	 * {@link FormularListener#formularComponentValueChanged(FormularEvent)}
	 * 
	 * @return the {@link FormularComponent}-change's source event
	 * 
	 * @since 3.1
	 */
	public Object getFormularComponentEventObject()
	{
		return formularComponentEventObject;
	}
	
	
	/**
	 * Returns the formular's current {@link WorkingState}, may be
	 * <code>null</code>.
	 * <p>
	 * Only used in
	 * {@link FormularListener#formularComponentValueChanged(FormularEvent)}
	 * 
	 * @return the formular's current {@link WorkingState}
	 * 
	 * @since 3.1
	 */
	public WorkingState getFormularWorkingState()
	{
		return formularWorkingState;
	}
}
