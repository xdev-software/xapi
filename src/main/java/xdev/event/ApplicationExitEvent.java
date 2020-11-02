package xdev.event;

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

import xdev.Application;


/**
 * An event which indicates that the XDEV Application Framework is about to
 * exit.
 * <p>
 * An {@link ApplicationExitListener} is able to veto the exit process by
 * invoking {@link #vetoExit()}.
 * 
 * @see Application#addExitListener(ApplicationExitListener)
 * @see Application#exit(Object)
 * 
 * @author XDEV Software
 * @since 3.0
 */
public class ApplicationExitEvent extends EventObject
{
	private boolean	veto	= false;


	/**
	 * Creates a new {@link ApplicationExitEvent}.
	 * 
	 * @param source
	 *            The initiator of the exit process
	 */
	public ApplicationExitEvent(Object source)
	{
		super(source);
	}


	/**
	 * Vetos the {@link Application}'s exit process.
	 * 
	 * @see Application#exit(Object)
	 */
	public void vetoExit()
	{
		veto = true;
	}


	/**
	 * Returns true if this event's {@link #vetoExit()} has been called.
	 * 
	 * @return <code>true</code> if this event vetos the exit process,
	 *         <code>false</code> otherwise
	 */
	public boolean hasVeto()
	{
		return veto;
	}
}
