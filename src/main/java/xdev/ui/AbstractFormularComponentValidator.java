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
 * @author XDEV Software
 * @since 3.1
 */
public abstract class AbstractFormularComponentValidator<T extends FormularComponent> implements
		Validator<T>
{
	private String	message;
	private String	title;
	

	public AbstractFormularComponentValidator(String message, String title)
	{
		this.message = message;
		this.title = title;
	}
	

	public String getMessage()
	{
		return message;
	}
	

	public void setMessage(String message)
	{
		this.message = message;
	}
	

	public String getTitle()
	{
		return title;
	}
	

	public void setTitle(String title)
	{
		this.title = title;
	}
	

	public void throwValidationException(Exception cause, T component) throws ValidationException
	{
		if(message != null)
		{
			String title = this.title;
			if(title == null || title.length() == 0)
			{
				title = component.getFormularName();
			}
			throw new ValidationException(component,Severity.ERROR,message,title,cause);
		}
		else
		{
			throw new ValidationException(component,Severity.ERROR,cause);
		}
	}
}
