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


import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.JComponent;

import xdev.db.Operator;
import xdev.util.StringUtils;
import xdev.vt.VirtualTable;


public class FormularComponentSupport<C extends JComponent & FormularComponent<C>>
{
	protected C					component;
	protected String			dataField;
	protected Operator			filterOperator;
	protected Set<Validator>	validators	= new LinkedHashSet();
	protected boolean			readOnly	= false;
	
	
	public FormularComponentSupport(C component)
	{
		this.component = component;
	}
	
	
	public String getFormularName()
	{
		return component.getName();
	}
	
	
	/**
	 * @since 3.1
	 */
	public void setDataField(String dataField)
	{
		this.dataField = dataField;
	}
	
	
	/**
	 * @since 3.1
	 */
	@SuppressWarnings("deprecation")
	public String getDataField()
	{
		if(dataField != null)
		{
			return dataField;
		}
		
		// stay compatible
		Object o = component.getClientProperty(ClientProperties.DATA_FIELD);
		if(o != null)
		{
			return o.toString();
		}
		
		return null;
	}
	
	
	/**
	 * @since 3.1
	 */
	public void setFilterOperator(Operator filterOperator)
	{
		this.filterOperator = filterOperator;
	}
	
	
	/**
	 * @since 3.1
	 */
	@SuppressWarnings("deprecation")
	public Operator getFilterOperator()
	{
		if(filterOperator != null)
		{
			return filterOperator;
		}
		
		// stay compatible
		Object o = component.getClientProperty(ClientProperties.FORMULAR_FILTER_OPERATOR);
		if(o != null)
		{
			if(o instanceof Operator)
			{
				return (Operator)o;
			}
			else
			{
				try
				{
					return Operator.valueOf(o.toString());
				}
				catch(IllegalArgumentException e)
				{
				}
			}
		}
		
		return component.isMultiSelect() ? Operator.IN : Operator.EQUAL;
	}
	
	
	public boolean verify()
	{
		try
		{
			validateState();
			
			return true;
		}
		catch(ValidationException e)
		{
			return false;
		}
	}
	
	
	/**
	 * @since 3.1
	 */
	public void addValidator(Validator validator)
	{
		validators.add(validator);
	}
	
	
	/**
	 * @since 3.1
	 */
	public void removeValidator(Validator validator)
	{
		validators.remove(validator);
	}
	
	
	/**
	 * @since 3.1
	 */
	@SuppressWarnings("deprecation")
	public Validator[] getValidators()
	{
		if(validators.size() > 0)
		{
			return validators.toArray(new Validator[validators.size()]);
		}
		
		// stay compatible
		Object o = component.getClientProperty(ClientProperties.FORMULAR_VERIFIER);
		if(o != null && o instanceof FormularVerifier)
		{
			final FormularVerifier verifier = (FormularVerifier)o;
			return new Validator[]{new Validator<FormularComponent>()
			{
				@Override
				public void validate(FormularComponent component) throws ValidationException
				{
					if(!verifier.verify())
					{
						throw new ValidationException(component,Severity.ERROR,
								verifier.getMessage(),verifier.getTitle());
					}
				}
			}};
		}
		
		return new Validator[0];
	}
	
	
	/**
	 * @since 3.1
	 */
	public void validateState() throws ValidationException
	{
		for(Validator validator : getValidators())
		{
			validator.validate(component);
		}
	}
	
	
	/**
	 * @since 3.1
	 */
	public void validateState(Validation validation) throws ValidationException
	{
		for(Validator validator : getValidators())
		{
			try
			{
				validator.validate(component);
			}
			catch(ValidationException e)
			{
				validation.record(e);
				if(!validation.continueValidation(e))
				{
					throw e;
				}
			}
		}
	}
	
	
	/**
	 * @since 3.2
	 */
	public void setReadOnly(boolean readOnly)
	{
		this.readOnly = readOnly;
	}
	
	
	/**
	 * @since 3.2
	 */
	public boolean isReadOnly()
	{
		return readOnly;
	}
	
	
	public boolean hasDataField()
	{
		String dataField = component.getDataField();
		return dataField != null && dataField.length() > 0;
	}
	
	
	/**
	 * Forwards the deprecated
	 * {@link FormularComponent#setFormularValue(VirtualTable, int, Object)} to
	 * {@link FormularComponent#setFormularValue(VirtualTable, Map)}.
	 * 
	 * @since 3.2
	 */
	public final void setFormularValue(VirtualTable vt, int col, Object value)
	{
		if(!hasDataField())
		{
			return;
		}
		
		Map<String, Object> map = new HashMap();
		map.put(vt.getColumnName(col),value);
		component.setFormularValue(vt,map);
	}
	
	
	public String getFormattedFormularValue(VirtualTable vt, Map<String, Object> record)
	{
		String dataField = getDataFields(component.getDataField())[0];
		String columnName = getColumnName(dataField);
		Object value = record.get(columnName);
		if(vt != null)
		{
			return vt.formatValue(value,columnName);
		}
		else
		{
			return value != null ? value.toString() : "";
		}
	}
	
	
	public Object getSingleValue(VirtualTable vt, Map<String, Object> record)
	{
		String dataField = component.getDataField();
		dataField = getFirstDataField(dataField);
		dataField = getColumnName(dataField);
		return record.get(dataField);
	}
	
	
	public String getFirstDataField(String dataField)
	{
		if(dataField.indexOf(FormularComponent.DATA_FIELD_SEPARATOR) != -1)
		{
			List<String> list = StringUtils.explode(dataField,
					FormularComponent.DATA_FIELD_SEPARATOR);
			return list.get(0).trim();
		}
		return dataField;
	}
	
	
	public static String[] getDataFields(String dataFieldStr)
	{
		if(dataFieldStr.indexOf(FormularComponent.DATA_FIELD_SEPARATOR) != -1)
		{
			List<String> list = StringUtils.explode(dataFieldStr,
					FormularComponent.DATA_FIELD_SEPARATOR);
			String[] array = list.toArray(new String[list.size()]);
			for(int i = 0; i < array.length; i++)
			{
				array[i] = array[i].trim();
			}
			return array;
		}
		return new String[]{dataFieldStr.trim()};
	}
	
	
	public String getColumnName(String singleDataField)
	{
		int dot = singleDataField.lastIndexOf('.');
		if(dot != -1)
		{
			return singleDataField.substring(dot + 1);
		}
		return singleDataField;
	}
	
	
	public LinkedHashMap<String, Object> getValuesForDataFields(Map<String, Object> record)
	{
		// use LinkedHashMap because the order is important
		LinkedHashMap<String, Object> value = new LinkedHashMap();
		String[] dataFields = getDataFields(component.getDataField());
		for(String dataField : dataFields)
		{
			dataField = getColumnName(dataField);
			if(record.containsKey(dataField))
			{
				value.put(dataField,record.get(dataField));
			}
		}
		return value;
	}
}
