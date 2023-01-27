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


import java.awt.Container;
import java.awt.event.HierarchyEvent;
import java.awt.event.HierarchyListener;
import java.beans.Beans;
import java.util.Map;
import java.util.StringTokenizer;

import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JCheckBox;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import xdev.db.DataType;
import xdev.db.Operator;
import xdev.vt.VirtualTable;


/**
 * The check box in XDEV. Based on {@link JCheckBox}.
 * 
 * @see JCheckBox
 * 
 * @author XDEV Software
 * 
 * @since 2.0
 */
@BeanSettings(useXdevCustomizer = true)
public class XdevCheckBox extends JCheckBox implements BooleanFormularComponent<XdevCheckBox>,
		XdevFocusCycleComponent
{
	private boolean											savedValue			= false;
	private String											formularValueList	= "";
	private String											trueReturnValue		= "";
	private String											falseReturnValue	= "";
	/**
	 * tabIndex is used to store the index for {@link XdevFocusCycleComponent}
	 * functionality.
	 */
	private int												tabIndex			= -1;
	
	private final FormularComponentSupport<XdevCheckBox>	support				= new FormularComponentSupport(
																						this);
	
	
	public XdevCheckBox()
	{
		super();
	}
	
	
	public XdevCheckBox(Action a)
	{
		super(a);
	}
	
	
	public XdevCheckBox(Icon icon, boolean selected)
	{
		super(icon,selected);
	}
	
	
	public XdevCheckBox(Icon icon)
	{
		super(icon);
	}
	
	
	public XdevCheckBox(String text, boolean selected)
	{
		super(text,selected);
	}
	
	
	public XdevCheckBox(String text, Icon icon, boolean selected)
	{
		super(text,icon,selected);
	}
	
	
	public XdevCheckBox(String text, Icon icon)
	{
		super(text,icon);
	}
	
	
	public XdevCheckBox(String text)
	{
		super(text);
	}
	
	/*
	 * Workaround for #13522
	 */
	{
		if(!Beans.isDesignTime())
		{
			addHierarchyListener(new HierarchyListener()
			{
				@Override
				public void hierarchyChanged(HierarchyEvent e)
				{
					Container parent = getParent();
					if(parent != null)
					{
						removeHierarchyListener(this);
						if(parent.getLayout() == null)
						{
							setSize(getPreferredSize());
						}
					}
				}
			});
		}
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	public String getFormularName()
	{
		return support.getFormularName();
	}
	
	
	/**
	 * {@inheritDoc}
	 * 
	 * @since 3.1
	 */
	@Override
	public void setDataField(String dataField)
	{
		support.setDataField(dataField);
	}
	
	
	/**
	 * {@inheritDoc}
	 * 
	 * @since 3.1
	 */
	@Override
	public String getDataField()
	{
		return support.getDataField();
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	@Deprecated
	public final void setFormularValue(VirtualTable vt, int col, Object value)
	{
		support.setFormularValue(vt,col,value);
	}
	
	
	/**
	 * {@inheritDoc}
	 * 
	 * @since 3.2
	 */
	@Override
	public void setFormularValue(VirtualTable vt, Map<String, Object> record)
	{
		if(!support.hasDataField())
		{
			return;
		}
		
		boolean b = false;
		
		Object value = support.getSingleValue(vt,record);
		if(value != null)
		{
			if(formularValueList != null && formularValueList.length() > 0)
			{
				b = contains(formularValueList,",",value);
			}
			else
			{
				String returnValue = isSelected() ? trueReturnValue : falseReturnValue;
				if(returnValue != null && returnValue.length() > 0)
				{
					b = VirtualTable.equals(returnValue,value,DataType.NUMERIC);
				}
				else
				{
					b = VirtualTable.equals(Boolean.TRUE,value,DataType.BOOLEAN);
				}
			}
		}
		
		setSelected(b);
	}
	
	
	private boolean contains(String list, String separator, Object value)
	{
		StringTokenizer st = new StringTokenizer(list,separator);
		while(st.hasMoreTokens())
		{
			if(VirtualTable.equals(st.nextToken(),value,DataType.NUMERIC))
			{
				return true;
			}
		}
		
		return false;
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	public Object getFormularValue()
	{
		if(isSelected())
		{
			if(trueReturnValue != null && trueReturnValue.length() > 0)
			{
				return trueReturnValue;
			}
			else
			{
				return Boolean.TRUE;
			}
		}
		else
		{
			if(falseReturnValue != null && falseReturnValue.length() > 0)
			{
				return falseReturnValue;
			}
			else
			{
				return Boolean.FALSE;
			}
		}
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	public void saveState()
	{
		savedValue = isSelected();
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	public void restoreState()
	{
		setSelected(savedValue);
	}
	
	
	/**
	 * {@inheritDoc}
	 * 
	 * @since 3.1
	 */
	@Override
	public boolean hasStateChanged()
	{
		return savedValue != isSelected();
	}
	
	
	/**
	 * {@inheritDoc}
	 * 
	 * @since 3.1
	 */
	@Override
	public void addValueChangeListener(final ValueChangeListener l)
	{
		addChangeListener(new ChangeListener()
		{
			@Override
			public void stateChanged(ChangeEvent e)
			{
				l.valueChanged(e);
			}
		});
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	public boolean isMultiSelect()
	{
		return false;
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	public boolean verify()
	{
		return support.verify();
	}
	
	
	/**
	 * {@inheritDoc}
	 * 
	 * @since 3.1
	 */
	@Override
	public void addValidator(Validator validator)
	{
		support.addValidator(validator);
	}
	
	
	/**
	 * {@inheritDoc}
	 * 
	 * @since 3.1
	 */
	@Override
	public void removeValidator(Validator validator)
	{
		support.removeValidator(validator);
	}
	
	
	/**
	 * {@inheritDoc}
	 * 
	 * @since 3.1
	 */
	@Override
	public Validator[] getValidators()
	{
		return support.getValidators();
	}
	
	
	/**
	 * {@inheritDoc}
	 * 
	 * @since 3.1
	 */
	@Override
	public void validateState() throws ValidationException
	{
		support.validateState();
	}
	
	
	/**
	 * {@inheritDoc}
	 * 
	 * @since 3.1
	 */
	@Override
	public void validateState(Validation validation) throws ValidationException
	{
		support.validateState(validation);
	}
	
	
	/**
	 * {@inheritDoc}
	 * 
	 * @since 3.1
	 */
	@Override
	public void setFilterOperator(Operator filterOperator)
	{
		support.setFilterOperator(filterOperator);
	}
	
	
	/**
	 * {@inheritDoc}
	 * 
	 * @since 3.1
	 */
	@Override
	public Operator getFilterOperator()
	{
		return support.getFilterOperator();
	}
	
	
	/**
	 * {@inheritDoc}
	 * 
	 * @since 3.2
	 */
	@Override
	public void setReadOnly(boolean readOnly)
	{
		support.setReadOnly(readOnly);
	}
	
	
	/**
	 * {@inheritDoc}
	 * 
	 * @since 3.2
	 */
	@Override
	public boolean isReadOnly()
	{
		return support.isReadOnly();
	}
	
	
	@BeanProperty(category = DefaultBeanCategories.DATA)
	public void setFormularValueList(String formularValueList)
	{
		this.formularValueList = formularValueList;
	}
	
	
	public String getFormularValueList()
	{
		return formularValueList;
	}
	
	
	public void setReturnValue(boolean selected, String value)
	{
		if(selected)
		{
			trueReturnValue = value;
		}
		else
		{
			falseReturnValue = value;
		}
	}
	
	
	@BeanProperty(category = DefaultBeanCategories.DATA)
	public void setTrueReturnValue(String trueReturnValue)
	{
		this.trueReturnValue = trueReturnValue;
	}
	
	
	@BeanProperty(category = DefaultBeanCategories.DATA)
	public void setFalseReturnValue(String falseReturnValue)
	{
		this.falseReturnValue = falseReturnValue;
	}
	
	
	public String getReturnValue(boolean selected)
	{
		return selected ? trueReturnValue : falseReturnValue;
	}
	
	
	public String getTrueReturnValue()
	{
		return trueReturnValue;
	}
	
	
	public String getFalseReturnValue()
	{
		return falseReturnValue;
	}
	
	
	/**
	 * {@inheritDoc}
	 * 
	 * @since 3.1
	 */
	@Override
	public boolean getBoolean()
	{
		return isSelected();
	}
	
	
	/**
	 * {@inheritDoc}
	 * 
	 * @since 3.1
	 */
	@Override
	public void setBoolean(boolean value)
	{
		setSelected(value);
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString()
	{
		String toString = UIUtils.toString(this);
		if(toString != null)
		{
			return toString;
		}
		
		return super.toString();
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getTabIndex()
	{
		return tabIndex;
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setTabIndex(int tabIndex)
	{
		if(this.tabIndex != tabIndex)
		{
			int oldValue = this.tabIndex;
			this.tabIndex = tabIndex;
			firePropertyChange(TAB_INDEX_PROPERTY,oldValue,tabIndex);
		}
	}
}
