package xdev.ui;

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


import xdev.vt.VirtualTable;


/**
 * Exception which is thrown if errors occur while handling master detail
 * relations.
 * 
 * @author XDEV Software
 */
public class MasterDetailException extends RuntimeException
{
	private final Object	master;
	private final Object	detail;
	
	
	/**
	 * Initializes a new {@link MasterDetailException}.
	 * 
	 * @param master
	 *            the affected master
	 * @param detail
	 *            the affected detail
	 */
	public MasterDetailException(Object master, Object detail)
	{
		super();
		
		this.master = master;
		this.detail = detail;
	}
	
	
	/**
	 * Initializes a new {@link MasterDetailException}.
	 * 
	 * @param master
	 *            the affected master
	 * @param detail
	 *            the affected detail
	 * @param message
	 *            the message of the exception.
	 * @param cause
	 *            the {@link Throwable} that caused this exception.
	 */
	public MasterDetailException(Object master, Object detail, String message, Throwable cause)
	{
		super(message,cause);
		
		this.master = master;
		this.detail = detail;
	}
	
	
	/**
	 * Initializes a new {@link MasterDetailException}.
	 * 
	 * @param master
	 *            the affected master
	 * @param detail
	 *            the affected detail
	 * @param message
	 *            the message of the exception.
	 */
	public MasterDetailException(Object master, Object detail, String message)
	{
		super(message);
		
		this.master = master;
		this.detail = detail;
	}
	
	
	/**
	 * Initializes a new {@link MasterDetailException}.
	 * 
	 * @param master
	 *            the affected master
	 * @param detail
	 *            the affected detail
	 * @param cause
	 *            the {@link Throwable} that caused this exception.
	 */
	public MasterDetailException(Object master, Object detail, Throwable cause)
	{
		super(cause);
		
		this.master = master;
		this.detail = detail;
	}
	
	
	/**
	 * Returns the affected master, e.g. a {@link MasterDetailComponent} or a
	 * {@link VirtualTable}.
	 * 
	 * @return the master object
	 */
	public Object getMaster()
	{
		return master;
	}
	
	
	/**
	 * Returns the affected detail, e.g. a {@link MasterDetailComponent}, a
	 * {@link VirtualTable} or a {@link Formular}.
	 * 
	 * @return the detail object
	 */
	public Object getDetail()
	{
		return detail;
	}
}
