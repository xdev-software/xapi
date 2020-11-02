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


import xdev.lang.Nullable;


/**
 * @author XDEV Software
 * @since 3.1
 */
public class NumberRangeValidator extends AbstractFormularComponentValidator<FormularComponent>
{
	private boolean	required;
	private Number	min;
	private Number	max;
	
	
	public NumberRangeValidator(boolean required, @Nullable Number min, @Nullable Number max)
	{
		this(required,min,max,null,null);
	}
	
	
	public NumberRangeValidator(boolean required, @Nullable Number min, @Nullable Number max,
			String message, String title)
	{
		super(message,title);
		
		this.required = required;
		this.min = min;
		this.max = max;
	}
	
	
	public boolean isRequired()
	{
		return required;
	}
	
	
	public void setRequired(boolean required)
	{
		this.required = required;
	}
	
	
	public Number getMin()
	{
		return min;
	}
	
	
	public void setMin(Number min)
	{
		this.min = min;
	}
	
	
	public Number getMax()
	{
		return max;
	}
	
	
	public void setMax(Number max)
	{
		this.max = max;
	}
	
	
	@Override
	public void validate(FormularComponent component) throws ValidationException
	{
		try
		{
			if(component instanceof NumberFormularComponent)
			{
				Number number = ((NumberFormularComponent)component).getNumber(null);
				if(number == null)
				{
					if(required)
					{
						throwValidationException(null,component);
					}
				}
				else if((min != null && number.doubleValue() < min.doubleValue())
						|| (max != null && number.doubleValue() > max.doubleValue()))
				{
					throwValidationException(null,component);
				}
			}
			else
			{
				Object value = component.getFormularValue();
				String text;
				if(value == null || (text = value.toString()).length() == 0)
				{
					if(required)
					{
						throwValidationException(null,component);
					}
				}
				else
				{
					Number number = Double.parseDouble(text);
					if((min != null && number.doubleValue() < min.doubleValue())
							|| (max != null && number.doubleValue() > max.doubleValue()))
					{
						throwValidationException(null,component);
					}
				}
			}
		}
		catch(NumberFormatException e)
		{
			throwValidationException(e,component);
		}
	}
}
