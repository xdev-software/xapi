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
 * @since 3.2
 */
public class RequiredFieldValidator extends AbstractFormularComponentValidator<FormularComponent>
{
	private boolean	required;
	

	public RequiredFieldValidator()
	{
		this(null,null);
	}
	

	public RequiredFieldValidator(String message, String title)
	{
		super(message,title);
	}
	

	public boolean isRequired()
	{
		return required;
	}
	

	public void setRequired(boolean required)
	{
		this.required = required;
	}
	

	@Override
	public void validate(FormularComponent component) throws ValidationException
	{
		Object value = component.getFormularValue();
		if(value == null || value.toString().length() == 0)
		{
			throwValidationException(null,component);
		}
	}
}
