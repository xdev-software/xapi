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
package com.xdev.jadoth.lang.exceptions;


/**
 * The Class NoArrayRuntimeException.
 * 
 * @author Thomas Muenz
 */
public class NotAnArrayException extends ClassCastException 
{

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1172749786323032546L;

	/** The wrong class. */
	private final Class<?> wrongClass;
	
	private final Throwable cause;
	
	/**
	 * Instantiates a new no array runtime exception.
	 */
	public NotAnArrayException() 
	{
		super();
		this.wrongClass = null;
		this.cause = null;
	}

	/**
	 * Instantiates a new no array runtime exception.
	 * 
	 * @param message the message
	 * @param cause the cause
	 */
	public NotAnArrayException(String message, Throwable cause) 
	{
		super(message);
		this.wrongClass = null;
		this.cause = cause;
	}

	/**
	 * Instantiates a new no array runtime exception.
	 * 
	 * @param message the message
	 */
	public NotAnArrayException(String message) {
		super(message);
		this.wrongClass = null;
		this.cause = null;
	}

	/**
	 * Instantiates a new no array runtime exception.
	 * 
	 * @param cause the cause
	 */
	public NotAnArrayException(Throwable cause) {
		super();
		this.wrongClass = null;
		this.cause = cause;
	}
	
	
	/**
	 * Instantiates a new no array runtime exception.
	 * 
	 * @param wrongClass the wrong class
	 */
	public NotAnArrayException(Class<?> wrongClass) {
		super();
		this.wrongClass = wrongClass;
		this.cause = null;
	}

	/**
	 * Instantiates a new no array runtime exception.
	 * 
	 * @param wrongClass the wrong class
	 * @param cause the cause
	 */
	public NotAnArrayException(Class<?> wrongClass, Throwable cause) {
		super();
		this.wrongClass = wrongClass;
		this.cause = cause;
	}
	
	/**
	 * Instantiates a new no array runtime exception.
	 * 
	 * @param wrongClass the wrong class
	 * @param message the message
	 */
	public NotAnArrayException(Class<?> wrongClass, String message) {
		super(message);
		this.wrongClass = wrongClass;
		this.cause = null;
	}
	
	/**
	 * Instantiates a new no array runtime exception.
	 * 
	 * @param wrongClass the wrong class
	 * @param message the message
	 * @param cause the cause
	 */
	public NotAnArrayException(Class<?> wrongClass, String message, Throwable cause) {
		super(message);
		this.wrongClass = wrongClass;
		this.cause = cause;
	}
	
	/**
	 * Gets the wrong class.
	 * 
	 * @return the wrong class
	 */
	public Class<?> getWrongClass() {
		return wrongClass;
	}
	
	
	/**
	 * @return
	 */
	@Override
	public Throwable getCause()
	{
		return this.cause;
	}
	

	/**
	 * @return
	 * @see java.lang.Throwable#getMessage()
	 */
	@Override
	public String getMessage() {
		return "Wrong Class: "+wrongClass.getName();
	}

	
	
	
}
