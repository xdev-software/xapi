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


import java.util.ArrayList;
import java.util.List;


/**
 * Object used to control a validation process and to record the validation
 * exceptions.
 * 
 * @author XDEV Software
 * @since 3.1
 */
public class Validation
{
	private List<ValidationException>	exceptions	= new ArrayList();
	
	
	/**
	 * Stores a validation exception.
	 * 
	 * @param e
	 *            the occured exception
	 */
	public void record(ValidationException e)
	{
		exceptions.add(e);
	}
	
	
	/**
	 * Returns <code>true</code> if the validation should be continued after a
	 * exception has occured, <code>false</code> otherwise.
	 * 
	 * @param e
	 *            the occured exception
	 */
	public boolean continueValidation(ValidationException e)
	{
		return true;
	}
	
	
	/**
	 * Returns all recorded exceptions of this validation process.
	 * 
	 * @return all recorded exceptions
	 */
	public ValidationException[] getExceptions()
	{
		return exceptions.toArray(new ValidationException[exceptions.size()]);
	}
	
	
	/**
	 * Returns all {@link ValidationException}s of the validated source object.
	 * 
	 * @param source
	 *            the validated object
	 * @return all {@link ValidationException}s resulted from source, or an
	 *         empty array if no exceptions exist
	 * @since 4.0
	 */
	public ValidationException[] getExceptions(Object source)
	{
		List<ValidationException> list = new ArrayList();
		for(ValidationException e : exceptions)
		{
			if(e.getSource() == source)
			{
				list.add(e);
			}
		}
		return list.toArray(new ValidationException[list.size()]);
	}
	
	
	/**
	 * Returns all exceptions with a specific severity of this validation
	 * process.
	 * 
	 * @param severity
	 * @return all exceptions with a specific severity
	 */
	public ValidationException[] getExceptionsOf(Severity severity)
	{
		List<ValidationException> list = new ArrayList();
		for(ValidationException e : exceptions)
		{
			if(e.getSeverity() == severity)
			{
				list.add(e);
			}
		}
		return list.toArray(new ValidationException[list.size()]);
	}
	
	
	/**
	 * Returns all exceptions of the source object with a specific severity of
	 * this validation process.
	 * 
	 * @param source
	 *            the validated object
	 * @param severity
	 * @return all exceptions with a specific severity
	 * @since 4.0
	 */
	public ValidationException[] getExceptionsOf(Severity severity, Object source)
	{
		List<ValidationException> list = new ArrayList();
		for(ValidationException e : exceptions)
		{
			if(e.getSeverity() == severity)
			{
				list.add(e);
			}
		}
		return list.toArray(new ValidationException[list.size()]);
	}
	
	
	/**
	 * @deprecated typo, use {@link #getExceptionsFrom(Severity)}
	 */
	@Deprecated
	public ValidationException[] getExceptiosFrom(Severity minSeverity)
	{
		return getExceptionsFrom(minSeverity);
	}
	
	
	/**
	 * Returns all exceptions with a minimum severity of this validation
	 * process.
	 * 
	 * @param minSeverity
	 * @return all exceptions with a minimum severity
	 */
	public ValidationException[] getExceptionsFrom(Severity minSeverity)
	{
		List<ValidationException> list = new ArrayList();
		for(ValidationException e : exceptions)
		{
			if(e.getSeverity().ordinal() >= minSeverity.ordinal())
			{
				list.add(e);
			}
		}
		return list.toArray(new ValidationException[list.size()]);
	}
	
	
	/**
	 * Returns all exceptions of the source object with a minimum severity of
	 * this validation process.
	 * 
	 * @param source
	 *            the validated object
	 * @param minSeverity
	 * @return all exceptions with a minimum severity
	 * @since 4.0
	 */
	public ValidationException[] getExceptionsFrom(Severity minSeverity, Object source)
	{
		List<ValidationException> list = new ArrayList();
		for(ValidationException e : getExceptions(source))
		{
			if(e.getSeverity().ordinal() >= minSeverity.ordinal())
			{
				list.add(e);
			}
		}
		return list.toArray(new ValidationException[list.size()]);
	}
	
	
	/**
	 * Returns <code>true</code> if at least one of the recorded exceptions is
	 * an error.
	 * 
	 * @see ValidationException#getSeverity()
	 */
	public boolean hasError()
	{
		for(ValidationException e : exceptions)
		{
			if(e.getSeverity() == Severity.ERROR)
			{
				return true;
			}
		}
		
		return false;
	}
	
	
	/**
	 * Returns <code>true</code> if at least one of the recorded exceptions of
	 * the validated source object is an error.
	 * 
	 * @param source
	 *            the validated object
	 * 
	 * @see ValidationException#getSeverity()
	 * 
	 * @since 4.0
	 */
	public boolean hasError(Object source)
	{
		for(ValidationException e : getExceptions(source))
		{
			if(e.getSeverity() == Severity.ERROR)
			{
				return true;
			}
		}
		
		return false;
	}
}
