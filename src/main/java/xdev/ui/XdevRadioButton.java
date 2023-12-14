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
import java.util.Enumeration;
import java.util.Map;
import java.util.StringTokenizer;

import javax.swing.Action;
import javax.swing.ButtonGroup;
import javax.swing.ButtonModel;
import javax.swing.DefaultButtonModel;
import javax.swing.Icon;
import javax.swing.JRadioButton;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import xdev.db.Operator;
import xdev.util.ObjectUtils;
import xdev.vt.VirtualTable;


/**
 * The radio button in XDEV. Based on {@link JRadioButton}.
 * 
 * @see JRadioButton
 * 
 * @author XDEV Software
 * 
 * @since 2.0
 */
@BeanSettings(useXdevCustomizer = true)
public class XdevRadioButton extends JRadioButton implements FormularComponent<XdevRadioButton>,
		XdevFocusCycleComponent
{
	private String											dataField;
	private boolean											savedValue			= false;
	private String											returnValue			= "";
	private String											formularValueList	= "";
	/**
	 * tabIndex is used to store the index for {@link XdevFocusCycleComponent}
	 * functionality.
	 */
	private int												tabIndex			= -1;
	
	private final FormularComponentSupport<XdevRadioButton>	support				= new FormularComponentSupport(
																						this);
	
	
	public XdevRadioButton()
	{
		super();
	}
	
	
	public XdevRadioButton(Action a)
	{
		super(a);
	}
	
	
	public XdevRadioButton(Icon icon, boolean selected)
	{
		super(icon,selected);
	}
	
	
	public XdevRadioButton(Icon icon)
	{
		super(icon);
	}
	
	
	public XdevRadioButton(String text, boolean selected)
	{
		super(text,selected);
	}
	
	
	public XdevRadioButton(String text, Icon icon, boolean selected)
	{
		super(text,icon,selected);
	}
	
	
	public XdevRadioButton(String text, Icon icon)
	{
		super(text,icon);
	}
	
	
	public XdevRadioButton(String text)
	{
		super(text);
	}
	
	/*
	 * Workaround for #13521
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
	 * @deprecated use {@link #setDataField(String)}
	 */
	@Deprecated
	public void setRadioGroup(String radioGroup)
	{
		setDataField(radioGroup);
	}
	
	
	/**
	 * @deprecated user {@link #getDataField()}
	 */
	@Deprecated
	public String getRadioGroup()
	{
		return getDataField();
	}
	
	
	/**
	 * Returns the group that the button belongs to.
	 * 
	 * @return the group that the button belongs to
	 */
	public ButtonGroup getButtonGroup()
	{
		ButtonModel model = getModel();
		if(model instanceof DefaultButtonModel)
		{
			return ((DefaultButtonModel)model).getGroup();
		}
		
		return null;
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	public String getFormularName()
	{
		return dataField;
	}
	
	
	/**
	 * {@inheritDoc}
	 * 
	 * @since 3.1
	 */
	@Override
	public void setDataField(String dataField)
	{
		this.dataField = dataField;
	}
	
	
	/**
	 * {@inheritDoc}
	 * 
	 * @since 3.1
	 */
	@Override
	public String getDataField()
	{
		return dataField;
	}
	
	
	/**
	 * 
	 * @param formularValueList
	 *            comma separated values e.g. "true,1,yes"
	 */
	@BeanProperty(category = DefaultBeanCategories.DATA)
	public void setFormularValueList(String formularValueList)
	{
		this.formularValueList = formularValueList;
	}
	
	
	public String getFormularValueList()
	{
		return formularValueList;
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
			String str = value.toString();
			
			if(formularValueList != null && formularValueList.length() > 0)
			{
				b = contains(formularValueList,",",str);
			}
			else
			{
				if(returnValue != null && returnValue.length() > 0)
				{
					b = returnValue.equalsIgnoreCase(str);
				}
				else
				{
					b = Boolean.valueOf(str);
				}
			}
		}
		
		setSelected(b);
	}
	
	
	private boolean contains(String list, String separator, String val)
	{
		StringTokenizer st = new StringTokenizer(list,separator);
		while(st.hasMoreTokens())
		{
			if(st.nextToken().equalsIgnoreCase(val))
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
		return getSelectedRadioButtonValue();
	}
	
	
	private Object getFormularValue0()
	{
		return returnValue;
	}
	
	
	public String getReturnValue()
	{
		return returnValue;
	}
	
	
	@BeanProperty(category = DefaultBeanCategories.DATA)
	public void setReturnValue(String returnValue)
	{
		this.returnValue = returnValue;
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
		return !ObjectUtils.equals(savedValue,getFormularValue());
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
	
	
	public void selectRadioButtonInGroup(Object value)
	{
		ButtonGroup buttonGroup = getButtonGroup();
		if(buttonGroup != null)
		{
			Enumeration e = buttonGroup.getElements();
			while(e.hasMoreElements())
			{
				Object o = e.nextElement();
				if(o instanceof XdevRadioButton)
				{
					XdevRadioButton radio = (XdevRadioButton)o;
					if(value.equals(radio.getFormularValue0()))
					{
						radio.setSelected(true);
						break;
					}
				}
			}
		}
	}
	
	
	public XdevRadioButton getSelectedRadioButtonInGroup()
	{
		ButtonGroup buttonGroup = getButtonGroup();
		if(buttonGroup != null)
		{
			Enumeration e = buttonGroup.getElements();
			while(e.hasMoreElements())
			{
				Object o = e.nextElement();
				if(o instanceof XdevRadioButton)
				{
					XdevRadioButton button = (XdevRadioButton)o;
					if(button.isSelected())
					{
						return button;
					}
				}
			}
		}
		
		return null;
	}
	
	
	public Object getSelectedRadioButtonValue()
	{
		XdevRadioButton button = getSelectedRadioButtonInGroup();
		if(button != null)
		{
			return button.getFormularValue0();
		}
		
		return null;
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
