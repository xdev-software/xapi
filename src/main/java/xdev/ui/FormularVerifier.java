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


import java.text.NumberFormat;

import xdev.util.ParseUtils;


/**
 * @see StringValidator
 * @see StringLengthValidator
 * @see NumberRangeValidator
 * @see EmailValidator
 * @see RequiredFieldValidator
 * 
 * @see FormularComponent#addValidator(Validator)
 * @see FormularComponent#validateState()
 * 
 * @deprecated use {@link Validator}s instead
 */
@Deprecated
public class FormularVerifier
{
	public static FormularVerifier createPlain(FormularComponent formularComponent,
			boolean required, String message)
	{
		return createPlain(formularComponent,required,message,null);
	}
	

	public static FormularVerifier createPlain(FormularComponent formularComponent,
			boolean required, String message, String title)
	{
		return new FormularVerifier(formularComponent,PLAIN,required,false,-1,false,-1,message,
				title);
	}
	

	public static FormularVerifier createNumeric(FormularComponent formularComponent,
			boolean required, boolean useMin, int min, boolean useMax, int max, String message)
	{
		return createNumeric(formularComponent,required,useMin,min,useMax,max,message,null);
	}
	

	public static FormularVerifier createNumeric(FormularComponent formularComponent,
			boolean required, boolean useMin, int min, boolean useMax, int max, String message,
			String title)
	{
		return new FormularVerifier(formularComponent,NUMERIC,required,useMin,min,useMax,max,
				message,title);
	}
	

	public static FormularVerifier createAlphaNumeric(FormularComponent formularComponent,
			boolean required, boolean useMin, int min, boolean useMax, int max, String message)
	{
		return createAlphaNumeric(formularComponent,required,useMin,min,useMax,max,message,null);
	}
	

	public static FormularVerifier createAlphaNumeric(FormularComponent formularComponent,
			boolean required, boolean useMin, int min, boolean useMax, int max, String message,
			String title)
	{
		return new FormularVerifier(formularComponent,ALPHANUMERIC,required,useMin,min,useMax,max,
				message,title);
	}
	

	public static FormularVerifier createEmail(FormularComponent formularComponent,
			boolean required, String message)
	{
		return createEmail(formularComponent,required,message,null);
	}
	

	public static FormularVerifier createEmail(FormularComponent formularComponent,
			boolean required, String message, String title)
	{
		return new FormularVerifier(formularComponent,EMAIL,required,false,-1,false,-1,message,
				title);
	}
	
	public final static int		PLAIN			= 0;
	public final static int		NUMERIC			= 1;
	public final static int		ALPHANUMERIC	= 2;
	public final static int		EMAIL			= 3;
	
	private FormularComponent	formularComponent;
	private int					type;
	private boolean				required;
	private boolean				useMin;
	private int					min;
	private boolean				useMax;
	private int					max;
	private String				message;
	private String				title;
	

	private FormularVerifier(FormularComponent formularComponent, int type, boolean required,
			boolean useMin, int min, boolean useMax, int max, String message, String title)
	{
		this.formularComponent = formularComponent;
		this.type = type;
		this.required = required;
		this.useMin = useMin;
		this.min = min;
		this.useMax = useMax;
		this.max = max;
		this.message = message;
		this.title = title;
	}
	

	public boolean verify()
	{
		Object value = formularComponent.getFormularValue();
		String str = value == null ? "" : value.toString();
		
		if(required && (value == null || str.length() == 0))
		{
			return false;
		}
		
		switch(type)
		{
			case NUMERIC:
			{
				double val = 0;
				
				try
				{
					val = Double.parseDouble(str);
				}
				catch(NumberFormatException e)
				{
					try
					{
						val = NumberFormat.getNumberInstance().parse(str).doubleValue();
					}
					catch(Exception e2)
					{
						return false;
					}
				}
				
				if(useMin && useMax)
				{
					if(val < min || val > max)
					{
						return false;
					}
				}
				else if(useMin)
				{
					if(val < min)
					{
						return false;
					}
				}
				else if(useMax)
				{
					if(val > max)
					{
						return false;
					}
				}
			}
			break;
			
			case ALPHANUMERIC:
			{
				int len = str.length();
				if(useMin && useMax)
				{
					if(len < min || len > max)
					{
						return false;
					}
				}
				else if(useMin)
				{
					if(len < min)
					{
						return false;
					}
				}
				else if(useMax)
				{
					if(len > max)
					{
						return false;
					}
				}
			}
			break;
			
			case EMAIL:
			{
				return ParseUtils.isEmail(str);
			}
		}
		
		return true;
	}
	

	public String getMessage()
	{
		return message;
	}
	

	public String getTitle()
	{
		return title != null && title.length() > 0 ? title : formularComponent.getFormularName();
	}
}
