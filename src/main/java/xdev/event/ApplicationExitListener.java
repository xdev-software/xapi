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


import java.util.EventListener;

import xdev.Application;


/**
 * Listener interface to observe the exit of the XDEV Application Framwork.
 * 
 * @see Application#addExitListener(ApplicationExitListener)
 * @see Application#removeExitListener(ApplicationExitListener)
 * 
 * @author XDEV Software
 * @since 3.0
 */
public interface ApplicationExitListener extends EventListener
{
	/**
	 * Invoked before the application will exit.
	 * <p>
	 * Call {@link ApplicationExitEvent#vetoExit()} to veto the exit process.
	 * 
	 * @param event
	 */
	public void applicationWillExit(ApplicationExitEvent event);
	
	
	/**
	 * Invoked after {@link #applicationWillExit(ApplicationExitEvent)} returns
	 * with veto.
	 * 
	 * @param event
	 * 
	 * @since 4.0
	 */
	public void applicationExitCancelled(ApplicationExitEvent event);
	
	
	/**
	 * Invoked after {@link #applicationWillExit(ApplicationExitEvent)} returns
	 * with no veto.
	 * 
	 * @param event
	 * 
	 * @since 4.0
	 */
	public void applicationExitGranted(ApplicationExitEvent event);
	
	
	/**
	 * Invoked while exiting the application, at this point the job queue is
	 * shut down, so all threads except the main thread are not granted to be
	 * executed.
	 */
	public void applicationExits(ApplicationExitEvent event);
}
