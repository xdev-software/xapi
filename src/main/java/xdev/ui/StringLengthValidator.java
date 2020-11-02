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
public class StringLengthValidator extends StringValidator
{
	private Integer	minLength;
	private Integer	maxLength;
	
	
	public StringLengthValidator(boolean required, @Nullable Integer minLength,
			@Nullable Integer maxLength)
	{
		this(required,minLength,maxLength,null,null);
	}
	
	
	public StringLengthValidator(boolean required, @Nullable Integer minLength,
			@Nullable Integer maxLength, String message, String title)
	{
		super(required,message,title);
		
		this.minLength = minLength;
		this.maxLength = maxLength;
	}
	
	
	public Integer getMinLength()
	{
		return minLength;
	}
	
	
	public void setMinLength(Integer minLength)
	{
		this.minLength = minLength;
	}
	
	
	public Integer getMaxLength()
	{
		return maxLength;
	}
	
	
	public void setMaxLength(Integer maxLength)
	{
		this.maxLength = maxLength;
	}
	
	
	@Override
	protected void validate(FormularComponent component, String value) throws ValidationException
	{
		int length = value.length();
		if((minLength != null && length < minLength) || (maxLength != null && length > maxLength))
		{
			throwValidationException(null,component);
		}
	}
}
