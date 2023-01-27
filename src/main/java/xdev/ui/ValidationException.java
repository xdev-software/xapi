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


/**
 * Exception which is thrown when a validator discovers an error.
 * 
 * @author XDEV Sofware
 * @since 3.1
 */
public class ValidationException extends Exception
{
	private final Object	source;
	private Severity		severity;
	private String			title;
	

	/**
	 * Creates a new validation exception
	 */
	public ValidationException(Object source, Severity severity)
	{
		super();
		
		this.source = source;
		
		setSeverity(severity);
	}
	

	/**
	 * Creates a new validation exception
	 */
	public ValidationException(Object source, Severity severity, String message, Throwable cause)
	{
		super(message,cause);
		
		this.source = source;
		
		setSeverity(severity);
	}
	

	/**
	 * Creates a new validation exception
	 */
	public ValidationException(Object source, Severity severity, String message, String title,
			Throwable cause)
	{
		super(message,cause);
		
		this.source = source;
		
		setSeverity(severity);
		setTitle(title);
	}
	

	/**
	 * Creates a new validation exception
	 */
	public ValidationException(Object source, Severity severity, String message)
	{
		super(message);
		
		this.source = source;
		
		setSeverity(severity);
	}
	

	/**
	 * Creates a new validation exception
	 */
	public ValidationException(Object source, Severity severity, String message, String title)
	{
		super(message);
		
		this.source = source;
		
		setSeverity(severity);
		setTitle(title);
	}
	

	/**
	 * Creates a new validation exception
	 */
	public ValidationException(Object source, Severity severity, Throwable cause)
	{
		super(cause);
		
		this.source = source;
		
		setSeverity(severity);
	}
	

	/**
	 * Returns the source object of the exception, in most cases the validated
	 * component.
	 * 
	 * @return the source of the exception
	 */
	public Object getSource()
	{
		return source;
	}
	

	/**
	 * Sets the severity of this validation exception.
	 * 
	 * @param severity
	 *            the new severity
	 */
	public void setSeverity(Severity severity)
	{
		if(severity == null)
		{
			throw new IllegalArgumentException("severity cannot be null");
		}
		
		this.severity = severity;
	}
	

	/**
	 * Returns the severity of this validation exception.
	 * 
	 * @return the severity of this validation exception
	 */
	public Severity getSeverity()
	{
		return severity;
	}
	

	/**
	 * Sets the title going with the message of this exception.
	 * 
	 * @param title
	 *            the new title
	 */
	public void setTitle(String title)
	{
		this.title = title;
	}
	

	/**
	 * Returns the title of this exception.
	 * 
	 * @return the title of this exception
	 */
	public String getTitle()
	{
		return title;
	}
}
