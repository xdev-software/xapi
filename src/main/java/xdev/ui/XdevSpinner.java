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


import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusListener;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.swing.AbstractButton;
import javax.swing.JComponent;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerDateModel;
import javax.swing.SpinnerListModel;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import xdev.db.Operator;
import xdev.util.ObjectUtils;
import xdev.util.logging.LoggerFactory;
import xdev.util.logging.XdevLogger;
import xdev.vt.VirtualTable;


/**
 * The standard spinner in XDEV. Based on {@link JSpinner}.
 * 
 * @see JSpinner
 * @see FormularComponent
 * @see XdevFocusCycleComponent
 * 
 * @author XDEV Software
 * 
 * @since 2.0
 */
@BeanSettings(useXdevCustomizer = true)
public class XdevSpinner extends JSpinner implements NumberFormularComponent<XdevSpinner>,
		XdevFocusCycleComponent
{
	
	/**
	 * Logger instance for this class.
	 */
	private static final XdevLogger						log					= LoggerFactory
																					.getLogger(XdevSpinner.class);
	
	/**
	 * 
	 */
	private static final long							serialVersionUID	= 4670379761510929176L;
	
	private Object										savedValue			= null;
	/**
	 * tabIndex is used to store the index for {@link XdevFocusCycleComponent}
	 * functionality.
	 */
	private int											tabIndex			= -1;
	
	private final FormularComponentSupport<XdevSpinner>	support				= new FormularComponentSupport(
																					this);
	
	
	/**
	 * Constructs a {@link XdevSpinner} with an
	 * <code>Integer {@link SpinnerNumberModel}</code> with initial value 0 and
	 * no minimum or maximum limits.
	 * 
	 * @see #setModel(SpinnerModel)
	 */
	public XdevSpinner()
	{
		super(new SpinnerNumberModel(0,Integer.MIN_VALUE,Integer.MAX_VALUE,1));
	}
	
	
	/**
	 * Constructs a complete {@link XdevSpinner} with pair of next/previous
	 * buttons and an editor for the {@link SpinnerModel}.
	 * 
	 * @param model
	 *            the new {@link SpinnerModel} for this {@link XdevSpinner}
	 */
	public XdevSpinner(SpinnerModel model)
	{
		super(model);
	}
	
	
	/**
	 * Returns the component that displays and potentially changes the model's
	 * value.
	 * 
	 * @return the {@link JTextField} that gives the user access to the
	 *         <code>SpinnerDateModel's</code> value. If the editor is not a
	 *         <code>DefaultEditor</code>, null is returned.
	 */
	public JTextField getTextField()
	{
		JComponent editor = getEditor();
		if(editor instanceof DefaultEditor)
		{
			return ((DefaultEditor)editor).getTextField();
		}
		
		return null;
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
		
		Object value = support.getSingleValue(vt,record);
		if(value != null)
		{
			SpinnerModel spinnerModel = getModel();
			if(spinnerModel instanceof SpinnerNumberModel)
			{
				if(value instanceof Number)
				{
					spinnerModel.setValue(value);
				}
				else
				{
					try
					{
						spinnerModel.setValue(Double.valueOf(value.toString()));
					}
					catch(NumberFormatException e)
					{
						log.error(e);
					}
				}
			}
			else if(spinnerModel instanceof SpinnerDateModel)
			{
				if(value instanceof Date)
				{
					spinnerModel.setValue(value);
				}
				else if(value instanceof Calendar)
				{
					Date d = new Date(((Calendar)value).getTimeInMillis());
					spinnerModel.setValue(d);
				}
			}
			else
			{
				spinnerModel.setValue(value);
			}
		}
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	public Object getFormularValue()
	{
		return getValue();
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	public void saveState()
	{
		savedValue = getValue();
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	public void restoreState()
	{
		setValue(savedValue);
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
	
	
	/**
	 * {@inheritDoc}
	 * 
	 * @since 3.1
	 */
	@NoBeanProperty
	@Override
	public Number getNumber() throws NumberFormatException
	{
		Object value = getValue();
		try
		{
			return (Number)value;
		}
		catch(ClassCastException e)
		{
			throw new NumberFormatException(String.valueOf(value));
		}
	}
	
	
	/**
	 * {@inheritDoc}
	 * 
	 * @since 3.1
	 */
	@Override
	public Number getNumber(Number defaultValue)
	{
		try
		{
			return getNumber();
		}
		catch(NumberFormatException e)
		{
			return defaultValue;
		}
	}
	
	
	/**
	 * {@inheritDoc}
	 * 
	 * @since 3.1
	 */
	@NoBeanProperty
	@Override
	public void setNumber(Number number)
	{
		setValue(number);
	}
	
	
	/**
	 * Constructs a {@link SpinnerNumberModel} with the specified
	 * <code>value</code>, <code>minimum</code>/<code>maximum</code> bounds, and
	 * <code>stepSize</code> and changes the model that represents the value of
	 * this {@link XdevSpinner}.
	 * 
	 * 
	 * @param value
	 *            the current value of the {@link SpinnerNumberModel}
	 * 
	 * @param minimum
	 *            the first number in the sequence
	 * 
	 * @param maximum
	 *            the last number in the sequence
	 * 
	 * @param stepSize
	 *            the difference between elements of the sequence
	 * 
	 * @throws IllegalArgumentException
	 *             if the following expression is false: minimum <= value <=
	 *             maximum
	 */
	public void setNumberModel(int value, int minimum, int maximum, int stepSize)
			throws IllegalArgumentException
	{
		setModel(new SpinnerNumberModel(value,minimum,maximum,stepSize));
	}
	
	
	/**
	 * Constructs a {@link SpinnerListModel} whose sequence of values is defined
	 * by the specified {@link List} and changes the model that represents the
	 * value of this {@link XdevSpinner}.
	 * 
	 * @param values
	 *            the values of the {@link SpinnerListModel}
	 * 
	 * @throws IllegalArgumentException
	 *             if model is <code>null</code>
	 * 
	 * @see #getModel
	 * @see #getEditor
	 * @see #setEditor
	 * @see SpinnerListModel
	 */
	public void setListModel(List<?> values) throws IllegalArgumentException
	{
		setModel(new SpinnerListModel(values));
	}
	
	private boolean	updatingUI	= false;
	
	
	@Override
	public void updateUI()
	{
		try
		{
			updatingUI = true;
			
			super.updateUI();
		}
		finally
		{
			updatingUI = false;
			
			ActionListener al = new ActionListener()
			{
				@Override
				public void actionPerformed(ActionEvent e)
				{
					requestFocus();
				}
			};
			for(Component c : getComponents())
			{
				if(c instanceof AbstractButton)
				{
					((AbstractButton)c).addActionListener(al);
				}
			}
		}
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setBackground(Color bg)
	{
		super.setBackground(bg);
		
		if(!updatingUI)
		{
			JTextField textField = getTextField();
			if(textField != null)
			{
				textField.setBackground(bg);
			}
		}
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Color getBackground()
	{
		JTextField textField = getTextField();
		if(textField != null)
		{
			textField.getBackground();
		}
		
		return super.getBackground();
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setForeground(Color fg)
	{
		super.setForeground(fg);
		
		if(!updatingUI)
		{
			JTextField textField = getTextField();
			if(textField != null)
			{
				textField.setForeground(fg);
			}
		}
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Color getForeground()
	{
		JTextField textField = getTextField();
		if(textField != null)
		{
			textField.getForeground();
		}
		
		return super.getForeground();
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setFont(Font f)
	{
		super.setFont(f);
		
		if(!updatingUI)
		{
			JTextField textField = getTextField();
			if(textField != null)
			{
				textField.setFont(f);
			}
		}
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Font getFont()
	{
		JTextField textField = getTextField();
		if(textField != null)
		{
			textField.getFont();
		}
		
		return super.getFont();
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setOpaque(boolean b)
	{
		super.setOpaque(b);
		
		if(!updatingUI)
		{
			JComponent editor = getEditor();
			if(editor != null)
			{
				editor.setOpaque(b);
				
				if(editor instanceof DefaultEditor)
				{
					((DefaultEditor)editor).getTextField().setOpaque(b);
				}
			}
		}
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isOpaque()
	{
		JComponent editor = getEditor();
		if(editor != null)
		{
			if(editor instanceof DefaultEditor)
			{
				return ((DefaultEditor)editor).getTextField().isOpaque();
			}
		}
		
		return super.isOpaque();
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setEnabled(boolean enabled)
	{
		super.setEnabled(enabled);
		
		if(!updatingUI)
		{
			JTextField textField = getTextField();
			if(textField != null)
			{
				textField.setEnabled(enabled);
			}
		}
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void requestFocus()
	{
		JTextField textField = getTextField();
		if(textField != null)
		{
			textField.requestFocus();
		}
		else
		{
			super.requestFocus();
		}
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public synchronized void addFocusListener(FocusListener l)
	{
		JTextField textField = getTextField();
		if(textField != null)
		{
			textField.addFocusListener(l);
		}
		else
		{
			super.addFocusListener(l);
		}
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public synchronized void removeFocusListener(FocusListener l)
	{
		JTextField textField = getTextField();
		if(textField != null)
		{
			textField.removeFocusListener(l);
		}
		else
		{
			super.removeFocusListener(l);
		}
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString()
	{
		return String.valueOf(getValue());
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
